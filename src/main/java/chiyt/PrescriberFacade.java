package chiyt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PrescriberFacade {
    private PatientDatabase patientDatabase;
    private Prescriber prescriber;
    private PatientReader patientReader;
    
    public PrescriberFacade() {
        this.patientDatabase = new PatientDatabase();
        this.prescriber = new Prescriber(patientDatabase);
        this.patientReader = new PatientReader();
    }
    
    /**
     * 執行完整的診斷流程
     * @param patientDataFile 病患資料JSON檔案名稱
     * @param supportedDiseasesFile 支援疾病檔案名稱
     * @param patientName 病患姓名
     * @param symptoms 症狀列表
     * @param outputFile 輸出檔案名稱
     * @param outputFormat 輸出格式 ("json" 或 "csv")
     * @return 是否成功完成診斷
     */
    public boolean performDiagnosis(String patientDataFile, String supportedDiseasesFile, 
                                   String patientName, List<String> symptoms, 
                                   String outputFile, String outputFormat) {
        try {
            // 1. 載入病患資料
            List<Patient> patients = patientReader.readPatientsFromJson(patientDataFile);
            patientDatabase.loadPatients(patients);
            
            // 2. 載入支援的疾病（可選，目前使用預設規則）
            loadSupportedDiseases(supportedDiseasesFile);
            
            // 3. 查找病患
            Patient patient = patientDatabase.getPatientByName(patientName);
            if (patient == null) {
                System.err.println("找不到病患: " + patientName);
                return false;
            }
            
            // 4. 設置回調來處理診斷結果
            CountDownLatch latch = new CountDownLatch(1);
            final boolean[] success = {false};
            
            prescriber.setDiagnosisCallback((p, s, prescription) -> {
                try {
                    // 輸出診斷結果
                    if ("json".equalsIgnoreCase(outputFormat)) {
                        ResultExporter.exportToJson(p, s, prescription, outputFile);
                    } else if ("csv".equalsIgnoreCase(outputFormat)) {
                        ResultExporter.exportToCsv(p, s, prescription, outputFile);
                    } else {
                        System.err.println("不支援的輸出格式: " + outputFormat);
                        return;
                    }
                    success[0] = true;
                } finally {
                    latch.countDown();
                }
            });
            
            // 5. 開始診斷服務
            prescriber.startDiagnosisService();
            
            // 6. 提交診斷請求
            prescriber.requestDiagnosis(patient.getId(), symptoms);
            
            // 7. 等待診斷完成（最多等待10秒）
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            
            // 8. 停止診斷服務
            prescriber.stopDiagnosisService();
            
            if (!completed) {
                System.err.println("診斷超時");
                return false;
            }
            
            return success[0];
            
        } catch (IOException e) {
            System.err.println("執行診斷時發生錯誤: " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("診斷被中斷");
            return false;
        }
    }
    
    /**
     * 簡化版本的診斷方法 - 使用預設檔案名稱
     * @param patientName 病患姓名
     * @param symptoms 症狀列表
     * @param outputFile 輸出檔案名稱
     * @param outputFormat 輸出格式
     * @return 是否成功
     */
    public boolean performDiagnosis(String patientName, List<String> symptoms, 
                                   String outputFile, String outputFormat) {
        return performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            patientName, symptoms, outputFile, outputFormat
        );
    }
    
    /**
     * 最簡化版本的診斷方法 - 使用預設設定
     * @param patientName 病患姓名
     * @param symptoms 症狀列表
     * @return 是否成功
     */
    public boolean performDiagnosis(String patientName, List<String> symptoms) {
        String outputFile = "diagnosis_result.json";
        return performDiagnosis(patientName, symptoms, outputFile, "json");
    }
    
    /**
     * 載入支援的疾病列表（目前為預留功能）
     * @param filename 疾病檔案名稱
     */
    private void loadSupportedDiseases(String filename) {
        try {
            List<String> diseases = Files.readAllLines(Paths.get(filename));
            System.out.println("載入支援的疾病: " + diseases);
            // 目前使用預設規則，未來可以根據檔案內容動態載入規則
        } catch (IOException e) {
            System.err.println("載入支援疾病檔案時發生錯誤: " + e.getMessage());
        }
    }
    
    /**
     * 獲取病患資料庫
     * @return 病患資料庫
     */
    public PatientDatabase getPatientDatabase() {
        return patientDatabase;
    }
    
    /**
     * 獲取診斷器
     * @return 診斷器
     */
    public Prescriber getPrescriber() {
        return prescriber;
    }
}

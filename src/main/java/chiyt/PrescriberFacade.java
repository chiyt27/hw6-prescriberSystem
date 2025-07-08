package chiyt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import chiyt.diagnosis.AttractiveRule;
import chiyt.diagnosis.COVID19Rule;
import chiyt.diagnosis.DiagnosisRule;
import chiyt.diagnosis.SleepApneaRule;

public class PrescriberFacade {
    private PatientDatabase database = new PatientDatabase();

    public PrescriberFacade() {}
    
    /**
     * 執行完整的診斷流程
     * @param patientDataFile 病患資料JSON檔案名稱
     * @param supportedDiseasesFile 支援疾病檔案名稱
     * @param patientName 病患身份證字號
     * @param symptoms 症狀列表
     * @param outputFile 輸出檔案名稱
     * @param outputFormat 輸出格式 ("json" 或 "csv")
     * @return 是否成功完成診斷
     */
    public void performDiagnosis(String patientDataFile, String supportedDiseasesFile, 
                                   String patientId, List<String> symptoms, 
                                   String outputFile, String outputFormat) {
        try {
            // 1. 載入病患資料
            database.loadPatientsFromJson(patientDataFile);

            // 2. 載入支援的疾病
            List<DiagnosisRule> supportedDiseases = loadSupportedDiseases(supportedDiseasesFile);
            Prescriber prescriber = new Prescriber(database, supportedDiseases);
            
            // 3. 查找病患
            Patient patient = database.getPatient(patientId);
            if (patient == null) {
                System.err.println("找不到病患: " + patientId);
                return;
            }
            
            // 4. 設置回調來處理診斷結果
            prescriber.startDiagnosisService();
            // CountDownLatch latch = new CountDownLatch(1);
            // final boolean[] success = {false};
            
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
            prescriber.requestDiagnosis(patient.getId(), symptoms, (id, symptoms, prescription) -> {
                System.out.println("\n[回調] 用戶收到診斷結果:");
                System.out.println("患者ID: " + id);
                System.out.println("症狀: " + symptoms);
                System.out.println("處方: " + prescription);
                
                // 用戶決定儲存為病例
                saveDiagnosisAsCase(id, symptoms, prescription);
            });
            
            // 7. 等待診斷完成（最多等待10秒）
            boolean completed = latch.await(10, TimeUnit.SECONDS);

            // 8. 停止診斷服務
            prescriber.stopDiagnosisService();

            if (!completed) {
                System.err.println("診斷超時");
            }
        } catch (Exception e) {
            System.err.println("執行診斷時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 簡化版本的診斷方法 - 使用預設檔案名稱
     * @param patientId 病患身份證字號
     * @param symptoms 症狀列表
     * @param outputFile 輸出檔案名稱
     * @param outputFormat 輸出格式
     */
    public void performDiagnosis(String patientId, List<String> symptoms, 
                                   String outputFile, String outputFormat) {
        performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            patientId, symptoms, outputFile, outputFormat
        );
    }

    private List<DiagnosisRule> loadSupportedDiseases(String filename) {
        List<DiagnosisRule> allDiagnosisRules = new ArrayList<>();
        allDiagnosisRules.add(new COVID19Rule());
        allDiagnosisRules.add(new AttractiveRule());
        allDiagnosisRules.add(new SleepApneaRule());
        List<DiagnosisRule> activeDiagnosisRules = new ArrayList<>();
        try {

            List<String> diseasesStr = Files.readAllLines(Paths.get(filename));
            for (DiagnosisRule rule : allDiagnosisRules) {
                if (diseasesStr.contains(rule.getSupportedDisease())) {
                    activeDiagnosisRules.add(rule);
                } else {
                }
            }
        } catch (IOException e) {
            System.err.println("載入支援疾病檔案時發生錯誤: " + e.getMessage());
        }
        return activeDiagnosisRules;
    }

    public boolean saveDiagnosisAsCase(String patientId, List<String> symptoms, Prescription prescription) {
        try {
            Patient patient = database.getPatient(patientId);
            if (patient == null) {
                System.err.println("找不到病患: " + patientId);
                return false;
            }
            
            Case newCase = new Case(symptoms, prescription);
            database.addCaseToPatient(patientId, newCase);
            
            System.out.println("✓ 病例已儲存 - 患者: " + patient.getName() + 
                             ", 診斷時間: " + newCase.getCaseTime() +
                             ", 處方: " + prescription.getName());
            return true;
            
        } catch (Exception e) {
            System.err.println("儲存病例時發生錯誤: " + e.getMessage());
            return false;
        }
    }
}

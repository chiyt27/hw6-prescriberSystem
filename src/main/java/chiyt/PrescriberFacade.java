package chiyt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import chiyt.diagnosis.AttractiveRule;
import chiyt.diagnosis.COVID19Rule;
import chiyt.diagnosis.DiagnosisRule;
import chiyt.diagnosis.SleepApneaRule;

public class PrescriberFacade {
    private PatientDatabase database = new PatientDatabase();

    public PrescriberFacade() {}
    
    private static Prescriber prescriber;
    private static boolean isServiceStarted = false;

    /**
     * 執行完整的診斷流程 (非同步)
     * @param patientDataFile 病患資料JSON檔案名稱
     * @param supportedDiseasesFile 支援疾病檔案名稱
     * @param patientId 病患身份證字號
     * @param symptoms 症狀列表
     * @param outputFile 輸出檔案名稱 (可選)
     * @param outputFormat 輸出格式 ("json" 或 "csv")
     */
    public void performDiagnosis(String patientDataFile, String supportedDiseasesFile, 
                                   String patientId, List<String> symptoms, 
                                   String outputFile, String outputFormat) {
        try {
            // 1. 初始化系統 (只執行一次)
            if (!isServiceStarted) {
                initializeSystem(patientDataFile, supportedDiseasesFile);
            }
            
            // 2. 查找病患
            Patient patient = database.getPatient(patientId);
            if (patient == null) {
                System.err.println("找不到病患: " + patientId);
                return;
            }
            
            // 3. 提交非同步診斷請求
            System.out.println("提交診斷請求 - 患者: " + patient.getName() + ", 症狀: " + symptoms);
            prescriber.requestDiagnosis(patient.getId(), symptoms, (id, symptomList, prescription) -> {
                // 診斷完成的回調處理
                handleDiagnosisResult(id, symptomList, prescription, outputFile, outputFormat);
            });
            
            System.out.println("診斷請求已提交，請等待診斷完成通知...");
            
        } catch (Exception e) {
            System.err.println("提交診斷請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 初始化診斷系統
     */
    private void initializeSystem(String patientDataFile, String supportedDiseasesFile) {
        try {
            // 載入病患資料
            database.loadPatientsFromJson(patientDataFile);

            // 載入支援的疾病
            List<DiagnosisRule> supportedDiseases = loadSupportedDiseases(supportedDiseasesFile);
            prescriber = new Prescriber(database, supportedDiseases);
            
            // 啟動診斷服務
            prescriber.startDiagnosisService();
            isServiceStarted = true;
            
            System.out.println("診斷系統已初始化完成");
        } catch (Exception e) {
            System.err.println("初始化診斷系統時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 處理診斷結果
     */
    private void handleDiagnosisResult(String patientId, List<String> symptoms, 
                                     Prescription prescription, String outputFile, String outputFormat) {
        System.out.println("\n=== 診斷完成通知 ===");
        System.out.println("患者ID: " + patientId);
        System.out.println("症狀: " + symptoms);
        if (prescription != null) {
            System.out.println("診斷結果: " + prescription.getName());
            System.out.println("潛在疾病: " + prescription.getPotentialDisease());
        } else {
            System.out.println("診斷結果: 無法診斷出對應疾病");
        }
        
        // 儲存為病例
        if (prescription != null) {
            saveDiagnosisAsCase(patientId, symptoms, prescription);
        }
        
        // 輸出到檔案 (如果指定)
        if (outputFile != null && outputFormat != null && prescription != null) {
            exportDiagnosisResult(patientId, symptoms, prescription, outputFile, outputFormat);
        }
        
        System.out.println("==================\n");
    }

    /**
     * 輸出診斷結果到檔案
     */
    private void exportDiagnosisResult(String patientId, List<String> symptoms, 
                                     Prescription prescription, String outputFile, String outputFormat) {
        try {
            Patient patient = database.getPatient(patientId);
            if ("json".equalsIgnoreCase(outputFormat)) {
                ResultExporter.exportToJson(patient, symptoms, prescription, outputFile);
                System.out.println("✓ 診斷結果已輸出到: " + outputFile + " (JSON格式)");
            } else if ("csv".equalsIgnoreCase(outputFormat)) {
                ResultExporter.exportToCsv(patient, symptoms, prescription, outputFile);
                System.out.println("✓ 診斷結果已輸出到: " + outputFile + " (CSV格式)");
            } else {
                System.err.println("不支援的輸出格式: " + outputFormat);
            }
        } catch (Exception e) {
            System.err.println("輸出診斷結果時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 停止診斷服務
     */
    public static void stopService() {
        if (prescriber != null) {
            prescriber.stopDiagnosisService();
            isServiceStarted = false;
            System.out.println("診斷服務已停止");
        }
    }

    /**
     * 簡化版本的診斷方法 - 使用預設檔案名稱
     * @param patientId 病患身份證字號
     * @param symptoms 症狀列表
     * @param outputFile 輸出檔案名稱 (可選)
     * @param outputFormat 輸出格式 (可選)
     */
    public void performDiagnosis(String patientId, List<String> symptoms, 
                                   String outputFile, String outputFormat) {
        performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            patientId, symptoms, outputFile, outputFormat
        );
    }

    /**
     * 最簡化版本 - 僅診斷，不輸出檔案
     * @param patientId 病患身份證字號
     * @param symptoms 症狀列表
     */
    public void performDiagnosis(String patientId, List<String> symptoms) {
        performDiagnosis(patientId, symptoms, null, null);
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
            
            System.out.println("病例已儲存 - 患者: " + patient.getName() + 
                             ", 診斷時間: " + newCase.getCaseTime() +
                             ", 處方: " + prescription.getName());
            return true;
            
        } catch (Exception e) {
            System.err.println("儲存病例時發生錯誤: " + e.getMessage());
            return false;
        }
    }
}

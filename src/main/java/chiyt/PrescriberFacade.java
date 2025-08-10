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
    private PatientDatabase database;
    private Prescriber prescriber;
    private List<DiagnosisRule> allDiagnosisRules;

    public PrescriberFacade() {
        this.database = new PatientDatabase();
        this.prescriber = new Prescriber(database, new ArrayList<>());// 先用空的診斷規則建立，後續再更新

        // OCP
        // TODO: 可以用反射動態載入所有診斷規則
        allDiagnosisRules = new ArrayList<>();
        allDiagnosisRules.add(new COVID19Rule());
        allDiagnosisRules.add(new AttractiveRule());
        allDiagnosisRules.add(new SleepApneaRule());
    }
    
    public void performDiagnosis(String patientDataFile, String supportedDiseasesFile, 
                                String patientId, List<String> symptoms, List<DiagnosisObserver> observers) {
        try {
            //1. 取回病患資料
            database.loadPatientsFromJson(patientDataFile);
            
            //2. 設定支援診斷的疾病
            List<DiagnosisRule> supportedDiseases = loadSupportedDiseases(supportedDiseasesFile);
            prescriber.setDiagnosisRules(supportedDiseases);
            
            //3. 送出診斷請求
            //3.1 查找病患
            Patient patient = database.getPatient(patientId);
            if (patient == null) {
                System.err.println("找不到病患: " + patientId);
                return;
            }
            
            //3.2 準備觀察者列表，如果有提供病患檔案路徑時才加入資料庫儲存觀察者
            List<DiagnosisObserver> allObservers = new ArrayList<>();
            if (observers != null) {
                allObservers.addAll(observers);
            }
            if (patientDataFile != null && !patientDataFile.trim().isEmpty()) {
                allObservers.add(new CaseDatabaseObserver(patientDataFile));
            }
            //3.3 提交診斷請求
            prescriber.requestDiagnosis(patient.getId(), symptoms, allObservers);

        } catch (Exception e) {
            System.err.println("提交診斷請求時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 載入支援的疾病診斷規則
     * @param filename 支援疾病檔案名稱
     * @return 支援的診斷規則列表
     */
    private List<DiagnosisRule> loadSupportedDiseases(String filename) {
        List<DiagnosisRule> activeDiagnosisRules = new ArrayList<>();
        try {
            List<String> diseasesStr = Files.readAllLines(Paths.get(filename));
            for (DiagnosisRule rule : allDiagnosisRules) {
                if (diseasesStr.contains(rule.getSupportedDisease())) {
                    activeDiagnosisRules.add(rule);
                }
            }
            // System.out.println("成功載入 " + activeDiagnosisRules.size() + " 個支援的診斷規則。");
        } catch (IOException e) {
            System.err.println("載入支援疾病檔案時發生錯誤: " + e.getMessage());
            System.err.println("將載入所有預設規則。");
            return allDiagnosisRules;
        }
        return activeDiagnosisRules;
    }

    // public void stopService() {
    //     if (prescriber != null) {
    //         prescriber.stopDiagnosisService();
    //         System.out.println("診斷服務已停止");
    //     }
    // }
}

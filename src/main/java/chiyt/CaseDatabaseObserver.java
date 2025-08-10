package chiyt;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 病例資料庫儲存觀察者
 * 負責在診斷完成後將病例儲存到資料庫並寫回檔案
 * 使用檔案級鎖定避免並發寫入衝突
 */
public class CaseDatabaseObserver implements DiagnosisObserver {
    private static final Map<String, Object> fileLocks = new ConcurrentHashMap<>();
    private String patientDataFile;
    
    public CaseDatabaseObserver(String patientDataFile) {
        this.patientDataFile = patientDataFile;
    }
    
    @Override
    public void onDiagnosisCompleted(Patient patient, List<String> symptoms, Prescription prescription) {
        if (prescription != null && patient != null && patientDataFile != null) {
            // 使用檔案路徑作為鎖定鍵，確保同一檔案的操作是同步的
            Object fileLock = fileLocks.computeIfAbsent(patientDataFile, k -> new Object());
            
            synchronized (fileLock) {
                try {
                    // 建立專用的 database 實例，避免與其他檔案的資料混亂
                    PatientDatabase fileDatabase = new PatientDatabase();
                    fileDatabase.loadPatientsFromJson(patientDataFile);
                    
                    // 加入新病例
                    Case newCase = new Case(symptoms, prescription);
                    fileDatabase.addCaseToPatient(patient.getId(), newCase);
                    
                    // 寫回檔案
                    fileDatabase.saveToFile(patientDataFile);
                    
                    System.out.println("病例已寫回檔案 - 患者: " + patient.getId() + 
                                     ", 診斷時間: " + newCase.getCaseTime() +
                                     ", 處方: " + prescription.getName() +
                                     ", 檔案: " + patientDataFile);
                    
                } catch (Exception e) {
                    System.err.println("儲存病例時發生錯誤: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
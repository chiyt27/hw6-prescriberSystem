package chiyt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientDatabase {
    private Map<String, Patient> patients = new HashMap<>();
    
    public PatientDatabase() {}
    
    /**
     * 從JSON檔案載入病患資料
     * @param patients 病患列表
     */
    public void loadPatients(List<Patient> patients) {
        for (Patient patient : patients) {
            this.patients.put(patient.getId(), patient);
        }
    }
    
    /**
     * 根據身分證字號查詢病患
     * @param id 身分證字號
     * @return 病患資料，如果不存在返回null
     */
    public Patient getPatient(String id) {
        return patients.get(id);
    }
    
    /**
     * 根據姓名查詢病患
     * @param name 病患姓名
     * @return 病患資料，如果不存在返回null
     */
    public Patient getPatientByName(String name) {
        for (Patient patient : patients.values()) {
            if (patient.getName().equals(name)) {
                return patient;
            }
        }
        return null;
    }
    
    /**
     * 添加病患的新病例
     * @param patientId 病患身分證字號
     * @param newCase 新病例
     * @return 是否成功添加
     */
    public boolean addCase(String patientId, Case newCase) {
        Patient patient = patients.get(patientId);
        if (patient != null) {
            patient.addCase(newCase);
            return true;
        }
        return false;
    }
    
    /**
     * 獲取所有病患
     * @return 病患列表
     */
    public List<Patient> getAllPatients() {
        return List.copyOf(patients.values());
    }
    
    /**
     * 檢查病患是否存在
     * @param id 身分證字號
     * @return 是否存在
     */
    public boolean containsPatient(String id) {
        return patients.containsKey(id);
    }
    
    /**
     * 獲取病患數量
     * @return 病患數量
     */
    public int getPatientCount() {
        return patients.size();
    }
} 
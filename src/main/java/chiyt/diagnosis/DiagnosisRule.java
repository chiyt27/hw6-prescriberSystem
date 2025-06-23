package chiyt.diagnosis;

import java.util.List;

import chiyt.Patient;
import chiyt.Prescription;

public interface DiagnosisRule {
    /**
     * 檢查病患是否符合此診斷規則
     * @param patient 病患資料
     * @param symptoms 症狀列表
     * @return 如果符合規則返回true，否則返回false
     */
    boolean matches(Patient patient, List<String> symptoms);
    
    /**
     * 根據規則生成處方
     * @return 對應的處方
     */
    Prescription generatePrescription();

    String getSupportedDisease();
} 
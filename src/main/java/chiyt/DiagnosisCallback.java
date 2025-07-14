package chiyt;

import java.util.List;

public interface DiagnosisCallback {
    /**
     * 當診斷完成時被調用
     * @param patientId 病患ID
     * @param symptoms 症狀列表
     * @param prescription 診斷結果處方
     */
    void onDiagnosed(String patientId, List<String> symptoms, Prescription prescription);
} 
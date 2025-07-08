package chiyt;

import java.util.List;

public interface DiagnosisCallback {
    /**
     * 當診斷完成時被調用
     * @param patient 病患資料
     * @param symptoms 症狀列表
     * @param prescription 診斷結果處方
     */
    void onDiagnosisComplete(Patient patient, List<String> symptoms, Prescription prescription);
} 
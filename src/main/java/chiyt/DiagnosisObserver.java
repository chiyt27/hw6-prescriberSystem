package chiyt;

import java.util.List;

/**
 * 觀察者介面，用於監聽診斷完成事件。
 * 任何關心診斷結果的類別都應實作此介面。
 */
public interface DiagnosisObserver {
    /**
     * 當診斷完成時由 Prescriber 調用。
     *
     * @param patient      被診斷的病患
     * @param symptoms     診斷時使用的症狀列表
     * @param prescription 生成的處方
     */
    void onDiagnosisCompleted(Patient patient, List<String> symptoms, Prescription prescription);
} 
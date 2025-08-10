package chiyt;

import java.util.List;

/**
 * 一個觀察者實現，負責將診斷結果保存到檔案（JSON或CSV）。
 */
public class SaveToFileObserver implements DiagnosisObserver {

    private final String filename;
    private final String format;

    /**
     * @param filename 要保存的檔案名稱
     * @param format   檔案格式，可以是 "json" 或 "csv"
     */
    public SaveToFileObserver(String filename, String format) {
        this.filename = filename;
        this.format = format;
    }

    @Override
    public void onDiagnosisCompleted(Patient patient, List<String> symptoms, Prescription prescription) {
        // 確保有有效的診斷結果才寫入檔案
        if (patient == null || prescription == null) {
            System.out.println("SaveToFileObserver: 無有效診斷結果，不執行寫入檔案。");
            return;
        }
        
        // System.out.println("SaveToFileObserver: 收到診斷完成通知，準備寫入檔案 " + filename);
        if ("json".equalsIgnoreCase(format)) {
            ResultExporter.exportToJson(patient, symptoms, prescription, filename);
            System.out.println(String.format("寫出診斷 json 檔案: %s, 診斷結果: %s", patient.getId(), prescription.getName()));
        } else if ("csv".equalsIgnoreCase(format)) {
            ResultExporter.exportToCsv(patient, symptoms, prescription, filename);
            System.out.println(String.format("寫出診斷 csv 檔案: %s, 診斷結果: %s", patient.getId(), prescription.getName()));
        } else {
            System.err.println("不支援的檔案格式：" + format + "。無法寫入檔案。");
        }
    }
} 
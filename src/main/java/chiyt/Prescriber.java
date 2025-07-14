package chiyt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chiyt.diagnosis.DiagnosisRule;

public class Prescriber {
    private List<DiagnosisRule> diagnosisRules = new ArrayList<>();
    private PatientDatabase database;
    private final BlockingQueue<DiagnosisRequest> queue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = false;
    private Thread diagnosisThread;
    
    public Prescriber(PatientDatabase database, List<DiagnosisRule> supportedDiseases) {
        this.database = database;
        this.diagnosisRules = new ArrayList<>(supportedDiseases);
    }
    
    /**
     * 開始診斷服務
     */
    public void startDiagnosisService() {
        if (isRunning) {
            System.out.println("診斷服務已經在運行中");
            return;
        }

        isRunning = true;
        diagnosisThread = new Thread(() -> {
            System.out.println("診斷服務已啟動，等待診斷請求...");
            while (isRunning) {
                try {
                    DiagnosisRequest req = queue.take(); // 阻塞直到有請求
                    System.out.println("開始處理診斷請求 - 患者ID: " + req.getPatientId() + ", 症狀: " + req.getSymptoms() + ", 佇列剩餘: " + queue.size());
                    // 模擬診斷耗時3秒
                    Thread.sleep(3000);
                    // 執行診斷邏輯
                    Prescription prescription = diagnose(req.getPatientId(), req.getSymptoms());
                    if (prescription != null)
                        System.out.println("診斷完成 - 患者: " + req.getPatientId() + ", 處方: " + prescription.getName());
                    else 
                        System.out.println("診斷失敗 - 無法診斷出對應疾病，患者: " + req.getPatientId());

                    // 回調通知用戶
                    req.getCallback().onDiagnosed(req.getPatientId(), req.getSymptoms(), prescription);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        diagnosisThread.setDaemon(true);
        diagnosisThread.start();
    }

    public void stopDiagnosisService() {
        isRunning = false;
        if (diagnosisThread != null) {
            diagnosisThread.interrupt();
        }
    }

    public void requestDiagnosis(String patientId, List<String> symptoms, DiagnosisCallback callback) {
        queue.offer(new DiagnosisRequest(patientId, symptoms, callback));
    }
    
    /**
     * 執行診斷
     * @param request 診斷請求
     */
    private Prescription diagnose(String patientId, List<String> symptoms) {
        Patient patient = database.getPatient(patientId);
        
        if (patient == null) {
            System.err.println("找不到病患: " + patientId);
            return null;
        }

        Prescription prescription = null;
            for (DiagnosisRule rule : diagnosisRules) {
                if (rule.matches(patient, symptoms)) {
                    prescription = rule.generatePrescription();
                    break;
                }
        }
        return prescription; // 無法診斷
    }
} 
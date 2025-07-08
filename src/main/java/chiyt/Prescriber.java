package chiyt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import chiyt.diagnosis.DiagnosisRule;

public class Prescriber {
    private List<DiagnosisRule> diagnosisRules = new ArrayList<>();
    // private BlockingQueue<DiagnosisRequest> requestQueue = new LinkedBlockingQueue<>();
    // private Thread diagnosisThread;
    // private AtomicBoolean isRunning = new AtomicBoolean(false);
    // private DiagnosisCallback callback;
    private PatientDatabase database;
    private final BlockingQueue<DiagnosisRequest> queue = new LinkedBlockingQueue<>();
    private volatile boolean running = false;
    private Thread diagnosisThread;
    
    public Prescriber(PatientDatabase database, List<DiagnosisRule> supportedDiseases) {
        this.database = database;
        this.diagnosisRules = new ArrayList<>(supportedDiseases);
    }
    
    /**
     * 設置診斷完成回調
     * @param callback 回調介面
     */
    // public void setDiagnosisCallback(DiagnosisCallback callback) {
    //     this.callback = callback;
    // }
    
    /**
     * 開始診斷服務
     */
    public void startDiagnosisService() {
        // if (isRunning.compareAndSet(false, true)) {
        //     diagnosisThread = new Thread(this::diagnosisWorker);
        //     diagnosisThread.setDaemon(true);
        //     diagnosisThread.start();
        // }
        if (running) {
            System.out.println("診斷服務已經在運行中");
            return;
        }

        running = true;
        diagnosisThread = new Thread(() -> {
            System.out.println("診斷服務已啟動，等待診斷請求...");
            while (running) {
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

                    // 回呼通知用戶
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
        running = false;
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
        
        // 使用策略模式進行診斷
        // for (DiagnosisRule rule : diagnosisRules) {
        //     if (rule.matches(patient, symptoms)) {
        //         return rule.createPrescription();
        //     }
        // }
        Prescription prescription = null;
            for (DiagnosisRule rule : diagnosisRules) {
                if (rule.matches(patient, symptoms)) {
                    prescription = rule.generatePrescription();
                    break;
                }
        }
        return prescription; // 無法診斷
    }

    // private void performDiagnosis(DiagnosisRequest request) {
    //     try {
    //         // 診斷耗時3秒
    //         Thread.sleep(3000);
            
    //         Patient patient = patientDatabase.getPatient(request.getPatientId());
    //         if (patient == null) {
    //             System.err.println("病患不存在: " + request.getPatientId());
    //             return;
    //         }

    //         // 尋找匹配的診斷規則
    //         Prescription prescription = null;
    //         for (DiagnosisRule rule : diagnosisRules) {
    //             if (rule.matches(patient, request.getSymptoms())) {
    //                 prescription = rule.generatePrescription();
    //                 break;
    //             }
    //         }

    //         // 如果沒有匹配的規則，生成預設處方
    //         if (prescription == null) {
    //             prescription = new Prescription(
    //                 "一般處方",
    //                 "未確診",
    //                 List.of("維他命C"),
    //                 "請多休息，多喝水。"
    //             );
    //         } else {
    //             // 有符合的才創建新病例並添加到資料庫
    //             Case newCase = new Case(request.getSymptoms(), prescription);
    //             patientDatabase.addCase(request.getPatientId(), newCase);
    //         }

    //         // 調用回調
    //         if (callback != null) {
    //             callback.onDiagnosisComplete(patient, request.getSymptoms(), prescription);
    //         }
            
    //     } catch (InterruptedException e) {
    //         Thread.currentThread().interrupt();
    //     } catch (Exception e) {
    //         System.err.println("診斷過程中發生錯誤: " + e.getMessage());
    //     }
    // }
} 
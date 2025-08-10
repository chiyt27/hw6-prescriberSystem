package chiyt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chiyt.diagnosis.DiagnosisRule;

public class Prescriber {
    private PatientDatabase database;
    private List<DiagnosisRule> diagnosisRules;
    private final BlockingQueue<DiagnosisDemand> queue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = false;
    private Thread diagnosisThread;

    public Prescriber(PatientDatabase database, List<DiagnosisRule> supportedDiseases) {
        this.database = database;
        this.diagnosisRules = new ArrayList<>(supportedDiseases);
        startDiagnosisService();
    }

    /**
     * 更新診斷規則
     * @param newRules 新的診斷規則列表
     */
    public void setDiagnosisRules(List<DiagnosisRule> newRules) {
        if (newRules != null) {
            this.diagnosisRules = new ArrayList<>(newRules);
            System.out.println("診斷規則已更新，共載入 " + newRules.size() + " 個規則");
        }
    }

    /**
     * 啟動診斷服務的線程
     */
    public void startDiagnosisService() {
        if (isRunning) {
            System.out.println("診斷服務已經在運行中");
            return;
        }

        isRunning = true;
        diagnosisThread = new Thread(() -> {
            System.out.println("啟動診斷服務，等待診斷請求...");
            while (isRunning) {
                try {
                    DiagnosisDemand req = queue.take(); // 阻塞直到有請求
                    System.out.println("開始處理診斷請求 - 患者ID: " + req.getPatientId() + ", 症狀: " + req.getSymptoms() + ", 佇列剩餘: " + queue.size());
                    
                    //1. 執行診斷
                    Patient patient = database.getPatient(req.getPatientId());
                    Prescription prescription = diagnose(patient, req.getSymptoms());
                    if (prescription != null) 
                        System.out.println("診斷完成 - 患者: " + req.getPatientId() + ", 處方: " + prescription.getName());
                    else 
                        System.out.println("診斷失敗 - 無法診斷出對應疾病，患者: " + req.getPatientId());
                    //2. 通知該請求的觀察者
                    notifyObservers(req, patient, prescription);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        diagnosisThread.start();
    }
    
    // public void stopDiagnosisService() {
    //     isRunning = false;
    //     if (diagnosisThread != null) {
    //         diagnosisThread.interrupt();
    //     }
    // }

    public void requestDiagnosis(String patientId, List<String> symptoms, List<DiagnosisObserver> observers) {
        System.out.println("提交診斷請求 - 患者: " + patientId + ", 症狀: " + symptoms);
        queue.offer(new DiagnosisDemand(patientId, symptoms, observers));
    }

    /**
     * 執行診斷
     */
    private Prescription diagnose(Patient patient, List<String> symptoms) {
        if (patient == null) {
            System.err.println("找不到病患資料，無法進行診斷。");
            return null;
        }
        
        try {
            Thread.sleep(3000); // 診斷耗時3秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        
        Prescription prescription = null;
        for (DiagnosisRule rule : diagnosisRules) {
            if (rule.matches(patient, symptoms)) {
                prescription = rule.generatePrescription();
                break;
            }
        }
        // 如果沒有匹配的規則，生成預設處方
        if (prescription == null) {
            prescription = new Prescription(
                "一般處方",
                "未確診",
                List.of("維他命C"),
                "請多休息，多喝水。"
            );
        }
        return prescription;
    }

    /**
     * 通知所有觀察者診斷完成
     * @param request 診斷請求
     * @param patient 病患資料
     * @param prescription 診斷結果處方
     */
    private void notifyObservers(DiagnosisDemand request, Patient patient, Prescription prescription) {
        for (DiagnosisObserver observer : request.getObservers()) {
            try {
                observer.onDiagnosisCompleted(patient, request.getSymptoms(), prescription);
            } catch (Exception e) {
                System.err.println("通知觀察者 " + observer.getClass().getSimpleName() + " 時發生錯誤: " + e.getMessage());
            }
        }
    }
} 
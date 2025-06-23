package chiyt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import chiyt.diagnosis.AttractiveRule;
import chiyt.diagnosis.COVID19Rule;
import chiyt.diagnosis.DiagnosisRule;
import chiyt.diagnosis.SleepApneaRule;

public class Prescriber {
    private List<DiagnosisRule> diagnosisRules = new ArrayList<>();
    private BlockingQueue<DiagnosisRequest> requestQueue = new LinkedBlockingQueue<>();
    private Thread diagnosisThread;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private DiagnosisCallback callback;
    private PatientDatabase patientDatabase;
    
    public Prescriber(PatientDatabase patientDatabase) {
        this.patientDatabase = patientDatabase;
        // 添加預設的診斷規則
        addDiagnosisRule(new COVID19Rule());
        addDiagnosisRule(new AttractiveRule());
        addDiagnosisRule(new SleepApneaRule());
    }
    
    /**
     * 添加診斷規則
     * @param rule 診斷規則
     */
    public void addDiagnosisRule(DiagnosisRule rule) {
        diagnosisRules.add(rule);
    }
    
    /**
     * 設置診斷完成回調
     * @param callback 回調介面
     */
    public void setDiagnosisCallback(DiagnosisCallback callback) {
        this.callback = callback;
    }
    
    /**
     * 開始診斷服務
     */
    public void startDiagnosisService() {
        if (isRunning.compareAndSet(false, true)) {
            diagnosisThread = new Thread(this::diagnosisWorker);
            diagnosisThread.setDaemon(true);
            diagnosisThread.start();
        }
    }
    
    /**
     * 停止診斷服務
     */
    public void stopDiagnosisService() {
        isRunning.set(false);
        if (diagnosisThread != null) {
            diagnosisThread.interrupt();
        }
    }
    
    /**
     * 提交診斷請求
     * @param patientId 病患身分證字號
     * @param symptoms 症狀列表
     */
    public void requestDiagnosis(String patientId, List<String> symptoms) {
        DiagnosisRequest request = new DiagnosisRequest(patientId, symptoms);
        requestQueue.offer(request);
    }
    
    /**
     * 診斷工作執行緒
     */
    private void diagnosisWorker() {
        while (isRunning.get()) {
            try {
                DiagnosisRequest request = requestQueue.take();
                performDiagnosis(request);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    /**
     * 執行診斷
     * @param request 診斷請求
     */
    private void performDiagnosis(DiagnosisRequest request) {
        try {
            // 診斷耗時3秒
            Thread.sleep(3000);
            
            Patient patient = patientDatabase.getPatient(request.getPatientId());
            if (patient == null) {
                System.err.println("病患不存在: " + request.getPatientId());
                return;
            }
            
            // 尋找匹配的診斷規則
            Prescription prescription = null;
            for (DiagnosisRule rule : diagnosisRules) {
                if (rule.matches(patient, request.getSymptoms())) {
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
            
            // 創建新病例並添加到資料庫
            Case newCase = new Case(request.getSymptoms(), prescription);
            patientDatabase.addCase(request.getPatientId(), newCase);
            
            // 調用回調
            if (callback != null) {
                callback.onDiagnosisComplete(patient, request.getSymptoms(), prescription);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("診斷過程中發生錯誤: " + e.getMessage());
        }
    }
    
    /**
     * 診斷請求內部類別
     */
    private static class DiagnosisRequest {
        private String patientId;
        private List<String> symptoms;
        
        public DiagnosisRequest(String patientId, List<String> symptoms) {
            this.patientId = patientId;
            this.symptoms = symptoms;
        }
        
        public String getPatientId() { return patientId; }
        public List<String> getSymptoms() { return symptoms; }
    }
} 
package chiyt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResultExporter {
    
    /**
     * 將診斷結果輸出為JSON格式
     * @param patient 病患資料
     * @param symptoms 症狀列表
     * @param prescription 處方
     * @param filename 輸出檔案名稱
     */
    public static void exportToJson(Patient patient, List<String> symptoms, Prescription prescription, String filename) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            
            DiagnosisResult result = new DiagnosisResult(patient, symptoms, prescription);
            mapper.writeValue(new java.io.File(filename), result);
            
            System.out.println("診斷結果已輸出到: " + filename);
        } catch (IOException e) {
            System.err.println("輸出JSON檔案時發生錯誤: " + e.getMessage());
        }
    }
    
    /**
     * 將診斷結果輸出為CSV格式
     * @param patient 病患資料
     * @param symptoms 症狀列表
     * @param prescription 處方
     * @param filename 輸出檔案名稱
     */
    public static void exportToCsv(Patient patient, List<String> symptoms, Prescription prescription, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // 寫入標題行
            writer.println("病患ID,姓名,性別,年齡,身高,體重,BMI,症狀,處方名稱,潛在疾病,藥物,使用方法,診斷時間");
            
            // 寫入資料行
            String symptomsStr = String.join(";", symptoms);
            String medicinesStr = String.join(";", prescription.getMedicines());
            
            writer.printf("\"%s\",\"%s\",\"%s\",%d,%.1f,%.1f,%.2f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                patient.getId(),
                patient.getName(),
                patient.getGender(),
                patient.getAge(),
                patient.getHeight(),
                patient.getWeight(),
                patient.getBMI(),
                symptomsStr,
                prescription.getName(),
                prescription.getPotentialDisease(),
                medicinesStr,
                prescription.getUsage().replace("\"", "\"\""),
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            
            System.out.println("診斷結果已輸出到: " + filename);
        } catch (IOException e) {
            System.err.println("輸出CSV檔案時發生錯誤: " + e.getMessage());
        }
    }
    
    /**
     * 診斷結果內部類別
     */
    private static class DiagnosisResult {
        private Patient patient;
        private List<String> symptoms;
        private Prescription prescription;
        private String diagnosisTime;
        
        public DiagnosisResult(Patient patient, List<String> symptoms, Prescription prescription) {
            this.patient = patient;
            this.symptoms = symptoms;
            this.prescription = prescription;
            this.diagnosisTime = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        // Getters for JSON serialization
        public Patient getPatient() { return patient; }
        public List<String> getSymptoms() { return symptoms; }
        public Prescription getPrescription() { return prescription; }
        public String getDiagnosisTime() { return diagnosisTime; }
    }
} 
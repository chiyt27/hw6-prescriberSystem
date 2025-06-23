package chiyt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
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
            
            ObjectNode patientNode = JsonNodeFactory.instance.objectNode();
            patientNode.put("id", patient.getId());
            patientNode.put("name", patient.getName());
            patientNode.put("gender", patient.getGender());
            patientNode.put("age", patient.getAge());
            patientNode.put("height", patient.getHeight());
            patientNode.put("weight", patient.getWeight());

            ObjectNode prescriptionnNode = JsonNodeFactory.instance.objectNode();
            prescriptionnNode.put("name", prescription.getName());
            prescriptionnNode.put("potentialDisease", prescription.getPotentialDisease());
            prescriptionnNode.put("medicines", String.join(";", prescription.getMedicines()));
            prescriptionnNode.put("usage", prescription.getUsage());

            ObjectNode result = JsonNodeFactory.instance.objectNode();
            result.set("patient", patientNode);
            result.put("symptoms", String.join(";", symptoms));
            result.set("prescription", prescriptionnNode);
            result.put("diagnosisTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // DiagnosisResult result = new DiagnosisResult(patient, symptoms, prescription);
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
            writer.println("病患ID,姓名,性別,年齡,身高,體重,症狀,處方名稱,潛在疾病,藥物,使用方法,診斷時間");
            
            // 寫入資料行
            String symptomsStr = String.join(";", symptoms);
            String medicinesStr = String.join(";", prescription.getMedicines());
            
            writer.printf("\"%s\",\"%s\",\"%s\",%d,%.1f,%.1f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                patient.getId(),
                patient.getName(),
                patient.getGender(),
                patient.getAge(),
                patient.getHeight(),
                patient.getWeight(),
                symptomsStr,
                prescription.getName(),
                prescription.getPotentialDisease(),
                medicinesStr,
                prescription.getUsage().replace("\"", "\"\""),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            System.out.println("診斷結果已輸出到: " + filename);
        } catch (IOException e) {
            System.err.println("輸出CSV檔案時發生錯誤: " + e.getMessage());
        }
    }

    // private static class DiagnosisResult {
    //     private Patient patient;
    //     private List<String> symptoms;
    //     private Prescription prescription;
    //     private String diagnosisTime;
        
    //     public DiagnosisResult(Patient patient, List<String> symptoms, Prescription prescription) {
    //         this.patient = patient;
    //         this.symptoms = symptoms;
    //         this.prescription = prescription;
    //         this.diagnosisTime = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    //     }
        
    //     // Getters for JSON serialization
    //     public Patient getPatient() { return patient; }
    //     public List<String> getSymptoms() { return symptoms; }
    //     public Prescription getPrescription() { return prescription; }
    //     public String getDiagnosisTime() { return diagnosisTime; }
    // }
} 
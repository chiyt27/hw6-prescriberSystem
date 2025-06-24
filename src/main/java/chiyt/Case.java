package chiyt;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Case {
    @JsonProperty("symptoms")
    private List<String> symptoms;
    
    @JsonProperty("prescription")
    private Prescription prescription;
    
    @JsonProperty("caseTime")
    private LocalDateTime caseTime;

    // 預設建構子（Jackson 需要）
    public Case() {}

    // 原有的建構子
    public Case(List<String> symptoms, Prescription prescription) {
        this.symptoms = symptoms;
        this.prescription = prescription;
        this.caseTime = LocalDateTime.now();
    }

    // Getters and Setters
    public List<String> getSymptoms() { return symptoms; }
    public void setSymptoms(List<String> symptoms) { this.symptoms = symptoms; }
    
    public Prescription getPrescription() { return prescription; }
    public void setPrescription(Prescription prescription) { this.prescription = prescription; }
    
    public LocalDateTime getCaseTime() { return caseTime; }
    public void setCaseTime(LocalDateTime caseTime) { this.caseTime = caseTime; }

    // @Override
    // public String toString() {
    //     return "Case{" +
    //             "symptoms=" + symptoms +
    //             ", prescription=" + prescription +
    //             ", caseTime=" + caseTime +
    //             '}';
    // }
} 
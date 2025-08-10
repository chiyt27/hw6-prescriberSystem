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

    public Case() {} // Jackson需要預設建構子

    public Case(List<String> symptoms, Prescription prescription) {
        this.symptoms = symptoms;
        this.prescription = prescription;
        this.caseTime = LocalDateTime.now();
    }

    public List<String> getSymptoms() { return symptoms; }
    public Prescription getPrescription() { return prescription; }
    public LocalDateTime getCaseTime() { return caseTime; }
} 
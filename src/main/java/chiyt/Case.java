package chiyt;

import java.time.LocalDateTime;
import java.util.List;

public class Case {
    private List<String> symptoms;
    private Prescription prescription;
    private LocalDateTime caseTime;

    public Case() {
        this.caseTime = LocalDateTime.now();
    }

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
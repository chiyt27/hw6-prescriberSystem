package chiyt;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisDemand {
    private final String patientId;
    private final List<String> symptoms;
    private final List<DiagnosisObserver> observers;

    public DiagnosisDemand(String patientId, List<String> symptoms, List<DiagnosisObserver> observers) {
        this.patientId = patientId;
        this.symptoms = symptoms;
        this.observers = observers != null ? new ArrayList<>(observers) : new ArrayList<>();
    }

    public String getPatientId() { return patientId;}
    public List<String> getSymptoms() { return symptoms; }
    public List<DiagnosisObserver> getObservers() { return observers; }
}

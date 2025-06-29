package chiyt;

import java.util.List;

public class DiagnosisRequest {
    private final String patientId;
    private final List<String> symptoms;
    private final DiagnosisCallback callback;

    public DiagnosisRequest(String patientId, List<String> symptoms, DiagnosisCallback callback) {
        this.patientId = patientId;
        this.symptoms = symptoms;
        this.callback = callback;
    }

    public String getPatientId() { return patientId;}
    public List<String> getSymptoms() { return symptoms; }
    public DiagnosisCallback getCallback() { return callback; }
}

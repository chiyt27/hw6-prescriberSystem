package chiyt;

import java.util.Arrays;
import java.util.List;

public class AttractiveRule implements DiagnosisRule {
    
    @Override
    public boolean matches(Patient patient, List<String> symptoms) {
        return patient.getAge() == 18 && 
               "female".equals(patient.getGender()) && 
               symptoms.contains("sneeze");
    }
    
    @Override
    public Prescription generatePrescription() {
        return new Prescription(
            "青春抑制劑",
            "有人想你了（專業學名：Attractive）",
            Arrays.asList("假鬢角", "臭味"),
            "把假鬢角黏在臉的兩側，讓自己異性緣差一點，自然就不會有人想妳了。"
        );
    }
    
    @Override
    public String getSupportedDisease() {
        return "Attractive";
    }
} 
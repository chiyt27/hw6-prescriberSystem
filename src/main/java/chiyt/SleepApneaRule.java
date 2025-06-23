package chiyt;

import java.util.Arrays;
import java.util.List;

public class SleepApneaRule implements DiagnosisRule {
    
    @Override
    public boolean matches(Patient patient, List<String> symptoms) {
        return patient.getBMI() > 26 && 
               symptoms.contains("snore");
    }
    
    @Override
    public Prescription generatePrescription() {
        return new Prescription(
            "打呼抑制劑",
            "睡眠呼吸中止症（專業學名：SleepApneaSyndrome）",
            Arrays.asList("一捲膠帶"),
            "睡覺時，撕下兩塊膠帶，將兩塊膠帶交錯黏在關閉的嘴巴上，就不會打呼了。"
        );
    }
    
    @Override
    public String getSupportedDisease() {
        return "SleepApneaSyndrome";
    }
} 
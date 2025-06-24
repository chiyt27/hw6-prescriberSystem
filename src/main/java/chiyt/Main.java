package chiyt;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        PrescriberFacade facade = new PrescriberFacade();
        // case 診斷COVID19
        List<String> covidSymptoms = Arrays.asList("sneeze", "headache", "cough");
        facade.performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            "A123456789", covidSymptoms, "covid_diagnosis.json", "json");
        
        facade.performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            "A123456789", covidSymptoms, "covid_diagnosis.csv", "csv");
            
        
        // case 診斷有人想你了
        // List<String> attractiveSymptoms = Arrays.asList("sneeze");
        // facade.performDiagnosis("J221987654", attractiveSymptoms, "attractive_diagnosis.csv", "csv");

        // case 診斷睡眠呼吸中止症
        // List<String> sleepApneaSymptoms = Arrays.asList("snore");
        // facade.performDiagnosis("C156789123", sleepApneaSymptoms, "sleep_apnea_diagnosis.json", "json");

        // List<String> generalSymptoms = Arrays.asList("fever", "fatigue");
        // facade.performDiagnosis("B187654321", generalSymptoms);
    }
}

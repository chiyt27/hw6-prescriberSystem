package chiyt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PrescriberFacade facade = new PrescriberFacade();
        
        // 用戶1: 診斷新冠肺炎，同時寫入JSON檔案並發送Email
        List<DiagnosisObserver> observers1 = new ArrayList<>();
        observers1.add(new SaveToFileObserver("covid_diagnosis.json", "json"));
        observers1.add(new SendEmailObserver("doctor@hospital.com"));

        facade.performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            "A123456789",
            Arrays.asList("sneeze", "headache", "cough"),
            observers1
        );
        
        // 用戶2: 診斷睡眠呼吸中止症，只寫入CSV檔案
        List<DiagnosisObserver> observers2 = List.of(
            new SaveToFileObserver("sleep_apnea_diagnosis.csv", "csv")
        );

        facade.performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            "C156789123",
            List.of("snore"),
            observers2
        );

        // 用戶3: 診斷有人想你了，只發送Email
        List<DiagnosisObserver> observers3 = List.of(
            new SendEmailObserver("friend@example.com")
        );

        facade.performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            "J221987654",
            List.of("sneeze"),
            observers3
        );

        // facade.stopService();
    }
}

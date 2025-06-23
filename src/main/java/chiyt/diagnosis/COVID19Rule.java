package chiyt.diagnosis;

import java.util.Arrays;
import java.util.List;

import chiyt.Patient;
import chiyt.Prescription;

public class COVID19Rule implements DiagnosisRule {
    
    @Override
    public boolean matches(Patient patient, List<String> symptoms) {
        return symptoms.contains("sneeze") && 
               symptoms.contains("headache") && 
               symptoms.contains("cough");
    }
    
    @Override
    public Prescription generatePrescription() {
        return new Prescription(
            "清冠一號",
            "新冠肺炎（專業學名：COVID-19）",
            Arrays.asList("清冠一號"),
            "將相關藥材裝入茶包裡，使用500 mL 溫、熱水沖泡悶煮1~3 分鐘後即可飲用。"
        );
    }
    
    @Override
    public String getSupportedDisease() {
        return "COVID-19";
    }
} 
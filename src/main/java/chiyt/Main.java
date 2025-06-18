package chiyt;

import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main(String[] args) {
        try {
            PatientReader reader = new PatientReader();
            String filePath = "src/main/resources/patientData.json";
            List<Patient> patients = reader.readPatientsFromJson(filePath);
            patients.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

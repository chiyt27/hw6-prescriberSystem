package chiyt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
 * B1 模組的維護者會使用 PatientDatabase 物件來查詢病患資料和添加病患的新病例
*/
public class PatientDatabase {
    private Map<String, Patient> patients = new HashMap<>();

    public void readPatientsFromJson(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

        ObjectMapper objectMapper = new ObjectMapper();
        List<Patient> patients = objectMapper.readValue(jsonContent, new TypeReference<List<Patient>>() {});

        for (Patient patient : patients) {
            this.patients.put(patient.getId(), patient);
        }
    }

    public Patient getPatient(String id) {
        return patients.get(id);
    }

    public void addCase(String id, Case newCase) {
        Patient patient = getPatient(id);
        if (patient != null)
            patient.addCase(newCase);
    }
} 
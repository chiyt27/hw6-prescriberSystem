package chiyt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


public class PatientReader {
    public List<Patient> readPatientsFromJson(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        System.out.println("JSON Content: " + jsonContent);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Patient> patients = objectMapper.readValue(jsonContent, new TypeReference<List<Patient>>() {});
        System.out.println("Parsed Patients: " + patients);

        return patients;
    }
}

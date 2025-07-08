package chiyt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/** 
 * B1 模組的維護者會使用 PatientDatabase 物件來查詢病患資料和添加病患的新病例
*/
public class PatientDatabase {
    private Map<String, Patient> patients = new HashMap<>();

    public void loadPatientsFromJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());// 註冊 JavaTimeModule 來處理 LocalDateTime
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 忽略未知屬性，避免 JSON 結構不一致時出錯

        TypeReference<List<Patient>> typeReference = new TypeReference<List<Patient>>() {};
        List<Patient> patientList = objectMapper.readValue(new File(filePath), typeReference);

        patients.clear();
        for (Patient patient : patientList) {
            patients.put(patient.getId(), patient);
        }
    }

    public Patient getPatient(String id) {
        return patients.get(id);
    }

    public void addCaseToPatient(String id, Case newCase) {
        Patient patient = getPatient(id);
        if (patient != null)
            patient.addCase(newCase);
    }
} 
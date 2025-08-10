package chiyt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
        if (patient != null) {
            patient.addCase(newCase);
        }
    }
    
    /**
     * 將病患資料儲存到指定的 JSON 檔案
     * @param filePath 目標檔案路徑
     * @throws IOException 檔案寫入錯誤
     */
    public void saveToFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true); // 格式化輸出
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 使用 ISO 格式輸出日期
        
        // 轉換為 List 格式寫回檔案
        List<Patient> patientList = new ArrayList<>(patients.values());
        objectMapper.writeValue(new File(filePath), patientList);
        
        // System.out.println("病患資料已成功寫回檔案: " + filePath);
    }
} 
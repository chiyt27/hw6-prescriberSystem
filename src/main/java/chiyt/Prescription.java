package chiyt;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prescription {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("potentialDisease")
    private String potentialDisease;
    
    @JsonProperty("medicines")
    private List<String> medicines;
    
    @JsonProperty("usage")
    private String usage;

    // 預設建構子（Jackson 需要）
    public Prescription() {}

    // 原有的建構子
    public Prescription(String name, String potentialDisease, List<String> medicines, String usage) {
        validateName(name);
        validatePotentialDisease(potentialDisease);
        validateMedicines(medicines);
        validateUsage(usage);

        this.name = name;
        this.potentialDisease = potentialDisease;
        this.medicines = medicines;
        this.usage = usage;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public String getPotentialDisease() { return potentialDisease; }
    public List<String> getMedicines() { return medicines; }
    public String getUsage() { return usage; }

    // Validation method
    private void validateName(String name) {
        if (name == null || name.length() < 4 || name.length() > 30) {
            throw new IllegalArgumentException("處方名稱長度必須在4-30字元之間");
        }
    }
    
    private void validatePotentialDisease(String disease) {
        if (disease == null || disease.length() < 3 || disease.length() > 100) {
            throw new IllegalArgumentException("潛在疾病長度必須在3-100字元之間");
        }
    }
    
    private void validateMedicines(List<String> medicines) {
        if (medicines == null || medicines.isEmpty()) {
            throw new IllegalArgumentException("藥物清單不能為空");
        }
        for (String medicine : medicines) {
            if (medicine == null || medicine.length() < 3 || medicine.length() > 30) {
                throw new IllegalArgumentException("藥物名稱長度必須在3-30字元之間");
            }
        }
    }
    
    private void validateUsage(String usage) {
        if (usage == null) {
            usage = "";
        }
        if (usage.length() > 1000) {
            throw new IllegalArgumentException("使用方法長度不能超過1000字元");
        }
    }
}

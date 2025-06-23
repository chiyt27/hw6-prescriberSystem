# 處方診斷系統 (Prescriber System)

這是一個醫院處方診斷系統的核心模組，支援多種疾病的診斷和處方生成。

## 系統架構

### 核心組件

1. **PatientDatabase** - 病患資料庫管理
2. **Prescriber** - 診斷器（支援隊列處理）
3. **DiagnosisRule** - 診斷規則介面
4. **PrescriberFacade** - 簡化API的門面類別
5. **ResultExporter** - 結果輸出器（支援JSON/CSV）

### 設計模式

- **Facade Pattern** - 簡化複雜的診斷流程
- **Strategy Pattern** - 可擴充的診斷規則
- **Observer Pattern** - 診斷結果回調機制
- **Singleton Pattern** - 病患資料庫單例管理

## 支援的疾病診斷

### 1. 新冠肺炎 (COVID-19)
- **症狀**: 打噴嚏 (sneeze) + 頭痛 (headache) + 咳嗽 (cough)
- **處方**: 清冠一號
- **使用方法**: 將相關藥材裝入茶包裡，使用500 mL 溫、熱水沖泡悶煮1~3 分鐘後即可飲用。

### 2. 有人想你了 (Attractive)
- **條件**: 18歲女性 + 打噴嚏 (sneeze)
- **處方**: 青春抑制劑
- **使用方法**: 把假鬢角黏在臉的兩側，讓自己異性緣差一點，自然就不會有人想妳了。

### 3. 睡眠呼吸中止症 (SleepApneaSyndrome)
- **條件**: BMI > 26 + 打呼 (snore)
- **處方**: 打呼抑制劑
- **使用方法**: 睡覺時，撕下兩塊膠帶，將兩塊膠帶交錯黏在關閉的嘴巴上，就不會打呼了。

## 使用方法

### 基本使用

```java
// 創建診斷系統
PrescriberFacade facade = new PrescriberFacade();

// 執行診斷（最簡化版本）
List<String> symptoms = Arrays.asList("sneeze", "headache", "cough");
boolean result = facade.performDiagnosis("Alice Johnson", symptoms);
```

### 完整使用

```java
// 指定所有參數
boolean result = facade.performDiagnosis(
    "src/main/resources/patientData.json",    // 病患資料檔案
    "src/main/resources/supportedDiseases.txt", // 支援疾病檔案
    "Alice Johnson",                          // 病患姓名
    Arrays.asList("sneeze", "headache", "cough"), // 症狀
    "diagnosis_result.json",                  // 輸出檔案
    "json"                                    // 輸出格式 (json/csv)
);
```

### 進階使用

```java
// 直接使用核心組件
PatientDatabase db = new PatientDatabase();
Prescriber prescriber = new Prescriber(db);

// 設置自定義回調
prescriber.setDiagnosisCallback((patient, symptoms, prescription) -> {
    System.out.println("診斷完成: " + prescription.getName());
    // 自定義處理邏輯
});

// 添加自定義診斷規則
prescriber.addDiagnosisRule(new CustomDiagnosisRule());

// 開始診斷服務
prescriber.startDiagnosisService();

// 提交診斷請求
prescriber.requestDiagnosis("A123456789", Arrays.asList("fever", "cough"));

// 停止服務
prescriber.stopDiagnosisService();
```

## 檔案格式

### 病患資料 (JSON)
```json
[
    {
        "id": "A123456789",
        "name": "Alice Johnson",
        "gender": "female",
        "age": 25,
        "height": 165.4,
        "weight": 55.2,
        "cases": []
    }
]
```

### 支援疾病 (TXT)
```
COVID-19
Attractive
SleepApneaSyndrome
```

### 輸出格式

#### JSON 格式
```json
{
    "patient": {
        "id": "A123456789",
        "name": "Alice Johnson",
        "gender": "female",
        "age": 25,
        "height": 165.4,
        "weight": 55.2,
        "bmi": 20.2
    },
    "symptoms": ["sneeze", "headache", "cough"],
    "prescription": {
        "name": "清冠一號",
        "potentialDisease": "新冠肺炎（專業學名：COVID-19）",
        "medicines": ["清冠一號"],
        "usage": "將相關藥材裝入茶包裡，使用500 mL 溫、熱水沖泡悶煮1~3 分鐘後即可飲用。"
    },
    "diagnosisTime": "2024-01-01 12:00:00"
}
```

#### CSV 格式
```csv
病患ID,姓名,性別,年齡,身高,體重,BMI,症狀,處方名稱,潛在疾病,藥物,使用方法,診斷時間
"A123456789","Alice Johnson","female",25,165.4,55.2,20.20,"sneeze;headache;cough","清冠一號","新冠肺炎（專業學名：COVID-19）","清冠一號","將相關藥材裝入茶包裡，使用500 mL 溫、熱水沖泡悶煮1~3 分鐘後即可飲用。","2024-01-01 12:00:00"
```

## 擴充診斷規則

要添加新的診斷規則，只需實現 `DiagnosisRule` 介面：

```java
public class CustomDiagnosisRule implements DiagnosisRule {
    @Override
    public boolean matches(Patient patient, List<String> symptoms) {
        // 實現診斷邏輯
        return symptoms.contains("custom_symptom") && patient.getAge() > 50;
    }
    
    @Override
    public Prescription generatePrescription() {
        return new Prescription(
            "自定義處方",
            "自定義疾病",
            Arrays.asList("自定義藥物"),
            "自定義使用方法"
        );
    }
    
    @Override
    public String getSupportedDisease() {
        return "CustomDisease";
    }
}
```

## 執行系統

```bash
# 編譯專案
mvn compile

# 執行主程式
mvn exec:java -Dexec.mainClass="chiyt.Main"

# 或者直接執行
java -cp target/classes chiyt.Main
```

## 系統特色

1. **可擴充性** - 支援動態添加診斷規則
2. **非阻塞** - 使用隊列處理多個診斷請求
3. **回調機制** - 外部可自定義診斷結果處理
4. **多格式輸出** - 支援JSON和CSV格式
5. **簡化API** - 1-3行程式碼即可完成完整診斷流程
6. **線程安全** - 支援多用戶同時使用

## 注意事項

- 診斷過程需要3秒鐘
- 系統會自動將診斷結果保存為病患的病例記錄
- 如果沒有匹配的診斷規則，會生成預設的"一般處方"
- 支援的病患資料格式必須符合JSON規範
- 症狀名稱必須使用英文小寫 
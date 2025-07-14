package chiyt;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PrescriberFacade facade = new PrescriberFacade();
        
        // 示範多用戶同時診斷 (非同步)
        System.out.println("=== 多用戶同時診斷測試 ===\n");
        
        // 用戶1: 診斷COVID19 - 輸出JSON
        List<String> covidSymptoms = Arrays.asList("sneeze", "headache", "cough");
        facade.performDiagnosis(
            "src/main/resources/patientData.json",
            "src/main/resources/supportedDiseases.txt",
            "A123456789", covidSymptoms, "covid_diagnosis1.json", "json");
        
        // 用戶2: 診斷COVID19 - 輸出CSV
        facade.performDiagnosis("A123456789", covidSymptoms, "covid_diagnosis2.csv", "csv");
        
        // 用戶3: 診斷有人想你了 - 不輸出檔案
        List<String> attractiveSymptoms = Arrays.asList("sneeze");
        facade.performDiagnosis("J221987654", attractiveSymptoms);
        
        // 用戶4: 診斷睡眠呼吸中止症 - 輸出JSON
        List<String> sleepApneaSymptoms = Arrays.asList("snore");
        facade.performDiagnosis("C156789123", sleepApneaSymptoms, "sleep_apnea_diagnosis.json", "json");
        
        // 用戶5: 無法診斷的症狀
        List<String> generalSymptoms = Arrays.asList("fever", "fatigue");
        facade.performDiagnosis("B187654321", generalSymptoms);
        
        System.out.println("所有診斷請求已提交完成，等待背景處理...\n");
        
        // 等待一段時間讓所有診斷完成
        try {
            Thread.sleep(20000); // 等待20秒讓所有診斷完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 停止服務
        PrescriberFacade.stopService();
        System.out.println("程式結束");
    }
}

package chiyt;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 處方診斷系統演示 ===");
        
        // 創建診斷系統
        PrescriberFacade facade = new PrescriberFacade();
        
        // 演示1: 新冠肺炎診斷
        System.out.println("\n1. 診斷新冠肺炎案例:");
        List<String> covidSymptoms = Arrays.asList("sneeze", "headache", "cough");
        boolean result1 = facade.performDiagnosis("Alice Johnson", covidSymptoms, "covid_diagnosis.json", "json");
        System.out.println("新冠肺炎診斷結果: " + (result1 ? "成功" : "失敗"));
        
        // 演示2: 有人想你了診斷
        System.out.println("\n2. 診斷有人想你了案例:");
        List<String> attractiveSymptoms = Arrays.asList("sneeze");
        boolean result2 = facade.performDiagnosis("Julia Anderson", attractiveSymptoms, "attractive_diagnosis.csv", "csv");
        System.out.println("有人想你了診斷結果: " + (result2 ? "成功" : "失敗"));
        
        // 演示3: 睡眠呼吸中止症診斷
        System.out.println("\n3. 診斷睡眠呼吸中止症案例:");
        List<String> sleepApneaSymptoms = Arrays.asList("snore");
        boolean result3 = facade.performDiagnosis("Charlie Brown", sleepApneaSymptoms, "sleep_apnea_diagnosis.json", "json");
        System.out.println("睡眠呼吸中止症診斷結果: " + (result3 ? "成功" : "失敗"));
        
        // 演示4: 簡化調用（使用預設設定）
        System.out.println("\n4. 簡化調用演示:");
        List<String> generalSymptoms = Arrays.asList("fever", "fatigue");
        boolean result4 = facade.performDiagnosis("Bob Smith", generalSymptoms);
        System.out.println("一般診斷結果: " + (result4 ? "成功" : "失敗"));
        
        System.out.println("\n=== 診斷系統演示完成 ===");
        
        // 顯示病患資料庫統計
        System.out.println("\n病患資料庫統計:");
        System.out.println("總病患數: " + facade.getPatientDatabase().getPatientCount());
        System.out.println("所有病患:");
        facade.getPatientDatabase().getAllPatients().forEach(System.out::println);
    }
}

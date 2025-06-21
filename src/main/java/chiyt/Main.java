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

            //3a. 裝著病患資料的 JSON 檔案名稱（需求 C-1)
            //3b. 裝著支援潛在疾病診斷的純文字檔案名稱（需求 C-2)
            //3c. 欲診斷病患的名稱和他的多項症狀。
            //3d. 在完成診斷之後，我能向模組要求把此次診斷結果存到哪個檔案，並且也能選擇要存成 JSON 格式還是 CSV 格式。


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

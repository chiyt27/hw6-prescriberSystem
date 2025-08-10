package chiyt;

import java.util.List;

/**
 * 一個觀察者實現，模擬發送Email通知。
 */
public class SendEmailObserver implements DiagnosisObserver {

    private final String recipient;

    /**
     * @param recipient 郵件接收者的地址
     */
    public SendEmailObserver(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public void onDiagnosisCompleted(Patient patient, List<String> symptoms, Prescription prescription) {
        if (patient == null || prescription == null) {
            System.out.println("SendEmailObserver: 無有效診斷結果，不發送Email。");
            return;
        }

        System.out.println(String.format("發送診斷結果Email: %s, 診斷結果: %s", patient.getId(), prescription.getName()));
        // System.out.println("SendEmailObserver: 準備發送診斷結果Email...");
        // String subject = "病患 " + patient.getName() + " 的診斷結果";
        // String body = "親愛的 " + recipient + ",\n\n" +
        //         "病患 " + patient.getName() + " (ID: " + patient.getId() + ") 的診斷已完成。\n" +
        //         "症狀: " + symptoms + "\n" +
        //         "處方: " + prescription.getName() + " - " + prescription.getPotentialDisease() + "\n" +
        //         "使用方法: " + prescription.getUsage() + "\n\n" +
        //         "祝您健康！\n" +
        //         "處方診斷系統";

        // System.out.println("-------------------- EMAIL SIMULATION --------------------");
        // System.out.println("To: " + recipient);
        // System.out.println("Subject: " + subject);
        // System.out.println("Body:\n" + body);
        // System.out.println("---------------------------------------------------------");
        // System.out.println("Email已成功發送 (模擬)。");
    }
} 
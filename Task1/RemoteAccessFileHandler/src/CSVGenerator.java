import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSVGenerator {
    public static void main(String[] args) {
        BufferedWriter w = null;
        String csvFileName = null;
        try {
            csvFileName = "ModutechEmployee.csv";
            String head = "NO , 이름 , 직책 , 부서 , 나이 , 성별 , 입사일";
            String firstLine = "1 , 윤석규 , 사원 , 개발1팀 , 26 , 남 , 2019-02-01";
            String secondLine = "2 , 정창훈 , 신입 , 개발2팀 , 27 , 남 , 2019-08-01";

            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName), "MS949"));
            w.write(head);
            w.write(firstLine);
            w.write(secondLine);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
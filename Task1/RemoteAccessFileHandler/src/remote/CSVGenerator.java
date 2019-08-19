package remote;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.*;

public class CSVGenerator {
    public static void main(String[] args) {
        String result = "";
        BufferedWriter w = null;
        try {
            String csvFileName = "모두텍구성원정보.csv";
            String head = "NO , 이름 , 직책 , 부서 , 나이 , 성별 , 입사일";
            String firstLine = "1 , 윤석규 , 사원 , 개발1팀 , 26 , 남 , 2019-02-01";
            String secondLine = "2 , 정창훈 , 신입 , 개발2팀 , 27 , 남 , 2019-08-01";

            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName), "MS949"));
            w.write(head);
            w.write(firstLine);
            w.write(secondLine);
            result = "Success!";
        } catch (IOException e) {
            e.printStackTrace();
            result = "Fail!";
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ModutechRestApiHelper ah = new ModutechRestApiHelper();
        ah.sendREST(result);
    }
}
package remote;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import  java.util.logging.*;

public class CSVGenerator {
    public static void main(String[] args) {
        BufferedWriter w = null;
        try{
            String csvFileName = "모두텍구성원정보.csv";
            String head = "NO , 이름 , 직책 , 부서 , 나이 , 성별 , 입사일";
            String firstLine = "1 , 윤석규 , 사원 , 개발1팀 , 26 , 남 , 2019-02-01";
            String secondLine = "2 , 정창훈 , 신입 , 개발2팀 , 27 , 남 , 2019-08-01";

            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName), "MS949"));
            w.write(head);
            w.write(firstLine);
            w.write(secondLine);
        }catch(IOException  e){
            e.printStackTrace();
        }finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        sendREST();
    }

    public static void sendREST() throws IllegalStateException {

        try {
            String downloadState = URLEncoder.encode("Completion!", "UTF-8");
            URL url = new URL("192.168.1.9:8081/index.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            String postParams = "downloadState=" + downloadState;
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
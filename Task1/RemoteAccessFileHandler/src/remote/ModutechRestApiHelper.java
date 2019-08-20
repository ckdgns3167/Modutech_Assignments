package remote;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;

public class ModutechRestApiHelper {

    private static final String API_SERVER_HOST  = "http://192.168.2.42:8081/index.jsp";

    public static void sendREST(String result) throws IllegalStateException {

        try {
            String downloadState = URLEncoder.encode(result, "UTF-8");
            URL url = new URL(API_SERVER_HOST);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            String postParams = "result=" + downloadState;
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

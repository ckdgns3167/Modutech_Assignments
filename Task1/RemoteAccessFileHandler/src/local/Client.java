package local;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class Client {

    private static JSch jsch = null;
    private static Session session = null;
    private static Channel channel = null;
    private static ChannelSftp sftpChannel = null;

    private static AccessTarget target = new AccessTarget();

    private static Scanner scan = null;

    private static boolean firstConnection = true;
    private static boolean isRunning = true;

    public static void connect() throws JSchException {
        System.out.println("connecting..."+ target.getTarget());
        // 1. JSch 객체를 생성한다.
        jsch = new JSch();
        // 2. 세션 객체를 생성한다(사용자 이름, 접속할 호스트, 포트를 인자로 전달한다.)
        session = jsch.getSession(target.getUser(), target.getTarget(), target.getPort());
        // 3. 세션과 관련된 정보를 설정한다.
        session.setConfig("StrictHostKeyChecking", "no");
        // 4. 패스워드를 설정한다.
        session.setPassword(target.password);
        // 5. 접속한다.
        session.connect();
        // 6. sftp 채널을 연다.
        channel = session.openChannel("sftp");
        // 7. 채널에 연결한다.
        channel.connect();
        // 8. 채널을 FTP용 채널 객체로 캐스팅한다.
        sftpChannel = (ChannelSftp) channel;

        System.out.println("Congratulations, you have successfully accessed.");
    }

    public static void upload(String fileName, String remoteDir) throws Exception {
        FileInputStream fis = null;
        try {
            // Change to output directory
            sftpChannel.cd(remoteDir);

            // Upload file
            File file = new File(fileName);

            // 입력 파일을 가져온다.
            fis = new FileInputStream(file);
            // 파일을 업로드한다.
            sftpChannel.put(fis, file.getName());

            fis.close();
            System.out.println("File uploaded successfully - " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public static void disconnect() {
        if (session.isConnected()) {
            sftpChannel.disconnect();
            channel.disconnect();
            session.disconnect();
            System.out.println("Connection has been terminated from " + target.getTarget());
        }
    }

    public static void main(String[] args) {

        scan = new Scanner(System.in);
        System.out.println("/............Start RemoteAccessFileHandler program...........\\");

        while (isRunning) {

            if(firstConnection) {
                System.out.print("[1] Connect to Remote [2] Upload File [3] Download File [4] Bye~ : ");
            }
            else
                System.out.print("[1] Connect to Another [2] Upload File [3] Download File [4] Bye~ : ");

            int userOrder = scan.nextInt();

            try {
                switch (userOrder) {
                    case 1:
                        if(!firstConnection) {
                            disconnect();
                        }
                        firstConnection = false;
                        System.out.println("Enter Target's SSH Access Info below...");
                        System.out.println("---------------------------------------");
                        System.out.print("  IP : ");//"192.168.1.126"
                        target.setTarget(scan.next());
                        System.out.print("user : ");//ubuntu
                        target.setUser(scan.next());
                        System.out.print("pass : ");//test
                        target.setPassword(scan.next());
                        System.out.print("port : ");//22
                        target.setPort(scan.nextInt());
                        System.out.println("---------------------------------------");
                        connect();
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        disconnect();
                        isRunning = false;
                        break;
                }
            } catch (Exception e) {

            }
        }
    }

}

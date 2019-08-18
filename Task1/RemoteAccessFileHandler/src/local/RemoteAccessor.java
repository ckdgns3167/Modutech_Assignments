package local;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class RemoteAccessor {
    private static final int S_CONNECTION_TIMEOUT = 30000;
    private static final int C_CONNECTION_TIMEOUT = 3000;
    private static final int BUFFER_SIZE = 1024;

    private JSch jsch = null;
    private Channel channel = null; // The abstract base class for the different types of channel which may be associated with a Session.
    private Session session = null; // A Session represents a connection to a SSH server.

    AccessTarget target = new AccessTarget(); // 접속 대상, 즉 ssh로 연결될 원격 컴퓨터의 정보를 추상화한 클래스. 이에 대한 인스턴스를 생성.

    // 원격으로 컴퓨터에 접속하고 shell prompt를 사용하게 해줌.
    public void ConnectToRemoteComputer() {
        try {
            jsch = new JSch();
            session = jsch.getSession(target.getUser(), target.getTarget(), target.getPort());
            session.setPassword(target.getPass());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setDaemonThread(false);
            /*
                config 설정 : ssh_config 에 호스트 키가 없더라도 바로 접속이 되도록 설정, 우분투의 경우 /etc/ssh/ssh_config 에 아래 설정을 추가됨.
                해주는 이유 : ssh 로 리모트 서버에 접속시 호스트 키가 ~/.ssh/known_hosts 파일에 없을경우 추가할지를 물어본다.
                보통 때는 문제 될 것이 없지만 배치 작업등을 할 경우는 일일이 호스트 키를 추가했는지 확인하기 때문에 귀찮다.
             */
            session.connect(S_CONNECTION_TIMEOUT); // 시간 내에 접속을 안하면 연결 요청 취소
            this.getRemoteShellPrompt(session);//원격에 있는 컴퓨터의 shell을 사용.
        } catch (JSchException e) {
            System.out.println(e);
        }
    }

    public void getRemoteShellPrompt(Session session) {
        System.out.println(">>>>> The connection is complete. Use as much as you like.<<<<<\n");
        try {
            channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.connect(C_CONNECTION_TIMEOUT);
            InputStream inputStream = channel.getInputStream(); // <- 일반 출력 스트림
            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(buffer, 0, BUFFER_SIZE);
                    if (i < 0) {
                        break;
                    }
                    String output = new String(buffer, 0, i);
                    if(!output.contains(": ")){
                        output = output.substring(output.indexOf("\n") + 1);
                    }else if(output.contains("-bash"))
                        output = output.substring(output.indexOf("\n") + 1);
                    System.out.print(output);
                }
                if (channel.isClosed()) {
                    if (inputStream.available() > 0) {
                        continue;
                    }
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (channel.isConnected())
                channel.disconnect();
            if (session.isConnected())
                session.disconnect();
            System.out.println("The connection has been terminated. Thank you!");
            System.exit(0);
        }
    }

//    public void upload(String fileName, String remoteDir) {
//        File file = null;
//        FileInputStream fis = null;
//        try {
//            sftpChannel.cd(remoteDir);
//            file = new File(fileName);
//            fis = new FileInputStream(file);
//            sftpChannel.put(fis, file.getName());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (SftpException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fis != null)
//                    fis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("File uploaded successfully - " + file.getAbsolutePath());
//    }

//    public void download(String fileName, String localDir) {
//
//        byte[] buffer = new byte[1024];
//        BufferedInputStream bis = null;
//        File file = null;
//        File newFile = null;
//        OutputStream os = null;
//        BufferedOutputStream bos = null;
//        // Change to output directory
//        String cdDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
//        try {
//
//            sftpChannel.cd(cdDir);
//            file = new File(fileName);
//            bis = new BufferedInputStream(sftpChannel.get(file.getName()));
//            newFile = new File(localDir + "/" + file.getName());
//
//            // Download file
//            os = new FileOutputStream(newFile);
//            bos = new BufferedOutputStream(os);
//            int readCount;
//            while ((readCount = bis.read(buffer)) > 0) {
//                bos.write(buffer, 0, readCount);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SftpException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bis != null)
//                    bis.close();
//                if (bos != null)
//                    bos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("File downloaded successfully - " + file.getAbsolutePath());
//    }

//    public void disconnect(AccessTarget target) {
//        if (session.isConnected()) {
//            sftpChannel.disconnect();
//            channel.disconnect();
//            session.disconnect();
//            System.out.println("Connection has been terminated from " + target.getTarget());
//        }
//    }
class AccessTarget{
    String target = null;//접속 대상 IP
    String user = null;
    String pass = null;
    Integer port = null;

    public String getTarget() {
        return target;
    }

    public void setTarget(String host) {
        this.target = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String userName) {
        this.user = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
}

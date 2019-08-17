package local;

import com.jcraft.jsch.*;

public class RemoteAccessor {

    private JSch jsch = null;
    private Session session = null;
    private Channel channel = null;

    AccessTarget target = new AccessTarget(); // 접속 대상, 즉 ssh로 연결될 원격 컴퓨터의 정보를 추상화한 클래스. 이에 대한 인스턴스를 생성.

    // 원격으로 컴퓨터에 접속하고 shell prompt를 사용하게 해줌.
    public void ConnectToRemoteComputer() {
        try {
            jsch = new JSch();
            session = jsch.getSession(target.getUser(), target.getTarget(), target.getPort());
            session.setPassword(target.getPass());
            System.out.println("1");
            session.setConfig("StrictHostKeyChecking", "no"); // config 설정 : ssh_config 에 호스트 키가 없더라도 바로 접속이 되도록 설정, 우분투의 경우 /etc/ssh/ssh_config 에 아래 설정을 추가됨.
            System.out.println("2");
            session.connect(30000); // 시간 내에 접속을 안하면 연결 요청 취소
            System.out.println("3");
            this.remoteShellPrompt(session);//원격에 있는 컴퓨터의 shell을 사용.
        } catch (JSchException e) {
            System.out.println(e);
        }
    }

    public void remoteShellPrompt(Session session) {
        try {
            System.out.println("2");
            channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect(3 * 1000);
        } catch (JSchException e) {
            e.printStackTrace();
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

}

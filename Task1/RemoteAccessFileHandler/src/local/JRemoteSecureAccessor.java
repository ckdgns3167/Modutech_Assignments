package local;

import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.*;

public class JRemoteSecureAccessor {

    private JSch jsch = null;
    private Session session = null;
    private Channel channel = null;
    private ChannelShell ShellChannel = null;
    private ChannelSftp sftpChannel = null;

    AccessTarget target = new AccessTarget();//접속 대상을 의미하는 클래스에 대한 인스턴스를 생성함.

    public void ConnectToRemoteComputer() {
        try {
            jsch = new JSch();
            Session session = jsch.getSession(target.getUser(), target.getTarget(), target.getPort());
            session.setPassword(target.getPass());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000);   // making a connection with timeout.

            remoteShellPrompt(session);
        } catch (JSchException e) {
            System.out.println(e);
        }
    }

    public void remoteShellPrompt(Session session) {
        try {
            Channel channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect(3 * 1000);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void upload(String fileName, String remoteDir) {
        File file = null;
        FileInputStream fis = null;
        try {
            sftpChannel.cd(remoteDir);
            file = new File(fileName);
            fis = new FileInputStream(file);
            sftpChannel.put(fis, file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("File uploaded successfully - " + file.getAbsolutePath());

    }

    public void download(String fileName, String localDir) {

        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        File file = null;
        File newFile = null;
        OutputStream os = null;
        BufferedOutputStream bos = null;
        // Change to output directory
        String cdDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
        try {

            sftpChannel.cd(cdDir);
            file = new File(fileName);
            bis = new BufferedInputStream(sftpChannel.get(file.getName()));
            newFile = new File(localDir + "/" + file.getName());

            // Download file
            os = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(os);
            int readCount;
            while ((readCount = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("File downloaded successfully - " + file.getAbsolutePath());
    }

    public void disconnect(AccessTarget target) {
        if (session.isConnected()) {
            sftpChannel.disconnect();
            channel.disconnect();
            session.disconnect();
            System.out.println("Connection has been terminated from " + target.getTarget());
        }
    }

}

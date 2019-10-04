import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RemoteFileManager {

	private SSH_Connection_Manager scm = null;
	private Channel channel = null;
	private ChannelSftp sftpChannel = null;

	public RemoteFileManager(SSH_Connection_Manager scm) {
		this.scm = scm;
		this.scm.connect();
	}

	public void executeFile(String remoteLocation, String filename, String result_fileName,
			String... required_file_names) {
		
		String requiredFiles = " ";
		for (String fn : required_file_names) {
			requiredFiles += fn + " ";
		}
		try {
			channel = scm.getSession().openChannel("exec");
			if (!requiredFiles.equals(" ")) {
				((ChannelExec) channel)
						.setCommand("cd " + remoteLocation + "; ./" + filename + requiredFiles + result_fileName);
			} else {
				((ChannelExec) channel).setCommand("cd " + remoteLocation + "; ./" + filename + " " + result_fileName);
			}

			((ChannelExec) channel).setErrStream(System.err);

			channel.connect();

			while (true) {
				if (channel.isClosed()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
					System.out.println(ee);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} finally {
			channel.disconnect();
		}
	}

	public void downloadFile(String remoteLocation, String localLocation, String result_fileName) {
		
		remoteLocation = remoteLocation + "/";
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		try {
			channel = scm.getSession().openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			String cdDir = remoteLocation;
			sftpChannel.cd(cdDir);

			File file = new File(remoteLocation + result_fileName);

			bis = new BufferedInputStream(sftpChannel.get(file.getName()));

			File newFile = new File(localLocation + "\\" + file.getName());

			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bis.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftpChannel.disconnect();
			channel.disconnect();
		}
	}

}

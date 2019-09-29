import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

public class DownloadResult {
	private String localLocation;
	private String remoteLocation;
	private String fileName;

	public DownloadResult(String localLocation, String remoteLocation, String fileName) {
		this.localLocation = localLocation;
		this.remoteLocation = remoteLocation + "/";
		this.fileName = fileName;
	}

	public void downloads(Session session) {
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		Channel channel = null;
		ChannelSftp sftpChannel = null;
		try {
			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			// Change to output directory
			String cdDir = remoteLocation;
			sftpChannel.cd(cdDir);

			File file = new File(remoteLocation + fileName);

			bis = new BufferedInputStream(sftpChannel.get(file.getName()));

			File newFile = new File(localLocation + "\\" + file.getName());
	
			// Download file
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bis.close();
			bos.close();

			System.out.println("[!] Successfully downloaded file from " + remoteLocation + " to " + localLocation);
			System.out.println("[!] File Name : " + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftpChannel.disconnect();
			channel.disconnect();
		}
	}
}

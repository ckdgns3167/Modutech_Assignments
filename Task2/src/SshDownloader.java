import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

public class SshDownloader {

	private Channel channel = null;
	private ChannelSftp sftpChannel = null;

	private SshConnector sshconnector = null;

	public SshDownloader(AccessTarget target) {
		this.sshconnector = new SshConnector(target);
		this.sshconnector.connect();
	}

	public void download(String localLocation, Object absoluteAddressFileName) throws IOException {
		// 파일의 절대 주소를 파일 이름과 상위 디렉터리 주소를 구분 짓기 위한 코드
		String[] token = absoluteAddressFileName.toString().split("/");
		String remoteLocation = "";
		String fileName = "";
		for (int i = 0; i < token.length; i++) {
			if (i == token.length - 1) {
				fileName = token[i];
				break;
			}
			remoteLocation += token[i] + "/";
		}

		byte[] buffer = new byte[1024];

		OutputStream os = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		// 파일 전송용 channel인 sftp를 open하고
		// local로 다운로드 하길 원하는 파일의 이름과 그 파일의 상위 디렉터리 주소가 유효한지 검사 후 통과되면 파일 다운로드
		try {
			channel = sshconnector.getSession().openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			String cdDir = remoteLocation;
			sftpChannel.cd(cdDir);

			File file = new File(remoteLocation + fileName);

			bis = new BufferedInputStream(sftpChannel.get(file.getName()));
			File localPath = new File(localLocation);
			if (localPath.isDirectory()) { // 다운로드 예외 상황 처리
				File newFile = new File(localLocation + "\\" + file.getName());

				os = new FileOutputStream(newFile);
				bos = new BufferedOutputStream(os);
				int readCount;
				while ((readCount = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, readCount);
				}
				System.out.println("다운로드 성공!");
				System.out.println("위치 : " + localLocation + "\\" + file.getName());
			} else {
				System.out.println("다운로드 실패!");
				System.out.println("이유 : " + localLocation + "가 존재하지 않는 디렉터리이다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sftpChannel != null) {
				sftpChannel.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (bos != null) {
				bos.close();
			}
			if (os != null) {
				os.close();
			}
			if (bis != null) {
				bis.close();
			}
		}
	}

}

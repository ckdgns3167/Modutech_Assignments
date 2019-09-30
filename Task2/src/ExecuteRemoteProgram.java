import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

public class ExecuteRemoteProgram {// 원격 서버에 SSH로 연결된 상태에서 원격 프로그램을 실행시킨다.
	private String remoteLocation;
	private String filename;
	private String result_fileName;

	public ExecuteRemoteProgram(String remoteLocation, String filename, String result_fileName) {
		this.remoteLocation = remoteLocation;
		this.filename = filename;
		this.result_fileName = result_fileName;
	}

	public void execute(Session session) {
		String executableFile = remoteLocation + '/' + filename;
		System.out.println("[!] The address of the file to be executed : " + executableFile);
		Channel channel = null;

		try {
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand("cd " + remoteLocation + "; ./" + filename + " test.txt " + result_fileName);
			System.out.println("[!] 실행할 명령어 : cd " + remoteLocation + "; ./" + filename + " test.txt " + result_fileName);
			((ChannelExec) channel).setErrStream(System.err);
			channel.connect();

			while (true) {
				if (channel.isClosed()) {
					if (channel.getExitStatus() == 0) { // 파일 정상 실행 완료
						System.out.println("[!] Program execution completed.");
					} else {
						System.out.println("[!] Program execution failed.");
					}
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
}

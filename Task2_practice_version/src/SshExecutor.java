import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class SshExecutor implements interfaces.Executor {

	private Channel channel = null;

	private SshConnector sshconnector = null;

	public SshExecutor(AccessTarget target) {
		this.sshconnector = new SshConnector(target);
		this.sshconnector.connect();
	}

	@Override
	public boolean run(String absoluteAddressFileName, String... required_file_names) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean run(String absoluteAddressFileName, Object result_fileName, String... required_file_names) {
		// 파일의 절대 주소를 파일 이름과 상위 디렉터리 주소를 구분 짓기 위한 코드
		String[] token = absoluteAddressFileName.split("/");
		String remoteLocation = "";
		String fileName = "";
		for (int i = 0; i < token.length; i++) {
			if (i == token.length - 1) {
				fileName = token[i];
				break;
			}
			remoteLocation += token[i] + "/";
		}
		// 파일 실행에 필요한 파일들을 받기 위한 코드.
		String requiredFiles = " ";
		for (String fn : required_file_names) {
			requiredFiles += fn + " ";
		}
		// 커맨드 실행용 channel인 exec채널을 open하고 파일 실행 커맨드 문자열을 구성해준 뒤 그것을 set하여 파일을 실행시키게 됨.
		try {
			channel = sshconnector.getSession().openChannel("exec");
			if (!requiredFiles.equals(" ")) {

				((ChannelExec) channel).setCommand(
						"cd " + remoteLocation + "; ./" + fileName + requiredFiles + result_fileName.toString());
			} else {
				((ChannelExec) channel)
						.setCommand("cd " + remoteLocation + "; ./" + fileName + " " + result_fileName.toString());
			}

			((ChannelExec) channel).setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			channel.connect();

			while (true) {
				if (channel.isClosed()) {
					if (((ChannelExec) channel).getExitStatus() != 0) {
						System.out.println("파일실행 실패!");
						System.out.println("다시 시도해주세요.");
						return false;
					} else {
						System.out.println("파일실행 완료!");
						return true;
					}
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
			if (channel != null) {
				channel.disconnect();
			}
		}
		return false;
	}

}

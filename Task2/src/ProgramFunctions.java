import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ProgramFunctions implements SSHRemoteAccessSystem {
	// 연결 시도 최대 제한 시간 설정
	private static final int S_CONNECTION_TIMEOUT = 30000;

	private JSch jsch = null;
	private Session session = null;
	private AccessTarget target = null;
	private Channel channel = null;
	private ChannelSftp sftpChannel = null;

	public ProgramFunctions(AccessTarget target) {
		this.target = target;
		this.connect();
	}

	public void connect() {
		try {
			jsch = new JSch();
			session = jsch.getSession(target.getUser(), target.getTarget(), target.getPort());
			session.setPassword(target.getPass());
			session.setConfig("StrictHostKeyChecking", "no");
			session.setDaemonThread(false);
			/*
			 * config 설정 : ssh_config 에 호스트 키가 없더라도 바로 접속이 되도록 설정, 우분투의 경우
			 * /etc/ssh/ssh_config 에 아래 설정을 추가됨. 해주는 이유 : ssh 로 리모트 서버에 접속시 호스트 키가
			 * ~/.ssh/known_hosts 파일에 없을경우 추가할지를 물어본다. 보통 때는 문제 될 것이 없지만 배치 작업등을 할 경우는 일일이
			 * 호스트 키를 추가했는지 확인하기 때문에 귀찮다.
			 */
			session.connect(S_CONNECTION_TIMEOUT); // 시간 내에 접속을 안하면 연결 요청 취소
		} catch (JSchException e) {
			System.out.println(e.toString());
		}
	}

	public void finishProgram() {
		if (session != null) {
			session.disconnect();
		}
		session = null;
	}
	
	@Override
	public boolean executeFile_1(String absoluteAddressFileName, String... required_file_names) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean executeFile_2(String absoluteAddressFileName, String result_fileName,
			String... required_file_names) {
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
			channel = this.session.openChannel("exec");
			if (!requiredFiles.equals(" ")) {
				((ChannelExec) channel)
						.setCommand("cd " + remoteLocation + "; ./" + fileName + requiredFiles + result_fileName);
			} else {
				((ChannelExec) channel).setCommand("cd " + remoteLocation + "; ./" + fileName + " " + result_fileName);
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

	@Override
	public void downloadFileToLocal(String localLocation, String absoluteAddressFileName) throws IOException {
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

		byte[] buffer = new byte[1024];

		OutputStream os = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		// 파일 전송용 channel인 sftp를 open하고
		// local로 다운로드 하길 원하는 파일의 이름과 그 파일의 상위 디렉터리 주소가 유효한지 검사 후 통과되면 파일 다운로드
		try {
			channel = this.session.openChannel("sftp");
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

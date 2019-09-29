import com.jcraft.jsch.*;

public class SSHConnector {

	// 연결 시도 최대 제한 시간 설정
	private static final int S_CONNECTION_TIMEOUT = 30000;

	private JSch jsch = null;
	private Session session = null;

	public Session getSession(AccessTarget target) {
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
		return session;
	}

}

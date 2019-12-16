import java.io.IOException;

public class Main {

	// 사용자로부터 받아야할 정보들...
	private static final String IP = "192.168.1.126";
	private static final String USER = "ubuntu";
	private static final String PASS = "test";
	private static final int PORT = 22;

	private static final String E_FILENAME = "/home/ubuntu/Downloads/txt_2_csv.sh";// 실행시킬 파일 이름
	private static final String LOCALFILEPATH = "C:\\Users\\modutech\\Desktop";// 다운로드할 장소
	private static final Object RESULT_FILENAME = "/home/ubuntu/Downloads/test.csv";// 다운로드할 파일 이름

	public static void main(String[] args) throws IOException {
		AccessTarget target = new AccessTarget(IP, USER, PASS, PORT);
		SshExecutor sshExecutor = new SshExecutor(target);
		SshDownloader sshDownloader = new SshDownloader(target);

		boolean isFileExecuted = sshExecutor.run(E_FILENAME, RESULT_FILENAME, "test.txt");

		if (isFileExecuted) {
			sshDownloader.download(LOCALFILEPATH, RESULT_FILENAME);
		}

	}
}

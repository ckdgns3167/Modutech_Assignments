public class Main {

	// 사용자로부터 받아야할 정보들...
	private static final String IP = "192.168.1.126";
	private static final String USER = "ubuntu";
	private static final String PASS = "test";
	private static final int PORT = 22;

	private static final String REMOTEFILEPATH = "/home/ubuntu/Downloads";// 실행시킬 파일 위치
	private static final String E_FILENAME = "txt_2_csv.sh";// 실행시킬 파일 이름
	private static final String LOCALFILEPATH = "C:\\Users\\82109\\Desktop";// 다운로드할 장소
	private static final String RESULT_FILENAME = "test.csv";// 다운로드할 파일 이름

	public static void main(String[] args) {
		SSH_Connection_Manager SSHcm = new SSH_Connection_Manager(new AccessTarget(IP, USER, PASS, PORT));
		RemoteFileManager remoteFileManager = new RemoteFileManager(SSHcm);

		// 마지막 파라미터는 없어도 되고 여러개 여도 됨. 파일을 실행시키기 위해 필요한 다른 파일들을 넣어주는 곳.
		remoteFileManager.executeFile(REMOTEFILEPATH, E_FILENAME, RESULT_FILENAME, "test.txt"); 
		remoteFileManager.downloadFile(REMOTEFILEPATH, LOCALFILEPATH, RESULT_FILENAME);

		SSHcm.finishProgram();// 사용자가 종료 버튼 누르는 행위
	}
}

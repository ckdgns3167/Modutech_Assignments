import com.jcraft.jsch.Session;

public class AppController {

	// 사용자로부터 받아야할 정보들...
	private static final String IP = "192.168.1.126";
	private static final String USER = "ubuntu";
	private static final String PASS = "test";
	private static final int PORT = 22;
	private static final String REMOTEFILEPATH = "/home/ubuntu/Downloads";// 실행시킬 파일 위치
	private static final String E_FILENAME = "txt_2_csv.sh";// 실행시킬 파일 이름
	private static final String LOCALFILEPATH = "C:\\Users\\82109\\Desktop";// 다운로드할 장소
	private static final String D_FILENAME = "test.csv";// 다운로드할 파일 이름

	private Session session = null;
	private ConsolePrinter printer;
	private SSHConnector sc;
	private AccessTarget target;
	private ExecuteRemoteProgram erp;
	private DownloadResult dr;

	public AppController() {
		this.printer = new ConsolePrinter();
		this.sc = new SSHConnector();
		this.target = new AccessTarget(IP, USER, PASS, PORT);
		this.erp = new ExecuteRemoteProgram(REMOTEFILEPATH, E_FILENAME);
		this.dr = new DownloadResult(LOCALFILEPATH, REMOTEFILEPATH, D_FILENAME);
	}

	public void run() {

		this.printer.printIntro();
		this.printer.printTargetInfo(target);
		this.session = printer.loading(sc, target);

		System.out.println("-------------원격으로 프로그램 실행-------------");
		erp.execute(session);
		System.out.println("------------프로그램 로컬로 다운로드-------------");
		/*
		 * 실행 완료됐다고 REST API로 알린 후 다운로드
		 */

		dr.downloads(session);

		this.printer.exitMessage(target);
		this.session.disconnect();
	}
}

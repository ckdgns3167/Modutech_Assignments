import java.io.IOException;

public class Main {

	// 사용자로부터 받아야할 정보들...
	private static final String IP = "192.168.1.126";
	private static final String USER = "ubuntu";
	private static final String PASS = "test";
	private static final int PORT = 22;

	private static final String E_FILENAME = "/home/ubuntu/Downloads/txt_2_csv.sh";// 실행시킬 파일 이름
	private static final String LOCALFILEPATH = "C:\\Users\\82109\\Desktop";// 다운로드할 장소
	private static final String RESULT_FILENAME = "/home/ubuntu/Downloads/test.csv";// 다운로드할 파일 이름

	public static void main(String[] args) throws IOException {
		ProgramFunctions func = new ProgramFunctions(new AccessTarget(IP, USER, PASS, PORT));
		
		boolean isFileExecuted = func.executeFile_2(E_FILENAME, RESULT_FILENAME, "test.txt");
		if(isFileExecuted) {
			func.downloadFileToLocal(LOCALFILEPATH, RESULT_FILENAME);
		}

		func.finishProgram();
	}
}

import java.io.IOException;

public interface SSHRemoteAccessSystem {
	
	public void connect();
	public void finishProgram();
	
	// 원격으로 파일을 커맨드로 실행하는 메서드 
	// -> 실행 후 결과 파일을 생성하는 파일이거나 아닐 수 있기 때문에 두개로 구분. 
	public boolean executeFile_1(String absoluteAddressFileName, String... required_file_names);
	public boolean executeFile_2(String absoluteAddressFileName, String result_fileName, String... required_file_names);

	// 원격에서 로컬로 파일 복사
	public void downloadFileToLocal(String localLocation, String absoluteAddressFileName) throws IOException;

	// 로컬에서 원격으로 파일 복사 - 아직 미구현
//	public void downloadFileToRemote();

}

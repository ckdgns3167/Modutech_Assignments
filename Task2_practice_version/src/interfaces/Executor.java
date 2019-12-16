package interfaces;

public interface Executor {
	// 원격으로 파일을 커맨드로 실행하는 메서드
	// -> 실행 후 결과 파일을 생성하는 파일이거나 아닐 수 있기 때문에 두개로 구분.
	public boolean run(String absoluteAddressFileName, String... required_file_names);

	public boolean run(String absoluteAddressFileName, Object result_fileName, String... required_file_names);
}

package interfaces;

import java.io.IOException;

public interface Downloader {
	// 원격에서 로컬로 파일 복사
	public void download(String localLocation, Object absoluteAddressFileName) throws IOException;

}

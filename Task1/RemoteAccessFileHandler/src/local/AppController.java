package local;

import com.jcraft.jsch.Session;

public class AppController { // controller

    private AppView view = null;
    private RemoteAccessor ra = null;
    public void run() {

        view = new AppView(); // view
        ra = new RemoteAccessor(); // model
        view.programIntro();
        view.EnterRemoteSshData(ra);

        Session session = ra.ConnectToRemoteComputer();

        while (true && session.isConnected()) {
            int userSelectNum = view.selectProgramFunction();
            switch (userSelectNum) {
                case 1: // shell
                    ra.getRemoteShellPrompt(session);
                    break;
                case 2: // upload
                    ra.upload(view.fileUploadMessage());
                    break;
                case 3: // execute and download : 원격에 있는 java 실행 후 결과로 나온 파일을 다운받는 기능.

                    break;
                case 4: // exit
                    view.exitMessage();
                    ra.disconnect();
                    System.exit(0);
                    break;
                default: // wrong input alert message
                    view.wrongInputAlertMessage();
            }
        }
    }
}

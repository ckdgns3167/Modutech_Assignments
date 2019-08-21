package local;

import com.jcraft.jsch.Session;

public class AppController { // controller

    private AppView view = null;
    private RemoteAccessor ra = null;

    public AppController(){
        this.view = new AppView(); // view
        this.ra = new RemoteAccessor(); // model
    }

    public void run() {

        view.intro();
        view.EnterSSHData(ra);

        Session session = ra.getSession();

        while (true && session.isConnected()) {
            int userSelectNum = view.selectProgramFunction();
            switch (userSelectNum) {
                case 1: // use shell
                    ra.openShell(session);
                    break;
                case 2: // use sFTP
                    ra.openSftp(session);
                    break;
                case 3: // program exit
                    ra.disconnect();
                    view.exitMessage(ra);
                    System.exit(0);
                    break;
                default: // wrong input alert message
                    view.wrongInputAlertMessage();
            }
        }
    }
}

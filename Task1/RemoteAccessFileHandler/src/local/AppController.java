package local;

import com.jcraft.jsch.Session;

public class AppController { // controller
    private Session session = null;
    private AppView view;
    private RemoteAccessor ra;

    public AppController() {
        this.view = new AppView(); // view
        this.ra = new RemoteAccessor(); // model
    }

    public void run() {

        this.view.intro();
        this.view.EnterSSHData(ra);
        this.session = view.loading(ra);

        String userInput;
        int selectedNumber = -1;
        while (true && session.isConnected()) {
            userInput = view.selectProgramFunction();
            if (view.isStringDouble(userInput))
                selectedNumber = Integer.parseInt(userInput);
            switch (selectedNumber) {
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
                default:
                    view.wrongInputAlertMessage();
            }
        }
    }
}

package local;

public class AppController {

    private AppView view = null;
    private RemoteAccessor jrsa = null;

    public void run() {

        view = new AppView();
        jrsa = new RemoteAccessor();

        view.programIntro();
        view.EnterRemoteSshData(jrsa);

        jrsa.ConnectToRemoteComputer();
    }
}

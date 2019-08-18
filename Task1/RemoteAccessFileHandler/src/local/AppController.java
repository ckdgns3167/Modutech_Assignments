package local;

public class AppController {

    private AppView view = null;
    private RemoteAccessor ra = null;

    public void run() {

        view = new AppView();
        ra = new RemoteAccessor();

        view.programIntro();
        view.EnterRemoteSshData(ra);

        ra.ConnectToRemoteComputer();
    }
}

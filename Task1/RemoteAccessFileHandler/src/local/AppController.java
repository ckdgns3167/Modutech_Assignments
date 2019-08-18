package local;

public class AppController { // controller

    private AppView view = null;
    private RemoteAccessor ra = null;

    public void run() {

        view = new AppView(); // view
        ra = new RemoteAccessor(); // model

        view.programIntro();
        view.EnterRemoteSshData(ra);

        ra.ConnectToRemoteComputer();
    }
}

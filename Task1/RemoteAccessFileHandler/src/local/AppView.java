package local;

import java.util.Scanner;

public class AppView {

    public Scanner scan;

    public AppView() {
        this.scan = new Scanner(System.in);
    }

    public void programIntro() {
        System.out.println("/.............................................................\\");
        System.out.println("|..............START REMOTE SHELL ACCESS PROGRAM..............|");
        System.out.println("\\............................................................./\n");
    }

    public void EnterRemoteSshData(RemoteAccessor ra) {
        System.out.println(">>>>>>>>>>> Enter Target's SSH Access Info below... <<<<<<<<<<<");
        System.out.println("---------------------------------------------------------------");
        System.out.print("  IP : ");//"192.168.1.126"
        ra.target.setTarget(scan.next());
        System.out.print("user : ");//ubuntu
        ra.target.setUser(scan.next());
        System.out.print("pass : ");//test
        ra.target.setPass(scan.next());
        System.out.print("port : ");//22
        ra.target.setPort(scan.nextInt());
        System.out.println("---------------------------------------------------------------");
    }

    public int selectProgramFunction() {
        System.out.println("[!] Choose the function you want.");
        System.out.print("[1] Connect shell [2] Upload file [3] Download file [4] Exit : ");

        int num = scan.nextInt();

        return num;
    }

    public String[] fileUploadMessage() {
        System.out.println("[!] Enter the location of the file to upload and the location to save");
        System.out.print(" · Local address : ");
        String local = scan.next();
        System.out.print(" · Remote address : ");
        String remote = scan.next();
        return new String[]{local, remote};
    }

    public void exitMessage(){
        System.out.println("[!] The connection has been terminated. Thank you!");
    }

    public void wrongInputAlertMessage(){
        System.out.println("[!] Incorrect input. Please re-enter. 1 ~ 4");
        System.out.println("---------------------------------------------------------------");
    }
}

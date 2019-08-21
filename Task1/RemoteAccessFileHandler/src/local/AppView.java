package local;

import java.util.Scanner;

public class AppView {

    public Scanner scan;

    public AppView() {
        this.scan = new Scanner(System.in);
    }

    public void intro() {
        System.out.println(" /..........................................\\");
        System.out.println("|.........START REMOTE ACCESS PROGRAM........|");
        System.out.println(" \\........................................../\n");
    }

    public void EnterSSHData(RemoteAccessor ra) {
        System.out.println(">>> Enter Target's SSH Access Info below... <<<");
        System.out.println("-----------------------------------------------");
//        System.out.print(" ·   IP : ");//"192.168.1.126"
//        ra.target.setTarget(scan.next());
        System.out.println(" ·   IP : 192.168.1.118");
        ra.target.setTarget("192.168.1.118");
//        System.out.print(" · USER : ");//ubuntu
//        ra.target.setUser(scan.next());
        System.out.println(" · USER : ubuntu");
        ra.target.setUser("ubuntu");
//        System.out.print(" · PASS : ");//test
//        ra.target.setPass(scan.next());
        System.out.println(" · PASS : test");
        ra.target.setPass("test");
//        System.out.print(" · PORT : ");//22
//        ra.target.setPort(scan.nextInt());
        System.out.println(" · PORT : 22");
        ra.target.setPort(22);
        System.out.println("-----------------------------------------------");
    }

    public int selectProgramFunction() {
        System.out.println("[!] Choose the function you want.");
        System.out.print("[1] Connect Shell [2] Connect sFTP [3] Exit : ");
        return scan.nextInt();
    }

    public void exitMessage(RemoteAccessor ra){
        System.out.println("[!] Connection has been terminated from " + ra.target.getTarget()+"Thank you!");
    }

    public void wrongInputAlertMessage(){
        System.out.println("[!] Incorrect input. Please re-enter. 1 ~ 4");
        System.out.println("-----------------------------------------------");
    }
}

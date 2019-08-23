package local;

import com.jcraft.jsch.Session;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppView {

    public Scanner scan;

    public AppView() {
        this.scan = new Scanner(System.in);
    }

    public void intro() {
        System.out.println(" //..........................................\\\\");
        System.out.println("||.........START REMOTE ACCESS PROGRAM........||");
        System.out.println(" \\\\..........................................//\n");
    }

    public void EnterSSHData(RemoteAccessor ra) {
        System.out.println(">>> Enter Target's SSH Access Info below... <<<");
        System.out.println("-----------------------------------------------");
//        System.out.print(" ·   IP : ");//"192.168.1.126"
//        ra.target.setTarget(scan.next());
        System.out.println(" ·   IP : 192.168.1.126");
        ra.target.setTarget("192.168.1.126");
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

    public Session loading(RemoteAccessor ra) {
        System.out.println("[!] Session connecting...Please wait.");
        Session session = ra.getSession();
        if (session.isConnected())
            System.out.println("[!] Session connection succeeded with " + ra.target.getTarget() + " Play!!");
        else
            System.out.println();
        return session;
    }

    public String selectProgramFunction() {
        System.out.println("[!] Choose the function you want.");
        System.out.println("-----------------------------------------------");
        System.out.print("--> [1] Connect Shell [2] Connect sFTP [3] Exit : ");
        return scan.next();
    }

    public void exitMessage(RemoteAccessor ra) {
        System.out.println("[!] Connection has been terminated from " + ra.target.getTarget() + "\n[!] Thank you!");
    }

    public void wrongInputAlertMessage() {
        System.out.println("[!] Incorrect input. Please re-enter. 1 ~ 3");
    }

    public boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

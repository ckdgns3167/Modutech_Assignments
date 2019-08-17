package local;

import java.util.Scanner;

public class AppView {

    public Scanner scan = null;

    public AppView(){
        this.scan = new Scanner(System.in);
    }

    public void programIntro(){
        System.out.println("/.............................................................\\");
        System.out.println("|..............START REMOTE SHELL ACCESS PROGRAM..............|");
        System.out.println("\\............................................................./\n");
    }

    public void EnterRemoteSshData(RemoteAccessor jrsa){
        System.out.println(">>>>>>>>>>> Enter Target's SSH Access Info below...<<<<<<<<<<<");
        System.out.println("--------------------------------------------------------------");
        System.out.print("  IP : ");//"192.168.1.126"
        jrsa.target.setTarget(scan.next());
        System.out.print("user : ");//ubuntu
        jrsa.target.setUser(scan.next());
        System.out.print("pass : ");//test
        jrsa.target.setPass(scan.next());
        System.out.print("port : ");//22
        jrsa.target.setPort(scan.nextInt());
        System.out.println("--------------------------------------------------------------");
    }
}

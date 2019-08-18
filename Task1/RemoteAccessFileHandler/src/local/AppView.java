package local;

import java.util.Scanner;

public class AppView {

    public Scanner scan = null;

    public AppView(){
        this.scan = new Scanner(System.in);
    }

    public void programIntro(){
        System.out.println("/............................................ㅁㄴㅇ.................\\");
        System.out.println("|..............START REMOTE SHELL ACCESS PROGRAM..............|");
        System.out.println("\\............................................................./\n");
    }

    public void EnterRemoteSshData(RemoteAccessor ra){
        System.out.println(">>>>>>>>>>> Enter Target's SSH Access Info below...<<<<<<<<<<<");
        System.out.println("--------------------------------------------------------------");
        System.out.print("  IP : ");//"192.168.1.126"
        ra.target.setTarget(scan.next());
        System.out.print("user : ");//ubuntu
        ra.target.setUser(scan.next());
        System.out.print("pass : ");//test
        ra.target.setPass(scan.next());
        System.out.print("port : ");//22
        ra.target.setPort(scan.nextInt());
        System.out.println("--------------------------------------------------------------");
    }
}

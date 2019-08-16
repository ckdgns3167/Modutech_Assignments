package local;

import com.jcraft.jsch.JSchException;

import java.util.Scanner;

public class AppController {

    private static Scanner scan = null;

    private static boolean isRunning = true;

    public static void main(String[] args) {

        scan = new Scanner(System.in);
        System.out.println("/.......................................................\\");
        System.out.println("|...........Start Remote Shell Access Program...........|");
        System.out.println("\\......................................................./\n");

        JRemoteSecureAccessor jrsa = new JRemoteSecureAccessor();

        System.out.println("> Enter Target's SSH Access Info below...");
        System.out.println("-----------------------------------------");
        System.out.print("  IP : ");//"192.168.1.126"
        jrsa.target.setTarget(scan.next());
        System.out.print("user : ");//ubuntu
        jrsa.target.setUser(scan.next());
        System.out.print("pass : ");//test
        jrsa.target.setPass(scan.next());
        System.out.print("port : ");//22
        jrsa.target.setPort(scan.nextInt());
        System.out.println("-----------------------------------------");

        jrsa.ConnectToRemoteComputer();
    }
}

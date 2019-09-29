import com.jcraft.jsch.Session;

public class ConsolePrinter {
	public void printIntro() {
		System.out.println(" //..........................................\\\\");
		System.out.println("||.........START REMOTE ACCESS PROGRAM........||");
		System.out.println(" \\\\..........................................//\n");
	}

	public void printTargetInfo(AccessTarget at) {
		System.out.println(">>>        Target's SSH Access Info...       <<<");
		System.out.println("------------------------------------------------");

		System.out.println(" ·   IP : " + at.getTarget());
		System.out.println(" · USER : " + at.getUser());
		System.out.println(" · PASS : " + at.getPass());
		System.out.println(" · PORT : " + at.getPort());

		System.out.println("------------------------------------------------");
	}

	public Session loading(SSHConnector sc, AccessTarget at) {
		System.out.println("[!] Please wait. Session connecting...");
		Session session = sc.getSession(at);
		if (session.isConnected())
			System.out.println("[!] Session connection succeeded with " + at.getTarget() + "!");
		else
			System.out.println();
		return session;
	}

	public void exitMessage(AccessTarget at) {
		System.out.println("[!] Connection has been terminated from " + at.getTarget() + "!\n[!] Thank you! bye~");
	}
}

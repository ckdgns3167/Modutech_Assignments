package local;

import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class RemoteAccessor {
    private static final int S_CONNECTION_TIMEOUT = 30000;
    private static final int C_CONNECTION_TIMEOUT = 3000;
    private static final int BUFFER_SIZE = 1024;

    private JSch jsch = null;
    private Channel channel = null; // The abstract base class for the different types of channel which may be associated with a Session.
    private Session session = null; // A Session represents a connection to a SSH server.
    private ChannelSftp channelSftp = null;

    public AccessTarget target = new AccessTarget(); // 접속 대상, 즉 ssh로 연결될 원격 컴퓨터의 정보를 추상화한 클래스. 이에 대한 인스턴스를 생성.

    // 원격으로 컴퓨터에 접속하고 shell prompt를 사용하게 해줌.
    public Session getSession() {
        try {
            jsch = new JSch();
            session = jsch.getSession(target.getUser(), target.getTarget(), target.getPort());
            session.setPassword(target.getPass());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setDaemonThread(false);
            /*
                config 설정 : ssh_config 에 호스트 키가 없더라도 바로 접속이 되도록 설정, 우분투의 경우 /etc/ssh/ssh_config 에 아래 설정을 추가됨.
                해주는 이유 : ssh 로 리모트 서버에 접속시 호스트 키가 ~/.ssh/known_hosts 파일에 없을경우 추가할지를 물어본다.
                보통 때는 문제 될 것이 없지만 배치 작업등을 할 경우는 일일이 호스트 키를 추가했는지 확인하기 때문에 귀찮다.
             */
            session.connect(S_CONNECTION_TIMEOUT); // 시간 내에 접속을 안하면 연결 요청 취소
        } catch (JSchException e) {
            System.out.println(e.toString());
        }
        return session;
    }

    public void openShell(Session session) {
        System.out.println("[!] Use the remote Shell as much as you want.");
        InputStream inputStream = null;
        try {
            channel = session.openChannel("shell");
            channel.setInputStream(System.in, true);
            channel.connect(C_CONNECTION_TIMEOUT);
            inputStream = channel.getInputStream(); // <- 일반 출력 스트림
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(buffer, 0, BUFFER_SIZE);
                    if (i < 0) {
                        break;
                    }
                    String output = new String(buffer, 0, i);
                    if (!output.contains(": ")) {
                        output = output.substring(output.indexOf("\n") + 1);
                    } else if (output.contains("-bash"))
                        output = output.substring(output.indexOf("\n") + 1);
                    System.out.print(output);
                }
                if (channel.isClosed()) {
                    if (inputStream.available() > 0) {
                        continue;
                    }
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (JSchException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    public void openSftp(Session session) {
        try {
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp c = (ChannelSftp) channel;

            java.io.InputStream in = System.in;
            java.io.PrintStream out = System.out;

            java.util.Vector cmds = new java.util.Vector();
            byte[] buf = new byte[1024];
            int i;
            String str;
            int level = 0;

            while (true) {
                out.print(" · sftp> ");
                cmds.removeAllElements();
                i = in.read(buf, 0, 1024);
                if (i <= 0) break;

                i--;
                if (i > 0 && buf[i - 1] == 0x0d) i--;
                int s = 0;
                for (int ii = 0; ii < i; ii++) {
                    if (buf[ii] == ' ') {
                        if (ii - s > 0) {
                            cmds.addElement(new String(buf, s, ii - s));
                        }
                        while (ii < i) {
                            if (buf[ii] != ' ') break;
                            ii++;
                        }
                        s = ii;
                    }
                }
                if (s < i) {
                    cmds.addElement(new String(buf, s, i - s));
                }
                if (cmds.size() == 0) continue;

                String cmd = (String) cmds.elementAt(0);
                if (cmd.equals("quit")) {
                    break;
                }
                if (cmd.equals("exit")) {
                    break;
                }
                if (cmd.equals("rekey")) {
                    session.rekey();
                    continue;
                }
                if (cmd.equals("compression")) {
                    if (cmds.size() < 2) {
                        out.println("compression level: " + level);
                        continue;
                    }
                    try {
                        level = Integer.parseInt((String) cmds.elementAt(1));
                        if (level == 0) {
                            session.setConfig("compression.s2c", "none");
                            session.setConfig("compression.c2s", "none");
                        } else {
                            session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none");
                            session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none");
                        }
                    } catch (Exception e) {
                    }
                    session.rekey();
                    continue;
                }
                if (cmd.equals("cd") || cmd.equals("lcd")) {
                    if (cmds.size() < 2) continue;
                    String path = (String) cmds.elementAt(1);
                    try {
                        if (cmd.equals("cd")) c.cd(path);
                        else c.lcd(path);
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("rm") || cmd.equals("rmdir") || cmd.equals("mkdir")) {
                    if (cmds.size() < 2) continue;
                    String path = (String) cmds.elementAt(1);
                    try {
                        if (cmd.equals("rm")) c.rm(path);
                        else if (cmd.equals("rmdir")) c.rmdir(path);
                        else c.mkdir(path);
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("chgrp") || cmd.equals("chown") || cmd.equals("chmod")) {
                    if (cmds.size() != 3) continue;
                    String path = (String) cmds.elementAt(2);
                    int foo = 0;
                    if (cmd.equals("chmod")) {
                        byte[] bar = ((String) cmds.elementAt(1)).getBytes();
                        int k;
                        for (int j = 0; j < bar.length; j++) {
                            k = bar[j];
                            if (k < '0' || k > '7') {
                                foo = -1;
                                break;
                            }
                            foo <<= 3;
                            foo |= (k - '0');
                        }
                        if (foo == -1) continue;
                    } else {
                        try {
                            foo = Integer.parseInt((String) cmds.elementAt(1));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    try {
                        if (cmd.equals("chgrp")) {
                            c.chgrp(foo, path);
                        } else if (cmd.equals("chown")) {
                            c.chown(foo, path);
                        } else if (cmd.equals("chmod")) {
                            c.chmod(foo, path);
                        }
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("pwd") || cmd.equals("lpwd")) {
                    str = (cmd.equals("pwd") ? "Remote" : "Local");
                    str += " working directory: ";
                    if (cmd.equals("pwd")) str += c.pwd();
                    else str += c.lpwd();
                    out.println(str);
                    continue;
                }
                if (cmd.equals("ls") || cmd.equals("dir")) {
                    String path = ".";
                    if (cmds.size() == 2) path = (String) cmds.elementAt(1);
                    try {
                        java.util.Vector vv = c.ls(path);
                        if (vv != null) {
                            for (int ii = 0; ii < vv.size(); ii++) {
                                Object obj = vv.elementAt(ii);
                                if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                                    out.println(((com.jcraft.jsch.ChannelSftp.LsEntry) obj).getLongname());
                                }

                            }
                        }
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("lls") || cmd.equals("ldir")) {
                    String path = ".";
                    if (cmds.size() == 2) path = (String) cmds.elementAt(1);
                    try {
                        java.io.File file = new java.io.File(path);
                        if (!file.exists()) {
                            out.println(path + ": No such file or directory");
                            continue;
                        }
                        if (file.isDirectory()) {
                            String[] list = file.list();
                            for (int ii = 0; ii < list.length; ii++) {
                                out.println(list[ii]);
                            }
                            continue;
                        }
                        out.println(path);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    continue;
                }
                if (cmd.equals("get") ||
                        cmd.equals("get-resume") || cmd.equals("get-append") ||
                        cmd.equals("put") ||
                        cmd.equals("put-resume") || cmd.equals("put-append")
                ) {
                    if (cmds.size() != 2 && cmds.size() != 3) continue;
                    String p1 = (String) cmds.elementAt(1);
                    String p2 = ".";
                    if (cmds.size() == 3) p2 = (String) cmds.elementAt(2);
                    try {
                        SftpProgressMonitor monitor = new MyProgressMonitor();
                        if (cmd.startsWith("get")) {
                            int mode = ChannelSftp.OVERWRITE;
                            if (cmd.equals("get-resume")) {
                                mode = ChannelSftp.RESUME;
                            } else if (cmd.equals("get-append")) {
                                mode = ChannelSftp.APPEND;
                            }
                            c.get(p1, p2, monitor, mode);
                        } else {
                            int mode = ChannelSftp.OVERWRITE;
                            if (cmd.equals("put-resume")) {
                                mode = ChannelSftp.RESUME;
                            } else if (cmd.equals("put-append")) {
                                mode = ChannelSftp.APPEND;
                            }
                            c.put(p1, p2, monitor, mode);
                        }
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("ln") || cmd.equals("symlink") ||
                        cmd.equals("rename") || cmd.equals("hardlink")) {
                    if (cmds.size() != 3) continue;
                    String p1 = (String) cmds.elementAt(1);
                    String p2 = (String) cmds.elementAt(2);
                    try {
                        if (cmd.equals("hardlink")) {
                            c.hardlink(p1, p2);
                        } else if (cmd.equals("rename")) c.rename(p1, p2);
                        else c.symlink(p1, p2);
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("df")) {
                    if (cmds.size() > 2) continue;
                    String p1 = cmds.size() == 1 ? "." : (String) cmds.elementAt(1);
                    SftpStatVFS stat = c.statVFS(p1);

                    long size = stat.getSize();
                    long used = stat.getUsed();
                    long avail = stat.getAvailForNonRoot();
                    long root_avail = stat.getAvail();
                    long capacity = stat.getCapacity();

                    System.out.println("Size: " + size);
                    System.out.println("Used: " + used);
                    System.out.println("Avail: " + avail);
                    System.out.println("(root): " + root_avail);
                    System.out.println("%Capacity: " + capacity);

                    continue;
                }
                if (cmd.equals("stat") || cmd.equals("lstat")) {
                    if (cmds.size() != 2) continue;
                    String p1 = (String) cmds.elementAt(1);
                    SftpATTRS attrs = null;
                    try {
                        if (cmd.equals("stat")) attrs = c.stat(p1);
                        else attrs = c.lstat(p1);
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    if (attrs != null) {
                        out.println(attrs);
                    } else {
                    }
                    continue;
                }
                if (cmd.equals("readlink")) {
                    if (cmds.size() != 2) continue;
                    String p1 = (String) cmds.elementAt(1);
                    String filename = null;
                    try {
                        filename = c.readlink(p1);
                        out.println(filename);
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("realpath")) {
                    if (cmds.size() != 2) continue;
                    String p1 = (String) cmds.elementAt(1);
                    String filename = null;
                    try {
                        filename = c.realpath(p1);
                        out.println(filename);
                    } catch (SftpException e) {
                        System.out.println(e.toString());
                    }
                    continue;
                }
                if (cmd.equals("version")) {
                    out.println("SFTP protocol version " + c.version());
                    continue;
                }
                if (cmd.equals("help") || cmd.equals("?")) {
                    out.println(help);
                    continue;
                }
                out.println("unimplemented command: " + cmd);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static class MyProgressMonitor implements SftpProgressMonitor {
        ProgressMonitor monitor;
        long count = 0;
        long max = 0;

        public void init(int op, String src, String dest, long max) {
            this.max = max;
            monitor = new ProgressMonitor(null,
                    ((op == SftpProgressMonitor.PUT) ?
                            "put" : "get") + ": " + src,
                    "", 0, (int) max);
            count = 0;
            percent = -1;
            monitor.setProgress((int) this.count);
            monitor.setMillisToDecideToPopup(1000);
        }

        private long percent = -1;

        public boolean count(long count) {
            this.count += count;

            if (percent >= this.count * 100 / max) {
                return true;
            }
            percent = this.count * 100 / max;

            monitor.setNote("Completed " + this.count + "(" + percent + "%) out of " + max + ".");
            monitor.setProgress((int) this.count);

            return !(monitor.isCanceled());
        }

        public void end() {
            monitor.close();
        }
    }

    public void disconnect() {
        if (session.isConnected()) {
            session.disconnect();
        }
        if (channel != null) {
            if (channel.isConnected()) {
                channelSftp.quit();
                channel.disconnect();
            }
        }
    }

    class AccessTarget {
        String target = null;//접속 대상 IP
        String user = null;
        String pass = null;
        Integer port = null;

        public String getTarget() {
            return target;
        }

        public void setTarget(String host) {
            this.target = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String userName) {
            this.user = userName;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
    }

    private static String help =
            "      Available commands:\n" +
                    "      * means unimplemented command.\n" +
                    "cd path                       Change remote directory to 'path'\n" +
                    "lcd path                      Change local directory to 'path'\n" +
                    "chgrp grp path                Change group of file 'path' to 'grp'\n" +
                    "chmod mode path               Change permissions of file 'path' to 'mode'\n" +
                    "chown own path                Change owner of file 'path' to 'own'\n" +
                    "df [path]                     Display statistics for current directory or filesystem containing 'path'\n" +
                    "help                          Display this help text\n" +
                    "get remote-path [local-path]  Download file\n" +
                    "get-resume remote-path [local-path]  Resume to download file.\n" +
                    "get-append remote-path [local-path]  Append remote file to local file\n" +
                    "hardlink oldpath newpath      Hardlink remote file\n" +
                    "*lls [ls-options [path]]      Display local directory listing\n" +
                    "ln oldpath newpath            Symlink remote file\n" +
                    "*lmkdir path                  Create local directory\n" +
                    "lpwd                          Print local working directory\n" +
                    "ls [path]                     Display remote directory listing\n" +
                    "*lumask umask                 Set local umask to 'umask'\n" +
                    "mkdir path                    Create remote directory\n" +
                    "put local-path [remote-path]  Upload file\n" +
                    "put-resume local-path [remote-path]  Resume to upload file\n" +
                    "put-append local-path [remote-path]  Append local file to remote file.\n" +
                    "pwd                           Display remote working directory\n" +
                    "stat path                     Display info about path\n" +
                    "exit                          Exit sftp\n" +
                    "quit                          Quit sftp\n" +
                    "rename oldpath newpath        Rename remote file\n" +
                    "rmdir path                    Remove remote directory\n" +
                    "rm path                       Delete remote file\n" +
                    "symlink oldpath newpath       Symlink remote file\n" +
                    "readlink path                 Check the target of a symbolic link\n" +
                    "realpath path                 Canonicalize the path\n" +
                    "rekey                         Key re-exchanging\n" +
                    "compression level             Packet compression will be enabled\n" +
                    "version                       Show SFTP version\n" +
                    "?                             Synonym for help";
}


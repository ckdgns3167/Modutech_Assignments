package local;

class AccessTarget{
    String target = null;//접속 대상 IP
    Integer port = null;
    String user = null;
    String pass = null;

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
class AccessTarget {
	String target = null;// 접속 대상 IP
	String user = null;
	String pass = null;
	Integer port = null;

	public AccessTarget(String target, String user, String pass, Integer port) {
		this.target = target;
		this.user = user;
		this.pass = pass;
		this.port = port;
	}

	public String getTarget() {
		return target;
	}

	public Integer getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

}
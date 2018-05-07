package Gnorma.model;

public class OpenStackExternalPort{
	
	private String name;
	private String fixedIP;
	private String floatingIP;
	private String networkName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFixedIP() {
		return fixedIP;
	}
	public void setFixedIP(String fixedIP) {
		this.fixedIP = fixedIP;
	}
	public String getFloatingIP() {
		return floatingIP;
	}
	public void setFloatingIP(String floatingIP) {
		this.floatingIP = floatingIP;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name + " - " + this.fixedIP + " - " + this.floatingIP + " - " + this.networkName;
	}
	

}

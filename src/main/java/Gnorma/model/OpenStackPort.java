package Gnorma.model;

public class OpenStackPort {
	
	private String name;
	private String fixedIP;
	private String macAddress;
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
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
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
		return this.name + " - " + this.fixedIP + " - " + this.networkName;
	}



	
}

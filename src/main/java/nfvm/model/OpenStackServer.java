package nfvm.model;

import java.util.LinkedList;

public class OpenStackServer {
	
	private String serverName;
	private String flavorName;
	private String securityGroup;
	private String vmImageName;
	private String keyPairName;
	private OpenStackExternalPort externalPort;
	private String availabilityZone;
	//private OpenStackExternalPort externalPort;
	private LinkedList<OpenStackPort> ports;

	

	public String getServerName() {
		return serverName;
	}



	public void setServerName(String serverName) {
		this.serverName = serverName;
	}



	public String getFlavorName() {
		return flavorName;
	}



	public void setFlavorName(String flavorName) {
		this.flavorName = flavorName;
	}



	public String getSecurityGroup() {
		return securityGroup;
	}



	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}



	public String getVmImageName() {
		return vmImageName;
	}



	public void setVmImageName(String vmImageName) {
		this.vmImageName = vmImageName;
	}



	public String getKeyPairName() {
		return keyPairName;
	}



	public void setKeyPairName(String keyPairName) {
		this.keyPairName = keyPairName;
	}



	public OpenStackExternalPort getExternalPort() {
		return externalPort;
	}



	public void setExternalPort(OpenStackExternalPort externalPort) {
		this.externalPort = externalPort;
	}



	public String getAvailabilityZone() {
		return availabilityZone;
	}



	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}



	public LinkedList<OpenStackPort> getPorts() {
		return ports;
	}



	public void setPorts(LinkedList<OpenStackPort> ports) {
		this.ports = ports;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("  ** Server Details **\n");
		sb.append("  ********************\n");
		sb.append("  name = " 		+ getServerName() 				+ "\n");
		sb.append("  flavor = "		+ getFlavorName() 				+ "\n");
		sb.append("  securityGroup = "+ getSecurityGroup() 			+ "\n");
		sb.append("  vmImage = "		+ getVmImageName()				+ "\n");
		sb.append("  keyPairName = "	+ getKeyPairName()				+ "\n");
		sb.append("  ExternalPort = "	+ this.externalPort.toString()	+ "\n");
		sb.append("    ** PORTS **\n");
		for (OpenStackPort port : this.getPorts()) {
			sb.append("    Port = "	+ port.toString()	+ "\n");
		}
		sb.append("\n");
		return sb.toString();
	}



}

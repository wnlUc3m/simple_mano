package Gnorma.model;

import java.util.LinkedList;

public class OpenStackNetwork {

	private String tenantId;
	private String networkName;
	private LinkedList<OpenStackSubnet> subnets;
	
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public LinkedList<OpenStackSubnet> getSubnets() {
		return subnets;
	}
	public void setSubnets(LinkedList<OpenStackSubnet> subnets) {
		this.subnets = subnets;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("  ***** Network *****\n");
		sb.append("  TenantId = " 		+ getTenantId()				+ "\n");
		sb.append("  Name = " 			+ getNetworkName()			+ "\n");
		for (OpenStackSubnet subnet : getSubnets()) {
			sb.append(subnet.toString());
		}
		sb.append("\n");
		return sb.toString();
	}
	
}

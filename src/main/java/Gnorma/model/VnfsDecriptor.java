package Gnorma.model;

import java.util.LinkedList;

public class VnfsDecriptor {

	private String tenantId;
	private String poolName;
	private String gatewayName;
	private LinkedList<OpenStackServer> vduds;
	

	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public String getGatewayName() {
		return gatewayName;
	}
	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}
	public LinkedList<OpenStackServer> getVduds() {
		return vduds;
	}
	public void setVduds(LinkedList<OpenStackServer> vduds) {
		this.vduds = vduds;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("***** VNFD *****\n");
		sb.append("vendor = " 		+ getTenantId() 				+ "\n");
		sb.append("pool = " 		+ getPoolName() 				+ "\n");
		sb.append("gateway = " 		+ getGatewayName() 				+ "\n");
		for (OpenStackServer ps : this.getVduds()) {
			sb.append(ps.toString());
		}
		sb.append("*****************************");
		return sb.toString();
	}
	
	
	
	
	
}

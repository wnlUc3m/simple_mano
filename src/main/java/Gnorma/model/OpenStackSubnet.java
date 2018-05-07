package Gnorma.model;

public class OpenStackSubnet {

	private String subnetName;
	private String startIPPool;
	private String endIPPool;
	private String cidr;
	private String defaultRouter;
	private String dhcp;
	
	public boolean isExternal() {
		if (this.getDefaultRouter() != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getSubnetName() {
		return subnetName;
	}
	public void setSubnetName(String subnetName) {
		this.subnetName = subnetName;
	}
	public String getStartIPPool() {
		return startIPPool;
	}
	public void setStartIPPool(String startIPPool) {
		this.startIPPool = startIPPool;
	}
	public String getEndIPPool() {
		return endIPPool;
	}
	public void setEndIPPool(String endIPPool) {
		this.endIPPool = endIPPool;
	}
	public String getCidr() {
		return cidr;
	}
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}
	public String getDefaultRouter() {
		return defaultRouter;
	}
	public void setDefaultRouter(String defaultRouter) {
		this.defaultRouter = defaultRouter;
	}
	public String getDhcp() {
		return dhcp;
	}
	public void setDhcp(String dhcp) {
		this.dhcp = dhcp;
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("    ***** Subnet *****\n");
		sb.append("    Name = " 		+ getSubnetName()		+ "\n");
		sb.append("    Pool = (" 		+ getStartIPPool() + " - " + getEndIPPool() +  ") \n");
		sb.append("    CIDR = " 		+ getCidr()				+ "\n");
		sb.append("    DHCP = " 		+ getDhcp()				+ "\n");
		sb.append("\n");
		return sb.toString();
	}
	
}

package Gnorma.model;

import java.util.LinkedList;

public class NetTopologyDecriptor {
	
	private String topologyName;
	private LinkedList<OpenStackNetwork> networks;
	
	public String getTopologyName() {
		return topologyName;
	}
	public void setTopologyName(String topologyName) {
		this.topologyName = topologyName;
	}
	public LinkedList<OpenStackNetwork> getNetworks() {
		return networks;
	}
	public void setNetworks(LinkedList<OpenStackNetwork> networks) {
		this.networks = networks;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("***** Topology decriptor *****\n");
		sb.append("  Name = " 		+ getTopologyName()				+ "\n");
		for (OpenStackNetwork n : this.getNetworks()) {
			sb.append(n.toString());
		}
		sb.append("*****************************");
		return sb.toString();
	}
	
}

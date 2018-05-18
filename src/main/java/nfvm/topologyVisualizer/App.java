package nfvm.topologyVisualizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import nfvm.model.NetTopologyDecriptor;
import nfvm.model.OpenStackNetwork;
import nfvm.model.VnfsDecriptor;
import nfvm.openstack_api_handler.OpenStackHandler;



public class App{

	String mme_slice1_image_id = "MME_S1_ID";
	String hss_slice1_image_id = "HSS_S1_ID";
	String spgw_slice1_image_id = "SPGW_S1_ID";

	String mme_slice2_image_id = "MME_S2_ID";
	String hss_slice2_image_id = "HSS_S2_ID";
	String spgw_slice2_image_id = "SPGW_S2_ID";
	
	/**
	 * Function to deploy 2 different instances of OpenAirCN (network core) in order to connect two slices.
	 */
	public void deploy() {
		long startTime = System.nanoTime();
		
    	//Common
    	String tenantId = "TENANT_ID";
    	String poolName = "POOL_NAME";
    	String routerName = "ROUTER_NAME";
    	String securityGroupName = "Default";
    	
    	/////////////
    	// SLICE 1 //
    	/////////////
    	
    	String slice1_net_name = "slice_1_net";
    	String slice1_subnet_name = "slice_1_subnet";
    	String slice1_startPool = "192.168.110.5";
    	String slice1_endPool = "192.168.110.200";
    	String slice1_cidr = "192.168.110.0/24";
    	OpenStackHandler.createNetwork(slice1_net_name, tenantId);
    	OpenStackHandler.createSubnet(slice1_net_name, slice1_subnet_name, tenantId, slice1_startPool, slice1_endPool, slice1_cidr);
    	OpenStackHandler.attachSubnetToExternalIface(routerName, slice1_subnet_name);
    	
    	LinkedList<String> mme_s1_ports = new LinkedList<String>();
    	LinkedList<String> spgw_s1_ports = new LinkedList<String>();
    	LinkedList<String> hss_s1_ports = new LinkedList<String>();

    	// S11 Network creation 
    	String s11_s1_netName = "s11_s1";
    	String s11_s1_subnetName = "s11_s1_subnet";
    	String mme_s1_s11_portName = "mme_s1_s11_port";
    	String mme_s1_s11_ip = "192.168.30.2";
    	String spgw_s1_s11_portName = "spgw_s1_s11_port";
    	String spgw_s1_s11_ip = "192.168.30.3";    	
    	OpenStackHandler.createNetwork(s11_s1_netName, tenantId);
    	OpenStackHandler.createSubnet(s11_s1_netName, s11_s1_subnetName, tenantId, "192.168.30.2", "192.168.30.50", "192.168.30.0/24");
    	OpenStackHandler.createPort(mme_s1_s11_portName, s11_s1_netName, mme_s1_s11_ip, s11_s1_subnetName);
    	OpenStackHandler.createPort(spgw_s1_s11_portName, s11_s1_netName, spgw_s1_s11_ip, s11_s1_subnetName);
    	mme_s1_ports.add(mme_s1_s11_portName);
    	spgw_s1_ports.add(spgw_s1_s11_portName);

    	// S6a Network creation
    	String s6a_s1_netName = "s6a_s1";
    	String s6a_s1_subnetName = "s6a_s1_subnet";
    	String mme_s1_s6a_portName = "mme_s1_s6a_port";
    	String mme_s1_s6a_ip = "192.168.40.2";
    	String hss_s1_s6a_portName = "hss_s1_s6a_port";
    	String hss_s1_s6a_ip = "192.168.40.3";    	
    	OpenStackHandler.createNetwork(s6a_s1_netName, tenantId);
    	OpenStackHandler.createSubnet(s6a_s1_netName, s6a_s1_subnetName, tenantId, "192.168.40.2", "192.168.40.50", "192.168.40.0/24");
    	OpenStackHandler.createPort(mme_s1_s6a_portName, s6a_s1_netName, mme_s1_s6a_ip, s6a_s1_subnetName);
    	OpenStackHandler.createPort(hss_s1_s6a_portName, s6a_s1_netName, hss_s1_s6a_ip, s6a_s1_subnetName);
    	mme_s1_ports.add(mme_s1_s6a_portName);
    	hss_s1_ports.add(hss_s1_s6a_portName);
    	
    	// Building MME
    	String mme_s1_instanceName = "MME_s1";
    	String mme_s1_flavorId = "2"; 					
    	String mme_s1_imageId = this.mme_slice1_image_id;
    	String mme_s1_portName = "s1c_s1_port";
    	String mme_s1_privateIP = "192.168.110.3";
    	String mme_s1_floatingIP = "192.168.1.70";
    	OpenStackHandler.createPort(mme_s1_portName, slice1_net_name, mme_s1_privateIP, slice1_subnet_name);
    	OpenStackHandler.createInstanceWithPort(mme_s1_portName, mme_s1_instanceName, mme_s1_flavorId, mme_s1_imageId, securityGroupName, mme_s1_ports);
    	OpenStackHandler.allocateRequestedFloatingIP(mme_s1_floatingIP, poolName);
    	
    	// Building HSS
    	String hss_s1_instanceName = "HSS_s1";
    	String hss_s1_flavorId = "2"; 
    	String hss_s1_imageId = this.hss_slice1_image_id;  // spgw_srsLTE
    	String hss_s1_portName = "hssControl_s1_port";
    	String hss_s1_privateIP = "192.168.110.2";
    	String hss_s1_floatingIP = "192.168.1.90";
    	OpenStackHandler.createPort(hss_s1_portName, slice1_net_name, hss_s1_privateIP, slice1_subnet_name);
    	OpenStackHandler.createInstanceWithPort(hss_s1_portName, hss_s1_instanceName, hss_s1_flavorId, hss_s1_imageId, securityGroupName, hss_s1_ports);
    	OpenStackHandler.allocateRequestedFloatingIP(hss_s1_floatingIP, poolName);
    	
    	String provider_s1_netName = "provider_s1";
    	String provider_s1_subnetName = "provider_s1_subnet";
    	String spgw_s1_provider_portName = "spgw_s1_provider_port";
    	String spgw_s1_provider_ip = "192.168.100.2";
    	OpenStackHandler.createNetwork(provider_s1_netName, tenantId);
    	OpenStackHandler.createSubnet(provider_s1_netName, provider_s1_subnetName, tenantId, "192.168.100.2", "192.168.100.50", "192.168.100.0/24");
    	OpenStackHandler.createPort(spgw_s1_provider_portName, provider_s1_netName, spgw_s1_provider_ip, provider_s1_subnetName);
    	spgw_s1_ports.add(spgw_s1_provider_portName);
    	
    	// Building SPGW
    	String spgw_s1_instanceName = "SPGW_s1";
    	String spgw_s1_flavorId = "4f25e4f6-081c-4b8b-b49f-02e5dc29e9d9";
    	String spgw_s1_imageId = this.spgw_slice1_image_id;
    	String spgw_s1_portName = "s1u_s1_port";
    	String spgw_s1_privateIP = "192.168.110.4";
    	String spgw_s1_floatingIP = "192.168.1.71";
    	OpenStackHandler.createPort(spgw_s1_portName, slice1_net_name, spgw_s1_privateIP, slice1_subnet_name);
    	OpenStackHandler.createInstanceWithPort(spgw_s1_portName, spgw_s1_instanceName, spgw_s1_flavorId, spgw_s1_imageId, securityGroupName, spgw_s1_ports);
    	OpenStackHandler.allocateRequestedFloatingIP(spgw_s1_floatingIP, poolName);

    	/////////////
		// SLICE 2 //
		/////////////
    	
		String slice2_net_name = "slice_2_net";
		String slice2_subnet_name = "slice_2_subnet";
		String slice2_startPool = "192.168.120.5";
		String slice2_endPool = "192.168.120.200";
		String slice2_cidr = "192.168.120.0/24";
		OpenStackHandler.createNetwork(slice2_net_name, tenantId);
		OpenStackHandler.createSubnet(slice2_net_name, slice2_subnet_name, tenantId, slice2_startPool, slice2_endPool, slice2_cidr);
		OpenStackHandler.attachSubnetToExternalIface(routerName, slice2_subnet_name);
		
		
		LinkedList<String> mme_s2_ports = new LinkedList<String>();
		LinkedList<String> spgw_s2_ports = new LinkedList<String>();
		LinkedList<String> hss_s2_ports = new LinkedList<String>();
	
		String s11_s2_netName = "s11_s2";
		String s11_s2_subnetName = "s11_s2_subnet";
		String mme_s2_s11_portName = "mme_s2_s11_port";
		String mme_s2_s11_ip = "192.168.50.2";
		String spgw_s2_s11_portName = "spgw_s2_s11_port";
		String spgw_s2_s11_ip = "192.168.50.3";    	
		OpenStackHandler.createNetwork(s11_s2_netName, tenantId);
		OpenStackHandler.createSubnet(s11_s2_netName, s11_s2_subnetName, tenantId, "192.168.50.2", "192.168.50.50", "192.168.50.0/24");
		OpenStackHandler.createPort(mme_s2_s11_portName, s11_s2_netName, mme_s2_s11_ip, s11_s2_subnetName);
		OpenStackHandler.createPort(spgw_s2_s11_portName, s11_s2_netName, spgw_s2_s11_ip, s11_s2_subnetName);
		mme_s2_ports.add(mme_s2_s11_portName);
		spgw_s2_ports.add(spgw_s2_s11_portName);
	
		String s6a_s2_netName = "s6a_s2";
		String s6a_s2_subnetName = "s6a_s2_subnet";
		String mme_s2_s6a_portName = "mme_s2_s6a_port";
		String mme_s2_s6a_ip = "192.168.60.2";
		String hss_s2_s6a_portName = "hss_s2_s6a_port";
		String hss_s2_s6a_ip = "192.168.60.3";    	
		OpenStackHandler.createNetwork(s6a_s2_netName, tenantId);
		OpenStackHandler.createSubnet(s6a_s2_netName, s6a_s2_subnetName, tenantId, "192.168.60.2", "192.168.60.50", "192.168.60.0/24");
		OpenStackHandler.createPort(mme_s2_s6a_portName, s6a_s2_netName, mme_s2_s6a_ip, s6a_s2_subnetName);
		OpenStackHandler.createPort(hss_s2_s6a_portName, s6a_s2_netName, hss_s2_s6a_ip, s6a_s2_subnetName);
		mme_s2_ports.add(mme_s2_s6a_portName);
		hss_s2_ports.add(hss_s2_s6a_portName);
		
		String mme_s2_instanceName = "MME_s2";
		String mme_s2_flavorId = "2";
		String mme_s2_imageId = this.mme_slice2_image_id;
		String mme_s2_portName = "s1c_s2_port";
		String mme_s2_privateIP = "192.168.120.3";
		String mme_s2_floatingIP = "192.168.1.72";
		OpenStackHandler.createPort(mme_s2_portName, slice2_net_name, mme_s2_privateIP, slice2_subnet_name);
		OpenStackHandler.createInstanceWithPort(mme_s2_portName, mme_s2_instanceName, mme_s2_flavorId, mme_s2_imageId, securityGroupName, mme_s2_ports);
		OpenStackHandler.allocateRequestedFloatingIP(mme_s2_floatingIP, poolName);
		
		String hss_s2_instanceName = "HSS_s2";
		String hss_s2_flavorId = "2"; 
		String hss_s2_imageId = this.hss_slice2_image_id;
		String hss_s2_portName = "hssControl_s2_port";
		String hss_s2_privateIP = "192.168.120.2";
		String hss_s2_floatingIP = "192.168.1.91";
		OpenStackHandler.createPort(hss_s2_portName, slice2_net_name, hss_s2_privateIP, slice2_subnet_name);
		OpenStackHandler.createInstanceWithPort(hss_s2_portName, hss_s2_instanceName, hss_s2_flavorId, hss_s2_imageId, securityGroupName, hss_s2_ports);
		OpenStackHandler.allocateRequestedFloatingIP(hss_s2_floatingIP, poolName);
		
		String provider_s2_netName = "provider_s2";
		String provider_s2_subnetName = "provider_s2_subnet";
		String spgw_s2_provider_portName = "spgw_s2_provider_port";
		String spgw_s2_provider_ip = "192.168.200.2";
		OpenStackHandler.createNetwork(provider_s2_netName, tenantId);
		OpenStackHandler.createSubnet(provider_s2_netName, provider_s2_subnetName, tenantId, "192.168.200.2", "192.168.200.50", "192.168.200.0/24");
		OpenStackHandler.createPort(spgw_s2_provider_portName, provider_s2_netName, spgw_s2_provider_ip, provider_s2_subnetName);
		spgw_s2_ports.add(spgw_s2_provider_portName);
		
		String spgw_s2_instanceName = "SPGW_s2";
		String spgw_s2_flavorId = "4f25e4f6-081c-4b8b-b49f-02e5dc29e9d9";
		String spgw_s2_imageId = this.spgw_slice2_image_id;
		String spgw_s2_portName = "s1u_s2_port";
		String spgw_s2_privateIP = "192.168.120.4";
		String spgw_s2_floatingIP = "192.168.1.73";
		OpenStackHandler.createPort(spgw_s2_portName, slice2_net_name, spgw_s2_privateIP, slice2_subnet_name);
		OpenStackHandler.createInstanceWithPort(spgw_s2_portName, spgw_s2_instanceName, spgw_s2_flavorId, spgw_s2_imageId, securityGroupName, spgw_s2_ports);
		OpenStackHandler.allocateRequestedFloatingIP(spgw_s2_floatingIP, poolName);
    	
		// Slice 1 -- floating IPs assignment
    	while(!OpenStackHandler.serverIsReady(mme_s1_instanceName)) {
			try {
				System.out.println("MME_s1 Still not active ...");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	OpenStackHandler.assignFloatingIP(mme_s1_instanceName, mme_s1_privateIP, mme_s1_floatingIP);
    	
    	while(!OpenStackHandler.serverIsReady(spgw_s1_instanceName)) {
			try {
				System.out.println("SPGW_s1 Still not active ...");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	OpenStackHandler.assignFloatingIP(spgw_s1_instanceName, spgw_s1_privateIP, spgw_s1_floatingIP);
    	
    	while(!OpenStackHandler.serverIsReady(hss_s1_instanceName)) {
			try {
				System.out.println("HSS_s1 Still not active ...");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	OpenStackHandler.assignFloatingIP(hss_s1_instanceName, hss_s1_privateIP, hss_s1_floatingIP);

    	
		// Slice 2 -- floating IPs assignment
    	while(!OpenStackHandler.serverIsReady(mme_s2_instanceName)) {
			try {
				System.out.println("MME_s2 Still not active ...");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	OpenStackHandler.assignFloatingIP(mme_s2_instanceName, mme_s2_privateIP, mme_s2_floatingIP);
    	
    	while(!OpenStackHandler.serverIsReady(spgw_s2_instanceName)) {
			try {
				System.out.println("SPGW_s2 Still not active ...");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	OpenStackHandler.assignFloatingIP(spgw_s2_instanceName, spgw_s2_privateIP, spgw_s2_floatingIP);
    	
    	while(!OpenStackHandler.serverIsReady(hss_s2_instanceName)) {
			try {
				System.out.println("HSS_s2 Still not active ...");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	OpenStackHandler.assignFloatingIP(hss_s2_instanceName, hss_s2_privateIP, hss_s2_floatingIP);
    	
    	long endTime   = System.nanoTime();
    	long totalTime = endTime - startTime;
    	System.out.println("Duration -- " + totalTime/1000 + "(seconds)");
    	
	}
	
    public static void main( String[] args ){

    	if (args.length != 2) {
    		System.out.println("2 JSON files expected");
    		System.out.println("  - Networks definition file");
    		System.out.println("  - VNFs definition file");
    	}else {
    		System.out.println("Reading " + args[0] + " and " + args[1] + "...");

        	// check if default router already exists
        	try {
    			byte[] topologyJSON = Files.readAllBytes(Paths.get(args[0]));
    			ObjectMapper objectMapper = new ObjectMapper();
    			
    			NetTopologyDecriptor ntd = objectMapper.readValue(topologyJSON, NetTopologyDecriptor.class);
    			System.out.println(ntd.toString());
    			
    			OpenStackHandler.getServers();
    			
    			for(OpenStackNetwork net : ntd.getNetworks()) {
    				OpenStackHandler.createNetwork(net);
    			}

    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        	
        	//TODO: check: falvorId, imageName, keypair and availabilityZone
        	try {
    			byte[] jsonData = Files.readAllBytes(Paths.get(args[1]));
    			ObjectMapper objectMapper = new ObjectMapper();
    			
    			VnfsDecriptor vnfd = objectMapper.readValue(jsonData, VnfsDecriptor.class);
    			System.out.println(vnfd.toString());
    			
    			//TODO: create resources using json files

    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        	
    	}	

    }
}

package nfvm.openstack_api_handler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.openstack.OSFactory;

import nfvm.model.OpenStackNetwork;
import nfvm.model.OpenStackSubnet;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.network.AttachInterfaceType;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;

public class OpenStackHandler {
	
	private static String url = "http://192.168.1.250:5000/v2.0";
	
	private static OSClientV2 connector = OSFactory.builderV2()
            .endpoint(url)
            .credentials("USER", "PASSWORD")
            .tenantName("TENAT_NAME")
            .authenticate();	
	
	private static final String poolName = "external";
	
	private static HashMap<String, String> serverIds = new HashMap<String, String>();
	private static HashMap<String, String> networkIds = new HashMap<String, String>();
	private static HashMap<String, String> subnetIds = new HashMap<String, String>();
	private static HashMap<String, String> routerIds = new HashMap<String, String>();
	private static HashMap<String, String> portIds = new HashMap<String, String>();
	
	
	public static List<? extends Image> getImages(Map<String, String> mapa){
		return connector.images().listAll();
	}
	public static List<? extends Server> getServers(){
		return connector.compute().servers().list();
	}
	public static List<String> getFloatingPools(){
		return connector.compute().floatingIps().getPoolNames();
	}
	public static List<? extends FloatingIP> getFloatingIPs(){
		return connector.compute().floatingIps().list();
	}
	public static boolean serverIsReady(String serverName) {
		Server server = connector.compute().servers().get(serverIds.get(serverName));

		if (server.getStatus() != Server.Status.ACTIVE) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Check whether an IP address is already allocated in the system or not
	 * @param ipAddress
	 * @return
	 */
	private static boolean isAllocated(String ipAddress) {
		List<? extends FloatingIP> ips = connector.compute().floatingIps().list();
		for (FloatingIP ip: ips) {
			if(ip.getFloatingIpAddress().equals(ipAddress)) {
				System.out.println("IP " + ip.getFloatingIpAddress() + " already allocated.");
				return true;
			}
		}
		System.out.println("IP " + ipAddress + " NOT allocated.");
		return false;
	}
	public static void allocateRequestedFloatingIP(String requestedIP, String poolName) {
		while (!isAllocated(requestedIP)) {
			FloatingIP newIP = connector.compute().floatingIps().allocateIP(poolName);
			System.out.println("New Floating IP allocated: " + newIP.getFloatingIpAddress());
		}
	}

	/**
	 * Network builder
	 * @param networkName
	 * @param tenantId
	 */
	public static void createNetwork(String networkName, String tenantId) {
		Network net = connector.networking().network().create(Builders.network().name(networkName).tenantId(tenantId).build());
		System.out.println("Network " + networkName + " created");
		networkIds.put(networkName, net.getId());
	}
	/**
	 * Network builder: Creates a network and its corresponding subnet. If the network is marked as external, it connects it to a router.
	 * @param network
	 */
	public static boolean createNetwork(OpenStackNetwork network) {
		if (network.getSubnets().size() <= 0) {
			System.out.println("No subnet available");
			return false;
		}
		else {
			Network net = connector.networking().network().create(Builders.network().name(network.getNetworkName()).tenantId(network.getTenantId()).build());
			System.out.println("  - Network " + network.getNetworkName() + " created");
			networkIds.put(network.getNetworkName(), net.getId());
			
			for (OpenStackSubnet subnet : network.getSubnets()) {
				createSubnet(network.getNetworkName(), subnet.getSubnetName(), network.getTenantId(), subnet.getStartIPPool(), subnet.getEndIPPool(), subnet.getCidr());
				if (subnet.isExternal()) {
					attachSubnetToExternalIface(subnet.getDefaultRouter(), subnet.getSubnetName());
				}
			}
			
			return true;
		}
		
	}
	
	/**
	 * Subnet builder
	 * @param networkName
	 * @param subnetName
	 * @param tenantId
	 * @param startPool
	 * @param endPool
	 * @param cidr
	 */
	public static void createSubnet(String networkName, String subnetName, String tenantId, String startPool, String endPool, String cidr) {
		Subnet subnet = connector.networking().subnet().create(Builders.subnet()
                .name(subnetName)
                .networkId(networkIds.get(networkName))
                .tenantId(tenantId)
                .addPool(startPool, endPool)
                .ipVersion(IPVersionType.V4)
                .cidr(cidr)
                .build());
		subnetIds.put(subnetName, subnet.getId());
		System.out.println("Subnet " + subnetName + " correctly created");
	}
	
	/**
	 * Attach subnet to an interface connected to the internet/external lan
	 * @param routerName
	 * @param subnetName
	 */
	public static void attachSubnetToExternalIface(String routerName, String subnetName) {
		List<? extends Router> routers = connector.networking().router().list();
		for(Router router: routers) {
			if(router.getName().equals(routerName)) {
				connector.networking().router().attachInterface(router.getId(), AttachInterfaceType.SUBNET, subnetIds.get(subnetName));
				System.out.println(router.getName() + " and " + subnetName + " attached");
			}
		}
	}
	
	/**
	 * Port builder
	 * @param portName
	 * @param privateNetworkName
	 * @param privateIp
	 * @param subnetName
	 * @return
	 */
	public static boolean createPort(String portName, String privateNetworkName, String privateIp, String subnetName) {
		try {
			Port port = connector.networking().port().create(Builders.port()
		              .name(portName)
		              .networkId(networkIds.get(privateNetworkName))
		              .fixedIp(privateIp, subnetIds.get(subnetName))
		              .build());
			portIds.put(portName, port.getId());
		} catch (Exception e) {
			System.out.println("Port cannot be created: " + e);
			return false;
		}
		System.out.println("Port " + portName + " correctly created");
		return true;

	}

	/**
	 * Instance Builder: Creates the instance with its corresponding ports
	 * @param portName
	 * @param instanceName
	 * @param flavorId
	 * @param imageId
	 * @param securityGroupName
	 * @param ports
	 */
	public static void createInstanceWithPort(String portName,String instanceName, String flavorId, String imageId, String securityGroupName,
			LinkedList<String> ports) {
		
		// Create a Server Model Object
		ServerCreate sc = Builders.server()
		                          .name(instanceName)
		                          .flavor(flavorId)
		                          .image(imageId)
		                          .addNetworkPort(portIds.get(portName))
		                          .availabilityZone("nova:buyo")
		                          .build();
		
		sc.addSecurityGroup(securityGroupName);
		sc.addSecurityGroup("1c9eea4c-9518-493c-be09-de6295493681");
		for(String port: ports) {
			sc.addNetworkPort(portIds.get(port));
		}
		
		// Boot the Server
		Server server = connector.compute().servers().boot(sc);
		System.out.println("Server " + instanceName + " created and connected to port: " + portName);
		serverIds.put(instanceName, server.getId());
	}
	
	/**
	 * Assigns a floating IP to an instance
	 * @param instanceName
	 * @param privateIp
	 * @param floatingIpToBeAssigned
	 */
	public static void assignFloatingIP(String instanceName, String privateIp, String floatingIpToBeAssigned) {
		
		List<? extends Server> servers = connector.compute().servers().list();
		List<? extends FloatingIP> ips = connector.compute().floatingIps().list();
		
		for (FloatingIP assignedIp: ips) {
			if (assignedIp.getFloatingIpAddress().equals(floatingIpToBeAssigned)) {
				System.out.println("IP " + assignedIp.getFloatingIpAddress() + " already in the pool");	
				
				NetFloatingIP netFloatingIP = connector.networking().floatingip().get(assignedIp.getId());
				Server server = connector.compute().servers().get(serverIds.get(instanceName));
				ActionResponse r = connector.compute().floatingIps().addFloatingIP(server, privateIp, netFloatingIP.getFloatingIpAddress());
				
				System.out.println("added " + r);
				
			}
		}		
	}

}

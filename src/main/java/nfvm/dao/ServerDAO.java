package nfvm.dao;

import org.openstack4j.model.compute.Server;

import nfvm.model.OpenStackServer;

public interface ServerDAO {

	/*
	 * Driver Methods interface
	 */
	public boolean addServer(OpenStackServer d);
	public boolean updateServer(OpenStackServer d);
	public boolean deleteServer(OpenStackServer id);
	public boolean existServer(String id);
	public OpenStackServer getServer(String id);
	/*
	public Driver getDriver(String id);
	public List<Driver> getDrivers(String... args);
	public LinkedList<Driver> getAllDrivers();
	public boolean existDriver(String id);
	*/
}

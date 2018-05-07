package Gnorma.dao;

/*
 * Abstract factory of MongoDB connectors
 */
public class MongoFactoryDAO extends FactoryDAO{
	
	protected static final String dbName = "openstack";
	
	@Override
	public ServerDAO getServerDAO() {
		return new MongoServerDAO();
	}

	public static String getDbname() {
		return dbName;
	}
	
}

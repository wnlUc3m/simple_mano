package nfvm.dao;

/*
 * Abstract factory in order to create an instance of an specific Database
 */
public abstract class FactoryDAO {
	
	// Singleton pattern
	private static FactoryDAO unicaInstancia = null;

	protected FactoryDAO() {
		
	}
	/*
	 * Creates an instance of an specific DAO
	 * @param database_type
	 * @return FactoryDAO
	 */
	public static FactoryDAO getFactoryDAO(FactoryDAOType type){
		
		switch (type) {
		case MONGO:
			unicaInstancia = new MongoFactoryDAO();
			return unicaInstancia;			
		/*case MYSQL:
			unicaInstancia = new MYSQLFactoriaDAO();
			return unicaInstancia;
			break;*/
		default:
			System.out.println("Database access instance not created. MONGODB");
			return null;
		}
	}
	/*
	 * Creates a connector to an specific Database in order to allow Users management.
	 * @return UserDAO
	 */
	public abstract ServerDAO getServerDAO();
	
}


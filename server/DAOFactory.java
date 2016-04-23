package cloud.server;

public abstract class DAOFactory {
	public static final int DAOCLOUD = 1;

	public abstract UserDAO getUserDAO();
	public abstract PathDAO getPathDAO();
	public abstract SyncDAO getSyncDAO();
	public abstract ModeDAO getModeDAO();

	public static DAOFactory getDAOFactory(int type){
		switch (type) {
			case DAOCLOUD:
				return new CloudDAOFactory();
			default:
				return null;
		}
	}
}
package cloud.server;
import java.util.ArrayList;

public interface SyncDAO {
	public boolean insertSync(Sync sync);
	public void deleteSync(Sync sync);
	public ArrayList<Sync> selectSync();
}
package cloud.server;

public interface ModeDAO {
	public boolean insertMode(Mode mode);
	public boolean deleteMode(Mode mode);
	public boolean contains(Mode mode);
	public String getModes();
}
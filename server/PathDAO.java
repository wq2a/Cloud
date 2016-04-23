package cloud.server;

public interface PathDAO {
	public int insertPath(String path);
	public boolean deletePath(String path);
	public String getPaths(String path);
	public boolean contains(String path);
}
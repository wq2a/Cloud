package cloud.server;

public interface UserDAO {
	public boolean insertUser(User user);
	public User findUser(User user);
	public boolean contains(User user);
}
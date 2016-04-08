package cloud.client;
import java.util.HashMap;
import cloud.client.Connection;
import cloud.client.ReceiverCallback;

interface Callback{
	public void start(Connection c);
	public void response(int requestID, int tag, ReceiverCallback rc,HashMap<String,String> response);
}
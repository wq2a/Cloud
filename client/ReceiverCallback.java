package cloud.client;
import java.util.HashMap;

interface ReceiverCallback{
	public void receive(int requestID,int tag,HashMap<String,String> data);
	public HashMap<String,String> preReceive(int requestID,int tag,HashMap<String,String> data);
}
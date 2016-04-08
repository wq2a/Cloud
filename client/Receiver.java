package cloud.client;
import java.util.HashMap;

public class Receiver implements Runnable{
	private ReceiverCallback callback;
	private int requestID;
	private int tag;
	private HashMap<String,String> data;
	Receiver(int requestID,int tag,HashMap<String,String> data,ReceiverCallback c){
		this.requestID = requestID;
		this.tag = tag;
		this.data = data;
		this.callback = c;
	}

	public void run(){
		callback.receive(requestID,tag,data);
	}

	public void pre(){
		this.data = callback.preReceive(requestID,tag,data);
	}
}
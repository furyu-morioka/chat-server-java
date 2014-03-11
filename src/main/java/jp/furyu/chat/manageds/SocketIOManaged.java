package jp.furyu.chat.manageds;

import jp.furyu.chat.resources.SocketIOResource;
import io.dropwizard.lifecycle.Managed;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class SocketIOManaged implements Managed {
    SocketIOServer server;

    public SocketIOManaged() {
	   	Configuration config = new Configuration();
	    config.setHostname("localhost");
	    config.setPort(9999);

	    this.server = new SocketIOServer(config);
	    SocketIOResource sl = new SocketIOResource();
	    this.server.addListeners(sl);
	    
	    /*
	   	this.server.addConnectListener(new ConnectListener() {			
	   		public void onConnect(SocketIOClient client) {
				System.out.println("connect");
				ChatObject object = new ChatObject();
				object.setText("Welcome !");
				object.setUser("user1");
				client.sendJsonObject(object);
			}
	    });

	   	this.server.addEventListener("message", ChatObject.class, new DataListener<ChatObject>() {
			public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) {
				System.out.println("d : " + data);
				server.getBroadcastOperations().sendEvent("message", data);;
			}
		});
		*/  
	   	
	    /*
	   	this.server.addMessageListener(new DataListener<String>() {
			public void onData(SocketIOClient client, String data, AckRequest ackSender) {
				System.out.println("d : " + data);
				ChatObject object = new ChatObject();
				object.setText("message !");
				object.setUser("user1");
				client.sendJsonObject(object);

				server.getBroadcastOperations().sendJsonObject(object);
			}
		});
		*/  

	   	/*
	    this.server.addJsonObjectListener(ChatObject.class, new DataListener<ChatObject>() {
			public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) {
				System.out.println("message");
				server.getBroadcastOperations().sendJsonObject(data);;
			}
		});
	   	 */
    }

    @Override
    public void start() throws Exception {
    	System.out.println("start managed socketio");
        server.start();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }
    
}

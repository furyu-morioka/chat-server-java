package jp.furyu.chat.manageds;

import io.dropwizard.lifecycle.Managed;
import jp.furyu.chat.resources.SocketIOResource;
import redis.clients.jedis.Jedis;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.store.RedisStoreFactory;

public class SocketIOManaged implements Managed {
    private SocketIOServer server;

//   redis設定とsocketio設定のinjection    
//    @Inject
//
    public SocketIOManaged() {
	   	Configuration configuration = new Configuration();
	    configuration.setHostname("localhost");
	    configuration.setPort(9999);
	    
	    /*
	    Jedis jedis = new Jedis("127.0.0.1", 6379);
	    Jedis jedisPub = new Jedis("127.0.0.1", 6379);
	    Jedis jedisSub = new Jedis("127.0.0.1", 6379);
	    RedisStoreFactory factory = new RedisStoreFactory(jedis, jedisPub, jedisSub);
	    configuration.setStoreFactory(factory);
	    */

	    this.server = new SocketIOServer(configuration);
	    SocketIOResource sl = new SocketIOResource(this.server);
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

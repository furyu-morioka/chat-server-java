package jp.furyu.chat.resources;

import java.util.HashMap;
import java.util.Map;

import jp.furyu.chat.api.ChatObject;
import jp.furyu.chat.api.RoomObject;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.annotation.OnMessage;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.pubsub.RedisPubSubConnection;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;

public class SocketIOResource {
	private final SocketIOServer server;
	private final RedisClient redisClient = new RedisClient("127.0.0.1", 6379);
	
	private final Map<String, Room> rooms = new HashMap<String, Room>();
	private RedisPubSubConnection<String, String> pub = redisClient.connectPubSub();
	
	public SocketIOResource(SocketIOServer server){
		this.server = server;
		

		System.out.println("server...");

	}

    @OnConnect
    public void onConnectHandler(SocketIOClient client) {
		System.out.println("connect");
		ChatObject object = new ChatObject();
		object.setText("Welcome !");
		object.setUser("user1");
		client.sendJsonObject(object);
		
		RedisPubSubConnection<String, String> sub = redisClient.connectPubSub();
		Room room = new Room(client, pub);
	    sub.addListener(room);
		sub.subscribe("channel");
		
		rooms.put(client.getSessionId().toString(), room);

		System.out.println("connect end");
    }

    @OnDisconnect
    public void onDisconnectHandler(SocketIOClient client) { 
    }

    // only data object is required in arguments, 
    // SocketIOClient and AckRequest could be ommited
    @OnMessage
    public void onMessageHandler(SocketIOClient client, String data, AckRequest ackRequest) {
		System.out.println("d : " + data);
		ChatObject object = new ChatObject();
		object.setText("message !");
		object.setUser("user1");
		client.sendJsonObject(object);
		
    }
    
    
    @OnEvent("message")
    public void onMessageEventHandler(SocketIOClient client, ChatObject data, AckRequest ackRequest) {    	
		System.out.println("h : " + data.getText());
//		client.getNamespace().getBroadcastOperations().sendEvent("message", data);
		
		
		Room room = rooms.get(client.getSessionId().toString());
		if(room != null){
			room.getPubConnection().publish("channel", data.getText());
		}

    }
    
    @OnEvent("join")
    public void onJoinEventHandler(SocketIOClient client, RoomObject data, AckRequest ackRequest) {    	
		System.out.println("r : " + data.getRoom());
		
		ChatObject object = new ChatObject();
		object.setText("message !");
		object.setUser("user2");	
		System.out.println("room size : " + server.getRoomOperations(data.getRoom()).getClients().size());
		// server.getRoomOperations(data.getRoom()).sendEvent("message", data);
		
		client.getNamespace().getBroadcastOperations().sendEvent("message", object);
    }
    
    
    class Room implements RedisPubSubListener<String, String> {
    	private final SocketIOClient client;
    	private final RedisPubSubConnection<String, String> pubConnection;    	
    	
    	public Room(SocketIOClient client, RedisPubSubConnection<String, String> pub){
    		this.client = client;
    		this.pubConnection = pub;
    	}
    	
    	public RedisPubSubConnection<String, String> getPubConnection(){
    		return this.pubConnection;
    	}

		@Override
		public void message(String channel, String message) {
			System.out.println("m : " + message);
			ChatObject object = new ChatObject();
			object.setText(message);
			object.setUser("user3");	
			client.sendEvent("message", object);
		}

		@Override
		public void message(String pattern, String channel, String message) {
			System.out.println("m2 : " + message);
			
		}

		@Override
		public void subscribed(String channel, long count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void psubscribed(String pattern, long count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unsubscribed(String channel, long count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void punsubscribed(String pattern, long count) {
			// TODO Auto-generated method stub
			
		}


    }

}

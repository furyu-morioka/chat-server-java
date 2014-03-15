package jp.furyu.chat.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.furyu.chat.api.MessageObject;
import jp.furyu.chat.api.RoomObject;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.annotation.OnMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.pubsub.RedisPubSubConnection;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;

public class SocketIOResource {
	private final SocketIOServer server;
	private final RedisClient redisClient = new RedisClient("127.0.0.1", 6379);
	
	private final Map<String, SessionObject> rooms = new HashMap<String, SessionObject>();
	private RedisPubSubConnection<String, String> pub = redisClient.connectPubSub();
	
	public SocketIOResource(SocketIOServer server){
		this.server = server;

		System.out.println("server...");
	}

    @OnConnect
    public void onConnectHandler(SocketIOClient client) {
		System.out.println("connect");
		MessageObject object = new MessageObject();
		object.setText("Welcome !");
		object.setUser("user1");
		client.sendJsonObject(object);
		
		RedisPubSubConnection<String, String> sub = redisClient.connectPubSub();
		Room room = new Room(client, pub);
	    sub.addListener(room);
	    SessionObject so = new SessionObject(sub);
	    rooms.put(client.getSessionId().toString(), so);
		
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
		MessageObject object = new MessageObject();
		object.setText("message !");
		object.setUser("user1");
		client.sendJsonObject(object);		
    }
    
    
    @OnEvent("message")
    public void onMessageEventHandler(SocketIOClient client, MessageObject data, AckRequest ackRequest) throws JsonProcessingException {    	
		System.out.println("h : " + data.getText());
//		client.getNamespace().getBroadcastOperations().sendEvent("message", data);		
		
		SessionObject room = rooms.get(client.getSessionId().toString());
		if(room != null && room.getRoomName() != null){
			String hoge = encodeToJsonString(data);
			System.err.println("mmm : " + hoge);
			pub.publish(room.getRoomName(), encodeToJsonString(data));
		}

    }
    
    @OnEvent("join")
    public void onJoinEventHandler(SocketIOClient client, RoomObject data, AckRequest ackRequest) throws JsonProcessingException {    	
		System.out.println("r : " + data.getName());
		System.out.println("r : " + data.getValue());		
		
		MessageObject object = new MessageObject();
		object.setText("welcome !");
		object.setUser("user2");	
  
		String key = client.getSessionId().toString();
		
		// room disable
		SessionObject so = rooms.get(key);
		if(so != null && so.getRoomName() != null){
			so.getSub().unsubscribe(so.getRoomName());
		}
		
		// room enable
		if(so != null){
			so.getSub().subscribe(data.getName());;			
			so.setRoomName(data.getName());
		}
		String hoge = encodeToJsonString(object);
		System.err.println("mmm : " + hoge);

		pub.publish(data.getName(), encodeToJsonString(object));
    }
    
    protected String encodeToJsonString(Object object) throws JsonProcessingException{
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.writeValueAsString(object);
    }
    
    protected Object decodeToJson(String jsonString) throws JsonParseException, JsonMappingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.readValue(jsonString, MessageObject.class);
    }
    
    class SessionObject {
    	private final RedisPubSubConnection<String, String> sub;
    	private String roomName;

		public SessionObject(RedisPubSubConnection<String, String> sub){
    		this.sub = sub;
    	}
		
    	public String getRoomName() {
			return roomName;
		}

		public void setRoomName(String roomName) {
			this.roomName = roomName;
		}

		public RedisPubSubConnection<String, String> getSub() {
			return sub;
		}
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
			try{
				client.sendEvent("message", decodeToJson(message));
			} catch(Exception e){
				e.printStackTrace();
			}
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

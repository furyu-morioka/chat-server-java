package jp.furyu.chat.resources;

import jp.furyu.chat.api.ChatObject;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.annotation.OnMessage;

public class SocketIOResource {

    @OnConnect
    public void onConnectHandler(SocketIOClient client) {
		System.out.println("connect");
		ChatObject object = new ChatObject();
		object.setText("Welcome !");
		object.setUser("user1");
		client.sendJsonObject(object);
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
    public void onSomeEventHandler(SocketIOClient client, ChatObject data, AckRequest ackRequest) {    	
		System.out.println("h : " + data.getText());
		client.getNamespace().getBroadcastOperations().sendJsonObject(data);
    }}

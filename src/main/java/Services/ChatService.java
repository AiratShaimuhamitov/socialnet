package Services;

import WebSockets.ChatWebSocket;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    private Set<ChatWebSocket> webSockets;

    public ChatService(){
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void sendMessage(String data){
        for(ChatWebSocket user : webSockets){
            try{
                user.sendString(data);
            } catch (Exception e){
                System.out.print(e.getMessage());
            }
        }
    }

    public void add(ChatWebSocket webSocket){
        webSockets.add(webSocket);
    }

    public void remove(ChatWebSocket webSocket){
        webSockets.remove(webSocket);
    }


}

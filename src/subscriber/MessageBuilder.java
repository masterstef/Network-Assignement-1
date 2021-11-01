package subscriber;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    final int VERSION = 1;

    public Message createSubscribeMessage(String... playload){
        return createSubscribeMessage(createPlayloadList(playload));
    }

    public Message createPublishMessage(String... playload){
        return createPublishMessage(createPlayloadList(playload));
    }

    public Message createAckMessage(String... playload){
        return createAckMessage(createPlayloadList(playload));
    }

    public Message createSubscribeMessage(List<String> playload){ return new Message(VERSION,0,playload);}

    public Message createPublishMessage(List<String> playload){ return new Message(VERSION,1,playload);}

    public Message createAckMessage(List<String> playload){ return new Message(VERSION,2,playload);}

    private List<String> createPlayloadList(String[] playload) {
        List<String> playloadList = new ArrayList<String>();
        for (String playloadString : playload){
            playloadList.add(playloadString);
        }
        return playloadList;
    }
}

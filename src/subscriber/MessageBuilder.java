package subscriber;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static subscriber.Constants.*;

public class MessageBuilder {

    /**
     * Create a subscribe message from a playload
     * @param playload list of string with playload infos
     */
    public Message createSubscribeMessage(String... playload){
        return createSubscribeMessage(createPlayloadList(playload));
    }

    /**
     * Create a publish message from a playload
     * @param playload list of string with playload infos
     */
    public Message createPublishMessage(String... playload){
        return createPublishMessage(createPlayloadList(playload));
    }

    /**
     * Create a publish message from a playload
     * @param playload list of string with playload infos
     */
    public Message createAckMessage(String... playload){
        return createAckMessage(createPlayloadList(playload));
    }

    public Message createSubscribeMessage(List<String> playload){ return new Message(VERSION,SUBSCRIBE_TYPE,playload);}

    public Message createPublishMessage(List<String> playload){ return new Message(VERSION,PUBLISH_TYPE,playload);}

    public Message createAckMessage(List<String> playload){ return new Message(VERSION,ACK_TYPE,playload);}

    private List<String> createPlayloadList(String[] playload) {
        List<String> playloadList = new ArrayList<String>();
        for (String playloadString : playload){
            playloadList.add(playloadString);
        }
        return playloadList;
    }
}

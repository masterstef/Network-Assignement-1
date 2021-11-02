import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    private final int VERSION = 1;
    /**
     * Create a subscribe message from a playload
     * @param playload ellipsis of string with playload infos
     * @return a subscribe message
     */
    public Message createSubscribeMessage(String... playload){
        return createSubscribeMessage(createPlayloadList(playload));
    }

    /**
     * Create a publish message from a playload
     * @param playload ellipsis of string with playload infos
     * @return a publish message
     */
    public Message createPublishMessage(String... playload){
        return createPublishMessage(createPlayloadList(playload));
    }

    /**
     * Create a publish message from a playload
     * @param playload ellipsis of string with playload infos
     * @return an ack message
     */
    public Message createAckMessage(String... playload){
        return createAckMessage(createPlayloadList(playload));
    }

    /**
     * Create a subscribe message from a playload
     * @param playload List of string with playload infos
     * @return a subscribe message
     */
    public Message createSubscribeMessage(List<String> playload){ return new Message(VERSION,0,playload);}

    /**
     * Create a publish message from a playload
     * @param playload List of string with playload infos
     * @return a publish message
     */
    public Message createPublishMessage(List<String> playload){ return new Message(VERSION,1,playload);}

    /**
     * Create a publish message from a playload
     * @param playload List of string with playload infos
     * @return an ack message
     */
    public Message createAckMessage(List<String> playload){ return new Message(VERSION,2,playload);}

    private List<String> createPlayloadList(String[] playload) {
        List<String> playloadList = new ArrayList<String>();
        for (String playloadString : playload){
            playloadList.add(playloadString);
        }
        return playloadList;
    }
}

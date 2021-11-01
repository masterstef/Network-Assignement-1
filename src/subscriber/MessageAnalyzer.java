package subscriber;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageAnalyzer {

    public String[] readPlayload(byte[] ackMessage){
        byte[] withoutHeader = Arrays.copyOfRange(ackMessage, 4,4 + ackMessage[2]);
        byte[] first = Arrays.copyOfRange(withoutHeader, 0, ackMessage[0]);
        byte[] withoutFirst = Arrays.copyOfRange(withoutHeader, withoutHeader[0],withoutHeader[withoutHeader[0]+1]);
        byte[] second = Arrays.copyOfRange(ackMessage, 4 + ackMessage[3],4 + ackMessage[3] + ackMessage[3 + ackMessage[3]]);
        return new String[]{new String(first, StandardCharsets.UTF_8),new String(second, StandardCharsets.UTF_8)};
    }

    // A VERIFIER
    public String[] readPublishSensor(byte[] ackMessage){
        List<String> positions = new ArrayList<String>();
        int currentMessageLength = 3 + ackMessage[2];
        while(ackMessage[0] != 0){
            positions.add(readPlayload(ackMessage)[1]);
            ackMessage = Arrays.copyOfRange(ackMessage, 4, ackMessage.length);
        }
        return positions.toArray(new String[positions.size()]);
    }

}

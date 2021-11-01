package subscriber;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message {

    private int version;
    private int type;
    private List<String> playload;

    public Message(int version,int type,List<String> playload) {
        this.version = version;
        this.type = type;
        this.playload = playload;
    }


    public byte[] toByte(){
        byte[] header = createHeaderByte();
        byte[] playload = createPlayloadByte();
        byte[] messageByte = new byte[playload.length + header.length];
        System.arraycopy(header , 0, messageByte , 0, header.length);
        System.arraycopy(playload , 0, messageByte , 3, playload.length);
        return messageByte;
    }

    public int getType() {
        return type;
    }

    public int getVersion() {
        return version;
    }

    public List<String> getPlayload() {
        return playload;
    }

    private byte[] createHeaderByte() {
        byte[] header = new byte[3];
        header[0] = (byte) version;
        header[1] = (byte) type;
        header[2] = (byte) getPlayloadLength();
        return header;
    }

    private int getPlayloadLength() {
        int playloadLength = 0;
        for (String x : playload) {
            playloadLength += x.getBytes(StandardCharsets.UTF_8).length;
        }
        return playloadLength + playload.size();
    }

    private byte[] createPlayloadByte() {
        byte [] playloadByte = new byte[getPlayloadLength()];
        int index = 0;
        for ( String p : playload ) {
            playloadByte[index] = (byte) p.getBytes(StandardCharsets.UTF_8).length;
            index++;
            System.arraycopy(p.getBytes(StandardCharsets.UTF_8), 0, playloadByte , index, playloadByte[index-1]);
            index += playloadByte[index-1];
        }
        return  playloadByte;
    }

    public Message(byte[] messageByte){
        version = messageByte[0];
        type = messageByte[1];
        playload = new ArrayList<>();
        byte[] playloadByte = new byte[messageByte[2]];
        System.arraycopy(messageByte, 3, playloadByte , 0, playloadByte.length);
        for (int i = 0; i < playloadByte.length; i+=playloadByte[i]+1){
            byte[] currentMessage = new byte[playloadByte[i]];
            System.arraycopy(playloadByte, i+1, currentMessage , 0, playloadByte[i]);
            playload.add(new String(currentMessage, StandardCharsets.UTF_8));
        }
    }

}

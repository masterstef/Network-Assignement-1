package subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class SubscriberManager {

    private MessageBuilder messageBuilder = new MessageBuilder();
    private Grid grid = new Grid();

    public void start(){
        try (Socket s = new Socket("localhost",2655)){
            s.setSoTimeout(1000);
            s.setTcpNoDelay(true);
            System.out.println("Welcome to the Monster Hunting Game");
            subscribeVictory(s);
            subscribePosition(s);
            read(s);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read(Socket s) throws Exception {
        try {
            byte[] msg = new byte[64];
            s.getInputStream().read(msg);
            for (int i = 0; i < msg.length ; i += msg[i+2]+3){
                if(i+2 > msg.length || msg[i+2] == 0) {
                    s.getInputStream().read(msg);
                    i = 0;
                    continue;
                }
                byte[] currentByteMessage = new byte[msg[i+2]+3];
                System.arraycopy(msg , i, currentByteMessage , 0, currentByteMessage.length);
                Message currentmessage = new Message(currentByteMessage);
                dispatch(currentmessage,s);
                System.out.println(currentmessage.getPlayload());
            }
        }catch (SocketTimeoutException e){

        }
    }

    private void dispatch(Message currentmessage, Socket s) throws Exception {
        switch (currentmessage.getType()){
            case 0 : handleSubscribeMessage(currentmessage,s); break;
            case 1 : handlePublishMessage(currentmessage,s); break;
            case 2 : handleAckMessage(currentmessage,s); break;
            default: throw new Exception("Message Type not handled");
        }

    }

    private void handleAckMessage(Message currentmessage, Socket s) {
    }

    private void handleSubscribeMessage(Message currentmessage, Socket s) {
    }

    private void handlePublishMessage(Message currentmessage, Socket s) throws Exception {
        switch (currentmessage.getPlayload().get(0)){
            case "position" :
                grid.addSensor(new Sensor(currentmessage.getPlayload()));

                break;
            case "guess" : break;
            case "victory" : break;
            default : throw new Exception("Message Topic not handled");
        }
    }


    private void subscribePosition(Socket s) throws IOException {
        Message subscribePositionMessage = messageBuilder.createSubscribeMessage("s213655","position");
        s.getOutputStream().write(subscribePositionMessage.toByte());
        s.getOutputStream().flush();
    }

    private void subscribeVictory(Socket s) throws IOException {
        Message subscribeVictoryMessage = messageBuilder.createSubscribeMessage("s213655","victory");
        s.getOutputStream().write(subscribeVictoryMessage.toByte());
        s.getOutputStream().flush();
    }

    private void ack(Socket s) throws IOException {
        Message subscribeVictoryMessage = messageBuilder.createSubscribeMessage("s213655","victory");
        s.getOutputStream().write(subscribeVictoryMessage.toByte());
        s.getOutputStream().flush();
    }



}

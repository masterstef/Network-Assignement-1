package subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            while (true){
                read(s);
                grid.printGrid();
                pushGuess(takeAGuess(),s);
                grid.resetSensors();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Position takeAGuess() {
        Position position = null;
        System.out.println();
        while (position == null) {
            System.out.println("Take a guess: ");
            Scanner sc = new Scanner(System.in);
            String guess = sc.nextLine();
            try {
                position = new Position(guess.substring(0,1),guess.substring(1));
                if (!grid.checkIndex(position.getRow(),position.getColumn())){
                    System.out.println("Index not in Sensors area try again");
                    position = null;
                }
            } catch (Exception e) {
                System.out.println("Not a correct position try again");
                position = null;
            }
        }
        return position;

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
            case 2 : handleAckMessage(currentmessage); break;
            default: throw new Exception("Message Type not handled");
        }
    }

    private void handleAckMessage(Message currentmessage) throws Exception {
        if (!currentmessage.getPlayload().get(0).equals("OK")) throw new Exception("Error in ack : " + currentmessage.getPlayload().get(1));
    }

    private void handleSubscribeMessage(Message currentmessage, Socket s) {
    }

    private void handlePublishMessage(Message currentmessage, Socket s) throws Exception {
        switch (currentmessage.getPlayload().get(0)){
            case "position" :
                grid.addSensor(new Sensor(currentmessage.getPlayload()));
                ack(s);
                break;
            case "victory" :
                System.out.println("/!\\ YOU GOT IT /!\\");
                grid.resetSensors();
                ack(s);
                break;
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

    private void pushGuess(Position guess, Socket s) throws IOException {
        Message subscribePositionMessage = messageBuilder.createPublishMessage("guess",guess.toString());
        s.getOutputStream().write(subscribePositionMessage.toByte());
        s.getOutputStream().flush();
    }

    private void ack(Socket s) throws IOException {
        Message ackMessage = messageBuilder.createAckMessage("OK");
        s.getOutputStream().write(ackMessage.toByte());
        s.getOutputStream().flush();
    }



}

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class SubscriberManager {

    private MessageBuilder messageBuilder = new MessageBuilder();
    private Grid grid = new Grid();
    private boolean continueTheGame = true;

    /**
     * main function used to open the socket to the server and manage all the game
     */
    public void start(){
        try (Socket s = new Socket("localhost",2655)){
            s.setSoTimeout(1000);
            s.setTcpNoDelay(true);
            System.out.println("Welcome to the Monster Hunting Game");
            subscribeVictory(s);
            subscribePosition(s);
            read(s);
            grid.print();
            pushGuess(takeAGuess(),s);
            grid.resetSensors();
            while (true){
                read(s);
                if(!continueTheGame) break;
                System.out.println("Whoops, you missed !");
                grid.print();
                pushGuess(takeAGuess(),s);
                grid.resetSensors();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (MessageException e) {
            System.out.println(e.getMessage());
        } catch (PositionException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * function used to record the user's guess and verify that it is correct
     */
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
            } catch (PositionException e) {
                System.out.println("Not a correct position try again");
                position = null;
            }
        }
        return position;

    }

    /**
     * Function used to read the messages received by the server and execute the appropriate procedure according to the message received
     * @param s : The socket linked to the server
     */
    private void read(Socket s) throws MessageException, IOException, PositionException {
        try {
            byte[] msg = new byte[64];
            s.getInputStream().read(msg);
            int i = 0;
            while(i < msg.length){
                if(i+2 > msg.length || msg[i+2] == 0) {
                    s.getInputStream().read(msg);
                    i = 0;
                    continue;
                }
                byte[] currentByteMessage = new byte[msg[i+2]+3];
                System.arraycopy(msg , i, currentByteMessage , 0, currentByteMessage.length);
                Message currentmessage = new Message(currentByteMessage);
                dispatch(currentmessage,s);
                i += msg[i+2]+3;
            }
        }catch (SocketTimeoutException e){}
    }

    /**
     * function determining the type of message received and executing the appropriate procedure
     * @param currentmessage : The read message
     * @param s : The socket linked to the server
     */
    private void dispatch(Message currentmessage, Socket s) throws MessageException, PositionException, IOException {
        switch (currentmessage.getType()){
            case 0 : handleSubscribeMessage(currentmessage,s); break;
            case 1 : handlePublishMessage(currentmessage,s); break;
            case 2 : handleAckMessage(currentmessage); break;
            default: throw new MessageException("Message Type not handled");
        }
    }

    /**
     * function used to manage received subscribe messages
     * @param currentmessage : The read message
     * @param s : The socket linked to the server
     */
    private void handleSubscribeMessage(Message currentmessage, Socket s) {
        //Message handled but nothing to do yet
    }

    /**
     * function used to manage received publish messages
     * @param currentmessage : The read message
     * @param s : The socket linked to the server
     */
    private void handlePublishMessage(Message currentmessage, Socket s) throws MessageException, IOException, PositionException {
        switch (currentmessage.getPlayload().get(0)){
            case "position" :
                grid.addSensor(new Sensor(currentmessage.getPlayload()));
                ack(s);
                break;
            case "victory" :
                System.out.println("/!\\ YOU GOT IT /!\\");
                ack(s);
                continueTheGame = false;
                break;
            default : throw new MessageException("Message Topic not handled : " + currentmessage.getPlayload().get(0));
        }
    }

    /**
     * function used to manage received ack messages
     * @param currentmessage : The read message
     */
    private void handleAckMessage(Message currentmessage) throws MessageException {
        if (!currentmessage.getPlayload().get(0).equals("OK")) throw new MessageException("Error in ack : " + currentmessage.getPlayload().get(1));
    }

    /**
     * Function used to send a subscribe position message to the server
     * @param s : The socket linked to the server
     */
    private void subscribePosition(Socket s) throws IOException {
        Message subscribePositionMessage = messageBuilder.createSubscribeMessage("s213655","position");
        s.getOutputStream().write(subscribePositionMessage.toByte());
        s.getOutputStream().flush();
    }

    /**
     * Function used to send a subscribe victory message to the server
     * @param s : The socket linked to the server
     */
    private void subscribeVictory(Socket s) throws IOException {
        Message subscribeVictoryMessage = messageBuilder.createSubscribeMessage("s213655","victory");
        s.getOutputStream().write(subscribeVictoryMessage.toByte());
        s.getOutputStream().flush();
    }

    /**
     * Function used to send a publish guess message to the server
     * @param guess : The position of the guess
     * @param s : The socket linked to the server
     */
    private void pushGuess(Position guess, Socket s) throws IOException {
        Message subscribePositionMessage = messageBuilder.createPublishMessage("guess",guess.toString());
        s.getOutputStream().write(subscribePositionMessage.toByte());
        s.getOutputStream().flush();
    }

    /**
     * Function used to send a ack message to the server
     * @param s : The socket linked to the server
     */
    private void ack(Socket s) throws IOException {
        Message ackMessage = messageBuilder.createAckMessage("OK");
        s.getOutputStream().write(ackMessage.toByte());
        s.getOutputStream().flush();
    }



}

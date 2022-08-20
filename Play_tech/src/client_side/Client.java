package client_side;

import javafx.scene.layout.VBox;
import util.ErrorHandler;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket,String userName) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        }catch (IOException e ){
            e.printStackTrace();
            System.out.println("error on client");
            ErrorHandler.closeEverything(socket,bufferedWriter,bufferedReader);
        }

    }

    public void sendMessageToServer(String messageToServer){
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error sending message to the client");
            ErrorHandler.closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }


    public void receiveMessageFromServer(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String messageFromClient  = bufferedReader.readLine();
                        Controller.addLabel(messageFromClient,vBox);
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("error reading message form client");
                        ErrorHandler.closeEverything(socket,bufferedWriter,bufferedReader);
                        break;
                    }
                }
            }
        }).start();
    }
}

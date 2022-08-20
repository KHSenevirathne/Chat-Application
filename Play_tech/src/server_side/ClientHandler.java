package server_side;

import util.ErrorHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
     try{
         this.socket = socket;
         this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         this.bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
         this.clientUsername = bufferedReader.readLine();
         clientHandlers.add(this);
         broadCastMessage("SERVER : "+clientUsername+" has entered the chat!");
     }catch (IOException e ){
         ErrorHandler.closeEverything(socket,bufferedWriter,bufferedReader);
     }

    }


    @Override
    public void run() {
        String  messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient=bufferedReader.readLine();
                broadCastMessage(messageFromClient);
            }catch (IOException e ){
                ErrorHandler.closeEverything(socket,bufferedWriter,bufferedReader);
                break;
            }
        }
    }


    public void broadCastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                ErrorHandler.closeEverything(socket,bufferedWriter,bufferedReader);
                removeClientHandler();
            }

        }
    }


    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("SERVER : " + clientUsername + "has left the chat");
    }
}

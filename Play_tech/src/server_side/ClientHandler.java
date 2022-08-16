package server_side;

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
         Scanner scanner = new Scanner(System.in);
         System.out.print("Please enter your user name for the chat(server) : " );
         this.clientUsername = scanner.nextLine();
         clientHandlers.add(this);
         broadCastMessage("SERVER : "+clientUsername+" has entered the chat!");
     }catch (IOException e ){
         closeEverything(socket,bufferedWriter,bufferedReader);
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
                closeEverything(socket,bufferedWriter,bufferedReader);
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
                closeEverything(socket,bufferedWriter,bufferedReader);
            }

        }
    }


    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("SERVER : " + clientUsername + "has left the chat");
    }


    public void closeEverything(Socket socket , BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        removeClientHandler();
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch (IOException e ){
            e.printStackTrace();
        }
    }
}

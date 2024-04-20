package org.example.MultiThreaded;

import org.example.DAOs.DAO;
import org.example.DTOs.Movie;
import org.example.JsonConverter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

/**
 * Main author: Joseph Byrne
 *
 */

public class Server {

    final int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start(){

        //initialize server
        ServerSocket serverSocket =null;
        //initialize client socket
        Socket clientSocket =null;

        try{

            //creating a new server socket
            serverSocket = new ServerSocket(SERVER_PORT_NUMBER);
            System.out.println("Server has now started");
            int clientNum = 0;

            //checking for incoming connections from the client
            while(true){

                System.out.println("Server: Listening/waiting for connections on port:" + SERVER_PORT_NUMBER);
                clientSocket = serverSocket.accept();
                clientNum++;
                System.out.println("Server: Listening for connections on port:" + SERVER_PORT_NUMBER);

                System.out.println("Server: Client " + clientNum + " has connected.");
                System.out.println("Server: Port number of remote client: " + clientSocket.getPort());
                System.out.println("Server: Port number of the socket used to talk with client " + clientSocket.getLocalPort());

                //starting a thread for each client connection
                //allows multiple clients to connect at once without interfering with each other
                Thread t = new Thread((Runnable) new ClientHandler(clientSocket, clientNum));
                t.start();

                System.out.println("Server: ClientHandler started in thread " + t.getName() + " for client " + clientNum + ". ");
            }
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

class ClientHandler implements Runnable{

    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket clientSocket;
    final int clientNum;

    public ClientHandler(Socket clientSocket, int clientNum){
        //initialise client number and socket
        this.clientNum = clientNum;
        this.clientSocket = clientSocket;

        try{
            //initialise socket writer and reader
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void run() {
        String request;

        try {
            while ((request = socketReader.readLine()) != null) {

                System.out.println("Server: (ClientHandler): Read command from client " + clientNum + ": " + request);

                if (request.startsWith("displayid")) {

                    //array splits the command in half (DisplayId, <ID>) using the delimiter " "
                    String[] splitCommand = request.split(" ");

                    //checks if there are 2 variables in the array, meaning the split was done correctly
                    if (splitCommand.length == 2) {
                        //gets the string at index 1 (ID) and parses it into an int
                        int id = Integer.parseInt(splitCommand[1].trim());

                        try {
                            Movie movie = DAO.getInstance().findMovieById(id); //calling the find by ID method in the DAO file

                            //making sure movie that was retrieved by id exists to avoid errors
                            if (movie != null) {

                                String jsonMovie = JsonConverter.movietoJson(movie); //converts obj movie to Json
                                socketWriter.println(jsonMovie); //sends the Json to client
                                System.out.println("Server Message: JSON movie was sent to the client");
                            }
                            else{
                                socketWriter.println("Error: Movie not found, with this ID"); //sends error to client
                                System.out.println("Server Message: Movie not found, with this ID");
                            }
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
                else if (request.startsWith("displayall")) {
                    try {
                        List<Movie> movies = DAO.getInstance().getAllMovies(); // Retrieve all movies from the DAO

                        // Check if any movies were found
                        if (!movies.isEmpty()) {
                            // Convert the list of movies to JSON
                            String jsonMovies = JsonConverter.moviesListToJson(movies);
                            socketWriter.println(jsonMovies); // Send the JSON to the client
                            System.out.println("Server Message: JSON movies were sent to the client");
                        } else {
                            socketWriter.println("Error: No movies found"); // Send error to client
                            System.out.println("Server Message: No movies found");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }


                else if (request.toLowerCase().startsWith("AddEntity")) {

                }
                else if (request.startsWith("Image")){
                    receiveFile("CA5_JosephByrne_MovieDB/images/TheGodFather_Image.jpeg");
                }
                else if (request.startsWith("Quit")) {
                    socketWriter.println("Sorry to see you leaving. Goodbye.");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }
                else {
                    socketWriter.println("error I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }

        finally {
            this.socketWriter.close();

            try {
                this.socketReader.close();
                this.clientSocket.close();
            }

            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Server: (ClientHandler): Handler for Client " + clientNum + " is terminating .....");
    }

    private void receiveFile(String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

        try {
            // DataInputStream allows us to read Java types from stream e.g. readLong()
            // read the size of the file
            long bytes_remaining = dataInputStream.readLong(); // bytes remaining to be read

            // create a buffer to receive the incoming bytes from the socket
            byte[] buffer = new byte[4 * 1024]; // 4 kilobyte buffer

            System.out.println("Server: Bytes remaining to be read from socket: " + bytes_remaining);
            int bytes_read; // number of bytes read from the socket

            // next, read the raw bytes in chunks (buffer size) that make up the image file
            while (bytes_remaining > 0 && (bytes_read = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, bytes_remaining))) != -1) {                // write the buffer data into the local file
                fileOutputStream.write(buffer, 0, bytes_read);

                // reduce the 'bytes_remaining' to be read by the number of bytes read
                bytes_remaining -= bytes_read;

                System.out.println("Server: Bytes remaining to be read from socket: " + bytes_remaining);
            }

            System.out.println("File is Received");
            socketWriter.println("File received successfully");
        } catch (IOException e) {
            e.printStackTrace();
            socketWriter.println("Error receiving file");
        } finally {
            fileOutputStream.close();
        }
    }

}
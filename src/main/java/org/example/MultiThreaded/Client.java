package org.example.MultiThreaded;

import org.example.DTOs.Movie;
import org.example.JsonConverter;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Main author: Joseph Byrne
 * Other contributors: Ema Eiliakas
 *
 */

public class Client {
    //Output stream for sending data to the server
    private static DataOutputStream dataOutputStream = null;
    //Input stream for recieving data from the server
    private static DataInputStream dataInputStream = null;
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start(){

        try(
                Socket socket = new Socket("Localhost", 8888);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream()); // Initialize DataOutputStream here
        ) {

            System.out.println("Client Message: The Client is running and has been connected to server");
            Scanner consoleInput = new Scanner(System.in);
            System.out.println("\nValid commands include: DisplayID <ID>, DisplayAll, AddEntity, DeleteEntity, ImageList, Quit");
            System.out.println("Enter your command:");
            String userCommand = consoleInput.nextLine().toLowerCase();

            while (true) {

                out.println(userCommand); //this is required to have(writes the request to socket along with a newline terminator.

                //types of commands go here, Process the answers returned by the server
                if(userCommand.startsWith("displayid")) {

                    //sends command to server
                    String jsonMovie = in.readLine(); //receives response from server

                    Movie movie = JsonConverter.jsonToMovie(jsonMovie, Movie.class); //converting Json movie back to an object

                    System.out.println("\nMovie Found!");

                    System.out.printf("%-5s %-20s %-12s %-22s %-24s %-12s %-10s\n",
                            "ID", "Title", "Release", "Genre", "Director", "Runtime", "Rating");

                    System.out.printf("%-5d %-20s %-12d %-22s %-24s %-12d %.2f\n",
                            movie.getMovie_id(), movie.getTitle(), movie.getRelease_year(),
                            movie.getGenre(), movie.getDirector(),movie.getRuntime_minutes(),movie.getRating());

                }

                else if(userCommand.startsWith("displayall")) {
                    String jsonMovies = in.readLine();

                    List<Movie> movies = JsonConverter.jsonToMovies(jsonMovies);

                    System.out.printf("%-5s %-20s %-12s %-22s %-24s %-12s %-10s\n",
                            "ID", "Title", "Release", "Genre", "Director", "Runtime", "Rating");

                    for (Movie movie : movies) {
                        System.out.printf("%-5d %-20s %-12d %-22s %-24s %-12d %.2f\n",
                                movie.getMovie_id(), movie.getTitle(), movie.getRelease_year(),
                                movie.getGenre(), movie.getDirector(),movie.getRuntime_minutes(),movie.getRating());
                    }
                }
                else if(userCommand.startsWith("AddEntity")) {

                }
                else if(userCommand.startsWith("image")){
                    System.out.println("Sending the File to the Server");
                    // Call SendFile Method
                    sendFile("CA5_JosephByrne_MovieDB/images/TheGodFather_Image.jpeg");   // we are hard coding the location
                    dataOutputStream.close();
                }

                else if(userCommand.startsWith("quit")){
                    String response = in.readLine(); //Waits for response
                    System.out.println("Client Message: Response from server: " + response);

                    break;
                }
                else{
                    System.out.println("Command not known, please try again\n");
                }

                //end of while statement clears and prompts the user for a new command
                //consoleInput = new Scanner(System.in);
                System.out.println("Valid commands include: DisplayID<ID>, DisplayAll, AddEntity , Quit");
                System.out.println("Enter your command:");
                userCommand = consoleInput.nextLine().toLowerCase();
            }
        }

        catch (Exception e){
            System.out.println("Client Message: IO Exception: " + e);
        }

        System.out.println("Exiting Client, check if server may still be running.");

    }
    public static void sendFile(String path) throws Exception{
        int bytes = 0;
        //Specific file location
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        //sending the length of the file to the server
        dataOutputStream.writeLong(file.length());

        //Breaking of file into chunks(bytes)
        byte[] buffer = new byte[4 * 1024];

        //reading the bytes from file
        while((bytes = fileInputStream.read(buffer)) != -1){
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush(); //needed to force the data into the stream
        }
        fileInputStream.close();
    }
}


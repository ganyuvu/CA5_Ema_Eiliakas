package org.example.MultiThreaded;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main author: Joseph Byrne
 * Other contributors: Ema Eiliakas
 *
 */

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start(){

        try(
                Socket socket = new Socket("Localhost", 8888);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {

            System.out.println("Client Message: The Client is running and has been connected to server");
            Scanner consoleInput = new Scanner(System.in);
            System.out.println("Valid commands include: ViewId<ID>, ViewAll, AddEntity , Quit");
            System.out.println("Enter your command:");
            String userCommand = consoleInput.nextLine();

            while (true) {

                out.println(userCommand); //this is required to have(writes the request to socket along with a newline terminator.

                //types of commands go here, Process the answers returned by the server
                //lots of if, elif and else statements.
                if(userCommand.startsWith("ViewId")) {
                    //Command1 code goes here, fill out after discussing with group what we want
                }
                else if(userCommand.startsWith("ViewAll")) {
                    //Command2 code goes here, fill out after discussing with group what we want
                }
                else if(userCommand.startsWith("AddEntity")) {
                    //Command3 code goes here, fill out after discussing with group what we want
                }
                else if(userCommand.startsWith("Quit")){
                    String response = in.readLine(); //Waits for response
                    System.out.println("Client Message: Response from server: " + response);

                    break;
                }
                else{
                    System.out.println("Command not known, please try again");
                }

                //end of while statement clears and prompts the user for a new command
                consoleInput = new Scanner(System.in);
                System.out.println("Valid commands include: ViewId<ID>, ViewAll, AddEntity , Quit");
                System.out.println("Enter your command:");
                userCommand = consoleInput.nextLine();
            }
        }

        catch (IOException e){
            System.out.println("Client Message: IO Exception: " + e);
        }

        System.out.println("Exiting Client, check if server may still be running.");

    }
}
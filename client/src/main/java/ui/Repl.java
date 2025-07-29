package ui;

import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.HashSet;

public class Repl implements MessagePrinter{

    ChessClient client = new ChessClient("http://localhost:3306");

    public void run(String [] args){
        System.out.println("Hello and welcome to Eden's Chess Game. Please sign in to start playing");
        while(true) {
            String outputString = null;
            if (args[0].equals("help")) {
                client.preloginHelp();
            } else if (args[0].equals("quit")){
                break;
            }

            System.out.print(outputString);
        }
    }
    // need to do all the pre-loginUI

    public void help(){
        System.out.print("Register: supply username, password, email \n");
        System.out.print("Login: username and password");
        System.out.print("Quit: exit the program");
    }

    public void login(String [] args){
        String username = args[1];
        String password = args[2];
        // create LoginRequest here or make it in another class
        // server.login(request)
    }

    public void register(String [] args){
        String username = args[1];
        String password = args[2];
        String email = args[3];
    }
    // if input == help, display:
    // quit - exits the program
    // register - need username, password, email
    // login - need username and password

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}

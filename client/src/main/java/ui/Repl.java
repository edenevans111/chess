package ui;

import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessPosition;
import dataaccess.DataAccessException;

import java.util.HashSet;
import java.util.Scanner;

public class Repl implements MessagePrinter{

    ChessClient client = new ChessClient("http://localhost:3306");

    // I should have three different repl loops (prelogin, postlogin)

    public void run(String [] args){
        System.out.println("Hello and welcome to Eden's Chess Game. Please login to start playing");
        System.out.print(client.preloginHelp());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            String line = scanner.nextLine();

            try{
                result = client.eval(line);
                System.out.print(result);
            } catch(DataAccessException e){
                var msg = e.getMessage();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    // need to do all the pre-loginUI

    public void help(){
        System.out.print("Register: supply username, password, email \n");
        System.out.print("Login: username and password");
        System.out.print("Quit: exit the program");
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

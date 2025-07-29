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

    public void run(){
        System.out.println(SET_TEXT_COLOR_WHITE + "Welcome to Eden's Chess Game. Please login to start playing");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (true){
            printPrompt();
            String line = scanner.nextLine();
            if(line.equals("quit")){
                break;
            }
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

    private void printPrompt(){
        System.out.print("\n" + RESET_TEXT_COLOR + ">>>" + SET_TEXT_COLOR_BLUE);
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

}

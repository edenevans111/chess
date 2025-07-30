package ui;

import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessPosition;
import serverfacade.ResponseException;

import java.util.HashSet;
import java.util.Scanner;

public class Repl implements MessagePrinter{

    ChessClient client = new ChessClient("http://localhost:8080");

    public void run(){
        System.out.println(SET_TEXT_COLOR_WHITE + "Welcome to Eden's Chess Game. Please login to start playing");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            if(line.equals("quit")){
                break;
            }
            try{
                result = client.eval(line);
                System.out.print(result);
            } catch(ResponseException e){
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

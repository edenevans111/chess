package ui;

import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.HashSet;

public class Repl implements MessagePrinter{

    public void run(){
        System.out.println("Hello and welcome to Eden's Chess Game. Please sign in to start playing");
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}

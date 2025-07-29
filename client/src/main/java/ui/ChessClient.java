package ui;

public class ChessClient {
    // handles all the logic for the Repl parameters
    // validation of the input
    public ChessClient(){

    }

    public String help(){
        StringBuilder helpString = new StringBuilder();
        System.out.print("Register: supply username, password, email\n");
        System.out.print("Login: username and password\n");
        System.out.print("Quit: exit the program\n");
        return helpString.toString();
    }

    public String login(String [] args){
        String username = args[1];
        String password = args[2];
        // now I need to make a LoginRequest and call the ServerFacade
    }

}

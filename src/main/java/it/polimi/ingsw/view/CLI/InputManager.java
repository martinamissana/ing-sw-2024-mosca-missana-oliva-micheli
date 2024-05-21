package it.polimi.ingsw.view.CLI;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

public class InputManager {
    Scanner input = new Scanner(System.in);
    private final PipedOutputStream pos;
    private final PipedInputStream pis;
    private boolean pipedInput = false;

    public InputManager () {
        this.pos = new PipedOutputStream();
        try {
            this.pis = new PipedInputStream(pos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setPipedInput(false);
    }

    public void setPipedInput (boolean in) {
        this.pipedInput = in;
        if (pipedInput) System.setIn(pis);
        else System.setIn(System.in);
    }

    public String askInput (String message) {
        if (!pipedInput) {
            String string = "";
            System.out.print(message);
            while (string.isEmpty()) string = input.nextLine();

            return string;
        } else {
            giveInput("console");
            setPipedInput(false);
            return "console";
        }
    }

    public String askInput(String message, String defaultValue) {
        if (!pipedInput) {
            System.out.print(message);
            String userInput = input.nextLine();
            if (userInput.isEmpty()) {
                userInput = defaultValue;
            }
            return userInput;
        } else {
            giveInput(defaultValue);
            setPipedInput(false);
            return defaultValue;
        }
    }

    public void giveInput (String in) {
        try {
            pos.write((in + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNumeric(String s){
        return s.matches("-?\\d+");
    }

}

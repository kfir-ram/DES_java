import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        System.out.println("~~~~~~~~~~~~~~~~~~~ Let's encrypt using DES! ~~~~~~~~~~~~~~~~~~~\n\nPlease enter a valid Key (with a size of 8 characters):");
        String key = userInput.nextLine();
        if (key.length() == 8){
            System.out.println("Please enter the Words:");
            String msg = userInput.nextLine();
            DES des = new DES();

            String encrypted_msg = des.Cipher(msg, key, 1);
            des.Cipher(encrypted_msg, key, 2);
        }
        else
            System.out.println("The key you entered is not valid (the size of it is " + key.length() + " characters).");
    }
}




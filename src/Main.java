import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        System.out.println("~~~~~~~~~~~~~~~~~~~ Let's encrypt using DES! ~~~~~~~~~~~~~~~~~~~\nPlease enter the Key:");
        String key = userInput.nextLine();
        System.out.println("Please enter the Words:");
        String msg = userInput.nextLine();
        DES des = new DES();

        String encrypted_msg = des.Cipher(msg, key, 1);

        des.Cipher(encrypted_msg, key, 2);
    }
}




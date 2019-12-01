import java.math.BigInteger;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args)
    {

        // Part 1 - Key generation
        KeyGeneration keyProccess = new KeyGeneration();
        String binaryKey =  keyProccess.ConvertBinary("nonsense");
        //System.out.println("Binary key is: " +binaryKey);
        String pcKey = keyProccess.PC(binaryKey,1);
        ArrayList <String> Keys = keyProccess.split_and_round(pcKey);
        System.out.println("Keys created are: " +Keys);

        //Part 2 -
        String msg = "thoughts";
        String key = "nonsense";
        DES des = new DES();
        String encripte_msg = des.Cipher(msg, key);

        System.out.println(encripte_msg);

    }
}



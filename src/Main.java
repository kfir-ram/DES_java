import java.math.BigInteger;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args)
    {

        //Type 1 = Encrypt, Type 2 = Decrypt.
        String msg = "thoughts";
        String key = "nonsense";
        DES des = new DES();
        String encrypte_msg = des.Cipher(msg, key, 1);
        System.out.println("*** START:! " + encrypte_msg);

        String decrypt_msg = des.Cipher(encrypte_msg, key, 2);

        System.out.println("*** FINAL:! " + decrypt_msg);

    }
}



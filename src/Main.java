import java.math.BigInteger;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args)
    {

        String msg = "thoughts";
        String key = "nonsense";
        DES des = new DES();
        String encripte_msg = des.Cipher(msg, key);

        System.out.println(encripte_msg);

    }
}



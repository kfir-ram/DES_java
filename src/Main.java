import java.math.BigInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        String msg = "thoughts";
        String key = "nonsense";
        DES des = new DES();
        String encripte_msg = des.Cipher(msg, key);

        System.out.println(encripte_msg);



    }

}

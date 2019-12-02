import java.math.BigInteger;
import java.util.ArrayList;

class DES {

    String Cipher(String msg, String key, int type) {
        // Part 1 - Key generation
        KeyGeneration keyProcess = new KeyGeneration();
        String binaryKey =  keyProcess.ConvertBinary(key);
        String pcKey = keyProcess.PC(binaryKey,1);
        ArrayList keys_list = keyProcess.split_and_round(pcKey);

        ArrayList<String> packages;
        if (type == 1)
            packages = make_packages(msg, 1);
        else
            packages = make_packages(msg, 0);

        ArrayList<String> encrypted_packages = new ArrayList<>();
        ArrayList<String> decrypted_packages = new ArrayList<>();

        //sending every single package into encryption
        if (type == 1) {
            for (String aPackage : packages)
                encrypted_packages.add(encrypt(aPackage, keys_list));
        }
        else {
            for (String aPackage : packages)
                decrypted_packages.add(decrypt(aPackage, keys_list));
        }
        StringBuilder cipher_msg = new StringBuilder();

        //END PART - grab together all of the packages.
        if (type == 1)
        {
            for (String encrypted_package : encrypted_packages)
                cipher_msg.append(encrypted_package);
        }
        if (type == 2)
        {
            for (int i = 0; i < decrypted_packages.size() - 1; i++)
                cipher_msg.append(decrypted_packages.get(i));

            StringBuilder last_part = new StringBuilder(decrypted_packages.get(decrypted_packages.size() - 1));
            while (last_part.charAt(0) == '0')
                last_part = new StringBuilder(last_part.substring(1));
            while (last_part.length() % 8 != 0)
                last_part.insert(0, "0");
            cipher_msg.append(last_part);
        }

        //Convert the binary to String.
        String get_msg = new String(new BigInteger(cipher_msg.toString(), 2).toByteArray());
        if (type == 1) {
            System.out.println("the encrypted msg is: "+ get_msg);
        }
        else {
            System.out.println("the decrypted msg is: "+ get_msg);
        }
        return cipher_msg.toString();
    }

    //a function that splits the message into packages of 64bit each. (type 0 = binary code, type 1 = english sentence)
    private ArrayList<String> make_packages(String msg, int type){
        ArrayList<String> packages = new ArrayList<>();
        String binary_msg = "";
        //convert the original message to binary code.
        if (type == 1){
            binary_msg = new BigInteger(msg.getBytes()).toString(2);
            binary_msg = "0" + binary_msg;
        }
        else
        binary_msg += msg;

        //cutting the message into 64 bits each
        while (binary_msg.length() > 64)
        {
            packages.add(binary_msg.substring(0,64));
            binary_msg = binary_msg.substring(64);
        }
        //for the last package (if it exist), I am filling it with many zeros at the end.
        // in order to make it also 64 bit.
        if (binary_msg.length() > 0)
        {
            String adder = new String(new char[64 - binary_msg.length()]).replace('\0', '0');
            binary_msg = adder.concat(binary_msg);
            packages.add(binary_msg);
        }
        return packages;
    }

    private String s_boxes(int box, int iRow, int iColumn){

        int[] sbox1 = { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
                0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14,
                8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9,
                1, 7, 5, 11, 3, 14, 10, 0, 6, 13 };
        int[] sbox2 = { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
                3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7,
                11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3,
                15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 };
        int[] sbox3 = { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
                13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4,
                9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9,
                8, 7, 4, 15, 14, 3, 11, 5, 2, 12 };
        int[] sbox4 = { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
                13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9,
                0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1,
                13, 8, 9, 4, 5, 11, 12, 7, 2, 14 };
        int[] sbox5 = { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
                14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1,
                11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1,
                14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 };
        int[] sbox6 = { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
                10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14,
                15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9,
                5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 };
        int[] sbox7 = { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
                13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11,
                13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4,
                10, 7, 9, 5, 0, 15, 14, 2, 3, 12 };
        int[] sbox8 = { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
                1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4,
                1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10,
                8, 13, 15, 12, 9, 0, 3, 5, 6, 11 };

        int code = 16 * iRow + iColumn;

        switch (box) {
            case (1):
                return Integer.toBinaryString(sbox1[code]);

            case (2):
                return Integer.toBinaryString(sbox2[code]);

            case (3):
                return Integer.toBinaryString(sbox3[code]);

            case (4):
                return Integer.toBinaryString(sbox4[code]);

            case (5):
                return Integer.toBinaryString(sbox5[code]);

            case (6):
                return Integer.toBinaryString(sbox6[code]);

            case (7):
                return Integer.toBinaryString(sbox7[code]);

            case (8):
                return Integer.toBinaryString(sbox8[code]);

            default:
                return null;
        }
    }

    private String encrypt(String msg, ArrayList<String> keys_list){
        String mL, mR, new_mL, f_box;

        msg = ip(msg);

        for (int i = 0; i < 16; i++) {
            mL = msg.substring(0, 32);
            mR = msg.substring(32);

            f_box = The_F_Function(mR, keys_list.get(i));

            new_mL = XOR(mL, f_box);

            if (i != 15)
                msg = "" + mR + new_mL;
            else
                msg = "" + new_mL + mR;
        }
        msg = ipInverse(msg);
        return msg;
    }

    private String decrypt(String msg, ArrayList<String> keys_list){
        String mL, mR, new_mL, f_box;
        msg = ip(msg);

        for (int i = 15; i >= 0; i--) {
            mL = msg.substring(0, 32);
            mR = msg.substring(32);

            f_box = The_F_Function(mR, keys_list.get(i));
            new_mL = XOR(mL, f_box);

            if (i != 0)
                msg = "" + mR + new_mL;
            else
                msg = "" + new_mL + mR;
        }
        msg = ipInverse(msg);
        return msg;
    }

    //initial Permutation, input should be binary string
    private String ip(String input){
        int[] ip =
                {
                        58, 50, 42, 34, 26, 18, 10 , 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14,
                        6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53,
                        45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
                };

        StringBuilder msg = new StringBuilder();

        char [] MsgArray = input.toCharArray();
        for (int i=0; i<ip.length; i++)
            msg.append(MsgArray[ip[i] - 1]);
        return msg.toString();
    }

    //final Permutation, input should be binary string
    private String ipInverse(String input){
        int[] ip =
                {
                        40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
                        36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43 ,11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25
                };

        StringBuilder msg = new StringBuilder();
        char [] MsgArray = input.toCharArray();

        for (int i=0; i<ip.length; i++)
            msg.append(MsgArray[ip[i] - 1]);

        return msg.toString();
    }

    //Expansion E - expand the string from 32 to 48 bits
    private String E(String input){
        int[] e =
                {
                        32,  1,  2,  3,  4,  5, 4,  5,  6,  7,  8,  9, 8,  9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
                        16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,  1
                };

        StringBuilder res = new StringBuilder();
        char [] MsgArray = input.toCharArray();

        for (int i=0; i < e.length; i++)
            res.append(MsgArray[e[i] - 1]);

        return res.toString();
    }

    //Permutation after the S_boxes
    private String P(String input){
        int[] p =
                {
                        16,7,20,21,29,12,28,17,
                        1,15,23,26,5,18,31,10,
                        2,8,24,14,32,27,3,9,
                        19,13,30,6,22,11,4,25
                };
        StringBuilder res = new StringBuilder();
        char [] MsgArray = input.toCharArray();

        for (int i=0; i < p.length; i++)
            res.append(MsgArray[p[i] - 1]);


        return res.toString();

    }

    //function that perform xor operator on 2 binary strings
    private String XOR (String msg, String Key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < msg.length(); i++)
            sb.append(msg.charAt(i)^Key.charAt(i));
        return sb.toString();
    }

    // F function inculde 4 stages init: 1.Expansion E, 2.XOR with round key, 3.S boxes, 4.Permutation
    private String The_F_Function (String MsgRight, String CurrentKey) {
        //Step 1 - Expansion E
        String ExpandMsg= E(MsgRight);
        //Step 2 - Xor with the key
        String XorMsg = XOR(ExpandMsg,CurrentKey);
        //step 3 - S boxes
        //cutting the message to packets of 6 byts
        ArrayList <String> pre_Sbox = new ArrayList<>();
        ArrayList <String> post_Sbox = new ArrayList<>();

        for (int i=0; i < 8; i++){
            pre_Sbox.add(XorMsg.substring(0, 6));
            XorMsg = XorMsg.substring(6);
        }

        //getting the coulmn and the row of each box.
        String coulmn, row;
        for (int i=0; i < 8; i++){
            row = "" + pre_Sbox.get(i).charAt(0) + pre_Sbox.get(i).charAt(5);
            coulmn = "" + pre_Sbox.get(i).substring(1,5);
            int iRow = Integer.parseInt(row, 2);
            int iColumn = Integer.parseInt(coulmn, 2);
            String output = ""+s_boxes(i+1, iRow, iColumn);
            post_Sbox.add(output);
        }

        for(int i = 0; i < 8; i++){
            if(post_Sbox.get(i).length() < 4){
                post_Sbox.set(i,"0" + post_Sbox.get(i));
                i --;
            }
        }
        StringBuilder sbox_out = new StringBuilder();
        for (int i = 0; i < 8; i++){
            sbox_out.append(post_Sbox.get(i));
        }
        sbox_out = new StringBuilder(P(sbox_out.toString()));

        return sbox_out.toString();
    }




}


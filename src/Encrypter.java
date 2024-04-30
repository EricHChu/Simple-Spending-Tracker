/**
 * Encrypter class for Financial Planner
 * @author Eric Chu
 * @version Jun 2023
 */

import java.io.File;
import java.io.*;
import java.util.ArrayList;
public class Encrypter {
    private String key;

    /**
     * Constructor for Encrypter with encryption key
     * @param key
     */
    public Encrypter(String key){
        this.key = key;
    }

    /**
     * Encrypts file according to key
     * @param file File to encrypt
     * @throws IOException
     */
    public void encrypt(File file)throws IOException{
        //Gets file
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ArrayList<Integer> bytes = new ArrayList<>();
        int c;
        //reads file
        while((c = bis.read()) != -1){
            bytes.add(c);
        }
        bis.close();
        //gets file again
        PrintWriter writer = new PrintWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        int letter = 0;
        Integer[] arrBytes = bytes.toArray(new Integer[bytes.size()]);
        //Encrypts according to key
        for(int i:arrBytes){
            i = i + (int)key.charAt(letter);
            bw.write("" + i + " ");
            letter++;
            if(letter == key.length()){
                letter = 0;
            }
        }
        bw.close();
    }

    /**
     * Decrypts file according to key
     * @param file File to decrypt
     * @throws IOException
     */
    public void decrypt(File file) throws IOException{
        //Gets and reads file
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        String[] bytes = br.readLine().split(" ");
        br.close();
        //Gets output file
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int letter = 0;
        //Decrypts file according to key
        for(String byteStr:bytes){
            int i = Integer.parseInt(byteStr);
            i = i - (int)key.charAt(letter);
            bos.write(i);
            letter++;
            if(letter == key.length()){
                letter = 0;
            }
        }
        bos.close();
    }
}

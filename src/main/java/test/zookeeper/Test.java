package test.zookeeper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by lins13 on 3/29/17.
 */
public class Test {

    public static void main(String[] args) {
        try{
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "ls -l"});
            p.waitFor();
            InputStreamReader in = new InputStreamReader(p.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            String lineStr;
            while((lineStr = bufferedReader.readLine()) != null) {
                System.out.println(lineStr);
            }
            System.out.println();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}

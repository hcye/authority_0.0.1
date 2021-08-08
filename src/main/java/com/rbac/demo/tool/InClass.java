package com.rbac.demo.tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InClass {
    public static void main(String[] args) {
        execCommand("ls");
    }
    public static void execCommand(String cmd){

        try{

            Runtime rt = Runtime.getRuntime();

            Process proc = rt.exec(cmd,null,null);

            InputStream stderr = proc.getInputStream();

            InputStreamReader isr = new InputStreamReader(stderr,"GBK");

            BufferedReader br = new BufferedReader(isr);

            String line="";

            while ((line = br.readLine()) != null) {

                System.out.println(line);

            }

        }catch (Exception e){

            e.printStackTrace();

        }

    }
}

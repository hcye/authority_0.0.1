package com.rbac.demo.tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExecShell {
    public static String execCommand(String cmd) {
        String lines = "";
        try {

            Runtime rt = Runtime.getRuntime();

            Process proc = rt.exec(cmd, null, null);

            InputStream stderr = proc.getInputStream();

            InputStreamReader isr = new InputStreamReader(stderr, "GBK");

            BufferedReader br = new BufferedReader(isr);


            String line = "";
            while ((line = br.readLine()) != null) {

                lines += line;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        return lines;
    }
}

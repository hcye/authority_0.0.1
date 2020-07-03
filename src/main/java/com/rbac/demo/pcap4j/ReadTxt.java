package com.rbac.demo.pcap4j;

import com.rbac.demo.entity.Oui;
import com.rbac.demo.jpa.JpaOui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
@Component
public class ReadTxt {
   public void read(JpaOui jpaOui) throws IOException {
       BufferedReader reader=new BufferedReader(new FileReader("D:\\vmware\\RBAC-0.0.1\\src\\main\\resources\\static\\file\\oui.txt"));
       Stream<String> lines=reader.lines();
       Iterator<String > iterator =lines.iterator();
        while (iterator.hasNext()){
            String line=iterator.next();
            if(line.length()<8){
                continue;
            }else if(!line.contains("(hex)")){
                continue;
            }
            String[] strings=line.split("\\(hex\\)");
            Oui oui=new Oui();
            String mac=strings[0].trim();
            mac=mac.toLowerCase().replace("-",":");
            oui.setMacHead(mac.trim());
            oui.setComInfo(strings[1].trim());
            jpaOui.save(oui);
        }
   }
}

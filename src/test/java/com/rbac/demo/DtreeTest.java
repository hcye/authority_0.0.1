package com.rbac.demo;

import com.rbac.demo.dtree.GetDtreeList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DtreeTest {
    @Autowired
    GetDtreeList list;
    @Test
    public void testSort(){
       /*System.out.println(list.getDtrees());*/
    }
}

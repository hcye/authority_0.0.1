package com.rbac.demo;

import com.rbac.demo.dtree.Dtree;
import com.rbac.demo.dtree.imp.GroupDtreeList;
import com.rbac.demo.tool.RepoUrlCheck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DtreeListTest {
    @Autowired
    private GroupDtreeList groupDtreeList;
    @Test
    public void test(){
        RepoUrlCheck.authSvn("svn://172.32.0.215/myp1ro","yehangcheng","yehangcheng");
    }
}

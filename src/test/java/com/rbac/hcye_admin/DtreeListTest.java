package com.rbac.hcye_admin;

import com.rbac.hcye_admin.dtree.imp.GroupDtreeList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DtreeListTest {
    @Autowired
    private GroupDtreeList groupDtreeList;
    @Test
    public void test(){
      //  RepoUrlCheck.authSvn("svn://172.32.0.215/myp1ro","yehangcheng","yehangcheng");
        // RepoUrlCheck.authGit("http://yehangcheng@gerrit2.hsaecd.com:80/a/hsae/ioc/nxp/s32k","","");
        //ExecShell.execCommand("netstat");
    }
}

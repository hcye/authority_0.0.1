package com.rbac.demo.init;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.SysGroup;
import com.rbac.demo.entity.User2Role;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaGroup;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.jpa.JpaUser2Role;
import com.rbac.demo.shiro.ShiroUtils;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.Date;


@Component
public class InitFunction implements CommandLineRunner {
    @Autowired
    private  JpaRole jpaRole;
    @Autowired
    private JpaGroup jpaGroup;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaUser2Role jpaUser2Role;

    @Override
    public void run(String... args) throws Exception {
        /**
         * 初始化超级管理员
         * */
        Role role=jpaRole.findRoleByAuthorityCode("administrator");
        if(role==null){
            Role role1=new Role();
            role1.setAvalible((byte) 1);
            role1.setAuthorityCode("administrator");
            role1.setCreateTime(new Timestamp(new Date().getTime()));
            role1.setRname("超级管理员");
            role1.setDeleteFlag((short) 0);
            role=jpaRole.saveAndFlush(role1);

        }

        Employee employee =jpaEmployee.findEmployeeByLoginName("root");
        if(employee==null){
            Employee admin=new Employee();
            admin.setEname("sys_root");
            admin.setStatus((byte) 0);
            admin.setLoginName("sys_root");
            admin.setOnjob("0");
            admin.setPingyin("root");
            if(jpaGroup.findAll().size()==0){
                SysGroup sysGroup=new SysGroup();
                sysGroup.setAvalible((byte) 1);
                sysGroup.setGname("深圳航盛");
                sysGroup.setDeleteFlag((byte) 0);
               admin.setSysGroupByGroupId(jpaGroup.saveAndFlush(sysGroup));
            }else {
                admin.setSysGroupByGroupId(jpaGroup.findAll().get(0));
            }
            String encryptPwd= ShiroUtils.encryption("toor", ByteSource.Util.bytes("root").toHex());
            admin.setPwd(encryptPwd);
            admin=jpaEmployee.saveAndFlush(admin);
            User2Role user2Role=new User2Role();
            user2Role.setEmployeeByUserId(admin);
            user2Role.setRoleByRoleId(role);
            jpaUser2Role.save(user2Role);
        }
    }
}

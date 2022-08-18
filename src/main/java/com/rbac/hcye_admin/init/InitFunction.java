package com.rbac.hcye_admin.init;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class InitFunction implements CommandLineRunner {

    @Override
    public void run(String... args) {
        /**
         * 初始化超级管理员
         * */
//        Role role=jpaRole.findRoleByAuthorityCode("administrator");
//        if(role==null){
//            Role role1=new Role();
//            role1.setAvalible((byte) 1);
//            role1.setAuthorityCode("administrator");
//            role1.setCreateTime(new Timestamp(new Date().getTime()));
//            role1.setRname("超级管理员");
//            role1.setDeleteFlag((short) 0);
//        }

    }
}

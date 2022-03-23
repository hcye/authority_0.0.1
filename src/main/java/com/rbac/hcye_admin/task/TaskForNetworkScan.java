package com.rbac.hcye_admin.task;

import com.rbac.hcye_admin.service.network.SnmpCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
class SaticScheduleTask {
    @Autowired
    private SnmpCore snmpCore;

    //3.添加定时任务
    @Scheduled(cron = "* 15 12 * * ?")
    //@Scheduled(fixedRate=5000)
    private void configureTasks() throws Exception {
        snmpCore.getIpMapInteger();
    }
}
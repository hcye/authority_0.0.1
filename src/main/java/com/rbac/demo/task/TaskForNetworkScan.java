package com.rbac.demo.task;

import com.rbac.demo.service.network.SnmpCore;
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
    @Scheduled(cron = "* 0 11 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() throws Exception {
        snmpCore.getIpMapInteger();
    }
}
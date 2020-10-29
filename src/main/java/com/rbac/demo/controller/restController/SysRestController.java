package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.AsmAction;
import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.EchangeDevs;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaExchangeDevs;
import com.rbac.demo.service.AsmRecordService;
import com.rbac.demo.tool.JSON;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SysRestController {
    @Autowired
    private JpaExchangeDevs jpaExchangeDevs;
    @Autowired
    private JpaAssert jpaAssert;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private AsmRecordService asmRecordService;

    @PostMapping("/sys/sys_even")
    public Map<String, String> sysEvent() {
        Map<String, String> map = new HashMap<>();
        List<EchangeDevs> list = jpaExchangeDevs.findAll();
        Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(list.size()==0){
            return map;
        }
        for (EchangeDevs echangeDevs : list) {
            Employee resiverFK = echangeDevs.getResiverFK();
            Employee senderFK = echangeDevs.getSenderFK();
            Employee currentBro=echangeDevs.getDevFK().getEmployeeByBorrower();
            /**
             *
             * 别人发给我的申请
             *
             * */
            if (resiverFK.getId().equals(employee.getId()) && currentBro.getId() .equals(employee.getId())&& echangeDevs.getIsDone().equals("0")&& echangeDevs.getReceived().equals("")) {
                if(!currentBro.getId().equals(employee.getId())&& !echangeDevs.getReceived().equals("")){
                    /**
                     * 资产已经不在接收人手里。
                     * */
                    echangeDevs.setReceived("error");
                    jpaExchangeDevs.save(echangeDevs);
                    map.put("error", "");
                    return map;
                }
                map.put("exchange", "reseive:/sys/exchange_resp");
                return map;
            }
            /**
             *
             * 我发给别人的申请,并且对方处理了
             *
             * */




            if (senderFK != null && senderFK.getId().equals(employee.getId())  && echangeDevs.getIsDone().equals("0")) {
                /**
                 *
                 * 我发给别人的申请,并且对方接受了
                 *
                 * */
                if (echangeDevs.getReceived() != null && echangeDevs.getReceived().equals("agree")) {
                    map.put("exchange", echangeDevs.getResiverFK().getEname() + "同意了你的资产流转!");
                    echangeDevs.setSenderFK(null);
                    echangeDevs.setDevFK(null);
                    echangeDevs.setResiverFK(null);
                    jpaExchangeDevs.delete(echangeDevs);
                    System.out.println("删除了");
                    return map;
                }
                /**
                 *
                 * 我发给别人的申请,并且对方拒绝了
                 *
                 * */
                if (echangeDevs.getReceived() != null && echangeDevs.getReceived().equals("disagree")) {
                    map.put("exchange", echangeDevs.getResiverFK().getEname() + "拒绝了你的资产流转!");
                    echangeDevs.setSenderFK(null);
                    echangeDevs.setDevFK(null);
                    echangeDevs.setResiverFK(null);
                    jpaExchangeDevs.delete(echangeDevs);
                    return map;
                }

                /**
                 *
                 * 资产在流转过程中持有者已经发生变化
                 *
                 * */
                if (echangeDevs.getReceived() != null && echangeDevs.getReceived().equals("error")) {
                    map.put("exchange",  "资产已被流转到："+echangeDevs.getDevFK().getEmployeeByBorrower().getEname());
                    echangeDevs.setSenderFK(null);
                    echangeDevs.setDevFK(null);
                    echangeDevs.setResiverFK(null);
                    jpaExchangeDevs.delete(echangeDevs);
                    return map;
                }

            }
        }
        return map;
    }

    @PostMapping("/sys/getExchangeEven")
    public Map<String, Object> getEven() {
        Map<String, Object> map = new HashMap<>();
        List<EchangeDevs> echangeDevs = jpaExchangeDevs.findAll();
        Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        for (EchangeDevs echange : echangeDevs) {
            if (echange.getReceived().equals("") && echange.getIsDone().equals("0") && echange.getResiverFK().getId() .equals(employee.getId()) ) {
                map.put("sender", echange.getSenderFK().getEname() + "-" + echange.getSenderFK().getLoginName());
                map.put("reason", echange.getReason());
                map.put("sendTime", echange.getSendTime().toString());
                Assert anAssert = echange.getDevFK();
                map.put("ast", anAssert);
                map.put("exid", echange.getId());
                break;
            }
        }
        return map;

    }

    @PostMapping("/sys/exchangePermit")
    public Map<String, Object> exP(int exid,String permi) {
        Map<String, Object> map = new HashMap<>();
        EchangeDevs echangeDevs=jpaExchangeDevs.findById(exid).get();
        if(permi.equals("agree")){
            Assert anAssert=echangeDevs.getDevFK();
            Employee employee = echangeDevs.getSenderFK();
            anAssert.setEmployeeByBorrower(employee);
            jpaAssert.save(anAssert);
            echangeDevs.setReceviedTime(new Timestamp(new Date().getTime()));
            echangeDevs.setReceived("agree");
            jpaExchangeDevs.save(echangeDevs);
            Employee me=(Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
            asmRecordService.write(AsmAction.dev_exc, new Timestamp(new java.util.Date().getTime()), me, employee, anAssert, "");
            map.put("ok","1");
            return map;
        }else {
            echangeDevs.setReceived("disagree");
            jpaExchangeDevs.save(echangeDevs);
            map.put("error","1");
            return map;
        }


    }
}

package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaExchangeDevs;
import com.rbac.demo.jpa.JpaMail;
import com.rbac.demo.service.AsmRecordService;
import com.rbac.demo.tool.JSON;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;

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
    @Autowired
    private JpaMail jpaMail;
    @PostMapping("/sys/sys_even")
    public Map<String, Object> sysEvent() {
        Map<String, Object> map = new HashMap<>();
        List<EchangeDevs> list = jpaExchangeDevs.findAll();



        Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(list.size()==0){
            return map;
        }

        List<Employee> senderList=new ArrayList<>();
        List<Assert> asserts=new ArrayList<>();
        List<EchangeDevs> reqList=new ArrayList<>();
        List<String> res=new ArrayList<>();
        for (EchangeDevs echangeDevs : list) {
            Employee resiverFK = echangeDevs.getResiverFK();
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
                    continue;
                }
                senderList.add(echangeDevs.getSenderFK());
                reqList.add(echangeDevs);
                asserts.add(echangeDevs.getDevFK());
            }
        }

        if(senderList.size()!=0){
            map.put("senders",senderList);
            map.put("req",reqList);
            map.put("ast",asserts);
        }





        for (EchangeDevs echangeDevs : list) {
            Employee senderFK = echangeDevs.getSenderFK();
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
                    res.add("流转成功:"+echangeDevs.getResiverFK().getEname() + "同意了你的资产流转!");
                    echangeDevs.setSenderFK(null);
                    echangeDevs.setDevFK(null);
                    echangeDevs.setResiverFK(null);
                    jpaExchangeDevs.delete(echangeDevs);
                }
                /**
                 *
                 * 我发给别人的申请,并且对方拒绝了
                 *
                 * */
                if (echangeDevs.getReceived() != null && echangeDevs.getReceived().equals("disagree")) {
                    res.add("流转失败:"+echangeDevs.getResiverFK().getEname() + "拒绝了你的资产流转!");
                    echangeDevs.setSenderFK(null);
                    echangeDevs.setDevFK(null);
                    echangeDevs.setResiverFK(null);
                    jpaExchangeDevs.delete(echangeDevs);
                }

                /**
                 *
                 * 资产在流转过程中持有者已经发生变化
                 *
                 * */
                if (echangeDevs.getReceived() != null && echangeDevs.getReceived().equals("error")) {
                    res.add( "流转失败:资产已被流转到"+echangeDevs.getDevFK().getEmployeeByBorrower().getEname());
                    echangeDevs.setSenderFK(null);
                    echangeDevs.setDevFK(null);
                    echangeDevs.setResiverFK(null);
                    jpaExchangeDevs.delete(echangeDevs);
                }

            }
        }

        if(res.size()!=0){
            map.put("resp",res);
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
    public Map<String,String> exP(String[] data) {
        Map<String, String> map = new HashMap<>();

        if(data==null){
            return map;
        }
        for (String str:data){
            Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
            String[] re=str.split("-");
            int id=Integer.parseInt(re[0]);
            String td=re[1];
            EchangeDevs echangeDevs=jpaExchangeDevs.findById(id).get();
            Employee sender=echangeDevs.getSenderFK();
            Assert ast=echangeDevs.getDevFK();
            Employee bros=ast.getEmployeeByBorrower();
            if(td.equals("true")){
                if(!bros.getId().equals(employee.getId())){
                    echangeDevs.setReceived("error");
                }else {
                    echangeDevs.setReceived("agree");
                    ast.setEmployeeByBorrower(sender);
                }

            }else if (td.equals("false")){
                echangeDevs.setReceived("disagree");
            }
            jpaExchangeDevs.save(echangeDevs);
            jpaAssert.save(ast);
        }
            return map;
        }


    @PostMapping("/sys/setMail")
//    host:host,sender:sender,accont:accont,pwd:pwd,forwhat:'流转服务',remark:$("#remark").val()
    public Map<String,String> saveMail(Integer id,String host,String sender,String accont,String pwd,String remark) {

        Map<String, String> map = new HashMap<>();
        SysMail sysMail;
        if(id==0){
            sysMail=new SysMail();
        }else {
            sysMail=jpaMail.findById(id).get();
        }
        sysMail.setForwhat("asm");
        sysMail.setHost(host);
        sysMail.setRemark(remark);
        sysMail.setSenderAccont(accont);
        sysMail.setSenderPwd(pwd);
        sysMail.setSenderAddr(sender);
        jpaMail.save(sysMail);
        map.put("success","保存成功");
        return map;
    }

    @PostMapping("/sys/getAsmMail")
//    host:host,sender:sender,accont:accont,pwd:pwd,forwhat:'流转服务',remark:$("#remark").val()
    public Map<String,Object> getAsmMail() {
        Map<String, Object> map = new HashMap<>();
        SysMail sysMail=jpaMail.findSysMailByForwhat("asm");
        if(sysMail==null){
            map.put("info","no");
        }else {
            map.put("mail",sysMail);
        }
        return map;

    }


    }

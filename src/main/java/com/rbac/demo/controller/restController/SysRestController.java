package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.EchangeDevs;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaExchangeDevs;
import com.rbac.demo.tool.JSON;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SysRestController {
    @Autowired
    private JpaExchangeDevs jpaExchangeDevs;
    @PostMapping("/sys/sys_even")
    public Map<String,String> sysEvent(){
        Map<String,String> map=new HashMap<>();
        List<EchangeDevs> list=jpaExchangeDevs.findAll();
        Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        for (EchangeDevs echangeDevs:list){
            Employee resiverFK=echangeDevs.getResiverFK();
            Employee senderFK=echangeDevs.getSenderFK();
            /**
             *
             * 别人发给我的申请
             *
             * */
            if(resiverFK!=null&&resiverFK.getId()==employee.getId()&&echangeDevs.getIsDone().equals("0")){
                map.put("exchange","reseive:/sys/exchange_resp");
                return map;
            }
            /**
             *
             * 我发给别人的申请,并且对方处理了
             *
             * */

            if(senderFK!=null&&senderFK.getId()==employee.getId()&&echangeDevs.getIsDone().equals("0")){
                /**
                 *
                 * 我发给别人的申请,并且对方接受了
                 *
                 * */
                if(echangeDevs.getReceived()!=null&&echangeDevs.getReceived().equals("agree")){
                    map.put("exchange",echangeDevs.getResiverFK().getEname()+"同意了你的资产流转!");
                    return map;
                }
                /**
                 *
                 * 我发给别人的申请,并且对方拒绝了
                 *
                 * */
                if(echangeDevs.getReceived()!=null&&echangeDevs.getReceived().equals("disagree")){
                    map.put("exchange",echangeDevs.getResiverFK().getEname()+"拒绝了你的资产流转!");
                    return map;
                }

                return map;
            }
        }
        map.put("exchange","");
        return map;
    }

    @PostMapping("/sys/getExchangeEven")
    public Map<String,Object> getEven() throws Exception {
        Map<String,Object> map=new HashMap<>();
        List<EchangeDevs> echangeDevs = jpaExchangeDevs.findAll();
        Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        for (EchangeDevs echange:echangeDevs){

            if(echange.getReceived().equals("")&&echange.getIsDone().equals("0")&&echange.getResiverFK().getId()==employee.getId()){
                map.put("sender",echange.getSenderFK().getEname());
                map.put("reason",echange.getReason());
                map.put("sendTime",echange.getSendTime().toString());
                Assert anAssert =echange.getDevFK();
                map.put("ast",anAssert);
                break;
            }
        }
        return map;

    }
}

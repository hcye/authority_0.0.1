package com.rbac.demo.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.rbac.demo.entity.RtcBug;
import com.rbac.demo.jpa.JpaRtcBug;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReadZentaoTableEventListenner extends AnalysisEventListener<ZentaoBugTableModel> {
    private JpaRtcBug jpaRtcBug;
    private List<RtcBug> bugList=new ArrayList<RtcBug>();
    public ReadZentaoTableEventListenner(JpaRtcBug jpaRtcBug){
        this.jpaRtcBug=jpaRtcBug;
    }
/*    //0	Bug编号
1	所属产品
2	分支/平台
//3	所属模块
4	所属项目
5	相关研发需求
6	相关任务
//7	Bug标题
8	关键词
//9	严重程度
//10	优先级
11	Bug类型
12	操作系统
13	浏览器
//14	重现步骤
//15	Bug状态
16	截止日期
17	激活次数
18	是否确认
19	抄送给
//20	由谁创建
21	创建日期
//22	影响版本
//23	指派给
24	指派日期
25	解决者
26	解决方案
27	解决版本
28	解决日期
29	由谁关闭
30	关闭日期
31	重复ID
32	相关Bug
//33	相关用例
34	最后修改者
35	修改日期
36	子状态
37	附件*/

    @Override
    public void invoke(ZentaoBugTableModel zentaoBugTableModel, AnalysisContext analysisContext) {
        String bugFlag=zentaoBugTableModel.getV0();
        String bugModule=zentaoBugTableModel.getV3();
        String bugSeriousness=zentaoBugTableModel.getV9();
        String bugPriority=zentaoBugTableModel.getV10();
        String bugRelative=zentaoBugTableModel.getV33();
        String bugSummary=zentaoBugTableModel.getV7();
        String bugDesc=zentaoBugTableModel.getV14();
        String bugStatus=zentaoBugTableModel.getV15();
        String bugCreater=zentaoBugTableModel.getV20();
        String bugOwner=zentaoBugTableModel.getV23();
        String bugVersion=zentaoBugTableModel.getV22();

        RtcBug rtcBug=new RtcBug();
        rtcBug.setBugFlag(bugFlag);
        rtcBug.setBugModule(bugModule);
        switch (bugSeriousness){
            case "1" :
                rtcBug.setBugSeriousness("A");
                break;
            case "2" :
                rtcBug.setBugSeriousness("B");
                break;
            case "3" :
                rtcBug.setBugSeriousness("C");
                break;
            case "4" :
                rtcBug.setBugSeriousness("D");
                break;
            default:
                rtcBug.setBugPriority("");
        }
        switch (bugPriority){
            case "1" :
                rtcBug.setBugPriority("高");
                break;
            case "2" :
                rtcBug.setBugPriority("中");
                break;
            case "3" :
                rtcBug.setBugPriority("低");
                break;
            default:
                rtcBug.setBugPriority("");
        }
        rtcBug.setBugRelative(bugRelative);
        rtcBug.setBugSummary(bugSummary);
        rtcBug.setBugDesc(bugDesc);
        rtcBug.setBugStatus(bugStatus);
        rtcBug.setBugCreater(bugCreater);
        rtcBug.setBugOwner(bugOwner);
        rtcBug.setBugVersion(bugVersion);
        rtcBug.setBugSrc("航盛");
        rtcBug.setBugClassfy1("软件");
        rtcBug.setBugClassfy2("台上");
        rtcBug.setBugDefectType("设计缺陷");
        rtcBug.setBugDiscoveryPhase("设计与开发阶段");
        rtcBug.setBugCate("缺陷");
        rtcBug.setBugType("问题和缺陷");
        rtcBug.setBugFrequence("always");
        bugList.add(rtcBug);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        jpaRtcBug.saveAll(bugList);
    }
}

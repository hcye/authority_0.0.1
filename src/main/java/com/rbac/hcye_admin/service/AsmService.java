package com.rbac.hcye_admin.service;

import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.JpaAssert;
import com.rbac.hcye_admin.jpa.JpaAssetType;
import com.rbac.hcye_admin.jpa.JpaDevType;
import com.rbac.hcye_admin.jpa.JpaResources;
import com.rbac.hcye_admin.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AsmService {

    @Autowired
    private JpaAssert jpaAssert;
    @Autowired
    private JpaAssetType jpaAssetType;
    @Autowired
    private JpaDevType jpaDevType;
    @Autowired
    private PermissionService permissionService;
    public boolean valid(String inputCode, String tep){
        if(inputCode.equals("")&&tep.equals("")){
            return true;
        }
        String[] input=inputCode.split("-");
        String[] tp=tep.split("-");
        String engRegex="[A-Za-z]+";
        String numRegex="[0-9]+";
        String allZero="[0]+";
        String allNine="[9]+";
        if(input.length!=tp.length){
            return false;
        }else {
            for (int i=0;i<tp.length;i++){
                boolean isMatch=true;

                if(tp[i].length()!=input[i].length()){
                    return false;
                }
                isMatch = Pattern.matches(allNine, input[input.length-1]);
                if(!isMatch){
                    return false;
                }
                if(tp[i].contains("x")){
                    isMatch = Pattern.matches(engRegex, input[i]);
                }else if(tp[i].contains("0")){
                    isMatch = Pattern.matches(numRegex, input[i]);
                }
                if(Pattern.matches(allZero, input[input.length-1])){
                    return false;
                }

                if(!isMatch){
                    return false;
                }
            }
        }
        return true;
    }

    public Page<Assert> queryPage(String type, String isDam, String search, Pageable pageable){
        Page<Assert> page;
        String damFlag="0";
        if(!isDam.equals("完好")){
            damFlag="1";
        }
        if(search.equals("")){
            page=jpaAssert.findAssertsBytype(type,damFlag,pageable);
        }else {
            search= ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAnameLikeAndDamFlagAndType(type,search,damFlag,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnumLikeAndDamFlagAndType(type,search,damFlag,pageable);
                if(page.isEmpty()){
                    page=jpaAssert.findAssertBySnnumLikeAndDamFlag(search,damFlag,pageable);
                    if(page.isEmpty()){
                        page=jpaAssert.findAssertsByBorroworPingyinLikeAndDamFlag(search,damFlag,pageable);
                        if(page.isEmpty()){
                            page=jpaAssert.findAssertsByBorroworNameLikeAndDamFlag(search,damFlag,pageable);
                        }
                    }
                }
            }
        }
        return page;
    }

    public Page<Assert> queryStockPage(String type, String dtype, Pageable pageable){
        Page<Assert> page;
        DevType devType=null;
        if(dtype!=null && !dtype.equals("")){
            devType=jpaDevType.findDevTypeByDevNameAndAssetTypeName(dtype,type);
        }
        if(devType==null){
            page=jpaAssert.findAssertByAssetTypeWithoutDamflagWithoutBroByPage(type,pageable);
        }else {
            page=jpaAssert.findAssertsByAnameAndAssetTypeNameWithoutBro(type,dtype,pageable);
        }
        return page;
    }

    public List<Assert> queryList(String type, String isDam, String search){
        List<Assert> list;
        if(isDam.equals("")&&search.equals("")){
            return jpaAssert.findAssertByAssetType_without_damflag(type);
        }
        String damFlag="0";
        if(!isDam.equals("完好")){
            damFlag="1";
        }
        if(search.equals("")){
            list=jpaAssert.findAssertsBytype(type,damFlag);
        }else {
            search= ConvertStrForSearch.getFormatedString(search);
            list=jpaAssert.findAssertsByAnameLikeAndDamFlagAndType(type,search,damFlag);
            if(list.isEmpty()){
                list=jpaAssert.findAssertsByAssestnumLikeAndDamFlagAndType(type,search,damFlag);
                if(list.isEmpty()){
                    list=jpaAssert.findAssertBySnnumLikeAndDamFlag(search,damFlag);
                    if(list.isEmpty()) {
                        list = jpaAssert.findAssertsByBorroworPingyinLikeAndDamFlag(search, damFlag);
                        if (list.isEmpty()) {
                            list = jpaAssert.findAssertsByBorroworNameLikeAndDamFlag(search, damFlag);
                        }
                    }
                }
            }
        }
        return list;
    }

    public String getMaxAssetNum(DevType devType){
        if(devType==null){
            return "";
        }
//        String dev_name=devType.getDevName();
        AssetType assetType=devType.getAssetTypeByAssertTypeId();

        String template=devType.getAssetNumTemplate();
        if(template==null||template.equals("")){
            return "";
        }
        List<DevType> devTypes=jpaDevType.findDevTypesByAssetNumTemplate(template);
        List<Assert> list=new ArrayList<>();
        for(DevType dtp:devTypes){
            String code= dtp.getAssetNumTemplate();
            if(code==null || code.equals("")){
                continue;
            }
            list.addAll(jpaAssert.findAssertsByAnameAndAssetType(dtp.getDevName(),assetType));
        }

//        List<Assert> list=jpaAssert.findAssertsByAnameAndAssetType(dev_name,devType.getAssetTypeByAssertTypeId());

        String maxNum="";
        int max=0;
        for (Assert asset:list){
            String assestnum=asset.getAssestnum();
            String a=assestnum.split("-")[assestnum.split("-").length-1];
            int xuhao=Integer.parseInt(a);
            if(xuhao>max){
                max=xuhao;
                maxNum=assestnum;
            }

        }
        if(!maxNum.equals("")){

            int length=maxNum.split("-").length;
            return Integer.parseInt(maxNum.split("-")[length-1])+1+"";
        }else {
            return "1";
        }
    }

    public List<AssetType> getPermitAsmAssetTypes(){
        List<AssetType> assetTypes=new ArrayList<>();
        List<AssetType> types=jpaAssetType.findAll();
        for (AssetType assetType:types){
            if(permissionService.isPermit(assetType.getPermiCode())){
                assetTypes.add(assetType);
            }
        }
        return assetTypes;


    }


    public boolean validDevTypeNum(String inputCode, String tep) {
        if(tep.equals("")){
            return true;
        }else if(inputCode.equals("")&&!tep.equals("")){
            return false;
        }
        String numRegex="[0-9]+";
        if(!Pattern.matches(numRegex,inputCode)){
            return false;
        }
//
//        int maxNum= Integer.parseInt(getMaxAssetNum(jpaDevType.findDevTypeByAssetNumTemplate(tep)));
////        String numRegex="[0-9]+";
////
//        int input=Integer.parseInt(inputCode);
//        if(input<maxNum){
//            return false;
//        }
//        /**
//         *
//         * 尾数含有非数字的情况
//         * */
        return true;
    }
    public boolean validRepeat(String tep) {
        if(tep.equals("")){
            return true;
        }
        Assert anAssert=jpaAssert.findAssertByAssestnum(tep);
        if(anAssert!=null){
            return false;
        }else {
            return true;
        }
    }
}

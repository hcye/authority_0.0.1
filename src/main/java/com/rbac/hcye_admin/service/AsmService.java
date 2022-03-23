package com.rbac.hcye_admin.service;

import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.JpaAssert;
import com.rbac.hcye_admin.jpa.JpaAssetType;
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
    private JpaResources jpaResources;
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
                    if(Pattern.matches(allZero, input[i])){
                        return false;
                    }
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
/*
    public List<Assert> queryList(String type, String isDam, String search){
        List<Assert> page;
        String damFlag="0";
        if(!isDam.equals("完好")){
            damFlag="1";
        }
        if(search.equals("")){
            page=jpaAssert.findAssertsBytype_to_list(type,damFlag);
        }else {
            search= ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAnameLikeAndDamFlagAndType_to_list(type,search,damFlag);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnumLikeAndDamFlagAndType_to_list(type,search,damFlag);
                if(page.isEmpty()){
                    page=jpaAssert.findAssertsByBorroworPingyinLikeAndDamFlag_to_list(search,damFlag);
                    if(page.isEmpty()){
                        page=jpaAssert.findAssertsByBorroworNameLikeAndDamFlag_to_list(search,damFlag);
                    }
                }
            }
        }
        return page;
    }*/

    public List<Assert> queryList(String type, String isDam, String search){
        List<Assert> list;
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
        String dev_name=devType.getDevName();

        List<Assert> list=jpaAssert.findAssertsByAnameAndAssetType(dev_name,devType.getAssetTypeByAssertTypeId());
        String template=devType.getAssetNumTemplate();
        if(template==null||template.equals("")){
            return "";
        }
        String maxNum="";
        int max=0;
        for (Assert asset:list){
            String assestnum=asset.getAssestnum();
            if(validDevTypeNum(asset.getAssestnum(),template)){
                String a=assestnum.split("-")[assestnum.split("-").length-1];
                int xuhao=Integer.parseInt(a);
                if(xuhao>max){
                    max=xuhao;
                    maxNum=assestnum;
                }
            }
        }
        if(!maxNum.equals("")){
            return "编码最大值是:"+maxNum;
        }else {
            return maxNum;
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
        if(inputCode.equals("")&&tep.equals("")){
            return true;
        }else if(inputCode.equals("")&&!tep.equals("")){
            return false;
        }else if(!inputCode.contains("-")){
            return false;
        }

        int index = inputCode.lastIndexOf("-");

        String prefixIn=inputCode.substring(0,index);
        String prefixTemp=tep.substring(0,index);
        String suffixIn=inputCode.substring(index+1);
        String suffixTemp=tep.substring(index+1);
        String numRegex="[0-9]+";
        String allZero="[0]+";
//        Pattern.matches(numRegex, input[i]);
        /**
         *
         * 前导不相同的情况
         * */
        if(!prefixIn.equals(prefixTemp)||suffixIn.length()!=suffixTemp.length()){
            return false;
        }
        /**
         *
         * 尾数含有非数字的情况
         * */
        if(!Pattern.matches(numRegex,suffixIn)){
            return false;
            /**
             *
             * 尾数全部是0的情况
             * */
        }else if(Pattern.matches(allZero,suffixIn)){
            return false;
        }
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

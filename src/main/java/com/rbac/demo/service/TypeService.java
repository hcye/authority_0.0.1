package com.rbac.demo.service;

import com.rbac.demo.entity.AssetType;
import com.rbac.demo.entity.DevType;
import com.rbac.demo.jpa.JpaAssetType;
import com.rbac.demo.jpa.JpaDevType;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;




@Repository
public class TypeService {
    @Autowired
    private JpaAssetType jpaAssetType;
    @Autowired
    private JpaDevType jpaDevType;
    private static final int pageSize=8;
    public Page<AssetType> getTypePage(String name,String pre,String next,int pageNow){
        Pageable pageable;
        if (pageNow != 0) {
            pageNow = pageNow - 1;  //Page 对象下标从0开始,所以从前端返回数据要减一
            if (!pre.equals("")&&pageNow>0) {
                pageNow = pageNow - 1;
            } else if (!next.equals("")) {
                pageNow = pageNow + 1;
            }
        }
        pageable = PageRequest.of(pageNow, pageSize);
        Page<AssetType> types;

        if (name.equals("")) {
            types =  jpaAssetType.findAll(pageable);
        } else {
            name= ConvertStrForSearch.getFormatedString(name);
            types = jpaAssetType.findAssertTypesByTypeNameLike(name, pageable);
        }


        return types;
    }

    public Page<DevType> getDevTypePage(String name, String pre, String next, int pageNow,String type){
        Pageable pageable;
        if (pageNow != 0) {
            pageNow = pageNow - 1;  //Page 对象下标从0开始,所以从前端返回数据要减一
            if (!pre.equals("")&&pageNow>0) {
                pageNow = pageNow - 1;
            } else if (!next.equals("")) {
                pageNow = pageNow + 1;
            }
        }
        pageable = PageRequest.of(pageNow, pageSize);
        Page<DevType> types;

        if (name.equals("")) {
            types =jpaDevType.findDevTypesByAssertType(type,pageable);
        } else {
            name= ConvertStrForSearch.getFormatedString(name);
            types = jpaDevType.findDevTypesByDevNameLike(name, pageable);
        }


        return types;
    }

    public String getTypeCode(String name){
       return jpaAssetType.findAssetTypeByName(name).getAssetCode();
    }

}

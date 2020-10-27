package com.rbac.demo.controller.restController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.rbac.demo.easyExcel.AssetDownloadModel;
import com.rbac.demo.easyExcel.AssetTypeDownloadModel;
import com.rbac.demo.easyExcel.DevTypeDownloadModel;
import com.rbac.demo.easyExcel.ReadAssetEventListener;
import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.AsmRecordService;
import com.rbac.demo.service.AsmService;
import com.rbac.demo.service.TypeService;
import com.rbac.demo.service.WriteLog;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class AsmRestController {
    private static final int pageSize=10;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaAssert jpaAssert;
    @Autowired
    private JpaAssetType jpaAssetType;
    @Autowired
    private WriteLog writeLog;
    @Autowired
    private JpaDevType jpaDevType;

    @Autowired
    private JpaOperatRecord jpaOperatRecord;
    @Autowired
    private AsmService asmService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private AsmRecordService asmRecordService;
    @PostMapping("/asm/queryPage")
    public Page<Assert> queryPage(String name,String search,String pre,String next,int pageIndex,String jumpFlag){
        /**
         *
         * 确定是否是翻页
         *
         * */
        Page<Assert> page;
        Pageable pageable;
        if(!pre.equals("")||!next.equals("")||!jumpFlag.equals("")){
            pageIndex=pageIndex-1;
            pageable=PageRequest.of(pageIndex,pageSize);
            if(!pre.equals("")){
                pageable=pageable.previousOrFirst();
            }else if(!next.equals("")){
                pageable=pageable.next();
            }
        }else {
            pageable=PageRequest.of(0,pageSize);
        }
        if(search.equals("")){
            page=jpaAssert.findAssertsByAname(name,pageable);
        }else {
            search=ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAname(search,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnum(search,pageable);
            }
        }
        return page;
    }
    @PostMapping("/asm/operat")
    public Map<String, List<Assert>> borrow(String selectDevIds,String name,String actionFlag){
        Map<String,List<Assert>> map=new HashMap<>();
        String[] ids=selectDevIds.split(",");

        Employee borrower=jpaEmployee.findEmployeeByLoginName(name.split("-")[1]);
        if(actionFlag.equals("bo")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(borrower);
                    asmRecordService.write(AsmAction.dev_borrow,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),borrower,ast,"");
                    jpaAssert.saveAndFlush(ast);
                }
            }
        }else if(actionFlag.equals("ret")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(null);
                    asmRecordService.write(AsmAction.dev_return,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),borrower,ast,"");
                    jpaAssert.saveAndFlush(ast);
                }
            }
        }
        List<Assert> asserts= (List<Assert>) borrower.getAssertsById();
        map.put("allDev",asserts);
        return map;
    }

    @PostMapping("/asm/devs")
    public Map<String, List<Assert>> getDevs(String name){
        List<AssetType> assetTypes=asmService.getPermitAsmAssetTypes();
        Map<String,List<Assert>> map=new HashMap<>();
        Employee returner=jpaEmployee.findEmployeeByLoginName(name.split("-")[1]);
        List<Assert> asserts= (List<Assert>) returner.getAssertsById();
        List<Assert> assertsHasPermit=new ArrayList<>();
        for(int i=0;i<asserts.size();i++){
            for (int j=0;j<assetTypes.size();j++){
                if(asserts.get(i).getAssetTypeByAssertType().equals(assetTypes.get(j))){
                    assertsHasPermit.add(asserts.get(i));
                }
            }
        }

        map.put("devs",assertsHasPermit);
        return map;
    }

    @PostMapping("/asm/addAssetType")
    public Map<String, String> getDevsNames(String devType,String template,String desc,String authority){
        Map<String ,String> map=new HashMap<>();
        devType=devType.trim();
        AssetType type = jpaAssetType.findAssetTypeByName(devType);
        if(type!=null){
            map.put("error","类型名称重复!");
            return map;
        }
        Timestamp ts=new Timestamp(new Date().getTime());
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        AssetType assertType=new AssetType();
        assertType.setAssetCode(template);
        assertType.setCreateTime(ts);
        assertType.setCreator(usname);
        assertType.setRemarks(desc);
        assertType.setTypeName(devType);
        assertType.setPermiCode(authority);
        jpaAssetType.save(assertType);
        asmRecordService.write(AsmAction.dev_add_type,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,null,devType);
        map.put("ok","新增成功!");
        return map;
    }


    @RequiresPermissions("asm:devType:edit")
    @PostMapping("/asm/editDevType")
    public Map<String, String> valid(int id,String input){
        Map<String ,String> map=new HashMap<>();
        DevType devType=jpaDevType.findById(id).get();
        String name=devType.getDevName();
        List<Assert> list = jpaAssert.findAssertsByAname(name);
        if(!list.isEmpty()){
            map.put("error","设备类型关联有设备不能修改！");
            return map;
        }else {
            String tem=devType.getAssetTypeByAssertTypeId().getAssetCode();
            if(asmService.valid(input,tem)){
                devType.setAssetNumTemplate(input);
                jpaDevType.save(devType);
                map.put("ok","修改成功！");

            }else {
                map.put("error","输入不匹配资产类型约束！");
            }
            return map;
        }

    }
    @RequiresPermissions("asm:type:edit")
    @PostMapping("/asm/saveAssetType")
    public Map<String, String> saveType(int id,String devType,String template,String desc,String authority){
        Map<String ,String> map=new HashMap<>();
        devType=devType.trim();
        if(authority.equals("")){
            map.put("error","权限字符不能为空!");
            return map;
        }
        AssetType type = jpaAssetType.findAssetTypeByName(devType);
        AssetType assertTypeOld= jpaAssetType.findById(id).get();
        if(type!=null&&type.getId()!=id){
            map.put("error","类型名称重复,修改失败!");
            return map;
        }
        assertTypeOld.setAssetCode(template);
        assertTypeOld.setRemarks(desc);
        assertTypeOld.setTypeName(devType);
        assertTypeOld.setPermiCode(authority);
        jpaAssetType.save(assertTypeOld);
        map.put("ok","修改成功!");
        return map;
    }

    @PostMapping("/asm/getDevNames")
    public Map<String, List<String>> get(String TpName,String getTypesOnly){
        Map<String,List<String>> map=new HashMap<>();
        AssetType assetType=jpaAssetType.findAssetTypeByName(TpName);
        if(getTypesOnly!=null){
            List<String> names=jpaDevType.findDevTypesNameByAssertType(TpName);
            map.put("name",names);
            return map;
        }
        String code=assetType.getAssetCode();
        List<String> codes=new ArrayList<>();
        List<String> devCodes=new ArrayList<>();
        List<String> names=jpaDevType.findDevTypesNameByAssertType(TpName);
        if(names.size()!=0){
            DevType devType1=jpaDevType.findDevTypeByDevName(names.get(0));
            devCodes.add(devType1.getAssetNumTemplate());
        }

        codes.add(code);
        map.put("name",names);
        map.put("code",codes);
        map.put("devCode",devCodes);
        return map;
    }
    @RequiresPermissions("asm:devType:add")
    @PostMapping("/asm/addDevType")
    public Map<String, String> addDevType(String devType,String dev_name,String desc,String temp){
        Map<String,String> map=new HashMap<>();
        AssetType assertType= jpaAssetType.findAssetTypeByName(devType);
        String assetCode=assertType.getAssetCode();
        boolean b=asmService.valid(temp,assetCode);
        if(!b){
            map.put("error","设备类型编码不匹配资产类型编码");
            return map;
        }
        DevType dtp=jpaDevType.findDevTypeByDevNameAndAssertType(dev_name,assertType);
        if(dtp!=null){

            map.put("error","资产名称重复！请重新填写");
            return map;
        }
        DevType devType1=new DevType();

        Timestamp ts=new Timestamp(new Date().getTime());
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        devType1.setCreateTime(ts);
        devType1.setCreator(usname);
        devType1.setRemarks(desc);
        devType1.setDevName(dev_name);
        devType1.setAssetNumTemplate(temp);
        devType1.setAssetTypeByAssertTypeId(assertType);
        jpaDevType.save(devType1);
        map.put("ok","增加完成");
        return map;
    }
    @PostMapping("/asm/getTypes")
    public Page<AssetType> getTypes(String name,String pre,String next,int pageNow){
        return  typeService.getTypePage( name, pre, next, pageNow);
    }



    @PostMapping("/asm/getTypeNames")
    public List<AssetType> getTypes(){

        return  jpaAssetType.findAll();
    }

    @PostMapping("/asm/getTypeName")
    public Map<String,String> getName(String name){
        Map<String,String> map=new HashMap<>();
        map.put("code",jpaAssetType.findAssetTypeByName(name).getAssetCode());
        return  map;
    }

    @PostMapping("/asm/validAssetNum")
    public Map<String,String> valid(String num,int id){
        Map<String,String> map=new HashMap<>();
        Assert anAssert=jpaAssert.findById(id).get();
        String name=anAssert.getAname();
        DevType devType=jpaDevType.findDevTypeByDevName(name);
        String template=devType.getAssetNumTemplate();
        boolean flag=asmService.validDevTypeNum(num,template);
        boolean repeatFlag=asmService.validRepeat(num);
        if(flag){
            map.put("ok","校验正确");
        }else {
            map.put("error","输入的资产编码不匹配设备类型约束！");
        }
        if(repeatFlag){
            map.put("ok","不含重复");
        }else {
            map.put("error","输入的资产编号重复！");
        }
        return  map;
    }


    @PostMapping("/asm/getDevTypes")
    public Page<DevType> getDevTypes(String name,String pre,String next,int pageNow,String type){
        return  typeService.getDevTypePage( name, pre, next, pageNow,type);
    }

    @RequiresPermissions("asm:type:delete")
    @PostMapping("/asm/deleteType")
    public Map<String,String> deleteType(int id){
        Map<String,String> map=new HashMap<>();
        AssetType type= jpaAssetType.findById(id).get();
        Collection<Assert> asserts=type.getAssertsById();
        if(asserts.isEmpty()){
            type.setDevTypesById(null);
            type.setAssertsById(null);
            jpaAssetType.delete(type);
            map.put("ok","删除成功");
        }else {
            map.put("error","删除失败");
        }
        return map;
    }

    @RequiresPermissions("asm:devType:delete")
    @PostMapping("/asm/deleteDevType")
    public Map<String,String> deleteDevType(int id){
        Map<String,String> map=new HashMap<>();
        DevType type= jpaDevType.findById(id).get();
        List<Assert> list=jpaAssert.findAssertsByAname(type.getDevName());
        if(list.isEmpty()){
            type.setAssetTypeByAssertTypeId(null);
            jpaDevType.delete(type);
            map.put("ok","删除成功");
        }else {
            map.put("error","删除失败");
        }
        return map;
    }



    @PostMapping("/asm/getDevNumTemplate")
    public Map<String, String> getNum(String devName){
        Map<String,String> map=new HashMap<>();
        DevType devType=jpaDevType.findDevTypeByDevName(devName);
        String max=asmService.getMaxAssetNum(devType);
        String devTypeAssetNumTemplate=devType.getAssetNumTemplate();
        map.put("code",devTypeAssetNumTemplate);
        map.put("max",max);
        return map;
    }

    @PostMapping("/asm/validInputAssetNum")
    public Map<String,String> valid(String inputCode,String tep,String num,String model,String price){
        Map<String,String> map=new HashMap<>();
        String numRegex="[0-9]+";
        String priceRegex="[0-9]+[\\.]?[0-9]*";
        if(model.equals("")){
            map.put("error","型号不能为空！");
            return map;
        }
        if(!Pattern.matches(numRegex, num)){
            map.put("error","数量必填且只接受数字！");
            return map;
        }
        if(!price.equals("")){
            if(!Pattern.matches(priceRegex, price)){
                map.put("error","价格只接受数字！");
                return map;
            }
        }
        boolean validRes=asmService.validDevTypeNum(inputCode, tep);
        if(validRes){
            map.put("ok","编号校验成功");
        }else {
            map.put("error","编号校验失败");
        }
        return map;
    }

    @PostMapping("/asm/queryListPage")
    public Page<Assert> queryListPage(String type,String isDam,String search,String pre,String next,int pageIndex,String jumpFlag){
        /**
         *
         * 确定是否是翻页
         *
         * */



        Page<Assert> page;
        Pageable pageable;
        if(!pre.equals("")||!next.equals("")||!jumpFlag.equals("")){
            pageIndex=pageIndex-1;
            pageable=PageRequest.of(pageIndex,pageSize);
            if(!pre.equals("")){
                pageable=pageable.previousOrFirst();
            }else if(!next.equals("")){
                pageable=pageable.next();
            }
        }else {
            pageable=PageRequest.of(0,pageSize);
        }
        page = asmService.queryPage(type,isDam,search,pageable);
        return page;
    }

    /**
     *
     * 报损校验
     *
     * */
    @RequiresPermissions("asm:dem:btn")
    @PostMapping("/asm/validForBad")
    public Map<String,String> validForBad(int id){
        Map<String,String> map=new HashMap<>();
        Assert anAssert=jpaAssert.findById(id).get();
        Employee borrower=anAssert.getEmployeeByBorrower();
        if(borrower!=null){
            map.put("error","设备被借出！归还后才能报损");
        }else {
            anAssert.setWorkless("1");
            anAssert.setDamagetime(new java.sql.Date(new java.util.Date().getTime()));
            jpaAssert.save(anAssert);
            asmRecordService.write(AsmAction.dev_dam,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,anAssert,"");
            map.put("ok","校验成功");
        }
        return map;
    }
    @RequiresPermissions("asm:delete:btn")
    @PostMapping("/asm/validForDel")
    public Map<String,String> del(int id){
        Map<String,String> map=new HashMap<>();
        Assert anAssert=jpaAssert.findById(id).get();
        Employee borrower=anAssert.getEmployeeByBorrower();
/*        List<OperatRecord> list=jpaOperatRecord.findOperatRecordsByAssertByAssertAsset(anAssert);*/
        if(borrower!=null){
            map.put("error","设备被借出！归还后才能删除");
        }else {
            jpaAssert.delete(anAssert);
            asmRecordService.write(AsmAction.dev_del,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,null,"");
            map.put("ok","删除成功");
        }
        return map;
    }



    /**
     *
     * 批量导入
     *
     * */
    @Transactional
    @RequiresPermissions("asm:inp:view")
    @PostMapping("/asm/putin")
    public Map<String,String> putin(String type, String model, String price, String name, String encode, String num){
        int nu=Integer.parseInt(num);
        Map<String,String> map=new HashMap<>();
        List<Assert> list=new ArrayList<>();
        AssetType assetType=jpaAssetType.findAssetTypeByName(type);
        String temp=assetType.getAssetCode();
        if(temp.equals("")&&encode.equals("")){
            for (int i=0;i<nu;i++){
                Assert ast=new Assert();
                ast.setAname(name);
                ast.setPrice(price);
                ast.setModel(model);
                ast.setAssetTypeByAssertType(assetType);
                list.add(ast);
                asmRecordService.write(AsmAction.dev_in,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,null,"");
            }
            jpaAssert.saveAll(list);
            map.put("ok","入库成功！");
            return map;
        }
        String[] encodes=encode.split("-");
        String zeroInhead="";
        if(encodes[encodes.length-1].charAt(0)=='0'){
            char[] chars=encodes[encodes.length-1].toCharArray();
            for (char c:chars){
                if(c=='0'){
                    zeroInhead=zeroInhead+"0";
                }else {
                    break;
                }
            }
        }
        if(zeroInhead.equals(encodes[encodes.length-1])){
            map.put("error","编号末尾不能全部是0");
            return map;
        }


        String head="";
        int lastIndexof=encode.lastIndexOf("-")+1;
        head=encode.substring(0,lastIndexof);
        int tail=Integer.parseInt(encodes[encodes.length-1]);
        int tailLen=encodes[encodes.length-1].length();

        for (int i=0;i<nu;i++){
            String tailStr=tail+"";
            Assert ast=new Assert();
            ast.setAname(name);
            ast.setPrice(price);
            ast.setModel(model);
            ast.setAssetTypeByAssertType(assetType);

            if((tailStr).length()>tailLen){
                map.put("error","编号超出限定范围，入库失败！");
                return map;
            }else {
                int zeroNum = tailLen - tailStr.length();
                for (int j = 0; j < zeroNum; j++) {
                    tailStr = "0" + tailStr;
                }
            }
            String code=head+tailStr;
            if(jpaAssert.findAssertByAssestnum(code)!=null){
                map.put("error","编号重复，入库失败！");
                return map;
            }
            ast.setAssestnum(code);
            tail++;

            list.add(ast);
    //        asmRecordService.write(AsmAction.dev_in,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,null);
        }
        jpaAssert.saveAll(list);
        map.put("ok","入库成功！");
        return map;
    }

    /**
     *
     * 上传资产excel
     *
     * */

    @PostMapping("asm/input")
    public Map<String,String> upload(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> json = new HashMap<>();
        request.setCharacterEncoding("UTF-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        Set set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            multipartFile = (MultipartFile) map.get(iterator.next());
        }
        String filename = multipartFile.getOriginalFilename();
        if (!filename.contains("xlsx")) {
            json.put("error", "文件类型错误");
            return json;
        }
        InputStream inputStream;
        ExcelReader reader = null;
        try {

            inputStream = multipartFile.getInputStream();
            /**
             *
             * 使用easyExcel读上传的表格
             *
             * */


            try {
                reader = EasyExcel.read(inputStream, AssetDownloadModel.class, new ReadAssetEventListener(jpaAssert, jpaEmployee, jpaAssetType, asmService, jpaOperatRecord,jpaDevType)).excelType(ExcelTypeEnum.XLSX).build();
                ReadSheet readSheet = EasyExcel.readSheet(0).build();
                reader.read(readSheet);
            } catch (Exception e) {
                json.put("error", e.toString());
                return json;
            }

            /**
             *
             * */

            inputStream.close();
        } catch (Exception e) {
            json.put("error", e.toString());
            System.out.println(e.toString());
            return json;
        } finally {
            if (reader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                reader.finish();
            }

        }
        json.put("ok", "上传成功");
        return json;
    }
    /**
     *
     * 下载模板
     *
     * */
    @GetMapping("asm/outputTemplate")
    public void exportModel(HttpServletResponse response) throws FileNotFoundException {
        OutputStream stream = null;
        try {
            stream =new BufferedOutputStream(response.getOutputStream()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
        String path= ClassUtils.getDefaultClassLoader().getResource("static/excel").getPath();   //上传资源到项目路径的路径获得

        File file=new File(path+"/"+"moban1.xlsx");
        InputStream inputStream =new FileInputStream(file);
        response.setHeader("Content-disposition", "attachment; filename=" + "template.xlsx");
        response.setContentType("application/msexcel;charset=UTF-8");//设置类型
        response.setHeader("Pragma", "No-cache");//设置头
        response.setHeader("Cache-Control", "no-cache");//设置头
        response.setDateHeader("Expires", 0);//设置日期头
        byte[] bytes=new byte[1024];
        try {
            //除放在static下和templates下的资源
            //idea必须使用项目路径，即以src开头的路径

            //jspringboot文件下载，的输入流智能按以上方式获得
            int i=0;
            while(((i=inputStream.read(bytes))!=-1)){
                stream.write(bytes,0,i);
            }
            inputStream.close();
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
//    type="+$("#dev_type").val()+"&isDam="+$("#is_damage").val()+"&search="+$("#keywords").val();

    @GetMapping("asm/out")
    public void exportExcel(String type,String isDam,String search,HttpServletResponse response) throws IOException {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("export", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AssetDownloadModel.class).sheet("01").doWrite(data(type,isDam,search));




    }

    @GetMapping("asm/out_types")
    public void exportExcel(HttpServletResponse response) throws IOException {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("type_export", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AssetTypeDownloadModel.class).sheet("01").doWrite(data());


    }

    @GetMapping("asm/out_DevTypes")
    public void exportDevExcel(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("dev_type_export", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DevTypeDownloadModel.class).sheet("01").doWrite(dev_data());


    }
    /**
     *
     * 把实体类数组转换成excel导出模板对象数组
     *
     * */

    private List<AssetTypeDownloadModel> data(){
        List<AssetType> list = jpaAssetType.findAll();
        List<AssetTypeDownloadModel> models=new ArrayList<>();
        for (AssetType tp:list){
            AssetTypeDownloadModel model=new AssetTypeDownloadModel();
            model.setTname(tp.getTypeName());
            model.setTemplate(tp.getAssetCode());
            model.setCreateTime(tp.getCreateTime().toString());
            if(tp.getRemarks()!=null){
                model.setRemark(tp.getRemarks());
            }
            model.setSign(tp.getPermiCode());
            models.add(model);
        }
        return models;
    }

    private List<DevTypeDownloadModel> dev_data(){
        List<DevType> list = jpaDevType.findAll();
        List<DevTypeDownloadModel> models=new ArrayList<>();
        for (DevType tp:list){
            DevTypeDownloadModel model=new DevTypeDownloadModel();
            model.setDevName(tp.getDevName());
            model.setAssetNumTemplate(tp.getAssetNumTemplate());
            if(tp.getCreateTime()!=null){
                model.setCreateTime(tp.getCreateTime().toString());
            }
            if(tp.getRemarks()!=null){
                model.setRemarks(tp.getRemarks());
            }
            if(tp.getCreator()!=null){
                model.setCreator(tp.getCreator());
            }
            models.add(model);
        }
        return models;
    }


    private List<AssetDownloadModel> data(String type, String isDam, String search){
        List<Assert> list = asmService.queryList(type,isDam,search);
        List<AssetDownloadModel> models=new ArrayList<>();
        for (Assert anAssert:list){
            AssetDownloadModel model=new AssetDownloadModel();

            if(anAssert.getAssestnum()!=null){
                model.setAssetNum(anAssert.getAssestnum());
            }
            if(anAssert.getAssetTypeByAssertType().getTypeName()!=null){
                model.setAssetType(anAssert.getAssetTypeByAssertType().getTypeName());
            }
            if(anAssert.getEmployeeByBorrower()!=null){
                model.setBorrower(anAssert.getEmployeeByBorrower().getEname());
            }
            model.setDevName(anAssert.getAname());
            if(anAssert.getBrotime()!=null){
                model.setBorTime(anAssert.getBrotime().toString());
            }
            if(anAssert.getModel()!=null){
                model.setModel(anAssert.getModel());
            }

            String damFlag=anAssert.getWorkless();
            if(damFlag!=null){
                String dam;
                if(damFlag.equals("0")){
                    dam="完好";
                }else {
                    dam="报废";
                }
                model.setWorkless(dam);
            }

            if(anAssert.getPutintime()!=null){
                model.setPutinTime(anAssert.getPutintime().toString());
            }
            if(anAssert.getPrice()!=null){
                model.setPrice(anAssert.getPrice());
            }
            if(anAssert.getSnnum()!=null){
                model.setSnNum(anAssert.getSnnum());
            }
            if(anAssert.getRemarks()!=null){
                model.setRemarks(anAssert.getRemarks());
            }
            models.add(model);
        }
        return models;
    }



}

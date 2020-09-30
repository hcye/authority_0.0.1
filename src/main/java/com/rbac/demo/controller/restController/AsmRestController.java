package com.rbac.demo.controller.restController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.rbac.demo.easyExcel.EasyExcelMapedModel;
import com.rbac.demo.easyExcel.ReadAssetEventListener;
import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.AssetType;
import com.rbac.demo.entity.DevType;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.AsmService;
import com.rbac.demo.service.TypeService;
import com.rbac.demo.service.WriteLog;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
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
    private ReadAssetEventListener readAssetEventListener;
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
        List<Employee> employees=jpaEmployee.findEmployeesByEname(name);
        Employee borrower=employees.get(0);
        if(actionFlag.equals("bo")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(borrower);
                    jpaAssert.saveAndFlush(ast);
                }
            }
        }else if(actionFlag.equals("ret")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(null);
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
        Map<String,List<Assert>> map=new HashMap<>();
        List<Employee> employees=jpaEmployee.findEmployeesByEname(name);
        Employee returner=employees.get(0);
        List<Assert> asserts= (List<Assert>) returner.getAssertsById();
        map.put("devs",asserts);
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
        map.put("ok","新增成功!");
        return map;
    }


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
    public Map<String, List<String>> addAssertType(String devType, Model model){
        Map<String,List<String>> map=new HashMap<>();
        AssetType assetType=jpaAssetType.findAssetTypeByName(devType);
        String code=assetType.getAssetCode();
        List<String> codes=new ArrayList<>();
        codes.add(code);
        List<String> names=jpaDevType.findDevTypesNameByAssertType(devType);
        map.put("name",names);
        map.put("code",codes);
        return map;
    }

    @PostMapping("/asm/addDevType")
    public Map<String, String> addDevType(String devType,String dev_name,String desc){
        Map<String,String> map=new HashMap<>();
        AssetType assertType= jpaAssetType.findAssetTypeByName(devType);
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
        devType1.setAssetTypeByAssertTypeId(assertType);
        jpaDevType.save(devType1);
        map.put("ok","增加完成");
        return map;
    }
    @PostMapping("/asm/getTypes")
    public Page<AssetType> getTypes(String name,String pre,String next,int pageNow){
        return  typeService.getPage( name, pre, next, pageNow);
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
    @PostMapping("/asm/validInputAssetNum")
    public Map<String,String> valid(String inputCode,String tep,String num,String model,String price){
        Map<String,String> map=new HashMap<>();
        String numRegex="[0-9]+";
        String priceRegex="[0-9]+[\\.]?[0-9]+";
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
        Map<String,String> validRes=asmService.valid( inputCode, tep);
        return validRes;
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
        if(search.equals("")){
            if(isDam.equals("完好")){
                page=jpaAssert.findAssertsBytype(type,"0",pageable);
            }else {
                page=jpaAssert.findAssertsBytype(type,"1",pageable);
            }
        }else {
            search=ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAnameLike(search,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnumLike(search,pageable);
                if(page.isEmpty()){
                    page=jpaAssert.findAssertsByBorroworPingyinLike(search,pageable);
                    if(page.isEmpty()){
                        page=jpaAssert.findAssertsByBorroworNameLike(search,pageable);
                    }
                }
            }
        }
        return page;
    }


    @PostMapping("/asm/validForBad")
    public Map<String,String> validForBad(int id){
        Map<String,String> map=new HashMap<>();
        Employee borrower=jpaAssert.findById(id).get().getEmployeeByBorrower();
        if(borrower!=null){
            map.put("error","设备被借出！归还后才能报损");
        }else {
            map.put("ok","校验成功");
        }
        return map;
    }
    @Transactional
    @RequiresPermissions("asm:inp:view")
    @PostMapping("/asm/putin")
    public Map<String,String> putin(String type, String model, String price, String name, String encode, String num){

        Map<String,String> map=new HashMap<>();
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
        int nu=Integer.parseInt(num);
        AssetType assetType=jpaAssetType.findAssetTypeByName(type);

        String head="";
        int lastIndexof=encode.lastIndexOf("-")+1;
        head=encode.substring(0,lastIndexof);
        int tail=Integer.parseInt(encodes[encodes.length-1]);
        int tailLen=encodes[encodes.length-1].length();
        List<Assert> list=new ArrayList<>();
        for (int i=0;i<nu;i++){
            String tailStr=tail+"";
            Assert ast=new Assert();
            ast.setAname(name);
            ast.setPrice(price);
            ast.setModel(model);
            ast.setAssetTypeByAssertType(assetType);

            if((tailStr).length()>tailLen){
                map.put("error","编号超出，入库失败！");
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
            json.put("ok", "文件类型错误");
            return json;
        }
        InputStream inputStream;
        ExcelReader reader = null;
        try {

            String path = ClassUtils.getDefaultClassLoader().getResource("static/excel").getPath();   //上传资源到项目路径的路径获得

            File file = new File(path + "/" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            inputStream = multipartFile.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }

            /**
             *
             * 使用easyExcel读上传的表格
             *
             * */


            try {

                reader = EasyExcel.read(inputStream, EasyExcelMapedModel.class, new ReadAssetEventListener(jpaAssert, jpaEmployee, jpaAssetType, asmService, jpaOperatRecord)).excelType(ExcelTypeEnum.XLSX).build();
                ReadSheet readSheet = EasyExcel.readSheet(0).build();
                reader.read(readSheet);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             *
             * */

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            json.put("error", e.toString());
            System.out.println(e.toString());
            return json;
        } finally {
            if (reader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                reader.finish();
            }
            json.put("ok", "上传成功");
            return json;
        }
    }
    /**
     *
     * 下载模板
     *
     * */
    @RequestMapping("asm/outputTemplate")
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

}

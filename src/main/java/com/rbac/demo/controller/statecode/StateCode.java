package com.rbac.demo.controller.statecode;

import org.springframework.web.bind.annotation.RestController;

//package com.rbac.demo.controller.statecode;
//
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.ExcelReader;
//import com.alibaba.excel.read.metadata.ReadSheet;
//import com.alibaba.excel.support.ExcelTypeEnum;
//import com.alibaba.excel.write.metadata.style.WriteCellStyle;
//import com.alibaba.excel.write.metadata.style.WriteFont;
//import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
//import com.rbac.demo.easyExcel.ReadZentaoTableEventListenner;
//import com.rbac.demo.easyExcel.RtcBugDownloadModel;
//import com.rbac.demo.easyExcel.ZentaoBugTableModel;
//import com.rbac.demo.entity.RtcBug;
//import com.rbac.demo.jpa.JpaRtcBug;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.*;
//
//@RestController
//public class StateCode {
//    @Autowired
//    private JpaRtcBug jpaRtcBug;
//
//
//    @PostMapping("/rtc/input")
//    public Map<String,String> rtc(HttpServletRequest request) throws UnsupportedEncodingException {
//
//        Map<String, String> json = new HashMap<>();
//        request.setCharacterEncoding("UTF-8");
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        /** 页面控件的文件流* */
//        MultipartFile multipartFile = null;
//        Map map = multipartRequest.getFileMap();
//        Set set = map.keySet();
//        Iterator<String> iterator = set.iterator();
//        while (iterator.hasNext()) {
//            multipartFile = (MultipartFile) map.get(iterator.next());
//        }
//        String filename = multipartFile.getOriginalFilename();
//        if (!filename.contains("xlsx")) {
//            json.put("error", "文件类型错误");
//            return json;
//        }
//        InputStream inputStream;
//        ExcelReader reader = null;
//        try {
//
//            inputStream = multipartFile.getInputStream();
//            /**
//             *
//             * 使用easyExcel读上传的表格
//             *
//             * */
//
//
//            try {
//                reader = EasyExcel.read(inputStream, ZentaoBugTableModel.class, new ReadZentaoTableEventListenner(jpaRtcBug)).excelType(ExcelTypeEnum.XLSX).build();
//                ReadSheet readSheet = EasyExcel.readSheet(0).build();
//                reader.read(readSheet);
//            } catch (Exception e) {
//                json.put("error", e.toString());
//                return json;
//            }
//
//            /**
//             *
//             * */
//
//            inputStream.close();
//        } catch (Exception e) {
//            json.put("error", e.toString());
//            System.out.println(e.toString());
//            return json;
//        } finally {
//            if (reader != null) {
//                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
//                reader.finish();
//            }
//
//        }
//        json.put("ok", "上传成功");
//        return json;
//    }
//    @GetMapping("/rtc/out")
//    public void exportExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/octet-stream");
//        response.setCharacterEncoding("utf-8");
//        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = "rtc_export.xlsx";
//        response.setHeader("Content-disposition", "attachment;filename="+fileName);
//        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
//
//        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        WriteFont headWriteFont = new WriteFont();
//        headWriteFont.setFontHeightInPoints((short)11);
//        headWriteFont.setBold(false);
//        headWriteFont.setFontName("等线");
//        headWriteCellStyle.setWriteFont(headWriteFont);
//        // 内容的策略
//        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
//        // 背景绿色
//        WriteFont contentWriteFont = new WriteFont();
//        // 字体大小
//        contentWriteFont.setFontHeightInPoints((short)11);
//        contentWriteFont.setBold(false);
//        contentWriteFont.setFontName("等线");
//        contentWriteCellStyle.setWriteFont(contentWriteFont);
//        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
//                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
//        EasyExcel.write(response.getOutputStream(), RtcBugDownloadModel.class).
//                registerWriteHandler(horizontalCellStyleStrategy).sheet("01").
//                doWrite(data());
//    }
//    private List<RtcBugDownloadModel> data(){
//        List<RtcBug> list = jpaRtcBug.findAll();
//        List<RtcBugDownloadModel> models=new ArrayList<>();
//        for (RtcBug rb:list){
//            RtcBugDownloadModel rtcBugDownloadModel=new RtcBugDownloadModel();
//            rtcBugDownloadModel.setBugCate(rb.getBugCate());
//            rtcBugDownloadModel.setBugClassfy1(rb.getBugClassfy1());
//            rtcBugDownloadModel.setBugClassfy2(rb.getBugClassfy2());
//            rtcBugDownloadModel.setBugCreater(rb.getBugCreater());
//            rtcBugDownloadModel.setBugDefectType(rb.getBugDefectType());
//            rtcBugDownloadModel.setBugDesc(rb.getBugDesc());
//            rtcBugDownloadModel.setBugDiscoveryPhase(rb.getBugDiscoveryPhase());
//            rtcBugDownloadModel.setBugFlag(rb.getRtcId());//rtc id
//            rtcBugDownloadModel.setBugFrequence(rb.getBugFrequence());
//            rtcBugDownloadModel.setBugModule(rb.getBugModule());
//            rtcBugDownloadModel.setBugOwner(rb.getBugOwner());
//            rtcBugDownloadModel.setBugPriority(rb.getBugPriority());
//            rtcBugDownloadModel.setBugVersion(rb.getBugVersion());
//            rtcBugDownloadModel.setBugType(rb.getBugType());
//            rtcBugDownloadModel.setBugSummary(rb.getBugSummary());
//            rtcBugDownloadModel.setBugSeriousness(rb.getBugSeriousness());
//            rtcBugDownloadModel.setBugSrc(rb.getBugSrc());
//            rtcBugDownloadModel.setBugRelative(rb.getBugRelative());
//            models.add(rtcBugDownloadModel);
//        }
//        //清空数据库
//        return models;
//    }
//}
@RestController
public class StateCode {

}
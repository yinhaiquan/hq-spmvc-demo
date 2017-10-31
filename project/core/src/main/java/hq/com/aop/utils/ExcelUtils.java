package hq.com.aop.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @title : excel文件读写工具类
 * @describle :
 * <p>
 *     <b>note:</b>
 *     注意：excel读写文件最大行数65531，对于多行、多列合并的Excel单元不支持，只支持统一规则数据模型;
 *          1. 若非要用poi导出数据: a. 业务控制分批导出 b. 设置sheet每页最大行数，超过最大值新增sheet(本工具类实现此方式)；
 *          2. 采用csv文件导出（适用大数据量导出）；
 * </p>
 * Create By yinhaiquan
 * @date 2017/10/30 14:18 星期一
 */
public class ExcelUtils {
    private final static String XLS_SUFFIX = "xls";
    private final static String XLSX_SUFFIX = "xlsx";
    /*标题栏单元高度*/
    private static short TITLE_HEIGHT = 500;
    /*其他描述信息单元高度*/
    private static short OTHERS_HEIGHT = 500;
    /*头信息高度*/
    private static short HEAD_HEIGHT = 1000;
    /*每页(sheet)显示最大行数，默认5000*/
    private static int SHEET_MAX_SIZE = 5000;

    /**
     * 读取Excel类
     * @descriptions: 默认无行合并项
     */
    public static class ReadExcel {
        /**
         * 读取excel
         * @param param 入参
         * @param sheetInfos sheet解析结果
         */
        public static final void read(ExcelParam.ReadInParam param, List<ExcelParam.ReadOutSheetInfo> sheetInfos) {
            if (StringUtils.isEmpty(param)||StringUtils.isEmpty(param.getFileName())){
                System.err.println("入参为空");
                return;
            }
            String fileType = param.getFileName().substring(param.getFileName().lastIndexOf(StringUtils.POINT) + 1, param.getFileName().length());
            InputStream is = null;
            Workbook wb = null;
            try {
                is = new FileInputStream(param.getFileName());
                if (fileType.equals(XLS_SUFFIX)){
                    wb = new HSSFWorkbook(is);
                } else if (fileType.equals(XLSX_SUFFIX)){
                    wb = new XSSFWorkbook(is);
                } else {
                    System.err.println("文件类型错误，不是Excel文件");
                }
                int sheetSize = wb.getNumberOfSheets();
                //遍历sheet页
                for (int i = 0; i < sheetSize ; i++) {
                    ExcelParam ep = new ExcelParam();
                    ExcelParam.ReadOutSheetInfo sheetInfo = ep.new ReadOutSheetInfo();
                    Sheet sheet = wb.getSheetAt(i);
                    sheetInfo.setSheetName(sheet.getSheetName());
                    List<Map<String,String>> sheetList = new ArrayList<>();
                    List<String> titles = new ArrayList<>();
                    int rowSize = sheet.getLastRowNum()+1;
                    int index = 0;
                    /*是否包含头信息*/
                    if (param.isHead()){
                        Row row = sheet.getRow(index);
                        sheetInfo.setSheetHead(getMergedRegionValue(sheet,0,row.getLastCellNum()-1));
                        index++;
                    }
                    /*是否包含其他信息*/
                    if (param.isOthers()){
                        List<String> others = new ArrayList<>();
                        for(int j=0;j<param.getRows();j++){
                            Row row = sheet.getRow(j+index);
                            StringBuffer sb = new StringBuffer();
                            for (Cell c : row) {
                                sb.append(formartCellValue(c));
                                sb.append(StringUtils.UNDER_RAIL);
                            }
                            others.add(sb.toString());
                        }
                        sheetInfo.setOthers(others);
                        index+=param.getRows();
                    }
                    /*是否包含标题信息*/
                    if (param.isTitle()){
                        Row row = sheet.getRow(index);
                        int cellSize = row.getLastCellNum();
                        for (int k = 0; k < cellSize; k++) {
                            Cell cell = row.getCell(k);
                            titles.add(cell.toString());
                        }
                        sheetInfo.setTitles(titles);
                        index++;
                    }

                    //遍历行
                    for (int j = 0+index; j <rowSize ; j++) {
                        Row row = sheet.getRow(j);
                        //空行不处理
                        if (StringUtils.isEmpty(row)||StringUtils.isEmpty(formartCellValue(row.getCell(0)))){
                            continue;
                        }
                        int cellSize = row.getLastCellNum();
                        //数据行
                        Map<String,String> rowData = new HashMap<>();
                        for (int k = 0; k < cellSize; k++) {
                            Cell cell = row.getCell(k);
                            String key = StringUtils.isEmpty(titles)?"row"+k:titles.get(k);
                            String value = formartCellValue(cell);
                            rowData.put(key,value);
                        }
                        sheetList.add(rowData);
                    }
                    sheetInfo.setBody(sheetList);
                    sheetInfos.add(sheetInfo);
                }
            } catch (FileNotFoundException e) {
                System.err.println(StringUtils.format("找不到文件:{0}",e.getMessage()));
            } catch (IOException e) {
                System.err.println(StringUtils.format("Excel文件读取时抛出异常:{0}",e.getMessage()));
            } finally {
                try {
                    if (null!=wb){
                        wb.close();
                    }
                    if (null!=is){
                        is.close();
                    }
                } catch (IOException e) {
                    System.err.println(StringUtils.format("关闭文件流抛出异常:{0}",e.getMessage()));
                }
            }
        }

        /**
         * 格式化单元格数据类型
         * @param cell 单元格
         * @return
         */
        private static final String formartCellValue(Cell cell){
            if (StringUtils.isEmpty(cell)){
                return null;
            }
            switch (cell.getCellTypeEnum()){
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)){
                        return DateUtils.dateToString(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()),DateUtils.YYYY_MM_DD_HH_MM_SS);
                    }
                    DecimalFormat df = new DecimalFormat("#.########");
                    return df.format(cell.getNumericCellValue());
                case BLANK:
                    return "";
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return null;
            }
        }

        /**
         * 判断指定的单元格是否是合并单元格
         * @param sheet
         * @param row 行下标
         * @param column 列下标
         * @return
         */
        private static final boolean isMergedRegion(Sheet sheet,int row ,int column) {
            int sheetMergeCount = sheet.getNumMergedRegions();
            for (int i = 0; i < sheetMergeCount; i++) {
                CellRangeAddress range = sheet.getMergedRegion(i);
                int firstColumn = range.getFirstColumn();
                int lastColumn = range.getLastColumn();
                int firstRow = range.getFirstRow();
                int lastRow = range.getLastRow();
                if(row >= firstRow && row <= lastRow){
                    if(column >= firstColumn && column <= lastColumn){
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * 获取合并单元格的值
         * @param sheet
         * @param row 行数
         * @param column 单元格数
         * @return
         */
        private static final String getMergedRegionValue(Sheet sheet ,int row , int column){
            int sheetMergeCount = sheet.getNumMergedRegions();
            for(int i = 0 ; i < sheetMergeCount ; i++){
                CellRangeAddress ca = sheet.getMergedRegion(i);
                int firstColumn = ca.getFirstColumn();
                int lastColumn = ca.getLastColumn();
                int firstRow = ca.getFirstRow();
                int lastRow = ca.getLastRow();
                if(row >= firstRow && row <= lastRow){
                    if(column >= firstColumn && column <= lastColumn){
                        Row fRow = sheet.getRow(firstRow);
                        Cell fCell = fRow.getCell(firstColumn);
                        return formartCellValue(fCell) ;
                    }
                }
            }
            return null ;
        }
    }

    /**
     * 写Excel类
     * 支持xlsx、xls类型文件
     */
    public static class WriteExcel {
        /**若response不为空，取fileName为文件名，亦可不指定，按默认规则生成文件名*/
        private String fileName;
        /**输出文件流*/
        private OutputStream outputStream;
        /**springmvc响应体*/
        private HttpServletResponse response;

        /**
         * 指定生成文件名（可选）
         * @description： 前提是response不为空，及直接生成excel文件流返回客户端
         * @param fileName 带后缀文件名
         * @return
         */
        public WriteExcel setOutPutFileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        /**
         * 创建Excel文件输出流（必选）
         * @param object OutputStream、HttpServletResponse
         * @return
         */
        public WriteExcel buildStream(Object object) throws IOException {
            if (object instanceof OutputStream){
                this.outputStream = (OutputStream) object;
            } else if (object instanceof HttpServletResponse){
                this.response = (HttpServletResponse) object;
                configResponse();
            }
            return this;
        }

        /**
         * 写数据(必选)
         * @param sheets
         * @param loadSheet
         */
        public void write(List<ExcelParam.WriteParam> sheets,ExcelParam.LoadSheet loadSheet){
            Workbook wb = null;
            try{
                if (StringUtils.isEmpty(outputStream)&&StringUtils.isEmpty(response)){
                    System.err.println("输出流为空!请调用buildStream方法设置输出流!");
                    return;
                }
                wb = new HSSFWorkbook();
                if (StringUtils.isNotEmpty(sheets)){
                    for (int i = 0;i<sheets.size();i++) {
                        ExcelParam.WriteParam sheetInfo = sheets.get(i);
                        if (StringUtils.isEmpty(sheetInfo.getBody())){
                            continue;
                        }
                        List<?> body = ListCopyUtils.deepCopy(sheetInfo.getBody());
                        int total = body.size();
                        int pages = 1;
                        pages = total/SHEET_MAX_SIZE;
                        if (total%SHEET_MAX_SIZE!=0){
                            pages+=1;
                        }
                        for (int j = 1; j <= pages; j++) {
                            int start = (j-1)*SHEET_MAX_SIZE;
                            int end = j==pages?total:j*SHEET_MAX_SIZE;
                            sheetInfo.setBody(body.subList(start,end));
                            StringBuffer sb = new StringBuffer();
                            sb.append(StringUtils.isNotEmpty(sheetInfo.getSheetName())?sheetInfo.getSheetName():("sheet"+i+1));
                            sb.append("【").append(start).append("-").append(end).append("】");
                            setData(wb,sheetInfo,loadSheet,i,sb.toString());
                        }
                    }
                }
                wb.write(outputStream);
            }catch (Exception e){
                System.err.println(StringUtils.format("excel导出到输出流抛出异常:{0}",e.getMessage()));
            } finally {
                if (StringUtils.isNotEmpty(wb)){
                    try {
                        wb.close();
                    } catch (IOException e) {
                        System.err.println(StringUtils.format("关闭文件流抛出异常:{0}",e.getMessage()));
                    }
                }
            }
        }

        /**sheet页面大小限制分页输出*/
        private void setData(Workbook wb, ExcelParam.WriteParam sheetInfo, ExcelParam.LoadSheet loadSheet, int i,String sheetName){
            int index = 0;
            Sheet sheet = wb.createSheet(sheetName);
            /**取标题栏长度 默认5 也可以在实现接口中传值final使用*/
            int titleLength = 5;
            if (StringUtils.isNotEmpty(sheetInfo.getTitles())){
                titleLength = sheetInfo.getTitles().size();
            }
            /**加载头信息*/
            index = loadHead(wb,sheet,index,titleLength,sheetInfo,loadSheet);
            /**加载其他信息*/
            index = loadOthers(wb,sheet,index,titleLength,sheetInfo,loadSheet);
            /**加载标题信息*/
            index = loadTitles(wb,sheet,index,sheetInfo,loadSheet);
            /**加载数据体信息*/
            loadBody(wb,sheet,index,titleLength,sheetInfo,loadSheet);
        }

        /**加载头信息*/
        private int loadHead(Workbook wb, Sheet sheet, int index, int titleLength, ExcelParam.WriteParam sheetInfo,ExcelParam.LoadSheet loadSheet){
            if (StringUtils.isEmpty(sheetInfo.getSheetHead())){
                System.err.println("头信息为空!");
                return index;
            }
            CellRangeAddress region = new CellRangeAddress(index, index, (short) 0, titleLength-1);
            sheet.addMergedRegion(region);
            Row row = sheet.createRow(index);
            row.setHeight(HEAD_HEIGHT);
            Cell cell = row.createCell(0);
            cell.setCellValue(sheetInfo.getSheetHead());
            cell.setCellStyle(loadSheet.defaultStyle(wb,HorizontalAlignment.CENTER));
            loadSheet.formartRegionStyle(sheet,region,loadSheet.defaultStyle(wb,HorizontalAlignment.CENTER));
            index++;
            return index;
        }

        /**加载其他信息*/
        private int loadOthers(Workbook wb, Sheet sheet, int index, int titleLength, ExcelParam.WriteParam sheetInfo,ExcelParam.LoadSheet loadSheet){
            if (StringUtils.isEmpty(sheetInfo.getOthers())){
                System.err.println("其他信息为空!");
                return index;
            }
            for (String other : sheetInfo.getOthers()) {
                loadSheet.loadOther(wb,sheet,other,index,titleLength,OTHERS_HEIGHT);
                index++;
            }
            return index;
        }

        /**加载标题信息*/
        private int loadTitles(Workbook wb, Sheet sheet, int index, ExcelParam.WriteParam sheetInfo,ExcelParam.LoadSheet loadSheet){
            if (StringUtils.isEmpty(sheetInfo.getTitles())){
                System.err.println("标题栏为空!");
                return index;
            }
            Row row = sheet.createRow(index);
            row.setHeight(TITLE_HEIGHT);
            for (int i = 0;i<sheetInfo.getTitles().size();i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(sheetInfo.getTitles().get(i));
                sheet.setColumnWidth(i,sheetInfo.getTitles().get(i).length()*10*256);
                if (StringUtils.isNotEmpty(loadSheet.titleStyle(wb))){
                    cell.setCellStyle(loadSheet.titleStyle(wb));
                } else {
                    cell.setCellStyle(loadSheet.defaultStyle(wb,HorizontalAlignment.CENTER));
                }
            }
            return index++;
        }
        
        /**加载数据体信息*/
        private void loadBody(Workbook wb, Sheet sheet, int index, int titleLength, ExcelParam.WriteParam sheetInfo,ExcelParam.LoadSheet loadSheet){
            if (StringUtils.isEmpty(sheetInfo.getBody())){
                System.err.println("数据体为空!");
                return;
            }
            for (int i=0;i<sheetInfo.getBody().size();i++){
                Row row = sheet.createRow(index+1);
                String [] objs = loadSheet.loadBody(sheetInfo.getBody().get(i),titleLength);
                for (int j=0;j<objs.length;j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(objs[j]);
                    if (StringUtils.isNotEmpty(loadSheet.bodyStyle(wb))){
                        cell.setCellStyle(loadSheet.bodyStyle(wb));
                    }
                }
                index++;
            }
        }

        /**
         * 配置文件流直接输出客户端配置
         * @throws IOException
         */
        private void configResponse() throws IOException {
            this.response.reset();
            this.response.setContentType("application/msexcel;charset=GBK");
            String filename = "";
            if (StringUtils.isEmpty(this.fileName)){
                fileName = UUID.randomUUID().toString().replaceAll(StringUtils.RAIL,StringUtils.BLANK);
            }
            try {
                filename = URLEncoder.encode(this.fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.err.println(e.getMessage());
                try {
                    filename = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
                } catch (UnsupportedEncodingException e1) {
                    System.err.println(e1.getMessage());
                }
            }
            StringBuffer sb = new StringBuffer();
            sb.append(filename).append(StringUtils.POINT).append(XLSX_SUFFIX);
            filename = sb.toString();
            this.response.setHeader("content-disposition", "attachment; filename=" + filename);
            this.outputStream = response.getOutputStream();
        }


    }

    public static void main(String[] args) throws IOException {
        /**测试生成Excel文件*/
        ExcelUtils.WriteExcel we = new ExcelUtils.WriteExcel();
        ExcelParam ep = new ExcelParam();
        List<ExcelParam.WriteParam> sheets = new ArrayList<>();
        ExcelParam.WriteParam sheet = ep.new WriteParam();
        sheet.setSheetHead("测试");
        sheet.setSheetName("超过规定数据");
        List<String> title = new ArrayList<>();
        title.add("呵呵1");
        title.add("呵呵2");
        title.add("呵呵3");
        title.add("呵呵4");
        title.add("呵呵5");
        title.add("呵呵6");
        sheet.setTitles(title);
        List<String> others = new ArrayList<>();
        others.add("fuck:sdfsdf");
        others.add("fuck:sdfsdf");
        others.add("fuck:sdfsdf");
        sheet.setOthers(others);
        Map<String,Object> m1 = new HashMap<>();
        m1.put("t1","12");
        m1.put("t2","sdasdfds");
        m1.put("t3","中文");
        m1.put("t4",123);
        m1.put("t5",new Date());
        m1.put("t6",123.9910203);
        List<Map<String,Object>> body = new ArrayList<>();
        body.add(m1);
        body.add(m1);
        body.add(m1);
        body.add(m1);
        body.add(m1);
        sheet.setBody(body);
        sheets.add(sheet);
        we.buildStream(new FileOutputStream("g:/123.xlsx")).write(sheets,ep.new LoadSheet<Map<String,Object>>(){
            @Override
            public String[] loadBody(Map<String, Object> obj, int titleLength) {
                String [] objs = new String[titleLength];
                objs[0]=obj.get("t1")+"";
                objs[1]=obj.get("t2")+"";
                objs[2]=obj.get("t3")+"";
                objs[3]=obj.get("t4")+"";
                objs[4]=obj.get("t5")+"";
                objs[5]=obj.get("t6")+"";
                return objs;
            }

            @Override
            public CellStyle bodyStyle(Workbook wb) {
                CellStyle cellStyle = wb.createCellStyle();
                Font font = wb.createFont();
                font.setFontName("宋体");
                font.setBold(false);//粗体显示
                cellStyle.setFont(font);
                cellStyle.setWrapText(true);
                return cellStyle;
            }

            @Override
            public CellStyle titleStyle(Workbook wb) {
                return null;
            }
        });

//        ExcelParam ep = new ExcelParam();
//        ExcelParam.ReadInParam readInParam = ep.new ReadInParam();
//        readInParam.setFileName("C:\\Users\\kidy\\Desktop\\rmb\\rechargeConfimetemplate2.xlsx");
//        readInParam.setHead(true);
//        readInParam.setOthers(true);
//        readInParam.setRows(2);
//        readInParam.setTitle(true);
//        List<ExcelParam.ReadOutSheetInfo> list = new ArrayList<>();
//        ExcelUtils.ReadExcel.read(readInParam,list);
//        System.out.println(list);
    }

}

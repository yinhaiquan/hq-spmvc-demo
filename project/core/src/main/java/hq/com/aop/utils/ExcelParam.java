package hq.com.aop.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;

/**
 * @title : excel出入参类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/10/30 15:39 星期一
 */
public class ExcelParam {
    /**
     * 读取结果类
     */
    public class ReadOutSheetInfo{
        /**SHEET名称*/
        private String sheetName;
        /**头信息*/
        private String sheetHead;
        /**其他描述信息，采用'_'隔开每行中每个单元格数据成字符串存储*/
        private List<String> others;
        /**标题列表*/
        private List<String> titles;
        /**数据体*/
        private List<Map<String,String>> body;

        public ReadOutSheetInfo() {
        }

        public ReadOutSheetInfo(String sheetName, String sheetHead, List<String> others, List<String> titles, List<Map<String, String>> body) {
            this.sheetName = sheetName;
            this.sheetHead = sheetHead;
            this.others = others;
            this.titles = titles;
            this.body = body;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getSheetHead() {
            return sheetHead;
        }

        public void setSheetHead(String sheetHead) {
            this.sheetHead = sheetHead;
        }

        public List<String> getOthers() {
            return others;
        }

        public void setOthers(List<String> others) {
            this.others = others;
        }

        public List<String> getTitles() {
            return titles;
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        public List<Map<String, String>> getBody() {
            return body;
        }

        public void setBody(List<Map<String, String>> body) {
            this.body = body;
        }

        @Override
        public String toString() {
            return "ReadOutSheetInfo{" +
                    "sheetName='" + sheetName + '\'' +
                    ", sheetHead='" + sheetHead + '\'' +
                    ", others=" + others +
                    ", titles=" + titles +
                    ", body=" + body +
                    '}';
        }
    }

    /**
     * 读取入参类
     */
    public class ReadInParam {
        /**Excel文件全路径*/
        private String fileName;
        /**是否含有标题*/
        private boolean isTitle;
        /**是否含有头信息(默认第一行文件头信息)*/
        private boolean isHead;
        /**是否含有其他描述信息*/
        private boolean isOthers;
        /**其他描述信息所占行数 若设置了isOthers则必须设置rows*/
        private int rows;

        public ReadInParam() {
        }

        public ReadInParam(String fileName, boolean isTitle, boolean isHead, boolean isOthers, int rows) {
            this.fileName = fileName;
            this.isTitle = isTitle;
            this.isHead = isHead;
            this.isOthers = isOthers;
            this.rows = rows;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public boolean isTitle() {
            return isTitle;
        }

        public void setTitle(boolean title) {
            isTitle = title;
        }

        public boolean isHead() {
            return isHead;
        }

        public void setHead(boolean head) {
            isHead = head;
        }

        public boolean isOthers() {
            return isOthers;
        }

        public void setOthers(boolean others) {
            isOthers = others;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        @Override
        public String toString() {
            return "ReadInParam{" +
                    "fileName='" + fileName + '\'' +
                    ", isTitle=" + isTitle +
                    ", isHead=" + isHead +
                    ", isOthers=" + isOthers +
                    ", rows=" + rows +
                    '}';
        }
    }

    /**
     * 写参数类
     */
    public class WriteParam {
        /**头信息*/
        private String sheetHead;
        /**sheet页名*/
        private String sheetName;
        /**标题栏列表信息*/
        private List<String> titles;
        /**数据体*/
        private List<?> body;
        /**其他描述信息*/
        private List<String> others;

        public WriteParam() {
        }

        public WriteParam(String sheetHead, String sheetName, List<String> titles, List<?> body, List<String> others) {
            this.sheetHead = sheetHead;
            this.sheetName = sheetName;
            this.titles = titles;
            this.body = body;
            this.others = others;
        }

        public String getSheetHead() {
            return sheetHead;
        }

        public void setSheetHead(String sheetHead) {
            this.sheetHead = sheetHead;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public List<String> getTitles() {
            return titles;
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        public List<?> getBody() {
            return body;
        }

        public void setBody(List<?> body) {
            this.body = body;
        }

        public List<String> getOthers() {
            return others;
        }

        public void setOthers(List<String> others) {
            this.others = others;
        }

        @Override
        public String toString() {
            return "WriteParam{" +
                    "sheetHead='" + sheetHead + '\'' +
                    ", sheetName='" + sheetName + '\'' +
                    ", titles=" + titles +
                    ", body=" + body +
                    ", others=" + others +
                    '}';
        }
    }

    /**
     * 加载Excel数据
     * 注意：
     *     所有抽象方法必须返回index
     */
    public abstract class LoadSheet<T> {

        /**
         * 设置数据体
         * @param obj 数据体信息
         * @param titleLength 标题长度
         */
        public abstract String[] loadBody(T obj, int titleLength);

        /**
         * body样式
         * @return
         */
        public abstract CellStyle bodyStyle(Workbook wb);

        /**
         * 标题栏样式(可不设置，采用默认样式)
         * @return
         */
        public abstract CellStyle titleStyle(Workbook wb);

        /**
         * 默认样式(头信息、标题栏信息、其他信息采用默认样式)
         * @descriptions:
         *          1. FillPatternType背景颜色颜色类型选择：
         *             http://blog.csdn.net/for_china2012/article/details/29844661
         * @param wb
         * @return
         */
        public CellStyle defaultStyle(Workbook wb,HorizontalAlignment alignment){
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(alignment); // 水平居中 HorizontalAlignment.CENTER
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = wb.createFont();
            font.setFontName("黑体");
            font.setBold(true);//粗体显示
            cellStyle.setFont(font);
            cellStyle.setWrapText(true);
            return cellStyle;
        }

        /**
         * 设置其他描述信息实体信息（可重写此方法，以适应业务变化）
         * @param wb Workbook
         * @param sheet sheet
         * @param other 其他描述信息
         * @param index 行下标
         * @param titleLength 标题长度
         * @return 行下标值
         */
        public void loadOther(Workbook wb,Sheet sheet, String other, int index,int titleLength,short height){
            Row row = sheet.createRow(index);
            row.setHeight(height);
            CellRangeAddress region = new CellRangeAddress(index, index, (short) 0, titleLength-1);
            sheet.addMergedRegion(region);
            Cell cell = row.createCell(0);
            cell.setCellValue(other);
            cell.setCellStyle(defaultStyle(wb,HorizontalAlignment.LEFT));
            formartRegionStyle(sheet,region,defaultStyle(wb,HorizontalAlignment.LEFT));
        }

        /**
         * 解决合并单元格边框消失问题，
         * 不仅需要调用此方法，单元格自
         * 身也需要正常设置上下左右的边框
         * @param sheet
         * @param region
         * @param cs
         */
        public void formartRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs) {
            for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
                }
                for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(cs);
                }
            }
        }
    }
}

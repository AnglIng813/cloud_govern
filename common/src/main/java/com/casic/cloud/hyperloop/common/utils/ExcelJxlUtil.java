package com.casic.cloud.hyperloop.common.utils;

import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
public class ExcelJxlUtil {
    /**
     * 导出excel
     *
     * @param list      数据集合
     * @param fieldMap  类的英文属性和Excel中的中文列名的对应关系
     * @param sheetName 工作表的名称
     * @param out       导出流
     */
    private static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, OutputStream out) {
        if (list.size() == 0 || list == null) {
            throw new CloudApiServerException(9999, "数据源中没有任何数据");
        }
        int sheetSize = list.size();
//		if (sheetSize > 65535 || sheetSize < 1) {
//			sheetSize = 65535;
//		}
        if (sheetSize > 30000 || sheetSize < 1) {
            sheetSize = 30000;
        }
        // 创建工作簿并发送到OutputStream指定的地方
        WritableWorkbook wwb;
        try {
            wwb = Workbook.createWorkbook(out);
            // 1.计算一共有多少个工作表
            int sheetNum = list.size() % sheetSize == 0 ? list.size() / sheetSize : (list.size() / sheetSize + 1);

            // 2.创建相应的工作表，并向其中填充数据
            for (int i = 0; i < sheetNum; i++) {
                // 如果只有一个工作表的情况
                if (1 == sheetNum) {
                    WritableSheet sheet = wwb.createSheet(sheetName, i);
                    fillSheet(sheet, list, fieldMap, 0, list.size() - 1);
                    // 有多个工作表的情况
                } else {
                    WritableSheet sheet = wwb.createSheet(sheetName + (i + 1), i);
                    // 获取开始索引和结束索引
                    int firstIndex = i * sheetSize;
                    int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1 : (i + 1) * sheetSize - 1;
                    // 填充工作表
                    fillSheet(sheet, list, fieldMap, firstIndex, lastIndex);
                }
            }
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置工作表自动列宽和首行加粗
    private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
        // 获取本列的最宽单元格的宽度
        for (int i = 0; i < ws.getColumns(); i++) {
            int colWith = 0;
            for (int j = 0; j < ws.getRows(); j++) {
                String content = ws.getCell(i, j).getContents().toString();
                int cellWith = content.length();
                if (colWith < cellWith) {
                    colWith = cellWith;
                }
            }
            // 设置单元格的宽度为最宽宽度+额外宽度
            ws.setColumnView(i, colWith + extraWith + 10);

        }
    }

    //向工作表中填充数据
    private static <T> void fillSheet(WritableSheet sheet, List<T> list,
                                      LinkedHashMap<String, String> fieldMap, int firstIndex, int lastIndex) throws Exception {

        // 定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        // 填充数组
        int count = 0;
        for (Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }

        //设置title
        Label title = new Label(0,0,sheet.getName(),setFormat());
        sheet.addCell(title);
        //合并title单元格
        sheet.mergeCells(0,0,--count,0);
        // 填充表头
        for (int i = 0; i < cnFields.length; i++) {
            Label label = new Label(i, 1, cnFields[i],setFormat());
            sheet.setRowView(0,500,false);
            sheet.addCell(label);
        }
        // 填充内容
        int rowNo = 2;
        for (int index = firstIndex; index <= lastIndex; index++) {
            // 获取单个对象
            T item = list.get(index);
            for (int i = 0; i < enFields.length; i++) {
                Object objValue = null;
                objValue = ReflectUtil.getNestedProperty(item, enFields[i]);
                String fieldValue = objValue == null ? "" : objValue.toString();
                Label label = new Label(i, rowNo, fieldValue);
                sheet.addCell(label);
            }
            rowNo++;
        }
        // 设置自动列宽
        setColumnAutoSize(sheet, 5);
    }

    /**
     * @param response
     * @param list        数据list
     * @param columnsList 表字段list
     * @param fileName    导出文件名
     * @param <T>
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> list, List<Map<String, String>> columnsList, String fileName, String sheetName) {
        try {
            //转换为 key 英文  value 中文 map
            LinkedHashMap<String, String> map = convertToMap(columnsList);
            //配置response
            response.setContentType("application/vnd.ms-excel");
            // iii.将文件名设置到响应消息头中
            fileName = new String(fileName.getBytes(), "iso8859-1")
                    + DateUtil.convert2Timestample(LocalDate.now()) + ".xls";
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            response.setCharacterEncoding("UTF-8");
            response.flushBuffer();
            ExcelJxlUtil.listToExcel(list, map, sheetName, response.getOutputStream());
        } catch (Exception e) {
            log.error("【按人员权限导出日志】失败");
            e.printStackTrace();
            throw new CloudApiServerException(9999, "日志导出失败");
        }
    }

    private static LinkedHashMap<String, String> convertToMap(List<Map<String, String>> columnsList) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        columnsList.stream().forEach(e -> {
            String columnName = e.get("columnName");
            if (columnName.indexOf("_") > 0) { //下划线转驼峰
                String[] split = columnName.split("_");
                String temp = "";
                for (int i = 1; i < split.length; i++) {
                    String firstStr = String.valueOf(split[i].charAt(0)).toUpperCase();
                    String substring = split[i].substring(1, split[i].length());
                    temp += firstStr + substring;
                }
                columnName = split[0] + temp;
            }
            map.put(columnName, e.get("columnComment"));
        });
        return map;
    }

    /**
     * 设置表头样式
     * @return
     * @throws WriteException
     */
    private static WritableCellFormat setFormat() throws WriteException {
        //设置字体
        WritableFont font = new WritableFont(WritableFont.ARIAL,12,WritableFont.BOLD);
        WritableCellFormat format = new WritableCellFormat(font);
        //设置样式居中
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        return format;
    }
}

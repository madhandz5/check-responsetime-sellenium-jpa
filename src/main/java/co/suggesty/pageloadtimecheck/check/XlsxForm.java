package co.suggesty.pageloadtimecheck.check;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component("xlsxForm")
public class XlsxForm extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(
            Map<String, Object> model, Workbook workbook
            ,HttpServletRequest request,  HttpServletResponse response)
            throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"speedcheck.xlsx\"");
        List<Check> xlsxCheckList = (List<Check>) model.get("xlsxCheckList");
        Sheet sheet = workbook.createSheet("speedcheck");

        CellStyle colorCellStyle = workbook.createCellStyle();
        colorCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        colorCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle redCellStyle = workbook.createCellStyle();
        redCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle numberCellStyle = workbook.createCellStyle();
        DataFormat numberDataFormat = workbook.createDataFormat();
        numberCellStyle.setDataFormat(numberDataFormat.getFormat("#,##0"));

        Row headerRow = sheet.createRow(0);
        Cell header0 = headerRow.createCell(0);
        header0.setCellValue("URL");
        header0.setCellStyle(colorCellStyle);
        Cell header1 = headerRow.createCell(1);
        header1.setCellValue("소요시간(ms)");
        header1.setCellStyle(colorCellStyle);
        Cell header2 = headerRow.createCell(2);
        header2.setCellValue("확인시점");
        header2.setCellStyle(colorCellStyle);

        for (int i = 0; i < xlsxCheckList.size() ; i++) {
            Check xlsxCheck = xlsxCheckList.get(i);
            Row row = sheet.createRow(i+1);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(xlsxCheck.getWebPage().getPageName());

            Cell cell1 = row.createCell(1);
            int ms = xlsxCheck.getTime();
            cell1.setCellValue(ms);
            cell1.setCellStyle(numberCellStyle);

            if (ms >= 5000) {
                cell1.setCellStyle(redCellStyle);
            }

            Cell cell2 = row.createCell(2);
            String checkedAt = String.valueOf(xlsxCheck.getCheckedAt());
            cell2.setCellValue(checkedAt);
        }
    }
}

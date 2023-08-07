package it.csi.smranag.smrgaa.presentation.excel;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;


/**
 * Classe generica per la generazione dei file excel
 * <p>Title: Smrgaa</p>
 * <p>Description: Classe di utilità per la generazione di file excel</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */
public abstract class ExcelServlet extends HttpServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 4249720251206534343L;
  
  
  
  public static final String  CONTENT_TYPE_DOWNLOAD="download";
  public static final String  FORMAT_DATE_1="dd/mm/yyyy";
  public static final String  FORMAT_DATE_2="dd/mm/yyyy HH:MM:SS";


  /**
   * Imposta i parametri dell'header della risposta, di defaul richiede
   * il salvataggio dell'excel, se si vuole farlo aprire direttamente dal
   * browser occorre cambiare i valori dell'header
   * @param resp
   */
  public void setResponseHeader(HttpServletResponse resp,HttpServletRequest request,String fileName)
  {
    resp.reset();
    resp.setContentType(ExcelServlet.CONTENT_TYPE_DOWNLOAD);
    resp.addHeader("Content-Disposition","attachment;filename = "+fileName+".xls");
  }



  /**
   * Crea una cella con contenuto un valore testuale
   * @param row
   * @param style
   * @param text
   * @param indiceCella
   */
  public static void buildCell(HSSFRow row, HSSFCellStyle style, String text,
                         short indiceCella)
  {
    HSSFCell cell = row.createCell(indiceCella);
    cell.setCellValue(text);
    if (style != null)
      cell.setCellStyle(style);
  }

  public static void buildCell(HSSFRow row, HSSFCellStyle style, long num,
                         short indiceCella)
  {
    HSSFCell cell = row.createCell(indiceCella);
    cell.setCellValue(num);
    if (style != null)
      cell.setCellStyle(style);
  }

  public static void buildCell(HSSFRow row, HSSFCellStyle style, double num,
                         short indiceCella)
  {
    HSSFCell cell = row.createCell(indiceCella);
    cell.setCellValue(num);
    if (style != null)
      cell.setCellStyle(style);
  }

  public static void buildCell(HSSFRow row, HSSFCellStyle style, Date date,
                         short indiceCella)
  {
    HSSFCell cell = row.createCell(indiceCella);
    cell.setCellValue(date);
    if (style != null)
      cell.setCellStyle(style);
  }



  /**
   * Crea una cella e ci mette dentro una formula
   * @param row
   * @param style
   * @param text
   * @param indiceCella
   */
  public void buildCellFormula(HSSFRow row, HSSFCellStyle style, String text,
                         short indiceCella)
  {
    HSSFCell cell = row.createCell(indiceCella);
    cell.setCellFormula(text);
    if (style != null)
      cell.setCellStyle(style);
  }

  public static void autosizeColumns(HSSFWorkbook workBook) {
      Graphics graphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();

      for(int k=0; k<workBook.getNumberOfSheets(); k++) {
        HSSFSheet sheet = workBook.getSheetAt(k);
        int columnsWidth[] = new int[0];
        Iterator<?> iterRows = sheet.rowIterator();
        Vector<Region> mergedRegions = new Vector<Region>();

        for(int i=0; i<sheet.getNumMergedRegions(); i++) {
          Region region = sheet.getMergedRegionAt(i);
          //SolmrLogger.debug(this, "MERGED REGION : COLUMN FROM "+region.getColumnFrom()+" - COLUMN TO : "+region.getColumnTo()+" - ROW FROM "+region.getRowFrom()+" - ROW TO "+region.getRowTo());
          if(region.getColumnTo()-region.getColumnFrom()>0)
            //SolmrLogger.debug(this, "REGION ADDED!!!");
            mergedRegions.add(region);
        }

        while(iterRows.hasNext()) {
          HSSFRow row = (HSSFRow)iterRows.next();

          //SolmrLogger.debug(this, "ROW "+row.getRowNum()+" WITH "+row.getLastCellNum()+" CELLS");
          for(int i=row.getFirstCellNum(); i<=row.getLastCellNum(); i++) {
            HSSFCell cell = row.getCell((short)i);
            if(cell!=null) {
              HSSFFont font = workBook.getFontAt(cell.getCellStyle().getFontIndex());
              //SolmrLogger.debug(this, "CELL NUMBER "+i+" WITH FONT "+font.getFontName()+" (BOLD "+(font.getBoldweight()==font.BOLDWEIGHT_BOLD)+" - ITALIC "+font.getItalic()+")");
              int mergedColumnsNum = 1;
              int lastColumnsMergeSize = 0;
              int fontStyle = Font.PLAIN;
              if(font.getBoldweight()==HSSFFont.BOLDWEIGHT_BOLD)
                fontStyle = fontStyle|Font.BOLD;
              if(font.getItalic())
                fontStyle = fontStyle|Font.ITALIC;
              Font awtFont = new Font(font.getFontName(), fontStyle, font.getFontHeight());

              String cellValue;
              if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC)
                cellValue = ""+cell.getNumericCellValue();
              else
                cellValue = cell.getStringCellValue();

              FontMetrics fontMetrics = graphics.getFontMetrics(awtFont);

              //int stringWidth = fontMetrics.stringWidth(cellValue);
              int stringWidth = 0;

              if(cellValue.indexOf("\n")>=0&&cell.getCellStyle().getWrapText()) {
                StringTokenizer st = new StringTokenizer(cellValue, "\n");
                int tokenLength;
                while(st.hasMoreTokens()) {
                  tokenLength = fontMetrics.stringWidth(st.nextToken());
                  stringWidth = Math.max(stringWidth, tokenLength);
                }
              }else
                stringWidth = fontMetrics.stringWidth(cellValue);

              //SolmrLogger.debug(this, "CELL VALUE : "+cellValue+" - LENGTH "+cellValue.length()+"- WIDTH "+stringWidth);
              for(int j=0; j<mergedRegions.size(); j++) {
                Region region = (Region)mergedRegions.get(j);
                if(region.contains(row.getRowNum(), (short)i)) {
                  int startCol = Math.min(region.getColumnFrom(), region.getColumnTo());
                  if(i==startCol) {
                    mergedColumnsNum = Math.abs(region.getColumnTo()-region.getColumnFrom())+1;
                    stringWidth = stringWidth / (mergedColumnsNum);
                    lastColumnsMergeSize = Math.max(Math.max(region.getColumnFrom(), region.getColumnTo())-row.getLastCellNum(), 0);
                    //SolmrLogger.debug(this, "IN REGION: COLUMNS NUM "+mergedColumnsNum+" - STRING WIDTH "+stringWidth);
                  }else {
                    mergedColumnsNum = 0;
                  }
                  break;
                }else if(region.getRowFrom() < row.getRowNum() &&
                     region.getRowTo() < row.getRowNum()) {
                    mergedRegions.remove(j--);
                }
              }

              if(columnsWidth.length<row.getLastCellNum()+lastColumnsMergeSize+1) {
                //SolmrLogger.debug(this, "AUMENTO LARGHEZZA ARRAY DA "+columnsWidth.length+" A "+(row.getLastCellNum()+lastColumnsMergeSize+1));
                int[] newColumnsWidth = new int[row.getLastCellNum()+lastColumnsMergeSize+1];
                System.arraycopy(columnsWidth, 0, newColumnsWidth, 0, columnsWidth.length);
                columnsWidth = newColumnsWidth;
              }

              for(int j=0; j<mergedColumnsNum; j++) {
                //SolmrLogger.debug(this, "COLUMN "+(i+j)+" - WIDTH "+columnsWidth[i+j]+" - STRING WIDTH "+stringWidth);
                if(columnsWidth[i+j]<stringWidth)
                  columnsWidth[i+j] = stringWidth;
              }
            }
          }
        }

        for(int i=0; i<columnsWidth.length; i++) {
          sheet.setColumnWidth((short)i, (short)(columnsWidth[i]*(2.6-(0.1*(columnsWidth[i]/1000)))));
        }
      }

    }

    /**
     * Imposta il bordo cella per uno stile (tutti e quattro i bordi con il medesimo formato)
     * @param style
     * @param border
     */
    protected void setCellBorder(HSSFCellStyle style,short border)
    {
      style.setBorderBottom(border);
      style.setBorderLeft(border);
      style.setBorderRight(border);
      style.setBorderTop(border);
    }

    protected void setCellBackgroundColor(HSSFCellStyle style,short color)
    {
      style.setFillForegroundColor(color);
      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }
}

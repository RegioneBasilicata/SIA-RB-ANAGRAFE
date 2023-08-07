package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.ws.cciaa.UvResponseCCIAA;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
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
public class ExcelUnitaVitateCUAAServlet extends ExcelServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 8137792970866346309L;

  /**
   * Inizializza il servlet
   *
   * @throws ServletException
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    process(request, response);
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    process(request, response);
  }

  public void destroy() {
    super.destroy();
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      SolmrLogger.debug(this, " - ExcelUnitaVitateCUAAServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) request.getSession().getAttribute("uvResponseCCIAA");

      //Creazione cartella excel
      HSSFWorkbook workBook=new HSSFWorkbook();



      //Creazione foglio excel
      HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA());

      //Permette di stampare orizzontale
      HSSFPrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);


      //Imposto la larghezza delle colonne
      sheet.setColumnWidth((short)0,(short)6000); //albo
      sheet.setColumnWidth((short)1,(short)1400); //pv
      sheet.setColumnWidth((short)2,(short)1400); //pv
      sheet.setColumnWidth((short)4,(short)5500); //comune
      sheet.setColumnWidth((short)5,(short)1200); //Sz.
      sheet.setColumnWidth((short)6,(short)1400); //Fgl.
      sheet.setColumnWidth((short)7,(short)1500); //Part.
      sheet.setColumnWidth((short)8,(short)1500); //Sub.
      sheet.setColumnWidth((short)10,(short)3500);

      HSSFRow row=null;

      //Creazione font di tipo bold
      HSSFFont fontBold=workBook.createFont();
      fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      fontBold.setFontHeight((short)160);

      HSSFFont fontRed=workBook.createFont();
      fontRed.setColor(HSSFFont.COLOR_RED);
      fontRed.setFontHeight((short)160);

      HSSFFont fontBlack=workBook.createFont();
      fontBlack.setColor(HSSFFont.COLOR_NORMAL);
      fontBlack.setFontHeight((short)160);


      //Creazione stile usato dall'header
      HSSFCellStyle styleHeader=workBook.createCellStyle();
      styleHeader.setFont(fontBold);

      //Creazione stile usato dall'header della tabella
      HSSFCellStyle styleHeaderTable=workBook.createCellStyle();
      styleHeaderTable.setFont(fontBold);
      styleHeaderTable.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleHeaderTable.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleHeaderTable.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleHeaderTable.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleHeaderTable.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleHeaderTable.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleHeaderTable.setWrapText(true);


      //Creazione stili usato dall'header (contenuti)
      HSSFCellStyle styleDatiTabellaLeft=workBook.createCellStyle();
      styleDatiTabellaLeft.setFont(fontBlack);
      styleDatiTabellaLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      styleDatiTabellaLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaLeft.setWrapText(true);

      HSSFCellStyle styleRedDatiTabellaLeft=workBook.createCellStyle();
      styleRedDatiTabellaLeft.setFont(fontRed);
      styleRedDatiTabellaLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      styleRedDatiTabellaLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleRedDatiTabellaLeft.setWrapText(true);


      HSSFCellStyle styleDatiTabellaCenter=workBook.createCellStyle();
      styleDatiTabellaCenter.setFont(fontBlack);
      styleDatiTabellaCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleDatiTabellaCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaCenter.setWrapText(true);

      HSSFCellStyle styleRedDatiTabellaCenter=workBook.createCellStyle();
      styleRedDatiTabellaCenter.setFont(fontRed);
      styleRedDatiTabellaCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleRedDatiTabellaCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleRedDatiTabellaCenter.setWrapText(true);


      HSSFCellStyle styleDatiTabellaNumber=workBook.createCellStyle();
      styleDatiTabellaNumber.setFont(fontBlack);
      styleDatiTabellaNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaNumber.setWrapText(true);

      HSSFCellStyle styleRedDatiTabellaNumber=workBook.createCellStyle();
      styleRedDatiTabellaNumber.setFont(fontRed);
      styleRedDatiTabellaNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleRedDatiTabellaNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleRedDatiTabellaNumber.setWrapText(true);


      //Formato per visualizzare correttamente le date
      HSSFDataFormat numberFormat = workBook.createDataFormat();
      short formato = numberFormat.getFormat("#,##0.0000");

      HSSFCellStyle styleDatiTabellaDouble=workBook.createCellStyle();
      styleDatiTabellaDouble.setFont(fontBlack);
      styleDatiTabellaDouble.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaDouble.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDouble.setDataFormat(formato);

      HSSFCellStyle styleRedDatiTabellaDouble=workBook.createCellStyle();
      styleRedDatiTabellaDouble.setFont(fontRed);
      styleRedDatiTabellaDouble.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDouble.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDouble.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDouble.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDouble.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleRedDatiTabellaDouble.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleRedDatiTabellaDouble.setDataFormat(formato);



      Region region =new Region();

      //Unisco le celle dove inserisco il valore del'ALBO VIGNETI
      region.setRowFrom(0);
      region.setRowTo(0);
      region.setColumnFrom((short)0);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);


      //Unisco le celle dove inserisco il valore del CUAA
      region.setRowFrom(2);
      region.setRowTo(2);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);

      //Unisco le celle dove inserisco il valore della DENOMINAZIONE
      region.setRowFrom(3);
      region.setRowTo(3);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);


      /*region.setRowFrom(5);
      region.setRowTo(6);
      region.setColumnFrom( (short) 0);
      region.setColumnTo( (short) 0);
      sheet.addMergedRegion(region);*/
      
      region.setRowFrom(5);
      region.setRowTo(5);
      region.setColumnFrom( (short) 0);
      region.setColumnTo( (short) 1);
      sheet.addMergedRegion(region);

      region.setRowFrom(5);
      region.setRowTo(5);
      region.setColumnFrom( (short) 2);
      region.setColumnTo( (short) 3);
      sheet.addMergedRegion(region);

      //Unisco le celle della prima riga della tabella con quelle della seconda riga
      for (int i=4;i<14;i++)
      {
        region.setRowFrom(5);
        region.setRowTo(6);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }



      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"ALBO VIGNETI SITUAZIONE AGGIORNATA AL "+DateUtils.getCurrentDateString(),(short) 0);

      row=sheet.createRow(nRiga++); //salto riga

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Cuaa:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getCUAA(),(short) 1);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Denominazione:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getDenominazione(),(short) 1);



      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Albo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Matricola",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Comune",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sz.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Fgl.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Part.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sub.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup. iscritta",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Vitigno",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Anno impianto",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"N. ceppi",(short) colonna++);
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"PV",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"PV",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Numero",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);

      //Devo far solo vedere le righe che rispettano il filtro
      boolean filtroAlbo=Validator.isNotEmpty(uvResponseCCIAA.getFiltroAlbo());
      boolean filtroVitigno=Validator.isNotEmpty(uvResponseCCIAA.getFiltroVitigno());


      for (int i=0;i<uvResponseCCIAA.getDatiUv().length;i++)
      {
        if (filtroAlbo)
          if (!uvResponseCCIAA.getFiltroAlbo().equals(uvResponseCCIAA.getDatiUv()[i].getDescAlbo())) continue;
        if (filtroVitigno)
          if (!uvResponseCCIAA.getFiltroVitigno().equals(uvResponseCCIAA.getDatiUv()[i].getVarieta())) continue;


        row=sheet.createRow(nRiga++);
        colonna=0;

        if (uvResponseCCIAA.getDatiUv()[i].isPresenteFascicolo())
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getDescAlbo(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,uvResponseCCIAA.getDatiUv()[i].getSiglaCciaa(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,uvResponseCCIAA.getDatiUv()[i].getSiglaProvincia(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getNrMatricola(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getDescComune()+" ("+uvResponseCCIAA.getDatiUv()[i].getSiglaProvincia()+")",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getSezione(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getFoglio(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getParticella(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++); //colonna del subalterno
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,uvResponseCCIAA.getDatiUv()[i].getSuperficieH(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getVarieta(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getAnnoVegetativo(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getNumCeppi(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getDescAlbo(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaCenter,uvResponseCCIAA.getDatiUv()[i].getSiglaCciaa(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaCenter,uvResponseCCIAA.getDatiUv()[i].getSiglaProvincia(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getNrMatricola(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getDescComune()+" ("+uvResponseCCIAA.getDatiUv()[i].getSiglaProvincia()+")",(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getSezione(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getFoglio(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getParticella(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,"",(short) colonna++); //colonna del subalterno
          ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,uvResponseCCIAA.getDatiUv()[i].getSuperficieH(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,uvResponseCCIAA.getDatiUv()[i].getVarieta(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getAnnoVegetativo(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaNumber,uvResponseCCIAA.getDatiUv()[i].getNumCeppi(),(short) colonna++);
        }
      }


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_AlboVigneti";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelUnitaVitateCUAAServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelUnitaVitateCUAAServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

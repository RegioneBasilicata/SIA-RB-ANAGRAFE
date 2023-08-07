package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.servizi.vitiserv.DirittoGaaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

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
 * Classe necessario alla generazione dei file excel dei diritti vitati
 * <p>Title: Smrgaa</p>
 * <p>Description: Classe di utilità per la generazione di file excel</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */
public class ExcelDirittiVitatiServlet extends ExcelServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 4847812240987195460L;

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
      SolmrLogger.debug(this, " - ExcelDirittiVitatiServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      
      DirittoGaaVO[] diritti = (DirittoGaaVO[])request.getAttribute("elencoDiritti");
      
      //UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) request.getSession().getAttribute("uvResponseCCIAA");

      //Creazione cartella excel
      HSSFWorkbook workBook=new HSSFWorkbook();



      //Creazione foglio excel
      HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA());

      //Permette di stampare orizzontale
      HSSFPrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);


      //Imposto la larghezza delle colonne
      sheet.setColumnWidth((short)0,(short)6000); //descrizione
      sheet.setColumnWidth((short)1,(short)1000); //pv
      sheet.setColumnWidth((short)2,(short)4000); //Amm. competenza del reimpianto
      sheet.setColumnWidth((short)3,(short)6000); //Varietà
      sheet.setColumnWidth((short)4,(short)6000); //vino
      sheet.setColumnWidth((short)5,(short)3500); //Nr. diritto
      sheet.setColumnWidth((short)6,(short)3500); //Stato 
      sheet.setColumnWidth((short)7,(short)3800); //Aut estirpo n.
      sheet.setColumnWidth((short)8,(short)2200); //Data estirpo
      sheet.setColumnWidth((short)9,(short)2200); //Data scadenza
      sheet.setColumnWidth((short)10,(short)2200); //Iniziale
      sheet.setColumnWidth((short)11,(short)2200); //Utilizzata
      sheet.setColumnWidth((short)12,(short)2200); //Trasferita
      sheet.setColumnWidth((short)13,(short)2200); //Residua
      
      
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
      
      
      //Formato per visualizzare correttamente le date
      HSSFDataFormat dateFormat = workBook.createDataFormat();
      
      HSSFCellStyle styleDatiTabellaDate=workBook.createCellStyle();
      styleDatiTabellaDate.setFont(fontBlack);
      styleDatiTabellaDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDate.setDataFormat(dateFormat.getFormat(ExcelServlet.FORMAT_DATE_1));
      styleDatiTabellaDate.setWrapText(true);

      HSSFCellStyle styleRedDatiTabellaDate=workBook.createCellStyle();
      styleRedDatiTabellaDate.setFont(fontRed);
      styleRedDatiTabellaDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDate.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleRedDatiTabellaDate.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleRedDatiTabellaDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleRedDatiTabellaDate.setDataFormat(dateFormat.getFormat(ExcelServlet.FORMAT_DATE_1));
      styleRedDatiTabellaDate.setWrapText(true);
      
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
      region.setColumnTo((short)12);
      sheet.addMergedRegion(region);


      //Unisco le celle dove inserisco il valore del CUAA
      region.setRowFrom(2);
      region.setRowTo(2);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)12);
      sheet.addMergedRegion(region);

      //Unisco le celle dove inserisco il valore della DENOMINAZIONE
      region.setRowFrom(3);
      region.setRowTo(3);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)12);
      sheet.addMergedRegion(region);
      
      //Unisco le celle dove inserisco il valore dell'INDIRIZZO
      region.setRowFrom(4);
      region.setRowTo(4);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)12);
      sheet.addMergedRegion(region);

      
      region.setRowFrom(6);
      region.setRowTo(6);
      region.setColumnFrom( (short) 0);
      region.setColumnTo( (short) 1);
      sheet.addMergedRegion(region);
      
      for (short i=0;i<3;i++)
      {
        region.setRowFrom(6);
        region.setRowTo(6);
        region.setColumnFrom( (short) (10+i));
        region.setColumnTo( (short) (11+i));
        sheet.addMergedRegion(region);
      }

      //Unisco le celle della prima riga della tabella con quelle della seconda riga
      for (int i=2;i<10;i++)
      {
        region.setRowFrom(6);
        region.setRowTo(7);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }



      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"STAMPA DIRITTI IMPIANTO VITATI DEL "+DateUtils.getCurrentDateString(),(short) 0);

      row=sheet.createRow(nRiga++); //salto riga

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Cuaa:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getCUAA(),(short) 1);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Denominazione:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getDenominazione(),(short) 1);
      
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Indirizzo:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,StringUtils.checkNull(anagAziendaVO.getSedelegIndirizzo())+ " - "
          + StringUtils.checkNull(anagAziendaVO.getSedelegCAP()+" ")
          + StringUtils.checkNull(anagAziendaVO.getDescComune()+" (")
          + StringUtils.checkNull(anagAziendaVO.getSedelegProv())+")",(short) 1); 



      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Origine",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Amm. competenza del reimpianto",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Varietà",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Vino",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Nr. diritto",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Stato",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Aut estirpo n.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data estirpo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data scadenza",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Superfici (ha)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Descrizione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Pv.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Iniziale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Utilizzata",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Trasferita",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Residua",(short) colonna++);


      for (int i=0;i<diritti.length;i++)
      {
        if (diritti[i].getDiritto()!=null)
        {
          row=sheet.createRow(nRiga++);
          colonna=0;
          
          BigDecimal supResidua=new BigDecimal(0);
          supResidua=supResidua.add(diritti[i].getDiritto().getSuperficieIniziale());
          supResidua=supResidua.subtract(diritti[i].getDiritto().getSuperficieUtilizzata());
          supResidua=supResidua.subtract(diritti[i].getDiritto().getSuperficieTrasferita());
  
          if (!diritti[i].isScaduto())
          {
            
            
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getDiritto().getDescOrigine(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaCenter,diritti[i].getSiglaProvinciaOrigine(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getDescAmmCompetenzaReimpianto(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getVarieta(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getVino(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getDiritto().getNumeroDritto(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getDiritto().getDescrStatoDiritto(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getDiritto().getAutorizzazione_estirpazione(),(short) colonna++);
            
            if (diritti[i].getDiritto().getDataEstirpazione()!=null)
              ExcelServlet.buildCell(row, styleDatiTabellaDate,diritti[i].getDiritto().getDataEstirpazione(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            
            if (diritti[i].getDiritto().getDataScadenza()!=null)
              ExcelServlet.buildCell(row, styleDatiTabellaDate,diritti[i].getDiritto().getDataScadenza(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            
            if (diritti[i].getDiritto().getSuperficieIniziale()!=null)
              ExcelServlet.buildCell(row, styleDatiTabellaDouble,diritti[i].getDiritto().getSuperficieIniziale().doubleValue(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleDatiTabellaDouble,0,(short) colonna++);
            
            if (diritti[i].getDiritto().getSuperficieUtilizzata()!=null)
              ExcelServlet.buildCell(row, styleDatiTabellaDouble,diritti[i].getDiritto().getSuperficieUtilizzata().doubleValue(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleDatiTabellaDouble,0,(short) colonna++);
            
            if (diritti[i].getDiritto().getSuperficieTrasferita()!=null)
              ExcelServlet.buildCell(row, styleDatiTabellaDouble,diritti[i].getDiritto().getSuperficieTrasferita().doubleValue(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleDatiTabellaDouble,0,(short) colonna++);
            
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,supResidua.doubleValue(),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,diritti[i].getDiritto().getDescOrigine(),(short) colonna++);
            ExcelServlet.buildCell(row, styleRedDatiTabellaCenter,diritti[i].getSiglaProvinciaOrigine(),(short) colonna++);
            ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,diritti[i].getDescAmmCompetenzaReimpianto(),(short) colonna++);
            ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,diritti[i].getVarieta(),(short) colonna++);
            ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,diritti[i].getVino(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getDiritto().getNumeroDritto(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,diritti[i].getDiritto().getDescrStatoDiritto(),(short) colonna++);
            ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,diritti[i].getDiritto().getAutorizzazione_estirpazione(),(short) colonna++);
            
            if (diritti[i].getDiritto().getDataEstirpazione()!=null)
              ExcelServlet.buildCell(row, styleRedDatiTabellaDate,diritti[i].getDiritto().getDataEstirpazione(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,"",(short) colonna++);
            
            if (diritti[i].getDiritto().getDataScadenza()!=null)
              ExcelServlet.buildCell(row, styleRedDatiTabellaDate,diritti[i].getDiritto().getDataScadenza(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,"",(short) colonna++);

            if (diritti[i].getDiritto().getSuperficieIniziale()!=null)
              ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,diritti[i].getDiritto().getSuperficieIniziale().doubleValue(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,0,(short) colonna++);
            
            if (diritti[i].getDiritto().getSuperficieUtilizzata()!=null)
              ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,diritti[i].getDiritto().getSuperficieUtilizzata().doubleValue(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,0,(short) colonna++);
            
            if (diritti[i].getDiritto().getSuperficieTrasferita()!=null)
              ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,diritti[i].getDiritto().getSuperficieTrasferita().doubleValue(),(short) colonna++);
            else
              ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,0,(short) colonna++);
            
            ExcelServlet.buildCell(row, styleRedDatiTabellaDouble,supResidua.doubleValue(),(short) colonna++);
          }
        }
      }


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_DirittiViticoli";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelDirittiVitatiServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelDirittiVitatiServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

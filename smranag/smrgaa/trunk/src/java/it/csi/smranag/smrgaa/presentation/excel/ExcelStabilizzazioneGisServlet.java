package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Vector;

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
import org.apache.poi.hssf.util.HSSFColor;
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
public class ExcelStabilizzazioneGisServlet extends ExcelServlet
{
 

  

  /**
   * 
   */
  private static final long serialVersionUID = -2021274026418106125L;

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
      SolmrLogger.debug(this, " - ExcelStabilizzazioneGisServlet - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
      FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)request
        .getSession().getAttribute("filtriParticellareRicercaVO");
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      Vector<AnagParticellaExcelVO> elencoParticelleExcel = gaaFacadeClient
        .searchParticelleStabGisExcelByParameters(filtriParticellareRicercaVO, anagAziendaVO.getIdAzienda());
      
      
      String descrizionePiano = (String)request.getAttribute("descrizionePiano");
      
      
      final int MAXCOLUMN = 17;
      final int BEGINCOLUMN = 0;
      
      
      
      //Creazione cartella excel
      HSSFWorkbook workBook=new HSSFWorkbook();



      //Creazione foglio excel
      HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA());
      
      //Imposto i margini di stampa
      sheet.setMargin(HSSFSheet.LeftMargin,0.55);
      sheet.setMargin(HSSFSheet.RightMargin,0.55);

      //Permette di stampare orizzontale
      HSSFPrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);


      //Imposto la larghezza delle colonne
      int indiceColonne = 0;
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Unita' Produttiva
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Identificativo catastale/Comune/Istat
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Identificativo catastale/Comune/Descrizione
      sheet.setColumnWidth((short)indiceColonne++,(short)1200); //Identificativo catastale/Sz.
      sheet.setColumnWidth((short)indiceColonne++,(short)1200); //Identificativo catastale/Fgl.
      sheet.setColumnWidth((short)indiceColonne++,(short)1200); //Identificativo catastale/Part.
      sheet.setColumnWidth((short)indiceColonne++,(short)1200); //Identificativo catastale/Sub.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Sup. Cat.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Sup. Graf.
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Conduzione/T.C.
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Conduzione/%
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Varieta'
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Superficie
      sheet.setColumnWidth((short)indiceColonne++,(short)2900); //Nuova superifice GIS
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Superficie assegnata con allineamento
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); //Note    
      
      HSSFRow row=null;

      //Creazione font di tipo bold
      HSSFFont fontBold=workBook.createFont();
      fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      fontBold.setFontHeight((short)160);

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
      
      
      HSSFCellStyle styleHeaderTableYellow=workBook.createCellStyle();
      styleHeaderTableYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
      styleHeaderTableYellow.setFillForegroundColor(HSSFColor.YELLOW.index);  
      styleHeaderTableYellow.setFont(fontBold);
      styleHeaderTableYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleHeaderTableYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleHeaderTableYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleHeaderTableYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleHeaderTableYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleHeaderTableYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleHeaderTableYellow.setWrapText(true);


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
      
      HSSFCellStyle styleDatiTabellaLeftGreen=workBook.createCellStyle();
      styleDatiTabellaLeftGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
      styleDatiTabellaLeftGreen.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);  
      styleDatiTabellaLeftGreen.setFont(fontBlack);
      styleDatiTabellaLeftGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeftGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeftGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeftGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeftGreen.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      styleDatiTabellaLeftGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaLeftGreen.setWrapText(true);


      HSSFCellStyle styleDatiTabellaCenter=workBook.createCellStyle();
      styleDatiTabellaCenter.setFont(fontBlack);
      styleDatiTabellaCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleDatiTabellaCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaCenter.setWrapText(true);
      
      HSSFCellStyle styleDatiTabellaCenterGreen=workBook.createCellStyle();
      styleDatiTabellaCenterGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
      styleDatiTabellaCenterGreen.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);  
      styleDatiTabellaCenterGreen.setFont(fontBlack);
      styleDatiTabellaCenterGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenterGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenterGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenterGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenterGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleDatiTabellaCenterGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaCenterGreen.setWrapText(true);


      HSSFCellStyle styleDatiTabellaNumber=workBook.createCellStyle();
      styleDatiTabellaNumber.setFont(fontBlack);
      styleDatiTabellaNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaNumber.setWrapText(true);


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
      
      
      
      HSSFCellStyle styleDatiTabellaDoubleYellow=workBook.createCellStyle();
      styleDatiTabellaDoubleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
      styleDatiTabellaDoubleYellow.setFillForegroundColor(HSSFColor.YELLOW.index);  
      styleDatiTabellaDoubleYellow.setFont(fontBlack);
      styleDatiTabellaDoubleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleYellow.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaDoubleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDoubleYellow.setDataFormat(formato);
      
      
      
      
      HSSFCellStyle styleDatiTabellaDoubleGreen=workBook.createCellStyle();
      styleDatiTabellaDoubleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
      styleDatiTabellaDoubleGreen.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);  
      styleDatiTabellaDoubleGreen.setFont(fontBlack);
      styleDatiTabellaDoubleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDoubleGreen.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaDoubleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDoubleGreen.setDataFormat(formato);
      
      
      
      
      
      //fontBlack.setColor(HSSFFont.COLOR_NORMAL);
      
      
      
      //Formato per visualizzare correttamente le date
      HSSFDataFormat dateFormat = workBook.createDataFormat();
      short formatoDate = dateFormat.getFormat(ExcelServlet.FORMAT_DATE_1);
      HSSFCellStyle styleDatiTabellaDate=workBook.createCellStyle();
      styleDatiTabellaDate.setFont(fontBlack);
      styleDatiTabellaDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleDatiTabellaDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDate.setDataFormat(formatoDate);
      
      //Formato per visualizzare correttamente le date con ore minuti secondi
      HSSFDataFormat dateFormatTime = workBook.createDataFormat();
      short formatoTime = dateFormatTime.getFormat(ExcelServlet.FORMAT_DATE_2);
      HSSFCellStyle styleDatiTabellaDateTime=workBook.createCellStyle();
      styleDatiTabellaDateTime.setFont(fontBlack);
      styleDatiTabellaDateTime.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      styleDatiTabellaDateTime.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDateTime.setDataFormat(formatoTime);
      
      
      
      



      Region region =new Region();
      int rigaCorrente = 0;

      //Unisco le celle dove inserisco il valore del piano colturale
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)BEGINCOLUMN);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      
      ++rigaCorrente;


      //Unisco le celle dove inserisco il valore del CUAA
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);

      //Unisco le celle dove inserisco il valore della DENOMINAZIONE
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      
      ++rigaCorrente;
      ++rigaCorrente;
      
      
      
      //Unisco le righe dell'ute
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+3));
      region.setColumnFrom( (short) 0);
      region.setColumnTo( (short) 0);
      sheet.addMergedRegion(region);
      
      
      //Unisco le righe/colonne dell'identificativo catastale
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)6);
      sheet.addMergedRegion(region);
      
      
      //Unisco le righe/colonne del comune
      region.setRowFrom(rigaCorrente+2);
      region.setRowTo(rigaCorrente+2);
      region.setColumnFrom( (short) 1);
      region.setColumnTo( (short) 2);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne tra comune e sup 
      for(int i=3;i<7;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom((short)i);
        region.setColumnTo((short)i);
        sheet.addMergedRegion(region);        
      }
      
      for(int i=7;i<9;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);    
      }
      
      
      //Unisco le righe/colonne della Conduzione
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)9);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne della T.C./%
      for (int i=9;i<11;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }      
      
      //Unisco le righe/colonne tra cond e uso prim
      for(int i=11;i<MAXCOLUMN;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }

      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      
      ExcelServlet.buildCell(row, styleHeader,"STAMPA PROSPETTO SITUAZIONE AZIENDALE POST "+
        "ALLINEAMENTO ALLA STABILIZZAZIONE GIS AGGIORNATO AL "+descrizionePiano,(short) 0);

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
      ExcelServlet.buildCell(row, styleHeaderTable,"Unita'\nproduttiva ",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Identificativo catastale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nCat.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nGraf.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Conduzione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uso del suolo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Varietà",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTableYellow,"Superficie",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTableYellow,"Nuova\nSuperficie Gis",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTableYellow,"Superficie\nassegnata\ncon allineamento",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Note",(short) colonna++);
      
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Comune",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sz.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Fgl.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Part.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sub.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"T.C.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"%",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Istat",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Descrizione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      

      Long idParticellaTmp = new Long(0);
      boolean colore = true;
      for (int i=0;i<elencoParticelleExcel.size();i++)
      {

        row=sheet.createRow(nRiga++);
        colonna=0;
        
        if(idParticellaTmp.compareTo(elencoParticelleExcel.get(i).getIdParticella()) !=0)
        {
          if(colore)
            colore = false;
          else
            colore = true;
            
        }
        
        HSSFCellStyle styleDatiTabellaLeftTmp = styleDatiTabellaLeft;
        HSSFCellStyle styleDatiTabellaCenterTmp = styleDatiTabellaCenter;
        HSSFCellStyle styleDatiTabellaDoubleTmp = styleDatiTabellaDouble;
        if(colore)
        {
          styleDatiTabellaLeftTmp = styleDatiTabellaLeftGreen; 
          styleDatiTabellaCenterTmp = styleDatiTabellaCenterGreen;
          styleDatiTabellaDoubleTmp = styleDatiTabellaDoubleGreen; 
        }
        
        
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeftTmp,elencoParticelleExcel.get(i).getLabelUte(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeftTmp,elencoParticelleExcel.get(i).getIstatComuneParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp,elencoParticelleExcel.get(i).getDescrizioneComuneParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp,elencoParticelleExcel.get(i).getSezione(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp,elencoParticelleExcel.get(i).getFoglio(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp,elencoParticelleExcel.get(i).getParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp,elencoParticelleExcel.get(i).getSubalterno(),(short) colonna++);
        if(elencoParticelleExcel.get(i).getSuperficieCatastale() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleTmp,Double.parseDouble(elencoParticelleExcel.get(i).getSuperficieCatastale().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleTmp,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSupUsoGrafico() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleTmp,Double.parseDouble(elencoParticelleExcel.get(i).getSupUsoGrafico().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleTmp,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp,elencoParticelleExcel.get(i).getIdTitoloPossesso(),(short) colonna++);
        BigDecimal percentualePossessoTmp = elencoParticelleExcel.get(i).getPercentualePossesso();
        if(percentualePossessoTmp != null)
        {
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }
          String percentualePossesso = Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp);
          ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp, percentualePossesso,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaCenterTmp, "",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaLeftTmp,elencoParticelleExcel.get(i).getUsoPrimario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeftTmp,elencoParticelleExcel.get(i).getVarieta(),(short) colonna++);
        if(elencoParticelleExcel.get(i).getSuperficieUtilizzata() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleYellow,Double.parseDouble(elencoParticelleExcel.get(i).getSuperficieUtilizzata().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleYellow,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSupEleggibileRiproporzionata() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleYellow,Double.parseDouble(elencoParticelleExcel.get(i).getSupEleggibileRiproporzionata().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleYellow,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSupAssegnataAllineamento() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleYellow,Double.parseDouble(elencoParticelleExcel.get(i).getSupAssegnataAllineamento().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDoubleYellow,"",(short) colonna++);
        }
          
        ExcelServlet.buildCell(row, styleDatiTabellaLeftTmp,elencoParticelleExcel.get(i).getNote(),(short) colonna++);
        
        
        idParticellaTmp = elencoParticelleExcel.get(i).getIdParticella();
        
      }
        

      OutputStream out = response.getOutputStream();

      String fileName="Stabilizzazione_GIS_"+anagAziendaVO.getCUAA();

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelStabilizzazioneGisServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelStabilizzazioneGisServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

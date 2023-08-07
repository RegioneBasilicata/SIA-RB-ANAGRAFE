package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
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
public class ExcelUnitaVitateElencoSempliceServlet extends ExcelServlet
{

  
  

  /**
   * 
   */
  private static final long serialVersionUID = 8981907392673811925L;
  
  
  

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
      SolmrLogger.debug(this, " - ExcelUnitaVitateElencoSempliceServlet - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)request.getSession().getAttribute("filtriUnitaArboreaRicercaVO");
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      Vector<StoricoParticellaArboreaExcelVO> partExcelVO = gaaFacadeClient
        .searchStoricoUnitaArboreaExcelSempliceByParameters(
            anagAziendaVO.getIdAzienda(), filtriUnitaArboreaRicercaVO);
      
      
      String descrizionePiano = (String)request.getAttribute("descrizionePiano");
      
      
      final int MAXCOLUMN = 34;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); // comune
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); // sezione
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); // foglio
      sheet.setColumnWidth((short)indiceColonne++,(short)1100); // particella
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); // subalterno
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); // sup. cat.
      sheet.setColumnWidth((short)indiceColonne++,(short)1400); // % Cond.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); // sup. graf.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); // sup. eleg.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); // % uso eleg
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); // sup vit
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // progressivo
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); // data impianto
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); // data prima produzione
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); // dest prod
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); // vitigno
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); // idoneità tipologia
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // idoneità anno
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); // Matricola
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); // Menzione geografica
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); // Vigna
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); // Vigna elenco regionale
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // sesto su file
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // sesto tra file
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // num. ceppi
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // % Fallanza
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // Sup. improd.
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); // forma allevamento
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); // % Vitigno
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); // altro vitigno
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); // fine validità
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); // Caus. Mod.
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //istanza riesame/richiesta
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //istanza riesame/evasa
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Notifica

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


      HSSFCellStyle styleDatiTabellaCenter=workBook.createCellStyle();
      styleDatiTabellaCenter.setFont(fontBlack);
      styleDatiTabellaCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleDatiTabellaCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaCenter.setWrapText(true);


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
      
      
      
      //Formato per visualizzare correttamente le date
      HSSFDataFormat dateFormat = workBook.createDataFormat();
      short formatoDate = dateFormat.getFormat(ExcelServlet.FORMAT_DATE_1);
      HSSFCellStyle styleDatiTabellaDate=workBook.createCellStyle();
      styleDatiTabellaDate.setFont(fontBlack);
      styleDatiTabellaDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setAlignment(HSSFCellStyle.ALIGN_LEFT);
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

      //Unisco le celle dove inserisco il valore del'ALBO VIGNETI
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
      
      
      
      //Unisco le celle prima dell'idoneità
      for (int i=0;i<16;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe dell'idoneità
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)16);
      region.setColumnTo((short)17);
      sheet.addMergedRegion(region);
      
      //Unisco le colonne dell'idoneità
      for (int i=16;i<18;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+2);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      

      //Unisco le celle della prima riga della tabella con quelle della seconda riga
      for (int i=18;i<32;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne dell'istanza riesame
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)32);
      region.setColumnTo((short)33);
      sheet.addMergedRegion(region);
      
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+2));
      region.setColumnFrom( (short) 34);
      region.setColumnTo( (short) 34);
      sheet.addMergedRegion(region);



      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      
      ExcelServlet.buildCell(row, styleHeader,"STAMPA ELENCO UNITA' ARBOREE AGGIORNATO AL "+descrizionePiano,(short) 0);

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
      ExcelServlet.buildCell(row, styleHeaderTable,"Comune",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sz.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Fgl.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Part.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sub.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nCat.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"%\nCond.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nGraf.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nEleg.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"% uso\nEleg.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nVit.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Pgr.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data\nimp.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data prima\nproduz.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Dest. prod.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Vitigno",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Idoneita'",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Matricola",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Menzione\ngeografica",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Vigna",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Vigna elenco regionale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"SsF\n(cm)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"StF\n(cm)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"N.\nceppi",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"%\nFal.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nimp.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"F.all",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"% V.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"A.V.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Fine Val.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Caus.\nMod.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Istanza riesame",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Notifica",(short) colonna++);
      
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
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipologia",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Anno",(short) colonna++);
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Richiesta",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Evasa",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);


      for (int i=0;i<partExcelVO.size();i++)
      {

        row=sheet.createRow(nRiga++);
        colonna=0;

        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDescrizioneComuneParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getSezione(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getFoglio(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getSubalterno(),(short) colonna++);
        if(partExcelVO.get(i).getSuperficieCatastale() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getSuperficieCatastale().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        
        BigDecimal percentualePossessoTmp = partExcelVO.get(i).getPercentualePossesso();
        if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
        {
          percentualePossessoTmp = new BigDecimal(1);
        }
        String percentualePossesso = Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter, percentualePossesso,(short) colonna++);
        if(partExcelVO.get(i).getSuperficieGrafica() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getSuperficieGrafica().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(partExcelVO.get(i).getSupEleggibile() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getSupEleggibile().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        BigDecimal percentualeUsoEleggTmp = partExcelVO.get(i).getPercentualeUsoElegg();
        if(percentualeUsoEleggTmp != null)
        {
          if(percentualeUsoEleggTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualeUsoEleggTmp = new BigDecimal(1);
          }
          String percentualeUsoElegg = Formatter.formatAndRoundBigDecimal0(percentualeUsoEleggTmp);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter, percentualeUsoElegg,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(partExcelVO.get(i).getArea() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getArea().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getProgressivo(),(short) colonna++);
        if(Validator.isNotEmpty(partExcelVO.get(i).getDataImpianto()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,partExcelVO.get(i).getDataImpianto(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        if(Validator.isNotEmpty(partExcelVO.get(i).getDataPrimaProduzione()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,partExcelVO.get(i).getDataPrimaProduzione(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDestinazioneProduttiva(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getVarieta(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDescTipologiaVino(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getAnnoIscrizioneAlbo(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getMatricolaCCIAA(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDescMenzioneGeografica(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getVigna(),(short) colonna++);
        
        if(Validator.isNotEmpty(partExcelVO.get(i).getVignaElencoReg()))
        {          
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, partExcelVO.get(i).getVignaElencoReg(),(short) colonna++);
        }
        else
        {        
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getSestoSuFila(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getSestoTraFile(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getNumeroCeppi(),(short) colonna++);
        BigDecimal percentualeFallanzaTmp = partExcelVO.get(i).getPercentualeFallanza();
        if(percentualeFallanzaTmp != null)
        {
          String percentualeFallanza = Formatter.formatDouble(percentualeFallanzaTmp);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter, percentualeFallanza,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getFlagImproduttiva(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDescFormaAllevamento(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getPercentualeVarieta(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getAltriVitigni(),(short) colonna++);
        if(Validator.isNotEmpty(partExcelVO.get(i).getDataFineValidita()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,partExcelVO.get(i).getDataFineValidita(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDescCausaleModifica(),(short) colonna++);
        
        
        if(Validator.isNotEmpty(partExcelVO.get(i).getDataRichiesta()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,partExcelVO.get(i).getDataRichiesta(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        if(Validator.isNotEmpty(partExcelVO.get(i).getDataEvasione()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,partExcelVO.get(i).getDataEvasione(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        
        
        String notifica = "No";
        if(Validator.isNotEmpty(partExcelVO.get(i).getInNotifica()))
        {
          notifica = "Si";
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter, notifica,(short) colonna++);
        
        
        
      }
        

      OutputStream out = response.getOutputStream();

      String fileName=""+anagAziendaVO.getCUAA()+"_UNAR";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelUnitaVitateElencoSempliceServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelUnitaVitateElencoSempliceServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

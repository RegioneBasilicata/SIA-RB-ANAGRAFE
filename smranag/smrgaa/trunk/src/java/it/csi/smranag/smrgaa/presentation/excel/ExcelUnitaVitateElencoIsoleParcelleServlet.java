package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.etc.SolmrConstants;
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
public class ExcelUnitaVitateElencoIsoleParcelleServlet extends ExcelServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = -3033500430335477895L;

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
      SolmrLogger.debug(this, " - ExcelUnitaVitateElencoIsoleParcelleServlet - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)request.getSession().getAttribute("filtriUnitaArboreaRicercaVO");
      AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
      String parametroUVAG = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_UVAG);
      Vector<StoricoParticellaArboreaExcelVO> partExcelVO = anagFacadeClient.searchStoricoUnitaArboreaExcelByParameters(
          parametroUVAG, anagAziendaVO.getIdAzienda(), filtriUnitaArboreaRicercaVO);
      
      
      String descrizionePiano = (String)request.getAttribute("descrizionePiano");
      
      
      final int MAXCOLUMN = 27;
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
      
      sheet.setColumnWidth((short)indiceColonne++,(short)3400); //Comune
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); //sezione
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); //foglio
      sheet.setColumnWidth((short)indiceColonne++,(short)1100); //particella
      sheet.setColumnWidth((short)indiceColonne++,(short)1100); //subalterno
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); //progressivo
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //sup. cat.
      sheet.setColumnWidth((short)indiceColonne++,(short)1400); //% cond
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //sup. graf.
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // sup. eleg.
      sheet.setColumnWidth((short)indiceColonne++,(short)1400); // % uso elegg.
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // sup vit
      sheet.setColumnWidth((short)indiceColonne++,(short)1600); // Sup. Post. Allinea
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); // Delta
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // Sup. parcella
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); // Id Ilo
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // Tol. Parc. Inf.
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // Tol. Parc. Sup.
      sheet.setColumnWidth((short)indiceColonne++,(short)1800); // Parc. Assoc.
      sheet.setColumnWidth((short)indiceColonne++,(short)3200); // tolleranza      
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); // data impianto
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); // data prima produzione
      sheet.setColumnWidth((short)indiceColonne++,(short)3400); // vitigno      
      sheet.setColumnWidth((short)indiceColonne++,(short)3400); // idoneità tipologia
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); // idoneità anno
      sheet.setColumnWidth((short)indiceColonne++,(short)3400); // Matricola
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); // istanza riesame/richiesta
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); // istanza riesame/lavorata
      

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
      for (int i=0;i<23;i++)
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
      region.setColumnFrom((short)23);
      region.setColumnTo((short)24);
      sheet.addMergedRegion(region);
      
      //Unisco le colonne dell'idoneità
      for (int i=23;i<25;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+2);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      

      //Unisco le celle della prima riga della tabella con quelle della seconda riga
      for (int i=25;i<26;i++)
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
      region.setColumnFrom((short)26);
      region.setColumnTo((short)27);
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Pgr.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nCat.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"%\nCond.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nGraf.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nEleg.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"% uso\nEleg.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nVit.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nPost.\nAllinea",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Delta",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nParcella",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Id Ilo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tol.\nParc.\nInf.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tol.\nParc.\nSup.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Parc.\nAssoc.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tolleranza",(short) colonna++);      
      ExcelServlet.buildCell(row, styleHeaderTable,"Data\nimp.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data prima\nproduz.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Vitigno",(short) colonna++);      
      ExcelServlet.buildCell(row, styleHeaderTable,"Idoneita'",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Matricola",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Istanza riesame",(short) colonna++);
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Richiesta",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Lavorata",(short) colonna++);


      for (int i=0;i<partExcelVO.size();i++)
      {

        row=sheet.createRow(nRiga++);
        colonna=0;
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDescrizioneComuneParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getSezione(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getFoglio(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getSubalterno(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getProgressivo(),(short) colonna++);        
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
        if(partExcelVO.get(i).getSupPostAllinea() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getSupPostAllinea().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if((partExcelVO.get(i).getArea() != null) 
            && (partExcelVO.get(i).getSupPostAllinea() != null))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,
              Double.parseDouble(partExcelVO.get(i).getSupPostAllinea().replace(',', '.')) -
              Double.parseDouble(partExcelVO.get(i).getArea().replace(',', '.')),
              (short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, SolmrConstants.DEFAULT_SUPERFICIE,(short) colonna++);
        }
        if(partExcelVO.get(i).getSupParcella() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getSupParcella().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getIdIlo(),(short) colonna++);
        if(partExcelVO.get(i).getSupParcellaInf() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getSupParcellaInf().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(partExcelVO.get(i).getSupParcellaSup() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(partExcelVO.get(i).getSupParcellaSup().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getAssociataParcella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getTolleranza(),(short) colonna++);        
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
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getVarieta(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getDescTipologiaVino(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,partExcelVO.get(i).getAnnoIscrizioneAlbo(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,partExcelVO.get(i).getMatricolaCCIAA(),(short) colonna++);
        
        
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
        
       
        
        
      }
        

      OutputStream out = response.getOutputStream();

      String fileName=""+anagAziendaVO.getCUAA()+"_Unar_parcelle";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelUnitaVitateElencoIsoleParcelleServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelUnitaVitateElencoIsoleParcelleServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

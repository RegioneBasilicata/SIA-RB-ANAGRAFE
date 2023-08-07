package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.terreni.RiepilogoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.UVCompensazioneVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
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
public class ExcelUnitaVitateElencoCompensazioneServlet extends ExcelServlet
{

  
  

  /**
   * 
   */
  private static final long serialVersionUID = 760261629116725069L;

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
      SolmrLogger.debug(this, " - ExcelUnitaVitateElencoCompensazioneServlet - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");      
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance(); 
      String descrizionePiano = (String)request.getAttribute("descrizionePiano");     
      
      //Creazione file excel
      HSSFWorkbook workBook=new HSSFWorkbook();
      //Racoglie tuti gli stili dei caratteri
      HashMap<String, HSSFCellStyle> hStyle = new HashMap<String, HSSFCellStyle>();
      //creazione stili caratteri      
      createStyle(workBook, hStyle);
      
      
      //creazione primo foglio (compensazione)
      HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA());
      Vector<UVCompensazioneVO> vUVCompensazione = gaaFacadeClient.getUVPerCompensazione(
          anagAziendaVO.getIdAzienda().longValue());
      if(vUVCompensazione != null)
      {
        sheetCompensazione(sheet, hStyle, descrizionePiano, anagAziendaVO, vUVCompensazione);
        
        //creazione secondo foglio (Riepilogo per vitigno e idoneità )
        Vector<RiepilogoCompensazioneVO> vPostAllinea = gaaFacadeClient.getRiepilogoPostAllinea(
            anagAziendaVO.getIdAzienda().longValue());
        if(vPostAllinea != null)
        {
          sheet=workBook.createSheet("VitignoIdoneità");        
          sheetVitignoIdoneita(sheet, hStyle, descrizionePiano, anagAziendaVO,
              vPostAllinea);
        }
        
        //creazione terzo foglio (Riepilogo diritti viticoli ipotetici )
        Vector<RiepilogoCompensazioneVO> vDirittiVitati = gaaFacadeClient.getRiepilogoDirittiVitati(
            anagAziendaVO.getIdAzienda().longValue());
        if(vDirittiVitati !=null)
        {
          sheet=workBook.createSheet("DirittiViticoli");        
          sheetDiritti(sheet, hStyle, descrizionePiano, anagAziendaVO,
              vDirittiVitati);  
        }
        
      }
      
      
        

      OutputStream out = response.getOutputStream();

      String fileName=""+anagAziendaVO.getCUAA()+"_COMPENSAZIONE";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelUnitaVitateElencoCompensazioneServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelUnitaVitateElencoCompensazioneServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }
  
  
  private void createStyle(HSSFWorkbook workBook, HashMap<String, HSSFCellStyle> hStyleTmp)
  {
    //Creazione font di tipo bold
    HSSFFont fontBold=workBook.createFont();
    fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    fontBold.setFontHeight((short)160);

    HSSFFont fontBlack=workBook.createFont();
    fontBlack.setColor(HSSFFont.COLOR_NORMAL);
    fontBlack.setFontHeight((short)160);
    
    HSSFFont fontRed=workBook.createFont();
    fontRed.setColor(HSSFFont.COLOR_RED);
    fontRed.setFontHeight((short)160);


    //Creazione stile usato dall'header
    HSSFCellStyle styleHeader=workBook.createCellStyle();
    styleHeader.setFont(fontBold);
    hStyleTmp.put("styleHeader", styleHeader);

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
    hStyleTmp.put("styleHeaderTable", styleHeaderTable);


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
    hStyleTmp.put("styleDatiTabellaLeft", styleDatiTabellaLeft);


    HSSFCellStyle styleDatiTabellaCenter=workBook.createCellStyle();
    styleDatiTabellaCenter.setFont(fontBlack);
    styleDatiTabellaCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    styleDatiTabellaCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    styleDatiTabellaCenter.setWrapText(true);
    hStyleTmp.put("styleDatiTabellaCenter", styleDatiTabellaCenter);


    HSSFCellStyle styleDatiTabellaNumber=workBook.createCellStyle();
    styleDatiTabellaNumber.setFont(fontBlack);
    styleDatiTabellaNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleDatiTabellaNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    styleDatiTabellaNumber.setWrapText(true);
    hStyleTmp.put("styleDatiTabellaNumber", styleDatiTabellaNumber);


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
    hStyleTmp.put("styleDatiTabellaDouble", styleDatiTabellaDouble);
    
    HSSFCellStyle styleDatiTabellaDoubleRed=workBook.createCellStyle();
    styleDatiTabellaDoubleRed.setFont(fontRed);
    styleDatiTabellaDoubleRed.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaDoubleRed.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaDoubleRed.setBorderRight(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaDoubleRed.setBorderTop(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaDoubleRed.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    styleDatiTabellaDoubleRed.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    styleDatiTabellaDoubleRed.setDataFormat(formato);
    hStyleTmp.put("styleDatiTabellaDoubleRed", styleDatiTabellaDoubleRed);
    
    
    
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
    hStyleTmp.put("styleDatiTabellaDate", styleDatiTabellaDate);
    
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
    hStyleTmp.put("styleDatiTabellaDateTime", styleDatiTabellaDateTime);
    
  }
  
  private void sheetCompensazione(HSSFSheet sheet, HashMap<String, HSSFCellStyle> hStyle, String descrizionePiano,
      AnagAziendaVO anagAziendaVO, Vector<UVCompensazioneVO> vUVCompensazione) throws SolmrException, Exception
  {    
    
    
    //Imposto i margini di stampa
    sheet.setMargin(HSSFSheet.LeftMargin,0.55);
    sheet.setMargin(HSSFSheet.RightMargin,0.55);

    //Permette di stampare orizzontale
    HSSFPrintSetup printSetup = sheet.getPrintSetup();
    printSetup.setLandscape(true);
    
    final int MAXCOLUMN = 18;
    final int BEGINCOLUMN = 0;


    //Imposto la larghezza delle colonne
    int indiceColonne = 0;     
    
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Comune
    sheet.setColumnWidth((short)indiceColonne++,(short)1000); //sezione
    sheet.setColumnWidth((short)indiceColonne++,(short)1000); //foglio
    sheet.setColumnWidth((short)indiceColonne++,(short)1100); //particella
    sheet.setColumnWidth((short)indiceColonne++,(short)1100); //subalterno
    sheet.setColumnWidth((short)indiceColonne++,(short)1100); //subalterno
    sheet.setColumnWidth((short)indiceColonne++,(short)1400); // % uso elegg.
    sheet.setColumnWidth((short)indiceColonne++,(short)1200); //progressivo
    sheet.setColumnWidth((short)indiceColonne++,(short)3500); // data impianto
    sheet.setColumnWidth((short)indiceColonne++,(short)3500); // data prima produzione
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // destinazione produttiva
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // vitigno      
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // idoneità     
    sheet.setColumnWidth((short)indiceColonne++,(short)2000); // sup. vit. ante 11/02/11
    sheet.setColumnWidth((short)indiceColonne++,(short)2000); // sup. Gis. Attuale
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); // sup. vitata in lavorazione
    sheet.setColumnWidth((short)indiceColonne++,(short)2000); // Sup. Post. Allinea
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); // Tolleranza
    sheet.setColumnWidth((short)indiceColonne++,(short)1700); // Delta
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); // Ist. Ries. in corso
    
    
    
    
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
    for (int i=0;i<MAXCOLUMN+1;i++)
    {
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+2));
      region.setColumnFrom( (short) i);
      region.setColumnTo( (short) i);
      sheet.addMergedRegion(region);
    }


    HSSFRow row = null;
    int nRiga = 0;

    //Creo e popolo l'header
    row=sheet.createRow(nRiga++);
    
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "CONSOLIDAMENTO AZIENDALE E GENERAZIONE DIRITTI VITICOLI AGGIORNATO AL "+descrizionePiano,(short) 0);

    row=sheet.createRow(nRiga++); //salto riga

    row=sheet.createRow(nRiga++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "Cuaa:",(short) 0);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), anagAziendaVO.getCUAA(),(short) 1);

    row=sheet.createRow(nRiga++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "Denominazione:",(short) 0);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), anagAziendaVO.getDenominazione(),(short) 1);



    row=sheet.createRow(nRiga++); //salto riga


    //Creo e popolo header della tabella
    row=sheet.createRow(nRiga++);
    int colonna=0;      
    
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Comune",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sz.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Fgl.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Part.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sub.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "% uso\nEleg.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Pgr.\nunar",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Data\nimp.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Data prima\nproduz.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Dest. prod.",(short) colonna++); 
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Vitigno",(short) colonna++); 
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Idoneita'",(short) colonna++);      
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sup.Vit.\nAnte\n11/02/11",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sup.Gis\nAttuale.",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sup.Vit in\nlavorazione",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sup.\nPost.\nAllinea",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Toll.",(short) colonna++);   
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Delta",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Ist.\nRies.\nin corso",(short) colonna++);
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    


    for (int i=0;i<vUVCompensazione.size();i++)
    {

      row=sheet.createRow(nRiga++);
      colonna=0;
      String descCom = vUVCompensazione.get(i).getDescComune()+" ("+vUVCompensazione.get(i).getSiglaProv()+")";
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), descCom, (short) colonna++);
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), vUVCompensazione.get(i).getSezione(),(short) colonna++);
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), vUVCompensazione.get(i).getFoglio(),(short) colonna++);
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), vUVCompensazione.get(i).getParticella(),(short) colonna++);
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), vUVCompensazione.get(i).getSubalterno(),(short) colonna++);
      
      BigDecimal percentualePossessoTmp = vUVCompensazione.get(i).getPercentualePossesso();
      if(percentualePossessoTmp != null)
      {
        if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
        {
          percentualePossessoTmp = new BigDecimal(1);
        }
        String percentualePossesso = Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp);
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), percentualePossesso,(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), "",(short) colonna++);
      }
      
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), vUVCompensazione.get(i).getProgrUnar(),(short) colonna++);
      if(Validator.isNotEmpty(vUVCompensazione.get(i).getDataImpianto()))
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDate"), vUVCompensazione.get(i).getDataImpianto(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDate"), "",(short) colonna++);
      }
      if(Validator.isNotEmpty(vUVCompensazione.get(i).getDataPrimaProduzione()))
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDate"), vUVCompensazione.get(i).getDataPrimaProduzione(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDate"), "",(short) colonna++);
      }
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), vUVCompensazione.get(i).getDestinazioneProduttiva(),(short) colonna++);
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), vUVCompensazione.get(i).getVitigno(),(short) colonna++);
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), vUVCompensazione.get(i).getIdoneita(),(short) colonna++);
      if(vUVCompensazione.get(i).getAreaDichiarata() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), vUVCompensazione.get(i).getAreaDichiarata().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      if(vUVCompensazione.get(i).getAreaGisRiproporzionata() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), vUVCompensazione.get(i).getAreaGisRiproporzionata().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      if(vUVCompensazione.get(i).getArea() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), vUVCompensazione.get(i).getArea().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      if(vUVCompensazione.get(i).getAreaPostAllinea() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), vUVCompensazione.get(i).getAreaPostAllinea().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), vUVCompensazione.get(i).getTolleranza(),(short) colonna++);
      if(vUVCompensazione.get(i).getDelta() !=null)
      {
        if(vUVCompensazione.get(i).getDelta().compareTo(new BigDecimal(0)) >= 0)
        {
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), vUVCompensazione.get(i).getDelta().doubleValue(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDoubleRed"), vUVCompensazione.get(i).getDelta().doubleValue(),(short) colonna++);
        }
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), vUVCompensazione.get(i).getIstanzaRiesame(),(short) colonna++);
      
      
      
      
    }
    
    
    
    
  }
  
  
  
  private void sheetVitignoIdoneita(HSSFSheet sheet, HashMap<String, HSSFCellStyle> hStyle, String descrizionePiano,
      AnagAziendaVO anagAziendaVO, Vector<RiepilogoCompensazioneVO> vPostAllinea) throws SolmrException, Exception
  {    
    
    //Imposto i margini di stampa
    sheet.setMargin(HSSFSheet.LeftMargin,0.55);
    sheet.setMargin(HSSFSheet.RightMargin,0.55);

    //Permette di stampare orizzontale
    HSSFPrintSetup printSetup = sheet.getPrintSetup();
    printSetup.setLandscape(true);
    
    final int MAXCOLUMN = 6;
    final int BEGINCOLUMN = 0;


    //Imposto la larghezza delle colonne
    int indiceColonne = 0;     
    
    sheet.setColumnWidth((short)indiceColonne++,(short)6000); // Destinazione produttiva
    sheet.setColumnWidth((short)indiceColonne++,(short)6000); // Vitigno
    sheet.setColumnWidth((short)indiceColonne++,(short)6000); // Idoneità
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // Sup.Vit. Ante 11/02/11
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // sup. vit. in lavorazione (ha)
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // Sup. Post. Allinea (ha)
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // Delta
    
    
    
    
    Region region =new Region();
    int rigaCorrente = 0;

    //Unisco le celle per nome foglio
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
    for (int i=0;i<MAXCOLUMN+1;i++)
    {
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+2));
      region.setColumnFrom( (short) i);
      region.setColumnTo( (short) i);
      sheet.addMergedRegion(region);
    }


    HSSFRow row = null;
    int nRiga = 0;

    //Creo e popolo l'header
    row=sheet.createRow(nRiga++);
    
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "COMPENSAZIONE AZIENDALE: RIEPILOGO PER VITIGNO E IDONEITA' AGGIORNATO AL "+descrizionePiano,(short) 0);

    row=sheet.createRow(nRiga++); //salto riga

    row=sheet.createRow(nRiga++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "Cuaa:",(short) 0);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), anagAziendaVO.getCUAA(),(short) 1);

    row=sheet.createRow(nRiga++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "Denominazione:",(short) 0);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), anagAziendaVO.getDenominazione(),(short) 1);



    row=sheet.createRow(nRiga++); //salto riga


    //Creo e popolo header della tabella
    row=sheet.createRow(nRiga++);
    int colonna=0;      
    
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Destinazione produttiva",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Vitigno",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Idoneità",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sup.Vit.\nAnte\n11/02/11",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sup vit.\nin lavorazione",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Sup. post.\nAllinea",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Delta",(short) colonna++);
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);


    for (int i=0;i<vPostAllinea.size();i++)
    {
      
      RiepilogoCompensazioneVO riepilogoCompensazioneVO = vPostAllinea.get(i);
      row=sheet.createRow(nRiga++);
      colonna=0;
      String destProd = "["+riepilogoCompensazioneVO.getCodDestProd()+"] "+riepilogoCompensazioneVO.getDescDestProd();
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), destProd, (short) colonna++);
      String vitigno = "["+riepilogoCompensazioneVO.getCodVitigno()+"] "+riepilogoCompensazioneVO.getDescVitigno();
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), vitigno, (short) colonna++);
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), riepilogoCompensazioneVO.getIdoneita(), (short) colonna++);
      if(riepilogoCompensazioneVO.getSupAreaDichiarata() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), riepilogoCompensazioneVO.getSupAreaDichiarata().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      if(riepilogoCompensazioneVO.getSupVitLavorazione() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), riepilogoCompensazioneVO.getSupVitLavorazione().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      if(riepilogoCompensazioneVO.getSupPastAllinea() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), riepilogoCompensazioneVO.getSupPastAllinea().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      if(riepilogoCompensazioneVO.getSumDelta() !=null)
      {
        if(riepilogoCompensazioneVO.getSumDelta().compareTo(new BigDecimal(0)) >= 0)
        {
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), riepilogoCompensazioneVO.getSumDelta().doubleValue(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDoubleRed"), riepilogoCompensazioneVO.getSumDelta().doubleValue(),(short) colonna++);
        }
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      
      
      
    }
    
    
    
    
  }
  
  
  private void sheetDiritti(HSSFSheet sheet, HashMap<String, HSSFCellStyle> hStyle, String descrizionePiano,
      AnagAziendaVO anagAziendaVO, Vector<RiepilogoCompensazioneVO> vDirittiVitati) throws SolmrException, Exception
  {    
    
    //Imposto i margini di stampa
    sheet.setMargin(HSSFSheet.LeftMargin,0.55);
    sheet.setMargin(HSSFSheet.RightMargin,0.55);

    //Permette di stampare orizzontale
    HSSFPrintSetup printSetup = sheet.getPrintSetup();
    printSetup.setLandscape(true);
    
    final int MAXCOLUMN = 3;
    final int BEGINCOLUMN = 0;


    //Imposto la larghezza delle colonne
    int indiceColonne = 0;     
    
    sheet.setColumnWidth((short)indiceColonne++,(short)8000); // Destinazione produttiva
    sheet.setColumnWidth((short)indiceColonne++,(short)8000); // Vitigno
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // Superficie vitata (ha)
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); // Vitigno particolare
    
    
    
    
    Region region =new Region();
    int rigaCorrente = 0;

    //Unisco le celle per nome foglio
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
    for (int i=0;i<MAXCOLUMN+1;i++)
    {
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+2));
      region.setColumnFrom( (short) i);
      region.setColumnTo( (short) i);
      sheet.addMergedRegion(region);
    }


    HSSFRow row = null;
    int nRiga = 0;

    //Creo e popolo l'header
    row=sheet.createRow(nRiga++);
    
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "COMPENSAZIONE AZIENDALE: RIEPILOGO DIRITTI VITICOLI IPOTETICI AGGIORNATO AL "+descrizionePiano,(short) 0);

    row=sheet.createRow(nRiga++); //salto riga

    row=sheet.createRow(nRiga++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "Cuaa:",(short) 0);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), anagAziendaVO.getCUAA(),(short) 1);

    row=sheet.createRow(nRiga++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), "Denominazione:",(short) 0);
    ExcelServlet.buildCell(row, hStyle.get("styleHeader"), anagAziendaVO.getDenominazione(),(short) 1);



    row=sheet.createRow(nRiga++); //salto riga


    //Creo e popolo header della tabella
    row=sheet.createRow(nRiga++);
    int colonna=0;      
    
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Destinazione produttiva",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Vitigno",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Superficie vitata",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "Vitigno particolare",(short) colonna++);
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"), "",(short) colonna++);


    for (int i=0;i<vDirittiVitati.size();i++)
    {
      
      RiepilogoCompensazioneVO riepilogoCompensazioneVO = vDirittiVitati.get(i);
      row=sheet.createRow(nRiga++);
      colonna=0;
      String destProd = "["+riepilogoCompensazioneVO.getCodDestProd()+"] "+riepilogoCompensazioneVO.getDescDestProd();
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), destProd, (short) colonna++);
      String vitigno = "["+riepilogoCompensazioneVO.getCodVitigno()+"] "+riepilogoCompensazioneVO.getDescVitigno();
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), vitigno, (short) colonna++);
      if(riepilogoCompensazioneVO.getSupVitata() !=null)
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), riepilogoCompensazioneVO.getSupVitata().doubleValue(),(short) colonna++);
      }
      else
      {
        ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), "",(short) colonna++);
      }
      String vitignoParticolare = "NO";
      if(riepilogoCompensazioneVO.getVitignoParticolare() == 1)
      {
        vitignoParticolare = "SI";
      } 
      ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), vitignoParticolare, (short) colonna++);
      
      
      
    }
    
    
    
    
  }


}

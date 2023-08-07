package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.DateUtils;
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
public class ExcelRiepilogoDestProdComuneServlet extends ExcelServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = -3598457827979682670L;

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
      SolmrLogger.debug(this, " - ExcelRiepilogoDestProdComuneServlet - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
      
      
      String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
      String descrizionePiano = "";
      Vector<RiepiloghiUnitaArboreaVO> elencoRiepiloghi = null;
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        Long idDichiarazioneConsistenzaLg = new Long(idDichiarazioneConsistenza);
        if(idDichiarazioneConsistenzaLg.longValue() == 0)
        {
          descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con conduzioni storicizzate)";
        }
        else if(idDichiarazioneConsistenzaLg.longValue() == -1)
        {
          descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione";
        }
        else
        {
          ConsistenzaVO consVO = anagFacadeClient.getDichiarazioneConsistenza(idDichiarazioneConsistenzaLg);
          descrizionePiano = consVO.getData();
        }
        
        if(idDichiarazioneConsistenzaLg.longValue() <= 0)
        {
          elencoRiepiloghi = gaaFacadeClient
            .riepilogoDestinazioneProduttivaComune(anagAziendaVO.getIdAzienda().longValue());
        }
        else
        {
          elencoRiepiloghi = gaaFacadeClient
            .riepilogoDestinazioneProduttivaComuneDichiarato(idDichiarazioneConsistenzaLg);
        }
      }
      
      
      
      
      final int MAXCOLUMN = 4;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Comune
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Uva da vino
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Uva da Mensa
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Altre destinazioni produttive
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Totale

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
      
      //Visulizzazione totali
      HSSFCellStyle styleTotaliTabellaDouble=workBook.createCellStyle();
      styleTotaliTabellaDouble.setFont(fontBold);
      styleTotaliTabellaDouble.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleTotaliTabellaDouble.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleTotaliTabellaDouble.setDataFormat(formato);
      
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
      


      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      
      ExcelServlet.buildCell(row, styleHeader,"STAMPA RIEPILOGO UNITA' ARBOREE PER DESTINAZIONE PRODUTTIVA - COMUNE AGGIORNATO AL "+descrizionePiano,(short) 0);

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
      ExcelServlet.buildCell(row, styleHeaderTable,"Uva da Vino",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uva da Mensa",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Altre destinazioni produttive",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Totale",(short) colonna++);
      
      
      
      BigDecimal totUvaVino = new BigDecimal(0);
      BigDecimal totUvaMensa = new BigDecimal(0);
      BigDecimal totAltreDestinazioni = new BigDecimal(0);
      BigDecimal totSupTotale = new BigDecimal(0);
      
      if(Validator.isNotEmpty(elencoRiepiloghi))
      {
        for (int i=0;i<elencoRiepiloghi.size();i++)
        {
  
          row=sheet.createRow(nRiga++);
          colonna=0;
          
          String comune = elencoRiepiloghi.get(i).getDescComune()+" ("+elencoRiepiloghi.get(i).getSiglaProv()+")";
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,comune,(short) colonna++);
          BigDecimal uvaVino = elencoRiepiloghi.get(i).getUvaDaVino();
          totUvaVino = totUvaVino.add(uvaVino);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, Double.parseDouble(uvaVino.toString().replace(',', '.')),(short) colonna++);
          BigDecimal uvaMensa = elencoRiepiloghi.get(i).getUvaDaMensa();
          totUvaMensa = totUvaMensa.add(uvaMensa);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, Double.parseDouble(uvaMensa.toString().replace(',', '.')),(short) colonna++);
          BigDecimal altreDestinazioni = elencoRiepiloghi.get(i).getAltreDestinazioniProduttive();
          totAltreDestinazioni = totAltreDestinazioni.add(altreDestinazioni);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, Double.parseDouble(altreDestinazioni.toString().replace(',', '.')),(short) colonna++);
          BigDecimal supTotale = elencoRiepiloghi.get(i).getSupVitata();
          totSupTotale = totSupTotale.add(supTotale);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, Double.parseDouble(supTotale.toString().replace(',', '.')),(short) colonna++);
          
        }
      }
      
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble,"Totale",(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble, Double.parseDouble(totUvaVino.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble, Double.parseDouble(totUvaMensa.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble, Double.parseDouble(totAltreDestinazioni.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble, Double.parseDouble(totSupTotale.toString().replace(',', '.')),(short) colonna++);
        

      OutputStream out = response.getOutputStream();

      String fileName="RiepilogoUV_"+anagAziendaVO.getCUAA()+"_DestProdComune";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelRiepilogoDestProdComuneServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelRiepilogoDestProdComuneServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

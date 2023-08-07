package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.Formatter;
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
public class ExcelRiepilogoVinoDOPProvinciaServlet extends ExcelServlet
{

 
  

  /**
   * 
   */
  private static final long serialVersionUID = -8260050217943192563L;

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
      SolmrLogger.debug(this, " - ExcelRiepilogoVinoDOPProvinciaServlet - INIZIO PAGINA ");

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
            .riepilogoProvinciaVinoDOP(anagAziendaVO.getIdAzienda().longValue());
        }
        else
        {
          elencoRiepiloghi = gaaFacadeClient
            .riepilogoProvinciaVinoDOPDichiarato(idDichiarazioneConsistenzaLg);
        }
      }
      
      
      
      
      final int MAXCOLUMN = 8;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Provincia
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Idoneità
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Resa max (q/ha)
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Sup. vit. 100% (ha)
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Prod. Resa 100% (q)
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Sup. vit. 70% (ha)
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Prod. Resa 70% (q)
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Sup. vit. 0% (ha)
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Prod. Resa 0% (q)

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
      
      
      
      //Formato per visualizzare correttamente le superifci
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
      
      //Formato per visualizzare correttamente le rese
      HSSFDataFormat numberFormat2 = workBook.createDataFormat();
      short formato2 = numberFormat2.getFormat("#,##0.00");

      HSSFCellStyle styleDatiTabellaDouble2 = workBook.createCellStyle();
      styleDatiTabellaDouble2.setFont(fontBlack);
      styleDatiTabellaDouble2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble2.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble2.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaDouble2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDouble2.setDataFormat(formato2);
      
      
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
      
      //Visulizzazione totali due cifre
      HSSFCellStyle styleTotaliTabellaDouble2 = workBook.createCellStyle();
      styleTotaliTabellaDouble2.setFont(fontBold);
      styleTotaliTabellaDouble2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble2.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble2.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaDouble2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleTotaliTabellaDouble2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleTotaliTabellaDouble2.setDataFormat(formato2);
      
      HSSFCellStyle styleTotaliTabellaNumber=workBook.createCellStyle();
      styleTotaliTabellaNumber.setFont(fontBold);
      styleTotaliTabellaNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleTotaliTabellaNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleTotaliTabellaNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      
      
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
      
      
      ++rigaCorrente;
      ++rigaCorrente;
      
      //Unisco le celle prima della resa
      for (int i=0;i<3;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+1));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe resa 100%
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente));
      region.setColumnFrom( (short) 3);
      region.setColumnTo( (short) 4);
      sheet.addMergedRegion(region);
      
      //Unisco le righe resa 70%
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente));
      region.setColumnFrom( (short) 5);
      region.setColumnTo( (short) 6);
      sheet.addMergedRegion(region);
      
      //Unisco le righe resa 0%
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente));
      region.setColumnFrom( (short) 7);
      region.setColumnTo( (short) 8);
      sheet.addMergedRegion(region);
      
      
      
      
      


      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      
      ExcelServlet.buildCell(row, styleHeader,"STAMPA RIEPILOGO UNITA' ARBOREE PER VINO - PROVINCIA AGGIORNATO AL "+descrizionePiano,(short) 0);

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
      ExcelServlet.buildCell(row, styleHeaderTable,"Provincia",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Idoneità",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Resa max (q/ha)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Resa 100%",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Resa 70%",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Resa 0%",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      
      
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup. vit. (ha)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Prod. (q)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup. vit. (ha)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Prod. (q)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup. vit. (ha)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Prod. (q)",(short) colonna++);
      
      
      
      
      BigDecimal totSupVit100 = new BigDecimal(0);
      BigDecimal totProdResa100 = new BigDecimal(0);
      BigDecimal totSupVit70 = new BigDecimal(0);
      BigDecimal totProdResa70 = new BigDecimal(0);
      BigDecimal totSupVit0 = new BigDecimal(0);
      BigDecimal totProdResa0 = new BigDecimal(0);
      
      if(Validator.isNotEmpty(elencoRiepiloghi))
      {
        for (int i=0;i<elencoRiepiloghi.size();i++)
        {
  
          row=sheet.createRow(nRiga++);
          colonna=0;
        
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRiepiloghi.get(i).getDescProv(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRiepiloghi.get(i).getTipoTipolgiaVino(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaNumber, Formatter.formatDouble(elencoRiepiloghi.get(i).getResa()),(short) colonna++);
          BigDecimal supVit100 = elencoRiepiloghi.get(i).getSupVitata100();
          totSupVit100 = totSupVit100.add(supVit100);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, Double.parseDouble(supVit100.toString().replace(',', '.')),(short) colonna++);
          BigDecimal prodResa100 = elencoRiepiloghi.get(i).getProdResa100();
          totProdResa100 = totProdResa100.add(prodResa100);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble2, Double.parseDouble(prodResa100.toString().replace(',', '.')),(short) colonna++);
          BigDecimal supVit70 = elencoRiepiloghi.get(i).getSupVitata70();
          totSupVit70 = totSupVit70.add(supVit70);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, Double.parseDouble(supVit70.toString().replace(',', '.')),(short) colonna++);
          BigDecimal prodResa70 = elencoRiepiloghi.get(i).getProdResa70();
          totProdResa70 = totProdResa70.add(prodResa70);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble2, Double.parseDouble(prodResa70.toString().replace(',', '.')),(short) colonna++);
          BigDecimal supVit0 = elencoRiepiloghi.get(i).getSupVitata0();
          totSupVit0 = totSupVit0.add(supVit0);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, Double.parseDouble(supVit0.toString().replace(',', '.')),(short) colonna++);
          BigDecimal prodResa0 = elencoRiepiloghi.get(i).getProdResa0();
          totProdResa0 = totProdResa0.add(prodResa0);
          ExcelServlet.buildCell(row, styleDatiTabellaDouble2, Double.parseDouble(prodResa0.toString().replace(',', '.')),(short) colonna++);
        }
      }
      
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga);
      region.setColumnFrom((short)0);
      region.setColumnTo((short)2);
      sheet.addMergedRegion(region);
      
      
      
      row=sheet.createRow(nRiga++);     
      colonna=0;
      ExcelServlet.buildCell(row, styleTotaliTabellaNumber,"Totale",(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaNumber,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaNumber,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble, Double.parseDouble(totSupVit100.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble2, Double.parseDouble(totProdResa100.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble, Double.parseDouble(totSupVit70.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble2, Double.parseDouble(totProdResa70.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble, Double.parseDouble(totSupVit0.toString().replace(',', '.')),(short) colonna++);
      ExcelServlet.buildCell(row, styleTotaliTabellaDouble2, Double.parseDouble(totProdResa0.toString().replace(',', '.')),(short) colonna++);
        
      
      //aggiunte note
      row=sheet.createRow(nRiga++); //salto riga
      row=sheet.createRow(nRiga++); //salto riga
      
      
      row=sheet.createRow(nRiga);
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga++);
      region.setColumnFrom((short)BEGINCOLUMN);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      
      
      ExcelServlet.buildCell(row, styleHeader, "Tutte le superfici sono espresse in ettari (ha).",(short) 0);
      
      
      row=sheet.createRow(nRiga);
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga++);
      region.setColumnFrom((short)BEGINCOLUMN);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      
      
      ExcelServlet.buildCell(row, styleHeader, "Attenzione: nel calcolo della resa non viene presa in considerazione l'eventuale percentuale fallanza dichiarata sulle unità vitate.",(short) 0);
      
      
      
      

      OutputStream out = response.getOutputStream();

      String fileName="RiepilogoUV_"+anagAziendaVO.getCUAA()+"_VinoProvincia";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelRiepilogoVinoDOPProvinciaServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelRiepilogoVinoDOPProvinciaServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;

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
public class ExcelVariazioniAziendaliServlet extends ExcelServlet
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -4233262285073220912L;

  /**
   * Inizializza il servlet
   *
   * @throws ServletException
   */
  public void init(ServletConfig config) throws ServletException 
  {
    super.init(config);
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
    process(request, response);
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
    process(request, response);
  }

  public void destroy() 
  {
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
      SolmrLogger.debug(this, " - ExcelVariazioniAziendaliServlet.jsp - INIZIO PAGINA ");
      
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      
      RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)request.getSession().getAttribute("utenteAbilitazioni");
      
      FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO = (FiltriRicercaVariazioniAziendaliVO) request.getSession()
      .getAttribute("filtriRicercaVariazioniAziendaliVO");
      
      RigaRicercaVariazioniAziendaliVO righe[] = gaaFacadeClient.getRigheRicercaVariazioni(filtriRicercaVariazioniAziendaliVO,utenteAbilitazioni,ruoloUtenza,true);

      
      
      int MAXCOLUMN = 10;
      final int BEGINCOLUMN = 0;
      

      //Creazione cartella excel
      HSSFWorkbook workBook=new HSSFWorkbook();


      //Creazione foglio excel
      HSSFSheet sheet=workBook.createSheet("variazioni");

      //Imposto i margini di stampa
      sheet.setMargin(HSSFSheet.LeftMargin,0.50);
      sheet.setMargin(HSSFSheet.RightMargin,0.50);

      //Permette di stampare orizzontale
      HSSFPrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);


      //Imposto la larghezza delle colonne
      int indiceColonne = 0;
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Presa visione
      sheet.setColumnWidth((short)indiceColonne++,(short)4200); //CUAA
      sheet.setColumnWidth((short)indiceColonne++,(short)2800); //Partita Iva
      sheet.setColumnWidth((short)indiceColonne++,(short)4100); //Denominazione
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Sede legale: comune
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Sede legale: indirizzo
      sheet.setColumnWidth((short)indiceColonne++,(short)1400); //Sede legale: CAP
      sheet.setColumnWidth((short)indiceColonne++,(short)2300); //Tipo variazione
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Tipo variazione
      sheet.setColumnWidth((short)indiceColonne++,(short)2250); //Data variazione
      sheet.setColumnWidth((short)indiceColonne++,(short)3300); //Amm. di Competenza
      
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

      
      

      Region region = new Region();
      
      int nRiga=0;

      //Unisco le celle dove inserisco "Variazioni aziendali"
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga);
      region.setColumnFrom((short)BEGINCOLUMN);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      
      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"VARIAZIONI AZIENDALI AGGIORNATE AL "+DateUtils.getCurrentDateString(),(short) BEGINCOLUMN);
            
      row=sheet.createRow(nRiga++); ////salto riga
      
      //Unisco le prima riga della tabella con la seconda
      for (int i=0;i<4;i++)
      {
        region.setRowFrom(nRiga);
        region.setRowTo(nRiga+1);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      //Unisco le colonne della sede legale
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga);
      region.setColumnFrom( (short) 4);
      region.setColumnTo( (short) 6);
      sheet.addMergedRegion(region);
      //Unisco le colonne del tipo variazione
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga);
      region.setColumnFrom( (short) 7);
      region.setColumnTo( (short) 8);
      sheet.addMergedRegion(region);
      //Unisco le colonne del data variazione
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga+1);
      region.setColumnFrom( (short) 9);
      region.setColumnTo( (short) 9);
      sheet.addMergedRegion(region);
      //Unisco le righe della amministrazione di competenza
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga+1);
      region.setColumnFrom( (short) 10);
      region.setColumnTo( (short) 10);
      sheet.addMergedRegion(region);
      
      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      
      
      ExcelServlet.buildCell(row, styleHeaderTable,"Presa visione "+ruoloUtenza.getTipoGruppoRuolo().getDescription(),(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"CUAA",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Partita Iva",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Denominazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sede legale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipo variazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data variazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Amm. di\nCompetenza",(short) colonna++);

      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Comune(PV)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Indirizzo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"CAP",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      
     
      
      //Creo e popolo contenuto della tabella
      for (int i=0;i<righe.length;i++)
      {
        row=sheet.createRow(nRiga++);
        colonna=0;
        if (righe[i].isFlagStoricizzato())
        {
          //righe storicizzate da fare in rosso
          if (righe[i].getIdVisioneVariazioneAzi()!=null)
            ExcelServlet.buildCell(row, styleRedDatiTabellaCenter,"SI",(short) colonna++);
          else
            ExcelServlet.buildCell(row, styleRedDatiTabellaCenter,"NO",(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getCuaa(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getPartitaIva(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getDenominazione(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getComune(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getIndirizzo(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getCap(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getTipologiaVariazione(),(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getVariazione(),(short) colonna++);
          if (righe[i].getDataVariazione()!=null)
            ExcelServlet.buildCell(row, styleRedDatiTabellaDate,righe[i].getDataVariazione(),(short) colonna++);
          else ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,"",(short) colonna++);
          ExcelServlet.buildCell(row, styleRedDatiTabellaLeft,righe[i].getDescProvAmmComp(),(short) colonna++);
        }
        else
        {
          //righe normali
          if (righe[i].getIdVisioneVariazioneAzi()!=null)
            ExcelServlet.buildCell(row, styleDatiTabellaCenter,"SI",(short) colonna++);
          else
            ExcelServlet.buildCell(row, styleDatiTabellaCenter,"NO",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getCuaa(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getPartitaIva(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getDenominazione(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getComune(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getIndirizzo(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getCap(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getTipologiaVariazione(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getVariazione(),(short) colonna++);
          if (righe[i].getDataVariazione()!=null)
            ExcelServlet.buildCell(row, styleDatiTabellaDate,righe[i].getDataVariazione(),(short) colonna++);
          else ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,righe[i].getDescProvAmmComp(),(short) colonna++);
        }
        
      }
      


      OutputStream out = response.getOutputStream();

      String fileName="Variazioni_aziendali";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelVariazioniAziendaliServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelVariazioniAziendaliServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }
}

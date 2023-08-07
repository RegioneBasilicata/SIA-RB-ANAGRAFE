package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.OutputStream;
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
public class ExcelControlliTerreniServlet extends ExcelServlet
{

  

  /**
   * 
   */
  private static final long serialVersionUID = 5219213355810551187L;


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
  @SuppressWarnings("static-access")
  private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      SolmrLogger.debug(this, " - ExcelControlliTerreniServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      Vector<?> anomalie= (Vector<?>)request.getSession().getAttribute("anomalieDichiarazioniConsistenza");
      HashMap<?,?> hFiltriVerificaTerreni = (HashMap<?,?>)request.getSession().getAttribute("hFiltriVerificaTerreni");
      final int MAXCOLUMN = 9;
      final int BEGINCOLUMN = 0;
      

      //Creazione cartella excel
      HSSFWorkbook workBook=new HSSFWorkbook();


      //Creazione foglio excel
      HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA());

      //Imposto i margini di stampa
      sheet.setMargin(sheet.LeftMargin,0.65);
      sheet.setMargin(sheet.RightMargin,0.65);

      //Permette di stampare orizzontale
      HSSFPrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);


      //Imposto la larghezza delle colonne
      int indiceColonne = 0;
      sheet.setColumnWidth((short)indiceColonne++,(short)1000);
      sheet.setColumnWidth((short)indiceColonne++,(short)5000);
      sheet.setColumnWidth((short)indiceColonne++,(short)5000);
      sheet.setColumnWidth((short)indiceColonne++,(short)1500);
      sheet.setColumnWidth((short)indiceColonne++,(short)1500);
      sheet.setColumnWidth((short)indiceColonne++,(short)1500);
      sheet.setColumnWidth((short)indiceColonne++,(short)1500);
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);
      sheet.setColumnWidth((short)indiceColonne++,(short)10000);

      HSSFRow row=null;

      //Creazione font di tipo bold
      HSSFFont fontBold=workBook.createFont();
      fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      fontBold.setFontHeight((short)160);

      HSSFFont fontNormal=workBook.createFont();
      fontNormal.setFontHeight((short)160);


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

      //Creazione stile usato dall'header (contenuti)
      HSSFCellStyle styleDati=workBook.createCellStyle();
      styleDati.setFont(fontNormal);



      //Creazione stili usato dall'header (contenuti)
      HSSFCellStyle styleDatiTabellaLeft=workBook.createCellStyle();
      styleDatiTabellaLeft.setFont(fontNormal);
      styleDatiTabellaLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      styleDatiTabellaLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaLeft.setWrapText(true);

      HSSFCellStyle styleDatiTabellaCenter=workBook.createCellStyle();
      styleDatiTabellaCenter.setFont(fontNormal);
      styleDatiTabellaCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      styleDatiTabellaCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaCenter.setWrapText(true);


      //Formato per visualizzare correttamente le date
      HSSFDataFormat dateFormat = workBook.createDataFormat();
      short formato = dateFormat.getFormat(ExcelServlet.FORMAT_DATE_1);
      HSSFCellStyle styleDatiTabellaDate=workBook.createCellStyle();
      styleDatiTabellaDate.setFont(fontNormal);
      styleDatiTabellaDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDate.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      styleDatiTabellaDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDate.setDataFormat(formato);


      Region region = new Region();
      int rigaCorrente = 0;

      //Unisco le celle dove inserisco Verifica Terreni
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
      region.setColumnFrom((short)2);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      //Unisco le celle dove inserisco il valore della denominazione
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)2);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      //Unisco le celle dove inserisco il valore dell' Indirizzo
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)2);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      
      ++rigaCorrente;
      
      //Unisco le celle dove inserisco il valore della tipologia del controllo
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)2);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      //Unisco le celle dove inserisco le Segnalazioni
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)2);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);

      ++rigaCorrente;

      ++rigaCorrente;
      //Unisco le celle della prima riga della tabella con quelle della seconda riga
      for (int i=0;i<10;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }






      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"ESITO CONTROLLI TERRENI AGGIORNATO AL "+DateUtils.getCurrentDateString(),(short) 0);

      row=sheet.createRow(nRiga++); //salto riga

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Cuaa:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getCUAA(),(short) 2);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Denominazione:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getDenominazione(),(short) 2);
      
      row=sheet.createRow(nRiga++);
      String comune = "";
      String sedeLegale = "";
      String cap = "";
      if(anagAziendaVO.getSedelegEstero() == null || anagAziendaVO.getSedelegEstero().equals("")) {
        comune =  anagAziendaVO.getDescComune();
        sedeLegale = anagAziendaVO.getSedelegProv();
        cap = anagAziendaVO.getSedelegCAP();
      }  
      else
      {
        comune = anagAziendaVO.getStatoEstero();
        sedeLegale = anagAziendaVO.getSedelegCittaEstero();
       }
      ExcelServlet.buildCell(row, styleHeader,"Indirizzo:",(short) BEGINCOLUMN);
      String indirizzo = getIndirizzo(anagAziendaVO.getSedelegIndirizzo(),
          cap, comune,  sedeLegale);
      ExcelServlet.buildCell(row, styleHeader,indirizzo,(short) 2);

      row=sheet.createRow(nRiga++);
      
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Tipologia:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, styleHeader, 
          StringUtils.checkNull((String)hFiltriVerificaTerreni.get("tipologia")),(short) 2);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Segnalazioni:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, styleHeader, 
          StringUtils.checkNull((String)hFiltriVerificaTerreni.get("segnalazioni")),(short) 2);

      

      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipologia",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Comune",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sez.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Fgl.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Part.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sub.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\ncat.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\ncond.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Descrizione",(short) colonna++);

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
      //Creo e popolo contenuto della tabella
      int size = anomalie.size();
      for (int i=0;i<size;i++)
      {
        row=sheet.createRow(nRiga++);
        ErrAnomaliaDicConsistenzaVO err = (ErrAnomaliaDicConsistenzaVO)anomalie.get(i);
        colonna=0;
        
        String valSegnalazione = "";
        if(err.getBloccanteStr().equalsIgnoreCase("P"))
        {
          valSegnalazione = "OK";
        }
        else if(err.getBloccanteStr().equalsIgnoreCase("S"))
        {
          valSegnalazione = "B";
        }
        else
        {
          valSegnalazione = "W";
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,valSegnalazione,(short) colonna++);
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,err.getTipoAnomaliaErrore(),(short) colonna++);
        
        if(Validator.isNotEmpty(err.getStoricoParticellaVO()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,
                StringUtils.checkNull(err.getStoricoParticellaVO().getComuneParticellaVO().getDescom())
                ,(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,
              StringUtils.checkNull(err.getStoricoParticellaVO().getSezione()),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,
              StringUtils.checkNull(err.getStoricoParticellaVO().getFoglio()),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,
              StringUtils.checkNull(err.getStoricoParticellaVO().getParticella()),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,
              StringUtils.checkNull(err.getStoricoParticellaVO().getSubalterno()),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,
              Formatter.formatDouble4(err.getStoricoParticellaVO().getSupCatastale()), (short) colonna++);    
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
        }
        
        if(Validator.isNotEmpty(err.getConduzioneParticellaVO()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,
              Formatter.formatDouble4(err.getConduzioneParticellaVO().getSuperficieCondotta()),(short) colonna++); 
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,err.getDescAnomaliaErrore(),(short) colonna++);
      }
      
      row=sheet.createRow(nRiga++);
      
      
      row=sheet.createRow(nRiga);
      region.setRowFrom(nRiga);
      region.setRowTo(nRiga);
      region.setColumnFrom((short)2);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);
      
      ExcelServlet.buildCell(row, styleHeader,"Legenda:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, styleHeader,"B=Bloccante, W=Warning, OK=Controllo positivo",(short) 2);
      
      
      
      


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_ControlliTerreni";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelControlliTerreniServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelControlliTerreniServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


  private String getIndirizzo(String indirizzo, String cap, String localita, String provincia)
  {
    String indirizzoVw = "";

    if(Validator.isNotEmpty(indirizzo))
    {
      indirizzoVw = indirizzo;
    }

    if(Validator.isNotEmpty(cap))
    {
      if(Validator.isNotEmpty(indirizzo))
      {
        indirizzoVw += " - ";
      }
      indirizzoVw += cap;
    }

    if(Validator.isNotEmpty(localita))
    {
      if(Validator.isNotEmpty(indirizzo))
      {
        indirizzoVw += " - ";
      }
      indirizzoVw += localita;
    }
    
    if(Validator.isNotEmpty(provincia))
    {
      if(Validator.isNotEmpty(provincia))
      {
        indirizzoVw += " (";
      }
      indirizzoVw += provincia +")";
    }

    return indirizzoVw;

  }


}

package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
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
public class ExcelBrogliaccioServlet extends ExcelServlet
{
 

  /**
   * 
   */
  private static final long serialVersionUID = 8544978459008947922L;

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
      SolmrLogger.debug(this, " - ExcelBrogliaccioServlet - INIZIO PAGINA ");
      
      

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
      FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)request
        .getSession().getAttribute("filtriParticellareRicercaVO");
      AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      
      
      String descrizionePiano = (String)request.getAttribute("descrizionePiano");
      
      Long idDichiarazioneConsistenza = null;
      if((filtriParticellareRicercaVO.getIdPianoRiferimento() != null)
        && (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() > 0))
      {
        idDichiarazioneConsistenza = filtriParticellareRicercaVO.getIdPianoRiferimento();
      }
      
      //Aggiungo colonne tipoArea
      Vector<TipoAreaVO> vTipoArea = gaaFacadeClient.getDescTipoAreaBrogliaccio(idDichiarazioneConsistenza);
      int sizeTipoArea = 0;
      if(vTipoArea != null)
        sizeTipoArea = vTipoArea.size();
      
      filtriParticellareRicercaVO.setvTipoArea(vTipoArea);
      
      
      Vector<AnagParticellaExcelVO> elencoParticelleExcel = anagFacadeClient
        .searchParticelleExcelByParameters(filtriParticellareRicercaVO, anagAziendaVO.getIdAzienda());
      
      
      
      
      
      
      int totaleColonne = 57 + sizeTipoArea;
      
      final int MAXCOLUMN = totaleColonne;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Sup. Agr.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Sup Eleg.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Sup Eleg. netta
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Registri
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); //C.P.
      sheet.setColumnWidth((short)indiceColonne++,(short)1000); //Irr.
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo primario/Occupazione suolo
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo primario/Destinazione
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo primario/Uso
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo primario/Qualità
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo primario/Varietà
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Uso del suolo primario/Sup.
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Semina primario/Epoca
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Semina primario/Tipo
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Semina primario/Data inizio
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Semina primario/Data fine
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo secondario/Occupazione suolo
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo secondario/Destinazione
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo secondario/Uso
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo secondario/Qualità
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Uso del suolo secondario/Varietà
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Uso del suolo secondario/Sup.
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Semina secondario/Epoca
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Semina secondario/Tipo
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Semina secondario/Data inizio
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Semina secondario/Data fine
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Mantenimento
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Allevamento
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Elementi caratteristici del paesaggio/Tipo
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Elementi caratteristici del paesaggio/Valore
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Elementi caratteristici del paesaggio/Unita di misura
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Elementi caratteristici del paesaggio/Valore in ettari
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Elementi caratteristici del paesaggio/Valore valido per il controllo
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Biologico/Bio
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Biologico/Conv.
      sheet.setColumnWidth((short)indiceColonne++,(short)1700); //Biologico/In conver.
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Biologico/D.i.
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Biologico/D.f.
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Impianto/Num. piante
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Impianto/Anno
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Zona alt.
      sheet.setColumnWidth((short)indiceColonne++,(short)6000); //Documento
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //istanza riesame/richiesta
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //istanza riesame/evasa
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Note
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Notifica
      
      //Aggiungo colonne tipoArea
      for(int t=0;t<sizeTipoArea;t++)
      {
        sheet.setColumnWidth((short)indiceColonne++,(short)3000); //TipoArea
      }
      
      
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
      region.setColumnTo((short)(MAXCOLUMN-1));
      sheet.addMergedRegion(region);
      
      ++rigaCorrente;


      //Unisco le celle dove inserisco il valore del CUAA
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)(MAXCOLUMN-1));
      sheet.addMergedRegion(region);

      //Unisco le celle dove inserisco il valore della DENOMINAZIONE
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)(MAXCOLUMN-1));
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
      for(int i=11;i<17;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      
      //Unisco le righe/colonne dell'uso primario
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)17);
      region.setColumnTo((short)22);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne del uso/destinazione/uso/qualita/varieta/sup
      for (int i=17;i<23;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne semina primario
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)23);
      region.setColumnTo((short)26);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne del epoca/tipo/data inizio/data fine
      for (int i=23;i<27;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne dell'uso secondario
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)27);
      region.setColumnTo((short)32);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne del occ/destinazione/uso/qualita/Varieta/sup
      for (int i=27;i<33;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne semina secondario
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)33);
      region.setColumnTo((short)36);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne del epoca/tipo/data inizio/data fine secondario
      for (int i=33;i<37;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe del mantenimento
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+3));
      region.setColumnFrom( (short) 37);
      region.setColumnTo( (short) 37);
      sheet.addMergedRegion(region);
      
      //Unisco le righe dell'allevamento
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+3));
      region.setColumnFrom( (short) 38);
      region.setColumnTo( (short) 38);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne element paesaggio
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)39);
      region.setColumnTo((short)43);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne del tipo/val/um/val et/val ctrl
      for (int i=39;i<44;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne del biologico
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)44);
      region.setColumnTo((short)48);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne del bio/conv/in conv./d.i./d.f.
      for (int i=44;i<49;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne dell'impianto
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)49);
      region.setColumnTo((short)50);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne del num piante/anno
      for (int i=49;i<51;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      
      for(int i=51;i<53;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);    
      }
      
      //Unisco le righe/colonne dell'istanza riesame
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)53);
      region.setColumnTo((short)54);
      sheet.addMergedRegion(region);
      //Unisco le righe/colonne del richiesta lavorata
      for (int i=53;i<55;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      for(int i=55;i<MAXCOLUMN;i++)
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
      
      ExcelServlet.buildCell(row, styleHeader,"STAMPA PIANO COLTURALE AGGIORNATO AL "+descrizionePiano,(short) 0);

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
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nAgr.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nEleg.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nEleg.\nnetta",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Registri",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"C.P.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Irr.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uso del suolo primario",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Semina primario",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uso del suolo secondario",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Semina secondario",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Mantenimento",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Allevamento",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Elementi caratteristici del paesaggio",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Biologico",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Impianto",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Zona\nAlt.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Documento",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Istanza riesame",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Note",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Notifica",(short) colonna++);
      //Aggiungo colonne tipoArea
      for(int t=0;t<sizeTipoArea;t++)
      {
        ExcelServlet.buildCell(row, styleHeaderTable,vTipoArea.get(t).getDescrizioneEstesa(),(short) colonna++);
      }
      
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
      //Aggiungo colonne tipoArea
      for(int t=0;t<sizeTipoArea;t++)
      {
        ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++); //TipoArea
      }
      
      row=sheet.createRow(nRiga++);
      row.setHeight((short)800);
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Occ. suolo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Destinazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uso",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Qualita'",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Varieta'",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Epoca",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data inizio",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data fine",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Occ. suolo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Destinazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uso",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Qualita'",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Varieta'",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Epoca",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data inizio",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data fine",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Valore",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Unita' di misura",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Valore in ettari",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Valore valido per il controllo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Bio",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Conv.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"In\nconver.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"D.i.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"D.f.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Num.\npiante",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Anno",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Richiesta",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Evasa",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      //Aggiungo colonne tipoArea
      for(int t=0;t<sizeTipoArea;t++)
      {
        ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++); //TipoArea
      }
      
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
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      //Aggiungo colonne tipoArea
      for(int t=0;t<sizeTipoArea;t++)
      {
        ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++); //TipoArea
      }
      


      for (int i=0;i<elencoParticelleExcel.size();i++)
      {

        row=sheet.createRow(nRiga++);
        colonna=0;

        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getLabelUte(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getIstatComuneParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getDescrizioneComuneParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getSezione(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getFoglio(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getParticella(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getSubalterno(),(short) colonna++);
        if(elencoParticelleExcel.get(i).getSuperficieCatastale() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSuperficieCatastale().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSuperficieGrafica() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSuperficieGrafica().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getIdTitoloPossesso(),(short) colonna++);
        BigDecimal percentualePossessoTmp = elencoParticelleExcel.get(i).getPercentualePossesso();
        if(percentualePossessoTmp != null)
        {
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }
          String percentualePossesso = Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp);
          ExcelServlet.buildCell(row, styleDatiTabellaCenter, percentualePossesso,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaCenter, "",(short) colonna++);
        }
        
        if(elencoParticelleExcel.get(i).getIdTitoloPossesso()
          .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
        {
          if(elencoParticelleExcel.get(i).getSuperficieCondotta() != null)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSuperficieCondotta().replace(',', '.')),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
          }
        }
        else
        {
          if(elencoParticelleExcel.get(i).getSupAgronomica() != null)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSupAgronomica().replace(',', '.')),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
          }
        }
        if(elencoParticelleExcel.get(i).getSupEleggibile() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSupEleggibile().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSupEleggibileNetta() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSupEleggibileNetta().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft, elencoParticelleExcel.get(i).getDescFonteRegPascoli(), (short) colonna++);
        
        
        
        
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getIdCasoParticolare()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getIdCasoParticolare(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getIrrigua(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getUsoPrimario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoDestinazionePrimario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoDettUsoPrimario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoQualitaUsoPrimario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getVarieta(),(short) colonna++);
        
        if(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.compareTo(elencoParticelleExcel.get(i).getIdTitoloPossesso()) == 0)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"0,0000",(short) colonna++);
        }
        else
        {
          if(elencoParticelleExcel.get(i).getSuperficieUtilizzata() != null)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSuperficieUtilizzata().replace(',', '.')),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
          }
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoSeminaPrimario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoEpocaSeminaPrimario(),(short) colonna++);
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataInzioDestPrim()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,elencoParticelleExcel.get(i).getDataInzioDestPrim(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataFineDestPrim()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,elencoParticelleExcel.get(i).getDataFineDestPrim(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getUsoSecondario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoDestinazioneSecondario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoDettUsoSecondario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoQualitaUsoSecondario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getVarietaSecondaria(),(short) colonna++);
        if(elencoParticelleExcel.get(i).getSuperficieUtilizzataSecondaria() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSuperficieUtilizzataSecondaria().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoSeminaSecondario(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoEpocaSeminaSecondario(),(short) colonna++);
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataInzioDestSec()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,elencoParticelleExcel.get(i).getDataInzioDestSec(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataFineDestSec()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,elencoParticelleExcel.get(i).getDataFineDestSec(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getMantenimento(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getAllevamento(),(short) colonna++);
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getTipoEfa(),(short) colonna++);
        if(elencoParticelleExcel.get(i).getValoreOriginale() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getValoreOriginale().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getDescUnitaMisura(),(short) colonna++);
        if(elencoParticelleExcel.get(i).getValoreDopoConversione() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getValoreDopoConversione().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getValoreDopoPonderazione() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getValoreDopoPonderazione().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSupBiologico() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSupBiologico().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSupConvenzionale() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSupConvenzionale().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(elencoParticelleExcel.get(i).getSupInConversione() != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,Double.parseDouble(elencoParticelleExcel.get(i).getSupInConversione().replace(',', '.')),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
        }
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataInizioConversione()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,elencoParticelleExcel.get(i).getDataInizioConversione(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataFineConversione()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,elencoParticelleExcel.get(i).getDataFineConversione(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getNumeroPianteCeppi()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaCenter, elencoParticelleExcel.get(i).getNumeroPianteCeppi(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getAnnoImpianto(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoParticelleExcel.get(i).getDescrizioneZonaAltimetrica(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getDocumento(),(short) colonna++);
        
        
        
          
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataRichiesta()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, elencoParticelleExcel.get(i).getDataRichiesta(), (short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getDataEvasione()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, elencoParticelleExcel.get(i).getDataEvasione(), (short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }  
          
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoParticelleExcel.get(i).getNote(),(short) colonna++);
        
        String notifica = "No";
        if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getInNotifica()))
        {
          notifica = "Si";
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter, notifica,(short) colonna++);
        
        
        //Aggiungo colonne tipoArea
        for(int t=0;t<sizeTipoArea;t++)
        {
          if(Validator.isNotEmpty(elencoParticelleExcel.get(i).getvDescValoreArea().get(t)))
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, elencoParticelleExcel.get(i).getvDescValoreArea().get(t),(short) colonna++);
          else
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, "",(short) colonna++);
        }
        
      }
        

      OutputStream out = response.getOutputStream();

      String fileName=""+anagAziendaVO.getCUAA();

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelBrogliaccioServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelBrogliaccioServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


}

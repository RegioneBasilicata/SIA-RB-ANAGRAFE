package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.ProfileUtils;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.io.OutputStream;
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
public class ExcelElencoNotificheServlet extends ExcelServlet
{

  

  /**
   * 
   */
  private static final long serialVersionUID = -6311469091806198159L;

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
  @SuppressWarnings("unchecked")
  private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      SolmrLogger.debug(this, " - ExcelElencoNotifiche - INIZIO PAGINA ");
      RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
      Vector<NotificaVO> elencoNotifiche = (Vector<NotificaVO>)request.getSession().getAttribute("elencoNotifiche");
      
      
      
      int MAXCOLUMN = 8;
      final int BEGINCOLUMN = 0;
      

      //Creazione cartella excel
      HSSFWorkbook workBook=new HSSFWorkbook();


      //Creazione foglio excel
      HSSFSheet sheet=workBook.createSheet("NOTIFICHE");

      //Imposto i margini di stampa
      sheet.setMargin(HSSFSheet.LeftMargin,0.55);
      sheet.setMargin(HSSFSheet.RightMargin,0.55);

      //Permette di stampare orizzontale
      HSSFPrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);


      //Imposto la larghezza delle colonne
      int indiceColonne = 0;
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Tipo
      sheet.setColumnWidth((short)indiceColonne++,(short)3000); //Categoria
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); //Azienda
      sheet.setColumnWidth((short)indiceColonne++,(short)5000); //Descrizione
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Apertura/Data
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Apertura/Utente
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Chiusura/Data
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Chiusura/Utente
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Chiusura/Motivo

      HSSFRow row=null;

      //Creazione font di tipo bold
      HSSFFont fontBold=workBook.createFont();
      fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      fontBold.setFontHeight((short)140);

      HSSFFont fontNormal=workBook.createFont();
      fontNormal.setFontHeight((short)140);


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
      
      //Formato per visualizzare correttamente le date con ore minuti secondi
      HSSFDataFormat dateFormatTime = workBook.createDataFormat();
      short formatoTime = dateFormatTime.getFormat(ExcelServlet.FORMAT_DATE_2);
      HSSFCellStyle styleDatiTabellaDateTime=workBook.createCellStyle();
      styleDatiTabellaDateTime.setFont(fontNormal);
      styleDatiTabellaDateTime.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDateTime.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      styleDatiTabellaDateTime.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDateTime.setDataFormat(formatoTime);
      
      HSSFDataFormat numberFormat = workBook.createDataFormat();
      formato = numberFormat.getFormat("#,##0.0000");
                 
      HSSFCellStyle styleDatiTabellaDouble=workBook.createCellStyle();
      styleDatiTabellaDouble.setFont(fontNormal);
      styleDatiTabellaDouble.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaDouble.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDouble.setDataFormat(formato);


      Region region = new Region();
      int rigaCorrente = 0;

      //Unisco le celle dove inserisco Elenco polizze
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)BEGINCOLUMN);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);

      ++rigaCorrente;
      ++rigaCorrente;
      ++rigaCorrente;
      
      //Unisco le righe prima dell'apertura
      for(int i=0;i<4;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne della Apertura
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)4);
      region.setColumnTo((short)5);
      sheet.addMergedRegion(region);
      
      //Unisco le righe/colonne della Chiusura
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)6);
      region.setColumnTo((short)8);
      sheet.addMergedRegion(region);
      





      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"NOTIFICHE - AGGIORNATO AL "+DateUtils.getCurrentDateString(),(short) BEGINCOLUMN);

      

      row=sheet.createRow(nRiga++);     
      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Categoria",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Azienda",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Descrizione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Apertura",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Chiusura",(short) colonna++);
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
      
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Utente",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Utente",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Motivo",(short) colonna++);
      

      
      //Creo e popolo contenuto della tabella
      if(elencoNotifiche != null)
      {
        for (int i=0;i<elencoNotifiche.size();i++)
        {
          row=sheet.createRow(nRiga++);
          NotificaVO notificaVO = elencoNotifiche.get(i);
          colonna=0;
          
          if(notificaVO.getIdTipologiaNotifica()
              .compareTo((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE")) == 0) 
          {
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, "Blocco",(short) colonna++);        
          }
          else if(notificaVO.getIdTipologiaNotifica()
            .compareTo((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_WARNING")) == 0) 
          {
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, "Warning",(short) colonna++);
          }
          else if (notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, "Blocco procedimenti vitivinicoli",(short) colonna++);
          }
          else if (notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE) == 0)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, "Variazioni catastali",(short) colonna++);
          }
          else 
          {
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, "Informazione",(short) colonna++);
          }
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaVO.getDescCategoriaNotifica(), (short) colonna++);
          String azienda = notificaVO.getCuaa()+" - "
              +notificaVO.getDenominazione()+" - "+notificaVO.getDescrizioneComuneSedeLegale();         
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, azienda, (short) colonna++);
          
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaVO.getDescrizione(), (short) colonna++);
          
          if(notificaVO.getDataInserimento() !=null)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDate, notificaVO.getDataInserimento(),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
          }         
          
          String utenteApertura = ProfileUtils.setFieldUltimaModificaByRuoloNoHtmpl(ruoloUtenza,null,notificaVO.getDenominazioneUtenteInserimento(),
              notificaVO.getDescEnteAppartenenzaUtenteInserimento(), null);         
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, utenteApertura, (short) colonna++);
          
          if(notificaVO.getDataChiusura() !=null)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDate, notificaVO.getDataChiusura(),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
          }         
          
          String utenteChiusura = ProfileUtils.setFieldUltimaModificaByRuoloNoHtmpl(ruoloUtenza,null,notificaVO.getDenominazioneUtenteChiusura(),
              notificaVO.getDescEnteAppartenenzaUtenteChiusura(), null);         
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, utenteChiusura, (short) colonna++);
          
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaVO.getNoteChisura(), (short) colonna++); 
                 
          
       
           
          
          
          
          
          
          
        } 
      }
      


      OutputStream out = response.getOutputStream();

      String fileName="ElencoNotifiche";
      
      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelElencoNotificheServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelElencoNotificheServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


  

}

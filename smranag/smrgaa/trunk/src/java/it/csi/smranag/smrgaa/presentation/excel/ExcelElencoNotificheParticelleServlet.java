package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.NotificaEntitaVO;
import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.ProfileUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

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
public class ExcelElencoNotificheParticelleServlet extends ExcelServlet
{

  

 
  

  /**
   * 
   */
  private static final long serialVersionUID = 2078888190152897637L;

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
      SolmrLogger.debug(this, " - ExcelElencoNotificheUv - INIZIO PAGINA ");
      RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
      @SuppressWarnings("unchecked")
      Vector<NotificaVO> elencoNotifiche = (Vector<NotificaVO>)request.getSession().getAttribute("elencoNotifiche");
      HashMap<Long,Vector<NotificaEntitaVO>> hEntita = null;
      if((elencoNotifiche != null) && (elencoNotifiche.size() > 0))
      {
        AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
        
        long[] ids = new long[elencoNotifiche.size()];
        for(int i=0;i<elencoNotifiche.size();i++)
        {
          ids[i] = elencoNotifiche.get(i).getIdNotifica().longValue();
        }        
        hEntita = anagFacadeClient.getNotificheEntitaParticellaFromIdNotifica(ids);
      }
      
      
      
      int MAXCOLUMN = 21;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Dati Particella/Comune
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Dati Particella/Sezione
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Dati Particella/Foglio
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Dati Particella/Particella
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Dati Particella/Subalterno
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Dati Particella/titolo possesso
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Dati Particella/percentuale possesso
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Dati Particella/uso suolo varietà
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Dati Particella/sup util
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Dati Particella/Note
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Dati Particella/Data chiusura
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Dati Particella/Utente chiusura
      sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Dati Particella/Motivo chiusura

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

      //Unisco le celle dove inserisco Elenco notifiche
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
        region.setRowTo((rigaCorrente+3));
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
      
      for(int i=4;i<6;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom((short)i);
        region.setColumnTo((short)i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne della Chiusura
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)6);
      region.setColumnTo((short)8);
      sheet.addMergedRegion(region);
      
      for(int i=6;i<9;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom((short)i);
        region.setColumnTo((short)i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne delle particelle
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)9);
      region.setColumnTo((short)21);
      sheet.addMergedRegion(region);
      
      for(int i=9;i<14;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom((short)i);
        region.setColumnTo((short)i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne della conduzione
      region.setRowFrom(rigaCorrente+2);
      region.setRowTo(rigaCorrente+2);
      region.setColumnFrom((short)14);
      region.setColumnTo((short)15);
      sheet.addMergedRegion(region);
      
      for(int i=16;i<22;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom((short)i);
        region.setColumnTo((short)i);
        sheet.addMergedRegion(region);
      }
      
      
      





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
      ExcelServlet.buildCell(row, styleHeaderTable,"Dati particelle",(short) colonna++);
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Comune",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sz.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Fgl.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Part.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sub.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Cond",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uso del suolo / varietà",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup.\nutil.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Note",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data chiusura",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Utente chiusura",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Motivo chiusura",(short) colonna++);
      
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
      ExcelServlet.buildCell(row, styleHeaderTable,"%",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      

      
      //Creo e popolo contenuto della tabella
      if(elencoNotifiche != null)
      {
        int size = elencoNotifiche.size();
        for (int i=0;i<size;i++)
        {
          
          
          boolean firstNotifica = true;
          int numRigheNotifica = 0;
          Vector<NotificaEntitaVO> vEntita = null;
          if(Validator.isNotEmpty(hEntita) 
            && hEntita.get(elencoNotifiche.get(i).getIdNotifica()) != null)
          {
            vEntita = hEntita.get(elencoNotifiche.get(i).getIdNotifica());
            if((vEntita != null) && (vEntita.size() > 0))
            {
              //num particelle
              for(int j=0;j<vEntita.size();j++)
              {
                NotificaEntitaVO notificaEntitaVO = vEntita.get(j);
                //num conduzioni
                for(int k=0;k<notificaEntitaVO.getStoricoParticellaVO().getvConduzioniDichiarate().size();k++)
                {
                  ConduzioneDichiarataVO conduzioneDichiarataVO = notificaEntitaVO.getStoricoParticellaVO().getvConduzioniDichiarate().get(k);
                  //Utilizzi
                  if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
                  {
                    numRigheNotifica += conduzioneDichiarataVO.getvUtilizzi().size();
                  }
                  else
                  {
                    numRigheNotifica++;
                  }
                }
              }
            }
          }
          
          for(int j=0;j<vEntita.size();j++)
          {
            NotificaEntitaVO notificaEntitaVO = vEntita.get(j); 
            boolean firstParticella = true;
            int numRigheParticella = 0;
            for(int k=0;k<notificaEntitaVO.getStoricoParticellaVO().getvConduzioniDichiarate().size();k++)
            {
              ConduzioneDichiarataVO conduzioneDichiarataVO = notificaEntitaVO.getStoricoParticellaVO().getvConduzioniDichiarate().get(k);
              //Utilizzi
              if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
              {
                numRigheParticella = numRigheParticella +conduzioneDichiarataVO.getvUtilizzi().size();
              }
              else
              {
                numRigheParticella++;
              }
            }
            
            
            for(int k=0;k<notificaEntitaVO.getStoricoParticellaVO().getvConduzioniDichiarate().size();k++)
            {
              
              boolean firstConduzione = true;
              int numRigheConduzione = 0;
              
              ConduzioneDichiarataVO conduzioneDichiarataVO = notificaEntitaVO.getStoricoParticellaVO().getvConduzioniDichiarate().get(k);
              
              if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
              {
                numRigheConduzione = numRigheConduzione +conduzioneDichiarataVO.getvUtilizzi().size();
              }
              else
              {
                numRigheConduzione++;
              }
              
              //Utilizzi
              if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
              {
                for(int z=0;z<conduzioneDichiarataVO.getvUtilizzi().size();z++)
                {
                  
                  colonna = 0;
                  row=sheet.createRow(nRiga);
                  int ampiezzaRiga = numRigheNotifica;
                  if(firstNotifica)
                  {
                    firstNotifica = false;
                    for (int a=0;a<9;a++)
                    {
                      region.setRowFrom(nRiga);
                      region.setRowTo((nRiga+ampiezzaRiga-1));
                      region.setColumnFrom( (short) a);
                      region.setColumnTo( (short) a);
                      sheet.addMergedRegion(region);
                    }
                
                    NotificaVO notificaVO = elencoNotifiche.get(i);
                
                
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
                  else
                  {
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++);             
                  }
                  
                  
                  
                  if(firstParticella)
                  {
                    //firstParticella = false;
                    int ampiezzaRigaParticella = numRigheParticella;
                    for (int b=9;b<14;b++)
                    {
                      region.setRowFrom(nRiga);
                      region.setRowTo((nRiga+ampiezzaRigaParticella-1));
                      region.setColumnFrom( (short) b);
                      region.setColumnTo( (short) b);
                      sheet.addMergedRegion(region);
                    }
                  
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaEntitaVO.getStoricoParticellaVO().getComuneParticellaVO().getDescom()
                        +" ("+notificaEntitaVO.getStoricoParticellaVO().getComuneParticellaVO().getSiglaProv()+")",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getSezione(),
                        (short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getFoglio(),
                        (short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getParticella(),
                        (short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getSubalterno(),
                        (short) colonna++);
                  }
                  else
                  {
                    ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);               
                  }
                  
                  
                  if(firstConduzione)
                  {
                    firstConduzione = false;
                    int ampiezzaRigaConduzione = numRigheConduzione;
                    for (int b=14;b<16;b++)
                    {
                      region.setRowFrom(nRiga);
                      region.setRowTo((nRiga+ampiezzaRigaConduzione-1));
                      region.setColumnFrom( (short) b);
                      region.setColumnTo( (short) b);
                      sheet.addMergedRegion(region);
                    }
                    
                    ExcelServlet.buildCell(row, styleDatiTabellaCenter,conduzioneDichiarataVO.getIdTitoloPossesso(),(short) colonna++);
                    BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
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
                  }
                  else
                  {
                    ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                  }
                  
                  
                  String descUtilizzo = "";
                  if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi().get(z).getTipoUtilizzoVO()))
                  {
                    descUtilizzo = "["+conduzioneDichiarataVO.getvUtilizzi().get(z).getTipoUtilizzoVO()
                      .getCodice()+"] "+conduzioneDichiarataVO.getvUtilizzi().get(z).getTipoUtilizzoVO().getDescrizione();
                  }
                  if(Validator.isNotEmpty(descUtilizzo))
                  {
                    descUtilizzo += "\n";
                  }
                  
                  if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi().get(z).getTipoVarietaVO()))
                  {
                    descUtilizzo += "["+conduzioneDichiarataVO.getvUtilizzi().get(z).getTipoVarietaVO()
                      .getCodiceVarieta()+"] "+conduzioneDichiarataVO.getvUtilizzi().get(z).getTipoVarietaVO().getDescrizione();
                  }
                  
                  
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,descUtilizzo,(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble, Formatter.formatDouble4(conduzioneDichiarataVO.getvUtilizzi().get(z).getSupUtilizzataBg()),(short) colonna++);
                  
                  if(firstParticella)
                  {
                    firstParticella = false;
                    int ampiezzaRigaParticella = numRigheParticella;
                    for (int b=18;b<22;b++)
                    {
                      region.setRowFrom(nRiga);
                      region.setRowTo((nRiga+ampiezzaRigaParticella-1));
                      region.setColumnFrom( (short) b);
                      region.setColumnTo( (short) b);
                      sheet.addMergedRegion(region);
                    }
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaEntitaVO.getNote(), (short) colonna++);
                    if(notificaEntitaVO.getDataFineValidita() !=null)
                    {
                      ExcelServlet.buildCell(row, styleDatiTabellaDate, notificaEntitaVO.getDataFineValidita(),(short) colonna++);
                    }
                    else
                    {
                      ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                    }
                    String utenteChiusuraParticella = "";
                    if(Validator.isNotEmpty(notificaEntitaVO.getNoteChiusuraEntita()))
                    {
                      utenteChiusuraParticella = ProfileUtils.setFieldUltimaModificaByRuoloNoHtmpl(ruoloUtenza,null,notificaEntitaVO.getDenUtente(),
                        notificaEntitaVO.getDenEnteUtente(), null);
                    }
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, utenteChiusuraParticella, (short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaEntitaVO.getNoteChiusuraEntita(), (short) colonna++);
                                       
                  }
                  else
                  {
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                  }
                  
                  
                  
                  
                  nRiga++;
                  
                } //for utilizzi
              }
              else
              {
                colonna = 0;
                row=sheet.createRow(nRiga);
                int ampiezzaRiga = numRigheNotifica;
                if(firstNotifica)
                {
                  firstNotifica = false;
                  for (int a=0;a<9;a++)
                  {
                    region.setRowFrom(nRiga);
                    region.setRowTo((nRiga+ampiezzaRiga-1));
                    region.setColumnFrom( (short) a);
                    region.setColumnTo( (short) a);
                    sheet.addMergedRegion(region);
                  }
              
                  NotificaVO notificaVO = elencoNotifiche.get(i);
              
              
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
                else
                {
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++); 
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, "", (short) colonna++);             
                }
                
                
                
                if(firstParticella)
                {
                  //firstParticella = false;
                  int ampiezzaRigaParticella = numRigheParticella;
                  for (int b=9;b<14;b++)
                  {
                    region.setRowFrom(nRiga);
                    region.setRowTo((nRiga+ampiezzaRigaParticella-1));
                    region.setColumnFrom( (short) b);
                    region.setColumnTo( (short) b);
                    sheet.addMergedRegion(region);
                  }
                
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaEntitaVO.getStoricoParticellaVO().getComuneParticellaVO().getDescom()
                      +" ("+notificaEntitaVO.getStoricoParticellaVO().getComuneParticellaVO().getSiglaProv()+")",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getSezione(),
                      (short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getFoglio(),
                      (short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getParticella(),
                      (short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaCenter, notificaEntitaVO.getStoricoParticellaVO().getSubalterno(),
                      (short) colonna++);
                }
                else
                {
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);               
                }
                
                
                if(firstConduzione)
                {
                  firstConduzione = false;
                  int ampiezzaRigaConduzione = numRigheConduzione;
                  for (int b=14;b<16;b++)
                  {
                    region.setRowFrom(nRiga);
                    region.setRowTo((nRiga+ampiezzaRigaConduzione-1));
                    region.setColumnFrom( (short) b);
                    region.setColumnTo( (short) b);
                    sheet.addMergedRegion(region);
                  }
                  
                  ExcelServlet.buildCell(row, styleDatiTabellaCenter,conduzioneDichiarataVO.getIdTitoloPossesso(),(short) colonna++);
                  BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
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
                }
                else
                {
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                }
                
                
                
                ExcelServlet.buildCell(row, styleDatiTabellaDouble,"",(short) colonna++);
                ExcelServlet.buildCell(row, styleDatiTabellaDouble, "",(short) colonna++);
                
                if(firstParticella)
                {
                  firstParticella = false;
                  int ampiezzaRigaParticella = numRigheParticella;
                  for (int b=18;b<22;b++)
                  {
                    region.setRowFrom(nRiga);
                    region.setRowTo((nRiga+ampiezzaRigaParticella-1));
                    region.setColumnFrom( (short) b);
                    region.setColumnTo( (short) b);
                    sheet.addMergedRegion(region);
                  }
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaEntitaVO.getNote(), (short) colonna++);
                  if(notificaEntitaVO.getDataFineValidita() !=null)
                  {
                    ExcelServlet.buildCell(row, styleDatiTabellaDate, notificaEntitaVO.getDataFineValidita(),(short) colonna++);
                  }
                  else
                  {
                    ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                  }
                  String utenteChiusuraParticella = "";
                  if(Validator.isNotEmpty(notificaEntitaVO.getNoteChiusuraEntita()))
                  {
                    utenteChiusuraParticella = ProfileUtils.setFieldUltimaModificaByRuoloNoHtmpl(ruoloUtenza,null,notificaEntitaVO.getDenUtente(),
                      notificaEntitaVO.getDenEnteUtente(), null);
                  }
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, utenteChiusuraParticella, (short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft, notificaEntitaVO.getNoteChiusuraEntita(), (short) colonna++);
                                  
                }
                else
                {
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                  ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
                }
                
                
                nRiga++;
              }
            } //for conduzioni
          }// for entita
          
           
            
            
            
          
          
            
         
          
          
        } //for notifica 
      }
      


      OutputStream out = response.getOutputStream();

      String fileName="ElencoNotificheParticelle";
      
      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelElencoNotificheParticelleServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelElencoNotificheParticelleServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


  

}

package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.comune.AmmCompetenzaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
public class ExcelElencoSociServlet extends ExcelServlet
{

  

  

  /**
   * 
   */
  private static final long serialVersionUID = -5343533829693554730L;
  
  


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
      SolmrLogger.debug(this, " - ExcelElencoSociServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      long[] elencoIdAziendeCollegateExcel = (long[])request.getSession().getAttribute("elencoIdAziendeCollegateExcel");
      ArrayList<?> arAziendeLink = (ArrayList<?>)request.getSession().getAttribute("elencoAziendeLink");
      //String storico = (String)request.getSession().getAttribute("flagStoricoAzColl");
      
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
      
      RigaRicercaAziendeCollegateVO rigaRicercaAziendeCollegateVO[] = gaaFacadeClient
        .getRigheRicercaAziendeCollegateByIdAziendaCollegata(elencoIdAziendeCollegateExcel);
      
      
      
      //Cerco la delega
      long[] idsAssociate = null;
      Vector<Long> idsAssociateLg = null;
      HashMap<Long,DelegaVO> hDelega = null;
      boolean flagSenzaDelega = false;
      Vector<Long> idUtenteLg = null;
      long[] arrIdUtente = null;
      HashMap<Long,RuoloUtenza> hRuoloUtenza = new HashMap<Long,RuoloUtenza>();
      AmmCompetenzaVO[] arrAmmCompetenza = null;
      HashMap<String,AmmCompetenzaVO> hAmmCompetenza = new HashMap<String,AmmCompetenzaVO>();          
      for(int i=0;i<rigaRicercaAziendeCollegateVO.length;i++)
      {
        if(rigaRicercaAziendeCollegateVO[i].getIdAziendaAssociata() != null)
        {
          if(idsAssociateLg == null)
          {
            idsAssociateLg = new Vector<Long>();
          }
          
          if(idUtenteLg == null)
          {
            idUtenteLg = new Vector<Long>();
          }
          
          if(!idUtenteLg.contains(rigaRicercaAziendeCollegateVO[i].getIdUtenteAggiornamento()))
          {
            idUtenteLg.add(rigaRicercaAziendeCollegateVO[i].getIdUtenteAggiornamento());
          }
          
          idsAssociateLg.add(rigaRicercaAziendeCollegateVO[i].getIdAziendaAssociata());
        }        
      }
      
      if(idsAssociateLg !=null)
      {
        idsAssociate = new long[idsAssociateLg.size()];
        for(int i=0;i<idsAssociateLg.size();i++)
        {
          idsAssociate[i] = ((Long)idsAssociateLg.get(i)).longValue();
        }
      }
      
      if(idUtenteLg !=null)
      {
        arrIdUtente = new long[idUtenteLg.size()];
        for(int i=0;i<idUtenteLg.size();i++)
        {
          arrIdUtente[i] = idUtenteLg.get(i).longValue();
        }
      }
      
      if(idsAssociate != null)
      {
        hDelega = gaaFacadeClient.getDelegaAndIntermediario(idsAssociate);
      }
      
      //Controllo se almeno una delle aziende associate non ha la delega
      if(hDelega != null)
      {
        for(int i=0;i<rigaRicercaAziendeCollegateVO.length;i++)
        {
          if(rigaRicercaAziendeCollegateVO[i].getIdAziendaAssociata() != null)
          {
            if(hDelega.get(rigaRicercaAziendeCollegateVO[i].getIdAziendaAssociata()) == null)
            {
              flagSenzaDelega = true;
              break;
            }
          }
        }
      }
      else
      {
        flagSenzaDelega = true;
      }
      
      
      if(flagSenzaDelega)
      {
        UtenteAbilitazioni[] arrUtenteAbilitazioni = anagFacadeClient.getUtenteAbilitazioniByIdUtenteLoginRange(arrIdUtente);
        if(arrUtenteAbilitazioni != null)
        {
          for(int i=0;i<arrUtenteAbilitazioni.length;i++)   
          {  
            RuoloUtenza ruoloUtenza = new RuoloUtenzaPapua(arrUtenteAbilitazioni[i]);
            hRuoloUtenza.put(arrUtenteAbilitazioni[i].getIdUtenteLogin(), ruoloUtenza); 
          }
        }
                
        
        arrAmmCompetenza = anagFacadeClient.serviceGetListAmmCompetenza();          
        if(arrAmmCompetenza != null)
        {
          for(int i=0;i<arrAmmCompetenza.length;i++)
          {
            hAmmCompetenza.put(arrAmmCompetenza[i].getCodiceAmministrazione(), arrAmmCompetenza[i]);              
          }            
        }          
      }
      
      
      
      
      //fine cerca Delega
      
      
      
      int MAXCOLUMN = 14;
      final int BEGINCOLUMN = 0;
      int MIDDLECOLUMN = 10;
      

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
      //Setto formato A3
      printSetup.setPaperSize((short)8);


      //Imposto la larghezza delle colonne
      int indiceColonne = 0;
      sheet.setColumnWidth((short)indiceColonne++,(short)3700);
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);
      sheet.setColumnWidth((short)indiceColonne++,(short)3200);
      sheet.setColumnWidth((short)indiceColonne++,(short)3400);
      sheet.setColumnWidth((short)indiceColonne++,(short)3900);
      sheet.setColumnWidth((short)indiceColonne++,(short)1400);
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);
      sheet.setColumnWidth((short)indiceColonne++,(short)1500);
      sheet.setColumnWidth((short)indiceColonne++,(short)1500);
      sheet.setColumnWidth((short)indiceColonne++,(short)3200);
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);
      sheet.setColumnWidth((short)indiceColonne++,(short)3700);

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

      //Unisco le celle dove inserisco Elenco soci
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
      
      //Unisco le celle dove inserisco il valore del link dellì'alberatura aziende
      ++rigaCorrente;
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente);
      region.setColumnFrom((short)BEGINCOLUMN);
      region.setColumnTo((short)MAXCOLUMN);
      sheet.addMergedRegion(region);

      ++rigaCorrente;

      ++rigaCorrente;
      

      //Unisco le celle della prima riga della tabella con quelle della seconda riga
      for (int i=0;i<3;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le celle della sede legale
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)3);
      region.setColumnTo((short)5);
      sheet.addMergedRegion(region);
      
      
      for (int i=3;i<6;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+2);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      for (int i=6;i<MIDDLECOLUMN;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      
      //Unisco le celle della sede legale
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)MIDDLECOLUMN);
      region.setColumnTo((short)(MIDDLECOLUMN+1));
      sheet.addMergedRegion(region);
      
      for (int i=MIDDLECOLUMN;i<MIDDLECOLUMN+2;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      for (int i=MIDDLECOLUMN+2;i<MAXCOLUMN+1;i++)
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
      ExcelServlet.buildCell(row, styleHeader,""+anagAziendaVO.getLabelElencoAssociati().toUpperCase()+" AGGIORNATO AL "+DateUtils.getCurrentDateString(),(short) BEGINCOLUMN);

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
      
      String linkLista = "";
      if(arAziendeLink !=null)
      {
        for(int i=0;i<arAziendeLink.size();i++)
        {
          CodeDescription code = (CodeDescription)arAziendeLink.get(i);
          linkLista += code.getCodeFlag()+" - "+code.getDescription()+";";
        }
      }
      
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader, linkLista, (short) BEGINCOLUMN);
      

      

      

      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"CUAA",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Partita IVA",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Denominazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sede legale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data\ningresso",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data\nuscita",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data inizio\nvalidità",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data fine\nvalidità",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sup. (ha) ",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data Ultima\nValidazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Detentore\nFascicolo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Ultimo\naggiornamento",(short) colonna++);

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
      
      row=sheet.createRow(nRiga++);
      colonna=0;
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Cond.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"SAU",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      //Creo e popolo contenuto della tabella
      int size = rigaRicercaAziendeCollegateVO.length;
      for (int i=0;i<size;i++)
      {
        row=sheet.createRow(nRiga++);
        RigaRicercaAziendeCollegateVO rigaAzzColl = rigaRicercaAziendeCollegateVO[i];
        colonna=0;
        
        String cuaa = null;
        String partitaIva = null;
        String denominazione = null;
        String comuneElenco = null;
        String indirizzoElenco = null;
        String capElenco = null;
        if(rigaAzzColl.getIdSoggettoAssociato() != null)
        {
          cuaa = rigaAzzColl.getSoggettoAssociato().getCuaa();
          partitaIva = rigaAzzColl.getSoggettoAssociato().getPartitaIva();
          denominazione = rigaAzzColl.getSoggettoAssociato().getDenominazione();
          comuneElenco = rigaAzzColl.getSoggettoAssociato().getDenominazioneComune() +" ("+rigaAzzColl.getSoggettoAssociato().getSglProv()+") ";
          indirizzoElenco = rigaAzzColl.getSoggettoAssociato().getIndirizzo();
          capElenco = rigaAzzColl.getSoggettoAssociato().getCap();
        }
        else
        {
          cuaa = rigaAzzColl.getCuaa();
          partitaIva = rigaAzzColl.getPartitaIva();
          denominazione = rigaAzzColl.getNomeAzienda();
          if(Validator.isNotEmpty(rigaAzzColl.getSedeCittaEstero()))
          {
            comuneElenco = rigaAzzColl.getSedeCittaEstero();
            indirizzoElenco = rigaAzzColl.getIndirizzo();
          }
          else
          {
            comuneElenco = rigaAzzColl.getComune() +" ("+rigaAzzColl.getSglProvincia()+") ";
            indirizzoElenco = rigaAzzColl.getIndirizzo();
            capElenco = rigaAzzColl.getCap();
          }
        }
        
        if(cuaa !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,cuaa,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(partitaIva != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,partitaIva,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(denominazione != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,denominazione,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(comuneElenco != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,comuneElenco,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(indirizzoElenco != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,indirizzoElenco,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(capElenco != null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,capElenco,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(rigaAzzColl.getDataIngresso() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,rigaAzzColl.getDataIngresso(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(rigaAzzColl.getDataUscita() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, rigaAzzColl.getDataUscita(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(rigaAzzColl.getDataInizioValidita() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, rigaAzzColl.getDataInizioValidita(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        
        if(rigaAzzColl.getDataFineValidita() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, rigaAzzColl.getDataFineValidita(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(rigaAzzColl.getSupCondotta() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, rigaAzzColl.getSupCondotta().doubleValue(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(rigaAzzColl.getSupSAU() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDouble, rigaAzzColl.getSupSAU().doubleValue(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(rigaAzzColl.getDataValidazione() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDateTime, rigaAzzColl.getDataValidazione(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if((hDelega !=null) && (hDelega.get(rigaAzzColl.getIdAziendaAssociata()) != null))
        {
          DelegaVO delegaVO = (DelegaVO)hDelega.get(rigaAzzColl.getIdAziendaAssociata());
          
          String detentoreFascicolo ="";
          if(delegaVO.getCodiceAgea() != null)
          {            
            detentoreFascicolo += StringUtils.parseCodiceAgea(delegaVO.getCodiceAgea())+"\n";
          }
          detentoreFascicolo += delegaVO.getDenomIntermediario();
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, detentoreFascicolo,(short) colonna++);
        }
        else
        {
          if((rigaAzzColl.getIdUtenteValidazione() != null) 
            && (hRuoloUtenza.get(rigaAzzColl.getIdUtenteValidazione()) != null))
          {
            RuoloUtenza ruoloUt = (RuoloUtenza)hRuoloUtenza.get(rigaAzzColl.getIdUtenteValidazione());
            if(ruoloUt.isUtentePA())
            {
              AmmCompetenzaVO ammComVO = (AmmCompetenzaVO)hAmmCompetenza.get(ruoloUt.getCodiceEnte());
              ExcelServlet.buildCell(row, styleDatiTabellaLeft, ammComVO.getDescrizione(),(short) colonna++);
            }
            else
            {
              ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            }
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);            
          }
        }
        
        String ultimoAggiornamento = "";
        ultimoAggiornamento += DateUtils.formatDateNotNull(rigaAzzColl.getDataAggiornamento());
        
        if(rigaAzzColl.getDescrizioneUtenteModifica() !=null)
        {
          ultimoAggiornamento += " - "+rigaAzzColl.getDescrizioneUtenteModifica();
        }
        
        if(rigaAzzColl.getDescrizioneEnteUtenteModifica() !=null)
        {
          ultimoAggiornamento += " - "+rigaAzzColl.getDescrizioneEnteUtenteModifica();
        }
        
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,ultimoAggiornamento,(short) colonna++);
        
        
      }     
      


      OutputStream out = response.getOutputStream();

      //String fileName=anagAziendaVO.getCUAA()+"_ElencoSoci";
      String fileName=anagAziendaVO.getCUAA()+"_"+anagAziendaVO.getLabelElencoAssociati().replace(" ", "_");
      
      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelElencoSociServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelElencoSociServlet Exception  "+ex.toString());
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

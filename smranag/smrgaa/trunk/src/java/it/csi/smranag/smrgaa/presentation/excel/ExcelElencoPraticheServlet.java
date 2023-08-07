package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ProcedimentoAziendaVO;
import it.csi.solmr.dto.anag.TipoProcedimentoVO;
import it.csi.solmr.dto.comune.AmmCompetenzaVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
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
 * Classe necessario alla generazione dei file excel dei diritti vitati
 * <p>Title: Smrgaa</p>
 * <p>Description: Classe di utilità per la generazione di file excel</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */
public class ExcelElencoPraticheServlet extends ExcelServlet
{

  

  /**
   * 
   */
  private static final long serialVersionUID = -7007835513860661897L;

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
      SolmrLogger.debug(this, " - ExcelElencoPraticheServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      String procedimento = request.getParameter("procedimento");
      String idAnno = request.getParameter("idAnno");
      String idAzienda = request.getParameter("cuaa");
      Long idAziendaPratiche = (Long)request.getSession().getAttribute("idAziendaPratiche");
      
      AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
      
      Long idAziendaSelezionata = null;
      if(Validator.isNotEmpty(idAzienda))
      {
        idAziendaSelezionata = new Long(idAzienda);
      }
      
      Long idProcedimento = null;
      if(Validator.isNotEmpty(procedimento))
      {
        idProcedimento = new Long(procedimento);
      }
      
      Long lgIdAnno = null;
      if(Validator.isNotEmpty(idAnno))
      {
        lgIdAnno = new Long(idAnno);
      }
      
      //Aggiunto Luca
      if(idAziendaPratiche == null)
      {
        idAziendaPratiche = anagAziendaVO.getIdAzienda();
      }
      
      Vector<ProcedimentoAziendaVO> elencoPratiche = anagFacadeClient.getElencoProcedimentiByIdAzienda(
        idAziendaPratiche, lgIdAnno, idProcedimento, idAziendaSelezionata);
      
      // Richiamo il servizio di comune per estrarre l'elenco delle amministrazioni
      // di competenza
      AmmCompetenzaVO[] elenco = anagFacadeClient.serviceGetListAmmCompetenza();
      Hashtable<String,AmmCompetenzaVO> elencoAmmCompetenza = null;
      if(elenco != null && elenco.length > 0) 
      {
        elencoAmmCompetenza = new Hashtable<String,AmmCompetenzaVO>();
        for(int i = 0; i < elenco.length; i++) 
        {
          AmmCompetenzaVO ammCompetenzaVO = (AmmCompetenzaVO)elenco[i];
          elencoAmmCompetenza.put(ammCompetenzaVO.getIdAmmCompetenza(), ammCompetenzaVO);
        }
      }
      
      
      
      int MAXCOLUMN = 6;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)5000);  //Procedimento
      sheet.setColumnWidth((short)indiceColonne++,(short)2100);  //Anno
      sheet.setColumnWidth((short)indiceColonne++,(short)4000);  //Pratica/Numero
      sheet.setColumnWidth((short)indiceColonne++,(short)7000);  //Pratica/Descrizione
      sheet.setColumnWidth((short)indiceColonne++,(short)4000);  //Pratica/Stato
      sheet.setColumnWidth((short)indiceColonne++,(short)2200);  //Pratica/Dal
      sheet.setColumnWidth((short)indiceColonne++,(short)5000);  //Amministrazione di competenza

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
      ++rigaCorrente;
      
      
      //Unisco le righe prima di pratica
      for(int i=0;i<2;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+2));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le righe/colonne della Pratica
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)2);
      region.setColumnTo((short)5);
      sheet.addMergedRegion(region);
      
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+2));
      region.setColumnFrom( (short) 6);
      region.setColumnTo( (short) 6);
      sheet.addMergedRegion(region);


      
      



      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"ELENCO PRATICHE AGGIORNATO AL "+DateUtils.getCurrentDateString(),(short) BEGINCOLUMN);

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
      if(anagAziendaVO.getSedelegEstero() == null || anagAziendaVO.getSedelegEstero().equals("")) 
      {
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
     


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Procedimento",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Anno",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Pratica",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Amministrazione\ndi competenza",(short) colonna++);
    
      
      row=sheet.createRow(nRiga++);
      colonna=0;
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Numero",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Descrizione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Stato",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Dal",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      
      
      
      
      
      //Creo e popolo contenuto della tabella      
      int size = elencoPratiche.size();
      for (int i=0;i<size;i++)
      {
        row=sheet.createRow(nRiga++);
        ProcedimentoAziendaVO procedimentoAziendaVO = elencoPratiche.get(i);
        TipoProcedimentoVO tipoProcedimentoVO = procedimentoAziendaVO.getTipoProcedimentoVO();
        colonna=0;
        
        ExcelServlet.buildCell(row, styleDatiTabellaLeft, tipoProcedimentoVO.getDescrizioneEstesa(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter, procedimentoAziendaVO.getAnnoCampagna(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft, procedimentoAziendaVO.getNumeroPratica(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft, procedimentoAziendaVO.getDescrizione(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft, procedimentoAziendaVO.getDescrizioneStato(),(short) colonna++);
        if(Validator.isNotEmpty(procedimentoAziendaVO.getDataValiditaStato()))
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, procedimentoAziendaVO.getDataValiditaStato(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate,"",(short) colonna++);
        }
        
        String ammCompetenza = "";
        if(procedimentoAziendaVO.getExIdAmmCompetenza() != null) 
        {
          AmmCompetenzaVO ammCompetenzaVO = (AmmCompetenzaVO)elencoAmmCompetenza.get(procedimentoAziendaVO.getExIdAmmCompetenza().toString());
          if(ammCompetenzaVO !=null)
          {
            ammCompetenza = ammCompetenzaVO.getDescrizione();
          }
        }
        ExcelServlet.buildCell(row, styleDatiTabellaLeft, ammCompetenza,(short) colonna++);
        
        
      }     
      


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_ElencoPratiche";
      
      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelElencoPraticheServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelElencoPraticheServlet Exception  "+ex.toString());
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

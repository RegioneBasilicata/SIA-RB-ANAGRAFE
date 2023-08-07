package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.BooleanVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaUmaExcelVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
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
public class ExcelElencoSociUmaServlet extends ExcelServlet
{

  


  /**
   * 
   */
  private static final long serialVersionUID = 1421551613511022270L;

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
      SolmrLogger.debug(this, " - ExcelElencoSociUmaServlet - INIZIO PAGINA ");
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      
      
      //Supponendo che al max ogni azienda possa avere nn più di 10 tipi di utilizzo 
      int NUMMAX = 3500;
      
      //Creazione cartella excel
      HSSFWorkbook workBook=new HSSFWorkbook();
      
      //Racoglie tuti gli stili dei caratteri
      HashMap<String, HSSFCellStyle> hStyle = new HashMap<String, HSSFCellStyle>();
      //creazione stili caratteri      
      createStyle(workBook, hStyle);      
      
      
      
      Vector<AziendaUmaExcelVO> vAziendeUma = gaaFacadeClient.getScaricoExcelElencoSociUma(
          anagAziendaVO.getIdAzienda().longValue());
      
      
      
      BooleanVO firstSheet = new BooleanVO();
      firstSheet.setValore(true);
      
      //Creazione foglio excel
      if(vAziendeUma.size() > NUMMAX)
      {
        int resto = vAziendeUma.size() % NUMMAX;
        int cicli = vAziendeUma.size() / NUMMAX;
        int begin = 0;
        int end = 0;
        for(int i=0;i<cicli;i++)
        {
          //prendo il resto
          end = begin +(NUMMAX);      
          
          List<AziendaUmaExcelVO> listaParz = vAziendeUma.subList(begin, end);
          
          HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA()+"_"+i);
          
          sheetElenco(sheet, hStyle, anagAziendaVO, listaParz, firstSheet);
          
          begin = end;
        }
        
        if(resto !=0)
        {
          //end = begin + resto - 1;
          end = begin + resto;
          List<AziendaUmaExcelVO> listaParz = vAziendeUma.subList(begin, end);
          
          HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA()+"_"+cicli);
          
          sheetElenco(sheet, hStyle, anagAziendaVO, listaParz, firstSheet);
          
        }
      }
      else
      {
        HSSFSheet sheet=workBook.createSheet(anagAziendaVO.getCUAA());
        
        sheetElenco(sheet, hStyle, anagAziendaVO, vAziendeUma, firstSheet);
      }
      


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_ElencoSociDitteUma";
      
      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelElencoSociUmaServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelElencoSociUmaServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }
  
  
  
  private void sheetElenco(HSSFSheet sheet, HashMap<String, HSSFCellStyle> hStyle, 
      AnagAziendaVO anagAziendaVO, List<AziendaUmaExcelVO> vAziendeUma, BooleanVO firstSheet) throws SolmrException, Exception
  {
    
    int MAXCOLUMN = 17;
    final int BEGINCOLUMN = 0;

    //Imposto i margini di stampa
    sheet.setMargin(HSSFSheet.LeftMargin,0.55);
    sheet.setMargin(HSSFSheet.RightMargin,0.55);

    //Permette di stampare orizzontale
    HSSFPrintSetup printSetup = sheet.getPrintSetup();
    printSetup.setLandscape(true);


    //Imposto la larghezza delle colonne
    int indiceColonne = 0;
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Cuaa
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Partita Iva
    sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Iscrizione CCIAA - Reg.Imprese
    sheet.setColumnWidth((short)indiceColonne++,(short)4000); //Denominazione
    sheet.setColumnWidth((short)indiceColonne++,(short)3500); //SedeLegale/Comune (PV)
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //SedeLegale/Indirizzo
    sheet.setColumnWidth((short)indiceColonne++,(short)1500); //SedeLegale/CAP
    sheet.setColumnWidth((short)indiceColonne++,(short)2300); //Data Ingresso
    //sheet.setColumnWidth((short)indiceColonne++,(short)2300); //Data Uscita
    sheet.setColumnWidth((short)indiceColonne++,(short)2300); //Data inizio validita
    //sheet.setColumnWidth((short)indiceColonne++,(short)2800); //Data fine validita
    sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Ultimo aggiornamento in elenco soci
    sheet.setColumnWidth((short)indiceColonne++,(short)3200); //Data Ultima validazione
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Superficie condotta
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Dati Uma/Prov
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Dati Uma/Nr. Ditta
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Dati Uma/Coltura Uma
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Dati Uma/Superficie
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Dati Uma/Lavorazione da eseguire
    sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Dati Uma/Sup. interessata
    

    HSSFRow row=null;


    Region region = new Region();
    int rigaCorrente = 0;

    if(firstSheet.isValore())
    {
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
      ++rigaCorrente;
    }
    
    
    
    
    //Unisco le celle della prima riga della tabella con quelle della seconda riga
    for (int i=0;i<4;i++)
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
    region.setColumnFrom((short)4);
    region.setColumnTo((short)6);
    sheet.addMergedRegion(region);      
    
    for (int i=7;i<12;i++)
    {
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+2));
      region.setColumnFrom( (short) i);
      region.setColumnTo( (short) i);
      sheet.addMergedRegion(region);
    }
    
    //Unisco le celle dita Uma
    region.setRowFrom(rigaCorrente);
    region.setRowTo(rigaCorrente+1);
    region.setColumnFrom((short)12);
    region.setColumnTo((short)17);
    sheet.addMergedRegion(region);
    





    int nRiga=0;
    
    if(firstSheet.isValore())
    {
      firstSheet.setValore(false);  
    

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, hStyle.get("styleHeader"),"ELENCO SOCI AGGIORNATO AL "+DateUtils.getCurrentDateString(),(short) BEGINCOLUMN);
  
      row=sheet.createRow(nRiga++); //salto riga
  
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, hStyle.get("styleHeader"),"Cuaa:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, hStyle.get("styleHeader"),anagAziendaVO.getCUAA(),(short) 2);
  
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, hStyle.get("styleHeader"),"Denominazione:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, hStyle.get("styleHeader"),anagAziendaVO.getDenominazione(),(short) 2);
      
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
      ExcelServlet.buildCell(row, hStyle.get("styleHeader"),"Indirizzo:",(short) BEGINCOLUMN);
      String indirizzo = getIndirizzo(anagAziendaVO.getSedelegIndirizzo(),
          cap, comune,  sedeLegale);
      ExcelServlet.buildCell(row, hStyle.get("styleHeader"),indirizzo,(short) 2);
      
      
      
      //row=sheet.createRow(nRiga++);
      row=sheet.createRow(nRiga++); //salto riga
    }


    //Creo e popolo header della tabella
    row=sheet.createRow(nRiga++);
    int colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"CUAA",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Partita Iva",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Iscrizione\nCCIAA-\nReg.Imprese",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Denominazione",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Sede Legale",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Data\nIngresso",(short) colonna++);
    //ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Data\nUscita",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Data Inizio\nValidita",(short) colonna++);
    //ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Data Fine\nValidita",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Ultimo aggiornamento\nin elenco soci",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Data Ultima\nValidazione",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Superficie\nCondotta",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Dati Uma",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    
    
    row=sheet.createRow(nRiga++);
    colonna=0;
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Comune(PV)",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Indirizzo",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"CAP",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Prov",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Nr. Ditta",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Coltura Uma",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Superficie",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Lavorazione\nda eseguire",(short) colonna++);
    ExcelServlet.buildCell(row, hStyle.get("styleHeaderTable"),"Sup.\ninteressata",(short) colonna++);
    

    
    //Creo e popolo contenuto della tabella
    int size = vAziendeUma.size();
    for (int i=0;i<size;i++)
    {
      AziendaUmaExcelVO aziendaUmaExcelVO = vAziendeUma.get(i);
      boolean first = true;
      int numColture = 1;
      if(aziendaUmaExcelVO.getColturaUma() != null)
      {
        numColture = aziendaUmaExcelVO.getColturaUma().size();
      }
      for(int j=0;j<numColture;j++)
      {
        colonna = 0;
        row=sheet.createRow(nRiga);
        int ampiezzaRiga = numColture;
        if(first)
        {
          first = false;
          for (int k=0;k<14;k++)
          {
            region.setRowFrom(nRiga);
            region.setRowTo((nRiga+ampiezzaRiga-1));
            region.setColumnFrom( (short) k);
            region.setColumnTo( (short) k);
            sheet.addMergedRegion(region);
          }
          
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getCuaa(),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getPartitaIva(),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getNumeroRegistroImprese(),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getDenominazione(),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getComune(),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getIndirizzo(),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getCap(),(short) colonna++);
          if(aziendaUmaExcelVO.getDataIngresso() !=null)
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDate"), aziendaUmaExcelVO.getDataIngresso() ,(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          }
          
          if(aziendaUmaExcelVO.getDataInizioValidita() !=null)
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDate"), aziendaUmaExcelVO.getDataInizioValidita() ,(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          }
          
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getDatiUltimoAggiornamento(),(short) colonna++);
          if(aziendaUmaExcelVO.getDataUltimaValidazione() !=null)
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDateTime"), aziendaUmaExcelVO.getDataUltimaValidazione() ,(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          }
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"),
              Formatter.formatDouble4Separator(aziendaUmaExcelVO.getSuperficieCondotta()),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), aziendaUmaExcelVO.getProvDittaUma(),(short) colonna++);
          if(aziendaUmaExcelVO.getNumDittaUma() != null)
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaCenter"), aziendaUmaExcelVO.getNumDittaUma(),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          }
                    
        }
        else
        {
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
        }
        
        if(aziendaUmaExcelVO.getColturaUma() != null)
        {
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"), aziendaUmaExcelVO.getColturaUma().get(j)
              .getColturaUma(),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaDouble"), Formatter.formatDouble4Separator(
              aziendaUmaExcelVO.getColturaUma().get(j).getSuperficieUma()),(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
          ExcelServlet.buildCell(row, hStyle.get("styleDatiTabellaLeft"),"",(short) colonna++);
        }
        
        nRiga++;
        
      }
      
      
      
      
    }     
    
  }
  
  
  
  private void createStyle(HSSFWorkbook workBook, HashMap<String, HSSFCellStyle> hStyleTmp)
  {
    //Creazione font di tipo bold
    HSSFFont fontBold=workBook.createFont();
    fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    fontBold.setFontHeight((short)140);

    HSSFFont fontNormal=workBook.createFont();
    fontNormal.setFontHeight((short)140);


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

    //Creazione stile usato dall'header (contenuti)
    HSSFCellStyle styleDati=workBook.createCellStyle();
    styleDati.setFont(fontNormal);
    hStyleTmp.put("styleDati", styleDati);



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
    hStyleTmp.put("styleDatiTabellaLeft", styleDatiTabellaLeft);

    HSSFCellStyle styleDatiTabellaCenter=workBook.createCellStyle();
    styleDatiTabellaCenter.setFont(fontNormal);
    styleDatiTabellaCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
    styleDatiTabellaCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    styleDatiTabellaCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    styleDatiTabellaCenter.setWrapText(true);
    hStyleTmp.put("styleDatiTabellaCenter", styleDatiTabellaCenter);


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
    hStyleTmp.put("styleDatiTabellaDate", styleDatiTabellaDate);
    
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
    hStyleTmp.put("styleDatiTabellaDateTime", styleDatiTabellaDateTime);
    
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
    hStyleTmp.put("styleDatiTabellaDouble", styleDatiTabellaDouble);
    
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

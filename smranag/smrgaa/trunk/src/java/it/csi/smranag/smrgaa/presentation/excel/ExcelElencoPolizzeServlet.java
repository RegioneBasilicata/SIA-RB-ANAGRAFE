package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.polizze.PolizzaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;
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
public class ExcelElencoPolizzeServlet extends ExcelServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 7419693050202513323L;


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
      SolmrLogger.debug(this, " - ExcelElencoPolizzeServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      
      
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      
      Vector<PolizzaVO> vPolizzaVO = gaaFacadeClient.getElencoPolizze(
          anagAziendaVO.getIdAzienda().longValue(), null, null);
      
      
      
      int MAXCOLUMN = 13;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)2100);  //Anno campagna
      sheet.setColumnWidth((short)indiceColonne++,(short)3500);  // numero polizza
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);  //cod intervento
      sheet.setColumnWidth((short)indiceColonne++,(short)7000);  //Macrouso
      sheet.setColumnWidth((short)indiceColonne++,(short)3500);  //consorzio di difesa
      sheet.setColumnWidth((short)indiceColonne++,(short)2200);  //Valore assicurato
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);  //importo premio
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);  //importo pagato
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);  //Valore risarcito
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);  //Data stipulazione
      sheet.setColumnWidth((short)indiceColonne++,(short)2000);  //Data quietanza
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);  //Periodo di riferimento
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);  //Polizza integrativa
      sheet.setColumnWidth((short)indiceColonne++,(short)2500);  //Polizza aggiuntiva
      

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
      





      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"ELENCO POLIZZE AGGIORNATO AL "+DateUtils.getCurrentDateString(),(short) BEGINCOLUMN);

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

      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Anno\ncampagna",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Numero polizza",(short) colonna++);      
      ExcelServlet.buildCell(row, styleHeaderTable,"Cod\nintervento",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Macrouso",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Consorzio\ndi difesa",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Valore \nassicurato",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Importo \npremio",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Importo \npagato",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Valore \nrisarcito",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data \nstipulazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data \nquietanza",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Periodo di\nriferimento",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Polizza\nintegrativa",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Polizza\naggiuntiva",(short) colonna++);

      
      //Creo e popolo contenuto della tabella
      TreeMap<String, String> tLegenda = new TreeMap<String, String>();
      int size = vPolizzaVO.size();
      for (int i=0;i<size;i++)
      {
        row=sheet.createRow(nRiga++);
        PolizzaVO polizzaVO = vPolizzaVO.get(i);
        colonna=0;        
        
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,polizzaVO.getAnnoCampagna(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,polizzaVO.getNumeroPolizza(),(short) colonna++);                
        if(tLegenda.get(polizzaVO.getCodiceIntervento()) ==null)
        {
          tLegenda.put(polizzaVO.getCodiceIntervento(), polizzaVO.getDescrizioneIntervento());
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,polizzaVO.getCodiceIntervento(),(short) colonna++);
        
        String descMacrouso = "";
        if(Validator.isNotEmpty(polizzaVO.getvMacroUso()))
        {
          for(int j=0;j<polizzaVO.getvMacroUso().size();j++)
          {
            descMacrouso += polizzaVO.getvMacroUso().get(j);
            if(j!=0)
            {
              descMacrouso += "\n";
            }
          }
        }
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,descMacrouso,(short) colonna++);
        
        if(polizzaVO.getCodiceConsorzio() != null)
        {
          String consorzio = "["+polizzaVO.getCodiceConsorzio()+"]"
            +" "+polizzaVO.getDescrizioneConsorzio();        
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,consorzio,(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        ExcelServlet.buildCell(row, styleDatiTabellaDouble,
            Formatter.formatDouble2Separator(polizzaVO.getValoreAssicurato()),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaDouble,
            Formatter.formatDouble2Separator(polizzaVO.getImportoPremio()),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaDouble,
            Formatter.formatDouble2Separator(polizzaVO.getImportoPagato()),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaDouble,
            Formatter.formatDouble2Separator(polizzaVO.getValoreRisarcito()),(short) colonna++);
                
        if(polizzaVO.getDataPolizza() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, polizzaVO.getDataPolizza(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }
        
        if(polizzaVO.getDataQuietanza() !=null)
        {
          ExcelServlet.buildCell(row, styleDatiTabellaDate, polizzaVO.getDataQuietanza(),(short) colonna++);
        }
        else
        {
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
        }        
        
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,polizzaVO.getDescrizionePeriodo(),(short) colonna++);
        
        String polizzaIntegrativa = "No";
        if("S".equalsIgnoreCase(polizzaVO.getPolizzaIntegrativa()))
        {
          polizzaIntegrativa = "Si";
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,polizzaIntegrativa,(short) colonna++);
        String aggiuntiva = "";
        if("S".equalsIgnoreCase(polizzaVO.getAggiuntiva()))
        {
          aggiuntiva = "Si";
        }
        else if("N".equalsIgnoreCase(polizzaVO.getAggiuntiva()))
        {
          aggiuntiva = "No";
        }
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,aggiuntiva,(short) colonna++);
        
      }
     
      if(tLegenda != null)
      {
        row=sheet.createRow(nRiga++);
        for(Map.Entry<String,String> entry : tLegenda.entrySet()) 
        {
          region.setRowFrom(nRiga);
          region.setRowTo(nRiga);
          region.setColumnFrom((short)BEGINCOLUMN);
          region.setColumnTo((short)MAXCOLUMN);
          sheet.addMergedRegion(region);
          row=sheet.createRow(nRiga++);
          String key = entry.getKey();
          String value = entry.getValue();
          String interventoElenco = "["+key+"]"+" "+value;
          ExcelServlet.buildCell(row, styleHeader,interventoElenco,(short) BEGINCOLUMN);        
        }      
        
      }
      


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_ElencoPolizze";
      
      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelElencoPolizzeServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelElencoPolizzeServlet Exception  "+ex.toString());
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

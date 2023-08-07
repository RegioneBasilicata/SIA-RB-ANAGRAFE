package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.sigop.dto.services.SchedaCreditoVO;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

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
public class ExcelRegistroDebitoriServlet extends ExcelServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 1931820306892241946L;


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
      SolmrLogger.debug(this, " - ExcelRegistroDebitoriServlet - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      
      SchedaCreditoVO[] schedeCreditoElenco = (SchedaCreditoVO[])request.getAttribute("schedeCredito");
      
      
      
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
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Numero scheda
      sheet.setColumnWidth((short)indiceColonne++,(short)2100); //Fondo
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Tipo Debito
      sheet.setColumnWidth((short)indiceColonne++,(short)2800); //Stato Scheda
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Data inizio debito
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //Presenza garanzia
      sheet.setColumnWidth((short)indiceColonne++,(short)2300); //Importo debito
      sheet.setColumnWidth((short)indiceColonne++,(short)2300); //Importo già recuperato
      sheet.setColumnWidth((short)indiceColonne++,(short)2300); //Importo da recuperare
      sheet.setColumnWidth((short)indiceColonne++,(short)2800); //Regolamento trasgredito
      sheet.setColumnWidth((short)indiceColonne++,(short)2800); //Intervento trasgredito
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Campagna
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Numero domanda
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Data domanda

      HSSFRow row=null;
      //HSSFRow rowFirst=null;

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
      ExcelServlet.buildCell(row, styleHeader,"STAMPA REGISTRO DEBITORI ARPEA AGGIORNATO AL "+DateUtils.getCurrentDateString(),(short) BEGINCOLUMN);

      row=sheet.createRow(nRiga++); //salto riga

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Cuaa:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getCUAA(),(short) 2);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Denominazione:",(short) BEGINCOLUMN);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getDenominazione(),(short) 2);
      
      row=sheet.createRow(nRiga++);
      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Numero\nscheda",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Fondo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Tipo debito",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Stato scheda",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data inizio\ndebito",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Pres.\ngaran.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Imp.\ndebito\n(€)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Imp. già\nrecuperato\n(€)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Imp. da\nrecuperare\n(€)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Reg.\ntrasgr.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Int.\ntrasgr.",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Campagna",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Numero domanda",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data\ndomanda",(short) colonna++);

      
      //Creo e popolo contenuto della tabella
      int size = schedeCreditoElenco.length;
      for (int i=0;i<size;i++)
      {
        SchedaCreditoVO schedaCredito = schedeCreditoElenco[i];
        boolean first = true;
        for(int j=0;j<schedaCredito.getElencoDisposizioniTrasgredite().length;j++)
        {
          colonna = 0;
          row=sheet.createRow(nRiga);
          int ampiezzaRiga = schedaCredito.getElencoDisposizioniTrasgredite().length;
          if(first)
          {
            first = false;
            for (int k=0;k<9;k++)
            {
              region.setRowFrom(nRiga);
              region.setRowTo((nRiga+ampiezzaRiga-1));
              region.setColumnFrom( (short) k);
              region.setColumnTo( (short) k);
              sheet.addMergedRegion(region);
            }
            
            String numeroScheda = "";
            if(Validator.isNotEmpty(schedaCredito.getNumeroScheda()))
            {
              numeroScheda = ""+schedaCredito.getNumeroScheda().intValue();
            }      
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, numeroScheda,(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft, schedaCredito.getCodiceFondo(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft, schedaCredito.getTipoDebito(),(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft, schedaCredito.getStatoScheda(),(short) colonna++);
            if(schedaCredito.getDataInizioDebito() !=null)
            {
              ExcelServlet.buildCell(row, styleDatiTabellaDate, schedaCredito.getDataInizioDebito() ,(short) colonna++);
            }
            else
            {
              ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            }
            
            String presenzaGaranzia = "";
            if(Validator.isNotEmpty(schedaCredito.getPresenzaGaranzia()))
            {
              if("S".equalsIgnoreCase(schedaCredito.getPresenzaGaranzia()))
              {
                presenzaGaranzia = "Si";
              }
              else if("N".equalsIgnoreCase(schedaCredito.getPresenzaGaranzia()))
              {
                presenzaGaranzia = "No";
              }
            }
            ExcelServlet.buildCell(row, styleDatiTabellaCenter, presenzaGaranzia,(short) colonna++);
            
            BigDecimal importoDaRecuperare = new BigDecimal(0);
            if(schedaCredito.getImportoDebito() != null)
            {
              importoDaRecuperare = importoDaRecuperare.add(schedaCredito.getImportoDebito());
            }
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,
                Formatter.formatDouble2Separator(schedaCredito.getImportoDebito()),(short) colonna++);
            if(schedaCredito.getImportoRecupero() != null)
            {
              importoDaRecuperare = importoDaRecuperare.subtract(schedaCredito.getImportoRecupero());
            }      
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,
                Formatter.formatDouble2Separator(schedaCredito.getImportoRecupero()),(short) colonna++);
            
            ExcelServlet.buildCell(row, styleDatiTabellaDouble,
                Formatter.formatDouble2Separator(importoDaRecuperare),(short) colonna++);            
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
          }
          
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, schedaCredito
              .getElencoDisposizioniTrasgredite()[j].getRegolamentoTrasgredito(),(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, schedaCredito
              .getElencoDisposizioniTrasgredite()[j].getInterventoTrasgredito(),(short) colonna++);
          String annoCampagna = "";
          if(Validator.isNotEmpty(schedaCredito
            .getElencoDisposizioniTrasgredite()[j].getAnnoCampagna()))
          {
            annoCampagna = ""+schedaCredito
            .getElencoDisposizioniTrasgredite()[j].getAnnoCampagna().intValue();
          }
          ExcelServlet.buildCell(row, styleDatiTabellaCenter, annoCampagna,(short) colonna++);
          ExcelServlet.buildCell(row, styleDatiTabellaLeft, schedaCredito
              .getElencoDisposizioniTrasgredite()[j].getNumeroDomanda(),(short) colonna++);
          
          if(schedaCredito.getElencoDisposizioniTrasgredite()[j].getDataDomanda() !=null)
          {
            ExcelServlet.buildCell(row, styleDatiTabellaDate, 
                schedaCredito.getElencoDisposizioniTrasgredite()[j].getDataDomanda(),(short) colonna++);
          }
          else
          {
            ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);
          }
          
          nRiga++;
          
        }
        
        
        
        
      }     
      


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_RegistroDebitori";
      
      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelElencoPolizzeServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelRegistroDebitoriServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


  

}

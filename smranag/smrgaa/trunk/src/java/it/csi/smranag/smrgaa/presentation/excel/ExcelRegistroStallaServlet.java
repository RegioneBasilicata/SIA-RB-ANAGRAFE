package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.teramo.ElencoRegistroDiStallaVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

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
public class ExcelRegistroStallaServlet extends ExcelServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 5597129770798082870L;


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
      SolmrLogger.debug(this, " - ExcelRegistroStallaServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      ElencoRegistroDiStallaVO elencoRegistroStalla = (ElencoRegistroDiStallaVO) request.getSession().getAttribute("elencoRegistroStalla");
      String descrizioneSpecie=(String)request.getSession().getAttribute("descrizioneSpecie");

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
      sheet.setColumnWidth((short)0,(short)4500);
      sheet.setColumnWidth((short)1,(short)4500);
      sheet.setColumnWidth((short)2,(short)3400);
      sheet.setColumnWidth((short)3,(short)3400);
      sheet.setColumnWidth((short)4,(short)3400);
      sheet.setColumnWidth((short)5,(short)1500);
      sheet.setColumnWidth((short)6,(short)3400);
      sheet.setColumnWidth((short)7,(short)3000);
      sheet.setColumnWidth((short)8,(short)2400);
      sheet.setColumnWidth((short)9,(short)2700);
      sheet.setColumnWidth((short)10,(short)3100);
      sheet.setColumnWidth((short)11,(short)2400);
      sheet.setColumnWidth((short)12,(short)2700);
      sheet.setColumnWidth((short)13,(short)3100);
      sheet.setColumnWidth((short)14,(short)2900);
      sheet.setColumnWidth((short)15,(short)2150);

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


      Region region =new Region();

      //Unisco le celle dove inserisco REGISTRO STALLA
      region.setRowFrom(0);
      region.setRowTo(0);
      region.setColumnFrom((short)0);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);


      //Unisco le celle dove inserisco il valore del CUAA
      region.setRowFrom(2);
      region.setRowTo(2);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);
      //Unisco le celle dove inserisco il valore della denominazione
      region.setRowFrom(3);
      region.setRowTo(3);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);
      //Unisco le celle dove inserisco il valore della Codice allevamento
      region.setRowFrom(4);
      region.setRowTo(4);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);
      //Unisco le celle dove inserisco il valore dell' Specie
      region.setRowFrom(5);
      region.setRowTo(5);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);

      //Unisco le celle dove inserisco il valore dell' Indirizzo
      region.setRowFrom(6);
      region.setRowTo(6);
      region.setColumnFrom((short)1);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);


      //Unisco le celle dell' Ingresso
      region.setRowFrom(8);
      region.setRowTo(8);
      region.setColumnFrom((short)8);
      region.setColumnTo((short)10);
      sheet.addMergedRegion(region);

      //Unisco le celle dell' Uscita
      region.setRowFrom(8);
      region.setRowTo(8);
      region.setColumnFrom((short)11);
      region.setColumnTo((short)13);
      sheet.addMergedRegion(region);

      //Unisco le celle della prima riga della tabella con quelle della seconda riga
      for (int i=0;i<8;i++)
      {
        region.setRowFrom(8);
        region.setRowTo(10);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      for (int i=8;i<14;i++)
      {
        region.setRowFrom(9);
        region.setRowTo(10);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }

      region.setRowFrom(8);
      region.setRowTo(10);
      region.setColumnFrom( (short) 14);
      region.setColumnTo( (short) 14);
      sheet.addMergedRegion(region);

      region.setRowFrom(8);
      region.setRowTo(10);
      region.setColumnFrom( (short) 15);
      region.setColumnTo( (short) 15);
      sheet.addMergedRegion(region);






      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"REGISTRO STALLA BDN SITUAZIONE AGGIORNATA AL "+DateUtils.getCurrentDateString(),(short) 0);

      row=sheet.createRow(nRiga++); //salto riga

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Cuaa:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getCUAA(),(short) 1);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Denominazione:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,anagAziendaVO.getDenominazione(),(short) 1);


      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Codice allevamento:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,elencoRegistroStalla.getRegistroDiStalla()[0].getAziendaCodice(),(short) 1);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Specie:",(short) 0);
      ExcelServlet.buildCell(row, styleHeader,descrizioneSpecie,(short) 1);

      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"Indirizzo:",(short) 0);

      String indirizzo=getIndirizzo(elencoRegistroStalla.getRegistroDiStalla()[0].getIndirizzo(),
                                    elencoRegistroStalla.getRegistroDiStalla()[0].getCap(),
                                    elencoRegistroStalla.getRegistroDiStalla()[0].getLocalita());

      ExcelServlet.buildCell(row, styleHeader,indirizzo,(short) 1);

      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Proprietario",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Detentore",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Marchio",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Identificativo\nElettronico\n(Tag)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Razza",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Sesso",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Madre",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data nascita",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Ingresso",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Uscita",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Codice Precedente",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Modello 4",(short) colonna++);
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
      ExcelServlet.buildCell(row, styleHeaderTable,"Data",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Causale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Provenienza",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Data",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Causale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Destinazione",(short) colonna++);
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


      //Creo e popolo contenuto della tabella
      int size=elencoRegistroStalla.getRegistroDiStalla().length;
      for (int i=0;i<size;i++)
      {
        row=sheet.createRow(nRiga++);
        colonna=0;
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getPropCognNome(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getDetenCognNome(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getMarchio(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getTag(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getRazza(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaCenter,elencoRegistroStalla.getRegistroDiStalla()[i].getSesso(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getCodiceMadre(),(short) colonna++);

        if (elencoRegistroStalla.getRegistroDiStalla()[i].getDtNascita()!=null)
          ExcelServlet.buildCell(row, styleDatiTabellaDate,DateUtils.parseDate(elencoRegistroStalla.getRegistroDiStalla()[i].getDtNascita()),(short) colonna++);
        else
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);

        if (elencoRegistroStalla.getRegistroDiStalla()[i].getDtIngresso()!=null)
          ExcelServlet.buildCell(row, styleDatiTabellaDate,DateUtils.parseDate(elencoRegistroStalla.getRegistroDiStalla()[i].getDtIngresso()),(short) colonna++);
        else
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);


        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getCaricoNascita(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getProvenienza(),(short) colonna++);

        if (elencoRegistroStalla.getRegistroDiStalla()[i].getDataMorteVendita()!=null)
          ExcelServlet.buildCell(row, styleDatiTabellaDate,DateUtils.parseDate(elencoRegistroStalla.getRegistroDiStalla()[i].getDataMorteVendita()),(short) colonna++);
        else
          ExcelServlet.buildCell(row, styleDatiTabellaLeft,"",(short) colonna++);

        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getScaricoMorte(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getDestinazione(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getCodPrecedente(),(short) colonna++);
        ExcelServlet.buildCell(row, styleDatiTabellaLeft,elencoRegistroStalla.getRegistroDiStalla()[i].getEstremiModello4(),(short) colonna++);
      }


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_RegistroStalla";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelRegistroStallaServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelRegistroStallaServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }


  private String getIndirizzo(String indirizzo, String cap, String localita)
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

    return indirizzoVw;

  }


}

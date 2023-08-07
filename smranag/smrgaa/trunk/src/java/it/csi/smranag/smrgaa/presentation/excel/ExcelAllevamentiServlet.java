package it.csi.smranag.smrgaa.presentation.excel;

import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

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
public class ExcelAllevamentiServlet extends ExcelServlet
{

 
  
  


  /**
   * 
   */
  private static final long serialVersionUID = -5892158174529073863L;
  
  


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
      SolmrLogger.debug(this, " - ExcelAllevamentiServlet.jsp - INIZIO PAGINA ");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute(("anagAziendaVO"));
      @SuppressWarnings("unchecked")
      Vector<AllevamentoVO> vElencoAllevamenti = (Vector<AllevamentoVO>)request.getAttribute("vAllevamenti");
      
      int MAXCOLUMN = 15;
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
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //unità produttiva
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //Cod azienda zoot.
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //comune
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //indirizzo
      sheet.setColumnWidth((short)indiceColonne++,(short)1500); //cap
      sheet.setColumnWidth((short)indiceColonne++,(short)1800); //Specie
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Categ - SottoCategoria
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //n' Capi in detenzione
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //n' Capi in proprietà
      sheet.setColumnWidth((short)indiceColonne++,(short)2600); //Stabulazione
      sheet.setColumnWidth((short)indiceColonne++,(short)2500); //numero capi stabulati
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Proprietario/Codice Fiscale
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Proprietario/Denominazione
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Detentore/Codice Fiscale
      sheet.setColumnWidth((short)indiceColonne++,(short)3500); //Detentore/Denominazione
      sheet.setColumnWidth((short)indiceColonne++,(short)2000); //Soccida

      HSSFRow row=null;

      //Creazione font di tipo bold
      HSSFFont fontBold=workBook.createFont();
      fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      fontBold.setFontHeight((short)130);

      HSSFFont fontNormal=workBook.createFont();
      fontNormal.setFontHeight((short)130);


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
      
      
      //Formato per visualizzare correttamente le date
      HSSFDataFormat numberFormat = workBook.createDataFormat();
      formato = numberFormat.getFormat("#,##0.0");

      HSSFCellStyle styleDatiTabellaDouble=workBook.createCellStyle();
      styleDatiTabellaDouble.setFont(fontNormal);
      styleDatiTabellaDouble.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaDouble.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaDouble.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaDouble.setDataFormat(formato);
      
      
      formato = numberFormat.getFormat("#,##0");
      HSSFCellStyle styleDatiTabellaNumber=workBook.createCellStyle();
      styleDatiTabellaNumber.setFont(fontNormal);
      styleDatiTabellaNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);
      styleDatiTabellaNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      styleDatiTabellaNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      styleDatiTabellaNumber.setDataFormat(formato);


      Region region = new Region();
      int rigaCorrente = 0;

      //Unisco le celle dove inserisco "Stampa Allevamenti..."
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
      
      //Doppia riga vuota
      ++rigaCorrente;
      ++rigaCorrente;
      

      //Unisco le celle della prima,seconda,terza riga della tabella
      for (int i=0;i<2;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le celle della ubicazione
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)2);
      region.setColumnTo((short)4);
      sheet.addMergedRegion(region);
      
      
      for (int i=2;i<5;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      for (int i=5;i<11;i++)
      {
        region.setRowFrom(rigaCorrente);
        region.setRowTo((rigaCorrente+3));
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le celle del detentore
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)11);
      region.setColumnTo((short)12);
      sheet.addMergedRegion(region);
      
      
      for (int i=11;i<13;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      //Unisco le celle del proprietario
      region.setRowFrom(rigaCorrente);
      region.setRowTo(rigaCorrente+1);
      region.setColumnFrom((short)13);
      region.setColumnTo((short)14);
      sheet.addMergedRegion(region);
      
      
      for (int i=13;i<15;i++)
      {
        region.setRowFrom(rigaCorrente+2);
        region.setRowTo(rigaCorrente+3);
        region.setColumnFrom( (short) i);
        region.setColumnTo( (short) i);
        sheet.addMergedRegion(region);
      }
      
      
           
      region.setRowFrom(rigaCorrente);
      region.setRowTo((rigaCorrente+3));
      region.setColumnFrom( (short) 15);
      region.setColumnTo( (short) 15);
      sheet.addMergedRegion(region);






      int nRiga=0;

      //Creo e popolo l'header
      row=sheet.createRow(nRiga++);
      ExcelServlet.buildCell(row, styleHeader,"STAMPA ALLEVAMENTI AGGIORNATO "+(String)request.getAttribute("pianoRiferimentoEx"),(short) BEGINCOLUMN);
      
      

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

      
      row=sheet.createRow(nRiga++); //salto riga


      //Creo e popolo header della tabella
      row=sheet.createRow(nRiga++);
      int colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"Unità Produttiva",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Codice\nazienda\nzootecnica",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Ubicazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Specie",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Categoria - Sottocategoria",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"n° Capi\nin detenzione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"n° Capi\nin proprietà",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Stabulazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"n° Capi\nstabulati",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Detentore",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Proprietario",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Soccida",(short) colonna++);

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
      
      row=sheet.createRow(nRiga++);
      colonna=0;
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Comune (PV)",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Indirizzo",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Cap",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Codice fiscale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Denominazione",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Codice fiscale",(short) colonna++);
      ExcelServlet.buildCell(row, styleHeaderTable,"Denominazione",(short) colonna++);
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
      if (vElencoAllevamenti!=null)
      {
        for (int i=0;i<vElencoAllevamenti.size();i++)
        {
      
          AllevamentoVO allVO = (AllevamentoVO)vElencoAllevamenti.get(i); 
      
          boolean first = true;
          int numStabulazioni = 1;
          if(allVO.getSottoCategoriaAllevamentoVO().getVStabulazioneTrattamentoVO() != null)
          {
            numStabulazioni = allVO.getSottoCategoriaAllevamentoVO().getVStabulazioneTrattamentoVO().size();
          }
          for(int j=0;j<numStabulazioni;j++)
          {
            colonna = 0;
            row=sheet.createRow(nRiga);
            int ampiezzaRiga = numStabulazioni;
            if(first)
            {
              for (int k=0;k<9;k++)
              {
                region.setRowFrom(nRiga);
                region.setRowTo((nRiga+ampiezzaRiga-1));
                region.setColumnFrom( (short) k);
                region.setColumnTo( (short) k);
                sheet.addMergedRegion(region);
              }
              
              String unitaProduttiva = "";
              unitaProduttiva += allVO.getUteDescom();
              unitaProduttiva += " ("+allVO.getUteSglProv()+") ";
              unitaProduttiva += " - " +allVO.getUteIndirizzo();
              ExcelServlet.buildCell(row, styleDatiTabellaLeft,unitaProduttiva,(short) colonna++);        
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,allVO.getCodiceAziendaZootecnica(),(short) colonna++);
              
              String comuneAll =  allVO.getAllevamentoDescom() + " ("+allVO.getAllevamentoSglProv()+") ";
              ExcelServlet.buildCell(row, styleDatiTabellaLeft, comuneAll,(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaLeft, allVO.getAllevamentoIndirizzo(),(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO.getAllevamentoCap(),(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaLeft, allVO
                  .getTipoSpecieAnimaleVO().getDescrizione(),(short) colonna++);
              
              String catSott = allVO.getTipoCategoriaAnimaleVO().getDescrizione();
              catSott += " - " +allVO.getTipoSottoCategoriaAnimaleVO().getDescrizione();
              ExcelServlet.buildCell(row, styleDatiTabellaLeft, catSott,(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO
                   .getSottoCategoriaAllevamentoVO().getQuantita(),(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO
                  .getSottoCategoriaAllevamentoVO().getQuantitaProprieta(),(short) colonna++);
            }
            else
            {
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
            }         
          
            if(allVO.getSottoCategoriaAllevamentoVO().getVStabulazioneTrattamentoVO() != null)
            {
              StabulazioneTrattamento staTrattVO = allVO.getSottoCategoriaAllevamentoVO()
                .getVStabulazioneTrattamentoVO().get(j);
              ExcelServlet.buildCell(row, styleDatiTabellaLeft,staTrattVO.getDescStabulazione(),(short) colonna++);              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,staTrattVO.getQuantita(),(short) colonna++);
            }
            else
            {
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
            }
              
            if(first)
            {
              first = false;
              
              for (int k=11;k<16;k++)
              {
                region.setRowFrom(nRiga);
                region.setRowTo((nRiga+ampiezzaRiga-1));
                region.setColumnFrom( (short) k);
                region.setColumnTo( (short) k);
                sheet.addMergedRegion(region);
              }
              
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO
                  .getCodFiscDetentore(),(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO
                  .getDenDetentore(),(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO
                  .getCodFiscProprietario(),(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO
                  .getDenProprietario(),(short) colonna++);
              
              ExcelServlet.buildCell(row, styleDatiTabellaCenter, allVO
                  .getSoccida(),(short) colonna++);
            }
            else
            {
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
              ExcelServlet.buildCell(row, styleDatiTabellaCenter,"",(short) colonna++);
            }
            
            nRiga++;
            
          }
          
          
        }
      }
      
      
      


      OutputStream out = response.getOutputStream();

      String fileName=anagAziendaVO.getCUAA()+"_Allevamenti";

      setResponseHeader(response,request,fileName);
      workBook.write(out);
      out.close();

      SolmrLogger.debug(this, " - ExcelAllevamentiServlet.jsp - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "ExcelAllevamentiServlet Exception  "+ex.toString());
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

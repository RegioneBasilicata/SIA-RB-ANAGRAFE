package it.csi.smranag.smrgaa.presentation.pdf;

/**
 * <p>Title: Aziende trasformatrici</p>
 * <p>Description: Servizi on-line per le aziende vinicole di trasformazione</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

import inetsoft.report.PDFPrinter;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.LettRichValidAz;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;

import java.awt.PrintJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class PdfNuovaRichiestaValidazione extends ModularReportPdf
{
    
  
  
  
  public static final String CODICE_STAMPA    = "RICHIESTA_VALIDAZIONE";

  public PdfNuovaRichiestaValidazione() throws Exception
  {
    super();
    this.templateXML = "it/csi/solmr/etc/anag/stampe/Blank.srt";
    this.nomeFilePdf = "stampaNuovaRichiestaValidazione.pdf";
    this.orientamentoStampa = StyleConstants.PAPER_A4;
  }

  protected HashMap<String, Object> stampaPdf(HttpServletRequest request, TabularSheet report,
      AnagFacadeClient anagFacadeclient, GaaFacadeClient gaaFacadeClient)
      throws Exception
  {
    
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    HashMap<String, Object> hmParametri = preload(request, report,
        anagFacadeclient, gaaFacadeClient, ruoloUtenza);
    
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)hmParametri.get("aziendaNuovaVO");
    
    //Da capire s emettere bozza o no!!!
    setBackground(report, aziendaNuovaVO.getIdStatoRichiesta());
    
    
    
    /*String denominazioneAzienda = null;
    if(Validator.isNotEmpty(aziendaNuovaVO.getDenominazione()))
    {
      if (aziendaNuovaVO.getDenominazione().length() > 74)
      {
        denominazioneAzienda = aziendaNuovaVO.getDenominazione().substring(0, 74)
            + " [...]";
      }
      else
      {
        denominazioneAzienda = aziendaNuovaVO.getDenominazione();
      }
    }
    else
    {
      denominazioneAzienda = aziendaNuovaVO.getCognome()+" "+aziendaNuovaVO.getNome();
    }

    report.setElement("txtIntestazioneAzienda", checkNull(aziendaNuovaVO.getCuaa()
        + " - " + denominazioneAzienda));
    
    Date dataStampa = (Date)hmParametri.get("dataStampa");
    report.setElement("TextFooterData", 
        checkNull(DateUtils.formatDateTimeNotNull(dataStampa)));*/
    
    
    processSubReports(report, 1, CODICE_STAMPA,
         anagFacadeclient, gaaFacadeClient, request, hmParametri);


    return hmParametri;
     
  }
  
  
  protected void generaOutputStampa(HttpServletRequest request,
      HttpServletResponse response, TabularSheet report,
      OutputStream outStampa, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> hmParametri) throws Exception
  {
    //Stampa creata ***inzio
    ByteArrayOutputStream outPdf = new ByteArrayOutputStream();    
    
    PDFPrinter pdf = new PDFPrinter(outPdf);
    pdf.setPageSize(orientamentoStampa);
    PrintJob pj = pdf.getPrintJob();
    report.print(pj);
    pdf.close();
    
    
    
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document, outStampa);
    document.open();
    PdfContentByte cb = writer.getDirectContent();
    
    InputStream inPdf = new ByteArrayInputStream(outPdf.toByteArray());
    PdfReader reader = new PdfReader(inPdf);
    for (int i = 1; i <= reader.getNumberOfPages(); i++) 
    {
      document.newPage();
      //import the page from source pdf
      PdfImportedPage page = writer.getImportedPage(reader, i);
      //add the page to the destination pdf
      cb.addTemplate(page, 0, 0);
    }
    //Fine stampa creata *****
    
   
    outStampa.flush();
    document.close();
    outStampa.close();
  }

  private void setBackground(TabularSheet report, Long idStatoRichiesta)
  {
    //bozza solo se nn allo stato trasmessa
    if(idStatoRichiesta.compareTo(SolmrConstants.RICHIESTA_STATO_FIRMA_DIGITALE) < 0)
    {
      
      report.setBackground(new ImageIcon(getClass().getClassLoader()
         .getResource(SolmrConstants.get("IMMAGINE_PDF_BOZZA").toString()))
         .getImage());
      report.setBackgroundLayout(StyleConstants.BACKGROUND_CENTER);
    }
  }

  private HashMap<String, Object> preload(HttpServletRequest request,
      TabularSheet report, AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    HashMap<String, Object> hmParametri = new HashMap<String, Object>();
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagAziendaVO");   
    hmParametri.put("anagAziendaVO", anagAziendaVO);
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
    hmParametri.put("aziendaNuovaVO", aziendaNuovaVO);
    hmParametri.put("dataStampa", new Date());
    hmParametri.put("dataConferma", new Date());
    
    
    return hmParametri;
  }
  
  
  protected void salvaPdf(HttpServletRequest request, ByteArrayOutputStream baos,
      AnagFacadeClient anagFacadeclient,
      GaaFacadeClient gaaFacadeClient) throws Exception
  {
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
    gaaFacadeClient.insertFileStampa(aziendaNuovaVO.getIdRichiestaAzienda().longValue(), baos.toByteArray());
  }

  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new LettRichValidAz());
  }
}
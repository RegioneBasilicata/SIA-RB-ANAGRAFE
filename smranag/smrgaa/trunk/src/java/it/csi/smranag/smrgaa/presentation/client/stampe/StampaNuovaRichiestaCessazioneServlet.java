package it.csi.smranag.smrgaa.presentation.client.stampe;

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
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.LettRichCessAz;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

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

public class StampaNuovaRichiestaCessazioneServlet extends ModularReport
{
    
   /**
   * 
   */
  private static final long serialVersionUID = -6262287497648836211L;
  
  
  
  public static final String CODICE_STAMPA    = "RICHIESTA_CESSAZIONE";

  public StampaNuovaRichiestaCessazioneServlet()
  {
    this.templateXML = "it/csi/solmr/etc/anag/stampe/Blank.srt";
    this.nomeFilePdf = "stampaNuovaRichiestaCessazione.pdf";
    this.orientamentoStampa = StyleConstants.PAPER_A4;
  }

  protected HashMap<String, Object> stampaPdf(HttpServletRequest request, TabularSheet report,
      AnagFacadeClient anagFacadeclient, GaaFacadeClient gaaFacadeClient, RuoloUtenza ruoloUtenza)
      throws Exception
  {
    
    HashMap<String, Object> hmParametri = preload(request, report,
        anagFacadeclient, gaaFacadeClient, ruoloUtenza);
    
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)hmParametri.get("aziendaNuovaVO");
    
    setBackground(report, aziendaNuovaVO.getIdStatoRichiesta(), request);
    
    
    
    
    
    
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

  private void setBackground(TabularSheet report, Long idStatoRichiesta, HttpServletRequest request)
  {
    //permetta di eliminare la scritta bozza nel
    //caso del tasto frima digitale che viene lanciata stampa e memorizzazione db in contemporanea..
    String param = request.getParameter("param");
    //boza solo se nn allo stato trasmessa
    if((idStatoRichiesta.compareTo(SolmrConstants.RICHIESTA_STATO_FIRMA_DIGITALE) < 0)
      && Validator.isEmpty(param))
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

  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new LettRichCessAz());
  }
}
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
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaDocumentoVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.AziendaRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.ConsistenzaZootecnicaRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.ContiCorrentiRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.DichiarazioniRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.DocumentiElencoRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.ElAssRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.FabbricatiRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.FirmaRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.HeaderRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.LettAccRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.SoggRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.TerreniElencoRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.TitolareRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.TrattamentoDatiRichAz;
import it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione.UteRichAz;
import it.csi.smranag.smrgaa.presentation.pdf.PdfFromImg;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.awt.PrintJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class StampaNuovaIscrizioneServlet extends ModularReport
{
    
  
  /**
   * 
   */
  private static final long serialVersionUID = 3399647968650487832L;
  
  
  public static final String CODICE_STAMPA    = "NUOVA_ISCRIZIONE";

  public StampaNuovaIscrizioneServlet()
  {
    this.templateXML = "it/csi/solmr/etc/anag/stampe/Blank.srt";
    this.nomeFilePdf = "stampaNuovaIscrizione.pdf";
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
    
    
    
    String denominazioneAzienda = null;
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
        checkNull(DateUtils.formatDateTimeNotNull(dataStampa)));
    
    
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
    
    //allegati jpg
    @SuppressWarnings("unchecked")
    Vector<Long> vIdAllegatiJpg = (Vector<Long>)hmParametri.get("vIdAllegatiJpg");
    if(Validator.isNotEmpty(vIdAllegatiJpg))
    {
      for(int i=0;i<vIdAllegatiJpg.size();i++)
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfFromImg pdfFromImg = new PdfFromImg();
        
        pdfFromImg.generaPdf(request, response, vIdAllegatiJpg.get(i), baos);
        
        byte[] b = baos.toByteArray();
        InputStream in = new ByteArrayInputStream(b);
        
        
        reader = new PdfReader(in);
        for (int j = 1; j <= reader.getNumberOfPages(); j++) 
        {
          document.newPage();
          //import the page from source pdf
          PdfImportedPage page = writer.getImportedPage(reader, j);
          //add the page to the destination pdf
          cb.addTemplate(page, 0, 0);
        }
      }
    }
    
    //allegati pdf
    @SuppressWarnings("unchecked")
    Vector<Long> vIdAllegatiPdf = (Vector<Long>)hmParametri.get("vIdAllegatiPdf");
    if(Validator.isNotEmpty(vIdAllegatiPdf))
    {
      for(int i=0;i<vIdAllegatiPdf.size();i++)
      {
        AllegatoDocumentoVO allegatoDocumentoVO = gaaFacadeClient.getFileAllegato(vIdAllegatiPdf.get(i));
        
        byte[] b = allegatoDocumentoVO.getFileAllegato();
        InputStream in = new ByteArrayInputStream(b);
        
        
        reader = new PdfReader(in);
        for (int j = 1; j <= reader.getNumberOfPages(); j++) 
        {
          document.newPage();
          //import the page from source pdf
          PdfImportedPage page = writer.getImportedPage(reader, j);
          //add the page to the destination pdf
          cb.addTemplate(page, 0, 0);
        }
      }
    }
    
   
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
    
    Long idAziendaNuova = (Long)request.getSession().getAttribute("idAziendaNuova");
    AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
   
    hmParametri.put("idAziendaNuova", idAziendaNuova);
    hmParametri.put("aziendaNuovaVO", aziendaNuovaVO);
    hmParametri.put("dataStampa", new Date());
    hmParametri.put("idTipologiaAzienda", aziendaNuovaVO.getIdTipologiaAzienda());
    
    //Per valorizzare il doc di identità!!
    Vector<RichiestaAziendaDocumentoVO> vAllegatiAziendaNuova = 
        gaaFacadeClient.getAllegatiAziendaNuovaIscrizione(idAziendaNuova.longValue(),aziendaNuovaVO.getIdTipoRichiesta());
    BigDecimal idDocIdentita = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ID_DOC_OBB_NAP);
    Vector<Long> vIdAllegatiJpg = null;
    Vector<Long> vIdAllegatiPdf = null;
    for(int i=0;i<vAllegatiAziendaNuova.size();i++)
    {
      
      RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO = vAllegatiAziendaNuova.get(i);
    
      if(richiestaAziendaDocumentoVO.getvAllegatoDocumento() != null)
      {
        for(int j=0;j<richiestaAziendaDocumentoVO.getvAllegatoDocumento().size();j++)
        {
          if(richiestaAziendaDocumentoVO.getExtIdDocumento().longValue()
              == idDocIdentita.longValue())
          {
            Long idAllegato = richiestaAziendaDocumentoVO.getvAllegatoDocumento().get(j).getIdAllegato();
            AllegatoDocumentoVO allegatoDocumentoVO = gaaFacadeClient.getFileAllegato(idAllegato);
            if(allegatoDocumentoVO.getNomeFisico().contains(".jpg")
                || allegatoDocumentoVO.getNomeFisico().contains(".gif"))
            {
              if(vIdAllegatiJpg == null)
              {
                vIdAllegatiJpg = new  Vector<Long>();
              }
              vIdAllegatiJpg.add(idAllegato);
            }
            else if(allegatoDocumentoVO.getNomeFisico().contains(".pdf"))
            {
              if(vIdAllegatiPdf == null)
              {
                vIdAllegatiPdf = new  Vector<Long>();
              }
              vIdAllegatiPdf.add(idAllegato);
            }
          }
        }
      }
    }
    hmParametri.put("vIdAllegatiJpg", vIdAllegatiJpg);
    hmParametri.put("vIdAllegatiPdf", vIdAllegatiPdf);
    
    
    
    return hmParametri;
  }  

  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new LettAccRichAz());
    addSubReport(new AziendaRichAz());
    addSubReport(new ConsistenzaZootecnicaRichAz());
    addSubReport(new ContiCorrentiRichAz());
    addSubReport(new DocumentiElencoRichAz());
    addSubReport(new FabbricatiRichAz());
    addSubReport(new FirmaRichAz());
    addSubReport(new HeaderRichAz());
    addSubReport(new TerreniElencoRichAz());
    addSubReport(new TitolareRichAz());
    addSubReport(new TrattamentoDatiRichAz());
    addSubReport(new UteRichAz());
    addSubReport(new DichiarazioniRichAz());
    addSubReport(new SoggRichAz());
    addSubReport(new ElAssRichAz());
  }
}
package it.csi.smranag.smrgaa.presentation.client.stampe;

/**
 * <p>Title: Aziende trasformatrici</p>
 * <p>Description: Servizi on-line per le aziende vinicole di trasformazione</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.documento.DocAltroParticelle;
import it.csi.smranag.smrgaa.presentation.client.stampe.documento.DocDichiarazioni;
import it.csi.smranag.smrgaa.presentation.client.stampe.documento.DocFirma;
import it.csi.smranag.smrgaa.presentation.client.stampe.documento.DocSottoscritto;
import it.csi.smranag.smrgaa.presentation.client.stampe.documento.DocTrattamentoDati;
import it.csi.smranag.smrgaa.presentation.client.stampe.documento.HeaderStampaDoc;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.DocumentoAnagraficoDett;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.DocumentoContoCorrente;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.DocumentoCorrettivaTerreni;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.DocumentoIdentificazione;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.DocumentoProprietari;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.DocumentoTerreni;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.DocumentoZootecnicoDett;
import it.csi.smranag.smrgaa.presentation.client.stampe.protocollo.HeaderDocumentoProtocollo;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class StampaProtocolloServlet extends ModularReport
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 188029137264310431L;

  public StampaProtocolloServlet()
  {
    this.templateXML = "it/csi/solmr/etc/anag/stampe/Blank.srt";
    this.nomeFilePdf = "StampaProtocollo.pdf";
    this.orientamentoStampa = StyleConstants.PAPER_A4;
  }

  protected HashMap<String, Object> stampaPdf(HttpServletRequest request, TabularSheet report,
      AnagFacadeClient anagFacadeclient, GaaFacadeClient gaaFacadeClient, 
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    

    HashMap<String, Object> hmParametri = preload(request, report,
        anagFacadeclient, gaaFacadeClient, ruoloUtenza);

    setBackground(report);
    processSubReports(report, 1, null,
        anagFacadeclient, gaaFacadeClient, request, hmParametri);
    //report.removeRows(0, 1);
    
    return hmParametri;
  }

  private void setBackground(TabularSheet report)
  {
    //Sfondo bianco
  }

  private HashMap<String, Object> preload(HttpServletRequest request,
      TabularSheet report, AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    HashMap<String, Object> hmParametri = new HashMap<String, Object>();
    
    // Dati anagrafici dell'azienda
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    
    String idTipoReport = request.getParameter("idTipoReport");
    hmParametri.put("idTipoReport", idTipoReport);
    
    String cuaa = request.getParameter("cuaa");
    hmParametri.put("cuaa", cuaa);
    
    hmParametri.put("anagAziendaVO", anagAziendaVO);
    
    PersonaFisicaVO personaFisicaVO = anagFacadeClient
        .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaVO
        .getIdAnagAzienda().longValue(), null);    
    hmParametri.put("personaFisicaVO", personaFisicaVO);
    
    return hmParametri;
  }
  
  
  
  
  protected int processSubReports(TabularSheet report, int riga,
      String codiceStampa,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HttpServletRequest request,
      HashMap<String, Object> parametri) throws Exception
  {
    
    long[] idDocumento = (long[])request.getSession().getAttribute("idDocumentoStampaProtocollo");
    
    for(int i=0;i<idDocumento.length;i++)
    {
      Long idTipoReport = null;
      //Arrivo dalla scelta stampa
      if(Validator.isNotEmpty(parametri.get("idTipoReport")))
      {
        idTipoReport = new Long((String)parametri.get("idTipoReport"));
      }
      //Stampa direta ho solo un tipo di stampa (scelta di default)
      else
      {
        Vector<TipoReportVO> vTipoStampaReport = gaaFacadeClient.getListTipiDocumentoStampaProtocollo(idDocumento);
        idTipoReport = new Long(vTipoStampaReport.get(0).getIdTipoReport());
      }
      
      String tipoDocumento = gaaFacadeClient.getTipoDocumentoStampaProtocollo(idTipoReport);
      //Metto il flag a false poiche Sergio ha dettoche bisogna prendere sempre tutto
      //quindi utilizzare la query col flag a false
      DocumentoVO documentoVO = anagFacadeClient.getDettaglioDocumento(idDocumento[i], false);      
      parametri.put("idDocumento", new Long(idDocumento[i]));
      parametri.put("documentoVO", documentoVO);
      parametri.put("numDocumento", new Integer(i));
      riga  = super.processSubReports(report, riga, tipoDocumento,
        anagFacadeClient, gaaFacadeClient, request, parametri);
      
    }   
    
    return riga;
  }
  
  
  
  
  

  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new DocumentoAnagraficoDett());
    addSubReport(new DocumentoContoCorrente());
    addSubReport(new DocumentoCorrettivaTerreni());
    addSubReport(new DocumentoIdentificazione());
    addSubReport(new DocumentoProprietari()); 
    addSubReport(new DocumentoTerreni());
    addSubReport(new DocumentoZootecnicoDett());
    addSubReport(new HeaderDocumentoProtocollo());
    addSubReport(new HeaderStampaDoc());
    addSubReport(new DocSottoscritto());
    addSubReport(new DocAltroParticelle());
    addSubReport(new DocDichiarazioni());
    addSubReport(new DocFirma());
    addSubReport(new DocTrattamentoDati());
  }
}
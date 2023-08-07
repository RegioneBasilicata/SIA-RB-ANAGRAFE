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
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Azienda;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Titolare;
import it.csi.smranag.smrgaa.presentation.client.stampe.istanzaRiesame.FirmaIstanzaRiesame;
import it.csi.smranag.smrgaa.presentation.client.stampe.istanzaRiesame.HeaderIstanzaRiesame;
import it.csi.smranag.smrgaa.presentation.client.stampe.istanzaRiesame.TerreniIstanzaRiesame;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class StampaIstanzaRiesameServlet extends ModularReport
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 1572099180608027807L;
  
  
  
  
  public static final String CODICE_STAMPA    = "ISTANZA_RIESAME";

  public StampaIstanzaRiesameServlet()
  {
    this.templateXML = "it/csi/solmr/etc/anag/stampe/Blank.srt";
    this.nomeFilePdf = "IstanzaRiesame.pdf";
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
    processSubReports(report, 1, CODICE_STAMPA,
        anagFacadeclient, gaaFacadeClient, request, hmParametri);
    
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
    return hmParametri;
  }
  
  
  protected int processSubReports(TabularSheet report, int riga,
      String codiceStampa,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HttpServletRequest request,
      HashMap<String, Object> parametri) throws Exception
  {
    
    String[] idDocumentoTmp = (String[])request.getParameterValues("idDocumento");
    long[] idDocumento = new long[idDocumentoTmp.length];
    for(int i=0;i<idDocumentoTmp.length;i++)
    {      
      idDocumento[i] = new Long(idDocumentoTmp[i]).longValue();
    }
    
    for(int i=0;i<idDocumento.length;i++)
    {
      AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
          .getAttribute("anagAziendaVO");
      
      //Metto il flag a false poiche Sergio ha dettoche bisogna prendere sempre tutto
      //quindi utilizzare la query col flag a false
      DocumentoVO documentoVO = anagFacadeClient.getDettaglioDocumento(idDocumento[i], false);  
      
      
      
      AnagAziendaVO anagAziendaIstanzaVO = gaaFacadeClient.getAnagraficaAzienda(
          anagAziendaVO.getIdAzienda(), documentoVO.getDataUltimoAggiornamento());
      //Per evitare null pointer nei sub Report
      if(anagAziendaIstanzaVO == null)
      {
        anagAziendaIstanzaVO = new AnagAziendaVO();
      }
      //dati rappresentate legale
      PersonaFisicaVO personaFisicaVO = anagFacadeClient
      .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaVO
        .getIdAnagAzienda().longValue(), documentoVO.getDataUltimoAggiornamento());
      //Per evitare null pointer nei sub Report
      if(personaFisicaVO == null)
      {
        personaFisicaVO = new PersonaFisicaVO();
      }
      
      Vector<ParticellaVO> vParticelle= gaaFacadeClient.getParticelleUtilizzoIstanzaRiesame(
          documentoVO.getIdDocumento().longValue());
      
      
      parametri.put("documentoVO", documentoVO);
      parametri.put("anagAziendaVO", anagAziendaIstanzaVO);
      parametri.put("personaFisicaVO", personaFisicaVO);
      parametri.put("vParticelle", vParticelle);
      parametri.put("dataRiferimento", documentoVO.getDataUltimoAggiornamento());
      parametri.put("numDocumento", new Integer(i));
      
      riga  = super.processSubReports(report, riga, CODICE_STAMPA,
        anagFacadeClient, gaaFacadeClient, request, parametri);
      
    }   
    
    return riga;
  }
  
  
  

  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new Azienda());
    addSubReport(new Titolare());
    addSubReport(new TerreniIstanzaRiesame());
    addSubReport(new HeaderIstanzaRiesame());
    addSubReport(new FirmaIstanzaRiesame());    
  }
}
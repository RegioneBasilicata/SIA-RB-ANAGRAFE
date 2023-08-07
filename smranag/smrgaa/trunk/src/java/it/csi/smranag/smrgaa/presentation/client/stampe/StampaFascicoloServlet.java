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
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Anomalie;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.AttivitaComplementari;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Azienda;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.ConduzioneAziendale;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.ConsistenzaZootecnica;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.ContiCorrenti;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Dichiarazioni;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.DocumentiElenco;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Fabbricati;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Manodopera;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.MotoriAgricoli;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.TerreniElenco;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.TerreniRiepilogoCond;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.TerreniRiepilogoMacroUso;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Titolare;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.TrattamentoDati;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.UVElenco;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.UVRiepilogoIdoneita;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Ute;
import it.csi.smranag.smrgaa.presentation.client.stampe.stampaFascicolo.FirmaFascicolo;
import it.csi.smranag.smrgaa.presentation.client.stampe.stampaFascicolo.HeaderFascicolo;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

public class StampaFascicoloServlet extends ModularReport
{
  
  
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 784421067150178413L;
  
  
  
  
  public static final String CODICE_STAMPA    = "FASCICOLO";

  public StampaFascicoloServlet()
  {
    this.templateXML = "it/csi/solmr/etc/anag/stampe/Blank.srt";
    this.nomeFilePdf = "stampaFascicolo.pdf";
    this.orientamentoStampa = StyleConstants.PAPER_A4;
  }

  protected HashMap<String, Object> stampaPdf(HttpServletRequest request, TabularSheet report,
      AnagFacadeClient anagFacadeclient, GaaFacadeClient gaaFacadeClient, 
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    

    HashMap<String, Object> hmParametri = preload(request, report,
        anagFacadeclient, gaaFacadeClient, ruoloUtenza);

    Long idDichiarazioneConsistenza = (Long)hmParametri.get("idDichiarazioneConsistenza");
    
    setBackground(report, idDichiarazioneConsistenza);
    //report.setCurrentHeader(TabularSheet.DEFAULT_HEADER);
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)hmParametri.get("anagAziendaVO");
    String denominazioneAzienda = null;
    if (anagAziendaVO.getDenominazione().length() > 74)
    {
      denominazioneAzienda = anagAziendaVO.getDenominazione().substring(0, 74)
          + " [...]";
    }
    else
    {
      denominazioneAzienda = anagAziendaVO.getDenominazione();
    }

    report.setElement("txtIntestazioneAzienda", checkNull(anagAziendaVO.getCUAA()
        + " - " + denominazioneAzienda));
    
    DichiarazioneConsistenzaGaaVO dichCons = 
      (DichiarazioneConsistenzaGaaVO)hmParametri.get("dichConsistenza");
    
    if(dichCons !=null)
    {
    	report.setElement("txtNumProtocollo", "Repertorio" +
          		" n. "
          + checkNull(dichCons.getNumeroProtocollo())
          + " del "
          + checkNull(DateUtils.formatDateNotNull(dichCons.getDataProtocollo())));
    }
    
    Date dataDichiarazioneStampa = (Date)hmParametri.get("dataDichiarazioneStampa");
    report.setElement("TextFooterData", 
        checkNull(DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa)));
    
    
    if(Validator.isNotEmpty(request.getParameter("codiceStampaGenerico")))
    {
      processSubReports(report, 1, request.getParameter("codiceStampaGenerico"),
          anagFacadeclient, gaaFacadeClient, request, hmParametri);
    }
    else
    {
      processSubReports(report, 1, CODICE_STAMPA,
        anagFacadeclient, gaaFacadeClient, request, hmParametri);
    }
    
    
    return hmParametri;
     
  }

  private void setBackground(TabularSheet report, Long idDichiarazioneConsistenza)
  {
    if(idDichiarazioneConsistenza == null)
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
    
    String dichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
    Date dataDichiarazioneStampa = null;
    Long idDichiarazioneConsistenza = null;
    
    
    
    //Sono in una dichiarazione di consistenza
    if(!"-1".equalsIgnoreCase(dichiarazioneConsistenza))
    {
      idDichiarazioneConsistenza = new Long(dichiarazioneConsistenza);
      ConsistenzaVO consistenzaVO = anagFacadeClient
        .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
      dataDichiarazioneStampa =  consistenzaVO.getDataDichiarazione();
    }
    else
    {
      dataDichiarazioneStampa = new Date();
    }
    
    hmParametri.put("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
    hmParametri.put("dataDichiarazioneStampa", dataDichiarazioneStampa);
    
    Date dataInserimentoDichiarazione = null;
    Long codiceFotografia = null;
    if(idDichiarazioneConsistenza !=null)
    {    
      DichiarazioneConsistenzaGaaVO  dichConsistenza = gaaFacadeClient
        .getDatiDichiarazioneConsistenza(idDichiarazioneConsistenza.longValue());
      
      codiceFotografia = dichConsistenza.getCodiceFotografia();
      dataInserimentoDichiarazione = dichConsistenza.getDataInserimentoDichiarazione();
      
      hmParametri.put("dichConsistenza", dichConsistenza);
    } 
    else
    {
      dataInserimentoDichiarazione = new Date();
    }
    
    hmParametri.put("dataInserimentoDichiarazione", dataInserimentoDichiarazione);
    hmParametri.put("codiceFotografia", codiceFotografia);
    
    // Dati anagrafici dell'azienda
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");    
    AnagAziendaVO anagAziendaTmpVO = null;
    anagAziendaTmpVO = gaaFacadeClient.getAnagraficaAzienda(
        anagAziendaVO.getIdAzienda(),
        dataInserimentoDichiarazione);
    //Per evitare nullPointer
    if(anagAziendaTmpVO == null)
    {
      anagAziendaTmpVO = new AnagAziendaVO();
    }   
    hmParametri.put("anagAziendaVO", anagAziendaTmpVO);
    
    //Dati Persona
    PersonaFisicaVO personaFisicaVO = anagFacadeClient
      .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaTmpVO
      .getIdAnagAzienda().longValue(), dataInserimentoDichiarazione);    
    hmParametri.put("personaFisicaVO", personaFisicaVO);
    
    //Dati fascicolo    
    InfoFascicoloVO infoFascicoloVO = gaaFacadeClient
      .getInfoFascicolo(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione, codiceFotografia);
    hmParametri.put("infoFascicoloVO", infoFascicoloVO);
    
    
    return hmParametri;
  }  

  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new Anomalie());
    addSubReport(new AttivitaComplementari());
    addSubReport(new Azienda());
    //addSubReport(new Condizionalita());
    addSubReport(new ConduzioneAziendale());
    addSubReport(new ConsistenzaZootecnica());
    addSubReport(new ContiCorrenti());
    addSubReport(new Dichiarazioni());
    addSubReport(new DocumentiElenco());
    addSubReport(new Fabbricati());
    addSubReport(new FirmaFascicolo());
    addSubReport(new HeaderFascicolo());
    addSubReport(new Manodopera());
    addSubReport(new MotoriAgricoli());
    addSubReport(new TerreniElenco());
    addSubReport(new TerreniRiepilogoCond());
    addSubReport(new TerreniRiepilogoMacroUso());
    addSubReport(new Titolare());
    addSubReport(new TrattamentoDati());
    addSubReport(new Ute());
    addSubReport(new UVElenco());
    addSubReport(new UVRiepilogoIdoneita());
  }
}
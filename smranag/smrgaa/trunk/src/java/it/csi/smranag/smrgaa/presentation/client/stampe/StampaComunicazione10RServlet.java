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
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Azienda;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.MotoriAgricoli;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Titolare;
import it.csi.smranag.smrgaa.presentation.client.stampe.comuni.Ute;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.CesAcqReflui10R;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.ConsistenzaZootecnica10R;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.EffluentiZootecnici;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.EffluentiZootecniciNew;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.Fabbricati10R;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.Firma10R;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.Header10R;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.RispettoVincoli10R;
import it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r.TerreniRiepilogoZVN;
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

public class StampaComunicazione10RServlet extends ModularReport
{
  /**
   * 
   */
  private static final long serialVersionUID = 2835827939702719427L;
  
  
  public static final String CODICE_STAMPA    = "COMUNICAZIONE10/R";

  public StampaComunicazione10RServlet()
  {
    this.templateXML = "it/csi/solmr/etc/anag/stampe/Blank.srt";
    this.nomeFilePdf = "stampaComunicazione10R.pdf";
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
      report.setElement("txtNumProtocollo", "Repertorio n. "
          + checkNull(dichCons.getNumeroProtocollo())
          + " del "
          + checkNull(DateUtils.formatDateNotNull(dichCons.getDataProtocollo())));
    }
    
    Date dataDichiarazioneStampa = (Date)hmParametri.get("dataDichiarazioneStampa");
    report.setElement("TextFooterData", 
        checkNull(DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa)));
    
    
    processSubReports(report, 1, CODICE_STAMPA,
        anagFacadeclient, gaaFacadeClient, request, hmParametri);
    
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
    Long idDichiarazioneFor10R = new Long(dichiarazioneConsistenza);
    
    
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
    hmParametri.put("dataRiferimento", dataInserimentoDichiarazione);
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
    
    // Dati comunicazione 10r
    Comunicazione10RVO[] comunicazione10RVO = gaaFacadeClient
      .getComunicazione10RByPianoRifererimento(anagAziendaTmpVO.getIdAzienda().longValue(), 
          idDichiarazioneFor10R.longValue());
    hmParametri.put("comunicazione10RVO", comunicazione10RVO);

    
    
    return hmParametri;
  }  

  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new Azienda());
    addSubReport(new CesAcqReflui10R());
    addSubReport(new ConsistenzaZootecnica10R());
    addSubReport(new EffluentiZootecnici());
    addSubReport(new EffluentiZootecniciNew());
    addSubReport(new Fabbricati10R());
    addSubReport(new Firma10R());
    addSubReport(new Header10R());
    addSubReport(new MotoriAgricoli());
    addSubReport(new TerreniRiepilogoZVN());
    addSubReport(new Titolare());
    addSubReport(new Ute());
    addSubReport(new RispettoVincoli10R());
  }
}
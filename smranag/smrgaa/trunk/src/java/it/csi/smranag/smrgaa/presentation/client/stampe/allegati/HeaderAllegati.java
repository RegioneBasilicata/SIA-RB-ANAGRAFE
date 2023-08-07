package it.csi.smranag.smrgaa.presentation.client.stampe.allegati;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.LogoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderAllegati extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/allegati/HeaderAllegati.srt";
  private final static String CODICE_SUB_REPORT = "HEADER_ALLEGATI";

  public HeaderAllegati() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    
    RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession()
      .getAttribute("ruoloUtenza");
  
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    LogoVO logoVO = gaaFacadeClient.getLogo(
        ruoloUtenza.getIstatRegioneAttiva(), anagAziendaVO.getProvCompetenza());
    
    
    subReport.setElement("imgRegione", logoVO.getLogoRegione()
        .getImage());
    
 
    subReport.removeElement("imgProvincia");
    
    DichiarazioneConsistenzaGaaVO dichCons = 
      (DichiarazioneConsistenzaGaaVO)parametri.get("dichConsistenza");
    Date dataDichiarazioneStampa = (Date)parametri.get("dataDichiarazioneStampa");
    
    if(dichCons !=null)
    {
      
      String txtIntestazioneAllegato = StampeGaaServlet.checkNull("Allegato alla dichiarazione del ")
      + StampeGaaServlet.checkNull(DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa))
      + StampeGaaServlet.checkNull(" prot. N. ")
      + StampeGaaServlet.checkNull(dichCons.getNumeroProtocollo())
      + StampeGaaServlet.checkNull(" del ")
      + StampeGaaServlet.checkNull(DateUtils.formatDateNotNull(dichCons.getDataProtocollo()));

      subReport.setElement("txtIntestazioneAllegato", txtIntestazioneAllegato);
      
      
      subReport.setElement("txtProtocolloTest", "Repertorio n. "
          + StampeGaaServlet.checkNull(dichCons.getNumeroProtocollo())
          + " del "
          + StampeGaaServlet.checkNull(DateUtils.formatDateNotNull(dichCons.getDataProtocollo())));
      
      subReport.setElement("lblDataSituazione", 
          StampeGaaServlet.checkNull("DICHIARAZIONE DEL"));
      
      subReport.setElement("txtMotivo", 
          StampeGaaServlet.checkNull(dichCons.getMotivo()));
    }
    else
    {
      subReport
      .setElement(
          "txtIntestazioneAllegato",
          StampeGaaServlet.checkNull("Allegato alla dichiarazione del ____________ prot. N. ____________ del _____________"));
      
      subReport.removeElement("lblMotivo");
      subReport.removeElement("txtMotivo");
    }
     
    subReport.setElement("txtDataSituazione",
        StampeGaaServlet.checkNull(DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa)));
    
    
    
    InfoFascicoloVO infoFascicoloVO = 
      (InfoFascicoloVO)parametri.get("infoFascicoloVO");
  
    String temp = infoFascicoloVO.getCodiceFiscale();
    if (temp != null && !"".equals(temp))
      temp = StringUtils.parseCodiceAgea(temp) + " - ";
    else
      temp = "";
  
    if (infoFascicoloVO.getDenominazione() != null)
      subReport.setElement("txtDetentoreFascicolo", temp
          + StampeGaaServlet.checkNull(infoFascicoloVO.getDenominazione()));
    else
      subReport.setElement("txtDetentoreFascicolo", temp
          + StampeGaaServlet.checkNull(infoFascicoloVO.getDescrizione()));
  
    subReport.setElement("txtIndirizzoFascicolo1", 
        StampeGaaServlet.checkNull(infoFascicoloVO.getIndirizzo()));
  
    String txtIndirizzoFascicolo2 = StampeGaaServlet.checkNull(
        infoFascicoloVO.getCap());
    if (infoFascicoloVO.getDescComune() != null)
      txtIndirizzoFascicolo2 = ("".equals(txtIndirizzoFascicolo2) ? ""
          : txtIndirizzoFascicolo2 + " ")
          + infoFascicoloVO.getDescComune();    
    if (infoFascicoloVO.getSiglaProv() != null)
    {
      txtIndirizzoFascicolo2 = ("".equals(txtIndirizzoFascicolo2) ? ""
          : txtIndirizzoFascicolo2 + " ")
          + "(" + infoFascicoloVO.getSiglaProv() + ")";
    }
    subReport.setElement("txtIndirizzoFascicolo2",
        StampeGaaServlet.checkNull(txtIndirizzoFascicolo2));   
    

    
    
    
  }
}
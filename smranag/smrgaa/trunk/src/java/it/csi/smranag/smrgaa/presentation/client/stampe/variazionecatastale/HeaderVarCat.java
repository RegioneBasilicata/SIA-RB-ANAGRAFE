package it.csi.smranag.smrgaa.presentation.client.stampe.variazionecatastale;

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
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderVarCat extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/variazionecatastale/HeaderVarCat.srt";
  private final static String CODICE_SUB_REPORT = "HEADER_VAR_CAT";

  public HeaderVarCat() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession()
      .getAttribute("ruoloUtenza");
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    LogoVO logoVO = gaaFacadeClient.getLogo(
        ruoloUtenza.getIstatRegioneAttiva(), anagAziendaVO.getProvCompetenza());
    
    
    subReport.setElement("imgRegione", logoVO.getLogoRegione()
        .getImage());
    
    
    DichiarazioneConsistenzaGaaVO dichCons = 
      (DichiarazioneConsistenzaGaaVO)parametri.get("dichConsistenza");
    Date dataDichiarazioneStampa = (Date)parametri.get("dataDichiarazioneStampa");
    
    if(dichCons !=null)
    {
      subReport.setElement("txtProtocolloTest", 
          "del "+StampeGaaServlet.checkNull(DateUtils.formatDateNotNull(dataDichiarazioneStampa))+
          " repertorio n. "
          + StampeGaaServlet.checkNull(dichCons.getNumeroProtocollo())
          + " del "
          + StampeGaaServlet.checkNull(DateUtils.formatDateNotNull(dichCons.getDataProtocollo())));
    }
    
    
    
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
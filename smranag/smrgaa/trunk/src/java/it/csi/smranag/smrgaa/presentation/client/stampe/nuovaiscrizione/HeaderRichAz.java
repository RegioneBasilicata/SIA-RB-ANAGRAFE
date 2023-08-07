package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.LogoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class HeaderRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/HeaderRichAz.srt";
  private final static String CODICE_SUB_REPORT = "HEADER_RICH_AZ";

  public HeaderRichAz() throws IOException, SolmrException
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
    
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    
    
    //mess aprvincia fittizia cuneo
    LogoVO logoVO = gaaFacadeClient.getLogo(
        ruoloUtenza.getIstatRegioneAttiva(), "004");
    
    
    subReport.setElement("imgRegione", logoVO.getLogoRegione()
        .getImage());
    
    
    String txtDescEstesa = aziendaNuovaVO.getDescEstesaTipoRichiesta();
    if(aziendaNuovaVO.getIdTipologiaAzienda().compareTo(SolmrConstants.COD_TIPOLOGIA_AZIENDA_ENTE) == 0)
    {
      txtDescEstesa += " imprese/enti";
    }
    else
    {
      txtDescEstesa += " privati";      
    }     
    subReport.setElement("txtDescEstesa", StampeGaaServlet.checkNull(txtDescEstesa));
    
    Vector<TipoAttestazioneVO> vAttestazioneVO = gaaFacadeClient.getAttestStampaProtoc(
        richiestaTipoReportVO.getIdReportSubReport().longValue());
    if(Validator.isNotEmpty(vAttestazioneVO))
    {
      String txtAttestazioni = "";
      for(int i=0;i<vAttestazioneVO.size();i++)
      {
        txtAttestazioni += vAttestazioneVO.get(i).getDescrizione()+"\n";
      }      
      subReport.setElement("txtAttestazioni",
          StampeGaaServlet.checkNull(txtAttestazioni));
    }
    else
    {
      subReport.removeElement("txtAttestazioni");
    }
    
  }

}
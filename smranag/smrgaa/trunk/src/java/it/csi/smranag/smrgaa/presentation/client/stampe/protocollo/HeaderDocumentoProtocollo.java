package it.csi.smranag.smrgaa.presentation.client.stampe.protocollo;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.LogoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderDocumentoProtocollo extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/protocollo/HeaderDocumentoProtocollo.srt";
  private final static String CODICE_SUB_REPORT = "HEADER_DOCUMENTO_PROTOCOLLO";

  public HeaderDocumentoProtocollo() throws IOException, SolmrException
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
      
    subReport.removeElement("imgProvincia");   
    
    
    
  }
}
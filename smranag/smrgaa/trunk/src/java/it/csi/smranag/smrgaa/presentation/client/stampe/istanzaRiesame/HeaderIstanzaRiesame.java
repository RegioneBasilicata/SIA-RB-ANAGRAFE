package it.csi.smranag.smrgaa.presentation.client.stampe.istanzaRiesame;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderIstanzaRiesame extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/istanzaRiesame/HeaderIstanzaRiesame.srt";
  private final static String CODICE_SUB_REPORT = "HEADER_ISTANZA_RIESAME";

  public HeaderIstanzaRiesame() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");
    
    if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo()))
    {
      subReport.setElement("numeroProtocollo", StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
      subReport.setElement("dataProtocollo", DateUtils.formatDateNotNull(documentoVO.getDataProtocollo()));
    }
    
  }

}
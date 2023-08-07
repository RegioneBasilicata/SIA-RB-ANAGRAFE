package it.csi.smranag.smrgaa.presentation.client.stampe.istanzaRiesame;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class FirmaIstanzaRiesame extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/istanzaRiesame/FirmaIstanzaRiesame.srt";
  private final static String CODICE_SUB_REPORT = "FIRMA_ISTANZA_RIESAME";

  public FirmaIstanzaRiesame() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)parametri.get("personaFisicaVO");
    
    String firma = StampeGaaServlet.checkNull(personaFisicaVO.getCognome()) + 
    StampeGaaServlet.checkNull(" ") + StampeGaaServlet.checkNull(personaFisicaVO.getNome());
    subReport.setElement("txtFirma", firma);
    
  }
}
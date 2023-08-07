package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class Azienda extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/Azienda.srt";
  private final static String CODICE_SUB_REPORT = "AZIENDA";

  public Azienda() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  
  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");

    subReport.setElement("txtCuaa", StampeGaaServlet.checkNull(anagAziendaVO.getCUAA()));
    subReport
        .setElement("txtPartitaIva", StampeGaaServlet.checkNull(anagAziendaVO.getPartitaIVA()));
    subReport.setElement("txtProvRea", StampeGaaServlet.checkNull(anagAziendaVO.getCCIAAprovREA()));
    subReport
        .setElement("txtNumRea", StampeGaaServlet.checkNull(anagAziendaVO.getCCIAAnumeroREA()));
    subReport.setElement("txtAnnoIscr", StampeGaaServlet.checkNull(anagAziendaVO
        .getCCIAAannoIscrizione()));
    subReport.setElement("txtFormaGiuridica", StampeGaaServlet.checkNull(anagAziendaVO
        .getTipoFormaGiuridica().getDescription()));

    subReport.setElement("txtDenominazioneAzienda", StampeGaaServlet.checkNull(anagAziendaVO
        .getDenominazione()));
    subReport.setElement("txtCodiceAteco", StampeGaaServlet.checkNull(anagAziendaVO
        .getTipoAttivitaATECO().getDescription()));
    subReport.setElement("txtIndirizzoSedeLegale", StampeGaaServlet.checkNull(anagAziendaVO
        .getSedelegIndirizzo()));
    subReport.setElement("txtComuneSedeLegale", StampeGaaServlet.checkNull(anagAziendaVO
        .getDescComune()));
    subReport.setElement("txtProvSedeLegale", StampeGaaServlet.checkNull(anagAziendaVO
        .getSedelegProv()));
    subReport.setElement("txtCapSedeLegale", StampeGaaServlet.checkNull(anagAziendaVO
        .getSedelegCAP()));
    subReport.setElement("txtMail", StampeGaaServlet.checkNull(anagAziendaVO
        .getMail()));
    subReport.setElement("txtPec", StampeGaaServlet.checkNull(anagAziendaVO
        .getPec()));
  }

  
}
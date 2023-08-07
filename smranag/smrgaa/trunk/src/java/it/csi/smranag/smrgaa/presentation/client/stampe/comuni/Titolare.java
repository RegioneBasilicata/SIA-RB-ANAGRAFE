package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class Titolare extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/Titolare.srt";
  private final static String CODICE_SUB_REPORT = "TITOLARE";

  public Titolare() throws IOException, SolmrException
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

    subReport.setElement("txtCognomeSezII",
        StampeGaaServlet.checkNull(personaFisicaVO.getCognome()));
    subReport.setElement("txtNomeSezII", StampeGaaServlet.checkNull(personaFisicaVO.getNome()));
    subReport.setElement("txtSessoSezII", StampeGaaServlet.checkNull(personaFisicaVO.getSesso()));
    subReport.setElement("txtCodiceFiscaleSezII", StampeGaaServlet.checkNull(personaFisicaVO
        .getCodiceFiscale()));
    // Valorizzo il campo relativo alla data di nascita del titolare solo se
    // è valorizzata
    if (Validator.isNotEmpty(personaFisicaVO.getNascitaData()))
    {
      subReport.setElement("txtDataDiNascitaSezII", StampeGaaServlet.checkNull(DateUtils
          .formatDate(personaFisicaVO.getNascitaData())));
    }
    else
    {
      subReport.setElement("txtDataDiNascitaSezII", "");
    }

    String txtLuogoDiNascitaSezIIStatoEstero = StampeGaaServlet.checkNull(personaFisicaVO
        .getNascitaStatoEstero());
    String txtLuogoDiNascitaSezIICittaEstero = StampeGaaServlet.checkNull(personaFisicaVO
        .getNascitaCittaEstero());
    if (!"".equals(txtLuogoDiNascitaSezIIStatoEstero)
        || !"".equals(txtLuogoDiNascitaSezIICittaEstero))
    {
      subReport.setElement("txtLuogoDiNascitaSezII",
          StampeGaaServlet.checkNull(txtLuogoDiNascitaSezIIStatoEstero
              + ("".equals(txtLuogoDiNascitaSezIICittaEstero) ? "" : " - "
                  + txtLuogoDiNascitaSezIICittaEstero)));
      subReport.setElement("txtProvSezII", "");
    }
    else
    {
      subReport.setElement("txtLuogoDiNascitaSezII", StampeGaaServlet.checkNull(personaFisicaVO
          .getDescNascitaComune()));
      subReport.setElement("txtProvSezII", StampeGaaServlet.checkNull(personaFisicaVO
          .getNascitaProv()));
    }

    subReport.setElement("txtTelefonoSezII", StampeGaaServlet.checkNull(personaFisicaVO
        .getResTelefono()));
    subReport.setElement("txtEmailSezII", StampeGaaServlet.checkNull(personaFisicaVO.getResMail()));
    subReport.setElement("txtIndirizzoResidenzaSezII", StampeGaaServlet.checkNull(personaFisicaVO
        .getResIndirizzo()));

    String txtComuneResidenzaSezIIStatoEstero = StampeGaaServlet.checkNull(personaFisicaVO
        .getStatoEsteroRes());
    String txtComuneResidenzaSezIICittaEstero = StampeGaaServlet.checkNull(personaFisicaVO
        .getResCittaEstero());
    if (!"".equals(txtComuneResidenzaSezIIStatoEstero)
        || !"".equals(txtComuneResidenzaSezIICittaEstero))
    {
      subReport.setElement("txtComuneResidenzaSezII",
          StampeGaaServlet.checkNull(txtComuneResidenzaSezIIStatoEstero
              + ("".equals(txtComuneResidenzaSezIIStatoEstero) ? "" : " - "
                  + txtComuneResidenzaSezIICittaEstero)));
      subReport.setElement("txtProvinciaResidenzaSezII", "");
      subReport.setElement("txtCapResidenzaSezII", "");
    }
    else
    {
      subReport.setElement("txtComuneResidenzaSezII", StampeGaaServlet.checkNull(personaFisicaVO
          .getDescResComune()));
      subReport.setElement("txtProvinciaResidenzaSezII", StampeGaaServlet.checkNull(personaFisicaVO
          .getResProvincia()));
      subReport.setElement("txtCapResidenzaSezII", StampeGaaServlet.checkNull(personaFisicaVO
          .getResCAP()));
    }
    
  }

  
}
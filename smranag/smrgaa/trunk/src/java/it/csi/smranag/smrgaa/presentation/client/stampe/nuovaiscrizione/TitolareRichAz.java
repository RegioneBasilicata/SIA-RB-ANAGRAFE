package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class TitolareRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/TitolareRichAz.srt";
  private final static String CODICE_SUB_REPORT = "TITOLARE_RICH_AZ";

  public TitolareRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");

    subReport.setElement("txtCognomeSezII",
        StampeGaaServlet.checkNull(aziendaNuovaVO.getCognome()));
    subReport.setElement("txtNomeSezII", StampeGaaServlet.checkNull(aziendaNuovaVO.getNome()));
    subReport.setElement("txtSessoSezII", StampeGaaServlet.checkNull(aziendaNuovaVO.getSesso()));
    subReport.setElement("txtCodiceFiscaleSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getCodiceFiscale()));
    // Valorizzo il campo relativo alla data di nascita del titolare solo se
    // è valorizzata
    if (Validator.isNotEmpty(aziendaNuovaVO.getDataNascita()))
    {
      subReport.setElement("txtDataDiNascitaSezII", StampeGaaServlet.checkNull(DateUtils
          .formatDate(aziendaNuovaVO.getDataNascita())));
    }
    else
    {
      subReport.setElement("txtDataDiNascitaSezII", "");
    }

    String txtLuogoDiNascitaSezIIStatoEstero = StampeGaaServlet.checkNull(aziendaNuovaVO.
        getDescNascitaComune());
    String txtLuogoDiNascitaSezIICittaEstero = StampeGaaServlet.checkNull(aziendaNuovaVO
        .getCittaNascitaEstero());
    if (!"".equals(txtLuogoDiNascitaSezIICittaEstero))
    {
      subReport.setElement("txtLuogoDiNascitaSezII",
          StampeGaaServlet.checkNull(txtLuogoDiNascitaSezIIStatoEstero
              + ("".equals(txtLuogoDiNascitaSezIICittaEstero) ? "" : " - "
                  + txtLuogoDiNascitaSezIICittaEstero)));
      subReport.setElement("txtProvSezII", "");
    }
    else
    {
      subReport.setElement("txtLuogoDiNascitaSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
          .getDescNascitaComune()));
      subReport.setElement("txtProvSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
          .getNascitaProv()));
    }

    subReport.setElement("txtTelefonoSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getTelefonoSoggetto()));
    subReport.setElement("txtEmailSezII", StampeGaaServlet.checkNull(aziendaNuovaVO.getMailSoggetto()));
    subReport.setElement("txtIndirizzoResidenzaSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getResIndirizzo()));

   subReport.setElement("txtComuneResidenzaSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
          .getDescResComune()));
   subReport.setElement("txtProvinciaResidenzaSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
         .getResProvincia()));
   subReport.setElement("txtCapResidenzaSezII", StampeGaaServlet.checkNull(aziendaNuovaVO
         .getResCap()));
  
    
  }

  
}
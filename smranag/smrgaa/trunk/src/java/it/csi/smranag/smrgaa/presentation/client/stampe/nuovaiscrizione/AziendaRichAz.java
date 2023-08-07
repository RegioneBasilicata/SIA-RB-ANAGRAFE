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

public class AziendaRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/AziendaRichAz.srt";
  private final static String CODICE_SUB_REPORT = "AZIENDA_RICH_AZ";

  public AziendaRichAz() throws IOException, SolmrException
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

    subReport.setElement("txtCuaa", StampeGaaServlet.checkNull(aziendaNuovaVO.getCuaa()));
    subReport
        .setElement("txtPartitaIva", StampeGaaServlet.checkNull(aziendaNuovaVO.getPartitaIva()));
    subReport.setElement("txtFormaGiuridica", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getDescFormaGiur()));
    String denominazioneAzienda = null;
    if(Validator.isNotEmpty(aziendaNuovaVO.getDenominazione()))
    {
      denominazioneAzienda = aziendaNuovaVO.getDenominazione();
    }
    else
    {
      denominazioneAzienda = aziendaNuovaVO.getCognome()+" "+aziendaNuovaVO.getNome();
    }
    
    if(aziendaNuovaVO.getCuaa().length() == 16)
    {
      if (Validator.isNotEmpty(aziendaNuovaVO.getDataNascita()))
      {
        subReport.setElement("txtDataNascita", StampeGaaServlet.checkNull(DateUtils
            .formatDate(aziendaNuovaVO.getDataNascita())));
      }
      else
      {
        subReport.setElement("txtDataNascita", "");
      }

      String txtLuogoDiNascitaSezIIStatoEstero = StampeGaaServlet.checkNull(aziendaNuovaVO.
          getDescNascitaComune());
      String txtLuogoDiNascitaSezIICittaEstero = StampeGaaServlet.checkNull(aziendaNuovaVO
          .getCittaNascitaEstero());
      if (!"".equals(txtLuogoDiNascitaSezIICittaEstero))
      {
        subReport.setElement("txtLuogoNascita",
            StampeGaaServlet.checkNull(txtLuogoDiNascitaSezIIStatoEstero
                + ("".equals(txtLuogoDiNascitaSezIICittaEstero) ? "" : " - "
                    + txtLuogoDiNascitaSezIICittaEstero)));
      }
      else
      {
        subReport.setElement("txtLuogoNascita", StampeGaaServlet.checkNull(aziendaNuovaVO
            .getDescNascitaComune()+" ("+aziendaNuovaVO.getNascitaProv()+")"));
      }
    }
    else
    {
      subReport.removeElement("txtLabelDataNascita");
      subReport.removeElement("spNascita1");
      subReport.removeElement("txtLabelLuogoNascita");
      subReport.removeElement("nlNascita1");
      subReport.removeElement("txtDataNascita");
      subReport.removeElement("spNascita2");
      subReport.removeElement("txtLuogoNascita");
      subReport.removeElement("nlNascita2");
      subReport.removeElement("nlNascita3");
    }

    subReport.setElement("txtDenominazioneAzienda", StampeGaaServlet.checkNull(denominazioneAzienda));
    subReport.setElement("txtIndirizzoSedeLegale", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getSedelegIndirizzo()));
    subReport.setElement("txtComuneSedeLegale", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getDescComune()));
    subReport.setElement("txtProvSedeLegale", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getSedelegProv()));
    subReport.setElement("txtCapSedeLegale", StampeGaaServlet.checkNull(aziendaNuovaVO
        .getSedelegCap()));
    String mail = null;
    String telefono = null;
    String fax = null;
    if("R".equalsIgnoreCase(aziendaNuovaVO.getTipoSoggetto()))
    {
      telefono = aziendaNuovaVO.getTelefono();
      fax = aziendaNuovaVO.getFax();
      mail = aziendaNuovaVO.getMail();
    }
    else
    {
      telefono = aziendaNuovaVO.getTelefonoSoggetto();
      fax = aziendaNuovaVO.getFaxSoggetto();
      mail = aziendaNuovaVO.getMailSoggetto();
    }
    subReport.setElement("txtTelefono", StampeGaaServlet.checkNull(telefono));
    subReport.setElement("txtFax", StampeGaaServlet.checkNull(fax));
    subReport.setElement("txtMail", StampeGaaServlet.checkNull(mail));
    subReport.setElement("txtPec", StampeGaaServlet.checkNull(aziendaNuovaVO.getPec()));
    
    String subentro = "No";
    if(Validator.isNotEmpty(aziendaNuovaVO.getIdAziendaSubentro()))
    {
      subentro = "Si";
    }
    subReport.setElement("txtSubentro", StampeGaaServlet.checkNull(subentro));
    subReport.setElement("txtCuaaSubentro", StampeGaaServlet.checkNull(aziendaNuovaVO.getCuaaSubentro()));
    subReport.setElement("txtDenomSubentro", StampeGaaServlet.checkNull(aziendaNuovaVO.getDenomSubentro()));
    
  }

  
}
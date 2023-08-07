package it.csi.smranag.smrgaa.presentation.client.stampe.protocollo;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class DocumentoIdentificazione extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/protocollo/DocumentoIdentificazione.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_IDENTIFICAZIONE";

  public DocumentoIdentificazione() throws IOException, SolmrException
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
    
    //Metto il flag a false poiche Sergio ha dettoche bisogna prendere sempre tutto
    //quindi utilizzare la query col flag a false
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");
    
    subReport.setElement("txtCuaa", StampeGaaServlet.checkNull(anagAziendaVO.getCUAA()));
    subReport.setElement("txtDenominazione", StampeGaaServlet.checkNull(
        anagAziendaVO.getDenominazione()));

    if (documentoVO.getTipoDocumentoVO() != null)
    {
      subReport.setElement("txtDescrizione", StampeGaaServlet.checkNull(
          documentoVO.getTipoDocumentoVO().getDescrizione()));
    }
    
    if(documentoVO.getTipoTipologiaDocumento() != null)
    {
      subReport.setElement("txtTipologiaDocumento", StampeGaaServlet.checkNull(
          documentoVO.getTipoTipologiaDocumento().getDescription()));
    }
    
    subReport.setElement("txtStato", StampeGaaServlet.checkNull(
        documentoVO.getDescStatoDocumento()));
    subReport.setElement("txtDataInizioValidita", StampeGaaServlet.checkNull(
        DateUtils.formatDateNotNull(documentoVO.getDataInizioValidita())));
    subReport.setElement("txtDataFineValidita", StampeGaaServlet.checkNull(
        DateUtils.formatDateNotNull(documentoVO.getDataFineValidita())));
    subReport.setElement("txtDataUltimoAggiornamento", StampeGaaServlet.checkNull(
        DateUtils.formatDateNotNull(documentoVO.getDataUltimoAggiornamento())));
    
    if (documentoVO.getNumeroProtocollo() != null)
    {
      subReport.setElement("txtNumeroProtocollo", StampeGaaServlet.checkNull(
        StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo())));
    }
    
    subReport.setElement("txtDataProtocollo", StampeGaaServlet.checkNull(
        DateUtils.formatDateNotNull(documentoVO.getDataProtocollo())));    
    subReport.setElement("txtProtocolloEsterno", StampeGaaServlet.checkNull(
        documentoVO.getNumeroProtocolloEsterno()));    
    subReport.setElement("txtCausaleModifica", StampeGaaServlet.checkNull(
        documentoVO.getDescCausaleModificaDocumento()));    
    subReport.setElement("txtNote", StampeGaaServlet.checkNull(
        documentoVO.getNote()));
    
    
    
    
    
    
    
    
    
    
    
  }
}
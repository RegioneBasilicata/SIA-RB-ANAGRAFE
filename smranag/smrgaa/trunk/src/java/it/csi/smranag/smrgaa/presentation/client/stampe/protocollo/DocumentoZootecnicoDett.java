package it.csi.smranag.smrgaa.presentation.client.stampe.protocollo;

import inetsoft.report.ReportSheet;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class DocumentoZootecnicoDett extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/protocollo/DocumentoZootecnicoDett.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_ZOOTECNICO_DETT";

  public DocumentoZootecnicoDett() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");
    
    subReport.setElement("txtCuaa", 
        StampeGaaServlet.checkNull(documentoVO.getCuaaSoccidario()));    
    
  }
}
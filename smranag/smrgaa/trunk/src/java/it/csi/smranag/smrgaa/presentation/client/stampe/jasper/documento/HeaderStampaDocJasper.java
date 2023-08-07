package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.documento.HeaderStampaDocumento;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;

public class HeaderStampaDocJasper extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "HEADER_STAMPA_DOC";

  public HeaderStampaDocJasper() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo,RichiestaTipoReportVO richiestaTipoReportVO, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  { 
    
	DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO"); 
    HeaderStampaDocumento headerStampaDocumento = new HeaderStampaDocumento();
     
    String idTipoReport = (String)parametri.get("idTipoReport");
    TipoReportVO tipoReportVO = gaaFacadeClient.getTipoReport(new Long(idTipoReport).longValue());
    headerStampaDocumento.setDescrizione(StampeGaaServlet.checkNull(tipoReportVO.getDescrizione()));
    headerStampaDocumento.setNote(StampeGaaServlet.checkNull(richiestaTipoReportVO.getNote()));
    protocollo.setHeaderStampaDocumento(headerStampaDocumento);
    
  }

}
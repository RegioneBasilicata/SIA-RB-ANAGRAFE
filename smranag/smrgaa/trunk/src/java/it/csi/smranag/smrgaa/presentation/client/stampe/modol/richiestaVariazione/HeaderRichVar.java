package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Header;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderRichVar extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "HEADER_RICH_VAR";

  public HeaderRichVar() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, 
      HashMap<String, Object> parametri)
      throws Exception
  {
    
    fascicoDigitale.setHeader(new Header());
    Header headerXml = fascicoDigitale.getHeader();
    headerXml.setVisibility(true);
    headerXml.setTitolo1(richiestaTipoReportVO.getQuadro());
    headerXml.setTitolo2(richiestaTipoReportVO.getSezione());    
    
    
  }

}
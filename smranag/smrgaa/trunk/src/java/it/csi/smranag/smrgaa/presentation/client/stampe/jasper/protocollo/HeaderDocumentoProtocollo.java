package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.LogoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.HeaderDocumento;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;

public class HeaderDocumentoProtocollo extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "HEADER_DOCUMENTO_PROTOCOLLO";

  public HeaderDocumentoProtocollo() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO,
       HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession()
        .getAttribute("ruoloUtenza");
  
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
  
    LogoVO logoVO = gaaFacadeClient.getLogo(
       ruoloUtenza.getIstatRegioneAttiva(), anagAziendaVO.getProvCompetenza());
  
    HeaderDocumento headerDocumento = new HeaderDocumento();
    
    if(logoVO.getLogoRegione() != null)
    {
	    headerDocumento.setImageRegione(logoVO.getLogoRegione()
	      .getImage());
    }
    
    protocollo.setHeaderDocumento(headerDocumento);    
    
    
    
  }
}
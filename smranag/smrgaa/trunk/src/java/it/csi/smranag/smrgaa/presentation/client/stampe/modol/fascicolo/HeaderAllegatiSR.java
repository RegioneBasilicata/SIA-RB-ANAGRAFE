package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.HeaderAllegati;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.StampeModolGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderAllegatiSR extends SubReportModol
{
 
  private final static String CODICE_SUB_REPORT = "HEADER_ALLEGATI";

  public HeaderAllegatiSR() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
        
    fascicoDigitale.setHeaderAllegati(new HeaderAllegati());
    HeaderAllegati headerAllegati = fascicoDigitale.getHeaderAllegati();
    headerAllegati.setVisibility(true);
    
    
    DichiarazioneConsistenzaGaaVO dichCons = 
        (DichiarazioneConsistenzaGaaVO)parametri.get("dichConsistenza");
    
    String protocollo = "";
    if(dichCons !=null)
    {
      protocollo = "Repertorio n. "
          + StampeModolGaaServlet.checkNull(dichCons.getNumeroProtocollo())
          + " del "
          + StampeModolGaaServlet.checkNull(DateUtils.formatDateNotNull(dichCons.getDataProtocollo()));
    }
    else
    {
      protocollo = "piano in lavorazione";
    }
    Date dataDichiarazioneStampa = (Date)parametri.get("dataDichiarazioneStampa");
    headerAllegati.setDataDich(DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa));
    headerAllegati.setProtocollo(protocollo);
    
  }
  
}
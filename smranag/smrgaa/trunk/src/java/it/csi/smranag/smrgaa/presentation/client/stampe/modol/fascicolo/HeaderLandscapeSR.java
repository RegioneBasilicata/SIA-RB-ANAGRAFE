package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Castelletto;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.HeaderLandscape;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.StampeModolGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderLandscapeSR extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "HEADER_LANDSCAPE";

  public HeaderLandscapeSR() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, 
      HashMap<String, Object> parametri)
      throws Exception
  {
    
    fascicoDigitale.setHeaderLandscape(new HeaderLandscape());
    HeaderLandscape headerXml = fascicoDigitale.getHeaderLandscape();
    headerXml.setVisibility(true);
    headerXml.setTitolo1(richiestaTipoReportVO.getQuadro());
    headerXml.setTitolo2(richiestaTipoReportVO.getSezione());
    
    DichiarazioneConsistenzaGaaVO dichCons = 
        (DichiarazioneConsistenzaGaaVO)parametri.get("dichConsistenza");
    
    Castelletto castelletto = new Castelletto();
    String motivo = "";
    if(dichCons !=null)
    {
      String protocollo = "Repertorio n. "
          + StampeModolGaaServlet.checkNull(dichCons.getNumeroProtocollo())
          + " del "
          + StampeModolGaaServlet.checkNull(DateUtils.formatDateNotNull(dichCons.getDataProtocollo()));
      castelletto.setProtocollo(protocollo);
      motivo = dichCons.getMotivo();
    }
    else
    {
      motivo = "piano in lavorazione";
    }
    Date dataDichiarazioneStampa = (Date)parametri.get("dataDichiarazioneStampa");
    castelletto.setDataDich(DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa));
    castelletto.setMotivoDich(motivo);   
    
    
    InfoFascicoloVO infoFascicoloVO = 
      (InfoFascicoloVO)parametri.get("infoFascicoloVO");
    
    String enteFascicolo = "";

    String temp = infoFascicoloVO.getCodiceFiscale();
    if (temp != null && !"".equals(temp))
      temp = StringUtils.parseCodiceAgea(temp) + " - ";
    else
      temp = "";

    if (infoFascicoloVO.getDenominazione() != null)
      enteFascicolo = temp + StampeModolGaaServlet.checkNull(infoFascicoloVO.getDenominazione());
    else
      enteFascicolo = temp + StampeModolGaaServlet.checkNull(infoFascicoloVO.getDescrizione());
    
    castelletto.setEnteFascicolo(enteFascicolo);

    castelletto.setIndirizzoEnte1(
        StampeModolGaaServlet.checkNull(infoFascicoloVO.getIndirizzo()));

    String indirizzoEnte2 = StampeModolGaaServlet.checkNull(
        infoFascicoloVO.getCap());
    if (infoFascicoloVO.getDescComune() != null)
      indirizzoEnte2 = ("".equals(indirizzoEnte2) ? ""
          : indirizzoEnte2 + " ")
          + infoFascicoloVO.getDescComune();    
    if (infoFascicoloVO.getSiglaProv() != null)
    {
      indirizzoEnte2 = ("".equals(indirizzoEnte2) ? ""
          : indirizzoEnte2 + " ")
          + "(" + infoFascicoloVO.getSiglaProv() + ")";
    }
    castelletto.setIndirizzoEnte2(indirizzoEnte2);
    
    headerXml.setCastelletto(castelletto);
       
    
  }

}
package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroSoggetti;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class SoggRichVar extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "SOGG_RICH_VAR";

  public SoggRichVar() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroSoggetti quadroSoggetti = new QuadroSoggetti();
    fascicoDigitale.setQuadroSoggetti(quadroSoggetti);
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    if(aziendaNuovaVO.getIdTipoRichiesta().compareTo(new Long(SolmrConstants.RICHIESTA_VAR_SOGGETTI)) != 0)
    {   
      quadroSoggetti.setVisibility(false);
    }
    else
    {
      quadroSoggetti.setVisibility(true);
      quadroSoggetti.setTitoloSoggetti(richiestaTipoReportVO.getQuadro());
    }
    
    int num = 0;
    if (num > 0)
    {
     


    }
    else
    {
      quadroSoggetti.setSezioneVuota(true);
    }
    
    
    
  }
}
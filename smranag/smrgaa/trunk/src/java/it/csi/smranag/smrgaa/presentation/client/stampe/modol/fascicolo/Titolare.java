package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroA;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.SezioneTitolare;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class Titolare extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "TITOLARE";

  public Titolare() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    if(fascicoDigitale.getQuadroA() == null)
      fascicoDigitale.setQuadroA(new QuadroA()); 
    QuadroA quadroA = fascicoDigitale.getQuadroA(); 
    
    quadroA.setSezioneTitolare(new SezioneTitolare());
    SezioneTitolare sezioneTitolare = quadroA.getSezioneTitolare();
    sezioneTitolare.setVisibility(true);
    sezioneTitolare.setTitoloSezTitolare(richiestaTipoReportVO.getSezione());
    
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)parametri.get("personaFisicaVO");
if(personaFisicaVO != null)
{
    sezioneTitolare.setCognome(personaFisicaVO.getCognome());
    sezioneTitolare.setNome(personaFisicaVO.getNome());
    sezioneTitolare.setCodiceFiscale(personaFisicaVO.getCodiceFiscale());
    sezioneTitolare.setEmail(personaFisicaVO.getResMail());
    sezioneTitolare.setTel(personaFisicaVO.getResTelefono());
} 
  }

  
}
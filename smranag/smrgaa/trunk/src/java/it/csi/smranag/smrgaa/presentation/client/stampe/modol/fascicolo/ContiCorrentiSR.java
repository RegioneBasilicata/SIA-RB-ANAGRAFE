package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ContiCorrenti;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Conto;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroA;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.SezioneConti;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ContiCorrentiSR extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "CONTI_CORRENTI";

  public ContiCorrentiSR() throws IOException, SolmrException
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
    
    quadroA.setSezioneConti(new SezioneConti());
    SezioneConti sezioneConti = quadroA.getSezioneConti();
    sezioneConti.setVisibility(true);
    sezioneConti.setTitoloSezConti(richiestaTipoReportVO.getSezione());
    
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    Vector<ContoCorrenteVO> contiCorrenti = gaaFacadeClient.getStampaContiCorrenti(
        anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione);
    int size = contiCorrenti.size();
    if (size > 0)
    {
      sezioneConti.setContiCorrenti(new ContiCorrenti());
      for (int i = 0; i < size; i++)
      {
        
        ContoCorrenteVO ccVO = contiCorrenti.get(i);
        Conto conto = new Conto();
        conto.setIban(ccVO.getIban());
        conto.setIstituto(ccVO.getDenominazioneBanca());
        conto.setAgenzia(ccVO.getDenominazioneSportello());
               
        sezioneConti.getContiCorrenti().getConto().add(conto);       

      }
    }
    else
    {
      sezioneConti.setSezioneVuota(true);
    }

    
    
    
    
    
    
  }
}
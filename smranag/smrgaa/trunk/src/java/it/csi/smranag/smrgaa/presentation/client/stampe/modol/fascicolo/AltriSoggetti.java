package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.AltroSoggetto;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroA;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.SezioneSoggetti;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.SoggettiCollegati;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class AltriSoggetti extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "ALTRI_SOGG";

  public AltriSoggetti() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, 
      HashMap<String, Object> parametri)
      throws Exception
  {
    if(fascicoDigitale.getQuadroA() == null)
      fascicoDigitale.setQuadroA(new QuadroA());
    QuadroA quadroA = fascicoDigitale.getQuadroA(); 
    quadroA.setSezioneSoggetti(new SezioneSoggetti());
    SezioneSoggetti sezioneSoggetti = quadroA.getSezioneSoggetti();
    sezioneSoggetti.setVisibility(true);
    sezioneSoggetti.setTitoloSezSoggetti(richiestaTipoReportVO.getSezione());
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    Vector<PersonaFisicaVO> vPersonaFisicaVO = 
        anagFacadeClient.getVAltriSoggettiFromIdAnagAziendaAndDichCons(
            anagAziendaVO.getIdAnagAzienda(), dataInserimentoDichiarazione);
    if(vPersonaFisicaVO != null)
    {
      sezioneSoggetti.setSoggettiCollegati(new SoggettiCollegati());
      for(int i=0;i<vPersonaFisicaVO.size();i++)
      {
        AltroSoggetto soggetto = new AltroSoggetto();
        soggetto.setCognome(vPersonaFisicaVO.get(i).getCognome());
        soggetto.setNome(vPersonaFisicaVO.get(i).getNome());
        soggetto.setCodiceFiscale(vPersonaFisicaVO.get(i).getCodiceFiscale());
        soggetto.setRuolo(vPersonaFisicaVO.get(i).getRuolo());
        soggetto.setEmail(vPersonaFisicaVO.get(i).getResMail());
        soggetto.setTel(vPersonaFisicaVO.get(i).getResTelefono());
        
        sezioneSoggetti.getSoggettiCollegati().getAltroSoggetto().add(soggetto);
      }
      
    }
    else
    {
      sezioneSoggetti.setSezioneVuota(true);
    }
    
  }

  
}
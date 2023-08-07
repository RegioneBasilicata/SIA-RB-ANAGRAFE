package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.IntroRichVar;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class LettAccRichVar extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "LETT_ACC_RICH_VAR";

  public LettAccRichVar() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, 
      HashMap<String, Object> parametri)
      throws Exception
  {
    
    fascicoDigitale.setIntroRichVar(new IntroRichVar());
    IntroRichVar introRichVar = fascicoDigitale.getIntroRichVar();
    introRichVar.setVisibility(true);
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    Date dataConferma = (Date)parametri.get("dataConferma");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    Vector<PersonaFisicaVO> vPersonaFisica = anagFacadeClient.getSoggetti(aziendaNuovaVO.getIdAzienda(), dataConferma);
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    String parDicSost = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_STR_DICH_SOST_AN);
    
    
    introRichVar.setTitolone(aziendaNuovaVO.getDescTipoRichiesta());
    introRichVar.setSottotitolo(parDicSost);   
    
    PersonaFisicaVO personaFisicaVO = null;
    PersonaFisicaVO rappVO = null;
    for(int i=0;i<vPersonaFisica.size();i++)
    {
      if(ruoloUtenza.getCodiceFiscale().equalsIgnoreCase(vPersonaFisica.get(i).getCodiceFiscale()))
      {
        personaFisicaVO = vPersonaFisica.get(i);
      }
      
      if(vPersonaFisica.get(i).getIdRuolo().compareTo(new Long(1)) == 0)
      {
        rappVO = vPersonaFisica.get(i);
      } 
    }
    
    String cognomeNome = "";
    String ruolo = "";
    if(Validator.isNotEmpty(personaFisicaVO))
    {
      cognomeNome = personaFisicaVO.getCognome()+" "+personaFisicaVO.getNome();
      ruolo = personaFisicaVO.getRuolo();
    }
    else
    {
      cognomeNome = rappVO.getCognome()+" "+rappVO.getNome();
      ruolo = rappVO.getRuolo();
    }
    
    introRichVar.setSottoscritto(cognomeNome);
    introRichVar.setInQualita(ruolo);
    
    introRichVar.setAzienda(anagAziendaVO.getCUAA()+" - "+anagAziendaVO.getDenominazione());
    introRichVar.setMotivoRichiesta(aziendaNuovaVO.getDescEstesaTipoRichiesta());
    introRichVar.setNote(aziendaNuovaVO.getNote());
       
    
  }

}
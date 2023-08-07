package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Dichiarazioni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Firma;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoFirmatarioVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class FirmaSR extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "FIRMA_FASCICOLO";

  public FirmaSR() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
   
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    TipoFirmatarioVO tipoFirmatarioVO = null;
    if(Validator.isNotEmpty(richiestaTipoReportVO.getIdTipoFirmatario()))
    {
      tipoFirmatarioVO = gaaFacadeClient.getTipoFirmatarioById(richiestaTipoReportVO.getIdTipoFirmatario());
    }
    
    if(fascicoDigitale.getDichiarazioni() == null)
    {
      Dichiarazioni dichiarazioni = new Dichiarazioni();
      fascicoDigitale.setDichiarazioni(dichiarazioni);
      dichiarazioni.setVisibility(true);
    }
    Firma firmaMd = new Firma();
    firmaMd.setVisibility(true);
    fascicoDigitale.getDichiarazioni().setFirma(firmaMd);
    
    
    DichiarazioneConsistenzaGaaVO  dichConsistenza = 
      (DichiarazioneConsistenzaGaaVO)parametri.get("dichConsistenza");
    
    InfoFascicoloVO infoFascicoloVO = (InfoFascicoloVO)parametri.get("infoFascicoloVO");
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)parametri.get("personaFisicaVO");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    
    
    //Quadro firma
    if (dichConsistenza != null)
    {  
      String luogoFirma = "";
      //esiste delega attiva
      if(Validator.isNotEmpty(infoFascicoloVO.getDescComuneIntermediario()))
      {
        luogoFirma = infoFascicoloVO.getDescComuneIntermediario()
          + " " + "("+ infoFascicoloVO.getSiglaProvIntermediario()+")";
      }
      else
      {
        luogoFirma = anagAziendaVO.getDescComune()
            + " " + "("+ anagAziendaVO.getSedelegProv()+")";        
      }      
      firmaMd.setLuogoFirma(luogoFirma);
      String data = DateUtils.formatDate(dichConsistenza.getData());
      firmaMd.setDataFirma(data);
      
      
      String luogoEData = "";
      //esiste delega attiva
      if(Validator.isNotEmpty(infoFascicoloVO.getDescComuneIntermediario()))
      {
        luogoEData = infoFascicoloVO.getDescComuneIntermediario()
          + " " + "("+ infoFascicoloVO.getSiglaProvIntermediario()+"), "
          + data;
      }
      else
      {
        luogoEData = anagAziendaVO.getDescComune()
            + " " + "("+ anagAziendaVO.getSedelegProv()+"), "
            + data;        
      }
      firmaMd.setLuogoEData(luogoEData);
      
      //etichetta firma
      String etichettaFirma = "";
      if(Validator.isNotEmpty(tipoFirmatarioVO)
        && "O".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
      {
        etichettaFirma = "Firmato elettronicamente";
      }
      else if("S".equalsIgnoreCase(dichConsistenza.getFlagInvioMail())
        && "S".equalsIgnoreCase(anagAziendaVO.getFascicoloDematerializzato()))
      {
        etichettaFirma = "Firmato elettronicamente del produttore";
      }
      else
      {
        etichettaFirma = "Firma";
      }
      firmaMd.setEtichettaFirma(etichettaFirma);
      
      
      //firmatario
      String firmatario = "";
      if(Validator.isNotEmpty(tipoFirmatarioVO))
      {
        if("T".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
        {
          if("S".equalsIgnoreCase(dichConsistenza.getFlagInvioMail())
              && "S".equalsIgnoreCase(anagAziendaVO.getFascicoloDematerializzato()))
          {
            firmatario = personaFisicaVO.getCognome()+" "+personaFisicaVO.getNome();
          }
        }
        else if("O".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
        {
          AllegatoDichiarazioneVO allegatoDichiarazioneVO = 
              gaaFacadeClient.getAllegatoDichiarazioneFromIdDichiarazione(
              idDichiarazioneConsistenza, richiestaTipoReportVO.getIdTipoAllegato());
          
          //RuoloUtenza ruoloUtenza = anagFacadeClient.serviceGetRuoloUtenzaByIdUtente(allegatoDichiarazioneVO.getIdUtenteAggiornamento());
          UtenteAbilitazioni utenteAbilitazioni =  anagFacadeClient.getUtenteAbilitazioniByIdUtenteLogin(allegatoDichiarazioneVO.getIdUtenteAggiornamento());
          RuoloUtenza ruoloUtenza = new RuoloUtenzaPapua(utenteAbilitazioni);
          firmatario = ruoloUtenza.getDenominazione();
        
        }
        else if("R".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
        {
          InfoFascicoloVO infoFascicoloTempVO = gaaFacadeClient
              .getInfoFascicolo(anagAziendaVO.getIdAzienda(), dichConsistenza.getData(), dichConsistenza.getCodiceFotografia());
          
          firmatario = infoFascicoloTempVO.getResponsabile();        
        }
      }
      
      firmaMd.setFirmatario(firmatario);
      
    
    
    }
    else
    {
      
      String etichettaFirma = "";
      if(Validator.isNotEmpty(tipoFirmatarioVO)
          && "O".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
      {
        etichettaFirma = "Firmato elettronicamente";
      }
      else
      {
        etichettaFirma = "Firma";
      }
      firmaMd.setEtichettaFirma(etichettaFirma);
      
      
      //firmatario
      String firmatario = "";
      if(Validator.isNotEmpty(tipoFirmatarioVO))
      {
        if("T".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
        {}
        else if("O".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
        {}
        else if("R".equalsIgnoreCase(tipoFirmatarioVO.getCodice()))
        {
          InfoFascicoloVO infoFascicoloTempVO = gaaFacadeClient
              .getInfoFascicolo(anagAziendaVO.getIdAzienda(), null, null);
          
          firmatario = infoFascicoloTempVO.getResponsabile();  
          
        } 
      }
      
      firmaMd.setFirmatario(firmatario);
      
      
    }
    
    
    
    
    
  }
  
}
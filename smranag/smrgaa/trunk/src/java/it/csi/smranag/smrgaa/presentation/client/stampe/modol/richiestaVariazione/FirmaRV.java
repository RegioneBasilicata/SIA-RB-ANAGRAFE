package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Dichiarazioni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Firma;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class FirmaRV extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "FIRMA_FASCICOLO";

  public FirmaRV() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
   
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
    
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    Date dataConferma = (Date)parametri.get("dataConferma");
    Vector<PersonaFisicaVO> vPersonaFisica = anagFacadeClient.getSoggetti(aziendaNuovaVO.getIdAzienda(), dataConferma);
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    
    
    
    
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
    String data = DateUtils.formatDate(aziendaNuovaVO.getDataAggiornamentoIter());
    firmaMd.setDataFirma(data);
    String luogoFirma = anagAziendaVO.getDescComune()
            + " " + "("+ anagAziendaVO.getSedelegProv()+")";        
    firmaMd.setLuogoEData(luogoFirma);
      
      
    //firmatario
    String firmatario = "";
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

    if(Validator.isNotEmpty(personaFisicaVO))
    {
      firmatario = personaFisicaVO.getCognome()+" "+personaFisicaVO.getNome();
    }
    else
    {
      firmatario = rappVO.getCognome()+" "+rappVO.getNome();
    }      
      
    firmaMd.setFirmatario(firmatario);
 
    
    
    
    
    
  }
  
}
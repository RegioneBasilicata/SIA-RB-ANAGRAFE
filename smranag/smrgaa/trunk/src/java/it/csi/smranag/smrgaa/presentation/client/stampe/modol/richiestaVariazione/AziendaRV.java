package it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione;

import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroA;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.SezioneAnagrafica;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class AziendaRV extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "AZIENDA";

  public AziendaRV() throws IOException, SolmrException
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
    if(quadroA.getSezioneAnagrafica() == null)
       quadroA.setSezioneAnagrafica(new SezioneAnagrafica());
    
    SezioneAnagrafica sezioneAnagrafica = quadroA.getSezioneAnagrafica();
    sezioneAnagrafica.setVisibility(true);
    sezioneAnagrafica.setTitolo1SezAnagrafica(richiestaTipoReportVO.getQuadro());
    sezioneAnagrafica.setTitolo2SezAnagrafica(richiestaTipoReportVO.getSezione());
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataConferma = (Date)parametri.get("dataConferma");

    sezioneAnagrafica.setCuaa(anagAziendaVO.getCUAA());
    sezioneAnagrafica.setPartitaIva(anagAziendaVO.getPartitaIVA());
    String denominazione = anagAziendaVO.getDenominazione();
    if(Validator.isNotEmpty(anagAziendaVO.getIntestazionePartitaIva()))
      denominazione += " - " +anagAziendaVO.getIntestazionePartitaIva();
    sezioneAnagrafica.setDenominazione(denominazione);
    String indirizzoSedeLeg = anagAziendaVO.getSedelegIndirizzo();
    if(Validator.isNotEmpty(anagAziendaVO.getSedelegCAP()))
      indirizzoSedeLeg += " - " +anagAziendaVO.getSedelegCAP();
    if(Validator.isNotEmpty(anagAziendaVO.getDescComune()))
      indirizzoSedeLeg += " " +anagAziendaVO.getDescComune();
    if(Validator.isNotEmpty(anagAziendaVO.getSedelegProv()))
      indirizzoSedeLeg += " (" +anagAziendaVO.getSedelegProv()+")";
    sezioneAnagrafica.setIndirizzoSedeLeg(indirizzoSedeLeg);
    
    sezioneAnagrafica.setPec(anagAziendaVO.getPec());
    sezioneAnagrafica.setEmail(anagAziendaVO.getMail());
    sezioneAnagrafica.setTel(anagAziendaVO.getTelefono());
    
    String ateco = "";
    if(Validator.isNotEmpty(anagAziendaVO.getTipoAttivitaATECO()))
    {
      ateco = anagAziendaVO.getTipoAttivitaATECO().getDescription();
      ateco += " ("+anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode()+")";
    }
    sezioneAnagrafica.setAteco(ateco);
    
    sezioneAnagrafica.setRegistro(anagAziendaVO.getCCIAAnumRegImprese());
    sezioneAnagrafica.setAnno(anagAziendaVO.getCCIAAannoIscrizione());
    
    Vector<TipoFormaConduzioneVO> vTipoFormaConduzione = gaaFacadeClient.getFormaConduzioneSezioneAnagrafica(
        anagAziendaVO.getIdAzienda(), dataConferma);
    
    if(Validator.isNotEmpty(vTipoFormaConduzione))
    {
      String formaConduzione = "";
      for(int i=0;i<vTipoFormaConduzione.size();i++)
      {
        if(i != 0)
          formaConduzione +=";";
        
        formaConduzione += vTipoFormaConduzione.get(i).getForma();
        if(vTipoFormaConduzione.get(i).getvDescAttivitaComplementari().size() > 0)
        {
          formaConduzione +="(";
          for(int j=0;j<vTipoFormaConduzione.get(i).getvDescAttivitaComplementari().size();j++)
          {
            if(j != 0)
              formaConduzione +=",";
            
            formaConduzione +=vTipoFormaConduzione.get(i).getvDescAttivitaComplementari().get(j);
          }
          
          formaConduzione +=") ";
        }
      }
      sezioneAnagrafica.setFormaConduzione(formaConduzione);
    }
    
    
    Vector<TipoInfoAggiuntivaVO> vTipoInfoAggiuntiva = gaaFacadeClient
        .getTipoInfoAggiuntiveStampa(anagAziendaVO.getIdAzienda(), dataConferma);
    if(Validator.isNotEmpty(vTipoInfoAggiuntiva))
    {
      String altreInfo = "";
      for(int i=0;i<vTipoInfoAggiuntiva.size();i++)
      {
        if(i != 0)
          altreInfo +=";";
        
        altreInfo += vTipoInfoAggiuntiva.get(i).getDescrizione();
      }
       
      sezioneAnagrafica.setAltreInfo(altreInfo);
    }
   
    
    
   
    
    
  }

  
}
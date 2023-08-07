package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Associazione;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Associazioni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroA;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.SezioneAssociazioni;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class AssociazioniSR extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "ASSOCIAZIONI";

  public AssociazioniSR() throws IOException, SolmrException
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
    quadroA.setSezioneAssociazioni(new SezioneAssociazioni());
    SezioneAssociazioni sezioneAssociazioni = quadroA.getSezioneAssociazioni();
    sezioneAssociazioni.setVisibility(true);
    sezioneAssociazioni.setTitoloSezAssociazioni(richiestaTipoReportVO.getSezione());
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    Vector<AnagAziendaVO> vAziendeAssociate = gaaFacadeClient.getAziendeAssociateStampa(
            anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione);
    if(vAziendeAssociate != null)
    {
      sezioneAssociazioni.setAssociazioni(new Associazioni());
      for(int i=0;i<vAziendeAssociate.size();i++)
      {
        Associazione associazione = new Associazione();
        String denominazione = vAziendeAssociate.get(i).getDenominazione();
        if(Validator.isNotEmpty(vAziendeAssociate.get(i).getIntestazionePartitaIva()))
          denominazione += " - "+vAziendeAssociate.get(i).getIntestazionePartitaIva();
            
        associazione.setDenominazione(denominazione);
        associazione.setCuaa(vAziendeAssociate.get(i).getCUAA());
        
        sezioneAssociazioni.getAssociazioni().getAssociazione().add(associazione);
      }
      
    }
    else
    {
      sezioneAssociazioni.setSezioneVuota(true);
    }
    
  }

  
}
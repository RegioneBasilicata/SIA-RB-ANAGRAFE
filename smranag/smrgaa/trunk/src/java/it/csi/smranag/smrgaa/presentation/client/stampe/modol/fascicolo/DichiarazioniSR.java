package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.BloccoConCheck;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.DichiarazioneCheck;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Dichiarazioni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ElencoCheck;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DichiarazioniSR extends SubReportModol
{
 
  private final static String CODICE_SUB_REPORT = "DICHIARAZIONI";

  public DichiarazioniSR() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
        
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    TipoAllegatoVO tipoAllegatoVO = (TipoAllegatoVO)parametri.get("tipoAllegatoVO");
    
    Vector<TipoAttestazioneVO> vAttestazioni = null;

    if(Validator.isNotEmpty(tipoAllegatoVO)
      && Validator.isNotEmpty(tipoAllegatoVO.getCodiceAttestazione()))
    {
      if(Validator.isNotEmpty(codiceFotografia))
      {
        vAttestazioni = gaaFacadeClient.getAttestazioniFromSubReportValidDettagli(
            richiestaTipoReportVO.getIdReportSubReport(), dataInserimentoDichiarazione, codiceFotografia);
    
      }
    }
    else
    {
      if (idDichiarazioneConsistenza == null)
      {
        vAttestazioni = gaaFacadeClient.getListAttestazioniDichiarazioniAlPianoAttuale(
            anagAziendaVO.getIdAzienda(), "DICHIARAZIONI", false);
      }
      else
      {
        vAttestazioni = gaaFacadeClient
            .getListAttestazioniDichiarazioniAllaValidazione(codiceFotografia, dataInserimentoDichiarazione, "DICHIARAZIONI", false);     
      }
    }

    if (vAttestazioni != null)
    {
      if(fascicoDigitale.getDichiarazioni() == null)
      {
        Dichiarazioni dichiarazioni = new Dichiarazioni();
        fascicoDigitale.setDichiarazioni(dichiarazioni);
        dichiarazioni.setVisibility(true);
      }
      
      fascicoDigitale.getDichiarazioni().setBloccoConCheck(new BloccoConCheck());
      fascicoDigitale.getDichiarazioni().getBloccoConCheck().setVisibility(true);
      
      fascicoDigitale.getDichiarazioni().getBloccoConCheck().setElencoCheck(new ElencoCheck());
      for (int i = 0; i < vAttestazioni.size(); i++)
      {
        TipoAttestazioneVO tipoAttestazioneVO = vAttestazioni.get(i);
        DichiarazioneCheck dichiarazioneCheck = new DichiarazioneCheck();
        String descrizione = creaLayoutForAttestazioni(tipoAttestazioneVO.getParametriAttAziendaVO(),
            tipoAttestazioneVO.getParametriAttDichiarataVO(), idDichiarazioneConsistenza, tipoAttestazioneVO);
        dichiarazioneCheck.setValoreDich(descrizione);
        if(tipoAttestazioneVO.isAttestazioneAzienda())
        {
          dichiarazioneCheck.setValoreCheck(new BigInteger("1"));
        }
        else
        {
          dichiarazioneCheck.setValoreCheck(new BigInteger("0"));
        }
        dichiarazioneCheck.setTipoRiga(tipoAttestazioneVO.getTipoRiga());
        dichiarazioneCheck.setColonneRiga(new BigInteger(tipoAttestazioneVO.getNumeroColonneRiga()));

        
        fascicoDigitale.getDichiarazioni().getBloccoConCheck()
          .getElencoCheck().getDichiarazioneCheck().add(dichiarazioneCheck);
      }
      
    }
    
    
    
    
    
  }
  
}
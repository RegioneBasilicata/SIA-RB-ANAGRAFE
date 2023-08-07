package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Allegati;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.AllegatiNormali;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.DichiarazioneCheck;
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

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class AllegatiSR extends SubReportModol
{
 
  private final static String CODICE_SUB_REPORT = "ALLEGATI";

  public AllegatiSR() throws IOException, SolmrException
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

    if (idDichiarazioneConsistenza == null)
    {
      vAttestazioni = gaaFacadeClient.getListAttestazioniDichiarazioniAlPianoAttuale(
          anagAziendaVO.getIdAzienda(), "ALLEGATI", false);
    }
    else
    {
      vAttestazioni = gaaFacadeClient
          .getElencoAllegatiAllaValidazionePerStampa(codiceFotografia, dataInserimentoDichiarazione, tipoAllegatoVO.getCodiceAttestazione());     
    }

    if (vAttestazioni != null)
    {
      if(fascicoDigitale.getAllegati() == null)
      {
        Allegati allegati = new Allegati();
        fascicoDigitale.setAllegati(allegati);
        allegati.setVisibility(true);
      }
      
      fascicoDigitale.getAllegati().setAllegatiNormali(new AllegatiNormali());
      fascicoDigitale.getAllegati().getAllegatiNormali().setVisibility(true);
      fascicoDigitale.getAllegati().getAllegatiNormali().setTitolo(richiestaTipoReportVO.getQuadro());
      
      fascicoDigitale.getAllegati().getAllegatiNormali().setElencoCheck(new ElencoCheck());
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

        
        fascicoDigitale.getAllegati().getAllegatiNormali()
          .getElencoCheck().getDichiarazioneCheck().add(dichiarazioneCheck);
      }
      
    }
    
    
    
    
    
  }
  
}
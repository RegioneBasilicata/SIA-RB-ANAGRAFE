package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Allegati;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.AllegatiCondizionalita;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.DichiarazioneCheck;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ElencoCheck;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
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

public class AllegatiCondizionalitaSR extends SubReportModol
{
 
  private final static String CODICE_SUB_REPORT = "CONDIZIONALITA";

  public AllegatiCondizionalitaSR() throws IOException, SolmrException
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

    Vector<TipoAttestazioneVO> vAttestazioni = null;

    if (idDichiarazioneConsistenza == null)
    {
      vAttestazioni = gaaFacadeClient.getListAttestazioniDichiarazioniAlPianoAttuale(
          anagAziendaVO.getIdAzienda(), "ALLEGATI", true);
    }
    else
    {
      vAttestazioni = gaaFacadeClient
          .getElencoAllegatiAllaValidazionePerStampa(codiceFotografia, dataInserimentoDichiarazione, "COND");     
    }

    if (vAttestazioni != null)
    {
      if(fascicoDigitale.getAllegati() == null)
      {
        Allegati allegati = new Allegati();
        fascicoDigitale.setAllegati(allegati);
        allegati.setVisibility(true);
      }
      
      fascicoDigitale.getAllegati().setAllegatiCondizionalita(new AllegatiCondizionalita());
      fascicoDigitale.getAllegati().getAllegatiCondizionalita().setVisibility(true);
      fascicoDigitale.getAllegati().getAllegatiCondizionalita().setTitolo(richiestaTipoReportVO.getQuadro());
      
      fascicoDigitale.getAllegati().getAllegatiCondizionalita().setElencoCheck(new ElencoCheck());
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

        
        fascicoDigitale.getAllegati().getAllegatiCondizionalita()
          .getElencoCheck().getDichiarazioneCheck().add(dichiarazioneCheck);
      }
      
    }
    
    
    
    
    
  }
  
}
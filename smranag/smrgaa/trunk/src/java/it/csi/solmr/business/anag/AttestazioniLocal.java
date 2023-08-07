package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface AttestazioniLocal
{

  public TipoAttestazioneVO[] getListTipoAttestazioneOfPianoRiferimento(
      Long idAzienda, String codiceFotografiaTerreni,
      java.util.Date dataAnnoCampagna, java.util.Date dataVariazione, String[] orderBy, 
      String voceMenu) throws Exception;

  public boolean isAttestazioneDichiarata(String codiceFotografiaTerreni)
      throws Exception;
  
  public boolean isAttestazioneAzienda(long idAzienda)
      throws Exception;

  public void aggiornaAttestazioniPlSql(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza, String codAttestazione) throws SolmrException, Exception;

  public void aggiornaAttestazioni(String[] elencoIdAttestazioni,
      AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza,
      Hashtable<?, ?> elencoParametri, String voceMenu,
      ConsistenzaVO consistenzaVO) throws Exception;
  
  public void aggiornaElencoAllegatiAttestazioni(String[] elencoIdAttestazioni, 
      RuoloUtenza ruoloUtenza, Hashtable<Long,ParametriAttDichiarataVO> elencoParametri, 
      TipoAllegatoVO tipoAllegatoVO, ConsistenzaVO consistenzaVO) throws Exception;

  public TipoAttestazioneVO[] getListTipoAttestazioneForUpdate(Long idAzienda,
      String[] orderBy, String voceMenu) throws Exception;

  public TipoParametriAttestazioneVO findTipoParametriAttestazioneByIdAttestazione(
      Long idAttestazione) throws Exception;

  public boolean getDataAttestazioneAllaDichiarazione(
      String codiceFotografiaTerreni,
      java.util.Date dataInserimentoDichiarazione, boolean flagVideo,
      boolean flagStampa, String codiceAttestazione, String voceMenu)
      throws Exception;

  public TipoAttestazioneVO[] getElencoTipoAttestazioneAlPianoAttuale(
      boolean flagVideo, boolean flagStampa, String[] orderBy,
      String codiceAttestazione, String voceMenu) throws Exception;

  public TipoAttestazioneVO[] getTipoAttestazioneAllegatiAllaDichiarazione(
      String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
      java.util.Date dataVariazione, String[] orderBy) throws Exception;
  
  public Vector<TipoAttestazioneVO> getElencoAttestazioniAllaDichiarazione(
      String codiceFotografiaTerreni, Date dataAnnoCampagna, 
      String codiceAttestazione) throws Exception;
  
  public AttestazioneAziendaVO getFirstAttestazioneAzienda(long idAzienda) throws Exception;
  
  public AttestazioneDichiarataVO getFirstAttestazioneDichiarata(long codiceFotografia) throws Exception;
  
  public Vector<java.util.Date> getDateVariazioniAllegati(long codiceFotografia) throws Exception;
  
}

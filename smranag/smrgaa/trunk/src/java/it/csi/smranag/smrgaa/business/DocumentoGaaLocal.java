package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.TipoFirmaVO;
import it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaIstanzaRiesameVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;
import java.util.Vector;

import javax.ejb.Local;




/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

@Local
public interface DocumentoGaaLocal 
{
  public Vector<BaseCodeDescription> getCategoriaDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException;
  
  public Vector<BaseCodeDescription> getDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException;
  
  public Vector<BaseCodeDescription> getProtocolliDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException;
  
  public boolean isDocumentoIstanzaRiesame(long idDocumento) throws SolmrException;
  
  public Vector<BaseCodeDescription> getCausaleModificaDocumentoValid() throws SolmrException;
  
  public Vector<Long> esisteDocumentoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException;
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegati(
      long idDocumento)
  throws SolmrException;
  
  public void deleteFileAllegato(long idAllegato, long idDocumento) 
      throws SolmrException;
  
  public void deleteFileAllegatoRichiesta(long idAllegato, long idRichiestaDocumento) 
      throws SolmrException;
  
  public void deleteFileAllegatoNotifica(long idAllegato, long idNotifica) 
      throws SolmrException;
  
  public Long insertFileAllegato(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException;
  
  public Long insertFileAllegatoSemplice(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException;
  
  public Long insertFileAllegatoRichiesta(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException;
  
  public Long insertFileAllegatoNotifica(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException;
  
  public AllegatoDocumentoVO getFileAllegato(long idAllegato) throws SolmrException;
  
  public Date getFirstDataInserimentoDocumento(long idDocumento)
      throws SolmrException;
  
  public boolean isIstanzaRiesameFotoInterpretataByDocumento(long idDocumento) 
      throws SolmrException;
  
  public boolean exitsOtherDocISForParticellaAndAzienda(long idParticella, long idAzienda, Long idDocumento) 
      throws SolmrException;
  
  //public boolean isFaseIstanzaRiesameEvasa(long idAzienda, int fase, Vector<Long> vIdParticella, String parametro) 
      //throws SolmrException;
  
  public boolean exsitsDocFaseIstanzaRiesameFasePrec(long idAzienda, int fase, 
      long idParticella, String parametro) throws SolmrException;
  
  public boolean isPossCreateDocFaseIstanzaRiesameFaseSucc(long idAzienda, long idParticella,
      int idFase, String parametro) throws SolmrException;
  
  public Date getDateLastBatchIstanzaRiesameOk() throws SolmrException;
  
  public boolean isSitiConvocaValid(long idAzienda, int anno, int fase) throws SolmrException;
  
  public boolean isDataSospensioneScaduta(long idAzienda, long idParticella, int anno) throws SolmrException;
  
  public boolean isParticellaEvasa(long idAzienda, long idParticella, int fase, int anno) 
      throws SolmrException;
      
  public boolean isVisibleTastoElimina(long idAzienda, int anno, int fase)  throws SolmrException;
  
  public Vector<DocumentoVO> getListDocFromRicerca(String strDescDocumento, boolean attivi) 
      throws SolmrException;
  
  public Vector<ParticellaIstanzaRiesameVO> getLisParticellaFromIstanzaFoto(long idAzienda, int anno)  
      throws SolmrException;
  
  public String annullaIstanzaRiesame(Vector<Long> vIdIstanzaRiesame, long idAzienda, int anno, 
      RuoloUtenza ruoloUtenza)  throws SolmrException;
  
  public boolean existAltraFaseFotoParticella(long idAzienda, long idParticella, int anno, long idFase)
      throws SolmrException;
  
  public Vector<TipoDocumentoVO> getDocumentiNuovaIscrizione() 
      throws SolmrException;
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiRichiesta(
      long idRichiestaDocumento) throws SolmrException;
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiNotifica(
      long idNotifica) throws SolmrException;
  
  public boolean isIstanzaAttiva(long idAzienda) throws SolmrException;
  
  public boolean isParticellaIstRiesameCancellabile(long idAzienda, 
      Vector<Long> vIdConduzioneParticella) throws SolmrException;
  
  public boolean existIstanzaEsameAttivaFase(long idAzienda, 
      Long idParticella, long idFase) throws SolmrException;
  
  public FaseRiesameDocumentoVO getFaseRiesameDocumentoByIdDocumento(long idDocumento) 
      throws SolmrException;
  
  public boolean isParticellaInPotenziale(long idAzienda, 
      long idParticella, int anno) throws SolmrException;
  
  public boolean isParticellaInPotenzialeContra(long idAzienda, 
      long idParticella, int anno) throws SolmrException;
  
  public void insertAllegatoDocumento(long idDocumento, long idAllegato) 
      throws SolmrException;
  
  public Long insertFileAllegatoNoFile(AllegatoDocumentoVO allegatoDocumentoVO)
      throws SolmrException;
  
  public void insertAllegatoDichiarazione(long idDichiarazioneConsistenza, long idAllegato)
      throws SolmrException;
  
  public boolean isNotPossibleIstanzaRiesameFaseSuccessiva(long idAzienda, 
      long idParticella, int idFase, int anno, String parametro) 
    throws SolmrException;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws SolmrException;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws SolmrException;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdAllegato(
      long idAllegato)
    throws SolmrException;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(
      long idDichiarazioneConsistenza, String tipoStampa)
    throws SolmrException;
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException;
  
  public void storicizzaAllegatoDichiarazione(long idAllegatoDichiarazione)
      throws SolmrException;
  
  public void storicizzaAllegatiAutomaticiDichiarazione(long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public void updateAllegatoForFirma(long idAllegato, long idUtente, Long idTipoFirma)
      throws SolmrException;
  
  public void updateAllegatoForExtIdDocIndex(long idAllegato, long idUtente, Long extIdDocumentoIndex) 
      throws SolmrException;
  
  public void deleteAllegatoFromVId(Vector<Long> vIdAllegato)
      throws SolmrException;
  
  public void deleteAllegatoDichiarazioneFromVId(Vector<Long> vIdAllegatoDichiarazione) 
      throws SolmrException;
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiDichiarazioneDefault(
      int idMotivoDichiarazione) 
    throws SolmrException;
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiAttiviDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException;
  
  public Vector<AllegatoDichiarazioneVO> getAllElencoAllegatiDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException;
  
  public Vector<TipoFirmaVO> getElencoTipoFirma()
      throws SolmrException;
}

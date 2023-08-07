package it.csi.solmr.business.anag.stampe;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.stampe.ConsistenzaZootecnicaStampa;
import it.csi.smranag.smrgaa.dto.stampe.QuadroDTerreni;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface StampeLocal
{
  public Vector<ParticellaVO> getElencoParticelleQuadroI1(Long idAzienda,Long codFotografia)
      throws SolmrException, Exception;
  
  public BigDecimal[] getTotSupQuadroI1CondottaAndAgronomica(Long idAzienda,Long codFotografia)
    throws SolmrException, Exception;
  
  public BigDecimal[] getTotSupQuadroI1CatastaleAndGrafica(Long idAzienda,Long codFotografia)
    throws SolmrException, Exception;

  public Long getCodFotTerreniQuadroI1(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;

  public Vector<UteVO> getUteQuadroB(Long idAzienda, java.util.Date dataRiferimento)
      throws SolmrException, Exception;

  public Vector<TipoFormaConduzioneVO> getFormeConduzioneQuadroD()
      throws SolmrException, Exception;

  public Long getFormaConduzioneQuadroD(Long idAzienda, java.util.Date dataRiferimento)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getAttivitaComplementariQuadroE(Long idAzienda, java.util.Date dataRiferimento)
      throws SolmrException, Exception;

  public Long getIdManodoperaQuadroF(Long idAzienda, java.util.Date dataRiferimento)
      throws SolmrException, Exception;

  public Vector<Long> getAllevamentiQuadroG(Long idAzienda, java.util.Date dataRiferimento)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getFabbricatiParticelle(Long idFabbricato, java.util.Date dataRiferimento)
      throws SolmrException, Exception;
  
  public Vector<FabbricatoVO> getFabbricati(
      Long idAzienda, java.util.Date dataRiferimento,boolean comunicazione10R) 
      throws SolmrException, Exception;

  public DocumentoVO getDettaglioDocumento(Long idDocumento,java.util.Date dataConsistenza,Long idDichiarazioneConsistenza)
      throws Exception, SolmrException;

  public Vector<DocumentoVO> getDocumenti(String idDocumenti[])
      throws Exception;
  
  public Vector<BaseCodeDescription> getTerreniQuadroI4(Long idAzienda, Long codFotografia) 
    throws SolmrException, Exception;
  
  public Vector<BaseCodeDescription> getTerreniQuadroI5(Long idAzienda,java.util.Date dataRiferimento, Long codFotografia) 
    throws SolmrException, Exception;
  
  
  /***************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * 
   * 
   * inizio metodi usati dalla stampa della comunicazione 10R
   * 
   * 
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   */
  
  //===================================================================
  // QUADRO C - Consistenza zootecnica
  //
  // prendere i dati da DB_ALLEVAMENTO
  // ===================================================================
  public Vector<ConsistenzaZootecnicaStampa> getAllevamentiQuadroC10R(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception;
  
  
  //===================================================================
  // QUADRO D - Terreni
  //
  // all situazione attuale
  // ===================================================================
  public Vector<QuadroDTerreni> getTerreniQuadroD10R(Long idAzienda) throws SolmrException, Exception;
  
  //===================================================================
  // QUADRO D - Terreni
  //
  // alla dichiarazione di consistenza
  // ===================================================================
  public Vector<QuadroDTerreni> getTerreniQuadroD10R(java.util.Date dataRiferimento,Long codFotografia) 
    throws SolmrException, Exception;
  
  //===================================================================
  // QUADRO N - Anomalie
  //
  // alla dichiarazione di consistenza
  // ===================================================================
  
  public Vector<String[]> getAnomalie(Long idAzienda, Long idDichiarazioneConsistenza) 
    throws SolmrException, Exception;
  
  //===================================================================
  // Quadro O - Documenti
  //
  // Stampa alla situazione attiva
  //
  // Recupero i documenti associati all'azienda
  // ===================================================================
  public Vector<DocumentoVO> getDocumentiStampa(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException, Exception;


}

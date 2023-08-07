package it.csi.solmr.business.anag;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.UfficioZonaIntermediarioVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AnagAAEPAziendaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagraficaAzVO;
import it.csi.solmr.dto.anag.AziendaCollegataVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.IntermediarioAnagVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.ProcedimentoAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.services.DelegaAnagrafeVO;
import it.csi.solmr.dto.anag.sian.SianFascicoloResponseVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.dto.uma.DittaUMAVO;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.SolmrException;

import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;

@Local
public interface AnagAziendaLocal
{
  public AnagAziendaVO getAziendaById(Long idAnagAzienda)
      throws NotFoundException, Exception;

  public AnagAziendaVO getAziendaCUAA(String CUAA, Date dataSituazioneAl)
      throws NotFoundException, Exception, SolmrException;

  public Vector<AnagAziendaVO> getAziendaCUAA(String CUAA)
      throws NotFoundException, Exception, SolmrException;

  public Vector<AnagAziendaVO> getAziendaByCriterioCessataAndProvvisoria(
      String CUAA) throws Exception, SolmrException;

  public AnagAziendaVO getAziendaPartitaIVA(String partitaIVA,
      Date dataSituazioneAl) throws NotFoundException, Exception,
      SolmrException;

  public Vector<Long> getListIdAziende(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool) throws NotFoundException,
      Exception, SolmrException;

  public Vector<Long> getListIdAziendeFlagProvvisorio(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool, boolean provvisorio)
      throws NotFoundException, Exception, SolmrException;

  public Vector<Long> getListOfIdAzienda(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool) throws NotFoundException,
      Exception, SolmrException;

  public Vector<AnagAziendaVO> getListAziendeByIdRange(
      Vector<Long> idAnagAzienda) throws NotFoundException, Exception,
      SolmrException;

  public Vector<AnagAziendaVO> getAziendeByIdAziendaRange(
      Vector<Long> idAnagAzienda) throws NotFoundException, Exception,
      SolmrException;

  public Vector<AnagAziendaVO> getListAziendeByIdRangeFromIdAzienda(
      Vector<Long> idAzienda) throws NotFoundException, Exception,
      SolmrException;

  public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(
      Long idAzienda, Date dataSituazioneAl) throws NotFoundException,
      Exception, SolmrException;

  public void updateSedeLegale(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento) throws SQLException, Exception,
      NotFoundException, DataAccessException, DataControlException;

  public void storicizzaSedeLegale(AnagAziendaVO anagAziendaVO) throws SQLException, Exception,
      NotFoundException, DataAccessException, SolmrException,
      DataControlException;

  public void checkDataCessazione(Long anagAziendaPK, String dataCessazione)
      throws Exception, SolmrException;

  public void cessaAzienda(AnagAziendaVO anagVO, Date dataCess, String causale,
      long idUtenteAggiornamento) throws Exception, SolmrException;

  public AnagAziendaVO findAziendaAttiva(Long idAzienda)
      throws DataAccessException, NotFoundException, Exception;

  public void checkCUAAandCodFiscale(String cuaa, String partitaIVA)
      throws DataAccessException, Exception, SolmrException;

  public AnagAziendaVO getAltraAziendaFromPartitaIVA(String partitaIVA,
      Long idAzienda) throws DataAccessException, Exception,
      SolmrException;

  public AnagAziendaVO getAziendaCUAAandCodFiscale(String cuaa,
      String partitaIVA) throws DataAccessException, Exception,
      SolmrException;

  public void checkPartitaIVA(String partitaIVA, Long idAzienda)
      throws DataAccessException, Exception, SolmrException;

  public void checkCUAA(String cuaa) throws DataAccessException,
      Exception, SolmrException;

  public void checkIsCUAAPresent(String cuaa, Long idAzienda)
      throws Exception, SolmrException;

  public PersonaFisicaVO getPersonaFisica(String cuaa)
      throws DataAccessException, Exception, SolmrException;

  public Long insertAzienda(AnagAziendaVO aaVO, PersonaFisicaVO pfVO,
      UteVO ute, long idUtenteAggiornamento,
      SianFascicoloResponseVO sianFascicoloResponseVO)
      throws DataAccessException, Exception, SolmrException;

  public void countUteByAziendaAndComune(Long idAzienda, String comune)
      throws DataAccessException, Exception, SolmrException;

  public void insertUte(UteVO uVO, long idUtenteAggiornamento)
      throws DataAccessException, Exception, SolmrException;

  public void cambiaRappresentanteLegale(Long aziendaPK,
      PersonaFisicaVO personaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public ComuneVO getComuneByCUAA(String cuaa) throws Exception,
      SolmrException;

  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
      throws Exception, SolmrException;

  public ComuneVO getComuneByISTAT(String istat) throws Exception,
      SolmrException;

  public String getComuneFromCF(String codiceFiscale) throws Exception,
      SolmrException;

  public void updateAzienda(AnagAziendaVO anagVO)
      throws Exception, SolmrException;

  public void storicizzaAzienda(AnagAziendaVO anagVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void utenteConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws Exception, SolmrException;

  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public String getFlagPartitaIva(Long idTipoFormaGiuridica)
      throws Exception, SolmrException;
  
  public String getObbligoGfFromFormaGiuridica(Long idTipoFormaGiuridica)
      throws Exception, SolmrException;

  public Long getIdTipologiaAziendaByFormaGiuridica(Long idTipoFormaGiuridica,
      Boolean flagAziendaProvvisoria) throws Exception, SolmrException;

  public Long getIdTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws Exception, SolmrException;

  public String getDescTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws Exception, SolmrException;

  public boolean isProvinciaReaValida(String siglaProvincia)
      throws Exception, SolmrException;

  public boolean isFlagUnivocitaAzienda(Integer idTipoAzienda)
      throws Exception, SolmrException;

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
      Long idAnagAzienda) throws Exception, SolmrException;

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws Exception,
      SolmrException;
  
  public Vector<PersonaFisicaVO> getVAltriSoggettiFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) 
    throws Exception, SolmrException;

  public UtenteIrideVO getUtenteIrideById(Long idUtente)
      throws Exception, SolmrException;

  public Long updateTitolareAzienda(AnagAziendaVO anagAziendaVO,
      PersonaFisicaVO personaTitolareOldVO,
      PersonaFisicaVO personaTitolareNewVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa,
      String partitaIva) throws Exception, SolmrException;

  public void insertRappLegaleTitolare(Long idAzienda, PersonaFisicaVO pfVO,
      long idUtenteAggiornamento) throws Exception, SolmrException;

  public String getDenominazioneByIdAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public String getRappLegaleTitolareByIdAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public Vector<AnagAziendaVO> findAziendeByIdAziende(Vector<Long> idAziendeVect)
      throws Exception, SolmrException;

  public String getSiglaProvinciaByIstatProvincia(String istatProvincia)
      throws Exception, SolmrException;

  public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)
      throws Exception, SolmrException;

  public Vector<AnagAziendaVO> getListaStoricoAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public Long storicizzaDatiResidenza(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void changeLegameBetweenPersoneAndAziende(Long newIdSoggetto,
      Long oldIdSoggetto, Long idAzienda) throws SolmrException, Exception;

  public void cessaLegameBetweenPersonaAndAzienda(Long idSoggetto,
      Long idAzienda) throws SolmrException, Exception;

  public Vector<String> getListCUAAByIdAzienda(Long idAzienda)
      throws Exception;

  public String getDenominazioneAziendaByCuaaAndIdAzienda(Long idAzienda,
      String cuaa) throws Exception;

  // INIZIO PROFILAZIONE
  public DelegaVO intermediarioConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws Exception, SolmrException;

  public boolean isIntermediarioConDelegaDiretta(long idIntermediario,
      Long idAzienda) throws Exception;

  public boolean isIntermediarioPadre(long idIntermediario, Long idAzienda)
      throws Exception;

  // FINE PROFILAZIONE

  // INIZIO PRATICHE
  public Vector<ProcedimentoAziendaVO> updateAndGetPraticheByAzienda(
      Long idAzienda) throws SolmrException, Exception;

  public CodeDescription[] getListCuaaAttiviProvDestByIdAzienda(Long idAzienda)
      throws Exception;

  // FINE PRATICHE

  // INIZIO ANAGRAFICA AZIENDA
  public void updateAnagrafe(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento, PersonaFisicaVO pfVO, boolean isCuaaChanged, 
      PersonaFisicaVO pfVOTributaria, Vector<Long> vIdAtecoTrib) throws Exception;
  
  public void updateAnagrafeSemplice(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) 
      throws Exception;

  public void checkStatoAzienda(Long idAzienda) throws SolmrException,
      Exception;

  public Date getMinDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws Exception;

  public void modificaGestoreFascicolo(AnagAziendaVO anagAziendaVO)
      throws Exception;

  public java.util.Date getDataMaxFineMandato(Long idAzienda)
      throws Exception;

  public Long insertDelegaForMandato(AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, DelegaVO delegaVO, DocumentoVO documentoVO)
      throws Exception, SolmrException;

  public DelegaVO getDelegaByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento) throws Exception;

  public java.util.Date getDataMaxInizioMandato(Long idAzienda)
      throws Exception;

  public DelegaVO[] getStoricoDelegheByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento, String[] orderBy) throws Exception;

  public AnagAziendaVO[] getListAnagAziendaVOByCuaa(String cuaa,
      boolean onlyActive, boolean isCessata, String[] orderBy)
      throws Exception;

  public Date getMaxDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws Exception;

  public AnagAziendaVO[] getListAnagAziendaDestinazioneByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws Exception;

  public Vector<AziendaCollegataVO> getAziendeCollegateByIdAzienda(
      Long idAzienda, boolean flagStorico) throws Exception,
      SolmrException;

  public Vector<AnagAziendaVO> getEntiAppartenenzaByIdAzienda(Long idAzienda,
      boolean flagStorico) throws Exception, SolmrException;

  public AnagAziendaVO findAziendaByIdAnagAzienda(Long anagAziendaPK)
      throws Exception, SolmrException;

  public Vector<AziendaCollegataVO> getAziendeCollegateByRangeIdAziendaCollegata(
      Vector<Long> vIdAziendaCollegata) throws Exception, SolmrException;

  public Vector<AnagAziendaVO> getIdAziendaCollegataAncestor(Long idAzienda)
      throws Exception, SolmrException;

  public Vector<AnagAziendaVO> getIdAziendaCollegataDescendant(Long idAzienda)
      throws Exception, SolmrException;

  public boolean controlloAziendeAssociate(String CUAApadre,
      Long idAziendaCollegata) throws Exception;

  public Vector<AnagAziendaVO> getAziendeCollegateByRangeIdAzienda(
      Vector<Long> vIdAzienda, Long idAziendaPadre) throws Exception,
      SolmrException;

  public void storicizzaAziendeCollegateBlocco(RuoloUtenza ruoloUtenza,
      Vector<AziendaCollegataVO> vAnagAziendaCollegateVO)
      throws Exception, SolmrException;

  public void eliminaAziendeCollegateBlocco(long idUtenteAggiornamento,
      Long idAziendaFather, Vector<Long> vIdAziendaVO) throws Exception,
      SolmrException;

  // FINE ANAGRAFICA AZIENDA

  public Vector<AnagAziendaVO> getElencoAziendeByCAA(DelegaVO delegaVO)
      throws Exception, SolmrException;

  public Long importaDatiAAEP(AnagAAEPAziendaVO anagAAEPAziendaVO,
      AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento, boolean denominazione,
      boolean partitaIVA, boolean descrizioneAteco, boolean provinciaREA,
      boolean numeroREA, boolean annoIscrizione, boolean numeroRegistroImprese, boolean pec,
      boolean sedeLegale, boolean titolareRappresentante, boolean formaGiuridica,boolean sezione,
      boolean descrizioneAtecoSec, boolean dataIscrizioneREA, boolean dataCancellazioneREA, boolean dataIscrizioneRI)
      throws Exception, SolmrException;

  /**
   * I seguenti metodi sono usati solo per fornire dei servizi (non per uma)
   * 
   * @param idAzienda
   *          Long
   * @throws Exception
   * @throws SolmrException
   * @return AnagAziendaVO
   */
  public AnagAziendaVO getAziendaByIdAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public Vector<AnagAziendaVO> getAssociazioniCollegateByIdAzienda(
      Long idAzienda, Date dataFineValidita) throws Exception,
      SolmrException;

  public AnagAziendaVO[] serviceGetListAziendeByIdRange(
      Vector<Long> collIdAziende, Date dataSituazioneAl)
      throws Exception, SolmrException;

  public AnagAziendaVO serviceGetAziendaById(Long idAzienda,
      Date dataSituazioneAl) throws Exception, SolmrException;

  public DelegaVO getIntermediarioPerDelega(Long idIntermediario)
      throws Exception, SolmrException;

  public IntermediarioAnagVO getIntermediarioAnagByIdIntermediario(
      long idIntermediario) throws Exception;
  
  public IntermediarioAnagVO findIntermediarioVOByCodiceEnte(String codEnte)
      throws Exception;
  
  public IntermediarioAnagVO findIntermediarioVOByIdAzienda(long idAzienda)
      throws Exception;
  
  public boolean isAziendaIntermediario(long idAzienda)
      throws Exception;
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioVOById(long idIntemediario)
      throws Exception;
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioAziendaVOById(long idIntemediario)
      throws Exception;

  public boolean controllaRegistrazioneMandato(AnagAziendaVO aziendaVO,
      String codiceEnte, DelegaAnagrafeVO delegaAnagrafeVO)
      throws Exception, SolmrException;
  
  public boolean controllaRevocaMandato(AnagAziendaVO aziendaVO,
      RuoloUtenza ruoloUtenza, DelegaAnagrafeVO delegaAnagrafeVO)
      throws Exception, SolmrException;

  /**
   * Questo metodo controlla se è presente una delega per questa azienda
   * relativamente al procedimento di anagrafe. Se è presente restituisce true.
   * Se non è presente restituisce false
   * 
   * @param aziendaVO
   *          AnagAziendaVO
   * @throws Exception
   * @return boolean
   */
  public boolean controllaPresenzaDelega(AnagAziendaVO aziendaVO)
      throws Exception;

  // Metodo per recuperare recuperare l'ufficio zona intermediario partendo
  // dalla chiave primaria
  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByPrimaryKey(
      Long idUfficioZonaIntermediario) throws Exception;

  /*
   * Aggiunto il 6/12/2004 per aggirare (temporaneamente) il mancato inserimento
   * della chiamata al metodo omonimo di DittaUmaDAO di UMA tramite CSI
   */
  // #-#
  public DittaUMAVO getDittaUmaByIdAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public void storicizzaDelega(DelegaVO dVO, RuoloUtenza ruoloUtenza,
      DocumentoVO documentoVO, AnagAziendaVO anagAziendaVO)
      throws Exception;
  
  public int storicizzaDelegaTemporanea(DelegaVO delegaVO, RuoloUtenza ruoloUtenza, 
      AnagAziendaVO anagAziendaVO) throws Exception;
  

  public void updateInsediamentoGiovani(Long idAzienda) throws Exception;

  public boolean testResources() throws Exception;

  public String[] cessazioneAziendaPLQSL(Long idAzienda)
      throws Exception, SolmrException;

  public boolean controllaObbligoFascicolo(AnagAziendaVO aziendaVO)
      throws Exception;

  public Boolean serviceIsEsenteDelega(Long idAzienda) throws Exception,
      SolmrException;

  public DelegaAnagrafeVO serviceGetDelega(Long idAzienda, String codiceEnte,
      Boolean ricercaSuEntiFigli, Date data) throws Exception,
      SolmrException;

  public Vector<AnagAziendaVO> getAziendaByIntermediarioAndCuaa(
      Long intermediario, String cuaa) throws Exception,
      NotFoundException, SolmrException;

  public Vector<AnagAziendaVO> getAziendeByListOfId(Vector<Long> vIdAzienda)
      throws Exception, NotFoundException, SolmrException;

  public void storicizzaDelegaBlocco(RuoloUtenza ruoloUtenza,
      Vector<AnagAziendaVO> vAnagAziendaVO, String oldIntermediario,
      String newIntermediario) throws Exception, SolmrException;

  public String[] getCUAACollegati(String cuaa) throws Exception;

  public Vector<Long> getIdAnagAziendeCollegatebyCUAA(String cuaa)
      throws Exception;

  public Vector<ProcedimentoAziendaVO> getElencoProcedimentiByIdAzienda(
      Long idAzienda, Long annoProc, Long idProcedimento,
      Long idAziendaSelezionata) throws Exception, NotFoundException,
      SolmrException;

  public AnagAziendaVO[] getListAziendeParticelleAsservite(
      Long idStoricoParticella, Long idAzienda, Long idTitoloPossesso, Date dataInserimentoDichiarazione)
      throws Exception;

  public boolean isCUAAAlreadyPresentInsediate(String cuaa)
      throws Exception;

  public void insediamentoAtomico(AnagAziendaVO modAnagAziendaVO,
      PersonaFisicaVO modPersonaVO, HttpServletRequest request,
      AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento)
      throws Exception, Exception;
  
  public boolean getDelegaBySocio(String codFiscIntermediario, Long idAziendaAssociata) 
    throws Exception;
  
  public AziendaCollegataVO findAziendaCollegataByFatherAndSon(Long idAziendaFather, Long idAziendaSon,
      Date dataSituazione) throws Exception;
  
  public boolean isSoggettoAssociatoByFatherAndSon(Long idAziendaFather, String cuaaSon,
      Date dataSituazione) throws Exception;

}

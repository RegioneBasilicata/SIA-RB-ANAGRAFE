package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO;
import it.csi.solmr.dto.anag.EsitoPianoGraficoVO;
import it.csi.solmr.dto.anag.TemporaneaPraticaAziendaVO;
import it.csi.solmr.dto.anag.consistenza.FascicoloNazionaleVO;
import it.csi.solmr.dto.anag.consistenza.TipoControlloVO;
import it.csi.solmr.dto.anag.consistenza.TipoMotivoDichiarazioneVO;
import it.csi.solmr.dto.anag.terreni.DichiarazioneSegnalazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface ConsistenzaLocal
{
  // Metodo per controllare se l'azienda idAzienda ha fatto una previsione del
  // piano
  // colturale per l'anno sucessivo. Restituisce true se è stata fatta la
  // previsione,
  // false se non è stata fatta
  public boolean previsioneAnnoSucessivo(Long idAzienda) throws Exception;

  // Metodo per controllare se sono state fatte delle modifiche a seguito
  // dell'ultima
  // dichiarazione. Restituisce true se sono state fatte modifiche, false
  // altrimenti
  public boolean controlloUltimeModifiche(Long idAzienda, Integer anno)
      throws Exception;

  /**
   * Metodo per richiamare la procedura plsql per i controlli sulla consistenza
   * Il parametro restituito può assumere tre valori: - N: richiedere la
   * motivazione della dichiarazione e permettere il salvataggio della
   * dichiarazione di consistenza - E: visualizzare gli errori in ordine di
   * argomento e non permettere il proseguimento - A: visualizzare le anomalie,
   * richiedere la motivazione della dichiarazione e permettere il salvataggio
   */
  public String controlliDichiarazionePLSQL(Long idAzienda, Integer anno,
      Long idMotivoDichiarazione, Long idUtente) throws Exception, SolmrException;

  /**
   * Metodo per richiamare la procedura plsql per i controlli sulla particelle
   */
  public void controlliParticellarePLSQL(Long idAzienda, Integer anno, Long idUtente)
      throws Exception, SolmrException;

  /**
   * Metodo per richiamare la procedura plsql per la verifica dei terreni (
   * idGruppoControllo = 1) o della consistenza (idGruppoControllo = null) Il
   * parametro restituito può assumere tre valori: - N: I dati relativi alla
   * consistenza risultano corretti - S: visualizzare le anomalie
   */
  public String controlliVerificaPLSQL(Long idAzienda, Integer anno,
      Integer idGruppoControllo, Long idUtente) throws Exception, SolmrException;

  // Metodo per recuperare l'elenco degli errori o delle anomalie dovute ad
  // una dichiarazione di consistenza
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(Long idAzienda, Long fase, Long idMotivoDichiarazione)
      throws Exception;
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsPG(long idDichiarazioneConsistenza, 
      long fase) throws Exception;

  // Metodo per recuperare l'elenco degli errori o delle anomalie dovute ad
  // una dichiarazione di consistenza relativamente ai terreni
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsistenzaTerreni(Long idAzienda)
      throws Exception;


  /**
   * Questo metodo viene utilizzato per fare il salvataggio della dichiarazione: -
   * salvataggio della testata su db_dichiarazione_consistenza - fare una
   * fotografia dei terreni dell'azienda nel caso in cui siano stati
   * modificati(db_conduzione_particella in db_conduzione_dichiarata e
   * db_utilizzo_particella in db_utilizzo dichiarato) - salvataggio delle
   * anomalie riscontrate
   * 
   * Viene usata una procedura PLSQL
   */
  public Long salvataggioDichiarazionePLQSL(ConsistenzaVO consistenzaVO,
      Long idAzienda, Integer anno, RuoloUtenza ruoloUtenza)
      throws Exception, SolmrException;

  /**
   * Metodo per richiamare la procedura plsql per i controlli sull'insediamento
   * giovani Il parametro restituito può assumere tre valori: - uguale ad E:
   * chiedere conferma per il proseguimento - diverso da E: non permettere di
   * proseguire à E necessario risolvere le anomalie bloccanti
   */
  public String controlliInsediamentoPLSQL(Long idAzienda, Long idUtente)
      throws Exception, SolmrException;

  // Metodo per recuperare l'elenco delle dichiarazione di consistenza di
  // un azienda
  public Vector<ConsistenzaVO> getDichiarazioniConsistenza(Long idAzienda)
      throws Exception;

  // Metodo per recuperare l'elenco minimo delle dichiarazione di consistenza di
  // un azienda
  public Vector<ConsistenzaVO> getDichiarazioniConsistenzaMinimo(Long idAzienda)
      throws Exception;

  // Metodo per recuperare il dettaglio di una dichiarazione di consistenza dato
  // un idDichiarazioneConsistenza
  public ConsistenzaVO getDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws Exception;

  /**
   * Restituisce l'ultima dichiarazione di consistenza (data più recente)
   * 
   * @param idAzienda
   * @param tipoConvalida
   * @param idProcedimento
   * @return
   */
  /*public ConsistenzaVO serviceGetUltimaDichiarazioneConsistenza(Long idAzienda)
      throws Exception, SolmrException;*/
  
  
  
  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda)
    throws Exception;

  /**
   * Restituisce l'ultima dichiarazione di consistenza alla data specificata
   * 
   * @param idAzienda
   * @param tipoConvalida
   * @param idProcedimento
   * @return
   */
  /*public ConsistenzaVO serviceGetUltimaDichiarazioneConsistenza(Long idAzienda,
      Date dataAl) throws Exception, SolmrException;*/

  /*
   * Servizio che permette di reperire i dati della ultima dichiarazione di
   * consistenza presente in Anagrafe. Per ogni procedimento posso essere
   * escluse determinate tipologie di dichiarazione
   */
  /*public ConsistenzaVO serviceGetUltimaDichiarazioneConsistenza(Long idAzienda,
      Date dataAl, Long idProcedimento, boolean escludiDichParticolari,
      boolean dichiarazioniAttive) throws Exception, SolmrException;*/

  /*public ConsistenzaVO[] serviceGetDichiarazioniConsistenza(Long idAzienda,
      Date dataAl, Date dataDal, Long idProcedimento,
      boolean escludiDichParticolari, boolean dichiarazioniAttive,
      String sottoProcedimento, Integer annoCampagna, boolean protocollo)
      throws Exception, SolmrException;*/

  /**
   * Restituisce una dichiarazione di consistenza in base all'id. Non posso
   * usare il servizio getDichiarazione di consistenza in quanto utilizza un
   * formato di data diverso da quello richiesto per i servizi.
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public ConsistenzaVO serviceGetDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws Exception, SolmrException;

  public Vector<TemporaneaPraticaAziendaVO> aggiornaPraticaAziendaPLQSL(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws Exception, SolmrException;
  
  public void aggiornaPraticaAziendaPLQSL(Long idAzienda) 
      throws Exception, SolmrException;

  public boolean deleteDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws Exception, SolmrException;
  
  public boolean newDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idMotivoDichiarazione) throws Exception, SolmrException;

  public void deleteDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws Exception;

  // Metodo per recuperare il dettaglio di un'anomalia
  public ErrAnomaliaDicConsistenzaVO getAnomaliaDichiarazioneConsistenza(
      Long idDichiarazioneSegnalazione) throws Exception;

  // Metodo per recuperare l'elenco degli errori o delle anomalie associati
  // all'elenco di IdDichiarazioneSegnalazione e vedre se possono essere
  // corrette.
  public Vector<ErrAnomaliaDicConsistenzaVO> getAnomaliePerCorrezione(
      Long elencoIdDichiarazioneSegnalazione[],long idMotivoDichiarazione) throws Exception;

  public Vector<CodeDescription> getDocumentiByIdControllo(Long idControllo)
      throws Exception;

  // Metodo per effettuare l'inserimento di una dichiarazione di correzione
  public void deleteInsertDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[], Vector<ErrAnomaliaDicConsistenzaVO> corrErr, long idUtente)
      throws Exception;

  // Metodo per effettuare la cancellazione di una dichiarazione di correzione
  public void deleteDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[]) throws Exception;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione) throws Exception;

  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioni(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoParticella)
      throws Exception;

  public ErrAnomaliaDicConsistenzaVO[] getListAnomalieByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, Long idFase,
      ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO,
      String[] orderBy) throws Exception;

  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda, String[] orderBy) throws Exception;
  
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAziendaVarCat(
      Long idAzienda, String[] orderBy) throws Exception;

  public void ripristinaPianoRiferimento(Long idDichiarazioneConsistenza,
      Long idUtente) throws SolmrException, Exception;

  public Vector<TipoMotivoDichiarazioneVO> getListTipoMotivoDichiarazione(long idAzienda) 
      throws Exception;

  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniUnar(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoUnitaArborea,
      String[] orderBy) throws Exception;
  
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniConsolidamentoUV(
      Long idAzienda) throws Exception;

  public void modificaDichiarazioneConsistenza(ConsistenzaVO consistenzaVO)
      throws Exception;

  public ConsistenzaVO findDichiarazioneConsistenzaByPrimaryKey(
      Long idDichiarazioneConsistenza) throws Exception;
  
  public FascicoloNazionaleVO getInfoRisultatiSianDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws Exception;
  
  public Vector<ConsistenzaVO> getListDichiarazioniPianoGrafico(Long idAzienda)
      throws Exception;
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromAccesso(long idAccessoPianoGrafico)
      throws Exception;
  
  public int preCaricamentoPianoGrafico(long idDichiarazioneConsistenza) throws Exception;
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromPK(long idEsitoGrafico) throws Exception;
  
  public Long insertStatoIncorsoPG(long idAccessoPianoGrafico, long idUtente) 
      throws Exception;
  
  public PlSqlCodeDescription controlliValidazionePlSql(long idAzienda, int idFase, 
      long idUtente, long idDichiarazioneConsistenza) throws Exception;

  public TipoControlloVO[] getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
      Long idDicharazioneConsistenza, Long idGruppoControllo, String orderBy[])
      throws Exception;

  public TipoControlloVO[] getListTipoControlloByIdGruppoControllo(
      Long idGruppoControllo, String orderBy[]) throws Exception;
  
  public TipoControlloVO[] getListTipoControlloByIdGruppoControlloAttivi(Long idGruppoControllo, String orderBy[])
      throws Exception;
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControllo(
      Long idGruppoControllo, Long idAzienda, String orderBy[]) throws Exception;
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
      Long idGruppoControllo, Long idDichiarazioneConsistenza, String orderBy[])
  throws Exception;

  public Long getAnnoDichiarazione(Long idDichiarazioneConsistenza)
      throws Exception;

  public Long getProcedimento(Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception;

  public long getLastIdDichiazioneConsistenza(Long idAzienda, Long anno)
      throws Exception;

  public long[] getElencoAnniDichiarazioniConsistenzaByIdParticella(
      long idParticella) throws Exception;
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieForControlliTerreni(Long idAzienda, Long idControllo, 
      Vector<String> vTipoErrori, boolean flagOK, String ordinamento) throws Exception;
  
  public String getLastAnnoCampagnaFromDichCons(long idAzienda) throws Exception;
  
  public TipoMotivoDichiarazioneVO findTipoMotivoDichiarazioneByPrimaryKey(Long idMotivoDichiarazione) 
    throws Exception;
  
  public String getLastDichConsNoCorrettiva(long idAzienda) throws Exception;
  
  public ConsistenzaVO getUltimaDichConsNoCorrettiva(long idAzienda) throws Exception;
  
  public Date getLastDateDichConsNoCorrettiva(long idAzienda) throws Exception;
  
  public Long getLastIdDichConsProtocollata(long idAzienda) throws Exception;
  
  public void updateDichiarazioneConsistenzaRichiestaStampa(Long idDichiarazioneConsistenza)
      throws Exception;
  
  public void updateNoteDichiarazioneConsistenza(String note,
      long idDichiarazioneConsistenza) throws Exception;
  
}

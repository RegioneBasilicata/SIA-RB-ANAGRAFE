package it.csi.smranag.smrgaa.interfacecsi.agriserv;

import it.csi.smranag.smrgaa.dto.agriserv.domande.DomandeVO;
import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoCCVO;
import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO;
import it.csi.smranag.smrgaa.exception.agriserv.AgriservInternalException;
import it.csi.smranag.smrgaa.exception.agriserv.AgriservInvalidParameterException;
import it.csi.smranag.smrgaa.exception.agriserv.AgriservQueryTimeOutException;
/**
 * Interfaccia CSI unificata per l'accesso ai servizi esposti dai procedimenti
 * per l'anagrafe.
 * Sep 22, 2008
 * @author TOBECONFIG
 */
public interface IAgriservCSIInterface
{ 

  /**
   * Restituisce le informazioni delle pratiche che riguardano la particella
   * individuata dagli id_storico_particella passati in input
   * 
   * @param elencoIdStoricoParticella
   *          elenco di id_storico_particella
   * @param idAzienda id dell'azienda su cui ricercare (facoltativo)          
   * @param tipologiaStati indica le operazioni di filtro sugli stati, i valori possibili sono
   * dati da combinazioni dei flag TipoProcedimentoVO.FLAG_STATO_XXXXX
   * @param idDichiarazioneConsistenza
   *          id della dichiarazione di consistenza di cui si vogliono le
   *          pratiche (facoltativo)
   * @param idProcedimento
   *          id del procedimento del quale si desiderano le pratiche: serve per
   *          applicativi che gestiscono più procedimenti quali gnps.
   *          (facoltativo)
   * @param annoCampagna
   *          anno della campagna a cui la pratica deve riferirsi (facoltativo)
   * @return informazioni sulle pratiche
   * @since 1.0.0
   */
  public PraticaProcedimentoVO[] agriservSearchPraticheProcedimento(
      long elencoIdStoricoParticella[], Long idAzienda, int tipologiaStati,
      Long idDichiarazioneConsistenza, Long idProcedimento, Long annoCampagna, int tipoOrdinamento)
  throws AgriservInternalException;
  
  
  /**
   * Restituisce l’elenco delle domande legate all'idAzienda o al numeroDomanda passato come parametro, completo di livello, 
   * aiuto e storico degli stati per ognuna delle domande restituite.
   * Almeno uno dei due parametri idAzienda/numeroDomanda deve essere specificato   
   * @param numeroDomanda Numero univoco collegato ad una domanda; ogni procedimento espositore lo interpreterà in base alle proprie esigenze
   * @param idAzienda idAzienda dell’azienda
   * @param codiciLivello Elenco di codici livello gestiti dall’applicazione fruitrice per il procedimento invocato.
   *                      Se non valorizzato non si devono porre constraint sul codice del livello.
   * @param maxRecord  Numero massimo di record da estrarre.Se tale limite viene superato bisogna resituire i primi maxRecord estratti.
   *        Se non valorizzato si dovranno estrarre tutti i record consentiti implicitamente dal servizio chiamato.
   * @param flagCodiceAgeaNull Utilizzato come filtro sui record restituiti solo quando numeroDomanda è valorizzato con un numero pratica.
   *                           False = restituisce solo i record con codiceAgea not null
   *                           True o non valorizzato = restituisce tutti i record a prescindere dal codiceAgea
   * @return
   * @throws AgriservInternalException
   * @throws AgriservQueryTimeOutException In caso di time out su db 
   * @throws AgriservInvalidParameterException Se nessuno dei parametri obbligatori viene specificato
   * @since 1.2.0
   */
  public DomandeVO agriservSearchDomande(String numeroDomanda, Long idAzienda, String codiciLivello[], Long maxRecord,Boolean flagCodiceAgeaNull)
    throws AgriservInternalException,AgriservQueryTimeOutException,AgriservInvalidParameterException;
  
  
  /**
   * Restituisce l’elenco delle pratiche di un determinato procedimento legate ad una particella catastale 
   * ed inoltre opzionalmente legata ad un determinato conto corrente
   * 
   * @param elencoIdContoCorrente
   *          elenco di id_conto_cotrrente
   * @param tipologiaStati indica le operazioni di filtro sugli stati, i valori possibili sono
   * dati da combinazioni dei flag TipoProcedimentoVO.FLAG_STATO_XXXXX
   * @param idDichiarazioneConsistenza
   *          id della dichiarazione di consistenza di cui si vogliono le
   *          pratiche (facoltativo)
   * @param idProcedimento
   *          id del procedimento del quale si desiderano le pratiche: serve per
   *          applicativi che gestiscono più procedimenti quali gnps.
   *          (facoltativo)
   * @param annoCampagna
   *          anno della campagna a cui la pratica deve riferirsi (facoltativo)
   * @return informazioni sulle pratiche
   * @since 2.0.0
   */
  public PraticaProcedimentoCCVO[] agriservSearchPraticheProcedimentoCC(
      long elencoIdContoCorrente[], int tipologiaStati,
      Long idDichiarazioneConsistenza, Long idProcedimento, Long annoCampagna, int tipoOrdinamento)
  throws AgriservInternalException;
}

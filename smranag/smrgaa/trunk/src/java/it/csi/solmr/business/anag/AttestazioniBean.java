package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.ConsistenzaDAO;
import it.csi.solmr.integration.anag.ParametriAttDichiarataDAO;
import it.csi.solmr.integration.anag.ParametriAttestazioneAziendaDAO;
import it.csi.solmr.integration.anag.TipoAttestazioneDAO;
import it.csi.solmr.integration.anag.TipoParametriAttestazioneDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/Attestazioni",mappedName="comp/env/solmr/anag/Attestazioni")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class AttestazioniBean implements AttestazioniLocal {

	private static final long serialVersionUID = 128464688264340973L;

	SessionContext sessionContext;
	private transient TipoAttestazioneDAO tipoAttestazioneDAO = null;
	private transient TipoParametriAttestazioneDAO tipoParametriAttestazioneDAO = null;
	private transient ParametriAttestazioneAziendaDAO parametriAttestazioneAziendaDAO = null;
	private transient ParametriAttDichiarataDAO parametriAttDichiarataDAO = null;
	private transient DocumentoGaaDAO documentoGaaDAO = null;
	private transient ConsistenzaDAO consistenzaDAO = null;

	

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
		initializeDAO();
	}

	private void initializeDAO() throws EJBException 
	{
		try 
		{
			tipoAttestazioneDAO = new TipoAttestazioneDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			tipoParametriAttestazioneDAO = new TipoParametriAttestazioneDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			parametriAttestazioneAziendaDAO = new ParametriAttestazioneAziendaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			parametriAttDichiarataDAO = new ParametriAttDichiarataDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			documentoGaaDAO = new DocumentoGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			consistenzaDAO = new ConsistenzaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
		}
		catch (ResourceAccessException ex) {
			SolmrLogger.fatal(this, ex.getMessage());
			throw new EJBException(ex);
		}
	}

	/**
	 * Metodo utilizzato per estrarre le attestazioni relative ad un determinato piano di riferimento
	 *
	 * @param idAzienda
	 * @param codiceFotografiaTerreni
	 * @param dataInserimentoDichiarazione
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return it.csi.solmr.dto.attestazioni.TipoAttestazioneVO[]
	 * @throws Exception
	 */
	public TipoAttestazioneVO[] getListTipoAttestazioneOfPianoRiferimento(Long idAzienda, String codiceFotografiaTerreni, 
	    java.util.Date dataAnnoCampagna, java.util.Date dataVariazione, String[] orderBy, String voceMenu) throws Exception 
	{
		try 
		{
			if(idAzienda != null) 
			{
				TipoAttestazioneVO[] elencoAttestazioni = tipoAttestazioneDAO.getListTipoAttestazioneAlPianoAttuale(
				    idAzienda, orderBy, voceMenu);
				for(int i = 0; i < elencoAttestazioni.length; i++) 
				{
					TipoAttestazioneVO tipoAttestazioneVO = elencoAttestazioni[i];
					ParametriAttAziendaVO parametriAttAziendaVO = tipoAttestazioneVO.getParametriAttAziendaVO();
					StringUtils.parsificaDescrizioneAttestazioni(parametriAttAziendaVO, tipoAttestazioneVO);
					elencoAttestazioni[i] = tipoAttestazioneVO;
				}
				return elencoAttestazioni;
			}
			else 
			{
			  TipoAttestazioneVO[] elencoAttestazioni = null;
			  if(SolmrConstants.VOCE_MENU_ATTESTAZIONI_DICHIARAZIONI.equalsIgnoreCase(voceMenu))
			  {
			    elencoAttestazioni = tipoAttestazioneDAO.getListTipoAttestazioneDichiarazioniAllaDichiarazione(
			        codiceFotografiaTerreni, dataAnnoCampagna, orderBy);
			  }
			  else
			  {
			    elencoAttestazioni = tipoAttestazioneDAO.getTipoAttestazioneAllegatiAllaDichiarazione(
			        codiceFotografiaTerreni, dataAnnoCampagna, dataVariazione, orderBy);
			  }
				
				if(elencoAttestazioni != null)
				{
  				for(int i = 0; i < elencoAttestazioni.length; i++) 
  				{
  					TipoAttestazioneVO tipoAttestazioneVO = elencoAttestazioni[i];
  					ParametriAttDichiarataVO parametriAttDichiarataVO = tipoAttestazioneVO.getParametriAttDichiarataVO();
  					StringUtils.parsificaDescrizioneAttestazioniDichiarate(parametriAttDichiarataVO, tipoAttestazioneVO);
  					elencoAttestazioni[i] = tipoAttestazioneVO;
  				}
				}
				return elencoAttestazioni;
			}
		}
		catch(DataAccessException dae) 
		{
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo utilizzato per confrontare la dataInserimentoDichiarazione con
	 * MIN(TA.DATA_INIZIO_VALIDITA) e MAX(TA.DATA_FINE_VALIDITA) della tabella
     * DB_TIPO_ATTESTAZIONE
     *
     * @param codiceFotografiaTerreni
     * @param dataInserimentoDichiarazione
     * @param flagVideo
     * @param flagStampa
     * @param codiceAttestazione
     * @param voceMenu
     * @return Integer possibili valori restituiti dal metodo
     *         null: non è stato trovato alcun record sul DB
     *         -1: la data dataInserimentoDichiarazione è minore della data MIN(TA.DATA_INIZIO_VALIDITA)
     *         0: la data dataInserimentoDichiarazione è compresa fra la data MIN(TA.DATA_INIZIO_VALIDITA) e la data MAX(TA.DATA_FINE_VALIDITA)
     *         +1: la data dataInserimentoDichiarazione è maggiore della data MAX(TA.DATA_FINE_VALIDITA)
     * @throws DataAccessException
     */
	public boolean getDataAttestazioneAllaDichiarazione(String codiceFotografiaTerreni, java.util.Date dataInserimentoDichiarazione, boolean flagVideo, boolean flagStampa, String codiceAttestazione, String voceMenu) throws Exception 
	{
		try 
		{
			return tipoAttestazioneDAO.getDataAttestazioneAllaDichiarazione(codiceFotografiaTerreni, dataInserimentoDichiarazione, flagVideo, flagStampa, codiceAttestazione, voceMenu);
	  }
	  catch(DataAccessException dae) 
	  {
	  	throw new Exception(dae.getMessage());
	  }
  }



	/**
	 * Metodo che mi restituisce l'elenco delle attestazioni relative al piano corrente
	 * Da usare solo nelle funzionalità di modifica in quanto mantiene il campo
	 * "DESCRIZIONE" del DB senza la decodifica ma con i parametri: es "$$parametro1".
	 * 
	 * 
	 * **************************************
	 * codiceFotografiaTerreni e dataAnnoCAmpagna nn dovrebbero più servire....tenuti per scrupolo
	 *
	 * @param idAzienda
	 * @param codiceFotografiaTerreni
	 * @param dataInserimentoDichiarazione
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return TipoAttestazioneVO[]
	 * @throws Exception
	 */
	public TipoAttestazioneVO[] getListTipoAttestazioneForUpdate(Long idAzienda, String[] orderBy, String voceMenu) throws Exception 
	{
		try 
		{		  
		  return tipoAttestazioneDAO.getListTipoAttestazioneAlPianoAttuale(idAzienda, orderBy, voceMenu);
		}
		catch(DataAccessException dae) 
		{
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo per verificare la presenza di attestazioni dichiarate
	 *
	 * @param codiceFotografiaTerreni
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isAttestazioneDichiarata(String codiceFotografiaTerreni) throws Exception {
		try {
			return tipoAttestazioneDAO.isAttestazioneDichiarata(codiceFotografiaTerreni);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	/**
   * Metodo per verificare la presenza di attestazioni azienda
   *
   * @param idAzienda
   * @return boolean
   * @throws Exception
   */
  public boolean isAttestazioneAzienda(long idAzienda) throws Exception {
    try {
      return tipoAttestazioneDAO.isAttestazioneAzienda(idAzienda);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }

	/**
	 * Metodo che si occupa di richiamare una procedura PL-SQL per calcolare le dichiarazioni che possono già essere selezionate dal sistema
	 *
	 * @param idAzienda
	 * @param idUtente
	 * @param voceMenu
	 * @throws SolmrException
	 * @throws Exception
	 */
	public void aggiornaAttestazioniPlSql(Long idAzienda, Long idUtente, Long idDichiarazioneConsistenza, String voceMenu) throws SolmrException, Exception 
	{
		try 
		{
			tipoAttestazioneDAO.aggiornaAttestazioniPlSql(idAzienda, idUtente, idDichiarazioneConsistenza, voceMenu);
			if((idDichiarazioneConsistenza != null) && SolmrConstants.VOCE_MENU_ATTESTAZIONI_ALLEGATI.equalsIgnoreCase(voceMenu))
	    {
			  PlSqlCodeDescription plCode = tipoAttestazioneDAO.raggruppaAttestazioniPlSql(idDichiarazioneConsistenza.longValue());
        
        if(plCode !=null)
        {
          if(Validator.isNotEmpty(plCode.getDescription()))
          {
            throw new DataAccessException("aggiornaAttestazioniPlSql attestazioni errore al plsql raggruppaAttestazioni: "+plCode.getDescription()+"-"
                +plCode.getOtherdescription());       
          }
        }
	    }
		}
		catch(DataAccessException dae) 
		{
			sessionContext.setRollbackOnly();
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo che mi permette di effettuare l'aggiornamento delle dichiarazioni
	 * associate all'azienda
	 *
	 * @param elencoIdAttestazioni
	 * @param anagAziendaVO
	 * @param ruoloUtenza
	 * @param elencoParametri
	 * @param voceMenu
	 * @param codiceFotografiaTerreni
	 * @throws Exception
	 */
	public void aggiornaAttestazioni(String[] elencoIdAttestazioni, 
	    AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza, Hashtable<?,?> elencoParametri, 
	    String voceMenu, ConsistenzaVO consistenzaVO) throws Exception 
	{
		try 
		{
			// Se sto modificando le attestazioni al piano di riferimento corrente
			if(voceMenu.equalsIgnoreCase(SolmrConstants.VOCE_MENU_ATTESTAZIONI_DICHIARAZIONI)) 
			{
			  Vector<ParametriAttAziendaVO> elenco = new Vector<ParametriAttAziendaVO>();
			  //Lock per eviatre accessi contemporanei
			  tipoAttestazioneDAO.lockTableAttestazioneAzienda(anagAziendaVO.getIdAzienda().longValue());
				// Elimino tutti i parametri legati precedentemente all'azienda
				parametriAttestazioneAziendaDAO.deleteParametriAttestazioneAziendaByIdAzienda(anagAziendaVO.getIdAzienda(), voceMenu);
				// Elimino tutte le precedenti attestazioni legate all'azienda
				tipoAttestazioneDAO.deleteAttestazioneAziendaByIdAzienda(anagAziendaVO.getIdAzienda(), voceMenu);
				// Inserisco i nuovi legami selezionati dall'utente
				if(elencoIdAttestazioni != null) 
				{
					for(int i = 0; i < elencoIdAttestazioni.length; i++) 
					{
						Long idAttestazione = Long.decode(elencoIdAttestazioni[i]);
						AttestazioneAziendaVO attestazioneAziendaVO = new AttestazioneAziendaVO();
						attestazioneAziendaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
						attestazioneAziendaVO.setIdAttestazione(idAttestazione);
						attestazioneAziendaVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
						attestazioneAziendaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
						Long idAttestazioneAzienda = tipoAttestazioneDAO.insertAttestazioneAzienda(attestazioneAziendaVO);
						if(elencoParametri != null && elencoParametri.size() > 0) 
						{
							ParametriAttAziendaVO parametriAttAziendaVO = (ParametriAttAziendaVO)elencoParametri.get(idAttestazione);
							if(parametriAttAziendaVO != null) 
							{
								parametriAttAziendaVO.setIdAttestazioneAzienda(idAttestazioneAzienda);
								elenco.add(parametriAttAziendaVO);
							}
						}
					}
				}
				// Effettuo l'inserimento massivo di tutti i parametri inseriti dall'utente
				parametriAttestazioneAziendaDAO.insertAllParametriAttAzienda((ParametriAttAziendaVO[])elenco.toArray(new ParametriAttAziendaVO[elenco.size()]));
			}
			else 
			{
			  // se sto modificando gli allegati al piano di riferimento corrente
				if(Validator.isEmpty(consistenzaVO)) 
				{
				  Vector<ParametriAttAziendaVO> elenco = new Vector<ParametriAttAziendaVO>();
				  //Lock per eviatre accessi contemporanei
	        tipoAttestazioneDAO.lockTableAttestazioneAzienda(anagAziendaVO.getIdAzienda().longValue());
					// Elimino tutti i parametri legati precedentemente all'azienda
					parametriAttestazioneAziendaDAO.deleteParametriAttestazioneAziendaByIdAzienda(anagAziendaVO.getIdAzienda(), voceMenu);
					// Elimino tutte le precedenti attestazioni legate all'azienda
					tipoAttestazioneDAO.deleteAttestazioneAziendaByIdAzienda(anagAziendaVO.getIdAzienda(), voceMenu);
					// Inserisco i nuovi legami selezionati dall'utente
					if(elencoIdAttestazioni != null) 
					{
						for(int i = 0; i < elencoIdAttestazioni.length; i++) 
						{
							Long idAttestazione = Long.decode(elencoIdAttestazioni[i]);
							AttestazioneAziendaVO attestazioneAziendaVO = new AttestazioneAziendaVO();
							attestazioneAziendaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
							attestazioneAziendaVO.setIdAttestazione(idAttestazione);
							attestazioneAziendaVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
							attestazioneAziendaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
							Long idAttestazioneAzienda = tipoAttestazioneDAO.insertAttestazioneAzienda(attestazioneAziendaVO);
							if(elencoParametri != null && elencoParametri.size() > 0) 
							{
								ParametriAttAziendaVO parametriAttAziendaVO = (ParametriAttAziendaVO)elencoParametri.get(idAttestazione);
								if(parametriAttAziendaVO != null) 
								{
									parametriAttAziendaVO.setIdAttestazioneAzienda(idAttestazioneAzienda);
									elenco.add(parametriAttAziendaVO);
								}
							}
						}
					}
					// Effettuo l'inserimento massivo di tutti i parametri inseriti dall'utente
					parametriAttestazioneAziendaDAO.insertAllParametriAttAzienda((ParametriAttAziendaVO[])elenco.toArray(new ParametriAttAziendaVO[elenco.size()]));
				}
				// Se invece sto modificando gli allegati alla dichiarazione di consistenza
				else 
				{
				  Vector<ParametriAttDichiarataVO> elenco = new Vector<ParametriAttDichiarataVO>();
				  //Lock per eviatre accessi contemporanei
          tipoAttestazioneDAO.lockTableAttestazioneDichiarata(consistenzaVO.getCodiceFotografiaTerreni().toString());
					//Storicizziamo anziche cancellare 13/07/2012
					tipoAttestazioneDAO.storicizzaAttDichiarata(consistenzaVO.getCodiceFotografiaTerreni(), voceMenu);
					// Inserisco i nuovi legami selezionati dall'utente
					if(elencoIdAttestazioni != null) 
					{
						for(int i = 0; i < elencoIdAttestazioni.length; i++) 
						{
							Long idAttestazione = Long.decode(elencoIdAttestazioni[i]);
							AttestazioneDichiarataVO attestazioneDichiarataVO = new AttestazioneDichiarataVO();
							attestazioneDichiarataVO.setCodiceFotografiaTerreni(consistenzaVO.getCodiceFotografiaTerreni().toString());
							attestazioneDichiarataVO.setIdAttestazione(idAttestazione);
							attestazioneDichiarataVO.setDataAggiornamento(new java.util.Date(System.currentTimeMillis()));
							attestazioneDichiarataVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
							attestazioneDichiarataVO.setDataInizioValidita(new java.util.Date(System.currentTimeMillis()));
							Long idAttestazioneDichiarata = tipoAttestazioneDAO.insertAttestazioneDichiarata(attestazioneDichiarataVO);
							if(elencoParametri != null && elencoParametri.size() > 0) 
							{
								ParametriAttDichiarataVO parametriAttDichiarataVO = (ParametriAttDichiarataVO)elencoParametri.get(idAttestazione);
								if(parametriAttDichiarataVO != null) 
								{
									parametriAttDichiarataVO.setIdAttestazioneDichiarata(idAttestazioneDichiarata);
									elenco.add(parametriAttDichiarataVO);
								}
							}
						}
					}
					// Effettuo l'inserimento massivo di tutti i parametri inseriti dall'utente
					parametriAttDichiarataDAO.insertAllParametriAttDichiarata((ParametriAttDichiarataVO[])elenco.toArray(new ParametriAttDichiarataVO[elenco.size()]));
				}
				
				PlSqlCodeDescription plCode = null;
				if(Validator.isNotEmpty(consistenzaVO)) 
        {
				  plCode = tipoAttestazioneDAO.raggruppaAttestazioniPlSql(new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
        }
				
				if(plCode !=null)
        {
          if(Validator.isNotEmpty(plCode.getDescription()))
          {
            throw new DataAccessException("Aggiorna attestazioni errore al plsql raggruppaAttestazioni: "+plCode.getDescription()+"-"
                +plCode.getOtherdescription());       
          }
        }     
				
			}
		}
		catch(DataAccessException dae) 
		{
			sessionContext.setRollbackOnly();
			throw new Exception(dae.getMessage());
		}
	}
	
	
	
	public void aggiornaElencoAllegatiAttestazioni(String[] elencoIdAttestazioni, 
    RuoloUtenza ruoloUtenza, Hashtable<Long,ParametriAttDichiarataVO> elencoParametri, 
    TipoAllegatoVO tipoAllegatoVO, ConsistenzaVO consistenzaVO) throws Exception 
  {
    try 
    {
      Vector<ParametriAttDichiarataVO> elenco = new Vector<ParametriAttDichiarataVO>();
      //Lock per eviatre accessi contemporanei
      tipoAttestazioneDAO.lockTableAttestazioneDichiarata(consistenzaVO.getCodiceFotografiaTerreni().toString());
      
      tipoAttestazioneDAO.storicizzaAttDichiarataCodAtt(consistenzaVO.getCodiceFotografiaTerreni(), tipoAllegatoVO.getCodiceAttestazione());
      // Inserisco i nuovi legami selezionati dall'utente
      if(elencoIdAttestazioni != null) 
      {
        for(int i = 0; i < elencoIdAttestazioni.length; i++) 
        {
          Long idAttestazione = Long.decode(elencoIdAttestazioni[i]);
          AttestazioneDichiarataVO attestazioneDichiarataVO = new AttestazioneDichiarataVO();
          attestazioneDichiarataVO.setCodiceFotografiaTerreni(consistenzaVO.getCodiceFotografiaTerreni().toString());
          attestazioneDichiarataVO.setIdAttestazione(idAttestazione);
          attestazioneDichiarataVO.setDataAggiornamento(new java.util.Date(System.currentTimeMillis()));
          attestazioneDichiarataVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
          attestazioneDichiarataVO.setDataInizioValidita(new java.util.Date(System.currentTimeMillis()));
          Long idAttestazioneDichiarata = tipoAttestazioneDAO.insertAttestazioneDichiarata(attestazioneDichiarataVO);
          if(elencoParametri != null && elencoParametri.size() > 0) 
          {
            ParametriAttDichiarataVO parametriAttDichiarataVO = (ParametriAttDichiarataVO)elencoParametri.get(idAttestazione);
            if(parametriAttDichiarataVO != null) 
            {
              parametriAttDichiarataVO.setIdAttestazioneDichiarata(idAttestazioneDichiarata);
              elenco.add(parametriAttDichiarataVO);
            }
          }
        }
      }
      // Effettuo l'inserimento massivo di tutti i parametri inseriti dall'utente
      parametriAttDichiarataDAO.insertAllParametriAttDichiarata((ParametriAttDichiarataVO[])elenco.toArray(new ParametriAttDichiarataVO[elenco.size()]));
            
      PlSqlCodeDescription plCode = tipoAttestazioneDAO.raggruppaAttestazioniPlSql(new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
        
      if(plCode !=null)
      {
        if(Validator.isNotEmpty(plCode.getDescription()))
        {
          throw new DataAccessException("Aggiorna attestazioni errore al plsql raggruppaAttestazioni: "+plCode.getDescription()+"-"
             +plCode.getOtherdescription());       
        }
      }
      
      //Verifica se esiste già una stampa valida memorizzata attiva per il tipo di allegato
      AllegatoDichiarazioneVO  allegatoDichiarazioneVO = documentoGaaDAO.getAllegatoDichiarazioneFromIdDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza()),
          new Long(tipoAllegatoVO.getIdTipoAllegato()));
      if(Validator.isNotEmpty(allegatoDichiarazioneVO))
      {
        //esite uno con stampa su index quindi valida
        if(Validator.isNotEmpty(allegatoDichiarazioneVO.getExtIdDocumentoIndex()))
        {          
          //storicizzo il record...
          documentoGaaDAO.storicizzaAllegatoDichiarazione(allegatoDichiarazioneVO.getIdAllegatoDichiarazione());
          //inserisco uno vuoto senza stampa blob vuoto
          //per fare vedere nella validazione che esite un record...
          AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
          allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
          Date dataAttuale = new Date();
          allegatoDocumentoVO.setDataRagistrazione(dataAttuale);
          allegatoDocumentoVO.setDataUltimoAggiornamento(dataAttuale);          
          String nomeFile = tipoAllegatoVO.getDescrizioneTipoAllegato().replace(" ", "");
          nomeFile += "_"+consistenzaVO.getNumeroProtocollo()+".pdf";          
          allegatoDocumentoVO.setNomeFisico(nomeFile);
          allegatoDocumentoVO.setNomeLogico(nomeFile);          
          if(ruoloUtenza.isUtentePA())
            allegatoDocumentoVO.setIdTipoFirma(new Long(4));
          
          allegatoDocumentoVO.setIdTipoAllegato(new Long(tipoAllegatoVO.getIdTipoAllegato()));
          Long idAllegato = documentoGaaDAO.insertFileAllegatoNoFile(allegatoDocumentoVO);
          documentoGaaDAO.insertAllegatoDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza()), 
              idAllegato.longValue());
          
        }
        //nn faccio nulla perchè un record vuoto (non su index) è già presente
        
      }
      else
      {
        //inserisco uno vuoto senza stampa
        AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
        allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
        Date dataAttuale = new Date();
        allegatoDocumentoVO.setDataRagistrazione(dataAttuale);
        allegatoDocumentoVO.setDataUltimoAggiornamento(dataAttuale);          
        String nomeFile = tipoAllegatoVO.getDescrizioneTipoAllegato().replace(" ", "");
        nomeFile += "_"+consistenzaVO.getNumeroProtocollo()+".pdf";          
        allegatoDocumentoVO.setNomeFisico(nomeFile);
        allegatoDocumentoVO.setNomeLogico(nomeFile);
        allegatoDocumentoVO.setIdTipoAllegato(new Long(tipoAllegatoVO.getIdTipoAllegato()));
        Long idAllegato = documentoGaaDAO.insertFileAllegatoNoFile(allegatoDocumentoVO);
        documentoGaaDAO.insertAllegatoDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza()), 
            idAllegato.longValue());
      }
      
      consistenzaDAO.updateDichiarazioneConsistenzaRichiestaStampa(new Long(consistenzaVO.getIdDichiarazioneConsistenza()));
        
    }
    catch(DataAccessException dae) 
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

	/**
	 * Metodo utilizzato per estrarre i tipi parametri previsti per una determinata attestazione
	 *
	 * @param idAttestazione
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO
	 * @throws Exception
	 */
	public TipoParametriAttestazioneVO findTipoParametriAttestazioneByIdAttestazione(Long idAttestazione) throws Exception {
		try {
			return tipoParametriAttestazioneDAO.findTipoParametriAttestazioneByIdAttestazione(idAttestazione);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	/**
	 * Metodo utilizzato per estrarre l'elenco delle attestazioni/allegati validi
	 * al piano di lavoro corrente
	 * 
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
	 * @throws Exception
	 */
	public TipoAttestazioneVO[] getElencoTipoAttestazioneAlPianoAttuale(boolean flagVideo, boolean flagStampa, String[] orderBy, String codiceAttestazione, String voceMenu) throws Exception {
		try {
			return tipoAttestazioneDAO.getElencoTipoAttestazioneAlPianoAttuale(flagVideo, flagStampa, orderBy, codiceAttestazione, voceMenu);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	/**
	 * Metodo utilizzato per estrarre le attestazioni/allegati validi ad una determinata dichiarazione di consistenza
	 *
	 * @param dataInserimentoDichiarazione
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
	 * @throws Exception
	 */
	public TipoAttestazioneVO[] getTipoAttestazioneAllegatiAllaDichiarazione(
      String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
      java.util.Date dataVariazione, String[] orderBy) 
	        throws Exception 
	{
		try 
		{
			return tipoAttestazioneDAO
			  .getTipoAttestazioneAllegatiAllaDichiarazione(codiceFotografiaTerreni, dataAnnoCampagna, 
			      dataVariazione, orderBy);
		}
		catch(DataAccessException dae) 
		{
			throw new Exception(dae.getMessage());
		}
	}
	
	public Vector<TipoAttestazioneVO> getElencoAttestazioniAllaDichiarazione(
      String codiceFotografiaTerreni, Date dataAnnoCampagna, 
      String codiceAttestazione)
    throws Exception 
  {
    try 
    {
      return tipoAttestazioneDAO.getElencoAttestazioniAllaDichiarazione(
          codiceFotografiaTerreni, dataAnnoCampagna, codiceAttestazione);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
	
	
	public AttestazioneAziendaVO getFirstAttestazioneAzienda(long idAzienda) throws Exception {
    try {
      return tipoAttestazioneDAO.getFirstAttestazioneAzienda(idAzienda);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }
	
	public AttestazioneDichiarataVO getFirstAttestazioneDichiarata(long codiceFotografia) throws Exception {
    try {
      return tipoAttestazioneDAO.getFirstAttestazioneDichiarata(codiceFotografia);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }
	
	public Vector<java.util.Date> getDateVariazioniAllegati(long codiceFotografia) throws Exception 
	{
    try 
    {
      return tipoAttestazioneDAO.getDateVariazioniAllegati(codiceFotografia);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
	
	
}

package it.csi.solmr.business.anag;

import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.TesserinoFitoSanitarioVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.CodiceFiscaleException;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AnagWriteDAO;
import it.csi.solmr.integration.anag.SoggettiDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.CaricaPersonaInfoc;
import it.csi.solmr.ws.infoc.ListaPersoneRI;
import it.csi.solmr.ws.infoc.PersonaRIInfoc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Stateless(name="comp/env/solmr/anag/Soggetti",mappedName="comp/env/solmr/anag/Soggetti")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SoggettiBean implements SoggettiLocal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5696458393831238386L;
	private transient SoggettiDAO sDAO= null;
	private transient AnagWriteDAO awDAO= null;
	private transient CommonDAO cDAO= null;
	SessionContext sessionContext;

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
		initializeDAO();
	}

	private void initializeDAO() throws EJBException {
		try {
			sDAO = new SoggettiDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			awDAO = new AnagWriteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			cDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
		}
		catch (ResourceAccessException ex) {
			SolmrLogger.fatal(this, ex.getMessage());
			throw new EJBException(ex);
		}
	}

	
	public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Date data) throws Exception, SolmrException{
		Vector<PersonaFisicaVO> result = null;
		try
		{
			result = sDAO.getSoggetti(idAzienda, data);
		}
		catch (NotFoundException nex)
		{
			throw new SolmrException((String)AnagErrors.get("SOGGETTO_NON_PRESENTE"));
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
		return result;
	}

	public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Boolean storico) throws Exception, SolmrException{
		Vector<PersonaFisicaVO> result = null;
		try
		{
			result = sDAO.getSoggetti(idAzienda, storico);
		}
		catch (NotFoundException nex)
		{
			throw new SolmrException((String)AnagErrors.get("SOGGETTO_NON_PRESENTE"));
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
		return result;
	}

	public void deleteContitolare(Long idContitolare) throws Exception, SolmrException{
		try {
			sDAO.deleteContitolare(idContitolare);
		}
		catch (DataAccessException dex) {
			throw new Exception(dex.getMessage());
		}
	}

	public void checkForDeleteSoggetto(Long idContitolare) throws Exception, SolmrException{
		try {
			deleteContitolare(idContitolare);
		}		
		catch (SolmrException ex) {
			sessionContext.setRollbackOnly();
			throw ex;
		}
		catch (Exception ex) {
			sessionContext.setRollbackOnly();
			throw new SolmrException (ex.getMessage());
		}
	}

	public PersonaFisicaVO getDettaglioSoggetti(Long idSoggetto, Long idAzienda)
	throws Exception
	{
		PersonaFisicaVO pfVO = null;
		try
		{
			pfVO = sDAO.getDettaglioSoggetti(idSoggetto, idAzienda, null);
		}
		catch (NotFoundException nexc)
		{
			throw new Exception(nexc.getMessage());
		}
		catch (DataAccessException dex) {
			throw new Exception(dex.getMessage());
		}
		return pfVO;
	}

	public void insertSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento) 
	  throws Exception, SolmrException
	{
		Long idSoggetto = null;
		try {
			if (pfVO.isNewPersonaFisica()) {
				idSoggetto = awDAO.insertSoggetto(SolmrConstants.FLAG_S);
				pfVO.setIdSoggetto(idSoggetto);
				awDAO.insertPersonaFisica(pfVO, idUtenteAggiornamento);
			}
			else {
				awDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), pfVO, idUtenteAggiornamento);
				idSoggetto = pfVO.getIdSoggetto();
			}
			sDAO.insertContitolareRuolo(pfVO.getIdAzienda(), idSoggetto, pfVO.getIdRuolo(), pfVO.getDataInizioRuoloMod());
		}
		catch (DataAccessException dae) {
			sessionContext.setRollbackOnly();
			throw new Exception(dae.getMessage());
		}
	}

	// Metodo per la modifica dei soggetti collegati
	public void updateSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)throws Exception,SolmrException{
		try {
			// Modifico il legame su DB_CONTITOLARE
			awDAO.updateContitolareRuolo(pfVO);
			awDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), pfVO, idUtenteAggiornamento);
		}
		catch (DataAccessException dae) {
			sessionContext.setRollbackOnly();
			throw new Exception(dae.getMessage());
		}
	}

	public Vector<Long> getIdPersoneFisiche(String codFiscale, String cognome, String nome, String dataNascita,
			String istatNascita, String istatResidenza, boolean personaAttiva)
	throws  Exception, SolmrException
	{
		Vector<Long> result = null;
		try
		{
			result = sDAO.getIdPersoneFisiche(codFiscale, cognome, nome, dataNascita, istatNascita, istatResidenza, personaAttiva);
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
		catch (SolmrException ex)
		{
			throw ex;
		}
		return result;
	}

	public Vector<PersonaFisicaVO> getListPersoneFisicheByIdRange(Vector<Long> collIdPF) throws Exception, SolmrException{
		Vector<PersonaFisicaVO> result = null;
		try {
			result = sDAO.getListPersoneFisicheByIdRange(collIdPF);
		}
		catch (DataAccessException dex) {
			throw new Exception(dex.getMessage());
		}catch (SolmrException ex) {
			throw ex;
		}
		return result;
	}

	public PersonaFisicaVO findByPrimaryKey(Long idPersonaFisica) throws Exception, SolmrException{
		try {
			return sDAO.findByPrimaryKey(idPersonaFisica);
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}catch (SolmrException ex) {
			throw ex;
		}
	}
	/*
  public Vector findPersonaFisicaVector(Long idPersonaFisica) throws Exception, SolmrException{
    try {
      return sDAO.findPersonaFisicaVector(idPersonaFisica);
    }
    catch (DataAccessException ex) {
      throw new Exception(ex.getMessage());
    }catch (SolmrException ex) {
      throw ex;
    }
  }*/

	public Vector<PersonaFisicaVO> findPersonaFisicaByIdSoggettoAndIdAzienda(Long idSoggetto, Long idAzienda) throws Exception, SolmrException{
		try {
			return sDAO.findPersonaFisicaByIdSoggettoAndIdAzienda(idSoggetto, idAzienda);
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}catch (SolmrException ex) {
			throw ex;
		}
	}

	public Vector<Long> getIdAziendeBySoggetto(Long idSoggetto) throws Exception, SolmrException{
		try {
			return sDAO.getIdAziendeBySoggetto(idSoggetto);
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}catch (SolmrException ex) {
			throw ex;
		}
	}

	public PersonaFisicaVO findPersonaFisica(Long idPersonaFisica) throws Exception, SolmrException{
		try {
			return sDAO.findPersonaFisica(idPersonaFisica);
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}catch (SolmrException ex) {
			throw ex;
		}
	}

	// Metodo per recuperare gli elementi del dettaglio del soggetto collegato a partire dall'id_contitolare
	public PersonaFisicaVO getDettaglioSoggettoByIdContitolare(Long idContitolare) throws Exception, SolmrException {
		PersonaFisicaVO personaVO = null;
		try {
			personaVO = sDAO.getDettaglioSoggettoByIdContitolare(idContitolare);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return personaVO;
	}
	
	public  TesserinoFitoSanitarioVO getTesserinoFitoSanitario(String codiceFiscale) 
	    throws Exception, SolmrException 
	{
	  TesserinoFitoSanitarioVO tesserinoVO = null;
	  try 
	  {
	    tesserinoVO = sDAO.getTesserinoFitoSanitario(codiceFiscale);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    return tesserinoVO;  
	}

	public PersonaFisicaVO getDatiSoggettoPerMacchina(Long idPersonaFisica)throws Exception, SolmrException{
		try {
			return sDAO.getDatiSoggettoPerMacchina(idPersonaFisica);
		}
		catch (DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch (NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	// Metodo per effettuare la modifica del ruolo, dei dati della persona e storicizzazione della residenza
	public void updateDatiSoggettoAndStoricizzaResidenza(PersonaFisicaVO newPersonaFisicaVO, 
	    PersonaFisicaVO oldPersonaFisicaVO, long idUtenteAggiornamento) 
	  throws Exception, SolmrException 
  {
		try {
			// Modifico su DB_CONTITOLARE
			awDAO.updateContitolareRuolo(newPersonaFisicaVO);
			// Modifico su DB_PERSONA_FISICA
			awDAO.updatePersonaFisica(newPersonaFisicaVO.getIdPersonaFisica(), newPersonaFisicaVO, idUtenteAggiornamento, true);
			// Storicizzo i dati della residenza
			awDAO.insertStoricoResidenza(oldPersonaFisicaVO, idUtenteAggiornamento);
		}
		catch(DataAccessException dae) {
			sessionContext.setRollbackOnly();
			throw new Exception(dae.getMessage());
		}
		catch(Exception e) {
			sessionContext.setRollbackOnly();
			throw new Exception(e.getMessage());
		}
	}


	/**
	 * I seguenti metodi sono usati solo per fornire dei servizi (non per uma)
	 * */
	/**************************************************************************/
	/**************************************************************************/
	/**************************************************************************/
	/*public PersonaFisicaVO[] serviceGetSoggetti(Long idAzienda, Date data)
	throws Exception, SolmrException
	{
		if (idAzienda == null || data==null)
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,ErrorTypes.INVALID_PARAMETER);
		Vector result = null;
		try
		{
			result = sDAO.serviceGetSoggetti(idAzienda, data);
			if (result==null) return null;
			int size=result==null?0:result.size();
			if (size==0)
			{
				return null;
			}
			try
			{
//				Elimino eventuali ore/minuti/secondi
				data=DateUtils.parseDate(DateUtils.formatDate(data));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				// Non faccion niente 
			}
			if (data!=null)
			{
				for(int i=0;i<size;i++)
				{
					PersonaFisicaVO pfVO=(PersonaFisicaVO)result.get(i);
					if (data!=null && pfVO!=null)
					{
						Date dataInizioResidenza=pfVO.getDataInizioResidenza();
						try
						{
							//        Elimino eventuali ore/minuti/secondi
							dataInizioResidenza=DateUtils.parseDate(DateUtils.formatDate(dataInizioResidenza));
						}
						catch (Exception ex)
						{  
						  //Non faccion niente     
						}
						SolmrLogger.debug(this,"dataInizioResidenza="+dataInizioResidenza);
						if (pfVO!=null && dataInizioResidenza!=null && data.before(dataInizioResidenza))
						{
							pfVO=cDAO.serviceGetStoricoResidenza(pfVO.getIdPersonaFisica(),data,pfVO);
						}
					}
				}
			}
			return (PersonaFisicaVO[])result.toArray(new PersonaFisicaVO[0]);
		}
		catch(NotFoundException ne)
		{
			return null;
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
	}*/

	/*public PersonaFisicaVO serviceGetDettaglioSoggetti(Long idSoggetto,
			Long idAzienda,
			java.util.Date data)
	throws Exception, SolmrException
	{
		if (idSoggetto==null || idAzienda == null || data==null)
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,ErrorTypes.INVALID_PARAMETER);
		PersonaFisicaVO pfVO = null;
		try
		{
			pfVO = sDAO.getDettaglioSoggetti(idSoggetto, idAzienda, data);
			if (pfVO!=null)
			{
				try
				{
					//        Elimino eventuali ore/minuti/secondi
					data=DateUtils.parseDate(DateUtils.formatDate(data));
				}
				catch (Exception ex)
				{ 
				  // Non faccion niente 
				    
				}
				Date dataInizioResidenza=pfVO.getDataInizioResidenza();
				try
				{
					//        Elimino eventuali ore/minuti/secondi
					dataInizioResidenza=DateUtils.parseDate(DateUtils.formatDate(dataInizioResidenza));
				}
				catch (Exception ex)
				{ 
				// Non faccion niente 
				  
				}
				if (pfVO!=null && data.before(dataInizioResidenza))
				{
					pfVO=cDAO.serviceGetStoricoResidenza(pfVO.getIdPersonaFisica(),data,pfVO);
				}
			}
		}
		catch(NotFoundException ne)
		{
			return null;
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
		return pfVO;
	}*/

	/*public PersonaFisicaVO serviceGetSoggetto(Long idSoggetto)
	throws SolmrException, Exception
	{
		if (idSoggetto==null)
		{
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,ErrorTypes.INVALID_PARAMETER);
		}
		PersonaFisicaVO pfVO = null;
		try
		{
			pfVO = sDAO.serviceGetSoggetto(idSoggetto);
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
		return pfVO;
	}*/

	/*public PersonaFisicaVO serviceGetSoggetto(Long idSoggetto, Date date)
	throws SolmrException, Exception
	{
		if (idSoggetto==null)
		{
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,ErrorTypes.INVALID_PARAMETER);
		}
		PersonaFisicaVO pfVO = null;
		try
		{
			pfVO = sDAO.serviceGetSoggetto(idSoggetto);
			if (date!=null && pfVO!=null)
			{
				try
				{
//					Elimino eventuali ore/minuti/secondi
					date=DateUtils.parseDate(DateUtils.formatDate(date));
				}
				catch (Exception ex){ 
				//Non faccion niente 
				  
				}
				Date dataInizioResidenza=pfVO.getDataInizioResidenza();
				try
				{
//					Elimino eventuali ore/minuti/secondi
					dataInizioResidenza=DateUtils.parseDate(DateUtils.formatDate(dataInizioResidenza));
				}
				catch (Exception ex){ 
				//Non faccion niente    
				 
				 }
				if (SolmrLogger.isDebugEnabled(this))
				{
					SolmrLogger.debug(this,"\n\ndate="+date);
					SolmrLogger.debug(this,"\n\n="+dataInizioResidenza);
					SolmrLogger.debug(this,"\n\ndate="+(date.before(dataInizioResidenza)));
				}
				if (pfVO!=null && date.before(dataInizioResidenza))
				{
					pfVO=cDAO.serviceGetStoricoResidenza(pfVO.getIdPersonaFisica(),date,pfVO);
				}
			}
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
		return pfVO;
	}*/

	/*public ContitolareVO serviceGetRuoloSoggetto(Long idSoggetto,
			Long idAzienda,
			Date dataSituazioneAl)
	throws Exception, SolmrException
	{
		if (idSoggetto==null || idAzienda==null)
		{
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,ErrorTypes.INVALID_PARAMETER);
		}
		if (dataSituazioneAl==null)
		{
			try
			{
				dataSituazioneAl=DateUtils.parseDate(DateUtils.getCurrentDateString());
			}
			catch (Exception ex)
			{
			}
		}
		ContitolareVO contitolareVO = null;
		try
		{
			contitolareVO = sDAO.serviceGetRuoloSoggetto(idSoggetto,idAzienda,dataSituazioneAl);
		}
		catch (DataAccessException dex)
		{
			throw new Exception(dex.getMessage());
		}
		return contitolareVO;
	}*/


	/*public PersonaFisicaVO[] serviceGetListSoggettiByIdSoggettoRange(Long idSoggetto[])
	throws
	Exception, SolmrException
	{
		// Controllo parametri 
		if (idSoggetto == null || idSoggetto.length == 0) {
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
					ErrorTypes.INVALID_PARAMETER);
		}
		// Tutti i valori passati nell'array devono essere != null 
		for (int i = 0; i < idSoggetto.length; i++) {
			if (idSoggetto[i] == null) {
				throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
						ErrorTypes.INVALID_PARAMETER);
			}
		}
		try {
			return sDAO.serviceGetListSoggettiByIdSoggettoRange(idSoggetto);
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}*/

	/*public boolean  serviceVerificaGerarchiaIride1(Long idUtenteConnesso,Long idUtentePratica)
	throws  Exception, SolmrException
	{
		// Controllo parametri 
		if (idUtenteConnesso == null || idUtentePratica == null)
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,ErrorTypes.INVALID_PARAMETER);
		try
		{
			return cDAO.serviceVerificaGerarchiaIride1(idUtenteConnesso,idUtentePratica);
		}
		catch (DataAccessException ex)
		{
			throw new Exception(ex.getMessage());
		}
	}*/

	public void importaSoggCollAAEP(AnagAziendaVO anagAziendaVO, Azienda impresaInfoc, HashMap<?,?> listaPersone,String idParametri[], Long idUtenteAggiornamento)
		throws Exception,SolmrException 
	{
		try
		{
			if(impresaInfoc!=null)
			{
			  List<ListaPersoneRI> listaPersonaRIInfoc=impresaInfoc.getListaPersoneRI();

				if (listaPersonaRIInfoc!=null)
				{
					for(int i=0;i<listaPersonaRIInfoc.size();i++)
					{
						Object pers[]=new Object[2];
						pers=(Object[])listaPersone.get(listaPersonaRIInfoc.get(i).getProgrPersona().getValue());

						if (pers!=null)
						{
							PersonaRIInfoc personaRIInfoc=(PersonaRIInfoc)pers[0];
							if (personaRIInfoc!=null)
							{
								List<CaricaPersonaInfoc> caricaPersonaInfoc=personaRIInfoc.getListaCaricaPersInfoc();
								if (caricaPersonaInfoc!=null)
								{
									//Visualizzo i dati dei soggetti collegati
									for (int j=0;j<caricaPersonaInfoc.size();j++)
									{

										//I soggetti con carica cessata (CaricaPersonaInfoc.dataFineCarica not null)
										//non sono importabili, non visualizzare il ceck di selezione nell\u2019elenco.
										// Anche I soggetti che presentano almeno una di queste casistiche,
										// non sono importabili (non visualizzare il check di selezione nell’elenco):
										// - data inizio carica non valorizzata
										// - codice fiscale non valorizzato
										// - descrizione carica non valorizzato
										// - cognome non valorizzato
										// - nome non valorizzato

										if (caricaPersonaInfoc.get(j).getDataFineCarica().getValue()==null
												&&
												caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()!=null
												&&
												personaRIInfoc.getCodiceFiscale().getValue()!=null
												&&
												caricaPersonaInfoc.get(j).getDescrCarica().getValue()!=null
												&&
												personaRIInfoc.getCognome().getValue()!=null
												&&
												personaRIInfoc.getNome().getValue()!=null)
										{
											String id=personaRIInfoc.getProgrOrdineVisura()+"-"+caricaPersonaInfoc.get(j).getCodiceCarica().getValue();
											boolean daImportare = false;
											for(int k=0;k<idParametri.length;k++)
											{
												if (id.equals(idParametri[k]))
													daImportare=true;
											}
											if (daImportare)
											{
												Long idSoggetto=cDAO.getSoggettoInAnagrafe(personaRIInfoc.getCodiceFiscale().getValue());
												SolmrLogger.debug(this,"\n\n\n\n"+personaRIInfoc.getCodiceFiscale().getValue() +" IdSoggetto "+idSoggetto);
												Long idRuolo=cDAO.getDecodificaRuoloAAEP(caricaPersonaInfoc.get(j).getCodiceCarica().getValue());


												PersonaFisicaVO pfNew=new PersonaFisicaVO();
												
												
												// codComuneRes : decodificare per avere codice istat prov + codice istat comune Es : 077+014
												SolmrLogger.error(this, " --- personaRIInfoc.codComuneRes ="+personaRIInfoc.getCodComuneRes().getValue());
												if(personaRIInfoc.getCodComuneRes().getValue() != null && personaRIInfoc.getSiglaProvResidenza().getValue() != null){
													String istatProv = cDAO.getIstatProvinciaBySiglaProvincia(personaRIInfoc.getSiglaProvResidenza().getValue());
													SolmrLogger.error(this, " --- istatProv ="+istatProv);
													String codComuneResNew = istatProv+personaRIInfoc.getCodComuneRes().getValue();
													SolmrLogger.error(this, " --- codComuneResNew ="+codComuneResNew);
													
													QName capQName = new QName("http://servizio.frontend.ls.com", "codComuneRes");
											        JAXBElement<String> codComuneResNewValue = new JAXBElement<String>(capQName, String.class, codComuneResNew);																										
													personaRIInfoc.setCodComuneRes(codComuneResNewValue);
												}

												/**
												 * Stati esteri
												 * In inserimento/aggiornamento ricercare la descrizione restituita dal servizio
												 * (PersonaRIInfoc.DescrStatoNascita , PersonaRIInfoc.DescrStatoRes) sulla tabella dei comuni in anagrafe (comune.descom).
												 *  - Se lo stato estero non viene trovato, aggiornare/inserire le seguenti
												 *    colonne della tabella db_persona_fisica
												 *      Nascita_comune	Null
												 *      citta_estero_nas	PersonaRIInfoc.DescrStatoNascita
												 *      Res_comune	Null
												 *      Citta_estero_res	PersonaRIInfoc.DescrStatoRes
												 *  -	Se lo stato estero viene trovato, aggiornare/inserire le seguenti
												 *    colonne della tabella db_persona_fisica
												 *      Nascita_comune	Comune.istat_comune a fronte del comune di nascita
												 *      citta_estero_nas	Null
												 *      Res_comune	Comune.istat_comune a fronte del comune di residenza
												 *      Citta_estero_res	Null
												 * */
												if (personaRIInfoc.getDescrStatoNascita().getValue()!=null)
												{
													try
													{
														String codComuneNascita = cDAO.getIstatByDescComune(personaRIInfoc.getDescrStatoNascita().getValue());
														QName capQName = new QName("http://servizio.frontend.ls.com", "codComuneNascita");
												        JAXBElement<String> codComuneNascitaValue = new JAXBElement<String>(capQName, String.class, codComuneNascita);
														
														personaRIInfoc.setCodComuneNascita(codComuneNascitaValue);
													}catch(Exception e) {}
													pfNew.setNascitaCittaEstero(personaRIInfoc.getDescrStatoNascita().getValue());
												}
												if (personaRIInfoc.getDescrStatoRes().getValue()!=null)
												{
													try
													{
														String codComuneRes = cDAO.getIstatByDescComune(personaRIInfoc.getDescrStatoNascita().getValue());
														QName capQName = new QName("http://servizio.frontend.ls.com", "codComuneRes");
												        JAXBElement<String> codComuneResValue = new JAXBElement<String>(capQName, String.class, codComuneRes);
												        
														personaRIInfoc.setCodComuneRes(codComuneResValue);
													}catch(Exception e) {}
													pfNew.setResCittaEstero(personaRIInfoc.getDescrStatoRes().getValue());
												}
												

												/**
												 * Vado a recuprerare i dati da AAEP per creare l'indirizzo
												 * */
												 String descrToponimoResid= personaRIInfoc.getDescrToponimoResid().getValue();
												 if (descrToponimoResid==null) descrToponimoResid="";
												 String viaResidenza= personaRIInfoc.getViaResidenza().getValue();
												 if (viaResidenza==null) viaResidenza="";
												 String numCivicoResid= personaRIInfoc.getNumCivicoResid().getValue();
												 if (numCivicoResid==null) numCivicoResid="";
												 String descrFrazioneRes= personaRIInfoc.getDescrFrazioneRes().getValue();
												 if (descrFrazioneRes==null) descrFrazioneRes="";
												 String resIndirizzo=descrToponimoResid+" "+viaResidenza+" "+numCivicoResid+" "+descrFrazioneRes;
												 
												 //Ricavo il codice fiscale del comune di nascita
												 String codFiscaleComune=null;
												 try
												 {
													 ComuneVO comuneTemp=cDAO.getComuneByISTAT(personaRIInfoc.getCodComuneNascita().getValue());												 
													 if (comuneTemp!=null)
														 codFiscaleComune=comuneTemp.getCodfisc();
												 }
												 catch(Exception e){}
												 //Controllo che i codici fiscali dei soggetti selezionati siano congruenti con i dati, altrimenti non
												 //lascio importare
												 String  errCodFis=verificaCf(personaRIInfoc.getNome().getValue(),personaRIInfoc.getCognome().getValue(),personaRIInfoc.getSesso().getValue(),
														 DateUtils.convert(personaRIInfoc.getDataNascita().getValue()),codFiscaleComune,personaRIInfoc.getCodiceFiscale().getValue());
													
												 if (errCodFis!=null) throw new SolmrException(errCodFis);


												 if (idSoggetto!=null)
												 {
													 //Codice Fiscale presente in archivio
													 //Controllo i dati relativi alla persona fisica

													 /**
													  * Nel caso in cui i campi discordanti riguardino la residenza del soggetto, in dettaglio:
													  * - indirizzo di residenza
													  * - comune di residenza
													  * - cap di residenza
													  * storicizzare i dati della residenza preocedere nel seguente modo:
													  * - inserire un record sulla tabella db_storico_residenza
													  *   valorizzando i dati  elencati nello specchietto sottostante
													  * - aggiornare l’occorrenza della tabella db_persona_fisica
													  *   valorizzato i dati elencati nello specchietto sottostante
													  *   che riguardano la residenza del soggetto.
													  * Devono essere aggiornati anche gli altri eventuali campi modificati dall’utente.
													  */

													 PersonaFisicaVO personaFisicaVO=sDAO.serviceGetSoggetto(idSoggetto);


													 boolean modificaResidenza=false;
													 boolean modificaAltreGeneralita=false;
													 
													 if("MULTI".equalsIgnoreCase(personaRIInfoc.getCapResidenza().getValue()))
													 {
													   try
		                         {
													     ComuneVO comuneRes = cDAO.getComuneByISTAT(personaRIInfoc.getCodComuneRes().getValue());													     
													     
													     QName capQName = new QName("http://servizio.frontend.ls.com", "capResidenza");
													     JAXBElement<String> capResidenzaValue = new JAXBElement<String>(capQName, String.class, comuneRes.getCap());
													     
													     personaRIInfoc.setCapResidenza(capResidenzaValue);													     													    
		                         }
													   catch(Exception e) {}
													 }


													 if(!confrontaStr(personaFisicaVO.getResIndirizzo(),resIndirizzo)) modificaResidenza=true;
													 if(!confrontaStr(personaFisicaVO.getResCAP(),personaRIInfoc.getCapResidenza().getValue())) modificaResidenza=true;
													 if(!confrontaStr(personaFisicaVO.getResComune(),personaRIInfoc.getCodComuneRes().getValue())) modificaResidenza=true;
													 if(!confrontaStr(personaFisicaVO.getCognome(),personaRIInfoc.getCognome().getValue())) modificaAltreGeneralita=true;
													 if(!confrontaStr(personaFisicaVO.getNome(),personaRIInfoc.getNome().getValue())) modificaAltreGeneralita=true;
													 if(!confrontaStr(personaFisicaVO.getSesso(),personaRIInfoc.getSesso().getValue())) modificaAltreGeneralita=true;
													 if(!confrontaStr(personaFisicaVO.getNascitaComune(),personaRIInfoc.getCodComuneNascita().getValue())) modificaAltreGeneralita=true;
													 String strDataNascitaAAEP="";
													 if (personaRIInfoc.getDataNascita()!=null)
														 strDataNascitaAAEP=DateUtils.formatDate(DateUtils.convert(personaRIInfoc.getDataNascita().getValue()));
													 String strDataNascita="";
													 if (personaFisicaVO.getNascitaData()!=null)
														 strDataNascita=DateUtils.formatDate(personaFisicaVO.getNascitaData());

													 if(!confrontaStr(strDataNascita,strDataNascitaAAEP)) modificaAltreGeneralita=true;

													 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": modificaResidenza "+modificaResidenza+"\n\n\n\n");
													 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": modificaAltreGeneralita "+modificaAltreGeneralita+"\n\n\n\n");

													 if (modificaResidenza)
													 {
														 //Sono cambiati i dati della residenza e quindi
														 //devo storicizzare
														 if (sDAO.controllaStoricizzazioneResidenza(personaFisicaVO.getIdPersonaFisica().longValue()))
														 {
															 awDAO.insertStoricoResidenza(personaFisicaVO,idUtenteAggiornamento);
														 }
													 }
													 if (modificaResidenza || modificaAltreGeneralita)
													 {
														 //Devo aggiornare i dati di DB_PERSONA_FISICA
														 pfNew.setCognome(personaRIInfoc.getCognome().getValue());
														 pfNew.setNome(personaRIInfoc.getNome().getValue());
														 pfNew.setSesso(personaRIInfoc.getSesso().getValue());
														 pfNew.setNascitaComune(personaRIInfoc.getCodComuneNascita().getValue());
														 pfNew.setNascitaData(DateUtils.convert(personaRIInfoc.getDataNascita().getValue()));
														 pfNew.setResIndirizzo(resIndirizzo);
														 pfNew.setResComune(personaRIInfoc.getCodComuneRes().getValue());
														 pfNew.setResCAP(personaRIInfoc.getCapResidenza().getValue());
														 pfNew.setIdPersonaFisica(personaFisicaVO.getIdPersonaFisica());
														 awDAO.updatePersonaFisicaForAAEP(pfNew,idUtenteAggiornamento);
													 }

													 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": presente in anagrafe \n\n\n\n");
													 //Controllare se il soggetto è già legato all'azienda
													 if (cDAO.isRuolosuAnagrafe(personaRIInfoc.getCodiceFiscale().getValue(),anagAziendaVO.getIdAzienda().longValue()))
													 {
														 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": legato all'azienda \n\n\n\n");
														 //Soggetto legato all'azienda
														 //Controllo se il ruolo è lo stesso
														 String dataInizioRuolo=cDAO.confrontaRuoloAAEPconAnagrafe(idRuolo,idSoggetto);
														 SolmrLogger.debug(this,"\n\n\n\n dataInizioRuolo "+dataInizioRuolo);
														 if (dataInizioRuolo==null)
														 {
															 //Ruolo discordante
															 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": Ruolo discordante \n\n\n\n");
															 sDAO.insertContitolareRuolo(anagAziendaVO.getIdAzienda(),
																	 idSoggetto,
																	 idRuolo,
																	 DateUtils.convert(caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()));

														 }
														 else
														 {
															 //Ruolo concorde con anagrafe
															 //controllo che la data di inizio ruolo sia la stessa
															 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": ruolo concorde \n\n\n\n");

															 String strInizioCaricaAAEP="";
															 if (caricaPersonaInfoc.get(j).getDataInizioCarica()!=null)
																 strInizioCaricaAAEP=DateUtils.formatDate(DateUtils.convert(caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()));
															 if(confrontaStr(dataInizioRuolo,strInizioCaricaAAEP))
															 {
																 //data uguale: nbon devo far niente
																 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": data inizio ruolo uguale \n\n\n\n");
															 }
															 else
															 {
																 //data diversa: devo aggiornarla
																 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": data inizio ruolo diversa \n\n\n\n");
																 sDAO.updateDataInizRuoloDbContitolare(idRuolo, idSoggetto,DateUtils.convert(caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()));
															 }


														 }
													 }
													 else
													 {
														 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": non legato all'azienda \n\n\n\n");
														 //Soggetto non legato all'azienda
														 sDAO.insertContitolareRuolo(anagAziendaVO.getIdAzienda(),
																 idSoggetto,
																 idRuolo,
																 DateUtils.convert(caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()));
														 

													 }
												 }
												 else
												 {
													 SolmrLogger.debug(this,"\n\n\n\n Soggetto "+personaRIInfoc.getCodiceFiscale()+": non presente in anagrafe \n\n\n\n");
													 //se il codice fiscale non è ancora presente in archivio
													 //inserire una nuova persona fisica in archivio e legare
													 //il nuovo soggetto all’azienda
													 pfNew.setIdSoggetto(awDAO.insertSoggetto(SolmrConstants.FLAG_S));
													 pfNew.setCodiceFiscale(personaRIInfoc.getCodiceFiscale().getValue());
													 pfNew.setCognome(personaRIInfoc.getCognome().getValue());
													 pfNew.setNome(personaRIInfoc.getNome().getValue());
													 pfNew.setSesso(personaRIInfoc.getSesso().getValue());
													 pfNew.setNascitaComune(personaRIInfoc.getCodComuneNascita().getValue());
													 pfNew.setNascitaData(DateUtils.convert(personaRIInfoc.getDataNascita().getValue()));
													 pfNew.setDataInizioResidenza(DateUtils.convert(caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()));
													 pfNew.setResIndirizzo(resIndirizzo);
													 pfNew.setResComune(personaRIInfoc.getCodComuneRes().getValue());
													 pfNew.setResCAP(personaRIInfoc.getCapResidenza().getValue());

													 awDAO.insertPersonaFisicaForAAEP(pfNew,idUtenteAggiornamento);
													 sDAO.insertContitolareRuolo(anagAziendaVO.getIdAzienda(),
															 pfNew.getIdSoggetto(),
															 idRuolo,
															 DateUtils.convert(caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()));
												 }
											}
										}
									}
								}
							}
						}
					}
				}

			}
		}
		catch (DataAccessException ex)
		{
			sessionContext.setRollbackOnly();
			SolmrLogger.error(this, "-- DataAccessException in importaSoggCollAAEP ="+ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}


	private boolean confrontaStr(String str1,String str2)
	{
		if (str1==null)
		{
			if (str2==null || "".equals(str2.trim())) return true;
			else return false;
		}
		else
		{
			if ("".equals(str1.trim()))
			{
				if (str2==null || "".equals(str2.trim())) return true;
				else return false;
			}
			else
			{
				if (str2==null) return false;
				if (eliminaSpazi(str1.trim()).equalsIgnoreCase(eliminaSpazi(str2.trim()))) return true;
			}
			return false;
		}
	}

	private String eliminaSpazi(String str)
	{
		StringBuffer result=new StringBuffer();
		if (str==null || str.equals("")) return "";
		int size=str.length();
		char old=str.charAt(0);
		boolean spazio=false;
		if (old==' ') spazio=true;
		if (!spazio) result.append(old);
		for (int i=1;i<size;i++)
		{
			if (!(spazio && str.charAt(i)==' ')) result.append(str.charAt(i));
			old=str.charAt(i);
			if (old==' ') spazio=true;
			else spazio=false;
		}
		//SolmrLogger.debug(this, "\n\n\n\nresult.toString() "+result.toString());
		return result.toString();
	}
	
	private String verificaCf(String nome,String cognome,String sesso,Date nascitaData,String codFiscaleComune,String codiceFiscale)
	{
		String result=null;
		 // Controllo della coerenza del codice fiscale
	  try 
	  {
	    Validator.verificaCf(nome,cognome,sesso,nascitaData,codFiscaleComune, codiceFiscale);
	  }
	  catch(CodiceFiscaleException ce) 
	  {	  	
	    if(ce.getNome())
	    	result=AnagErrors.ERR_AEEP_NOME_CODICE_FISCALE;
	    else 
	    	if(ce.getCognome()) 
	    		result=AnagErrors.ERR_AEEP_COGNOME_CODICE_FISCALE;
		    else 
		    	if(ce.getAnnoNascita()) 
			    	result=AnagErrors.ERR_AEEP_ANNO_NASCITA_CODICE_FISCALE;
			    else 
			    	if(ce.getMeseNascita()) 
				    	result=AnagErrors.ERR_AEEP_MESE_NASCITA_CODICE_FISCALE;
				    else 
				    	if(ce.getGiornoNascita()) 
					    	result=AnagErrors.ERR_AEEP_GIORNO_NASCITA_CODICE_FISCALE;
					    else 
					    	if(ce.getComuneNascita()) 
						    	result=AnagErrors.ERR_AEEP_COMUNE_NASCITA_CODICE_FISCALE;
						    else 
						    	if(ce.getSesso()) 
							    	result=AnagErrors.ERR_AEEP_SESSO_CODICE_FISCALE;
							    else 
							    	result=ce.getMessage()+": impossibile procedere";
	    result= result +codiceFiscale;
	  }
	  return result;
	}

}

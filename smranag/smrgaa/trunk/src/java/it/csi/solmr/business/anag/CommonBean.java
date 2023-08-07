package it.csi.solmr.business.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.util.SolmrLogger;


import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Bean per la gestione e l'esposizione dei metodi di utilità comune all'anagrafe
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */
@Stateless(name="comp/env/solmr/anag/Common",mappedName="comp/env/solmr/anag/Common")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class CommonBean implements CommonLocal {

	/**
	 *
	 */
	private static final long serialVersionUID = -8108268976505444091L;
	SessionContext sessionContext;
	private transient CommonDAO commonDAO = null;

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
		initializeDAO();
	}

	private void initializeDAO() throws EJBException {
		try {
			commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
		}
		catch (ResourceAccessException ex) {
			SolmrLogger.error(this, ex.getMessage());
			throw new EJBException(ex);
		}
	}

	/**
	 * Metodo che si occupa di i dati dalla tabella REGIONE in relazione all'istat della provincia
	 *
	 * @param istatProvincia String
	 * @return CodeDescription
	 * @throws Exception
	 */
	public CodeDescription findRegioneByIstatProvincia(String istatProvincia) throws Exception {
		try {
			return commonDAO.findRegioneByIstatProvincia(istatProvincia);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo che si occupa di reperire i dati dalla tabella REGIONE relativi
	 * all'intermediario loggato partendo dal codice_fiscale
	 *
	 * @param codiceFiscaleIntermediario String
	 * @return CodeDescription
	 * @throws Exception
	 */
	public CodeDescription findRegioneByCodiceFiscaleIntermediario(String codiceFiscaleIntermediario) throws Exception {
		try {
			return commonDAO.findRegioneByCodiceFiscaleIntermediario(codiceFiscaleIntermediario);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo che si occupa di estrarre tutti i gruppo controllo associati
	 * ad una determinata dichiarazione di consistenza
	 *
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoGruppoControlloByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza, String[] orderBy) throws Exception {
		try {
			return commonDAO.getListTipoGruppoControlloByIdDichiarazioneConsistenza(idDichiarazioneConsistenza, orderBy);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

  
  public Object getValoreParametroAltriDati(String codiceParametro)
    throws Exception
  {
    try 
    {
      return commonDAO.getValoreParametroAltriDati(codiceParametro);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * Usaato per restituire i dati relativi alle tabelle di decodifica di anagrafe create con i nuovi standard
   * @param tableName nome della tabella da cui prendere i dati
   * @param filtro viene utilizzato quando si vuole filtrare la tabella per un id: usato quando si vogliono caricare
   * combo che dipendano da altre combo
   * @param orderBy clausola di order by, se non viene inserita l'ordinamento viene fatto per descrizione
   * @return
   * @throws Exception
   */
  public CodeDescription[] getCodeDescriptionsNew(String tableName, String filtro, String valFiltro, String orderBy)
    throws Exception
  {
    try 
    {
      return commonDAO.getCodeDescriptionsNew(tableName, filtro, valFiltro, orderBy);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public it.csi.solmr.dto.profile.CodeDescription getGruppoRuolo(String ruolo)
    throws Exception
  {
    try 
    {
      return commonDAO.getGruppoRuolo(ruolo);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public String testDB() throws Exception
  {
      return commonDAO.testDB();
  }
  
  
        

}

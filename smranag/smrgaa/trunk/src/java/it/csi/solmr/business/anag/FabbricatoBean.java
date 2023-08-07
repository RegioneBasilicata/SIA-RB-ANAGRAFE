package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.services.FabbricatoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.integration.anag.FabbricatoDAO;
import it.csi.solmr.util.SolmrLogger;


import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/Fabbricato",mappedName="comp/env/solmr/anag/Fabbricato")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class FabbricatoBean implements FabbricatoLocal {
	
	private static final long serialVersionUID = 8163638825017557527L;
	SessionContext sessionContext;
	private transient FabbricatoDAO fabbricatoDAO = null;

	
    @Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
		initializeDAO();
	}

	private void initializeDAO() throws EJBException {
		try {
			fabbricatoDAO = new FabbricatoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
		}
		catch (ResourceAccessException ex) {
			SolmrLogger.fatal(this, ex.getMessage());
			throw new EJBException(ex);
		}
	}
	
	/**
	 * Metodo utilizzato per estrarre i fabbricati di un'azienda relativi ad una
	 * specifica particella
	 * 
	 * @param idConduzioneParticella
	 * @param orderBy
	 * @param onlyActive
	 * @return
	 * @throws Exception
	 */
	public FabbricatoParticellaVO[] getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(Long idConduzioneParticella, Long idAzienda, String[] orderBy, boolean onlyActive) throws Exception {
		try {
			return fabbricatoDAO.getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(idConduzioneParticella, idAzienda, orderBy, onlyActive);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	/**
	 * Metodo che mi permette di recuperare l'elenco dei fabbricati di un'azienda
	 * relativi ad un determinato piano di riferimento
	 * 
	 * @param idAzienda
	 * @param idPianoRiferimento
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.FabbricatoParticellaVO[]
	 * @throws DataAccessException
	 */
	public FabbricatoVO[] getListFabbricatiAziendaByPianoRifererimento(Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy) throws Exception {
		try {
			return fabbricatoDAO.getListFabbricatiAziendaByPianoRifererimento(idAzienda, idPianoRiferimento, idUte, orderBy);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
}

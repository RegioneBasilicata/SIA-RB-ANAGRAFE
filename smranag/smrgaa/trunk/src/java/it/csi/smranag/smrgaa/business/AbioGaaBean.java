package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.abio.OperatoreBiologicoVO;
import it.csi.smranag.smrgaa.dto.abio.PosizioneOperatoreVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.integration.AbioDAO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name=AbioGaaBean.jndiName,mappedName=AbioGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AbioGaaBean implements AbioGaaLocal {

	SessionContext sessionContext;
	private AbioDAO abioDAO = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2974006393105765578L;
	public final static String jndiName="comp/env/solmr/gaa/AbioGaa";

	
	
	@Resource
	public void setSessionContext(SessionContext sessionContext) throws EJBException
	{
	    SolmrLogger.debug(this, "[AbioBean::setSessionContext] BEGIN.");
	    this.sessionContext = sessionContext;
	    initializeDAO();
	    SolmrLogger.debug(this, "[AbioBean::setSessionContext] END.");
	}


	public void initializeDAO() throws EJBException
	{
		SolmrLogger.debug(this, "[AbioBean::initializeDAO] BEGIN.");
		try
		{
		  abioDAO = new AbioDAO();
		}
		catch (Exception e)
		{
		  SolmrLogger.fatal(this, "[AbioBean::initializeDAO] Eccezione nella creazione del DAO AbioDAO. Eccezione: "
		      + LoggerUtils.getStackTrace(e));
		}
	
		SolmrLogger.debug(this, "[AbioBean::initializeDAO] END.");
	}
	  
	public OperatoreBiologicoVO getOperatoreBiologicoByIdAzienda(Long idAzienda, Date dataInizioAttivita) throws SolmrException{
		OperatoreBiologicoVO operatoreBiologico = null;
		try {
			operatoreBiologico = abioDAO.getOperatoreBiologicoByExtIdAzienda(idAzienda, dataInizioAttivita);
		} 
		catch (Exception e)
	    {
	      // Log dell'errore
	      Parametro parametri[] = new Parametro[]
	      { new Parametro("idAzienda", idAzienda), 
	    	new Parametro("dataInizioAttivita", dataInizioAttivita)};
	      Variabile variabile[] = new Variabile[]
	      { new Variabile("operatoreBiologico", operatoreBiologico)};

	      LoggerUtils.logEJBError(this, "[AbioGaaBean::getOperatoreBiologicoByIdAzienda]", e, variabile, parametri);
	      throw new SolmrException(e.getMessage());
	    }
	    finally
	    {
	      SolmrLogger.debug(this, "[AbioGaaBean::getOperatoreBiologicoByIdAzienda] END.");
	    }
	    return operatoreBiologico;
	}
	
	public PosizioneOperatoreVO[] getAttivitaBiologicheByIdAzienda(
		      Long idOperatoreBiologico, Date dataFineValidita, boolean checkStorico) throws SolmrException{
		
		PosizioneOperatoreVO[] posizioneOperatoreVO = null;
		try {
			posizioneOperatoreVO = abioDAO.getAttivitaBiologicheByIdAzienda(idOperatoreBiologico, dataFineValidita, checkStorico);
		} 
		catch (Exception e)
	    {
	      // Log dell'errore
	      Parametro parametri[] = new Parametro[]
	      { new Parametro("idOperatoreBiologico", idOperatoreBiologico), 
	  	    new Parametro("dataFineValidita", dataFineValidita),
	    	new Parametro("checkStorico", checkStorico)};
	      Variabile variabile[] = new Variabile[]
	      { new Variabile("posizioneOperatoreVO", posizioneOperatoreVO)};

	      LoggerUtils.logEJBError(this, "[AbioGaaBean::getAttivitaBiologicheByIdAzienda]", e, variabile, parametri);
	      throw new SolmrException(e.getMessage());
	    }
	    finally
	    {
	      SolmrLogger.debug(this, "[AbioGaaBean::getAttivitaBiologicheByIdAzienda] END.");
	    }
	    return posizioneOperatoreVO;
	}
	
	public CodeDescription[] getODCbyIdOperatoreBiologico(
			Long idOperatoreBiologico, Date dataInizioValidita, boolean pianoCorrente) throws SolmrException{
		  
		CodeDescription[] codeDesc = null;
		try{
			codeDesc = abioDAO.getODCbyIdOperatoreBiologico(idOperatoreBiologico, dataInizioValidita, pianoCorrente);
		}
		catch(Exception e){
		  
		      // Log dell'errore
			Parametro parametri[] = new Parametro[]
			{ new Parametro("idOperatoreBiologico", idOperatoreBiologico), 
			  new Parametro("dataInizioValidita", dataInizioValidita),
			  new Parametro("pianoCorrente", pianoCorrente)};
			Variabile variabile[] = new Variabile[]
			{ new Variabile("codeDesc", codeDesc)};

			LoggerUtils.logEJBError(this, "[AbioGaaBean::getODCbyIdOperatoreBiologico]", e, variabile, parametri);
			throw new SolmrException(e.getMessage());
	    }
	    finally
	    {
	      SolmrLogger.debug(this, "[AbioGaaBean::getODCbyIdOperatoreBiologico] END.");
	    }
	    return codeDesc;
	  }
	
	public OperatoreBiologicoVO getOperatoreBiologicoAttivo(Long idAzienda)
    throws SolmrException
  {
    
    try
    {
      return abioDAO.getOperatoreBiologicoAttivo(idAzienda); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
}

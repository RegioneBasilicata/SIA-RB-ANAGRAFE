package it.csi.smranag.smrgaa.business;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioFabbricato;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioTerreno;
import it.csi.sigmater.sigtersrv.dto.daticatastali.SoggettoFisico;
import it.csi.sigmater.sigtersrv.dto.daticatastali.SoggettoGiuridico;
import it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita;
import it.csi.sigmater.sigtersrv.exception.daticatastali.AutorizzMancanteEnteException;
import it.csi.sigmater.sigtersrv.exception.daticatastali.OutputException;
import it.csi.sigmater.sigtersrv.exception.daticatastali.ParInputObblMancantiException;
import it.csi.sigmater.sigtersrv.exception.daticatastali.ParInputValNonCorrettoException;
import it.csi.sigmater.sigtersrv.exception.daticatastali.PermissionException;
import it.csi.sigmater.sigtersrv.interfacecsi.daticatastali.DatiCatastaliSrv;
import it.csi.smranag.smrgaa.util.AnagUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.util.SolmrLogger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * EJB utilizzato per accedere ai servizi PA/PD esposti da SigMater
 * @author 70525
 *
 */
@Stateless(name=SigmaterBean.jndiName,mappedName=SigmaterBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SigmaterBean implements SigmaterLocal
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3414392662188899897L;
	public final static String jndiName = "comp/env/solmr/gaa/Sigmater";

	SessionContext sessionContext;
	
	private transient CommonDAO cDAO;
	
	private void initializeDAO() throws EJBException
	{
	    try
	    {
	    	cDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	    }
	    catch (ResourceAccessException ex)
	    {
	      SolmrLogger.fatal(this, ex.getMessage());
	      throw new EJBException(ex);
	    }
	}
	
	private transient InfoPortaDelegata informazioniPortaDelegata;

	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
		try
		{
			initializeDAO();
			// caricamento porta delegata
			informazioniPortaDelegata = PDConfigReader.read(getClass().getResourceAsStream(SolmrConstants.FILE_PD_SIGMATER));
		}
		catch(Exception e)
		{
			SolmrLogger.fatal(this, "SigmaterBean:setSessionContext: "+e.getMessage());
		}
	}

	/**
	 *
	 * @throws Exception
	 */
	private DatiCatastaliSrv getSigmaterClient() throws Exception
	{
		//Recupero i dati dell'utenza e sella password
		String dati[]=cDAO.getUserPwdServizio(SolmrConstants.NOME_SERVIZIO_SIGMATER);
		//Imposto i parametri
		InfoPortaDelegata info = informazioniPortaDelegata.getPlugins()[0];
	  //info.getProperties().put("csi.auth.basic","APPL_TEST/test");
		info.getProperties().put("csi.auth.basic",dati[0]+"/"+dati[1]);
		return (DatiCatastaliSrv) PDProxy.newInstance(informazioniPortaDelegata);
	}
	
	/**
	 * Il servizio Ricerca dati di Terreno restituisce tutti i dati riguardanti un terreno identificato dai parametri di input.
	 * @param codIstatComune codice Istat del Comune 
	 * @param codBelfioreComune codice Belfiore del Comune
	 * @param sezione sezione del comune
	 * @param foglio unità territoriale
	 * @param numero unità territoriale
	 * @param subalterno unità territoriale
	 * @param progressivo numero indicante lo stadio dell’immobile
	 * @return
	 * @throws SolmrException
	 */
	public DettaglioTerreno cercaTerreno(String codIstatComune,
										 String codBelfioreComune,
										 String sezione,
										 String foglio,
										 String numero,
										 String subalterno,
										 String progressivo)
	throws SolmrException
	{
		try
		{
			return getSigmaterClient().cercaTerreno(codIstatComune, codBelfioreComune, sezione, AnagUtils.leftFill(foglio, '0', 5), AnagUtils.leftFill(numero, '0', 5), AnagUtils.leftFill(subalterno, '0', 4), progressivo);
		}
		catch(PermissionException pe)
		{
			SolmrLogger.fatal(this, "cercaTerreno - PermissionException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch(ParInputValNonCorrettoException pe)
		{
			SolmrLogger.fatal(this, "cercaTerreno - ParInputValNonCorrettoException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch(AutorizzMancanteEnteException ae)
		{
			SolmrLogger.fatal(this, "cercaTerreno - AutorizzMancanteEnteException: "+ae.getMessage());
			throw new SolmrException(ae.getMessage());
		}
		catch(OutputException oe)
		{
			SolmrLogger.fatal(this, "cercaTerreno - OutputException: "+oe.getMessage());
			throw new SolmrException(oe.getMessage());
		}
		catch(ParInputObblMancantiException pe)
		{
			SolmrLogger.fatal(this, "cercaTerreno - ParInputObblMancantiException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "cercaTerreno - Exception: "+e.getMessage());
			throw new SolmrException(e.getMessage());
		}
	}
	
	/**
	 * Il servizio Ricerca dati di Fabbricato restituisce tutti i dati riguardanti un determinato fabbricato identificato dai parametri di input.
	 * @param codIstatComune codice Istat del Comune 
	 * @param codBelfioreComune codice Belfiore del Comune
	 * @param sezione sezione del comune
	 * @param foglio unità territoriale
	 * @param numero unità territoriale
	 * @param subalterno unità territoriale
	 * @param progressivo numero indicante lo stadio dell’immobile
	 * @return
	 * @throws SolmrException
	 */
	public DettaglioFabbricato cercaFabbricato(String codIstatComune,
											   String codBelfioreComune,
											   String sezione,
											   String foglio,
											   String numero,
											   String subalterno,
											   String progressivo)
	throws SolmrException
	{
		try
		{
			return getSigmaterClient().cercaFabbricato(codIstatComune, codBelfioreComune, sezione, AnagUtils.leftFill(foglio, '0', 4), AnagUtils.leftFill(numero, '0', 5), AnagUtils.leftFill(subalterno, '0', 4), progressivo);
		}
		catch(PermissionException pe)
		{
			SolmrLogger.fatal(this, "cercaFabbricato - PermissionException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch(ParInputValNonCorrettoException pe)
		{
			SolmrLogger.fatal(this, "cercaFabbricato - ParInputValNonCorrettoException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch(AutorizzMancanteEnteException ae)
		{
			SolmrLogger.fatal(this, "cercaFabbricato - AutorizzMancanteEnteException: "+ae.getMessage());
			throw new SolmrException(ae.getMessage());
		}
		catch(OutputException oe)
		{
			SolmrLogger.fatal(this, "cercaFabbricato - OutputException: "+oe.getMessage());
			throw new SolmrException(oe.getMessage());
		}
		catch(ParInputObblMancantiException pe)
		{
			SolmrLogger.fatal(this, "cercaFabbricato - ParInputObblMancantiException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch (UnrecoverableException u)
		{
			//ArrayIndexOutOfBoundsException
			if (u!=null && u.getNestedExcClassName().endsWith("ArrayIndexOutOfBoundsException"))
			  return null;
			SolmrLogger.fatal(this, "cercaFabbricato - UnrecoverableException: "+u.getMessage());
			throw new SolmrException(u.getMessage());
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "cercaFabbricato - Exception: "+e.getMessage());
			throw new SolmrException(e.getMessage());
		}
	}
	
	/**
	 * Il servizio Ricerca dati di Titolarità per Oggetto Catastale restituisce tutti i dati delle titolarità riguardanti 
	 * un determinato immobile di un certo tipo (Fabbricato o Terreno) identificato dai parametri di input.
	 * Il servizio restituisce tutti i dati, attuali e storici, riguardanti le titolarità e gli oggetti ad esse collegati.
	 * @param codIstatComune codice Istat del Comune
	 * @param codBelfioreComune codice Belfiore del Comune
	 * @param sezione sezione del comune
	 * @param idImmobile Identificativo dell’immobile cui si riferisce la titolarità. Consente, assieme al codice del comune 
	 * e alla sezione, di individuare l’immobile nella tabella dei terreni o delle UIU, a seconda del tipo di immobile.
	 * @param tipoImmobile Specifica il tipo dell’immobile: terreno (T) oppure unità immobiliare urbana (F).
	 * @param dataDa limite inferiore dell’intervallo per il filtro di ricerca per data
	 * @param dataA limite superiore dell’intervallo per il filtro di ricerca per data
	 * @return
	 * @throws SolmrException
	 */
	public Titolarita[] cercaTitolaritaOggettoCatastale(String codIstatComune,
			   											 String codBelfioreComune,
			   											 String sezione,
			   											 String idImmobile,
			   											 String tipoImmobile,
			   											 String dataDa,
			   											 String dataA)
	throws SolmrException
	{
		try
		{
			return getSigmaterClient().cercaTitolaritaOggettoCatastale(codIstatComune, codBelfioreComune, sezione, idImmobile, tipoImmobile, dataDa, dataA);
		}
		catch(PermissionException pe)
		{
			SolmrLogger.fatal(this, "cercaTitolaritaOggettoCatastale - PermissionException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch(ParInputValNonCorrettoException pe)
		{
			SolmrLogger.fatal(this, "cercaTitolaritaOggettoCatastale - ParInputValNonCorrettoException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch(AutorizzMancanteEnteException ae)
		{
			SolmrLogger.fatal(this, "cercaTitolaritaOggettoCatastale - AutorizzMancanteEnteException: "+ae.getMessage());
			throw new SolmrException(ae.getMessage());
		}
		catch(OutputException oe)
		{
			SolmrLogger.fatal(this, "cercaTitolaritaOggettoCatastale - OutputException: "+oe.getMessage());
			throw new SolmrException(oe.getMessage());
		}
		catch(ParInputObblMancantiException pe)
		{
			SolmrLogger.fatal(this, "cercaTitolaritaOggettoCatastale - ParInputObblMancantiException: "+pe.getMessage());
			throw new SolmrException(pe.getMessage());
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "cercaTitolaritaOggettoCatastale - Exception: "+e.getMessage());
			throw new SolmrException(e.getMessage());
		}
	}
	
	
	public SoggettoFisico[] cercaSoggettoFisico(String codIstatComune, String codBelfioreComune,
        String nome, String cognome, String codFiscale, String ricercaEsatta)
  throws SolmrException
  {
    try
    {
      return getSigmaterClient().cercaSoggettoFisico(codIstatComune, codBelfioreComune, nome,
              cognome, codFiscale, ricercaEsatta);
    }
    catch(PermissionException pe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoFisico - PermissionException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch(ParInputValNonCorrettoException pe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoFisico - ParInputValNonCorrettoException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch(AutorizzMancanteEnteException ae)
    {
      SolmrLogger.fatal(this, "cercaSoggettoFisico - AutorizzMancanteEnteException: "+ae.getMessage());
      throw new SolmrException(ae.getMessage());
    }
    catch(OutputException oe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoFisico - OutputException: "+oe.getMessage());
      throw new SolmrException(oe.getMessage());
    }
    catch(ParInputObblMancantiException pe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoFisico - ParInputObblMancantiException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "cercaSoggettoFisico - Exception: "+e.getMessage());
      throw new SolmrException(e.getMessage());
    }
  }
	
	
	public SoggettoGiuridico[] cercaSoggettoGiuridico(String codIstatComune, String codBelfioreComune,
      String partitaIva, String denominazione, String ricercaEsatta)
  throws SolmrException
  {
    try
    {
      return getSigmaterClient().cercaSoggettoGiuridico(codIstatComune, codBelfioreComune, partitaIva,
              denominazione, ricercaEsatta);
    }
    catch(PermissionException pe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoGiuridico - PermissionException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch(ParInputValNonCorrettoException pe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoGiuridico - ParInputValNonCorrettoException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch(AutorizzMancanteEnteException ae)
    {
      SolmrLogger.fatal(this, "cercaSoggettoGiuridico - AutorizzMancanteEnteException: "+ae.getMessage());
      throw new SolmrException(ae.getMessage());
    }
    catch(OutputException oe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoGiuridico - OutputException: "+oe.getMessage());
      throw new SolmrException(oe.getMessage());
    }
    catch(ParInputObblMancantiException pe)
    {
      SolmrLogger.fatal(this, "cercaSoggettoGiuridico - ParInputObblMancantiException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "cercaSoggettoGiuridico - Exception: "+e.getMessage());
      throw new SolmrException(e.getMessage());
    }
  }
	
	
	public Titolarita[] cercaTitolaritaSoggettoCatastale(
      String codIstatComune, String codBelfioreComune, String sezione, String idSoggetto, 
      String tipoSoggetto, String dataDa, String dataA)
  throws SolmrException
  {
    try
    {
      return getSigmaterClient().cercaTitolaritaSoggettoCatastale(codIstatComune, 
          codBelfioreComune, sezione, idSoggetto, tipoSoggetto, dataDa, dataA);
    }
    catch(PermissionException pe)
    {
      SolmrLogger.fatal(this, "cercaTitolaritaSoggettoCatastale - PermissionException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch(ParInputValNonCorrettoException pe)
    {
      SolmrLogger.fatal(this, "cercaTitolaritaSoggettoCatastale - ParInputValNonCorrettoException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch(AutorizzMancanteEnteException ae)
    {
      SolmrLogger.fatal(this, "cercaTitolaritaSoggettoCatastale - AutorizzMancanteEnteException: "+ae.getMessage());
      throw new SolmrException(ae.getMessage());
    }
    catch(OutputException oe)
    {
      SolmrLogger.fatal(this, "cercaTitolaritaSoggettoCatastale - OutputException: "+oe.getMessage());
      throw new SolmrException(oe.getMessage());
    }
    catch(ParInputObblMancantiException pe)
    {
      SolmrLogger.fatal(this, "cercaTitolaritaSoggettoCatastale - ParInputObblMancantiException: "+pe.getMessage());
      throw new SolmrException(pe.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "cercaTitolaritaSoggettoCatastale - Exception: "+e.getMessage());
      throw new SolmrException(e.getMessage());
    }
  }
	
	


}

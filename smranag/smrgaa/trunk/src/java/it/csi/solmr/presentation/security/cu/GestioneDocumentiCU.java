package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.services.DelegaAnagrafeVO;
import it.csi.solmr.dto.anag.services.MandatoVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneDocumentiCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3010563459591964479L;

	public GestioneDocumentiCU()
	{
		this.cuName="GESTIONE_DOCUMENTI";
		this.isCUForReadWrite=true;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	public String writeMenu(Htmpl htmpl, HttpServletRequest request) {

		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
		return null;
	}

	/**
	 * Esegue i controlli sulla competenza di dato per l'utente in questione.
	 *
	 */
	public String hasCompetenzaDato(HttpServletRequest request,
			HttpServletResponse response,
			RuoloUtenza ruoloUtenza,
			AnagFacadeClient anagFacadeClient)
	{
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}
	
	/**
   * Valida l'accesso di un CAA ad una azienda
   *
   * @param ruoloUtenza RuoloUtenza
   * @param anagFacadeClient AnagFacadeClient
   * @param anagAziendaVO AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateCAA(RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient, AnagAziendaVO anagAziendaVO) {
    SolmrLogger.debug(this, "Sono nel metodo validateCAA in Autorizzazione\n");
    try {
      SolmrLogger.debug(this, "Valore del parametro isCUForReadWrite nel metodo validateCAA in Autorizzazione:"+this.isCUForReadWrite+"\n");
      if(this.isCUForReadWrite) {
        SolmrLogger.debug(this, "Valore del parametro data fine validita nel metodo validateCAA in Autorizzazione:"+anagAziendaVO.getDataFineVal()+"\n");
        if(anagAziendaVO.getDataFineVal() != null) {
          return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA;
        }
      }
      Long idAzienda = anagAziendaVO.getIdAzienda();
      SolmrLogger.debug(this, "Valore del parametro id azienda nel metodo validateCAA in Autorizzazione:"+idAzienda+"\n");
      String codiceIntermediario = ruoloUtenza.getCodiceEnte();
      SolmrLogger.debug(this, "Valore del parametro codice intermediario nel metodo validateCAA in Autorizzazione:"+codiceIntermediario+"\n");
      MandatoVO mandatoVO = anagFacadeClient.serviceGetMandato(idAzienda,codiceIntermediario);
      SolmrLogger.debug(this, "Value of parameter [MANDATO_VO] in method validateCAA in Autorizzazione.java: "+mandatoVO+"\n");
      // Mi estraggo la delega
      DelegaAnagrafeVO delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
      SolmrLogger.debug(this, "Value of parameter [DELEGA_ANAGRAFE_VO] in method validateCAA in Autorizzazione.java: "+delegaAnagrafeVO+"\n");
      if(delegaAnagrafeVO != null) {
        // Se esiste una delega devo controllare che il codice dell'ente
        // intermediario a cui appartiene questa delega sia lo stesso dell'ente
        // intemediario dell'utente connesso o che sia stato valorizzato a "S" il
        // FLAG_FIGLIO che è valorizzato in questo modo SE E SOLO SE l'ente che ha
        // la delega è un figlio dell'ente il cui codice fiscale è stato passato
        // al metodo serviceGetMandato
        SolmrLogger.debug(this, "Value of parameter [CODICE_INTERMEDIARIO] in method validateCAA in Autorizzazione.java: "+codiceIntermediario+"\n");
        SolmrLogger.debug(this, "Value of parameter [CODICE_FISCALE_INTERMEDIARIO_DELEGA] in method validateCAA in Autorizzazione.java: "+delegaAnagrafeVO.getCodiceFiscIntermediario()+"\n");
        SolmrLogger.debug(this, "Value of parameter [FLAG_FIGLIO_DELEGA] in method validateCAA in Autorizzazione.java: "+delegaAnagrafeVO.getFlagFiglio()+"\n");
        if(!(codiceIntermediario.equalsIgnoreCase(delegaAnagrafeVO.getCodiceFiscIntermediario()) || SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO.getFlagFiglio()))) {
          return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
        }
      }
      else {
        // Se non c'è la delega ==> Errore
        return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
      }
      // Se è arrivato qui, è abilitato!
      return null;
    }
    catch (SolmrException ex) {
      return ex.getMessage();
    }
    catch (Exception ex) {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }
  
  /**
   * Valida l'accesso di un regionale sulle aziende prive di mandato
   *
   * @param anagAziendaVO AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateRegionale(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA;
      }
    }
    else return null;
    if (!anagAziendaVO.isPossiedeDelegaAttiva())
      return null;
    else return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
  }
  
  /**
   * Valida l'accesso di un provinciale sulle aziende prive di mandato e della
   * propria provincia di competenza
   *
   * @param ruoloUtenza RuoloUtenza
   * @param anagAziendaVO AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateProvinciale(RuoloUtenza ruoloUtenza,
      AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA;
      }
    }
    else return null;
    try
    {
      if (!ruoloUtenza.getIstatProvincia().equals(anagAziendaVO.getProvCompetenza()))
        return AnagErrors.ERRORE_AUT_NO_PROV_COMPETENZA;
      if (anagAziendaVO.isPossiedeDelegaAttiva())
        return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
      return null;
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }
  
  /**
   * Valida l'accesso di un OPR sulle aziende prive di mandato
   *
   * @param anagAziendaVO AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateOPR(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA;
      }
    }
    else return null;
    if (!anagAziendaVO.isPossiedeDelegaAttiva())
      return null;
    else return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
  }
  
  /**
   * Valida l'accesso di un OPR Gestore (OPR con delega) ad un'azienda
   *
   * @param ruoloUtenza RuoloUtenza
   * @param anagFacadeClient AnagFacadeClient
   * @param anagAziendaVO AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateORPGestore(RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient, AnagAziendaVO anagAziendaVO) {
    try {
      if(this.isCUForReadWrite) {
        if(anagAziendaVO.getDataFineVal() != null) {
          return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA;
        }
        Long idAzienda = anagAziendaVO.getIdAzienda();
        String codiceIntermediario = ruoloUtenza.getCodiceEnte();
        MandatoVO mandatoVO = anagFacadeClient.serviceGetMandato(idAzienda, codiceIntermediario);
        // Mi estraggo la delega
        DelegaAnagrafeVO delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
        if(delegaAnagrafeVO != null) {
          // Se esiste una delega devo controllare che il codice dell'ente
          // intermediario a cui appartiene questa delega sia lo stesso dell'ente
          // intemediario dell'utente connesso o che sia stato valorizzato a "S" il
          // FLAG_FIGLIO che è valorizzato in questo modo SE E SOLO SE l'ente che ha
          // la delega è un figlio dell'ente il cui codice fiscale è stato passato
          // al metodo serviceGetMandato
          if(!codiceIntermediario.equalsIgnoreCase(delegaAnagrafeVO.getCodiceFiscIntermediario()) && !SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO.getFlagFiglio())) {
            return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
          }
          else {
            return null;
          }
        }
        else {
          // Se non c'è la delega ==> Errore
          return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
        }
      }
      else {
        return null;
      }
    }
    catch (SolmrException ex) {
      return ex.getMessage();
    }
    catch (Exception ex) {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }
  
  /**
   * Valida l'accesso del profilo relativo alla comunità montana
   *
   * @param anagAziendaVO AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateComunitaMontana(AnagAziendaVO anagAziendaVO) {
    if(this.isCUForReadWrite) {
      if(anagAziendaVO.getDataFineVal() != null) {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA;
      }
    }
    return null;
  }

}

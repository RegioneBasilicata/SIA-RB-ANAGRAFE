package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneAziendaCU extends AutorizzazioneCUStandard
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5393726738267665895L;

	public GestioneAziendaCU()
	{
		this.cuName="GESTIONE_AZIENDA";
		this.isCUForReadWrite=true;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	
  @SuppressWarnings("rawtypes")
  public String writeMenu(Htmpl htmpl, HttpServletRequest request) 
  {
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);

		Object objForMenu = null;
		// Dettaglio o elenco soggetti collegati
		if(request.getAttribute("personaVO") != null || request.getSession().getAttribute("v_soggetti") != null) {
			objForMenu = request.getAttribute("personaVO");
			if(objForMenu == null) {
				objForMenu = (Vector)request.getSession().getAttribute("v_soggetti");
			}
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkSoggettiCollegati", objForMenu, null, request);
		}
		// Dettaglio o elenco unità produttive
		else if(request.getSession().getAttribute("uteVO") != null || request.getSession().getAttribute("v_ute") != null) {
			objForMenu = request.getSession().getAttribute("uteVO");
			if(objForMenu == null) {
				objForMenu = (Vector)request.getSession().getAttribute("v_ute");
			}
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkUnitaProduttive", objForMenu, null, request);
		}
		// Dettaglio dell'azienda
		else {
			objForMenu = anagAziendaVO;
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null, objForMenu, null, request);
		}

		return null;
	}

	/**
	 * Esegue i controlli sulla competenza di dato per l'utente in questione.
	 *
	 */
	public String hasCompetenzaDato(HttpServletRequest request, HttpServletResponse response, 
	    RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient) 
	{
		String iridePageNameForCU = (String)request.getAttribute("iridePageNameForCU");
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}		
		
		String errorMassage = validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
		
		//solo se l'azienda nn è cessata!!!
		if(errorMassage != null && !errorMassage.equalsIgnoreCase(AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA))
		{
  		boolean controlloValidate = true;
  		/*
  		 * Per TOBECONFIG : commentato parte del controllo per far si che anche gli utenti
  		 * con ruolo regionale possano modificare sempre i dati dell'aziedna
  		 */
      if(ruoloUtenza.isReadWrite() && (  //ruoloUtenza.isUtenteRegionale() 
          //|| 
          ruoloUtenza.isUtenteOPRGestore() || ruoloUtenza.isUtenteProvinciale()))
      {
        if (ruoloUtenza.isUtenteProvinciale())
        {
          SolmrLogger.debug(this, "-- isUtenteProvinciale");	
          //l'utente può modificare l'azienda solo se nella sua provincia
          if (ruoloUtenza.getIstatProvincia().equals(anagAziendaVO.getProvCompetenza()))
          {
        	SolmrLogger.debug(this, "-- l'utente può modificare l'azienda solo se nella sua provincia : setto controlloValidate = false");  
            controlloValidate = false;
          }
        }
        else
        {
          SolmrLogger.debug(this, "-- non e' UtenteProvinciale");	
          SolmrLogger.debug(this, "-- setto controlloValidate = false");
          controlloValidate = false;
        }       
      }
      
      //con questo flag permetto la modifca per alcuni ruoli solo di alcuni campi anagrafici...
      if(!controlloValidate)
      {
    	SolmrLogger.debug(this, "-- setto modificaCampiAnagrafici a true");
        request.getSession().setAttribute("modificaCampiAnagrafici", "true");
        errorMassage = null;
      }
      else
        request.getSession().removeAttribute("modificaCampiAnagrafici");
		}
		
		
		
		if(errorMassage != null) 
		{
			return errorMassage;
		}
		else 
		{
			if(iridePageNameForCU.startsWith("anagrafica") && anagAziendaVO.getIdFormaGiuridica() == null 
			    && anagAziendaVO.getTipoFormaGiuridica().getCode() == null) 
			{
				return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO_AZIENDA_NO_FORMA_GIURIDICA;
			}
			//Controllo eliminato su richiesta di Teresa in data 30/11/2011
			/*else if(iridePageNameForCU.startsWith("anagrafica") 
			    && (anagAziendaVO.getIdFormaGiuridica() != null 
			    || anagAziendaVO.getTipoFormaGiuridica().getCode() != null)) 
			{
				if((anagAziendaVO.getIdFormaGiuridica() != null 
				    && anagAziendaVO.getIdFormaGiuridica().compareTo((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_INDIVIDUALE")) == 0) 
				    || (anagAziendaVO.getTipoFormaGiuridica().getCode() != null 
				        && anagAziendaVO.getTipoFormaGiuridica().getCode().compareTo(Integer.decode(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_INDIVIDUALE")).toString())) == 0)) 
				{
					if(!iridePageNameForCU.equalsIgnoreCase("anagraficaIndividualePassaggioCtrl.jsp") &&
					   !iridePageNameForCU.equalsIgnoreCase("anagraficaIndividualePassaggioNuovoTitolareCtrl.jsp") &&
							!iridePageNameForCU.equalsIgnoreCase("anagraficaIndividualePassaggioSedeCtrl.jsp") &&
							!iridePageNameForCU.equalsIgnoreCase("anagraficaSedeModCtrl.jsp") && 
							!iridePageNameForCU.equalsIgnoreCase("anagraficaIndicatoriModCtrl.jsp")) 
					
					{
						return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO_AZIENDA_FOR_FORMA_GIURIDICA;
					}
				}
				else 
				{
					if(!iridePageNameForCU.equalsIgnoreCase("anagraficaSocieta_cambiaRappresentanteCtrl.jsp")
					    && !iridePageNameForCU.equalsIgnoreCase("anagraficaSedeModCtrl.jsp")
              && !iridePageNameForCU.equalsIgnoreCase("anagraficaIndicatoriModCtrl.jsp")) 
					{
						return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO_AZIENDA_FOR_FORMA_GIURIDICA;
					}
				}
			}*/
		}
		return null;
	}
}

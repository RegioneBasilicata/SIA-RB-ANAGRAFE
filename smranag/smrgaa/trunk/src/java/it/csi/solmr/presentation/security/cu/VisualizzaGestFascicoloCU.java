package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaGestFascicoloCU extends AutorizzazioneCUStandard
{
	


	/**
   * 
   */
  private static final long serialVersionUID = 548778795280084343L;

  public VisualizzaGestFascicoloCU()
	{
		this.cuName="VISUALIZZA_GEST_FASCICOLO";
		this.isCUForReadWrite=false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
  @SuppressWarnings("rawtypes")
  public String writeMenu(Htmpl htmpl, HttpServletRequest request) {
		SolmrLogger.debug(this, "Invocating method writeMenu in VisualizzaGestFascicoloCU\n");
		AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
		SolmrException sex = null;

		if(anagAziendaVO != null) {
			try {
				anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
			}
			catch(SolmrException se) {
				sex = se;
			}
		}

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeBarraStato(sex, htmpl);

		SolmrLogger.debug(this, "Value of parameter[V_SOGGETTI] method writeMenu in VisualizzaGestFascicoloCU: "+request.getSession().getAttribute("v_soggetti")+"\n");
		SolmrLogger.debug(this, "Value of parameter[V_UTE] method writeMenu in VisualizzaGestFascicoloCU: "+request.getSession().getAttribute("v_ute")+"\n");
		SolmrLogger.debug(this, "Value of parameter[PERSONA_VO] method writeMenu in VisualizzaGestFascicoloCU: "+request.getAttribute("personaVO")+"\n");
		SolmrLogger.debug(this, "Value of parameter[UTE_VO] method writeMenu in VisualizzaGestFascicoloCU: "+request.getSession().getAttribute("uteVO")+"\n");

		Object objForMenu = null;
		// Elenco soggetti collegati
		if(request.getSession().getAttribute("v_soggetti") != null) {
			objForMenu = (Vector)request.getSession().getAttribute("v_soggetti");
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkSoggettiCollegati", objForMenu, null, request);
		}
		// Elenco unità produttive
		else if(request.getSession().getAttribute("v_ute") != null) {
			objForMenu = (Vector)request.getSession().getAttribute("v_ute");
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkUnitaProduttive", objForMenu, null, request);
		}
		// Dettaglio soggetti collegati
		else if(request.getAttribute("personaVO") != null) {
			objForMenu = request.getAttribute("personaVO");
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkSoggettiCollegati", objForMenu, "getDataFineRuolo", request);
		}
		// Dettaglio unità produttive
		else if(request.getSession().getAttribute("uteVO") != null) {
			objForMenu = request.getSession().getAttribute("uteVO");
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkUnitaProduttive", objForMenu, "getDataFineAttivita", request);
		}
		// Dettaglio dell'azienda
		else {
			if(anagAziendaVO == null) {
				anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("insAnagVO");
			}
			objForMenu = anagAziendaVO;
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null, objForMenu, null, request);
		}
		
		// Blocco modifica gestore fascicolo
		if (iride2AbilitazioniVO.isUtenteAbilitato("MOD_GEST_FASCICOLO"))
      htmpl.newBlock("blkModificaGestoreFascicolo");
		/*if(ruoloUtenza.isReadWrite() && ruoloUtenza.isUtenteOPRGestore()) {
			htmpl.newBlock("blkModificaGestoreFascicolo");
		}*/
		SolmrLogger.debug(this, "Invocated method writeMenu in VisualizzaGestFascicoloCU\n");
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
		
		return validate(request, response, ruoloUtenza, anagFacadeClient, anagAziendaVO);
	}
	
	public String validate(HttpServletRequest request,
      HttpServletResponse response,
      RuoloUtenza ruoloUtenza,
      AnagFacadeClient anagFacadeClient,
      AnagAziendaVO anagAziendaVO)
	{
    if(anagAziendaVO == null) 
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}
	 
}

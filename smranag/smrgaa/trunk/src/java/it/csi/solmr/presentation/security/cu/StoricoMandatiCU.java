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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StoricoMandatiCU extends AutorizzazioneCUStandard
{
	


	/**
   * 
   */
  private static final long serialVersionUID = -1538316896579543930L;

  public StoricoMandatiCU()
	{
		this.cuName="STORICO_MANDATI";
		this.isCUForReadWrite=false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
  public String writeMenu(Htmpl htmpl, HttpServletRequest request) {
		SolmrLogger.debug(this, "Invocating method writeMenu in StoricoMandatiCU\n");
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

		SolmrLogger.debug(this, "Value of parameter[V_SOGGETTI] method writeMenu in StoricoMandatiCU: "+request.getSession().getAttribute("v_soggetti")+"\n");
		SolmrLogger.debug(this, "Value of parameter[V_UTE] method writeMenu in StoricoMandatiCU: "+request.getSession().getAttribute("v_ute")+"\n");
		SolmrLogger.debug(this, "Value of parameter[PERSONA_VO] method writeMenu in StoricoMandatiCU: "+request.getAttribute("personaVO")+"\n");
		SolmrLogger.debug(this, "Value of parameter[UTE_VO] method writeMenu in StoricoMandatiCU: "+request.getSession().getAttribute("uteVO")+"\n");

		Object objForMenu = null;
		
		if(anagAziendaVO == null) {
			anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("insAnagVO");
		}
		objForMenu = anagAziendaVO;
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null, objForMenu, null, request);
		
		
		// Blocco modifica gestore fascicolo
		if (iride2AbilitazioniVO.isUtenteAbilitato("MOD_GEST_FASCICOLO"))
      htmpl.newBlock("blkModificaGestoreFascicolo");
		
		SolmrLogger.debug(this, "Invocated method writeMenu in StoricoMandatiCU\n");
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

package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaContiCorrentiCU extends AutorizzazioneCUStandard
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5885660442928606102L;

	public VisualizzaContiCorrentiCU()
	{
		this.cuName="VISUALIZZA_CONTI_CORRENTI";
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

		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		SolmrException sex = (SolmrException)request.getAttribute("statoAzienda");
		Object objForMenu = (Vector)request.getAttribute("conti");
		if(objForMenu == null) {
			objForMenu = (ContoCorrenteVO)request.getAttribute("contoCorrenteVO");
		}

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeBarraStato(sex, htmpl);

		if(objForMenu instanceof Vector) {
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkContiCorrenti", objForMenu, null, request);
		}
		else {
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkContiCorrenti", objForMenu, "getDataEstinzione", request);
		}
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

}

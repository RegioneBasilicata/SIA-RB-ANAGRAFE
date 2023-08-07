package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaAllevamentiCU extends AutorizzazioneCUStandard
{
	/**
	 *
	 */
	private static final long serialVersionUID = -8165134621896508234L;

	public VisualizzaAllevamentiCU()
	{
		this.cuName="VISUALIZZA_ALLEVAMENTI";
		this.isCUForReadWrite=false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	public String writeMenu(Htmpl htmpl, HttpServletRequest request) 
	{

		SolmrException se = (SolmrException)request.getAttribute("statoAzienda");
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		AllevamentoAnagVO[] elenco = (AllevamentoAnagVO[])request.getAttribute("elencoAllevamenti");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		AllevamentoAnagVO allevamentoAnagVO = (AllevamentoAnagVO)request.getAttribute("allevamento");
		Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
		
		Object objForMenu = null;
		if(elenco != null && elenco.length > 0) {
			objForMenu = elenco;
		}
		else {
			objForMenu = allevamentoAnagVO;
		}

		AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
		Vector<UteVO> elencoUte = null;

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeBarraStato(se, htmpl);

		if(anagAziendaVO != null) {
			try {
				elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
			}
			catch(SolmrException sex) {
			}
		}

		//********************
		// Gestione specifica delle voci di menù relative agli allevamenti
		if(objForMenu instanceof AllevamentoAnagVO[]) {
			// Gestione delle voci di menù di anagrafe
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
			if(elenco != null && elenco.length > 0) 
			{
				htmpl.newBlock("blkAllevamenti.blkDettaglio");
				htmpl.newBlock("blkAllevamenti.blkControlli");
				
				if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_BDN"))
        {
				  htmpl.newBlock("blkAllevamenti.blkRegistroStallaBDN");				  
        }
		    
				if(elencoUte != null && elencoUte.size() > 0 && anagAziendaVO != null && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) {
					if(anagAziendaVO != null && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) {
						htmpl.newBlock("blkAllevamenti.blkElimina");
						htmpl.newBlock("blkAllevamenti.blkModifica");
						htmpl.newBlock("blkAllevamenti.blkCessazione");
					}
				}
				if(elencoUte != null && elencoUte.size() > 0 && anagAziendaVO != null && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) {
					htmpl.newBlock("blkAllevamenti.blkInserisci");
				}
			}
		}
		else 
		{
			super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkAllevamenti", objForMenu, "getDataFineDate", request);
		}
		
		
		if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_BDN"))
    {
      htmpl.newBlock("blkAllevamenti.blkVisuraBDN");          
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

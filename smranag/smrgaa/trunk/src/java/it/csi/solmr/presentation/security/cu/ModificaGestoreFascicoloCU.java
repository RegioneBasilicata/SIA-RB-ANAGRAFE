package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModificaGestoreFascicoloCU extends AutorizzazioneCUStandard {
	
	private static final long serialVersionUID = 7550518762261222632L;

	public ModificaGestoreFascicoloCU() {
		this.cuName = "MOD_GEST_FASCICOLO";
		this.isCUForReadWrite = true;
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
	  RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		
		super.writeBanner(htmpl, request);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null, anagAziendaVO, null, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		
    Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    
    if (iride2AbilitazioniVO.isUtenteAbilitato("GESTIONE_MANDATO"))
      htmpl.newBlock("blkRegistrazioneMandato");
		
		/*if(ruoloUtenza != null && ruoloUtenza.isValid()) {
			if(ruoloUtenza.isReadWrite()) {
				if((ruoloUtenza.isUtenteIntermediario() && SolmrConstants.TIPO_INTERMEDIARIO_CAA.equalsIgnoreCase(ruoloUtenza.getTipoIntermediario())) || ruoloUtenza.isUtenteOPRGestore()) {
					htmpl.newBlock("blkRegistrazioneMandato");
				}
			}
		}*/
		return null;
	}

	/**
	 * Esegue i controlli sulla competenza di dato per l'utente in questione.
	 *
	 */
	public String hasCompetenzaDato(HttpServletRequest request, HttpServletResponse response,
	    RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient) 
	{
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		else if(!ruoloUtenza.isUtenteOPRGestore()) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": !ruoloUtenza.isUtenteOPRGestore()");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		else {
			return null;
		}
	}
}

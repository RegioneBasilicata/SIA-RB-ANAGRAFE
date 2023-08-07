package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.util.PaginazioneUtils;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneVariazioniAziendaCU extends Autorizzazione
{
	/**
   * serialVersionUID
   */
  private static final long serialVersionUID = -6030046650244501745L;


	public GestioneVariazioniAziendaCU()
	{
		this.cuName="GESTIONE_VARIAZIONI_AZIENDA";
		this.isCUForReadWrite=true;
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

		super.writeBanner(htmpl, request);
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		super.writeGenericMenu(ruoloUtenza, null, htmpl, request);
		
    htmpl.newBlock("blkVariazione");
    PaginazioneUtils pager = (PaginazioneUtils) request.getAttribute("pager");
    //Se ci sono record permetto all'utente di effettuare la presa visione
    if (pager!=null && pager.getRighe()!=null)
      htmpl.newBlock("blkVariazione.blkPresaVisione");
		
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
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.
		getSession().getAttribute("anagAziendaVO");
		if (anagAziendaVO==null)
			return null;
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}

}

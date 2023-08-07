package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NuovaAziendaCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5041637732375196810L;

	public NuovaAziendaCU()
	{
		this.cuName="NUOVA_AZIENDA";
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
	  RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		super.writeBanner(htmpl, request);
		super.writeGenericMenu(ruoloUtenza, null, htmpl, request);
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
		return null;
	}

}

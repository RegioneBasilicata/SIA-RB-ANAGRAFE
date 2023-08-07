package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaBioCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5286821529378447780L;

	public VisualizzaBioCU()
	{
		this.cuName="VISUALIZZA_BIO";
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

	  RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
		if(ruoloUtenza.isReadWrite()) {
			if(ruoloUtenza.isUtenteProvinciale() || ruoloUtenza.isUtenteRegionale() ||  ruoloUtenza.isUtenteComunitaMontana()) {
				if(anagAziendaVO != null && anagAziendaVO.getDataCessazione() == null) {
					htmpl.newBlock("blkInsBiologico");
					htmpl.newBlock("blkModBiologico");
					htmpl.newBlock("blkCesBiologico");
				}
			}
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
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.
		getSession().getAttribute("anagAziendaVO");
		if (anagAziendaVO==null)
			return null;
		else
			return validateGenericUser(ruoloUtenza,anagAziendaVO, anagFacadeClient,request);
	}

}

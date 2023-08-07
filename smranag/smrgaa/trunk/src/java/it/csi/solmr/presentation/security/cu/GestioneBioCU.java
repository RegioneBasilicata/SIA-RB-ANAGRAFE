package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneBioCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4783694397592185497L;

	public GestioneBioCU()
	{
		this.cuName="GESTIONE_BIO";
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
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");

		super.writeBanner(htmpl, request);
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

	public String hasCompetenzaDato(HttpServletRequest request,
			HttpServletResponse response,
			RuoloUtenza ruoloUtenza,
			AnagFacadeClient anagFacadeClient) {
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		// Se l'azienda è null significa che c'è stato un tentativo di forzare l'accesso
		if(anagAziendaVO == null) {
			return AnagErrors.ERRORE_ABILITAZIONE_NO_ABILITAZIONE;
		}
		else {
			return null;
		}
	}


}

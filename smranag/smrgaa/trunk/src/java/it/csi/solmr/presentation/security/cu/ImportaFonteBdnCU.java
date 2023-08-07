package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImportaFonteBdnCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6953539005815229180L;

	public ImportaFonteBdnCU()
	{
		this.cuName="IMPORTA_FONTE_BDN";
		this.isCUForReadWrite=true;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	public String writeMenu(Htmpl htmpl, HttpServletRequest request) {
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		//Hashtable<?,?> elencoAllevamentiSian = (Hashtable<?,?>)request.getSession().getAttribute("elencoAllevamentiSian");

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
		/*if(elencoAllevamentiSian != null) {
			htmpl.newBlock("blkSiAllevamenti");
			if(super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) {
				if(elencoAllevamentiSian.size() > 0) {
					htmpl.newBlock("blkSiAllevamenti.blkAnagrafeZootecnicaButtons");
				}
			}
		}*/
		if(super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) 
    {
      request.setAttribute("modificaBdn", SolmrConstants.MODIFICA_BDN);
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
		else {
			// Ricerco nuovamente i dati dell'azienda perchè, per impedire l'accesso alla funzionalità, voglio essere sicuro di lavorare
			// sui dati dell'azienda corrispondenti al momento esatto dell'accesso dell'utente alla funzionalità.
			try {
				anagAziendaVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
				request.getSession().setAttribute("anagAziendaVO", anagAziendaVO);
				if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) {
					return AnagErrors.ERRORE_CUAA_SIAN_NON_VALORIZZATO;
				}
			}
			catch(SolmrException se) {
				return AnagErrors.ERRORE_DI_SISTEMA_AZIENDA_NON_REPERITA;
			}
		}
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}

}

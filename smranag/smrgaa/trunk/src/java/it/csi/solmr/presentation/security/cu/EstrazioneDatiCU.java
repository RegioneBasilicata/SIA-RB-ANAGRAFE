package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/*
 * 
 * Classe inserita per completezza ma non usata, poichè i report dinamici
 * non fanno uso dell'autorizzazione attraverso l'iride2Config.
 * Però è usato il macrocaso uso iride associato al ruolo per la visualizzazione
 * del blocco relativo.
 * 
 * 
 */

public class EstrazioneDatiCU extends Autorizzazione
{
	

	/**
   * 
   */
  private static final long serialVersionUID = 3243174450238247768L;

  public EstrazioneDatiCU()
	{
		this.cuName="ESTRAZIONE_DATI";
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

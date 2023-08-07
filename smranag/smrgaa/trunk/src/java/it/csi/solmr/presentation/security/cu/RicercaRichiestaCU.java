package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RicercaRichiestaCU extends Autorizzazione
{
	

	/**
   * 
   */
  private static final long serialVersionUID = -6793407999798649154L;

  public RicercaRichiestaCU()
	{
		this.cuName="RICERCA_RICHIESTA";
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
		
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
    
    if(Validator.isNotEmpty(aziendaNuovaVO))
    {
      htmpl.set("cuaaIntestazione", aziendaNuovaVO.getCuaa());
      String denominazioneIntestazione = "";
      if(aziendaNuovaVO.getCuaa().length() == 11)
      {
        denominazioneIntestazione = aziendaNuovaVO.getDenominazione();
      }
      else
      {
        denominazioneIntestazione = aziendaNuovaVO.getCognome()+" "+aziendaNuovaVO.getNome();
      }
      htmpl.set("denominazioneIntestazione", denominazioneIntestazione);
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
		return null;
	}

}

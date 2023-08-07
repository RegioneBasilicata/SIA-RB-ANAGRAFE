package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaSchedarioCU extends AutorizzazioneCUStandard
{
	

	/**
   * 
   */
  private static final long serialVersionUID = -5578573359567964950L;
  
  

  public VisualizzaSchedarioCU()
	{
		this.cuName="VISUALIZZA_SCHEDARIO";
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

		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null, anagAziendaVO, null, request);
		
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
		
		boolean hasProperties = false;
		
		
		if (ruoloUtenza.isReadWrite())
    {
      if (anagAziendaVO.getDataCessazione() == null
          && anagAziendaVO.getDataCessazioneStr() == null
          && anagAziendaVO.getDataFineVal() == null)
      {
        if (ruoloUtenza.isUtenteRegionale()
         //   || ruoloUtenza.isUtenteIntermediario()
            || ruoloUtenza.isUtenteOPRGestore())
        {
          hasProperties = true;
        }
        else if(ruoloUtenza.isUtenteProvinciale()
            && (ruoloUtenza.getCodiceEnte() != null))
        { //Il controllo sulla singola UV è fatto nel metodo elencoUnitaVitatePerCUAA!!!!!
          
          hasProperties = true;
        }
      }
    }
		
		if (hasProperties)
    {
      request.setAttribute("visualizzaCheck", "true");
    }
		return null;
	}

}

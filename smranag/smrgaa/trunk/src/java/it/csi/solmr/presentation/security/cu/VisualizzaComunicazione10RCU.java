package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaComunicazione10RCU extends AutorizzazioneCUStandard
{
	


	/**
   * 
   */
  private static final long serialVersionUID = -5427593563980240840L;

  public VisualizzaComunicazione10RCU()
	{
		this.cuName="VISUALIZZA_COMUNICAZIONE_10R";
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
		SolmrLogger.debug(this, "Invocating method writeMenu in VisualizzaComunicazione10RCU\n");
		AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		SolmrException sex = null;

		if(anagAziendaVO != null) {
			try {
				anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
			}
			catch(SolmrException se) {
				sex = se;
			}
		}

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeBarraStato(sex, htmpl);

		writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, "blkComunicazione10R", request);
		
		
		
		SolmrLogger.debug(this, "Invocated method writeMenu in VisualizzaComunicazione10RCU\n");
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
	
	/**
   * Metodo che si occupa di far visualizzare tutte le funzionalità specifiche
   * dei casi d'uso che presentano le funzionalità standard di modifica,
   * elimina, cessazione
   * 
   * @param ruoloUtenza
   * @param anagAziendaVO
   * @param htmpl
   * @param nomeBlocco
   * @param request
   */
  public void writeGenericMenu(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO, 
      Htmpl htmpl, String nomeBlocco, HttpServletRequest request) 
  {
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
    Iride2AbilitazioniVO iride2AbilitazioniVO 
      = (Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    // Verifico che il profilo ci sia e sia corretto
    if(ruoloUtenza != null && ruoloUtenza.isValid()) 
    {
      // Controllo che l'azienda ci sia e che l'utente non stia visualizzando un record storicizzato
      if(anagAziendaVO != null && !Validator.isNotEmpty(anagAziendaVO.getDataFineVal())) 
      {
        htmpl.newBlock(nomeBlocco + ".blkDettaglio");
        if(hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) 
        {
          if(iride2AbilitazioniVO.isUtenteAbilitato("GESTIONE_COMUNICAZIONE_10R"))
          {
            htmpl.newBlock(nomeBlocco + ".blkModifica");
            htmpl.newBlock(nomeBlocco + ".blkRicalcola");
          }
        }
           
      }
    }
  }
	 
}

package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaMotoriAgricoliCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7088597131442898160L;

	public VisualizzaMotoriAgricoliCU()
	{
		this.cuName="VISUALIZZA_MOTORI_AGRICOLI";
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
		SolmrException sex = (SolmrException)request.getAttribute("statoAzienda");
		Iride2AbilitazioniVO iride2AbilitazioniVO 
    = (Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeBarraStato(sex, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
		
		if (iride2AbilitazioniVO.isUtenteAbilitato("GESTISCI_MOTORI_AGRICOLI"))
		{
		  boolean trovato = false;
		  if (anagAziendaVO != null
          && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
      {
        htmpl.newBlock("blkMotoriAgricoli.blkInserisciMacchina");
        htmpl.newBlock("blkMotoriAgricoli.blkModificaMacchina");
        htmpl.newBlock("blkMotoriAgricoli.blkCaricaMacchina");
        
        trovato = true;
      }
		  
		  
		  if (ruoloUtenza.isReadWrite() && !trovato)
      {
        if (anagAziendaVO.getDataCessazione() == null
            && anagAziendaVO.getDataCessazioneStr() == null
            && anagAziendaVO.getDataFineVal() == null)
        {  
          if (ruoloUtenza.isUtenteRegionale()
              || ruoloUtenza.isUtenteProvinciale())
          {
            htmpl.newBlock("blkMotoriAgricoli.blkInserisciMacchina");
            htmpl.newBlock("blkMotoriAgricoli.blkModificaMacchina");
            htmpl.newBlock("blkMotoriAgricoli.blkCaricaMacchina");
          }
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
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}

}

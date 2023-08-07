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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RevocaDelegaCU extends AutorizzazioneCUStandard
{
	

	

  /**
   * 
   */
  private static final long serialVersionUID = -6593691271735260463L;

  public RevocaDelegaCU()
	{
		this.cuName="REVOCA_DELEGA";
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
		AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
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
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null, anagAziendaVO, null, request);
		
    Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    if (iride2AbilitazioniVO.isUtenteAbilitato("GESTIONE_MANDATO"))
      htmpl.newBlock("blkRegistrazioneMandato");
		
    if (iride2AbilitazioniVO.isUtenteAbilitato("MOD_GEST_FASCICOLO"))
      htmpl.newBlock("blkModificaGestoreFascicolo");
    
    if(iride2AbilitazioniVO.isUtenteAbilitato("REVOCA_DELEGA"))
    {
      htmpl.newBlock("blkRevocaMandato");
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
		String iridePageNameForCU = (String)request.getAttribute("iridePageNameForCU");
		if ("revocaDelegaCtrl.jsp".equals(iridePageNameForCU)
		    || "confermaRevocaDelegaCtrl.jsp".equals(iridePageNameForCU))
			return null;
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}
	
	
	protected String validateRegionale(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
    }
    else
      return null;
    
    if (anagAziendaVO.isPossiedeDelegaAttiva())
      return null;
    else
      return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
  }
	
	
	protected String validateOPR(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
    }
    else
      return null;
    if (anagAziendaVO.isPossiedeDelegaAttiva())
      return null;
    else
      return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
  }
	
	
	protected String validateProvinciale(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
    }
    else
      return null;
    try
    {
      if (!ruoloUtenza.getIstatProvincia().equals(anagAziendaVO.getProvCompetenza()))
        return AnagErrors.ERRORE_AUT_NO_PROV_COMPETENZA;
      if (anagAziendaVO.isPossiedeDelegaAttiva())
        return null;
      else
        return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }


}

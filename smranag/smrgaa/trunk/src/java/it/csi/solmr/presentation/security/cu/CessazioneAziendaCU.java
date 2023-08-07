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

public class CessazioneAziendaCU extends AutorizzazioneCUStandard
{

	/**
   * 
   */
  private static final long serialVersionUID = 6240418497663218933L;


  public CessazioneAziendaCU()
	{
		this.cuName="CESSAZIONE_AZIENDA";
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
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");


		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);

		Object objForMenu = anagAziendaVO;
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null, objForMenu, null, request);

		return null;
	}

	/**
	 * Esegue i controlli sulla competenza di dato per l'utente in questione.
	 *
	 */
	public String hasCompetenzaDato(HttpServletRequest request, HttpServletResponse response, 
	  RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient) 
	{
		String iridePageNameForCU = (String)request.getAttribute("iridePageNameForCU");
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		String errorMassage = validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
		if(errorMassage != null) 
		{
			return errorMassage;
		}
		else 
		{
			if(iridePageNameForCU.startsWith("anagrafica") && anagAziendaVO.getIdFormaGiuridica() == null && anagAziendaVO.getTipoFormaGiuridica().getCode() == null) 
			{
				return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO_AZIENDA_NO_FORMA_GIURIDICA;
			}
			else if(iridePageNameForCU.startsWith("anagrafica") && (anagAziendaVO.getIdFormaGiuridica() != null || anagAziendaVO.getTipoFormaGiuridica().getCode() != null)) 
			{
				return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO_AZIENDA_FOR_FORMA_GIURIDICA;
			}
		}
		return null;
	}
	
	
	/**
   * Valida l'accesso di un regionale e un provinciale sulle aziende 
   * per la cessazione anche quelle col mandato.
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateRegionale(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
      return null;
    }
    else
      return null;
  }
  
  /**
   * Valida l'accesso di un OPR gestore sulle aziende 
   * per la cessazione anche quelle col mandato.
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateORPGestore(RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient, AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
      return null;
    }
    else
      return null;
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
      return null;
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }
  
  
}

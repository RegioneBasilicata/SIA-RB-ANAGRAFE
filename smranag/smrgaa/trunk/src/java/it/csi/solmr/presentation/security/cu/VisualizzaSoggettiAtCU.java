package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaSoggettiAtCU extends AutorizzazioneCUStandard
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 8945179962302309325L;


  public VisualizzaSoggettiAtCU()
  {
    this.cuName = "VISUALIZZA_SOGGETTI_AT";
    this.isCUForReadWrite = false;
  }

  /**
   * Genera il menu delle pagine html relative a questo macro CU Iride2
   * 
   * @param htmpl
   *          Htmpl
   * @param request
   *          HttpServletRequest
   * @return java.lang.String
   */
  public String writeMenu(Htmpl htmpl, HttpServletRequest request)
  {
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    SianAnagTributariaVO anagTrib = (SianAnagTributariaVO) request.getSession()
        .getAttribute("anagTrib");

    super.writeBanner(htmpl, request);
    super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null,
        anagAziendaVO, null, request);

    String istatResidenza = (String) request.getAttribute("istatResidenza");
    if (istatResidenza == null)
      istatResidenza = request.getParameter("istatResidenzaTributaria");
    String istatSedeLegale = (String) request.getAttribute("istatSedeLegale");
    if (istatSedeLegale == null)
      istatSedeLegale = request.getParameter("istatSedeLegaleTributaria");
    String istatDomicilioFiscale = (String) request
        .getAttribute("istatDomicilioFiscale");
    if (istatDomicilioFiscale == null)
      istatDomicilioFiscale = request
          .getParameter("istatDomicilioFiscaleTributaria");
    if ("".equals(istatResidenza))
      istatDomicilioFiscale = null;
    if ("".equals(istatSedeLegale))
      istatSedeLegale = null;
    if ("".equals(istatDomicilioFiscale))
      istatDomicilioFiscale = null;

    if (ruoloUtenza != null && ruoloUtenza.isValid() && anagAziendaVO != null
        && !Validator.isNotEmpty(anagAziendaVO.getDataFineVal()))
    {
      if (anagTrib != null
          && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
      {
      	scriviCheckBox(anagTrib,htmpl,ruoloUtenza,istatResidenza,istatSedeLegale,istatDomicilioFiscale);
      }
      else
      {
    	  //Questa codice pemette di Visualizzare il check per i profili Arpea e Regione
    	  //anche se l'azienda presenta una delega
    	  if (anagTrib != null)
    	  {
    		  //CUAA Visualizzare il check solo per i profili Arpea e Regione
	          if (ruoloUtenza.isUtenteOPRGestore() || ruoloUtenza.isUtenteRegionale())
	          {
	          	scriviCheckBox(anagTrib,htmpl,ruoloUtenza,istatResidenza,istatSedeLegale,istatDomicilioFiscale);
	          }
    	  }
      }
    }
    return null;

  }
  
  
  private void scriviCheckBox(SianAnagTributariaVO anagTrib,Htmpl htmpl,RuoloUtenza ruoloUtenza,
  		String istatResidenza,String istatSedeLegale,String istatDomicilioFiscale)
  {
    if (anagTrib != null)
    {
      htmpl.newBlock("blkGestioneSian");
      htmpl.newBlock("blkGestioneSian.blkImporta");
      htmpl.newBlock("blkGestioneSian.blkImportaRadio");
      
      
      //CUAA Visualizzare il check solo per i profili Arpea e Regione
      if (ruoloUtenza.isUtenteOPR() || ruoloUtenza.isUtenteOPRGestore()
          || ruoloUtenza.isUtenteRegionale())
        htmpl.newBlock("blkGestioneSian.blkImportaCUAA");
      else htmpl.newBlock("blkGestioneSian.blkNoRadioCUAA");

      if (istatSedeLegale == null)
        htmpl.newBlock("blkGestioneSian.blkNoRadioComuneSedeLegale");
      else
        htmpl.newBlock("blkGestioneSian.blkImportaComuneSedeLegale");

      if (istatDomicilioFiscale == null)
        htmpl.newBlock("blkGestioneSian.blkNoRadioComuneDomicilioFiscale");
      else
        htmpl.newBlock("blkGestioneSian.blkImportaComuneDomicilioFiscale");

      if (anagTrib.getDenominazione() == null
          || "".equals(anagTrib.getDenominazione()))
        htmpl.newBlock("blkGestioneSian.blkNoRadioDenominazione");
      else
        htmpl.newBlock("blkGestioneSian.blkImportaDenominazione");

      if (anagTrib.getCodiceFiscale().length() == 16)
      {
        htmpl.newBlock("blkGestioneSian.blkIndividuale");
        htmpl.newBlock("blkGestioneSian.blkIndividuale.blkImportaRadio");
        if (istatResidenza == null)
          htmpl
              .newBlock("blkGestioneSian.blkIndividuale.blkNoRadioComuneResidenza");
        else
          htmpl
              .newBlock("blkGestioneSian.blkIndividuale.blkImportaComuneResidenza");
      }
      else
      {
        htmpl.newBlock("blkGestioneSian.blkBloccoNoIndividuale1");
        htmpl.newBlock("blkGestioneSian.blkBloccoNoIndividuale2");
        htmpl
            .newBlock("blkGestioneSian.blkBloccoNoIndividuale1.blkImportaRadio");
        htmpl
            .newBlock("blkGestioneSian.blkBloccoNoIndividuale2.blkImportaRadio");
      }
    }
  }
  

  /**
   * Esegue i controlli sulla competenza di dato per l'utente in questione.
   * 
   */
  public String hasCompetenzaDato(HttpServletRequest request,
      HttpServletResponse response, RuoloUtenza ruoloUtenza,
      AnagFacadeClient anagFacadeClient)
  {
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    String CUAAselezionato = request.getParameter("CUAAselezionato");
    if (anagAziendaVO == null)
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO
          + ": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    else
    {
      // Ricerco nuovamente i dati dell'azienda perchè, per impedire l'accesso
      // alla funzionalità, voglio essere sicuro di lavorare
      // sui dati dell'azienda corrispondenti al momento esatto dell'accesso
      // dell'utente alla funzionalità.
      try
      {
        anagAziendaVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO
            .getIdAzienda());
        request.getSession().setAttribute("anagAziendaVO", anagAziendaVO);
        if (!Validator.isNotEmpty(anagAziendaVO.getCUAA())
            && !Validator.isNotEmpty(CUAAselezionato))
        {
          return AnagErrors.ERRORE_CUAA_SIAN_NON_VALORIZZATO;
        }
      }
      catch (SolmrException se)
      {
        return AnagErrors.ERRORE_DI_SISTEMA_AZIENDA_NON_REPERITA;
      }
    }
    return validateGenericUser(ruoloUtenza, anagAziendaVO,
        anagFacadeClient, request);
  }

}

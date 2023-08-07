package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImportaFonteAtCU extends AutorizzazioneCUStandard
{
  /**
   * 
   */
  private static final long serialVersionUID = -83233945692546844L;

  public ImportaFonteAtCU()
  {
    this.cuName = "IMPORTA_FONTE_AT";
    this.isCUForReadWrite = true;
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
    SianAnagTributariaGaaVO anagTrib = (SianAnagTributariaGaaVO) request.getSession()
        .getAttribute("anagTrib");
    @SuppressWarnings("unchecked")
    Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)request.getSession().getAttribute("vAziendaAtecoSec");

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
      	scriviCheckBox(anagTrib,anagAziendaVO,vAziendaAtecoSec,htmpl,ruoloUtenza,istatResidenza,istatSedeLegale,istatDomicilioFiscale);
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
	          	scriviCheckBox(anagTrib,anagAziendaVO,vAziendaAtecoSec,htmpl,ruoloUtenza,istatResidenza,istatSedeLegale,istatDomicilioFiscale);
	          }
    	  }
      }
    }
    return null;

  }
  
  
  private void scriviCheckBox(SianAnagTributariaGaaVO anagTrib, AnagAziendaVO anagAziendaVO, 
      Vector<AziendaAtecoSecVO> vAziendaAtecoSec, Htmpl htmpl,RuoloUtenza ruoloUtenza,
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
      
      
      if((anagTrib.getIdAttivitaAteco() == null)
          && (anagAziendaVO.getTipoAttivitaATECO() == null))
      {
        htmpl.newBlock("blkGestioneSian.blkNoRadioAtecoPrinc");
      }
      else
      {
        htmpl.newBlock("blkGestioneSian.blkImportaAtecoPrinc");
      }
      
      
      if((anagTrib.getvAtecoSecSian() == null)
          && (vAziendaAtecoSec == null))
      {
        htmpl.newBlock("blkGestioneSian.blkNoRadioAtecoSec");
      }
      else
      {
        htmpl.newBlock("blkGestioneSian.blkImportaAtecoSec");
      }
      
      

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
        if (!Validator.isNotEmpty(anagAziendaVO.getCUAA()))
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
  
  
  /**
   * Valida l'accesso di un regionale sulle aziende anche con mandato: usato
   * solo da questo caso d'uso
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
    }
    return null;
  }
   
  /**
   * Valida l'accesso di un OPR Gestore (OPR con delega) ad un'azienda
   * 
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anagFacadeClient
   *          AnagFacadeClient
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
    }
    return null;
  }

}

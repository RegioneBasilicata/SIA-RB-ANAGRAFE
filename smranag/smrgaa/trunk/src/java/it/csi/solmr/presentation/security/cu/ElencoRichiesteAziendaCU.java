package it.csi.solmr.presentation.security.cu;

import java.util.Vector;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.IntermediarioAnagVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ElencoRichiesteAziendaCU extends Autorizzazione
{
	

	
  /**
   * 
   */
  private static final long serialVersionUID = -579251325405243606L;

  public ElencoRichiesteAziendaCU()
	{
		this.cuName="ELENCO_RICHIESTE_AZIENDA";
		this.isCUForReadWrite=false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	@SuppressWarnings("unchecked")
  public String writeMenu(Htmpl htmpl, HttpServletRequest request) 
	{
	  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagAziendaVO");
	  
		super.writeBanner(htmpl, request);
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		super.writeGenericMenu(ruoloUtenza, null, htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		
		Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
		
		
		if(iride2AbilitazioniVO.isUtenteAbilitato("RICHIESTE_CESS_VAL"))
    {
      htmpl.newBlock("blkRichiesteCessVal");
    }
		
		if(iride2AbilitazioniVO.isUtenteAbilitato("RICHIESTA_AZIENDA"))
    {
		  htmpl.newBlock("blkRichiestaAzienda");
    }
		
		
		if(iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_RICHIESTA"))
    {
      request.getSession().setAttribute("scritturaRichiesta","true");
    }
		
		Vector<String> vActor = (Vector<String>)request.getSession().getAttribute("vActor");
    if(vActor.contains(SolmrConstants.GESTORE_CAA))
    {
      htmpl.newBlock("blkGestoreCaa");
    }
    else
    {
      htmpl.newBlock("blkAltro");
    }
		
		
       
    /*if(Validator.isNotEmpty(aziendaNuovaVO))
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
    }*/
		
		
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
    
    return validate(request, response, ruoloUtenza, anagFacadeClient, anagAziendaVO);
	}
	
	
	public String validate(HttpServletRequest request,
      HttpServletResponse response,
      RuoloUtenza ruoloUtenza,
      AnagFacadeClient anagFacadeClient,
      AnagAziendaVO anagAziendaVO)
  {
	  
	  try
	  {	    
	    
	    Long idAzienda = null;
	    if(Validator.isEmpty(request.getParameter("idAzienda")))
	    {
	      idAzienda = (Long)request.getSession().getAttribute("idAziendaValida");
	    }
	    else
	    {
	      idAzienda = Long.decode(request.getParameter("idAzienda"));
	    }
	    
	    //se arrivo di qua ho idAnag
	    if(anagAziendaVO == null)
	    {
  	    String arrivo = request.getParameter("arrivo");
  	    if(Validator.isNotEmpty(arrivo) && "elenco".equalsIgnoreCase(arrivo))
  	    {
  	      anagAziendaVO = anagFacadeClient.getAziendaById(idAzienda);
  	    }
  	    else
  	    {
  	      anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
  	      anagAziendaVO = anagFacadeClient.getAziendaById(anagAziendaVO.getIdAnagAzienda());
  	    }
	    }
	    
	  }
	  catch(SolmrException se)
	  {
	    SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
	  }
    
	  return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
  }
	
	
	
	protected String validateGenericUser(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO, AnagFacadeClient anagFacadeClient, HttpServletRequest request)
  {
    if (ruoloUtenza.isUtenteRegionale())
    {
      return validateRegionale(anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteProvinciale())
    {
      return validateProvinciale(ruoloUtenza, anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteOPR())
    {
      return validateOPR(anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteOPRGestore())
    {
      return validateORPGestore(ruoloUtenza, anagFacadeClient, anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteIntermediario())
    {
      SolmrLogger.debug(this, "Sono nel caso di utente intermediario del metodo validateGenericUser in Autorizzazione\n");
      
      String validateCAAStr = null;
      @SuppressWarnings("unchecked")
      Vector<String> vActor = (Vector<String>)request.getSession().getAttribute("vActor");
      if(vActor.contains(SolmrConstants.GESTORE_CAA))
      {
        
        try
        {
          IntermediarioAnagVO  intermediarioAnagVO = anagFacadeClient.findIntermediarioVOByIdAzienda(anagAziendaVO.getIdAzienda());
          String codiceIntermediario = ruoloUtenza.getCodiceEnte();
          if(!codiceIntermediario.substring(1,3).equalsIgnoreCase(intermediarioAnagVO.getCodiceFiscale().substring(1,3)))
          {
            return AnagErrors.ERRORE_DI_DELEGA_GESTORE_CAA_SEL;
          }
        }
        catch (SolmrException ex)
        {
          return ex.getMessage();
        }
        catch (Exception ex)
        {
          return AnagErrors.ERRORE_DI_DELEGA_GESTORE_CAA;
        }
      }
      else
      {      
        validateCAAStr = validateCAA(ruoloUtenza, anagFacadeClient, anagAziendaVO);
      }
      
      return validateCAAStr;
    }
    if (ruoloUtenza.isUtenteComunitaMontana())
    {
      return validateComunitaMontana(anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteTitolareCf() || ruoloUtenza.isUtenteLegaleRappresentante())
    {
      // TITOLARE
      return validateTitolare(anagAziendaVO, ruoloUtenza, request, anagFacadeClient);
    }
    if (ruoloUtenza.isUtenteNonIscrittoCIIA())
    {
      // isUtenteAziendaAgricola
      return validateAziendaAgricola(anagAziendaVO, ruoloUtenza, request, anagFacadeClient);
    }
    return null;
  }

}

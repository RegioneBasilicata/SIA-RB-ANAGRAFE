package it.csi.solmr.presentation.security.cu;

import java.util.Vector;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RichiesteCessValCU extends Autorizzazione
{
	
	/**
   * 
   */
  private static final long serialVersionUID = 1141210281467741207L;

  public RichiesteCessValCU()
	{
		this.cuName="RICHIESTE_CESS_VAL";
		//Il caso d'uso è in scrittura però è associato a titolare/rappresentante/azienda agricola che sono in lettura
		//quindi si è deciso per poter accedere di metterlo fittiziamente in lettura!!!
		this.isCUForReadWrite=false;
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
		
		htmpl.set("aziendaIntestazione", "azienda");
		
		if(request.getAttribute("valCess") == null)
		{
  		if(Validator.isNotEmpty(aziendaNuovaVO) && Validator.isNotEmpty(aziendaNuovaVO.getCuaa()))
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
		}
		else
		{
		  if(Validator.isNotEmpty(aziendaNuovaVO))
      {
        htmpl.set("cuaaIntestazione", aziendaNuovaVO.getCuaa());
        htmpl.set("denominazioneIntestazione", aziendaNuovaVO.getDenominazione());
      }		  
		}
		
		
		@SuppressWarnings("unchecked")
    Vector<String> vActor = (Vector<String>)request.getSession().getAttribute("vActor");
    if(vActor.contains(SolmrConstants.GESTORE_CAA))
    {
      htmpl.newBlock("blkGestoreCaa");
    }
    else
    {
      htmpl.newBlock("blkAltro");
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

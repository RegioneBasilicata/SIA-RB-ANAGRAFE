package it.csi.solmr.util;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.dto.anag.sian.SianUtenteVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;

/**
 * <p>Title: Utilita per il Profilo</p>
 * <p>Description: Classe astratta contenente metodi di utilità per l'uso della classe Profile</p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: CSI Piemonte</p>
 * @author TOBECONFIG
 * @version 1.1
 */

public abstract class ProfileUtils {

	public static void setFieldUltimaModificaByRuolo(RuoloUtenza ruoloUtenza,
	    Htmpl htmpl, String bloccoCampo, String data, 
	    String utente, String ente, String motivoModifica) 
	{
	  String ultimaModifica = "";
		if(ruoloUtenza.isUtentePALocale() || ruoloUtenza.isUtentePALocaleSuper())
		{
		  if(Validator.isNotEmpty(data))
      {
		    ultimaModifica += data;
      }
		}
		else
		{
		  if(Validator.isNotEmpty(data))
      {
		    ultimaModifica += data;
      }
      
      String tmp="";
      if(Validator.isNotEmpty(utente))
      {
        if(tmp.equals(""))
          tmp+=" (";
        tmp+=utente;
      }
      if(Validator.isNotEmpty(ente))
      {
        if(tmp.equals(""))
          tmp+=" (";
        else
          tmp+=" - ";
        tmp+=ente;
      }
      if(Validator.isNotEmpty(motivoModifica))
      {
        if(tmp.equals(""))
          tmp+=" (";
        else
          tmp+=" - ";
        tmp+=motivoModifica;
      }
      if(!tmp.equals(""))
        tmp+=")";
      ultimaModifica+=tmp;	  
		}
		
		htmpl.set(bloccoCampo, ultimaModifica);
	}
	
	
	public static String setFieldUltimaModificaByRuoloNoHtmpl(RuoloUtenza ruoloUtenza,
      String data, String utente, String ente, String motivoModifica) 
  {
    String ultimaModifica = "";
    if(ruoloUtenza.isUtentePALocale() || ruoloUtenza.isUtentePALocaleSuper())
    {
      if(Validator.isNotEmpty(data))
      {
        ultimaModifica += data;
      }
    }
    else
    {
      if(Validator.isNotEmpty(data))
      {
        ultimaModifica += data;
      }
      
      String tmp="";
      if(Validator.isNotEmpty(utente))
      {
        if(tmp.equals(""))
          tmp+=" (";
        tmp+=utente;
      }
      if(Validator.isNotEmpty(ente))
      {
        if(tmp.equals(""))
          tmp+=" (";
        else
          tmp+=" - ";
        tmp+=ente;
      }
      if(Validator.isNotEmpty(motivoModifica))
      {
        if(tmp.equals(""))
          tmp+=" (";
        else
          tmp+=" - ";
        tmp+=motivoModifica;
      }
      if(!tmp.equals(""))
        tmp+=")";
      ultimaModifica+=tmp;    
    }
    
    return ultimaModifica;
  }
	
	
	public static void setFieldUltimaModificaByRuolo(boolean paLocale,
      Htmpl htmpl, String bloccoCampo, String data, 
      String utente, String ente, String motivoModifica) 
  {
    String ultimaModifica = "";
    if(paLocale)
    {
      if(Validator.isNotEmpty(data))
      {
        ultimaModifica += data;
      }
    }
    else
    {
      if(Validator.isNotEmpty(data))
      {
        ultimaModifica += data;
      }
      
      String tmp="";
      if(Validator.isNotEmpty(utente))
      {
        if(tmp.equals(""))
          tmp+=" (";
        tmp+=utente;
      }
      if(Validator.isNotEmpty(ente))
      {
        if(tmp.equals(""))
          tmp+=" (";
        else
          tmp+=" - ";
        tmp+=ente;
      }
      if(Validator.isNotEmpty(motivoModifica))
      {
        if(tmp.equals(""))
          tmp+=" (";
        else
          tmp+=" - ";
        tmp+=motivoModifica;
      }
      if(!tmp.equals(""))
        tmp+=")";
      ultimaModifica+=tmp;    
    }
    
    htmpl.set(bloccoCampo, ultimaModifica);
  }
	
	
	public static SianUtenteVO getSianUtente(RuoloUtenza ruolo)
	{
	  SianUtenteVO siaVO = new SianUtenteVO();
	  
	  siaVO.setCodiceEnte(ruolo.getCodiceEnte());
	  siaVO.setCodiceFiscale(ruolo.getCodiceFiscale());
	  siaVO.setIdProcedimento(new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE).longValue());
	  siaVO.setRuolo(ruolo.getCodiceRuolo());
	  
	  return siaVO;
	}
	
}

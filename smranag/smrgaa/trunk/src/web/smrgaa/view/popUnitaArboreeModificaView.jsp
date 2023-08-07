<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popUnitaArboreeModifica.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	//  Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	StoricoParticellaVO storicoParticellaArboreaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaArboreaVO");
 	Vector<TipoVarietaVO> elencoVarieta = (Vector<TipoVarietaVO>)request.getAttribute("elencoVarieta");
 	String reload = (String)request.getAttribute("reload");
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	Hashtable<Integer,ValidationErrors> erroriAltriVitigni = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriAltriVitigni");
 	
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String popupError = "alert({0})";
	StringProcessor jssp = new JavaScriptStringProcessor();
 	
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	else 
  {
 		htmpl.newBlock("blkElencoDati");
 		// Gestisco gli errori relativi ai dati dell'operazione selezionata
 		if(errors != null && errors.size() > 0) 
    {
 			Iterator<String> iter = htmpl.getVariableIterator();
 			while(iter.hasNext()) {
 				String key = (String)iter.next();
 				if(key.equals("err_error")) {
 					String property = key.substring(4);
 			    	Iterator<ValidationError> errorIterator = errors.get(property);
 			    	if(errorIterator != null) 
            {
 			    		ValidationError error = (ValidationError)errorIterator.next();
 			    		htmpl.set(key, MessageFormat.format(popupError, new Object[] {"'"+jssp.process(error.getMessage())+"'"}), null);
 			    	}
 				}
 				else if(key.startsWith("err_")) 
        {
		    	String property = key.substring(4);
		    	Iterator<ValidationError> errorIterator = errors.get(property);
		    	if(errorIterator != null) 
          {
		    		ValidationError error = (ValidationError)errorIterator.next();
		    		htmpl.set("blkElencoDati.err_"+property,
		                      MessageFormat.format(htmlStringKO,
		                      new Object[] {
		                      pathErrori + "/"+ imko,
		                      "'"+jssp.process(error.getMessage())+"'",
		                      error.getMessage()}),
		                      null);
		    	}
		    }
 			}
 		}
 		htmpl.set("blkElencoDati.descComuneParticella", storicoParticellaArboreaVO.getComuneParticellaVO().getDescom());
 		htmpl.set("blkElencoDati.siglaProvinciaParticella", "("+storicoParticellaArboreaVO.getComuneParticellaVO().getSiglaProv()+")");
 		htmpl.set("blkElencoDati.sezione", storicoParticellaArboreaVO.getSezione());
 		htmpl.set("blkElencoDati.foglio", storicoParticellaArboreaVO.getFoglio());
 		htmpl.set("blkElencoDati.particella", storicoParticellaArboreaVO.getParticella());
 		htmpl.set("blkElencoDati.subalterno", storicoParticellaArboreaVO.getSubalterno());
 		htmpl.set("blkElencoDati.supCatastale", StringUtils.parseSuperficieField(storicoParticellaArboreaVO.getSupCatastale()));
 		htmpl.set("blkElencoDati.progrUnar", storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getProgrUnar());
 		if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdUtilizzo() != null) {
 			htmpl.set("blkElencoDati.destProduttiva", "["+storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO().getCodice()+"] "+storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO().getDescrizione());
 		}
 		if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdVarieta() != null) {
 			htmpl.set("blkElencoDati.varieta", "["+storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoVarietaVO().getCodiceVarieta()+"] "+storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoVarietaVO().getDescrizione());
 		}
    
    if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getArea())) 
    {
      htmpl.set("blkElencoDati.area", StringUtils.parseSuperficieField(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getArea()));
    }
    else 
    {
      htmpl.set("blkElencoDati.area", "");
    }
    
    
    
    //Gestione menzione geografica
    // se N o nullail flag solo visualizzazione 
    if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
    {
      TipoTipologiaVinoVO tipoTipologiaVinoVO = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO();
      if("N".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneMenzione()))
      {
        String menzioneGeografica = "";
        if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
          .getElencoMenzioneGeografica()))
        {
          Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = storicoParticellaArboreaVO
            .getStoricoUnitaArboreaVO().getElencoMenzioneGeografica();
          for(int i=0;i<elencoMenzioneGeografica.size();i++)
          {
            TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = elencoMenzioneGeografica.get(i);
            if(i !=0)
            {
              menzioneGeografica += " - "; 
            }          
            menzioneGeografica += tipoMenzioneGeograficaVO.getDescrizione();
          }
        }
        htmpl.newBlock("blkElencoDati.blkMenzioneGeografica");
        htmpl.set("blkElencoDati.blkMenzioneGeografica.menzioneGeografica", menzioneGeografica);
      }
    }
    else
    {    
      String menzioneGeografica = "";
      if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
        .getElencoMenzioneGeografica()))
      {
        Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = storicoParticellaArboreaVO
          .getStoricoUnitaArboreaVO().getElencoMenzioneGeografica();
        for(int i=0;i<elencoMenzioneGeografica.size();i++)
        {
          TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = elencoMenzioneGeografica.get(i);
          if(i !=0)
          {
            menzioneGeografica += " - "; 
          }          
          menzioneGeografica += tipoMenzioneGeograficaVO.getDescrizione();
        }
      }
      htmpl.newBlock("blkElencoDati.blkMenzioneGeografica");
      htmpl.set("blkElencoDati.blkMenzioneGeografica.menzioneGeografica", menzioneGeografica);
    }
    
    
    
    
    if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getProvinciaCCIAA() !=null)
    {
      htmpl.set("blkElencoDati.provinciaCCIAA",storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getProvinciaCCIAA());
    }
    else if(storicoParticellaArboreaVO.getComuneParticellaVO() != null)
    {
      htmpl.set("blkElencoDati.provinciaCCIAA",storicoParticellaArboreaVO
        .getComuneParticellaVO().getSiglaProv());
    }
    else
    {
      htmpl.set("blkElencoDati.provinciaCCIAA","");
    }
    
    
   //Matricola CCIAA
    if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getMatricolaCCIAA() !=null)
    {
      htmpl.set("blkElencoDati.matricolaCCIAA", storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getMatricolaCCIAA());
    }
    else
    {
      htmpl.set("blkElencoDati.matricolaCCIAA","");
    }
    
    //Anno iscrizione albo
    if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getAnnoIscrizioneAlbo() !=null)
    {
      htmpl.set("blkElencoDati.annoIscrizioneAlbo", storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getAnnoIscrizioneAlbo());
    }
    else
    {
      htmpl.set("blkElencoDati.annoIscrizioneAlbo","");
    }
    
    if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO() !=null)
    {
      if((storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getVinoDoc() != null)
        && storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getVinoDoc().equalsIgnoreCase("S"))
      {
        htmpl.set("blkElencoDati.checkedVinoDoc", "checked=\"checked\"");
        htmpl.set("blkElencoDati.vinoDoc", "S");
      }
      else
      {
        htmpl.set("blkElencoDati.checkedVinoDoc", "");
        htmpl.set("blkElencoDati.vinoDoc", "N");
      }
    }
    else
    {
      htmpl.set("blkElencoDati.checkedVinoDoc", "");
      htmpl.set("blkElencoDati.vinoDoc", "N");
    }
    
    if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO() !=null)
    {    
      if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getCodiceMipaf() !=null)
      {
        htmpl.set("blkElencoDati.codiceMipaf",storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
          .getTipoTipologiaVinoVO().getCodiceMipaf());
      }
      else
      {
        htmpl.set("blkElencoDati.codiceMipaf","");
      }
      
      if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getDescrizione() !=null)
      {
        htmpl.set("blkElencoDati.descrizioneTipologiaVino",storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
          .getTipoTipologiaVinoVO().getDescrizione());
      }
      else
      {
        htmpl.set("blkElencoDati.descrizioneTipologiaVino","");
      }
      
    }
    
    
    if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
    {
      if("N".equalsIgnoreCase(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
        .getTipoTipologiaVinoVO().getFlagGestioneVigna()))
      {
        htmpl.set("blkElencoDati.readVigna", "readOnly=\"readOnly\"");
      }
      else if(("S".equalsIgnoreCase(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
        .getTipoTipologiaVinoVO().getIndicazioneVigna()))
       && Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
        .getVignaVO()))
      {
        htmpl.set("blkElencoDati.readVigna", "readOnly=\"readOnly\"");
      }
    }
    else if(Validator.isEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
    {
      htmpl.set("blkElencoDati.readVigna", "readOnly=\"readOnly\"");
    }
    
    
    if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
    {
      if("N".equalsIgnoreCase(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
        .getTipoTipologiaVinoVO().getFlagGestioneEtichetta()))
      {
        htmpl.set("blkElencoDati.readAnnotazioneEtichetta", "readOnly=\"readOnly\"");
      }
    }
    else if(Validator.isEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
    {
      htmpl.set("blkElencoDati.readAnnotazioneEtichetta", "readOnly=\"readOnly\"");
    }
    
    
    if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
    {
      TipoTipologiaVinoVO tipoTipologiaVinoVO = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO();
      if(tipoTipologiaVinoVO.getVignaVO() != null)
      {        
        htmpl.set("blkElencoDati.provVignaRegionale", tipoTipologiaVinoVO.getVignaVO().getMenzione()); 
      }
    }     
    
 		if(Validator.isNotEmpty(reload)) 
    { 
      
 			// RICADUTA
 			if(request.getParameter("ricaduta").equalsIgnoreCase(SolmrConstants.FLAG_S)) {
 				htmpl.set("blkElencoDati.checkedSiRicaduta", "checked=\"checked\"");
 			}
 			else {
 				htmpl.set("blkElencoDati.checkedNoRicaduta", "checked=\"checked\"");
 			}
 			// COLTURA SPECIALIZZATA
 			if(request.getParameter("colturaSpecializzata").equalsIgnoreCase(SolmrConstants.FLAG_S)) {
 				htmpl.set("blkElencoDati.checkedSiSpec", "checked=\"checked\"");
 			}
 			else {
 				htmpl.set("blkElencoDati.checkedNoSpec", "checked=\"checked\"");
 			}
      
      
      
      //Vigna      
      String vigna = request.getParameter("vigna");
      if(Validator.isNotEmpty(vigna))
      {
        htmpl.set("blkElencoDati.vigna", vigna);
      }
      else
      {
        htmpl.set("blkElencoDati.vigna", "");
      }
      
      
      //Annotazione in etichetta      
      String annotazioneEtichetta = request.getParameter("annotazioneEtichetta");
      if(Validator.isNotEmpty(annotazioneEtichetta))
      {
        htmpl.set("blkElencoDati.annotazioneEtichetta", annotazioneEtichetta);
      }
      else
      {
        htmpl.set("blkElencoDati.annotazioneEtichetta", "");
      }
      
      
      
      
      String idMenzioneGeografica = request.getParameter("idMenzioneGeografica");
      if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
      {
        TipoTipologiaVinoVO tipoTipologiaVinoVO = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO();
        if("S".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneMenzione()))
        {
          Vector<TipoMenzioneGeograficaVO> vTipoMenzioneGeografica = storicoParticellaArboreaVO
            .getStoricoUnitaArboreaVO().getElencoMenzioneGeografica();
          htmpl.newBlock("blkElencoDati.blkTipoMenzioneGeografica");
          for(int i = 0; i < vTipoMenzioneGeografica.size(); i++) 
          {
            TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = vTipoMenzioneGeografica.get(i);
            htmpl.newBlock("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica");
            htmpl.set("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica.idMenzioneGeografica", 
              ""+tipoMenzioneGeograficaVO.getIdMenzioneGeografica());
            htmpl.set("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica.descrizione", 
              tipoMenzioneGeograficaVO.getDescrizione());
            if(Validator.isNotEmpty(idMenzioneGeografica)) 
            {
              Long idMenzioneGeograficaLg = new Long(idMenzioneGeografica);
              if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdMenzioneGeografica().compareTo(
                idMenzioneGeograficaLg) == 0) 
              {
                htmpl.set("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica.selected", "selected=\"selected\"");
              }
            }
          }        
        }         
      }
      
      
      
   
 		}
 		else 
    {      
     
      //Vigna
      if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
      {
        //se il flag del vino non lo permette devo resettare il campo....
        if("N".equalsIgnoreCase(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
          .getTipoTipologiaVinoVO().getFlagGestioneVigna()))
        {
          htmpl.set("blkElencoDati.vigna", "");
        }
        else
        {
          if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getVigna() !=null)
          {    
            htmpl.set("blkElencoDati.vigna", storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getVigna());
          }
          else
          {
            htmpl.set("blkElencoDati.vigna","");
          }
        }
      }
      else
      {
        if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getVigna() !=null)
        {    
          htmpl.set("blkElencoDati.vigna", storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getVigna());
        }
        else
        {
          htmpl.set("blkElencoDati.vigna","");
        }
      }
      
      //Annotazione in etichetta
      if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
      {
        //se il flag del vino non lo permette devo resettare il campo....
        if("N".equalsIgnoreCase(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO()
          .getTipoTipologiaVinoVO().getFlagGestioneEtichetta()))
        {
          htmpl.set("blkElencoDati.annotazioneEtichetta", "");
        }
        else
        {
          if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getEtichetta() !=null)
          {    
            htmpl.set("blkElencoDati.annotazioneEtichetta", storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getEtichetta());
          }
          else
          {
            htmpl.set("blkElencoDati.annotazioneEtichetta","");
          }
        }
      }
      else
      {
        if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getEtichetta() !=null)
        {    
          htmpl.set("blkElencoDati.annotazioneEtichetta", storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getEtichetta());
        }
        else
        {
          htmpl.set("blkElencoDati.annotazioneEtichetta","");
        }
      }
      
      
 			// RICADUTA
 			if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getRicaduta().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
 				htmpl.set("blkElencoDati.checkedSiRicaduta", "checked=\"checked\"");
 			}
 			else {
 				htmpl.set("blkElencoDati.checkedNoRicaduta", "checked=\"checked\"");
 			}
 			// COLTURA SPECIALIZZATA
 			if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getColturaSpecializzata().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
 				htmpl.set("blkElencoDati.checkedSiSpec", "checked=\"checked\"");
 			}
 			else {
 				htmpl.set("blkElencoDati.checkedNoSpec", "checked=\"checked\"");
 			}
      
      
      
      
      if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
      {
        TipoTipologiaVinoVO tipoTipologiaVinoVO = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO();
        if("S".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneMenzione()))
        {
          Vector<TipoMenzioneGeograficaVO> vTipoMenzioneGeografica = storicoParticellaArboreaVO
            .getStoricoUnitaArboreaVO().getElencoMenzioneGeografica();
          htmpl.newBlock("blkElencoDati.blkTipoMenzioneGeografica");
          for(int i = 0; i < vTipoMenzioneGeografica.size(); i++) 
          {
            TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = vTipoMenzioneGeografica.get(i);
            htmpl.newBlock("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica");
            htmpl.set("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica.idMenzioneGeografica", 
              ""+tipoMenzioneGeograficaVO.getIdMenzioneGeografica());
            htmpl.set("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica.descrizione", 
              tipoMenzioneGeograficaVO.getDescrizione());
            if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdMenzioneGeografica())) 
            {
              if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdMenzioneGeografica().compareTo(
                tipoMenzioneGeograficaVO.getIdMenzioneGeografica()) == 0) 
              {
                htmpl.set("blkElencoDati.blkTipoMenzioneGeografica.blkElencoMenzioneGeografica.selected", "selected=\"selected\"");
              }
            }
          }        
        }         
      }
      
      
      
      
 		}
    
 		AltroVitignoVO[] elencoAtriVitigni = null;
 		if(request.getAttribute("elencoAltriVitigniVisualizza") != null) 
 		{
 			elencoAtriVitigni = (AltroVitignoVO[])request.getAttribute("elencoAltriVitigniVisualizza");
 			reload = null;
 		}
 		else 
 		{
 			elencoAtriVitigni = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni();
 		}
 		if(elencoAtriVitigni != null && elencoAtriVitigni.length > 0) 
    {
 			htmpl.newBlock("blkElencoDati.blkAltriVitigni");
 			for(int i = 0; i < elencoAtriVitigni.length; i++) 
 			{
 				AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAtriVitigni[i];
 				htmpl.newBlock("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni");
 				htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.numeroAltriVitigni", String.valueOf(i));
 				htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.contatore", String.valueOf(i));
 				if((storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdUtilizzo() != null)
 				  && (storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDestinazione() != null)
 				  && (storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso() != null)
 				  && (storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoQualitaUso() != null))    
 				{
 					for(int a = 0; a < elencoVarieta.size(); a++) 
 					{
 						TipoVarietaVO tipoVarietaVO = elencoVarieta.get(a);
 						htmpl.newBlock("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.blkVarieta");
 						htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.blkVarieta.idVarieta", tipoVarietaVO.getIdVarieta()+"/"+tipoVarietaVO.getIdUtilizzo());
 						htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.blkVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
 						if(Validator.isNotEmpty(reload)) {
 		 					if(request.getParameterValues("idVarieta") != null && i < request.getParameterValues("idVarieta").length && Validator.isNotEmpty(request.getParameterValues("idVarieta")[i])) {
 		 						int endIndex = request.getParameterValues("idVarieta")[i].indexOf("/");
 		 						Long idVarieta = Long.decode(request.getParameterValues("idVarieta")[i].substring(0, endIndex));
 		 						if(idVarieta.compareTo(tipoVarietaVO.getIdVarieta()) == 0) {
 		 							htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.blkVarieta.selected", "selected=\"selected\"", null);
 		 						}
 		 					}
 		 				}
 		 				else 
 		 				{
 		 					if(altroVitignoVO != null && altroVitignoVO.getIdVarieta() != null && altroVitignoVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
 		 					{
 		 						htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.blkVarieta.selected", "selected=\"selected\"", null);
 		 					}
 		 				}
 					}
 				}
 				if(Validator.isNotEmpty(reload)) {
 					if(request.getParameterValues("percentuale") != null && i < request.getParameterValues("percentuale").length) {
 						htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.percentuale", request.getParameterValues("percentuale")[i]);
 					}
 				}
 				else {
 					htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.percentuale", altroVitignoVO.getPercentualeVitigno());
 				}
 				// Gestione errori
 				if(erroriAltriVitigni != null && erroriAltriVitigni.size() > 0) {
 					ValidationErrors errorsAltriVitigni = (ValidationErrors)erroriAltriVitigni.get(new Integer(i));
 					if(errorsAltriVitigni != null && errorsAltriVitigni.size() > 0) {
 					 	Iterator iter = htmpl.getVariableIterator();
 					 	while(iter.hasNext()) {
 					 		String chiave = (String)iter.next();
 					 		if(chiave.startsWith("err_")) {
 					 		    String property = chiave.substring(4);
 					 		    Iterator<ValidationError> errorIterator = errorsAltriVitigni.get(property);
 					 			if(errorIterator != null) {
 					 			 	ValidationError error = (ValidationError)errorIterator.next();
 					 			   	htmpl.set("blkElencoDati.blkAltriVitigni.blkElencoAltriVitigni.err_"+property,
 					 			               MessageFormat.format(htmlStringKO,
 					 			               new Object[] {
 					 			               pathErrori + "/"+ imko,
 					 			               "'"+jssp.process(error.getMessage())+"'",
 					 			               error.getMessage()}),
 					 			               null);
 					 			}
 					 		}
 					 	}
 					 }
 				}
 			}
 		}
    
    
    
 	}

%>
<%=htmpl.text()%>

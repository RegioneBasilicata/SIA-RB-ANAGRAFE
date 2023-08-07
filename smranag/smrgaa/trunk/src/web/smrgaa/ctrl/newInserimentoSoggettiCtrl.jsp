<%@page import="it.csi.smranag.smrgaa.util.CodiceFiscaleUtils"%>
<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoSoggettiCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova");

  String newInserimentoSoggettiUrl = "/view/newInserimentoSoggettiView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoAllevamentiCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoSoggAssCtrl.jsp";
  String pageNextCaa = "/ctrl/newInserimentoSoggAssCaaCtrl.jsp";
  
  String pageNextNoAssociati = "/ctrl/newInserimentoContiCorrentiCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoAllevamenti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_SC);
  request.setAttribute("testoHelp", testoHelp);
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  String operazione = request.getParameter("operazione");
  String regimeInserimentoSoggetti = request.getParameter("regimeInserimentoSoggetti");
  
  //usaro per evitare che los tesso soggettosia inserito più volte...
  Vector<String> vCodFiscSoggettiInseriti = new Vector<String>();  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (regimeInserimentoSoggetti != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  

 
  
  Vector<SoggettoAziendaNuovaVO> vSoggettiAziendaNuova = null;
  try
  {
    if(regimeInserimentoSoggetti == null)
	  {
	    //Prima volta che entro carico tuti i soggetti..da db
	    vSoggettiAziendaNuova = gaaFacadeClient.getSoggettiAziendaNuovaIscrizione(idAziendaNuova.longValue());
	    request.setAttribute("vSoggettiAziendaNuova", vSoggettiAziendaNuova);
	  }
	  
	  SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = gaaFacadeClient.getRappLegaleNuovaIscrizione(idAziendaNuova.longValue());
    request.setAttribute("soggettoAziendaNuovaVO", soggettoAziendaNuovaVO);
  
    Vector<CodeDescription> elencoRuoli = anagFacadeClient.getTipiRuoloNonTitolareAndNonSpecificato();
    request.setAttribute("elencoRuoli", elencoRuoli);
    
    Vector<ProvinciaVO> vProvince =  anagFacadeClient.getProvince();
    String[] orderBy = new String[] {SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC};
    request.setAttribute("vProvince", vProvince);
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoSoggettiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  String[] idRuolo = request.getParameterValues("idRuolo");
  String[] dataInizioRuolo = request.getParameterValues("dataInizioRuolo");
  String[] codiceFiscale = request.getParameterValues("codiceFiscale");
  String[] cognome = request.getParameterValues("cognome");
  String[] nome = request.getParameterValues("nome");
  String[] istatProvincia = request.getParameterValues("istatProvincia");
  String[] istatComune = request.getParameterValues("istatComune");
  String[] indirizzo = request.getParameterValues("indirizzo");
  String[] cap = request.getParameterValues("cap");
  String[] telefono = request.getParameterValues("telefono");
  String[] email = request.getParameterValues("email");
  
  
  
  // Ricerco il comune in relazione alla provincia
  HashMap<String, Vector<ComuneVO>> hProvCom = new HashMap<String, Vector<ComuneVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeInserimentoSoggetti))
    {
      if(istatProvincia != null) 
      {
        for(int i = 0; i < istatProvincia.length; i++) 
        {         
          if(Validator.isNotEmpty(istatProvincia[i])) 
          {
            Vector<ComuneVO> vComune = anagFacadeClient.getComuniAttiviByIstatProvincia(istatProvincia[i]);
            hProvCom.put(istatProvincia[i], vComune);   
          }
        }
      }
    }
    //prima volta che entro!!!
    else 
    {
      if(vSoggettiAziendaNuova != null) 
      {
        for(int i = 0; i < vSoggettiAziendaNuova.size(); i++) 
        {          
          if(vSoggettiAziendaNuova.get(i).getIstatProvincia() != null) 
          {
            Vector<ComuneVO> vComune = anagFacadeClient.getComuniAttiviByIstatProvincia(vSoggettiAziendaNuova.get(i).getIstatProvincia());
            hProvCom.put(vSoggettiAziendaNuova.get(i).getIstatProvincia(), vComune);   
          }
        } 
      }
    }
    request.setAttribute("hProvCom", hProvCom);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - newInserimentoSoggettiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(se);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (regimeInserimentoSoggetti != null)) 
  {    
    //E' stata inserita almeno una persona
    if(Validator.isNotEmpty(idRuolo))
    {
      Vector<SoggettoAziendaNuovaVO> vSoggettoAziendaNuovaMod = new Vector<SoggettoAziendaNuovaVO>();  
	    Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
	    int countErrori = 0;
	    
	    //Controlli da effettuare
	    for(int i=0;i<idRuolo.length;i++)
	    {
	      ValidationErrors errors = new ValidationErrors();
	      
	      //Controllo solo se almeno un campo è stato valorizzato
	      if(Validator.isNotEmpty(idRuolo[i])
	        || Validator.isNotEmpty(dataInizioRuolo[i])
	        || Validator.isNotEmpty(codiceFiscale[i])
	        || Validator.isNotEmpty(cognome[i])
	        || Validator.isNotEmpty(nome[i])
	        || Validator.isNotEmpty(istatProvincia[i])
	        || Validator.isNotEmpty(istatComune[i])
	        || Validator.isNotEmpty(indirizzo[i])
	        || Validator.isNotEmpty(cap[i])
	        || Validator.isNotEmpty(telefono[i])
	        || Validator.isNotEmpty(email[i]))
	      {
	        SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = new SoggettoAziendaNuovaVO();
	        
	        soggettoAziendaNuovaVO.setIdAziendaNuova(idAziendaNuova);
	      
		      if (Validator.isEmpty(idRuolo[i]))
			    {
			      errors.add("idRuolo", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
			      countErrori++;
			    }
			    else
			    {
			      soggettoAziendaNuovaVO.setIdRuolo(new Integer(idRuolo[i]));
			    }
			    
			    if (Validator.isEmpty(dataInizioRuolo[i]))
          {
            errors.add("dataInizioRuolo", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
          else
          {
            if(!Validator.isDate(dataInizioRuolo[i]))
            {
              errors.add("dataInizioRuolo", new ValidationError("Il formato della Data inizio ruolo deve essere gg/mm/aaaa."));
              countErrori++;
            }
            else
            {
              Date today = new Date();
              Date dataInizioRuoloDt = DateUtils.parseDate(dataInizioRuolo[i].trim());
              if(dataInizioRuoloDt.after(new Date())) 
			        {
			          errors.add("dataInizioRuolo", new ValidationError(AnagErrors.ERRORE_KO_DATA_MAX_DATA_CORRENTE));
			          countErrori++;
			        }
			                   
              soggettoAziendaNuovaVO.setDataInizioRuolo(dataInizioRuoloDt);
            }
          }
          
          if (Validator.isEmpty(codiceFiscale[i]))
          {
            errors.add("codiceFiscale", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
          else
          {
            String codFiscaleStr = codiceFiscale[i].trim().toUpperCase();
            if(!Validator.controlloCf(codiceFiscale[i]))
            {
              errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_CUAA_NO_CORRETTO")));
            }
            else
            {
	            try 
	            {
	               if (Validator.isNotEmpty(cognome[i])
	                 && Validator.isNotEmpty(nome[i]))
	               {
	                 String sesso = CodiceFiscaleUtils.estraiSessoCodiceFiscale(codFiscaleStr);
	                 String codFiscComune = CodiceFiscaleUtils.estraiCodiceFiscaleComune(codFiscaleStr);
	                 Date dataNascita = CodiceFiscaleUtils.estraiDataNascitaCodiceFiscale(codFiscaleStr);
	                 Validator.verificaCf(nome[i].trim().toUpperCase(),cognome[i].trim().toUpperCase(),
	                    sesso, dataNascita, codFiscComune, codFiscaleStr);
	               }
	            }
	            catch(CodiceFiscaleException ce) 
	            {
	              if(ce.getNome()) 
	              {
	                errors.add("nome",new ValidationError(AnagErrors.ERR_NOME_CODICE_FISCALE));
	                countErrori++;
	              }
					      if(ce.getCognome()) {
					        errors.add("cognome",new ValidationError(AnagErrors.ERR_COGNOME_CODICE_FISCALE));
					        countErrori++;
					      }
					    }
					  }
             
            soggettoAziendaNuovaVO.setCodiceFiscale(codFiscaleStr);
            
            
            boolean rappLegale = false;
            if(Validator.isNotEmpty(aziendaNuovaVO.getCodiceFiscale()))
            {
              if(aziendaNuovaVO.getCodiceFiscale().equalsIgnoreCase(codFiscaleStr))
              {
                rappLegale = true;
                errors.add("generico", new ValidationError("Il rappresentante legale non puo'' essere inserito con piu'' ruoli"));
                countErrori++;
              }            
            }
            
            
            if(!rappLegale)
            {
	            if(!vCodFiscSoggettiInseriti.contains(codFiscaleStr))
	            {            
	              vCodFiscSoggettiInseriti.add(codFiscaleStr);
	            }
	            else
	            {
	              errors.add("generico", new ValidationError("Non e'' possibile inserire piu'' volte lo stesso soggetto"));
	              countErrori++;
	            }
	          }
          }
          
          if (Validator.isEmpty(cognome[i]))
          {
            errors.add("cognome", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
          else
          {
            soggettoAziendaNuovaVO.setCognome(cognome[i].toUpperCase().trim());
          }
          
          if (Validator.isEmpty(nome[i]))
          {
            errors.add("nome", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
          else
          {
            soggettoAziendaNuovaVO.setNome(nome[i].toUpperCase().trim());
          }
			    
			    if (Validator.isEmpty(istatProvincia[i]))
	        {
	          errors.add("istatProvincia", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	        }
	        else
          {
            soggettoAziendaNuovaVO.setIstatProvincia(istatProvincia[i]);
          }
	        
	        if (Validator.isEmpty(istatComune[i]))
	        {
	          errors.add("istatComune", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	        }
	        else
          {
            soggettoAziendaNuovaVO.setIstatComune(istatComune[i]);
          }
          
          if (Validator.isEmpty(indirizzo[i]))
          {
            errors.add("indirizzo", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
          else
          {
            soggettoAziendaNuovaVO.setIndirizzo(indirizzo[i].toUpperCase().trim());
          }
          
          if (Validator.isEmpty(cap[i]))
          {
            errors.add("cap", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
          else
          {
            soggettoAziendaNuovaVO.setCap(cap[i].trim());
          }
          
          if(Validator.isNotEmpty(telefono[i]))
            soggettoAziendaNuovaVO.setTelefono(telefono[i].trim());         
          
          if (Validator.isEmpty(email[i]))
          {
            errors.add("email", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
          else
          {
            if(!Validator.isValidEmail(email[i]))
            {
              errors.add("email", new ValidationError("L''indirizzo di posta inserito non è valido!"));
            }
            else
            {
              soggettoAziendaNuovaVO.setEmail(email[i]);
            }
          }
          
          
          vSoggettoAziendaNuovaMod.add(soggettoAziendaNuovaVO);
	        
	        elencoErrori.add(errors);	    
		    }
		  }
		  
		  if(countErrori > 0) 
		  {
	      request.setAttribute("elencoErrori", elencoErrori);
	    }
	    else
	    {	          
		    try 
		    {
		      if(vSoggettoAziendaNuovaMod.size() > 0)
          {
		        gaaFacadeClient.aggiornaSoggettiAziendaNuovaIscrizione(
		          aziendaNuovaVO, ruoloUtenza.getIdUtente(), vSoggettoAziendaNuovaMod);
		      }
		      else
		      {
		        //Cancello tutto...rimaste vuoto tutti itasti...
            gaaFacadeClient.aggiornaSoggettiAziendaNuovaIscrizione(
               aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
		      }
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoSoggettiCtrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
		      request.setAttribute("messaggioErrore",messaggio);
		      request.setAttribute("pageBack", actionUrl);
		      %>
		        <jsp:forward page="<%= erroreViewUrl %>" />
		      <%
		      return;
		    }
		    
		    
		    //Controllo il tipo dell'azienda in base a questo faccio vdere o meno la pagina associati..
	      TipoTipologiaAziendaVO tipoTipologiaAziendaVO = anagFacadeClient.getTipologiaAzienda(aziendaNuovaVO.getIdTipologiaAzienda());
	      if("S".equalsIgnoreCase(tipoTipologiaAziendaVO.getFlagFormaAssociata()))
	      { 
	        Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
	        if(vActor.contains(SolmrConstants.GESTORE_CAA))
	        {
	          %>
	            <jsp:forward page="<%= pageNextCaa %>"/>
	          <%
	          return;
	        }
	        else
	        {
	          %>
	            <jsp:forward page="<%= pageNext %>"/>
	          <%
	          return;
	        }
	      }
	      else
	      {
	        %>
	          <jsp:forward page="<%= pageNextNoAssociati %>"/>
	        <%
	        return;     
	      }
		     
		    
		  }
		}
		else
		{
		  
		  //Cancello tutto...
		  try 
      {
        gaaFacadeClient.aggiornaSoggettiAziendaNuovaIscrizione(
          aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - newInserimentoSoggettiCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
		
		  //Controllo il tipo dell'azienda in base a questo faccio vdere o meno la pagina associati..
		  TipoTipologiaAziendaVO tipoTipologiaAziendaVO = anagFacadeClient.getTipologiaAzienda(aziendaNuovaVO.getIdTipologiaAzienda());
		  if("S".equalsIgnoreCase(tipoTipologiaAziendaVO.getFlagFormaAssociata()))
		  {	
			  Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
			  if(vActor.contains(SolmrConstants.GESTORE_CAA))
			  {
				  %>
			      <jsp:forward page="<%= pageNextCaa %>"/>
			    <%
			    return;
			  }
			  else
			  {
			    %>
	          <jsp:forward page="<%= pageNext %>"/>
	        <%
	        return;
			  }
			}
			else
			{
			  %>
          <jsp:forward page="<%= pageNextNoAssociati %>"/>
        <%
        return;			
			}
	    
		}
		
		
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoSoggettiUrl %>"/>
  

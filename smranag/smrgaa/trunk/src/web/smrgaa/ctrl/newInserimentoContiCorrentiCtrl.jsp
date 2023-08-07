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

  String iridePageName = "newInserimentoContiCorrentiCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova,vCCAziendaNuova,elencoAllevamentiBdn");

  String newInserimentoCCUrl = "/view/newInserimentoContiCorrentiView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoSoggAssCtrl.jsp";
  String indietroCaaUrl = "/ctrl/newInserimentoSoggAssCaaCtrl.jsp";
  String indietroNoAssociatiUrl = "/ctrl/newInserimentoSoggettiCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoAllegatiCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoContiCorrenti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_CC);
  request.setAttribute("testoHelp", testoHelp);
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  String operazione = request.getParameter("operazione");
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (request.getParameter("regimeInserimentoCC") != null)) 
  {
    //Controllo il tipo dell'azienda in base a questo faccio vdere o meno la pagina associati..
    TipoTipologiaAziendaVO tipoTipologiaAziendaVO = anagFacadeClient.getTipologiaAzienda(aziendaNuovaVO.getIdTipologiaAzienda());
    if("S".equalsIgnoreCase(tipoTipologiaAziendaVO.getFlagFormaAssociata()))
    {    
	    Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
	    if(vActor.contains(SolmrConstants.GESTORE_CAA))
	    {
		    request.getRequestDispatcher(indietroCaaUrl).forward(request, response);
		    return;
		  }
		  else
		  {
		    request.getRequestDispatcher(indietroUrl).forward(request, response);
	      return;
		  }
		}
		else
		{
		  request.getRequestDispatcher(indietroNoAssociatiUrl).forward(request, response);
      return;
		}
  }
  
  
  
  Vector<CCAziendaNuovaVO> vCCAziendaNuova = null;
  if(request.getParameter("regimeInserimentoCC") == null)
  {
    //Prima volta che entro carico le ute da db
    vCCAziendaNuova = gaaFacadeClient.getCCAziendaNuovaIscrizione(idAziendaNuova.longValue());
  }
  else
  {
    vCCAziendaNuova = (Vector<CCAziendaNuovaVO>)session.getAttribute("vCCAziendaNuova");
  }
 
  
  
  String[] iban = request.getParameterValues("iban");
  String[] banca = request.getParameterValues("banca");
  String[] filiale = request.getParameterValues("filiale");
  
  
  
  if((vCCAziendaNuova != null) && Validator.isNotEmpty(iban))
  {
	  for(int i=0;i<vCCAziendaNuova.size();i++)
	  {
	    CCAziendaNuovaVO cCAziendaNuovaVO = vCCAziendaNuova.get(i);	    
      cCAziendaNuovaVO.setIban(iban[i]);	    
	  }
	}
  
  if(Validator.isNotEmpty(operazione) && "cambio".equalsIgnoreCase(operazione))
  {
    //nn fa nulla....
  }
  else if(Validator.isNotEmpty(operazione) && "elimina".equalsIgnoreCase(operazione))      
  {
    String idRigaElim = request.getParameter("idRigaElim");
    vCCAziendaNuova.remove(new Integer(idRigaElim).intValue());
  }
  else if(Validator.isNotEmpty(operazione) && "inserisci".equalsIgnoreCase(operazione))
  {
    if(vCCAziendaNuova == null)
    {
      vCCAziendaNuova = new Vector<CCAziendaNuovaVO>();
    }
    vCCAziendaNuova.add(new CCAziendaNuovaVO());
  }
  else if(Validator.isNotEmpty(operazione) && "aggiorna".equalsIgnoreCase(operazione))
  {
    Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
    int countErrori = 0;
    if(Validator.isNotEmpty(vCCAziendaNuova)
      && (vCCAziendaNuova.size() > 0))
    {
      for(int i=0;i<vCCAziendaNuova.size();i++)
      {    
	      ValidationErrors errors = new ValidationErrors();
	      CCAziendaNuovaVO cCAziendaNuovaVO = vCCAziendaNuova.get(i);
	      
	      if (Validator.isEmpty(cCAziendaNuovaVO.getIban()))
	      {
	        errors.add("iban", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	        countErrori++;
	      }
	      else
	      {
	        String msg = Validator.checkIBAN(cCAziendaNuovaVO.getIban());
	        if(!"OK".equalsIgnoreCase(msg))
	        {
	          errors.add("iban", new ValidationError(msg));
	          countErrori++;
	        }
	        else
	        {
	          String abi = cCAziendaNuovaVO.getIban().substring(5, 10);
	          String cab = cCAziendaNuovaVO.getIban().substring(10, 15);
	          BancaSportelloVO[] aSportello = anagFacadeClient.searchSportello(abi, cab, null);
	          if(aSportello != null)
	          {
	            cCAziendaNuovaVO.setDescBanca(aSportello[0].getDenominazioneBanca());
	            cCAziendaNuovaVO.setDescFiliale(aSportello[0].getDenominazioneSportello());
	          }
	        }       
	      }   
      
        elencoErrori.add(errors);
      }
    
	    if(countErrori > 0) 
	    {
	      request.setAttribute("elencoErrori", elencoErrori);
	    } 
    }
  }
  
  
  session.setAttribute("vCCAziendaNuova", vCCAziendaNuova);
  
  
  
  
  Vector<Integer> vCCVuoti = new Vector<Integer>();
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (request.getParameter("regimeInserimentoCC") != null)) 
  {     
    
    if(Validator.isNotEmpty(vCCAziendaNuova)
      && (vCCAziendaNuova.size() > 0))
    {
    
	    Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
	    int countErrori = 0;
	    
	    //Controlli da effettuare
	    for(int i=0;i<vCCAziendaNuova.size();i++)
	    {
	      ValidationErrors errors = new ValidationErrors();
	      CCAziendaNuovaVO cCAziendaNuovaVO = vCCAziendaNuova.get(i);
	      
	      if(Validator.isNotEmpty(cCAziendaNuovaVO.getIban()))
        {
	      
		      cCAziendaNuovaVO.setIdAziendaNuova(idAziendaNuova);
		      
		      if (Validator.isEmpty(cCAziendaNuovaVO.getIban()))
			    {
			      errors.add("iban", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
			      countErrori++;
			    }
			    else
			    {
			      String msg = Validator.checkIBAN(cCAziendaNuovaVO.getIban());
			      if(!"OK".equalsIgnoreCase(msg))
			      {
			        errors.add("iban", new ValidationError(msg));
			        countErrori++;
			      }
			      else
			      {
			        String abi = cCAziendaNuovaVO.getIban().substring(5, 10);
			        String cab = cCAziendaNuovaVO.getIban().substring(10, 15);
			        BancaSportelloVO[] aSportello = anagFacadeClient.searchSportello(abi, cab, null);
			        if(aSportello != null)
			        {
			          cCAziendaNuovaVO.setDescBanca(aSportello[0].getDenominazioneBanca());
			          cCAziendaNuovaVO.setDescFiliale(aSportello[0].getDenominazioneSportello());
			        }
			      }		    
			    }
			  }
			  else
        {
          if(vCCVuoti == null)
          {
            vCCVuoti = new Vector<Integer>();
          }       
          vCCVuoti.add(new Integer(i));
        }
		    
		    
        
        elencoErrori.add(errors);
		  }
		  
		  if(countErrori > 0) 
		  {
	      request.setAttribute("elencoErrori", elencoErrori);
	    }
	    else
	    {
	      if(Validator.isNotEmpty(vCCVuoti))
        {
          Collections.reverse(vCCVuoti);
          for(int j=0;j<vCCVuoti.size();j++)
          {
            vCCAziendaNuova.remove(vCCVuoti.get(j).intValue());       
          }
        }	       
	          
		    try 
		    {
		      if(vCCAziendaNuova.size() > 0)
          {
            gaaFacadeClient.aggiornaCCAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), vCCAziendaNuova);
          }
          else
          {
            //Cancello tutto...rimaste vuoto tutti i tasti...
            gaaFacadeClient.aggiornaCCAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
          }
		    
		  
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoContiCorrentiCtrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
		      request.setAttribute("messaggioErrore",messaggio);
		      request.setAttribute("pageBack", actionUrl);
		      %>
		        <jsp:forward page="<%= erroreViewUrl %>" />
		      <%
		      return;
		    }
		    
		    %>
		      <jsp:forward page="<%= pageNext %>"/>
		    <%
		    return;		    
		    
		  }
		}
		else
		{
		
		  //Cancello tutto se vuoto
		  try 
      {
        gaaFacadeClient.aggiornaCCAziendaNuovaIscrizione(
          aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - newInserimentoContiCorrentiCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
		
		
		  %>
	      <jsp:forward page="<%= pageNext %>"/>
	    <%
	    return;
		}
		
		
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoCCUrl %>"/>
  

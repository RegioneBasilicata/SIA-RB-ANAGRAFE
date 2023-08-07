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
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoUteCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova,vUteAziendaNuova,elencoAllevamentiBdn");

  String newInserimentoUteUrl = "/view/newInserimentoUteView.jsp";  
  String indietroEntiUrl = "/ctrl/newInserimentoAnagraficaImpreseEntiCtrl.jsp";
  String indietroPrivatiUrl = "/ctrl/newInserimentoAnagraficaPrivatiCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoParticelleCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoUte.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(
        idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  HashMap<String, Vector<ComuneVO>> hProvCom = new HashMap<String, Vector<ComuneVO>>();
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (request.getParameter("regimeInserimentoUte") != null)) 
  { 
    if((aziendaNuovaVO.getCuaa().length() == 11)
      || Validator.isNotEmpty(aziendaNuovaVO.getCodEnte()))
    {   
      request.getRequestDispatcher(indietroEntiUrl).forward(request, response);
      return;
    }
    else
    {
      request.getRequestDispatcher(indietroPrivatiUrl).forward(request, response);
      return;
    }
  }
  
  
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_UTE);
  request.setAttribute("testoHelp", testoHelp);
  
  
  
  
  
  
  
  String operazione = request.getParameter("operazione");
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = null;
  if(request.getParameter("regimeInserimentoUte") == null)
  {
    //Prima volta che entro carico le ute da db
    vUteAziendaNuova = gaaFacadeClient.getUteAziendaNuovaIscrizione(idAziendaNuova.longValue());
  }
  else
  {
    vUteAziendaNuova = (Vector<UteAziendaNuovaVO>)session.getAttribute("vUteAziendaNuova");
  }
  
  Vector<ProvinciaVO> vProvince =  anagFacadeClient.getProvince();
     String[] orderBy = new String[] {SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC};
  request.setAttribute("vProvince", vProvince);
     
  
  String[] denominazione = request.getParameterValues("denominazione");
  String[] indirizzo = request.getParameterValues("indirizzo");
  String[] istatProvincia = request.getParameterValues("istatProvincia");
  String[] istatComune = request.getParameterValues("istatComune");
  String[] cap = request.getParameterValues("cap");
  String[] telefono = request.getParameterValues("telefono");
  String[] fax = request.getParameterValues("fax");
  String[] note = request.getParameterValues("note");
  
  if((vUteAziendaNuova != null))
  {
	  for(int i=0;i<vUteAziendaNuova.size();i++)
	  {
	    UteAziendaNuovaVO uteAziendaNuovaVO = vUteAziendaNuova.get(i);
	    
	    
	    if(request.getParameter("regimeInserimentoUte") != null)
	    {
		    uteAziendaNuovaVO.setIdAziendaNuova(aziendaNuovaVO.getIdAziendaNuova());
		    
		    if(Validator.isNotEmpty(denominazione[i]))
		      denominazione[i] = denominazione[i].toUpperCase();
		    uteAziendaNuovaVO.setDenominazione(denominazione[i]);
		    if(Validator.isNotEmpty(indirizzo[i]))
          indirizzo[i] = indirizzo[i].toUpperCase();
		    uteAziendaNuovaVO.setIndirizzo(indirizzo[i]);
		    uteAziendaNuovaVO.setIstatProvincia(istatProvincia[i]);
		    
		    uteAziendaNuovaVO.setIstatComune(istatComune[i]);
	      uteAziendaNuovaVO.setCap(cap[i]);
	      uteAziendaNuovaVO.setTelefono(telefono[i]);
	      uteAziendaNuovaVO.setFax(fax[i]);
	      uteAziendaNuovaVO.setNote(note[i]);
	    }
	    
	    if(Validator.isNotEmpty(vUteAziendaNuova.get(i).getIstatProvincia()) 
	      && (hProvCom.get(vUteAziendaNuova.get(i).getIstatProvincia()) == null))
	    {
	      String istatProvinciaTmp = vUteAziendaNuova.get(i).getIstatProvincia();
	      hProvCom.put(istatProvinciaTmp, anagFacadeClient.getComuniAttiviByIstatProvincia(istatProvinciaTmp));        
	    }        
	    
	  }
	  
	  request.setAttribute("hProvCom", hProvCom); 
	}
  
  if(Validator.isNotEmpty(operazione) && "cambio".equalsIgnoreCase(operazione))
  {
    //nn fa nulla....
  }
  else if(Validator.isNotEmpty(operazione) && "elimina".equalsIgnoreCase(operazione))      
  {
    String idRigaElim = request.getParameter("idRigaElim");
    UteAziendaNuovaVO uteAziendaNuovaVORm = vUteAziendaNuova.get(new Integer(idRigaElim).intValue());
    if(Validator.isNotEmpty(uteAziendaNuovaVORm.getIdUteAziendaNuova()))
    {
      if(gaaFacadeClient.existsDependenciesUte(uteAziendaNuovaVORm.getIdUteAziendaNuova()))
      {
        ValidationError errorElim = new ValidationError("Non si puo'' eliminare l''ute, potrebbe essere legata a Fabbricati, Particelle o Allevamenti.");
        request.setAttribute("errorElim", errorElim);
        request.setAttribute("idRigaElim", new Integer(idRigaElim));
        %>
          <jsp:forward page="<%= newInserimentoUteUrl %>"/>
        <%
        return;
      }
    }
    
    vUteAziendaNuova.remove(new Integer(idRigaElim).intValue());
  }
  else if(Validator.isNotEmpty(operazione) && "inserisci".equalsIgnoreCase(operazione))
  {
    if(vUteAziendaNuova == null)
    {
      vUteAziendaNuova = new Vector<UteAziendaNuovaVO>();
    }
    vUteAziendaNuova.add(new UteAziendaNuovaVO());
  }
  
  
  session.setAttribute("vUteAziendaNuova", vUteAziendaNuova);
  
  
  
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (request.getParameter("regimeInserimentoUte") != null)) 
  { 
    if(Validator.isEmpty(vUteAziendaNuova)
      || (Validator.isNotEmpty(vUteAziendaNuova) && (vUteAziendaNuova.size() == 0)))
    {
      request.setAttribute("messaggio", "E' obbligatorio inserire un'unità produttiva.");
      %>
        <jsp:forward page="<%= newInserimentoUteUrl %>"/>
      <%
      return;
    }
    
    Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
    int countErrori = 0;
    
    //Controlli da effettuare
    Vector<Integer> vUteVuote = new Vector<Integer>();
    for(int i=0;i<vUteAziendaNuova.size();i++)
    {
      ValidationErrors errors = new ValidationErrors();
      UteAziendaNuovaVO uteAziendaNuovaVO = vUteAziendaNuova.get(i);    
	    /*if (Validator.isEmpty(uteAziendaNuovaVO.getDenominazione()))
	    {
	      errors.add("denominazione", new ValidationError(
	          AnagErrors.ERR_DENOMINAZIONE_OBBLIGATORIA));
	      countErrori++;
	    }*/
	    
	    if (Validator.isNotEmpty(uteAziendaNuovaVO.getIndirizzo())
	      || Validator.isNotEmpty(uteAziendaNuovaVO.getIstatProvincia())
	      || Validator.isNotEmpty(uteAziendaNuovaVO.getIstatComune())
	      || Validator.isNotEmpty(uteAziendaNuovaVO.getCap()))
	    {
	     
		    if (Validator.isEmpty(uteAziendaNuovaVO.getIndirizzo()))
	      {
	        errors.add("indirizzo", new ValidationError(
	            AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	        countErrori++;
	      }
	      
	      if (Validator.isEmpty(uteAziendaNuovaVO.getIstatProvincia()))
	      {
	        errors.add("istatProvincia", new ValidationError(
	            AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	        countErrori++;
	      }
	      
	      if (Validator.isEmpty(uteAziendaNuovaVO.getIstatComune()))
	      {
	        errors.add("istatComune", new ValidationError(
	            AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	        countErrori++;
	      }
	      
	      if (Validator.isEmpty(uteAziendaNuovaVO.getCap()))
	      {
	        errors.add("cap", new ValidationError(
	            AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	        countErrori++;
	      }
	    }
	    else
	    {
	      if(vUteVuote == null)
	      {
	        vUteVuote = new Vector<Integer>();
	      }	      
	      vUteVuote.add(new Integer(i));
	    }
      
      
	    
	    
	    elencoErrori.add(errors);
	  }
	  
	  if(countErrori > 0) 
	  {
      request.setAttribute("elencoErrori", elencoErrori);
    }
    else
    { 
    
      if(Validator.isNotEmpty(vUteVuote))
      {
        Collections.reverse(vUteVuote);
        for(int j=0;j<vUteVuote.size();j++)
        {
          vUteAziendaNuova.remove(vUteVuote.get(j).intValue());       
        }
      }
      
      if(Validator.isEmpty(vUteAziendaNuova)
	      || (Validator.isNotEmpty(vUteAziendaNuova) && (vUteAziendaNuova.size() == 0)))
	    {
	      request.setAttribute("messaggio", "E' obbligatorio inserire un'unità produttiva.");
	      %>
	        <jsp:forward page="<%= newInserimentoUteUrl %>"/>
	      <%
	      return;
	    }
           
	    try 
	    {
	      gaaFacadeClient.aggiornaUteAziendaNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente(), vUteAziendaNuova);
	    }
	    catch(Exception ex)
	    {
	      SolmrLogger.info(this, " - newInserimentoUtetrl.jsp - FINE PAGINA");
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
	  }
  }
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoUteUrl %>"/>
  

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

  String iridePageName = "newInserimentoSoggAssCaaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova");

  String newInserimentoSoggAssCaaUrl = "/view/newInserimentoSoggAssCaaView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoSoggettiCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoContiCorrentiCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoSoggetti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_AA);
  request.setAttribute("testoHelp", testoHelp);
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  String operazione = request.getParameter("operazione");
  String regimeInserimentoSoggAssCaa = request.getParameter("regimeInserimentoSoggAssCaa");
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (regimeInserimentoSoggAssCaa != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  Vector<IntermediarioAnagVO> vIntermediari = null;
  try
  {
    IntermediarioAnagVO intermediario = anagFacadeClient
      .findIntermediarioVOByCodiceEnte(aziendaNuovaVO.getCodEnte());
    if(Validator.isNotEmpty(intermediario)) 
      vIntermediari = anagFacadeClient.findFigliIntermediarioVOById(intermediario.getIdIntermediario());
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoSoggAssCaaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  HashMap<String, AzAssAziendaNuovaVO> hAssAzienda = null;
  if(regimeInserimentoSoggAssCaa == null)
  {
    //Prima volta che entro carico le ute da db
    hAssAzienda = gaaFacadeClient.getAziendeAssociateCaaAziendaNuovaIscrizione(idAziendaNuova.longValue());
    request.setAttribute("hAssAzienda", hAssAzienda);
  }
  
 
  
  
  String[] dataIngresso = request.getParameterValues("dataIngresso");
  String[] codiceEnte = request.getParameterValues("codiceEnte"); 
  Vector<String> vCodiceEnte = new Vector<String>();
  if(Validator.isNotEmpty(codiceEnte))
  {
    for(int i=0;i<codiceEnte.length;i++)
    {
	    if(Validator.isNotEmpty(codiceEnte[i]))
	    {
	      vCodiceEnte.add(codiceEnte[i]);
	    }
	  }
  }
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (regimeInserimentoSoggAssCaa != null)) 
  {     
    //E' stata almeno lasciata una azienda
    if(Validator.isNotEmpty(dataIngresso))
    {
    
	    Vector<AzAssAziendaNuovaVO> vAzAssAziendaNuovaVOMod = new Vector<AzAssAziendaNuovaVO>();  
      Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
      int countErrori = 0;
	    
	    //Controlli da effettuare
	    for(int i=0;i<dataIngresso.length;i++)
      {
	      ValidationErrors errors = new ValidationErrors();
	      
	      AzAssAziendaNuovaVO azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
          
        azAssAziendaNuovaVO.setIdAziendaNuova(idAziendaNuova);
        azAssAziendaNuovaVO.setCodEnte(codiceEnte[i]);
	      
	      if (Validator.isEmpty(dataIngresso[i]))
        {
          errors.add("dataIngresso", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        else
        {
          if(!Validator.isDate(dataIngresso[i]))
          {
            errors.add("dataIngresso", new ValidationError("Il formato della Data inizio ingresso deve essere gg/mm/aaaa."));
            countErrori++;
          }
          else
          {
            Date today = new Date();
            Date dataIngressoDt = DateUtils.parseDate(dataIngresso[i].trim());
            if(dataIngressoDt.after(new Date())) 
            {
              errors.add("dataIngresso", new ValidationError(AnagErrors.ERRORE_KO_DATA_MAX_DATA_CORRENTE));
              countErrori++;
            }
          
            azAssAziendaNuovaVO.setDataIngresso(DateUtils.parseDate(dataIngresso[i].trim()));
          }
        }
		    
		    
        vAzAssAziendaNuovaVOMod.add(azAssAziendaNuovaVO);
        
        elencoErrori.add(errors);
		  }
		  
		  if(countErrori > 0) 
		  {
	      request.setAttribute("elencoErrori", elencoErrori);
	    }
	    else
	    {
	          
	          
		    try 
		    {
		      if(vAzAssAziendaNuovaVOMod.size() > 0)
          {
            gaaFacadeClient.aggiornaAzAssociateCaaAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), vAzAssAziendaNuovaVOMod);
          }
          else
          {
            //Cancello tutto...rimaste vuoto tutti i tasti...
            gaaFacadeClient.aggiornaAzAssociateCaaAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
          }
		    
		  
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoSoggAssCaaCtrl.jsp - FINE PAGINA");
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
        gaaFacadeClient.aggiornaAzAssociateCaaAziendaNuovaIscrizione(
          aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - newInserimentoSoggAssCaaCtrl.jsp - FINE PAGINA");
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
  
  //prima volta che entro
  if(regimeInserimentoSoggAssCaa == null)
  {
    request.setAttribute("vIntermediari", vIntermediari);
  }
  else
  {
    //E' presente almeno un caa a video...
    if(vCodiceEnte.size() > 0)
    {
      Vector<IntermediarioAnagVO> vIntermediariMod = new Vector<IntermediarioAnagVO>();
      //Devo far vedere solo quelli che non sono stati eliminati...
      for(int j=0;j<vIntermediari.size();j++)
      {
        IntermediarioAnagVO intermediarioAnagVO = vIntermediari.get(j);
        if(vCodiceEnte.contains(intermediarioAnagVO.getCodiceFiscale()))
        {
          vIntermediariMod.add(intermediarioAnagVO);
        }
      }
    
      request.setAttribute("vIntermediari", vIntermediariMod);
    }
  
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoSoggAssCaaUrl %>"/>
  

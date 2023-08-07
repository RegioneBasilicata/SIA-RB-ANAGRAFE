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

  String iridePageName = "newInserimentoSoggAssCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova");

  String newInserimentoSoggAssUrl = "/view/newInserimentoSoggAssView.jsp";  
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
  String regimeInserimentoSoggAss = request.getParameter("regimeInserimentoSoggAss");
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (regimeInserimentoSoggAss != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  
  Vector<AzAssAziendaNuovaVO> vAssAzienda = null;
  
  try
  {
    if(regimeInserimentoSoggAss == null)
    {
      //Prima volta che entro carico le ute da db
      vAssAzienda = gaaFacadeClient.getAziendeAssociateAziendaNuovaIscrizione(idAziendaNuova);
      request.setAttribute("vAssAzienda", vAssAzienda);
    }
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoSoggAssCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
 
  
  
  String[] idAziendaEl = request.getParameterValues("idAziendaEl");
  String[] cuaaEl = request.getParameterValues("cuaaEl");
  String[] partitaIvaEl = request.getParameterValues("partitaIvaEl");
  String[] denominazioneEl = request.getParameterValues("denominazioneEl");
  String[] istatComuneEl = request.getParameterValues("istatComuneEl");
  String[] indirizzoEl = request.getParameterValues("indirizzoEl");
  String[] capEl = request.getParameterValues("capEl");
  String[] dataIngresso = request.getParameterValues("dataIngresso"); 
  
  //LO valorizzo per evitare doppi inserimenti
  Vector<Long> vIdAziendeTmp = new Vector<Long>(); 
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (regimeInserimentoSoggAss != null)) 
  {     
    
    //E' stata inserita almeno una azienda
    if(Validator.isNotEmpty(cuaaEl))
    {
        
	    Vector<AzAssAziendaNuovaVO> vAssAziendaMod = new Vector<AzAssAziendaNuovaVO>();  
      Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
      int countErrori = 0;
      
      //Controlli da effettuare
      for(int i=0;i<cuaaEl.length;i++)
      {
        ValidationErrors errors = new ValidationErrors();
        AzAssAziendaNuovaVO azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
          
        azAssAziendaNuovaVO.setIdAziendaNuova(idAziendaNuova);
        
        if(Validator.isNotEmpty(idAziendaEl[i]))
        {
          Long idAziendaElLg = new Long(idAziendaEl[i]);
          azAssAziendaNuovaVO.setIdAziendaAssociata(idAziendaElLg);
          if(!vIdAziendeTmp.contains(idAziendaElLg))
            vIdAziendeTmp.add(idAziendaElLg);
          else
          {
            errors.add("generico", new ValidationError("Attenzione l''azienda e'' gia'' presente tra l''elenco degli associati."));
            countErrori++;          
          }
        }
        if(Validator.isNotEmpty(cuaaEl[i]))
          azAssAziendaNuovaVO.setCuaa(cuaaEl[i]);
        if(Validator.isNotEmpty(partitaIvaEl[i]))
          azAssAziendaNuovaVO.setPartitaIva(partitaIvaEl[i]);
        if(Validator.isNotEmpty(denominazioneEl[i]))
          azAssAziendaNuovaVO.setDenominazione(denominazioneEl[i]);
        if(Validator.isNotEmpty(istatComuneEl[i]))
          azAssAziendaNuovaVO.setIstatComune(istatComuneEl[i]);
        if(Validator.isNotEmpty(indirizzoEl[i]))
          azAssAziendaNuovaVO.setIndirizzo(indirizzoEl[i]);
        if(Validator.isNotEmpty(capEl[i]))
          azAssAziendaNuovaVO.setCap(capEl[i]);
        
        
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
          
            azAssAziendaNuovaVO.setDataIngresso(dataIngressoDt);
          }
        }
	    
	    
		    
        vAssAziendaMod.add(azAssAziendaNuovaVO);
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
		      if(vAssAziendaMod.size() > 0)
          {
            gaaFacadeClient.aggiornaAzAssociateAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), vAssAziendaMod);
          }
          else
          {
            //Cancello tutto...rimaste vuoto tutti i tasti...
            gaaFacadeClient.aggiornaAzAssociateAziendaNuovaIscrizione(
              aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
          }
		    
		  
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoSoggAssCtrl.jsp - FINE PAGINA");
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
        gaaFacadeClient.aggiornaAzAssociateAziendaNuovaIscrizione(
          aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - newInserimentoSoggAssCtrl.jsp - FINE PAGINA");
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
  <jsp:forward page="<%= newInserimentoSoggAssUrl %>"/>
  

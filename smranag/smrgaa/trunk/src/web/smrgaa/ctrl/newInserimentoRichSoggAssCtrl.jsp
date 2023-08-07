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
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

  String iridePageName = "newInserimentoRichSoggAssCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  

  final String errMsg = "Impossibile procedere nella sezione nuova richiesta variazione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String newInserimentoRichSoggAssUrl = "/view/newInserimentoRichSoggAssView.jsp"; 
  String newInserimentoConfermaVariazioneUrl = "/ctrl/newInserimentoConfermaVariazioneCtrl.jsp";   
  String indietroUrl = "../layout/newInserimentoRichiestaVariazione.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String msgErrore = null;
   
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  
  String regimeInserimentoRichSoggAss = request.getParameter("regimeInserimentoRichSoggAss");
  
  
  String idAzienda = null;
  if(Validator.isNotEmpty(regimeInserimentoRichSoggAss))  
    idAzienda = request.getParameter("idAziendaInd");
  else
    idAzienda = request.getParameter("idAzienda");
  
  String operazione = request.getParameter("operazione");
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (regimeInserimentoRichSoggAss != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  
  
  AziendaNuovaVO aziendaNuovaVO = null;
  Vector<AzAssAziendaNuovaVO> vAssAzienda = null;
  AnagAziendaVO anagAziendaVO = null;
  try
  {
  
    String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_VAP_AA);
    request.setAttribute("testoHelp", testoHelp);
  
    anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
    request.setAttribute("anagAziendaVO", anagAziendaVO);
  
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
        new Long(SolmrConstants.RICHIESTA_VAR_SOCI).longValue());
    
    aziendaNuovaVO.setCuaa(anagAziendaVO.getCUAA());
    aziendaNuovaVO.setDenominazione(anagAziendaVO.getDenominazione());      
    request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
    request.setAttribute("valCess", "valCess"); 
  
      
      
    
    //Prima volta che entro carico le aziende ass da db
    if(regimeInserimentoRichSoggAss == null)
    {      
      //Prima volta che entro carico tutte le aziende collegate
      //if(!"S".equalsIgnoreCase(aziendaNuovaVO.getFlagSoloAggiunta()))
      gaaFacadeClient.caricaAziendeAssociateRichiesta(anagAziendaVO.getIdAzienda(), aziendaNuovaVO.getIdRichiestaAzienda(), aziendaNuovaVO.getFlagSoloAggiunta());
            
      vAssAzienda = gaaFacadeClient.getAziendeAssociateAziendaRichVariazione(aziendaNuovaVO.getIdRichiestaAzienda());
      request.setAttribute("vAssAzienda", vAssAzienda);
    }

  
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoRichSoggAssCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("history","history");
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  
 
  
  
  String[] idAziendaEl = request.getParameterValues("idAziendaEl");
  String[] idAssociateAzNuove = request.getParameterValues("idAssociateAzNuove");
  String[] cuaaEl = request.getParameterValues("cuaaEl");
  String[] partitaIvaEl = request.getParameterValues("partitaIvaEl");
  String[] denominazioneEl = request.getParameterValues("denominazioneEl");
  String[] istatComuneEl = request.getParameterValues("istatComuneEl");
  String[] indirizzoEl = request.getParameterValues("indirizzoEl");
  String[] capEl = request.getParameterValues("capEl");
  String[] dataIngresso = request.getParameterValues("dataIngresso"); 
  String[] dataUscita = request.getParameterValues("dataUscita"); 
  
  //LO valorizzo per evitare doppi inserimenti
  Vector<Long> vIdAziendeTmp = new Vector<Long>(); 
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (regimeInserimentoRichSoggAss != null)) 
  {
  
    
    Vector<AzAssAziendaNuovaVO> vAssAziendaDb = null;
    try
    { 
      vAssAziendaDb = gaaFacadeClient.getAziendeAssociateAziendaRichVariazione(aziendaNuovaVO.getIdRichiestaAzienda());
    }
	  catch(Exception ex)
	  {
	    SolmrLogger.info(this, " - newInserimentoRichSoggAssCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("history","history");
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
    
    
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
          
        azAssAziendaNuovaVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
        
        
        if(Validator.isNotEmpty(idAssociateAzNuove[i]))
          azAssAziendaNuovaVO.setIdAssociateAzNuove(new Long(idAssociateAzNuove[i]));
        
        if(Validator.isNotEmpty(vAssAziendaDb)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getIdAssociateAzNuove()))
        {
          for(int j=0;j<vAssAziendaDb.size();j++)
          {
            if(vAssAziendaDb.get(j).getIdAssociateAzNuove().compareTo(azAssAziendaNuovaVO.getIdAssociateAzNuove()) == 0)
            {
              azAssAziendaNuovaVO.setIdAziendaCollegata(vAssAziendaDb.get(j).getIdAziendaCollegata());
              break;
            }        
          }        
        }
        
        
        
        
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
        
        
        boolean flagOkDataIngresso = false;
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
            else
            {
              flagOkDataIngresso = true;
            }
          
            azAssAziendaNuovaVO.setDataIngresso(dataIngressoDt);
          }
        }
        
        if(Validator.isNotEmpty(dataUscita[i])
          && flagOkDataIngresso)
        {
          if(!Validator.isDate(dataUscita[i]))
          {
            errors.add("dataUscita", new ValidationError("Il formato della Data uscita deve essere gg/mm/aaaa."));
            countErrori++;
          }
          else
          {
            Date today = new Date();
            Date dataUscitaDt = DateUtils.parseDate(dataUscita[i].trim());
            if(dataUscitaDt.before(azAssAziendaNuovaVO.getDataIngresso())) 
            {
              errors.add("dataUscita", new ValidationError(AnagErrors.ERRORE_DATA_INGRESSO_POST_DATA_USCITA));
              countErrori++;
            }
          
            azAssAziendaNuovaVO.setDataUscita(DateUtils.parseDate(dataUscita[i].trim()));
          }
        }
        
        
        //Controlli fatti solo se inserimento
        if("S".equalsIgnoreCase(aziendaNuovaVO.getFlagSoloAggiunta()))
	      {
	        if(Validator.isNotEmpty(idAziendaEl[i]))
          {
            Long idAziendaElLg = new Long(idAziendaEl[i]);
            if(anagFacadeClient.findAziendaCollegataByFatherAndSon(anagAziendaVO.getIdAzienda(), idAziendaElLg, null) != null)
            {
              errors.add("generico", new ValidationError("Attenzione l''azienda e'' gia'' presente in anagrafe negli associati."));
              countErrori++;
            }
          }
          else
          {
            if(Validator.isNotEmpty(cuaaEl[i]))
            {
	            if(anagFacadeClient.isSoggettoAssociatoByFatherAndSon(anagAziendaVO.getIdAzienda(), cuaaEl[i], null))
	            {
	              errors.add("generico", new ValidationError("Attenzione l''azienda e'' gia'' presente in anagrafe negli associati."));
	              countErrori++;
	            }
	          }
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
          gaaFacadeClient.aggiornaAzAssociateRichiestaVariazione(
            aziendaNuovaVO, ruoloUtenza.getIdUtente(), vAssAziendaMod);    
        }
        catch(Exception ex)
        {
          SolmrLogger.info(this, " - newInserimentoRichSoggAssCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("history","history");
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        %>
          <jsp:forward page="<%= newInserimentoConfermaVariazioneUrl %>" >
            <jsp:param name="idTipoRichiesta" value="<%=new Integer(SolmrConstants.RICHIESTA_VAR_SOCI).toString() %>" />
          </jsp:forward>
        <%
        return;
		    
		  }
		}
		else
		{		
		  msgErrore = "Deve essere inserita almeno un'azienda."; 
      request.setAttribute("msgErrore", msgErrore); 
		}
		
		
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoRichSoggAssUrl %>"/>
  

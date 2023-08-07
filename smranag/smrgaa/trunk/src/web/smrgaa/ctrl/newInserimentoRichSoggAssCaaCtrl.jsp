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
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>



<%

  String iridePageName = "newInserimentoRichSoggAssCaaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%


  final String errMsg = "Impossibile procedere nella sezione nuova richiesta variazione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String newInserimentoRichSoggAssCaaUrl = "/view/newInserimentoRichSoggAssCaaView.jsp"; 
  String newInserimentoConfermaVariazioneUrl = "/ctrl/newInserimentoConfermaVariazioneCtrl.jsp";   
  String indietroUrl = "../layout/newInserimentoRichiestaVariazione.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String msgErrore = null;
   
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String idAzienda = request.getParameter("idAzienda");  
  String operazione = request.getParameter("operazione");
  String regimeInserimentoRichSoggAssCaa = request.getParameter("regimeInserimentoRichSoggAssCaa");
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (regimeInserimentoRichSoggAssCaa != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  
  
  Vector<IntermediarioAnagVO> vIntermediari = null;
  AziendaNuovaVO aziendaNuovaVO = null;
  HashMap<String, AzAssAziendaNuovaVO> hAssAzienda = null; 
  try
  {
  
    String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_VAP_AA);
    request.setAttribute("testoHelp", testoHelp);
  
    AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
    request.setAttribute("anagAziendaVO", anagAziendaVO);
  
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
        new Long(SolmrConstants.RICHIESTA_VAR_SOCI).longValue());
    
    aziendaNuovaVO.setCuaa(anagAziendaVO.getCUAA());
    aziendaNuovaVO.setDenominazione(anagAziendaVO.getDenominazione());      
    request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
    request.setAttribute("valCess", "valCess"); 
  
    IntermediarioAnagVO intermediario = anagFacadeClient
      .findIntermediarioVOByIdAzienda(new Long(idAzienda));
      
    vIntermediari = anagFacadeClient.findFigliIntermediarioAziendaVOById(intermediario.getIdIntermediario());
    //  request.setAttribute("vIntermediari", vIntermediari);
      
      
    if(regimeInserimentoRichSoggAssCaa == null)
    {
      //Prima volta che entro carico tutte le aziende collegate
      gaaFacadeClient.caricaAziendeAssociateCaaRichiesta(anagAziendaVO.getIdAzienda(), aziendaNuovaVO.getIdRichiestaAzienda());
    }
    
    hAssAzienda = gaaFacadeClient.getAziendeAssociateCaaAziendaRichVariazione(aziendaNuovaVO.getIdRichiestaAzienda());
    
    //Prima volta che entro carico le aziende ass da db
    if(regimeInserimentoRichSoggAssCaa == null)
    {
      request.setAttribute("hAssAzienda", hAssAzienda);
    }

  
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoRichSoggAssCaaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("history","history");
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  String[] dataIngresso = request.getParameterValues("dataIngresso");
  String[] dataUscita = request.getParameterValues("dataUscita");
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
    && (regimeInserimentoRichSoggAssCaa != null)) 
  {     
    //E' stata almeno lasciata una azienda
    if(Validator.isNotEmpty(vIntermediari)
      && (vIntermediari.size() > 0))
    {  
    
	    Vector<AzAssAziendaNuovaVO> vAzAssAziendaNuovaVOMod = new Vector<AzAssAziendaNuovaVO>();  
      Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
      int countErrori = 0;
      
      int i=0;
      for(int j=0;j<vIntermediari.size();j++)
      {        
        IntermediarioAnagVO intermediarioAnagModVO = vIntermediari.get(j);
        AzAssAziendaNuovaVO azAssAziendaNuovaVOMod = new AzAssAziendaNuovaVO();
        ValidationErrors errors = new ValidationErrors();
        azAssAziendaNuovaVOMod.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
        azAssAziendaNuovaVOMod.setIdAziendaAssociata(intermediarioAnagModVO.getExtIdAzienda());
        
        if((hAssAzienda != null) && hAssAzienda.get(intermediarioAnagModVO.getCodiceFiscale()) != null)
        {
          AzAssAziendaNuovaVO azAssAziendaNuovaVODb = hAssAzienda.get(intermediarioAnagModVO.getCodiceFiscale());
          //quelle già presenti su anagrafe
          if(Validator.isNotEmpty(azAssAziendaNuovaVODb.getIdAziendaCollegata()))
          {
            azAssAziendaNuovaVOMod.setIdAssociateAzNuove(azAssAziendaNuovaVODb.getIdAssociateAzNuove());
            azAssAziendaNuovaVOMod.setIdAziendaCollegata(azAssAziendaNuovaVODb.getIdAziendaCollegata());
          }
        } 
        
        
        //presente a video
        if(vCodiceEnte.contains(intermediarioAnagModVO.getCodiceFiscale()))
        {        
	        azAssAziendaNuovaVOMod.setCodEnte(codiceEnte[i]);	        
	        
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
	            errors.add("dataIngresso", new ValidationError("Il formato della Data ingresso deve essere gg/mm/aaaa."));
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
	          
	            azAssAziendaNuovaVOMod.setDataIngresso(DateUtils.parseDate(dataIngresso[i].trim()));
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
	            if(dataUscitaDt.before(azAssAziendaNuovaVOMod.getDataIngresso())) 
	            {
	              errors.add("dataUscita", new ValidationError(AnagErrors.ERRORE_DATA_INGRESSO_POST_DATA_USCITA));
	              countErrori++;
	            }
	          
	            azAssAziendaNuovaVOMod.setDataUscita(DateUtils.parseDate(dataUscita[i].trim()));
	          }
	        }
	        
	        
          i++;
        }
        else
        {
          if(Validator.isNotEmpty(azAssAziendaNuovaVOMod.getIdAziendaCollegata()))
          {
            azAssAziendaNuovaVOMod.setFlagEliminato("S");
          }                  
        }
	    
	    
		    
		    
        vAzAssAziendaNuovaVOMod.add(azAssAziendaNuovaVOMod);
        
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
		      gaaFacadeClient.aggiornaAzAssociateCaaRichiestaVariazione(
            aziendaNuovaVO, ruoloUtenza.getIdUtente(), vAzAssAziendaNuovaVOMod);	  
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoRichSoggAssCaaCtrl.jsp - FINE PAGINA");
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
  
  //prima volta che entro
  if(regimeInserimentoRichSoggAssCaa == null)
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
  <jsp:forward page="<%= newInserimentoRichSoggAssCaaUrl %>"/>
  

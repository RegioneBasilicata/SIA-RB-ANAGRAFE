<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaMacchineAgricoleVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.TipoGenereMacchinaGaaVO" %>
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

  String motoriAgricoliIncaricoUrl = "/view/motori_agricoli_incaricoView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String action = "../layout/motori_agricoli_incarico.htm";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione macchine agricole. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, "filtriRicercaMacchineAgricoleVO");

 	String iridePageName = "motori_agricoli_incaricoCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	String storico = request.getParameter("storico");
 	String operazione = request.getParameter("operazione");
 	String regimeElencoMotoriAgricoli = request.getParameter("regimeElencoMotoriAgricoli");
 	
 	
 	String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
  
  String pathToFollow = (String)session.getAttribute("pathToFollow");
  String percorsoErrori = null;
  if (pathToFollow.equalsIgnoreCase("rupar"))
  {
    percorsoErrori = "/ris/css/agricoltura/im/";
  }
  else
  {
    percorsoErrori = "/css/agricoltura/im/";
  }
 	
 	
 	
 	
 	
 	if("attenderePrego".equalsIgnoreCase(operazione)) 
  {     
    request.setAttribute("action", action);
    operazione = "allinea";
    request.setAttribute("operazione", operazione);
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
    return;
  }
 	
 	
 	
 	
 	
 	if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("allinea"))
 	{
 	  try 
    {
 	    gaaFacadeClient.popolaTabelleMacchineAgricoleConServizio(anagAziendaVO.getIdAzienda());
 	    WebUtils.removeUselessFilter(session,null);
 	    //ricaricoi per prendermi il valore data_aggiornamento_uma  	   
      anagAziendaVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
 	    session.setAttribute("anagAziendaVO", anagAziendaVO);
 	  }
 	  catch(SolmrException se) 
	  {
	    SolmrLogger.info(this, " - motori_agricoli_incaricoCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_IMPORTA_DATI_UMA+".\n"+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
 	  catch(Exception e) 
	  {
	    SolmrLogger.info(this, " - motori_agricoli_incaricoCtrl.jsp - FINE PAGINA");
	    SolmrLogger.error(this, " -- Exception in motori_agricoli_incaricoCtrl ="+e.getMessage());
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_IMPORTA_DATI_UMA+".\n"+e.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
 	}
 	
 	
 	

 	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
 	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
 	// in modo che venga sempre effettuato
 	try 
 	{
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
   	request.setAttribute("statoAzienda", se);
  }
  
  FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO 
    = (FiltriRicercaMacchineAgricoleVO) session.getAttribute("filtriRicercaMacchineAgricoleVO");
  if (filtriRicercaMacchineAgricoleVO == null)
  {
    filtriRicercaMacchineAgricoleVO = new FiltriRicercaMacchineAgricoleVO(); 
  }
  
  session.setAttribute("filtriRicercaMacchineAgricoleVO", filtriRicercaMacchineAgricoleVO);

  Date parametroScadenzaImpoMacc = null;
  try 
  {
    parametroScadenzaImpoMacc = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_SCADENZA_IMPO_MACC);
    request.setAttribute("parametroScadenzaImpoMacc", parametroScadenzaImpoMacc);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - motori_agricoli_incaricoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_SCADENZA_IMPO_MACC+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  try 
  {
    Vector<TipoGenereMacchinaGaaVO> vTipoGenereMacchina 
      =  gaaFacadeClient.getElencoTipoGenereMacchina();
    request.setAttribute("vTipoGenereMacchina", vTipoGenereMacchina);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - motori_agricoli_incaricoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_GENERE_MACCHINA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }


 	try 
 	{
   	
   	AnagFacadeClient anagClient = new AnagFacadeClient();
    DittaUMAVO dittaUmaVO = anagClient.getDittaUmaByIdAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("dittaUmaVO", dittaUmaVO);
    
    
    filtriRicercaMacchineAgricoleVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    
    
    if(Validator.isNotEmpty(regimeElencoMotoriAgricoli))
    {
	    String idGenereMacchinaStr = (String)request.getParameter("idGenereMacchina");
	    
	    if(Validator.isNotEmpty(idGenereMacchinaStr))
	      filtriRicercaMacchineAgricoleVO.setIdGenereMacchina(new Long(idGenereMacchinaStr));
	    else
	      filtriRicercaMacchineAgricoleVO.setIdGenereMacchina(null);
	      
	      
	    if(Validator.isNotEmpty(storico))
	    {
	      filtriRicercaMacchineAgricoleVO.setStorico(true);
	    }
	    else
	    {
	      filtriRicercaMacchineAgricoleVO.setStorico(false);
	    }
	  }
      
    
    
    long ids[] = gaaFacadeClient.ricercaIdPossessoMacchina(filtriRicercaMacchineAgricoleVO);
    if (ids != null)
    {
      String paginaCorrenteRichiesta = "";
      //Setto a 1 la pagina sto facendo la ricerca
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("aggiorna"))
      {
        paginaCorrenteRichiesta = "1";
      }
      else
      {
        paginaCorrenteRichiesta = request.getParameter("paginaCorrente");
        if (paginaCorrenteRichiesta==null)
        {
          String mantieniPagina=request.getParameter("mantieniPagina");
          if ("true".equals(mantieniPagina))
          {
            paginaCorrenteRichiesta=String.valueOf(filtriRicercaMacchineAgricoleVO.getPaginaCorrente());
          }
        }
      }
      PaginazioneUtils pager = PaginazioneUtils.newInstance(ids,
          SolmrConstants.NUM_RIGHE_PER_PAGINA_AZIENDE_COLLEGATE, filtriRicercaMacchineAgricoleVO.getPaginaCorrente(),
          paginaCorrenteRichiesta, "paginaCorrente");
      long idsForPage[] = pager.getIdForCurrentPage(true);
      if (idsForPage != null && idsForPage.length > 0)
      {
        RigaRicercaMacchineAgricoleVO righe[] = gaaFacadeClient
            .getRigheRicercaMacchineAgricoleById(idsForPage);
            
            
        for(int i=0;i<righe.length;i++)
        {
          if("S".equalsIgnoreCase(righe[i].getFlagValida()))
          {
            righe[i].setImgConfermata(percorsoErrori + "/"+ imok);
          }
          else 
          {
            righe[i].setImgConfermata(percorsoErrori + "/"+ imko);             
          }        
        }
           
                 
        pager.setRighe(righe);
      }
      filtriRicercaMacchineAgricoleVO.setPaginaCorrente(pager.getPaginaCorrente());
      session.setAttribute("filtriRicercaMacchineAgricoleVO", filtriRicercaMacchineAgricoleVO);
      request.setAttribute("pager", pager);    
      
    }
    else
    {
      request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_MACCHINE_AGRICOLE);
    }
   	
 	}
 	catch(SolmrException se) 
 	{
   	SolmrLogger.info(this, " - motori_agricoli_incaricoCtrl.jsp - FINE PAGINA");
    String messaggioErrore = (String)AnagErrors.ERRORE_KO_RIC_MACCHINE_AGRICOLE;
    String messaggio = messaggioErrore +": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
 	}
 	
 	%>
   	<jsp:forward page="<%=motoriAgricoliIncaricoUrl %>" />



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
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String importaMacchinaAgricolaUrl = "/view/importaMacchinaAgricolaView.jsp";
  String importaMacchinaAgricolaModUrl = "/ctrl/importaMacchinaAgricolaModCtrl.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione importa macchine agricole. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	

 	String iridePageName = "motori_agricoli_incaricoCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	String operazione = request.getParameter("operazione");
 	String regimeImportaMacchinaAgricola = request.getParameter("regimeImportaMacchinaAgricola");
 	String regimeImportaMacchinaAgricolaMod = request.getParameter("regimeImportaMacchinaAgricolaMod");
 	ValidationErrors errors = null;
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	
 	
 	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  
  //resetto solo s eprima volta che entro e non arrivo dall'indietro della modifica
  if(Validator.isEmpty(regimeImportaMacchinaAgricola)
    && Validator.isEmpty(regimeImportaMacchinaAgricolaMod))
  {
    WebUtils.removeUselessFilter(session, null);
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

 	try 
 	{
 	
 	  if(Validator.isNotEmpty(operazione) && "ricerca".equalsIgnoreCase(operazione))
 	  {    
      String cuaa = request.getParameter("cuaa");
      //non arrivo dal tasto indietro della modifca importa
      if(Validator.isEmpty(cuaa) && Validator.isEmpty(regimeImportaMacchinaAgricolaMod))
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"ricerca", AnagErrors.ERR_CAMPO_OBBLIGATORIO);      
      }
      else
      {
      
        filtriRicercaMacchineAgricoleVO.setIdAzienda(anagAziendaVO.getIdAzienda());
        if(Validator.isNotEmpty(cuaa))        
		      filtriRicercaMacchineAgricoleVO.setCuaa(cuaa.trim());		    
		    filtriRicercaMacchineAgricoleVO.setCodiceRuolo(ruoloUtenza.getCodiceRuolo());
		      
		    long ids[] = gaaFacadeClient.ricercaIdPossessoMacchinaImport(filtriRicercaMacchineAgricoleVO);
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
		  
		  
		  
		  if(errors !=null) 
      {
        request.setAttribute("errors", errors);
      }
		  
		  
	  }
   	
 	}
 	catch(SolmrException se) 
 	{
   	SolmrLogger.info(this, " - importaMacchinaAgricolaCtrl.jsp - FINE PAGINA");
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
   	<jsp:forward page="<%=importaMacchinaAgricolaUrl %>" />



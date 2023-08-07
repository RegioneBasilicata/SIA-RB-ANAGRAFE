<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
	private Vector<PersonaFisicaVO> getSoggetti(PageContext pc,Boolean storico) 
	  throws SolmrException, Exception 
	{
  	pc.removeAttribute("v_soggetti", PageContext.SESSION_SCOPE);
   	AnagAziendaVO anagVO = (AnagAziendaVO)pc.getAttribute("anagAziendaVO",PageContext.SESSION_SCOPE);
   	Vector<PersonaFisicaVO> v_soggetti = null;
   	AnagFacadeClient client = new AnagFacadeClient();

   	v_soggetti = client.getSoggetti(anagVO.getIdAzienda(),storico);
   	if(v_soggetti!= null) 
   	{
   		pc.setAttribute("v_soggetti", v_soggetti, PageContext.SESSION_SCOPE);
    }
    return v_soggetti;
  }
%>

<%

	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);


  String iridePageName = "contitolariCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%

  String url = "/view/contitolariView.jsp";

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	session.removeAttribute("personaVO");

  ValidationError error = null;
  ValidationErrors errors = null;

  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  try 
  {
   	client.checkStatoAzienda(anagVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
   	request.setAttribute("statoAzienda", se);
  }

  session.removeAttribute("inserimento");

  if(request.getParameter("operazione")!= null 
    && request.getParameter("operazione").equals("nuovo")) 
  {
   	session.setAttribute("inserimento", "inserimento");
   	url = "/layout/contitolari_new.htm";
  }
  // L'utente ha selezionato la funzionalità di modifica dall'elenco dei soggetti
  // collegati
  else if(request.getParameter("operazione")!= null 
    && request.getParameter("operazione").equals("modifica")) 
  {
   	Long idContitolare = new Long(request.getParameter("radiobutton"));
   	request.setAttribute("idContitolare", idContitolare);
   	url = "/layout/contitolari_mod.htm";
  }
  // L'utente ha selezionato la funzionalità di elimina dall'elenco dei soggetti
  // collegati
  else if(request.getParameter("operazione")!= null 
    && request.getParameter("operazione").equals("elimina")) 
  {
   	// Recupero l'id_contitolare
   	Long idContitolare = new Long(request.getParameter("radiobutton"));

   	// Recupero la persona fisica e controllo che non abbia ruolo uguale a titolare/rappresentante legale
   	// altrimenti avviso l'utente che non è possibile eliminare un soggetto con questo ruolo.
   	PersonaFisicaVO personaFisicaVO = null;
   	try 
   	{
    	personaFisicaVO = client.getDettaglioSoggettoByIdContitolare(idContitolare);
    }
    catch(SolmrException se) 
    {
    	error = new ValidationError(se.getMessage());
    	errors = new ValidationErrors();
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
    }
    if(personaFisicaVO.getIdRuolo()
      .compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_TITOL_RAPPR_LEG"))) == 0) 
    {
    	error = new ValidationError((String)AnagErrors.get("ERR_TITOLARE_NO_ELIMINABILE"));
    	errors = new ValidationErrors();
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
    }
    // Se il soggetto collegato selezionato ha data fine ruolo valorizzata
    // impedisco l'eliminazione
    if(Validator.isNotEmpty(personaFisicaVO.getDataFineRuolo())) 
    {
    	error = new ValidationError((String)AnagErrors.get("ERR_SOGGETTO_RUOLO_CESSATO"));
    	errors = new ValidationErrors();
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
    }
    session.setAttribute("idContitolare", idContitolare);
    url = "/layout/confirmDelSoggetto.htm";
  }
  // L'utente ha selezionato la funzionalità di dettaglio dall'elenco dei soggetti
  // collegati
  else if(request.getParameter("operazione")!= null 
    && request.getParameter("operazione").equals("dettaglio")) 
  {
   	// Recupero l'id contitolare dall'elenco dei soggetti
   	Long idContitolare = new Long(request.getParameter("radiobutton"));

   	// Ricerco i dati in dettaglio della persona a partire dall'id_contitolare
   	PersonaFisicaVO personaFisicaDettaglioVO = null;
   	TesserinoFitoSanitarioVO tesserinoVO = null;
   	try 
   	{
    	personaFisicaDettaglioVO = client.getDettaglioSoggettoByIdContitolare(idContitolare);
    	tesserinoVO = client.getTesserinoFitoSanitario(personaFisicaDettaglioVO.getCodiceFiscale());
    }
    catch (SolmrException sex) 
    {
    	error = new ValidationError(sex.getMessage());
    	errors = new ValidationErrors();
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    personaFisicaDettaglioVO.setIdContitolare(idContitolare);
    request.setAttribute("personaVO", personaFisicaDettaglioVO);
    request.setAttribute("tesserinoVO", tesserinoVO);
    
    
    
    
    
    url = "/view/contitolariDettView.jsp";
  }
  else if(request.getParameter("operazione")!=null
    && request.getParameter("operazione").equals("aggiorna")) 
  {
  	try 
  	{
    	if("si".equals(request.getParameter("storico"))) 
    	{
      	getSoggetti(pageContext,new Boolean(true));
      }
      else 
      {
      	getSoggetti(pageContext,new Boolean(false));
      }
    }
    catch(SolmrException sex) 
    {
    	error = new ValidationError(sex.getMessage());
    	errors = new ValidationErrors();
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
    }
  }
  else 
  {
  	try 
  	{
    	getSoggetti(pageContext,new Boolean(false));
    }
    catch(SolmrException sex) 
    {
    	error = new ValidationError(sex.getMessage());
    	errors = new ValidationErrors();
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
    }
  }
%>
<jsp:forward page="<%=url%>"/>

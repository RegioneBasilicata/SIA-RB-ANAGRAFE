<%@ page language="java" contentType="text/html" isErrorPage="true"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%!
  public static final String VIEW    = "/view/ricercaVariazioniVisioneView.jsp";
  public static final String RICERCA = "../layout/ricercaVariazioni.htm";
  public static final String ELENCO = "../layout/elencoVariazioni.htm";
%>

<%

  String iridePageName = "ricercaVariazioniVisioneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO = (FiltriRicercaVariazioniAziendaliVO) session
      .getAttribute("filtriRicercaVariazioniAziendaliVO");
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");   
  
  Vector elencoIdPresaVisione=(Vector)request.getAttribute("elencoIdPresaVisione"); 
  
  String conferma=request.getParameter("conferma");
  if (Validator.isNotEmpty(conferma))
  {
    //l'utente ha scelto di confermare l'operazione
    elencoIdPresaVisione=new Vector();
    HashMap numVariazioniSelezionate = (HashMap)session.getAttribute("numVariazioniSelezionate");
    Set elencoKeys = numVariazioniSelezionate.keySet();
    Iterator iteraKey = elencoKeys.iterator();
    while(iteraKey.hasNext()) 
    {
      String selezioni[]= (String[])numVariazioniSelezionate.get((String)iteraKey.next());
      if(selezioni != null) 
        for(int a = 0; a < selezioni.length; a++)
          elencoIdPresaVisione.add(new Long(selezioni[a]));
    }
    
    gaaFacadeClient.insertVisioneVariazione(elencoIdPresaVisione, ruoloUtenza);
    session.removeAttribute("numVariazioniSelezionate");
    response.sendRedirect(RICERCA);
    return;
  }
  
  String annulla=request.getParameter("annulla");
  if (Validator.isNotEmpty(annulla))
  {
    //l'utente ha scelto di annullare l'operazione quindi ritorno alal pagina di ricerca
    session.removeAttribute("numVariazioniSelezionate");
    response.sendRedirect(ELENCO);
    return;
  }
  
  RigaRicercaVariazioniAziendaliVO righe[] = gaaFacadeClient.getRigheVariazioniVisione(elencoIdPresaVisione,filtriRicercaVariazioniAziendaliVO);
  request.setAttribute("righe", righe);
%>
<jsp:forward page="<%=VIEW%>" />



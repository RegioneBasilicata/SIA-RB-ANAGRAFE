<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
	public final static String VIEW = "../view/manodoperaView.jsp";
%>

<%
	// Pulisco la sessione dai filtri di altre sezioni
  //String noRemove = "idPianoRiferimentoManodopera";
	WebUtils.removeUselessFilter(session, null);

	String iridePageName = "manodoperaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  ValidationError error = null;
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  if(errors == null) 
  {
    errors = new ValidationErrors();
  }
  
  
  String pianoRiferimento = request.getParameter("idDichiarazioneConsistenza");  
  Long idPianoRiferimento = null;
  if(!Validator.isNotEmpty(pianoRiferimento)) 
  {
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);    
    //idPianoRiferimento = new Long("-1");
  }
  else 
  {
    idPianoRiferimento = Long.decode(pianoRiferimento);
  }

	try 
  {
  	//pulizia session
  	if(session.getAttribute("common") != null) 
    		session.removeAttribute("common");

  	//Click sulla voce di menù "Manodopera"
 	 	Vector<FrmManodoperaVO> elencoManodopera = null;
  	ManodoperaVO manodoperaVO = null;
	  try 
    {
  		manodoperaVO = new ManodoperaVO();
  		manodoperaVO.setIdAziendaLong(anagAziendaVO.getIdAzienda());
  		elencoManodopera = anagFacadeClient.getManodoperaByPianoRifererimento(manodoperaVO,idPianoRiferimento);
  	}
  	catch(SolmrException se) 
    {
  		errors.add("error", new ValidationError(se.getMessage()));
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(VIEW).forward(request, response);
  		return;
  	}

  	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  	// in modo che venga sempre effettuato
  	try 
    {
      anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  	}
  	catch(SolmrException se) {
    		request.setAttribute("statoAzienda", se);
  	}

  	request.setAttribute("elencoManodopera", elencoManodopera);
  	%>
    		<jsp:forward page="<%= VIEW %>"/>
  	<%
	}
	catch(Exception e) {
  	if(e instanceof SolmrException) {
    		setError(request, e.getMessage());
  	}
  	else {
    		e.printStackTrace();
    		setError(request, "Si è verificato un errore di sistema");
  	}
  	%>
    		<jsp:forward page="<%=VIEW%>" />
  	<%
	}
%>

<%!
	private void setError(HttpServletRequest request, String msg) {
    	ValidationErrors errors = new ValidationErrors();
    	errors.add("error", new ValidationError(msg));
    	request.setAttribute("errors", errors);
  	}
%>

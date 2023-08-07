<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@page import="it.csi.solmr.client.anag.*"%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	String iridePageName = "ripristinaDichConfermaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String ripristinaDichConfermaUrl = "/view/ripristinaDichConfermaView.jsp";
	String attenderePregoUrl = "/view/attenderePregoView.jsp";
	String actionUrl = "../layout/ripristinaDichConferma.htm";
	String erroreViewUrl = "/view/erroreView.jsp";
	String terreniParticellareElencoCtrlUrl = "/ctrl/terreniParticellareElencoCtrl.jsp";
	String dichiarazioneConsistenzaActionUrl = "../layout/dichiarazioneConsistenza.htm";
	
	// Recupero l'id dichiarazione consistenza selezionato
	Long idDichiarazioneConsistenza = Long.decode(request.getParameter("radiobutton"));
	request.setAttribute("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
	String operazione = request.getParameter("operazione");
	ValidationErrors errors = new ValidationErrors();
	ValidationError error = null;
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
	
	if(Validator.isNotEmpty(operazione)) 
	{
		// L'utente ha selezionato il pulsante conferma
		if(operazione.equalsIgnoreCase("conferma")) 
		{
			// Verifico che l'utente abbia effettivamente preso visione delle informazioni
			// riportate
			boolean isConferma = Validator.isNotEmpty(request.getParameter("confermaOperazione"));
			if(!isConferma) 
			{
				error = new ValidationError(AnagErrors.ERR_NO_CONFERMA_RIPRISTINO_DICHIARAZIONE);
				errors.add("confermaOperazione", error);
		    request.setAttribute("errors", errors);
		    request.getRequestDispatcher(ripristinaDichConfermaUrl).forward(request, response);
		    return;
			}
			else 
			{
			  try 
	      {
	        if(gaaFacadeClient.isIstanzaAttiva(anagAziendaVO.getIdAzienda().longValue()))
	        {
	          error = new ValidationError(AnagErrors.ERR_ISTANZA_ESAME_ATTIVA);
	          errors.add("confermaOperazione", error);
	          request.setAttribute("errors", errors);
	          request.getRequestDispatcher(ripristinaDichConfermaUrl).forward(request, response);
	          return;
	        }
	      }
	      catch(SolmrException se) 
	      {
	        String messaggio = se.getMessage();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", dichiarazioneConsistenzaActionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	      }
			
				// Se l'utente ha preso visione vado alla pagina di attesa per lanciare
				// il ripristino della dichiarazione
				request.setAttribute("action", actionUrl);
		   	String operation = "ripristinaDichiarazione";
		   	request.setAttribute("operazione", operation);
		   	request.setAttribute("dichiarazioneConsistenza", idDichiarazioneConsistenza.toString());
		   	%>
					<jsp:forward page= "<%= attenderePregoUrl %>" />
		   	<%
			}
		}
		// L'utente arriva dalla pagina di "attendere prego" per il ripristino della
		// dichiarazione
		if(operazione.equalsIgnoreCase("ripristinaDichiarazione")) 
		{
			// Richiamo la procedura PL-SQL che si occupa di ripristinare il piano
			// di riferimento
    	try 
    	{
      	anagFacadeClient.ripristinaPianoRiferimento(idDichiarazioneConsistenza, ruoloUtenza.getIdUtente());
	    }
      catch(SolmrException se) 
      {
        String messaggio = se.getMessage();
	     	request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", dichiarazioneConsistenzaActionUrl);
        %>
         	<jsp:forward page="<%= erroreViewUrl %>" />
        <%
      }
      // Vado alla pagina di ricerca elenco terreni
      %>
      	<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" />
      <%
		}
	}
  
  //Verifico che la dichiarazione di consistenza non sia antecedente al 2007 
  //(deve essere DATA_INSERIMENTO_DICHIARAZIONE>=2007)
  
  Long annoDichiarazione=anagFacadeClient.getAnnoDichiarazione(idDichiarazioneConsistenza);
  
  if (annoDichiarazione==null)
  {
    //la dichiarazione è antecedente al 2007
    request.setAttribute("messaggioErrore",AnagErrors.ERRORE_RIPRISTINO_DICH_ANT_2007);
    request.setAttribute("pageBack", dichiarazioneConsistenzaActionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  else
  {
    //Si può ripristinare solo ultima di ogni anno legata ad una pratica RPU / PSR 
    //oppure non legata ad alcuna pratica a patto che nn sia l'ultima 
    //dell'anno in corso.
    Long idProcedimento=anagFacadeClient.getProcedimento(anagAziendaVO.getIdAzienda(),idDichiarazioneConsistenza);
    if (idProcedimento==null)
    {
      //dichiarazione non legata ad alcuna pratica
      long idDichiarazioneUltima=anagFacadeClient.getLastIdDichiazioneConsistenza(anagAziendaVO.getIdAzienda(), null);
      //Posso ripristinare la dichiarazione a patto che non sia l'ultima
      if (idDichiarazioneUltima==idDichiarazioneConsistenza.longValue())
      {
        request.setAttribute("messaggioErrore",AnagErrors.ERRORE_RIPRISTINO_DICH);
        request.setAttribute("pageBack", dichiarazioneConsistenzaActionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
       } 
    }
    else
    {
      if (idProcedimento.longValue() == SolmrConstants.ID_PROCEDIMENTO_PSR
            || idProcedimento.longValue() == SolmrConstants.ID_PROCEDIMENTO_RPU)
      {
        //pratica legata a PSR o RPU
        long idDichiarazioneUltimaAnno=anagFacadeClient.getLastIdDichiazioneConsistenza(anagAziendaVO.getIdAzienda(), annoDichiarazione);
        //posso ripristinare la dichiarazione solo se è legata all'ultima dell'anno
        //in corso
        if (idDichiarazioneUltimaAnno!=idDichiarazioneConsistenza.longValue())
        {
          request.setAttribute("messaggioErrore",AnagErrors.ERRORE_RIPRISTINO_DICH);
          request.setAttribute("pageBack", dichiarazioneConsistenzaActionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
      }
      else
      {
        //pratica legata a qualche pratica ma non a PSR o RPU
        request.setAttribute("messaggioErrore",AnagErrors.ERRORE_RIPRISTINO_DICH);
        request.setAttribute("pageBack", dichiarazioneConsistenzaActionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
  }
	
	// Vado alla pagina di richiesta conferma del ripristino della dichiarazione di
	// consistenza
	%>
		<jsp:forward page="<%=ripristinaDichConfermaUrl%>" />
	<%

%>

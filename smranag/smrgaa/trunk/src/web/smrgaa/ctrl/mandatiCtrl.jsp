<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "mandatiCtrl.jsp";
  boolean forZona = false;
  %>
     <%@include file = "/include/autorizzazione.inc" %>
  <%

  String mandatiUrl = "/view/mandatiView.jsp";
  String excelUrl = "/servlet/ExcelBuilderServlet";

  	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  	UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  	Vector elencoMandati = null;
  	Vector errori = new Vector();
    ValidationErrors errors = new ValidationErrors();
  	
  	// Recupero il tipo ricerca selezionato dall'utente
  	forZona = request.getParameter("suddividi")!=null;
  	request.setAttribute("forZona", new Boolean(forZona));
	try {	
	    /*String dal = "01/01/"+DateUtils.getCurrentYear();
	    Date dataDal = DateUtils.parseDate(dal);
	    Date dataAl = DateUtils.parseDate(DateUtils.getCurrent());
	    */
	    Date dataDal = null;
	    Date dataAl = null;  
	    String dal = request.getParameter("dal");
	    String al = request.getParameter("al");

        if(!Validator.isNotEmpty(dal)) {	
          dal = DateUtils.getCurrentDay() + "/" + DateUtils.getCurrentMonth() + "/" + DateUtils.getCurrentYear();
        }

        	if(!Validator.validateDateF(dal)) {
	            ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_DAL_ERRATA"));
	            errors.add("dal", error);
        	}
        	else{		
			        if(!Validator.isNotEmpty(al)) {
			        	al = DateUtils.getCurrentDay() + "/" + DateUtils.getCurrentMonth() + "/" + DateUtils.getCurrentYear();
			          }
			        	  if(!Validator.validateDateF(al)) {
			              	ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_DAL_ERRATA"));
			              	errors.add("al", error);
			          		}
			          	  else{
			          		  if(DateUtils.parseDate(al).before(DateUtils.parseDate(dal))) {
					              ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_AL_MINORE_DATA_DAL"));
					              errors.add("al", error);
			          		  }
			                  else{
			                	  	dataDal = DateUtils.parseDate(dal);
			                	  	dataAl = DateUtils.parseDate(al);
			                  }
			          		}

        	}

        // Se si sono verificati degli errori non effettuo la ricerca e li visualizzo a video
        if(errors.size() > 0) {
	          request.setAttribute("errors", errors);
	          request.getRequestDispatcher(mandatiUrl).forward(request, response);
	          return;
        }

		    elencoMandati = anagFacadeClient.getMandatiByUtente(utenteAbilitazioni, forZona, dataDal, dataAl);

  	}
	catch(SolmrException se) {
		request.setAttribute("messaggioErrore", se.getMessage());
		%>
			<jsp:forward page= "<%= mandatiUrl %>" />
		<%
  	}
	//Metto in request l'elenco dei mandati trovato
  	request.setAttribute("elencoMandati",elencoMandati);
  	
	// Operazione Scarica in Excel del report
  	if(Validator.isNotEmpty(request.getParameter("operazione")) && request.getParameter("operazione").equalsIgnoreCase("scarica")) {
	  	elencoMandati = (Vector)request.getAttribute("elencoMandati");

	    // Metto il vettore in request x la servlet dedicata allo scarico in excel
	   	request.setAttribute("elenco", elencoMandati);   
	   	request.setAttribute("fileName","RiepilogoMandatiEValidazioni");
	   	
	   	if(ruoloUtenza.isUtenteIntermediario())
	   		request.setAttribute("foglioExcel", "elencoMandatiValidazioni");
	   	else{
	   		if(!forZona)
	   			request.setAttribute("foglioExcel", "elencoMandatiValidazioniNotIntermediario");
	   		else
	   			request.setAttribute("foglioExcel", "elencoMandatiValidazioniNotIntermediarioForProvincia");
	   	}	
	   	
   		// Vado alla servlet che si occupa di generare il file excel
   		%>
      		<jsp:forward page="<%=excelUrl%>" />
   		<%
  	}
  	// Vado all'elenco dei mandati
	%>
  		<jsp:forward page= "<%= mandatiUrl %>" />
	<%

%>

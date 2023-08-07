<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/attenderePrego.htm");
  	Htmpl htmpl = new Htmpl(layout);

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

  	AnagFacadeClient client = new AnagFacadeClient();

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);

  	String action = (String)request.getAttribute("action");
  	String operazione = (String)request.getAttribute("operazione");
  	String CUAAselezionato = (String)request.getAttribute("CUAAselezionato");
  	String idDichiarazioneConsistenza = (String)request.getAttribute("dichiarazioneConsistenza");

  	htmpl.set("action", action);
  	htmpl.set("operazione", operazione);
  	if(CUAAselezionato != null) {
    	htmpl.set("CUAAselezionato", CUAAselezionato);
  	}
  	if(idDichiarazioneConsistenza != null) {
  		htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
  	}
  	
  	//Per anagraficaVisuraCamerale INIZIO
  	if(request.getAttribute("denominazioneAttAAEP") != null)
  	{
  	  htmpl.set("denominazioneAttAAEP", (String)request.getAttribute("denominazioneAttAAEP"));
  	}
  	if(request.getAttribute("partitaIVAAttAAEP") != null)
    {
      htmpl.set("partitaIVAAttAAEP", (String)request.getAttribute("partitaIVAAttAAEP"));
    }
    if(request.getAttribute("provinciaREAAttAAEP") != null)
    {
      htmpl.set("provinciaREAAttAAEP", (String)request.getAttribute("provinciaREAAttAAEP"));
    }
    if(request.getAttribute("numeroREAAttAAEP") != null)
    {
      htmpl.set("numeroREAAttAAEP", (String)request.getAttribute("numeroREAAttAAEP"));
    }
    if(request.getAttribute("annoIscrizioneAttAAEP") != null)
    {
      htmpl.set("annoIscrizioneAttAAEP", (String)request.getAttribute("annoIscrizioneAttAAEP"));
    }
    if(request.getAttribute("numeroRegistroImpreseAttAAEP") != null)
    {
      htmpl.set("numeroRegistroImpreseAttAAEP", (String)request.getAttribute("numeroRegistroImpreseAttAAEP"));
    }
    if(request.getAttribute("pecAttAAEP") != null)
    {
      htmpl.set("pecAttAAEP", (String)request.getAttribute("pecAttAAEP"));
    }
    if(request.getAttribute("sedeLegaleAttAAEP") != null)
    {
      htmpl.set("sedeLegaleAttAAEP", (String)request.getAttribute("sedeLegaleAttAAEP"));
    }
    if(request.getAttribute("titolareRappresentanteAttAAEP") != null)
    {
      htmpl.set("titolareRappresentanteAttAAEP", (String)request.getAttribute("titolareRappresentanteAttAAEP"));
    }
    if(request.getAttribute("formaGiuridicaAttAAEP") != null)
    {
      htmpl.set("formaGiuridicaAttAAEP", (String)request.getAttribute("formaGiuridicaAttAAEP"));
    }
    if(request.getAttribute("descrizioneAtecoAttAAEP") != null)
    {
      htmpl.set("descrizioneAtecoAttAAEP", (String)request.getAttribute("descrizioneAtecoAttAAEP"));
    }
    if(request.getAttribute("sezioneAttAAEP") != null)
    {
      htmpl.set("sezioneAttAAEP", (String)request.getAttribute("sezioneAttAAEP"));
    }
    if(request.getAttribute("descrizioneAtecoSecAttAAEP") != null)
    {
      htmpl.set("descrizioneAtecoSecAttAAEP", (String)request.getAttribute("descrizioneAtecoSecAttAAEP"));
    }
    if(request.getAttribute("dataIscrizioneREAAttAAEP") != null)
    {
      htmpl.set("dataIscrizioneREAAttAAEP", (String)request.getAttribute("dataIscrizioneREAAttAAEP"));
    }
    if(request.getAttribute("dataIscrizioneREAAttAAEP") != null)
    {
      htmpl.set("dataIscrizioneREAAttAAEP", (String)request.getAttribute("dataIscrizioneREAAttAAEP"));
    }
    if(request.getAttribute("dataCancellazioneREAAttAAEP") != null)
    {
      htmpl.set("dataCancellazioneREAAttAAEP", (String)request.getAttribute("dataCancellazioneREAAttAAEP"));
    } 
    if(request.getAttribute("dataIscrizioneRIAttAAEP") != null)
    {
      htmpl.set("dataIscrizioneRIAttAAEP", (String)request.getAttribute("dataIscrizioneRIAttAAEP"));
    } 
      
    //FINE anagraficaVisuraCamerale

%>

<%= htmpl.text()%>

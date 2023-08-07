  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>


<%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/newInserimentoAzienda.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);

  //Valorizza segnaposto errore htmpl
  HtmplUtil.setErrors(htmpl, errors, request, application);
  
  //String cuaaIndietro = (String)request.getAttribute("cuaaIndietro");
  String cuaa = request.getParameter("cuaaInserimento");
  String partitaIva = request.getParameter("partitaIvaInserimento");
  String cuaaCaa = request.getParameter("cuaaCaaInserimento");
  String partitaIvaCaa = request.getParameter("partitaIvaCaaInserimento");
  //potrebbe essere che arrivo dal tasto indietro
  String cuaaIndietro = request.getParameter("cuaa");
  String codEnteIndietro = request.getParameter("codEnte");
  //Cuaa eventuale se clicco su nuova iscrizione da sezioni successive...
  String cuaaSalto = (String)request.getAttribute("cuaaAziendaNuova");
  if(Validator.isEmpty(cuaa)
    && Validator.isEmpty(partitaIva)
    && Validator.isEmpty(cuaaCaa)
    && (Validator.isNotEmpty(cuaaIndietro)
      || Validator.isNotEmpty(codEnteIndietro)))
  {
    if(Validator.isNotEmpty(codEnteIndietro))
    {
      cuaaCaa = codEnteIndietro;
      partitaIvaCaa = cuaaIndietro;
      cuaa = "";
      partitaIva = "";
    }
    else
    {    
	    if(cuaaIndietro.length() == 11)
	    {
	      partitaIva = cuaaIndietro;
	      cuaa = "";
	      cuaaCaa = "";
	    }
	    else
	    {
	      cuaa = cuaaIndietro;
	      partitaIva = "";
	      cuaaCaa = "";
	    }
	  }  
  }
  else if (Validator.isNotEmpty(cuaaSalto))
  {
    if(cuaaSalto.length() == 11)
    {
      partitaIva = cuaaSalto;
      cuaa = "";
      cuaaCaa = "";
    }
    else if(cuaaSalto.length() == 9)
    {
      cuaaCaa = cuaaSalto;
      //!?!?!?
      //partitaIvaCaa = "";
      //!!!!!!
      partitaIva = "";
      cuaa = "";
    }
    else
    {
      cuaa = cuaaSalto;
      partitaIva = "";
      cuaaCaa = "";
    }  
  }  
  
 
  Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
  if(vActor.contains(SolmrConstants.GESTORE_CAA))
  {
    htmpl.newBlock("blkGestoreIntermediario");
    htmpl.set("blkGestoreIntermediario.cuaaCaaInserimento", cuaaCaa);
    htmpl.set("blkGestoreIntermediario.partitaIvaCaaInserimento", partitaIvaCaa);
  }
  else
  {
    htmpl.newBlock("blkNormale");
    htmpl.set("blkNormale.cuaaInserimento", cuaa);
    htmpl.set("blkNormale.partitaIvaInserimento", partitaIva);
  }
  
  
  
  String messaggioErrore = (String)request.getAttribute("msgErrore");
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }


%>
<%= htmpl.text()%>

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
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/errore.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String msgErrore=(String)request.getParameter("errore");
  if (msgErrore==null)
    msgErrore=(String)request.getAttribute("errore");

  String pageBack=(String)request.getParameter("pageBack");

  if (pageBack==null) {
    pageBack=(String)request.getAttribute("pageBack");
  }
  
  if (pageBack==null) {
    pageBack=(String)session.getAttribute("pageBack");
  }

  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  if(!Validator.isNotEmpty(messaggioErrore)) {
    messaggioErrore = (String)session.getAttribute("messaggioErrore");
  }
  if(Validator.isEmpty(messaggioErrore)) {
    msgErrore = request.getParameter("messaggioErrore");
  }
  if(Validator.isNotEmpty(messaggioErrore)) {
    msgErrore = messaggioErrore;
  }

  String chiudi = (String)session.getAttribute("chiudi");
  session.removeAttribute("chiudi");
  if(Validator.isNotEmpty(chiudi)) {
    htmpl.newBlock("blkChiudi");
    htmpl.set("exception",msgErrore);
  }
  else 
  {
    htmpl.set("exception",msgErrore);
    
    //se settato il parametro history in request sul tasto indietro verrà eseguita la history(-1)
    if(request.getAttribute("history") !=null)
    {
      htmpl.newBlock("blkHistory");
    }
    else if(request.getAttribute("history2") !=null)
    {
      htmpl.newBlock("blkHistory2");
    }
    else
    {
      htmpl.newBlock("blkBack");
      htmpl.set("blkBack.backPage",pageBack);
    }
  }

  out.print(htmpl.text());

%>

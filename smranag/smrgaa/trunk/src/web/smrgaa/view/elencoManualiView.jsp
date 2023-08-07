<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page  import="it.csi.smranag.smrgaa.dto.manuali.ManualeVO" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/elencoManuali.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

	 // Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	 
	 
	Vector<ManualeVO> vManuali = (Vector<ManualeVO>)request.getAttribute("vManuali");
	 
	Long idSezioneTmp = new Long(0);
	if(vManuali != null)
	{
	  htmpl.newBlock("blkPulsanti");
	  for(int i=0;i<vManuali.size();i++)
	  {
	    htmpl.newBlock("blkManuale");
	    ManualeVO manualeVO = vManuali.get(i);
	    if(manualeVO.getIdSezione() != null)
	    {
	      if(idSezioneTmp.compareTo(manualeVO.getIdSezione()) !=0)
	      {
	        htmpl.newBlock("blkManuale.blkSezione");
	        htmpl.set("blkManuale.blkSezione.descSezione", manualeVO.getDescSezione());
	      }
	      idSezioneTmp = manualeVO.getIdSezione();
	    }
	    
	    htmpl.set("blkManuale.idManuale", ""+manualeVO.getIdManuale());
      htmpl.set("blkManuale.descManuale", manualeVO.getDescManuale());	     
	     
	  }
	}
	else
	{
	  htmpl.newBlock("blkNoManuali");
	}
	
	 
	
	// Recupero eventuale messaggio di errore generico
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	if(Validator.isNotEmpty(messaggioErrore)) {
	  htmpl.newBlock("blkErrore");
	  htmpl.set("blkErrore.messaggio", messaggioErrore);
	}


%>

<%= htmpl.text()%>

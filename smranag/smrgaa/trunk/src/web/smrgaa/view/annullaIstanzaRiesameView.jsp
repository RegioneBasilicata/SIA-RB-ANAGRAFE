<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/annullaIstanzaRiesame.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
 	
 	// Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
 	
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	
 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	
 	String anno = (String)request.getAttribute("anno");   
 	htmpl.set("anno", anno);
 	
 	Vector<ParticellaIstanzaRiesameVO> vListParticelle = (Vector<ParticellaIstanzaRiesameVO>)request.getAttribute("vListParticelle");
  if(vListParticelle != null)
  {
    htmpl.newBlock("blkDati");
    for(int i=0;i<vListParticelle.size();i++)
    {
       htmpl.newBlock("blkDati.blkElencoParticelle");
       htmpl.set("blkDati.blkElencoParticelle.idIstanzaRiesame", ""+vListParticelle.get(i).getIdIstanzaRiesame());
       htmpl.set("blkDati.blkElencoParticelle.descComune", vListParticelle.get(i).getDescComune());
       htmpl.set("blkDati.blkElencoParticelle.sezione", vListParticelle.get(i).getSezione());
       htmpl.set("blkDati.blkElencoParticelle.foglio", vListParticelle.get(i).getFoglio());
       htmpl.set("blkDati.blkElencoParticelle.particella", vListParticelle.get(i).getParticella());
       htmpl.set("blkDati.blkElencoParticelle.subalterno", vListParticelle.get(i).getSubalterno());
       htmpl.set("blkDati.blkElencoParticelle.annoIstanza", ""+vListParticelle.get(i).getAnno());
       htmpl.set("blkDati.blkElencoParticelle.dataRichiesta", DateUtils.formatDateNotNull(vListParticelle.get(i).getDataRichiesta()));
       htmpl.set("blkDati.blkElencoParticelle.dataEvasione", DateUtils.formatDateNotNull(vListParticelle.get(i).getDataEvasione()));
    }
  
  }
  
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore, null);
  }
 	
 	
 	
 	

%>
<%= htmpl.text()%>

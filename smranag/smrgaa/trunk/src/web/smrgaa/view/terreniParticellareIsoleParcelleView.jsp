<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="java.util.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareIsoleParcelle.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

	Vector<TipoSettoreAbacoVO> vTipoSettoreAbaco = (Vector<TipoSettoreAbacoVO>)request.getAttribute("vTipoSettoreAbaco");
  String codSettore = request.getParameter("codSettore");
  String arrivoDa = request.getParameter("arrivoDa");
  htmpl.set("arrivoDa", arrivoDa);
  if("terreni".equalsIgnoreCase(arrivoDa))
  {
    htmpl.set("linkArrivoDa", "Terreni");
  }
  else
  {
    htmpl.set("linkArrivoDa", "Unit&agrave Vitate", null);
  }
  
 	
	
	// Combo unità produttive
	if(vTipoSettoreAbaco != null) 
  {
		for(int i=0;i<vTipoSettoreAbaco.size();i++)
    {
			TipoSettoreAbacoVO tipoSettoreAbaco = (TipoSettoreAbacoVO)vTipoSettoreAbaco.get(i);
			htmpl.newBlock("blkTipoSettoreAbaco");
			htmpl.set("blkTipoSettoreAbaco.codSettore", ""+tipoSettoreAbaco.getCodSettore());
			String descrizione = "["+tipoSettoreAbaco.getDesc_breve()+"]"+" "+tipoSettoreAbaco.getDescrizione();
			htmpl.set("blkTipoSettoreAbaco.descrizione", descrizione);
      
      if(Validator.isNotEmpty(codSettore)) 
      {
        if(new Long(codSettore).longValue() == tipoSettoreAbaco.getCodSettore()) {
          htmpl.set("blkTipoSettoreAbaco.selected", "selected=\"selected\"");
          htmpl.set("codSettoreSel", ""+tipoSettoreAbaco.getCodSettore());
        }
      }
      else
      {
        //Metto di default il piano corrente
        if(new Long(100).longValue() == tipoSettoreAbaco.getCodSettore()) {
          htmpl.set("blkTipoSettoreAbaco.selected", "selected=\"selected\"");
          htmpl.set("codSettoreSel", ""+tipoSettoreAbaco.getCodSettore());
        }
      }
      
		}
	}
	
	

%>
<%= htmpl.text()%>


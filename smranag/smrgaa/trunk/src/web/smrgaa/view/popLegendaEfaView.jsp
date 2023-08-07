<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.Date"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popLegendaEfa.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	
	Vector<TipoEfaVO> vTipoEfa = (Vector<TipoEfaVO>)request.getAttribute("vTipoEfa");	
  
  if(Validator.isNotEmpty(vTipoEfa))
  {
    for(int i=0;i<vTipoEfa.size();i++)
    {
      htmpl.newBlock("blkElencoEfa");
      TipoEfaVO tipoEfaVO = vTipoEfa.get(i);
      if(tipoEfaVO.isPadre())
      {
        htmpl.newBlock("blkElencoEfa.blkPrimoEfa");
        htmpl.set("blkElencoEfa.blkPrimoEfa.descTipoEfa", tipoEfaVO.getDescrizioneTipoEfa());
      }
      else
      {
        htmpl.newBlock("blkElencoEfa.blkSecondoEfa");
        htmpl.set("blkElencoEfa.blkSecondoEfa.descTipoEfa", tipoEfaVO.getDescrizioneTipoEfa());
        htmpl.set("blkElencoEfa.blkSecondoEfa.descUnitaMisura", tipoEfaVO.getDescUnitaMisura());
        htmpl.set("blkElencoEfa.blkSecondoEfa.fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
        htmpl.set("blkElencoEfa.blkSecondoEfa.fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
      }    
      
    }
  
  }

 	


%>
<%= htmpl.text()%>

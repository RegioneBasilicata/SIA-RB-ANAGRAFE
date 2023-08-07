<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.comune.*"%>
<%@ page import="java.util.*"%>

<%

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popPraticheDichiarazione.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	Vector elencoPratiche = (Vector)request.getAttribute("elencoPratiche");
 	Hashtable elencoAmmCompetenza = (Hashtable)request.getAttribute("elencoAmmCompetenza");
 	
 	if(elencoPratiche != null && elencoPratiche.size() > 0) {
 		
  		for(int i = 0; i < elencoPratiche.size(); i++) {
  			TemporaneaPraticaAziendaVO temporaneaPraticaAziendaVO = (TemporaneaPraticaAziendaVO)elencoPratiche.elementAt(i);
  			AmmCompetenzaVO ammCompetenzaVO = null;
  			htmpl.newBlock("blkElencoPratiche");
      		htmpl.set("blkElencoPratiche.descProcedimento",temporaneaPraticaAziendaVO.getDescProcedimento());
     	 	htmpl.set("blkElencoPratiche.descrizione",temporaneaPraticaAziendaVO.getDescrizione());
      		htmpl.set("blkElencoPratiche.numeroPratica",temporaneaPraticaAziendaVO.getNumeroPratica());
     	 	htmpl.set("blkElencoPratiche.descrizioneStato",temporaneaPraticaAziendaVO.getDescrizioneStato());
      		htmpl.set("blkElencoPratiche.dataValiditaStato",temporaneaPraticaAziendaVO.getDataValiditaStato());
      		if(temporaneaPraticaAziendaVO.getExtIdAmmCompetenza() != null) {
  				ammCompetenzaVO = (AmmCompetenzaVO)elencoAmmCompetenza.get(temporaneaPraticaAziendaVO.getExtIdAmmCompetenza().toString());
      			htmpl.set("blkElencoPratiche.amministrazioneCompetenza", ammCompetenzaVO.getDescrizione());
      		}
    	}
  	}

%>
<%= htmpl.text()%>

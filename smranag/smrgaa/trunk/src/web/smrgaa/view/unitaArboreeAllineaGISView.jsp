<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeAllineaGIS.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 	
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	Vector<IsolaParcellaVO> elencoIsoleParcelleVO = (Vector<IsolaParcellaVO>)request.getAttribute("elencoIsoleParcelleVO");
	  
	String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
	
  StringProcessor jssp = new JavaScriptStringProcessor();
	
	if(Validator.isNotEmpty(messaggioErrore)) {
		htmpl.newBlock("blkErrore");
		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
	}
	else 
  {
    if(Validator.isNotEmpty(elencoIsoleParcelleVO))
    {
      htmpl.set("idDichConsAllineaUV", ""+elencoIsoleParcelleVO.get(0).getIdDichiarazioneConsistenza());
    
		  htmpl.newBlock("blkDati");
      htmpl.set("blkDati.totaleRighe",""+elencoIsoleParcelleVO.size());
      
  		for(int i = 0; i < elencoIsoleParcelleVO.size(); i++) 
      {      
  			htmpl.newBlock("blkDati.blkElenco");
        IsolaParcellaVO isoPVO = elencoIsoleParcelleVO.get(i);
  			htmpl.set("blkDati.blkElenco.idIsola", ""+isoPVO.getIdIlo());
  			htmpl.set("blkDati.blkElenco.parcella", ""+isoPVO.getIdIsolaParcella());
  			htmpl.set("blkDati.blkElenco.supVitata", StringUtils
          .parseSuperficieFieldBigDecimal(isoPVO.getSupVitata()));
        htmpl.set("blkDati.blkElenco.supParcella", StringUtils
          .parseSuperficieFieldBigDecimal(isoPVO.getSupParcella()));
        htmpl.set("blkDati.blkElenco.numeroUV", ""+isoPVO.getNumeroUV());
  			
        //TOLLERANZA
        if(Validator.isNotEmpty(isoPVO.getTolleranza()) 
          && (isoPVO.getTolleranza().compareTo(SolmrConstants.IN_TOLLERANZA) == 0)) 
        {
          htmpl.newBlock("blkDati.blkElenco.blkPieno");
          htmpl.set("blkDati.blkElenco.blkPieno.idIsolaParcella", ""+isoPVO.getIdIsolaParcella());
          htmpl.set("blkDati.blkElenco.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ISO_TOLLERANZA_OK, ""}), null);
        }
        else if(Validator.isNotEmpty(isoPVO.getTolleranza()) 
          && (isoPVO.getTolleranza().compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0))  
        {
          htmpl.newBlock("blkDati.blkElenco.blkVuoto");
          htmpl.set("blkDati.blkElenco.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ISO_TOLLERANZA_UVDOPPIE_KO, ""}), null);              
        }
        else if(Validator.isNotEmpty(isoPVO.getTolleranza())
          && (isoPVO.getTolleranza().compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0))
        {
          htmpl.newBlock("blkDati.blkElenco.blkVuoto");
          htmpl.set("blkDati.blkElenco.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ISO_TOLLERANZA_FUORI_KO, ""}), null);
        }
        else
        {
          htmpl.newBlock("blkDati.blkElenco.blkVuoto");
          htmpl.set("blkDati.blkElenco.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ISO_TOLLERANZA_PLSQL_KO, ""}), null);
        }
        
        
  		}
    }
	}

%>
<%= htmpl.text()%>


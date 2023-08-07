<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/titoli.htm");
	Htmpl htmpl = new Htmpl(layout);

	%>
   		<%@include file = "/view/remoteInclude.inc" %>
	<%

	Vector elencoSianTitoliAggregatiVO = (Vector)request.getAttribute("elencoSianTitoliAggregatiVO");
	Vector elencoDescOrigineTitolo = (Vector)request.getAttribute("elencoDescOrigineTitolo");
	Vector elencoDescMovimento = (Vector)request.getAttribute("elencoDescMovimento");

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	if(Validator.isNotEmpty(messaggioErrore)) {
  		htmpl.newBlock("blkErrore");
  		htmpl.set("blkErrore.messaggio", messaggioErrore);
	}
	else {
  		htmpl.newBlock("blkTitoli");
  		if(elencoSianTitoliAggregatiVO != null) {
                    long quantitaTotale=0;
                    double valoreTitoli=0;
                    double superficiTitoli=0;
    		for(int i = 0; i < elencoSianTitoliAggregatiVO.size(); i++) {
      			htmpl.newBlock("blkTitoli.blkElencoTitoli");
		      	SianTitoliAggregatiVO sianTitoliAggregatiVO = (SianTitoliAggregatiVO)elencoSianTitoliAggregatiVO.elementAt(i);
                        try
                        {
                          quantitaTotale+=Long.parseLong(sianTitoliAggregatiVO.getQuantitaTitoli());
                        }
                        catch(Exception e) {}
                        try
                        {
                          valoreTitoli+=Double.parseDouble(sianTitoliAggregatiVO.getValoreTitoli())*Long.parseLong(sianTitoliAggregatiVO.getQuantitaTitoli());
                        }
                        catch(Exception e) {}
                        try
                        {
                          superficiTitoli+=Double.parseDouble(sianTitoliAggregatiVO.getSuperficie())*Long.parseLong(sianTitoliAggregatiVO.getQuantitaTitoli());
                        }
                        catch(Exception e) {}
  		      	htmpl.set("blkTitoli.blkElencoTitoli.descrizioneTipoTitolo", sianTitoliAggregatiVO.getDescrizioneTipoTitolo());
			    htmpl.set("blkTitoli.blkElencoTitoli.quantitaTitoli", sianTitoliAggregatiVO.getQuantitaTitoli());
		      	htmpl.set("blkTitoli.blkElencoTitoli.valoreTitoli", StringUtils.parseDoubleField(sianTitoliAggregatiVO.getValoreTitoli()));
		      	htmpl.set("blkTitoli.blkElencoTitoli.superficie", StringUtils.parseDoubleField(sianTitoliAggregatiVO.getSuperficie()));
		      	htmpl.set("blkTitoli.blkElencoTitoli.ubaObbligatori", StringUtils.parseDoubleField(sianTitoliAggregatiVO.getUbaObbligatori()));
		      	htmpl.set("blkTitoli.blkElencoTitoli.primoIdentificativo", sianTitoliAggregatiVO.getPrimoIdentificativo());
		      	htmpl.set("blkTitoli.blkElencoTitoli.ultimoIdentificativo", sianTitoliAggregatiVO.getUltimoIdentificativo());
		      	htmpl.set("blkTitoli.blkElencoTitoli.codiceOrigine", sianTitoliAggregatiVO.getCodiceOrigine());
		      	htmpl.set("blkTitoli.blkElencoTitoli.CUAAOrigine", sianTitoliAggregatiVO.getCuaaOrigine());
		      	htmpl.set("blkTitoli.blkElencoTitoli.codiceMovimento", sianTitoliAggregatiVO.getCodiceMovimento());
		      	htmpl.set("blkTitoli.blkElencoTitoli.CUAASoccidario", sianTitoliAggregatiVO.getCUAASoccidario());
    		}
                htmpl.set("blkTitoli.quantitaTotaleTot",""+ quantitaTotale);
                htmpl.set("blkTitoli.valoreTitoliTot", StringUtils.parseDoubleField(valoreTitoli+""));
                htmpl.set("blkTitoli.superficiTitoliTot", StringUtils.parseDoubleField(superficiTitoli+""));
   	 		// LEGENDA
    		if(elencoDescOrigineTitolo != null) {
      			for(int i = 0; i < elencoDescOrigineTitolo.size(); i++) {
        			htmpl.newBlock("blkTitoli.blkLegendaTitoli");
        			StringcodeDescription stringCode = (StringcodeDescription)elencoDescOrigineTitolo.elementAt(i);
        			htmpl.set("blkTitoli.blkLegendaTitoli.codiceOrigine", stringCode.getCode());
        			htmpl.set("blkTitoli.blkLegendaTitoli.descrizioneCodiceOrigine", stringCode.getDescription());
      			}
    		}
    		if(elencoDescMovimento != null) {
      			for(int i = 0; i < elencoDescMovimento.size(); i++) {
        			htmpl.newBlock("blkTitoli.blkLegendaMovimento");
        			StringcodeDescription stringCode = (StringcodeDescription)elencoDescMovimento.elementAt(i);
        			htmpl.set("blkTitoli.blkLegendaMovimento.codiceMovim", stringCode.getCode());
        			htmpl.set("blkTitoli.blkLegendaMovimento.descrizioneCodiceMovim", stringCode.getDescription());
      			}
    		}
  		}
	}


%>
<%= htmpl.text()%>

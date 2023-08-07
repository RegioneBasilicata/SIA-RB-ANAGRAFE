<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.servizi.vitiserv.DirittoGaaVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/dirittiVitati.htm");
	Htmpl htmpl = new Htmpl(layout);

	%>
   		<%@include file = "/view/remoteInclude.inc" %>
	<%

	DirittoGaaVO[] diritti = (DirittoGaaVO[])request.getAttribute("elencoDiritti");
	

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	if(Validator.isNotEmpty(messaggioErrore)) 
  {
		htmpl.newBlock("blkErrore");
		htmpl.set("blkErrore.messaggio", messaggioErrore);
	}
	else 
  {
		if(diritti != null && diritti.length!=0) 
    {
      htmpl.newBlock("blkDirittoVitato");
      
      if ("true".equals(request.getParameter("flagScaduti")))
        htmpl.set("blkDirittoVitato.checkedFlagScaduti","checked=\"checked\"");
      
  		for(int i = 0; i < diritti.length; i++) 
      {
        if (diritti[i].getDiritto()!=null)
        {
          htmpl.newBlock("blkDirittoVitato.blkElencoDiritti");
          
          if (diritti[i].isScaduto())  //se il diritto è scsduto evidenzio di rosso la riga
            htmpl.newBlock("blkDirittoVitato.blkElencoDiritti.blkRed");
          else  
            htmpl.newBlock("blkDirittoVitato.blkElencoDiritti.blkBlack");
          
          htmpl.set("blkDirittoVitato.blkElencoDiritti.descOrigine",StringUtils.checkNull(diritti[i].getDiritto().getDescOrigine()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.siglaProvinciaOrigine",StringUtils.checkNull(diritti[i].getSiglaProvinciaOrigine()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.descAmmCompetenzaReimpianto",StringUtils.checkNull(diritti[i].getDescAmmCompetenzaReimpianto()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.varieta",StringUtils.checkNull(diritti[i].getVarieta()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.vino",StringUtils.checkNull(diritti[i].getVino()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.numeroDiritto",StringUtils.checkNull(diritti[i].getDiritto().getNumeroDritto()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.descStatoDiritto",StringUtils.checkNull(diritti[i].getDiritto().getDescrStatoDiritto()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.autorizzazione_estirpazione",StringUtils.checkNull(diritti[i].getDiritto().getAutorizzazione_estirpazione()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.dataEstirpazione",DateUtils.formatDateNotNull(diritti[i].getDiritto().getDataEstirpazione()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.dataScadenza",DateUtils.formatDateNotNull(diritti[i].getDiritto().getDataScadenza()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.superficieIniziale",Formatter.formatDouble4ForBigDecimal(diritti[i].getDiritto().getSuperficieIniziale()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.superficieUtilizzata",Formatter.formatDouble4ForBigDecimal(diritti[i].getDiritto().getSuperficieUtilizzata()));
          htmpl.set("blkDirittoVitato.blkElencoDiritti.superficieTrasferita",Formatter.formatDouble4ForBigDecimal(diritti[i].getDiritto().getSuperficieTrasferita()));
          BigDecimal supResidua=new BigDecimal(0);
          supResidua=supResidua.add(diritti[i].getDiritto().getSuperficieIniziale());
          supResidua=supResidua.subtract(diritti[i].getDiritto().getSuperficieUtilizzata());
          supResidua=supResidua.subtract(diritti[i].getDiritto().getSuperficieTrasferita());
          htmpl.set("blkDirittoVitato.blkElencoDiritti.supResidua",Formatter.formatDouble4ForBigDecimal(supResidua));
        }
  		}
		}
	}


%>
<%= htmpl.text()%>

<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.math.*"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeAssociaPercUsoElegg.htm");
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
	StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
	Vector<ValidationErrors> elencoErrori = (Vector<ValidationErrors>)request.getAttribute("elencoErrori");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	String regimeUvAssocElegg = request.getParameter("regimeUvAssocElegg");
	
  
  
	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
  
  
	StringProcessor jssp = new JavaScriptStringProcessor();
	
	
	htmpl.newBlock("blkDati");
	for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
  {
  
		htmpl.newBlock("blkDati.blkElenco");
    
    StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
    StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
    
		// COMUNE
		htmpl.set("blkDati.blkElenco.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
		htmpl.set("blkDati.blkElenco.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
		// SEZIONE
		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
			htmpl.set("blkDati.blkElenco.sezione", storicoParticellaVO.getSezione());
		}
		// FOGLIO
		htmpl.set("blkDati.blkElenco.foglio", storicoParticellaVO.getFoglio());
		// PARTICELLA
		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
			htmpl.set("blkDati.blkElenco.particella", storicoParticellaVO.getParticella());
		}
		// SUBALTERNO
		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
			htmpl.set("blkDati.blkElenco.subalterno", storicoParticellaVO.getSubalterno());
		}
		// SUPERFICIE CATASTALE
		htmpl.set("blkDati.blkElenco.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
		//Progr unar
		htmpl.set("blkDati.blkElenco.progrUnar", storicoUnitaArboreaVO.getProgrUnar());
		//Destinazione produttiva
		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdUtilizzo()))
		{
		  htmpl.set("blkDati.blkElenco.descrizioneUtilizzo", "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] "
		    + storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
		}
		//percentuale possesso
		BigDecimal percentualePossessoTmp = storicoParticellaVO.getElencoConduzioni()[0].getPercentualePossesso();
    if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
    {
      percentualePossessoTmp = new BigDecimal(1);
    } 
    htmpl.set("blkDati.blkElenco.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
    htmpl.set("blkDati.blkElenco.eleggibilita", "VITE");
    //SUPERFICIE ELEGGIBILE
    if(storicoParticellaVO.getParticellaCertificataVO() != null)
    {
      ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO.getParticellaCertificataVO();
      if(Validator.isNotEmpty(particellaCertificataVO.getVParticellaCertEleg()) 
        && (particellaCertificataVO.getVParticellaCertEleg().size() > 0)) 
      {
        //Per la query è popolato solo il primo elemento
        ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)particellaCertificataVO.getVParticellaCertEleg().get(0);
        if(Validator.isNotEmpty(partCertVO.getSuperficie())) {
          htmpl.set("blkDati.blkElenco.supEleggibile", Formatter.formatDouble4(partCertVO.getSuperficie()));         
        }
        else 
        {
          htmpl.set("blkDati.blkElenco.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
        }
      }
      else
      {
        htmpl.set("blkDati.blkElenco.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
      }
    }
    
    if(Validator.isNotEmpty(regimeUvAssocElegg))
    {
	    if(Validator.isNotEmpty(request.getParameterValues("percUtilizzoEleg"))
	     && Validator.isNotEmpty(request.getParameterValues("percUtilizzoEleg")[i])) 
	    {
	      htmpl.set("blkDati.blkElenco.percUtilizzoEleg", request.getParameterValues("percUtilizzoEleg")[i]);
	    }
	  }
    else
    {   
      htmpl.set("blkDati.blkElenco.percUtilizzoEleg", Formatter.formatDouble2(storicoParticellaVO.getPercentualeUtilizzoEleg()));
    }
    
    
    
    
		
    
    
		
    
    
    
    
		
		// GESTIONE ERRORI
		if(elencoErrori != null && elencoErrori.size() > 0) 
		{
			ValidationErrors errors = (ValidationErrors)elencoErrori.elementAt(i);
			if(errors != null && errors.size() > 0) {
				Iterator<String> iter = htmpl.getVariableIterator();
	 			while(iter.hasNext()) {
	 				String key = (String)iter.next();
	 			    if(key.startsWith("err_")) {
	 			    	String property = key.substring(4);
	 			    	Iterator<ValidationError> errorIterator = errors.get(property);
	 			    	if(errorIterator != null) {
	 			    		ValidationError error = (ValidationError)errorIterator.next();
	 			    		htmpl.set("blkDati.blkElenco.err_"+property,
	 			                      MessageFormat.format(htmlStringKO,
	 			                      new Object[] {
	 			                      pathErrori + "/"+ imko,
	 			                      "'"+jssp.process(error.getMessage())+"'",
	 			                      error.getMessage()}),
	 			                      null);
	 			    	}
	 			    }
	 			}
			}
		}
		
	}
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
	

%>
<%= htmpl.text()%>


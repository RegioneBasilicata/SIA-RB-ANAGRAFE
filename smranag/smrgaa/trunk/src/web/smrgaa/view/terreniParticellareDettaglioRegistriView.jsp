<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareDettaglioRegistri.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	String urlRSDI = (String) SolmrConstants.get("RSDI_URL");

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Long idConduzione = (Long)request.getAttribute("idConduzione");
  Vector<RegistroPascoloVO> vRegistroPascoloVO = (Vector<RegistroPascoloVO>)request.getAttribute("vRegistroPascoloVO");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO"); 

 	// Se si è verificato qualche errore visualizzo il messaggio all'utente
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti estraggo i dati
 	else 
  {
 		htmpl.set("idParticella", storicoParticellaVO.getIdParticella().toString());
 		htmpl.set("idConduzione", idConduzione.toString());
		htmpl.set("idAzienda", anagAziendaVO.getIdAzienda().toString());
		htmpl.set("urlRSDI", urlRSDI); 			

 		htmpl.newBlock("blkDatiDettaglio");
		// Dati di destata
		htmpl.set("blkDatiDettaglio.idConduzione", idConduzione.toString());
			htmpl.set("blkDatiDettaglio.idAzienda", anagAziendaVO.getIdAzienda().toString());
			htmpl.set("blkDatiDettaglio.urlRSDI", urlRSDI); 			
 		
 		String titleGis = "GIS";
 		if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
 		{          
	    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	        .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	    {
	      titleGis += " - foglio stabilizzato.";
	    }
	    else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	       && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	         .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	    {
	      titleGis += "- foglio in corso di stabilizzazione.";
	    }
	  }                
    htmpl.set("blkDatiDettaglio.controlliP", titleGis);
    
    String immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS;
    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
    {
	    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	        .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	    {
	      immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB;
	    }
	    else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	          .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	    {
	      immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB_CORSO;
	    }
	  }          
    htmpl.set("blkDatiDettaglio.immaginiControlliP", immaginiControlliP);
 		
 		
 		
 		
 		htmpl.set("blkDatiDettaglio.descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
 		if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia())) {
 			htmpl.set("blkDatiDettaglio.siglaProvincia", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")"); 			
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
 			htmpl.set("blkDatiDettaglio.sezione", storicoParticellaVO.getSezione());
 		}
 		htmpl.set("blkDatiDettaglio.foglio", storicoParticellaVO.getFoglio());
 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
 			htmpl.set("blkDatiDettaglio.particella", storicoParticellaVO.getParticella());
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
 			htmpl.set("blkDatiDettaglio.subalterno", storicoParticellaVO.getSubalterno());
 		}
 		htmpl.set("blkDatiDettaglio.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("blkDatiDettaglio.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
 		
 		
 	
    
    //Registro storico pascoli
    if(vRegistroPascoloVO != null)
    {
      htmpl.newBlock("blkDatiDettaglio.blkDatiRegistroPascoli");
      for(int i=0;i<vRegistroPascoloVO.size();i++)
      {
        htmpl.newBlock("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli");
        htmpl.set("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.annoCampagna",
           ""+vRegistroPascoloVO.get(i).getAnnoCampagna());
        htmpl.set("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.descFonte",
           vRegistroPascoloVO.get(i).getDescFonte());
        htmpl.set("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.superficie", 
          Formatter.formatDouble4(vRegistroPascoloVO.get(i).getSuperficie()));
      }
       
    }
    else
    {
      htmpl.newBlock("blkDatiDettaglio.blkNoDatiRegistroPascoli");
    }
    
 	}

%>
<%= htmpl.text()%>

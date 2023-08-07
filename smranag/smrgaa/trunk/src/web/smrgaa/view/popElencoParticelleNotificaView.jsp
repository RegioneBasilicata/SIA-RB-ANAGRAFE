<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>

<%


 	java.io.InputStream layout = application.getResourceAsStream("/layout/popElencoParticelleNotifica.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
  
  String idDichiarazioneConsistenza = (String)request.getParameter("idDichiarazioneConsistenza");

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();

 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	String messaggioErrore = null;
	
 	String urlChiamante = request.getParameter("urlChiamante");
 	htmpl.set("urlChiamante", urlChiamante);
 	String idTipoEntita = request.getParameter("idTipoEntita");
  htmpl.set("idTipoEntita", idTipoEntita);
  String descrizione = request.getParameter("descrizione");
  htmpl.set("descrizione", descrizione);
 	
  
  
  
  if(Validator.isNotEmpty(idDichiarazioneConsistenza))
    htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
  
 	
 	// Recupero i parametri dal javascript
 	if(Validator.isNotEmpty(urlChiamante)
 	  && (urlChiamante.indexOf("nuovaNotifica") != -1)) 
 	{
	 	htmpl.set("idTipologiaNotifica", request.getParameter("idTipologiaNotifica"));
	 	htmpl.set("idCategoriaNotifica", request.getParameter("idCategoriaNotifica"));
 	}
 	
 	String istatComuniConduzioniParticelle = request.getParameter("istatComuniConduzioniParticelle");
 	String istatProvinciaConduzioniParticelle = request.getParameter("istatProvinciaConduzioniParticelle");
 	String sezione = request.getParameter("sezione");
 	String foglio = request.getParameter("foglio");
 	String particella = request.getParameter("particella");
 	String subalterno = request.getParameter("subalterno");
 	String idTipoUtilizzo = request.getParameter("idTipoUtilizzo");
 	String idAnomalie = request.getParameter("idAnomalie");

 	// Costruisco il VO filtro per la ricerca
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
 	filtriParticellareRicercaVO.setIdPianoRiferimento(new Long(idDichiarazioneConsistenza));
 	filtriParticellareRicercaVO.setIstatComune(istatComuniConduzioniParticelle);
 	filtriParticellareRicercaVO.setSezione(sezione);
 	filtriParticellareRicercaVO.setFoglio(foglio);
 	filtriParticellareRicercaVO.setParticella(particella);
 	filtriParticellareRicercaVO.setSubalterno(subalterno);
 	if(Validator.isNotEmpty(idTipoUtilizzo))
 	  filtriParticellareRicercaVO.setIdUtilizzo(new Long(idTipoUtilizzo));
 	if(Validator.isNotEmpty(idAnomalie))
    filtriParticellareRicercaVO.setIdControllo(new Long(idAnomalie));
 	
 	// Effettuo la ricerca
 	Vector<StoricoParticellaVO> elencoParticelle = null;
 	if(!Validator.isNotEmpty(messaggioErrore)) 
  {
 		try 
    {
      elencoParticelle = gaaFacadeClient.getElencoParticelleForPopNotifica(filtriParticellareRicercaVO);
 		}
 		catch(SolmrException se) {
   		messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
 		}
 	}
  
    
 	// Se ci sono errori li visualizzo
 	if(Validator.isNotEmpty(messaggioErrore) || Validator.isEmpty(elencoParticelle)) 
  {
 		htmpl.newBlock("blkNoParticelle");
 		if(Validator.isNotEmpty(messaggioErrore)) 
 		{
   		htmpl.set("blkNoParticelle.messaggioErrore", messaggioErrore);
 		}
 		else 
 		{
 			htmpl.set("blkNoParticelle.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
 		}
 	}
 	// Altrimenti visualizzo l'elenco delle particelle trovate
 	else 
  {
   	htmpl.newBlock("blkParticelle");
   	for(int i = 0; i < elencoParticelle.size(); i++) 
    {
   		htmpl.newBlock("blkParticelle.blkElencoParticelle");
   		htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveParticelle");
   		StoricoParticellaVO storicoParticellaElencoVO = elencoParticelle.get(i);
   		int numRigheTot = 0;
   		for(int j=0;j<storicoParticellaElencoVO.getvConduzioniDichiarate().size();j++)
   		{
   		  ConduzioneDichiarataVO conduzioneDichiarataVO = storicoParticellaElencoVO.getvConduzioniDichiarate().get(j);
   		  if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
   		  {
   		    numRigheTot = numRigheTot+conduzioneDichiarataVO.getvUtilizzi().size();
   		  }
   		  else
   		  {   		  
   		    numRigheTot = numRigheTot+1;
   		  }
   		}
   		
   		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.rowTot", ""+numRigheTot);   		
   		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.idStoricoParticella", ""+storicoParticellaElencoVO.getIdStoricoParticella());
   		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.descComuneParticella", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
   		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.siglaProvinciaParticella", storicoParticellaElencoVO.getComuneParticellaVO().getSiglaProv());
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) {
     		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
   		}
   		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.foglio", storicoParticellaElencoVO.getFoglio());
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) {
     		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.particella", storicoParticellaElencoVO.getParticella());
   		}
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) {
     		htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.subalterno", storicoParticellaElencoVO.getSubalterno());
   		}
   		
   		for(int j=0;j<storicoParticellaElencoVO.getvConduzioniDichiarate().size();j++)
      {
        ConduzioneDichiarataVO conduzioneDichiarataVO = storicoParticellaElencoVO.getvConduzioniDichiarate().get(j);
        if(j==0)
        {
          int numRigheCond = 0;
          if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
          {
            numRigheCond = conduzioneDichiarataVO.getvUtilizzi().size();
          }
          else
          {         
            numRigheCond = 1;
          }
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.rowCond", ""+numRigheCond);
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.idTitoloPosseso", ""+conduzioneDichiarataVO.getIdTitoloPossesso());
          BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }  
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.percentualPossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));  
          if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
          {
	          for(int z=0;z<conduzioneDichiarataVO.getvUtilizzi().size();z++)
	          {
	            UtilizzoDichiaratoVO utilizzoDichiaratoVO = conduzioneDichiarataVO.getvUtilizzi().get(z);
	            if(z==0)
	            {
	              String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
	                +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
	                +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
	                +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
	              htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.descTipologiaProduttiva", descTipologiaProduttiva, null);
	              htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
	            }
	            else
	            {
		            htmpl.newBlock("blkParticelle.blkElencoParticelle");
		            htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo");
		            String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
		              +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
		              +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
		              +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
		            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.descTipologiaProduttiva", descTipologiaProduttiva, null);
		            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
		          }
	          }
	        }
        }
        else
        {
	        htmpl.newBlock("blkParticelle.blkElencoParticelle");
	        htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveConduzione");
	        int numRigheCond = 0;
	        if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
	        {
	          numRigheCond = conduzioneDichiarataVO.getvUtilizzi().size();
	        }
	        else
	        {         
	          numRigheCond = 1;
	        }
	        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.rowCond", ""+numRigheCond);
	        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.idTitoloPosseso", ""+conduzioneDichiarataVO.getIdTitoloPossesso());
	        BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
	        if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
	        {
	          percentualePossessoTmp = new BigDecimal(1);
	        }  
	        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.percentualPossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));  
	        if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
          {
		        for(int z=0;z<conduzioneDichiarataVO.getvUtilizzi().size();z++)
		        {
		          UtilizzoDichiaratoVO utilizzoDichiaratoVO = conduzioneDichiarataVO.getvUtilizzi().get(z);
	            if(z==0)
	            {
	              String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
	                +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
	                +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
	                +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
	              htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.descTipologiaProduttiva", descTipologiaProduttiva, null);
	              htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
	            }
	            else
	            {
	              htmpl.newBlock("blkParticelle.blkElencoParticelle");
	              htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo");
	              String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
	                +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
	                +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
	                +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
	              htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.descTipologiaProduttiva", descTipologiaProduttiva, null);
	              htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
	            }
		        }
		      }
	      }
      
      }
    }
 	}

%>
<%= htmpl.text()%>

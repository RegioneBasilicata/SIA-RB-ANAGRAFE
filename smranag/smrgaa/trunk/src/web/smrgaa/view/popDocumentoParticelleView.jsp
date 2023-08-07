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
<%@ page import="it.csi.util.performance.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%

	// Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
 	StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
 	// START
 	watcher.start();

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popDocumentoParticelle.htm");

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
 	
 	//Solo per modifica
 	DocumentoVO documentoVO = (DocumentoVO)session.getAttribute("documentoModificaVO");
  
  
  
  if(Validator.isNotEmpty(idDichiarazioneConsistenza))
    htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
  
 	
 	// Recupero i parametri dal javascript
 	if(Validator.isNotEmpty(urlChiamante)) 
 	{
	 	Long idTipologiaDocumento = Long.decode(request.getParameter("idTipologiaDocumento"));
	 	htmpl.set("idTipologiaDocumento", idTipologiaDocumento.toString());
	 	Long idCategoriaDocumento = Long.decode(request.getParameter("idCategoriaDocumento"));
	 	htmpl.set("idCategoriaDocumento", idCategoriaDocumento.toString());
	 	Long idTipoDocumento = Long.decode(request.getParameter("idTipoDocumento"));
	 	htmpl.set("idTipoDocumento", idTipoDocumento.toString());
 	}
 	String dataInizioValidita = request.getParameter("dataInizioValidita");
 	if(Validator.isNotEmpty(dataInizioValidita)) 
 	{
   		htmpl.set("dataInizioValidita", dataInizioValidita);
 	}
 	String dataFineValidita = request.getParameter("dataFineValidita");
 	if(Validator.isNotEmpty(dataFineValidita)) 
 	{
   		htmpl.set("dataFineValidita", dataFineValidita);
 	}
 	String idCausaleModificaDocumento = request.getParameter("idCausaleModificaDocumento");
  if(Validator.isNotEmpty(idCausaleModificaDocumento)) 
  {
    htmpl.set("idCausaleModificaDocumento", idCausaleModificaDocumento);
  }
 	String protocolla = request.getParameter("protocolla");
 	if(Validator.isNotEmpty(protocolla) && protocolla.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
 	{
  	htmpl.set("protocolla", protocolla);
 	}
 	String numeroProtocolloEsterno = request.getParameter("numeroProtocolloEsterno");
 	if(Validator.isNotEmpty(numeroProtocolloEsterno)) 
 	{
 		htmpl.set("numeroProtocolloEsterno", numeroProtocolloEsterno);
 	}
 	String note = request.getParameter("note");
 	if(Validator.isNotEmpty(note)) 
 	{
 		htmpl.set("note", note);
 	}
 	String siglaProvinciaParticella = request.getParameter("siglaProvinciaParticella");
 	if(Validator.isNotEmpty(siglaProvinciaParticella)) 
 	{
 		htmpl.set("siglaProvinciaParticella", siglaProvinciaParticella);
 	}
 	String comune = request.getParameter("comune");
 	if(Validator.isNotEmpty(comune)) 
  {
 		htmpl.set("comune", comune);
 	}
 	String titoloPossesso = request.getParameter("idTitoloPossesso");
 	if(Validator.isNotEmpty(titoloPossesso)) 
  {
  	htmpl.set("idTitoloPossesso", titoloPossesso);
 	}
 	String casoParticolare = request.getParameter("idCasoParticolare");
 	if(Validator.isNotEmpty(casoParticolare)) 
  {
  	htmpl.set("idCasoParticolare", casoParticolare);
 	}
  
  Long idAnomalieLg = null;
  String idAnomalie = request.getParameter("idAnomalie");
  if(Validator.isNotEmpty(idAnomalie)) 
  {
    htmpl.set("idAnomalie", idAnomalie);
    idAnomalieLg = new Long(idAnomalie);
  } 
  
  String inContradditorio	= request.getParameter("inContradditorio");
  if(Validator.isNotEmpty(inContradditorio)) 
  {
    htmpl.set("inContradditorio", inContradditorio);
  } 
  
  String hdChkExtraSistema = request.getParameter("hdChkExtraSistema");
  if(Validator.isNotEmpty(hdChkExtraSistema)) 
  {
    htmpl.set("hdChkExtraSistema", hdChkExtraSistema);
  } 
 
  String hdValoreC = request.getParameter("hdValoreC");
  if(Validator.isNotEmpty(hdValoreC)) 
  {
    htmpl.set("hdValoreC", hdValoreC);
  }  
  

 	String visualizzaParticelleAssociate = request.getParameter("particelleAssociate");
 	boolean hasUnitToDocument = false;
 	if(Validator.isNotEmpty(visualizzaParticelleAssociate)) 
 	{
  	hasUnitToDocument = true;
 	}


  // Effettuo la ricerca
  StoricoParticellaVO[] elencoParticelle = null;
  if(Validator.isNotEmpty(hdValoreC))
  {
    if(!Validator.isNotEmpty(messaggioErrore)) 
    {
      try 
      {
        elencoParticelle = anagFacadeClient.getListParticelleForDocumentValoreC( 
          anagAziendaVO, DateUtils.getCurrentYear(), hasUnitToDocument);        
      }
      catch(SolmrException se) {
        messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
      }
    }
  }
  else if(Validator.isNotEmpty(hdChkExtraSistema))
  {
    if(!Validator.isNotEmpty(messaggioErrore)) 
    {
      try 
      {
        elencoParticelle = anagFacadeClient.getListParticelleForDocumentExtraSistema(
          anagAziendaVO, DateUtils.getCurrentYear(), hasUnitToDocument);        
      }
      catch(SolmrException se) {
        messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
      }
    }
  }
  else
  { 
	 	// Recupero il codice istat comune
	 	ComuneVO comuneVO = null;
	 	try 
	 	{
	   	comuneVO = anagFacadeClient.getComuneByParameters(comune, siglaProvinciaParticella, null, SolmrConstants.FLAG_S, null);
	  }
	 	catch(SolmrException se) 
	 	{
	  	messaggioErrore = AnagErrors.ERRORE_COMUNE_INESISTENTE;
	 	}
	
	 	// Costruisco il VO filtro per la ricerca
	 	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	 	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
	 	if(comuneVO != null) 
	 	{
	 		storicoParticellaVO.setIstatComune(comuneVO.getIstatComune());
	 	}
	 	if(Validator.isNotEmpty(titoloPossesso)) 
	 	{
	  	conduzioneParticellaVO.setIdTitoloPossesso(Long.decode(titoloPossesso));
	 	}
	 	storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
	 	if(Validator.isNotEmpty(casoParticolare)) 
	 	{
	 		storicoParticellaVO.setIdCasoParticolare(Long.decode(casoParticolare));
	 	}

	 	
	 	if(!Validator.isNotEmpty(messaggioErrore)) 
	  {
	 		try 
	    {
	      if ("-1".equals(idDichiarazioneConsistenza) || Validator.isEmpty(idDichiarazioneConsistenza))
	      {
	   		  elencoParticelle = anagFacadeClient.getListParticelleForDocument(storicoParticellaVO, 
	          anagAziendaVO, hasUnitToDocument, idAnomalieLg, SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY);
	      }
	      else
	      {
	        elencoParticelle = anagFacadeClient.getListParticelleForDocument(storicoParticellaVO, 
	          anagAziendaVO, Long.parseLong(idDichiarazioneConsistenza), hasUnitToDocument, idAnomalieLg,
	          SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY);
	      }	      
	      
	      //filtro in contradditorio
	      if(Validator.isNotEmpty(elencoParticelle)
	        && (elencoParticelle.length > 0)
	        && Validator.isNotEmpty(inContradditorio)
	        && "S".equalsIgnoreCase(inContradditorio))
	      {
	        Vector<StoricoParticellaVO> vPartContradditorio = new Vector<StoricoParticellaVO>();
	        for(int i = 0; i < elencoParticelle.length; i++) 
	        {
	          StoricoParticellaVO stvoTmp = elencoParticelle[i];
	          Long idParticellaCertificata = null;
	          if(Validator.isNotEmpty(stvoTmp.getParticellaCertificataVO()))
	          {
	            idParticellaCertificata = stvoTmp.getParticellaCertificataVO().getIdParticellaCertificata();
	          }
	          
            Date dataInserimento = new Date();
            if(Validator.isNotEmpty(documentoVO)
              && Validator.isNotEmpty(documentoVO.getDataInserimento()))
            {
              dataInserimento = documentoVO.getDataInserimento();
            }
	          
	          
	          
	          int p26 = gaaFacadeClient.calcolaP26PlSql(anagAziendaVO.getIdAzienda().longValue(),
	            stvoTmp.getIdParticella().longValue(), idParticellaCertificata);
	          if(p26 != 1)
	          {              
              if(!gaaFacadeClient.isParticellaInPotenzialeContra(anagAziendaVO.getIdAzienda().longValue(),
                 stvoTmp.getIdParticella().longValue(), DateUtils.extractYearFromDate(dataInserimento)))
              {
                continue;
              }           
	          }
	          
	          boolean isPiemontese = false;
	          ProvinciaVO provinciaControlloVO = anagFacadeClient.getProvinciaByCriterio(stvoTmp.getComuneParticellaVO().getSiglaProv());
	          if(provinciaControlloVO != null) 
	          {
	            if(provinciaControlloVO.getIdRegione().equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE)) 
	            {
	              isPiemontese = true;    
	            }
	          }          
	          if(!isPiemontese)
	          {
	            continue;
	          }
	          
	          if(!gaaFacadeClient.isPossCreateDocFaseIstanzaRiesameFaseSucc(anagAziendaVO.getIdAzienda().longValue(), 
	            stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_FOTO, SolmrConstants.PARAMETRO_GGFOTO))
	          {
	            continue;
	          }
	          
	          
	          if(!gaaFacadeClient.isPossCreateDocFaseIstanzaRiesameFaseSucc(anagAziendaVO.getIdAzienda().longValue(), 
	             stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_FOTO, SolmrConstants.PARAMETRO_GGFOTO))
	          {
	            continue;
	          }
	          
	          if(gaaFacadeClient.existIstanzaEsameAttivaFase(anagAziendaVO.getIdAzienda().longValue(), 
	            stvoTmp.getIdParticella().longValue(), new Long(SolmrConstants.FASE_IST_RIESAM_CONTRO).longValue()))
	          {
	            continue;
	          }
	          
	          if(gaaFacadeClient.existAltraFaseFotoParticella(anagAziendaVO.getIdAzienda().longValue(), 
              stvoTmp.getIdParticella().longValue(), DateUtils.extractYearFromDate(dataInserimento),
              new Long(SolmrConstants.FASE_IST_RIESAM_CONTRO).longValue()))
            {
              continue;
            }
	          
	          
	          vPartContradditorio.add(stvoTmp);
	        }
	        
	        
	        if(vPartContradditorio.size() == 0) 
	        {
			      elencoParticelle = (StoricoParticellaVO[])vPartContradditorio.toArray(new StoricoParticellaVO[0]);
			    }
			    else 
			    {
			      elencoParticelle = (StoricoParticellaVO[])vPartContradditorio.toArray(new StoricoParticellaVO[vPartContradditorio.size()]);
			    }
	              
	      }
	      
	 		}
	 		catch(SolmrException se) {
	   		messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
	 		}
	 	}
	}
  
  if(Validator.isNotEmpty(visualizzaParticelleAssociate)) 
  {
    htmpl.set("checkedParticelleAssociate", "checked=\"checked\"");
  }
    
 	// Se ci sono errori li visualizzo
 	if(Validator.isNotEmpty(messaggioErrore) 
 	  || elencoParticelle.length == 0) 
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
 		/*if(Validator.isNotEmpty(visualizzaParticelleAssociate)) {
   		htmpl.set("checkedParticelleAssociate", "checked=\"checked\"");
 		}*/
   	htmpl.newBlock("blkParticelle");
   	htmpl.newBlock("blkParticelle1");
   	for(int i = 0; i < elencoParticelle.length; i++) 
    {
   		htmpl.newBlock("blkParticelle1.blkElencoParticelle");
   		StoricoParticellaVO storicoParticellaElencoVO = (StoricoParticellaVO)elencoParticelle[i];
   		htmpl.set("blkParticelle1.blkElencoParticelle.idConduzioneParticella", storicoParticellaElencoVO.getElencoConduzioni()[0].getIdConduzioneParticella().toString());
   		htmpl.set("blkParticelle1.blkElencoParticelle.descComuneParticella", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
   		htmpl.set("blkParticelle1.blkElencoParticelle.siglaProvinciaParticella", storicoParticellaElencoVO.getComuneParticellaVO().getSiglaProv());
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) {
     		htmpl.set("blkParticelle1.blkElencoParticelle.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
   		}
   		htmpl.set("blkParticelle1.blkElencoParticelle.foglio", storicoParticellaElencoVO.getFoglio());
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) {
     		htmpl.set("blkParticelle1.blkElencoParticelle.particella", storicoParticellaElencoVO.getParticella());
   		}
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) {
     		htmpl.set("blkParticelle1.blkElencoParticelle.subalterno", storicoParticellaElencoVO.getSubalterno());
   		}
   		htmpl.set("blkParticelle1.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaElencoVO.getSupCatastale()));
   		htmpl.set("blkParticelle1.blkElencoParticelle.idTitoloPossesso", storicoParticellaElencoVO.getElencoConduzioni()[0].getIdTitoloPossesso().toString());
   		htmpl.set("blkParticelle1.blkElencoParticelle.superficieCondotta", StringUtils.parseSuperficieField(storicoParticellaElencoVO.getElencoConduzioni()[0].getSuperficieCondotta()));
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getIdCasoParticolare())) {
   			htmpl.set("blkParticelle1.blkElencoParticelle.idCasoParticolare", storicoParticellaElencoVO.getIdCasoParticolare().toString());
   		}
    }
 	}

 	// Monitoraggio
 	watcher.dumpElapsed("popDocumentoParticelleView.jsp", "Creation of list of particellaVO", "In popDocumentoParticelleView.jsp view from the beginning to the end", "");
 	// STOP
 	watcher.stop();

%>
<%= htmpl.text()%>

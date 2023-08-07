<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO"%>
<%@page
  import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>

<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	String iridePageName = "eliminaParticellaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
  
  SolmrLogger.debug(this, " - eliminaParticellaCtrl.jsp - INIZIO PAGINA");

	String terreniParticellareElencoUrl = "/ctrl/terreniParticellareElencoCtrl.jsp";
	String eliminaParticellaUrl = "/view/eliminaParticellaView.jsp";
  
  String actionUrl = "../layout/terreniParticellareElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione elimina particella."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();

	ValidationErrors errors = new ValidationErrors();
    

	// L'utente ha selezionato il pulsante annulla dalla pagina che presenta l'elenco
	// delle particelle eliminabili
	if(Validator.isNotEmpty(request.getParameter("annulla"))) 
  {
		session.removeAttribute("elencoParticelleEliminabili");
		session.removeAttribute("numParticelleSelezionate");
 	  session.removeAttribute("numUtilizziSelezionati");
  	// Torno alla pagina di elenco delle particelle
  	%>
    		<jsp:forward page= "<%= terreniParticellareElencoUrl %>" />
  	<%
	}
	// L'utente ha selezionato il pulsante conferma dalla pagina che presenta l'elenco
	// delle particelle eliminabili
	else if(Validator.isNotEmpty(request.getParameter("conferma"))) 
  {

    // Recupero il vettore contenente l'elenco degli id conduzione particella
    // che l'utente intente eliminare
    Vector<Long> elencoConduzioni = (Vector<Long>)session.getAttribute("elencoConduzioni");
    
    
    //Mi estraggo eventuali conduzioni con uv
    Vector<Long> vIdConduzioniUV = gaaFacadeClient.esisteUVByConduzioneAndAzienda(elencoConduzioni,
        anagAziendaVO.getIdAzienda());
    request.setAttribute("vIdConduzioniUV", vIdConduzioniUV);
    //Mi estraggo eventuali conduzioni con fabbricati attivi
    HashMap<Long,String> hIdConduzioniFabbricati = gaaFacadeClient.esisteFabbricatoAttivoByConduzioneAndAzienda(elencoConduzioni,
        anagAziendaVO.getIdAzienda());
    request.setAttribute("hIdConduzioniFabbricati", hIdConduzioniFabbricati);
    //Mi estraggo eventuali conduzioni con documenti attivi
    Vector<Long> vIdConduzioniDocumenti = gaaFacadeClient.esisteDocumentoAttivoByConduzioneAndAzienda(elencoConduzioni,
        anagAziendaVO.getIdAzienda());
    request.setAttribute("vIdConduzioniDocumenti", vIdConduzioniDocumenti);
    
    
    
    //Eliminati tutti i controlli come da richiesta Sergio/teresa in data 04/05/2012   
    /*if(!ruoloUtenza.isUtentePA())
    {
          
      //Mi estraggo eventuali conduzioni con uv modificate proc viti
      Vector<Long> vIdConduzioniModProcVITIUV = gaaFacadeClient.esisteUVModProcVITIByConduzioneAndAzienda(elencoConduzioni,
          anagAziendaVO.getIdAzienda());
      if(Validator.isNotEmpty(vIdConduzioniModProcVITIUV))
      {
        request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_MULTIPLE);
        request.setAttribute("vIdConduzioniModProcVITIUV",vIdConduzioniModProcVITIUV);
        %>
          <jsp:forward page="<%= eliminaParticellaUrl %>" />
        <%
        return;     
      }
      
      
      //Mi estraggo eventuali conduzioni con uv validate
      Vector<Long> vIdConduzioniValidateUV = gaaFacadeClient.esisteUVValidataByConduzioneAndAzienda(elencoConduzioni,
          anagAziendaVO.getIdAzienda());
              
      if(Validator.isNotEmpty(vIdConduzioniValidateUV))
      {
        request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_ELIMINA_CONDUZIONE_UV_VALIDATE_PA);
        %>
          <jsp:forward page="<%= eliminaParticellaUrl %>" />
        <%     
      }
      
      Vector<Long> vIdParticella = gaaFacadeClient.getIdParticellaByIdConduzione(elencoConduzioni);
      //controllo che non ci siano pratiche uv associate alla particella
      if(Validator.isNotEmpty(vIdParticella))
      {
        long[] aIdParticella = new long[vIdParticella.size()];
        for(int i=0;i<vIdParticella.size();i++)
        {
          aIdParticella[i] = vIdParticella.get(i).longValue();
        }
        
        RitornoAgriservUvVO ritornoAgriservUvVO = gaaFacadeClient.existPraticheEstirpoUV(aIdParticella,
          anagAziendaVO.getIdAzienda(), null, null, null, 0);
        Vector<Long>  vPraticheIdParticella = ritornoAgriservUvVO.getvPraticheIdParticella();
        
        //Passando una sola particella se il vettore è valorizzato vuol dire che ad
        // essa sono associate pratiche
        if(Validator.isNotEmpty(vPraticheIdParticella))
        {
          request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_MULTIPLE);
          request.setAttribute("vPraticheIdParticella",vPraticheIdParticella);
          %>
            <jsp:forward page="<%= eliminaParticellaUrl %>" />
          <%
          return;     
        }
        
        //errore nella chiamata al servizio
        if(Validator.isNotEmpty(ritornoAgriservUvVO.getErrori())  && (ritornoAgriservUvVO.getErrori().length > 0))
        {
          request.setAttribute("messaggioErrore", ritornoAgriservUvVO.getErrori()[0]);
          %>
            <jsp:forward page="<%= eliminaParticellaUrl %>" />
          <%
          return;         
        }
      }
    }*/
    
    
    
    
  	

  	// Procedo con l'eliminazione
  	try 
    {
    	anagFacadeClient.eliminaParticelle(elencoConduzioni, anagAziendaVO.getIdAzienda(), ruoloUtenza);
  	}
  	catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - eliminaParticellaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+se.getMessage();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
  	}
  	session.removeAttribute("elencoConduzioni");
  	session.removeAttribute("elencoParticelleEliminabili");
  	session.removeAttribute("numParticelleSelezionate");
 	  session.removeAttribute("numUtilizziSelezionati");
  	%>
    		<jsp:forward page= "<%= terreniParticellareElencoUrl %>" />
  	<%
	}
  // L'utente ha selezionato la funzione elimina particelle dalla pagina di elenco delle
	// particelle
	else 
  {
		
		HashMap<String,Vector<Long>> numParticelleSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numParticelleSelezionate");
		HashMap<String,Vector<Long>> numUtilizziSelezionati = (HashMap<String,Vector<Long>>)session.getAttribute("numUtilizziSelezionati");
		String[] idConduzione = request.getParameterValues("idConduzione");
		String[] elencoIdUtilizzo = request.getParameterValues("idUtilizzo");
		String numPagina = request.getParameter("pagina");
		Vector<Long> elencoIdDaModificare = new Vector<Long>();
		
	  // Gestione della selezione delle particelle
		Vector<Long> elenco = new Vector<Long>();
		Vector<Long> elencoUtilizzi = new Vector<Long>();
		if(numParticelleSelezionate != null && numParticelleSelezionate.size() > 0) {
			numParticelleSelezionate.remove(numPagina);
			if(idConduzione != null && idConduzione.length > 0) {
				for(int i = 0; i < idConduzione.length; i++) {
					elenco.add(Long.decode((String)idConduzione[i]));
					elencoUtilizzi.add(Long.decode((String)elencoIdUtilizzo[i]));
				}
			}
		}
		else 
    {
			if(numParticelleSelezionate == null) 
      {
				numParticelleSelezionate = new HashMap<String,Vector<Long>>();
				numUtilizziSelezionati = new HashMap<String,Vector<Long>>();
			}
			for(int i = 0; i < idConduzione.length; i++) 
      {
				Long idElemento = Long.decode((String)idConduzione[i]);
				elenco.add(idElemento);
				elencoUtilizzi.add(Long.decode((String)elencoIdUtilizzo[i]));
			}
		}
		if(elenco.size() > 0) 
    {
			numParticelleSelezionate.put(numPagina, elenco);
			session.setAttribute("numParticelleSelezionate", numParticelleSelezionate);
			numUtilizziSelezionati.put(numPagina, elencoUtilizzi);
			session.setAttribute("numUtilizziSelezionati", numUtilizziSelezionati);
		}     
      
  		
	  // Recupero il parametro che mi indica il numero massimo di record selezionabili
		String parametroRTMO = null;
		try 
    {
			parametroRTMO = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RTMO);;
		}
		catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - eliminaParticellaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_RTMO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
		}
		
	  // Verifico che non siano state selezionate più particelle rispetto a quelle consentite
		Set<String> elencoKeys = numParticelleSelezionate.keySet();
		Iterator<String> iteraKey = elencoKeys.iterator();
		while(iteraKey.hasNext()) {
			Vector<Long> selezioni = (Vector<Long>)numParticelleSelezionate.get((String)iteraKey.next());
			if(selezioni != null && selezioni.size() > 0) {
				for(int a = 0; a < selezioni.size(); a++) {
					Long elemento = (Long)selezioni.elementAt(a);
					if(elencoIdDaModificare.size() == 0 || !elencoIdDaModificare.contains(elemento)) {
						elencoIdDaModificare.add(elemento);
					}
				}
			}
		}
		if(elencoIdDaModificare.size() > Integer.parseInt(parametroRTMO)) 
    {
			ValidationError error = new ValidationError(AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRTMO+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
			request.setAttribute("error", error);
			%>
				<jsp:forward page="<%= terreniParticellareElencoUrl %>" />
			<%
		}     

  	// Metto in sessione il vettore contenente le conduzioni
  	session.setAttribute("elencoConduzioni", elencoIdDaModificare);
  	
  	// Controllo che non sia stata fatta una dichiarazione di consistenza che includeva
  	// una delle particelle selezionate
  	try 
    {
    	anagFacadeClient.checkParticellaLegataDichiarazioneConsistenza(elencoIdDaModificare);
  	}
  	catch(SolmrException se) 
    {
  		ValidationError error = new ValidationError(se.getMessage());
  		request.setAttribute("error", error);
		  %>
			  <jsp:forward page="<%= terreniParticellareElencoUrl %>" />
		  <%
  	}
  	
  	
  	// Procedo con l'eliminazione
    try 
    {
      if(!gaaFacadeClient.isParticellaIstRiesameCancellabile(anagAziendaVO.getIdAzienda().longValue(), elencoIdDaModificare))
      {
        ValidationError error = new ValidationError("Tra le particelle selezionate ne esiste almeno una legata a una istanza di riesame. "+
          "E'' possibile solo cessare la conduzione o annullare l''istanza se inevasa.");
	      request.setAttribute("error", error);
	      %>
	        <jsp:forward page="<%= terreniParticellareElencoUrl %>" />
	      <%
	      return;
      }
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - eliminaParticellaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+se.getMessage();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  	
  	
  	
    
    //Mi estraggo eventuali conduzioni con uv 
    Vector<Long> vIdConduzioniUV = gaaFacadeClient.esisteUVByConduzioneAndAzienda(elencoIdDaModificare,
        anagAziendaVO.getIdAzienda());
    request.setAttribute("vIdConduzioniUV", vIdConduzioniUV);
    
    //Mi estraggo eventuali conduzioni con fabbricati attivi
    HashMap<Long,String> hIdConduzioniFabbricati = gaaFacadeClient.esisteFabbricatoAttivoByConduzioneAndAzienda(elencoIdDaModificare,
        anagAziendaVO.getIdAzienda());
    request.setAttribute("hIdConduzioniFabbricati", hIdConduzioniFabbricati);
    
    //Mi estraggo eventuali conduzioni con documenti attivi
    Vector<Long> vIdConduzioniDocumenti = gaaFacadeClient.esisteDocumentoAttivoByConduzioneAndAzienda(elencoIdDaModificare,
        anagAziendaVO.getIdAzienda());
    request.setAttribute("vIdConduzioniDocumenti", vIdConduzioniDocumenti);
    
    
    

  	// Se non ci sono ricerco le particelle che è possibile eliminare e le visualizzo
  	Vector<ParticellaVO> elencoParticelleEliminabili = new Vector<ParticellaVO>();
  	try 
    {
   		for(int i = 0; i < elencoIdDaModificare.size(); i++) 
      {
      	Long idConduzioneParticellaEliminabile = (Long)elencoIdDaModificare.elementAt(i);
      	ParticellaVO particellaEliminabileVO = anagFacadeClient.getParticellaVOByIdConduzioneParticella(idConduzioneParticellaEliminabile);
      	elencoParticelleEliminabili.add(particellaEliminabileVO);
    	}
  	}
  	catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - eliminaParticellaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+se.getMessage();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
  	}

  	if(elencoParticelleEliminabili.size() > 0) 
    {
    	session.setAttribute("elencoParticelleEliminabili", elencoParticelleEliminabili);
  	}
  	%>
    		<jsp:forward page= "<%= eliminaParticellaUrl %>" />
  	<%
  }
%>

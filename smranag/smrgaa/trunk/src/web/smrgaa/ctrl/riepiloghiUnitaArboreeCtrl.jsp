<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "riepiloghiUnitaArboreeCtrl.jsp";
  %>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	final String errMsg = "Impossibile procedere nella sezione riepiloghi unità vitate."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

	String riepiloghiUrl = "/view/riepiloghiUnitaArboreeView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
	String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";

	ValidationErrors errors = new ValidationErrors();
  
  
  
  
  if("excel".equals(request.getParameter("operazione")))
  {
    Long tipoRicerca = Long.decode(request.getParameter("idTipoRiepilogo"));
    
    if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTIONAZIONE_PRODUTTIVA_COMUNE) == 0) 
    {
      String excelUrl = "/servlet/ExcelRiepilogoDestProdComuneServlet";  
      %>
        <jsp:forward page="<%=excelUrl %>" />
      <%
      return;
    }
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTINAZIONE_PRODUTTIVA_UVA_DA_VINO) == 0)
    {
      String excelUrl = "/servlet/ExcelRiepilogoDestProdUvaVinoComuneServlet";  
      %>
        <jsp:forward page="<%=excelUrl %>" />
      <%
      return;
    }
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_VINO_DOP) == 0) 
    {
      String excelUrl = "/servlet/ExcelRiepilogoVinoDOPComuneServlet";  
      %>
        <jsp:forward page="<%=excelUrl %>" />
      <%
      return;
    }
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_PROVINCIA_VINO_DOP) == 0) 
    {
      String excelUrl = "/servlet/ExcelRiepilogoVinoDOPProvinciaServlet";  
      %>
        <jsp:forward page="<%=excelUrl %>" />
      <%
      return;
    }
    
    
  }
  
  
  


	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
	// in modo che venga sempre effettuato
	try 
  {
  	anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
	}
	catch(SolmrException se) 
  {
  	request.setAttribute("statoAzienda", se);
	}
  
  String pianoRiferimento = request.getParameter("idDichiarazioneConsistenza");
  Long idPianoRiferimento = null;
  // Nel caso in cui non sia valorizzato vuol dire che è la prima volta che accedo alla pagina e quindi imposto il piano di riferimento alla data odierna
  if(!Validator.isNotEmpty(pianoRiferimento)) 
  {
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);  
  }
  else 
  {
    idPianoRiferimento = Long.decode(pianoRiferimento);
  }
  
  
  
  Vector<TipoRiepilogoVO> vTipoRiepilogo = null;
  try 
  {
    vTipoRiepilogo = gaaFacadeClient.getTipoRiepilogo(SolmrConstants.RIEPILOGO_UNITA_VITATE, 
      ruoloUtenza.getCodiceRuolo());
    request.setAttribute("vTipoRiepilogo", vTipoRiepilogo);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - riepiloghiUnitaArboreeCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_TIPO_RIEPILOGHI+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  
  	
	int numeroProblemi = 0;
	// L'utente ha cliccato il pulsante "elenco unita vitate"
	if(request.getParameter("elencoUnitaVitate") != null) 
  {
		// Recupero i parametri
		Long tipoRicerca = Long.decode(request.getParameter("idTipoRiepilogo"));
		
		// RIEPILOGO PER DESTIONAZIONE PRODUTTIVA COMUNE
		if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTIONAZIONE_PRODUTTIVA_COMUNE) == 0) 
    {
			String istatComune = request.getParameter("istatComune");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(istatComune)) 
      {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
    		errors.add("error", error);
    		request.setAttribute("errors", errors);
    		numeroProblemi++;
			}
			else 
      {
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
				filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        filtriUnitaArboreaRicercaVO.setIstatComune(istatComune);
				// Rimetto il filtro in sessione
				session.setAttribute("filtriUnitaArboreaRicercaVO", filtriUnitaArboreaRicercaVO);
			}
		}
		// RIEPILOGO DESTINAZIONE PRODUTTIVA UVA DA VINO
		else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTINAZIONE_PRODUTTIVA_UVA_DA_VINO) == 0) 
    {
			String istatComune = request.getParameter("istatComune");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(istatComune)) 
      {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
  			errors.add("error", error);
  			request.setAttribute("errors", errors);
  			numeroProblemi++;
			}
			else 
      {
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
				filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        filtriUnitaArboreaRicercaVO.setIstatComune(istatComune);
        filtriUnitaArboreaRicercaVO.setIdUtilizzo(SolmrConstants.UVA_DA_VINO);
				
				// Rimetto il filtro in sessione
				session.setAttribute("filtriUnitaArboreaRicercaVO", filtriUnitaArboreaRicercaVO);
			}
		}
		// RIEPILOGO VINO DOP
		else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_VINO_DOP) == 0) 
    {
			String vinoComune = request.getParameter("vinoComune");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(vinoComune)) 
      {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
  			errors.add("error", error);
  			request.setAttribute("errors", errors);
  			numeroProblemi++;
			}
			else {
				// Rimuovo dalla sessione il precedente filtro del particellare
				//session.removeAttribute("filtriParticellareRicercaVO");
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
				filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        StringTokenizer strToken = new StringTokenizer(vinoComune,",");
        //il primo valore è l'istatComune
        filtriUnitaArboreaRicercaVO.setIstatComune((String)strToken.nextElement());
        //Il secondo valore è la tipologia el vino
        filtriUnitaArboreaRicercaVO.setIdTipologiaVino(new Long((String)strToken.nextElement()));
        
				//filtriParticellareRicercaVO.setIstatComune(request.getParameterValues("istatComuneParticella")[Integer.parseInt(indice)]);
				//filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
				
				// Rimetto il filtro in sessione
				session.setAttribute("filtriUnitaArboreaRicercaVO", filtriUnitaArboreaRicercaVO);
			}
		}
    // RIEPILOGO PROVINCIA VINO DOP
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_PROVINCIA_VINO_DOP) == 0) 
    {
      String vinoProvincia = request.getParameter("vinoProvincia");
      // Controllo che sia stata selezionata una voce dall'elenco
      if(!Validator.isNotEmpty(vinoProvincia)) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
        errors.add("error", error);
        request.setAttribute("errors", errors);
        numeroProblemi++;
      }
      else {
        // Rimuovo dalla sessione il precedente filtro del particellare
        //session.removeAttribute("filtriParticellareRicercaVO");
        session.removeAttribute("filtriUnitaArboreaRicercaVO");
        // Lo ricreo
        FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
        filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        StringTokenizer strToken = new StringTokenizer(vinoProvincia,",");
        //il primo valore è l'istatComune
        filtriUnitaArboreaRicercaVO.setIstatProvincia((String)strToken.nextElement());
        //Il secondo valore è la tipologia el vino
        filtriUnitaArboreaRicercaVO.setIdTipologiaVino(new Long((String)strToken.nextElement()));
        
        //filtriParticellareRicercaVO.setIstatComune(request.getParameterValues("istatComuneParticella")[Integer.parseInt(indice)]);
        //filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
        
        // Rimetto il filtro in sessione
        session.setAttribute("filtriUnitaArboreaRicercaVO", filtriUnitaArboreaRicercaVO);
      }
    }
		
	
		// Torno alla pagina di ricerca/elenco dei terreni
		if(numeroProblemi == 0) 
		{
			%>
				<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
			<%
			return;
		}
	}
	
	Long tipoRicerca = null;
	if(request.getParameter("aggiornaRiepilogo") != null || numeroProblemi > 0) 
  {
     tipoRicerca = Long.decode(request.getParameter("idTipoRiepilogo"));
  }
  //prima volta che entro su riepiloghi
  //prendo quello di dafault
  else
  {  
    if(Validator.isNotEmpty(vTipoRiepilogo))
    {
      for(int i=0;i<vTipoRiepilogo.size();i++) 
      {
        TipoRiepilogoVO tipoRiepilogoVO = vTipoRiepilogo.get(i);
        if("S".equalsIgnoreCase(tipoRiepilogoVO.getFlagDefault()))
        {
          tipoRicerca = new Long(tipoRiepilogoVO.getIdTipoRiepilogo());
          break;
        }
      }
    }
  }
  request.setAttribute("idTipoRicerca",tipoRicerca);

 	// Se la ricerca è per destinazione produttiva comune
 	if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTIONAZIONE_PRODUTTIVA_COMUNE) == 0) 
  {
    Vector<RiepiloghiUnitaArboreaVO> elencoRiepiloghi = null;
 	  // Se l'utente ha selezionato l'anno corrente
 		if(idPianoRiferimento.longValue() == -1) 
    {
   		try 
      {
   			elencoRiepiloghi = gaaFacadeClient.riepilogoDestinazioneProduttivaComune(anagAziendaVO.getIdAzienda().longValue());
   			if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
         
        request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
   		}
   		catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
			    <jsp:forward page="<%= riepiloghiUrl %>" />
		    <%
   		}
 		}
 		// Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
 		else 
    {
   		try 
      {
   			elencoRiepiloghi = gaaFacadeClient.riepilogoDestinazioneProduttivaComuneDichiarato(idPianoRiferimento.longValue());
   			if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
         
        request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
   		}
   		catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
			    <jsp:forward page="<%= riepiloghiUrl %>" />
		    <%
   		}
 		}
 	}
 	// Se la ricerca è per destinazione produttiva uva da vino
 	else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTINAZIONE_PRODUTTIVA_UVA_DA_VINO) == 0) 
  {
 	  Vector<RiepiloghiUnitaArboreaVO> elencoRiepiloghi = null;
 		// Se l'utente ha selezionato l'anno corrente
 		if(idPianoRiferimento.longValue() == -1)
    {
   		try 
      {
   			elencoRiepiloghi = gaaFacadeClient.riepilogoDestinazioneProduttivaUvaDaVino(anagAziendaVO.getIdAzienda().longValue());
   			if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
         
         
         request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
   		}
   		catch(SolmrException se) 
      {
     		String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
     		request.setAttribute("messaggioErrore", messaggioErrore);
        %>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
				<%
     	}
   	}
 		else 
    {
   		// Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
   		try 
      {
   			elencoRiepiloghi = gaaFacadeClient.riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(idPianoRiferimento.longValue());
   			if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
         
        request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
   		}
   		catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
			    <jsp:forward page="<%= riepiloghiUrl %>" />
		    <%
   		}
 		}
 	}
  // Il tipo di riepilogo è per vino DOP
	else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_VINO_DOP) == 0) 
  {
		Vector<RiepiloghiUnitaArboreaVO> elencoRiepiloghi = null;
		// Se l'utente ha selezionato l'anno corrente
		if(idPianoRiferimento.longValue() == -1) 
    {
 			try 
      {
 				elencoRiepiloghi = gaaFacadeClient.riepilogoDestinazioneProduttivaVinoDOP(anagAziendaVO.getIdAzienda().longValue());
 				if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
         
         request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
   	 	}
 			catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>					
	  		  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
     	}
   	}
   	else 
    {
 			// Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
 			try 
      {
 				elencoRiepiloghi = gaaFacadeClient.riepilogoDestinazioneProduttivaVinoDOPDichiarato(idPianoRiferimento.longValue());
 				if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
         
         request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
     	}
 			catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
     	}
 		}
	}
  // Il tipo di riepilogo è per provincia e vino DOP
  else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_PROVINCIA_VINO_DOP) == 0) 
  {
    Vector<RiepiloghiUnitaArboreaVO> elencoRiepiloghi = null;
    // Se l'utente ha selezionato l'anno corrente
    if(idPianoRiferimento.longValue() == -1) 
    {
      try 
      {
        elencoRiepiloghi = gaaFacadeClient.riepilogoProvinciaVinoDOP(anagAziendaVO.getIdAzienda().longValue());
        if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
        
        request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
    else 
    {
      // Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
      try 
      {
        elencoRiepiloghi = gaaFacadeClient.riepilogoProvinciaVinoDOPDichiarato(idPianoRiferimento.longValue());
        if(elencoRiepiloghi == null)
        {
          String messaggioErrore = AnagErrors.ERRORE_NO_ELEMENTI_SISTEMA;
          request.setAttribute("messaggioErrore", messaggioErrore);
        }
        
        request.setAttribute("elencoRiepiloghi", elencoRiepiloghi);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
  }            
	
 	// Vado alla pagina di di riepiloghi terreni
	%>
		<jsp:forward page="<%= riepiloghiUrl %>" />

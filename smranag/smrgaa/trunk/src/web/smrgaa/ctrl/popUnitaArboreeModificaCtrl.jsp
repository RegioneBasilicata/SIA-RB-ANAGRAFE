<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popUnitaArboreeModificaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
  String popUnitaArboreeModificaUrl = "/view/popUnitaArboreeModificaView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica unità arboree "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
  String[] orderBy = null;
  ValidationErrors errors = null;
  Long idStoricoUnitaArborea = null;
  
  
  
  if(Validator.isNotEmpty(request.getParameter("idStoricoUnitaArborea"))) 
  {
	  idStoricoUnitaArborea = Long.decode(request.getParameter("idStoricoUnitaArborea"));
	  request.setAttribute("idStoricoUnitaArborea",idStoricoUnitaArborea);
  }
	
  String messaggioErrore = null;
  String[] elencoIdVarieta = request.getParameterValues("idVarieta");
  String[] elencoPercentuale = request.getParameterValues("percentuale");
  String pulisci = request.getParameter("pulisci");
  // Parametro che indica la particella selezionata dall'elenco...
  String numero = request.getParameter("numero");
  boolean mustUpgrade = false;
  // Se è valorizzato vuol dire che ho richiamato la pop-up dall'elenco
  if(Validator.isNotEmpty(numero)) 
  {
    mustUpgrade = true;
  	// Se il valore è diverso da quello che avevo selezionato precedentemente...
  	if(!numero.equalsIgnoreCase((String)session.getAttribute("numero"))) 
    {
  	  // rimuovo i possibili vecchi utilizzi selezionati perchè riguarderebbero
  	  // un'altra particella
  	  session.removeAttribute("storicoParticellaArboreaVO");
  	  session.removeAttribute("oldAltriVitigni");
  	}
  	session.setAttribute("numero", numero);
  }
  else 
  {
    numero = (String)session.getAttribute("numero");
  }
  
  // Recupero il parametro che mi indica l'obbligatorietà di un campo o meno
  boolean datiObbligatori=false;
  try 
  {
    String parametroAlbo = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.ID_PARAMETRO_ALBO);;
    if (SolmrConstants.FLAG_S.equals(parametroAlbo)) datiObbligatori=true;
  }
  catch(SolmrException se) 
  {
    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_UTILIZZO;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= popUnitaArboreeModificaUrl %>" />
    <%
  }
	
  // Recupero i dati della particella selezionata
  StoricoParticellaVO storicoParticellaArboreaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaArboreaVO");
  AltroVitignoVO[] oldElencoAltriVitigni = (AltroVitignoVO[])session.getAttribute("oldElencoAltriVitigni");
  
  
  if(Validator.isNotEmpty(request.getParameter("abbandona"))) 
  {
    storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setElencoAltriVitigni(oldElencoAltriVitigni);
    session.setAttribute("storicoParticellaArboreaVO", storicoParticellaArboreaVO);
    
    %>
      <html>
        <head>
           <script type="text/javascript">
             window.opener.location.href='../layout/unitaArboreeModifica.htm';
             window.close();
           </script>
        </head>
      </html>
    <%
    return;
  }
  
  
  
  
  // Se è la prima volta che accedo alla pop-up per l'unità arborea selezionata
  // Modifico i valori degli oggetti della tabella in modo che siano mantenuti solo se arrivo dalla pagina di modifica e non dalla pop-up
  if(mustUpgrade) 
  {
	  for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
    {
	    StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
	    StoricoUnitaArboreaVO storicoArboreaVO = storicoVO.getStoricoUnitaArboreaVO();
	    // DESTINAZIONE PRODUTTIVA
	    if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i])) 
      {
		    storicoArboreaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
		    if(i == Integer.parseInt(numero)) 
        {
		      TipoUtilizzoVO tipoUtilizzoVO = null;
		      try 
          {
			      tipoUtilizzoVO = anagFacadeClient.findTipoUtilizzoByPrimaryKey(storicoArboreaVO.getIdUtilizzo());
			      storicoArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
		      }
		      catch(SolmrException se) 
          {
			      messaggioErrore = AnagErrors.ERRORE_KO_TIPO_UTILIZZO;
			      request.setAttribute("messaggioErrore", messaggioErrore);
			      %>
			        <jsp:forward page="<%=popUnitaArboreeModificaUrl%>" />
			      <%
		      }
		    }
      }
  	  else 
      {
  		  storicoArboreaVO.setIdUtilizzo(null); 
  	  }
  	  if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i])) 
      {
        storicoArboreaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[i]));
      }
      else 
      {
        storicoArboreaVO.setIdTipoDestinazione(null); 
      }
      if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i])) 
      {
        storicoArboreaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[i]));
      }
      else 
      {
        storicoArboreaVO.setIdTipoDettaglioUso(null); 
      }
      if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i])) 
      {
        storicoArboreaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[i]));
      }
      else 
      {
        storicoArboreaVO.setIdTipoQualitaUso(null); 
      }
	    // VITIGNO
	    if(Validator.isNotEmpty(request.getParameterValues("idVarieta")[i])) 
      {
		    storicoArboreaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[i]));
		    if(i == Integer.parseInt(numero)) 
        {
		      TipoVarietaVO tipoVarietaVO = null;
		      try 
          {
			      tipoVarietaVO = anagFacadeClient.findTipoVarietaByPrimaryKey(storicoArboreaVO.getIdVarieta());
			      storicoArboreaVO.setTipoVarietaVO(tipoVarietaVO);
		      }
		      catch(SolmrException se) 
          {
			      messaggioErrore = AnagErrors.ERRORE_KO_TIPO_VARIETA;
			      request.setAttribute("messaggioErrore", messaggioErrore);
			      %>
		          <jsp:forward page="<%=popUnitaArboreeModificaUrl%>" />
		        <%
		      }
		    }
	    }
	    else 
      {
		    storicoArboreaVO.setIdVarieta(null); 
	    }
	    // SUPERFICIE VITATA
	    if(Validator.isNotEmpty(request.getParameterValues("area")[i])) 
      {
		    storicoArboreaVO.setArea(request.getParameterValues("area")[i]); 					
	    }
	    else 
      {
		    storicoArboreaVO.setArea(null); 
	    }
	    
	    // Anno Riferimento
	    if(Validator.isNotEmpty(request.getParameterValues("annoRiferimento")[i])) 
      {
		    storicoArboreaVO.setAnnoRiferimento(request.getParameterValues("annoRiferimento")[i]); 					
	    }
	    else 
      {
	  	  storicoArboreaVO.setAnnoRiferimento(null); 
	    }
	    // SESTO SU FILA
	    if(Validator.isNotEmpty(request.getParameterValues("sestoSuFila")[i])) 
      {
		    storicoArboreaVO.setSestoSuFila(request.getParameterValues("sestoSuFila")[i]); 					
	    }
	    else 
      {
		    storicoArboreaVO.setSestoSuFila(null); 
	    }
	    // SESTO TRA FILE
	    if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) 
      {
		    storicoArboreaVO.setSestoTraFile(request.getParameterValues("sestoTraFile")[i]); 					
	    }
	    else 
      {
		    storicoArboreaVO.setSestoTraFile(null); 
	    }
	    // NUMERO CEPPI
	    if(Validator.isNotEmpty(request.getParameterValues("numeroCeppi")[i])) 
      {
		    storicoArboreaVO.setNumCeppi(request.getParameterValues("numeroCeppi")[i]); 					
	    }
	    else 
      {
		    storicoArboreaVO.setNumCeppi(null); 
	    }
	     // PERCENTUALE FALLANZA
      if(Validator.isNotEmpty(request.getParameterValues("percentualeFallanza")[i])) 
      {
        try
        {
          storicoArboreaVO.setPercentualeFallanza(new BigDecimal(request.getParameterValues("percentualeFallanza")[i]));
        }
        catch (Exception e)
        {
          storicoArboreaVO.setPercentualeFallanza(new BigDecimal(0)); 
        }          
      }
      else 
      {
        storicoArboreaVO.setPercentualeFallanza(new BigDecimal(0)); 
      }
      // flagImproduttiva
      storicoArboreaVO.setFlagImproduttiva(request.getParameter("flagImproduttiva"+String.valueOf(i)));
      
      //Tipologia di vino
      TipoTipologiaVinoVO tipoTipologiaVinoVOTmp = null;
      String idTipologiaVino=request.getParameterValues("idTipologiaVino")[i];
      if(Validator.isNotEmpty(idTipologiaVino))
      {
        if((storicoVO.getIstatComune() != null)
         && (storicoArboreaVO.getIdVarieta() != null))
        {
          Long idTipologiaVinoLg = new Long(idTipologiaVino);
          storicoArboreaVO.setIdTipologiaVino(idTipologiaVinoLg);
          Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = anagFacadeClient
            .getListActiveTipoTipologiaVinoByComuneAndVarieta(storicoVO.getIstatComune(), storicoArboreaVO.getIdVarieta(), storicoVO.getIdParticella());
          //Tipologia di vino
          if(vTipoTipologiaVino != null)
          { 
            for(TipoTipologiaVinoVO tipoTipologiaVinoVO:vTipoTipologiaVino)
            {
              if(idTipologiaVinoLg.compareTo(tipoTipologiaVinoVO.getIdTipologiaVino()) == 0)
              {
                storicoArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
                tipoTipologiaVinoVOTmp = tipoTipologiaVinoVO;
                break;
              }   
            }
          }
        }
      }
      else
      {
        storicoArboreaVO.setIdTipologiaVino(null);
        storicoArboreaVO.setTipoTipologiaVinoVO(null);
      }
      
      //Menzione geografica dinamica
      if(Validator.isNotEmpty(tipoTipologiaVinoVOTmp))
      {
        if("S".equalsIgnoreCase(tipoTipologiaVinoVOTmp.getFlagGestioneMenzione()))
        {
          Vector<TipoMenzioneGeograficaVO> vTipoMenzioneGeografica = anagFacadeClient
            .getListTipoMenzioneGeografica(storicoArboreaVO.getIdParticella().longValue(),
              tipoTipologiaVinoVOTmp.getIdTipologiaVino(), null);
              
          //request.setAttribute("vTipoMenzioneGeografica", vTipoMenzioneGeografica);
          storicoArboreaVO.setElencoMenzioneGeografica(vTipoMenzioneGeografica);
        }      
      }   
      
      
      
      //Anno iscrizione albo
      if(Validator.isNotEmpty(request.getParameterValues("annoIscrizioneAlbo")[i]))
        storicoArboreaVO.setAnnoIscrizioneAlbo(request.getParameterValues("annoIscrizioneAlbo")[i]);          
      else
        storicoArboreaVO.setAnnoIscrizioneAlbo(null); 
      
      //Matricola
      if(Validator.isNotEmpty(request.getParameterValues("matricolaCCIAA")[i]))
        storicoArboreaVO.setMatricolaCCIAA(request.getParameterValues("matricolaCCIAA")[i]);          
      else 
        storicoArboreaVO.setMatricolaCCIAA(null); 
        
      
	    // FORMA ALLEVAMENTO
	    if(Validator.isNotEmpty(request.getParameterValues("idFormaAllevamento")[i])) 
      {
		    storicoArboreaVO.setIdFormaAllevamento(Long.decode(request.getParameterValues("idFormaAllevamento")[i])); 					
	    }
	    else 
      {
		    storicoArboreaVO.setIdFormaAllevamento(null); 
	    }
	    // PERCENTUALE VITIGNO
	    if(Validator.isNotEmpty(request.getParameterValues("percentualeVitigno")[i])) 
      {
		    storicoArboreaVO.setPercentualeVarieta(request.getParameterValues("percentualeVitigno")[i]); 					
	    }
	    else 
      {
		    storicoArboreaVO.setPercentualeVarieta(null); 
	    }
	    // ALTRI VITIGNI
	    storicoArboreaVO.setPresenzaAltriVitigni(request.getParameter("altroVitigno"+String.valueOf(i)));
	    // CAUSALE MODIFICA
	    if(Validator.isNotEmpty(request.getParameterValues("idCausaleModifica")[i])) 
      {
		    storicoArboreaVO.setIdCausaleModifica(Long.decode(request.getParameterValues("idCausaleModifica")[i])); 					
	    }
	    else 
      {
		    storicoArboreaVO.setIdCausaleModifica(null); 
	    }
	    storicoVO.setStoricoUnitaArboreaVO(storicoArboreaVO);
	    elencoStoricoParticellaArboreaVO[i] = storicoVO;
	  }
	  // Creo l'array di appoggio per gli altri vitigni in modo da poterlo sfruttare
	  // nel caso l'utente non confermi le operazioni dopo aver usato i pulsanti
	  // aggiungi e/o elimina della pop-up
	  storicoParticellaArboreaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[Integer.parseInt(numero)];
	  if(session.getAttribute("storicoParticellaArboreaVO") == null) 
    {
	    oldElencoAltriVitigni = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni();
	    session.setAttribute("oldElencoAltriVitigni", oldElencoAltriVitigni);
	  }
  }
  // Setto tutte le condizioni che possono scatenare il refresh della pagina
  // per le quali devo far visualizzare a video i nuovi dati inseriti dall'utente
  // e non i vecchi anche se il conferma non ha dato esito positivo.
  if(!Validator.isNotEmpty(pulisci) 
    && !Validator.isNotEmpty(request.getParameter("confermo")) 
    && !Validator.isNotEmpty(request.getParameter("operazione"))) 
  {
	  storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setElencoAltriVitigni(oldElencoAltriVitigni);
  }
  session.setAttribute("storicoParticellaArboreaVO", storicoParticellaArboreaVO);
	
  // Varietà altri vitigni
  Vector<TipoVarietaVO> elencoVarieta = null;
  if((storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdUtilizzo() != null)
    && (storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDestinazione() != null)
    && (storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso() != null)
    && (storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoQualitaUso() != null))    
  {
    try 
    {
	    elencoVarieta = anagFacadeClient.getListTipoVarietaVitignoByMatriceAndComune(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdUtilizzo(), 
	      storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDestinazione(), 
	      storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso(), 
	      storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoQualitaUso(), 
	      storicoParticellaArboreaVO.getIstatComune()); 
	    // Nel caso in cui non ci siano legami e quindi non vengano estratti
	    // records, recupero il vitigno solo in relazione all'utilizzo selezionato
	    if(elencoVarieta == null) 
      {
		    elencoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(
		      storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdUtilizzo(),
          storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDestinazione(), 
          storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso(), 
          storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdTipoQualitaUso());
	    }
	    request.setAttribute("elencoVarieta", elencoVarieta);
	  }
	  catch(SolmrException se) 
    {
	    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_VARIETA;
	    request.setAttribute("messaggioErrore", messaggioErrore);
	    %>
	      <jsp:forward page="<%=popUnitaArboreeModificaUrl%>" />
	    <%
	  }
  }
  
  	
  // L'utente ha selezionato il pulsante conferma
  if(Validator.isNotEmpty(request.getParameter("confermo"))) 
  {
 	  request.setAttribute("reload", "reload");
 	  String ricaduta = request.getParameter("ricaduta");
 	  String colturaSpecializzata = request.getParameter("colturaSpecializzata");
 	  // Se uno dei campi relativi all'unità arborea è valorizzato...
 	  Hashtable<Integer,ValidationErrors> erroriAltriVitigni = new Hashtable<Integer,ValidationErrors>();
 	  ValidationErrors errori = new ValidationErrors();
 	  int countErroriPercentuale = 0;
 	  int totPercentualeAltriVitigni = 0;
 	  if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni() != null) 
    {
	    for(int i = 0; i < storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni().length; i++) 
      { 
	      // Vitigno
	 	    if(!Validator.isNotEmpty(elencoIdVarieta[i])) 
        {
	 	      errori.add("idVarieta", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
	 	    }
	 	    else 
        {
	 	      // La varietà dell'altro vitigno deve essere diversa da quella dell'UV principale
	 	      int endIndex = elencoIdVarieta[i].indexOf("/");
	 	      Long idVarieta = Long.decode(elencoIdVarieta[i].substring(0, endIndex));
          //Controllo nel caso nel vitigno principale è stato messo null!!!
          if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdVarieta()))
          {
  	 		    if(idVarieta.compareTo(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdVarieta()) == 0) 
            {
  	 		      errori.add("idVarieta", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_VARIETA_UGUALE_VITIGNO_PRINCIPALE));
  	 		    }
          }
	 	    }
	 	    // Percentuale
	 	    if(!Validator.isNotEmpty(elencoPercentuale[i])) 
        {
	 	      errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
	 	      countErroriPercentuale++;
	 	    }
	 	    else 
        {
	 	      if(!Validator.isNumericInteger(elencoPercentuale[i]) 
            || Integer.parseInt(elencoPercentuale[i]) == 0 
            || Integer.parseInt(elencoPercentuale[i]) > 100) 
          {
	 		      errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	 		      countErroriPercentuale++;
	 	      }
	 	      else 
          {
	 		      totPercentualeAltriVitigni += Integer.parseInt(elencoPercentuale[i]);
	 	      }
	 	    }
	 	    erroriAltriVitigni.put(new Integer(i), errori);
      }
      if(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni().length > 0)
	    {
	    
		    if(countErroriPercentuale == 0)          
	      {
		      int percentualeVarieta = 0;
		 	    if(Validator.isNotEmpty(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getPercentualeVarieta())) 
	        {
		 	      percentualeVarieta = Integer.parseInt(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getPercentualeVarieta());
		      }
		 	    if((percentualeVarieta + totPercentualeAltriVitigni) > 100) 
	        {
		 	      errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_PERCENTUALE_ALTRI_VITIGNI_ERRATA));
		 	      erroriAltriVitigni.put(new Integer(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni().length - 1), errori);
		 	    }
	        if((percentualeVarieta + totPercentualeAltriVitigni) < 100) 
	        {
	          errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_PERCENTUALE_ALTRI_VITIGNI_MINORE_100));
	          erroriAltriVitigni.put(new Integer(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni().length - 1), errori);
	        }        
		    }
		  }
 	  }
    
    //Controlla Vigna max 1000 caratteri
    String vigna = request.getParameter("vigna");
    if(Validator.isNotEmpty(vigna))
    {
      vigna = vigna.trim();
    }
    if(Validator.isNotEmpty(vigna) && (vigna.length() > 1000))
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "vigna", AnagErrors.ERR_VIGNA);
    }
    
    //Controlla Annotazione in etichetta max 1000 caratteri
    String annotazioneEtichetta = request.getParameter("annotazioneEtichetta");
    if(Validator.isNotEmpty(annotazioneEtichetta))
    {
      annotazioneEtichetta = annotazioneEtichetta.trim();
    }
    if(Validator.isNotEmpty(annotazioneEtichetta) && (annotazioneEtichetta.length() > 1000))
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "annotazioneEtichetta", AnagErrors.ERR_ANNOTAZIONE_ETICHETTA);
    }
    
 		
   	// Se si sono verificati errori li visualizzo
   	if((errori.size() > 0) || ((errors !=null) && (errors.size() > 0))) 
    {
   	  request.setAttribute("errors", errors);
   	  request.setAttribute("erroriAltriVitigni", erroriAltriVitigni);
   	  request.getRequestDispatcher(popUnitaArboreeModificaUrl).forward(request, response);
   	  return;
   	}
 	  // Se ho passato tutti i controlli allora setto i nuovi valori all'interno del VO
 	  else 
    {
      
      if(Validator.isNotEmpty(vigna))
      {
        storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setVigna(vigna);
      }
      else
      {
        storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setVigna(null);
      }
      
      
      if(Validator.isNotEmpty(annotazioneEtichetta))
      {
        storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setEtichetta(annotazioneEtichetta);
      }
      else
      {
        storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setEtichetta(null);
      }
      
      String idMenzioneGeografica = request.getParameter("idMenzioneGeografica");
      if(Validator.isNotEmpty(idMenzioneGeografica))
      {
        storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setIdMenzioneGeografica(new Long(idMenzioneGeografica));
      }
      else
      {
        storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setIdMenzioneGeografica(null);
      }
      
      
  		storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setRicaduta(ricaduta);
  		storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setColturaSpecializzata(colturaSpecializzata);
  		// ALTRI VITIGNI
  		for(int i = 0; i < storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni().length; i++) 
      {
  			AltroVitignoVO altroVitignoVO = (AltroVitignoVO)storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni()[i];
  			int endIndex = elencoIdVarieta[i].indexOf("/");
  			altroVitignoVO.setIdVarieta(Long.decode(elencoIdVarieta[i].substring(0, endIndex)));
  			TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
  			tipoVarietaVO.setIdVarieta(Long.decode(elencoIdVarieta[i].substring(0, endIndex)));
  			tipoVarietaVO.setIdUtilizzo(Long.decode(elencoIdVarieta[i].substring(endIndex+1)));
  			altroVitignoVO.setTipoVarietaVO(tipoVarietaVO);
  			altroVitignoVO.setPercentualeVitigno(elencoPercentuale[i]);
  			storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni()[i] = altroVitignoVO;
  		}
  		// Se ho passato i controlli chiudo la pop up e aggiorno la pagina chiamante
  		elencoStoricoParticellaArboreaVO[Integer.parseInt(numero)] = storicoParticellaArboreaVO;
  		session.setAttribute("elencoStoricoParticellaArboreaVO", elencoStoricoParticellaArboreaVO);
  		session.removeAttribute("storicoParticellaArboreaVO");
  		session.removeAttribute("oldElencoAltriVitigni");
   		%>
    		<html>
    		  <head>
    			   <script type="text/javascript">
    					 window.opener.location.href='../layout/unitaArboreeModifica.htm';
    					 window.close();
    				 </script>
    			</head>
    		</html>
  	  <%
  	  return;
    }
  }
 	// L'utente ha cambiato i valori dalle combo degli utilizzi o ha scelto di pulire
 	// i dati di un utilizzo selezionato
 	else if(Validator.isNotEmpty(pulisci)) 
  {
 		request.setAttribute("reload", "reload");
 		request.getRequestDispatcher(popUnitaArboreeModificaUrl).forward(request, response);
	    return;
 	}
 	// L'utente ha premuto uno dei due pulsanti aggiungi/elimina
 	else if(Validator.isNotEmpty(request.getParameter("operazione"))) 
  {
 		Vector<AltroVitignoVO> temp = new Vector<AltroVitignoVO>();
 		Vector<AltroVitignoVO> tempRemove = new Vector<AltroVitignoVO>();
 		request.setAttribute("reload", "reload");
 		AltroVitignoVO[] elencoAltriVitigni = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni();
 		AltroVitignoVO[] elencoAltriVitigniVisualizza = null;
 		if(elencoAltriVitigni != null) 
    {
 			elencoAltriVitigniVisualizza = new AltroVitignoVO[storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni().length];
 			for(int i = 0; i < elencoAltriVitigni.length; i++) 
      {
 				AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAltriVitigni[i];
 				temp.add(altroVitignoVO);
 				AltroVitignoVO altroVitignoVisualizzaVO = new AltroVitignoVO();
 				if(Validator.isNotEmpty(request.getParameterValues("idVarieta")) 
          && Validator.isNotEmpty(request.getParameterValues("idVarieta")[i])) 
        {
 					int endIndex = request.getParameterValues("idVarieta")[i].indexOf("/");
 					altroVitignoVisualizzaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[i].substring(0, endIndex)));
 				}
 				if(Validator.isNotEmpty(request.getParameterValues("percentuale")) 
          && Validator.isNotEmpty(request.getParameterValues("percentuale")[i])) 
        {
 					altroVitignoVisualizzaVO.setPercentualeVitigno(request.getParameterValues("percentuale")[i]);
 				}
 				elencoAltriVitigniVisualizza[i] = altroVitignoVisualizzaVO;
 				tempRemove.add(altroVitignoVisualizzaVO);
 			}
 		}
 		// AGGIUNGI	
 		if(request.getParameter("operazione").equalsIgnoreCase("aggiungi")) 
    {
 			AltroVitignoVO nuovoAltroVitignoVO = new AltroVitignoVO();
 			nuovoAltroVitignoVO.setIdAltroVitigno(new Long(-1));
 			temp.add(nuovoAltroVitignoVO);
 			tempRemove.add(nuovoAltroVitignoVO);
 			elencoAltriVitigni = (AltroVitignoVO[])temp.toArray(new AltroVitignoVO[temp.size()]);
 			elencoAltriVitigniVisualizza = (AltroVitignoVO[])tempRemove.toArray(new AltroVitignoVO[tempRemove.size()]);
 			storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setElencoAltriVitigni(elencoAltriVitigni);
 			session.setAttribute("storicoParticellaArboreaVO", storicoParticellaArboreaVO);
 	 		request.getRequestDispatcher(popUnitaArboreeModificaUrl).forward(request, response);
 		  return;
 		}
 		// ELIMINA
 		else if(request.getParameter("operazione").equalsIgnoreCase("elimina")) 
    {
 			// Controllo che sia stato selezionato almeno un elemento
 			String[] altriVitigniDaEliminare = request.getParameterValues("numeroAltriVitigni");
 			if(altriVitigniDaEliminare == null || altriVitigniDaEliminare.length == 0) 
      {
 				errors = new ValidationErrors();
 				errors.add("error", new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT));
 				request.setAttribute("errors", errors);
 	 	    request.getRequestDispatcher(popUnitaArboreeModificaUrl).forward(request, response);
 	 	    return;
 			}
 			// Se sì...
 			else 
      {
 				// Elimino gli utilizzi selezionati
 				if(altriVitigniDaEliminare.length == storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getElencoAltriVitigni().length) 
        {
 					elencoAltriVitigni = new AltroVitignoVO[0];
 				}
 				else 
        {
 					int contatore = 0;
 					for(int i = 0; i < altriVitigniDaEliminare.length; i++) 
          {
 						String indiceToRemove = (String)altriVitigniDaEliminare[i];
 						if(i == 0) 
            {
 							temp.removeElementAt(Integer.parseInt(indiceToRemove));
 							tempRemove.removeElementAt(Integer.parseInt(indiceToRemove));
 						}
 						else 
            {
 							temp.removeElementAt(Integer.parseInt(indiceToRemove) - contatore);
 							tempRemove.removeElementAt(Integer.parseInt(indiceToRemove) - contatore);
 						}
 						contatore++;
 					}
 					elencoAltriVitigni = (AltroVitignoVO[])temp.toArray(new AltroVitignoVO[temp.size()]);
 					elencoAltriVitigniVisualizza = (AltroVitignoVO[])tempRemove.toArray(new AltroVitignoVO[tempRemove.size()]);
 					request.setAttribute("elencoAltriVitigniVisualizza", elencoAltriVitigniVisualizza);
 				}	
 				storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().setElencoAltriVitigni(elencoAltriVitigni);
 	 	 		session.setAttribute("storicoParticellaArboreaVO", storicoParticellaArboreaVO);
 	 	 		request.getRequestDispatcher(popUnitaArboreeModificaUrl).forward(request, response);
 	 		  return;
 			}
 		}
 	}
 	else 
  {
		%>
  	  <jsp:forward page="<%=popUnitaArboreeModificaUrl%>" />
    <%
 	}

%>


<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.profile.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.csi.wrapper.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "sianAnagrafeZootecnicaImportCtrl.jsp";
  %>
   	<%@include file = "/include/autorizzazione.inc" %>
  <%
    
  String sianAnagrafeZootecnicaBackUrl = "../view/sianAnagrafeZootecnicaView.jsp";
    
  //E' stato premuto il pulsante indietro quindi devo tornare alla pagina con 
  //l'elenco degli allevamenti da importare
  if ("indietro".equals(request.getParameter("operazione")))
  {
    request.getRequestDispatcher(sianAnagrafeZootecnicaBackUrl).forward(request, response);
    return;
  }
    

  String sianAnagrafeZootecnicaUrl = "../view/sianAnagrafeZootecnicaSelectView.jsp";
  String action = "../layout/visuraBDN.htm";
  String erroreImportUrl = "../layout/allevamenti.htm";
  String erroreUrl = "/view/erroreView.jsp";
  String allevamentiCtrlUrl = "/ctrl/allevamentiCtrl.jsp";


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  ValidationError error = null;
  ValidationErrors errors = new ValidationErrors();
  
  String idSpecieSiap[]=request.getParameterValues("idSpecieSiap");

    

   	
  // Controllo che l'utente abbia selezionato almeno un allevamento da importare
  if(idSpecieSiap == null || idSpecieSiap.length == 0) 
  {
  	error = new ValidationError((String)AnagErrors.get("ERR_NO_ALLEVAMENTI_SIAN_SELECTED_FOR_IMPORT"));
   	errors.add("error", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(sianAnagrafeZootecnicaUrl).forward(request, response);
   	return;
  }
  else
  {
    //Controllo che per tutti gli allevamenti selezionati sia stata scelta una specie
    for(int i=0;i<idSpecieSiap.length;i++)
    {
      if ("-1".equals(idSpecieSiap[i]))
      {
        request.getRequestDispatcher(sianAnagrafeZootecnicaUrl).forward(request, response);
        return;
      }
    }
  }
    
 	// Recupero gli allevamenti SIAN selezionati dall'utente
 	Hashtable<BigDecimal,SianAllevamentiVO> elencoAllevamentiSian = (Hashtable<BigDecimal,SianAllevamentiVO>)session.getAttribute("elencoAllevamentiSian");
  Enumeration<SianAllevamentiVO> enumeraAllevamenti = elencoAllevamentiSian.elements();
  
 	Vector<AllevamentoAnagVO> listaNuoviAllevamenti = new Vector<AllevamentoAnagVO>();
  int i=0;
 	while(enumeraAllevamenti.hasMoreElements()) 
  {
    SianAllevamentiVO sianAllevamentiVO = ((SianAllevamentiVO)enumeraAllevamenti.nextElement());
    if (sianAllevamentiVO.isSelect())
    {
     	AllevamentoAnagVO allevamentoAnagVO = new AllevamentoAnagVO();
  
     	// Se ho potuto contattare "COMUNE" e reperire l'ID_ASL procedo con il calcolo degli altri dati
     	// attraverso gli elementi reperiti dal SIAN
     	String istatComune = null;
     	String istatProvincia = null;
     	Long idUte = null;
     	Vector<Long> elencoIdUte = null;
      try 
      {
     		SolmrLogger.debug(this, "Valore di AZIENDA_CODICE in sianAnagrafeZootecnicaCtrl.jsp*************:"+sianAllevamentiVO.getAziendaCodice().substring(3,5)+"\n");
     		// Recupero l'istat della provincia in relazione al valore di AZIENDA_CODICE che mi arriva dal SIAN
     		istatProvincia = anagFacadeClient.getIstatProvinciaBySiglaProvincia(sianAllevamentiVO.getAziendaCodice().substring(3,5));
     		// Calcolo l'istat del comune e lo setto nel VO
     		SolmrLogger.debug(this, "Valore di ISTAT_PROVINCIA in sianAnagrafeZootecnicaCtrl.jsp*************:"+istatProvincia+"\n");
     		istatComune = istatProvincia.concat(sianAllevamentiVO.getAziendaCodice().substring(0,3));
     		allevamentoAnagVO.setIstatComuneAllevamento(istatComune);
     		// Recupero l'id_ute attraverso l'istat_comune appena calcolato e lo setto nel VO
     		elencoIdUte = anagFacadeClient.getListIdUteByIstatComuneAndIdAzienda(istatComune, anagAziendaVO.getIdAzienda(), true);
     		if(elencoIdUte != null && elencoIdUte.size() > 0) 
     		{
       		idUte = (Long)elencoIdUte.elementAt(0);
       		allevamentoAnagVO.setIdUTELong(idUte);
       		allevamentoAnagVO.setIdUTE(String.valueOf(idUte));
     		}
      }
      catch(SolmrException se) 
      {
    		if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_UTE_ATTIVE_FOR_COMUNE"))) 
    		{
       		error = new ValidationError(se.getMessage());
       		errors.add("error", error);
       		request.setAttribute("errors", errors);
       		request.getRequestDispatcher(sianAnagrafeZootecnicaUrl).forward(request, response);
       		return;
     		}
     		else 
     		{
       		Vector<UteVO> elencoUte = null;
       		try 
       		{
       			elencoUte = anagFacadeClient.getElencoUteAttiveForAzienda(anagAziendaVO.getIdAzienda());
       			UteVO uteVO = (UteVO)elencoUte.elementAt(0);
       			idUte = uteVO.getIdUte();
       			allevamentoAnagVO.setIdUTELong(idUte);
       			allevamentoAnagVO.setIdUTE(String.valueOf(idUte));
       		}
       		catch(SolmrException sex) 
       		{
      			String messaggio = (String)AnagErrors.get("ERR_IMPORTAZIONE_BDN_KO_FOR_NO_UTE");
      			request.setAttribute("messaggioErrore",messaggio);
      			request.setAttribute("pageBack", action);
      			%>
         			<jsp:forward page="<%=erroreUrl%>" />
      			<%
      			return;
       		}
     		}
     	}
  
     	// Contatto il servizio di comune per recuperare l'ID_ASL
     	AmmCompetenzaVO[] elencoAmmCompetenza = null;
     	AmmCompetenzaVO ammCompetenzaVO = null;
     	try 
     	{
        elencoAmmCompetenza = anagFacadeClient.serviceGetListAmmCompetenzaByComuneCollegato(allevamentoAnagVO.getIstatComuneAllevamento(), AgriConstants.TIPO_AMMINISTRAZIONE_ASL);
       	if(elencoAmmCompetenza!=null && elencoAmmCompetenza.length>0)
          ammCompetenzaVO = elencoAmmCompetenza[0];
      }
      catch(SystemException sie) 
      {
        SolmrLogger.error(this, "Catching System Exception generated by serviceGetListAmmCompetenzaByComuneCollegato in sianAnagrafeZootecnicaCtrl\n");
        String messaggio = (String)AnagErrors.get("ERR_IMPORTAZIONE_BDN_KO_FOR_COMUNE_ASL")+" "+allevamentoAnagVO.getIstatComuneAllevamento();
       	request.setAttribute("messaggioErrore",messaggio);
       	request.setAttribute("pageBack", sianAnagrafeZootecnicaUrl);
       	%>
        	<jsp:forward page="<%=erroreUrl%>" />
       	<%
       	return;
     	}
     	catch(SolmrException se) 
     	{
    		SolmrLogger.error(this, "Catching Solmr Exception generated by serviceGetListAmmCompetenzaByComuneCollegato in sianAnagrafeZootecnicaCtrl\n");
    		String messaggio = (String)AnagErrors.get("ERR_IMPORTAZIONE_BDN_KO_FOR_COMUNE_ASL")+" "+allevamentoAnagVO.getIstatComuneAllevamento();
    		request.setAttribute("messaggioErrore",messaggio);
    		request.setAttribute("pageBack", sianAnagrafeZootecnicaUrl);
    		%>
      		<jsp:forward page="<%=erroreUrl%>" />
    		<%
    		return;
     	}
  
      TipoASLAnagVO tipoASLAnagVO = null;
      try 
      {
     	  if(ammCompetenzaVO != null) 
     	  {
      		tipoASLAnagVO = anagFacadeClient.getTipoASLAnagVOByExtIdAmmCompetenza(Long.decode(ammCompetenzaVO.getIdAmmCompetenza()), true);
     		}
   		}
   		catch(SolmrException se) 
   		{
  			if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_ASL_FOUND_FOR_ID_AMM_COMPETENZA"))) 
  			{
    			String messaggio = (String)AnagErrors.get("ERR_GENERIC_IMPORTAZIONE_BDN_KO")+" "+se.getMessage();
    			request.setAttribute("messaggioErrore",messaggio);
    			request.setAttribute("pageBack", sianAnagrafeZootecnicaUrl);
    			%>
       			<jsp:forward page="<%=erroreUrl%>" />
    			<%
    			return;
  			}
   		}
  
   		if(tipoASLAnagVO != null) 
   		{
     		allevamentoAnagVO.setIdASL(tipoASLAnagVO.getIdASL());
     		allevamentoAnagVO.setIdASLLong(tipoASLAnagVO.getIdASLLong());
   		}
  
	   	// Recupero l'id specie animale in relazione al codice specie proveniente dal SIAN e, nel caso
	   	// in cui non riuscissi a codificarlo, blocco l'operazione
	
	    if (!"-1".equals(idSpecieSiap[i]))
	    {
	      allevamentoAnagVO.setIdSpecieAnimale(idSpecieSiap[i]);
	      allevamentoAnagVO.setIdAllevamentoLong(new Long(idSpecieSiap[i]));
	    }
	    else
	    {
	      String messaggio = (String)AnagErrors.get("ERR_IMPORTAZIONE_BDN_KO_FOR_SPECIE_ANIMALE")+" "+sianAllevamentiVO.getSpeCodice()+" e descrizione: "+sianAllevamentiVO.getDescrizioneSpecie();
	      request.setAttribute("messaggioErrore",messaggio);
	      request.setAttribute("pageBack", erroreImportUrl);
	      %>
	        <jsp:forward page="<%=erroreUrl%>" />
	      <%
	      return;
	    }
	    
	    
	    TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = null;
      try 
      {
        tipoSpecieAnimaleAnagVO = anagFacadeClient.getTipoSpecieAnimale(new Long(idSpecieSiap[i]));
      }
      catch(SolmrException se) 
      {
        String messaggio = "Errore nel ricavo della specie"+" "+se.getMessage();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", sianAnagrafeZootecnicaUrl);
        %>
          <jsp:forward page="<%=erroreUrl%>" />
        <%
        return;
      }
	    
	    //devo prendere il comune solo per alcune specie dalla descrizione che arriva dal sian.
	    if("N".equalsIgnoreCase(tipoSpecieAnimaleAnagVO.getFlagControlloComune()))
	    {
	      String istatComuneDiretto = "";
	      if(Validator.isNotEmpty(sianAllevamentiVO.getComune()))
	      {
		      try 
		      {
		        istatComuneDiretto = anagFacadeClient.getIstatByDescComune(sianAllevamentiVO.getComune().trim().toUpperCase());
		      }
		      catch(SolmrException se) 
		      {
		        String messaggio = "Errore nel ricavo della specie"+" "+se.getMessage();
		        request.setAttribute("messaggioErrore",messaggio);
		        request.setAttribute("pageBack", sianAnagrafeZootecnicaUrl);
		        %>
		          <jsp:forward page="<%=erroreUrl%>" />
		        <%
		        return;
		      }
		    }
		    
		    if(Validator.isEmpty(istatComuneDiretto))
		    {
		      String messaggio = "Errore nel ricavare il comune direttamente da BDN";
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", sianAnagrafeZootecnicaUrl);
          %>
            <jsp:forward page="<%=erroreUrl%>" />
          <%
          return;
		    }
		    
	      allevamentoAnagVO.setIstatComuneAllevamento(istatComuneDiretto);
	    }
  
   		allevamentoAnagVO.setCodiceAziendaZootecnica(sianAllevamentiVO.getAziendaCodice());
   		allevamentoAnagVO.setDataInizioDate(new Date(System.currentTimeMillis()));
   		SolmrLogger.debug(this, "Value of data inizio in sianAnagrafeZootecnicaCtrl.jsp: "+allevamentoAnagVO.getDataInizioDate()+"\n");
   		SolmrLogger.debug(this, "Value of date current in sianAnagrafeZootecnicaCtrl.jsp: "+DateUtils.getCurrent()+"\n");
   		allevamentoAnagVO.setNote("Importazione da BDN del: "+DateUtils.getCurrent());
   		if(Validator.isNotEmpty(sianAllevamentiVO.getIndirizzo())) 
   		{
     	  allevamentoAnagVO.setIndirizzo(sianAllevamentiVO.getIndirizzo().trim());
   		}
      allevamentoAnagVO.setCap(sianAllevamentiVO.getCap());
  
      allevamentoAnagVO.setCodiceFiscaleProprietario(sianAllevamentiVO.getCodFiscaleProp());
      allevamentoAnagVO.setDenominazioneProprietario(sianAllevamentiVO.getDenomProprietario());
      allevamentoAnagVO.setCodiceFiscaleDetentore(sianAllevamentiVO.getCodFiscaleDeten());
      allevamentoAnagVO.setDenominazioneDetentore(sianAllevamentiVO.getDenomDetentore());

      if(Validator.isNotEmpty(sianAllevamentiVO.getDtInizioDetentore()))
        allevamentoAnagVO.setDataInizioDetenzione(StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtInizioDetentore()));

      if(Validator.isNotEmpty(sianAllevamentiVO.getDtFineDetentore()))
        allevamentoAnagVO.setDataFineDetenzione(StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtFineDetentore()));



      if (it.csi.solmr.etc.SolmrConstants.FLAG_S.equals(sianAllevamentiVO.getSoccida()))
        allevamentoAnagVO.setFlagSoccida(true);
      else  allevamentoAnagVO.setFlagSoccida(false);

      allevamentoAnagVO.setIdTipoProduzione(sianAllevamentiVO.getIdTipoProduzione());
      allevamentoAnagVO.setIdOrientamentoProduttivo(sianAllevamentiVO.getIdOrientamentoProduttivo());
      allevamentoAnagVO.setDenominazione(sianAllevamentiVO.getDenominazione());
      allevamentoAnagVO.setLatitudine(sianAllevamentiVO.getLatitudine());
      allevamentoAnagVO.setLongitudine(sianAllevamentiVO.getLongitudine());
      //Bdn ritorna la data in formato ad minchiam...
      String dataInizioValidita = null;
      if(Validator.isNotEmpty(sianAllevamentiVO.getDtInizioAttivita()))
      {
        Date dataInizio = DateUtils.parseDateTeramo(sianAllevamentiVO.getDtInizioAttivita());
        dataInizioValidita = DateUtils.formatDate(dataInizio);
      }
      allevamentoAnagVO.setDataInizioAttivita(dataInizioValidita);
  
      listaNuoviAllevamenti.add(allevamentoAnagVO);
      i++;
    }
  }

	// Procedo con l'importazione dei dati dalla BDN
	try 
	{
 		for(i = 0; i < listaNuoviAllevamenti.size(); i++) 
 		{
   		AllevamentoAnagVO allevamentoVO = (AllevamentoAnagVO)listaNuoviAllevamenti.elementAt(i);
   		anagFacadeClient.insertAllevamento(allevamentoVO , ruoloUtenza.getIdUtente());
 		}
	}
	catch(SolmrException se) 
	{
 		String messaggio = (String)AnagErrors.get("ERR_GENERIC_IMPORTAZIONE_BDN_KO")+" "+se.getMessage();
 		request.setAttribute("messaggioErrore",messaggio);
 		request.setAttribute("pageBack", sianAnagrafeZootecnicaUrl);
 		%>
    		<jsp:forward page="<%=erroreUrl%>" />
 		<%
 		return;
	}

	// Rimuovo dalla sessione il precedente vettore selezionato
	session.removeAttribute("elencoAllevamentiSian");
	%>
  	<jsp:forward page="<%=allevamentiCtrlUrl%>" />
	

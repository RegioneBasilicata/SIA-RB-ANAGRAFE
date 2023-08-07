<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

  String iridePageName = "ricercaRichiestaCtrl.jsp";
  
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  final String errMsg = "Impossibile procedere nella sezione valida del ricerca richiesta azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  int sizeResult = 0;
  String errorPage = "/view/ricercaRichiestaView.jsp";
  String ricercaRichiestaURL = "/view/ricercaRichiestaView.jsp";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/ricercaRichiesta.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String accessoAziendaURL = "/ctrl/accessoAziendaCtrl.jsp";
  String annullaRichiestaURL = "/ctrl/annullaRichiestaCtrl.jsp";

  int paginaCorrente = 0;
  Integer paginaCorrenteInteger;
  String dataAvanzata="";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String annullaRichiesta = request.getParameter("annullaRichiesta");
  
  boolean flagGestoreCaa = false;
  Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
  if(vActor.contains(SolmrConstants.GESTORE_CAA))
  {
    flagGestoreCaa = true;
  }

  Vector<CodeDescription> vTipoRichiesta = gaaFacadeClient.getListTipoRichiesta();
  if(Validator.isNotEmpty(vTipoRichiesta))
  {
    CodeDescription cd = new CodeDescription();
    cd.setCode(new Integer(0));
    vTipoRichiesta.add(0, cd);
  }
  request.setAttribute("vTipoRichiesta", vTipoRichiesta);
  Vector<StatoRichiestaVO> vStatoRichiesta = gaaFacadeClient.getListStatoRichiesta();
  if(Validator.isNotEmpty(vStatoRichiesta))
  {
    StatoRichiestaVO statoRichiestaVO = new StatoRichiestaVO();
    statoRichiestaVO.setIdStatoRichiesta(new Integer(0));
    vStatoRichiesta.add(0, statoRichiestaVO);
  }
  request.setAttribute("vStatoRichiesta", vStatoRichiesta);
  
  String operazione = request.getParameter("operazione");
  String regimeRicercaRichiesta = request.getParameter("regimeRicercaRichiesta");

  ValidationErrors errors = new ValidationErrors();
  String richiestaAziendaSel = request.getParameter("idRichiestaAzienda");
  if(Validator.isNotEmpty(richiestaAziendaSel)) 
  {
    request.setAttribute("idRichiestaAziendaSel", Long.decode(richiestaAziendaSel));
  }
  
  String idTipoRichiestaStr = request.getParameter("idTipoRichiesta");
  Long idTipoRichiesta = null;
  if(Validator.isNotEmpty(idTipoRichiestaStr))
  {
    idTipoRichiesta = new Long(idTipoRichiestaStr);
    request.setAttribute("idTipoRichiestaSel", idTipoRichiesta);
  }
  else if(Validator.isNotEmpty(annullaRichiesta))
  {
    operazione = "aggiorna";
    idTipoRichiesta = (Long)session.getAttribute("idTipoRichiesta");
    request.setAttribute("idTipoRichiestaSel", idTipoRichiesta);
  }
  
  String idStatoRichiestaStr = request.getParameter("idStatoRichiesta");
  Long idStatoRichiesta = null;
  if(Validator.isNotEmpty(idStatoRichiestaStr))
  {
    idStatoRichiesta = new Long(idStatoRichiestaStr);
    request.setAttribute("idStatoRichiestaSel", idStatoRichiesta);
  }
  else if(Validator.isNotEmpty(annullaRichiesta))
  {
    idStatoRichiesta = (Long)session.getAttribute("idStatoRichiesta");
    request.setAttribute("idStatoRichiestaSel", idStatoRichiesta);
  }
  
  String cuaaRic = request.getParameter("cuaaRic");
  if(Validator.isNotEmpty(cuaaRic))
  {
    request.setAttribute("cuaaRic", cuaaRic);
  }
  else if(Validator.isNotEmpty(annullaRichiesta))
  {
    cuaaRic = (String)session.getAttribute("cuaaRic");
    request.setAttribute("cuaaRic", cuaaRic);
  }
  
  String partitaIvaRic = request.getParameter("partitaIvaRic");
  if(Validator.isNotEmpty(partitaIvaRic))
  {
    request.setAttribute("partitaIvaRic", partitaIvaRic);
  }
  else if(Validator.isNotEmpty(annullaRichiesta))
  {
    partitaIvaRic = (String)session.getAttribute("partitaIvaRic");
    request.setAttribute("partitaIvaRic", partitaIvaRic);
  }
  
  String denominazioneRic = request.getParameter("denominazioneRic");
  if(Validator.isNotEmpty(denominazioneRic))
  {
    request.setAttribute("denominazioneRic", denominazioneRic);
  }
  else if(Validator.isNotEmpty(annullaRichiesta))
  {
    denominazioneRic = (String)session.getAttribute("denominazioneRic");
    request.setAttribute("denominazioneRic", denominazioneRic);
  }
  
  if(Validator.isNotEmpty(regimeRicercaRichiesta) 
    && Validator.isEmpty(idTipoRichiesta)
    && Validator.isEmpty(idStatoRichiesta)
    && Validator.isEmpty(cuaaRic)
    && Validator.isEmpty(partitaIvaRic)
    && Validator.isEmpty(denominazioneRic))
  {
    errors.add("idTipoRichiesta", new ValidationError(
       AnagErrors.ERR_STATO_TIPO_RICHIESTA));
    errors.add("idStatoRichiesta", new ValidationError(
       AnagErrors.ERR_STATO_TIPO_RICHIESTA));
    errors.add("cuaaRic", new ValidationError(
       AnagErrors.ERR_STATO_TIPO_RICHIESTA));
    errors.add("partitaIvaRic", new ValidationError(
       AnagErrors.ERR_STATO_TIPO_RICHIESTA));
    errors.add("denominazioneRic", new ValidationError(
       AnagErrors.ERR_STATO_TIPO_RICHIESTA));
  }
  
  if(errors != null && errors.size() > 0) 
  {
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(ricercaRichiestaURL).forward(request, response);
    return;
  }

  //prima volta che entro
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {     
    request.setAttribute("action", action);
    operazione = "valida";
    request.setAttribute("operazione", operazione);
    session.setAttribute("idRichiestaAziendaValida", Long.decode(richiestaAziendaSel));
    session.setAttribute("idTipoRichiesta", idTipoRichiesta);
    session.setAttribute("idStatoRichiesta", idStatoRichiesta);
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }


  //se faccio aggiorna riparto dalla prima!!!!
  if(Validator.isNotEmpty(operazione) && "aggiorna".equalsIgnoreCase(operazione))
  { 
    session.removeAttribute("listIdRicercaAzienda");
    try 
    {
      if(Validator.isNotEmpty(cuaaRic))
        cuaaRic = cuaaRic.trim();
      if(Validator.isNotEmpty(partitaIvaRic))
        partitaIvaRic = partitaIvaRic.trim();
      if(Validator.isNotEmpty(denominazioneRic))
        denominazioneRic = denominazioneRic.trim();    
      
      Vector<Long> vectIdRichiestaAzienda = null;
      if(flagGestoreCaa)
      {
        vectIdRichiestaAzienda = gaaFacadeClient.getElencoIdRichiestaAziendaGestCaa(idTipoRichiesta, 
          idStatoRichiesta, cuaaRic, partitaIvaRic, denominazioneRic, ruoloUtenza);
      }
      else
      {
        vectIdRichiestaAzienda = gaaFacadeClient.getElencoIdRichiestaAzienda(idTipoRichiesta, 
          idStatoRichiesta, cuaaRic, partitaIvaRic, denominazioneRic, null, ruoloUtenza);
      }
       
      
      
      session.setAttribute("listIdRicercaAzienda", vectIdRichiestaAzienda);
      if(vectIdRichiestaAzienda!=null)
      {        
        sizeResult = vectIdRichiestaAzienda.size();
       
        paginaCorrente = 1;
        Vector<Long> rangeIdAA = new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdRichiestaAzienda.elementAt(i));
        }
        
        Vector<AziendaNuovaVO> rangeAziendaNuova = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(rangeIdAA);
        request.setAttribute("listRangeAziendaNuova", rangeAziendaNuova);

        
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);

      }
      else
      {
        String messaggio = "Non sono presenti richieste inviate da aziende";
        request.setAttribute("messaggio", messaggio);
      }
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  }  
  else if(Validator.isNotEmpty(operazione) && "avanti".equalsIgnoreCase(operazione))
  {    
    try 
    {
      Vector<Long> vectIdRichiestaAzienda = (Vector<Long>)session.getAttribute("listIdRicercaAzienda");
      if(vectIdRichiestaAzienda !=null)
      {
        sizeResult = vectIdRichiestaAzienda.size();
        paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
        if(paginaCorrenteInteger.toString().equals(request.getParameter("totalePagine")))
          paginaCorrente = paginaCorrenteInteger.intValue();
        else
          paginaCorrente = paginaCorrenteInteger.intValue()+1;
        Vector<Long> rangeIdAA = new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG; i<limiteA; i++)
        {
          rangeIdAA.addElement(vectIdRichiestaAzienda.elementAt(i));
        }
        
        Vector<AziendaNuovaVO> rangeAziendaNuova = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(rangeIdAA);
        request.setAttribute("listRangeAziendaNuova", rangeAziendaNuova);

        session.removeAttribute("currPage");
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);

      }
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  }
  else if(Validator.isNotEmpty(operazione) && "indietro".equalsIgnoreCase(operazione))
  {
    
    try 
    {
      Vector<Long> vectIdRichiestaAzienda = (Vector<Long>)session.getAttribute("listIdRicercaAzienda");
      
      if(vectIdRichiestaAzienda!=null)
      {        
        sizeResult = vectIdRichiestaAzienda.size();
        paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
        if(paginaCorrenteInteger.toString().equals("1"))
          paginaCorrente = paginaCorrenteInteger.intValue();
        else
          paginaCorrente = paginaCorrenteInteger.intValue()-1;
        Vector<Long> rangeIdAA = new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdRichiestaAzienda.elementAt(i));
        }
        Vector<AziendaNuovaVO> rangeAziendaNuova = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(rangeIdAA);
        request.setAttribute("listRangeAziendaNuova", rangeAziendaNuova);

        session.removeAttribute("currPage");
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);
      }
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  }
  else if(Validator.isNotEmpty(operazione) && "valida".equalsIgnoreCase(operazione))
  {
    String messaggio = "";
    try 
    {
    
      Long idRichiestaAziendaValida = (Long)session.getAttribute("idRichiestaAziendaValida");
      Vector<Long> vIdRichTmp = new Vector<Long>();
      vIdRichTmp.add(idRichiestaAziendaValida);
      Vector<AziendaNuovaVO> vAziendaNuovaVOTmp  = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichTmp);
      if(vAziendaNuovaVOTmp.get(0).getIdStatoRichiesta().longValue() != SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA)
      {
        messaggio = "Non è possibile prendere in carico la richiesta selezionata";
      }
      
      if(Validator.isEmpty(messaggio))
      {
        if(!gaaFacadeClient.isUtenteAbilitatoPresaInCarico(vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue(),
          ruoloUtenza.getCodiceRuolo()))
        {
          messaggio = "Utente non abilitato alla presa in carico";
        }      
      }
      
      
      if(Validator.isEmpty(messaggio))
      {
	      if (ruoloUtenza.isUtenteRegionale()
	        && Validator.isNotEmpty(vAziendaNuovaVOTmp.get(0).getIdAzienda()))
	      {
	        AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(vAziendaNuovaVOTmp.get(0).getIdAzienda());
	        if(anagAziendaVO.isPossiedeDelegaAttiva())
	           messaggio = AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;	      
	      }
	    }
      
      if(Validator.isEmpty(messaggio))
      {
	      if(vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue()  == SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE)
	      {
		      //Controllo che l'azienda da cui provengo sia cessata...
		      if(vAziendaNuovaVOTmp.get(0).getIdAziendaSubentro() != null)
		      {
		        AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(
		          vAziendaNuovaVOTmp.get(0).getIdAziendaSubentro());
		        if(Validator.isEmpty(anagAziendaVO.getDataCessazione()))
		        {
		          messaggio = "Attenzione: l'azienda di provenienza indicata nell'iscrizione " +
		            "risulta ancora attiva. Procedere a cessare l'azienda prima della validazione.";
		          request.setAttribute("messaggio", messaggio);
		        }
		      }
		    }
		  }
	      
	    if(Validator.isEmpty(messaggio))
	    {
	      if((vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() != SolmrConstants.RICHIESTA_CESSAZIONE)
	        && (vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() != SolmrConstants.RICHIESTA_VALIDAZIONE))
	      {
	        PlSqlCodeDescription plCode = null;
	     
			    try
			    {
			      plCode = gaaFacadeClient.ribaltaAziendaPlSql(idRichiestaAziendaValida.longValue());
			    }
			    catch (SolmrException se) 
			    {
			      SolmrLogger.info(this, " - ricercaRichiestaCtrl.jsp - FINE PAGINA");
			      messaggio = errMsg+""+se.toString();
			      request.setAttribute("messaggioErrore",messaggio);
			      request.setAttribute("pageBack", action);
			      %>
			        <jsp:forward page="<%= erroreViewUrl %>" />
			      <%
			      return;
			    }
			    
			    
			    if((plCode !=null) && (Validator.isNotEmpty(plCode.getOtherdescription())))
			    {
			      messaggio = plCode.getOtherdescription(); 
			    }
	      
	      }
	    }
    
    
      //Rimango dove sono!!!
      if(Validator.isNotEmpty(messaggio))
      {
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("idRichiestaAziendaSel", idRichiestaAziendaValida);
        request.setAttribute("idTipoRichiestaSel", session.getAttribute("idTipoRichiesta"));
        request.setAttribute("idStatoRichiestaSel", session.getAttribute("idStatoRichiesta"));
        Vector<Long> vectIdRichiestaAzienda = (Vector<Long>)session.getAttribute("listIdRicercaAzienda");
              
	      if(vectIdRichiestaAzienda!=null)
	      {        
	        sizeResult = vectIdRichiestaAzienda.size();
	        paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
	        paginaCorrente = paginaCorrenteInteger.intValue();
	        
	        Vector<Long> rangeIdAA = new Vector<Long>();
	        int limiteA;
	        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
	          limiteA=sizeResult;
	        else
	          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
	        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
	          i<limiteA;i++)
	        {
	          rangeIdAA.addElement(vectIdRichiestaAzienda.elementAt(i));
	        }
	        Vector<AziendaNuovaVO> rangeAziendaNuova = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(rangeIdAA);
	        request.setAttribute("listRangeAziendaNuova", rangeAziendaNuova);
	
	        session.removeAttribute("currPage");
	        paginaCorrenteInteger = new Integer(paginaCorrente);
	        session.setAttribute("currPage",paginaCorrenteInteger);
	      }
      
      }
      else
      {
            
	      if((vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue()  == SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE)
	        || (vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue()  == SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA))
	      {
	        //Carico file/documenti su agriwell
	        vAziendaNuovaVOTmp  = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichTmp);
	        DocumentoVO documentoRicercaVO = new DocumentoVO();
	        documentoRicercaVO.setCuaa(vAziendaNuovaVOTmp.get(0).getCuaa());
	        documentoRicercaVO.setIdAzienda(vAziendaNuovaVOTmp.get(0).getIdAzienda());
	        Vector<DocumentoVO> elencoDocumenti = anagFacadeClient.searchDocumentiByParameters(documentoRicercaVO, null, null);
	            
	        if(elencoDocumenti != null)
	        {
	          for(int i=0;i<elencoDocumenti.size();i++)
	          {
	            DocumentoVO documentoVO = elencoDocumenti.get(i);
	            if(documentoVO.getvAllegatoDocumento() != null)
	            {
	              for(int j=0;j<documentoVO.getvAllegatoDocumento().size();j++)
	              {
	                AllegatoDocumentoVO allegatoVO = documentoVO.getvAllegatoDocumento().get(j);
	                allegatoVO = gaaFacadeClient.getFileAllegato(allegatoVO.getIdAllegato());
	                Date DateTmp = new Date();
	                allegatoVO.setIdDocumento(documentoVO.getIdDocumento());
						      allegatoVO.setDataRagistrazione(DateTmp);
						      allegatoVO.setDataUltimoAggiornamento(DateTmp);
						      allegatoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());                
	                gaaFacadeClient.insertFileAllegatoAgriWell(allegatoVO);                              
	              }
	            }
	          }
	        }
	        
	        //carico documento stampa su agriwell
	        RichiestaAziendaVO richiestaAziendaVO = gaaFacadeClient.getPdfAziendaNuova(
	          vAziendaNuovaVOTmp.get(0).getIdRichiestaAzienda());
	          
	        gaaFacadeClient.insertFileRichiestaAgriWell(vAziendaNuovaVOTmp.get(0), 
	          richiestaAziendaVO.getFileStampa(), ruoloUtenza);	        
		    }
		    
		    if((vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_CESSAZIONE)
          || (vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_VALIDAZIONE))
        {
        
          IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
			    iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_VALIDATA);
			    gaaFacadeClient.aggiornaStatoRichiestaValCess(vAziendaNuovaVOTmp.get(0), ruoloUtenza.getIdUtente().longValue(), 
			      iterRichiestaAziendaVO);        
        }
        
        //vado nel dettaglio        
        Long idAzienda = vAziendaNuovaVOTmp.get(0).getIdAzienda();       
        session.removeAttribute("anagAziendaVO");
        
        //AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(idAzienda);
        //Passo questo parametro per indicare che arrivo dal dettaglio ricerca
        //per avitare problemi ruolo/iride col mandato (dopo introduzione caa_limitato elenco soci ndr)  
        request.setAttribute("RICERCAJSP","RICERCAJSP");
        request.setAttribute("idAziendaAccesso",idAzienda.toString());
        %>
          <jsp:forward page = "<%=accessoAziendaURL%>" />
        <%
        return;  
		  }
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  }
  else if(Validator.isNotEmpty(operazione) && "annulla".equalsIgnoreCase(operazione))
  {
  
    session.setAttribute("idRichiestaAziendaValida", Long.decode(richiestaAziendaSel));
    session.setAttribute("idTipoRichiesta", idTipoRichiesta);
    session.setAttribute("idStatoRichiesta", idStatoRichiesta); 
    try 
    {
      String idRichiestaAzienda = request.getParameter("idRichiestaAzienda");
      Long idRichiestaAziendaLg = new Long(idRichiestaAzienda);
      Vector<Long> vIdRichTmp = new Vector<Long>();
      vIdRichTmp.add(idRichiestaAziendaLg);
      Vector<AziendaNuovaVO> vAziendaNuovaVOTmp  = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichTmp);
      if(vAziendaNuovaVOTmp.get(0).getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA) > 0)
      {      
        Vector<Long> vectIdRichiestaAzienda = (Vector<Long>)session.getAttribute("listIdRicercaAzienda");            
        sizeResult = vectIdRichiestaAzienda.size();
       
        //paginaCorrente = 1;
        paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));        
        paginaCorrente = paginaCorrenteInteger.intValue();
        
        Vector<Long> rangeIdAA = new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdRichiestaAzienda.elementAt(i));
        }
        
        Vector<AziendaNuovaVO> rangeAziendaNuova = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(rangeIdAA);
        request.setAttribute("listRangeAziendaNuova", rangeAziendaNuova);

        
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);
      
      
      
      
        ValidationError error = new ValidationError(AnagErrors.ERR_NO_ANNULLA_VALIDATA);
	      errors.add("error", error);
	      request.setAttribute("errors", errors);
	      request.getRequestDispatcher(errorPage).forward(request, response);
	      return;      
      }
      
      
      
      
      %>
        <jsp:forward page = "<%=annullaRichiestaURL %>" />
      <%
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  }
  else if(Validator.isNotEmpty(operazione) && "dettaglio".equalsIgnoreCase(operazione))
  {
  
    String idRichiestaAzienda = request.getParameter("idRichiestaAzienda");
    Long idRichiestaAziendaLg = new Long(idRichiestaAzienda);
    Vector<Long> vIdRichTmp = new Vector<Long>();
    vIdRichTmp.add(idRichiestaAziendaLg);
    Vector<AziendaNuovaVO> vAziendaNuovaVOTmp  = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichTmp);
    if(Validator.isEmpty(vAziendaNuovaVOTmp.get(0).getIdAzienda()))
    {
      request.setAttribute("idRichiestaAziendaSel", idRichiestaAziendaLg);
      request.setAttribute("idTipoRichiestaSel", session.getAttribute("idTipoRichiesta"));
      request.setAttribute("idStatoRichiestaSel", session.getAttribute("idStatoRichiesta"));
      Vector<Long> vectIdRichiestaAzienda = (Vector<Long>)session.getAttribute("listIdRicercaAzienda");
            
      if(vectIdRichiestaAzienda!=null)
      {        
        sizeResult = vectIdRichiestaAzienda.size();
        paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
        paginaCorrente = paginaCorrenteInteger.intValue();
        
        Vector<Long> rangeIdAA = new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdRichiestaAzienda.elementAt(i));
        }
        Vector<AziendaNuovaVO> rangeAziendaNuova = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(rangeIdAA);
        request.setAttribute("listRangeAziendaNuova", rangeAziendaNuova);

        session.removeAttribute("currPage");
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);
      }
      
      
      String messaggio = "Non e' possibile accedere al dettaglio dell'azienda se la richiesta "+
        "di iscrizione non e' ancora stata validata";
      request.setAttribute("messaggio", messaggio);
    
    }
    else
    {
      //vado nel dettaglio        
	    Long idAzienda = vAziendaNuovaVOTmp.get(0).getIdAzienda();       
	    session.removeAttribute("anagAziendaVO");
	    
	    //AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(idAzienda);
	    //Passo questo parametro per indicare che arrivo dal dettaglio ricerca
	    //per avitare problemi ruolo/iride col mandato (dopo introduzione caa_limitato elenco soci ndr)  
	    request.setAttribute("RICERCAJSP","RICERCAJSP");
	    request.setAttribute("idAziendaAccesso",idAzienda.toString());
	    %>
	      <jsp:forward page = "<%=accessoAziendaURL%>" />
	    <%
	    return;         
    
    } 
  
  
    
  }
  else
  {
    session.removeAttribute("listIdRicercaAzienda");
    try 
    {
      Vector<Long> vectIdRichiestaAzienda = null;
      if(flagGestoreCaa)
      {
        vectIdRichiestaAzienda = gaaFacadeClient.getElencoIdRichiestaAziendaGestCaa(null, 
          SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA, null, null, null, ruoloUtenza);
      }
      else
      {
        vectIdRichiestaAzienda = gaaFacadeClient.getElencoIdRichiestaAzienda(null, 
          SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA, null, null, null, null, ruoloUtenza);
      }
      
      session.setAttribute("listIdRicercaAzienda", vectIdRichiestaAzienda);
      if(vectIdRichiestaAzienda!=null)
      {        
        sizeResult = vectIdRichiestaAzienda.size();
       
        paginaCorrente = 1;
        Vector<Long> rangeIdAA = new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdRichiestaAzienda.elementAt(i));
        }
        
        Vector<AziendaNuovaVO> rangeAziendaNuova = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(rangeIdAA);
        request.setAttribute("listRangeAziendaNuova", rangeAziendaNuova);

        
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);

      }
      else
      {
        String messaggio = "Non sono presenti richieste inviate da aziende";
        request.setAttribute("messaggio", messaggio);
      }
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  
  }  
  


%>
  <jsp:forward page = "<%=ricercaRichiestaURL %>" />

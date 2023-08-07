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
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "elencoRicercaRichiestaCtrl.jsp";
  
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  final String errMsg = "Impossibile procedere nella sezione elenco richieste azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  int sizeResult = 0;
  String elencoRicercaRichiestaURL = "/view/elencoRicercaRichiestaView.jsp";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/elencoRicercaRichiesta.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String accessoAziendaURL = "/ctrl/accessoAziendaCtrl.jsp";
  String annullaRichiestaURL = "/ctrl/annullaRichiestaCtrl.jsp";
  
  String idAzienda = request.getParameter("idAzienda");
  String arrivo = request.getParameter("arrivo");
  AnagAziendaVO anagAziendaVO = null;
  
  if(Validator.isNotEmpty(idAzienda))
  {
	  if(Validator.isNotEmpty(arrivo) && "elenco".equalsIgnoreCase(arrivo))
	    anagAziendaVO = anagFacadeClient.findAziendaByIdAnagAzienda(new Long(idAzienda));
	  else
	    anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
	}
	else
	{
	  anagAziendaVO = anagFacadeClient.findAziendaAttiva((Long)session.getAttribute("idAziendaValida"));
	}
    
  request.setAttribute("anagAziendaVO", anagAziendaVO);

  int paginaCorrente = 0;
  Integer paginaCorrenteInteger;
  String dataAvanzata="";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String annullaRichiesta = request.getParameter("annullaRichiesta");

  
  String operazione = request.getParameter("operazione");
  String regimeRicercaRichiesta = request.getParameter("regimeRicercaRichiesta");

  ValidationErrors errors = new ValidationErrors();
  String richiestaAziendaSel = request.getParameter("idRichiestaAzienda");
  if(Validator.isNotEmpty(richiestaAziendaSel)) 
  {
    request.setAttribute("idRichiestaAziendaSel", Long.decode(richiestaAziendaSel));
  }
  
  
  
  
  
  
  


  //prima volta che entro
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {     
    request.setAttribute("action", action);
    operazione = "valida";
    request.setAttribute("operazione", operazione);
    session.setAttribute("idRichiestaAziendaValida", Long.decode(richiestaAziendaSel));
    session.setAttribute("idAziendaValida", Long.decode(idAzienda));
   
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }


  if(Validator.isNotEmpty(operazione) && "avanti".equalsIgnoreCase(operazione))
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
      request.getRequestDispatcher(elencoRicercaRichiestaURL).forward(request, response);
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
      request.getRequestDispatcher(elencoRicercaRichiestaURL).forward(request, response);
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
          AnagAziendaVO anagAziendaTmpVO = anagFacadeClient.findAziendaAttiva(vAziendaNuovaVOTmp.get(0).getIdAzienda());
          if(anagAziendaTmpVO.isPossiedeDelegaAttiva())
             messaggio = AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;       
        }
      }
      
      if(Validator.isEmpty(messaggio))
      {
        if(!ruoloUtenza.isReadWrite())
        {
          messaggio = "Utente non abilitato alla presa in carico";
        }      
      }       
      
      
      if(Validator.isEmpty(messaggio))
      {
        if(vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue()  == SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE)
        {
		      //Controllo che l'azienda da cui provengo sia cessata...
		      if(vAziendaNuovaVOTmp.get(0).getIdAziendaSubentro() != null)
		      {
		        AnagAziendaVO anagAziendaTmpVO = anagFacadeClient.findAziendaAttiva(
		          vAziendaNuovaVOTmp.get(0).getIdAziendaSubentro());
		        if(Validator.isEmpty(anagAziendaTmpVO.getDataCessazione()))
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
			      SolmrLogger.info(this, " - elencoRicercaRichiestaCtrl.jsp - FINE PAGINA");
			      messaggio = errMsg+""+se.toString();
			      request.setAttribute("messaggioErrore",messaggio);
			      request.setAttribute("history","history");
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
        //Long idAzienda = vAziendaNuovaVOTmp.get(0).getIdAzienda();       
        //session.removeAttribute("anagAziendaVO");
	      
        //AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(idAzienda);
        //Passo questo parametro per indicare che arrivo dal dettaglio ricerca
        //per avitare problemi ruolo/iride col mandato (dopo introduzione caa_limitato elenco soci ndr)  
        request.setAttribute("RICERCAJSP","RICERCAJSP");
        request.setAttribute("idAziendaAccesso",anagAziendaVO.getIdAzienda().toString());
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
      request.getRequestDispatcher(elencoRicercaRichiestaURL).forward(request, response);
      return;
    }
  }
  else if(Validator.isNotEmpty(operazione) && "annulla".equalsIgnoreCase(operazione))
  {
  
    session.setAttribute("idRichiestaAziendaValida", Long.decode(richiestaAziendaSel));
    
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
	      request.getRequestDispatcher(elencoRicercaRichiestaURL).forward(request, response);
	      return;      
      }
      
      
      
      
      %>
        <jsp:forward page = "<%=annullaRichiestaURL %>" >
          <jsp:param name="arrivo" value="elencoRichiesta" /> 
        </jsp:forward>
      <%
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(elencoRicercaRichiestaURL).forward(request, response);
      return;
    }
  }
  else if(Validator.isNotEmpty(operazione) && "dettaglio".equalsIgnoreCase(operazione))
  {   
	    
	  //AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(idAzienda);
	  //Passo questo parametro per indicare che arrivo dal dettaglio ricerca
	  //per avitare problemi ruolo/iride col mandato (dopo introduzione caa_limitato elenco soci ndr)  
	  request.setAttribute("RICERCAJSP","RICERCAJSP");
	  request.setAttribute("idAziendaAccesso", idAzienda);
	  %>
	    <jsp:forward page = "<%=accessoAziendaURL%>" />
	  <%
	  return;            
  }
  else
  {
    session.removeAttribute("listIdRicercaAzienda");
    try 
    {
      Vector<Long> vectIdRichiestaAzienda = gaaFacadeClient
        .getElencoRichieseteAziendaByIdRichiestaAzienda(anagAziendaVO.getIdAzienda(), ruoloUtenza.getCodiceRuolo());
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
        String messaggio = "Non sono presenti richieste inviate dall'azienda";
        request.setAttribute("messaggio", messaggio);
      }
    }
    catch (SolmrException sex) 
    {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(elencoRicercaRichiestaURL).forward(request, response);
      return;
    }
  
  }  
  


%>
  <jsp:forward page = "<%=elencoRicercaRichiestaURL %>" />

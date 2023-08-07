<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoAllegatiCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova,elencoAllevamentiBdn,allegatoDocumentoVO,vElencoFileAllegati");

  String newInserimentoAllegatiUrl = "/view/newInserimentoAllegatiView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoContiCorrentiCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoConfermaCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_DOC);
  request.setAttribute("testoHelp", testoHelp);
  
  String testoDichiarazione = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_CK_ALL_OBB_NAP);
  request.setAttribute("testoDichiarazione", testoDichiarazione);
  
  BigDecimal idDocIdentita = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ID_DOC_OBB_NAP);
  request.setAttribute("idDocIdentita", idDocIdentita);
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  String operazione = request.getParameter("operazione");
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (request.getParameter("regimeInserimentoAllegati") != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  Vector<RichiestaAziendaDocumentoVO> vAllegatiAziendaNuova = gaaFacadeClient
    .getAllegatiAziendaNuovaIscrizione(idAziendaNuova.longValue(),aziendaNuovaVO.getIdTipoRichiesta());
  
  Vector<TipoDocumentoVO> vTipoDocumento = gaaFacadeClient.getDocumentiNuovaIscrizione();
  request.setAttribute("vTipoDocumento", vTipoDocumento);
  
  
    
  if(Validator.isNotEmpty(operazione) && "elimina".equalsIgnoreCase(operazione))      
  {
    String idRigaElim = request.getParameter("idRigaElim");
    String idDocRich = request.getParameter("idDocRich");
    gaaFacadeClient.deleteDocumentoRichiesta(new Long(idDocRich));
    
    if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
    {
      IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
      iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
      gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
        iterRichiestaAziendaVO);
    }   
    
    vAllegatiAziendaNuova.remove(new Integer(idRigaElim).intValue());
  }
  else if(Validator.isNotEmpty(operazione) && "inserisci".equalsIgnoreCase(operazione))
  {
    ValidationErrors errors = new ValidationErrors();
    String extIdDocumento = request.getParameter("extIdDocumento");
    Long extIdDocumentoLg = null;
    if(Validator.isNotEmpty(extIdDocumento)) 
    {
      extIdDocumentoLg = Long.decode(extIdDocumento);
    }
    else    
    {
      errors.add("extIdDocumento", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    
    String dataInizioValidita = request.getParameter("dataInizioValidita");
    Date dataInizioValiditaDt = null;
    if (Validator.isEmpty(dataInizioValidita))
    {
      errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else 
    {
      if(dataInizioValidita.length() != 10) 
      {
        errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(dataInizioValidita)) 
      {
        errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
      }
      else 
      {
        try 
        {
          dataInizioValiditaDt = DateUtils.parseDate(dataInizioValidita);
        }
        catch (Exception ex) 
        {
          errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
        }
      }
    }
    
    String dataFineValidita = request.getParameter("dataFineValidita");
    Date dataFineValiditaDt = null;
    if (Validator.isEmpty(dataFineValidita))
    {
      errors.add("dataFineValidita", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else 
    {
      if(dataFineValidita.length() != 10) 
      {
        errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(dataFineValidita)) 
      {
        errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
      }
      else 
      {
        try 
        {
          dataFineValiditaDt = DateUtils.parseDate(dataFineValidita);
        }
        catch (Exception ex) 
        {
          errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
        }
      }
    }
    
    if(Validator.isNotEmpty(dataInizioValiditaDt)
      && Validator.isNotEmpty(dataFineValiditaDt))
    {
      if(dataInizioValiditaDt.after(dataFineValiditaDt))
      {
        errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
        errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
      }
    }
    
    
    
    
    
    
    
    
    String numeroDocumento = "";
    String enteRilascioDocumento = "";
    for(int j=0;j<vTipoDocumento.size();j++)
    {      
      if(Validator.isNotEmpty(extIdDocumentoLg)
        && (extIdDocumentoLg.compareTo(vTipoDocumento.get(j).getIdDocumento()) == 0))
      {        
        if("S".equalsIgnoreCase(vTipoDocumento.get(j).getFlagObbligoEnteNumero()))
        {
          numeroDocumento = request.getParameter("numeroDocumento");
          if(Validator.isEmpty(numeroDocumento)) 
			    {
			      errors.add("numeroDocumento", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
			    }
			    enteRilascioDocumento = request.getParameter("enteRilascioDocumento");
          if(Validator.isEmpty(numeroDocumento)) 
          {
            errors.add("enteRilascioDocumento", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          }
        }     
      }
    }
    
    if(errors.size() > 0)
    {
      request.setAttribute("vAllegatiAziendaNuova", vAllegatiAziendaNuova);
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page="<%= newInserimentoAllegatiUrl %>"/>
      <%
    }
    
    
    
    if(vAllegatiAziendaNuova == null)
    {
      vAllegatiAziendaNuova = new Vector<RichiestaAziendaDocumentoVO>();
    }
    
    RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO = new RichiestaAziendaDocumentoVO();
      
    richiestaAziendaDocumentoVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
    richiestaAziendaDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
    richiestaAziendaDocumentoVO.setDataInizioValidita(dataInizioValiditaDt);
    richiestaAziendaDocumentoVO.setDataFineValidita(dataFineValiditaDt);    
    richiestaAziendaDocumentoVO.setNumeroDocumento(numeroDocumento);
    richiestaAziendaDocumentoVO.setEnteRilascioDocumento(enteRilascioDocumento);

         
    richiestaAziendaDocumentoVO.setExtIdDocumento(extIdDocumentoLg);
    if(vTipoDocumento != null)
	  {
	    for(int j=0;j<vTipoDocumento.size();j++)
	    {
	      if(richiestaAziendaDocumentoVO.getExtIdDocumento().compareTo(vTipoDocumento.get(j).getIdDocumento()) == 0)
	      {
	        richiestaAziendaDocumentoVO.setDescDocumento(vTipoDocumento.get(j).getDescrizione());
	        break;
	      }
	    }
	  }
    
    
    
    Long idRichiestaAziendaDocumento = gaaFacadeClient.insertRichAzDocAziendaNuova(richiestaAziendaDocumentoVO);
    if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
    {
      IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
      iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
      gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
        iterRichiestaAziendaVO);
    }
    
    
    richiestaAziendaDocumentoVO.setIdRichiestaAziendaDocumento(idRichiestaAziendaDocumento);
    
    vAllegatiAziendaNuova.add(richiestaAziendaDocumentoVO);
  }  
  else if(Validator.isNotEmpty(operazione) && "cambia".equalsIgnoreCase(operazione))
  {
    
  }
  
  
  
  request.setAttribute("vAllegatiAziendaNuova", vAllegatiAziendaNuova);
  
  
  
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (request.getParameter("regimeInserimentoAllegati") != null)) 
  {     
    
    ValidationErrors errors = new ValidationErrors();
    
    String chkDichiarazione = request.getParameter("chkDichiarazione");
    if(Validator.isNotEmpty(vAllegatiAziendaNuova)
      && (vAllegatiAziendaNuova.size() > 0))
    {
    
      
      //Controlli da effettuare
      boolean trovatoDocIdentita = false;
      boolean trovatoAllegati = false;
      for(int i=0;i<vAllegatiAziendaNuova.size();i++)
      {
        
        RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO = vAllegatiAziendaNuova.get(i);
        if(richiestaAziendaDocumentoVO.getExtIdDocumento().longValue()
           == idDocIdentita.longValue())
        {
          if(!trovatoDocIdentita)
          {
            trovatoDocIdentita = true;
          }
          
          if(Validator.isNotEmpty(richiestaAziendaDocumentoVO.getvAllegatoDocumento()))
          {
            if(!trovatoAllegati)
	          {
	            trovatoAllegati = true;
	          }
          }
          
        }
           
        
      }
      
      if(!trovatoDocIdentita)
      {
        errors.add("avanti", new ValidationError(AnagErrors.ERR_DOC_IDENTITA_OBBLIGATORIO));
      }
      else
      {
        if(!trovatoAllegati)
        {
          
          if(!"S".equalsIgnoreCase(chkDichiarazione))
          {
            errors.add("avanti", new ValidationError(AnagErrors.ERR_DOC_IDENTITA_SPUNTA_OBBLIGATORIO));
          }        
        } 
      }
      
      
    }
    else
    {
      errors.add("avanti", new ValidationError(AnagErrors.ERR_DOC_IDENTITA_OBBLIGATORIO));
    }
    
    
    if(errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
    }
    else
    {      
      //E' valorizzato
      if(Validator.isNotEmpty(chkDichiarazione))
      {
        //diverso dal valore presentesu DB
        if(!"S".equalsIgnoreCase(aziendaNuovaVO.getFlagDichiarazioneAllegati()))
        {
          gaaFacadeClient.updateFlagDichiarazioneAllegati(aziendaNuovaVO.getIdRichiestaAzienda().longValue(), 
            "S");
            
          if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
			    {
			      IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
			      iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
			      gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
			        iterRichiestaAziendaVO);
			    }
        }
      }
      else
      {
        //N lo metto solo se c'e' qualche altro valore su db
        if(Validator.isNotEmpty(aziendaNuovaVO.getFlagDichiarazioneAllegati()))
        {
	        if(!"N".equalsIgnoreCase(aziendaNuovaVO.getFlagDichiarazioneAllegati()))
	        {
	          gaaFacadeClient.updateFlagDichiarazioneAllegati(aziendaNuovaVO.getIdRichiestaAzienda().longValue(), 
	            "N");
	          if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) != 0)
	          {
	            IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
	            iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
	            gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
	              iterRichiestaAziendaVO);
	          }
	        }
	      }
      } 
      
      %>
        <jsp:forward page="<%= pageNext %>"/>
      <%
      return;       
      
    }
    
    
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoAllegatiUrl %>"/>
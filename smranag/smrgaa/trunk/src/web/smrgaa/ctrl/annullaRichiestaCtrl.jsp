<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.smrcomms.siapcommws.InvioPostaCertificata" %>
<%@ page import="it.csi.smrcomms.siapcommws.DatiMailVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "annullaRichiestaCtrl.jsp";
  
  %><%@include file = "/include/autorizzazione.inc" %><%

  String annullaRichiestaUrl = "/view/annullaRichiestaView.jsp";
  String ricercaRichiestaUrl = "/ctrl/ricercaRichiestaCtrl.jsp";
  String elencoRicercaRichiestaUrl = "/ctrl/elencoRicercaRichiestaCtrl.jsp";
  String errorPage = "/view/annullaRichiestaView.jsp";
  
  final String errMsg = "L'annullamento è avvenuto con successo ma non è stato possibile inviare "+
    "la mail per il seguente errore: ";
  String actionUrl = "../layout/ricercaRichiesta.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  
  
  

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  ValidationError error = null;
  ValidationErrors errors = new ValidationErrors();
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
  String msgAnnulla = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MSG_NAP_ANNULLA);
  request.setAttribute("msgAnnulla", msgAnnulla);
  
  String idRichiestaAzienda = request.getParameter("idRichiestaAzienda");
  Long idRichiestaAziendaLg = new Long(idRichiestaAzienda);
  Vector<Long> vIdRichTmp = new Vector<Long>();
  vIdRichTmp.add(idRichiestaAziendaLg);
  Vector<AziendaNuovaVO> vAziendaNuovaVOTmp  = gaaFacadeClient.getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichTmp);
  AziendaNuovaVO aziendaNuovaVO = null;
  
  
  
  if(vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE)
  {
    aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(vAziendaNuovaVOTmp.get(0).getIdAziendaNuova());
  }
  else
  {
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(vAziendaNuovaVOTmp.get(0).getIdAzienda().longValue(), 
      vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue());  
  }
  /*else if(vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_VALIDAZIONE)
  {
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(vAziendaNuovaVOTmp.get(0).getIdAzienda().longValue(), 
      SolmrConstants.RICHIESTA_VALIDAZIONE);
  }
  else if(vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_CESSAZIONE)
  {
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(vAziendaNuovaVOTmp.get(0).getIdAzienda().longValue(), 
      SolmrConstants.RICHIESTA_CESSAZIONE);
  }*/
  
  String arrivo = request.getParameter("arrivo");
  // L'utente ha selezionato il tasto annulla
  if(request.getParameter("annulla") != null) 
  {
    
    if(Validator.isNotEmpty(arrivo) && "elencoRichiesta".equalsIgnoreCase(arrivo))
    { 
      %>
         <jsp:forward page="<%= elencoRicercaRichiestaUrl %>">
           <jsp:param name="annullaRichiesta" value="annullaRichiesta" />
           <jsp:param name="idAzienda" value="<%=aziendaNuovaVO.getIdAzienda().toString() %>" />  
         </jsp:forward>
      <%
    }
    //arivo dall'elenco richieste
    else
    {
      %>
         <jsp:forward page="<%= ricercaRichiestaUrl %>">
           <jsp:param name="annullaRichiesta" value="annullaRichiesta" /> 
         </jsp:forward>
      <%
    }
  }
  
  
  
  //request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);

  
  

  // L'utente ha selezionato il tasto conferma
  if(request.getParameter("conferma") != null) 
  {
    String note = request.getParameter("note");
    String chkMail = request.getParameter("chkMail");
    if(Validator.isEmpty(note))
    {
      errors.add("note", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else
    {
      if(note.length() > 1000)
      {
        errors.add("note", new ValidationError(AnagErrors.ERR_CAMPO_NOTE_MAX));
      }
    }
    
    
    if(errors.size() > 0)
    {
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page="<%= annullaRichiestaUrl %>"/>
      <%
      return;
    }
    
    
    try 
    {
	    IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
	    iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_ANNULLAMENTO);
	    iterRichiestaAziendaVO.setNote(note);
	    String flagMailNotifica = "N";
	    if(Validator.isNotEmpty(chkMail))
	    { 
	      flagMailNotifica = "S";
	    }
	    iterRichiestaAziendaVO.setFlagMailNotifica(flagMailNotifica);
	    
	    if(vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE)
      {
	      gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
	        iterRichiestaAziendaVO);
	    }
	    else
      {
        gaaFacadeClient.aggiornaStatoRichiestaValCess(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
          iterRichiestaAziendaVO);      
      }
	    /*else if((vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_VALIDAZIONE)
        || (vAziendaNuovaVOTmp.get(0).getIdTipoRichiesta().longValue() == SolmrConstants.RICHIESTA_CESSAZIONE))
      {
        gaaFacadeClient.aggiornaStatoRichiestaValCess(aziendaNuovaVO, profile.getRuoloUtenza().getIdUtente().longValue(), 
          iterRichiestaAziendaVO);      
      }*/
	      
	  }
    catch (SolmrException sex) 
    {
      error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
    
    if(Validator.isNotEmpty(chkMail)
      && "S".equalsIgnoreCase(chkMail)
      && (Validator.isNotEmpty(aziendaNuovaVO.getMail()) || Validator.isNotEmpty(aziendaNuovaVO.getPec())))
    {
      //Andato tutto ok mando la mail!!!    
	    try 
	    {
	      
	      // ** Costruisco gli oggetti con i dati per l'invio mail      
	      SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
	      
	      
	      //se sono qui la pec o la mail sono per forza uno dei due valorizzati
	      String destinatarioMail = aziendaNuovaVO.getMail();
	      if(Validator.isNotEmpty(aziendaNuovaVO.getPec()))
	      {
	        destinatarioMail = aziendaNuovaVO.getPec();
	      }
	        
	      // ----- Mittente -> VTMA di DB_ALTRI_DATI       
	      String mittente = "";
	      try 
	      {
	        mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_RICH_AZ);
	      }
	      catch(SolmrException se) 
	      {
	        SolmrLogger.info(this, " - annullaRichiestaCtrl.jsp - FINE PAGINA");
	        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MITT_MAIL_RICH_AZ+".\n"+se.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	        return;
	      }
	      SolmrLogger.debug(this, " --- MITTENTE ="+mittente);
	      
	      String mittenteReplyTo = "";
        try 
        {
          mittenteReplyTo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_REPLY_TO_MAIL);
        }
        catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - newInserimentoConfermaCessazioneCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_REPLY_TO_MAIL+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        SolmrLogger.debug(this, " --- MITTENTE REPLYTO="+mittenteReplyTo);
	          
	      // ---- Oggetto
	      String oggettoMail = "";
	      try 
	      {
	        oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_ANN_NAP);
	      }
	      catch(SolmrException se) 
	      {
	        SolmrLogger.info(this, " - annullaRichiestaCtrl.jsp - FINE PAGINA");
	        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_ANN_NAP+".\n"+se.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	        return;
	      }
	      
	      if(Validator.isNotEmpty(oggettoMail))
	      {
	        oggettoMail = oggettoMail.replaceAll("<<TIPO_RICHIESTA>>", aziendaNuovaVO.getDescEstesaTipoRichiesta());      
	      }        
	      SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
	          
	          
	      // ----- Testo
	      String testo = "";
	      try 
	      {
	        testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_TXT_ANN_NAP);
	      }
	      catch(SolmrException se) 
	      {
	        SolmrLogger.info(this, " - annullaRichiestaCtrl.jsp - FINE PAGINA");
	        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_TXT_ANN_NAP+".\n"+se.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	        return;
	      }
	      
	      if(Validator.isNotEmpty(testo))
	      {
	        testo = testo.replaceAll("<<TESTO_ANNULLAMENTO>>", aziendaNuovaVO.getTestoAnnullamento());
	        testo = testo.replaceAll("<<TIPO_RICHIESTA>>", aziendaNuovaVO.getDescEstesaTipoRichiesta());
	        testo = testo.replaceAll("<<AZIENDA>>", aziendaNuovaVO.getCuaa()+" - "+aziendaNuovaVO.getDenominazione());
          testo = testo.replaceAll("<<DATA_RICHIESTA>>", DateUtils.formatDateNotNull(aziendaNuovaVO.getDataAggiornamentoIter()));
	        testo = testo.replaceAll("<<MOTIVO_ANNULLAMENTO>>", note);
	          
	      }    
	      SolmrLogger.debug(this, " --- TESTO ="+testo);
	          
	      // Setto i valori per le mail da inviare
        InvioPostaCertificata invioPosta = new InvioPostaCertificata();
        DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
        DatiMailVO datiMailVO = new DatiMailVO(); 
        
        String[] arrDestinatari = new String[1];
        arrDestinatari[0] = destinatarioMail;
        datiMailVO.setDestinatariA(arrDestinatari);
        datiMailVO.setMittente(mittente);
        datiMailVO.setMittenteDisplayName(mittenteReplyTo);
        datiMailVO.setOggetto(oggettoMail);
        datiMailVO.setTesto(testo);
        
        
        arrDatiMail[0] = datiMailVO;
        invioPosta.setInput(arrDatiMail);
        
        
        anagFacadeClient.serviceInviaPostaCertificata(invioPosta);
	      
	      
	      
	      /*Vector<String> destinatariMailA = new Vector<String>(); 
	      destinatariMailA.add(destinatarioMail);           
	      
	      DatiMail datiMail = new DatiMail();
	      datiMail.setDestinatariA(destinatariMailA);
	      datiMail.setMittente(mittente);
	      datiMail.setMittenteReplyTo(mittenteReplyTo);           
	      datiMail.setOggetto(oggettoMail);           
	      datiMail.setTesto(testo);           
	      datiMailVect.add(datiMail); 
	                     
	       
	      
	      
	      // Se ci sono destinatari per l'invio mail, invio MAIL
	      SolmrLogger.debug(this, " --- controllo se devono essere inviate delle mail");
	      if(datiMailVect != null && datiMailVect.size()>0)
	      {
	        SolmrLogger.error(this, " --- ******** INVIO DELLE MAIL dopo la TRASMISSIONE *****-----");
	        SolmrLogger.error(this, " -- numero di mail da inviare ="+datiMailVect.size());
	        invioMail.sendMail(datiMailVect);     
	                
	        //messaggio = AgriConstants.MESSAGGIO_INVIO_MAIL_TRASMISSIONE_OK;
	      }*/
	      
	    }
	    catch(SolmrException e) 
	    {
	      SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
	      String messaggio = AnagErrors.ERRORE_KO_INVIO_MAIL_RICH_AZ+".\n"+e.toString();
	      request.setAttribute("messaggioErrore",messaggio);
	      request.setAttribute("pageBack", actionUrl);
	      %>
	        <jsp:forward page="<%= erroreViewUrl %>" />
	      <%
	      return;
	    }
    
    }    
    

    // Torno alla pagina della ricerca/elenco
    if(Validator.isNotEmpty(arrivo) && "elencoRichiesta".equalsIgnoreCase(arrivo))
    { 
      %>
         <jsp:forward page="<%= elencoRicercaRichiestaUrl %>">
           <jsp:param name="annullaRichiesta" value="annullaRichiesta" />
           <jsp:param name="idAzienda" value="<%=aziendaNuovaVO.getIdAzienda().toString() %>" />  
         </jsp:forward>
      <%
    }
    //arivo dall'elenco richieste
    else
    {
      %>
         <jsp:forward page="<%= ricercaRichiestaUrl %>">
           <jsp:param name="annullaRichiesta" value="annullaRichiesta" /> 
         </jsp:forward>
      <%
    }
  }

%>
  <jsp:forward page="<%= annullaRichiestaUrl %>" />

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
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.pdf.PdfNuovaIscrizione" %>
<%@ page import="it.csi.smrcomms.siapcommws.InvioPostaCertificata" %>
<%@ page import="it.csi.smrcomms.siapcommws.DatiMailVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoConfermaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova");
  
  String newInserimentoConfermaUrl = "/view/newInserimentoConfermaView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoAllegatiCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoAllegatiCtrl.jsp";
  String stampaNuovaIscrizioneUrl = "/servlet/StampaNuovaIscrizioneServlet";
  String stampaNuovaIscrizioneFirmaUrl = "/servlet/StampaNuovaIscrizioneServlet?param=firma";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoContiCorrenti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  
  String testoNoAllegati = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_PAG_FINE_NAP_1);
  request.setAttribute("testoNoAllegati", testoNoAllegati+"<br>");
  String testoPerTutti = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_PAG_FINE_NAP_2);
  request.setAttribute("testoPerTutti", testoPerTutti+"<br><br>");
  
  
  Vector<StatoRichiestaVO> vStatoRichiesta = gaaFacadeClient.getListStatoRichiesta();
  request.setAttribute("vStatoRichiesta", vStatoRichiesta);
  
  
  
  if(Validator.isNotEmpty(operazione) && "indietro".equalsIgnoreCase(operazione)) 
  {         
    %>
      <jsp:forward page="<%= indietroUrl %>"/>
    <%
    return;
  }
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if(Validator.isNotEmpty(operazione) && "stampa".equalsIgnoreCase(operazione)) 
  {
    %>
      <jsp:forward page="<%= stampaNuovaIscrizioneUrl %>"/>
    <%
    return;
  }
  
  if(Validator.isNotEmpty(operazione) && "stampaFirma".equalsIgnoreCase(operazione)) 
  {
    %>
      <jsp:forward page="<%= stampaNuovaIscrizioneFirmaUrl %>"/>
    <%
    return;
  }  
  
  
  if(Validator.isNotEmpty(operazione) && "stampaConferma".equalsIgnoreCase(operazione)) 
  {  
    IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
    iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_STAMPATA);
    gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
      iterRichiestaAziendaVO);
    aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
    
    //Chiamo la stampa per salvarla su DB
    PdfNuovaIscrizione pdfGenerator = new PdfNuovaIscrizione();
    pdfGenerator.generaDocumento(request, response);
    
  }
  
  if(Validator.isNotEmpty(operazione) && "appFirmDig".equalsIgnoreCase(operazione)) 
  {  
    IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
    iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_FIRMA_DIGITALE);
    gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
      iterRichiestaAziendaVO);
    aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
    
    //Chiamo la stampa per salvarla su DB
    PdfNuovaIscrizione pdfGenerator = new PdfNuovaIscrizione();
    pdfGenerator.generaDocumento(request, response);
  }
  
  if(Validator.isNotEmpty(operazione) && "trasmPA".equalsIgnoreCase(operazione)) 
  {    
  
    IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
    iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA);
    gaaFacadeClient.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
      iterRichiestaAziendaVO);
    
    //Invio mail a Savio  
    String parametroInvioMailRichAz = "";
	  try 
	  {
	    parametroInvioMailRichAz = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_INVIO_MAIL_RICH_AZ);
	  }
	  catch(SolmrException se) 
	  {
	    SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_INVIO_MAIL_RICH_AZ+".\n"+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
	  SolmrLogger.debug(this, " --- ABILITA MAIL ="+parametroInvioMailRichAz);
	  
	  //Andato tutto ok mando la mail!!!
	  if("S".equalsIgnoreCase(parametroInvioMailRichAz))
	  {	  
	    try 
	    {
	           
	      // ** Costruisco gli oggetti con i dati per l'invio mail      
	      SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
	      
	      
	      
	      String destinatarioMailReg = "";
	      try 
		    {
		      destinatarioMailReg = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DEST_MAIL_RICH_AZ);
		    }
		    catch(SolmrException se) 
		    {
		      SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DEST_MAIL_RICH_AZ+".\n"+se.toString();
		      request.setAttribute("messaggioErrore",messaggio);
		      request.setAttribute("pageBack", actionUrl);
		      %>
		        <jsp:forward page="<%= erroreViewUrl %>" />
		      <%
		      return;
		    }
			  
	        
	      // ----- Mittente -> VTMA di DB_ALTRI_DATI       
	      String mittente = "";
	      try 
	      {
	        mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_RICH_AZ);
	      }
	      catch(SolmrException se) 
	      {
	        SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
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
	        oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_MAIL_PER_RICH_AZ);
	      }
	      catch(SolmrException se) 
	      {
	        SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
	        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_MAIL_PER_RICH_AZ+".\n"+se.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	        return;
	      }
	      
	      if(Validator.isNotEmpty(oggettoMail))
	      {
	        oggettoMail = oggettoMail.replaceAll("<<RICH_AZIENDA>>", aziendaNuovaVO.getDescTipoRichiesta());
	        oggettoMail = oggettoMail.replaceAll("<<CUAA>>", aziendaNuovaVO.getCuaa());
	        oggettoMail = oggettoMail.replaceAll("<<DENOMINAZIONE>>", aziendaNuovaVO.getDenominazione());       
	      }        
	      SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
	          
	          
	      // ----- Testo
	      String testo = "";
	      try 
	      {
	        testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAIL_PER_RICH_AZ);
	      }
	      catch(SolmrException se) 
	      {
	        SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
	        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAIL_PER_RICH_AZ+".\n"+se.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	        return;
	      }
	      
	      if(Validator.isNotEmpty(testo))
	      {
	        testo = testo.replaceAll("<<SYSDATE>>", DateUtils.getCurrent());
	        testo = testo.replaceAll("<<RICH_AZIENDA>>", aziendaNuovaVO.getDescTipoRichiesta());
	        testo = testo.replaceAll("<<CUAA>>", aziendaNuovaVO.getCuaa());
	        testo = testo.replaceAll("<<DENOMINAZIONE>>", aziendaNuovaVO.getDenominazione());       
	      }    
	      SolmrLogger.debug(this, " --- TESTO ="+testo);
	      
	      
	      // Setto i valori per le mail da inviare
        InvioPostaCertificata invioPosta = new InvioPostaCertificata();
        DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
        DatiMailVO datiMailVO = new DatiMailVO(); 
        
        String[] arrDestinatari = new String[1];
        arrDestinatari[0] = destinatarioMailReg;
        datiMailVO.setDestinatariA(arrDestinatari);
        datiMailVO.setMittente(mittente);
        datiMailVO.setMittenteDisplayName(mittenteReplyTo);
        datiMailVO.setOggetto(oggettoMail);
        datiMailVO.setTesto(testo);
        
        
        arrDatiMail[0] = datiMailVO;
        invioPosta.setInput(arrDatiMail);
        
        
        anagFacadeClient.serviceInviaPostaCertificata(invioPosta);
	      
	      
	      
	         
	      // Setto i valori per le mail da inviare
	      /*Vector<String> destinatariMailA = new Vector<String>();
	      destinatariMailA.add(destinatarioMailReg);    
	      
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
	    catch (SolmrException e) 
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
	  
	  
	  //Invio mail a azienda 
    String parametroInvioRichAAz = "";
    try 
    {
      parametroInvioRichAAz = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_INVIO_RICH_A_AZ);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_INVIO_RICH_A_AZ+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    SolmrLogger.debug(this, " --- ABILITA MAIL ="+parametroInvioRichAAz);
	  
	  
	  if("S".equalsIgnoreCase(parametroInvioRichAAz))
    {   
      try 
      {        
        // ** Costruisco gli oggetti con i dati per l'invio mail      
        SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
        
        
        //se sono qui la pec o la mail sono per forza uno dei due valorizzati        
        String destinatarioMailAz = "";
        boolean trovataMailAz = false;
        if(Validator.isNotEmpty(aziendaNuovaVO.getPec()))
        {
          destinatarioMailAz = aziendaNuovaVO.getPec();
        }
        else
        {
          destinatarioMailAz = aziendaNuovaVO.getMail();
        }
                
        SolmrLogger.debug(this, " --- sono stati trovati dei DESTINATARIO :"+destinatarioMailAz);
        if(Validator.isNotEmpty(destinatarioMailAz))
        {
          trovataMailAz = true;
        }
          
        // ----- Mittente -> VTMA di DB_ALTRI_DATI       
        String mittente = "";
        try 
        {
          mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_RICH_AZ);
        }
        catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
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
          oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_RICH_ISC_A_AZ);
        }
        catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_RICH_ISC_A_AZ+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        if(Validator.isNotEmpty(oggettoMail))
        {
          oggettoMail = oggettoMail.replaceAll("<<RICH_AZIENDA>>", aziendaNuovaVO.getDescTipoRichiesta());
          oggettoMail = oggettoMail.replaceAll("<<CUAA>>", aziendaNuovaVO.getCuaa());
          oggettoMail = oggettoMail.replaceAll("<<DENOMINAZIONE>>", aziendaNuovaVO.getDenominazione());       
        }        
        SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
            
            
        // ----- Testo
        String testo = "";
        try 
        {
          testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAIL_RICH_ISC_A_AZ);
        }
        catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - newInserimentoConfermaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAIL_RICH_ISC_A_AZ+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        if(Validator.isNotEmpty(testo))
        {
          testo = testo.replaceAll("<<SYSDATE>>", DateUtils.getCurrent());
          testo = testo.replaceAll("<<RICH_AZIENDA>>", aziendaNuovaVO.getDescTipoRichiesta());
          testo = testo.replaceAll("<<CUAA>>", aziendaNuovaVO.getCuaa());
          testo = testo.replaceAll("<<DENOMINAZIONE>>", aziendaNuovaVO.getDenominazione());       
        }    
        SolmrLogger.debug(this, " --- TESTO ="+testo);
        
        
        // Setto i valori per le mail da inviare
        InvioPostaCertificata invioPosta = new InvioPostaCertificata();
        DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
        DatiMailVO datiMailVO = new DatiMailVO(); 
        
        String[] arrDestinatari = new String[1];
        arrDestinatari[0] = destinatarioMailAz;
        datiMailVO.setDestinatariA(arrDestinatari);
        datiMailVO.setMittente(mittente);
        datiMailVO.setMittenteDisplayName(mittenteReplyTo);
        datiMailVO.setOggetto(oggettoMail);
        datiMailVO.setTesto(testo);
        
        
        arrDatiMail[0] = datiMailVO;
        invioPosta.setInput(arrDatiMail);
        
        
        anagFacadeClient.serviceInviaPostaCertificata(invioPosta);
           
        // Setto i valori per le mail da inviare
        /*Vector<String> destinatariMailA = new Vector<String>();
        destinatariMailA.add(destinatarioMailAz);           
        
        DatiMail datiMail = new DatiMail();
        datiMail.setDestinatariA(destinatariMailA);
        datiMail.setMittente(mittente);
        datiMail.setMittenteReplyTo(mittenteReplyTo);           
        datiMail.setOggetto(oggettoMail);           
        datiMail.setTesto(testo);           
        datiMailVect.add(datiMail); 
                       
         
        
        
        // Se ci sono destinatari per l'invio mail, invio MAIL
        SolmrLogger.debug(this, " --- controllo se devono essere inviate delle mail");
        if(datiMailVect != null && datiMailVect.size()>0
          && (trovataMailAz))       
        {
          SolmrLogger.error(this, " --- ******** INVIO DELLE MAIL dopo la TRASMISSIONE *****-----");
          SolmrLogger.error(this, " -- numero di mail da inviare ="+datiMailVect.size());
          invioMail.sendMail(datiMailVect);     
                  
          //messaggio = AgriConstants.MESSAGGIO_INVIO_MAIL_TRASMISSIONE_OK;
        }*/
        
      }
      catch (SolmrException e) 
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
	  
      
   
    aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  }
  
  
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  
  
  Vector<RichiestaAziendaDocumentoVO> vAllegatiAziendaNuova =
    gaaFacadeClient.getAllegatiAziendaNuovaIscrizione(idAziendaNuova.longValue(),aziendaNuovaVO.getIdTipoRichiesta());
  request.setAttribute("vAllegatiAziendaNuova", vAllegatiAziendaNuova);  
  
  
  

  
 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoConfermaUrl %>"/>
  

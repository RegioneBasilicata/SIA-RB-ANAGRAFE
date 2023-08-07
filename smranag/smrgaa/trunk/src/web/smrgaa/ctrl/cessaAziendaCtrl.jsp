
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.smrcomms.siapcommws.InvioPostaCertificata" %>
<%@ page import="it.csi.smrcomms.siapcommws.DatiMailVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "cessaAziendaCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  final String errMsg = "Impossibile procedere nella sezione cessazione azienda."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";

  String url = "/view/cessaAziendaView.jsp";
  ValidationErrors errors = new ValidationErrors();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagAziendaVO anagVO = new AnagAziendaVO();
  anagVO.setPossiedeDelegaAttiva(anagAziendaVO.isPossiedeDelegaAttiva());
  
	// TIPO CESSAZIONE
	TipoCessazioneVO[] elencoTipiCessazione = null;
	try 
  {
		elencoTipiCessazione = anagFacadeClient.getListTipoCessazione(null, true);
		request.setAttribute("elencoTipiCessazione", elencoTipiCessazione);
	}
	catch(SolmrException se) 
	{
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_CESSAZIONE);
	  url = "/view/cessaAziendaView.jsp";
	  errors.add("idCessazione", error);
	  request.setAttribute("errors", errors);
	  request.getRequestDispatcher(url).forward(request, response);
	}
	
	
	try 
  {
    AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAziendaConValida(anagAziendaVO.getIdAzienda().longValue(), 
      SolmrConstants.RICHIESTA_CESSAZIONE);
    request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  }
  catch(SolmrException se) 
  {
    String messaggio = AnagErrors.ERR_KO_RICERCA_DATI_RICH_AZ;
    request.setAttribute("messaggioErrore",messaggio);
    %>
      <jsp:forward page="<%=url%>" />
    <%
  }

  if(request.getParameter("salva") != null ) 
  {
   	boolean common[] = new boolean[2];
  	common[0] = false;
   	common[1] = false;
    try 
    {
    	common = (boolean[])session.getAttribute("common");
    }
    catch(Exception e) {}

   	if(!(common[0] && common[1])) 
   	{
    	url = "/layout/anagrafica.htm";
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
   	}

   	String dataCessStr = "";
   	dataCessStr = request.getParameter("dataCessazioneStr");
   	String tipoCessazione = request.getParameter("idCessazione");
   	String causaleCess = request.getParameter("causaleCessazione");

   	anagVO.setDataCessazioneStr(dataCessStr);
   	anagVO.setCausaleCessazione(causaleCess);
   	if(Validator.isNotEmpty(tipoCessazione)) 
   	{
   		anagVO.setIdCessazione(Long.decode(tipoCessazione));
   		request.setAttribute("idCessazione", anagVO.getIdCessazione());
   	}
   	errors = anagVO.validateCessaAzienda();
   	if(!(errors == null || errors.size() == 0)) 
   	{
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
   	}
   	// Se non ci sono errori di correttezza formale, recupero la min data inizio
   	// validita relativo all'azienda in esame
   	Date maxDataInizioValidita = null;
   	try 
   	{
   		maxDataInizioValidita = anagFacadeClient.getMaxDataInizioValiditaAnagraficaAzienda(anagAziendaVO.getIdAzienda());
   		// Verifico che la data di cessazione non sia minore della min data inizio
   		// validita dell'azienda in esame
   		if(anagVO.getDataCessazione().before(maxDataInizioValidita)) 
   		{
   			ValidationError error = new ValidationError(AnagErrors.ERRORE_DATA_CESSAZIONE_NO_MIN+" "+DateUtils.formatDate(maxDataInizioValidita));
        url = "/view/cessaAziendaView.jsp";
        errors.add("dataCessazioneStr", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
   		}
   	}
   	catch(SolmrException se) 
   	{
   		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_DATA_MAX_ANAG_AZIENDA);
   	  url = "/view/cessaAziendaView.jsp";
   	  errors.add("dataCessazioneStr", error);
   	  request.setAttribute("errors", errors);
   	  request.getRequestDispatcher(url).forward(request, response);
   	  return;
   	}
   	
   	
   	String parametroAbilitaMail = "";
    try 
    {
      parametroAbilitaMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ABILITA_INVIO_MAIL);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - cessaAziendaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ABILITA_INVIO_MAIL+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    SolmrLogger.debug(this, " --- ABILITA MAIL ="+parametroAbilitaMail);
    
    
    AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAziendaConValida(anagAziendaVO.getIdAzienda().longValue(), 
      SolmrConstants.RICHIESTA_CESSAZIONE);
    boolean trovataRichiesta = false;
    if(Validator.isNotEmpty(aziendaNuovaVO) 
      && aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) == 0)
    {
      trovataRichiesta = true;
    }
   	
   	
   	try 
   	{
    	if(anagAziendaVO.getDataCessazione() == null) 
    	{
      	Date dataCessazione = DateUtils.parseDate(dataCessStr);
      	anagFacadeClient.checkDataCessazione(anagAziendaVO.getIdAnagAzienda(),anagVO.getDataCessazioneStr());
      	anagAziendaVO.setIdCessazione(anagVO.getIdCessazione());
      	anagFacadeClient.cessaAzienda(anagAziendaVO, dataCessazione, anagVO.getCausaleCessazione(), ruoloUtenza.getIdUtente());
      	
      	
      	//Andato tutto ok mando la mail!!!
        if("S".equalsIgnoreCase(parametroAbilitaMail)
          && trovataRichiesta 
          && (Validator.isNotEmpty(anagAziendaVO.getPec()) || Validator.isNotEmpty(anagAziendaVO.getMail())))
        {
          try 
          {            
            // ** Costruisco gli oggetti con i dati per l'invio mail      
            SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");            
            
            //se sono qui la pec o la mail sono per forza uno dei due valorizzati
            String destinatarioMail = "";
            if(Validator.isNotEmpty(anagAziendaVO.getPec()))
            {
              destinatarioMail = anagAziendaVO.getPec();
            }
            else
            {
              destinatarioMail = anagAziendaVO.getMail();
            }
            
            SolmrLogger.debug(this, " --- sono stati trovati dei DESTINATARIO :"+destinatarioMail);
              
            // ----- Mittente -> VTMA di DB_ALTRI_DATI       
            String mittente = "";
            try 
            {
              mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_RICH_AZ);
            }
            catch(SolmrException se) 
            {
              SolmrLogger.info(this, " - cessaAziendaCtrl.jsp - FINE PAGINA");
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
              oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_VAL_RICH_CESS);
            }
            catch(SolmrException se) 
            {
              SolmrLogger.info(this, " - cessaAziendaCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_VAL_RICH_CESS+".\n"+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
              
            SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
                
                
            // ----- Testo
            String testo = "";
            try 
            {
              testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_TXT_VAL_RICH_CESS);
            }
            catch(SolmrException se) 
            {
              SolmrLogger.info(this, " - cessaAziendaCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_TXT_VAL_RICH_CESS+".\n"+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
            
            if(Validator.isNotEmpty(testo))
            {              
              String denAzienda = anagAziendaVO.getCUAA()+" - "+anagAziendaVO.getDenominazione();         
              testo = testo.replaceAll("<<AZIENDA>>", denAzienda);
              testo = testo.replaceAll("<<DATA_RICHIESTA>>", DateUtils.formatDate(aziendaNuovaVO.getDataAggiornamentoIter()));
              testo = testo.replaceAll("<<MOTIVO_CESSAZIONE>>", aziendaNuovaVO.getDescCessazione());
                          
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
          catch (Exception e) 
          {
            SolmrLogger.info(this, " - cessaAziendaCtrl.jsp - FINE PAGINA");
            String messaggio = AnagErrors.ERRORE_KO_INVIO_MAIL+".\n"+e.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
        }
      	
      	
      	
      	
      	
      	
      	
      	
      	
      	session.removeAttribute("common");
      	String dataSitAl = anagAziendaVO.getDataSituazioneAlStr();
      	anagVO.setDataCessazione(dataCessazione);
      	anagVO = anagFacadeClient.getAziendaById(anagAziendaVO.getIdAnagAzienda());
      	anagVO.setDataSituazioneAlStr(dataSitAl);
      	session.setAttribute("anagAziendaVO", anagVO);
      	url = "/view/anagraficaView.jsp";
     	}
     	else 
     	{
      	url = "/layout/elenco.htm";
      	throw new SolmrException(""+AnagErrors.get("AZIENDA_CESSATA"));
     	}
   	}
   	catch(SolmrException ex) 
   	{
    	ValidationError error = new ValidationError(ex.getMessage());
    	url = "/view/cessaAziendaView.jsp";
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
   	}
 	}
	else 
	{
  	if(request.getParameter("annulla")!=null) 
  	{
    	url = "/layout/anagrafica.htm";
    }
    else 
    {
    	if(anagAziendaVO.getDataCessazione() != null) 
    	{
    		ValidationError error = new ValidationError(""+AnagErrors.get("AZIENDA_CESSATA"));
			  url = "/layout/anagrafica.htm";
			  errors.add("error", error);
			  request.setAttribute("errors", errors);
			  request.getRequestDispatcher(url).forward(request, response);
    	}
      // Se è possibile procedere con la cessazione
      else 
      {
      	// Setto la data di cessazione
      	request.setAttribute("dataCessazione", DateUtils.getCurrentDateString());
      }
  	}
	}

%>

<jsp:forward page="<%=url%>"/>

<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.anag.services.MandatoVO" %>
<%@ page import="it.csi.solmr.dto.anag.services.DelegaAnagrafeVO" %>
<%@ page import="it.csi.smrcomms.siapcommws.InvioPostaCertificata" %>
<%@ page import="it.csi.smrcomms.siapcommws.DatiMailVO" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.datientiprivati.DatiEntePrivatoVO" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

	String iridePageName = "chiusuraNotificaCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%
 	
 	final String errMsg = "Impossibile procedere nella sezione chiudi una notifica."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/notifiche.htm";
  String erroreViewUrl = "/view/erroreView.jsp";

	String chiusuraNotificaUrl = "/view/chiusuraNotificaView.jsp";
	String notificheCtrlUrl = "/ctrl/notificheCtrl.jsp";
	String notificheUrl = "/view/notificheView.jsp";
	String errorPage = request.getParameter("paginaChiamante");
	String dettaglioNotificaUrl = "/view/dettaglioNotificaView.jsp";

	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

	ValidationErrors errors = new ValidationErrors();

	Vector elencoNotifiche = null;
	NotificaVO notificaVO = null;

	// L'utente ha premuto il pulsante salva
	if(request.getParameter("salva") != null) 
  {
  	// Recupero i parametri
  	String noteChiusura = request.getParameter("noteChiusura");
  	Long idNotifica = Long.decode(request.getParameter("idNotifica"));
  	// Recupero il VO
  	try 
    {
    	notificaVO = anagFacadeClient.findNotificaByPrimaryKey(idNotifica, null);
  	}
  	catch(SolmrException se) 
    {
   		ValidationError error = new ValidationError(se.getMessage());
   		errors.add("error",error);
   		request.setAttribute("errors", errors);
   		request.getRequestDispatcher(chiusuraNotificaUrl).forward(request, response);
   		return;
  	}

  	// Setto i parametri nel VO
  	notificaVO.setNoteChisura(noteChiusura);

  	// Metto l'oggetto in request
  	request.setAttribute("chiusuraNotificaVO", notificaVO);

  	
  	boolean flagInvioMailTutteUv = false;
  	
  	String[] idStoricoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
  	boolean flagInvioMailTutteParticelle = false;
    String[] idStoricoParticella = request.getParameterValues("idStoricoParticella");
    Vector<StoricoParticellaVO> elencoParticelle = notificaVO.getElencoParticelle();
    if(Validator.isNotEmpty(notificaVO.getIdTipoEntita()) 
      && SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(notificaVO.getIdTipoEntita().toString()))
    { 
      if(Validator.isNotEmpty(elencoParticelle) && (elencoParticelle.size() > 0))
      {   
		    if(Validator.isEmpty(idStoricoUnitaArborea))
		    {
		      ValidationError error = new ValidationError(AnagErrors.ERR_NO_ENTITA_NOTIFICA_CHIUSURA);
	        errors.add("salva", error);
		    }
		    else
		    {
		      //se è valorizzato il motivo chiusura della notifica devono anche 
		      //essere selzionate tutte le uv!!
		      if(Validator.isNotEmpty(noteChiusura))
		      {
		        if(notificaVO.getvNotificaEntita().size() != idStoricoUnitaArborea.length)
	          {
		          ValidationError error = new ValidationError(AnagErrors.ERR_NO_TUTTE_ENTITA_NOTIFICA_CHIUSURA);
		          errors.add("salva", error);	          
		        }
		        else
		        {
		          flagInvioMailTutteUv = true;
		        }	      
		      }
		      else
		      {
		        if(notificaVO.getvNotificaEntita().size() == idStoricoUnitaArborea.length)
	          {
	            errors.add("noteChiusura", new ValidationError( (String) AnagErrors.get("ERR_NOTE_CHIUSURA_OBBLIGATORIE")));
	          }	      
		      }
		    
		    }   
	    }
	    else
	    {
	      errors = notificaVO.validateCloseNotifica();
	    }
	  }
	  else if(Validator.isNotEmpty(notificaVO.getIdTipoEntita()) 
      && SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(notificaVO.getIdTipoEntita().toString()))
    { 
      if(Validator.isNotEmpty(elencoParticelle) && (elencoParticelle.size() > 0))
      {   
	      if(Validator.isEmpty(idStoricoParticella))
	      {
	        ValidationError error = new ValidationError(AnagErrors.ERR_NO_ENTITA_NOTIFICA_CHIUSURA);
	        errors.add("salva", error);
	      }
	      else
	      {
	        //se è valorizzato il motivo chiusura della notifica devono anche 
	        //essere selzionate tutte le uv!!
	        if(Validator.isNotEmpty(noteChiusura))
	        {
	          if(notificaVO.getvNotificaEntita().size() != idStoricoParticella.length)
	          {
	            ValidationError error = new ValidationError(AnagErrors.ERR_NO_TUTTE_ENTITA_NOTIFICA_CHIUSURA);
	            errors.add("salva", error);           
	          }
	          else
	          {
	            flagInvioMailTutteParticelle = true;
	          }       
	        }
	        else
	        {
	          if(notificaVO.getvNotificaEntita().size() == idStoricoParticella.length)
	          {
	            errors.add("noteChiusura", new ValidationError( (String) AnagErrors.get("ERR_NOTE_CHIUSURA_OBBLIGATORIE")));
	          } 
	        }
	      
	      }   
      }
      else
      {
        errors = notificaVO.validateCloseNotifica();
      }
    } 
	  else
	  {
	    // Effettuo la validazione formale dei dati
      errors = notificaVO.validateCloseNotifica();
	  }

  	// Se ci sono errori li visualizzo
  	if(errors.size() > 0) 
    {
   		request.setAttribute("errors", errors);
   		request.getRequestDispatcher(chiusuraNotificaUrl).forward(request, response);
   		return;
  	}
  	
  	
  	
  	
    
    if(Validator.isNotEmpty(elencoParticelle) && (elencoParticelle.size() > 0))
    {   
      Vector<NotificaEntitaVO> vNotificaEntita = notificaVO.getvNotificaEntita();
      Vector<ValidationErrors> elencoErroriNote = new Vector<ValidationErrors>();
      int countErroriNote = 0;
      
      if(Validator.isNotEmpty(notificaVO.getIdTipoEntita()) 
	      && SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(notificaVO.getIdTipoEntita().toString()))
	    {
	      String[] arrNoteUv = request.getParameterValues("noteChiusuraUV");
	      for(int i=0;i<elencoParticelle.size();i++)
	      {
	        NotificaEntitaVO notificaEntitaVO = vNotificaEntita.get(i);
	        ValidationErrors errorsNoteParticelle = new ValidationErrors();
	        //nn sono state selezionate tutte le uv
	        // è obbligatorio inserire le note di chiusura
	        if(notificaVO.getvNotificaEntita().size() != idStoricoUnitaArborea.length)
	        {
	          for(int j=0;j<idStoricoUnitaArborea.length;j++)
	          {
	            if(elencoParticelle.get(i).getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea()
	              .toString().equalsIgnoreCase(idStoricoUnitaArborea[j]))
	            {
			          if(Validator.isEmpty(arrNoteUv))
			          {
			            errorsNoteParticelle.add("noteChiusuraUV", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
			            countErroriNote++;
			          }      
				        else if(Validator.isEmpty(arrNoteUv[i]))
				        {
				          errorsNoteParticelle.add("noteChiusuraUV", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
				          countErroriNote++;
				        }
				        else if(arrNoteUv[i].length() > 2000)
	              {
	                errorsNoteParticelle.add("noteChiusuraUV", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	                countErroriNote++;
	              }
				        else
	              {
	                notificaEntitaVO.setNoteChiusuraEntita(arrNoteUv[i]);
	              }
	            }
	          }
		        
	          
	        }
	        else
	        {
	          if(Validator.isEmpty(arrNoteUv))
	          {
	            notificaEntitaVO.setNoteChiusuraEntita(noteChiusura);
	          }      
	          else 
	          {
	            if(Validator.isEmpty(arrNoteUv[i]))
	            {
	              notificaEntitaVO.setNoteChiusuraEntita(noteChiusura);
	            }
		          else if(arrNoteUv[i].length() > 2000)
		          {
		            errorsNoteParticelle.add("noteChiusuraUV", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
		            countErroriNote++;
		          }
		          else
		          {
		            notificaEntitaVO.setNoteChiusuraEntita(arrNoteUv[i]);
		          }
		        }
	        
	        }
	        
	        elencoErroriNote.add(errorsNoteParticelle);            
	      
	      }
	    }
	    else if(Validator.isNotEmpty(notificaVO.getIdTipoEntita()) 
        && SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(notificaVO.getIdTipoEntita().toString()))
      {
        String[] arrNoteParticelle = request.getParameterValues("noteChiusuraParticelle");
        for(int i=0;i<elencoParticelle.size();i++)
        {
          NotificaEntitaVO notificaEntitaVO = vNotificaEntita.get(i);
          ValidationErrors errorsNoteParticelle = new ValidationErrors();
          //nn sono state selezionate tutte le uv
          // è obbligatorio inserire le note di chiusura
          if(notificaVO.getvNotificaEntita().size() != idStoricoParticella.length)
          {
            for(int j=0;j<idStoricoParticella.length;j++)
            {
              if(elencoParticelle.get(i).getIdStoricoParticella()
                .toString().equalsIgnoreCase(idStoricoParticella[j]))
              {
                if(Validator.isEmpty(arrNoteParticelle))
                {
                  errorsNoteParticelle.add("noteChiusuraParticelle", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
                  countErroriNote++;
                }      
                else if(Validator.isEmpty(arrNoteParticelle[i]))
                {
                  errorsNoteParticelle.add("noteChiusuraParticelle", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
                  countErroriNote++;
                }
                else if(arrNoteParticelle[i].length() > 2000)
                {
                  errorsNoteParticelle.add("noteChiusuraParticelle", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
                  countErroriNote++;
                }
                else
                {
                  notificaEntitaVO.setNoteChiusuraEntita(arrNoteParticelle[i]);
                }
              }
            }
            
            
          }
          else
          {
            if(Validator.isEmpty(arrNoteParticelle))
            {
              notificaEntitaVO.setNoteChiusuraEntita(noteChiusura);
            }      
            else 
            {
              if(Validator.isEmpty(arrNoteParticelle[i]))
              {
                notificaEntitaVO.setNoteChiusuraEntita(noteChiusura);
              }
              else if(arrNoteParticelle[i].length() > 2000)
              {
                errorsNoteParticelle.add("noteChiusuraParticelle", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
                countErroriNote++;
              }
              else
              {
                notificaEntitaVO.setNoteChiusuraEntita(arrNoteParticelle[i]);
              }
            }
          
          }
          
          elencoErroriNote.add(errorsNoteParticelle);            
        
        }
      }
      
      if(countErroriNote > 0) 
      {
        request.setAttribute("elencoErroriNote", elencoErroriNote);
        request.getRequestDispatcher(chiusuraNotificaUrl).forward(request, response);
        return;
      }     
      
      notificaVO.setvNotificaEntita(vNotificaEntita);
    }	
  	
  	
  	// Se non ci sono errori proseguo con la chiusura della notifica
  	try 
    {
    	anagFacadeClient.closeNotifica(notificaVO, ruoloUtenza.getIdUtente());
  	}
  	catch(SolmrException se) 
    {
  		ValidationError error = new ValidationError(se.getMessage());
  		errors.add("error",error);
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(chiusuraNotificaUrl).forward(request, response);
  		return;
  	}
  	
  	
  	String parametroInvioMailNotif = "";
    try 
    {
      parametroInvioMailNotif = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_INVIO_MAIL_NOTIF);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - chiusuraNotificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_INVIO_MAIL_NOTIF+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    SolmrLogger.debug(this, " --- ABILITA MAIL ="+parametroInvioMailNotif);
  	
  	boolean flagInvioMail = false;
  	if("S".equalsIgnoreCase(notificaVO.getInviaEmail())
       && "S".equalsIgnoreCase(parametroInvioMailNotif)
       && flagInvioMailTutteUv )
    {
      flagInvioMail = true;
    }
  	
  	//Invio mail
    if(flagInvioMail)
    {
      try 
      {        
        // ** Costruisco gli oggetti con i dati per l'invio mail      
        SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
        
        // Recupero i dati relativi all'utente che ha inserito la notifica
		    UtenteAbilitazioni utenteAbilitazioni = anagFacadeClient.getUtenteAbilitazioniByIdUtenteLogin(notificaVO.getIdUtenteInserimento());
        RuoloUtenza ruoloUtenzaInserimentoNotifica = new RuoloUtenzaPapua(utenteAbilitazioni); 
		        
		    String[] codiceEntePrivato = new String[1];
		    codiceEntePrivato[0] = ruoloUtenzaInserimentoNotifica.getCodiceEnte();
		        
		    SolmrLogger.debug(this, " -- chiamata a smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange con codiceEntePrivato ="+codiceEntePrivato);
		    long[] arrIdEntePrivato = anagFacadeClient.smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(
		      codiceEntePrivato, false);
		   
		    DatiEntePrivatoVO[] arrEntePrivato = null;
		    if(Validator.isNotEmpty(arrIdEntePrivato) && (arrIdEntePrivato.length > 0))
		    {   
		      SolmrLogger.debug(this, " -- chiamata a smrcommGetEntiPrivatiByIdEntePrivatoRange"); 	
		      arrEntePrivato = anagFacadeClient.smrcommGetEntiPrivatiByIdEntePrivatoRange(
		        arrIdEntePrivato, 2, null);
		    }
		    
		    Vector<String> destinatariMailA = new Vector<String>();		   		   
		    
		    if(Validator.isNotEmpty(arrEntePrivato) && (arrEntePrivato.length > 0))
		    {
		      for(int i=0;i<arrEntePrivato.length;i++)
		      {
		        if(Validator.isNotEmpty(arrEntePrivato[i].getDatiRecapitoEntePrivato()))
		        {
		          for(int j=0;j<arrEntePrivato[i].getDatiRecapitoEntePrivato().length;j++)
		          {
		            if(arrEntePrivato[i].getDatiRecapitoEntePrivato()[j].getIdTipoRecapito() == 5)
		            {
		              SolmrLogger.debug(this, " -- recapito ="+arrEntePrivato[i].getDatiRecapitoEntePrivato()[j].getRecapito());
		              destinatariMailA.add(arrEntePrivato[i].getDatiRecapitoEntePrivato()[j].getRecapito());
		            }
		          }		        
		        }
		      }		    
		    }
		     
        
        String destinatarioMail = "";
        for(int i=0;i<destinatariMailA.size();i++)
        {
          destinatarioMail += destinatariMailA.get(i)+",";
        }
        SolmrLogger.debug(this, " --- sono stati trovati dei DESTINATARIO :"+destinatarioMail);
        
        if(destinatariMailA.size() > 0)
        {
          
          // ----- Mittente -> VTMA di DB_ALTRI_DATI       
          String mittente = "";
          try 
          {
            mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_NOTIF);
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - chiusuraNotificaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MITT_MAIL_NOTIF+".\n"+se.toString();
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
            oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_MAIL_NOTIF);
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - chiusuraNotificaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_MAIL_NOTIF+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          if(Validator.isNotEmpty(oggettoMail))
          { 
            oggettoMail = oggettoMail.replaceAll("<<CUAA>>",anagAziendaVO.getCUAA());
            oggettoMail = oggettoMail.replaceAll("<<TIPO_NOTIFICA>>", notificaVO.getDescCategoriaNotifica());          
          }
            
          SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
              
              
          // ----- Testo
          String testo = "";
          try 
          {
            testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAIL_PER_CH_NOTIF);
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - chiusuraNotificaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAIL_PER_NOTIF+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          if(Validator.isNotEmpty(testo))
          {                   
            testo = testo.replaceAll("<<DATA_CREAZIONE>>", DateUtils.getCurrent());
            testo = testo.replaceAll("<<TIPO_NOTIFICA>>", notificaVO.getDescCategoriaNotifica());
            testo = testo.replaceAll("<<CUAA>>",anagAziendaVO.getCUAA());
            testo = testo.replaceAll("<<DENOMINAZIONE>>",anagAziendaVO.getDenominazione());       
            
          }
          SolmrLogger.debug(this, " --- TESTO ="+testo);
              
          
          // Setto i valori per le mail da inviare
          InvioPostaCertificata invioPosta = new InvioPostaCertificata();
          DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
          DatiMailVO datiMailVO = new DatiMailVO(); 
          
          String[] arrDestinatari = new String[destinatariMailA.size()];
          for(int k=0;k<destinatariMailA.size();k++)
          {
            arrDestinatari[k] = destinatariMailA.get(k);
          }
          datiMailVO.setDestinatariA(arrDestinatari);
          datiMailVO.setMittente(mittente);
          datiMailVO.setMittenteDisplayName(mittenteReplyTo);
          datiMailVO.setOggetto(oggettoMail);
          datiMailVO.setTesto(testo);
          
          
          arrDatiMail[0] = datiMailVO;
          invioPosta.setInput(arrDatiMail);
            
            
          anagFacadeClient.serviceInviaPostaCertificata(invioPosta);
          
          
          
          
          //destinatariMailA.add(destinatarioMail);           
          
          /*DatiMail datiMail = new DatiMail();
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
        
      }
      catch (SolmrException e) 
      {
        SolmrLogger.info(this, " - chiusuraNotificaCtrl.jsp - FINE PAGINA");
        String messaggio = AnagErrors.ERRORE_KO_INVIO_MAIL+".\n"+e.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
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

  	// Effettuo la ricerca delle notifiche
  	// Creo l'oggetto contenente i criteri per effettuare la ricerca
  	NotificaVO notificaRicercaVO = new NotificaVO();
  	notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
  	notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
  	try 
    {
    	elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(false), null);
  	}
  	catch(SolmrException se) 
    {
  		if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
      {
    		ValidationError error = new ValidationError(se.getMessage());
    		errors.add("error",error);
    		request.setAttribute("errors", errors);
    		request.getRequestDispatcher(errorPage).forward(request, response);
    		return;
  		}
  		else 
      {
    		request.setAttribute("messaggio", se.getMessage());
    		%>
      		<jsp:forward page= "<%= notificheUrl %>" />
    		<%
  		}
  	}

  	// Se trovo delle notifiche metto il vettore in request
  	request.setAttribute("elencoNotifiche", elencoNotifiche);

  	// Vado alla pagina di elenco notifiche
  	%>
    		<jsp:forward page= "<%= notificheCtrlUrl %>" />
  	<%

	}
	// L'utente ha selezionato il pulsante annulla
	else if(request.getParameter("annulla") != null) 
  {
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
 		// Effettuo la ricerca delle notifiche
 		// Creo l'oggetto contenente i criteri per effettuare la ricerca
 		NotificaVO notificaRicercaVO = new NotificaVO();
 		notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
 		notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
 		try 
     {
   		elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(false), null);
 		}
 		catch(SolmrException se) 
     {
   		if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
       {
   			ValidationError error = new ValidationError(se.getMessage());
   			errors.add("error",error);
   			request.setAttribute("errors", errors);
   			request.getRequestDispatcher(errorPage).forward(request, response);
   			return;
   		}
   		else 
       {
   			request.setAttribute("messaggio", se.getMessage());
   			%>
     			<jsp:forward page= "<%= notificheUrl %>" />
   			<%
   		}
 		}

 		// Se trovo delle notifiche metto il vettore in request
 		request.setAttribute("elencoNotifiche", elencoNotifiche);

 		// Vado alla pagina di elenco delle notifiche
 		%>
   		<jsp:forward page= "<%= notificheUrl %>" />
 		<%
  	return;
	}
	// L'utente ha selezionato la funzione chiusura notifica
	else 
  {
  	// Recupero l'id notifica dell'elemento selezionato
  	Long idNotifica = Long.decode(request.getParameter("idNotifica"));
  	request.setAttribute("idNotifica", idNotifica);
  	// Recupero il tipo di visualizzazione
  	boolean storico = request.getParameter("storico") != null;
  	request.setAttribute("storico", new Boolean(storico));
  	// Recupero l'oggetto notifica
  	try 
    {
    	notificaVO = anagFacadeClient.findNotificaByPrimaryKey(idNotifica, null);
  	}
  	catch(SolmrException se) 
    {
  		ValidationError error = new ValidationError(se.getMessage());
  		errors.add("error",error);
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(errorPage).forward(request, response);
  		return;
  	}
  	// Se la notifica è già stata chiusa avviso l'utente dell'impossibilità
  	// di effettuare l'operazione
  	if(Validator.isNotEmpty(notificaVO.getDataChiusura())) 
    {
    	if(errorPage.equalsIgnoreCase(notificheUrl)) 
      {
    		// Effettuo la ricerca delle notifiche
    		// Creo l'oggetto contenente i criteri per effettuare la ricerca
    		NotificaVO notificaRicercaVO = new NotificaVO();
    		notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
    		notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    		try 
        {
      		elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
    		}
    		catch(SolmrException se) 
        {
    			if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
          {
      			ValidationError error = new ValidationError(se.getMessage());
      			errors.add("error",error);
      			request.setAttribute("errors", errors);
      			request.getRequestDispatcher(errorPage).forward(request, response);
      			return;
    			}
    			else 
          {
      			request.setAttribute("messaggio", se.getMessage());
    			}
    		}
      	request.setAttribute("elencoNotifiche", elencoNotifiche);
    	}
  		else 
      {
    		// Metto l'oggetto in request
    		request.setAttribute("dettaglioNotificaVO", notificaVO);
  		}
  		ValidationError error = new ValidationError((String)AnagErrors.get("ERR_NOTIFICA_CHIUSA"));
  		errors.add("error",error);
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(errorPage).forward(request, response);
  		return;
  	}
  	// Se non lo è e il profilo dell'utente loggato non è assistenza CSI
    RuoloUtenza ruoloUtenzaInserimentoNotifica = null;
    
    // Recupero i dati relativi all'utente che ha inserito la notifica
    try 
    {
              
      UtenteAbilitazioni utenteAbilitazioni = anagFacadeClient.getUtenteAbilitazioniByIdUtenteLogin(notificaVO.getIdUtenteInserimento());
      ruoloUtenzaInserimentoNotifica = new RuoloUtenzaPapua(utenteAbilitazioni); 
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_DATI_PROFILO_NOTIFICA);
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
    
    
    //Prima fase controlli..
    //Se chi ha inserito lanotifica è asistenza csi (Batch)
    //devo fare dei controlli a DOC.
    //Se il controllo è positivo salto i successivi altrimenti li eseguo
    //boolean flagChiudiOK = false;
    /*if(ruoloUtenzaInserimentoNotifica.isUtenteAssistenzaCsi())
    {
      if(ruoloUtenza.isUtenteOPR())
      {
        flagChiudiOK = true;
      }
      
      
      if(ruoloUtenza.isUtenteIntermediario())
      {
        flagChiudiOK = true;
        String codiceIntermediario = ruoloUtenza.getCodiceEnte();
        MandatoVO mandatoVO = anagFacadeClient.serviceGetMandato(anagAziendaVO.getIdAzienda(),
          codiceIntermediario);
        DelegaAnagrafeVO delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
        if (delegaAnagrafeVO != null)
        {
	        // Se esiste una delega devo controllare che il codice dell'ente
	        // intermediario a cui appartiene questa delega sia lo stesso dell'ente
	        // intemediario dell'utente connesso o che sia stato valorizzato a "S"
	        // il
	        // FLAG_FIGLIO che è valorizzato in questo modo SE E SOLO SE l'ente che
	        // ha
	        // la delega è un figlio dell'ente il cui codice fiscale è stato passato
	        // al metodo serviceGetMandato
	        if (!(codiceIntermediario.equalsIgnoreCase(delegaAnagrafeVO.getCodiceFiscIntermediario()) 
	            || SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO.getFlagFiglio())))
          {
            flagChiudiOK = false;
          }
        }
        else
        {
          flagChiudiOK = false;
        }        
        
      }
      
      if(ruoloUtenza.isUtenteProvinciale())
      {
        if(!anagAziendaVO.isPossiedeDelegaAttiva())
        {
          if(ruoloUtenza.getIstatProvincia().equals(anagAziendaVO.getProvCompetenza()))
          {
            flagChiudiOK = true;
          } 
        }
      }     
      
    }*/
            
    
    //Controlli successivi
    //if(!flagChiudiOK)
    //{
      /*if(ruoloUtenza.isUtenteServiziAgri() 
        || (ruoloUtenza.isUtentePA() && ruoloUtenza.isUtenteRegionale() && ruoloUtenza.isReadWrite()))
      {
        //Non faccio nulla lascio proseguire (chiudere notifica).
        //Se l'utente è servizi agricoltura o l'utente è regionale pa in scrittura può chiudere 
        //tutte le notifiche
      }    
	  	else //if(!ruoloUtenza.isUtenteServiziAgri()) 
	    {*/
	  		// Verifico che l'utente che ha inserto la notifica abbia un profilo compatibile con l'utente loggato
 		if("S".equalsIgnoreCase(notificaVO.getChiusaDaAltroRuolo()))
 		{
 		  if(!anagFacadeClient.isChiusuraNotificaRuoloPossibile(ruoloUtenza, notificaVO.getIdCategoriaNotifica().longValue()))
 		  {
 		    // Effettuo la ricerca delle notifiche
          // Creo l'oggetto contenente i criteri per effettuare la ricerca
          NotificaVO notificaRicercaVO = new NotificaVO();
          notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
          notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
          try 
          {
            elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
          }
          catch(SolmrException se) 
          {
            if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
            {
              ValidationError error = new ValidationError(se.getMessage());
              errors.add("error",error);
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(errorPage).forward(request, response);
              return;
            }
            else 
            {
              request.setAttribute("messaggio", se.getMessage());
            }
          }
          request.setAttribute("elencoNotifiche", elencoNotifiche);
          // Segnalo l'errore   
          ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_RUOLO);
          errors.add("error",error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(errorPage).forward(request, response);
          return;
 		  }	  		
 		}
 		else
 		{
 		
  		if(ruoloUtenza.isUtentePA()) 
      {
        if(!ruoloUtenzaInserimentoNotifica.isUtentePA())
        {
          // Effettuo la ricerca delle notifiche
           // Creo l'oggetto contenente i criteri per effettuare la ricerca
           NotificaVO notificaRicercaVO = new NotificaVO();
           notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
           notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
           try 
           {
             elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
           }
           catch(SolmrException se) 
           {
             if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
             {
               ValidationError error = new ValidationError(se.getMessage());
               errors.add("error",error);
               request.setAttribute("errors", errors);
               request.getRequestDispatcher(errorPage).forward(request, response);
               return;
             }
             else 
             {
               request.setAttribute("messaggio", se.getMessage());
             }
           }
           request.setAttribute("elencoNotifiche", elencoNotifiche);
           // Segnalo l'errore   
           ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
           errors.add("error",error);
           request.setAttribute("errors", errors);
           request.getRequestDispatcher(errorPage).forward(request, response);
           return;	        
        }
        else
        {
  			// Se l'utente loggato è un regionale
	  			if(ruoloUtenza.isUtenteRegionale()) 
	        {
	          if(ruoloUtenza.isReadOnly())
	          {
	            if(ruoloUtenzaInserimentoNotifica.isReadWrite())
	  			    {
	    					// Effettuo la ricerca delle notifiche
	          		// Creo l'oggetto contenente i criteri per effettuare la ricerca
	          		NotificaVO notificaRicercaVO = new NotificaVO();
	          		notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
	          		notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
	          		try 
	              {
	            		elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
	          		}
	          		catch(SolmrException se) 
	              {
	            		if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
	                {
	            			ValidationError error = new ValidationError(se.getMessage());
	            			errors.add("error",error);
	            			request.setAttribute("errors", errors);
	            			request.getRequestDispatcher(errorPage).forward(request, response);
	            			return;
	          			}
	          			else 
	                {
	            			request.setAttribute("messaggio", se.getMessage());
	          			}
	          		}
	          		request.setAttribute("elencoNotifiche", elencoNotifiche);
	      				// Segnalo l'errore		
	      				ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
	  	      		errors.add("error",error);
	  	      		request.setAttribute("errors", errors);
	  	      		request.getRequestDispatcher(errorPage).forward(request, response);
	  	      		return;
	    				}
	  			  }
	        }
	  			// Se si tratta di un utente provinciale
	  			else if(ruoloUtenza.isUtenteProvinciale()) 
	        {
	          //Se i provinciali sono di due amministrazioni di competenza diverse 
	          //non posso chiudere la notifica
	  				if(!ruoloUtenza.getCodiceEnte().equalsIgnoreCase(
	            ruoloUtenzaInserimentoNotifica.getCodiceEnte())) 
	          {
	  					// Effettuo la ricerca delle notifiche
	        		// Creo l'oggetto contenente i criteri per effettuare la ricerca
	        		NotificaVO notificaRicercaVO = new NotificaVO();
	        		notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
	        		notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
	        		try 
	            {
	          	  elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
	        		}
	        		catch(SolmrException se) 
	            {
	          		if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
	              {
	          			ValidationError error = new ValidationError(se.getMessage());
	                errors.add("error",error);
	          			request.setAttribute("errors", errors);
	          			request.getRequestDispatcher(errorPage).forward(request, response);
	          			return;
	        			}
	        			else 
	              {
	          			request.setAttribute("messaggio", se.getMessage());
	        			}
	        		}
	        		request.setAttribute("elencoNotifiche", elencoNotifiche);
	    				// Segnalo l'errore		
	    				ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
		      		errors.add("error",error);
		      		request.setAttribute("errors", errors);
		      		request.getRequestDispatcher(errorPage).forward(request, response);
		      		return;
	  				}
	          else
				    {
	            //Se l'utente loggato è in sola lettura e l'utente che ha fatto la modifica è in scrittura
	            //non posso chiudere la notifica.
	            if(ruoloUtenza.isReadOnly())
	            {
	              if(ruoloUtenzaInserimentoNotifica.isReadWrite())
	              {
	                // Effettuo la ricerca delle notifiche
	                // Creo l'oggetto contenente i criteri per effettuare la ricerca
	                NotificaVO notificaRicercaVO = new NotificaVO();
	                notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
	                notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
	                try 
	                {
	                  elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
	                }
	                catch(SolmrException se) 
	                {
	                  if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
	                  {
	                    ValidationError error = new ValidationError(se.getMessage());
	                    errors.add("error",error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(errorPage).forward(request, response);
	                    return;
	                  }
	                  else 
	                  {
	                    request.setAttribute("messaggio", se.getMessage());
	                  }
	                }
	                request.setAttribute("elencoNotifiche", elencoNotifiche);
	                // Segnalo l'errore   
	                ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
	                errors.add("error",error);
	                request.setAttribute("errors", errors);
	                request.getRequestDispatcher(errorPage).forward(request, response);
	                return;
	              }
	            }
	  		    }
	        }
	        else
	        {
	          if(!ruoloUtenza.getCodiceRuolo().equalsIgnoreCase(
	            ruoloUtenzaInserimentoNotifica.getCodiceRuolo()))
	          {
	            // Effettuo la ricerca delle notifiche
	            // Creo l'oggetto contenente i criteri per effettuare la ricerca
	            NotificaVO notificaRicercaVO = new NotificaVO();
	            notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
	            notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
	            try 
	            {
	              elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
	            }
	            catch(SolmrException se) 
	            {
	              if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
	              {
	                ValidationError error = new ValidationError(se.getMessage());
	                errors.add("error",error);
	                request.setAttribute("errors", errors);
	                request.getRequestDispatcher(errorPage).forward(request, response);
	                return;
	              }
	              else 
	              {
	                request.setAttribute("messaggio", se.getMessage());
	              }
	            }
	            request.setAttribute("elencoNotifiche", elencoNotifiche);
	            // Segnalo l'errore   
	            ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
	            errors.add("error",error);
	            request.setAttribute("errors", errors);
	            request.getRequestDispatcher(errorPage).forward(request, response);
	            return;        
	          }
	          else
	          {
	            if(ruoloUtenza.isReadOnly())
	            {
	              if(ruoloUtenzaInserimentoNotifica.isReadWrite())
	              {
	                // Effettuo la ricerca delle notifiche
	                // Creo l'oggetto contenente i criteri per effettuare la ricerca
	                NotificaVO notificaRicercaVO = new NotificaVO();
	                notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
	                notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
	                try 
	                {
	                  elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
	                }
	                catch(SolmrException se) 
	                {
	                  if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
	                  {
	                    ValidationError error = new ValidationError(se.getMessage());
	                    errors.add("error",error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(errorPage).forward(request, response);
	                    return;
	                  }
	                  else 
	                  {
	                    request.setAttribute("messaggio", se.getMessage());
	                  }
	                }
	                request.setAttribute("elencoNotifiche", elencoNotifiche);
	                // Segnalo l'errore   
	                ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
	                errors.add("error",error);
	                request.setAttribute("errors", errors);
	                request.getRequestDispatcher(errorPage).forward(request, response);
	                return;
	              }            
	            }
	          }       
	        
	        }
	      }
      } //fine if ruolo utente PA
  		// Se si tratta di intermediario
  		else if(ruoloUtenza.isUtenteIntermediario()) 
      {
        if(!ruoloUtenzaInserimentoNotifica.isUtenteIntermediario())
        {
          // Effettuo la ricerca delle notifiche
          // Creo l'oggetto contenente i criteri per effettuare la ricerca
          NotificaVO notificaRicercaVO = new NotificaVO();
          notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
          notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
          try 
          {
            elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
          }
          catch(SolmrException se) 
          {
            if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
            {
              ValidationError error = new ValidationError(se.getMessage());
              errors.add("error",error);
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(errorPage).forward(request, response);
              return;
            }
            else 
            {
              request.setAttribute("messaggio", se.getMessage());
            }
          }
          request.setAttribute("elencoNotifiche", elencoNotifiche);
          // Segnalo l'errore   
          ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
          errors.add("error",error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(errorPage).forward(request, response);
          return;
        }
        else
        {        
          boolean isOk = false;
     			try 
          {
    				isOk = anagFacadeClient.verificaGerarchia(ruoloUtenza.getIdUtente(), notificaVO.getIdUtenteInserimento());
    			}
    			catch(SolmrException se) 
          {
    				ValidationError error = new ValidationError(AnagErrors.PROBLEMI_ACCESSO_COMUNE);
        		errors.add("error",error);
        		request.setAttribute("errors", errors);
        		request.getRequestDispatcher(errorPage).forward(request, response);
        		return;
    			}
    			if(!isOk) 
          {
    				// Effettuo la ricerca delle notifiche
        		// Creo l'oggetto contenente i criteri per effettuare la ricerca
        		NotificaVO notificaRicercaVO = new NotificaVO();
        		notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
        		notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
        		try 
            {
          		elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
        		}
        		catch(SolmrException se) 
            {
        			if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
              {
          			ValidationError error = new ValidationError(se.getMessage());
          			errors.add("error",error);
          			request.setAttribute("errors", errors);
          			request.getRequestDispatcher(errorPage).forward(request, response);
          			return;
        			}
        			else 
              {
          			request.setAttribute("messaggio", se.getMessage());
        			}
        		}
        		request.setAttribute("elencoNotifiche", elencoNotifiche);
    				// Segnalo l'errore		
    				ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
        		errors.add("error",error);
        		request.setAttribute("errors", errors);
        		request.getRequestDispatcher(errorPage).forward(request, response);
        		return;
    			}
          else
    		  {
            //Se l'utente loggato è in sola lettura e l'utente che ha fatto la modifica è in scrittura
            //non posso chiudere la notifica.
            if(ruoloUtenza.isReadOnly())
            {
              if(ruoloUtenzaInserimentoNotifica.isReadWrite())
              {
                // Effettuo la ricerca delle notifiche
                // Creo l'oggetto contenente i criteri per effettuare la ricerca
                NotificaVO notificaRicercaVO = new NotificaVO();
                notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
                notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
                try 
                {
                  elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
                }
                catch(SolmrException se) 
                {
                  if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
                  {
                    ValidationError error = new ValidationError(se.getMessage());
                    errors.add("error",error);
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher(errorPage).forward(request, response);
                    return;
                  }
                  else 
                  {
                    request.setAttribute("messaggio", se.getMessage());
                  }
                }
                request.setAttribute("elencoNotifiche", elencoNotifiche);
                // Segnalo l'errore   
                ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
                errors.add("error",error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(errorPage).forward(request, response);
                return;
              }
            }
    	    }
        }
      }
      // Se si tratta di intermediario o OPR gestore
      /*else if(ruoloUtenza.isUtenteOPRGestore()) 
      {
      
        if(!ruoloUtenzaInserimentoNotifica.isUtenteOPRGestore())
        {
          // Effettuo la ricerca delle notifiche
          // Creo l'oggetto contenente i criteri per effettuare la ricerca
          NotificaVO notificaRicercaVO = new NotificaVO();
          notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
          notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
          try 
          {
            elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
          }
          catch(SolmrException se) 
          {
            if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
            {
              ValidationError error = new ValidationError(se.getMessage());
              errors.add("error",error);
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(errorPage).forward(request, response);
              return;
            }
            else 
            {
              request.setAttribute("messaggio", se.getMessage());
            }
          }
          request.setAttribute("elencoNotifiche", elencoNotifiche);
          // Segnalo l'errore   
          ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
          errors.add("error",error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(errorPage).forward(request, response);
          return;
        }
        else
        {        
      
          boolean isOk = false;
          try 
          {
            isOk = anagFacadeClient.serviceVerificaGerarchia(ruoloUtenza.getIdUtente(), notificaVO.getIdUtenteInserimento());
          }
          catch(SolmrException se) 
          {
            ValidationError error = new ValidationError(AnagErrors.PROBLEMI_ACCESSO_COMUNE);
            errors.add("error",error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(errorPage).forward(request, response);
            return;
          }
          if(!isOk) 
          {
            // Effettuo la ricerca delle notifiche
            // Creo l'oggetto contenente i criteri per effettuare la ricerca
            NotificaVO notificaRicercaVO = new NotificaVO();
            notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
            notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
            try 
            {
              elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
            }
            catch(SolmrException se) 
            {
              if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
              {
                ValidationError error = new ValidationError(se.getMessage());
                errors.add("error",error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(errorPage).forward(request, response);
                return;
              }
              else 
              {
                request.setAttribute("messaggio", se.getMessage());
              }
            }
            request.setAttribute("elencoNotifiche", elencoNotifiche);
            // Segnalo l'errore   
            ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
            errors.add("error",error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(errorPage).forward(request, response);
            return;
          }
          else
          {
            //Se l'utente loggato è in sola lettura e l'utente che ha fatto la modifica è in scrittura
            //non posso chiudere la notifica.
            if(ruoloUtenza.isReadOnly())
            {
              if(ruoloUtenzaInserimentoNotifica.isReadWrite())
              {
                // Effettuo la ricerca delle notifiche
                // Creo l'oggetto contenente i criteri per effettuare la ricerca
                NotificaVO notificaRicercaVO = new NotificaVO();
                notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
                notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
                try 
                {
                  elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
                }
                catch(SolmrException se) 
                {
                  if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
                  {
                    ValidationError error = new ValidationError(se.getMessage());
                    errors.add("error",error);
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher(errorPage).forward(request, response);
                    return;
                  }
                  else 
                  {
                    request.setAttribute("messaggio", se.getMessage());
                  }
                }
                request.setAttribute("elencoNotifiche", elencoNotifiche);
                // Segnalo l'errore   
                ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
                errors.add("error",error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(errorPage).forward(request, response);
                return;
              }
            }
          }
        }
      }*/
      else
      {
        if(!ruoloUtenza.getCodiceRuolo().equalsIgnoreCase(
          ruoloUtenzaInserimentoNotifica.getCodiceRuolo()))
        {
          // Effettuo la ricerca delle notifiche
          // Creo l'oggetto contenente i criteri per effettuare la ricerca
          NotificaVO notificaRicercaVO = new NotificaVO();
          notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
          notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
          try 
          {
            elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
          }
          catch(SolmrException se) 
          {
            if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
            {
              ValidationError error = new ValidationError(se.getMessage());
              errors.add("error",error);
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(errorPage).forward(request, response);
              return;
            }
            else 
            {
              request.setAttribute("messaggio", se.getMessage());
            }
          }
          request.setAttribute("elencoNotifiche", elencoNotifiche);
          // Segnalo l'errore   
          ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
          errors.add("error",error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(errorPage).forward(request, response);
          return;        
        }
        else
        {
          if(ruoloUtenza.isReadOnly())
          {
            if(ruoloUtenzaInserimentoNotifica.isReadWrite())
            {
              // Effettuo la ricerca delle notifiche
              // Creo l'oggetto contenente i criteri per effettuare la ricerca
              NotificaVO notificaRicercaVO = new NotificaVO();
              notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
              notificaRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
              try 
              {
                elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaRicercaVO, new Boolean(storico), null);
              }
              catch(SolmrException se) 
              {
                if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
                {
                  ValidationError error = new ValidationError(se.getMessage());
                  errors.add("error",error);
                  request.setAttribute("errors", errors);
                  request.getRequestDispatcher(errorPage).forward(request, response);
                  return;
                }
                else 
                {
                  request.setAttribute("messaggio", se.getMessage());
                }
              }
              request.setAttribute("elencoNotifiche", elencoNotifiche);
              // Segnalo l'errore   
              ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE);
              errors.add("error",error);
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(errorPage).forward(request, response);
              return;
            }            
          }
        }       
      }
    } //else "N" chiusa altro ruolo
	      
	      
	   // } //else ruolo nn pa nn srvizi!! 
	  //}
  	request.setAttribute("chiusuraNotificaVO", notificaVO);

  	request.setAttribute("paginaChiamante", errorPage);
  	// Vado alla pagina di chiusura della notifica
  	%>
    		<jsp:forward page= "<%= chiusuraNotificaUrl %>" />
  	<%
	}

%>

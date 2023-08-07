<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoMotivoDichiarazioneVO" %>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "correzioneAnomaliaCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	String correzioneAnomaliaUrl = "/view/correzioneAnomaliaView.jsp";
 	String dichiarazioneAnomaliaUrl = "/view/dichiarazioneAnomaliaView.jsp";
  final String DICHIARAZIONE_CORRETTIVA = "C";

 	try 
  {
   	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
   	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
   	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
      Long idMotivoDichiarazione = (Long)session.getAttribute("idMotivoDichiarazione");
   	Vector<ErrAnomaliaDicConsistenzaVO> errAnomaliaDicConsistenzaVOs = null;
   	if(request.getParameter("annulla") != null) 
    {
      Vector<ErrAnomaliaDicConsistenzaVO> anomalie = anagFacadeClient
        .getErroriAnomalieDichiarazioneConsistenza(anagAziendaVO.getIdAzienda(),idMotivoDichiarazione);
    	if(anomalie != null) 
      {
        request.setAttribute("anomalieDichiarazioniConsistenza", anomalie);
      	HashMap<String,DocumentoVO> elencoDocumenti = new HashMap<String,DocumentoVO>();
      	DocumentoVO documentoVO = null;
     		//... vado a ricercare se esiste un documento attivo associato
     		// alla correzione dell'anomalia
     		for(int i = 0; i < anomalie.size(); i++) 
        {
     			ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = (ErrAnomaliaDicConsistenzaVO)anomalie.elementAt(i);
     			if(errAnomaliaDicConsistenzaVO.getIdDocumento() != null) 
          {
     			  try 
            {
     				  documentoVO = anagFacadeClient.findDocumentoVOByPrimaryKey(errAnomaliaDicConsistenzaVO.getIdDocumento());
     					elencoDocumenti.put(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione(),documentoVO);
     				}
       			catch(SolmrException se) 
            {
       			  ValidationErrors errors = new ValidationErrors();
       				errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_DOCUMENTO));
    			    request.setAttribute("errors", errors);
    			    request.getRequestDispatcher(dichiarazioneAnomaliaUrl).forward(request, response);
    				  return;
       			}
       		}
       	}
       	request.setAttribute("elencoDocumenti", elencoDocumenti);
       	
       	
       	
       	AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAziendaConValida(anagAziendaVO.getIdAzienda().longValue(), 
	      SolmrConstants.RICHIESTA_VALIDAZIONE);
	      boolean trovataRichiesta = false;
	      if(Validator.isNotEmpty(aziendaNuovaVO) 
	        && aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) == 0)
	      {
	        request.setAttribute("trovataRichiesta", "true");
	      }
       	
       	
       	
       	
    		%>
      		<jsp:forward page= "<%= dichiarazioneAnomaliaUrl %>" />
    		<%
    	}
  	}
   	if(request.getParameter("salva") != null) 
    {
    	SolmrLogger.debug(this, " - \n\n\ncorrezioneAnomaliaCtrl.jsp - salva\n\n\n");
    	ConsistenzaVO consistenzaVO = new ConsistenzaVO();
    	consistenzaVO.setPossibileProtocollo(false);
    	String parametroDCNP = null;
      try 
      {
       	parametroDCNP = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_COMUNE_DCNP);
       	if(SolmrConstants.FLAG_S.equals(parametroDCNP)) 
        {
          consistenzaVO.setPossibileProtocollo(true);
        }
      }
      catch(SolmrException se) 
      {
       	// Catturo l'eccezione ma non modifico il comportamento dell'applicativo: se non fosse possibile reperire il
        // valore da comune lo considero = null
      }
      request.setAttribute("consistenzaVO",consistenzaVO);
    		
    	String elencoIdDichiarazioneSegnalazione[] = request.getParameterValues("idDichiarazioneSegnalazione");
    	Vector<String> newElencoId = new Vector<String>();
    	String elencoFlagDocumentoGiustificativo[] = request.getParameterValues("flagDocumentoGiustificativo");
    	String elencoIdControllo[] = request.getParameterValues("idControllo");
    	String elencoIdStoricoParticella[] = request.getParameterValues("idStoricoParticella");
   		String elencoGiustificativo[] = request.getParameterValues("idDocumento");
   		errAnomaliaDicConsistenzaVOs = new Vector<ErrAnomaliaDicConsistenzaVO>();
   		int err = 0;
   		for(int i = 0; i < elencoIdDichiarazioneSegnalazione.length; i++) 
      {
 				if(Validator.isNotEmpty(elencoGiustificativo[i])) 
        {
    			ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = new ErrAnomaliaDicConsistenzaVO();
    			errAnomaliaDicConsistenzaVO.setIdDichiarazioneSegnalazione(elencoIdDichiarazioneSegnalazione[i]);
    			errAnomaliaDicConsistenzaVO.setFlagDocumentoGiustificativo(elencoFlagDocumentoGiustificativo[i]);
    			errAnomaliaDicConsistenzaVO.setIdControllo(elencoIdControllo[i]);
    			errAnomaliaDicConsistenzaVO.setIdStoricoParticella(elencoIdStoricoParticella[i]);
    			errAnomaliaDicConsistenzaVO.setIdDocumento(Long.decode(elencoGiustificativo[i]));
    			errAnomaliaDicConsistenzaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    			if(errAnomaliaDicConsistenzaVO.validateCorrezioneDichiarazione()) {
      			err++;
    			}
    			errAnomaliaDicConsistenzaVOs.add(errAnomaliaDicConsistenzaVO);
    			newElencoId.add(elencoIdDichiarazioneSegnalazione[i]);
 				}
  		}
   		if(err != 0) 
      {
     	  request.setAttribute("errAnomaliaDicConsistenzaVOs",errAnomaliaDicConsistenzaVOs);
   		}
   		else 
      {
     	  // PER la insert
     		if(newElencoId.size() > 0) 
        {
      	  anagFacadeClient.deleteInsertDichiarazioneCorrezione((String[])newElencoId.toArray(new String[newElencoId.size()]),
            errAnomaliaDicConsistenzaVOs, ruoloUtenza.getIdUtente().longValue());
     		}
     		Vector<ErrAnomaliaDicConsistenzaVO> anomalie = anagFacadeClient.getErroriAnomalieDichiarazioneConsistenza(anagAziendaVO.getIdAzienda(),idMotivoDichiarazione);
      	if(anomalie != null) 
        {
          request.setAttribute("anomalieDichiarazioniConsistenza", anomalie);
         	HashMap<String,DocumentoVO> elencoDocumenti = new HashMap<String,DocumentoVO>();
        	DocumentoVO documentoVO = null;
         	//... vado a ricercare se esiste un documento attivo associato
         	// alla correzione dell'anomalia
         	for(int i = 0; i < anomalie.size(); i++) 
          {
         	  ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = (ErrAnomaliaDicConsistenzaVO)anomalie.elementAt(i);
         		if(errAnomaliaDicConsistenzaVO.getIdDocumento() != null) 
            {
         		  try 
              {
         			  documentoVO = anagFacadeClient.findDocumentoVOByPrimaryKey(errAnomaliaDicConsistenzaVO.getIdDocumento());
         				elencoDocumenti.put(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione(), documentoVO);
         			}
         			catch(SolmrException se) 
              {
         			  ValidationErrors errors = new ValidationErrors();
         				errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_DOCUMENTO));
      			    request.setAttribute("errors", errors);
      			    request.getRequestDispatcher(dichiarazioneAnomaliaUrl).forward(request, response);
      				  return;
         			}
         		}
          }
          request.setAttribute("elencoDocumenti", elencoDocumenti);
      	}
      	
      	
      	
      	AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAziendaConValida(anagAziendaVO.getIdAzienda().longValue(), 
        SolmrConstants.RICHIESTA_VALIDAZIONE);
        boolean trovataRichiesta = false;
        if(Validator.isNotEmpty(aziendaNuovaVO) 
          && aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) == 0)
        {
          request.setAttribute("trovataRichiesta", "true");
        }
      	
      	
      	%>
          <jsp:forward page= "<%= dichiarazioneAnomaliaUrl %>" />
      	<%
    	}
   	}
   	String elencoIdDichiarazioneSegnalazioneStr[] = request.getParameterValues("idDichiarazioneSegnalazione");
   	Long elencoIdDichiarazioneSegnalazione[] = new Long[elencoIdDichiarazioneSegnalazioneStr.length];
   	for(int i = 0; i <elencoIdDichiarazioneSegnalazione.length; i++) 
    {
    	elencoIdDichiarazioneSegnalazione[i] = new Long(elencoIdDichiarazioneSegnalazioneStr[i]);
   	}
   	Vector<ErrAnomaliaDicConsistenzaVO> anomalie = anagFacadeClient.getAnomaliePerCorrezione(elencoIdDichiarazioneSegnalazione,idMotivoDichiarazione.longValue());
   	if(errAnomaliaDicConsistenzaVOs == null) 
    {
    	errAnomaliaDicConsistenzaVOs = anagFacadeClient.getErroriAnomalieDichiarazioneConsistenza(elencoIdDichiarazioneSegnalazione,idMotivoDichiarazione.longValue());
    	request.setAttribute("errAnomaliaDicConsistenzaVOs",errAnomaliaDicConsistenzaVOs);
   	}
   	if(anomalie == null || anomalie.size() == 0) 
   	{
    	throw new SolmrException((String)it.csi.solmr.etc.anag.AnagErrors.get("ERR_ANOMALIA_NO_GIUSTIFICATIVI"));
   	}
   	else 
    {
		  // Se sono presenti delle anomalie...
  	  HashMap elencoDocumentiCorrezioni = new HashMap();
		  DocumentoVO[] elencoDocumenti = null;
		  TipoDocumentoVO[] elencoTipiDocumento = null;
  		//... vado a ricercare se esistono dei documenti attivi associati
  		// all'azienda selezionata in grado di risolvere l'anomalia
  		String[] orderBy = {SolmrConstants.ORDER_BY_TIPO_DOCUMENTO_DESCRIPTION};
      String[] orderByData = {SolmrConstants.ORDER_BY_TIPO_DOCUMENTO_DESCRIPTION_DATA};
      //Ricavo il tipo di dichiarazione di conseistenza
      TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = anagFacadeClient.findTipoMotivoDichiarazioneByPrimaryKey(idMotivoDichiarazione);
  		for(int i = 0; i < anomalie.size(); i++) 
      {
  			ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = (ErrAnomaliaDicConsistenzaVO)anomalie.elementAt(i);
  			try 
        {
          String idStoricoParticella = errAnomaliaDicConsistenzaVO.getIdStoricoParticella();
            
          if((tipoMotivoDichiarazioneVO.getTipoDichiarazione() != null)
            && tipoMotivoDichiarazioneVO.getTipoDichiarazione().equalsIgnoreCase(DICHIARAZIONE_CORRETTIVA))
          {
            if(Validator.isNotEmpty(idStoricoParticella))
            {
              elencoDocumenti = anagFacadeClient.getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
                anagAziendaVO, Long.decode(errAnomaliaDicConsistenzaVO.getIdControllo()), 
                Long.decode(idStoricoParticella), tipoMotivoDichiarazioneVO.getAnnoCampagna(), orderByData);
            }
            else
            {
              elencoDocumenti = anagFacadeClient.getListDocumentiPerDichCorrettiveAziendaByIdControllo(
                anagAziendaVO, Long.decode(errAnomaliaDicConsistenzaVO.getIdControllo()), 
                tipoMotivoDichiarazioneVO.getAnnoCampagna(), orderByData);
            }
              
            //Controllo che sia presente un solo documento per ogni
            //DB_TIPO_DOCUMENTO.ID_TIPOLOGIA_DOCUMENTO 
            ArrayList<DocumentoVO> arrDocumenti = new ArrayList<DocumentoVO>();
            //Per mantenere l'ordinamento!!
            ArrayList<Long> arrIdTipologiaDocumento = new ArrayList<Long>();    
            for(int j=0;j<elencoDocumenti.length;j++)
            {
              Long idTipologiaDocumento = elencoDocumenti[j].getTipoDocumentoVO().getIdTipologiaDocumento();
              if(!arrIdTipologiaDocumento.contains(idTipologiaDocumento))
              {
                arrIdTipologiaDocumento.add(idTipologiaDocumento);
                arrDocumenti.add(elencoDocumenti[j]);
              }
            }
              
            elencoDocumenti = new DocumentoVO[arrDocumenti.size()];
            for(int j=0;j<arrDocumenti.size();j++)
            {
              elencoDocumenti[j] = (DocumentoVO)arrDocumenti.get(j);
            }
          }
          else
          {
            if(Validator.isNotEmpty(idStoricoParticella))
            {
              elencoDocumenti = anagFacadeClient.getListDocumentiAziendaByIdControlloAndParticella(
              anagAziendaVO, Long.decode(errAnomaliaDicConsistenzaVO.getIdControllo()), 
              Long.decode(idStoricoParticella), true, orderBy);
            }
            else
            {
     				  elencoDocumenti = anagFacadeClient.getListDocumentiAziendaByIdControllo(
                anagAziendaVO, Long.decode(errAnomaliaDicConsistenzaVO.getIdControllo()), true, orderBy);
            }
          }
   		  }
   			catch(SolmrException se) 
        {
   			  ValidationErrors errors = new ValidationErrors();
   				errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_DOCUMENTI_AZIENDA));
			    request.setAttribute("errors", errors);
			    request.getRequestDispatcher(correzioneAnomaliaUrl).forward(request, response);
				  return;
   			}
   			// Se ho trovato dei documenti li associo all'anomalia rilevata
   			if(elencoDocumenti != null && elencoDocumenti.length > 0) 
   			{
   				elencoDocumentiCorrezioni.put(Long.decode(errAnomaliaDicConsistenzaVO.getIdDichiarazioneSegnalazione()), elencoDocumenti);
   			}
   			// Altrimenti...
   			else 
   			{
				  // Segnalo all'utente quale tipologia di documenti deve andare
					// ad inserire per correggere l'anomalia rilevata dal sistema
					try 
					{
						elencoTipiDocumento = anagFacadeClient.getListTipoDocumentoByIdControllo(Long.decode(errAnomaliaDicConsistenzaVO.getIdControllo()), true, orderBy);
					}
					catch(SolmrException se) 
          {
						ValidationErrors errors = new ValidationErrors();
				    errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_TIPO_DOCUMENTO));
				    request.setAttribute("errors", errors);
				    request.getRequestDispatcher(correzioneAnomaliaUrl).forward(request, response);
					  return;
	   			}
					elencoDocumentiCorrezioni.put(Long.decode(errAnomaliaDicConsistenzaVO.getIdDichiarazioneSegnalazione()), elencoTipiDocumento);
   			}
   		}
   		request.setAttribute("elencoDocumentiCorrezioni", elencoDocumentiCorrezioni);
   	}
   	request.setAttribute("anomalie", anomalie);
 	}
 	catch(Exception e) 
  {
		SolmrLogger.error(this, "\n\n\n\n\n\n\n\n\n\n\nmsg="+e.getMessage()+"\n\n\n\n\n\n\n\n");
    ValidationErrors errors = new ValidationErrors();
    errors.add("error", new ValidationError(e.getMessage()));
    request.setAttribute("errors", errors);
		request.getRequestDispatcher(correzioneAnomaliaUrl).forward(request, response);
	  return;
 	}
 	
 	%>
		<jsp:forward page= "<%= correzioneAnomaliaUrl %>" />
	<%
%>

<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>

<jsp:useBean id="consistenzaVO" scope="request" class="it.csi.solmr.dto.anag.ConsistenzaVO"/>


<%

	SolmrLogger.debug(this, " - dichiarazioneAnomaliaView.jsp - INIZIO PAGINA");

 	java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazione_A.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	int size = 0;


  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	HashMap elencoDocumenti = (HashMap)request.getAttribute("elencoDocumenti");
 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	HtmplUtil.setValues(htmpl, anagAziendaVO);

 	if(Validator.isNotEmpty(request.getParameter("anno"))) 
 	{
   	htmpl.bset("anno",request.getParameter("anno"));
 	}
 	else 
 	{
   	Integer annoDichiarazione = (Integer)session.getAttribute("anno");
   	htmpl.bset("anno", annoDichiarazione.toString());
 	}

 	SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());
 	Vector<ErrAnomaliaDicConsistenzaVO> anomalie = (Vector<ErrAnomaliaDicConsistenzaVO>)request.getAttribute("anomalieDichiarazioniConsistenza");
 	if(anomalie == null) 
 	{
   	anomalie = (Vector<ErrAnomaliaDicConsistenzaVO>)session.getAttribute("anomalieDichiarazioniConsistenza");
 	}
 	String percorsoErrori = null;
 	if(pathToFollow.equalsIgnoreCase("rupar")) 
 	{
   	percorsoErrori = "/ris/css/agricoltura/im/";
 	}
 	else if(pathToFollow.equalsIgnoreCase("sispie")) {
  	    percorsoErrori = "/css/agricoltura/im/";
  	}
  	else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
  	    percorsoErrori="/css/agricoltura/im/";  
  	}

	if(anomalie != null) 
	{
  	size = anomalie.size();
  	int anomBlocNonRisolvibile = 0;
  	int numAnomBlocCorr = 0;
  	int numCheckBox = 0;	
  	if(size > 0) 
  	{
   		Iterator<ErrAnomaliaDicConsistenzaVO> iteraAnomalie = anomalie.iterator();
   		ErrAnomaliaDicConsistenzaVO err = null;
   		String descAnomaliaOld = "";
   		while(iteraAnomalie.hasNext()) 
   		{
     		err = (ErrAnomaliaDicConsistenzaVO)iteraAnomalie.next();
     		if(!descAnomaliaOld.equals(err.getDescGruppoControllo())) 
     		{
       		descAnomaliaOld = err.getDescGruppoControllo();
       		htmpl.newBlock("blkTabAnomalia");
       		htmpl.set("blkTabAnomalia.descrizioneAnomalia",descAnomaliaOld);
     		}
      	htmpl.newBlock("blkTabAnomalia.blkAnomalia");
     		// Controllo se è un'anomalia risolvibile
     		if(err.getFlagDocumentoGiustificativo() != null) 
     		{
       		htmpl.newBlock("blkTabAnomalia.blkAnomalia.blkCheck");
       		htmpl.set("blkTabAnomalia.blkAnomalia.blkCheck.idDichiarazioneSegnalazione",err.getIdDichiarazioneSegnalazione());
       		numCheckBox++;
       		if(err.getIdDichiarazioneCorrezione() != null 
       		  && !"".equals(err.getIdDichiarazioneCorrezione())) 
       		{
        		numAnomBlocCorr++;
       		}
     		}
      	else 
      	{
          if(err.isBloccante()) 
          {
          	anomBlocNonRisolvibile++;
        	}
        	htmpl.newBlock("blkTabAnomalia.blkAnomalia.blkNoCheck");
      	}
      	htmpl.set("blkTabAnomalia.blkAnomalia.tipologia",err.getTipoAnomaliaErrore());
      	htmpl.set("blkTabAnomalia.blkAnomalia.descrizione",err.getDescAnomaliaErrore());
      	if(err.isBloccante()) 
      	{
          // Reperisco l'immagine da ANDROMEDA
        	htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Bloccante.gif");
        	htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
      	}
      	else 
      	{
        	// Reperisco l'immagine da ANDROMEDA
        	htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Warning.gif");
        	htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
      	}
      	if(err.getIdDichiarazioneCorrezione() != null 
      	  && !"".equals(err.getIdDichiarazioneCorrezione())) 
      	{
        	htmpl.set("blkTabAnomalia.blkAnomalia.ris1",(String)SolmrConstants.get("VALORE_SI"));
      		if(elencoDocumenti != null && elencoDocumenti.size() > 0) 
      		{
      			if(elencoDocumenti.get(err.getIdDichiarazioneCorrezione()) != null) 
      			{
      				DocumentoVO documentoVO = (DocumentoVO)elencoDocumenti.get(err.getIdDichiarazioneCorrezione());
      				String descrizione = documentoVO.getTipoDocumentoVO().getDescrizione();
      				if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) 
      				{
      					descrizione += " Prot. "+StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo())+" del "+DateUtils.formatDate(documentoVO.getDataProtocollo());
      				}
      				htmpl.set("blkTabAnomalia.blkAnomalia.ris2", descrizione);
      			}
      		}
      		else 
      		{
        		htmpl.set("blkTabAnomalia.blkAnomalia.ris2",err.getRisoluzione());
      		}
      	}
    	}
    	if(numCheckBox > 0) 
    	{
      	//Devo far comparire i pulsanti di seleziona/deseleziona tutto
      	htmpl.newBlock("blkPulsantiSeleziona");
    	}
  	}
    if(numCheckBox != numAnomBlocCorr || anomBlocNonRisolvibile != 0) 
    {
    	htmpl.newBlock("blkAnomaliaBloc");
    	htmpl.set("blkAnomaliaBloc.anomaliaBloc",(String)AnagErrors.get("ERR_ANOMALIA"));
    }
    else 
    {
    	htmpl.newBlock("blkNoAnomaliaBloc");
    	if(numCheckBox > 0) 
    	{
    		htmpl.newBlock("blkNoAnomaliaBloc.blkMess");
      }
      if(consistenzaVO.isPossibileProtocollo()) 
      {
      	//do la possibilita di protocollare
      	htmpl.newBlock("blkNoAnomaliaBloc.blkDichiarazioneProtocollo");
      	if(SolmrConstants.FLAG_S.equals(consistenzaVO.getFlagProtocollo()))
      	{
      		htmpl.set("blkNoAnomaliaBloc.blkDichiarazioneProtocollo.checkedSi","checked");
      	}
        else
        { 
        	htmpl.set("blkNoAnomaliaBloc.blkDichiarazioneProtocollo.checkedNo","checked");
        }
      }
      if(consistenzaVO.getNote() != null)
      {
      	htmpl.bset("note",consistenzaVO.getNote());
      }
      
      htmpl.newBlock("blkNoAnomaliaBloc.blkMail");
      
      //NN esiste nessuna mail perl'invio
      if(Validator.isEmpty(anagAziendaVO.getMail()) && Validator.isEmpty(anagAziendaVO.getPec()))
      {
        htmpl.set("blkNoAnomaliaBloc.blkMail.disabled", "disabled=\"disabled\"", null);
      }
      else
      {
        if(request.getAttribute("trovataRichiesta") != null)
        {
          htmpl.set("blkNoAnomaliaBloc.blkMail.disabled", "disabled=\"disabled\"", null);
          htmpl.set("blkNoAnomaliaBloc.blkMail.checkedSi", "checked=\"checked\"", null);
        }
	      else
	      {
	        if(Validator.isNotEmpty(request.getParameter("flagInvioMail"))) 
				  {
				    if("N".equalsIgnoreCase(request.getParameter("flagInvioMail")))
				    {
				      htmpl.set("blkNoAnomaliaBloc.blkMail.checkedNo", "checked=\"checked\"", null);
				    }
				    else
				    {
				      htmpl.set("blkNoAnomaliaBloc.blkMail.checkedSi", "checked=\"checked\"", null);
				    }			    
				  }
				  else
				  {
				    htmpl.set("blkNoAnomaliaBloc.blkMail.checkedNo", "checked=\"checked\"", null);
				  }
	      }
	    } 
    }
  }

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);
  		
  SolmrLogger.debug(this, " - dichiarazioneAnomaliaView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>


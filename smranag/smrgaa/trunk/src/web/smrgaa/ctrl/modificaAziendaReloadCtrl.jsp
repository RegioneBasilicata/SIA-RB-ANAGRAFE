<%@ page language="java" contentType="text/html" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%

	String iridePageName = "modificaAziendaReloadCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  String url = "/view/modificaAziendaView.jsp";
  AnagFacadeClient client = new AnagFacadeClient();

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  ValidationErrors errors = new ValidationErrors();
   	
  if(anagAziendaVO != null) 
  {
    //Setto la dimensione azienda col valore impostato nell'interfaccia 
    String idDimensioneAzienda = request.getParameter("idDimensioneAzienda");
    if(Validator.isNotEmpty(idDimensioneAzienda))
    {
      anagAziendaVO.setIdDimensioneAzienda(new Long(idDimensioneAzienda));
    }
    
	  // Se l'azienda selezionata ha flag_provvisoria = NULL o = N e id_azienda_provenienza
	  // not null
		if(anagAziendaVO.getIdAziendaProvenienza() != null) 
    {
			AnagAziendaVO anagAziendaProvenienzaVO = null;
			try 
      {
				anagAziendaProvenienzaVO = client.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaProvenienza());
				request.setAttribute("anagAziendaProvenienzaVO", anagAziendaProvenienzaVO);
			}
			catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_PROVENIENZA);
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
		}
		// Recupero le eventuali aziende di destinazione dell'azienda in esame
		AnagAziendaVO[] elencoAziendeDestinazione = null;
		try 
    {
			elencoAziendeDestinazione = client.getListAnagAziendaDestinazioneByIdAzienda(anagAziendaVO.getIdAzienda(), true, null);
			request.setAttribute("elencoAziendeDestinazione", elencoAziendeDestinazione);
		}
		catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_DESTINAZIONE);
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
	}
   	
  AnagAziendaVO voAnagModifica = new AnagAziendaVO();
  try 
  {
    voAnagModifica = client.getAziendaById(anagAziendaVO.getIdAnagAzienda());
    session.removeAttribute("voAnagModifica");
  }
  catch(SolmrException sex) 
  {
    errors = new ValidationErrors();
    ValidationError error = new ValidationError(sex.getMessage());
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(url).forward(request, response);
    return;
  }

  String provinciaCompetenza = request.getParameter("provincePiemonte");

  String cuaa = request.getParameter("cuaa");
  voAnagModifica.setCUAA(cuaa);

  voAnagModifica.setDenominazione(request.getParameter("denominazione"));
  voAnagModifica.setIntestazionePartitaIva(request.getParameter("intestazionePartitaIva"));
  voAnagModifica.setPartitaIVA(request.getParameter("partitaIVA"));
  voAnagModifica.setProvCompetenza(provinciaCompetenza);
  voAnagModifica.setProvincePiemonte(provinciaCompetenza);

  Integer idTipoAzienda = null;
  if(Validator.isNotEmpty(request.getParameter("tipiAzienda"))) 
  {
    idTipoAzienda = Integer.decode(request.getParameter("tipiAzienda"));
  }
  if(idTipoAzienda != null) 
  {
    voAnagModifica.setTipiAzienda(String.valueOf(idTipoAzienda));
    String tipiAzienda = client.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_AZIENDA, idTipoAzienda);
    voAnagModifica.setTipoTipologiaAzienda(new CodeDescription(idTipoAzienda,tipiAzienda));
  }
  else 
  {
    voAnagModifica.setTipiAzienda(null);
    voAnagModifica.setTipoTipologiaAzienda(null);
  }
  voAnagModifica.setTipoFormaGiuridica(null);

  if(request.getParameter("CCIAAprovREA") != null) 
  {
    voAnagModifica.setCCIAAprovREA(request.getParameter("CCIAAprovREA").toUpperCase());
  }
  else 
  {
    voAnagModifica.setCCIAAprovREA("");
  }
  voAnagModifica.setStrCCIAAnumeroREA(request.getParameter("strCCIAAnumeroREA"));

  if(request.getParameter("CCIAAnumRegImprese") != null) 
  {
    voAnagModifica.setCCIAAnumRegImprese(request.getParameter("CCIAAnumRegImprese"));
  }
  if(request.getParameter("CCIAAannoIscrizione") != null) 
  {
    voAnagModifica.setCCIAAannoIscrizione(request.getParameter("CCIAAannoIscrizione"));
  }

  voAnagModifica.setNote(request.getParameter("note"));

  voAnagModifica.setIdIntermediarioDelegato(null);

  session.setAttribute("voAnagModifica", voAnagModifica);




  %>
    <jsp:forward page="<%= url %>"/>





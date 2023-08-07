<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  Enumeration en = session.getAttributeNames();
  java.io.InputStream layout = application.getResourceAsStream("/layout/contitolari_new.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  PersonaFisicaVO nuovaPersonaFisicaVO = (PersonaFisicaVO)session.getAttribute("nuovaPersonaFisicaVO");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  if(errors == null) {
    if(nuovaPersonaFisicaVO != null) {
      if(nuovaPersonaFisicaVO.isNewPersonaFisica()){
        Date dataNascita = DateUtils.getDataNascitaFromCF(nuovaPersonaFisicaVO.getCodiceFiscale());
        String sesso = StringUtils.getSessoFromCF(nuovaPersonaFisicaVO.getCodiceFiscale());
        ComuneVO comuneVO = null;
        try {
          comuneVO = anagFacadeClient.getComuneByCUAA(nuovaPersonaFisicaVO.getCodiceFiscale().substring(11,15).toUpperCase());
        }
        catch(Exception e) {
        }
        nuovaPersonaFisicaVO.setNascitaData(dataNascita);
        nuovaPersonaFisicaVO.setSesso(sesso);
        if(comuneVO != null) {
          if(comuneVO.getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
            nuovaPersonaFisicaVO.setDescNascitaComune(comuneVO.getDescom());
            nuovaPersonaFisicaVO.setNascitaProv(comuneVO.getSiglaProv());
          }
          else {
            nuovaPersonaFisicaVO.setNascitaStatoEstero(comuneVO.getDescom());
          }
        }
      }

      if(nuovaPersonaFisicaVO.getNascitaData() != null) {
        htmpl.set("strNascitaData",DateUtils.formatDate(nuovaPersonaFisicaVO.getNascitaData()));
      }
      else if(nuovaPersonaFisicaVO.getStrNascitaData() != null) {
        htmpl.set("strNascitaData",nuovaPersonaFisicaVO.getStrNascitaData());
      }
      if(nuovaPersonaFisicaVO.getSesso() != null) {
        if(nuovaPersonaFisicaVO.getSesso().equalsIgnoreCase("M")) {
          htmpl.set("checkedM","checked");
        }
        else {
          htmpl.set("checkedF","checked");
        }
      }
      HtmplUtil.setValues(htmpl, nuovaPersonaFisicaVO);
    }
    HtmplUtil.setValues(htmpl, request);
  }
  else {
    String _sesso_ = (String)session.getAttribute("_sesso_");
    session.removeAttribute("_sesso_");
    String statoEsteroResidenza = request.getParameter("statoEsteroRes");
    String cittaEsteroResidenza = request.getParameter("cittaResidenza");
    if(_sesso_ != null) {
      if(_sesso_.equalsIgnoreCase("M")) {
        htmpl.set("checkedM","checked");
      }
      else {
        htmpl.set("checkedF","checked");
      }
    }
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }
  // Informazioni sul telefono cellulare	
  /*Vector elencoPrefissiCellulare = null;
  Long idPrefixCellulare = (Long)session.getAttribute("idPrefixCellulare");
	
	try{
	elencoPrefissiCellulare = anagFacadeClient.getPrefissiCellulare();
	}catch(SolmrException se){
		//@FIX Eccezione non trattata
	}
	  if(elencoPrefissiCellulare != null) {
	    Iterator iteraPrefissiCellulare = elencoPrefissiCellulare.iterator();
	    while(iteraPrefissiCellulare.hasNext()) {
	      htmpl.newBlock("elencoPrefissiCellulare");
	      CodeDescription code = (CodeDescription)iteraPrefissiCellulare.next();
	      if(nuovaPersonaFisicaVO != null){
	 		if(nuovaPersonaFisicaVO.getIdPrefissoCellulareNaz() != null) {
	          if(nuovaPersonaFisicaVO.getIdPrefissoCellulareNaz().compareTo(Long.decode(code.getCode().toString())) == 0){
	            htmpl.set("elencoPrefissiCellulare.check", "selected");
	         }
            }
         }
	      htmpl.set("elencoPrefissiCellulare.idCodice", code.getCode().toString());
	      htmpl.set("elencoPrefissiCellulare.descrizione", code.getDescription());
	    }
	}*/
	if(nuovaPersonaFisicaVO != null){
	if(nuovaPersonaFisicaVO.getdesNumeroCellulare() != null)
	    	htmpl.set("CellulareNumero", nuovaPersonaFisicaVO.getdesNumeroCellulare());
	}
	
  // Informazioni relative al titolo di studio
  Vector elencoTitoliStudio = null;
  Long idTitoloStudio = (Long)session.getAttribute("idTitoloStudio");
  try {
    elencoTitoliStudio = anagFacadeClient.getTitoliStudio();
  }
  catch(SolmrException se) {}

  if(elencoTitoliStudio != null) {
    Iterator iteraTitoliStudio = elencoTitoliStudio.iterator();
    while(iteraTitoliStudio.hasNext()) {
      htmpl.newBlock("elencoTitoliStudio");
      CodeDescription code = (CodeDescription)iteraTitoliStudio.next();
      if(nuovaPersonaFisicaVO != null) {
        if(nuovaPersonaFisicaVO.getIdTitoloStudio() != null) {
          if(nuovaPersonaFisicaVO.getIdTitoloStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
            htmpl.set("elencoTitoliStudio.check","selected");
          }
        }
      }
      htmpl.set("elencoTitoliStudio.idCodice", code.getCode().toString());
      htmpl.set("elencoTitoliStudio.descrizione", code.getDescription());
    }
    if(nuovaPersonaFisicaVO != null) {
      if(nuovaPersonaFisicaVO.getIdTitoloStudio() != null) {
        Vector elencoIndirizziStudio = null;
        try {
          elencoIndirizziStudio = anagFacadeClient.getIndirizzoStudioByTitolo(nuovaPersonaFisicaVO.getIdTitoloStudio());
        }
        catch(SolmrException se) {
        }
        if(elencoIndirizziStudio != null) {
          Iterator iteraIndirizziStudio = elencoIndirizziStudio.iterator();
          while(iteraIndirizziStudio.hasNext()) {
            htmpl.newBlock("elencoIndirizziStudio");
            CodeDescription codeIndirizzo = (CodeDescription)iteraIndirizziStudio.next();
            if(nuovaPersonaFisicaVO.getIdIndirizzoStudio() != null) {
              if(nuovaPersonaFisicaVO.getIdIndirizzoStudio().compareTo(Long.decode(codeIndirizzo.getCode().toString())) == 0) {
                htmpl.set("elencoIndirizziStudio.check","selected");
              }
            }
            htmpl.set("elencoIndirizziStudio.idCodice",codeIndirizzo.getCode().toString());
            htmpl.set("elencoIndirizziStudio.descrizione",codeIndirizzo.getDescription());
          }
        }
      }
    }
  }
%>
<%= htmpl.text()%>

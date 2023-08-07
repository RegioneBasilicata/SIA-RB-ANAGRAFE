<%@ page language="java" contentType="text/html" isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%

  //AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/conti_correnti_mod.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String regime = request.getParameter("regimeModificaCC");
  ContoCorrenteVO contoCorrenteVO = (ContoCorrenteVO)request.getAttribute("contoCorrenteVO");

  ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");

  //Prima volta
  String motivoRivalidazione = "";
  if(Validator.isEmpty(regime))
  {
    htmpl.set("cctrl",contoCorrenteVO.getCifraCtrl());
    htmpl.set("cin",contoCorrenteVO.getCin());
    htmpl.set("intestatario", contoCorrenteVO.getIntestazione());
    htmpl.set("iban", contoCorrenteVO.getIban());
    if(Validator.isNotEmpty(contoCorrenteVO.getMotivoRivalidazione()))
    {
      motivoRivalidazione = contoCorrenteVO.getMotivoRivalidazione();
    }
  }
  else
  {
    htmpl.set("cctrl", request.getParameter("cctrl"));
    htmpl.set("cin", request.getParameter("cin"));
    htmpl.set("intestatario", request.getParameter("intestatario"));
    htmpl.set("iban", request.getParameter("iban"));    
    motivoRivalidazione = request.getParameter("motivoValidazione");
  }
  
  htmpl.set("motivoValidazione", motivoRivalidazione);
  
  
  
  
  String obbligoGF = (String)request.getAttribute("obbligoGF");
  if("S".equalsIgnoreCase(obbligoGF))
  {
    htmpl.newBlock("blkCCSpeciale");
    
    String flagContoGf = request.getParameter("flagContoGf");
    if(Validator.isEmpty(regime))
    {
      if(Validator.isNotEmpty(contoCorrenteVO.getFlagContoGf())
        && "S".equalsIgnoreCase(contoCorrenteVO.getFlagContoGf()))
      {
        htmpl.set("blkCCSpeciale.checkedFlagContoGf", "checked=\"true\"", null);
      }
    }
    else
    {
      if("S".equalsIgnoreCase(flagContoGf))
      {
        htmpl.set("blkCCSpeciale.checkedFlagContoGf", "checked=\"true\"", null);
      }
    }
  }
  
  
  
  
  
  htmpl.set("idContoCorrente", ""+contoCorrenteVO.getIdContoCorrente());
  htmpl.set("abi", contoCorrenteVO.getAbi());  
  htmpl.set("denominazioneBanca", contoCorrenteVO.getDenominazioneBanca());
  htmpl.set("bic", contoCorrenteVO.getBic());
  htmpl.set("cab", contoCorrenteVO.getCab());
  htmpl.set("indirizzoSportello", contoCorrenteVO.getIndirizzoSportello());
  htmpl.set("descrizioneComuneSportello", contoCorrenteVO.getDescrizioneComuneSportello());
  htmpl.set("capSportello", contoCorrenteVO.getCapSportello());    
	htmpl.set("codpaese", contoCorrenteVO.getCodPaese());
  htmpl.set("numeroContoCorrente", contoCorrenteVO.getNumeroContoCorrente());  

  
  if (contoCorrenteVO.getDataInizioValiditaContoCorrente()!=null)
  {
    htmpl.set("dataInizioValiditaContoCorrente",
      DateUtils.formatDate(contoCorrenteVO.getDataInizioValiditaContoCorrente()));
  }
  
  if (request.getParameter("estinto")!=null)
  {
    htmpl.set("checkedEstinto","checked=checked");
  }
  
  
  //Invalidazione
  String validazione = "";
  if(Validator.isNotEmpty(contoCorrenteVO.getflagValidato()) 
    && contoCorrenteVO.getflagValidato().equalsIgnoreCase("S"))
  {
    validazione = "S"; 
  }
  else if(Validator.isNotEmpty(contoCorrenteVO.getflagValidato()) 
    && contoCorrenteVO.getflagValidato().equalsIgnoreCase("N"))
  {
    validazione = "N";              
  }
  htmpl.set("flagValidazione",validazione);
  htmpl.set("causale",contoCorrenteVO.getDescrizioneCausaInvalidazione());
  htmpl.set("note",contoCorrenteVO.getNote());
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
  
  

%>


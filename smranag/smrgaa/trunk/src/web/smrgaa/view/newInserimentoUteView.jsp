<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="java.text.*"%>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoUte.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  
  if(request.getAttribute("messaggio") != null)
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", (String)request.getAttribute("messaggio"));
  }
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
  
  Vector<ValidationErrors> elencoErrori = (Vector<ValidationErrors>)request.getAttribute("elencoErrori"); 
  
  Vector<ProvinciaVO> vProvince =  (Vector<ProvinciaVO>)request.getAttribute("vProvince");
  HashMap<String, Vector<ComuneVO>> hProvCom = (HashMap<String, Vector<ComuneVO>>)request.getAttribute("hProvCom"); 
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = (Vector<UteAziendaNuovaVO>)session.getAttribute("vUteAziendaNuova");
  ValidationError errorElim = (ValidationError)request.getAttribute("errorElim");
  
  
  if(vUteAziendaNuova != null)
  {
  
    for(int i=0;i<vUteAziendaNuova.size();i++)
    {
      htmpl.newBlock("blkElencoUnitaProduttive");
      if(Validator.isNotEmpty(errorElim)
        && (i == ((Integer)request.getAttribute("idRigaElim")).intValue()) )
      {
        htmpl.set("blkElencoUnitaProduttive.err_elimina",
                      MessageFormat.format(htmlStringKO,
                      new Object[] {
                      pathErrori + "/"+ imko,
                      "'"+jssp.process(errorElim.getMessage())+"'",
                      errorElim.getMessage()}),
                      null);      
      }
      
      UteAziendaNuovaVO uteAziendaNuovaVO = vUteAziendaNuova.get(i);
      htmpl.set("blkElencoUnitaProduttive.idRiga", ""+i);
      if(Validator.isNotEmpty(uteAziendaNuovaVO.getDenominazione()))
        htmpl.set("blkElencoUnitaProduttive.denominazione", uteAziendaNuovaVO.getDenominazione().toUpperCase());
      if(Validator.isNotEmpty(uteAziendaNuovaVO.getIndirizzo()))
        htmpl.set("blkElencoUnitaProduttive.indirizzo", uteAziendaNuovaVO.getIndirizzo().toUpperCase());
		  if(vProvince!=null)
		  {
		    for(int j=0;j<vProvince.size();j++)
		    {
		      ProvinciaVO provinciaVO = vProvince.get(j);
		      htmpl.newBlock("blkElencoProvince");
		      htmpl.set("blkElencoUnitaProduttive.blkElencoProvince.istatProvincia", provinciaVO.getIstatProvincia());
		      htmpl.set("blkElencoUnitaProduttive.blkElencoProvince.sglProv", provinciaVO.getSiglaProvincia());
		      if(Validator.isNotEmpty(uteAziendaNuovaVO.getIstatProvincia()) 
		        && uteAziendaNuovaVO.getIstatProvincia().equals(provinciaVO.getIstatProvincia()))
		      {
		        htmpl.set("blkElencoUnitaProduttive.blkElencoProvince.selected","selected", null);
		      }
		    }
		  }
		  
		  
		  String capTmp = "";
		  if(Validator.isNotEmpty(uteAziendaNuovaVO.getIstatProvincia()))
		  {
		    Vector<ComuneVO> vComune = hProvCom.get(uteAziendaNuovaVO.getIstatProvincia());
		    for(int j=0;j<vComune.size();j++)
        {
          ComuneVO comuneVO = vComune.get(j);
          htmpl.newBlock("blkElencoComuni");
          htmpl.set("blkElencoUnitaProduttive.blkElencoComuni.istatComune", comuneVO.getIstatComune());
          htmpl.set("blkElencoUnitaProduttive.blkElencoComuni.desCom", comuneVO.getDescom());
          if(Validator.isNotEmpty(uteAziendaNuovaVO.getIstatComune()) 
            && uteAziendaNuovaVO.getIstatComune().equals(comuneVO.getIstatComune()))
          {
            htmpl.set("blkElencoUnitaProduttive.blkElencoComuni.selected","selected", null);
            capTmp = comuneVO.getCap();
          }
        }
		  
		  }
		  
		  if(Validator.isNotEmpty(uteAziendaNuovaVO.getCap()))
		    capTmp = uteAziendaNuovaVO.getCap();
		  if(Validator.isNotEmpty(capTmp))  
        htmpl.set("blkElencoUnitaProduttive.cap", capTmp);
      if(Validator.isNotEmpty(uteAziendaNuovaVO.getTelefono()))
        htmpl.set("blkElencoUnitaProduttive.telefono", uteAziendaNuovaVO.getTelefono());
      if(Validator.isNotEmpty(uteAziendaNuovaVO.getFax()))
        htmpl.set("blkElencoUnitaProduttive.fax", uteAziendaNuovaVO.getFax());
      if(Validator.isNotEmpty(uteAziendaNuovaVO.getNote()))
        htmpl.set("blkElencoUnitaProduttive.note", uteAziendaNuovaVO.getNote());
        
        
        
      if(elencoErrori != null && elencoErrori.size() > 0) 
		  {
		    ValidationErrors errors = (ValidationErrors)elencoErrori.elementAt(i);
		    if(errors != null && errors.size  () > 0) 
		    {
		      Iterator iter = htmpl.getVariableIterator();
		      while(iter.hasNext()) 
		      {
		        String key = (String)iter.next();
		        if(key.startsWith("err_")) 
		        {
		          String property = key.substring(4);
		          Iterator errorIterator = errors.get(property);
		          if(errorIterator != null) 
		          {
		            ValidationError error = (ValidationError)errorIterator.next();
		            htmpl.set("blkElencoUnitaProduttive.err_"+property,
		                  MessageFormat.format(htmlStringKO,
		                  new Object[] {
		                  pathErrori + "/"+ imko,
		                  "'"+jssp.process(error.getMessage())+"'",
		                  error.getMessage()}),
		                  null);
		          }
		        }
		      }
		    }
		  }
    }
  
  }
  
  
  
  
  
  
  
  
  
  
	
	


      
  

%>
<%= htmpl.text() %>

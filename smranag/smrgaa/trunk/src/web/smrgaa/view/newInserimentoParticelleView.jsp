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
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoParticelle.htm");
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
  
  Vector<ValidationErrors> elencoErrori = (Vector<ValidationErrors>)request.getAttribute("elencoErrori"); 
  
  Vector<ProvinciaVO> vProvince =  (Vector<ProvinciaVO>)request.getAttribute("vProvince");
  HashMap<String, Vector<ComuneVO>> hProvCom = (HashMap<String, Vector<ComuneVO>>)request.getAttribute("hProvCom"); 
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = (Vector<UteAziendaNuovaVO>)request.getAttribute("vUteAziendaNuova");
  Vector<CodeDescription> vIndirizzoUtilizzo = (Vector<CodeDescription>)request.getAttribute("vIndirizzoUtilizzo");
  HashMap<Long, Vector<CodeDescription>> hIndUtil = (HashMap<Long, Vector<CodeDescription>>)request.getAttribute("hIndUtil");
  HashMap<Long, TipoVarietaVO[]> hUtilVar = (HashMap<Long, TipoVarietaVO[]>)request.getAttribute("hUtilVar");
  CodeDescription[] elencoTipoTitoloPossesso = (CodeDescription[])request.getAttribute("elencoTipoTitoloPossesso");
  Vector<UnitaMisuraVO> vUnitaMisura = (Vector<UnitaMisuraVO>)request.getAttribute("vUnitaMisura");
  Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova = (Vector<ParticellaAziendaNuovaVO>)session.getAttribute("vParticelleAziendaNuova");   
  
  if(vParticelleAziendaNuova != null)
  {
  
    for(int i=0;i<vParticelleAziendaNuova.size();i++)
    {
      htmpl.newBlock("blkElencoParticelle");
      ParticellaAziendaNuovaVO particellaAziendaNuovaVO = vParticelleAziendaNuova.get(i);
      htmpl.set("blkElencoParticelle.idRiga", ""+i);
	    
	    if(vProvince!=null)
      {
        for(int j=0;j<vProvince.size();j++)
        {
          ProvinciaVO provinciaVO = vProvince.get(j);
          htmpl.newBlock("blkElencoParticelle.blkElencoProvince");
          htmpl.set("blkElencoParticelle.blkElencoProvince.istatProvincia", provinciaVO.getIstatProvincia());
          htmpl.set("blkElencoParticelle.blkElencoProvince.sglProv", provinciaVO.getSiglaProvincia());
          if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIstatProvincia()) 
            && particellaAziendaNuovaVO.getIstatProvincia().equals(provinciaVO.getIstatProvincia()))
          {
            htmpl.set("blkElencoParticelle.blkElencoProvince.selected","selected", null);
          }
        }
      }
      
      
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIstatProvincia()))
      {
        Vector<ComuneVO> vComune = hProvCom.get(particellaAziendaNuovaVO.getIstatProvincia());
        for(int j=0;j<vComune.size();j++)
        {
          ComuneVO comuneVO = vComune.get(j);
          htmpl.newBlock("blkElencoParticelle.blkElencoComuni");
          htmpl.set("blkElencoParticelle.blkElencoComuni.istatComune", comuneVO.getIstatComune());
          htmpl.set("blkElencoParticelle.blkElencoComuni.desCom", comuneVO.getDescom());
          if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIstatComune()) 
            && particellaAziendaNuovaVO.getIstatComune().equals(comuneVO.getIstatComune()))
          {
            htmpl.set("blkElencoParticelle.blkElencoComuni.selected","selected", null);
          }
        }
      
      }      
      
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getSezione()))
        htmpl.set("blkElencoParticelle.sezione", particellaAziendaNuovaVO.getSezione());
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getStrFoglio()))
        htmpl.set("blkElencoParticelle.foglio", particellaAziendaNuovaVO.getStrFoglio());
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getStrParticella()))
        htmpl.set("blkElencoParticelle.particella", particellaAziendaNuovaVO.getStrParticella());
		  
		 
		 if(elencoTipoTitoloPossesso !=null)
      {
        for(int j=0;j<elencoTipoTitoloPossesso.length;j++)
        {
          CodeDescription cd = elencoTipoTitoloPossesso[j];
          htmpl.newBlock("blkElencoParticelle.blkElencoTitoloPossesso");
          htmpl.set("blkElencoParticelle.blkElencoTitoloPossesso.idTitoloPossesso", ""+cd.getCode());
          htmpl.set("blkElencoParticelle.blkElencoTitoloPossesso.descTitoloPossesso", cd.getDescription());
          if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdTitoloPossesso()) 
            && (particellaAziendaNuovaVO.getIdTitoloPossesso().compareTo(cd.getCode()) == 0))
          {
            htmpl.set("blkElencoParticelle.blkElencoTitoloPossesso.selected","selected", null);
          }
        }      
      } 
		  
		  if(Validator.isNotEmpty(particellaAziendaNuovaVO.getStrPercentualePossesso()))
        htmpl.set("blkElencoParticelle.percentualePossesso", particellaAziendaNuovaVO.getStrPercentualePossesso());
		  
      
      if(vIndirizzoUtilizzo !=null)
      {
        for(int j=0;j<vIndirizzoUtilizzo.size();j++)
        {
          CodeDescription cd = vIndirizzoUtilizzo.get(j);
          htmpl.newBlock("blkElencoParticelle.blkElencoIndirizzoUtilizzo");
          htmpl.set("blkElencoParticelle.blkElencoIndirizzoUtilizzo.idIndirizzoUtilizzo", ""+cd.getCode());
          htmpl.set("blkElencoParticelle.blkElencoIndirizzoUtilizzo.descIndirizzoUtilizzo", cd.getDescription());
          if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo()) 
            && particellaAziendaNuovaVO.getIdIndirizzoUtilizzo().intValue() == cd.getCode().intValue())
          {
            htmpl.set("blkElencoParticelle.blkElencoIndirizzoUtilizzo.selected","selected", null);
          }
        }
      }
      
      boolean trovatoUtilizzo = false;
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo()))
      {
	      Vector<CodeDescription> vTipiUtilizzo = hIndUtil.get(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo());	      
	    
		    for(int j=0;j<vTipiUtilizzo.size();j++)
	      {
	        CodeDescription cd = vTipiUtilizzo.get(j);
	        htmpl.newBlock("blkElencoParticelle.blkElencoUtilizzi");
	        htmpl.set("blkElencoParticelle.blkElencoUtilizzi.idUtilizzo", ""+cd.getCode());
	        htmpl.set("blkElencoParticelle.blkElencoUtilizzi.descUtilizzo", 
	         "["+cd.getSecondaryCode()+"] "+cd.getDescription());
	        if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUtilizzo()) 
	          && particellaAziendaNuovaVO.getIdUtilizzo().intValue() == cd.getCode().intValue())
	        {
	          trovatoUtilizzo = true;
	          htmpl.set("blkElencoParticelle.blkElencoUtilizzi.selected","selected", null);
	        }
	      }
	    }
      
      
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUtilizzo()) && trovatoUtilizzo)
      {
        TipoVarietaVO[] aTipoVarieta = hUtilVar.get(particellaAziendaNuovaVO.getIdUtilizzo());
        for(int j=0;j<aTipoVarieta.length;j++)
        {
          TipoVarietaVO tipoVarietaVO = aTipoVarieta[j];
          htmpl.newBlock("blkElencoVarieta");
          htmpl.set("blkElencoParticelle.blkElencoVarieta.idVarieta", ""+tipoVarietaVO.getIdVarieta());
          htmpl.set("blkElencoParticelle.blkElencoVarieta.descVarieta", 
            "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
          if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdVarieta()) 
            && (particellaAziendaNuovaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0)
            || (aTipoVarieta.length == 1))
          {
            htmpl.set("blkElencoParticelle.blkElencoVarieta.selected","selected", null);
          }
        }      
      }
      
      if(vUnitaMisura !=null)
      {
        for(int j=0;j<vUnitaMisura.size();j++)
        {
          UnitaMisuraVO unitaMisuraVO = vUnitaMisura.get(j);
          htmpl.newBlock("blkElencoParticelle.blkElencoUnitaMisura");
          htmpl.set("blkElencoParticelle.blkElencoUnitaMisura.idUnitaMisura", ""+unitaMisuraVO.getIdUnitaMisura());
          htmpl.set("blkElencoParticelle.blkElencoUnitaMisura.descUnitaMisura", unitaMisuraVO.getDescrizione());
          if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUnitaMisura()) 
            && particellaAziendaNuovaVO.getIdUnitaMisura().intValue() == unitaMisuraVO.getIdUnitaMisura().intValue())
          {
            htmpl.set("blkElencoParticelle.blkElencoUnitaMisura.selected","selected", null);
          }
        }
      }
      
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getStrSuperficie()))
        htmpl.set("blkElencoParticelle.superficie", particellaAziendaNuovaVO.getStrSuperficie());
     
     
      for(int j=0;j<vUteAziendaNuova.size();j++)
      {
        htmpl.newBlock("blkElencoParticelle.blkElencoUte");
        htmpl.set("blkElencoParticelle.blkElencoUte.idUteAziendaNuova", ""+vUteAziendaNuova.get(j).getIdUteAziendaNuova());
        String descUte = "";
        if(Validator.isNotEmpty(vUteAziendaNuova.get(j).getDenominazione()))
        {
          descUte += vUteAziendaNuova.get(j).getDenominazione() + " - ";
        }
        descUte += vUteAziendaNuova.get(j).getDesCom()+" ("+vUteAziendaNuova.get(j).getSglProvincia()+")";
        htmpl.set("blkElencoParticelle.blkElencoUte.descUte", descUte);
        if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUteAziendaNuova()) 
          && (particellaAziendaNuovaVO.getIdUteAziendaNuova().compareTo(vUteAziendaNuova.get(j).getIdUteAziendaNuova()) ==0)
          || (vUteAziendaNuova.size() == 1))
        {
          htmpl.set("blkElencoParticelle.blkElencoUte.selected","selected", null);
        }   
      }   
        
        
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
		            htmpl.set("blkElencoParticelle.err_"+property,
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

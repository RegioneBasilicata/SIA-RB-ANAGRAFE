<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO" %>

<%
  
  java.io.InputStream layout = application.getResourceAsStream("/layout/terreniImportaAsservimentoOk.htm");
  
  Htmpl htmpl = new Htmpl(layout);
  
  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  //Gli id sono idParticella se la ricerca è stata fatta per particellare
  // idConduzioneDichiarata se fatta col Cuaa
  String idParticelleNonImportate[]=(String[])request.getAttribute("idParticelleNonImportate");
  String idParticelleImportate[]=(String[])request.getAttribute("idParticelleImportate");
  String tipoImport = (String)request.getAttribute("tipoImport");
  
  
  if (idParticelleImportate!=null)
  {
    if (idParticelleImportate.length>0)
    {
    
      Vector elencoParticelleImportate = new Vector();
      
      for(int i = 0; i < idParticelleImportate.length; i++) 
      {
        try 
        {
          
          StoricoParticellaVO stVO = null;
          Long id = null;
          if(tipoImport.equalsIgnoreCase(SolmrConstants.IMPORTA_ASSERVIMENTO_CUAA))
          {
            Long idConduzioneDichiarata = new Long(idParticelleImportate[i]);
            ConduzioneDichiarataVO condDichVO = anagFacadeClient.findConduzioneDichiarataByPrimaryKey(idConduzioneDichiarata);
            id = condDichVO.getIdParticella();            
          }
          else
          {
            id = new Long(idParticelleImportate[i]);
          }
          stVO = anagFacadeClient.findCurrStoricoParticellaByIdParticella(id);
          elencoParticelleImportate.add(stVO);
        }
        catch(Exception se) 
        {
          se.toString();
        }
      }
      if(elencoParticelleImportate != null && elencoParticelleImportate.size() > 0) 
      {
        Iterator iteraParticelle = elencoParticelleImportate.iterator();
        htmpl.newBlock("blkParticelleImportate");
        while(iteraParticelle.hasNext()) 
        {
          StoricoParticellaVO stVO = (StoricoParticellaVO)iteraParticelle.next();
          htmpl.newBlock("blkParticelleImportate.blkElencoParticelleImportate");
          htmpl.set("blkParticelleImportate.blkElencoParticelleImportate.descComuneParticella", stVO.getComuneParticellaVO().getDescom());
          htmpl.set("blkParticelleImportate.blkElencoParticelleImportate.siglaProvinciaParticella", 
            stVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
          htmpl.set("blkParticelleImportate.blkElencoParticelleImportate.sezione", stVO.getSezione());
          htmpl.set("blkParticelleImportate.blkElencoParticelleImportate.foglio", stVO.getFoglio().toString());
          if(Validator.isNotEmpty(stVO.getParticella())) {
           htmpl.set("blkParticelleImportate.blkElencoParticelleImportate.particella", stVO.getParticella().toString());
          }
          if(Validator.isNotEmpty(stVO.getSubalterno())) {
           htmpl.set("blkParticelleImportate.blkElencoParticelleImportate.subalterno", stVO.getSubalterno());
          }
          htmpl.set("blkParticelleImportate.blkElencoParticelleImportate.supCatastale", stVO.getSupCatastale());
        }
      }
    
    }
  }
  
  
  
  if (idParticelleNonImportate!=null)
  {
    if (idParticelleNonImportate.length>0)
    {
    
      Vector elencoParticelleNonImportate = new Vector();
      
      for(int i = 0; i < idParticelleNonImportate.length; i++) 
      {
        try 
        {
          
          StoricoParticellaVO stVO = null;
          Long id = null;
          if(tipoImport.equalsIgnoreCase(SolmrConstants.IMPORTA_ASSERVIMENTO_CUAA))
          {
            Long idConduzioneDichiarata = new Long(idParticelleNonImportate[i]);
            ConduzioneDichiarataVO condDichVO = anagFacadeClient.findConduzioneDichiarataByPrimaryKey(idConduzioneDichiarata);
            id = condDichVO.getIdParticella();            
          }
          else
          {
            id = new Long(idParticelleNonImportate[i]);
          }
          stVO = anagFacadeClient.findCurrStoricoParticellaByIdParticella(id);
          elencoParticelleNonImportate.add(stVO);
        }
        catch(Exception se) 
        {
          se.toString();
        }
      }
      if(elencoParticelleNonImportate != null && elencoParticelleNonImportate.size() > 0) 
      {
        Iterator iteraParticelle = elencoParticelleNonImportate.iterator();
        htmpl.newBlock("blkParticelleNonImportate");
        while(iteraParticelle.hasNext()) 
        {
          StoricoParticellaVO stVO = (StoricoParticellaVO)iteraParticelle.next();
          htmpl.newBlock("blkParticelleNonImportate.blkElencoParticelleNonImportate");
          htmpl.set("blkParticelleNonImportate.blkElencoParticelleNonImportate.descComuneParticella", stVO.getComuneParticellaVO().getDescom());
          htmpl.set("blkParticelleNonImportate.blkElencoParticelleNonImportate.siglaProvinciaParticella", 
            stVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
          htmpl.set("blkParticelleNonImportate.blkElencoParticelleNonImportate.sezione", stVO.getSezione());
          htmpl.set("blkParticelleNonImportate.blkElencoParticelleNonImportate.foglio", stVO.getFoglio().toString());
          if(Validator.isNotEmpty(stVO.getParticella())) {
           htmpl.set("blkParticelleNonImportate.blkElencoParticelleNonImportate.particella", stVO.getParticella().toString());
          }
          if(Validator.isNotEmpty(stVO.getSubalterno())) {
           htmpl.set("blkParticelleNonImportate.blkElencoParticelleNonImportate.subalterno", stVO.getSubalterno());
          }
          htmpl.set("blkParticelleNonImportate.blkElencoParticelleNonImportate.supCatastale", stVO.getSupCatastale());
        }
      }
    
    }
  }

%>
<%= htmpl.text()%>

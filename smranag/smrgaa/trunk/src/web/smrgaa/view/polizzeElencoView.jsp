<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.PolizzaVO" %>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.FiltroPolizzaVO" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/polizzeElenco.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
      <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Vector<Integer> elencoAnnoCampagna = (Vector<Integer>)request.getAttribute("elencoAnnoCampagna");
  Vector<BaseCodeDescription> elencoIntervento = (Vector<BaseCodeDescription>)request.getAttribute("elencoIntervento");
  Vector<PolizzaVO> vPolizzaVO = (Vector<PolizzaVO>)request.getAttribute("vPolizzaVO");
  
  FiltroPolizzaVO filtroPolizzaVO = (FiltroPolizzaVO)session.getAttribute("filtroPolizzaVO");
  
  
  String annoCampagna = filtroPolizzaVO.getAnnoCampagna();
  String intervento = filtroPolizzaVO.getIntervento();
  
  
  if(elencoAnnoCampagna != null) 
  {
    htmpl.newBlock("blkAnnoCampagna");
        
    for(int i=0;i<elencoAnnoCampagna.size();i++) 
    {
      Integer anno = elencoAnnoCampagna.get(i);
      String annoCampagnaStr = anno.toString();      
        
      htmpl.set("blkAnnoCampagna.annoCampagnaValue", annoCampagnaStr);
      htmpl.set("blkAnnoCampagna.annoCampagnaDesc", annoCampagnaStr);
      
      if(Validator.isNotEmpty(annoCampagna) && annoCampagna.equalsIgnoreCase(annoCampagnaStr)) 
      {
        htmpl.set("blkAnnoCampagna.selectedAnnoCampagna", "selected=\"selected\"");
      }
        
    }
  }
  
  if(elencoIntervento != null) 
  {
    htmpl.newBlock("blkIntervento");
        
    for(int i=0;i<elencoIntervento.size();i++) 
    {
      BaseCodeDescription bcd = elencoIntervento.get(i);
      long id = bcd.getCode();
      String desc = bcd.getDescription();      
        
      htmpl.set("blkIntervento.interventoValue", ""+id);
      htmpl.set("blkIntervento.interventoDesc", desc);
      
      if(Validator.isNotEmpty(intervento) && intervento.equalsIgnoreCase(""+id)) 
      {
        htmpl.set("blkIntervento.selectedIntervento", "selected=\"selected\"");
      }
        
    }
  }
 
  //Terreni
  BigDecimal totValoreAssicurato = new BigDecimal(0);
  BigDecimal totImportoPremio = new BigDecimal(0);
  BigDecimal totValoreRisarcito = new BigDecimal(0);
  BigDecimal totImportoPagato = new BigDecimal(0);
  
  TreeMap<String, String> tLegenda = new TreeMap<String, String>();


  if(Validator.isNotEmpty(vPolizzaVO)) 
  {
    htmpl.newBlock("blkPresenzaPolizze");
    
    
    htmpl.set("blkPresenzaPolizze.totaleRighe",
         ""+vPolizzaVO.size());
    
    for(int i=0;i<vPolizzaVO.size();i++)
    {
      PolizzaVO polizzaVO = vPolizzaVO.get(i);
      htmpl.newBlock("blkPresenzaPolizze.blkElencoPolizze");
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.idPolizzaAssicurativa",
         ""+polizzaVO.getIdPolizzaAssicurativa());
      if(Validator.isNotEmpty(filtroPolizzaVO)
        && new Long(polizzaVO.getIdPolizzaAssicurativa()).toString().equalsIgnoreCase(filtroPolizzaVO.getIdPolizzaAssicurativa()))
      {
        htmpl.set("blkPresenzaPolizze.blkElencoPolizze.checked",
         "checked");
      }
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.annoCampagnaElenco",
        ""+polizzaVO.getAnnoCampagna());
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.numeroPolizza",
        polizzaVO.getNumeroPolizza());
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.codIntervento", polizzaVO.getCodiceIntervento());
      if(tLegenda.get(polizzaVO.getCodiceIntervento()) ==null)
      {
        tLegenda.put(polizzaVO.getCodiceIntervento(), polizzaVO.getDescrizioneIntervento());
      }
      
      String descMacrouso = "";
      if(Validator.isNotEmpty(polizzaVO.getvMacroUso()))
      {
        for(int j=0;j<polizzaVO.getvMacroUso().size();j++)
        {
          descMacrouso += polizzaVO.getvMacroUso().get(j);
          if(j!=0)
          {
            descMacrouso += "<br>";
          }
        }
      }
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.descMacrouso", descMacrouso, null);      
      
      String compagnia = "["+polizzaVO.getCodiceCompagnia()+"]"
        +" "+polizzaVO.getDenominazioneCompagnia();
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.compagnia", compagnia);
      
      if(Validator.isNotEmpty(polizzaVO.getCodiceConsorzio()))
      {
        String consorzio = "["+polizzaVO.getCodiceConsorzio()+"]"
          +" "+polizzaVO.getDescrizioneConsorzio();
        htmpl.set("blkPresenzaPolizze.blkElencoPolizze.consorzio", consorzio);
      }
      
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.valoreAssicurato", 
        Formatter.formatDouble2Separator(polizzaVO.getValoreAssicurato()));
      totValoreAssicurato = totValoreAssicurato.add(polizzaVO.getValoreAssicurato());
      
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.importoPremio", 
        Formatter.formatDouble2Separator(polizzaVO.getImportoPremio()));
      totImportoPremio = totImportoPremio.add(polizzaVO.getImportoPremio());
      
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.valoreRisarcito", 
        Formatter.formatDouble2Separator(polizzaVO.getValoreRisarcito()));
      totValoreRisarcito = totValoreRisarcito.add(polizzaVO.getValoreRisarcito());
        
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.importoPagato", 
        Formatter.formatDouble2Separator(polizzaVO.getImportoPagato()));
      totImportoPagato = totImportoPagato.add(polizzaVO.getImportoPagato());
      
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.dataStipulazione",
        DateUtils.formatDateNotNull(polizzaVO.getDataPolizza()));
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.dataQuietanza",
        DateUtils.formatDateNotNull(polizzaVO.getDataQuietanza()));
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.periodoRiferimento",
        polizzaVO.getDescrizionePeriodo());
      String polizzaIntegrativa = "No";
      if("S".equalsIgnoreCase(polizzaVO.getPolizzaIntegrativa()))
      {
        polizzaIntegrativa = "Si";
      }
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.polizzaIntegrativa",polizzaIntegrativa);
      String aggiuntiva = "No";
      if("S".equalsIgnoreCase(polizzaVO.getAggiuntiva()))
      {
        aggiuntiva = "Si";
      }
      htmpl.set("blkPresenzaPolizze.blkElencoPolizze.aggiuntiva", aggiuntiva);
    
    
    }
    
    
    htmpl.set("blkPresenzaPolizze.totValoreAssicurato", 
        Formatter.formatDouble2Separator(totValoreAssicurato));
    htmpl.set("blkPresenzaPolizze.totImportoPremio", 
        Formatter.formatDouble2Separator(totImportoPremio));
    htmpl.set("blkPresenzaPolizze.totValoreRisarcito", 
        Formatter.formatDouble2Separator(totValoreRisarcito));
    htmpl.set("blkPresenzaPolizze.totImportoPagato", 
        Formatter.formatDouble2Separator(totImportoPagato));
        
    //popola legenda
    if(tLegenda != null)
    {
      for(Map.Entry<String,String> entry : tLegenda.entrySet()) 
      {
        htmpl.newBlock("blkPresenzaPolizze.blkLegendaIntervento");
        String key = entry.getKey();
        String value = entry.getValue();
        String interventoElenco = "["+key+"]"+" "+value;
        htmpl.set("blkPresenzaPolizze.blkLegendaIntervento.interventoElenco", interventoElenco);        
      }      
    
    }
    
    
    
  }
  else 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", SolmrConstants.NO_POLIZZE);
  }


%>
<%= htmpl.text()%>

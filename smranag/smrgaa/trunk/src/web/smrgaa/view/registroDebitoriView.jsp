<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.sigop.dto.services.SchedaCreditoVO" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="java.util.TreeMap" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/registroDebitori.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  SchedaCreditoVO[] schedeCredito = (SchedaCreditoVO[])session.getAttribute("schedeCredito");
  
  String fondo = request.getParameter("fondo");
  String tipoDebito = request.getParameter("tipoDebito");
  String statoScheda = request.getParameter("statoScheda");
  
  
  if(schedeCredito != null)
  {
    TreeMap<String, String> tFondo = null;
    TreeMap<String, String> tTipoDebito = null;
    TreeMap<String, String> tStatoScheda = null;
    
    for(int i=0;i<schedeCredito.length;i++)
    {
      String idFondo = schedeCredito[i].getCodiceFondo();
      if(idFondo != null)
      {
        if(tFondo == null)
        {
          tFondo = new TreeMap<String, String>();
        }
                
        if(tFondo.get(idFondo) == null)
        {        
          tFondo.put(idFondo, idFondo);
        }
      }
      
      String idTipoDebito = schedeCredito[i].getTipoDebito();
      if(idTipoDebito != null)
      {
        if(tTipoDebito == null)
        {
          tTipoDebito = new TreeMap<String, String>();
        }
                
        if(tTipoDebito.get(idTipoDebito) == null)
        {        
          tTipoDebito.put(idTipoDebito, idTipoDebito);
        }
      }
      
      String idStatoScheda = schedeCredito[i].getStatoScheda();
      if(idStatoScheda != null)
      {
        if(tStatoScheda == null)
        {
          tStatoScheda = new TreeMap<String, String>();
        }
                
        if(tStatoScheda.get(idStatoScheda) == null)
        {        
          tStatoScheda.put(idStatoScheda, idStatoScheda);
        }
      }
      
        
    }
    
    if(tFondo != null) 
    {
      Iterator<String> iteraFondo = tFondo.keySet().iterator();
      while(iteraFondo.hasNext()) 
      {
        String desc = iteraFondo.next();
        htmpl.newBlock("blkElencoFondo");
        htmpl.set("blkElencoFondo.codiceFondo", desc);
        htmpl.set("blkElencoFondo.descFondo", desc);
        
        if(Validator.isNotEmpty(fondo) && fondo.equalsIgnoreCase(desc)) 
        {
          htmpl.set("blkElencoFondo.selected", "selected=\"selected\"",null);
        }
        
      }
    }
    
    
    if(tTipoDebito != null) 
    {
      Iterator<String> iteraTipoDebito = tTipoDebito.values().iterator();
      while(iteraTipoDebito.hasNext()) 
      {
        String desc = iteraTipoDebito.next();
        htmpl.newBlock("blkElencoTipoDebito");
        htmpl.set("blkElencoTipoDebito.codiceTipoDebito", desc);
        htmpl.set("blkElencoTipoDebito.descTipoDebito", desc);
        
        if(Validator.isNotEmpty(tipoDebito) && tipoDebito.equalsIgnoreCase(desc)) 
        {
          htmpl.set("blkElencoTipoDebito.selected", "selected=\"selected\"",null);
        }
        
      }
    }
    
    if(tStatoScheda != null) 
    {
      Iterator<String> iteraStatoScheda = tStatoScheda.values().iterator();
      while(iteraStatoScheda.hasNext()) 
      {
        String desc = iteraStatoScheda.next();
        htmpl.newBlock("blkElencoStatoScheda");
        htmpl.set("blkElencoStatoScheda.codiceStatoScheda", desc);
        htmpl.set("blkElencoStatoScheda.descStatoScheda", desc);
        
        if(Validator.isNotEmpty(statoScheda) && statoScheda.equalsIgnoreCase(desc)) 
        {
          htmpl.set("blkElencoStatoScheda.selected", "selected=\"selected\"",null);
        }
        
      }
    }       
 
  }
  
  
  
 
 
 
  SchedaCreditoVO[] schedeCreditoElenco = (SchedaCreditoVO[])request.getAttribute("schedeCredito");
  if(schedeCreditoElenco != null)
  {
    htmpl.newBlock("blkElencoSchede");
    htmpl.set("blkElencoSchede.numeroRecord",""+schedeCreditoElenco.length);
    int cont = 1;
    for(int i=0;i<schedeCreditoElenco.length;i++)
    {
      SchedaCreditoVO schedaCreditoVO = schedeCreditoElenco[i];
      htmpl.newBlock("blkScheda");
      htmpl.set("blkElencoSchede.blkScheda.aggancio","tr"+cont);
      String numeroScheda = "";
      if(Validator.isNotEmpty(schedaCreditoVO.getNumeroScheda()))
      {
        numeroScheda = ""+schedaCreditoVO.getNumeroScheda().intValue();
      }      
      htmpl.set("blkElencoSchede.blkScheda.numeroScheda", numeroScheda);
      htmpl.set("blkElencoSchede.blkScheda.codiceFondo", schedaCreditoVO.getCodiceFondo());
      htmpl.set("blkElencoSchede.blkScheda.tipoDebito", schedaCreditoVO.getTipoDebito());
      htmpl.set("blkElencoSchede.blkScheda.statoScheda", schedaCreditoVO.getStatoScheda());
      String dataInizioDebito = "";
      if(Validator.isNotEmpty(schedaCreditoVO.getDataInizioDebito()))
      {
        dataInizioDebito = DateUtils.formatDate(schedaCreditoVO.getDataInizioDebito());
      }
      htmpl.set("blkElencoSchede.blkScheda.dataInizioDebito", dataInizioDebito);
      String presenzaGaranzia = "";
      if(Validator.isNotEmpty(schedaCreditoVO.getPresenzaGaranzia()))
      {
        if("S".equalsIgnoreCase(schedaCreditoVO.getPresenzaGaranzia()))
        {
          presenzaGaranzia = "Si";
        }
        else if("N".equalsIgnoreCase(schedaCreditoVO.getPresenzaGaranzia()))
        {
          presenzaGaranzia = "No";
        }
      }
      htmpl.set("blkElencoSchede.blkScheda.presenzaGaranzia", presenzaGaranzia);
      BigDecimal importoDaRecuperare = new BigDecimal(0);
      if(schedaCreditoVO.getImportoDebito() != null)
      {
        importoDaRecuperare = importoDaRecuperare.add(schedaCreditoVO.getImportoDebito());
      }
      htmpl.set("blkElencoSchede.blkScheda.importoDebito", Formatter.formatDouble2(schedaCreditoVO.getImportoDebito()));
      if(schedaCreditoVO.getImportoRecupero() != null)
      {
        importoDaRecuperare = importoDaRecuperare.subtract(schedaCreditoVO.getImportoRecupero());
      }      
      htmpl.set("blkElencoSchede.blkScheda.importoRecupero", Formatter.formatDouble2(schedaCreditoVO.getImportoRecupero()));
      
      htmpl.set("blkElencoSchede.blkScheda.importoDaRecuperare", Formatter.formatDouble2(importoDaRecuperare));
      
      if(schedaCreditoVO.getElencoDisposizioniTrasgredite() != null)
      {
        htmpl.newBlock("blkElencoDettaglioScheda");
        String blk = "blkElencoSchede.blkScheda.blkElencoDettaglioScheda";
        htmpl.set(blk+".aggancio","tr"+cont); 
        for(int j=0;j<schedaCreditoVO.getElencoDisposizioniTrasgredite().length;j++)
        {
          htmpl.newBlock("blkDettaglioScheda");
          htmpl.set(blk+".blkDettaglioScheda.regolamentoTrasgredito", schedaCreditoVO
            .getElencoDisposizioniTrasgredite()[j].getRegolamentoTrasgredito());
          htmpl.set(blk+".blkDettaglioScheda.interventoTrasgredito", schedaCreditoVO
            .getElencoDisposizioniTrasgredite()[j].getInterventoTrasgredito());
          String annoCampagna = "";
          if(Validator.isNotEmpty(schedaCreditoVO
            .getElencoDisposizioniTrasgredite()[j].getAnnoCampagna()))
          {
            annoCampagna = ""+schedaCreditoVO
            .getElencoDisposizioniTrasgredite()[j].getAnnoCampagna().intValue();
          }
          htmpl.set(blk+".blkDettaglioScheda.annoCampagna", annoCampagna);          
          htmpl.set(blk+".blkDettaglioScheda.numeroDomanda", schedaCreditoVO
            .getElencoDisposizioniTrasgredite()[j].getNumeroDomanda());
          String dataDomanda = "";
		      if(Validator.isNotEmpty(schedaCreditoVO.getElencoDisposizioniTrasgredite()[j].getDataDomanda()))
		      {
		        dataDomanda = DateUtils.formatDate(schedaCreditoVO.getElencoDisposizioniTrasgredite()[j].getDataDomanda());
		      }
          htmpl.set(blk+".blkDettaglioScheda.dataDomanda", dataDomanda);
        }
      }
      
      cont++;
                  
    }   
 
  }
  
  
  
 
  
 
  
  
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>

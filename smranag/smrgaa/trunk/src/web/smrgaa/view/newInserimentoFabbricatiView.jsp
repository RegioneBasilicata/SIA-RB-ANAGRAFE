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
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoFabbricati.htm");
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
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  Vector<ValidationErrors> elencoErroriPart = (Vector<ValidationErrors>)request.getAttribute("elencoErroriPart"); 
  FabbricatoAziendaNuovaVO fabbricatoAziendaNuovaVO = (FabbricatoAziendaNuovaVO)session.getAttribute("fabbricatoAziendaNuovaVO");
  Vector<CodeDescription> elencoTipologiaFabbricato = (Vector<CodeDescription>)request.getAttribute("elencoTipologiaFabbricato");
  Vector<ProvinciaVO> vProvince =  (Vector<ProvinciaVO>)request.getAttribute("vProvince");
  HashMap<String, Vector<ComuneVO>> hProvCom = (HashMap<String, Vector<ComuneVO>>)request.getAttribute("hProvCom"); 
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = (Vector<UteAziendaNuovaVO>)request.getAttribute("vUteAziendaNuova");
  Vector<PartFabbrAziendaNuovaVO> vPartFabbrAziendaNuova = (Vector<PartFabbrAziendaNuovaVO>)session
    .getAttribute("vPartFabbrAziendaNuova");
  CodeDescription[] elencoTipoTitoloPossesso = (CodeDescription[])request.getAttribute("elencoTipoTitoloPossesso");
  Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova = (Vector<FabbricatoAziendaNuovaVO>)session.getAttribute("vFabbrAziendaNuova");
  
  
  
  if(elencoTipologiaFabbricato != null) 
  {
    Iterator iteraTipologiaFabbricato = elencoTipologiaFabbricato.iterator();
    while(iteraTipologiaFabbricato.hasNext()) 
    {
      htmpl.newBlock("tipologiaFabbricato");
      CodeDescription code = (CodeDescription)iteraTipologiaFabbricato.next();
      htmpl.set("tipologiaFabbricato.idCodice",code.getCode().toString());
      if(fabbricatoAziendaNuovaVO != null) 
      {
        if(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato() != null) 
        {
          if(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato().compareTo(Long.decode(code.getCode().toString())) == 0) 
          {
            htmpl.set("tipologiaFabbricato.check", "selected");
          }
        }
      }
      htmpl.set("tipologiaFabbricato.descrizione",code.getDescription());
    }
  }
  
  if(vUteAziendaNuova != null)
  {
    for(int i=0;i<vUteAziendaNuova.size();i++)
    {
      htmpl.newBlock("blkElencoUte");
      htmpl.set("blkElencoUte.idUteAziendaNuova", ""+vUteAziendaNuova.get(i).getIdUteAziendaNuova());
      String descUte = "";
      if(Validator.isNotEmpty(vUteAziendaNuova.get(i).getDenominazione()))
      {
        descUte = vUteAziendaNuova.get(i).getDenominazione()+ " - ";
      }
      descUte += vUteAziendaNuova.get(i).getDesCom()+" ("+vUteAziendaNuova.get(i).getSglProvincia()+")";
      htmpl.set("blkElencoUte.descUte", descUte);
      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO) && Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getIdUteAziendaNuova()) 
        && (fabbricatoAziendaNuovaVO.getIdUteAziendaNuova().compareTo(vUteAziendaNuova.get(i).getIdUteAziendaNuova()) ==0)
        || (vUteAziendaNuova.size() == 1))
      {
        htmpl.set("blkElencoUte.selected","selected", null);
      }   
    }
  
  }
  
  Long idTipologiaFabbricato=null;
  if(fabbricatoAziendaNuovaVO != null)
  { 
    if (Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getUnitaMisura()))
    {
      htmpl.set("unitaMisuraPar", "("+fabbricatoAziendaNuovaVO.getUnitaMisura()+")");
      htmpl.set("unitaMisura", fabbricatoAziendaNuovaVO.getUnitaMisura());
    }
    else htmpl.set("unitaMisuraPar", "");
    
    if (Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrSuperficie()))
      htmpl.set("superficieFabbricato", fabbricatoAziendaNuovaVO.getStrSuperficie());
    if (Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrDimensione()))
      htmpl.set("dimensioneFabbricato", fabbricatoAziendaNuovaVO.getStrDimensione());
    if (Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrAnnoCostruzione()))
      htmpl.set("annoCostruzioneFabbricato", fabbricatoAziendaNuovaVO.getStrAnnoCostruzione());
    if (Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrSuperficieCoperta()))
      htmpl.set("superficieCopertaFabbricato", fabbricatoAziendaNuovaVO.getStrSuperficieCoperta());
    if (Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrSuperficieScoperta()))
      htmpl.set("superficieScopertaFabbricato", fabbricatoAziendaNuovaVO.getStrSuperficieScoperta());
    
    
	  if(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato() != null)
	  { 
	    idTipologiaFabbricato=fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato();
	    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = fabbricatoAziendaNuovaVO.getTipoTipologiaFabbricatoVO();
	    if(tipoTipologiaFabbricatoVO.getVLabel() !=null)
	    {
	      Vector vLabel = tipoTipologiaFabbricatoVO.getVLabel();
	      if(vLabel.size() == 2)
	      {
	        htmpl.set("colspanPrima","3");
	        htmpl.newBlock("tipologiaFabbricatoDimensioniA");
	        htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricatoLabel",(String)vLabel.get(0));
	        htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricatoLabel",(String)vLabel.get(1));
	        htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricato",fabbricatoAziendaNuovaVO.getStrLarghezza());
	        htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricato",fabbricatoAziendaNuovaVO.getStrLunghezza());
	      }
	      else if(vLabel.size() == 3)
	      {
	        htmpl.set("colspanPrima","5");
	        htmpl.newBlock("tipologiaFabbricatoDimensioniB");
	        htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricatoLabel",(String)vLabel.get(0));
	        htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricatoLabel",(String)vLabel.get(1));
	        htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricatoLabel",(String)vLabel.get(2));
	        htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricato",fabbricatoAziendaNuovaVO.getStrLarghezza());
	        htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricato",fabbricatoAziendaNuovaVO.getStrLunghezza());
	        htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricato",fabbricatoAziendaNuovaVO.getStrAltezza());
	      }
	      else
	      {
	        throw new SolmrException("Il Vettore con le Label ha dimensione diversa da 2 o 3");
	      }
	    }
	  }
	}
  
  htmpl.set("dimcolspan","2");
  htmpl.set("dimcolspanAnno","5");
  
  
  
  
  
  if(vPartFabbrAziendaNuova != null)
  {
  
    for(int i=0;i<vPartFabbrAziendaNuova.size();i++)
    {
      htmpl.newBlock("blkElencoParticelle");
      PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVO = vPartFabbrAziendaNuova.get(i);
      htmpl.set("blkElencoParticelle.idRigaPart", ""+i);
		  if(vProvince!=null)
		  {
		    for(int j=0;j<vProvince.size();j++)
		    {
		      ProvinciaVO provinciaVO = vProvince.get(j);
		      htmpl.newBlock("blkElencoProvince");
		      htmpl.set("blkElencoParticelle.blkElencoProvince.istatProvincia", provinciaVO.getIstatProvincia());
		      htmpl.set("blkElencoParticelle.blkElencoProvince.sglProv", provinciaVO.getSiglaProvincia());
		      if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIstatProvincia()) 
		        && partFabbrAziendaNuovaVO.getIstatProvincia().equals(provinciaVO.getIstatProvincia()))
		      {
		        htmpl.set("blkElencoParticelle.blkElencoProvince.selected","selected", null);
		      }
		    }
		  }
		  
		  
		  if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIstatProvincia()))
		  {
		    Vector<ComuneVO> vComune = hProvCom.get(partFabbrAziendaNuovaVO.getIstatProvincia());
		    for(int j=0;j<vComune.size();j++)
        {
          ComuneVO comuneVO = vComune.get(j);
          htmpl.newBlock("blkElencoComuni");
          htmpl.set("blkElencoParticelle.blkElencoComuni.istatComune", comuneVO.getIstatComune());
          htmpl.set("blkElencoParticelle.blkElencoComuni.desCom", comuneVO.getDescom());
          if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIstatComune()) 
            && partFabbrAziendaNuovaVO.getIstatComune().equals(comuneVO.getIstatComune()))
          {
            htmpl.set("blkElencoParticelle.blkElencoComuni.selected","selected", null);
          }
        }
		  
		  }
		  
		  if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getSezione()))
        htmpl.set("blkElencoParticelle.sezione", partFabbrAziendaNuovaVO.getSezione());
      if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getStrFoglio()))
        htmpl.set("blkElencoParticelle.foglio", partFabbrAziendaNuovaVO.getStrFoglio());
      if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getStrParticella()))
        htmpl.set("blkElencoParticelle.particella", partFabbrAziendaNuovaVO.getStrParticella());
      if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getSubalterno()))
        htmpl.set("blkElencoParticelle.subalterno", partFabbrAziendaNuovaVO.getSubalterno());
      if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getStrSuperficie()))
        htmpl.set("blkElencoParticelle.superficiePart", partFabbrAziendaNuovaVO.getStrSuperficie());
        
      
      if(elencoTipoTitoloPossesso !=null)
      {
        for(int j=0;j<elencoTipoTitoloPossesso.length;j++)
        {
          CodeDescription cd = elencoTipoTitoloPossesso[j];
          htmpl.newBlock("blkElencoTitoloPossesso");
          htmpl.set("blkElencoParticelle.blkElencoTitoloPossesso.idTitoloPossesso", ""+cd.getCode());
          htmpl.set("blkElencoParticelle.blkElencoTitoloPossesso.descTitoloPossesso", cd.getDescription());
          if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIdTitoloPossesso()) 
            && (partFabbrAziendaNuovaVO.getIdTitoloPossesso().compareTo(cd.getCode()) == 0))
          {
            htmpl.set("blkElencoParticelle.blkElencoTitoloPossesso.selected","selected", null);
          }
        }
      
      } 
      
        
      if(elencoErroriPart != null && elencoErroriPart.size() > 0) 
		  {
		    ValidationErrors errorsPart = (ValidationErrors)elencoErroriPart.elementAt(i);
		    if(errorsPart != null && errorsPart.size  () > 0) 
		    {
		      Iterator iter = htmpl.getVariableIterator();
		      while(iter.hasNext()) 
		      {
		        String key = (String)iter.next();
		        if(key.startsWith("err_")) 
		        {
		          String property = key.substring(4);
		          Iterator errorIterator = errorsPart.get(property);
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
  
  htmpl.newBlock("blkInserisciFabb");
  
  if((vFabbrAziendaNuova != null)
    && (vFabbrAziendaNuova.size() > 0))
  {
    htmpl.newBlock("blkFabbricati");
    for(int i=0;i<vFabbrAziendaNuova.size();i++)
    {
      boolean primo = true;
      int numPart = 1;
      FabbricatoAziendaNuovaVO fabbricatoAziendaNuovaVOTmp = vFabbrAziendaNuova.get(i);
      htmpl.newBlock("blkFabbricati.blkElencoFabbricati");
      if(primo)
      {
        htmpl.newBlock("blkFabbricati.blkElencoFabbricati.blkRigaPrimo");      
	      htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.idRiga",""+i);
	      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVOTmp.getvPartFabbrAziendaNuova()))
	      {
	        numPart = fabbricatoAziendaNuovaVOTmp.getvPartFabbrAziendaNuova().size();
	      }
	      htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.numPart",""+numPart);
	      htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.denominazUte",
	        fabbricatoAziendaNuovaVOTmp.getDenominazUte());
	      htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.descFabbricato",
          fabbricatoAziendaNuovaVOTmp.getDescFabbricato());
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.annoCostruzione",
          ""+fabbricatoAziendaNuovaVOTmp.getAnnoCostruzione());
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.larghezza",
          Formatter.formatAndRoundBigDecimal1(fabbricatoAziendaNuovaVOTmp.getLarghezza()));
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.lunghezza",
          Formatter.formatAndRoundBigDecimal1(fabbricatoAziendaNuovaVOTmp.getLunghezza()));
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.altezza",
          Formatter.formatAndRoundBigDecimal1(fabbricatoAziendaNuovaVOTmp.getAltezza()));
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.superficie",
          Formatter.formatDouble2(fabbricatoAziendaNuovaVOTmp.getSuperficie()));
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.dimensione",
          Formatter.formatDouble2(fabbricatoAziendaNuovaVOTmp.getDimensione()));
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.unitaMisura",
          fabbricatoAziendaNuovaVOTmp.getUnitaMisura());
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.superficieCoperta",
          Formatter.formatDouble2(fabbricatoAziendaNuovaVOTmp.getSuperficieCoperta()));
        htmpl.set("blkFabbricati.blkElencoFabbricati.blkRigaPrimo.superficieScoperta",
          Formatter.formatDouble2(fabbricatoAziendaNuovaVOTmp.getSuperficieScoperta()));
        if(Validator.isNotEmpty(fabbricatoAziendaNuovaVOTmp.getvPartFabbrAziendaNuova()))
        {
          for(int j=0;j<fabbricatoAziendaNuovaVOTmp.getvPartFabbrAziendaNuova().size();j++)
          {
            if(primo)
            {
              primo = false;
            }
            else
            {
               htmpl.newBlock("blkFabbricati.blkElencoFabbricati");
            }           
            PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVOTmp = fabbricatoAziendaNuovaVOTmp.getvPartFabbrAziendaNuova().get(j);
            htmpl.set("blkFabbricati.blkElencoFabbricati.descComune", 
              partFabbrAziendaNuovaVOTmp.getDesCom()+" ("+partFabbrAziendaNuovaVOTmp.getSglProv()+")");
              			      
			      if(Validator.isNotEmpty(partFabbrAziendaNuovaVOTmp.getSezione()))
			        htmpl.set("blkFabbricati.blkElencoFabbricati.sezione", partFabbrAziendaNuovaVOTmp.getSezione());
			      htmpl.set("blkFabbricati.blkElencoFabbricati.foglio", ""+partFabbrAziendaNuovaVOTmp.getFoglio());
			      htmpl.set("blkFabbricati.blkElencoFabbricati.particella", ""+partFabbrAziendaNuovaVOTmp.getParticella());
			      if(Validator.isNotEmpty(partFabbrAziendaNuovaVOTmp.getSubalterno()))
			        htmpl.set("blkFabbricati.blkElencoFabbricati.subalterno", partFabbrAziendaNuovaVOTmp.getSubalterno());
			      htmpl.set("blkFabbricati.blkElencoFabbricati.superficiePart", 
			       Formatter.formatDouble4(partFabbrAziendaNuovaVOTmp.getSuperficie()));
			      htmpl.set("blkFabbricati.blkElencoFabbricati.destinazioneUso",
			        partFabbrAziendaNuovaVOTmp.getDescTipoUtilizzo()+"\n"+partFabbrAziendaNuovaVOTmp.getDescTipoVarieta());  
			      htmpl.set("blkFabbricati.blkElencoFabbricati.descTitoloPossesso",
              partFabbrAziendaNuovaVOTmp.getDescTitoloPossesso());		      
          
          }
        
        
        }
          
	    }
    
    }
  } 
  
  
  HtmplUtil.setErrors(htmpl,errors,request, application);
  
	
	


      
  

%>
<%= htmpl.text() %>

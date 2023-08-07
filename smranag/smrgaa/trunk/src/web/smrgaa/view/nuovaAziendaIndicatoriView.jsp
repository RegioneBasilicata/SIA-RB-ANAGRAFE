<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.client.anag.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/nuovaAziendaIndicatori.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");
  Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)session.getAttribute("insVAziendaAtecoSec");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  	
  htmpl.set("radiobuttonAzienda",(String)request.getAttribute("radiobuttonAzienda"));
	request.setAttribute("radiobuttonAzienda", (String)request.getAttribute("radiobuttonAzienda"));
	htmpl.set("cuaaProvenienza",(String)request.getAttribute("cuaaProvenienza"));
	request.setAttribute("cuaaProvenienza", (String)request.getAttribute("cuaaProvenienza"));
	
	
	
	
	
	
	Vector<TipoDimensioneAziendaVO> vDimAzienda = gaaFacadeClient.getListActiveTipoDimensioneAzienda();
	String idDimensioneAzienda = request.getParameter("idDimensioneAzienda");
  if(vDimAzienda != null) 
  {
    for(int i=0;i<vDimAzienda.size();i++)
    {
      TipoDimensioneAziendaVO tipoDimensioneAzienda = (TipoDimensioneAziendaVO)vDimAzienda.get(i);
      htmpl.newBlock("cmbDimAzienda");
      htmpl.set("cmbDimAzienda.idDimensioneAzienda",""+tipoDimensioneAzienda.getIdDimensioneAzienda());
      htmpl.set("cmbDimAzienda.descDimensioneAzienda",tipoDimensioneAzienda.getDescrizione());
      if(!Validator.isNotEmpty(request.getParameter("regimeNuovaAziendaIndicatori")))
      {
        if(anagAziendaVO != null && (anagAziendaVO.getIdDimensioneAzienda() != null)
          && (tipoDimensioneAzienda.getIdDimensioneAzienda() == anagAziendaVO.getIdDimensioneAzienda().longValue())) 
        {
          htmpl.set("cmbDimAzienda.selected","selected");
        }
      }
      else
      {
        if(Validator.isNotEmpty(idDimensioneAzienda))
        {
          Long idDimensioneAziendaLg = new Long(idDimensioneAzienda);
          if(tipoDimensioneAzienda.getIdDimensioneAzienda() == idDimensioneAziendaLg.longValue())
          {
            htmpl.set("cmbDimAzienda.selected","selected");
          }        
        }
      }
    }
  }
  
  
  
  String obbligoGF = anagFacadeClient.getObbligoGfFromFormaGiuridica(new Long(anagAziendaVO.getTipoFormaGiuridica().getCode().intValue()));
 
  if("S".equalsIgnoreCase(obbligoGF))
  {
    htmpl.newBlock("blkCCSpeciale");
    
    String esoneroPagamentoGf = request.getParameter("esoneroPagamentoGf");
    if(Validator.isEmpty(request.getParameter("regimeNuovaAziendaIndicatori")))
    {
      if(Validator.isNotEmpty(anagAziendaVO.getEsoneroPagamentoGF())
        && "S".equalsIgnoreCase(anagAziendaVO.getEsoneroPagamentoGF()))
      {
        htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfS", "checked=\"true\"", null);
      }
      else
      {
        htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfN", "checked=\"true\"", null);
      } 
    }
    else
    {
      if("S".equalsIgnoreCase(esoneroPagamentoGf))
      {
        htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfS", "checked=\"true\"", null);
      }
      else
      {
        htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfN", "checked=\"true\"", null);
      } 
    }
  }
  
  
  
  if((vAziendaAtecoSec != null) && (vAziendaAtecoSec.size() > 0))
  {
    htmpl.newBlock("blkAttivitaATECOSec");
    for(int i=0;i<vAziendaAtecoSec.size();i++)
    {
      htmpl.newBlock("blkElencoAttivitaATECOSec");
      AziendaAtecoSecVO aziendaAtecoSec = (AziendaAtecoSecVO)vAziendaAtecoSec.get(i);
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.chkAttivitaAtecoSec", new Integer(i).toString());
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.idAttivitaATECOElenco", new Long(aziendaAtecoSec.getIdAttivitaAteco()).toString()); 
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.codiceATECOSecElenco", aziendaAtecoSec.getCodAttivitaAteco());
      htmpl.set("blkAttivitaATECOSec.blkElencoAttivitaATECOSec.descrizioneATECOSecElenco", aziendaAtecoSec.getDescAttivitaAteco());
    }
  }
	
	
	
	
	if(errors == null)
  {
    if(anagAziendaVO != null)
    {
      if(anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode() != null)
        htmpl.set("codiceOTE",anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode().toString());
      if(anagAziendaVO.getTipoAttivitaOTE().getDescription() != null)
        htmpl.set("descrizioneOTE",anagAziendaVO.getTipoAttivitaOTE().getDescription());
      if(anagAziendaVO.getTipoAttivitaATECO() == null)
      {
        throw new Exception("Problemi nel reperimento del codice ateco");
      }
      else
      {
	      if(anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode() != null)
	        htmpl.set("codiceATECO",anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode().toString());
	      if(anagAziendaVO.getTipoAttivitaATECO().getDescription() != null)
	        htmpl.set("descrizioneATECO",anagAziendaVO.getTipoAttivitaATECO().getDescription());
	    }
      if(Validator.isNotEmpty(anagAziendaVO.getCCIAAprovREA())) {
        htmpl.set("CCIAAprovREA", anagAziendaVO.getCCIAAprovREA());
      }
      HtmplUtil.setValues(htmpl, anagAziendaVO);
    }
    HtmplUtil.setValues(htmpl, request);
  }
  else
  {
    
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }
  
  
  
  
	
 
 
 
 

%>

<%= htmpl.text() %>

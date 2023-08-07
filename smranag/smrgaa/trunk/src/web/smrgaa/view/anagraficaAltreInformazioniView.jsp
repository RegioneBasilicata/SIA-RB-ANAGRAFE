  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.abio.OperatoreBiologicoVO" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="it.csi.smranag.smrgaa.dto.abio.PosizioneOperatoreVO"%>

<%
 	java.io.InputStream layout = application.getResourceAsStream("/layout/anagraficaAltreInformazioni.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  htmpl.set("CCIAAprovREA", anagAziendaVO.getCCIAAprovREA());
  if(anagAziendaVO.getCCIAAnumeroREA() != null)
  {
    htmpl.set("CCIAAnumeroREA", ""+anagAziendaVO.getCCIAAnumeroREA());
  }
  htmpl.set("CCIAAannoIscrizione", anagAziendaVO.getCCIAAannoIscrizione());
  htmpl.set("CCIAAnumRegImprese", anagAziendaVO.getCCIAAnumRegImprese());
  
  
  String flagIap = "No";
  if("S".equalsIgnoreCase(anagAziendaVO.getFlagIap()))
    flagIap = "Si";
  htmpl.set("flagIap", flagIap);
  
  
  String obbligoGF = (String)request.getAttribute("obbligoGF");
  if("S".equalsIgnoreCase(obbligoGF))
  {
    htmpl.newBlock("blkCCSpeciale");
    if("S".equalsIgnoreCase(anagAziendaVO.getEsoneroPagamentoGF()))
    {
      htmpl.set("blkCCSpeciale.esoneroPagamentoGf", "Si");
    }
    else
    {
      htmpl.set("blkCCSpeciale.esoneroPagamentoGf", "No");    
    } 
    
  }
  
  OperatoreBiologicoVO operatoreBiologico = (OperatoreBiologicoVO)session.getAttribute("operatoreBiologico");
  
  if(operatoreBiologico==null)
  { 
    htmpl.set("blkABIO.msgABIO", "L'azienda non è un operatore biologico");
  }
  else
  {
    if(operatoreBiologico.getDataInizioAttivita() != null && !operatoreBiologico.getDataInizioAttivita().equals(""))
      htmpl.set("blkDatiABIO.dataDal", DateUtils.formatDate(operatoreBiologico.getDataInizioAttivita()));
    
    if(operatoreBiologico.getDataFineAttivita() != null && !operatoreBiologico.getDataFineAttivita().equals(""))
      htmpl.set("blkDatiABIO.dataAl", DateUtils.formatDate(operatoreBiologico.getDataFineAttivita()));
    
    if(anagAziendaVO.getDataFineVal()==null || anagAziendaVO.getDataFineVal().equals("")){
      htmpl.newBlock("blkAggiornaDataCorrente");
    }
    if(request.getParameter("chkStorico")!=null&&"S".equals(request.getParameter("chkStorico")))
      htmpl.set("blkAggiornaDataCorrente.chkStorico", "checked");
       
    PosizioneOperatoreVO[] posizioneOperatore = (PosizioneOperatoreVO[])session.getAttribute("posizioneOperatore");
    
    if(posizioneOperatore!=null && posizioneOperatore.length>0){
      htmpl.newBlock("blkIntestazioneTableAttivitaBio");
      for(int i=0;i<posizioneOperatore.length; i++){
      	@SuppressWarnings("unchecked")
        HashMap<Date, CodeDescription[]> hm = (HashMap<Date, CodeDescription[]>)session.getAttribute("ODC");
        CodeDescription[] cd = hm.get(posizioneOperatore[i].getDataInizioValidita());
        
        if(cd!=null && cd.length>0){
          htmpl.set("blkTableAttivitaBio.righeODC", ""+cd.length);
          htmpl.set("blkTableAttivitaBio.primoODC", "["+cd[0].getCodeFlag()+"] "+cd[0].getDescription());
          for(int count=1; count<cd.length; count++){
            htmpl.set("blkTableAttivitaBio.blkAltriODC.altriODC", "["+cd[count].getCodeFlag()+"] "+cd[count].getDescription());
          }
        }else htmpl.set("blkTableAttivitaBio.righeODC", "1");
        
        htmpl.set("blkTableAttivitaBio.categoria", posizioneOperatore[i].getDescCategoria());
        htmpl.set("blkTableAttivitaBio.stato", posizioneOperatore[i].getDescStatoAttivita());
        
        if(posizioneOperatore[i].getDataInizioValidita()!=null && !posizioneOperatore[i].getDataInizioValidita().equals(""))  
          htmpl.set("blkTableAttivitaBio.dal", DateUtils.formatDate(posizioneOperatore[i].getDataInizioValidita()));
        if(posizioneOperatore[i].getDataFineValidita()!=null && !posizioneOperatore[i].getDataFineValidita().equals(""))
          htmpl.set("blkTableAttivitaBio.al", DateUtils.formatDate(posizioneOperatore[i].getDataFineValidita()));
        
      }
    }
  }

	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

	if (errors!=null) {
  	HtmplUtil.setErrors(htmpl, errors, request, application);
	}
	
  HtmplUtil.setValues(htmpl, request);

%>
<%= htmpl.text()%>

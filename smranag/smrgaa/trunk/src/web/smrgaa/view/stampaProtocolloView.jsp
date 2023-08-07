<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.TipoReportVO" %>



<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/stampaProtocollo.htm");
  
  %> 
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  TreeMap<String,RichiestaTipoReportVO[]> hSubReportElements = 
    (TreeMap<String,RichiestaTipoReportVO[]>)request.getAttribute("hSubReportElements");
  HashMap<Long,String> hReportCompatibile = (HashMap<Long,String>)request.getAttribute("hReportCompatibile");  
  Vector<CodeDescription> vElencoPropietari = (Vector<CodeDescription>)request.getAttribute("vElencoPropietari");
  HashMap<Long,String> hProprietari = (HashMap<Long,String>)request.getAttribute("hProprietari");
  boolean almenoUnQuadro = false;
  HashMap<String,String> hProprietariInTutti = (HashMap<String,String>)request.getAttribute("hProprietariInTutti"); 
  
  if(hSubReportElements != null)
  {    
     Iterator<String> it = hSubReportElements.keySet().iterator(); 
     while(it.hasNext()) 
     { 
       String titolo = it.next(); 
       RichiestaTipoReportVO[] subReportElements = hSubReportElements.get(titolo);
       
       htmpl.newBlock("blkStampaReport");
       //E' uguale per tutti quindi prendo il primo...
       htmpl.set("blkStampaReport.idTipoReport", ""+subReportElements[0].getIdTipoReport());
       htmpl.set("blkStampaReport.descrizioneReport", titolo);
       
       if(hReportCompatibile.get(new Long(subReportElements[0].getIdTipoReport())) != null)
       {
         htmpl.set("blkStampaReport.compatibile", "ok");
       }
       
       if(hProprietari.get(new Long(subReportElements[0].getIdTipoReport())) != null)
       {
         htmpl.set("blkStampaReport.proprietari", "ok");
       }
       
       for(int i=0;i<subReportElements.length;i++)
       {   
         
         RichiestaTipoReportVO richiestaTipoReportVO = subReportElements[i];
         
         if(richiestaTipoReportVO.isSelezionabile())
         {
           if(!almenoUnQuadro)
           {
             almenoUnQuadro = true;
           }
         
           htmpl.newBlock("blkStampaReport.blkQuadri");
           //Se non sono al piano corrente devo disabilitare alcuni quadri
           //poiche sono obbligatori
           
           htmpl.set("blkStampaReport.blkQuadri.idTipoReport", ""+richiestaTipoReportVO.getIdTipoReport());         
           htmpl.set("blkStampaReport.blkQuadri.codice", ""+richiestaTipoReportVO.getIdReportSubReport());
           htmpl.set("blkStampaReport.blkQuadri.descrizioneSelezione", richiestaTipoReportVO.getDescrizioneSelezione());
         }
       }
       
       if(almenoUnQuadro)
       {
         htmpl.set("blkStampaReport.quadri", "ok");
       }
       
       if(Validator.isNotEmpty(vElencoPropietari) 
         && (hProprietari.get(new Long(subReportElements[0].getIdTipoReport())) != null))
       {
         htmpl.newBlock("blkStampaReport.blkElencoProprietari");
         for(int i=0;i<vElencoPropietari.size();i++)
         {
           htmpl.newBlock("blkStampaReport.blkElencoProprietari.blkProprietari");
           htmpl.set("blkStampaReport.blkElencoProprietari.blkProprietari.cuaa", vElencoPropietari.get(i).getDescription());         
           htmpl.set("blkStampaReport.blkElencoProprietari.blkProprietari.denominazione", vElencoPropietari.get(i).getSecondaryCode());
           
           if(hProprietariInTutti.get(vElencoPropietari.get(i).getDescription()) != null)
           {
             htmpl.set("blkStampaReport.blkElencoProprietari.blkProprietari.propInTutti", "ok");
           }
           
         }   
       
       }
       
        
     } 
     
       
  }

  ValidationErrors errors = (ValidationErrors) request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);
%>
<%=htmpl.text()%>

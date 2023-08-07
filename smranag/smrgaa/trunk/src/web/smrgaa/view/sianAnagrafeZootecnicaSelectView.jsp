<%@page language="java" contentType="text/html" isErrorPage="true"%>

<%@page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@page import="it.csi.solmr.dto.anag.sian.*"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.solmr.util.*"%>
<%@ page import="java.math.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/visuraBDNspecie.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  Hashtable elencoAllevamentiSian = (Hashtable) session.getAttribute("elencoAllevamentiSian");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String idSpecieSiap[]=request.getParameterValues("idSpecieSiap");
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "alt=\"{2}\" border=\"0\"></a>";
  String htmlStringOK = "<img src=\"{0}\" alt=\"{1}\">";
  String imko = "ko.gif";
  String imok = "ok.gif";
  String erroreSpecieSiap="Selezionare la specie SIAP";
  StringProcessor jssp = new JavaScriptStringProcessor();

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String messaggioErrore = (String) request.getAttribute("messaggioErrore");
  if (Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", messaggioErrore);
  }
  else 
  {
    if(elencoAllevamentiSian != null) 
    {
      Enumeration enumeraAllevamenti = elencoAllevamentiSian.elements();
      int i=0;
      while(enumeraAllevamenti.hasMoreElements()) 
      {
        SianAllevamentiVO sianAllevamentiVO = ((SianAllevamentiVO)enumeraAllevamenti.nextElement());
        if (sianAllevamentiVO.isSelect())
        {
          htmpl.newBlock("blkSiAllevamenti.blkElencoAllevamenti");
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.idAllevamento", sianAllevamentiVO.getAllevId().toString());
                   
          
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.codiceAllevamento", sianAllevamentiVO.getAziendaCodice());
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.specie", sianAllevamentiVO.getDescrizioneSpecie());
          
          
          if (sianAllevamentiVO.getSpecieSiap() != null)
          {
            if (sianAllevamentiVO.getSpecieSiap().length>1)
              htmpl.newBlock("blkSiAllevamenti.blkElencoAllevamenti.blkDefault");
            for (int j = 0; j < sianAllevamentiVO.getSpecieSiap().length; j++)
            {
              htmpl.newBlock("blkSiAllevamenti.blkElencoAllevamenti.blkComboSpecie");
              htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.blkComboSpecie.idSpecieSiap", "" + sianAllevamentiVO.getSpecieSiap()[j].getCode());
              
              if (idSpecieSiap!=null && idSpecieSiap.length>0)              
                if (("" + sianAllevamentiVO.getSpecieSiap()[j].getCode()).equals(idSpecieSiap[i]))
                  htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.blkComboSpecie.selected", "selected");
              htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.blkComboSpecie.specieSiapDesc", sianAllevamentiVO.getSpecieSiap()[j].getDescription());
            }
            if (idSpecieSiap!=null && idSpecieSiap.length>0 )
            {
              if ("-1".equals(idSpecieSiap[i]))
                //Non è stato selezionato nulla quindi devo segnalare l'errore all'utente
                //htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.err_idSpecieSiap", "Selezionare la specie SIAP");
                
                htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.err_idSpecieSiap", MessageFormat.format(htmlStringKO, new Object[]
                { pathErrori + "/" + imko, "'" + jssp.process(erroreSpecieSiap) + "'", erroreSpecieSiap }), null);
                
              else htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.err_idSpecieSiap",
                                         MessageFormat.format(htmlStringOK,
                                           new Object[] {
                                           pathErrori + imok,""}),
                                           null);
            }
            
          }
          
          
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.indirizzo", sianAllevamentiVO.getIndirizzo());
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.cap", sianAllevamentiVO.getCap());
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.localita", sianAllevamentiVO.getLocalita());
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.comune", sianAllevamentiVO.getComune());
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.denominazione", sianAllevamentiVO.getDenominazione());
          if(Validator.isNotEmpty(sianAllevamentiVO.getCapiTotali())) {
            htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.numero", sianAllevamentiVO.getCapiTotali().toString());
          }
  
          if(Validator.isNotEmpty(sianAllevamentiVO.getDataCalcoloCapi())) {
            htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.dataCalcoloCapi", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDataCalcoloCapi()));
          }
  
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.tipoProduzione", sianAllevamentiVO.getDescTipoProduzione());
          htmpl.set("blkSiAllevamenti.blkElencoAllevamenti.orientamentoProduttivo", sianAllevamentiVO.getDescOrientamentoProduttivo() );
          i++;
        }
      }
      
      
      if(request.getAttribute(SolmrConstants.MODIFICA_BDN) != null)
      {
        htmpl.newBlock("blkAnagrafeZootecnicaButtons");
      }
      
      
    }
  }

  // SEZIONE ERRORI GENERICI DA IMPORTA DATI
  if(errors != null && errors.size() > 0) {
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }

%>
<%= htmpl.text()%>

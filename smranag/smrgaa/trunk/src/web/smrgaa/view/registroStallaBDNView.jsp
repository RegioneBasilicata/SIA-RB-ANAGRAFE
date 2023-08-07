<%@page language="java" contentType="text/html" isErrorPage="true"%>

<%@page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.teramo.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/registroStallaBDN.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
  ElencoRegistroDiStallaVO elencoRegistroStalla = (ElencoRegistroDiStallaVO) session.getAttribute("elencoRegistroStalla");
  String descrizioneSpecie=(String)session.getAttribute("descrizioneSpecie");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String messaggioErrore = (String) request.getAttribute("messaggioErrore");
  if (Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", messaggioErrore);
  }
  else
  {
    if (elencoRegistroStalla!=null)
    {
       if (elencoRegistroStalla.getCodErrore()!=null && !"".equals(elencoRegistroStalla.getCodErrore()))
       {
         //Anche se ci sono errori valorizzo comunque la testata
         htmpl.newBlock("blkHeaderStalla");
         htmpl.set("blkHeaderStalla.codiceAziendaZootecnica",(String)request.getAttribute("codiceAziendaZoo"));
         htmpl.set("blkHeaderStalla.descrizioneSpecie",descrizioneSpecie);

         setIndirizzoView(htmpl,(String)request.getAttribute("indirizzoAll"),
             (String)request.getAttribute("capAll"), (String)request.getAttribute("comuneAll"));

         //htmpl.set("blkHeaderStalla.indirizzo",(String)request.getAttribute("indirizzoAll"));
         //htmpl.set("blkHeaderStalla.cap",(String)request.getAttribute("capAll"));
         //htmpl.set("blkHeaderStalla.localita",(String)request.getAttribute("comuneAll"));
         //Teramo ci ha risposto con un'errore che deve essere comunicato all'utente
         
         htmpl.newBlock("blkErrore");
        // htmpl.set("blkErrore.messaggio", it.csi.solmr.etc.anag.AnagErrors.ERR_SERVIZIO_BDN+elencoRegistroStalla.getDescErrore()+" ("+elencoRegistroStalla.getCodErrore()+")");
         SolmrLogger.error(this," -- SIAN descErrore = "+elencoRegistroStalla.getDescErrore()+" - codErrore ="+elencoRegistroStalla.getCodErrore());
         htmpl.set("blkErrore.messaggio", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
       }
       else
       {
         //Tutto ok, procedo quindi con la visualizzazione degli allevamenti
         if (elencoRegistroStalla.getRegistroDiStalla()!=null && elencoRegistroStalla.getRegistroDiStalla().length>0)
         {
           htmpl.newBlock("blkHeaderStalla");
           htmpl.set("blkHeaderStalla.codiceAziendaZootecnica",elencoRegistroStalla.getRegistroDiStalla()[0].getAziendaCodice());
           htmpl.set("blkHeaderStalla.descrizioneSpecie",descrizioneSpecie);

           setIndirizzoView(htmpl,elencoRegistroStalla.getRegistroDiStalla()[0].getIndirizzo(),
             elencoRegistroStalla.getRegistroDiStalla()[0].getCap(),
             elencoRegistroStalla.getRegistroDiStalla()[0].getLocalita());

           //htmpl.set("blkHeaderStalla.indirizzo",elencoRegistroStalla.getRegistroDiStalla()[0].getIndirizzo());
           //htmpl.set("blkHeaderStalla.cap",elencoRegistroStalla.getRegistroDiStalla()[0].getCap());
           //htmpl.set("blkHeaderStalla.localita",elencoRegistroStalla.getRegistroDiStalla()[0].getLocalita());

           htmpl.newBlock("blkRegistroStalla");
           htmpl.set("blkRegistroStalla.numAllevamenti",""+elencoRegistroStalla.getRegistroDiStalla().length);
           for (int i=0;i<elencoRegistroStalla.getRegistroDiStalla().length;i++)
           {
             htmpl.newBlock("blkRegistroStalla.blkAllevamenti");
             htmpl.set("blkRegistroStalla.blkAllevamenti.proprietario",elencoRegistroStalla.getRegistroDiStalla()[i].getPropCognNome());
             htmpl.set("blkRegistroStalla.blkAllevamenti.detentore",elencoRegistroStalla.getRegistroDiStalla()[i].getDetenCognNome());
             htmpl.set("blkRegistroStalla.blkAllevamenti.marchio",elencoRegistroStalla.getRegistroDiStalla()[i].getMarchio());
             htmpl.set("blkRegistroStalla.blkAllevamenti.tag",elencoRegistroStalla.getRegistroDiStalla()[i].getTag());
             htmpl.set("blkRegistroStalla.blkAllevamenti.razza",elencoRegistroStalla.getRegistroDiStalla()[i].getRazza());
             htmpl.set("blkRegistroStalla.blkAllevamenti.sesso",elencoRegistroStalla.getRegistroDiStalla()[i].getSesso());
             htmpl.set("blkRegistroStalla.blkAllevamenti.madre",elencoRegistroStalla.getRegistroDiStalla()[i].getCodiceMadre());
             htmpl.set("blkRegistroStalla.blkAllevamenti.dataNascita",elencoRegistroStalla.getRegistroDiStalla()[i].getDtNascita());
             htmpl.set("blkRegistroStalla.blkAllevamenti.dataIngresso",elencoRegistroStalla.getRegistroDiStalla()[i].getDtIngresso());
             htmpl.set("blkRegistroStalla.blkAllevamenti.causaleIngresso",elencoRegistroStalla.getRegistroDiStalla()[i].getCaricoNascita());
             htmpl.set("blkRegistroStalla.blkAllevamenti.provenienza",elencoRegistroStalla.getRegistroDiStalla()[i].getProvenienza());
             htmpl.set("blkRegistroStalla.blkAllevamenti.dataUscita",elencoRegistroStalla.getRegistroDiStalla()[i].getDataMorteVendita());
             htmpl.set("blkRegistroStalla.blkAllevamenti.causaleUscita",elencoRegistroStalla.getRegistroDiStalla()[i].getScaricoMorte());
             htmpl.set("blkRegistroStalla.blkAllevamenti.destinazione",elencoRegistroStalla.getRegistroDiStalla()[i].getDestinazione());
             htmpl.set("blkRegistroStalla.blkAllevamenti.codPrecedente",elencoRegistroStalla.getRegistroDiStalla()[i].getCodPrecedente());
             htmpl.set("blkRegistroStalla.blkAllevamenti.modello4",elencoRegistroStalla.getRegistroDiStalla()[i].getEstremiModello4());
           }
         }
         else
         {
           //Anche se ci sono errori valorizzo comunque la testata
           htmpl.newBlock("blkHeaderStalla");
           htmpl.set("blkHeaderStalla.codiceAziendaZootecnica",(String)request.getAttribute("codiceAziendaZoo"));
           htmpl.set("blkHeaderStalla.descrizioneSpecie",descrizioneSpecie);

           setIndirizzoView(htmpl,(String)request.getAttribute("indirizzoAll"),
             (String)request.getAttribute("capAll"), (String)request.getAttribute("comuneAll"));

           //htmpl.set("blkHeaderStalla.indirizzo",(String)request.getAttribute("indirizzoAll"));
           //htmpl.set("blkHeaderStalla.cap",(String)request.getAttribute("capAll"));
           //htmpl.set("blkHeaderStalla.localita",(String)request.getAttribute("comuneAll"));

           //Teramo ci ha risposto con un'errore che deve essere comunicato all'utente
           htmpl.newBlock("blkErrore");
           htmpl.set("blkErrore.messaggio", AnagErrors.ERR_BDN_NO_REGISTRO_STALLA);
         }
       }
    }
  }


  // SEZIONE ERRORI GENERICI DA IMPORTA DATI
  if(errors != null && errors.size() > 0) {
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }

%>
<%= htmpl.text()%>


<%!
    private void setIndirizzoView(Htmpl htmpl, String indirizzo, String cap, String localita)
    {
      String indirizzoVw = "";

      if(Validator.isNotEmpty(indirizzo))
      {
        indirizzoVw = indirizzo;
      }

      if(Validator.isNotEmpty(cap))
      {
        if(Validator.isNotEmpty(indirizzo))
        {
          indirizzoVw += " - ";
        }
        indirizzoVw += cap;
      }

      if(Validator.isNotEmpty(localita))
      {
        if(Validator.isNotEmpty(indirizzo))
        {
          indirizzoVw += " - ";
        }
        indirizzoVw += localita;
      }

      htmpl.set("blkHeaderStalla.indirizzoVw",indirizzoVw);

    }

 %>

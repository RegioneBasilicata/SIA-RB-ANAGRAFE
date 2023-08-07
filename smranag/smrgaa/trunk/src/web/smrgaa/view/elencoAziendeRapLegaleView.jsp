<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.util.ValidationError" %>
<%@ page import="it.csi.solmr.util.ValidationErrors" %>

<%!
  public static final String LAYOUT="/layout/elencoAziendeRapLegale.htm";
%>

<%
  SolmrLogger.debug(this,"[elencoAziendeRapLegaleView:service] BEGIN.");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Vector vElencoAziende = (Vector) request.getAttribute("vElencoAziende");
  String errorAAEP = (String) request.getAttribute("errorAAEP");
  if (errorAAEP != null &&
      !errorAAEP.equals(""))
  {
    htmpl.newBlock("messaggioBlk");
    htmpl.set("messaggioBlk.messaggio",errorAAEP);
  }

  //Messaggi restituiti dal servizio
  String messaggi[]=(String[])request.getAttribute("messaggi");
  int length=messaggi==null?0:messaggi.length;
  for(int i=0;i<length;i++)
  {
    htmpl.newBlock("messaggioBlk");
    htmpl.set("messaggioBlk.messaggio",messaggi[i]);
  }

  if (vElencoAziende !=null &&
      vElencoAziende.size() >0) {
    //elenco aziende

    htmpl.newBlock("dataBlk");
    htmpl.set("dataBlk.numAziende",vElencoAziende.size()+"");

    for (int i = 0; i < vElencoAziende.size(); i++)
    {
      AnagAziendaVO  anagAziendaVO = (AnagAziendaVO) vElencoAziende.get(i);

      htmpl.newBlock("dataBlk.radioElencoAziende");
      if (i==0)
      {
        //di default prima azienda selezionata
        htmpl.set("dataBlk.radioElencoAziende.checked","checked");
      }

      htmpl.set("dataBlk.radioElencoAziende.idAzienda",anagAziendaVO.getIdAzienda()!=null?""+anagAziendaVO.getIdAzienda():"");
      htmpl.set("dataBlk.radioElencoAziende.CUAA",anagAziendaVO.getCUAA()!=null?""+anagAziendaVO.getCUAA():"");
      htmpl.set("dataBlk.radioElencoAziende.denominazione",anagAziendaVO.getDenominazione()!=null?""+anagAziendaVO.getDenominazione():"");
      htmpl.set("dataBlk.radioElencoAziende.partitaIvaAzienda",anagAziendaVO.getPartitaIVA()!=null?""+anagAziendaVO.getPartitaIVA():"");

      String sede=null;
      if(anagAziendaVO.getDescComune()!= null && !anagAziendaVO.getDescComune().equals(""))
        sede=anagAziendaVO.getDescComune();
      else if(anagAziendaVO.getSedelegCittaEstero()!=null&&!anagAziendaVO.getSedelegCittaEstero().equals(""))
        sede=anagAziendaVO.getSedelegCittaEstero();
      String indirizzo=anagAziendaVO.getSedelegIndirizzo();

      if (sede==null) sede="";
      if (indirizzo==null) indirizzo="";
      htmpl.set("dataBlk.radioElencoAziende.sede",sede+" - "+indirizzo);
    }
  }

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  it.csi.solmr.util.HtmplUtil.setErrors(htmpl, errors, request, application);

  out.print(htmpl.text());
%>

  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/elencoPersone.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  int totalePagine;
  int pagCorrente;
  Integer currPage;

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Vector vectIdPF = (Vector)session.getAttribute("listIdPF");
  Vector vectRange = (Vector)session.getAttribute("listRange");

  if(session.getAttribute("currPage")==null)
    pagCorrente=1;
  else
    pagCorrente = ((Integer)session.getAttribute("currPage")).intValue();
  if(vectIdPF!=null){
    totalePagine=vectIdPF.size()/SolmrConstants.NUM_MAX_ROWS_PAG;
    int resto = vectIdPF.size()%SolmrConstants.NUM_MAX_ROWS_PAG;
    if(resto!=0)
      totalePagine+=1;
    htmpl.set("currPage",""+pagCorrente);
    htmpl.set("totPage",""+totalePagine);
    htmpl.set("numeroRecord",""+vectIdPF.size());
    currPage = new Integer(pagCorrente);
    session.setAttribute("currPage",currPage);

    if(pagCorrente>1)
      htmpl.newBlock("bottoneIndietro");
    if(pagCorrente<totalePagine)
      htmpl.newBlock("bottoneAvanti");

  }

  if(vectRange!=null && vectRange.size()>0){
    for(int i=0; i<vectRange.size();i++){
      PersonaFisicaVO pfVO = (PersonaFisicaVO)vectRange.elementAt(i);
      htmpl.newBlock("rigaPersone");
      htmpl.set("rigaPersone.idPersonaFisica",""+pfVO.getIdPersonaFisica());
      htmpl.set("rigaPersone.idSoggetto",""+pfVO.getIdSoggetto());
      htmpl.set("rigaPersone.cognomeNome",pfVO.getCognome()+" - "+pfVO.getNome());
      htmpl.set("rigaPersone.codFiscale",pfVO.getCodiceFiscale());
      if(pfVO.getNascitaData() != null && !pfVO.getNascitaData().equals("")) {
        htmpl.set("rigaPersone.dataNascita",DateUtils.formatDate(pfVO.getNascitaData()));
      }
      if(pfVO.getDescNascitaComune() != null && !pfVO.getDescNascitaComune().equals("")) {
        htmpl.set("rigaPersone.comuneNascita",pfVO.getDescNascitaComune());
      }
      if(pfVO.getNascitaCittaEstero() != null && !pfVO.getNascitaCittaEstero().equals("")) {
        htmpl.set("rigaPersone.cittaNascita","("+pfVO.getNascitaCittaEstero()+")");
      }
      htmpl.set("rigaPersone.residenza",pfVO.getDescResComune());
      if(pfVO.getResCittaEstero() != null && !pfVO.getResCittaEstero().equals("")) {
        htmpl.set("rigaPersone.cittaResidenza",pfVO.getResCittaEstero());
      }
      htmpl.set("rigaPersone.indirizzo",pfVO.getResIndirizzo());
    }
  }
  HtmplUtil.setErrors(htmpl, (ValidationErrors) request.getAttribute("errors"), request, application);
%>
<%= htmpl.text()%>

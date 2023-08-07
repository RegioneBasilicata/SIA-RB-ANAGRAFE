<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "elencoPersoneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  //PersonaFisicaVO pfVO = null;

  int sizeResult = 0;
  String errorPage = "/view/ricercaPersonaView.jsp";
  String errorSelezionePage = "/view/elencoPersoneView.jsp";
  //String dettaglioURL = "/view/anagraficaView.jsp";
  String dettaglioURL = "/view/elencoPersoneView.jsp";
  String avantiIndietroURL = "/view/elencoPersoneView.jsp";

  int paginaCorrente = 0;
  Integer paginaCorrenteInteger;
  //String dataAvanzata="";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");

  Vector vectIdPF = (Vector)session.getAttribute("listIdPF");
  Vector rangePF = (Vector)session.getAttribute("listRange");

  Validator validator = null;
  ValidationErrors errors = new ValidationErrors();

  if(Validator.isNotEmpty(request.getParameter("operazione"))) {
    if(request.getParameter("operazione").equalsIgnoreCase("avanti")) {
      try {
        if(vectIdPF!=null){
          sizeResult = vectIdPF.size();
          paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
          if(paginaCorrenteInteger.toString().equals(request.getParameter("totalePagine")))
          paginaCorrente = paginaCorrenteInteger.intValue();
          else
          paginaCorrente = paginaCorrenteInteger.intValue()+1;
          Vector rangeIdPF=new Vector();
          int limiteA;
          if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
          else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
          for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++){
            rangeIdPF.addElement(vectIdPF.elementAt(i));
          }
          session.removeAttribute("listRange");
          rangePF = anagFacadeClient.getListPersoneFisicheByIdRange(rangeIdPF);
          session.setAttribute("listRange",rangePF);

          session.removeAttribute("currPage");
          paginaCorrenteInteger = new Integer(paginaCorrente);
          session.setAttribute("currPage",paginaCorrenteInteger);

        }
      }
      catch (SolmrException sex) {
        ValidationError error = new ValidationError(sex.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(errorPage).forward(request, response);
        return;
      }
      %>
      <jsp:forward page = "/view/elencoPersoneView.jsp" />
      <%
      //response.sendRedirect(request.getContextPath()+avantiIndietroURL);
    }
    else if(request.getParameter("operazione").equalsIgnoreCase("indietro")) {
      try {
        if(vectIdPF!=null){

          sizeResult = vectIdPF.size();
          paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
          if(paginaCorrenteInteger.toString().equals("1"))
          paginaCorrente = paginaCorrenteInteger.intValue();
          else
          paginaCorrente = paginaCorrenteInteger.intValue()-1;
          Vector rangeIdPF=new Vector();
          int limiteA;
          if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
          else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
          for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++){
            rangeIdPF.addElement(vectIdPF.elementAt(i));
          }
          session.removeAttribute("listRange");
          rangePF = anagFacadeClient.getListPersoneFisicheByIdRange(rangeIdPF);
          session.setAttribute("listRange",rangePF);

          session.removeAttribute("currPage");
          paginaCorrenteInteger = new Integer(paginaCorrente);
          session.setAttribute("currPage",paginaCorrenteInteger);

        }
      }
      catch (SolmrException sex) {
        ValidationError error = new ValidationError(sex.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(errorPage).forward(request, response);
        return;
      }
      %>
      <jsp:forward page = "/view/elencoPersoneView.jsp" />
      <%
      //response.sendRedirect(request.getContextPath()+avantiIndietroURL);
    }
  }
  //else if(request.getParameter("dettaglio") != null) {
  else if(request.getParameter("funzionalitaSelezionata") != null) {
    if(request.getParameter("idPersonaFisica")== null){
      ValidationError error = new ValidationError("Selezionare una persona");
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(avantiIndietroURL).forward(request, response);
      return;
    }
    else{
      session.removeAttribute("personaVO");
      if(rangePF!= null){
        for(int i=0;i<rangePF.size();i++){
          PersonaFisicaVO pfVO = (PersonaFisicaVO)rangePF.elementAt(i);
            if (utenteAbilitazioni.getRuolo().isUtenteIntermediario()){
              try{
                anagFacadeClient.utenteConDelega(utenteAbilitazioni, pfVO.getIdAzienda());
              }
              catch(SolmrException se)
              {
                ValidationError error = new ValidationError(AnagErrors.INTERMEDIARIO_SENZA_DELEGA);
                errors.add("error", error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(avantiIndietroURL).forward(request, response);
                return;
              }
            }
            session.setAttribute("personaVO",pfVO);
            break;
        }
	%>
        <jsp:forward page = "<%=dettaglioURL%>" />
        <%
      }
    }
  }
  %>
  <jsp:forward page = "<%=errorSelezionePage%>" />
  <%
%>

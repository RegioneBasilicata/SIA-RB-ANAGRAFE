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
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "elencoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  session.removeAttribute("personaVO");

  int sizeResult = 0;
  String errorPage = "/view/ricAnagAziendaView.jsp";
  String dettaglioURL = "/view/anagraficaView.jsp";
  String avantiIndietroURL = "/view/elencoView.jsp";
  String accessoAziendaURL = "/ctrl/accessoAziendaCtrl.jsp";

  int paginaCorrente = 0;
  Integer paginaCorrenteInteger;
  String dataAvanzata="";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  Vector vectIdAnagAzienda = (Vector)session.getAttribute("listIdAzienda");
  Vector rangeAnagAzienda = (Vector)session.getAttribute("listRange");

  ValidationErrors errors = new ValidationErrors();
  String aziendaSelezionata = request.getParameter("idAzienda");
  if(Validator.isNotEmpty(aziendaSelezionata)) {
    request.setAttribute("idAnagAziendaSelezionata", Long.decode(aziendaSelezionata));
  }


  if(request.getParameter("annulla") != null) {
    session.removeAttribute("anagAziendaVODelegaTemp");
    %>
       <jsp:forward page = "/view/elencoView.jsp" />
    <%
    return;
  }
  if(request.getParameter("conferma") != null) 
  {
    AnagAziendaVO anagAziendaVO= (AnagAziendaVO)session.getAttribute("anagAziendaVODelegaTemp");

    session.setAttribute("anagAziendaVO",anagAziendaVO);
    if (ruoloUtenza!=null)
    {
      boolean delegaDiretta=false;
      boolean padre=false;
      if (ruoloUtenza.isUtenteIntermediario())
      {
        delegaDiretta=anagFacadeClient.isIntermediarioConDelegaDiretta(
          utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario(),anagAziendaVO.getIdAzienda());
        padre=anagFacadeClient.isIntermediarioPadre(
          utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario(),anagAziendaVO.getIdAzienda());
      }
      ruoloUtenza.setIsIntermediarioConDelega(delegaDiretta);
      ruoloUtenza.setIsIntermediarioPadre(padre);
      session.setAttribute("ruoloUtenza",ruoloUtenza);
    }
    session.removeAttribute("anagAziendaVODelegaTemp");
    iridePageName = "registrazioneDelegaCtrl.jsp";
    %><%@include file = "/include/autorizzazione.inc" %><%
    %>
       <jsp:forward page = "/view/registrazioneDelegaView.jsp" />
    <%
    return;
  }
  if(Validator.isNotEmpty(request.getParameter("operazione"))) 
  {
    if(request.getParameter("operazione").equalsIgnoreCase("avanti")) 
    {
      try 
      {
        if(vectIdAnagAzienda!=null)
        {
          if(rangeAnagAzienda!=null && rangeAnagAzienda.size()>0)
          {
            dataAvanzata = ((AnagAziendaVO)rangeAnagAzienda.elementAt(0)).getDataSituazioneAlStr();
          }
          sizeResult = vectIdAnagAzienda.size();
          paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
          if(paginaCorrenteInteger.toString().equals(request.getParameter("totalePagine")))
          paginaCorrente = paginaCorrenteInteger.intValue();
          else
          paginaCorrente = paginaCorrenteInteger.intValue()+1;
          Vector rangeIdAA=new Vector();
          int limiteA;
          if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
          else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
          for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++){
            rangeIdAA.addElement(vectIdAnagAzienda.elementAt(i));
          }
          session.removeAttribute("listRange");
          rangeAnagAzienda = anagFacadeClient.getListAziendeByIdRange(rangeIdAA);
          session.setAttribute("listRange",rangeAnagAzienda);

          for(int j=0; j<rangeAnagAzienda.size(); j++){
            ((AnagAziendaVO)rangeAnagAzienda.elementAt(j)).setDataSituazioneAlStr(dataAvanzata);
          }

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
        <jsp:forward page = "/view/elencoView.jsp" />
      <%
    }
    else 
    {
      try 
      {
        if(vectIdAnagAzienda!=null)
        {
          if(rangeAnagAzienda!=null && rangeAnagAzienda.size()>0)
          {
            dataAvanzata = ((AnagAziendaVO)rangeAnagAzienda.elementAt(0)).getDataSituazioneAlStr();
          }
          sizeResult = vectIdAnagAzienda.size();
          paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
          if(paginaCorrenteInteger.toString().equals("1"))
            paginaCorrente = paginaCorrenteInteger.intValue();
          else
            paginaCorrente = paginaCorrenteInteger.intValue()-1;
          Vector rangeIdAA=new Vector();
          int limiteA;
          if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
            limiteA=sizeResult;
          else
            limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
          for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
            i<limiteA;i++)
          {
            rangeIdAA.addElement(vectIdAnagAzienda.elementAt(i));
          }
          session.removeAttribute("listRange");
          rangeAnagAzienda = anagFacadeClient.getListAziendeByIdRange(rangeIdAA);
          session.setAttribute("listRange",rangeAnagAzienda);

          for(int j=0; j<rangeAnagAzienda.size(); j++)
          {
            ((AnagAziendaVO)rangeAnagAzienda.elementAt(j)).setDataSituazioneAlStr(dataAvanzata);
          }

          session.removeAttribute("currPage");
          paginaCorrenteInteger = new Integer(paginaCorrente);
          session.setAttribute("currPage",paginaCorrenteInteger);

        }
      }
      catch (SolmrException sex) 
      {
        ValidationError error = new ValidationError(sex.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(errorPage).forward(request, response);
        return;
      }
      %>
         <jsp:forward page = "/view/elencoView.jsp" />
      <%
    }
  }
  else if(request.getParameter("funzionalitaSelezionata") != null) 
  {
    if(request.getParameter("idAzienda")== null)
    {
      ValidationError error = new ValidationError("Selezionare un''azienda");
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(avantiIndietroURL).forward(request, response);
      return;
    }
    else
    {
      session.removeAttribute("anagAziendaVO");
      if(rangeAnagAzienda!= null)
      {
        for(int i=0;i<rangeAnagAzienda.size();i++)
        {
          AnagAziendaVO anagAziendaVO = (AnagAziendaVO)rangeAnagAzienda.elementAt(i);
          if(anagAziendaVO.getIdAnagAzienda().toString().equals(request.getParameter("idAzienda")))
          {
            //Passo questo parametro per indicare che arrivo dal dettaglio ricerca
            //per avitare problemi ruolo/iride col mandato (dopo introduzione caa_limitato elenco soci ndr)  
            request.setAttribute("RICERCAJSP","RICERCAJSP");
            request.setAttribute("idAziendaAccesso",anagAziendaVO.getIdAzienda().toString());
            %>
              <jsp:forward page = "<%=accessoAziendaURL%>" />
            <%
            return;          
          }
        }
        
        /***
         *  Dato che questo controller lavora  con diverse view (appartenenti
         * a macroCU diversi sono necessarie più include)
         **/
        iridePageName = "anagraficaCtrl.jsp";
        %><%@include file = "/include/autorizzazione.inc" %><%
        %>
        <jsp:forward page = "<%=dettaglioURL%>" />
        <%
        
      }
    }
  }


%>

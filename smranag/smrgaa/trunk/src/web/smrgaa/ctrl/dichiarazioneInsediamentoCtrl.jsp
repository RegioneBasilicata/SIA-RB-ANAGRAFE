<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!  
  public static String DICHIARAZIONE_INSEDIAMENTO_URL="/view/dichiarazioneInsediamentoView.jsp";
  public static String DICHIARAZIONE_INSEDIAMENTO_CONFERMA_URL="/view/confermaDichiarazioneInsediamentoView.jsp";
%>

<%

  //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
  ResourceBundle res = ResourceBundle.getBundle("config");
  String ambienteDeploy = res.getString("ambienteDeploy");
  SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
  String ANAGRAFICA_URL ="";
  if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
	  ANAGRAFICA_URL = "../layout/";
  else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
	  ANAGRAFICA_URL = "/layout/";
  ANAGRAFICA_URL += "anagrafica.htm";


  String iridePageName = "dichiarazioneInsediamentoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  String url=DICHIARAZIONE_INSEDIAMENTO_URL;
  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

    AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

    if ("conferma".equals(request.getParameter("insediamento")))
    {
      if (((String)SolmrConstants.get("P_ESITO_CONTR")).equals((String)session.getAttribute("common")))
      {
         //non posso proseguire
         throw new Exception("Non è un errore: è un tentativo di chiamare la pagina "+
                             " dichiarazioneInsediamentoCtrl.jsp, simulando la pressione "+
                             "del pulsante conferma, in presenza di anomalie bloccanti");
      }
      else
      {
        session.removeAttribute("common");
        anagFacadeClient.updateInsediamentoGiovani(anagVO.getIdAzienda());
        anagVO.setFlagAziendaProvvisoria(false);
        session.setAttribute("anagAziendaVO",anagVO);
        url=ANAGRAFICA_URL;
      }
    }
    else
    {
      if ("conferma".equals(request.getParameter("conferma")))
      {
        //Arrivo a questa pagina perchè è stato premuto il pulsante conferma
        //sulla pagina insediamento giovani

        if (((String)SolmrConstants.get("P_ESITO_CONTR")).equals((String)session.getAttribute("common")))
        {
          //non posso proseguire
          throw new Exception("Non è un errore: è un tentativo di chiamare la pagina "+
                              " dichiarazioneInsediamentoCtrl.jsp, simulando la pressione "+
                              "del pulsante conferma, in presenza di anomalie bloccanti");
        }
        else
        {
          url=DICHIARAZIONE_INSEDIAMENTO_CONFERMA_URL;
        }
      }
      else
      {
        //Sono arrivato a questa pagina perchè è stata richiamata dal menu
        url=DICHIARAZIONE_INSEDIAMENTO_URL;
        
        //Recupero le eventuali anomalie
        Vector anomalie = (Vector)session.getAttribute("anomalieDichiarazione");


        request.setAttribute("anomalieDichiarazioniInsediamento", anomalie);
      }
    }
  }
  catch(SolmrException s)
  {
    request.setAttribute("messaggioErrore",s.getMessage());
    s.printStackTrace();
  }
  catch(Exception e)
  {
    request.setAttribute("messaggioErrore",(String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION"));
    e.printStackTrace();
  }
  %><jsp:forward page="<%= url %>"/><%
%>


<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>


<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%!
  private static final String URL_ANAGRAFICA_VISURA_CAMERE_CONTITOLARI_VIEW = "/view/contitolariAAEPView.jsp";
  private static final String URL_ERRORE = "/view/erroreView.jsp";
  private static final String URL_PAGE_BACK = "../layout/contitolari.htm";
  private static final String URL_PAGE_BACK_ERRORE = "../layout/contitolari.htm";
  private static final String ACTION = "../layout/contitolariAAEP.htm";
  private static final String URL_ATTENDERE_PREGO = "/view/attenderePregoView.jsp";
%>

<%

  String iridePageName = "contitolariAAEPImportCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

    Azienda impresaInfoc = null;
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

    String operazione = request.getParameter("operazione");

    // L'utente ha selezionato la voce di menù AAEP e io lo
    // mando alla pagina di attesa per il caricamento dati
    if("attenderePrego".equalsIgnoreCase(operazione)) {
      request.setAttribute("action", ACTION);
      operazione = null;
      request.setAttribute("operazione", operazione);
      %>
        <jsp:forward page= "<%= URL_ATTENDERE_PREGO %>" />
      <%
    }

    if("importaDati".equals(request.getParameter("importaDati")))
    {
      anagFacadeClient.importaSoggCollAAEP(anagAziendaVO,
                                           (Azienda)session.getAttribute("impresaInfoc"),
                                           (HashMap)session.getAttribute("listaPersone"),
                                           request.getParameterValues("soggetti"),
                                           ruoloUtenza.getIdUtente());
      session.removeAttribute("listaPersone");
      session.removeAttribute("impresaInfoc");
      response.sendRedirect(URL_PAGE_BACK);
      return;
    }


    try
    {
      impresaInfoc = anagFacadeClient.cercaPerCodiceFiscale(anagAziendaVO.getCUAA());

      /* Se viene restituito null segnalo che non è stata trovata nessuna anagrafica
         Non dovrebbe accadere perchè AAEP se non trova niente dovrebbe rilanciare un
         eccezione.
      */
      if (impresaInfoc==null) throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));

      List<ListaPersoneRI> listaPersonaRIInfoc=impresaInfoc.getListaPersoneRI();

      if (listaPersonaRIInfoc!=null)
      {
        TreeMap listaPers = new TreeMap();

        /***
         * Ordino per codice fiscale. Aggiungere il valore di i al codice fiscale prima
         * di fare la put mi permette di gestire anche codici fiscali duplicati
         */
        /*for(int i=0;i<listaPersonaRIInfoc.size();i++)
          listaPers.put(listaPersonaRIInfoc.get(i).getCodiceFiscale().getValue()+i,listaPersonaRIInfoc.get(i));

        listaPersonaRIInfoc=(ListaPersoneRIInfoc[])listaPers.values().toArray(new ListaPersoneRIInfoc[0]);

        impresaInfoc.setListaPersoneRIInfoc(listaPersonaRIInfoc);*/

        HashMap listaPersone=new HashMap();
        for(int i=0;i<listaPersonaRIInfoc.size();i++)
        {
          PersonaRIInfoc personaRIInfoc=anagFacadeClient.cercaPuntualePersona(impresaInfoc.getCodiceFiscale().getValue(), listaPersonaRIInfoc.get(i).getProgrPersona().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
        		  
          Object pers[]=new Object[2];
          pers[0]=personaRIInfoc;
          pers[1]=new Boolean(anagFacadeClient.isRuolosuAnagrafe(personaRIInfoc.getCodiceFiscale().getValue(),anagAziendaVO.getIdAzienda().longValue()));
          listaPersone.put(listaPersonaRIInfoc.get(i).getProgrPersona().getValue(),pers);
        }
        session.setAttribute("listaPersone",listaPersone);
      }
      session.setAttribute("impresaInfoc",impresaInfoc);
    }
    catch(SolmrException sx) {
      request.setAttribute("errore",sx.getMessage());
      request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
      %>
         <jsp:forward page="<%= URL_ERRORE %>"/>
      <%
      return;
    }
  }
  catch(SolmrException se)
  {
    request.setAttribute("errore",se.getMessage());
    request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
    %>
       <jsp:forward page="<%= URL_ERRORE %>"/>
    <%
    return;
  }
  catch(Exception e)
  {
    request.setAttribute("errore",e.getMessage());
    request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
    %>
       <jsp:forward page="<%= URL_ERRORE %>"/>
    <%
    return;
  }
%>
<jsp:forward page="<%= URL_ANAGRAFICA_VISURA_CAMERE_CONTITOLARI_VIEW %>"/>

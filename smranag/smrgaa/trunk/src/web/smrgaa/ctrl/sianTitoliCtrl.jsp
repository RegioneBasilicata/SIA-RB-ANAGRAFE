<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.dto.anag.PersonaFisicaVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "sianTitoliCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String sianTitoliUrl = "/view/sianTitoliView.jsp";
  String action = "../layout/titoli.htm";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String messaggioErrore = null;
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");

   // L'utente ha selezionato la voce di menù titoli e io lo mando alla
   // pagina di attesa per il caricamento dati
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {
    request.setAttribute("action", action);
    operazione = null;
    request.setAttribute("operazione", operazione);
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }
  else 
  {
    // Se ha un CUAA valido cerco nell'anagrafe tributaria l'azienda
    // corrispondente
    Vector<SianTitoliAggregatiVO> elencoSianTitoliAggregatiVO = null;
    try 
    {
      elencoSianTitoliAggregatiVO = anagFacadeClient
        .titoliProduttoreAggregati(anagAziendaVO.getCUAA(), null, ProfileUtils.getSianUtente(ruoloUtenza));
      request.setAttribute("elencoSianTitoliAggregatiVO", elencoSianTitoliAggregatiVO);
    }
    catch(SolmrException se) 
    {
      messaggioErrore = se.getMessage();
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= sianTitoliUrl %>"/>
      <%
    }

    // Se ho reperito dei titoli per l'azienda in questione devo
    // recuperare le codifiche dei codici che mi arrivano dal SIAN
    // dal momento che quest'ultimo non le restituisce.
    Vector<String> elencoCodiciOrigine = new Vector<String>();
    Vector<String> elencoCodiciMovimento = new Vector<String>();
    for(int i = 0; i < elencoSianTitoliAggregatiVO.size(); i++) 
    {
      SianTitoliAggregatiVO sianTitoliAggregatiVO = (SianTitoliAggregatiVO)elencoSianTitoliAggregatiVO.elementAt(i);
      elencoCodiciOrigine.add(sianTitoliAggregatiVO.getCodiceOrigine());
      elencoCodiciMovimento.add(sianTitoliAggregatiVO.getCodiceMovimento());
    }

    Vector<String> newElencoCodiciOrigine = StringUtils.getElencoCodiciForLegendaSIAN(elencoCodiciOrigine);
    Vector<StringcodeDescription> elencoDescOrigineTitolo = new Vector<StringcodeDescription>();
    String descOrigineTitolo = null;
    try 
    {
      for(int i = 0; i < newElencoCodiciOrigine.size(); i++) 
      {
        descOrigineTitolo = anagFacadeClient.getDescriptionSIANFromCode((String)SolmrConstants.get("TAB_SIAN_TIPO_ORIGINE_TITOLO"),
          (String)newElencoCodiciOrigine.elementAt(i));

        StringcodeDescription stringCode = new StringcodeDescription((String)newElencoCodiciOrigine.elementAt(i),descOrigineTitolo);
        elencoDescOrigineTitolo.add(stringCode);
      }
    }
    catch(SolmrException se) 
    {
      messaggioErrore = se.getMessage();
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= sianTitoliUrl %>"/>
      <%
    }
    request.setAttribute("elencoDescOrigineTitolo", elencoDescOrigineTitolo);

    Vector<String> newElencoCodiciMovimento = StringUtils.getElencoCodiciForLegendaSIAN(elencoCodiciMovimento);
    Vector<StringcodeDescription> elencoDescMovimento = new Vector<StringcodeDescription>();
    String descMovimento = null;
    try 
    {
      for(int i = 0; i < newElencoCodiciMovimento.size(); i++) 
      {
        descMovimento = anagFacadeClient.getDescriptionSIANFromCode((String)SolmrConstants.get("TAB_SIAN_TIPO_MOVIMENTO_TITOLO"),
          (String)newElencoCodiciMovimento.elementAt(i));

        StringcodeDescription stringCode = new StringcodeDescription((String)newElencoCodiciMovimento.elementAt(i),descMovimento);
        elencoDescMovimento.add(stringCode);
      }
    }
    catch(SolmrException se) 
    {
      messaggioErrore = se.getMessage();
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= sianTitoliUrl %>"/>
      <%
    }
    request.setAttribute("elencoDescMovimento", elencoDescMovimento);

    %>
      <jsp:forward page="<%= sianTitoliUrl %>"/>
    <%
  }
%>


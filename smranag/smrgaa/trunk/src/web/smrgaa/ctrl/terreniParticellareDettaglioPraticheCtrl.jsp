<%@ page language="java" contentType="text/html" isErrorPage="true"%>
<%@page import="it.csi.smranag.smrgaa.exception.ErrorPageException"%>
<%@page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@page import="it.csi.solmr.dto.anag.AnagAziendaVO"%>
<%@page import="it.csi.solmr.util.Validator"%>
<%@page import="java.util.TreeMap"%>
<%@page import="it.csi.smranag.smrgaa.util.ReverseComparator"%>
<%@page import="it.csi.smranag.smrgaa.dto.RigaPraticaParticellaVO"%>
<%@page
	import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>
<%@page
	import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@page import="java.util.HashMap"%>
<%@page
	import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PPUtilizzoVO"%>
<%@page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.csi.smranag.smrgaa.util.CodeDescriptionUtils"%>
<%@page import="java.util.Arrays"%>
<%@page	import="it.csi.smranag.smrgaa.util.RigaPraticheParticellaComparator"%>
<%@page import="it.csi.smranag.smrgaa.dto.RitornoAgriservVO"%>
<%@page import="it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO"%>
<%@page import="it.csi.solmr.exception.SolmrException"%>
<%@page import="it.csi.solmr.etc.anag.AnagErrors"%>
<%@page import="it.csi.solmr.util.WebUtils" %>

<%!
  public static final String  VIEW                 = "/view/terreniParticellareDettaglioPraticheView.jsp";
  private static final String NOME_ORDINE_PER_CUAA = "cuaa";
  private static final String NOME_ORDINE_PER_ANNO = "anno";%>
<%
  String iridePageName = "terreniParticellareDettaglioPraticheCtrl.jsp";
%><%@include file="/include/autorizzazione.inc"%>
<%

  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  final String errMsg = "Impossibile procedere nella sezione terreni particellare. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  Long annoPratica = null;
  String annoPraticaStr = request.getParameter("annoPratica");
  String idConduzione = request.getParameter("idConduzione");
  String regimeDettaglioPratiche = request.getParameter("regimeDettaglioPratiche");
  //prima volta che entro resetto la sessione
  if(Validator.isEmpty(regimeDettaglioPratiche))
  {
    WebUtils.removeUselessFilter(session, "filtriParticellareRicercaVO");
  }
  
  try
  {
    annoPratica = new Long(annoPraticaStr);
  }
  catch (Exception e)
  {
    SolmrLogger.debug(this,
        "terreniParticellareDettaglioPratiche::service] richiamata pagina con annoPratica non valido. annoPratica="
            + annoPraticaStr);
  }
  if (Validator.isEmpty(annoPratica))
  {
    annoPratica = null;
  }
  else
  {
    if (session.getAttribute("TERRENI_PARTICELLARE_ANNI_PRATICHE") == null)
    {
      // Caso limite ==> l'utente ha premuto il pulsante back (del browser)
      // e sta tornando a questa pagina senza avere l'elenco degli anni in
      // sessione ==> per poter funzioare la ricerca perde il filtro dell'anno
      annoPratica = null;
    }
  }

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  // Carico i dati delle pratiche
  FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  
  if(filtriParticellareRicercaVO == null || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento())) 
  {
    String messaggio = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
  }
  
  StoricoParticellaVO storicoParticellaVO = null;
  try 
  {
    storicoParticellaVO = anagFacadeClient.getDettaglioParticella(filtriParticellareRicercaVO, new Long(idConduzione));
    
    long idParticella = storicoParticellaVO.getIdParticella().longValue();
    int tipologia = PraticaProcedimentoVO.FLAG_STATO_SOLO_ULTIMO;
    tipologia += PraticaProcedimentoVO.FLAG_STATO_ESCLUDI_BOZZA;
    tipologia += PraticaProcedimentoVO.FLAG_STATO_ESCLUDI_RESPINTO;
    tipologia += PraticaProcedimentoVO.FLAG_STATO_ESCLUDI_ANNULLATO;
    tipologia += PraticaProcedimentoVO.FLAG_STATO_ESCLUDI_ANNULLATO_PER_SOSTITUZIONE;
    
    Long idDichiarazioneConsistenza = null;
    if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() <= 0)
    {
      idDichiarazioneConsistenza = null;
    }
    else
    {
      idDichiarazioneConsistenza = filtriParticellareRicercaVO.getIdPianoRiferimento();
    }
    
    RitornoAgriservVO ritornoAgriservVO = gaaFacadeClient.searchPraticheProcedimento(idParticella,
      anagAziendaVO.getIdAzienda(), tipologia, idDichiarazioneConsistenza, null, annoPratica, 0);
    RigaPraticaParticellaVO praticheProcedimenti[] = (RigaPraticaParticellaVO[]) ritornoAgriservVO.getRighe();
    
    request.setAttribute("storicoParticellaVO", storicoParticellaVO);
    request.setAttribute("ritornoAgriservVO", ritornoAgriservVO);
    String anni[] = null;
    if (annoPratica == null)
    {
      anni = getAnniPratica(praticheProcedimenti);
      if (anni != null)
      {
        request.setAttribute("TERRENI_PARTICELLARE_ANNI_PRATICHE", anni);
        session.setAttribute("TERRENI_PARTICELLARE_ANNI_PRATICHE", anni);
      }
    }
    else
    {
      anni = (String[]) session.getAttribute("TERRENI_PARTICELLARE_ANNI_PRATICHE");
      if (anni != null)
      {
        request.setAttribute("TERRENI_PARTICELLARE_ANNI_PRATICHE", anni);
      }
    }
    TreeMap<String,String> tTipologiaPratica = null;
    if((session.getAttribute("tTipologiaPratica") == null)
      && Validator.isNotEmpty(praticheProcedimenti))
    {
      tTipologiaPratica = new TreeMap<String,String>();
      for(int i=0;i<praticheProcedimenti.length;i++)
      {
        String tipologiaPratica = praticheProcedimenti[i].getPraticaProcedimentoVO().getDescrizione();
        if(tTipologiaPratica.get(tipologiaPratica) == null)
        {
          tTipologiaPratica.put(tipologiaPratica,tipologiaPratica);
        }    
      }
      session.setAttribute("tTipologiaPratica", tTipologiaPratica); 
    }
    
    HashMap decodifiche = getMapUtilizzi(praticheProcedimenti, gaaFacadeClient);
    if (decodifiche == null)
    {
      decodifiche = new HashMap(); // Gestione uniforme nella view
    }
    ordinaPratiche(praticheProcedimenti, request);
    request.setAttribute("DECODIFICHE", decodifiche);
    
  }
  catch(SolmrException se) 
  {
    String messaggio = errMsg+"::"+AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
  }
  
%>
<jsp:forward page="<%=VIEW%>" />
<%!//
  private void ordinaPratiche(RigaPraticaParticellaVO praticheProcedimenti[], HttpServletRequest request)
  {
    if (praticheProcedimenti == null || praticheProcedimenti.length<=1)
    {
      return; // Nessun ordinamento se vettore null o di lunghezza 0 o 1
    }
    String ordine = request.getParameter("ordine");
    if (ordine == null)
    {
      ordine = "";
    }
    if (NOME_ORDINE_PER_CUAA.equalsIgnoreCase(ordine))
    {
      Arrays.sort(praticheProcedimenti, new RigaPraticheParticellaComparator(RigaPraticheParticellaComparator.COMPARE_CUAA,
          "true".equalsIgnoreCase(request.getParameter("ascendente"))));
    }
    else
    {
      if (NOME_ORDINE_PER_ANNO.equalsIgnoreCase(ordine))
      {
        Arrays.sort(praticheProcedimenti, new RigaPraticheParticellaComparator(
            RigaPraticheParticellaComparator.COMPARE_ANNO, "true".equalsIgnoreCase(request.getParameter("ascendente"))));
      }
      else
      {
        Arrays.sort(praticheProcedimenti, new RigaPraticheParticellaComparator(
            RigaPraticheParticellaComparator.COMPARE_DEFAULT, false));
      }
    }
  }

  //

  private String[] getAnniPratica(RigaPraticaParticellaVO pratiche[])
  {
    int len = pratiche == null ? 0 : pratiche.length;
    TreeMap mapAnni = new TreeMap(new ReverseComparator());
    for (int i = 0; i < len; ++i)
    {
      String annoPratica = String.valueOf(pratiche[i].getPraticaProcedimentoVO().getAnnoCampagna());
      mapAnni.put(annoPratica, annoPratica);
    }
    int size = mapAnni.size();
    if (size > 0)
    {
      return (String[]) mapAnni.values().toArray(new String[size]);
    }
    else
    {
      return null;
    }
  }

  private HashMap getMapUtilizzi(RigaPraticaParticellaVO righe[], GaaFacadeClient gaaFacadeClient) throws Exception
  {

    int len = righe == null ? 0 : righe.length;
    HashMap mapIds = new HashMap();
    for (int i = 0; i < len; ++i)
    {
      PPUtilizzoVO utilizzi[] = righe[i].getPraticaProcedimentoVO().getUtilizzi();
      int len2 = utilizzi == null ? 0 : utilizzi.length;
      for (int k = 0; k < len2; ++k)
      {
        Long id = utilizzi[k].getIdUtilizzo();
        if (id != null)
        {
          mapIds.put(id, id);
        }
      }
    }
    int size = mapIds.size();
    if (size == 0)
    {
      return null;
    }
    long ids[] = new long[size];
    Iterator iterator = mapIds.values().iterator();
    int i = 0;
    while (iterator.hasNext())
    {
      ids[i++] = ((Long) iterator.next()).longValue();
    }
    BaseCodeDescription decodifiche[] = gaaFacadeClient.baseDecodeUtilizzoByIdRange(ids);
    HashMap mapDecofiche = CodeDescriptionUtils.convertIntoHashMap(decodifiche, true);
    return mapDecofiche;
  }
%>

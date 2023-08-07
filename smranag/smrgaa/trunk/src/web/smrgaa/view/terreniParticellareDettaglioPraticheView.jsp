<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.smranag.smrgaa.dto.RigaPraticaParticellaVO"%>
<%@page import="java.util.HashMap"%>
<%@page	import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>
<%@page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@page	import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PPUtilizzoVO"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>
<%@page import="it.csi.smranag.smrgaa.dto.RitornoAgriservVO"%>
<%@page import="java.util.*"%>


<%!public static final String LAYOUT                  = "/layout/terreniParticellareDettaglioPratiche.htm";
  public static final String ORDINAMENTO_ASCENDENTE  = "ordine crescente";
  public static final String ORDINAMENTO_DISCENDENTE = "ordine decrescente";
  public static final String CLASSE_ORDINAMENTO_ASC  = "su";
  public static final String CLASSE_ORDINAMENTO_DESC = "giu";%>
<%
  SolmrLogger.debug(this, "[terreniPartcellareDettaglioPraticheView:service] BEGIN.");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
  {
    StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) request.getAttribute("storicoParticellaVO");
    // Dati di testata
    
    String titleGis = "GIS";
    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
    {          
      if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
        && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
          .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
      {
        titleGis += " - foglio stabilizzato.";
      }
      else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
         && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
           .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
      {
        titleGis += "- foglio in corso di stabilizzazione.";
      }
    }                 
    htmpl.set("controlliP", titleGis);
      
    String immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS;
    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
    {  
      if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
        && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
          .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
      {
        immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB;
      }
      else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
        && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
            .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
      {
        immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB_CORSO;
      }
    }          
    htmpl.set("immaginiControlliP", immaginiControlliP);
    
    
    
    
    htmpl.set("descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
    if (storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null
        && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()))
    {
      htmpl.set("siglaProvincia", "(" + storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()
          + ")");
    }
    if (Validator.isNotEmpty(storicoParticellaVO.getSezione()))
    {
      htmpl.set("sezione", storicoParticellaVO.getSezione());
    }
    htmpl.set("foglio", storicoParticellaVO.getFoglio());
    if (Validator.isNotEmpty(storicoParticellaVO.getParticella()))
    {
      htmpl.set("particella", storicoParticellaVO.getParticella());
    }
    if (Validator.isNotEmpty(storicoParticellaVO.getSubalterno()))
    {
      htmpl.set("subalterno", storicoParticellaVO.getSubalterno());
    }
    htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
  
    htmpl.set("idParticella", StringUtils.checkNull(storicoParticellaVO.getIdParticella()));
    String idStoricoParticella = StringUtils.checkNull(storicoParticellaVO.getIdStoricoParticella());
    htmpl.set("idStoricoParticella", idStoricoParticella);
    String idConduzione = request.getParameter("idConduzione");
    htmpl.set("idConduzione", idConduzione);
    RitornoAgriservVO ritornoAgriservVO = (RitornoAgriservVO) request.getAttribute("ritornoAgriservVO");
    TreeMap<String,String> tTipologiaPratica = (TreeMap<String,String>)session.getAttribute("tTipologiaPratica");
    TreeMap mapConduzioni = new TreeMap();
    boolean noPratiche = false;

    RigaPraticaParticellaVO praticheProcedimenti[] = (RigaPraticaParticellaVO[]) ritornoAgriservVO.getRighe();
    int len = praticheProcedimenti == null ? 0 : praticheProcedimenti.length;
    if (len == 0)
    {
      htmpl.newBlock("blkNoPratiche");
      noPratiche = true;
    }
    // Combo degli anni
    String anni[] = (String[]) request.getSession().getAttribute("TERRENI_PARTICELLARE_ANNI_PRATICHE");
    /*if (anni == null)
    {
      return;
    }*/
    String selected = request.getParameter("annoPratica");
    String selectedTipologiaPratica = request.getParameter("tipologiaPratica");
    int lenAnni = anni == null ? 0 : anni.length;
    if (lenAnni > 0)
    {
      htmpl.newBlock("blkCombo");
      for (int i = 0; i < lenAnni; ++i)
      {
        htmpl.newBlock("blkCombo.blkAnno.value");
        String anno = anni[i];
        htmpl.set("blkCombo.blkAnno.value", anno);
        htmpl.set("blkCombo.blkAnno.descrizione", anno);
        if (anno.equals(selected))
        {
          htmpl.set("blkCombo.blkAnno.selected", SolmrConstants.HTML_SELECTED, null);
        }
      }
      // Combo delle tipologie
      Set<String> keys = tTipologiaPratica.keySet();
      for (Iterator<String> i = keys.iterator(); i.hasNext();) 
      {
        htmpl.newBlock("blkCombo.blkTipologiaPratica");
        String tipologiaPratica = i.next();
        htmpl.set("blkCombo.blkTipologiaPratica.value", tipologiaPratica);
        htmpl.set("blkCombo.blkTipologiaPratica.descrizione", tipologiaPratica);
        if (tipologiaPratica.equals(selectedTipologiaPratica))
        {
          htmpl.set("blkCombo.blkTipologiaPratica.selected", SolmrConstants.HTML_SELECTED, null);
        }
        
      }
    
    }
    // Elenco
    boolean trovatoAlmenoUno = false;
    int numeroPratiche = 0;
    HashMap mapDecodifiche = (HashMap) request.getAttribute("DECODIFICHE");
    for (int i = 0; i < len; ++i)
    {
      RigaPraticaParticellaVO rigaVO = praticheProcedimenti[i];
      PraticaProcedimentoVO praticaVO = rigaVO.getPraticaProcedimentoVO();
      if(Validator.isNotEmpty(selectedTipologiaPratica))
      {
        if(!selectedTipologiaPratica.equalsIgnoreCase(praticaVO.getDescrizione()))
          continue;
      }
      //trovato un primo elemento
      if(!trovatoAlmenoUno)
      {
        htmpl.newBlock("blkElenco");
        trovatoAlmenoUno = true;
      }
      
      htmpl.newBlock("blkElenco.blkRiga");
      htmpl.set("blkElenco.blkRiga.tipologia", praticaVO.getDescrizione());
      htmpl.set("blkElenco.blkRiga.numeroPratica", praticaVO.getNumeroPratica());
      htmpl.set("blkElenco.blkRiga.annoPratica", String.valueOf(praticaVO.getAnnoCampagna()));
      htmpl.set("blkElenco.blkRiga.statoPratica", praticaVO.getDescrizioneStato());
      Date dataValiditaStato = praticaVO.getDataValiditaStato();
      if (dataValiditaStato != null)
      {
        htmpl.set("blkElenco.blkRiga.dataDal", DateUtils.formatDateTimeNotNull(dataValiditaStato));
      }
      String supCatastale = Formatter.formatDouble4(rigaVO.getSupCatastale());
      htmpl.set("blkElenco.blkRiga.supCatastale", supCatastale);
      addToMapConduzioni(mapConduzioni, rigaVO.getConduzioni());
      String descTitoloPossesso = getDescTitoloPossesso(rigaVO.getConduzioni());
      htmpl.set("blkElenco.blkRiga.descTitoloPossesso", descTitoloPossesso);
      PPUtilizzoVO usiSuolo[] = praticaVO.getUtilizzi();
      int lenSuolo = usiSuolo == null ? 0 : usiSuolo.length;
      for (int j = 0; j < lenSuolo; ++j)
      {
        PPUtilizzoVO usoVO = usiSuolo[j];
        String descUtilizzo = getDecodificaUtilizzo(mapDecodifiche, usoVO.getIdUtilizzo());
        String sup = Formatter.formatDouble4(usoVO.getSuperficie());
        if (j == 0)
        {
          htmpl.set("blkElenco.blkRiga.descUtilizzo", descUtilizzo);
          htmpl.set("blkElenco.blkRiga.supUtilizzo", sup);
          htmpl.set("blkElenco.blkRiga.causale", usoVO.getCausale());
        }
        else
        {
          htmpl.newBlock("blkElenco.blkRiga.blkUtilizzoRiga");
          htmpl.set("blkElenco.blkRiga.blkUtilizzoRiga.descUtilizzo", descUtilizzo);
          htmpl.set("blkElenco.blkRiga.blkUtilizzoRiga.supUtilizzo", sup);
          htmpl.set("blkElenco.blkRiga.blkUtilizzoRiga.causale", usoVO.getCausale());
        }
      }
      if (lenSuolo == 0)
      {
        lenSuolo = 1;
      }
      htmpl.set("blkElenco.blkRiga.rowSpan", String.valueOf(lenSuolo));
      
      
      numeroPratiche++;
    }
    
    if(trovatoAlmenoUno)
    {
      htmpl.set("blkElenco.numPratiche", ""+numeroPratiche);
      htmpl.set("blkElenco.legendaConduzione", getLegendaConduzioni(mapConduzioni));
      writeOrdinamentoCorrente(htmpl, request);
    }
    else
    {
      if(!noPratiche)
        htmpl.newBlock("blkNoPraticheFiltri");
    } 
    
    writeBlkErroriAgriserv(htmpl, ritornoAgriservVO.getErrori());
  }
%><%=htmpl.text()%>
<%
  SolmrLogger.debug(this, "[terreniParticellareDettaglioPraticheView:service] END.");
%><%!
  public void writeBlkErroriAgriserv(Htmpl htmpl, String errori[])
  {
    int len=errori==null?0:errori.length;
    if (len==0)
    {
      return;
    }
    StringBuffer sb=new StringBuffer();
    for(int i=0;i<len;++i)
    {
      if (i>0)
      {
        sb.append("<br/>");
      }
      sb.append(errori[i]);
    }
    if(len > 0)
    {
      htmpl.newBlock("blkAgriErrori");
      htmpl.set("blkAgriErrori.erroriAgriserv",sb.toString(),null);
    }
  }

  public void writeOrdinamentoCorrente(Htmpl htmpl, HttpServletRequest request)
  {
    String ordine = request.getParameter("ordine");
    if ("cuaa".equalsIgnoreCase(ordine))
    {
      // Ordinamento per cuaa
      writeOrdinamento(htmpl, "blkElenco.descOrdineCuaa", "blkElenco.ordineCuaaAsc", "blkElenco.classOrdineCuaa", !"false"
          .equals(request.getParameter("ascendente")));

      writeOrdinamento(htmpl, "blkElenco.descOrdineAnno", "blkElenco.ordineAnnoAsc", "blkElenco.classOrdineAnno", false);
    }
    else
    {
      if ("anno".equalsIgnoreCase(ordine))
      {
        // Ordinamento per anno
        writeOrdinamento(htmpl, "blkElenco.descOrdineAnno", "blkElenco.ordineAnnoAsc", "blkElenco.classOrdineAnno", !"false"
            .equals(request.getParameter("ascendente")));

        writeOrdinamento(htmpl, "blkElenco.descOrdineCuaa", "blkElenco.ordineCuaaAsc", "blkElenco.classOrdineCuaa", true);
      }
      else
      {
        // Ordinamento per anno
        writeOrdinamento(htmpl, "blkElenco.descOrdineAnno", "blkElenco.ordineAnnoAsc", "blkElenco.classOrdineAnno", false);

        writeOrdinamento(htmpl, "blkElenco.descOrdineCuaa", "blkElenco.ordineCuaaAsc", "blkElenco.classOrdineCuaa", true);
      }
    }
  }

  public void writeOrdinamento(Htmpl htmpl, String segnapostoDesc, String segnapostoName, String segnapostoClass, boolean asc)
  {
    htmpl.set(segnapostoDesc, asc ? ORDINAMENTO_ASCENDENTE : ORDINAMENTO_DISCENDENTE);
    htmpl.set(segnapostoName, asc ? "false" : "true");
    htmpl.set(segnapostoClass, asc ? CLASSE_ORDINAMENTO_ASC : CLASSE_ORDINAMENTO_DESC);
  }

  public String getDecodificaUtilizzo(HashMap map, Long idUtilizzo)
  {
    if (idUtilizzo == null)
    {
      return "";
    }
    BaseCodeDescription bcd = (BaseCodeDescription) map.get(idUtilizzo);
    if (bcd == null)
    {
      return "";
    }
    String codice = (String) bcd.getItem();
    String descrizione = bcd.getDescription();
    StringBuffer sb = new StringBuffer();
    if (codice != null)
    {
      sb.append("[").append(codice).append("] ");
    }
    return sb.append(descrizione).toString();
  }

  public String getLegendaConduzioni(TreeMap conduzioni)
  {
    Iterator iterator = conduzioni.keySet().iterator();
    Object key = null;
    String value = null;
    int i = 0;
    StringBuffer legenda = new StringBuffer();
    while (iterator.hasNext())
    {
      key = iterator.next();
      value = (String) conduzioni.get(key);
      if (i > 0)
      {
        legenda.append(", ");
      }
      ++i;
      legenda.append(key).append(" - ").append(value);
    }
    return legenda.toString();
  }

  public void addToMapConduzioni(TreeMap mapConduzioni, BaseCodeDescription conduzioni[])
  {
    int len = conduzioni == null ? 0 : conduzioni.length;
    for (int i = 0; i < len; ++i)
    {
      BaseCodeDescription conduzioneVO = conduzioni[i];
      mapConduzioni.put(new Long(conduzioneVO.getCode()), conduzioneVO.getDescription());
    }
  }

  public static String getDescTitoloPossesso(BaseCodeDescription conduzioni[])
  {
    StringBuffer cBuf = new StringBuffer();
    int len = conduzioni == null ? 0 : conduzioni.length;
    for (int i = 0; i < len; ++i)
    {
      BaseCodeDescription conduzioneVO = conduzioni[i];
      if (i > 0)
      {
        cBuf.append("; ");
      }
      cBuf.append(conduzioneVO.getCode());
      //String sup = Formatter.formatDouble4(conduzioneVO.getItem());
      //cBuf.append(" (").append(sup).append(" ha)");
    }
    return cBuf.toString();
  }

  %>

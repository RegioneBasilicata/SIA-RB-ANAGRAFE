<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@page
	import="it.csi.smranag.smrgaa.dto.ParticellaDettaglioValidazioniVO"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@page import="java.math.*"%>


<%!public static final String LAYOUT                  = "/layout/ricercaTerrenoDettaglioValidazioni.htm";
  public static final String ORDINAMENTO_ASCENDENTE  = "ordine crescente";
  public static final String ORDINAMENTO_DISCENDENTE = "ordine decrescente";
%><%
  SolmrLogger.debug(this, "[RicercaTerrenoDettaglioView:service] BEGIN.");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) request
      .getAttribute("storicoParticellaVO");
  String idStoricoParticella = StringUtils.checkNull(storicoParticellaVO.getIdStoricoParticella());
  htmpl.set("idStoricoParticella", idStoricoParticella);
  // Dati di destata
  htmpl.set("descComune", storicoParticellaVO.getComuneParticellaVO()
      .getDescom());
  if (storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null
      && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO()
          .getProvinciaVO().getSiglaProvincia()))
  {
    htmpl.set("siglaProvincia", "("
        + storicoParticellaVO.getComuneParticellaVO().getProvinciaVO()
            .getSiglaProvincia() + ")");
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
  htmpl.set("supCatastale", StringUtils
      .parseSuperficieField(storicoParticellaVO.getSupCatastale()));
  htmpl.set("superficieGrafica", StringUtils
      .parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));

  htmpl.set("idParticella", StringUtils.checkNull(storicoParticellaVO
      .getIdParticella()));

  ParticellaDettaglioValidazioniVO particellaDettaglioValidazioni[] = (ParticellaDettaglioValidazioniVO[]) request
      .getAttribute("particellaDettaglioValidazioni");
  if (particellaDettaglioValidazioni != null
      && particellaDettaglioValidazioni.length > 0)
  {
    writeComboAnni(htmpl, request);
    htmpl.newBlock("blkElencoValidazioni");
    writeOrdinamentoColonne(htmpl, request);
    int len = particellaDettaglioValidazioni.length;
    long lastIdDichiarazione = -1;
    long lastIdConduzione = -1;
    int rowSpanConsistenza = 1;
    int rowSpanConduzione = 1;
    int numValidazioni = 0;
    TreeMap conduzioni = new TreeMap();
    boolean bBlkConduzione = false;
    for (int i = 0; i < len; ++i)
    {
      ParticellaDettaglioValidazioniVO particellaDettaglioValidazioniVO = particellaDettaglioValidazioni[i];
      long idDichiarazioneConsistenza = particellaDettaglioValidazioniVO
          .getIdDichiarazioneConsistenza();
      long idConduzioneDichiarata = particellaDettaglioValidazioniVO
          .getIdConduzioneDichiarata();

      if (idDichiarazioneConsistenza != lastIdDichiarazione)
      {
        ++numValidazioni;
        // Rottura di codice per la dichiarazione
        // Scrivo i dati della prima riga della dichiarazione
        if (lastIdDichiarazione != -1)
        // Se lastIdDichiarazione != -1 ==> esiste una riga precedente ==> Aggiorno il rowspan 
        {

          // Aggiorno entrambi i rowspan (dichiarazione e conduzione)
          if (bBlkConduzione)
          {
            htmpl.set("blkElencoValidazioni.blkRiga.blkConduzione.rowSpan",
                String.valueOf(rowSpanConduzione));
          }
          else
          {
            htmpl.set("blkElencoValidazioni.blkRiga.conduzioneRowSpan",
                String.valueOf(rowSpanConduzione));
          }
          htmpl.set("blkElencoValidazioni.blkRiga.rowSpan", String
              .valueOf(rowSpanConsistenza));
        }
        bBlkConduzione = false;
        rowSpanConduzione = 1;
        rowSpanConsistenza = 1;
        lastIdDichiarazione = idDichiarazioneConsistenza;
        lastIdConduzione = idConduzioneDichiarata;
        // Scrivo i dati della dichiarazione
        writeDatiDichiarazione(htmpl, particellaDettaglioValidazioniVO);
        // Scrivo i dati della conduzione
        bBlkConduzione |= writeDatiConduzione(htmpl,
            particellaDettaglioValidazioniVO, rowSpanConsistenza,
            rowSpanConduzione);
        // Scrivo i dati dell'uso
        writeDatiUso(htmpl, particellaDettaglioValidazioniVO,
            rowSpanConsistenza, rowSpanConduzione);
      }
      else
      {
        ++rowSpanConsistenza;
        if (idConduzioneDichiarata != lastIdConduzione)
        {
          // Non ho bisogno di controllare lastIdDichiarazione == -1 perchè 
          // ricade nel caso lastIdDichiarazione == -1 
          // Aggiorno solo il rowspan conduzione
          if (rowSpanConduzione != rowSpanConsistenza - 1)
          {
            htmpl.set("blkElencoValidazioni.blkRiga.blkConduzione.rowSpan",
                String.valueOf(rowSpanConduzione));
          }
          else
          {
            htmpl.set("blkElencoValidazioni.blkRiga.conduzioneRowSpan",
                String.valueOf(rowSpanConduzione));
          }
          rowSpanConduzione = 1;
          // Scrivo i dati della conduzione
          bBlkConduzione |= writeDatiConduzione(htmpl,
              particellaDettaglioValidazioniVO, rowSpanConsistenza,
              rowSpanConduzione);
          // Scrivo i dati dell'uso
          writeDatiUso(htmpl, particellaDettaglioValidazioniVO,
              rowSpanConsistenza, rowSpanConduzione);
          lastIdConduzione = idConduzioneDichiarata;
        }
        else
        {
          ++rowSpanConduzione;
          writeDatiUso(htmpl, particellaDettaglioValidazioniVO,
              rowSpanConsistenza, rowSpanConduzione);
        }
      }
      conduzioni.put(new Long(particellaDettaglioValidazioniVO
          .getIdTitoloPossesso()), particellaDettaglioValidazioniVO
          .getDescrizioneTitoloPossesso());
    }
    if (len > 0)
    {
      // Setto il rowspan dell'ultimo elemento che, dato che va a rottura di codice,
      // non può essere messo durante il ciclo
      if (bBlkConduzione)
      {
        htmpl.set("blkElencoValidazioni.blkRiga.blkConduzione.rowSpan",
            String.valueOf(rowSpanConduzione));
      }
      else
      {
        htmpl.set("blkElencoValidazioni.blkRiga.conduzioneRowSpan", String
            .valueOf(rowSpanConduzione));
      }
      htmpl.set("blkElencoValidazioni.blkRiga.rowSpan", String
          .valueOf(rowSpanConsistenza));
    }
    htmpl.set("blkElencoValidazioni.legendaConduzione",
        getLegendaConduzioni(conduzioni));
    htmpl.set("blkElencoValidazioni.numValidazioni", String
        .valueOf(numValidazioni));
  }
  else
  {
    htmpl.newBlock("blkNoValidazioni");
  }
  HtmplUtil.setErrors(htmpl,(ValidationErrors)request.getAttribute("errors"),request,application);
%><%=htmpl.text()%>
<%
  SolmrLogger.debug(this, "[RicercaTerrenoDettaglioView:service] END.");
%><%!public boolean writeDatiConduzione(Htmpl htmpl,
      ParticellaDettaglioValidazioniVO particellaDettaglioValidazioniVO,
      int rowSpanConsistenza, int rowSpanConduzione)
  {
    boolean bBloccoConduzione = false;
    String blkName = "";
    if (rowSpanConsistenza == 1)
    {
      // Primo tr della dichirazione
      blkName = "blkElencoValidazioni.blkRiga";
    }
    else
    {
      // Tr successivi al primo
      blkName = "blkElencoValidazioni.blkRiga.blkConduzione";
      htmpl.newBlock(blkName);
      bBloccoConduzione = true;
    }

    htmpl.set(blkName + ".idConduzioneDichiarata", String
        .valueOf(particellaDettaglioValidazioniVO.getIdConduzioneDichiarata()));
    htmpl.set(blkName + ".idDichiarazioneConsistenza", String
        .valueOf(particellaDettaglioValidazioniVO
            .getIdDichiarazioneConsistenza()));
    htmpl.set(blkName + ".idAzienda", String
        .valueOf(particellaDettaglioValidazioniVO.getIdAzienda()));

    htmpl.set(blkName + ".idTitoloPossesso", String
        .valueOf(particellaDettaglioValidazioniVO.getIdTitoloPossesso()));
    BigDecimal percentualePossessoTmp = particellaDettaglioValidazioniVO.getPercentualePossesso();
    if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
    {
      percentualePossessoTmp = new BigDecimal(1);
    }
    htmpl.set(blkName + ".percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
    writeEsitoControllo(htmpl, particellaDettaglioValidazioniVO, blkName);
    return bBloccoConduzione;
  }

  public void writeDatiUso(Htmpl htmpl,
      ParticellaDettaglioValidazioniVO particellaDettaglioValidazioniVO,
      int rowSpanConsistenza, int rowSpanConduzione)
  {
    String blkName;
    String uso = getUso(particellaDettaglioValidazioniVO);
    if (rowSpanConsistenza == rowSpanConduzione)
    {

      // Primo tr della riga
      if (rowSpanConduzione == 1)
      {
        blkName = "blkElencoValidazioni.blkRiga";
      }
      else
      {
        blkName = "blkElencoValidazioni.blkRiga.blkUso";
        htmpl.newBlock(blkName);
      }
    }
    else
    {
      if (rowSpanConduzione == 1)
      {
        // Primo tr della conduzione (ma non della dichiarazione)
        blkName = "blkElencoValidazioni.blkRiga.blkConduzione";
      }
      else
      {
        blkName = "blkElencoValidazioni.blkRiga.blkConduzione.blkUso";
        htmpl.newBlock(blkName);
      }
    }
    // Sono nella prima riga ==> stesso tr degli altri dati, quindi stesso blocco
    htmpl.set(blkName + ".codiceMacroUso", particellaDettaglioValidazioniVO
        .getCodiceMacroUso());
    htmpl.set(blkName + ".descMacroUso", particellaDettaglioValidazioniVO
        .getDescrizioneMacroUso());
    htmpl.set(blkName + ".superficieUtilizzata", Formatter
        .formatDouble4(particellaDettaglioValidazioniVO
            .getSuperficieUtilizzata()));
    htmpl.set(blkName + ".uso", uso.toString());
  }

  public void writeEsitoControllo(Htmpl htmpl,
      ParticellaDettaglioValidazioniVO particellaDettaglioValidazioniVO,
      String blkName)
  {
    String esitoControllo = particellaDettaglioValidazioniVO
        .getEsitoControllo();
    if (SolmrConstants.ESITO_CONTROLLO_BLOCCANTE.equals(esitoControllo))
    {
      htmpl.set(blkName + ".classImmagine",
          SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
      htmpl.set(blkName + ".descTitleAnomalia",
          (String) SolmrConstants.DESC_TITLE_BLOCCANTE);
    }
    else
    {
      if (SolmrConstants.ESITO_CONTROLLO_POSITIVO.equals(esitoControllo))
      {
        htmpl.set(blkName + ".classImmagine",
            SolmrConstants.IMMAGINE_ESITO_POSITIVO);
        htmpl.set(blkName + ".descTitleAnomalia",
            (String) SolmrConstants.DESC_TITLE_POSITIVO);
      }
      else
      {

        if (SolmrConstants.ESITO_CONTROLLO_WARNING.equals(esitoControllo))
        {
          htmpl.set(blkName + ".classImmagine",
              SolmrConstants.IMMAGINE_ESITO_WARNING);
          htmpl.set(blkName + ".descTitleAnomalia",
              (String) SolmrConstants.DESC_TITLE_WARNING);
        }
      }
    }
  }

  public void writeDatiDichiarazione(Htmpl htmpl,
      ParticellaDettaglioValidazioniVO particellaDettaglioValidazioniVO)
  {
    htmpl.newBlock("blkElencoValidazioni.blkRiga"); // Nuova riga con 1..n utilizzi
    htmpl.set("blkElencoValidazioni.blkRiga.idParticella", String
        .valueOf(particellaDettaglioValidazioniVO.getIdParticella()));
    htmpl.set("blkElencoValidazioni.blkRiga.cuaa",
        particellaDettaglioValidazioniVO.getCuaa());
    htmpl.set("blkElencoValidazioni.blkRiga.denominazione",
        particellaDettaglioValidazioniVO.getDenominazione());
    htmpl.set("blkElencoValidazioni.blkRiga.validazione",
        particellaDettaglioValidazioniVO.getValidazione());
    htmpl.set("blkElencoValidazioni.blkRiga.supCatastale", Formatter
        .formatDouble4(particellaDettaglioValidazioniVO.getSupCatastale()));
  }

  public void writeComboAnni(Htmpl htmpl, HttpServletRequest request)
  {
    long anni[] = (long[]) request.getAttribute("anni");
    String selected = request.getParameter("anno");
    int len = anni == null ? 0 : anni.length;
    if (len > 0)
    {
      htmpl.newBlock("blkComboAnni");
      for (int i = 0; i < len; ++i)
      {
        htmpl.newBlock("blkComboAnni.blkAnno.value");
        String anno = String.valueOf(anni[i]);
        htmpl.set("blkComboAnni.blkAnno.value", anno);
        htmpl.set("blkComboAnni.blkAnno.descrizione", anno);
        if (anno.equals(selected))
        {
          htmpl.set("blkComboAnni.blkAnno.selected",
              SolmrConstants.HTML_SELECTED, null);
        }
      }
    }
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

  public void writeOrdinamentoColonne(Htmpl htmpl, HttpServletRequest request)
  {
    boolean ordineAscendente[] = (boolean[]) request
        .getAttribute("ordineAscendente");
    if (ordineAscendente[0])
    {
      // Ascendente
      htmpl.set("blkElencoValidazioni.classOrdineCUAA",
          SolmrConstants.CLASSE_ORDINAMENTO_ASCENDENTE);
      htmpl.set("blkElencoValidazioni.descOrdineCUAA", ORDINAMENTO_ASCENDENTE);
    }
    else
    {
      // Discendente
      htmpl.set("blkElencoValidazioni.classOrdineCUAA",
          SolmrConstants.CLASSE_ORDINAMENTO_DESCRESCENTE);
      htmpl.set("blkElencoValidazioni.descOrdineCUAA", ORDINAMENTO_DISCENDENTE);
      htmpl.set("ordineCuaaDesc", "true");
    }

    if (ordineAscendente[1])
    {
      // Ascendente
      htmpl.set("blkElencoValidazioni.classOrdineVal",
          SolmrConstants.CLASSE_ORDINAMENTO_ASCENDENTE);
      htmpl.set("blkElencoValidazioni.descOrdineVal", ORDINAMENTO_ASCENDENTE);
    }
    else
    {
      // Discendente
      htmpl.set("blkElencoValidazioni.classOrdineVal",
          SolmrConstants.CLASSE_ORDINAMENTO_DESCRESCENTE);
      htmpl.set("blkElencoValidazioni.descOrdineVal", ORDINAMENTO_DISCENDENTE);
      htmpl.set("ordineValidazioniDesc", "true");
    }
    htmpl.set("ordineSelezionato", request.getParameter("ordine"));

  }

  public String getUso(
      ParticellaDettaglioValidazioniVO particellaDettaglioValidazioniVO)
  {
    StringBuffer uso = new StringBuffer("");
    String codiceUtilizzo = particellaDettaglioValidazioniVO
        .getCodiceUtilizzo();
    if (Validator.isNotEmpty(codiceUtilizzo))
    {
      uso.append("[").append(codiceUtilizzo).append("] ");
    }
    String descrizioneUtilizzo = particellaDettaglioValidazioniVO
        .getDescrizioneUtilizzo();
    if (Validator.isNotEmpty(descrizioneUtilizzo))
    {
      uso.append(descrizioneUtilizzo);
    }
    return uso.toString();
  }%>

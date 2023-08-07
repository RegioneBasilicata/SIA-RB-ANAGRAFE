<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  SolmrLogger.debug(this, " - manodoperaDetView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/manodoperaDet.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  //ciascuna matrice rappresenta una riga di dettaglio manodopera
  //raggruppata per tipo classe e verrà utilizzata per il calcolo del totale colonne
  long[] rigaFamTempoPieno = null;
  long[] rigaSalFisTempoPieno = null;
  long[] rigaFamTempoParziale = null;
  long[] rigaSalFisTempoParziale = null;

  SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

  //hidden necessario per eliminare la manodopera dal dettaglio
  htmpl.set("idManodopera", request.getParameter("idManodopera"));

  ManodoperaVO manodoperaVO = (ManodoperaVO)request.getAttribute("manodoperaVO");
  SolmrLogger.debug(this, "manodoperaVO: " + manodoperaVO);

  if (manodoperaVO != null)
  {
    SolmrLogger.debug(this, "manodoperaVO.getCodiceInps(): " + manodoperaVO.getCodiceInps());
    htmpl.set("matricolaInail", manodoperaVO.getMatricolaInail());
    htmpl.set("formaConduzione", manodoperaVO.getTipoFormaConduzioneVO().getForma());

    htmpl.set("descTipoIscrizioneINPS", manodoperaVO.getDescTipoIscrizioneINPS());
    htmpl.set("codiceInps", manodoperaVO.getCodiceInps());
    htmpl.set("dataInizioIscrizione", DateUtils.formatDateNotNull(manodoperaVO.getDataInizioIscrizioneDate()));
    htmpl.set("dataCessazzioneIscrizione", DateUtils.formatDateNotNull(manodoperaVO.getDataCessazioneIscrizioneDate()));
    
    htmpl.set("dataInizioValidita", manodoperaVO.getDataInizioValidita());
    htmpl.set("dataFineValidita", manodoperaVO.getDataFineValidita());
    //htmpl.set("dataAggiornamentoManodopera", manodoperaVO.getDataAggiornamento());
    
    htmpl.set("desConduzione", manodoperaVO.getTipoFormaConduzioneVO().getDescrizione());

    //Dettaglio manodopera
    Vector vDettaglioManodopera = manodoperaVO.getVDettaglioManodopera();

    if (vDettaglioManodopera != null && vDettaglioManodopera.size() > 0) {
      SolmrLogger.debug(this, "vDettaglioManodopera.size(): " + vDettaglioManodopera.size());

      Iterator iteraDettaglioManodopera = vDettaglioManodopera.iterator();
      DettaglioManodoperaVO dettaglioManodoperaVO = null;

      while (iteraDettaglioManodopera.hasNext()) {
        dettaglioManodoperaVO = (DettaglioManodoperaVO)iteraDettaglioManodopera.next();

        SolmrLogger.debug(this, "dettaglioManodoperaVO.getCodTipoClasseManodopera(): " + dettaglioManodoperaVO.getCodTipoClasseManodopera());

        //Familiari a tempo pieno
        if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString().equals(SolmrConstants.get(
          "CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO").toString()))
        {
          rigaFamTempoPieno = impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "famUominiTempoPieno", "famDonneTempoPieno", "famTotaleTempoPieno");
        }

        //Salariati fissi a tempo pieno
        if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString().equals(SolmrConstants.get(
          "CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO").toString()))
        {
          rigaSalFisTempoPieno = impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "salFisUominiTempoPieno", "salFisDonneTempoPieno", "salFisTotaleTempoPieno");
        }

        //Familiari a tempo parziale
        if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString().equals(SolmrConstants.get(
          "CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE").toString()))
        {
          rigaFamTempoParziale = impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "famUominiTempoParziale", "famDonneTempoParziale", "famTotaleTempoParziale");
        }

        //Salariati fissi a tempo parziale
        if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString().equals(SolmrConstants.get(
          "CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE").toString()))
        {
          rigaSalFisTempoParziale = impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "salFisUominiTempoParziale", "salFisDonneTempoParziale", "salFisTotaleTempoParziale");
        }

        //Salariati avventizi
        if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString().equals(SolmrConstants.get(
          "CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI").toString()))
        {
          impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "salAvvUomini", "salAvvDonne", "salAvvTotale");
          //N. giornate/anno
          htmpl.set("salAvvGiornateAnnue", dettaglioManodoperaVO.getGiornateAnnue());
        }
      }

      //Somma familiari e salariati fissi a tempo pieno
      impostaTotaliColonne(rigaFamTempoPieno, rigaSalFisTempoPieno, htmpl, "totUominiTempoPieno", "totDonneTempoPieno", "totTotaleTempoPieno");

      //Somma familiari e salariati fissi a tempo parziale
      impostaTotaliColonne(rigaFamTempoParziale, rigaSalFisTempoParziale, htmpl, "totUominiTempoParziale", "totDonneTempoParziale", "totTotaleTempoParziale");
    }

    //Attività complementari
    Vector vDettaglioAttivita = manodoperaVO.getVDettaglioAttivita();
    if (vDettaglioAttivita != null && vDettaglioAttivita.size() > 0) {
      SolmrLogger.debug(this, "vDettaglioAttivita.size(): " + vDettaglioAttivita.size());

      Iterator iteraDettaglioAttivita = vDettaglioAttivita.iterator();
      DettaglioAttivitaVO dettaglioAttivitaVO = null;

      htmpl.newBlock("blkTipoAttivita");
      htmpl.newBlock("blkDettaglioAttivita");
      while (iteraDettaglioAttivita.hasNext()) {
        dettaglioAttivitaVO = (DettaglioAttivitaVO)iteraDettaglioAttivita.next();

        //descrizione Tipo Attivita Complementari
        htmpl.set("blkTipoAttivita.desTipoAttivita", dettaglioAttivitaVO.getAttivitaComplementari().getDescription());

        //descrizione Dettaglio Attivita
        htmpl.set("blkTipoAttivita.blkDettaglioAttivita.desDettaglioAttivita", StringUtils.checkNull(dettaglioAttivitaVO.getDescrizione()));
      }
    }
    else
    {
      //nessuna attività complementare svolta in azienda

      //descrizione Tipo Attivita Complementari
      htmpl.set("blkTipoAttivita.desTipoAttivita", "");

      //descrizione Dettaglio Attivita
      htmpl.set("blkTipoAttivita.blkDettaglioAttivita.desDettaglioAttivita", "");
    }

    //Visualizzazione Utente Aggiornamento
    SolmrLogger.debug(this, "manodoperaVO.getUtenteAggiornamento().getIdIntermediario(): " + manodoperaVO.getUtenteAggiornamento().getIdIntermediario());
    String utenteAgg = "";
    String enteAgg = "";
    if (manodoperaVO.getUtenteAggiornamento().getIdIntermediario() == null)
    {
      //Utente PA: denominazione utente
      //htmpl.set("utenteAggiornamentoManodopera", manodoperaVO.getUtenteAggiornamento().getDenominazione());
      utenteAgg = manodoperaVO.getUtenteAggiornamento().getDenominazione();
    }
    else
    {
      //Utente Intermediario: denominazione utente - descrizione ente appartenenza
      //htmpl.set("utenteAggiornamentoManodopera", manodoperaVO.getUtenteAggiornamento().getDenominazione() +
      //" - " + manodoperaVO.getUtenteAggiornamento().getDescrizioneEnteAppartenenza());
      utenteAgg = manodoperaVO.getUtenteAggiornamento().getDenominazione();
      enteAgg = manodoperaVO.getUtenteAggiornamento().getDescrizioneEnteAppartenenza();
    }
    
    //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
    //al ruolo!
    ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
      "ultimaModificaVw", manodoperaVO.getDataAggiornamento(), 
      utenteAgg, enteAgg, null);
    }

  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - manodoperaDetView.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>

<%!
  private long[] impostaRigaDettaglioManodopera(DettaglioManodoperaVO dettaglioManodoperaVO, Htmpl htmpl, String segnUomini, String segnDonne, String segnTot)
  {
    long[] somma = new long[3];
    //indice 0: uomini
    //indice 1: donne
    //indice 2: somma uomini + donne

    somma[0] = dettaglioManodoperaVO.getUominiLong().longValue();
    somma[1] = dettaglioManodoperaVO.getDonneLong().longValue();
    somma[2] = dettaglioManodoperaVO.getUominiLong().longValue() +
                dettaglioManodoperaVO.getDonneLong().longValue();

    htmpl.set(segnUomini, "" + somma[0]);
    htmpl.set(segnDonne, "" + somma[1]);
    htmpl.set(segnTot, "" + somma[2]);

    return somma;
  }

  private void impostaTotaliColonne(long[] riga1, long[] riga2, Htmpl htmpl, String segnTotUomini, String segnTotDonne, String segnTotTotale)
  {
    long[] totaliColonne = new long[3];
    //indice 0: totale uomini
    //indice 1: totale donne
    //indice 2: totale somma uomini + donne

    //inizializzazione a zero dei vettori null
    if (riga1 == null) riga1 = totaliColonne;
    if (riga2 == null) riga2 = totaliColonne;

    SolmrLogger.debug(this, "riga1.length: " + riga1.length + " riga2.length: " + riga2.length);

    //somma colonne delle due righe
    for (int i = 0; i < riga1.length; i++) {
      totaliColonne[i] = riga1[i] + riga2[i];
    }

    htmpl.set(segnTotUomini, "" + totaliColonne[0]);
    htmpl.set(segnTotDonne, "" + totaliColonne[1]);
    htmpl.set(segnTotTotale, "" + (totaliColonne[0] +  totaliColonne[1]));
  }
%>

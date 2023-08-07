<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.IntermediarioAnagVO" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.dto.comune.AmmCompetenzaVO" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<jsp:useBean id="delegaVO" scope="request" class="it.csi.solmr.dto.anag.DelegaVO"/>

<%
  SolmrLogger.debug(this, " - gestoreFascicoloView.jsp - INIZIO PAGINA");
  java.io.InputStream layout = application.getResourceAsStream("/layout/gestoreFascicoloCAA.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");
  boolean modificaDelega=false;
  Vector elencoErrori = (Vector)request.getAttribute("elencoErrori");
  IntermediarioAnagVO intermediarioVO = null;
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  if ("OK".equals(request.getAttribute("modificaDelega"))) modificaDelega=true;

  try
  {
    AnagFacadeClient client = new AnagFacadeClient();

    // Nuova gestione fogli di stile
    htmpl.set("head", head, null);
    htmpl.set("header", header, null);
    htmpl.set("footer", footer, null);

    String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "alt=\"{2}\" border=\"0\"></a>";
    String imko = "ko.gif";
    StringProcessor jssp = new JavaScriptStringProcessor();

    AnagAziendaVO anagAziendaVO=(AnagAziendaVO)session.getAttribute("anagAziendaVO");

    // Se arrivo da registra mandato recupero l'oggetto DelegaVO da anagAziendaVO
    if(!Validator.isNotEmpty(delegaVO.getIdDelega())) {
      delegaVO = anagAziendaVO.getDelegaVO();
    }

    //PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");
    UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = (UfficioZonaIntermediarioVO)request.getAttribute("ufficioZonaIntermediarioVO");
    if((delegaVO != null) && (delegaVO.getIdIntermediario() != null))
    {
      intermediarioVO = client
        .getIntermediarioAnagByIdIntermediario(delegaVO.getIdIntermediario().longValue());
    }

    String messaggioErrore = (String)request.getAttribute("messaggioErrore");

    if(Validator.isNotEmpty(messaggioErrore)) {
      htmpl.newBlock("blkGestioneFascicoloMessaggio");
      htmpl.set("blkGestioneFascicoloMessaggio.messaggio",messaggioErrore);
    }

    htmpl.set("CUAA",anagAziendaVO.getCUAA() );
    htmpl.set("partitaIVA",anagAziendaVO.getPartitaIVA() );
    htmpl.set("denominazione",anagAziendaVO.getDenominazione() );

    if (ruoloUtenza.isIsIntermediarioConDelega() && ruoloUtenza.isReadWrite())
    {
      //caso 1
      //azienda con delega
      htmpl.newBlock("blkGestioneFascicoloRead");
      htmpl.newBlock("blkGestioneFascicoloWrite");
      if (modificaDelega) {
        htmpl.newBlock("blkGestioneFascicoloWrite.blkGestioneUfficioRead");
        visualizzaDelega(htmpl, ruoloUtenza, delegaVO, ufficioZonaIntermediarioVO, intermediarioVO, false);
      }
      else
      {
        htmpl.newBlock("blkGestioneFascicoloButton");
        visualizzaDelega(htmpl,ruoloUtenza, delegaVO, ufficioZonaIntermediarioVO, intermediarioVO, true);
      }
    }
    else 
    {
      if ((ruoloUtenza.isIsIntermediarioConDelega() && !ruoloUtenza.isReadWrite())
          ||
          ruoloUtenza.isIsIntermediarioPadre())
      {
        //caso 2
        //azienda con delega
        htmpl.newBlock("blkGestioneFascicoloRead");
        htmpl.newBlock("blkGestioneFascicoloWrite");
        htmpl.newBlock("blkGestioneFascicoloWrite.blkGestioneUfficioRead");
        visualizzaDelega(htmpl, ruoloUtenza, delegaVO, ufficioZonaIntermediarioVO, intermediarioVO, false);
      }
      else 
      {
        //caso 3
        if (client.controllaObbligoFascicolo(anagAziendaVO)) 
        {
          //azienda con obbligo fascicolo
          if (anagAziendaVO.isPossiedeDelegaAttiva()) 
          {
            //azienda con delega
            htmpl.newBlock("blkGestioneFascicoloRead");
            htmpl.newBlock("blkGestioneFascicoloWrite");
            htmpl.newBlock("blkGestioneFascicoloWrite.blkGestioneUfficioRead");
            visualizzaDelega(htmpl, ruoloUtenza, delegaVO, ufficioZonaIntermediarioVO, intermediarioVO, false);
          }
          else 
          {
            //azienda senza delega
            
            ConsistenzaVO lastDichCons = client
              .getUltimaDichiarazioneConsistenza(anagAziendaVO.getIdAzienda());
            //CU-GAA15-02 Visualizzazione del gestore del fascicolo v4
            if((lastDichCons != null) &&  (lastDichCons.getIdUtente() !=null))
            {
              UtenteAbilitazioni utenteAbilitazioni = client.getUtenteAbilitazioniByIdUtenteLogin(new Long(lastDichCons.getIdUtente()));
              RuoloUtenza ruoloUtenzaUltimaDichConsistenza = new RuoloUtenzaPapua(utenteAbilitazioni); 
                
              if(ruoloUtenzaUltimaDichConsistenza.isUtentePA())
              {
                AmmCompetenzaVO ammVO = client.serviceFindAmmCompetenzaByCodiceAmm(
                  ruoloUtenzaUltimaDichConsistenza.getCodiceEnte());
                  
                htmpl.newBlock("blkGestioneFascicoloNoDelega");
                ComuneVO comVO = client.getComuneByISTAT(ammVO.getCapoluogo());
                
                visualizzaNoDelega(htmpl, ammVO, comVO);
              }
              else
              {
                htmpl.newBlock("blkGestioneFascicoloMessaggio");
                htmpl.set("blkGestioneFascicoloMessaggio.messaggio",(String)AnagErrors.get("ERR_VIS_NO_MANDATO"));
              }
            }
            else
            {
              htmpl.newBlock("blkGestioneFascicoloMessaggio");
              htmpl.set("blkGestioneFascicoloMessaggio.messaggio",(String)AnagErrors.get("ERR_VIS_NO_MANDATO"));
            }
          }
        }
        else 
        {
          htmpl.newBlock("blkGestioneFascicoloMessaggio");
          htmpl.set("blkGestioneFascicoloMessaggio.messaggio",(String)AnagErrors.get("ERR_VIS_MANDATO_NO_OBBLIGO"));
        }
      }
    }
    if(elencoErrori != null && elencoErrori.size() > 0) {
      String errore = (String)elencoErrori.elementAt(0);
       if(errore != null) {
         htmpl.set("blkGestioneFascicoloWrite.blkGestioneUfficioWrite.err_idUfficioZonaIntermediario",
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(errore)+"'",
                    errore}),
                    null);
        }
    }
    

   
    try
    {
      
      if(anagAziendaVO.getCUAA() !=null)
      {
        //Recupero i dati da SIAN
        if(session.getAttribute("gestoreFascicoloSIAN") == null)
        {
          SianFascicoloResponseVO sianFascicoloResponseVO = client.trovaFascicolo(anagAziendaVO.getCUAA());
          SianTrovaFascicoloVO sianTrovaFascicoloVO = (SianTrovaFascicoloVO)sianFascicoloResponseVO.getContenuto();
          session.setAttribute("gestoreFascicoloSIAN", sianTrovaFascicoloVO);
        }
      }
      else
      {
        htmpl.newBlock("blkGestioneFascicoloMessaggio");
        htmpl.set("blkGestioneFascicoloMessaggio.messaggio",AnagErrors.ERRORE_KO_CUAA_AZIENDA);
      }
    }
    catch(SolmrException se)
    {
      SolmrLogger.error(this,"\n\n Errore nella pagina gestoreFascicoloView.jsp nella chiamata al servizio trovaFascicolo: "+se.toString()+"\n\n");
      htmpl.newBlock("blkGestioneFascicoloMessaggio");
      htmpl.set("blkGestioneFascicoloMessaggio.messaggio", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
    
    }

  }
  catch(Exception e)
  {
    SolmrLogger.fatal(this,"\n\n Errore nella pagina gestoreFascicoloView.jsp: "+e.toString()+"\n\n");
    if (errors ==null) errors=new ValidationErrors();
    errors.add("error", new ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  }
  HtmplUtil.setErrors(htmpl,errors,request, application);
  out.print(htmpl.text());

  SolmrLogger.debug(this, " - gestoreFascicoloView.jsp - FINE PAGINA");
%>

<%!
  private void visualizzaDelega(Htmpl htmpl, RuoloUtenza ruoloUtenza, DelegaVO delegaVO, UfficioZonaIntermediarioVO ufficioVO, 
    IntermediarioAnagVO intermediarioVO, boolean isModificabile)
  {
    if (delegaVO!=null)
    {
      //GESTORE
      htmpl.set("blkGestioneFascicoloRead.denomIntermediario",delegaVO.getDenomIntermediario());
      htmpl.set("blkGestioneFascicoloRead.dataInizioMandato", DateUtils.formatDate(delegaVO.getDataInizioMandato()));
      
      //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
      //al ruolo!
      String dateUlt = "";
      if(delegaVO.getDataInizio() !=null)
      {
        dateUlt = DateUtils.formatDate(delegaVO.getDataInizio());
      } 
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkGestioneFascicoloRead.ultimaModificaVw", dateUlt, delegaVO.getNomeUtenIrideIndDelega(),
        delegaVO.getEnteUtenIrideIndDelega(),null);
      
      

      if(ufficioVO != null) {
        htmpl.set("blkGestioneFascicoloWrite.codiceAgea", StringUtils.parseCodiceAgea(ufficioVO.getCodiceAgea()));
        htmpl.set("blkGestioneFascicoloWrite.denominazioneUfficio",ufficioVO.getDenominazione());
        htmpl.set("blkGestioneFascicoloWrite.indirizzo",ufficioVO.getIndirizzo());
        htmpl.set("blkGestioneFascicoloWrite.siglaProvincia",ufficioVO.getSiglaProvincia());
        htmpl.set("blkGestioneFascicoloWrite.descrizioneComune",ufficioVO.getDescrizioneComune());
        htmpl.set("blkGestioneFascicoloWrite.cap",ufficioVO.getCap());
        if(intermediarioVO != null)
        {
          htmpl.set("blkGestioneFascicoloWrite.telefono",intermediarioVO.getTelefono());
          htmpl.set("blkGestioneFascicoloWrite.fax",intermediarioVO.getFax());
          htmpl.set("blkGestioneFascicoloWrite.email",intermediarioVO.getEmail());
          htmpl.set("blkGestioneFascicoloWrite.pec", intermediarioVO.getPec());
        }
      }
    }
  }
  
  private void visualizzaNoDelega(Htmpl htmpl, AmmCompetenzaVO ammVO, ComuneVO comVO)
  {
    if (ammVO!=null)
    {
      //GESTORE
      htmpl.set("blkGestioneFascicoloNoDelega.denomIntermediario", ammVO.getDescrizione());
      
      htmpl.set("blkGestioneFascicoloNoDelega.denominazione", ammVO.getDenominazione1());
      htmpl.set("blkGestioneFascicoloNoDelega.indirizzo", ammVO.getDenominazione2());
      htmpl.set("blkGestioneFascicoloNoDelega.siglaProvincia", comVO.getSiglaProv());
      htmpl.set("blkGestioneFascicoloNoDelega.descrizioneComune", comVO.getDescom());
      htmpl.set("blkGestioneFascicoloNoDelega.cap", comVO.getCap());
        
      htmpl.set("blkGestioneFascicoloNoDelega.telefono", ammVO.getTelefono());
      htmpl.set("blkGestioneFascicoloNoDelega.fax", ammVO.getFax());
      htmpl.set("blkGestioneFascicoloNoDelega.email", ammVO.getEmail());
        
      
    }
  }


%>

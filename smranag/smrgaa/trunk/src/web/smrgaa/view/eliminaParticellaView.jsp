<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.math.*"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/eliminaParticella.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  Vector<Long> vIdConduzioniUV = (Vector<Long>)request.getAttribute("vIdConduzioniUV");
  HashMap<Long,String> hIdConduzioniFabbricati = (HashMap<Long,String>)request.getAttribute("hIdConduzioniFabbricati");
  Vector<Long> vIdConduzioniDocumenti = (Vector<Long>)request.getAttribute("vIdConduzioniDocumenti");
  //Gestione errori
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  Vector<Long> vPraticheIdParticella = (Vector<Long>)request.getAttribute("vPraticheIdParticella");
  Vector<Long> vIdConduzioniModProcVITIUV = (Vector<Long>)request.getAttribute("vIdConduzioniModProcVITIUV");
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);

  if(errors != null && errors.size() > 0) 
  {
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }

  // Recupero il vettore contenente l'elenco delle particelle eliminabili
  Vector<ParticellaVO> elencoParticelleEliminabili = (Vector<ParticellaVO>)session.getAttribute("elencoParticelleEliminabili");
  if(elencoParticelleEliminabili != null && elencoParticelleEliminabili.size() > 0) 
  {
    Iterator<ParticellaVO> iteraParticelleEliminabili = elencoParticelleEliminabili.iterator();
    while(iteraParticelleEliminabili.hasNext()) 
    {
      ParticellaVO particellaEliminabileVO = (ParticellaVO)iteraParticelleEliminabili.next();
      htmpl.newBlock("blkElencoParticelleEliminabili");
      
      if(Validator.isNotEmpty(vPraticheIdParticella))
      {
        if(vPraticheIdParticella.contains(particellaEliminabileVO.getIdParticella()))
        {
          htmpl.set("blkElencoParticelleEliminabili.pratiche", MessageFormat.format(htmlStringKO,
            new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT+"'",
            AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT}), null);
        }
      }
      
      if(Validator.isNotEmpty(vIdConduzioniModProcVITIUV))
      {
        if(vIdConduzioniModProcVITIUV.contains(particellaEliminabileVO.getIdConduzioneParticella()))
        {
          htmpl.set("blkElencoParticelleEliminabili.pratiche", MessageFormat.format(htmlStringKO,
            new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_ELIMINA+"'",
            AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_ELIMINA}), null);
        }
      }
      
      
      htmpl.set("blkElencoParticelleEliminabili.descComuneParticella", particellaEliminabileVO.getDescComuneParticella());
      htmpl.set("blkElencoParticelleEliminabili.siglaProvinciaParticella", particellaEliminabileVO.getSiglaProvinciaParticella());
      htmpl.set("blkElencoParticelleEliminabili.sezione", particellaEliminabileVO.getSezione());
      htmpl.set("blkElencoParticelleEliminabili.foglio", particellaEliminabileVO.getFoglio().toString());
      if(Validator.isNotEmpty(particellaEliminabileVO.getParticella())) 
      {
        htmpl.set("blkElencoParticelleEliminabili.particella", particellaEliminabileVO.getParticella().toString());
      }
      if(Validator.isNotEmpty(particellaEliminabileVO.getSubalterno())) 
      {
        htmpl.set("blkElencoParticelleEliminabili.subalterno", particellaEliminabileVO.getSubalterno());
      }
      htmpl.set("blkElencoParticelleEliminabili.supCatastale", particellaEliminabileVO.getSupCatastale());
      BigDecimal percentualePossessoTmp = particellaEliminabileVO.getPercentualePossesso();
      if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
      {
        percentualePossessoTmp = new BigDecimal(1);
      }
      htmpl.set("blkElencoParticelleEliminabili.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
      htmpl.set("blkElencoParticelleEliminabili.descTitoloPossesso", particellaEliminabileVO.getDescrizioneTitoloPossesso());
      htmpl.set("blkElencoParticelleEliminabili.dataInizioConduzione", DateUtils.formatDate(particellaEliminabileVO.getDataInizioConduzione()));
      htmpl.set("blkElencoParticelleEliminabili.idConduzione", ""+particellaEliminabileVO.getIdConduzioneParticella());
      if(Validator.isNotEmpty(vIdConduzioniUV) 
        && vIdConduzioniUV.contains(particellaEliminabileVO.getIdConduzioneParticella()))
      { 
        htmpl.set("blkElencoParticelleEliminabili.classSchedario", SolmrConstants.CLASS_SCHEDARIO);
      }
      
      if(Validator.isNotEmpty(vIdConduzioniDocumenti) 
        && vIdConduzioniDocumenti.contains(particellaEliminabileVO.getIdConduzioneParticella()))
      { 
        htmpl.set("blkElencoParticelleEliminabili.classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_OK);
      }
      
      
      if(Validator.isNotEmpty(hIdConduzioniFabbricati) 
        && (hIdConduzioniFabbricati.get(particellaEliminabileVO.getIdConduzioneParticella()) !=null))
      { 
        htmpl.set("blkElencoParticelleEliminabili.descFabbricati", 
          hIdConduzioniFabbricati.get(particellaEliminabileVO.getIdConduzioneParticella()));
      }
    }
      
  }
  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }


%>
<%= htmpl.text()%>

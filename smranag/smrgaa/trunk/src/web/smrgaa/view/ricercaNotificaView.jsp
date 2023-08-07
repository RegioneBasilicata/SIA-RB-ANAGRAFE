<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/ricercaNotifica.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  NotificaVO notificaRicercaVO = (NotificaVO)session.getAttribute("notificaRicercaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  Boolean storico = (Boolean) request.getAttribute("storico");
  if (Validator.isNotEmpty(storico))
  {
    if (storico.booleanValue())
    {
      htmpl.set("checked", "checked=checked");
    }
  }

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // Combo relativa ai tipi tipologia notifica
  Vector<CodeDescription> vTipologiaNotifica = (Vector<CodeDescription>)request.getAttribute("vTipologiaNotifica");
  if(vTipologiaNotifica != null)
  {
    for(int i = 0; i < vTipologiaNotifica.size(); i++) 
    {
      CodeDescription code = vTipologiaNotifica.get(i);
      htmpl.newBlock("blkTipologiaNotifica");
      htmpl.set("blkTipologiaNotifica.idTipologiaNotifica", code.getCode().toString());
      htmpl.set("blkTipologiaNotifica.descTipologiaNotifica", code.getDescription());
      
      if(Validator.isNotEmpty(notificaRicercaVO) 
         && (notificaRicercaVO.getIdTipologiaNotifica().compareTo(new Long(code.getCode())) == 0)) 
      {
        htmpl.set("blkTipologiaNotifica.selected", "selected=\"selected\"", null);
      }
    }
  }
  
  // Combo Categoria
  Vector<CodeDescription> vCategoriaNotifica = (Vector<CodeDescription>)request.getAttribute("vCategoriaNotifica");
  if(vCategoriaNotifica != null) 
  {
    for(int i=0; i<vCategoriaNotifica.size(); i++)
    {
      CodeDescription code = vCategoriaNotifica.get(i);
      htmpl.newBlock("blkCategoriaCombo");
      htmpl.set("blkCategoriaCombo.idCategoriaNotifica", ""+code.getCode());
      htmpl.set("blkCategoriaCombo.idTipologiaNotifica", code.getSecondaryCode());
      htmpl.set("blkCategoriaCombo.descCategoriaNotifica", code.getDescription());
      htmpl.set("blkCategoriaCombo.index", ""+i);
      
      if(Validator.isNotEmpty(notificaRicercaVO) && Validator.isNotEmpty(notificaRicercaVO.getIdCategoriaNotifica())
        && (notificaRicercaVO.getIdCategoriaNotifica().compareTo(new Long(code.getCode())) == 0)) 
      {
        htmpl.set("categoriaSel",""+code.getCode());        
      }
    }
  }
 
 
 
 
 
 

 // Recupero il vettore contenente l'elenco delle province
 Vector<ProvinciaVO> elencoProvince = (Vector<ProvinciaVO>)request.getAttribute("elencoProvincePiemonte");
 Iterator<ProvinciaVO> iteraProvince = elencoProvince.iterator();
 while(iteraProvince.hasNext()) 
 {
   ProvinciaVO provinciaVO = (ProvinciaVO)iteraProvince.next();
   htmpl.newBlock("blkProvince");
   htmpl.set("blkProvince.idCodice", provinciaVO.getIstatProvincia());
   htmpl.set("blkProvince.descrizione", provinciaVO.getSiglaProvincia());
   if(notificaRicercaVO != null) {
     if(Validator.isNotEmpty(notificaRicercaVO.getIstatProvinciaUtente())) 
     {
       if(provinciaVO.getIstatProvincia().equalsIgnoreCase(notificaRicercaVO.getIstatProvinciaUtente())) 
       {
         htmpl.set("blkProvince.check", "selected = selected");
       }
     }
   }
 }

 if(Validator.isNotEmpty(notificaRicercaVO.getDataAl())) 
 {
   htmpl.set("strDataAl", notificaRicercaVO.getDataAl());
 }

 String percorsoErrori = null;
 if(pathToFollow.equalsIgnoreCase("rupar")) 
 {
   percorsoErrori = "/css_rupar/agricoltura/im/";
 }
 else if(pathToFollow.equalsIgnoreCase("sispie")) {
    percorsoErrori = "/css/agricoltura/im/";
 }
 else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
    percorsoErrori="/css/agricoltura/im/";  
 }

 // Setto i restanti valori relativi alla ricerca
 HtmplUtil.setValues(htmpl, notificaRicercaVO);

 // Sezione relativa all'elenco delle notifiche trovate
 Vector<NotificaVO> elencoNotifiche = (Vector<NotificaVO>)request.getAttribute("elencoNotifiche");
 if(elencoNotifiche != null) 
 {
   htmpl.newBlock("blkElencoNotifiche");
   htmpl.set("blkElencoNotifiche.numeroTotNotifiche", String.valueOf(elencoNotifiche.size()));
   if(elencoNotifiche.size() > 0) 
   {
     String indice = (String)session.getAttribute("indice");
     int i = 0;
     if(indice != null) 
     {
       i = Integer.parseInt(indice);
       if(i <= 0) 
       {
         i=0;
       }
       else if(i >= elencoNotifiche.size()) 
       {
         i = (elencoNotifiche.size()-1)-(elencoNotifiche.size()-1)%15;
       }
     }
     int j = i+14;

     Iterator<NotificaVO> iteraNotifiche = elencoNotifiche.iterator();

     if(iteraNotifiche.hasNext()) 
     {
       for(; i < elencoNotifiche.size() && i <= j; i++) 
       {
         NotificaVO notificaVO = (NotificaVO)elencoNotifiche.elementAt(i);
         htmpl.newBlock("blkElencoNotifiche.blkElenco");
         // Costruisco la chiave
         String primaryKey = notificaVO.getIdNotifica()+"/"+notificaVO.getIdAnagraficaAzienda();
         htmpl.set("blkElencoNotifiche.blkElenco.primaryKey", primaryKey);
         // In relazione al tipo di notifica visualizzo un'icona diversa
         if(notificaVO.getIdTipologiaNotifica()
           .compareTo((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE")) == 0) 
         {
           htmpl.set("blkElencoNotifiche.blkElenco.immagine", percorsoErrori + (String)SolmrConstants.get("IMMAGINE_BLOCCANTE"));
         }
         else if(notificaVO.getIdTipologiaNotifica()
           .compareTo((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_WARNING")) == 0) 
         {
           htmpl.set("blkElencoNotifiche.blkElenco.immagine", percorsoErrori + (String)SolmrConstants.get("IMMAGINE_WARNING"));
         }
         else if (notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0)
          {  
            htmpl.set("blkElencoNotifiche.blkElenco.immagine", percorsoErrori + SolmrConstants.IMMAGINE_BLOCCOPROCEDIMENTI);
          }
         else 
         {
           htmpl.set("blkElencoNotifiche.blkElenco.immagine", percorsoErrori + (String)SolmrConstants.get("IMMAGINE_OK"));
         }
         htmpl.set("blkElencoNotifiche.blkElenco.descCategoriaNotifica", notificaVO.getDescCategoriaNotifica());
         htmpl.set("blkElencoNotifiche.blkElenco.cuaa", notificaVO.getCuaa());
         htmpl.set("blkElencoNotifiche.blkElenco.denominazione", notificaVO.getDenominazione());
         htmpl.set("blkElencoNotifiche.blkElenco.comuneSedeLegale", notificaVO.getDescrizioneComuneSedeLegale());
         htmpl.set("blkElencoNotifiche.blkElenco.descrizione", notificaVO.getDescrizione());
         htmpl.set("blkElencoNotifiche.blkElenco.dataInserimento", DateUtils.formatDate(notificaVO.getDataInserimento()));
         //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
         //al ruolo!
         ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
            "blkElencoNotifiche.blkElenco.ultimaModificaVw", null,notificaVO.getDenominazioneUtenteInserimento(),
            notificaVO.getDescEnteAppartenenzaUtenteInserimento(), null);
         htmpl.set("blkElencoNotifiche.blkElenco.dataChiusura", DateUtils.formatDateNotNull(notificaVO.getDataChiusura()));
         ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
            "blkElencoNotifiche.blkElenco.datiChiusura", null,notificaVO.getDenominazioneUtenteChiusura(),
            notificaVO.getDescEnteAppartenenzaUtenteChiusura(), null);
         htmpl.set("blkElencoNotifiche.blkElenco.motivoChiusura", notificaVO.getNoteChisura());
         
         
         if(notificaVO.getvAllegatoDocumento() != null)
         {
           for(int k=0;k<notificaVO.getvAllegatoDocumento().size();k++)
           {
             htmpl.newBlock("blkElencoNotifiche.blkElenco.blkFileAllegato");
             htmpl.set("blkElencoNotifiche.blkElenco.blkFileAllegato.idDocumento", 
               ""+notificaVO.getIdNotifica());
             htmpl.set("blkElencoNotifiche.blkElenco.blkFileAllegato.idAllegato", 
               ""+notificaVO.getvAllegatoDocumento().get(k).getIdAllegato());
             htmpl.set("blkElencoNotifiche.blkElenco.blkFileAllegato.titleAllegato", 
               notificaVO.getvAllegatoDocumento().get(k).getNomeLogico()+" (" +
               notificaVO.getvAllegatoDocumento().get(k).getNomeFisico()+")");
           }
         }
         
         
         
       }
     }

     // Valorizzazione dei blocchi pulsanti avanti/indietro
     if(i > 15) 
     {
       htmpl.newBlock("blkElencoNotifiche.frecciaSinistra");
       htmpl.set("blkElencoNotifiche.frecciaSinistra.valore",""+(i-30+(15-i%15)%15));
     }

     int valoreTotale = 1;
     int numParziale = 1;

     if(elencoNotifiche.size() > 15 && i < elencoNotifiche.size()) 
     {
       htmpl.newBlock("blkElencoNotifiche.frecciaDestra");
       htmpl.set("blkElencoNotifiche.frecciaDestra.valore",""+i);
     }
     valoreTotale = (int)Math.ceil(elencoNotifiche.size()/15.0);
     htmpl.set("blkElencoNotifiche.numeroTotale", String.valueOf(valoreTotale));
     numParziale = ((i-1)/15)+1;
     htmpl.set("blkElencoNotifiche.numeroParziale",String.valueOf(numParziale));
   }
 }
 else 
 {
   String messaggio = (String)request.getAttribute("messaggio");
   htmpl.newBlock("blkNoNotifiche");
   htmpl.set("blkNoNotifiche.messaggio", messaggio);
 }

 // Sezione relativa ai messaggi di errore
 HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>

<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>

<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/notifiche.htm");
  Htmpl htmpl = new Htmpl(layout);
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  ValidationErrors errors = (ValidationErrors) request.getAttribute("errors");

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);

  Boolean storico = (Boolean) request.getAttribute("storico");
  if (Validator.isNotEmpty(storico))
  {
    if (storico.booleanValue())
    {
      htmpl.set("checked", "checked=checked");
    }
  }

  Long idNotifica = (Long) request.getAttribute("idNotifica");

  String percorsoErrori = null;
  if (pathToFollow.equalsIgnoreCase("rupar"))
  {
    percorsoErrori = "/ris/css/agricoltura/im/";
  }
  else if(pathToFollow.equalsIgnoreCase("sispie")) {
	    percorsoErrori = "/css/agricoltura/im/";
  }
  else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
	  percorsoErrori="/css/agricoltura/im/";  
  }

  // Sezione relativa all'elenco delle notifiche trovate
  Vector<NotificaVO> elencoNotifiche = (Vector<NotificaVO>) request.getAttribute("elencoNotifiche");
  if (elencoNotifiche != null)
  {
    htmpl.newBlock("blkSiNotifiche");
    htmpl.set("blkSiNotifiche.numeroTotNotifiche", String.valueOf(elencoNotifiche.size()));
    if (elencoNotifiche.size() > 0)
    {
    
      String blk="";
      if (Validator.isNotEmpty(storico))
      {
        if(storico.booleanValue()) 
        {
          blk="blkSiNotifiche.blkStoricoNotifiche";
        }
        else 
        {
          blk="blkSiNotifiche.blkNotifiche";
        }
      }
      else blk="blkSiNotifiche.blkNotifiche";
      
      htmpl.newBlock(blk);

      String indice = (String) session.getAttribute("indice");
      int i = 0;
      if (indice != null)
      {
        i = Integer.parseInt(indice);
        if (i <= 0)
        {
          i = 0;
        }
        else if (i >= elencoNotifiche.size())
        {
          i = (elencoNotifiche.size() - 1) - (elencoNotifiche.size() - 1) % 15;
        }
      }
      int j = i + 14;

      Iterator<NotificaVO> iteraNotifiche = elencoNotifiche.iterator();

      if (iteraNotifiche.hasNext())
      {
        for (; i < elencoNotifiche.size() && i <= j; i++)
        {
          NotificaVO notificaVO = (NotificaVO) elencoNotifiche.elementAt(i);
          htmpl.newBlock(blk+".blkElencoNotifiche");
          htmpl.set(blk+".blkElencoNotifiche.idNotifica", notificaVO.getIdNotifica().toString());
          if (Validator.isNotEmpty(idNotifica))
          {
            if (idNotifica.compareTo(notificaVO.getIdNotifica()) == 0)
            {
              htmpl.set(blk+".blkElencoNotifiche.radioChecked", "checked=checked");
            }
          }
          // In relazione al tipo di notifica visualizzo un'icona diversa
          if (notificaVO.getIdTipologiaNotifica().compareTo((Long) SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE")) == 0)
          {
            htmpl.set(blk+".blkElencoNotifiche.immagine", percorsoErrori + (String) SolmrConstants.get("IMMAGINE_BLOCCANTE"));
          }
          else if (notificaVO.getIdTipologiaNotifica().compareTo((Long) SolmrConstants.get("ID_TIPO_TIPOLOGIA_WARNING")) == 0)
          {  
            htmpl.set(blk+".blkElencoNotifiche.immagine", percorsoErrori + (String) SolmrConstants.get("IMMAGINE_WARNING"));
          }
          else if (notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0)
          {  
            htmpl.set(blk+".blkElencoNotifiche.immagine", percorsoErrori + SolmrConstants.IMMAGINE_BLOCCOPROCEDIMENTI);
          }
          else if (notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE) == 0)
          {  
            htmpl.set(blk+".blkElencoNotifiche.immagine", percorsoErrori + SolmrConstants.IMMAGINE_VARIAZIONICATASTALI);
          }
          else
          {
            htmpl.set(blk+".blkElencoNotifiche.immagine", percorsoErrori + (String) SolmrConstants.get("IMMAGINE_OK"));
          }
          
          htmpl.set(blk+".blkElencoNotifiche.descTipologiaNotifica", notificaVO.getDescTipologiaNotifica());
          htmpl.set(blk+".blkElencoNotifiche.descCategoriaNotifica", notificaVO.getDescCategoriaNotifica());
          htmpl.set(blk+".blkElencoNotifiche.strDataInserimento", notificaVO.getStrDataInserimento());
          if (Validator.isNotEmpty(notificaVO.getStrDataChiusura()))
          {
            htmpl.set(blk+".blkElencoNotifiche.strDataChiusura", notificaVO.getStrDataChiusura());
          }
          if (notificaVO.getDescrizione().length() > 100)
          {
            htmpl.set(blk+".blkElencoNotifiche.descrizione", notificaVO.getDescrizione().substring(0, 100) + "[...]");
          }
          else
          {
            htmpl.set(blk+".blkElencoNotifiche.descrizione", notificaVO.getDescrizione());
          }
          
          
          if(notificaVO.getvAllegatoDocumento() != null)
          {
            for(int k=0;k<notificaVO.getvAllegatoDocumento().size();k++)
            {
              htmpl.newBlock(blk+".blkElencoNotifiche.blkFileAllegato");
              htmpl.set(blk+".blkElencoNotifiche.blkFileAllegato.idDocumento", 
                ""+notificaVO.getIdNotifica());
              htmpl.set(blk+".blkElencoNotifiche.blkFileAllegato.idAllegato", 
                ""+notificaVO.getvAllegatoDocumento().get(k).getIdAllegato());
              htmpl.set(blk+".blkElencoNotifiche.blkFileAllegato.titleAllegato", 
                notificaVO.getvAllegatoDocumento().get(k).getNomeLogico()+" (" +
                notificaVO.getvAllegatoDocumento().get(k).getNomeFisico()+")");
            }
          }
            
            
          //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
          //al ruolo!
          ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
            blk+".blkElencoNotifiche.ultimaModificaVw", null,notificaVO.getDenominazioneUtenteInserimento(),
            notificaVO.getDescEnteAppartenenzaUtenteInserimento(), null);
            
          ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
            blk+".blkElencoNotifiche.ultimaModificaChiusuraVw", null,notificaVO.getDenominazioneUtenteChiusura(),
            notificaVO.getDescEnteAppartenenzaUtenteChiusura(), null);  
            
        }
      }

      // Valorizzazione dei blocchi pulsanti avanti/indietro
      if (i > 15)
      {
        htmpl.newBlock("blkSiNotifiche.frecciaSinistra");
        htmpl.set("blkSiNotifiche.frecciaSinistra.valore", "" + (i - 30 + (15 - i % 15) % 15));
      }

      int valoreTotale = 1;
      int numParziale = 1;

      if (elencoNotifiche.size() > 15 && i < elencoNotifiche.size())
      {
        htmpl.newBlock("blkSiNotifiche.frecciaDestra");
        htmpl.set("blkSiNotifiche.frecciaDestra.valore", "" + i);
      }
      valoreTotale = (int) Math.ceil(elencoNotifiche.size() / 15.0);
      htmpl.set("blkSiNotifiche.numeroTotale", String.valueOf(valoreTotale));
      numParziale = ((i - 1) / 15) + 1;
      htmpl.set("blkSiNotifiche.numeroParziale", String.valueOf(numParziale));
    }
  }
  else
  {
    String messaggio = (String) request.getAttribute("messaggio");
    htmpl.newBlock("blkNoNotifiche");
    htmpl.set("blkNoNotifiche.messaggio", messaggio);
  }

  // Sezione relativa ai messaggi di errore
  HtmplUtil.setErrors(htmpl, errors, request, application);
%>
<%=htmpl.text()%>

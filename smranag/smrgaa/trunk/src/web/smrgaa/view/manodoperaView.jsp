<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/manodopera.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  Vector<FrmManodoperaVO> elencoManodopera = (Vector<FrmManodoperaVO>)request.getAttribute("elencoManodopera");
  
  
  // Combo Piano di riferimento
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
    ruoloUtenza);

  Long idElemento = (Long)request.getAttribute("idElemento");
  String idManodopera = request.getParameter("idManodopera");

  if(elencoManodopera != null && elencoManodopera.size() > 0) 
  {

    htmpl.newBlock("blkEtichettaManodopera");

    Iterator<FrmManodoperaVO> iteraManodopera = elencoManodopera.iterator();
    FrmManodoperaVO frmManodoperaVO = null;
    while (iteraManodopera.hasNext()) 
    {
      htmpl.newBlock("elencoManodopera");
      frmManodoperaVO = iteraManodopera.next();
      if(idElemento != null) 
      {
        if(idElemento.compareTo(frmManodoperaVO.getIdManodoperaLong()) == 0) 
        {
          htmpl.set("elencoManodopera.checked", "checked");
        }
      }
      else 
      {
        if(idManodopera != null && !idManodopera.equals("")) 
        {
          if(frmManodoperaVO.getIdManodoperaLong().compareTo(Long.decode(idManodopera)) == 0) 
          {
            htmpl.set("elencoFabbricati.checked", "checked");
          }
        }
      }
      htmpl.set("elencoManodopera.idManodopera", frmManodoperaVO.getIdManodopera());
      htmpl.set("elencoManodopera.codiceInps", frmManodoperaVO.getCodiceINPS());
      htmpl.set("elencoManodopera.matricolaInail", frmManodoperaVO.getMatrciolaInail());
      htmpl.set("elencoManodopera.numPersTempoPieno", "" + frmManodoperaVO.getNumPersTempoPienoLong());
      htmpl.set("elencoManodopera.numPersTempoParziale", "" + frmManodoperaVO.getNumPersTempoParzLong());
      htmpl.set("elencoManodopera.numSalariatiAvventizi", "" + frmManodoperaVO.getNumSalariatiAvventiziLong());
      htmpl.set("elencoManodopera.desTipoFormaConduzione", frmManodoperaVO.getTipoFormaConduzioneVO().getDescrizione());
      htmpl.set("elencoManodopera.dataInizioValidita", frmManodoperaVO.getDataInizioValidita());
      htmpl.set("elencoManodopera.dataFineValidita", frmManodoperaVO.getDataFineValidita());
    }
  }
  else {
    htmpl.newBlock("noManodopera");
  }

  HtmplUtil.setErrors(htmpl, errors, request, application);

%>

<%= htmpl.text()%>

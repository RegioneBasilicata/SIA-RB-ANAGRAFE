<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popupVariazioniParticella.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	Vector<ParticellaVO> elencoStoricoParticella = (Vector<ParticellaVO>)request.getAttribute("elencoStoricoParticella");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	EventoParticellaVO[] elencoEventoParticella = (EventoParticellaVO[])request.getAttribute("elencoEventoParticella");

 	// Sì è verificato un errore durante il recupero delle variazioni storiche della particella
 	// selezionata
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
  	htmpl.newBlock("blkErrore");
  	htmpl.set("blkErrore.messaggio", messaggioErrore);
 	}
 	else 
  {
  	htmpl.newBlock("blkStoricoParticella");
  	Iterator<ParticellaVO> iteraStoricoParticella = elencoStoricoParticella.iterator();
  	while(iteraStoricoParticella.hasNext()) 
    {
    	htmpl.newBlock("blkStoricoParticella.blkElencoStoricoParticella");
    	ParticellaVO particellaVO = (ParticellaVO)iteraStoricoParticella.next();
    	htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.supCatastale", particellaVO.getSupCatastale());
      htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.superficieGrafica", particellaVO.getSuperficieGrafica());
    	htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.dataInizioValidita", DateUtils.formatDate(particellaVO.getDataInizioValidita()));
    	if(Validator.isNotEmpty(particellaVO.getDataFineValidita())) 
      {
      	htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.dataFineValidita", DateUtils.formatDate(particellaVO.getDataFineValidita()));
     	}
        
      //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
      //al ruolo!
      String enteAgg = "";
      if(Validator.isNotEmpty(particellaVO.getFonteDato()))
      {
        enteAgg = "Fonte: "+particellaVO.getFonteDato().getDescription();
      }
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkStoricoParticella.blkElencoStoricoParticella.ultimaModificaVw", null, 
        particellaVO.getDenominazioneUtenteAggiornamento(), enteAgg, null);
        
   		//if(Validator.isNotEmpty(particellaVO.getDenominazioneUtenteAggiornamento())) {
    	 		//htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.denominazioneUtenteAggiornamento", particellaVO.getDenominazioneUtenteAggiornamento());
   		//}
   		//if(Validator.isNotEmpty(particellaVO.getFonteDato())) {
     			//htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.fonteDato", "(Fonte:"+particellaVO.getFonteDato().getDescription()+")");
   		//}
   		if(Validator.isNotEmpty(particellaVO.getCausaleModParticella())) {
     			htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.causale", particellaVO.getCausaleModParticella().getDescription());
   		}
   		if(Validator.isNotEmpty(particellaVO.getMotivoModifica())) {
     			htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.motivo", particellaVO.getMotivoModifica());
   		}
   		if(Validator.isNotEmpty(particellaVO.getTipoDocumento())) {
     			htmpl.set("blkStoricoParticella.blkElencoStoricoParticella.documento", particellaVO.getTipoDocumento().getDescription());
   		}
 		}
 		if(elencoEventoParticella != null && elencoEventoParticella.length > 0) {
 			htmpl.newBlock("blkStoricoParticella.blkEvento");
 			for(int i = 0; i < elencoEventoParticella.length; i++) {
 				EventoParticellaVO eventoParticellaVO = (EventoParticellaVO)elencoEventoParticella[i];
 				htmpl.newBlock("blkStoricoParticella.blkEvento.blkElencoEventiParticella");	
 				htmpl.set("blkStoricoParticella.blkEvento.blkElencoEventiParticella.descEvento", eventoParticellaVO.getTipoEvento().getDescription());
 				htmpl.set("blkStoricoParticella.blkEvento.blkElencoEventiParticella.dataAggiornamento", DateUtils.formatDate(eventoParticellaVO.getDataAggiornamento()));
 				htmpl.set("blkStoricoParticella.blkEvento.blkElencoEventiParticella.descrizioneParticella", eventoParticellaVO.getDescParticellaOrigineEvento());
 			}
 		}
 	}

%>
<%= htmpl.text()%>

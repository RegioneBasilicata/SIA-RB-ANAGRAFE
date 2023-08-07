<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.EsitoControlloAllevamento" %>
<%@ page import="it.csi.solmr.client.anag.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popAnomalieAllevamento.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	
 	
	//Dati relativi all'allevamento
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AllevamentoAnagVO allevamentoVO = (AllevamentoAnagVO)request.getAttribute("allevamentoVO");
 	htmpl.set("denominazioneAllevamento", allevamentoVO.getDenominazione());
 	UteVO ute = anagFacadeClient.getUteById(allevamentoVO.getIdUTELong());
 	ComuneVO comune = anagFacadeClient.getComuneByISTAT(ute.getIstat());
  htmpl.set("comuneUte", comune.getDescom() + " (" + anagFacadeClient.getSiglaProvinciaByIstatProvincia(comune.getIstatProvincia()) + ")");
  if (Validator.isNotEmpty(ute.getIndirizzo()))
  {
    htmpl.set("indirizzoUte", "- " + ute.getIndirizzo());
  }
 	
 	comune = anagFacadeClient.getComuneByISTAT(allevamentoVO.getIstatComuneAllevamento());
  String comuneAllevamento = comune.getDescom() + " (" + anagFacadeClient.getSiglaProvinciaByIstatProvincia(comune.getIstatProvincia()) + ")";
 	htmpl.set("comuneAllevamento", comuneAllevamento);
  htmpl.set("codiceAziendaZootecnico", allevamentoVO.getCodiceAziendaZootecnica());
  TipoSpecieAnimaleAnagVO tipoSpecie = allevamentoVO.getTipoSpecieAnimaleAnagVO();
  htmpl.set("specie", tipoSpecie.getDescrizione());
  htmpl.set("codFiscDetentore", allevamentoVO.getCodiceFiscaleDetentore());
  htmpl.set("denominazioneDetentore", allevamentoVO.getDenominazioneDetentore());
  htmpl.set("codFiscProprietario", allevamentoVO.getCodiceFiscaleProprietario());
  htmpl.set("denominazioneProprietario", allevamentoVO.getDenominazioneProprietario());
  
 	String messaggio = (String)request.getAttribute("messaggio");
 	Vector<EsitoControlloAllevamento> elencoEsito = (Vector<EsitoControlloAllevamento>)request.getAttribute("elencoEsito");

 	// Non sono state trovate anomalie
 	if(Validator.isNotEmpty(messaggio) || Validator.isEmpty(elencoEsito)) 
 	{
   	htmpl.newBlock("blkNoAnomalie");
   	htmpl.set("blkNoAnomalie.messaggio", messaggio);
 	}
 	else 
 	{
  	htmpl.newBlock("blkAnomalie");
  	for(int i=0;i<elencoEsito.size();i++)
  	{
  	  htmpl.newBlock("blkAnomalie.blkElencoAnomalie");
     	EsitoControlloAllevamento esitoControlloAllevamento = elencoEsito.get(i);
			htmpl.set("blkAnomalie.blkElencoAnomalie.descrizioneTipoControllo", esitoControlloAllevamento.getDescControllo());
			htmpl.set("blkAnomalie.blkElencoAnomalie.descrizione", esitoControlloAllevamento.getDescrizione());
			if(esitoControlloAllevamento.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
			{
			 	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", (String)SolmrConstants.get("IMMAGINE_ESITO_BLOCCANTE"));
			}
			if(esitoControlloAllevamento.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
			{
			 	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", (String)SolmrConstants.get("IMMAGINE_ESITO_WARNING"));
			}
     		
   	}
  }
 	


%>
<%= htmpl.text()%>

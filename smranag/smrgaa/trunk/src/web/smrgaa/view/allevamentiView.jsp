<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/allevamenti.htm");
	
	%>
  	<%@include file = "/view/remoteInclude.inc" %>
	<%

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagAziendaVO aziendaVOTmp = (AnagAziendaVO)request.getAttribute("aziendaVOTmp");
  AllevamentoAnagVO[] elencoAllevamenti = (AllevamentoAnagVO[])request.getAttribute("elencoAllevamenti");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  Vector<UteVO> elencoUte = (Vector<UteVO>)request.getAttribute("elencoUte");

	Long idElemento = (Long)request.getAttribute("idElemento");

	if(errors != null && errors.size() > 0) 
  {
		HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);
	}
  
  
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
    ruoloUtenza);
    
    
  // Combo unità produttive
  String idUte = request.getParameter("idUte");
  if(elencoUte != null && elencoUte.size() > 0) 
  {
    Iterator<UteVO> iteraUte = elencoUte.iterator();
    while(iteraUte.hasNext()) 
    {
      UteVO uteVO = (UteVO)iteraUte.next();
      htmpl.newBlock("blkElencoUte");
      htmpl.set("blkElencoUte.idUte", uteVO.getIdUte().toString());
      String descrizione = uteVO.getComuneUte().getDescom()+" - "+uteVO.getIndirizzo();
      if(Validator.isNotEmpty(uteVO.getDenominazione()))
        descrizione += " - "+uteVO.getDenominazione();
      if(Validator.isNotEmpty(uteVO.getDataFineAttivita())) {
        descrizione += " fino al "+DateUtils.formatDate(uteVO.getDataFineAttivita());
      }
      htmpl.set("blkElencoUte.descUte", descrizione);
      if(Validator.isNotEmpty(idUte)) 
      {
        if(new Long(idUte).compareTo(uteVO.getIdUte()) == 0) 
        {
          htmpl.set("blkElencoUte.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
  
  htmpl.set("dataControlli", DateUtils.formatDateTimeNotNull(aziendaVOTmp.getDataControlliAllevamenti()));
  


	String startRowStr = request.getParameter("startRow");
	int startRow = 0;
	int rows = elencoAllevamenti.length;

	if(startRowStr != null) 
  {
  	try 
    {
    	startRow = new Integer(startRowStr).intValue();
  	}
  	catch(Exception e) {}
	}

	int prevPage = startRow-SolmrConstants.NUM_MAX_ROWS_PAG;
  int nextPage = startRow+SolmrConstants.NUM_MAX_ROWS_PAG;
  if(nextPage >= rows) 
  {
  	nextPage=startRow;
	}
	if(prevPage <= 0) 
  {
  	prevPage = 0;
	}

	int maxPage = rows/SolmrConstants.NUM_MAX_ROWS_PAG+(rows%SolmrConstants.NUM_MAX_ROWS_PAG>0?1:0);

	if(elencoAllevamenti.length == 0) 
  {
  	maxPage = 1;
	}

	int currentPage = startRow/SolmrConstants.NUM_MAX_ROWS_PAG+1+(startRow%SolmrConstants.NUM_MAX_ROWS_PAG>0?1:0);

	int size = elencoAllevamenti.length;

	if(size > 0) 
  {
  	// Intestazione contenente il numero di allevamenti trovati, la pagina attuale e le pagine
  	// complessive
  	htmpl.newBlock("blkNumeroAllevamenti");
  	htmpl.newBlock("elencoAllevamenti");
  	if(currentPage != 1) 
    {
    	htmpl.set("blkNumeroAllevamenti.prev.prevPage",""+prevPage);
  	}
  	if(currentPage != maxPage) 
    {
    	htmpl.set("blkNumeroAllevamenti.next.nextPage",""+nextPage);
  	}
  	htmpl.set("blkNumeroAllevamenti.numAllevamenti",""+elencoAllevamenti.length);
  	htmpl.set("blkNumeroAllevamenti.maxPage",""+maxPage);
  	htmpl.set("blkNumeroAllevamenti.currentPage",""+currentPage);
  	Long radio = null;
  	try 
    {
    	radio = new Long(request.getParameter("radiobutton"));
  	}
  	catch(Exception e) {}
  	java.text.DecimalFormat numericFormat2 = new java.text.DecimalFormat(SolmrConstants.FORMATO_NUMERIC_1INT_2DEC);
  	boolean flagIntermediario = false;
  	double totUba = 0;
  	htmpl.newBlock("elencoAllevamenti.blkIntestazione");
  	for(int i = startRow; i < size && i < startRow+it.csi.solmr.etc.SolmrConstants.NUM_MAX_ROWS_PAG; i++) 
    {
  		AllevamentoAnagVO all = new AllevamentoAnagVO();
  		all = (AllevamentoAnagVO)elencoAllevamenti[i];
  		TipoSpecieAnimaleAnagVO tipoSpecie = all.getTipoSpecieAnimaleAnagVO();
  		htmpl.newBlock("elencoAllevamenti.blkAllevamento");
  		if(radio != null && radio.equals(all.getIdAllevamentoLong())) 
      {
    		htmpl.set("elencoAllevamenti.blkAllevamento.checked", "checked=\"checked\"");
  		}
  		else if(idElemento != null) 
      {
    		if(idElemento.compareTo(all.getIdAllevamentoLong()) == 0) 
        {
      			htmpl.set("elencoAllevamenti.blkAllevamento.checked", "checked=\"checked\"");
    		}
  		}
  		htmpl.set("elencoAllevamenti.blkAllevamento.idAllevamento", all.getIdAllevamento());
  		htmpl.set("elencoAllevamenti.blkAllevamento.codiceAziendaZootecnico", all.getCodiceAziendaZootecnica());
  		String comuneAllevamento = all.getComuneVO().getDescom() + " (" + all.getComuneVO().getProvinciaVO().getSiglaProvincia() + ")";
  		java.text.DecimalFormat nf4 = new java.text.DecimalFormat("#0.0000");
      
      // Icona relativa al biologico
      if(all.getBiologico() != null) 
      {
        htmpl.set("elencoAllevamenti.blkAllevamento.descBiologico", SolmrConstants.DESC_TITLE_BIOLOGICO);
        htmpl.set("elencoAllevamenti.blkAllevamento.classBiologico", SolmrConstants.CLASS_BIOLOGICO);
      }
      
      
      // Bloccante
      if(all.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_BLOCCANTE))
      {
        htmpl.set("elencoAllevamenti.blkAllevamento.classImmagine", SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
        htmpl.set("elencoAllevamenti.blkAllevamento.descTitleAnomalia", SolmrConstants.DESC_TITLE_BLOCCANTE);
      }
      // Warning
      else if(all.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_WARNING))
      {
        htmpl.set("elencoAllevamenti.blkAllevamento.classImmagine", SolmrConstants.IMMAGINE_ESITO_WARNING);
        htmpl.set("elencoAllevamenti.blkAllevamento.descTitleAnomalia", SolmrConstants.DESC_TITLE_WARNING);
      }
      else
      {
        htmpl.set("elencoAllevamenti.blkAllevamento.classImmagine", SolmrConstants.IMMAGINE_ESITO_POSITIVO);
        htmpl.set("elencoAllevamenti.blkAllevamento.descTitleAnomalia", SolmrConstants.DESC_TITLE_POSITIVO);
      }
      
      
      
      htmpl.set("elencoAllevamenti.blkAllevamento.denominazioneAllevamento", all.getDenominazione());
      String comuneUte = all.getComuneUteVO().getDescom() + " (" + all.getComuneUteVO().getProvinciaVO().getSiglaProvincia() + ")";
      htmpl.set("elencoAllevamenti.blkAllevamento.comuneUte", comuneUte);
      htmpl.set("elencoAllevamenti.blkAllevamento.indirizzoUte", all.getIndirizzoUte());
      
  		htmpl.set("elencoAllevamenti.blkAllevamento.comuneAllevamento", comuneAllevamento);
  		htmpl.set("elencoAllevamenti.blkAllevamento.specie", tipoSpecie.getDescrizione());
  		htmpl.set("elencoAllevamenti.blkAllevamento.quantitaDetenzione", all.getSommaQuantita());
  		htmpl.set("elencoAllevamenti.blkAllevamento.quantitaProprieta", all.getSommaQuantitaProprietario());
  		htmpl.set("elencoAllevamenti.blkAllevamento.unitaMisura", tipoSpecie.getUnitaDiMisura());
  		try 
      {
    		htmpl.set("elencoAllevamenti.blkAllevamento.UBA",""+StringUtils.parseDoubleField(String.valueOf(Double.parseDouble(all.getSommaUBA()))));
  		}
  		catch(Exception e) 
      {
    		htmpl.set("elencoAllevamenti.blkAllevamento.UBA","0.0000");
  		}
  		htmpl.set("elencoAllevamenti.blkAllevamento.codFiscDetentore", all.getCodiceFiscaleDetentore());
      htmpl.set("elencoAllevamenti.blkAllevamento.denominazioneDetentore", all.getDenominazioneDetentore());
  		htmpl.set("elencoAllevamenti.blkAllevamento.codFiscProprietario", all.getCodiceFiscaleProprietario());
      htmpl.set("elencoAllevamenti.blkAllevamento.denominazioneProprietario", all.getDenominazioneProprietario());
  		
  		String soccida = "No";
  		if(all.isFlagSoccida())
  		  soccida = "Si";
  		htmpl.set("elencoAllevamenti.blkAllevamento.soccida", soccida);
  		 
  		
  		if(all.getDataFineDate() != null) 
      {
  			htmpl.set("elencoAllevamenti.blkAllevamento.dataAl", DateUtils.formatDate(all.getDataFineDate()));
  		}
  	}
  	if(startRow < size) 
    {
  		htmpl.newBlock("blkTotaleUba");
  		htmpl.set("blkTotaleUba.totaleUba", StringUtils.parseDoubleField(String.valueOf(totUba)));
  	}
	}
	else 
  {
    htmpl.newBlock("noAllevamenti");
 	}
  out.print(htmpl.text());
%>

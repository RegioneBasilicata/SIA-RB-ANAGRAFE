<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>

<%
  
  Vector dichiarazioniConsistenza = null;
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/dichiarazioneConsistenza.htm");

  %>
  	<%@include file = "/view/remoteInclude.inc" %>
  <%

  HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  //Arrivo da una validazione appena fatta
  if(Validator.isNotEmpty(session.getAttribute("idDichiarazioneConsistenzaStampaPartenza")))
  {
    htmpl.newBlock("blkPdfValidazione");
    htmpl.set("idDichiarazioneConsistenzaStampa", ""+(Long)session.getAttribute("idDichiarazioneConsistenzaStampaPartenza"));
    //rimuovo il valore per evitare ripartenza nel caso si ritorni nellenco senza che la stampasia conclusa!!!!
    session.removeAttribute("idDichiarazioneConsistenzaStampaPartenza");
    //session.removeAttribute("idDichiarazioneConsistenzaAllegati");
  }
  
  String fallitaStampa = request.getParameter("fallitaStampa");
  Date parametroDataSTMPLivecycle = (Date)request.getAttribute("parametroDataSTMPLivecycle");
  BigDecimal parametroDeleyNewStampa = (BigDecimal)request.getAttribute("parametroDeleyNewStampa");

  dichiarazioniConsistenza = (Vector)request.getAttribute("dichiarazioniConsistenza");
  if(dichiarazioniConsistenza == null) 
  {
    dichiarazioniConsistenza = new Vector();
  }

  int size = dichiarazioniConsistenza.size();
  if(size > 0) 
  {
  
    if(request.getAttribute("errorsJQuery") != null)
    {
      htmpl.newBlock("blkjQuery");
      htmpl.set("errorsJQuery", (String)request.getAttribute("errorsJQuery"));      
    }
  
  
    // Gestione pulsanti
    htmpl.newBlock("blkEtichettaDichiarazione");
    Iterator iteraDichiarazione = dichiarazioniConsistenza.iterator();
    ConsistenzaVO consistenzaVO = null;
    String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
		String imko = "ko.gif";
		String imok = "ok.gif";
		String clessidra = "clessidra.gif";
		int indice = 0;
    while(iteraDichiarazione.hasNext()) 
    {
      htmpl.newBlock("blkElencoDichiarazioniConsistenza");
    	consistenzaVO = (ConsistenzaVO)iteraDichiarazione.next();
    	String blk = "blkElencoDichiarazioniConsistenza";
    	if(Validator.isEmpty(consistenzaVO.getvAllegatoDichiarazioneVO()))
    	{
    	  blk += ".blkNoNuoviAllegati";
	    } //if no allegati
	    else
	    {	    
	      blk += ".blkEsistonoAllegati";
	    }// else si allegati
	    
	    
	    htmpl.newBlock(blk);
      htmpl.set(blk+".idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
      htmpl.set(blk+".anno", consistenzaVO.getAnno());
      htmpl.set(blk+".data", consistenzaVO.getData());
      if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo())) 
      {
        htmpl.set(blk+".numeroProtocollo", StringUtils.parseNumeroProtocolloField(consistenzaVO.getNumeroProtocollo()));
        htmpl.set(blk+".dataProtocollo", consistenzaVO.getDataProtocolloStr());
      }
      htmpl.set(blk+".motivo", consistenzaVO.getMotivo());
      if(consistenzaVO.isAnomalie()) 
      {
        htmpl.set(blk+".anomalia", "Si");
      }
      else 
      {
        htmpl.set(blk+".anomalia", "");
      }
      // Icona relativa alla validazione dati da parte del SIAN Fascicolo
      if(Validator.isNotEmpty(consistenzaVO.getInvioFascicoliVO()) 
        && SolmrConstants.SCHED_STATO_INVIATO.equalsIgnoreCase(consistenzaVO.getInvioFascicoliVO().getStatoInvio()))
      {
        if("S".equalsIgnoreCase(consistenzaVO.getInvioFascicoliVO().getFlagDatiAnagrafici()))
        {
          htmpl.set(blk+".aggiornamentoSian", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ clessidra, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_WAIT, ""}), null);
        }
        if("S".equalsIgnoreCase(consistenzaVO.getInvioFascicoliVO().getFlagTerreni()))
        {
          htmpl.set(blk+".aggiornamentoSianCT", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ clessidra, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_WAIT, ""}), null);
        }
        if("S".equalsIgnoreCase(consistenzaVO.getInvioFascicoliVO().getFlagUv()))
        {
          htmpl.set(blk+".aggiornamentoSianUV", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ clessidra, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_WAIT, ""}), null);
        }
        if("S".equalsIgnoreCase(consistenzaVO.getInvioFascicoliVO().getFlagFabbricati()))
        {
          htmpl.set(blk+".aggiornamentoSianFab", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ clessidra, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_WAIT, ""}), null);
        }
        if("S".equalsIgnoreCase(consistenzaVO.getInvioFascicoliVO().getFlagCc()))
        {
          htmpl.set(blk+".aggiornamentoSianCC", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ clessidra, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_WAIT, ""}), null);
        }
       
      }
      else
      {
        if(consistenzaVO.getDataAggiornamentoFascicolo() != null) 
        {
          if(Validator.isNotEmpty(consistenzaVO.getFlagAnomalia()) && consistenzaVO.getFlagAnomalia().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
          {
            htmpl.set(blk+".aggiornamentoSian", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_OK, ""}), null);
          }
          else 
          {
            htmpl.set(blk+".aggiornamentoSian", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_KO, ""}), null);             
          }
        }
        // Icona relativa alla validazione dati da parte del SIAN Consistenza Territoriale      
        if(consistenzaVO.getDataAggiornamentoColtura() != null) 
        {
          if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaColtura()) && consistenzaVO.getFlagAnomaliaColtura().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
          {
            htmpl.set(blk+".aggiornamentoSianCT", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_OK, ""}), null);
          }
          else 
          {
            htmpl.set(blk+".aggiornamentoSianCT", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_KO, ""}), null);              
          }
        }
        // Icona relativa alla validazione dati da parte del SIAN UV
        if(consistenzaVO.getDataAggiornamentoUV() != null) 
        {
          if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaUV()) && consistenzaVO.getFlagAnomaliaUV().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
          {
            htmpl.set(blk+".aggiornamentoSianUV", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_OK, ""}), null);
          }
          else 
          {
            htmpl.set(blk+".aggiornamentoSianUV", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_KO, ""}), null);              
          }
        }
        // Icona relativa alla validazione dati da parte del SIAN Fabbricati
        if(consistenzaVO.getDataAggiornamentoFabbricati() != null) 
        {
          if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaFabbricati()) && consistenzaVO.getFlagAnomaliaFabbricati().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
          {
            htmpl.set(blk+".aggiornamentoSianFab", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_OK, ""}), null);
          }
          else 
          {
            htmpl.set(blk+".aggiornamentoSianFab", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_KO, ""}), null);              
          }
        }
        // Icona relativa alla validazione dati da parte del SIAN ContiCorrenti
        if(consistenzaVO.getDataAggiornamentoCC() != null) 
        {
          if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaCC()) && consistenzaVO.getFlagAnomaliaCC().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
          {
            htmpl.set(blk+".aggiornamentoSianCC", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_OK, ""}), null);
          }
          else 
          {
            htmpl.set(blk+".aggiornamentoSianCC", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_KO, ""}), null);              
          }
        }
      }
      
      if(Validator.isNotEmpty(consistenzaVO.getIdAccessoPianoGrafico()))
      {
        htmpl.set(blk+".descPianoGrafico", "Piano grafico generato");
        htmpl.set(blk+".classPianoGrafico", SolmrConstants.IMMAGINE_PIANO_GRAFICO);
      }
      
      //Visualizzazione stampa!!
      htmpl.set(blk+".indice", ""+indice);
      if(Validator.isNotEmpty(consistenzaVO.getExtIdDocumentoIndex()))
      {
        htmpl.newBlock(blk+".blkFileAllegato");
        htmpl.set(blk+".blkFileAllegato.idAllegato", ""+consistenzaVO.getIdAllegato());
        String titleAllegato = "";
        String immagineStampa = "";
        if(Validator.isNotEmpty(consistenzaVO.getStileFirma()))
        {
          titleAllegato = consistenzaVO.getDescrizioneTipoFirma();
          immagineStampa = consistenzaVO.getStileFirma();
        }
        else
        {
          titleAllegato = "Non firmata";
          immagineStampa = "noFirma";
        }
        
        htmpl.set(blk+".blkFileAllegato.titleAllegato", titleAllegato);
        htmpl.set(blk+".blkFileAllegato.immagineStampa", immagineStampa);  
      }
      else
      { 
        if(Validator.isNotEmpty(consistenzaVO.getIdAllegato()))
        {
          //Caso in cui ci sono i file  ma andati male (id allegato valorizzato ext id documento no)
	        if(Validator.isNotEmpty(fallitaStampa))
	        {
	          htmpl.newBlock(blk+".blkRifareStampa");
	          htmpl.set(blk+".blkRifareStampa.idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
	          htmpl.set(blk+".blkRifareStampa.indice", ""+indice);
	        }
	        else
	        {                   
	          Date dataMaxDelay = null;
	          if(Validator.isNotEmpty(consistenzaVO.getDataRichiestaStampa()))
	          {
	            dataMaxDelay = consistenzaVO.getDataRichiestaStampa();
	          }
	          else
	          {
	            dataMaxDelay = parametroDataSTMPLivecycle;
	          }
	          Calendar cal = new GregorianCalendar();
	          cal.setTime(dataMaxDelay);
	          cal.add(Calendar.MILLISECOND, parametroDeleyNewStampa.intValue());
	          Date dataAttuale = new Date();
	          dataMaxDelay = cal.getTime();
	          //se la data corrente super la dataMaxDelay, vuol dire che la stampa
	          //è andata male quindi devo dare la possibilità di ricrearla....  
	          if(dataAttuale.after(dataMaxDelay))
	          {
	            htmpl.newBlock(blk+".blkRifareStampa");
	            htmpl.set(blk+".blkRifareStampa.idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
	            htmpl.set(blk+".blkRifareStampa.indice", ""+indice);
	          }
	          else
	          {         
	            htmpl.newBlock(blk+".blkAttendereStampa");
	          }
	        }
        }
      }
      
      
      if(consistenzaVO.getEsistePratica() == null) 
      {
        htmpl.newBlock(blk+".blkErroreSqlPratica");
        htmpl.set(blk+".blkErroreSqlPratica.err_pratica",
        java.text.MessageFormat.format(htmlStringKO, new Object[] {pathErrori + "/"+ imko, "", ""}), null);
      }
      else 
      {
        if(consistenzaVO.getEsistePratica().booleanValue()) 
        {
          htmpl.newBlock(blk+".blkImmaginePratica");
          htmpl.set(blk+".blkImmaginePratica.idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
        }
        else 
        {
          htmpl.newBlock(blk+".blkNoImmaginePratica");
        }
      }
      
      if(Validator.isNotEmpty(consistenzaVO.getvAllegatoDichiarazioneVO()))
      {
	      htmpl.set(blk+".aggancio", "trAggancio"+indice);
	      Vector<AllegatoDichiarazioneVO> vAllegato = consistenzaVO.getvAllegatoDichiarazioneVO();
	      
	      for(int h=0;h<vAllegato.size();h++)
	      {
	        AllegatoDichiarazioneVO allDichiaraVO =  vAllegato.get(h);
	        htmpl.newBlock(blk+".blkElencoAllegati");
	        htmpl.set(blk+".blkElencoAllegati.aggancio", "trAggancio"+indice);
	        htmpl.set(blk+".blkElencoAllegati.descTipoAllegato", allDichiaraVO.getDescTipoAllegato());
	        if(Validator.isNotEmpty(allDichiaraVO.getIdAllegato()))
	          htmpl.set(blk+".blkElencoAllegati.dataInizioAllegato", DateUtils.formatDateTimeNotNull(allDichiaraVO.getDataInizioValidita()));
	        
	        htmpl.set(blk+".blkElencoAllegati.indice", ""+indice);
	        
	        
	        if("S".equalsIgnoreCase(allDichiaraVO.getFlagDaFirmare()))
	        {
	          htmpl.newBlock(blk+".blkElencoAllegati.blkFirmaAllegato");
	          htmpl.set(blk+".blkElencoAllegati.blkFirmaAllegato.idDichiarazioneConsistenzaIdAllegato", 
	            consistenzaVO.getIdDichiarazioneConsistenza()+"_"+allDichiaraVO.getIdAllegato());
	          //htmpl.set(blk+".blkElencoAllegati.blkFirmaAllegato.idAllegato", ""+allDichiaraVO.getIdAllegato());	        
	        }
	        
	        
		      if(Validator.isNotEmpty(allDichiaraVO.getExtIdDocumentoIndex()))
		      {
		        htmpl.newBlock(blk+".blkElencoAllegati.blkFileAllegato");
		        htmpl.set(blk+".blkElencoAllegati.blkFileAllegato.idAllegato", ""+allDichiaraVO.getIdAllegato());
		        String titleAllegato = "";
		        String immagineStampa = "";
		        if(Validator.isNotEmpty(vAllegato.get(h).getStileFirma()))
		        {
		          titleAllegato = vAllegato.get(h).getDescrizioneTipoFirma();
		          immagineStampa = vAllegato.get(h).getStileFirma();
		        }
		        else
		        {
		          titleAllegato = "Non firmata";
		          immagineStampa = "noFirma";
		        }
		        
		        htmpl.set(blk+".blkElencoAllegati.blkFileAllegato.titleAllegato", titleAllegato);
		        htmpl.set(blk+".blkElencoAllegati.blkFileAllegato.immagineStampa", immagineStampa);  
		      }
		      else
		      {		      
		        if(Validator.isNotEmpty(allDichiaraVO.getIdAllegato()))
		        {
		          if(Validator.isNotEmpty(fallitaStampa))
	            {
	              htmpl.newBlock(blk+".blkElencoAllegati.blkRifareStampa");
	              htmpl.set(blk+".blkElencoAllegati.blkRifareStampa.idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
	              htmpl.set(blk+".blkElencoAllegati.blkRifareStampa.indice", ""+indice);
	              htmpl.set(blk+".blkElencoAllegati.blkRifareStampa.idTipoAllegato", ""+allDichiaraVO.getIdTipoAllegato());
	            }
	            else
	            { 		        
			          Date dataMaxDelay = null;
	              if(Validator.isNotEmpty(consistenzaVO.getDataRichiestaStampa()))
	              {
	                dataMaxDelay = consistenzaVO.getDataRichiestaStampa();
	              }
	              else
	              {
	                dataMaxDelay = allDichiaraVO.getDataInizioValidita();
	              }
	              Calendar cal = new GregorianCalendar();
	              cal.setTime(dataMaxDelay);
	              cal.add(Calendar.MILLISECOND, parametroDeleyNewStampa.intValue());
	              Date dataAttuale = new Date();
	              dataMaxDelay = cal.getTime();
	              //se la data corrente super la dataMaxDelay, vuol dire che la stampa
	              //è andata male quindi devo dare la possibilità di ricrearla....  
	              if(dataAttuale.after(dataMaxDelay))
	              {
	                htmpl.newBlock(blk+".blkElencoAllegati.blkRifareStampa");
	                htmpl.set(blk+".blkElencoAllegati.blkRifareStampa.idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
	                htmpl.set(blk+".blkElencoAllegati.blkRifareStampa.indice", ""+indice);
	                htmpl.set(blk+".blkElencoAllegati.blkRifareStampa.idTipoAllegato", ""+allDichiaraVO.getIdTipoAllegato());
	              }
	              else
	              {         
	                htmpl.newBlock(blk+".blkElencoAllegati.blkAttendereStampa");
	              }
	            }
		        }
		        
		      }
	      }
	    
	    }
    	
    	indice++;
    }
  }
  else 
  {
    htmpl.newBlock("noDichiarazioniConsistenza");
  }
  out.print(htmpl.text());

  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
   	htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }


%>

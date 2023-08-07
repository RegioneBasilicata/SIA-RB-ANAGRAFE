<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/allevamenti_det.htm");

  Htmpl htmpl = new Htmpl(layout);
  AllevamentoAnagVO all = (AllevamentoAnagVO) request.getAttribute("allevamento");
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  ValidationErrors errors = (ValidationErrors) request.getAttribute("errors");
  RuoloUtenza ruoloUtenza = (RuoloUtenza) session.getAttribute("ruoloUtenza");
  HtmplUtil.setErrors(htmpl, errors, request, application);
  Date parametroAllSuGnps = (Date)request.getAttribute("parametroAllSuGnps");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  htmpl.set("idDichiarazioneConsistenza", request.getParameter("idDichiarazioneConsistenza"));

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
  Date dataDettaglioAllevamenti = (Date)request.getAttribute("dataDettaglioAllevamenti");
  Date dataInserimentoDichiarazione = (Date)request.getAttribute("dataInserimentoDichiarazione");
  if(Validator.isEmpty(dataInserimentoDichiarazione))
    dataInserimentoDichiarazione = new Date();
  String idAnno = (String) request.getAttribute("idAnno");
  if (Validator.isNotEmpty(idAnno))
  {
    htmpl.set("idAnno", idAnno);
  }

  AnagFacadeClient client = new AnagFacadeClient();
  if (anagAziendaVO.getCUAA() != null && !anagAziendaVO.getCUAA().equals(""))
  {
    htmpl.set("CUAA", anagAziendaVO.getCUAA());
  }
  htmpl.set("denominazione", anagAziendaVO.getDenominazione());
  htmpl.set("dataSituazioneAlStr", anagAziendaVO.getDataSituazioneAlStr());

  TipoSpecieAnimaleAnagVO specie = all.getTipoSpecieAnimaleAnagVO();

  if (all != null)
  {
    UteVO ute = client.getUteById(all.getIdUTELong());
    TipoASLAnagVO asl = all.getTipoASLAnagVO();
    
    
    htmpl.set("denominazioneAllevamento", all.getDenominazione());
    htmpl.set("dataInizioAttivita", all.getDataInizioAttivita());

    ComuneVO comune = client.getComuneByISTAT(ute.getIstat());
    htmpl.set("comuneUnitaProduttiva", comune.getDescom() + " (" + client.getSiglaProvinciaByIstatProvincia(comune.getIstatProvincia()) + ")");
    if (Validator.isNotEmpty(ute.getIndirizzo()))
    {
      htmpl.set("indirizzoUnitaProduttiva", "- " + ute.getIndirizzo());
    }
    htmpl.set("codiceAziendaZootecnica", all.getCodiceAziendaZootecnica());
    htmpl.set("cap", all.getCap());
    htmpl.set("indirizzo", all.getIndirizzo());
    htmpl.set("telefono", all.getTelefono());
    htmpl.set("longitudine", Formatter.formatDouble6(all.getLongitudine()));
    
    htmpl.set("latitudine", Formatter.formatDouble6(all.getLatitudine()));
    
    
    
    htmpl.set("descrizioneASL", asl.getDescrizione());
    comune = client.getComuneByISTAT(asl.getIstatComune());
    htmpl.set("comuneASL", comune.getDescom());
    comune = client.getComuneByISTAT(all.getIstatComuneAllevamento());
    htmpl.set("provinciaAllevamento", client.getSiglaProvinciaByIstatProvincia(comune.getIstatProvincia()));
    htmpl.set("comuneAllevamento", comune.getDescom());
    htmpl.set("descTipoProduzione", all.getDescTipoProduzione());
    htmpl.set("descOrientamentoProduttivo", all.getDescOrientamentoProduttivo());
    htmpl.set("descTipoProduzioneCosman", all.getDescTipoProduzioneCosman());
    
    htmpl.set("codiceFiscaleProprietario", all.getCodiceFiscaleProprietario());
    htmpl.set("denominazioneProprietario", all.getDenominazioneProprietario());
    htmpl.set("codiceFiscaleDetentore", all.getCodiceFiscaleDetentore());
    htmpl.set("denominazioneDetentore", all.getDenominazioneDetentore());
    htmpl.set("dataInizioDetenzione", all.getDataInizioDetenzione());
    htmpl.set("dataFineDetenzione", all.getDataFineDetenzione());
     if (all.isFlagSoccida())
      htmpl.set("flagSoccida", SolmrConstants.FLAG_SI);
    else
      htmpl.set("flagSoccida", SolmrConstants.FLAG_NO);
      
    htmpl.set("motivoSoccida", all.getMotivoSoccida());
    
    if (SolmrConstants.PROPRIETARIO.equalsIgnoreCase(all.getFlagAssicuratoCosman())) {
    	htmpl.set("soggettoSoccida", SolmrConstants.DESC_PROPRIETARIO);
    }else if (SolmrConstants.DETENTORE.equalsIgnoreCase(all.getFlagAssicuratoCosman())) {
    	htmpl.set("soggettoSoccida", SolmrConstants.DESC_DETENTORE);
    }
    
    htmpl.set("note", all.getNote());
    
    if(parametroAllSuGnps.after(dataInserimentoDichiarazione))
    {
      htmpl.newBlock("blkLettiera");
      htmpl.set("blkLettiera.superficieLettieraPermanente", all.getSuperficieLettieraPermanente());
      htmpl.set("blkLettiera.altezza", all.getAltezzaLettieraPermanente());
      htmpl.set("blkLettiera.volume",(String) request.getAttribute("volume"));
    }
    
    htmpl.set("validitaDal", all.getDataInizio());
    htmpl.set("validitaAl", all.getDataFine());
    htmpl.set("idAllevamento", all.getIdAllevamento());
   

    UtenteIrideVO utenteAggiornamento = all.getUtenteAggiornamento();

    //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
    //al ruolo!
    ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza, htmpl, "ultimaModificaVw", all.getDataAggiornamento(), utenteAggiornamento.getDenominazione(), utenteAggiornamento
        .getDescrizioneEnteAppartenenza(), null);
    
    String idSpecie=all.getIdSpecieAnimale();
    
    
    
    String unitaMisuraSpecie = all.getTipoSpecieAnimaleAnagVO().getUnitaDiMisura();
    
    
    
    /******************************************************************************/
    /******************************************************************************/
    /****          FINE PARTE RELATIVA ALLA CONSISTENZA ZOOTECNICA             ****/
    /******************************************************************************/
    /******************************************************************************/
    //Vado a vedere se ci sono sottocategorie inserite dall'utente
    @SuppressWarnings("unchecked")
    Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = (Vector<SottoCategoriaAllevamento>) request.getAttribute("sottoCategorieAllevamenti");
    if (sottoCategorieAllevamenti != null && sottoCategorieAllevamenti.size() > 0)
    {
      if(Validator.isNotEmpty(dataInserimentoDichiarazione)
        && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
      {
        htmpl.newBlock("consOldStyle");
	      htmpl.set("consOldStyle.unitaMisuraSpecie", unitaMisuraSpecie);
	      htmpl.set("consOldStyle.specie", specie.getDescrizione());
	
	      int size = sottoCategorieAllevamenti.size();
	      BigDecimal totaliPesi = new BigDecimal(0);
	      int sommaTotaleCapi = 0;
	      double sommaTotaleUBA =0;
	      //Devo visualizzare tante categorie quante sono presenti nel vettore
	      for (int i = 0; i < size; i++)
	      {
	        SottoCategoriaAllevamento sottoCategoria = (SottoCategoriaAllevamento) sottoCategorieAllevamenti.get(i);
	        htmpl.newBlock("consOldStyle.blksottoCategoriaAllevamenti");
	
	        htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.descCategoriaTab", sottoCategoria.getDescCategoriaAnimale());
	        htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.descSottoCategoriaTab", sottoCategoria.getDescSottoCategoriaAnimale());
	        htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.capiTab", sottoCategoria.getQuantita());
	        
	        if (sottoCategoria.getPesoVivo()!=null)          
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.pesoVivoTab", StringUtils.parsePesoCapi(sottoCategoria.getPesoVivo().replace(',','.')));
	        else  
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.pesoVivoTab","");
	
	        //Calcolo il peso vivo totale
	        try
	        {
	          BigDecimal quantita = new BigDecimal(sottoCategoria.getQuantita());
	          BigDecimal pesoVivoUnitario = new BigDecimal(0);
	          if (sottoCategoria.getPesoVivo()!=null)
	            pesoVivoUnitario = new BigDecimal(sottoCategoria.getPesoVivo().replace(',', '.'));
	          
	          BigDecimal totalePesoVivo = quantita.multiply(pesoVivoUnitario);
	          totaliPesi = totaliPesi.add(totalePesoVivo); 
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.pesoVivoTotTab", StringUtils.parsePesoCapi(totalePesoVivo.toString()));
	          sommaTotaleCapi+=quantita.intValue();
	          double totaleUBA = quantita.doubleValue() * sottoCategoria.getCoefficienteUba();
	          sommaTotaleUBA += totaleUBA;
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.totaleUBA", StringUtils.parsePesoCapi(String.valueOf(NumberUtils.arrotonda(totaleUBA, 2))));
	        }
	        catch (Exception e){}
	        if (sottoCategoria.isFlagStallaPascolo())
	        {
	
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.giorniVuotoSanitarioTab", sottoCategoria.getGiorniVuotoSanitario());
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.cicliTab", sottoCategoria.getCicli());
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.giorniPascoloEstateTab", sottoCategoria.getGiorniPascoloEstate());
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.orePascoloEstateTab", sottoCategoria.getOrePascoloEstate());
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.giorniPascoloInvernoTab", sottoCategoria.getGiorniPascoloInverno());
	          htmpl.set("consOldStyle.blksottoCategoriaAllevamenti.orePascoloInvernoTab", sottoCategoria.getOrePascoloInverno());
	        }
	      }
	      if (size>0)
	      {
	        htmpl.newBlock("consOldStyle.blksottoCategoriaAllevamentiTot");
	        htmpl.set("consOldStyle.blksottoCategoriaAllevamentiTot.sommaTotaleCapi", sommaTotaleCapi+"");
	        htmpl.set("consOldStyle.blksottoCategoriaAllevamentiTot.sommaPesoVivoTotale", StringUtils.parsePesoCapi(totaliPesi.toString()));
	        htmpl.set("consOldStyle.blksottoCategoriaAllevamentiTot.sommaTotaleUBA", StringUtils.parsePesoCapi(String.valueOf(NumberUtils.arrotonda(sommaTotaleUBA,2))));
	      }
	     
	      
	    }
		  else
		  {
		    htmpl.newBlock("consNewStyle");
	      htmpl.set("consNewStyle.unitaMisuraSpecie", unitaMisuraSpecie);
	      htmpl.set("consNewStyle.specie", specie.getDescrizione());
	  
	      int size = sottoCategorieAllevamenti.size();
	      BigDecimal totaliPesi = new BigDecimal(0);
	      int sommaTotaleCapi = 0;
        int sommaTotaleCapiProprieta = 0;
        double sommaTotaleUBA =0;
        BigDecimal sommaTotaleAzoto = new BigDecimal(0);
        //Devo visualizzare tante categorie quante sono presenti nel vettore
        for (int i = 0; i < size; i++)
        {
          SottoCategoriaAllevamento sottoCategoria = (SottoCategoriaAllevamento) sottoCategorieAllevamenti.get(i);
          htmpl.newBlock("consNewStyle.blksottoCategoriaAllevamenti");
  
          htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.descCategoriaTab", sottoCategoria.getDescCategoriaAnimale());
          htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.descSottoCategoriaTab", sottoCategoria.getDescSottoCategoriaAnimale());
          
          htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.capiTab", sottoCategoria.getQuantita());
          htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.capiProprietaTab", sottoCategoria.getQuantitaProprieta());
          
          if (sottoCategoria.getPesoVivo()!=null)          
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.pesoVivoTab", StringUtils.parsePesoCapi(sottoCategoria.getPesoVivo().replace(',','.')));
          else  
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.pesoVivoTab","");
            
          
          
          try
	        {
	          long quantita = Long.parseLong(sottoCategoria.getQuantita());
	          if(quantita == 0)
	          {
	            quantita = Long.parseLong(sottoCategoria.getQuantitaProprieta());
	          }
	          double pesoVivo = Double.parseDouble(sottoCategoria.getPesoVivo().replace(',', '.'));
	          double pesoVivoAzoto = sottoCategoria.getPesoVivoAzoto();
	          double ris = pesoVivo * quantita;
	          double risAzoto = ris * pesoVivoAzoto / 1000;
	          BigDecimal risAzotoBg = new BigDecimal(risAzoto);
	          sommaTotaleAzoto = sommaTotaleAzoto.add(risAzotoBg);
	          htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.azotoZootecnicoTab", Formatter.formatAndRoundBigDecimal1(risAzotoBg));
	        }
	        catch (Exception e)
	        {
	          htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.azotoZootecnicoTab", "");
	        }  
            
          
  
          //Calcolo il peso vivo totale
          try
          {
            BigDecimal quantita = new BigDecimal(sottoCategoria.getQuantita());
            if(quantita.compareTo(new BigDecimal(0)) == 0)
            {
              quantita = new BigDecimal(sottoCategoria.getQuantitaProprieta());
            }
            BigDecimal pesoVivoUnitario = new BigDecimal(0);
            if (sottoCategoria.getPesoVivo()!=null)
              pesoVivoUnitario = new BigDecimal(sottoCategoria.getPesoVivo().replace(',', '.'));
            
            BigDecimal totalePesoVivo = quantita.multiply(pesoVivoUnitario);
            totaliPesi = totaliPesi.add(totalePesoVivo); 
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.pesoVivoTotTab", StringUtils.parsePesoCapi(totalePesoVivo.toString()));
            sommaTotaleCapi+=quantita.intValue();
            double totaleUBA = quantita.doubleValue() * sottoCategoria.getCoefficienteUba();
            sommaTotaleUBA += totaleUBA;
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.totaleUBA", StringUtils.parsePesoCapi(String.valueOf(NumberUtils.arrotonda(totaleUBA, 2))));
          }
          catch (Exception e){}
          
          try
          {
            BigDecimal quantitaProprieta = new BigDecimal(sottoCategoria.getQuantitaProprieta());
            sommaTotaleCapiProprieta+=quantitaProprieta.intValue();
          }
          catch (Exception e){}
          
          
          if (sottoCategoria.isFlagStallaPascolo())
          {
  
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.numeroClicliAnnualiTab", sottoCategoria.getNumeroCicliAnnuali());
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.cicliTab", sottoCategoria.getCicli());
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.giorniPascoloEstateTab", sottoCategoria.getGiorniPascoloEstate());
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.orePascoloEstateTab", sottoCategoria.getOrePascoloEstate());
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.giorniPascoloInvernoTab", sottoCategoria.getGiorniPascoloInverno());
            htmpl.set("consNewStyle.blksottoCategoriaAllevamenti.orePascoloInvernoTab", sottoCategoria.getOrePascoloInverno());
          }
        }
        if (size>0)
        {
          htmpl.newBlock("consNewStyle.blksottoCategoriaAllevamentiTot");
          htmpl.set("consNewStyle.blksottoCategoriaAllevamentiTot.sommaTotaleCapi", sommaTotaleCapi+"");
          htmpl.set("consNewStyle.blksottoCategoriaAllevamentiTot.sommaTotaleCapiProprieta", sommaTotaleCapiProprieta+"");
          htmpl.set("consNewStyle.blksottoCategoriaAllevamentiTot.sommaPesoVivoTotale", StringUtils.parsePesoCapi(totaliPesi.toString()));
          htmpl.set("consNewStyle.blksottoCategoriaAllevamentiTot.sommaTotaleAzoto", Formatter.formatDouble1(sommaTotaleAzoto));
          htmpl.set("consNewStyle.blksottoCategoriaAllevamentiTot.sommaTotaleUBA", StringUtils.parsePesoCapi(String.valueOf(NumberUtils.arrotonda(sommaTotaleUBA,2))));
        }
      }
	  
	  }
    
    /******************************************************************************/
    /******************************************************************************/
    /****          FINE PARTE RELATIVA ALLA CONSISTENZA ZOOTECNICA             ****/
    /******************************************************************************/
    /******************************************************************************/
    
    
    
    
    /******************************************************************************/
    /******************************************************************************/
    /****               INIZIO PARTE RELATIVA ALLE STABULAZIONI                ****/
    /******************************************************************************/
    /******************************************************************************/

    //Le stabulazioni hanno senso solo se flagStallaPascolo è true
    if (all.getTipoSpecieAnimaleAnagVO().isFlagStallaPascolo() && parametroAllSuGnps.after(dataInserimentoDichiarazione))
    {
      if(Validator.isNotEmpty(dataInserimentoDichiarazione)
        && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
      {
	      htmpl.newBlock("stabOldStyle");
		    htmpl.newBlock("stabOldStyle.blkStabulazioniInsert");
		    htmpl.set("stabOldStyle.blkStabulazioniInsert.descrizioneAltriTrattam", all.getDescrizioneAltriTrattam());
		    //Vado a vedere se ci sono stabulazioni inserite dall'utente
		    @SuppressWarnings("unchecked")
		    Vector<StabulazioneTrattamento> stabulazioniTrattamenti = (Vector<StabulazioneTrattamento>) request.getAttribute("stabulazioniTrattamenti");
		    if (stabulazioniTrattamenti != null && stabulazioniTrattamenti.size() > 0)
		    {
		      htmpl.newBlock("stabOldStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti");
		      htmpl.set("stabOldStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti.unitaMisuraSpecie", unitaMisuraSpecie);
		
		      int size = stabulazioniTrattamenti.size();
		      //Devo visualizzare tante categorie quante sono presenti nel vettore
		      
		      long quantitaTotL=0;
		      BigDecimal palabileTotB = new BigDecimal(0);
		      BigDecimal nonPalabileTotB = new BigDecimal(0);
		      BigDecimal palabileTratTotB = new BigDecimal(0);
		      BigDecimal nonPalabileTratTotB = new BigDecimal(0);
		      BigDecimal totaleAzotoTotB = new BigDecimal(0);
		      
		      for (int i = 0; i < size; i++)
		      {
		        StabulazioneTrattamento stabulazione = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
		        String blk = "stabOldStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti.blkStabulazioneTrattamento";
		        htmpl.newBlock(blk);
		        //htmpl.set(blk + ".idStabulazioneTab", "" + i);        
	        
	        
		        htmpl.set(blk + ".descCategoria", stabulazione.getDescCategoria());
		        htmpl.set(blk + ".descSottoCategoria", stabulazione.getDescSottoCategoria());
		        htmpl.set(blk + ".stabulazioneDesc", stabulazione.getDescStabulazione());
		        htmpl.set(blk + ".quantitaStab", stabulazione.getQuantita());
		        
		        long quantitaL=Long.parseLong(stabulazione.getQuantita());
		        BigDecimal palabileB = new BigDecimal(stabulazione.getPalabile().replace(',', '.'));
		        BigDecimal nonPalabileB = new BigDecimal(stabulazione.getNonPalabile().replace(',', '.'));
		        BigDecimal palabileTratB = new BigDecimal(stabulazione.getPalabileTrat().replace(',', '.'));
		        BigDecimal nonPalabileTratB = new BigDecimal(stabulazione.getNonPalabileTrat().replace(',', '.'));
		        BigDecimal totaleAzotoB = new BigDecimal(stabulazione.getTotaleAzoto().replace(',', '.'));
		        
		        htmpl.set(blk + ".palabile", StringUtils.parsePesoCapi(palabileB.toString()));
		        htmpl.set(blk + ".nonPalabile", StringUtils.parsePesoCapi(nonPalabileB.toString()));
		        
		        htmpl.set(blk + ".idTrattamentoStab", stabulazione.getIdTrattamento());
		        htmpl.set(blk + ".trattamentiDesc", stabulazione.getDescTrattamento());
		        
		        
		        htmpl.set(blk + ".palabileTrat", StringUtils.parsePesoCapi(palabileTratB.toString()));
		        htmpl.set(blk + ".nonPalabileTrat", StringUtils.parsePesoCapi(nonPalabileTratB.toString()));
		        htmpl.set(blk + ".totaleAzoto", StringUtils.parsePesoCapi(totaleAzotoB.toString()));
		        
		        quantitaTotL+=quantitaL;
		        palabileTotB=palabileTotB.add(palabileB);
		        nonPalabileTotB=nonPalabileTotB.add(nonPalabileB);
		        palabileTratTotB=palabileTratTotB.add(palabileTratB);
		        nonPalabileTratTotB=nonPalabileTratTotB.add(nonPalabileTratB);
		        totaleAzotoTotB=totaleAzotoTotB.add(totaleAzotoB);
		        
		        
		      }
		      String blk = "stabOldStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti.blkStabulazioneTrattamentoTot";
		      htmpl.newBlock(blk);
		      htmpl.set(blk + ".quantitaStab", ""+quantitaTotL);
		      htmpl.set(blk + ".palabile", StringUtils.parsePesoCapi(palabileTotB.toString()));
		      htmpl.set(blk + ".nonPalabile", StringUtils.parsePesoCapi(nonPalabileTotB.toString()));
		      htmpl.set(blk + ".palabileTrat", StringUtils.parsePesoCapi(palabileTratTotB.toString()));
		      htmpl.set(blk + ".nonPalabileTrat", StringUtils.parsePesoCapi(nonPalabileTratTotB.toString()));
		      htmpl.set(blk + ".totaleAzoto", StringUtils.parsePesoCapi(totaleAzotoTotB.toString()));
		      
		    }
		  }
		  else
		  {
		    htmpl.newBlock("stabNewStyle");
	      htmpl.newBlock("stabNewStyle.blkStabulazioniInsert");
	      //Vado a vedere se ci sono stabulazioni inserite dall'utente
	      
	      Vector<StabulazioneTrattamento> stabulazioniTrattamenti = (Vector<StabulazioneTrattamento>) request.getAttribute("stabulazioniTrattamenti");
	      if (stabulazioniTrattamenti != null && stabulazioniTrattamenti.size() > 0)
	      {
	        htmpl.newBlock("stabNewStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti");
	        htmpl.set("stabNewStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti.unitaMisuraSpecie", unitaMisuraSpecie);
	  
	        int size = stabulazioniTrattamenti.size();
	        //Devo visualizzare tante categorie quante sono presenti nel vettore
	        
	        long quantitaTotL=0;
	        
          BigDecimal escretoAlPascoloTotB = new BigDecimal(0);
	        BigDecimal palabileTotB = new BigDecimal(0);
	        BigDecimal palabileTAnnoTotB = new BigDecimal(0);
	        BigDecimal azotoPalabileTotB = new BigDecimal(0);
	        BigDecimal nonPalabileTotB = new BigDecimal(0);
	        BigDecimal azotoNonPalabileTotB = new BigDecimal(0);
	        
	        SottoCategoriaAllevamento sottoCategoriaCalcoli = null;
	        
	        for (int i = 0; i < size; i++)
	        {
	          StabulazioneTrattamento stabulazione = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
	          
	          
	          //BaseCodeDescription[] stabulazioni = clientGaa.getTipiStabulazione(Long.parseLong(stabulazione.getIdSottoCategoriaAnimale()));

	          for (int j = 0; j < sottoCategorieAllevamenti.size(); j++)
	          {
	            if(sottoCategorieAllevamenti.get(j).getIdSottoCategoriaAnimale() 
	              == new Long(stabulazione.getIdSottoCategoriaAnimale()).longValue())
	            {
	              sottoCategoriaCalcoli = sottoCategorieAllevamenti.get(j);
	            }           
	          }
	          
	          
	          
	          
	          String blk = "stabNewStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti.blkStabulazioneTrattamento";
	          htmpl.newBlock(blk);
	          //htmpl.set(blk + ".idStabulazioneTab", "" + i);
	    
	           
	          
	          
	          
	          
	          htmpl.set(blk + ".descCategoria", stabulazione.getDescCategoria());
	          htmpl.set(blk + ".descSottoCategoria", stabulazione.getDescSottoCategoria());
	          htmpl.set(blk + ".stabulazioneDesc", stabulazione.getDescStabulazione());
	          htmpl.set(blk + ".quantitaStab", stabulazione.getQuantita());
	          
	          long quantitaL=Long.parseLong(stabulazione.getQuantita());
	          BigDecimal permanenzaInStallaB = new BigDecimal(0);
            BigDecimal escretoAlPascoloB = new BigDecimal(0);
	          try
            {
              long quantita = Long.parseLong(stabulazione.getQuantita());
              double pesoVivo = Double.parseDouble(sottoCategoriaCalcoli.getPesoVivo().replace(',', '.'));
              double pesoVivoAzoto = sottoCategoriaCalcoli.getPesoVivoAzoto();
              double risPermanenza = sottoCategoriaCalcoli.ggPermanenzaStalla();
              double risEscreto = pesoVivo / 1000 * quantita * pesoVivoAzoto * (1-(risPermanenza / 365));
              
              permanenzaInStallaB = new BigDecimal(risPermanenza);
              escretoAlPascoloB = new BigDecimal(risEscreto);
            }
            catch (Exception e)
            {}       
	          
	          BigDecimal palabileB = new BigDecimal(stabulazione.getPalabile().replace(',', '.'));
	          BigDecimal palabileTAnnoB = new BigDecimal(stabulazione.getPalabileTAnno().replace(',', '.'));
	          BigDecimal azotoPalabileB = new BigDecimal(stabulazione.getAzotoPalabile().replace(',', '.'));
	          BigDecimal nonPalabileB = new BigDecimal(stabulazione.getNonPalabile().replace(',', '.'));
	          BigDecimal azotoNonPalabileB = new BigDecimal(stabulazione.getAzotoNonPalabile().replace(',', '.'));
	          
	          
	         
	          htmpl.set(blk + ".permanenzaInStalla", StringUtils.parsePesoCapi(permanenzaInStallaB.toString()));
            htmpl.set(blk + ".escretoAlPascolo", StringUtils.parsePesoCapi(escretoAlPascoloB.toString()));
	          
	          htmpl.set(blk + ".palabile", StringUtils.parsePesoCapi(palabileB.toString()));	          
	          htmpl.set(blk + ".palabileTAnno", StringUtils.parsePesoCapi(palabileTAnnoB.toString()));
		        htmpl.set(blk + ".azotoPalabile", StringUtils.parsePesoCapi(azotoPalabileB.toString()));
		        htmpl.set(blk + ".nonPalabile", StringUtils.parsePesoCapi(nonPalabileB.toString()));
		        htmpl.set(blk + ".azotoNonPalabile", StringUtils.parsePesoCapi(azotoNonPalabileB.toString()));
	     
	          BigDecimal totaleAzotoB = new BigDecimal(stabulazione.getTotaleAzoto().replace(',', '.'));
	          htmpl.set(blk + ".totaleAzoto", StringUtils.parsePesoCapi(totaleAzotoB.toString()));
	          
	          quantitaTotL+=quantitaL;
	         
	          escretoAlPascoloTotB=escretoAlPascoloTotB.add(escretoAlPascoloB);
	          palabileTotB=palabileTotB.add(palabileB);
	          palabileTAnnoTotB=palabileTAnnoTotB.add(palabileTAnnoB);
	          azotoPalabileTotB = azotoPalabileTotB.add(azotoPalabileB);
	          nonPalabileTotB=nonPalabileTotB.add(nonPalabileB);
	          azotoNonPalabileTotB = azotoNonPalabileTotB.add(azotoNonPalabileB);
	          
	          
	        }
	        String blk = "stabNewStyle.blkStabulazioniInsert.blkStabulazioniTrattamenti.blkStabulazioneTrattamentoTot";
	        htmpl.newBlock(blk);
	        htmpl.set(blk + ".quantitaStab", ""+quantitaTotL);
          htmpl.set(blk + ".escretoAlPascolo", StringUtils.parsePesoCapi(escretoAlPascoloTotB.toString()));
	        htmpl.set(blk + ".palabile", StringUtils.parsePesoCapi(palabileTotB.toString()));
	        htmpl.set(blk + ".palabileTAnno", StringUtils.parsePesoCapi(palabileTAnnoTotB.toString()));
	        htmpl.set(blk + ".azotoPalabile", StringUtils.parsePesoCapi(azotoPalabileTotB.toString()));
	        htmpl.set(blk + ".nonPalabile", StringUtils.parsePesoCapi(nonPalabileTotB.toString()));
	        htmpl.set(blk + ".azotoNonPalabile", StringUtils.parsePesoCapi(azotoNonPalabileTotB.toString()));
	        
	      }
		  
		  }
	  }
	  /******************************************************************************/
	  /******************************************************************************/
	  /****               FINE PARTE RELATIVA ALLE STABULAZIONI                  ****/
	  /******************************************************************************/
	  /******************************************************************************/
    
    
    
    //Parte riservata agli avicoli
    if (AllevamentoAnagVO.ID_AVICOLI.equals(idSpecie) && parametroAllSuGnps.after(dataInserimentoDichiarazione))
    {
      htmpl.newBlock("blkSoloAvicoli");
      if (SolmrConstants.FLAG_S.equals(all.getFlagDeiezioneAvicoli()))
        htmpl.set("blkSoloAvicoli.flagDeiezioneAvicoli", SolmrConstants.FLAG_SI);
      else
        htmpl.set("flagDeiezioneAvicoli", SolmrConstants.FLAG_NO);
    }
    
    
    
    /******************************************************************************/
    /******************************************************************************/
    /****        INIZIO PARTE RELATIVA ALLA STRUTTURE DI MUNGITURA             ****/
    /******************************************************************************/
    /******************************************************************************/

    //Questa sezione è da prevedere unicamente se nella consistenza zootecnica è 
    //stata definita una specie animali Bovini o Ovini o Caprini.
    if ((AllevamentoAnagVO.ID_SPECIE_BOVINI_CARNE.equals(idSpecie) || AllevamentoAnagVO.ID_SPECIE_BOVINI_ALLEVAMENTO.equals(idSpecie) || AllevamentoAnagVO.ID_SPECIE_OVINI.equals(idSpecie)
        || AllevamentoAnagVO.ID_SPECIE_CAPRINI.equals(idSpecie)) && parametroAllSuGnps.after(dataInserimentoDichiarazione))
    {
      if(Validator.isNotEmpty(dataInserimentoDichiarazione)
        && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
      {
	      htmpl.newBlock("mungOldStyle");
	      htmpl.newBlock("mungOldStyle.blkStruttureMungitura");
	      htmpl.set("mungOldStyle.blkStruttureMungitura.mediaCapiLattazione", all.getMediaCapiLattazione());
	      htmpl.set("mungOldStyle.blkStruttureMungitura.quantitaAcquaLavaggio", StringUtils.parsePesoCapi(all.getQuantitaAcquaLavaggio())); 
	      
	      
	      
	      if(Validator.isNotEmpty(all.getQuantitaAcquaLavaggio()) && Validator.isNotEmpty(all.getMediaCapiLattazione()))
	      {
	        double quantitaAcquaLavaggioDb = Double.parseDouble(all.getQuantitaAcquaLavaggio().replace(',', '.'));
	        double mediaCapiLattazioneDb = Double.parseDouble(all.getMediaCapiLattazione());
	        
	        
	        double litriAcquaCapoGiornoDb =  quantitaAcquaLavaggioDb * 1000 / 365  / mediaCapiLattazioneDb;
	        
	        htmpl.set("mungOldStyle.blkStruttureMungitura.litriAcquaCapoGiorno", StringUtils.parseIntegerField(String.valueOf(NumberUtils.arrotonda(litriAcquaCapoGiornoDb,0))));
	      }
	      
	
	      if (SolmrConstants.FLAG_N.equals(all.getFlagAcqueEffluenti()))
	        htmpl.set("mungOldStyle.blkStruttureMungitura.flagAcqueEffluenti", SolmrConstants.FLAG_NO);
	      else if ((SolmrConstants.FLAG_S.equals(all.getFlagAcqueEffluenti())))
	        htmpl.set("mungOldStyle.blkStruttureMungitura.flagAcqueEffluenti", SolmrConstants.FLAG_SI);
	
	      if (AllevamentoAnagVO.ID_SPECIE_BOVINI_CARNE.equals(idSpecie) || AllevamentoAnagVO.ID_SPECIE_BOVINI_ALLEVAMENTO.equals(idSpecie))
	      {
	        //Visibile ed obbligatorio se la specie dichiarata in consistenza zootecnica 
	        //è bovini altrimenti questo campo non è visibile e null
	        htmpl.newBlock("mungOldStyle.blkStruttureMungitura.blkTipologiaStruttureMungitura");
	        htmpl.set("mungOldStyle.blkStruttureMungitura.blkTipologiaStruttureMungitura.descMungitura", all.getDescMungitura());
	
	      }
	    }
	    else
	    {
	      htmpl.newBlock("mungNewStyle");
        htmpl.newBlock("mungNewStyle.blkStruttureMungitura");
        htmpl.set("mungNewStyle.blkStruttureMungitura.mediaCapiLattazione", all.getMediaCapiLattazione());
        htmpl.set("mungNewStyle.blkStruttureMungitura.quantitaAcquaLavaggio", StringUtils.parsePesoCapi(all.getQuantitaAcquaLavaggio())); 
        
        
        
        if(Validator.isNotEmpty(all.getQuantitaAcquaLavaggio()) && Validator.isNotEmpty(all.getMediaCapiLattazione()))
        {
          double quantitaAcquaLavaggioDb = Double.parseDouble(all.getQuantitaAcquaLavaggio().replace(',', '.'));
          double mediaCapiLattazioneDb = Double.parseDouble(all.getMediaCapiLattazione());
          
          
          double litriAcquaCapoGiornoDb =  quantitaAcquaLavaggioDb * 1000 / 365  / mediaCapiLattazioneDb;
          
          htmpl.set("mungNewStyle.blkStruttureMungitura.litriAcquaCapoGiorno", StringUtils.parseIntegerField(String.valueOf(NumberUtils.arrotonda(litriAcquaCapoGiornoDb,0))));
        }
        
        
        
        Vector<AllevamentoAcquaLavaggio> vAllevamentoAcquaLavaggio = (Vector<AllevamentoAcquaLavaggio>)request.getAttribute("vAllevamentoAcquaLavaggio");
        if(Validator.isNotEmpty(vAllevamentoAcquaLavaggio))
        {
          int size = vAllevamentoAcquaLavaggio.size();
	        boolean first = true;
	        for(int i = 0; i<size; i++) 
	        {
	          htmpl.newBlock("mungNewStyle.blkStruttureMungitura.blkDestinoAcquaLavaggio");
	            
	          String blk = "";
	          if(first)
	          {
	            blk = "mungNewStyle.blkStruttureMungitura.blkDestinoAcquaLavaggio.blkPrimoAcquaLavaggio";
	            htmpl.newBlock(blk);
	          
	            htmpl.set(blk+".numDestinoAcqualLavaggio", ""+size);
	            htmpl.set(blk+".descDestinoAcquaLavaggio", vAllevamentoAcquaLavaggio.get(i).getDescDestinoAcquaLavaggio());
	            htmpl.set(blk+".destQuantitaAcquaLavaggio",  vAllevamentoAcquaLavaggio.get(i).getQuantitaAcquaLavaggioStr());
	                    
	            first = false;
	          }
	          else
	          {
	            blk =  "mungNewStyle.blkStruttureMungitura.blkDestinoAcquaLavaggio.blkSecondoAcquaLavaggio";
	            htmpl.newBlock(blk);
	                     
              htmpl.set(blk+".descDestinoAcquaLavaggio", vAllevamentoAcquaLavaggio.get(i).getDescDestinoAcquaLavaggio());
              htmpl.set(blk+".destQuantitaAcquaLavaggio",  vAllevamentoAcquaLavaggio.get(i).getQuantitaAcquaLavaggioStr());
	          }
	        }
	      }
	            
	            
        
  
        
  
        if (AllevamentoAnagVO.ID_SPECIE_BOVINI_CARNE.equals(idSpecie) || AllevamentoAnagVO.ID_SPECIE_BOVINI_ALLEVAMENTO.equals(idSpecie))
        {
          //Visibile ed obbligatorio se la specie dichiarata in consistenza zootecnica 
          //è bovini altrimenti questo campo non è visibile e null
          htmpl.newBlock("mungNewStyle.blkStruttureMungitura.blkTipologiaStruttureMungitura");
          htmpl.set("mungNewStyle.blkStruttureMungitura.blkTipologiaStruttureMungitura.descMungitura", all.getDescMungitura());
  
        }
	    
	    
	    }
    }

    /******************************************************************************/
    /******************************************************************************/
    /****         FINE PARTE RELATIVA ALLA STRUTTURE DI MUNGITURA             ****/
    /******************************************************************************/
    /******************************************************************************/
    
    
    
    
    //Dati biologico
    Vector<AllevamentoBioVO> vAllevamentiBioVO = (Vector<AllevamentoBioVO>)request.getAttribute("vAllevamentiBioVO");
    if(vAllevamentiBioVO != null)
    {
      //Prendo la prima data poichè dovrebbero essere tutte uguali!!!!
      htmpl.set("dataAggiornamentoAbio", "del " +DateUtils.formatDateNotNull(vAllevamentiBioVO.get(0).getDataInizioValidita())); 
      htmpl.newBlock("blkDatiAbio");
      for(int i=0;i<vAllevamentiBioVO.size();i++)
      {
        if(i==0)
        {
          if(Validator.isNotEmpty(vAllevamentiBioVO.get(i).getUnitaMisura()))
          {
            String unitaMisura = "("+vAllevamentiBioVO.get(i).getUnitaMisura()+")";
            htmpl.set("blkDatiAbio.unitaMisura", unitaMisura);
          }    
        }
        htmpl.newBlock("blkDatiAbio.blkElencoDatiAbio");
        
        htmpl.set("blkDatiAbio.blkElencoDatiAbio.descCategoria", vAllevamentiBioVO.get(i).getDescrizioneCategoria());
        htmpl.set("blkDatiAbio.blkElencoDatiAbio.supConvenzionale", Formatter.formatDouble(
          vAllevamentiBioVO.get(i).getQuantitaConvenzionale()));
        htmpl.set("blkDatiAbio.blkElencoDatiAbio.supBiologico", Formatter.formatDouble(
          vAllevamentiBioVO.get(i).getQuantitaBiologico()));
       
      }
    }
    else
    {
      htmpl.newBlock("blkNoDatiAbio");
      htmpl.set("blkNoDatiAbio.nessunDato", SolmrConstants.NO_BIOLOGICO_ALLEVAMENTI);
    }
    
    
    //Legenda
    if(Validator.isNotEmpty(dataInserimentoDichiarazione)
      && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
    {
      htmpl.newBlock("blkLegenda");    
    } 
    

  }
  
  //HtmplUtil.setValues(htmpl, all);
%>
<%=htmpl.text()%>

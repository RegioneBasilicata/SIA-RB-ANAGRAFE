<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@page import="java.math.BigDecimal"%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/riepiloghiUnitaArboree.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
  	<%@include file = "/view/remoteInclude.inc" %>
  <%
  
	Vector<RiepiloghiUnitaArboreaVO> elencoRiepiloghi = (Vector<RiepiloghiUnitaArboreaVO>)request.getAttribute("elencoRiepiloghi");
	Long idTipoRicerca = (Long)request.getAttribute("idTipoRicerca");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");  
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  Vector<TipoRiepilogoVO> vTipoRiepilogo = (Vector<TipoRiepilogoVO>)request.getAttribute("vTipoRiepilogo");
	
	HtmplUtil.setErrors(htmpl, errors, request, application);
	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	
	if(Validator.isNotEmpty(vTipoRiepilogo))
  {
    for(int i=0;i<vTipoRiepilogo.size();i++) 
    {
      TipoRiepilogoVO tipoRiepilogoVO = vTipoRiepilogo.get(i);
      htmpl.newBlock("blkTipoRiepilogo");
      htmpl.set("blkTipoRiepilogo.idTipoRiepilogo", ""+tipoRiepilogoVO.getIdTipoRiepilogo());
      htmpl.set("blkTipoRiepilogo.descrizione", tipoRiepilogoVO.getNome());
      if((idTipoRicerca != null) && tipoRiepilogoVO.getIdTipoRiepilogo() == idTipoRicerca.longValue()) 
      {
        htmpl.set("blkTipoRiepilogo.selected", "selected=\"selected\"", null);
      }
      else
      {
        if("S".equalsIgnoreCase(tipoRiepilogoVO.getFlagDefault()))
        {
          htmpl.set("blkTipoRiepilogo.selected", "selected=\"selected\"", null);
        }
      }
      
    }
  }

  
  
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
    ruoloUtenza);

	// Nel caso in cui si sia verificato un errore durante l'elaborazione dei
	// riepiloghi
	if(Validator.isNotEmpty(messaggioErrore)) 
  {
		htmpl.newBlock("blkErrore");
		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
	}
	else 
  {
		// RIEPILOGO DESTINAZIONE PRODUTTIVA COMUNE
		if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTIONAZIONE_PRODUTTIVA_COMUNE) == 0) 
    {
      if(elencoRiepiloghi != null) 
      {
        htmpl.newBlock("blkEsportaDati");
			  htmpl.newBlock("blkRisultatoRiepilogoDestinazioneProduttivaComune");
        BigDecimal totUvaVino = new BigDecimal(0);
        BigDecimal totUvaMensa = new BigDecimal(0);
        BigDecimal totAltreDestinazioni = new BigDecimal(0);
        BigDecimal totSupTotale = new BigDecimal(0);
        BigDecimal totUvaVinoParz = new BigDecimal(0);
        BigDecimal totUvaMensaParz = new BigDecimal(0);
        BigDecimal totAltreDestinazioniParz = new BigDecimal(0);
        BigDecimal totSupTotaleParz = new BigDecimal(0);
        String provinciaTmp = null;
        String blk = "blkRisultatoRiepilogoDestinazioneProduttivaComune.blkCorpo";
        String blkParz = blk+".blkTotaleRisultato";
        
        for(int i=0;i<elencoRiepiloghi.size();i++)
        {
          
          if(!elencoRiepiloghi.get(i).getDescProv().equals(provinciaTmp))
          {
            if(i !=0)
            {
              htmpl.newBlock(blkParz);
              htmpl.set(blkParz+".descProv", provinciaTmp);
              
              htmpl.set(blkParz+".supUvaVinoParz", 
                Formatter.formatDouble4(totUvaVinoParz));
              totUvaVinoParz = new BigDecimal(0);
              htmpl.set(blkParz+".supUvaMensaParz", 
                Formatter.formatDouble4(totUvaMensaParz));
              totUvaMensaParz = new BigDecimal(0);
              htmpl.set(blkParz+".supAltreDestinazioniParz", 
                Formatter.formatDouble4(totAltreDestinazioniParz));
              totAltreDestinazioniParz = new BigDecimal(0);
              htmpl.set(blkParz+".supTotaleParz", 
                Formatter.formatDouble4(totSupTotaleParz));
              totSupTotaleParz = new BigDecimal(0);
              
              
            }
            htmpl.newBlock(blk);
            String blkDesc = blk+".blkDescrizioneRisultato";
            htmpl.newBlock(blkDesc);
            htmpl.set(blkDesc+".descProv", elencoRiepiloghi.get(i).getDescProv());
          }          
          
          
          String blkElenco = blk+".blkElencoRisultato";
          htmpl.newBlock(blkElenco);
          htmpl.set(blkElenco+".istatComune", elencoRiepiloghi.get(i).getIstatComune());
          htmpl.set(blkElenco+".descComune", elencoRiepiloghi.get(i).getDescComune());
          htmpl.set(blkElenco+".siglaProv", elencoRiepiloghi.get(i).getSiglaProv());
          BigDecimal uvaVino = elencoRiepiloghi.get(i).getUvaDaVino();
          totUvaVino = totUvaVino.add(uvaVino);
          totUvaVinoParz = totUvaVinoParz.add(uvaVino);
          htmpl.set(blkElenco+".supUvaVino", Formatter.formatDouble4(uvaVino));
          BigDecimal uvaMensa = elencoRiepiloghi.get(i).getUvaDaMensa();
          totUvaMensa = totUvaMensa.add(uvaMensa);
          totUvaMensaParz = totUvaMensaParz.add(uvaMensa);
          htmpl.set(blkElenco+".supUvaMensa", Formatter.formatDouble4(uvaMensa));
          BigDecimal altreDestinazioni = elencoRiepiloghi.get(i).getAltreDestinazioniProduttive();
          totAltreDestinazioni = totAltreDestinazioni.add(altreDestinazioni);
          totAltreDestinazioniParz = totAltreDestinazioniParz.add(altreDestinazioni);
          htmpl.set(blkElenco+".supAltreDestinazioni", Formatter.formatDouble4(altreDestinazioni));
          BigDecimal supTotale = elencoRiepiloghi.get(i).getSupVitata();
          totSupTotale = totSupTotale.add(supTotale);
          totSupTotaleParz = totSupTotaleParz.add(supTotale);
          htmpl.set(blkElenco+".supTotale", Formatter.formatDouble4(supTotale));
          
          
          provinciaTmp = elencoRiepiloghi.get(i).getDescProv();
          
        }
        
        
        htmpl.newBlock(blkParz);
        htmpl.set(blkParz+".descProv", provinciaTmp);
              
        htmpl.set(blkParz+".supUvaVinoParz", 
          Formatter.formatDouble4(totUvaVinoParz));
        totUvaVinoParz = new BigDecimal(0);
        htmpl.set(blkParz+".supUvaMensaParz", 
          Formatter.formatDouble4(totUvaMensaParz));
        totUvaMensaParz = new BigDecimal(0);
        htmpl.set(blkParz+".supAltreDestinazioniParz", 
          Formatter.formatDouble4(totAltreDestinazioniParz));
        totAltreDestinazioniParz = new BigDecimal(0);
        htmpl.set(blkParz+".supTotaleParz", 
          Formatter.formatDouble4(totSupTotaleParz));
        totSupTotaleParz = new BigDecimal(0);
        
        htmpl.set("blkRisultatoRiepilogoDestinazioneProduttivaComune.totaleSuperficiUvaVino", 
          Formatter.formatDouble4(totUvaVino));
        htmpl.set("blkRisultatoRiepilogoDestinazioneProduttivaComune.totaleSuperficiUvaMensa", 
          Formatter.formatDouble4(totUvaMensa));
        htmpl.set("blkRisultatoRiepilogoDestinazioneProduttivaComune.totaleSuperficiAltreDestinazioni", 
          Formatter.formatDouble4(totAltreDestinazioni));
        htmpl.set("blkRisultatoRiepilogoDestinazioneProduttivaComune.totaleSuperficiTotale", 
          Formatter.formatDouble4(totSupTotale));
        
        
        
      }
        
		}
		// RIEPILOGO DESTINAZIONE PRODUTTIVA UVA DA VINO
		else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTINAZIONE_PRODUTTIVA_UVA_DA_VINO) == 0) 
    {
			if(elencoRiepiloghi != null) 
      {
        htmpl.newBlock("blkEsportaDati");
        htmpl.newBlock("blkRisultatoRiepilogoDestinazioneProduttivaUvaDaVino");
        BigDecimal totSupVitDOP = new BigDecimal(0);
        BigDecimal totSupVitVinoTav = new BigDecimal(0);
        BigDecimal totSupVitTot = new BigDecimal(0);
        BigDecimal totSupVitDOPParz = new BigDecimal(0);
        BigDecimal totSupVitVinoTavParz = new BigDecimal(0);
        BigDecimal totSupVitTotParz = new BigDecimal(0);
        String provinciaTmp = null;
        String blk = "blkRisultatoRiepilogoDestinazioneProduttivaUvaDaVino.blkCorpo";
        String blkParz = blk+".blkTotaleRisultato";
        
        for(int i=0;i<elencoRiepiloghi.size();i++)
        {
          
          if(!elencoRiepiloghi.get(i).getDescProv().equals(provinciaTmp))
          {
            if(i !=0)
            {
              htmpl.newBlock(blkParz);
              htmpl.set(blkParz+".descProv", provinciaTmp);
              
              htmpl.set(blkParz+".supVitDOPParz", 
                Formatter.formatDouble4(totSupVitDOPParz));
              totSupVitDOPParz = new BigDecimal(0);
              htmpl.set(blkParz+".supVitVinoTavParz", 
                Formatter.formatDouble4(totSupVitVinoTavParz));
              totSupVitVinoTavParz = new BigDecimal(0);
              htmpl.set(blkParz+".supVitTotParz", 
                Formatter.formatDouble4(totSupVitTotParz));              
              totSupVitTotParz = new BigDecimal(0);              
            }
            
            htmpl.newBlock(blk);
            String blkDesc = blk+".blkDescrizioneRisultato";
            htmpl.newBlock(blkDesc);
            htmpl.set(blkDesc+".descProv", elencoRiepiloghi.get(i).getDescProv());
          }          
          
          
          String blkElenco = blk+".blkElencoRisultato";
          htmpl.newBlock(blkElenco);
          htmpl.set(blkElenco+".istatComune", elencoRiepiloghi.get(i).getIstatComune());
          htmpl.set(blkElenco+".descComune", elencoRiepiloghi.get(i).getDescComune());
          htmpl.set(blkElenco+".siglaProv", elencoRiepiloghi.get(i).getSiglaProv());
          BigDecimal supVitDOP = elencoRiepiloghi.get(i).getSupVitataDOP();
          totSupVitDOP = totSupVitDOP.add(supVitDOP);
          totSupVitDOPParz = totSupVitDOPParz.add(supVitDOP);
          htmpl.set(blkElenco+".supVitDOP", Formatter.formatDouble4(supVitDOP));
          BigDecimal supVitVinoTav = elencoRiepiloghi.get(i).getSupVitataVinoTavola();
          totSupVitVinoTav = totSupVitVinoTav.add(supVitVinoTav);
          totSupVitVinoTavParz = totSupVitVinoTavParz.add(supVitVinoTav);
          htmpl.set(blkElenco+".supVitVinoTav", Formatter.formatDouble4(supVitVinoTav));
          BigDecimal supVitTot = elencoRiepiloghi.get(i).getSupVitata();
          totSupVitTot = totSupVitTot.add(supVitTot);
          totSupVitTotParz = totSupVitTotParz.add(supVitTot);
          htmpl.set(blkElenco+".supVitTot", Formatter.formatDouble4(supVitTot));          
          
          provinciaTmp = elencoRiepiloghi.get(i).getDescProv();
          
        }
        
        
        htmpl.newBlock(blkParz);
        htmpl.set(blkParz+".descProv", provinciaTmp);
              
        htmpl.set(blkParz+".supVitDOPParz", 
          Formatter.formatDouble4(totSupVitDOPParz));
        totSupVitDOPParz = new BigDecimal(0);
        htmpl.set(blkParz+".supVitVinoTavParz", 
          Formatter.formatDouble4(totSupVitVinoTavParz));
        totSupVitVinoTavParz = new BigDecimal(0);
        htmpl.set(blkParz+".supVitTotParz", 
          Formatter.formatDouble4(totSupVitTotParz));              
        totSupVitTotParz = new BigDecimal(0);
        
        htmpl.set("blkRisultatoRiepilogoDestinazioneProduttivaUvaDaVino.totaleSupVitDOP", 
          Formatter.formatDouble4(totSupVitDOP));
        htmpl.set("blkRisultatoRiepilogoDestinazioneProduttivaUvaDaVino.totaleSupVitVinoTav", 
          Formatter.formatDouble4(totSupVitVinoTav));
        htmpl.set("blkRisultatoRiepilogoDestinazioneProduttivaUvaDaVino.totaleSupVitTot", 
          Formatter.formatDouble4(totSupVitTot));   
        
      }
		}
		// RIEPILOGO VINO DOP
		else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_VINO_DOP) == 0) 
    {
      if(elencoRiepiloghi != null) 
      {
        htmpl.newBlock("blkEsportaDati");
  			htmpl.newBlock("blkRisultatoRiepilogoVinoDOP");
        BigDecimal totSupVit = new BigDecimal(0);
        BigDecimal totSupVitParz = new BigDecimal(0);
        String tipoTipologiaVinoTmp = null;
        String blk = "blkRisultatoRiepilogoVinoDOP.blkCorpo";
        String blkParz = blk+".blkTotaleRisultato";
        
        for(int i=0;i<elencoRiepiloghi.size();i++)
        {
          
          if(!elencoRiepiloghi.get(i).getTipoTipolgiaVino().equals(tipoTipologiaVinoTmp))
          {
            if(i !=0)
            {
              htmpl.newBlock(blkParz);
              htmpl.set(blkParz+".tipoTipologiaVino", tipoTipologiaVinoTmp);
              
              htmpl.set(blkParz+".supVitParz", 
                Formatter.formatDouble4(totSupVitParz));
              totSupVitParz = new BigDecimal(0);
              
              
            }
            htmpl.newBlock(blk);
            String blkDesc = blk+".blkDescrizioneRisultato";
            htmpl.newBlock(blkDesc);
            htmpl.set(blkDesc+".tipoTipologiaVino", elencoRiepiloghi.get(i).getTipoTipolgiaVino());
          }          
          
          
          String blkElenco = blk+".blkElencoRisultato";
          htmpl.newBlock(blkElenco);
          htmpl.set(blkElenco+".vinoComune", 
            elencoRiepiloghi.get(i).getIstatComune()+","+elencoRiepiloghi.get(i).getIdTipolgiaVino());
          htmpl.set(blkElenco+".descComune", elencoRiepiloghi.get(i).getDescComune());
          htmpl.set(blkElenco+".siglaProv", elencoRiepiloghi.get(i).getSiglaProv());
          BigDecimal supVit = elencoRiepiloghi.get(i).getSupVitata();
          totSupVit = totSupVit.add(supVit);
          totSupVitParz = totSupVitParz.add(supVit);
          htmpl.set(blkElenco+".supVit", Formatter.formatDouble4(supVit));
          
          
          
          tipoTipologiaVinoTmp = elencoRiepiloghi.get(i).getTipoTipolgiaVino();
          
        }
        
        
        htmpl.newBlock(blkParz);
        htmpl.set(blkParz+".tipoTipologiaVino", tipoTipologiaVinoTmp);
              
        htmpl.set(blkParz+".supVitParz", 
          Formatter.formatDouble4(totSupVitParz));
        totSupVitParz = new BigDecimal(0);
        
        htmpl.set("blkRisultatoRiepilogoVinoDOP.totaleSuperficiVitata", 
          Formatter.formatDouble4(totSupVit));
      }
    }
    // RIEPILOGO PROVINCIA/VINO DOP
    else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_PROVINCIA_VINO_DOP) == 0) 
    {
      if(elencoRiepiloghi != null) 
      {
        htmpl.newBlock("blkEsportaDati");
        htmpl.newBlock("blkRisultatoRiepilogoProvinciaVinoDOP");
        BigDecimal totSupVit100 = new BigDecimal(0);
        BigDecimal totSupVit100Parz = new BigDecimal(0);
        BigDecimal totProdResa100 = new BigDecimal(0);
        BigDecimal totProdResa100Parz = new BigDecimal(0);
        BigDecimal totSupVit70 = new BigDecimal(0);
        BigDecimal totSupVit70Parz = new BigDecimal(0);
        BigDecimal totProdResa70 = new BigDecimal(0);
        BigDecimal totProdResa70Parz = new BigDecimal(0);
        BigDecimal totSupVit0 = new BigDecimal(0);
        BigDecimal totSupVit0Parz = new BigDecimal(0);
        BigDecimal totProdResa0 = new BigDecimal(0);
        BigDecimal totProdResa0Parz = new BigDecimal(0);
        String descProvTmp = null;
        String blk = "blkRisultatoRiepilogoProvinciaVinoDOP.blkCorpo";
        String blkParz = blk+".blkTotaleRisultato";
        
        for(int i=0;i<elencoRiepiloghi.size();i++)
        {
          
          if(!elencoRiepiloghi.get(i).getDescProv().equals(descProvTmp))
          {
            if(i !=0)
            {
              htmpl.newBlock(blkParz); 
              htmpl.set(blkParz+".descProv", descProvTmp);
                           
              htmpl.set(blkParz+".supVit100Parz", 
                Formatter.formatDouble4(totSupVit100Parz));
              totSupVit100Parz = new BigDecimal(0);
              htmpl.set(blkParz+".prodResa100Parz", 
                Formatter.formatDouble2(totProdResa100Parz));
              totProdResa100Parz = new BigDecimal(0);
              htmpl.set(blkParz+".supVit70Parz", 
                Formatter.formatDouble4(totSupVit70Parz));
              totSupVit70Parz = new BigDecimal(0);
              htmpl.set(blkParz+".prodResa70Parz", 
                Formatter.formatDouble2(totProdResa70Parz));
              totProdResa70Parz = new BigDecimal(0);
              htmpl.set(blkParz+".supVit0Parz", 
                Formatter.formatDouble4(totSupVit0Parz));
              totSupVit0Parz = new BigDecimal(0);
              htmpl.set(blkParz+".prodResa0Parz", 
                Formatter.formatDouble2(totProdResa0Parz));
              totProdResa0Parz = new BigDecimal(0);               
            }
            htmpl.newBlock(blk);
          }          
          
          
          String blkElenco = blk+".blkElencoRisultato";
          htmpl.newBlock(blkElenco);
          htmpl.set(blkElenco+".vinoProvincia", 
            elencoRiepiloghi.get(i).getIstatProv()+","+elencoRiepiloghi.get(i).getIdTipolgiaVino());
          htmpl.set(blkElenco+".descProv", elencoRiepiloghi.get(i).getDescProv());
          htmpl.set(blkElenco+".tipoTipologiaVinoDOP", elencoRiepiloghi.get(i).getTipoTipolgiaVino());
          htmpl.set(blkElenco+".resa", Formatter.formatDouble(elencoRiepiloghi.get(i).getResa()));
          BigDecimal supVit100 = elencoRiepiloghi.get(i).getSupVitata100();
          totSupVit100 = totSupVit100.add(supVit100);
          totSupVit100Parz = totSupVit100Parz.add(supVit100);
          htmpl.set(blkElenco+".supVit100", Formatter.formatDouble4(supVit100));
          BigDecimal prodResa100 = elencoRiepiloghi.get(i).getProdResa100();
          totProdResa100 = totProdResa100.add(prodResa100);
          totProdResa100Parz = totProdResa100Parz.add(prodResa100);
          htmpl.set(blkElenco+".prodResa100", Formatter.formatDouble2(prodResa100));
          BigDecimal supVit70 = elencoRiepiloghi.get(i).getSupVitata70();
          totSupVit70 = totSupVit70.add(supVit70);
          totSupVit70Parz = totSupVit70Parz.add(supVit70);
          htmpl.set(blkElenco+".supVit70", Formatter.formatDouble4(supVit70));
          BigDecimal prodResa70 = elencoRiepiloghi.get(i).getProdResa70();
          totProdResa70 = totProdResa70.add(prodResa70);
          totProdResa70Parz = totProdResa70Parz.add(prodResa70);
          htmpl.set(blkElenco+".prodResa70", Formatter.formatDouble2(prodResa70));
          BigDecimal supVit0 = elencoRiepiloghi.get(i).getSupVitata0();
          totSupVit0 = totSupVit0.add(supVit0);
          totSupVit0Parz = totSupVit0Parz.add(supVit0);
          htmpl.set(blkElenco+".supVit0", Formatter.formatDouble4(supVit0));
          BigDecimal prodResa0 = elencoRiepiloghi.get(i).getProdResa0();
          totProdResa0 = totProdResa0.add(prodResa0);
          totProdResa0Parz = totProdResa0Parz.add(prodResa0);
          htmpl.set(blkElenco+".prodResa0", Formatter.formatDouble2(prodResa0));
          
          
          
          descProvTmp = elencoRiepiloghi.get(i).getDescProv();
          
        }
        
        
        htmpl.newBlock(blkParz);
        htmpl.set(blkParz+".descProv", descProvTmp);
              
        htmpl.set(blkParz+".supVit100Parz", 
          Formatter.formatDouble4(totSupVit100Parz));
        totSupVit100Parz = new BigDecimal(0);
        htmpl.set(blkParz+".prodResa100Parz", 
          Formatter.formatDouble2(totProdResa100Parz));
        totProdResa100Parz = new BigDecimal(0);
        htmpl.set(blkParz+".supVit70Parz", 
          Formatter.formatDouble4(totSupVit70Parz));
        totSupVit70Parz = new BigDecimal(0);
        htmpl.set(blkParz+".prodResa70Parz", 
          Formatter.formatDouble2(totProdResa70Parz));
        totProdResa0Parz = new BigDecimal(0);
        htmpl.set(blkParz+".supVit0Parz", 
          Formatter.formatDouble4(totSupVit0Parz));
        totSupVit0Parz = new BigDecimal(0);
        htmpl.set(blkParz+".prodResa0Parz", 
          Formatter.formatDouble2(totProdResa0Parz));
        totProdResa0Parz = new BigDecimal(0);
        
        htmpl.set("blkRisultatoRiepilogoProvinciaVinoDOP.totaleSuperficieVitata100", 
          Formatter.formatDouble4(totSupVit100));
        htmpl.set("blkRisultatoRiepilogoProvinciaVinoDOP.totaleProdResa100", 
          Formatter.formatDouble2(totProdResa100));
        htmpl.set("blkRisultatoRiepilogoProvinciaVinoDOP.totaleSuperficieVitata70", 
          Formatter.formatDouble4(totSupVit70));
        htmpl.set("blkRisultatoRiepilogoProvinciaVinoDOP.totaleProdResa70", 
          Formatter.formatDouble2(totProdResa70));
        htmpl.set("blkRisultatoRiepilogoProvinciaVinoDOP.totaleSuperficieVitata0", 
          Formatter.formatDouble4(totSupVit0));
        htmpl.set("blkRisultatoRiepilogoProvinciaVinoDOP.totaleProdResa0", 
          Formatter.formatDouble2(totProdResa0));
      }     
    }
		
		
		
    
    
    
	}

  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>

<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="java.util.Date" %>

<%
  SolmrLogger.debug(this, " - terreniParticellareSigmaterView.jsp - INIZIO PAGINA");
 	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/terreniParticellareSigmater.htm");

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
  
  try
  {
  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    String onLoad = (String)request.getAttribute("onLoad");
    
    if(Validator.isNotEmpty(onLoad)) {
      htmpl.set("onLoad", onLoad);
    }
    
    StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
    FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
    String messaggioErrore = (String)request.getAttribute("messaggioErrore");
    
    if (storicoParticellaVO!=null && filtriParticellareRicercaVO!=null)
    {
      htmpl.set("idParticella", storicoParticellaVO.getIdParticella().toString());
      if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) 
        htmpl.set("sezioneSiap", storicoParticellaVO.getSezione());
      htmpl.set("foglioSiap", storicoParticellaVO.getFoglio());
      if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
        htmpl.set("particellaSiap", storicoParticellaVO.getParticella());
      if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno()))
        htmpl.set("subalternoSiap", storicoParticellaVO.getSubalterno());
      htmpl.set("supCatSiap", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    
      //Valorizzo i dati della testata della particella
      if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
      {
        // Dati di conduzione
        ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)storicoParticellaVO.getElencoConduzioni()[0];
        htmpl.set("idConduzioneSiap", conduzioneParticellaVO.getIdConduzioneParticella().toString());
        htmpl.set("idConduzione", conduzioneParticellaVO.getIdConduzioneParticella().toString());
      }
      // Piano di riferimento ad una dichiarazione di consistenza
      else 
      {
        // Dati di conduzione
        ConduzioneDichiarataVO conduzioneDichiarataVO = (ConduzioneDichiarataVO)storicoParticellaVO.getElencoConduzioniDichiarate()[0];
        htmpl.set("idConduzioneSiap", conduzioneDichiarataVO.getIdConduzioneDichiarata().toString());
        htmpl.set("idConduzione", conduzioneDichiarataVO.getIdConduzioneDichiarata().toString());
      }
      
      htmpl.set("comuneSiap", storicoParticellaVO.getComuneParticellaVO().getDescom());
      if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()))
        htmpl.set("provinciaSiap", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
    }
    
    
    String messaggioErrore1 = (String)request.getAttribute("messaggioErrore1");
    if(Validator.isNotEmpty(messaggioErrore1)) 
    {
      htmpl.newBlock("blkErrore");
      htmpl.set("blkErrore.messaggio1", messaggioErrore1);
      htmpl.set("blkErrore.messaggio2", (String)request.getAttribute("messaggioErrore2"));
    }
    else
    {
      //Se non ci sono stati errori nella control procedo con la visualizzazione della pagina
      DettaglioTerreno dettTerreno=(DettaglioTerreno)request.getAttribute("dettaglioTerreno"); 
      
      if (dettTerreno!=null)
      {
        htmpl.set("dataValiditaSigmater", dettTerreno.getDataValidita());
        Terreno terreno=dettTerreno.getTerreno();
        
        //Se sigmater ha restituito qualcosa provvedo con la valorizzazione dei dati della pagina
        if (terreno!=null)
        {
          /****************************************************
           *   Blocco Dati catastali e di classamento begin   *
           ****************************************************/  
          //Creo il blocco dei dati catastali e di classamento e valorizzo i vari valori
          htmpl.newBlock("blkCatClassamento");
          if (terreno.getClassTerreno()!=null)
          {
            String superficie="";
            String ettari="0"; 
            if (Validator.isNotEmpty(terreno.getClassTerreno().getEttari())) ettari=terreno.getClassTerreno().getEttari();
            String are="00"; 
            if (Validator.isNotEmpty(terreno.getClassTerreno().getAre())) are=terreno.getClassTerreno().getAre();
            String centiare="00"; 
            if (Validator.isNotEmpty(terreno.getClassTerreno().getCentiare())) centiare=terreno.getClassTerreno().getCentiare();
            superficie=ettari+","+AnagUtils.leftFill(are, '0', 2)+AnagUtils.leftFill(centiare, '0', 2);
            htmpl.set("blkCatClassamento.superficie", superficie);
          }
          if (terreno.getIdentTerreno()!=null)
          {
            htmpl.set("blkCatClassamento.denominatore", terreno.getIdentTerreno().getDenominatore());
            htmpl.set("blkCatClassamento.edificalita", terreno.getIdentTerreno().getEdificalita());
          }
          htmpl.set("blkCatClassamento.partita", terreno.getPartita());
          htmpl.set("blkCatClassamento.partitaSpeciale", terreno.getPartitaSpeciale());
          
          if (terreno.getClassTerreno()!=null)
          {
            String codQualita="",descQualita=""; 
            if (terreno.getClassTerreno().getCodQualita()!=null) codQualita=terreno.getClassTerreno().getCodQualita();
            if (terreno.getClassTerreno().getDescQualita()!=null) descQualita=terreno.getClassTerreno().getDescQualita();
            htmpl.set("blkCatClassamento.qualita", aggiungiQuadre(codQualita)+descQualita);
            htmpl.set("blkCatClassamento.classe", terreno.getClassTerreno().getClasse());
          }
          htmpl.set("blkCatClassamento.annotazione", terreno.getAnnotazione());
          
          String presenzaFabbricati=(String)request.getAttribute("presenzaFabbricati");
          //Se presenzaFabbricati contiene un'errore devo visualizzarlo in rosso
          if (!(SolmrConstants.FLAG_SI.equals(presenzaFabbricati) || SolmrConstants.FLAG_NO.equals(presenzaFabbricati)))
            htmpl.newBlock("blkCatClassamento.blkRed");
          
          htmpl.set("blkCatClassamento.presenzaFabbricati",presenzaFabbricati ,null);
          /**************************************************
           *   Blocco Dati catastali e di classamento end   *
           **************************************************/  
          
          /**************************************
           *   Blocco Tariffa - Reddito begin   *
           **************************************/  
          //Creo il blocco dei dati relativi alla Tariffa - Reddito
          htmpl.newBlock("blkTarReddito");
          if (terreno.getClassTerreno()!=null && terreno.getClassTerreno().getTariffaTerreno()!=null)
          {
            htmpl.set("blkTarReddito.tariffaDominicale", Formatter.formatEuro(terreno.getClassTerreno().getTariffaTerreno().getTariffaDominicaleEuro()));
            htmpl.set("blkTarReddito.tariffaAgraria", Formatter.formatEuro(terreno.getClassTerreno().getTariffaTerreno().getTariffaAgrariaEuro()));
            htmpl.set("blkTarReddito.dataInizioTariffa", terreno.getClassTerreno().getTariffaTerreno().getTariffaDataInizio());
            htmpl.set("blkTarReddito.dataFineTariffa", terreno.getClassTerreno().getTariffaTerreno().getTariffaDataFine());
          }
          if (dettTerreno.getReddito()!=null)
          {
            htmpl.set("blkTarReddito.redditoDominicale", Formatter.formatEuro(dettTerreno.getReddito().getRedditoDominicaleEuro()));
            htmpl.set("blkTarReddito.redditoAgrario", Formatter.formatEuro(dettTerreno.getReddito().getRedditoAgrarioEuro()));
          }
          /************************************
           *   Blocco Tariffa - Reddito end   *
           ************************************/  
        }
        
        /******************************
         *   Blocco Deduzione begin   *
         ******************************/  
         //Per ogni istanza della classe Deduzione(Nel caso in cui non fosse presente alcun dato non visualizzare questa sezione)
        Deduzione deduzione[]=dettTerreno.getDeduzioni();
        
        if (deduzione!=null && deduzione.length!=0)
        {
          //Creo il blocco dei dati relativi alle deduzioni
          htmpl.newBlock("blkDeduzione");
          for (int i=0;i<deduzione.length;i++)
          {
            if (deduzione[i]!=null)
            {
              htmpl.newBlock("blkDeduzione.blkDeduzioni");
              htmpl.set("blkDeduzione.blkDeduzioni.simbolo", deduzione[i].getSimboloDeduzione());
              if (deduzione[i].getValoriDeduzioni()!=null)
              {
                htmpl.set("blkDeduzione.blkDeduzioni.tipo", deduzione[i].getValoriDeduzioni().getDeduzioneTipo());
                htmpl.set("blkDeduzione.blkDeduzioni.percentuale", deduzione[i].getValoriDeduzioni().getDeduzionePercentuale());
                htmpl.set("blkDeduzione.blkDeduzioni.importo", Formatter.formatEuro(deduzione[i].getValoriDeduzioni().getDeduzioneEuro()));
                htmpl.set("blkDeduzione.blkDeduzioni.dataInizioValidita", deduzione[i].getValoriDeduzioni().getDeduzioneDataInizio());
                htmpl.set("blkDeduzione.blkDeduzioni.dataFineValidita", deduzione[i].getValoriDeduzioni().getDeduzioneDataFine());
              }
            }
          }
        }
        /******************************
         *   Blocco Deduzione end   *
         ******************************/  
        
        /****************************
         *   Blocco Riserva begin   *
         ****************************/  
        //Per ogni istanza della classe RiservaTerreno(Nel caso in cui non fosse presente alcun dato non visualizzare questa sezione)
        RiservaTerreno riserve[]=dettTerreno.getRiserve();
        
        if (riserve!=null && riserve.length!=0)
        {
          //Creo il blocco dei dati relativi alla riserve
          htmpl.newBlock("blkRiserva");
          for (int i=0;i<riserve.length;i++)
          {
            if (riserve[i]!=null)
            {
              htmpl.newBlock("blkRiserva.blkRiserve");
              htmpl.set("blkRiserva.blkRiserve.codRiserva", riserve[i].getCodRiserva());
              htmpl.set("blkRiserva.blkRiserve.descRiserva", riserve[i].getDescRiserva());
              htmpl.set("blkRiserva.blkRiserve.partitaIscrizione", riserve[i].getPartitaIscrizione());
            }
          }
        }
        /**************************
         *   Blocco Riserva end   *
         **************************/  
         
        /*****************************
         *   Blocco Porzione begin   *
         *****************************/  
         //Per ogni istanza della classe Porzione(Nel caso in cui non fosse presente alcun dato non visualizzare questa sezione)
        Porzione porzione[]=dettTerreno.getPorzioni();
        
        if (porzione!=null && porzione.length!=0)
        {
          //Creo il blocco dei dati relativi alle porzioni
          htmpl.newBlock("blkPorzione");
          for (int i=0;i<porzione.length;i++)
          {
            if (porzione[i]!=null)
            {
              htmpl.newBlock("blkPorzione.blkPorzioni");
              htmpl.set("blkPorzione.blkPorzioni.codQualita", aggiungiQuadre(porzione[i].getCodQualita()));
              htmpl.set("blkPorzione.blkPorzioni.descQualita", porzione[i].getDescQualita());
              htmpl.set("blkPorzione.blkPorzioni.classe", porzione[i].getClasse());
              
              String superficie="";
              String ettari="0"; 
              if (Validator.isNotEmpty(porzione[i].getEttari())) ettari=porzione[i].getEttari();
              String are="00"; 
              if (Validator.isNotEmpty(porzione[i].getAre())) are=porzione[i].getAre();
              String centiare="00"; 
              if (Validator.isNotEmpty(porzione[i].getCentiare())) centiare=porzione[i].getCentiare();
              superficie=ettari+","+AnagUtils.leftFill(are, '0', 2)+AnagUtils.leftFill(centiare, '0', 2);
              
              htmpl.set("blkPorzione.blkPorzioni.superficie", superficie);
              
              if (porzione[i].getTariffaTerreno()!=null)
              {
                htmpl.set("blkPorzione.blkPorzioni.dominicale", Formatter.formatEuro(porzione[i].getTariffaTerreno().getTariffaDominicaleEuro()));
                htmpl.set("blkPorzione.blkPorzioni.agraria", Formatter.formatEuro(porzione[i].getTariffaTerreno().getTariffaAgrariaEuro()));
                htmpl.set("blkPorzione.blkPorzioni.dataInizio", porzione[i].getTariffaTerreno().getTariffaDataInizio());
                htmpl.set("blkPorzione.blkPorzioni.dataFine", porzione[i].getTariffaTerreno().getTariffaDataFine());
              }
            }
          }
        }
        /*****************************
         *   Blocco Porzione end     *
         *****************************/  
         
        /********************************
         *   Blocco Mutazione begin     *
         ********************************/   
        if (terreno!=null)
        {
          //Nel caso non fossero presenti i dati su mutazioneIniziale, MutazioneFinale non visualizzare questa sezione
          if (terreno.getMutazioneIniziale()!=null || terreno.getMutazioneFinale()!=null)
          {
            htmpl.newBlock("blkMutazione");
            //Visualizzare i dati, su righe differenti, considerando prima quelli presenti in mutazioneIniziale e 
            //successivamente quelli in MutazioneFinale 
            for (int i=0;i<2;i++)
            {
              Mutazione mutazione=null;
              if (i==0) mutazione=terreno.getMutazioneIniziale();
              else mutazione=terreno.getMutazioneFinale();
              if (mutazione!=null)
              {
                htmpl.newBlock("blkMutazione.blkMutazioni");
                if (i==0) htmpl.set("blkMutazione.blkMutazioni.tipo", "I");
                else htmpl.set("blkMutazione.blkMutazioni.tipo", "F");
                 
                htmpl.set("blkMutazione.blkMutazioni.codTipoNota", aggiungiQuadre(mutazione.getCodTipoNota()));
                htmpl.set("blkMutazione.blkMutazioni.descTipoNota", mutazione.getDescTipoNota());
                htmpl.set("blkMutazione.blkMutazioni.num", mutazione.getNumNota());
                htmpl.set("blkMutazione.blkMutazioni.prog", mutazione.getProgrNota());
                htmpl.set("blkMutazione.blkMutazioni.anno", mutazione.getAnnoNota());
                htmpl.set("blkMutazione.blkMutazioni.dataInizioValidita", mutazione.getDataEfficacia());
                htmpl.set("blkMutazione.blkMutazioni.dataRegistrazione", mutazione.getDataRegistrazione());
                htmpl.set("blkMutazione.blkMutazioni.codCausale", aggiungiQuadre(mutazione.getCodCausale()));
                htmpl.set("blkMutazione.blkMutazioni.descCausale", mutazione.getDescrCausale());
                htmpl.set("blkMutazione.blkMutazioni.descMutazione", mutazione.getDescrMutazione());
              }
            }
          }
        }
        /******************************
         *   Blocco Mutazione end     *
         ******************************/  
         
        /***********************************************
         *   Blocco Titolarità - aggiornata  begin     *
         ***********************************************/  
         //Le informazioni riportate sono recuperate tramite il servizio CercaTitolaritaOggettoCatastaleVisualizzare: 
         //"Titolarità  - aggiornata al " + Titolarita.dataValidita (Formattare la data in gg/mm/aaaa)
         //Per ogni istanza della classe Titolarita(Nel caso in cui non fosse presente alcun dato non visualizzare questa sezione)
         //Nel caso in cui il servizio non fosse disponibile oppure venga restituita un'eccezione visualizzare sotto 
         //l'intestazione di questa sezione:"servizio momentaneamente non disponibile [sigmater]"
        Titolarita titolarita[]=(Titolarita[])session.getAttribute("titolarita");
        
        if (titolarita==null || titolarita.length==0)
        {
          //controllo se non mi sono stati restituiti dati a causa di un errore di sigmater
          String messaggioErrore3 = (String)request.getAttribute("messaggioErrore3");
          if(Validator.isNotEmpty(messaggioErrore3)) 
          {
            htmpl.newBlock("blkTitolaritaErrore");
            htmpl.set("blkTitolaritaErrore.messaggio1", messaggioErrore3);
            htmpl.set("blkTitolaritaErrore.messaggio2", (String)request.getAttribute("messaggioErrore4"));
          }
        }
        else
        {
          //htmpl.newBlock("blkTitolarita");
          
          //Visualizzare: "Titolarità  - aggiornata al " + Titolarita.dataValidita (Formattare la data in gg/mm/aaaa)
          //(Considerare la min(dataValidita) tra tutte le istanze restituite)
          String dataMinS="31/12/9999";
          Date dataMinD=DateUtils.parseDate(dataMinS);
          
          for (int i=0;i<titolarita.length;i++)
          {
            try
            {
              Date temp=DateUtils.parseDate(titolarita[i].getDataValidita());
              if (dataMinD.after(temp))
              {
                dataMinD=temp;
                dataMinS=titolarita[i].getDataValidita();
              }
            }
            catch(Exception e) {}  
            
            htmpl.newBlock("blkTitolarita.blkTitMultiple");
            htmpl.set("blkTitolarita.blkTitMultiple.codiceDiritto", aggiungiQuadre(titolarita[i].getCodiceDiritto()));
            htmpl.set("blkTitolarita.blkTitMultiple.descDiritto", titolarita[i].getDescDiritto());
            htmpl.set("blkTitolarita.blkTitMultiple.quotaNumeratore", titolarita[i].getQuotaNumeratore());
            htmpl.set("blkTitolarita.blkTitMultiple.quotaDenominatore", titolarita[i].getQuotaDenominatore());
            htmpl.set("blkTitolarita.blkTitMultiple.codRegime", aggiungiQuadre(titolarita[i].getCodRegime()));
            htmpl.set("blkTitolarita.blkTitMultiple.descRegime", titolarita[i].getDescRegime());
            
            for (int j=0;j<2;j++)
            {
              SoggettoCatastale soggetto=null;
              String blk=null;
              if (j==0) 
              {
                soggetto=titolarita[i].getSoggetto();
                //Se sono presenti due titolarità devo portare il rowspan a 2
                if (titolarita[i].getSoggettoDiRiferimento()!=null)
                  htmpl.set("blkTitolarita.blkTitMultiple.numSoggetti", "2");
                else
                  htmpl.set("blkTitolarita.blkTitMultiple.numSoggetti", "1");  
                blk="blkTitolarita.blkTitMultiple";  
              }
              else 
              {
                soggetto=titolarita[i].getSoggettoDiRiferimento();
                if (soggetto!=null)
                {
                  htmpl.newBlock("blkTitolarita.blkTitMultiple.blkSoggettoDiRiferimento");
                  blk="blkTitolarita.blkTitMultiple.blkSoggettoDiRiferimento";
                }
              }
              if (soggetto!=null)
              {
                String tipoPersona=null;
                String toolTipPersona=null;
                String codiceFiscale=null;
                String denominazione=null;
                String nome=null;
                String sesso=null;
                String dataNascita=null;
                String luogoNascita=null;
                
                if ("P".equals(soggetto.getTipoSoggetto())) 
                {
                  tipoPersona="F";
                  toolTipPersona="Persona fisica";
                  if(soggetto.getSoggFisico()!=null)
                  {
                    codiceFiscale=soggetto.getSoggFisico().getCodFiscale();
                    denominazione=soggetto.getSoggFisico().getCognome();
                    nome=soggetto.getSoggFisico().getNome();
                    sesso=soggetto.getSoggFisico().getSesso();
                    if ("1".equals(sesso)) sesso="M";
                    if ("2".equals(sesso)) sesso="F";
                    dataNascita=soggetto.getSoggFisico().getDataNascita();
                    luogoNascita=soggetto.getSoggFisico().getLuogoNascita();
                  }
                }
                else
                {
                  tipoPersona="G";
                  toolTipPersona="Persona giuridica";
                  if(soggetto.getSoggGiuridico()!=null)
                  {
                    codiceFiscale=soggetto.getSoggGiuridico().getCodFiscale();
                    denominazione=soggetto.getSoggGiuridico().getDenominazione();
                    luogoNascita=soggetto.getSoggGiuridico().getSede();
                  }
                }
                
                ComuneVO comuneVO = null;
                try 
                {
                  comuneVO = anagFacadeClient.getComuneByCUAA(luogoNascita);
                }
                catch(Exception e) {
                }
                
                
                htmpl.set(blk+".tipoPersona", tipoPersona);
                htmpl.set(blk+".toolTipPersona", toolTipPersona);
                htmpl.set(blk+".codiceFiscale", codiceFiscale);
                htmpl.set(blk+".denominazione", denominazione);
                htmpl.set(blk+".nome", nome);
                htmpl.set(blk+".sesso", sesso);
                htmpl.set(blk+".dataNascita", dataNascita);
                if (comuneVO!=null)
                {
                  htmpl.set(blk+".luogoNascitaCom", comuneVO.getDescom());
                  htmpl.set(blk+".luogoNascitaProv", comuneVO.getSiglaProv());
                }
              }
            }
            
            htmpl.set("blkTitolarita.blkTitMultiple.protNotifica", titolarita[i].getProtNotifica());
            htmpl.set("blkTitolarita.blkTitMultiple.dataNotifica", titolarita[i].getDataNotifica());
            
            if (titolarita[i].getMutazioneIniziale()!=null)
            {
              htmpl.set("blkTitolarita.blkTitMultiple.dataEfficaciaMutIniz", titolarita[i].getMutazioneIniziale().getDataEfficacia());
              htmpl.set("blkTitolarita.blkTitMultiple.codCausaleMutIniz", aggiungiQuadre(titolarita[i].getMutazioneIniziale().getCodCausale()));
              htmpl.set("blkTitolarita.blkTitMultiple.descrCausaleMutIniz", titolarita[i].getMutazioneIniziale().getDescrCausale());
            }
            
            if (titolarita[i].getMutazioneFinale()!=null)
            {
              htmpl.set("blkTitolarita.blkTitMultiple.dataEfficaciaMutFinale", titolarita[i].getMutazioneFinale().getDataEfficacia());
              htmpl.set("blkTitolarita.blkTitMultiple.codCausaleMutFinale", aggiungiQuadre(titolarita[i].getMutazioneFinale().getCodCausale()));
              htmpl.set("blkTitolarita.blkTitMultiple.descrCausaleMutFinale", titolarita[i].getMutazioneFinale().getDescrCausale());
            }
            
            
          }
          if (!"31/12/9999".equals(dataMinS))
            htmpl.set("blkTitolarita.dataMinTitolarita", dataMinS);
        }
        
        
        if(Validator.isNotEmpty(messaggioErrore)) 
			  {
			    htmpl.newBlock("blkImportazioneErrore");
			    htmpl.set("blkImportazioneErrore.messaggioErrore", messaggioErrore);
			  } 
        
        
        
        /*********************************************
         *   Blocco Titolarità - aggiornata  end     *
         *********************************************/  

        htmpl.newBlock("blkLegenda");
      }
      else
      {
        //Nessuna dato trovato
        htmpl.newBlock("blkErrore");
        htmpl.set("blkErrore.messaggio1", "PER QUESTA PARTICELLA NON è STATA TROVATA NESSUNA CORRISPONDENZA SU SIGMATER");
      }
    }
  }
  catch(Exception e)
  {
    SolmrLogger.getStackTrace(e);
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio1", AnagErrors.ERRORE_500);
    htmpl.set("blkErrore.messaggio2", e.toString());
  }
  out.print(htmpl.text());
  SolmrLogger.debug(this, " - terreniParticellareSigmaterView.jsp - FINE PAGINA");
%>

<%! 
  private String aggiungiQuadre(String testo)
  {
    if (testo!=null)
    {
      testo=testo.trim();
      if (Validator.isNotEmpty(testo) && !"_".equals(testo)) testo="["+testo+"] ";
    }
    else testo= "";
    return testo;
  }
%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.PolizzaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.PolizzaDettaglioVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/polizza_dettaglio.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
  

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	PolizzaVO polizzaVO = (PolizzaVO)request.getAttribute("polizzaVO");
  TreeMap<Long,Vector<PolizzaDettaglioVO>> tPolizzaDettaglioColturaVO = 
    (TreeMap<Long,Vector<PolizzaDettaglioVO>>)request.getAttribute("tPolizzaDettaglioColturaVO");
  TreeMap<Long,Vector<PolizzaDettaglioVO>> tPolizzaDettaglioStrutturaVO = 
    (TreeMap<Long,Vector<PolizzaDettaglioVO>>)request.getAttribute("tPolizzaDettaglioStrutturaVO");
  TreeMap<Long,Vector<PolizzaDettaglioVO>> tPolizzaDettaglioZootecniaVO = 
    (TreeMap<Long,Vector<PolizzaDettaglioVO>>)request.getAttribute("tPolizzaDettaglioZootecniaVO");  
  
  String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	// Se si è verificato un errore
 	if(Validator.isNotEmpty(messaggioErrore)) {
   		htmpl.newBlock("blkErrore");
   		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti popolo la sezione relativa al dettaglio
 	else 
  {
    if(Validator.isNotEmpty(polizzaVO))
    {
      htmpl.set("idPolizzaAssicurativa", ""+polizzaVO.getIdPolizzaAssicurativa());
      htmpl.set("numeroPolizza", polizzaVO.getNumeroPolizza());
      htmpl.set("annoCampagna", ""+polizzaVO.getAnnoCampagna());
      String interventoElenco = "["+polizzaVO.getCodiceIntervento()+"]"
        +" "+polizzaVO.getDescrizioneIntervento();
      htmpl.set("intervento", interventoElenco);
      String compagnia = "["+polizzaVO.getCodiceCompagnia()+"]"
        +" "+polizzaVO.getDenominazioneCompagnia();
      htmpl.set("compagnia", compagnia);
      
      if(Validator.isNotEmpty(polizzaVO.getCodiceConsorzio()))
      {
        String consorzio = "["+polizzaVO.getCodiceConsorzio()+"]"
          +" "+polizzaVO.getDescrizioneConsorzio();
        htmpl.set("consorzio", consorzio);
      }
      
      String contributoConsorzio = "No";
      if(polizzaVO.getContributoConsorzio().equalsIgnoreCase("S"))
        contributoConsorzio = "Si";
      htmpl.set("contributoConsorzio", contributoConsorzio);
      
      htmpl.set("dataStipulazione", DateUtils.formatDateNotNull(polizzaVO.getDataPolizza()));
      htmpl.set("dataQuietanza", DateUtils.formatDateNotNull(polizzaVO.getDataQuietanza()));
      htmpl.set("periodoRiferimento", polizzaVO.getDescrizionePeriodo());
      htmpl.set("dataInformatizzazione", DateUtils.formatDateNotNull(polizzaVO.getDataInformatizzazione()));
      String polizzaIntegrativa = "No";
      if("S".equalsIgnoreCase(polizzaVO.getPolizzaIntegrativa()))
      {
        polizzaIntegrativa = "Si";
      }
      htmpl.set("polizzaIntegrativa", polizzaIntegrativa);
      htmpl.set("giorniFuoriTermine", Formatter.formatDouble(polizzaVO.getGiorniFuoriTermine()));
      String aggiuntiva = "No";
      if("S".equalsIgnoreCase(polizzaVO.getAggiuntiva()))
      {
        aggiuntiva = "Si";
      }
      htmpl.set("aggiuntiva", aggiuntiva);
    
    }
    
    
    if(Validator.isNotEmpty(tPolizzaDettaglioColturaVO))
    {
    
      BigDecimal totSupAssicurata = new BigDecimal(0);
      BigDecimal totQtaAssicurata = new BigDecimal(0);
      BigDecimal totValAssicurata = new BigDecimal(0);
      BigDecimal totImportoPremio = new BigDecimal(0);
      //BigDecimal totImportoProposto = new BigDecimal(0);
      BigDecimal totImportoPagato = new BigDecimal(0);
      BigDecimal totSupDanneggiata = new BigDecimal(0);
      BigDecimal totQtaDanneggiata = new BigDecimal(0);
      BigDecimal totSupRisarcita = new BigDecimal(0);
      BigDecimal totQtaRisarcita = new BigDecimal(0);
      BigDecimal totValRisarcita = new BigDecimal(0);
      
      
      htmpl.newBlock("blkDettaglioPolizzaColtura");
      Collection<Vector<PolizzaDettaglioVO>> c = tPolizzaDettaglioColturaVO.values();
      Iterator<Vector<PolizzaDettaglioVO>> itr = c.iterator();

      while(itr.hasNext())
      {
        Vector<PolizzaDettaglioVO> vPolizzaDettaglioColturaVO = itr.next();
        boolean first = true;
        for(int i=0;i<vPolizzaDettaglioColturaVO.size();i++)
        {
          PolizzaDettaglioVO polDetVO = vPolizzaDettaglioColturaVO.get(i);
          String blk = "blkDettaglioPolizzaColtura.blkElencoDettaglioPolizzaColtura";
          htmpl.newBlock(blk);
          String descComune = polDetVO.getDescComune() +" ("+polDetVO.getSiglaProvincia()+")";
          htmpl.set(blk+".descComune", descComune);
          
          String descMacroUso = "["+polDetVO.getCodMacroUso()+"]"
            +" "+polDetVO.getDescMacroUso();
          htmpl.set(blk+".descMacroUso", descMacroUso);
          
          String descProdotto = "["+polDetVO.getCodProdotto()+"]"
            +" "+polDetVO.getDescProdotto();
          htmpl.set(blk+".descProdotto", descProdotto);
          htmpl.set(blk+".progrRaccolto", Formatter.formatDouble(polDetVO.getProgrRaccolto()));
          
          htmpl.set(blk+".percentualeFranchigia", ""+polDetVO.getPercentualeFranchigia());
          
          if(first)
          {
            String blk2 = "blkDettaglioPolizzaColtura.blkElencoDettaglioPolizzaColtura.blkRigaPrimo";
            htmpl.newBlock(blk2);
            htmpl.set(blk2+".dimRighe", ""+vPolizzaDettaglioColturaVO.size());
            htmpl.set(blk2+".parMipaaf", Formatter.formatDouble2Separator(polDetVO.getParametroMipaaf()));
            htmpl.set(blk2+".spesaParametrata", Formatter.formatDouble2Separator(polDetVO.getSpesaParametrata()));
            htmpl.set(blk2+".spesaAmmessa", Formatter.formatDouble2Separator(polDetVO.getSpesaAmmessa()));
            BigDecimal supUtil = polDetVO.getSuperficieUtilizzata();
            if(Validator.isNotEmpty(supUtil))
            {
              supUtil = supUtil.divide(new BigDecimal(10000));
              htmpl.set(blk2+".superficieUtilizzata", Formatter.formatDouble4Separator(supUtil));
              
            }
            /*if(Validator.isNotEmpty(polDetVO.getImportoProposto()))
            {
              totImportoProposto = totImportoProposto.add(polDetVO.getImportoProposto());
            }           
            htmpl.set(blk2+".importoProposto", Formatter.formatDouble2Separator(polDetVO
              .getImportoProposto()));*/
            if(Validator.isNotEmpty(polDetVO.getImportoPagato()))
            {
              totImportoPagato = totImportoPagato.add(polDetVO.getImportoPagato());
            } 
            htmpl.set(blk2+".importoPagato", Formatter.formatDouble2Separator(polDetVO
              .getImportoPagato()));
              
            /*if(Validator.isNotEmpty(polDetVO.getAnomalia())) 
            {
              htmpl.set(blk2+".anomalia", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, polDetVO.getAnomalia(), ""}), null);              
            }*/
            
            first = false; 
            
            
          }
          
          
          BigDecimal supAss = polDetVO.getSuperficieAssicurata();
          if(Validator.isNotEmpty(supAss))
          {
            supAss = supAss.divide(new BigDecimal(10000));
            totSupAssicurata = totSupAssicurata.add(supAss);
            htmpl.set(blk+".superficieAssicurata", Formatter.formatDouble4Separator(supAss));            
          }
          
          String quantitaAssicurata = "";
          if(Validator.isNotEmpty(polDetVO.getQuantitaAssicurata()))
          {
            totQtaAssicurata = totQtaAssicurata.add(polDetVO.getQuantitaAssicurata());
            quantitaAssicurata = Formatter.formatDouble2Separator(polDetVO.getQuantitaAssicurata())
              + " ("+polDetVO.getUnitaMisura()+")";
          }
          htmpl.set(blk+".quantitaAssicurata", quantitaAssicurata);
          if(Validator.isNotEmpty(polDetVO.getValoreAssicurato()))
          {
            totValAssicurata = totValAssicurata.add(polDetVO.getValoreAssicurato());
          }
          htmpl.set(blk+".valoreAssicurato", Formatter.formatDouble2Separator(polDetVO.getValoreAssicurato()));
          htmpl.set(blk+".tassoApplicato", Formatter.formatDouble2Separator(polDetVO.getTassoApplicato()));
          if(Validator.isNotEmpty(polDetVO.getImportoPremio()))
          {
            totImportoPremio = totImportoPremio.add(polDetVO.getImportoPremio());
          }
          htmpl.set(blk+".importoPremio", Formatter.formatDouble2Separator(polDetVO
              .getImportoPremio()));
              
          String descGaranzia = "";
          Vector<String> vGaranzia = polDetVO.getVDescGaranzia();
          if(Validator.isNotEmpty(vGaranzia))
          {
            for(int j=0;j<vGaranzia.size();j++)
            {
              descGaranzia += vGaranzia.get(j);
              if(j !=(vGaranzia.size()-1))
              {
                descGaranzia += " - ";
              }
            }
          }    
          htmpl.set(blk+".descGaranzia", descGaranzia);
          
          BigDecimal supDan = polDetVO.getSuperficieDanneggiata();
          if(Validator.isNotEmpty(supDan))
          {
            supDan = supDan.divide(new BigDecimal(10000));
            totSupDanneggiata = totSupDanneggiata.add(supDan);
            htmpl.set(blk+".superficieDannegiata", Formatter.formatDouble4Separator(supDan));            
          }
          String quantitaDanneggiata = "";
          if(Validator.isNotEmpty(polDetVO.getQuantitaDanneggiata()))
          {
            totQtaDanneggiata = totQtaDanneggiata.add(polDetVO.getQuantitaDanneggiata());
            quantitaDanneggiata = Formatter.formatDouble2Separator(polDetVO.getQuantitaDanneggiata())
              + " ("+polDetVO.getUnitaMisura()+")";
          }
          htmpl.set(blk+".quantitaDanneggiata", quantitaDanneggiata);
          
          BigDecimal supRis = polDetVO.getSuperficieRisarcita();
          if(Validator.isNotEmpty(supRis))
          {
            supRis = supRis.divide(new BigDecimal(10000));
            totSupRisarcita = totSupRisarcita.add(supRis);
            htmpl.set(blk+".superficieRisarcita", Formatter.formatDouble4Separator(supRis));            
          }
          String quantitaRisarcita = "";
          if(Validator.isNotEmpty(polDetVO.getQuantitaRisarcita()))
          {
            totQtaRisarcita = totQtaRisarcita.add(polDetVO.getQuantitaRisarcita());
            quantitaRisarcita = Formatter.formatDouble2Separator(polDetVO.getQuantitaRisarcita())
              + " ("+polDetVO.getUnitaMisura()+")";
          }
          htmpl.set(blk+".quantitaRisarcita", quantitaRisarcita);
          if(Validator.isNotEmpty(polDetVO.getValoreRisarcito()))
          {
            totValRisarcita = totValRisarcita.add(polDetVO.getValoreRisarcito());
          }
          htmpl.set(blk+".valoreRisarcito", Formatter.formatDouble2Separator(
            polDetVO.getValoreRisarcito()));
            
          htmpl.set(blk+".dataInizioCopertura", DateUtils.formatDateNotNull(
            polDetVO.getDataInizioCopertura()));
          htmpl.set(blk+".dataFineCopertura", DateUtils.formatDateNotNull(
            polDetVO.getDataFineCopertura()));
            
          if(Validator.isNotEmpty(polDetVO.getvAnomalia())) 
          {
            String anomalia = "";
            for(int j=0;j<polDetVO.getvAnomalia().size();j++)
            {
              anomalia += polDetVO.getvAnomalia().get(j);
              if(j !=polDetVO.getvAnomalia().size()-1)
              {
                anomalia += " - ";
              }
            }
            htmpl.set(blk+".anomalia", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, anomalia, ""}), null);              
          }
          
          String extraResa = "No";
          if("S".equalsIgnoreCase(polDetVO.getExtraResa()))
          {
            extraResa = "Si";
          }        
          htmpl.set(blk+".extraResa", extraResa);
        
        } // fine for
      
      } // fine while
    
      
      htmpl.set("blkDettaglioPolizzaColtura.totSupAssicurata", 
        Formatter.formatDouble4Separator(totSupAssicurata));
      htmpl.set("blkDettaglioPolizzaColtura.totQtaAssicurata", 
        Formatter.formatDouble2Separator(totQtaAssicurata));
      htmpl.set("blkDettaglioPolizzaColtura.totValAssicurata",
        Formatter.formatDouble2Separator(totValAssicurata));
      htmpl.set("blkDettaglioPolizzaColtura.totImportoPremio", 
        Formatter.formatDouble2Separator(totImportoPremio));
      //htmpl.set("blkDettaglioPolizzaColtura.totImportoProposto",
        //Formatter.formatDouble2Separator(totImportoProposto));
      htmpl.set("blkDettaglioPolizzaColtura.totImportoPagato",
        Formatter.formatDouble2Separator(totImportoPagato));
      htmpl.set("blkDettaglioPolizzaColtura.totSupDanneggiata",
        Formatter.formatDouble4Separator(totSupDanneggiata));
      htmpl.set("blkDettaglioPolizzaColtura.totQtaDanneggiata",
        Formatter.formatDouble2Separator(totQtaDanneggiata));
      htmpl.set("blkDettaglioPolizzaColtura.totSupRisarcita",
        Formatter.formatDouble4Separator(totSupRisarcita));
      htmpl.set("blkDettaglioPolizzaColtura.totQtaRisarcita",
        Formatter.formatDouble2Separator(totQtaRisarcita));
      htmpl.set("blkDettaglioPolizzaColtura.totValRisarcita",
        Formatter.formatDouble2Separator(totValRisarcita));
    }
    
    
    if(Validator.isNotEmpty(tPolizzaDettaglioStrutturaVO))
    {
    
      BigDecimal totSupAssicurata = new BigDecimal(0);
      BigDecimal totValAssicurata = new BigDecimal(0);
      BigDecimal totImportoPremio = new BigDecimal(0);
      BigDecimal totImportoProposto = new BigDecimal(0);
      BigDecimal totImportoPagato = new BigDecimal(0);
      BigDecimal totSupDanneggiata = new BigDecimal(0);
      BigDecimal totSupRisarcita = new BigDecimal(0);
      BigDecimal totValRisarcita = new BigDecimal(0);
      
      
      htmpl.newBlock("blkDettaglioPolizzaStruttura");
      Collection<Vector<PolizzaDettaglioVO>> c = tPolizzaDettaglioStrutturaVO.values();
      Iterator<Vector<PolizzaDettaglioVO>> itr = c.iterator();

      while(itr.hasNext())
      {
        Vector<PolizzaDettaglioVO> vPolizzaDettaglioStrutturaVO = itr.next();
        boolean first = true;
        for(int i=0;i<vPolizzaDettaglioStrutturaVO.size();i++)
        {
          PolizzaDettaglioVO polDetVO = vPolizzaDettaglioStrutturaVO.get(i);
          String blk = "blkDettaglioPolizzaStruttura.blkElencoDettaglioPolizzaStruttura";
          htmpl.newBlock(blk);
          String descComune = polDetVO.getDescComune() +" ("+polDetVO.getSiglaProvincia()+")";
          htmpl.set(blk+".descComune", descComune);
          
          String descProdotto = "["+polDetVO.getCodProdotto()+"]"
            +" "+polDetVO.getDescProdotto();
          htmpl.set(blk+".descProdotto", descProdotto);
          
          htmpl.set(blk+".percentualeFranchigia", ""+polDetVO.getPercentualeFranchigia());
          
          if(first)
          {
            String blk2 = "blkDettaglioPolizzaStruttura.blkElencoDettaglioPolizzaStruttura.blkRigaPrimo";
            htmpl.newBlock(blk2);
            htmpl.set(blk2+".dimRighe", ""+vPolizzaDettaglioStrutturaVO.size());
            htmpl.set(blk2+".parMipaaf", Formatter.formatDouble2Separator(polDetVO.getParametroMipaaf()));
            htmpl.set(blk2+".spesaParametrata", Formatter.formatDouble2Separator(polDetVO.getSpesaParametrata()));
            htmpl.set(blk2+".spesaAmmessa", Formatter.formatDouble2Separator(polDetVO.getSpesaAmmessa()));
            BigDecimal supUtil = polDetVO.getSuperficieUtilizzata();
            if(Validator.isNotEmpty(supUtil))
            {
              supUtil = supUtil.divide(new BigDecimal(10000));
              htmpl.set(blk2+".superficieUtilizzata", Formatter.formatDouble4Separator(supUtil));
              
            }
            if(Validator.isNotEmpty(polDetVO.getImportoProposto()))
            {
              totImportoProposto = totImportoProposto.add(polDetVO.getImportoProposto());
            }           
            htmpl.set(blk2+".importoProposto", Formatter.formatDouble2Separator(polDetVO
              .getImportoProposto()));
            if(Validator.isNotEmpty(polDetVO.getImportoPagato()))
            {
              totImportoPagato = totImportoPagato.add(polDetVO.getImportoPagato());
            } 
            htmpl.set(blk2+".importoPagato", Formatter.formatDouble2Separator(polDetVO
              .getImportoPagato()));
              
            /*if(Validator.isNotEmpty(polDetVO.getAnomalia())) 
            {
              htmpl.set(blk2+".anomalia", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, polDetVO.getAnomalia(), ""}), null);              
            }*/
            
            first = false; 
            
            
          }
          
          
          BigDecimal supAss = polDetVO.getSuperficieAssicurata();
          if(Validator.isNotEmpty(supAss))
          {
            supAss = supAss.divide(new BigDecimal(10000));
            totSupAssicurata = totSupAssicurata.add(supAss);
            htmpl.set(blk+".superficieAssicurata", Formatter.formatDouble4Separator(supAss));            
          }
          if(Validator.isNotEmpty(polDetVO.getValoreAssicurato()))
          {
            totValAssicurata = totValAssicurata.add(polDetVO.getValoreAssicurato());
          }
          htmpl.set(blk+".valoreAssicurato", Formatter.formatDouble2Separator(polDetVO.getValoreAssicurato()));
          htmpl.set(blk+".tassoApplicato", Formatter.formatDouble2Separator(polDetVO.getTassoApplicato()));
          if(Validator.isNotEmpty(polDetVO.getImportoPremio()))
          {
            totImportoPremio = totImportoPremio.add(polDetVO.getImportoPremio());
          }
          htmpl.set(blk+".importoPremio", Formatter.formatDouble2Separator(polDetVO
              .getImportoPremio()));
              
          String descGaranzia = "";
          Vector<String> vGaranzia = polDetVO.getVDescGaranzia();
          if(Validator.isNotEmpty(vGaranzia))
          {
            for(int j=0;j<vGaranzia.size();j++)
            {
              descGaranzia += vGaranzia.get(j);
              if(j !=(vGaranzia.size()-1))
              {
                descGaranzia += " - ";
              }
            }
          }    
          htmpl.set(blk+".descGaranzia", descGaranzia);
          
          BigDecimal supDan = polDetVO.getSuperficieDanneggiata();
          if(Validator.isNotEmpty(supDan))
          {
            supDan = supDan.divide(new BigDecimal(10000));
            totSupDanneggiata = totSupDanneggiata.add(supDan);
            htmpl.set(blk+".superficieDannegiata", Formatter.formatDouble4Separator(supDan));            
          }
          if(Validator.isNotEmpty(polDetVO.getSuperficieRisarcita()))
          {
            totSupRisarcita = totSupRisarcita.add(polDetVO.getSuperficieRisarcita());
          }
          htmpl.set(blk+".superficieRisarcita", Formatter.formatDouble4Separator(
            polDetVO.getSuperficieRisarcita()));
          if(Validator.isNotEmpty(polDetVO.getValoreRisarcito()))
          {
            totValRisarcita = totValRisarcita.add(polDetVO.getValoreRisarcito());
          }
          htmpl.set(blk+".valoreRisarcito", Formatter.formatDouble2Separator(
            polDetVO.getValoreRisarcito()));
            
          htmpl.set(blk+".dataInizioCopertura", DateUtils.formatDateNotNull(
            polDetVO.getDataInizioCopertura()));
          htmpl.set(blk+".dataFineCopertura", DateUtils.formatDateNotNull(
            polDetVO.getDataFineCopertura()));
            
            
          if(Validator.isNotEmpty(polDetVO.getvAnomalia())) 
          {
            String anomalia = "";
            for(int j=0;j<polDetVO.getvAnomalia().size();j++)
            {
              anomalia += polDetVO.getvAnomalia();
              if(j !=polDetVO.getvAnomalia().size()-1)
              {
                anomalia += " - ";
              }
            }
            htmpl.set(blk+".anomalia", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, anomalia, ""}), null);              
          }
        
        } // fine for
      
      } // fine while
    
      
      htmpl.set("blkDettaglioPolizzaStruttura.totSupAssicurata", 
        Formatter.formatDouble4Separator(totSupAssicurata));
      htmpl.set("blkDettaglioPolizzaStruttura.totValAssicurata",
        Formatter.formatDouble2Separator(totValAssicurata));
      htmpl.set("blkDettaglioPolizzaStruttura.totImportoPremio", 
        Formatter.formatDouble2Separator(totImportoPremio));
      htmpl.set("blkDettaglioPolizzaStruttura.totImportoProposto",
        Formatter.formatDouble2Separator(totImportoProposto));
      htmpl.set("blkDettaglioPolizzaStruttura.totImportoPagato",
        Formatter.formatDouble2Separator(totImportoPagato));
      htmpl.set("blkDettaglioPolizzaStruttura.totSupDanneggiata",
        Formatter.formatDouble4Separator(totSupDanneggiata));
      htmpl.set("blkDettaglioPolizzaStruttura.totSupRisarcita",
        Formatter.formatDouble4Separator(totSupRisarcita));
      htmpl.set("blkDettaglioPolizzaStruttura.totValRisarcita",
        Formatter.formatDouble2Separator(totValRisarcita));
    }
    
    
    if(Validator.isNotEmpty(tPolizzaDettaglioZootecniaVO))
    {
    
      BigDecimal totQtaFranchigia = new BigDecimal(0);
      BigDecimal totQtaAssicurata = new BigDecimal(0);
      BigDecimal totValAssicurata = new BigDecimal(0);
      BigDecimal totImportoPremio = new BigDecimal(0);
      BigDecimal totImportoProposto = new BigDecimal(0);
      BigDecimal totImportoPagato = new BigDecimal(0);
      BigDecimal totQtaRitirata = new BigDecimal(0);
      BigDecimal totQtaRisarcita = new BigDecimal(0);
      BigDecimal totValRisarcita = new BigDecimal(0);
      
      
      htmpl.newBlock("blkDettaglioPolizzaZootecnia");
      Collection<Vector<PolizzaDettaglioVO>> c = tPolizzaDettaglioZootecniaVO.values();
      Iterator<Vector<PolizzaDettaglioVO>> itr = c.iterator();

      while(itr.hasNext())
      {
        Vector<PolizzaDettaglioVO> vPolizzaDettaglioZootecniaVO = itr.next();
        boolean first = true;
        for(int i=0;i<vPolizzaDettaglioZootecniaVO.size();i++)
        {
          PolizzaDettaglioVO polDetVO = vPolizzaDettaglioZootecniaVO.get(i);
          String blk = "blkDettaglioPolizzaZootecnia.blkElencoDettaglioPolizzaZootecnia";
          htmpl.newBlock(blk);
          String descComune = polDetVO.getDescComune() +" ("+polDetVO.getSiglaProvincia()+")";
          htmpl.set(blk+".descComune", descComune);
          htmpl.set(blk+".indirizzo", polDetVO.getIndirizzo());
          
          String descProdotto = "["+polDetVO.getCodProdotto()+"]"
            +" "+polDetVO.getDescProdotto();
          htmpl.set(blk+".descProdotto", descProdotto);
          
          htmpl.set(blk+".codiceAsl", polDetVO.getCodiceAsl());
          
          if(first)
          {
            String blk2 = "blkDettaglioPolizzaZootecnia.blkElencoDettaglioPolizzaZootecnia.blkRigaPrimo";
            htmpl.newBlock(blk2);
            htmpl.set(blk2+".dimRighe", ""+vPolizzaDettaglioZootecniaVO.size());
            htmpl.set(blk2+".parMipaaf", Formatter.formatDouble2Separator(polDetVO.getParametroMipaaf()));
            htmpl.set(blk2+".spesaParametrata", Formatter.formatDouble2Separator(polDetVO.getSpesaParametrata()));
            htmpl.set(blk2+".spesaAmmessa", Formatter.formatDouble2Separator(polDetVO.getSpesaAmmessa()));
            
            if(Validator.isNotEmpty(polDetVO.getImportoProposto()))
            {
              totImportoProposto = totImportoProposto.add(polDetVO.getImportoProposto());
            }           
            htmpl.set(blk2+".importoProposto", Formatter.formatDouble2Separator(polDetVO
              .getImportoProposto()));
            if(Validator.isNotEmpty(polDetVO.getImportoPagato()))
            {
              totImportoPagato = totImportoPagato.add(polDetVO.getImportoPagato());
            } 
            htmpl.set(blk2+".importoPagato", Formatter.formatDouble2Separator(polDetVO
              .getImportoPagato()));
              
            /*if(Validator.isNotEmpty(polDetVO.getAnomalia())) 
            {
              htmpl.set(blk2+".anomalia", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, polDetVO.getAnomalia(), ""}), null);              
            }*/
            
            first = false; 
            
            
          }
          
          
          htmpl.set(blk+".percentualeFranchigia", ""+polDetVO.getPercentualeFranchigia());          
          BigDecimal qtaFranchigia = polDetVO.getQuantitaCopertaFranchigia();
          if(Validator.isNotEmpty(qtaFranchigia))
          {
            totQtaFranchigia = totQtaFranchigia.add(qtaFranchigia);
            htmpl.set(blk+".quantitaFranchigia", Formatter.formatDouble2Separator(qtaFranchigia));            
          }
          
          String quantitaAssicurata = "";
          if(Validator.isNotEmpty(polDetVO.getQuantitaAssicurata()))
          {
            totQtaAssicurata = totQtaAssicurata.add(polDetVO.getQuantitaAssicurata());
            quantitaAssicurata = Formatter.formatDouble2Separator(polDetVO.getQuantitaAssicurata());
          }
          htmpl.set(blk+".quantitaAssicurata", quantitaAssicurata);
          if(Validator.isNotEmpty(polDetVO.getValoreAssicurato()))
          {
            totValAssicurata = totValAssicurata.add(polDetVO.getValoreAssicurato());
          }
          htmpl.set(blk+".valoreAssicurato", Formatter.formatDouble2Separator(polDetVO.getValoreAssicurato()));
          htmpl.set(blk+".tassoApplicato", Formatter.formatDouble2Separator(polDetVO.getTassoApplicato()));
          if(Validator.isNotEmpty(polDetVO.getImportoPremio()))
          {
            totImportoPremio = totImportoPremio.add(polDetVO.getImportoPremio());
          }
          htmpl.set(blk+".importoPremio", Formatter.formatDouble2Separator(polDetVO
              .getImportoPremio()));
              
          String descGaranzia = "";
          Vector<String> vGaranzia = polDetVO.getVDescGaranzia();
          if(Validator.isNotEmpty(vGaranzia))
          {
            for(int j=0;j<vGaranzia.size();j++)
            {
              descGaranzia += vGaranzia.get(j);
              if(j !=(vGaranzia.size()-1))
              {
                descGaranzia += " - ";
              }
            }
          }    
          htmpl.set(blk+".descGaranzia", descGaranzia);
          
          String descEpizoozia = "";
          Vector<String> vEpizoozia = polDetVO.getVDescEpizoozia();
          if(Validator.isNotEmpty(vEpizoozia))
          {
            for(int j=0;j<vEpizoozia.size();j++)
            {
              descEpizoozia += vEpizoozia.get(j);
              if(j !=(vGaranzia.size()-1))
              {
                descEpizoozia += " - ";
              }
            }
          }    
          htmpl.set(blk+".descEpizoozia", descEpizoozia);
          
          String quantitaRitirata = "";
          if(Validator.isNotEmpty(polDetVO.getQuantitaRitirata()))
          {
            totQtaRitirata = totQtaRitirata.add(polDetVO.getQuantitaRitirata());
            quantitaRitirata = Formatter.formatDouble2Separator(polDetVO.getQuantitaRitirata());
          }
          htmpl.set(blk+".quantitaRitirata", quantitaRitirata);
          String quantitaRisarcita = "";
          if(Validator.isNotEmpty(polDetVO.getQuantitaRisarcita()))
          {
            totQtaRisarcita = totQtaRisarcita.add(polDetVO.getQuantitaRisarcita());
            quantitaRisarcita = Formatter.formatDouble2Separator(polDetVO.getQuantitaRisarcita());
          }
          htmpl.set(blk+".quantitaRisarcita", quantitaRisarcita);
          if(Validator.isNotEmpty(polDetVO.getValoreRisarcito()))
          {
            totValRisarcita = totValRisarcita.add(polDetVO.getValoreRisarcito());
          }
          htmpl.set(blk+".valoreRisarcito", Formatter.formatDouble2Separator(
            polDetVO.getValoreRisarcito()));
            
          htmpl.set(blk+".dataInizioCopertura", DateUtils.formatDateNotNull(
            polDetVO.getDataInizioCopertura()));
          htmpl.set(blk+".dataFineCopertura", DateUtils.formatDateNotNull(
            polDetVO.getDataFineCopertura()));
            
          if(Validator.isNotEmpty(polDetVO.getvAnomalia())) 
          {
            String anomalia = "";
            for(int j=0;j<polDetVO.getvAnomalia().size();j++)
            {
              anomalia += polDetVO.getvAnomalia();
              if(j !=polDetVO.getvAnomalia().size()-1)
              {
                anomalia += " - ";
              }
            }
            htmpl.set(blk+".anomalia", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, anomalia, ""}), null);              
          }
        
        } // fine for
      
      } // fine while
    
      
      htmpl.set("blkDettaglioPolizzaZootecnia.totQtaFranchigia", 
        Formatter.formatDouble2Separator(totQtaFranchigia));
      htmpl.set("blkDettaglioPolizzaZootecnia.totQtaAssicurata", 
        Formatter.formatDouble2Separator(totQtaAssicurata));
      htmpl.set("blkDettaglioPolizzaZootecnia.totValAssicurata",
        Formatter.formatDouble2Separator(totValAssicurata));
      htmpl.set("blkDettaglioPolizzaZootecnia.totImportoPremio", 
        Formatter.formatDouble2Separator(totImportoPremio));
      htmpl.set("blkDettaglioPolizzaZootecnia.totImportoProposto",
        Formatter.formatDouble2Separator(totImportoProposto));
      htmpl.set("blkDettaglioPolizzaZootecnia.totImportoPagato",
        Formatter.formatDouble2Separator(totImportoPagato));
      htmpl.set("blkDettaglioPolizzaZootecnia.totQtaRitirata",
        Formatter.formatDouble2Separator(totQtaRitirata));
      htmpl.set("blkDettaglioPolizzaZootecnia.totQtaRisarcita",
        Formatter.formatDouble2Separator(totQtaRisarcita));
      htmpl.set("blkDettaglioPolizzaZootecnia.totValRisarcita",
        Formatter.formatDouble2Separator(totValRisarcita));
    }
    
    
    
   		
  }
%>
<%= htmpl.text()%>

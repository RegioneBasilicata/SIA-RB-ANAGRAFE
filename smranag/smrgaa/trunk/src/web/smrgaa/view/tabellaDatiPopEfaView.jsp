<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/tabellaDatiPopEfa.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  StoricoParticellaVO[] elencoStoricoParticellaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaEfaVO");
  String tipoModifica = request.getParameter("tipoModifica");
  String numRiga = request.getParameter("numRiga");
  String[] arrIdTipoEfa = request.getParameterValues("idTipoEfa");
  
  String[] elencoUtilizziSelezionati = request.getParameterValues("idTipoUtilizzoEfa");
  String[] elencoDestinazioneSelezionate = request.getParameterValues("idTipoDestinazioneEfa");
  String[] elencoDettaglioUsoSelezionate = request.getParameterValues("idTipoDettaglioUsoEfa");
  String[] elencoQualitaUsoSelezionate = request.getParameterValues("idTipoQualitaUsoEfa");
  String[] elencoVarietaSelezionate = request.getParameterValues("idVarietaEfa");
  String[] elencoAbbPonderazioneVarieta = request.getParameterValues("abbPonderazioneVarieta");
  String[] elencoValoreOriginale = request.getParameterValues("valoreOriginale");
  StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO"); 
  
  try
  {
  
    Vector<TipoEfaVO> vTipoEfa = gaaFacadeClient.getListTipoEfa();
    
    
    Hashtable<Integer, Vector<TipoUtilizzoVO>> elencoUtilizzi = new Hashtable<Integer, Vector<TipoUtilizzoVO>>();
    if(Validator.isNotEmpty(arrIdTipoEfa))
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      { 
        if(Validator.isNotEmpty(arrIdTipoEfa[i]))
        {        
	        Vector<TipoUtilizzoVO> vUtilizzi = gaaFacadeClient.getListTipoUtilizzoEfa(new Long(arrIdTipoEfa[i]));
	        if(vUtilizzi != null)
	          elencoUtilizzi.put(new Integer(i), vUtilizzi);
	      }
      }   
    }
    
    
    // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer, Vector<TipoDestinazioneVO>> elencoDestinazioneEfa = new Hashtable<Integer, Vector<TipoDestinazioneVO>>();
	  if(Validator.isNotEmpty(arrIdTipoEfa)) 
	  {
	    for(int i = 0; i < arrIdTipoEfa.length; i++) 
	    { 
	      if(Validator.isNotEmpty(arrIdTipoEfa[i]))
	      {        
	        Vector<TipoDestinazioneVO> vTipoDestinazione = gaaFacadeClient.getListTipoDestinazioneEfa(
	          new Long(arrIdTipoEfa[i]), new Long(elencoUtilizziSelezionati[i]));
	        elencoDestinazioneEfa.put(new Integer(i), vTipoDestinazione);
	      }       
	    }
	  }
	  request.setAttribute("elencoDestinazioneEfa", elencoDestinazioneEfa);
	  
	  // Recupero la varietà primaria relativa ai tipi uso suolo utilizzati dall'azienda in esame  
    Hashtable<Integer, Vector<TipoDettaglioUsoVO>> elencoDettaglioUso = new Hashtable<Integer, Vector<TipoDettaglioUsoVO>>();
    if(Validator.isNotEmpty(arrIdTipoEfa))
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      {         
        if(Validator.isNotEmpty(arrIdTipoEfa[i])) 
        {
          Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoEfaByMatrice(
          new Long(arrIdTipoEfa[i]), Long.decode(elencoUtilizziSelezionati[i]), Long.decode(elencoDestinazioneSelezionate[i]));
          if(Validator.isNotEmpty(vTipoDettaglioUso))
            elencoDettaglioUso.put(new Integer(i), vTipoDettaglioUso);
        }
      }
    }
    
    Hashtable<Integer, Vector<TipoQualitaUsoVO>> elencoQualitaUsoEfa = new Hashtable<Integer, Vector<TipoQualitaUsoVO>>();
	  if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      {
        if(Validator.isNotEmpty(arrIdTipoEfa[i]))
        {         
          Vector<TipoQualitaUsoVO> vTipoQualitaUso = gaaFacadeClient.getListQualitaUsoEfaByMatrice(new Long(arrIdTipoEfa[i]), 
            new Long(elencoUtilizziSelezionati[i]), new Long(elencoDestinazioneSelezionate[i]), 
            new Long(elencoDettaglioUsoSelezionate[i]));
          if(Validator.isNotEmpty(vTipoQualitaUso))
            elencoQualitaUsoEfa.put(new Integer(i), vTipoQualitaUso);
        }
      }
    }
	  request.setAttribute("elencoQualitaUsoEfa", elencoQualitaUsoEfa);
    
    
    
    // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
    Hashtable<Integer, Vector<TipoVarietaVO>> elencoVarietaEfa = new Hashtable<Integer, Vector<TipoVarietaVO>>();
    if(Validator.isNotEmpty(elencoUtilizziSelezionati)
      && Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
      {         
        if(Validator.isNotEmpty(arrIdTipoEfa[i])) 
        {
          Vector<TipoVarietaVO> varieta = gaaFacadeClient.getListTipoVarietaEfaByMatrice(
            new Long(arrIdTipoEfa[i]), new Long(elencoUtilizziSelezionati[i]),  new Long(elencoDestinazioneSelezionate[i]),
             new Long(elencoDettaglioUsoSelezionate[i]), new Long(elencoQualitaUsoSelezionate[i]));
          if(Validator.isNotEmpty(varieta))
            elencoVarietaEfa.put(new Integer(i), varieta);      
        }
      }
    }
    
 
  
  
    

    
    
  
  
  
    //Calcolo numero utilizzi
    int numeroUtilizzi = 0;
    if(Validator.isNotEmpty(arrIdTipoEfa))
    {
      numeroUtilizzi = arrIdTipoEfa.length;
    }
    
    if(Validator.isNotEmpty(tipoModifica)
      && "inserisci".equalsIgnoreCase(tipoModifica))
    {
      numeroUtilizzi++;
    }
    
    int countUtilizzi = 0;
    for(int i = 0; i < numeroUtilizzi; i++) 
    {
    
      TipoEfaVO tipoEfaVOSel = null;
      if(Validator.isNotEmpty(tipoModifica)
        && "elimina".equalsIgnoreCase(tipoModifica))
      {
        if(i == new Integer(numRiga).intValue())
        {          
          continue;
        }
      }
      
      htmpl.newBlock("blkElencoEfa");
      htmpl.set("blkElencoEfa.numeroUtilizziEfa", String.valueOf(countUtilizzi));
      
      
      // Combo tipo efa
      if(vTipoEfa != null) 
      {
        for(int j=0;j<vTipoEfa.size();j++) 
        {
          TipoEfaVO tipoEfaVO = vTipoEfa.get(j);
          htmpl.newBlock("blkElencoEfa.blkTipoEfa");
          htmpl.set("blkElencoEfa.blkTipoEfa.idTipoEfa", ""+tipoEfaVO.getIdTipoEfa());
          htmpl.set("blkElencoEfa.blkTipoEfa.descrizioneEstesa", tipoEfaVO.getDescrizioneEstesaTipoEfa());
          htmpl.set("blkElencoEfa.blkTipoEfa.descrizione", tipoEfaVO.getDescrizioneTipoEfa());
          if(Validator.isNotEmpty(arrIdTipoEfa)
            && (i < arrIdTipoEfa.length)
            && Validator.isNotEmpty(arrIdTipoEfa[i])
            && new Long(arrIdTipoEfa[i]).longValue() == tipoEfaVO.getIdTipoEfa()) 
          {
            htmpl.set("blkElencoEfa.blkTipoEfa.selected", "selected=\"selected\"", null);
            htmpl.set("blkElencoEfa.descUnitaMisura", tipoEfaVO.getDescUnitaMisura());
            htmpl.set("blkElencoEfa.fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
            htmpl.set("blkElencoEfa.fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
          }
        }
      }
      
      
      if(Validator.isNotEmpty(elencoUtilizzi)
        && (elencoUtilizzi.size() > 0))
      {
        Vector<TipoUtilizzoVO> vTipoUtilizzo = elencoUtilizzi.get(new Integer(i));
        
        if(Validator.isNotEmpty(arrIdTipoEfa)
          && (i < arrIdTipoEfa.length)
          && Validator.isNotEmpty(arrIdTipoEfa[i]))
        {
	        tipoEfaVOSel = gaaFacadeClient.getTipoEfaFromPrimaryKey(new Long(arrIdTipoEfa[i]).longValue());
	      }
	      if(Validator.isNotEmpty(vTipoUtilizzo)
	        && vTipoUtilizzo.size() > 0)
	      {
		      for(int j=0;j<vTipoUtilizzo.size();j++) 
		      {
		        TipoUtilizzoVO tipoUtilizzoVO = vTipoUtilizzo.get(j);
		        htmpl.newBlock("blkElencoEfa.blkTipiUsoSuoloEfa");
		        
		        htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.idTipoUtilizzoEfa", ""+tipoUtilizzoVO.getIdUtilizzo());
		        htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
		        
		        String descrizione = null;
		        if(tipoUtilizzoVO.getDescrizione().length() > 20) 
		        {
		          descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
		        }
		        else 
		        {
		          descrizione = tipoUtilizzoVO.getDescrizione();
		        }
		        htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
		        
		        if(Validator.isNotEmpty(elencoUtilizziSelezionati)
		          && (i < elencoUtilizziSelezionati.length)
		          && Validator.isNotEmpty(elencoUtilizziSelezionati[i]))
		        {
		          if(new Long(elencoUtilizziSelezionati[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0)
		          {	
		            htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.selected", "selected=\"selected\"", null);
		            
		          }       
		        }
		      }
		    }    
        else
	      {
	        htmpl.newBlock("blkElencoEfa.blkNoTipiUsoSuoloEfa");
	      }
      }
      else
      {
        htmpl.newBlock("blkElencoEfa.blkNoTipiUsoSuoloEfa");
      }
      
      
      if(elencoDestinazioneEfa != null && elencoDestinazioneEfa.size() > 0)
	    {
	      if((request.getParameterValues("idTipoUtilizzoEfa") != null)
	        && (i < request.getParameterValues("idTipoUtilizzoEfa").length)
	        && (request.getParameterValues("idTipoUtilizzoEfa")[i] != null))
        {
          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneEfa.get(new Integer(i));
          if(vDestinazione != null)
          {
            for(int d = 0; d < vDestinazione.size(); d++) 
            {
              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
              htmpl.newBlock("blkElencoEfa.blkTipiDestinazioneEfa");
              htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.idTipoDestinazioneEfa", ""+tipoDestinazioneVO.getIdTipoDestinazione());
              String descrizione = null;
              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
              }
              htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDestinazione") != null && i < request.getParameterValues("idTipoDestinazione").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneEfa")[i]) 
                  && request.getParameterValues("idTipoDestinazioneEfa")[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
                {
                  htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoEfa.blkNoTipiDestinazioneEfa");
		      }
        }
        else
	      {
	        htmpl.newBlock("blkElencoEfa.blkNoTipiDestinazioneEfa");
	      }
      }
      else
      {
        htmpl.newBlock("blkElencoEfa.blkNoTipiDestinazioneEfa");
      }
      
      if(Validator.isNotEmpty(elencoDettaglioUso)
        && (elencoDettaglioUso.size() > 0))
      {
        Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = elencoDettaglioUso.get(new Integer(i));
        if(Validator.isNotEmpty(vTipoDettaglioUso)
          && vTipoDettaglioUso.size() > 0)
        {
          for(int j=0;j<vTipoDettaglioUso.size();j++) 
          {
            TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(j);
            htmpl.newBlock("blkElencoEfa.blkTipiDettaglioUsoEfa");
            
            htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.idTipoDettaglioUsoEfa", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
            htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.descCompleta", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+tipoDettaglioUsoVO.getDescrizione());
            
            String descrizione = null;
            if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
            {
              descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
            }
            else 
            {
              descrizione = tipoDettaglioUsoVO.getDescrizione();
            }
            htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
            
            
            if(Validator.isNotEmpty(elencoDettaglioUsoSelezionate)
              && (i < elencoDettaglioUsoSelezionate.length)
              && Validator.isNotEmpty(elencoDettaglioUsoSelezionate[i]))
            {
              if(new Long(elencoDettaglioUsoSelezionate[i]).compareTo(tipoDettaglioUsoVO.getIdTipoDettaglioUso()) == 0)
              { 
                htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.selected", "selected=\"selected\"", null);
              }       
            }
           
          }
        }
        else
	      {
	        htmpl.newBlock("blkElencoEfa.blkNoTipiDettaglioUsoEfa");
	      }
	    }
      else
      {
        htmpl.newBlock("blkElencoEfa.blkNoTipiDettaglioUsoEfa");
      }
      
      if(Validator.isNotEmpty(elencoQualitaUsoEfa))
	    {
	      Vector<TipoQualitaUsoVO> vTipoQualitaUso = elencoQualitaUsoEfa.get(new Integer(i));
	      if(Validator.isNotEmpty(vTipoQualitaUso)
	        && vTipoQualitaUso.size() > 0)
	      {
	        for(int j=0;j<vTipoQualitaUso.size();j++) 
	        {
	          TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(j);
	          htmpl.newBlock("blkElencoEfa.blkTipiQualitaUsoEfa");
	          
	          htmpl.set("blkElencoEfa.blkTipiQualitaUsoEfa.idTipoQualitaUsoEfa", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
	          
	          String descrizione = null;
	          if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
	          {
	            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
	          }
	          else 
	          {
	            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
	          }
	          htmpl.set("blkElencoEfa.blkTipiQualitaUsoEfa.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
	          
	          if(Validator.isNotEmpty(elencoQualitaUsoSelezionate)
              && (i < elencoQualitaUsoSelezionate.length)
              && Validator.isNotEmpty(elencoQualitaUsoSelezionate[i]))
            {
              if(new Long(elencoQualitaUsoSelezionate[i]).compareTo(tipoQualitaUsoVO.getIdTipoQualitaUso()) == 0)
              { 
                htmpl.set("blkElencoEfa.blkTipiQualitaUsoEfa.selected", "selected=\"selected\"", null); 
              }       
            }      
	        }
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoEfa.blkNoTipiQualitaUsoEfa");
	      }  
	    }
	    else
      {
        htmpl.newBlock("blkElencoEfa.blkNoTipiQualitaUsoEfa");
      }
	      
      
      if(Validator.isNotEmpty(elencoVarietaEfa)
        && (elencoVarietaEfa.size() > 0))
      {
        Vector<TipoVarietaVO> vTipoVarieta = elencoVarietaEfa.get(new Integer(i));
        if(Validator.isNotEmpty(vTipoVarieta)
          && vTipoVarieta.size() > 0)
        {
		      for(int j=0;j<vTipoVarieta.size();j++) 
		      {
		        TipoVarietaVO tipoVarietaVO = vTipoVarieta.get(j);
		        htmpl.newBlock("blkElencoEfa.blkTipiVarietaEfa");
		        
		        htmpl.set("blkElencoEfa.blkTipiVarietaEfa.idVarietaEfa", tipoVarietaVO.getIdVarieta().toString());
		        htmpl.set("blkElencoEfa.blkTipiVarietaEfa.descCompleta", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
		        
		        String descrizione = null;
		        if(tipoVarietaVO.getDescrizione().length() > 20) 
		        {
		          descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
		        }
		        else 
		        {
		          descrizione = tipoVarietaVO.getDescrizione();
		        }
		        htmpl.set("blkElencoEfa.blkTipiVarietaEfa.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
		        
		        
		        if(Validator.isNotEmpty(elencoVarietaSelezionate)
		          && (i < elencoVarietaSelezionate.length)
	            && Validator.isNotEmpty(elencoVarietaSelezionate[i]))
	          {
	            if(new Long(elencoVarietaSelezionate[i]).compareTo(tipoVarietaVO.getIdVarieta()) == 0)
	            { 
	              htmpl.set("blkElencoEfa.blkTipiVarietaEfa.selected", "selected=\"selected\"", null);
	            }
	                   
	          }	        
		      }
		    }
		    else
	      {
	        htmpl.newBlock("blkElencoEfa.blkNoTipiVarietaEfa");
	      }    
      }
      else
      {
        htmpl.newBlock("blkElencoEfa.blkNoTipiVarietaEfa");
      }
            
      
      Integer abbaPonderazione = new Integer(1);
	    if(Validator.isNotEmpty(arrIdTipoEfa)
	       && (i < arrIdTipoEfa.length)
	       && Validator.isNotEmpty(arrIdTipoEfa[i]))
	    {
	      htmpl.set("blkElencoEfa.abbPonderazioneVarieta", elencoAbbPonderazioneVarieta[i]);
	      abbaPonderazione = new Integer(elencoAbbPonderazioneVarieta[i]);
	    }                   
      
      
      if(Validator.isNotEmpty(elencoValoreOriginale)
        && (i < elencoValoreOriginale.length)
        && Validator.isNotEmpty(elencoValoreOriginale[i]))
      {
        htmpl.set("blkElencoEfa.valoreOriginale", elencoValoreOriginale[i]);
        
        if(Validator.isNotEmpty(tipoEfaVOSel))
        {
          BigDecimal valoreOriginale = null;
          BigDecimal valoreDopoConversione = null;
          BigDecimal valoreDopoPonderazione = null;
          try
          {
            valoreOriginale = new BigDecimal(elencoValoreOriginale[i].replace(",", "."));
          }
          catch(Exception ex){}
          
          if(Validator.isNotEmpty(valoreOriginale))
          {
            valoreDopoConversione = valoreOriginale.multiply(tipoEfaVOSel.getFattoreDiConversione());
            valoreDopoConversione = valoreDopoConversione.divide(new BigDecimal(10000), 4,BigDecimal.ROUND_HALF_UP);
            valoreDopoPonderazione = valoreDopoConversione.multiply(tipoEfaVOSel.getFattoreDiPonderazione());
            valoreDopoPonderazione = valoreDopoPonderazione.multiply(new BigDecimal(abbaPonderazione.intValue()));
          }
          
          
          if(Validator.isNotEmpty(valoreDopoConversione))
            htmpl.set("blkElencoEfa.valoreDopoConversione", Formatter.formatDouble4(valoreDopoConversione));
            
          if(Validator.isNotEmpty(valoreDopoPonderazione))
            htmpl.set("blkElencoEfa.valoreDopoPonderazione", Formatter.formatDouble4(valoreDopoPonderazione));
        } 
        
               
      }
      
      
      
      
      countUtilizzi++;
    }  //for numero utilizzi
	  
	  
	  
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>

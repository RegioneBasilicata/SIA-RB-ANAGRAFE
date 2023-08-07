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
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/tabellaDatiPopCondUso.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String tipoModifica = request.getParameter("tipoModifica");
  String numRiga = request.getParameter("numRiga");
  String[] elencoUtilizziSelezionati = request.getParameterValues("idTipoUtilizzo");
  String[] elencoUtilizziSecondariSelezionati = request.getParameterValues("idTipoUtilizzoSecondario");
  String[] elencoDestSelezionati = request.getParameterValues("idTipoDestinazione");
  String[] elencoDestSecondariSelezionati = request.getParameterValues("idTipoDestinazioneSecondario");
  String[] elencoDettUsoSelezionati = request.getParameterValues("idTipoDettaglioUso");
  String[] elencoDettUsoSecondariSelezionati = request.getParameterValues("idTipoDettaglioUsoSecondario");
  String[] elencoQualitaUsoSelezionati = request.getParameterValues("idTipoQualitaUso");
  String[] elencoQualitaUsoSecondariSelezionati = request.getParameterValues("idTipoQualitaUsoSecondario");
  String[] elencoVarietaSelezionate = request.getParameterValues("idVarieta");
  String[] elencoVarietaSecondarieSelezionate = request.getParameterValues("idVarietaSecondaria");
  StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO"); 
  String provenienza = request.getParameter("provenienzaModRig");
  
  try
  {
  
    // Recupero l'elenco delle piante consociate attive censite sul DB
	  Vector<TipoPiantaConsociataVO> elencoPianteConsociate = anagFacadeClient.getListPianteConsociate(true);
	  request.setAttribute("elencoPianteConsociate", elencoPianteConsociate);
	  
	  
	  // Recupero tipo periodo semina
	  //Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = gaaFacadeClient.getListTipoPeriodoSemina();
	  //request.setAttribute("vTipoPeriodoSemina", vTipoPeriodoSemina);
	  
	  Vector<TipoSeminaVO> vTipoSemina = gaaFacadeClient.getElencoTipoSemina();
    request.setAttribute("vTipoSemina", vTipoSemina);
    
    Vector<TipoFaseAllevamentoVO> vTipoFaseAllev = gaaFacadeClient.getElencoFaseAllevamento();
    request.setAttribute("vTipoFaseAllev", vTipoFaseAllev);
	    
	  
	  Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloPrimario");
	  if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
	  {
      elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
    }
    request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
  
  
  
    // Ricerco la destinazione primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazione = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
        {
          Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(Long.decode(elencoUtilizziSelezionati[i]));
          elencoDestinazione.put(new Integer(i), vDestinazione);
        }
      }
    }
	  request.setAttribute("elencoDestinazione", elencoDestinazione);
	  
	  // Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUso = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
        {
          Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
            Long.decode(elencoDestSelezionati[i]));
          elencoDettUso.put(new Integer(i), vDettUso);
        }
      }
    }
	  request.setAttribute("elencoDettUso", elencoDettUso);
	  
	  // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
        {
          Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
            Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]));
          elencoQualitaUso.put(new Integer(i), vQualitaUso);
        }
      }
    }
	  request.setAttribute("elencoQualitaUso", elencoQualitaUso);
	
	  // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = new Hashtable<Integer,Vector<TipoVarietaVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
        {
          Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
            Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), Long.decode(elencoQualitaUsoSelezionati[i]));
          elencoVarieta.put(new Integer(i), vVarieta);
        }
      }
    }
	  request.setAttribute("elencoVarieta", elencoVarieta);
	  
	  
	  // Ricerco il periodo semina in relazione alla mtarice
	  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSemina = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
	  if(elencoUtilizziSelezionati != null) 
    {
      for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
        {
          Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(Long.decode(elencoUtilizziSelezionati[i]),
            Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), Long.decode(elencoQualitaUsoSelezionati[i]),
            Long.decode(elencoVarietaSelezionate[i]));
          elencoPerSemina.put(new Integer(i), vPerSemina);
        }
      }
    }
    request.setAttribute("elencoPerSemina", elencoPerSemina);
    
    // Ricerco il pratica Mantenim in relazione alla mtarice
	  Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>> elencoPerMantenim = new Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>>();
	  Hashtable<Integer,String> elencoFaseAllev = new Hashtable<Integer,String>();
	  if(elencoUtilizziSelezionati != null) 
	  {
	    for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
	    {
	      if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
	      {
	        CatalogoMatriceVO catalogoMatricePraticVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(Long.decode(elencoUtilizziSelezionati[i]),
	          Long.decode(elencoVarietaSelezionate[i]), Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), 
	          Long.decode(elencoQualitaUsoSelezionati[i]));
	        Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "N");
	        Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
	        elencoPerMantenim.put(new Integer(i), vPraticaMantenim);
	        
	        String faseAllevamento = "N";
          if("S".equalsIgnoreCase(catalogoMatricePraticVO.getPermanente()))
            faseAllevamento = "S";
          elencoFaseAllev.put(new Integer(i), faseAllevamento);
	      }
	    }
	  }
	  request.setAttribute("elencoPerMantenim", elencoPerMantenim);
	  request.setAttribute("elencoFaseAllev", elencoFaseAllev);
  
    
    
    Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloSecondario");
    if(Validator.isEmpty(elencoTipiUsoSuoloSecondario) || elencoTipiUsoSuoloSecondario.size() == 0) 
    {
      elencoTipiUsoSuoloSecondario = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
      request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
    }
  
    // Ricerco la destinazione primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioneSecondario = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
        {
          Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]));
          elencoDestinazioneSecondario.put(new Integer(i), vDestinazione);
        }
      }
    }
	  request.setAttribute("elencoDestinazioneSecondario", elencoDestinazioneSecondario);
	 
	  // Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUsoSecondario = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
        {
          Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
            Long.decode(elencoDestSecondariSelezionati[i]));
          elencoDettUsoSecondario.put(new Integer(i), vDettUso);
        }
      }
    }
	  request.setAttribute("elencoDettUsoSecondario", elencoDettUsoSecondario);	  
	  
	  // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUsoSecondario = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
        {
          Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
            Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]));
          elencoQualitaUsoSecondario.put(new Integer(i), vQualitaUso);
        }
      }
    }
	  request.setAttribute("elencoQualitaUsoSecondario", elencoQualitaUsoSecondario);
	  
	  // Ricerco la varietà secondaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarietaSecondaria = new Hashtable<Integer,Vector<TipoVarietaVO>>();
	  if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati)) 
    {
      for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
        {
          Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
            Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]), Long.decode(elencoQualitaUsoSecondariSelezionati[i]));
          elencoVarietaSecondaria.put(new Integer(i), vVarieta);
        }
      }
    }
	  request.setAttribute("elencoVarietaSecondaria", elencoVarietaSecondaria);
	  
	  // Ricerco il periodo semina in relazione alla mtarice
	  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSeminaSecondario = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
	  if(elencoUtilizziSecondariSelezionati != null) 
    {
      for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
      {
        if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
        {
          Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(Long.decode(elencoUtilizziSecondariSelezionati[i]),
            Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]), Long.decode(elencoQualitaUsoSecondariSelezionati[i]),
            Long.decode(elencoVarietaSecondarieSelezionate[i]));
          elencoPerSeminaSecondario.put(new Integer(i), vPerSemina);
        }
      }
    }
	  request.setAttribute("elencoPerSeminaSecondario", elencoPerSeminaSecondario);
  
    // Ricerco i tipi impianti attivi
    TipoImpiantoVO[] elencoTipoImpianto = anagFacadeClient.getListTipoImpianto(true, SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
    request.setAttribute("elencoTipoImpianto", elencoTipoImpianto);
    
    
    // Labels relative ai dati delle piante consociate
    if(elencoPianteConsociate != null && elencoPianteConsociate.size() > 0) 
    {
      htmpl.set("colspan", String.valueOf(elencoPianteConsociate.size()));
      for(int i = 0; i < elencoPianteConsociate.size(); i++) {
        TipoPiantaConsociataVO tipoPiantaConsociataVO = (TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(i);
        htmpl.newBlock("blkEtichettaPianteConsociate");
        htmpl.set("blkEtichettaPianteConsociate.descPianteConsociate", tipoPiantaConsociataVO.getDescrizione());
      }
    }
    
    
    //Calcolo numero utilizzi
    int numeroUtilizzi = 0;
    if(Validator.isNotEmpty(elencoUtilizziSelezionati))
    {
      numeroUtilizzi = elencoUtilizziSelezionati.length;
    }
    
    if(Validator.isNotEmpty(tipoModifica)
      && "inserisci".equalsIgnoreCase(tipoModifica))
    {
      numeroUtilizzi++;
    }
    
    
    htmpl.set("provenienza", provenienza);
    int countUtilizzi = 0;
    for(int i = 0; i < numeroUtilizzi; i++) 
    {
    
      if(Validator.isNotEmpty(tipoModifica)
	      && "elimina".equalsIgnoreCase(tipoModifica))
	    {
	      if(i == new Integer(numRiga).intValue())
	      {	         
	        continue;
	      }
	    }
    
      htmpl.newBlock("blkElencoUtilizzi");
      
      htmpl.set("blkElencoUtilizzi.provenienza", provenienza);
      htmpl.set("blkElencoUtilizzi.numeroUtilizzi", String.valueOf(countUtilizzi));
      
      for(int a = 0; a < elencoTipiUsoSuolo.size(); a++) 
      {
        TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(a);
        htmpl.newBlock("blkElencoUtilizzi.blkTipiUsoSuolo");
        htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
        htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
        String descrizione = "";
        if(tipoUtilizzoVO.getDescrizione().length() > 20) 
        {
          descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
        }
        else 
        {
          descrizione = tipoUtilizzoVO.getDescrizione();
        }
        htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
        if(request.getParameterValues("idTipoUtilizzo") != null && i < request.getParameterValues("idTipoUtilizzo").length) 
        {
          if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i]) 
            && Long.decode(request.getParameterValues("idTipoUtilizzo")[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
          {
            htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
          }
        }        
      }
      
      if(elencoDestinazione != null && elencoDestinazione.size() > 0)
	    {
	      if((request.getParameterValues("idTipoUtilizzo") != null)
           && (i < request.getParameterValues("idTipoUtilizzo").length)
           && (request.getParameterValues("idTipoUtilizzo")[i] != null))
        {
          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazione.get(new Integer(i));
          if(vDestinazione != null)
          {
            for(int d = 0; d < vDestinazione.size(); d++) 
            {
              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDestinazione");
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
              String descrizione = null;
              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDestinazione") != null && i < request.getParameterValues("idTipoDestinazione").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i]) 
                  && request.getParameterValues("idTipoDestinazione")[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazione");
		      }
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazione");
	      }
	    }
	    else
	    {
	      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazione");
	    }
	      
	      
	    
	    if(elencoDettUso != null && elencoDettUso.size() > 0)
	    {
	      if((request.getParameterValues("idTipoDestinazione") != null)
          && (i < request.getParameterValues("idTipoDestinazione").length)
          && (request.getParameterValues("idTipoDestinazione")[i] != null))
        {
          Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUso.get(new Integer(i));
          if(vDettaglioUso != null)
          {
            for(int d = 0; d < vDettaglioUso.size(); d++) 
            {
              TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDettaglioUso");
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
              String descrizione = null;
              if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDettaglioUso") != null && i < request.getParameterValues("idTipoDettaglioUso").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i]) 
                  && request.getParameterValues("idTipoDettaglioUso")[i].equalsIgnoreCase(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUso");
		      }
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUso");
	      }
	    }
	    else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUso");
      }
	      
	    if(elencoQualitaUso != null && elencoQualitaUso.size() > 0)
	    {
	      if((request.getParameterValues("idTipoDettaglioUso") != null)
          && (i < request.getParameterValues("idTipoDettaglioUso").length)
          && (request.getParameterValues("idTipoDettaglioUso")[i] != null))
        {
          Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUso.get(new Integer(i));
          if(vQualitaUso != null)
          {
            for(int d = 0; d < vQualitaUso.size(); d++) 
            {
              TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiQualitaUso");
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
              String descrizione = null;
              if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
              }
              else 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoQualitaUso") != null && i < request.getParameterValues("idTipoQualitaUso").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i]) 
                  && request.getParameterValues("idTipoQualitaUso")[i].equalsIgnoreCase(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUso");
		      }
        }
        else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUso");
	      }
	    }
	    else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUso");
      }
	      
	    
	    
	    
	    // Carico la combo della varietà solo se l'utente ha selezionato il tipo
	    // uso suolo corrispondente
	    if(elencoVarieta != null && elencoVarieta.size() > 0) 
	    {  
	      if((request.getParameterValues("idTipoQualitaUso") != null)
          && (i < request.getParameterValues("idTipoQualitaUso").length)
          && (request.getParameterValues("idTipoQualitaUso")[i] != null))
        {
          Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarieta.get(new Integer(i));
          if(tipoVarieta != null)
          {
            for(int l = 0; l < tipoVarieta.size(); l++) 
            {
              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(l);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiVarieta");
              htmpl.set("blkElencoUtilizzi.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
              String descrizione = null;
              if(tipoVarietaVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoVarietaVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
             
              if(request.getParameterValues("idVarieta") != null && i < request.getParameterValues("idVarieta").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idVarieta")[i]) 
                  && request.getParameterValues("idVarieta")[i].equalsIgnoreCase(tipoVarietaVO.getIdVarieta().toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiVarieta.selected", "selected=\"selected\"", null);
                }
              }               
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarieta");
		      }
        }
        else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarieta");
	      }
      }
      else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarieta");
      }
	      
	    
      if(request.getParameterValues("supUtilizzata") != null 
        && i <  request.getParameterValues("supUtilizzata").length 
        && Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[i])) 
      {
        htmpl.set("blkElencoUtilizzi.supUtilizzata", request.getParameterValues("supUtilizzata")[i]);
      } 
      
      
      // Carico la combo del perido semina solo se l'utente ha selezionato il tipo
	    // uso suolo corrispondente
	    if(elencoPerSemina != null && elencoPerSemina.size() > 0) 
	    {  
	      if((request.getParameterValues("idTipoUtilizzo") != null)
	        && i <  request.getParameterValues("idTipoUtilizzo").length
	        && (request.getParameterValues("idTipoUtilizzo")[i] != null))
	      {
	        Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSemina.get(new Integer(i));
	        if(vTipoPeriodoSemina != null)
	        {
	          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
	          {
	            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
	            htmpl.newBlock("blkElencoUtilizzi.blkTipoPeriodoSemina");
	            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
	            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.descrizione", tipoPeriodoSeminaVO.getDescrizione());
	           
	            if(request.getParameterValues("idTipoPeriodoSemina") != null 
	              && i < request.getParameterValues("idTipoPeriodoSemina").length) 
	            {
	              if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina")[i]) 
	                && request.getParameterValues("idTipoPeriodoSemina")[i].equalsIgnoreCase(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()).toString())) 
	              {
	                htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.selected", "selected=\"selected\"", null);
	              }
	            }               
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSemina");
		      } 
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSemina");
	      } 
	    }
	    else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSemina");
      } 
	    
	    if(elencoPerMantenim != null && elencoPerMantenim.size() > 0) 
	    {  
	      if((request.getParameterValues("idTipoUtilizzo") != null)
	        && i <  request.getParameterValues("idTipoUtilizzo").length
          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
        {
          Vector<TipoPraticaMantenimentoVO> vTipoPraticMantenim = (Vector<TipoPraticaMantenimentoVO>)elencoPerMantenim.get(new Integer(i));
          if(vTipoPraticMantenim != null)
          {
            for(int l = 0; l < vTipoPraticMantenim.size(); l++) 
            {
              TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vTipoPraticMantenim.get(l);
              htmpl.newBlock("blkElencoUtilizzi.blkTipoPraticaMantenimento");
              htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
              htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.descrizione", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
             
              if(request.getParameterValues("idPraticaMantenimento") != null && i < request.getParameterValues("idPraticaMantenimento").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idPraticaMantenimento")[i]) 
                  && request.getParameterValues("idPraticaMantenimento")[i].equalsIgnoreCase(new Long(tipoPraticaMantenimentoVO.getIdPraticaMantenimento()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.selected", "selected=\"selected\"", null);
                }
              }               
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
		      } 
        }
        else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
	      } 
      }
      else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
      } 
	      
      
      
      
      //abilita semina
	    boolean abilitaSemina = false;
	    if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo"))
	      && i <  request.getParameterValues("idTipoUtilizzo").length
	      && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i]))
	    {
	      abilitaSemina = true;
	    }
	    
	    
	    if(abilitaSemina)
	    {
      
	      for(int e=0;e<vTipoSemina.size(); e++) 
		    {
		      TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
		      htmpl.newBlock("blkElencoUtilizzi.blkTipoSemina");
		      htmpl.set("blkElencoUtilizzi.blkTipoSemina.idTipoSemina", ""+tipoSeminaVO.getIdTipoSemina());
		      htmpl.set("blkElencoUtilizzi.blkTipoSemina.descrizione", tipoSeminaVO.getDescrizioneSemina());
		      if(request.getParameterValues("idTipoSemina") != null 
	          && i < request.getParameterValues("idTipoSemina").length) 
	        {
	          if(Validator.isNotEmpty(request.getParameterValues("idTipoSemina")[i]) 
	            && Long.decode(request.getParameterValues("idTipoSemina")[i]).compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0) 
	          {
	            htmpl.set("blkElencoUtilizzi.blkTipoSemina.selected", "selected=\"selected\"", null);
	          }
	        }
		    }
		  }
		  else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoSemina");
      } 
		  
		  if(abilitaSemina)
      {
        if(request.getParameterValues("dataInizioDestinazione") != null 
          && (i < request.getParameterValues("dataInizioDestinazione").length) 
          && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazione")[i]))
        { 
          htmpl.set("blkElencoUtilizzi.dataInizioDestinazione", request.getParameterValues("dataInizioDestinazione")[i]);
        }
        
        if(request.getParameterValues("dataFineDestinazione") != null 
          && (i < request.getParameterValues("dataFineDestinazione").length) 
          && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazione")[i]))
        { 
          htmpl.set("blkElencoUtilizzi.dataFineDestinazione", request.getParameterValues("dataFineDestinazione")[i]);
        }
      }
      else
      {
        htmpl.set("blkElencoUtilizzi.readOnlyDataInizioDestinazione", "readOnly=\"true\"", null);
        htmpl.set("blkElencoUtilizzi.readOnlyDataFineDestinazione", "readOnly=\"true\"", null);
      } 
      
      
      
      if(elencoFaseAllev != null && elencoFaseAllev.size() > 0) 
	    {  
	      if((request.getParameterValues("idTipoUtilizzo") != null)
	        && i <  request.getParameterValues("idTipoUtilizzo").length
          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
        {
          if("S".equalsIgnoreCase(elencoFaseAllev.get(new Integer(i))))
          {
            if(vTipoFaseAllev != null)
            {
              for(int l = 0; l < vTipoFaseAllev.size(); l++) 
              {
                TipoFaseAllevamentoVO tipoFaseAllevamentoVO = vTipoFaseAllev.get(l);
                htmpl.newBlock("blkElencoUtilizzi.blkTipoFaseAllevamento");
                htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.idFaseAllevamento", ""+tipoFaseAllevamentoVO.getIdFaseAllevamento());
                htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.descrizione", tipoFaseAllevamentoVO.getDescrizioneFaseAllevamento());
               
                if(request.getParameterValues("idFaseAllevamento") != null 
                  && i < request.getParameterValues("idFaseAllevamento").length) 
                {
                  if(Validator.isNotEmpty(request.getParameterValues("idFaseAllevamento")[i]) 
                    && request.getParameterValues("idFaseAllevamento")[i].equalsIgnoreCase(new Long(tipoFaseAllevamentoVO.getIdFaseAllevamento()).toString())) 
                  {
                    htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.selected", "selected=\"selected\"", null);
                  }
                }               
              }
            }
          }
          else
          {
            htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
            htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
            htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
          }
        }
        else
        {
          htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
          htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
          htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
        }
	    }
	    else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
        htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
        htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
      }
	      
      
      
      
      if(elencoTipiUsoSuoloSecondario != null && elencoTipiUsoSuoloSecondario.size() > 0) 
      {
        for(int c = 0; c < elencoTipiUsoSuoloSecondario.size(); c++) 
        {
          TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuoloSecondario.elementAt(c);
          htmpl.newBlock("blkElencoUtilizzi.blkTipiUsoSuoloSecondario");
          htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.idTipoUtilizzoSecondario", tipoUtilizzoVO.getIdUtilizzo().toString());
          htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
          String descrizione = "";
          if(tipoUtilizzoVO.getDescrizione().length() > 20) 
          {
            descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
          }
          else 
          {
            descrizione = tipoUtilizzoVO.getDescrizione();
          }
          htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
          if(request.getParameterValues("idTipoUtilizzoSecondario") != null 
            && i < request.getParameterValues("idTipoUtilizzoSecondario").length) 
          {
            if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]) 
              && Long.decode(request.getParameterValues("idTipoUtilizzoSecondario")[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
            {
              htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"", null);
            }
          }
        }
      }
      
      if(elencoDestinazioneSecondario != null && elencoDestinazioneSecondario.size() > 0)
	    {
	      if((request.getParameterValues("idTipoUtilizzoSecondario") != null)
          && (i < request.getParameterValues("idTipoUtilizzoSecondario").length)
          && (request.getParameterValues("idTipoUtilizzoSecondario")[i] != null))
        {
          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneSecondario.get(new Integer(i));
          if(vDestinazione != null)
          {
            for(int d = 0; d < vDestinazione.size(); d++) 
            {
              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDestinazioneSecondario");
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
              String descrizione = null;
              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDestinazioneSecondario") != null && i < request.getParameterValues("idTipoDestinazioneSecondario").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneSecondario")[i]) 
                  && request.getParameterValues("idTipoDestinazioneSecondario")[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
		      } 
        }
        else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
	      } 
      }
      else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
      } 
	      
	    
	    if(elencoDettUsoSecondario != null && elencoDettUsoSecondario.size() > 0)
	    {
	      if((request.getParameterValues("idTipoDestinazioneSecondario") != null)
          && (i < request.getParameterValues("idTipoDestinazioneSecondario").length)
          && (request.getParameterValues("idTipoDestinazioneSecondario")[i] != null))
        {
          Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUsoSecondario.get(new Integer(i));
          if(vDettaglioUso != null)
          {
            for(int d = 0; d < vDettaglioUso.size(); d++) 
            {
              TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario");
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
              String descrizione = null;
              if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDettaglioUsoSecondario") != null && i < request.getParameterValues("idTipoDettaglioUsoSecondario").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]) 
                  && request.getParameterValues("idTipoDettaglioUsoSecondario")[i].equalsIgnoreCase(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
		      } 
        }
        else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
	      } 
      }
      else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
      } 
	      
	      
	    if(elencoQualitaUsoSecondario != null && elencoQualitaUsoSecondario.size() > 0)
	    {
	      if((request.getParameterValues("idTipoDettaglioUsoSecondario") != null)
          && (i < request.getParameterValues("idTipoDettaglioUsoSecondario").length)
          && (request.getParameterValues("idTipoDettaglioUsoSecondario")[i] != null))
        {
          Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUsoSecondario.get(new Integer(i));
          if(vQualitaUso != null)
          {
            for(int d = 0; d < vQualitaUso.size(); d++) 
            {
              TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiQualitaUsoSecondario");
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
              String descrizione = null;
              if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
              }
              else 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoQualitaUsoSecondario") != null && i < request.getParameterValues("idTipoQualitaUsoSecondario").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario")[i]) 
                  && request.getParameterValues("idTipoQualitaUsoSecondario")[i].equalsIgnoreCase(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
		      } 
        }
        else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
	      } 
      }
      else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
      } 
	      
	    
	    
	    if(elencoVarietaSecondaria != null && elencoVarietaSecondaria.size() > 0 ) 
	    {
	      if((request.getParameterValues("idTipoQualitaUsoSecondario") != null)
          && (i < request.getParameterValues("idTipoQualitaUsoSecondario").length)
          && Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario")[i]))
        {
          Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarietaSecondaria.get(new Integer(i));
          if(tipoVarieta != null) 
          {
            for(int h = 0; h < tipoVarieta.size(); h++) 
            {
              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(h);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiVarietaSecondaria");
              htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
              String descrizione = null;
              if(tipoVarietaVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoVarietaVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
              if(request.getParameterValues("idVarietaSecondaria") != null 
                && i < request.getParameterValues("idVarietaSecondaria").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idVarietaSecondaria")[i]) 
                  && request.getParameterValues("idVarietaSecondaria")[i].equalsIgnoreCase(tipoVarietaVO.getIdVarieta().toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
		      } 
        }
        else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
	      } 
      }
      else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
      } 
      
      // Carico la combo del perido semina solo se l'utente ha selezionato il tipo
	    // uso suolo corrispondente
	    if(elencoPerSeminaSecondario != null && elencoPerSeminaSecondario.size() > 0) 
	    {  
	      if((request.getParameterValues("idTipoUtilizzoSecondario") != null)
	        && i <  request.getParameterValues("idTipoUtilizzoSecondario").length
	        && (request.getParameterValues("idTipoUtilizzoSecondario")[i] != null))
	      {
	        Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSeminaSecondario.get(new Integer(i));
	        if(vTipoPeriodoSemina != null)
	        {
	          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
	          {
	            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
	            htmpl.newBlock("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario");
	            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.idTipoPeriodoSeminaSecondario", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
	            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.descrizione", tipoPeriodoSeminaVO.getDescrizione());
	           
	            if(request.getParameterValues("idTipoPeriodoSeminaSecondario") != null && i < request.getParameterValues("idTipoPeriodoSeminaSecondario").length) 
	            {
	              if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]) 
	                && request.getParameterValues("idTipoPeriodoSeminaSecondario")[i].equalsIgnoreCase(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()).toString())) 
	              {
	                htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.selected", "selected=\"selected\"", null);
	              }
	            }               
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
		      } 
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
	      } 
	    }
	    else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
      } 
      
		        
      
      
      
      //abilita semina
	    boolean abilitaSeminaSec = false;
	    if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario"))
	      && i <  request.getParameterValues("idTipoUtilizzoSecondario").length
	      && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]))
	    {
	      abilitaSeminaSec = true;
	    }    
    
      if(abilitaSeminaSec)
      {      
	      for(int e=0;e<vTipoSemina.size(); e++) 
		    {
		      TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
		      htmpl.newBlock("blkElencoUtilizzi.blkTipoSeminaSecondario");
		      htmpl.set("blkElencoUtilizzi.blkTipoSeminaSecondario.idTipoSeminaSecondario", ""+tipoSeminaVO.getIdTipoSemina());
		      htmpl.set("blkElencoUtilizzi.blkTipoSeminaSecondario.descrizione", tipoSeminaVO.getDescrizioneSemina());
		      if(request.getParameterValues("idTipoSeminaSecondario") != null 
	          && i < request.getParameterValues("idTipoSeminaSecondario").length) 
	        {
	          if(Validator.isNotEmpty(request.getParameterValues("idTipoSeminaSecondario")[i]) 
	            && Long.decode(request.getParameterValues("idTipoSeminaSecondario")[i]).compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0) 
	          {
	            htmpl.set("blkElencoUtilizzi.blkTipoSeminaSecondario.selected", "selected=\"selected\"", null);
	          }
	        }    
	      }
	    }
	    else
      {
        htmpl.newBlock("blkElencoUtilizzi.blkNoTipoSeminaSecondario");
      }  
	    
	    if(abilitaSeminaSec)
	    {
	      if(request.getParameterValues("dataInizioDestinazioneSec") != null 
	        && (i < request.getParameterValues("dataInizioDestinazioneSec").length) 
	        && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazioneSec")[i]))
	      { 
	        htmpl.set("blkElencoUtilizzi.dataInizioDestinazioneSec", request.getParameterValues("dataInizioDestinazioneSec")[i]);
	      }
	      
	      if(request.getParameterValues("dataFineDestinazioneSec") != null 
	        && (i < request.getParameterValues("dataFineDestinazioneSec").length) 
	        && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazioneSec")[i]))
	      { 
	        htmpl.set("blkElencoUtilizzi.dataFineDestinazioneSec", request.getParameterValues("dataFineDestinazioneSec")[i]);
	      }
	    }
	    else
	    {
	      htmpl.set("blkElencoUtilizzi.readOnlyDataInizioDestinazioneSec", "readOnly=\"true\"", null);
	      htmpl.set("blkElencoUtilizzi.readOnlyDataFineDestinazioneSec", "readOnly=\"true\"", null);
	    } 
	    
	    if(request.getParameterValues("supUtilizzataSecondaria") != null 
        && i <  request.getParameterValues("supUtilizzataSecondaria").length 
        && Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[i])) 
      {
        htmpl.set("blkElencoUtilizzi.supUtilizzataSecondaria", request.getParameterValues("supUtilizzataSecondaria")[i]);
      }               
      
    
      
      
      if(request.getParameterValues("annoImpianto") != null 
        && i < request.getParameterValues("annoImpianto").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[i])) {
          htmpl.set("blkElencoUtilizzi.annoImpianto", request.getParameterValues("annoImpianto")[i]);
        }
      }
      
      for(int e = 0; e < elencoTipoImpianto.length; e++) 
      {
        TipoImpiantoVO tipoImpiantoVO = (TipoImpiantoVO)elencoTipoImpianto[e];
        htmpl.newBlock("blkElencoUtilizzi.blkTipiImpianto");
        htmpl.set("blkElencoUtilizzi.blkTipiImpianto.idImpianto", tipoImpiantoVO.getIdImpianto().toString());
        htmpl.set("blkElencoUtilizzi.blkTipiImpianto.descrizione", tipoImpiantoVO.getDescrizione());
        if(request.getParameterValues("idImpianto") != null 
          && i < request.getParameterValues("idImpianto").length) 
        {
          if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i]) 
            && Long.decode(request.getParameterValues("idImpianto")[i]).compareTo(tipoImpiantoVO.getIdImpianto()) == 0) 
          {
            htmpl.set("blkElencoUtilizzi.blkTipiImpianto.selected", "selected=\"selected\"", null);
          }
        }
      }
      
      if(request.getParameterValues("sestoSuFile") != null 
        && i < request.getParameterValues("sestoSuFile").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.sestoSuFile", request.getParameterValues("sestoSuFile")[i]);
        }
      }
      
      if(request.getParameterValues("sestoTraFile") != null 
        && i < request.getParameterValues("sestoTraFile").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.sestoTraFile", request.getParameterValues("sestoTraFile")[i]);
        }
      }
      
      if(request.getParameterValues("numeroPianteCeppi") != null 
        && i < request.getParameterValues("numeroPianteCeppi").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.numeroPianteCeppi", request.getParameterValues("numeroPianteCeppi")[i]);
        }
      }
      
      // Sezione relativa alle piante e agli utilizzi consociati
      if(elencoPianteConsociate != null && elencoPianteConsociate.size() > 0)
      {
        boolean isNewConsociato = false;
        for(int a = 0; a < elencoPianteConsociate.size(); a++) 
        {
          htmpl.newBlock("blkElencoUtilizzi.blkElencoPianteConsociate");
          if(i == 0) 
          {
            if(request.getParameterValues("numeroPianteConsociate") != null 
              && Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[a])) 
            {
              htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", request.getParameterValues("numeroPianteConsociate")[a]);
            }
            else 
            {
              htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
            }
          }
          else 
          {
            int valoreSelected = (i * elencoPianteConsociate.size()) + a;
            if(valoreSelected < request.getParameterValues("numeroPianteConsociate").length) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[valoreSelected])) 
              {
                htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", request.getParameterValues("numeroPianteConsociate")[valoreSelected]);
              }
              else 
              {
                htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
              }
            }
            
          }
          
          
        }
        
      }
      
      countUtilizzi++;
    }
    
  
  
	  
	  
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>

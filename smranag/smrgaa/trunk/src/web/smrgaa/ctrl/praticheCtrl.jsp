<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.comune.*"%>
<%@ page import="it.csi.solmr.dto.CodeDescription"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

	String iridePageName = "praticheCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String praticheUrl = "/view/praticheView.jsp";
	String excelUrl = "/servlet/ExcelElencoPraticheServlet";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	Vector<ProcedimentoAziendaVO> elencoPratiche = null;
  Hashtable<String,AmmCompetenzaVO> elencoAmmCompetenza = null;
  String operazione = request.getParameter("operazione");
  String procedimento = request.getParameter("procedimento");
  String idAnno = request.getParameter("idAnno");
  String idAzienda = request.getParameter("cuaa");
  Long idAziendaPratiche = (Long)session.getAttribute("idAziendaPratiche");

  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
	// in modo che venga sempre effettuato
  
	try 
	{
  	anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
	}
	catch(SolmrException se) 
	{
  	request.setAttribute("statoAzienda", se);
	}
	
	
	if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("excel"))
  {
    
    %>
        <jsp:forward page="<%=excelUrl%>" />
    <%
    return;
  }
	
	
	
  
  // L'utente ha cliccato sul pulsante ricerca
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("ricerca")) 
  {

  	// Richiamo il metodo per recuperare le pratiche relative
  	// all'azienda agricola selezionata dall'utente coi parametri anno e idProcedimento
  	try 
    {
      Long idAziendaSelezionata = null;
      if(Validator.isNotEmpty(idAzienda))
      {
        idAziendaSelezionata = new Long(idAzienda);
      }
      
      Long idProcedimento = null;
      if(Validator.isNotEmpty(procedimento))
      {
        idProcedimento = new Long(procedimento);
      }
      
      Long lgIdAnno = null;
      if(Validator.isNotEmpty(idAnno))
      {
        lgIdAnno = new Long(idAnno);
      }
      
      //Aggiunto Luca
      if(idAziendaPratiche == null)
      {
        idAziendaPratiche = anagAziendaVO.getIdAzienda();
      }
      
    	elencoPratiche = anagFacadeClient.getElencoProcedimentiByIdAzienda(
        idAziendaPratiche, lgIdAnno, idProcedimento, idAziendaSelezionata);
      
      // Richiamo il servizio di comune per estrarre l'elenco delle amministrazioni
      // di competenza
      AmmCompetenzaVO[] elenco = anagFacadeClient.serviceGetListAmmCompetenza();
      if(elenco != null && elenco.length > 0) 
      {
        elencoAmmCompetenza = new Hashtable<String,AmmCompetenzaVO>();
        for(int i = 0; i < elenco.length; i++) 
        {
          AmmCompetenzaVO ammCompetenzaVO = (AmmCompetenzaVO)elenco[i];
          elencoAmmCompetenza.put(ammCompetenzaVO.getIdAmmCompetenza(), ammCompetenzaVO);
        }
      }
  	}
  	catch(SolmrException se) 
  	{
    	request.setAttribute("messaggioErrore", se.getMessage());
  	}

  	request.setAttribute("elencoPratiche", elencoPratiche);
    request.setAttribute("elencoAmmCompetenza", elencoAmmCompetenza);
    
  }
  else //inizializzazione
  {
    // Pulisco la sessione dai filtri di altre sezioni
    String noRemove = "idAziendaPratiche";
    WebUtils.removeUselessFilter(session, noRemove);
    //PAOLA
    TreeMap<Integer,String> descCUAA = null;
    
    if(descCUAA == null)
    {
      descCUAA = new TreeMap<Integer,String>();
    }
        
    CodeDescription[] cd = anagFacadeClient.getListCuaaAttiviProvDestByIdAzienda(anagAziendaVO.getIdAzienda());
    if(Validator.isNotEmpty(cd))
    {
      for(int i=0; i<cd.length; i++){
        descCUAA.put(cd[i].getCode(), cd[i].getDescription());        
      }
      session.setAttribute("elencoCUAA", descCUAA);
    }
    //PAOLA
    // Richiamo il metodo per aggiornare e recuperare le pratiche relative
    // all'azienda agricola selezionata dall'utente
    try 
    {  
      
      idAziendaPratiche = anagAziendaVO.getIdAzienda();
      if(idAzienda!=null)
      {             
        idAziendaPratiche = new Long(idAzienda);
      }
      session.setAttribute("idAziendaPratiche",idAziendaPratiche);
      elencoPratiche = anagFacadeClient.updateAndGetPraticheByAzienda(idAziendaPratiche);
      
      // Richiamo il servizio di comune per estrarre l'elenco delle amministrazioni
      // di competenza
      AmmCompetenzaVO[] elenco = anagFacadeClient.serviceGetListAmmCompetenza();
      if(elenco != null && elenco.length > 0) 
      {
        elencoAmmCompetenza = new Hashtable<String,AmmCompetenzaVO>();
        for(int i = 0; i < elenco.length; i++) 
        {
          AmmCompetenzaVO ammCompetenzaVO = (AmmCompetenzaVO)elenco[i];
          elencoAmmCompetenza.put(ammCompetenzaVO.getIdAmmCompetenza(), ammCompetenzaVO);
        }
      }
      
      
    }
    catch(SolmrException se) 
    {
      request.setAttribute("messaggioErrore", se.getMessage());
    }
    
    TreeMap<String,Long> descProcedimento = null;
    ArrayList<Integer> anni = null;
    if(elencoPratiche != null && elencoPratiche.size() > 0) 
    {
      Iterator<?> iteraPratiche = elencoPratiche.iterator();
      while(iteraPratiche.hasNext()) 
      {
        ProcedimentoAziendaVO procedimentoAziendaVO = (ProcedimentoAziendaVO)iteraPratiche.next();
        if(procedimentoAziendaVO.getDataValiditaStato() !=null)
        {            
          Integer intG = new Integer(procedimentoAziendaVO.getAnnoCampagna().intValue());
          if(anni == null)
          {
            anni = new ArrayList<Integer>();
          }
          //Dinstinct degli anni
          if(!anni.contains(intG))
          {
            anni.add(intG);
          }
          
          
        }
        
        if(descProcedimento == null)
        {
          descProcedimento = new TreeMap<String,Long>();
        }
        
        TipoProcedimentoVO tipoProcedimentoVO = procedimentoAziendaVO.getTipoProcedimentoVO();
        descProcedimento.put(tipoProcedimentoVO.getDescrizioneEstesa(),
          tipoProcedimentoVO.getIdProcedimento());        
        
      }
    }
    if(Validator.isNotEmpty(descProcedimento))
    {
      session.setAttribute("filtriPraticheProcedimenti",descProcedimento);
    }
    
    /*GregorianCalendar gcActual = new GregorianCalendar();
    Integer currYear = new Integer(gcActual.get(Calendar.YEAR));
    if(anni == null)
    {
      anni = new ArrayList<Integer>();
      anni.add(currYear);
    }
    else
    {
      if(!anni.contains(currYear))
      {
        anni.add(currYear);
      } 
    }*/
    
    //Ordino per visualizzare in modo descrescente
    if(anni != null)
    {
      Collections.sort(anni);
      Collections.reverse(anni);        
      session.setAttribute("filtriPraticheAnniCombo",anni);
    }
    
    request.setAttribute("inizializzaAnno","true");
    
    // Richiamo il metodo per recuperare le pratiche relative
    // all'azienda agricola selezionata dall'utente coi parametri anno e idProcedimento
    try 
    {
      
      Long lgIdAnno = null;
      if(anni != null)
      {
        lgIdAnno = new Long(anni.get(0));
      }
      else
      {
        lgIdAnno = new Long(DateUtils.getCurrentYear());      
      }
      
      elencoPratiche = anagFacadeClient.getElencoProcedimentiByIdAzienda(
        idAziendaPratiche, lgIdAnno, null, null);
    }
    catch(SolmrException se) {
      request.setAttribute("messaggioErrore", se.getMessage());
      elencoPratiche = null;
    }

    request.setAttribute("elencoPratiche", elencoPratiche);
    request.setAttribute("elencoAmmCompetenza", elencoAmmCompetenza);
  }
  
  

	// Vado alla pagina di elenco delle pratiche
	%>
  	<jsp:forward page= "<%= praticheUrl %>" />
  	


<%!/**
   * Carica la successione degli anni nel combo partendo dall'anno passato come parametro
   * firstAnno fino all'anno corrente. ***Non piu' usato!!****
   * 
   * @param request
   * @param firstAnno
   */

  private Vector<String> getAnniFromFirst(Date firstAnno)
  {
    int anno = DateUtils.extractYearFromDate(firstAnno);

    Vector<String> anniAl = new Vector<String>();
    Vector<String> anniA2 = new Vector<String>();
    GregorianCalendar gc = new GregorianCalendar(anno, 0, 1);
    GregorianCalendar gcActual = new GregorianCalendar();
    while (gc.get(Calendar.YEAR) <= gcActual.get(Calendar.YEAR))
    {
      anniAl.add(new Integer(gc.get(Calendar.YEAR)).toString());
      gc.add(Calendar.YEAR, 1);
    }

    int k = anniAl.size() - 1;
    for (int i = 0; i < anniAl.size(); i++)
    {
      anniA2.add((String) anniAl.get(k));
      k--;
    }

    return anniA2;
  }%>

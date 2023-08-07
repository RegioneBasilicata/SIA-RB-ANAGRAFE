<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "aziendeCollegateInserisciCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - aziendeColleagateInserisciCtrl.jsp - INIZIO PAGINA");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  //ValidationError error = null;
  String operazione = request.getParameter("operazione");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String aziendeCollegateInserisciUrl = "/view/aziendeCollegateInserisciView.jsp";
  String actionUrl = "../layout/aziendeCollegate.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String aziendeCollegateCtrlUrl = "/ctrl/aziendeCollegateCtrl.jsp";
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //Vector elencoCollegate = null;
  boolean flagPagErrore = false;
  
  
  //tutte le aziende per associare correttamente le date
  String[] elencoDataIngressoAzCollAll = request.getParameterValues("dataIngresso");
  String[] elencoDataUscitaAzCollAll = request.getParameterValues("dataUscita");
  
  if(elencoDataIngressoAzCollAll !=null)
    session.setAttribute("elencoDataIngressoAzCollAll",elencoDataIngressoAzCollAll);    
  if(elencoDataUscitaAzCollAll !=null)
    session.setAttribute("elencoDataUscitaAzCollAll",elencoDataUscitaAzCollAll);
    
    
  //idAzienda dell'Aziende inserite in sessione
  Vector vAziendaIns = (Vector)session.getAttribute("vAziendaIns");
  
  if((vAziendaIns == null) || ((vAziendaIns !=null) && (vAziendaIns.size() == 0)))
  {
    session.removeAttribute("elencoDataIngressoAzCollAll");
    session.removeAttribute("elencoDataUscitaAzCollAll");
  }
   
  
  
  
  final String errMsg = "Impossibile procedere nella sezione "+anagAziendaVO.getLabelElencoAssociati()+"."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/aziendeCollegateInserisci.htm";
 
 
  try
  { 
    if("attenderePrego".equalsIgnoreCase(operazione)) 
    {     
      request.setAttribute("action", action);
      operazione = "conferma";
      request.setAttribute("operazione", operazione);
      %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
      <%
    }
    else 
    {
      
      //provengo da aziende collegate --- INIZIALIZZAZIONE
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisci"))
      {
        WebUtils.removeUselessFilter(session,null);
      }
      
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciAzienda"))
      {
        ValidationErrors errors = null;
        Long idAziendaFigliaL = null;
        String idAziendaFiglia = request.getParameter("idAzienda");
        boolean inserimentoManuale = false;
        
        if(Validator.isNotEmpty(idAziendaFiglia)) //Passaggio dal popup
        {    
          idAziendaFigliaL = new Long(idAziendaFiglia);
        }
        else //non siamo passati attraverso il popup
        {
          errors = new AziendaCollegataVO().validateRicAziendeCollegateForSoci(request); //controllo correttezza cuaa
          if (errors != null) 
          {
            request.setAttribute("errors", errors);
          }
          else //il cuaa se inserito è corretto!!!
          {
            String cuaa = request.getParameter("cuaa");
            String partitaIva = request.getParameter("partitaIva");
            String denominazione = request.getParameter("denominazione");
            AnagAziendaVO azVO = new AnagAziendaVO();
            //se sono valorizzati sia il cuaa e la denominazione 
            //uso solo il cuaa nella ricerca
            if(Validator.isNotEmpty(cuaa))
            {
              cuaa = cuaa.trim();
              cuaa = cuaa.toUpperCase();
              azVO.setCUAA(cuaa);
            }
            else if(Validator.isNotEmpty(partitaIva))
            {
              partitaIva = partitaIva.trim();
              partitaIva = partitaIva.toUpperCase();
              azVO.setPartitaIVA(partitaIva);
            }
            else if(Validator.isNotEmpty(denominazione))
            {
              azVO.setDenominazione(denominazione);
            }
            
            
            try
            {
              Vector vectIdAziendaTmp = anagFacadeClient.getListOfIdAzienda(azVO, null,true);
              if((vectIdAziendaTmp !=null) && (vectIdAziendaTmp.size() == 1))
              {           
                idAziendaFigliaL =  (Long)vectIdAziendaTmp.get(0);
              }
              else if((vectIdAziendaTmp == null) || ((vectIdAziendaTmp !=null) && (vectIdAziendaTmp.size() == 0)))
              {                
                //Caso inserimento a mano
                inserimentoManuale = true; 
                //Non ho trovato aziende coi parametri di ricerca impostati
                //quindi devo settare in sessione una valore per
                //sbloccare l'inserimento a mano dell'azienda
                session.setAttribute("sbloccaSociInsAMano","true"); 
              }
              else
              {
                if(errors == null)
                {
                  errors = new ValidationErrors();
                }
                errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_TROPPEAZIENDE_TROVATE));
              }
            }
            catch (SolmrException se) //Spero che sia eccezzione di max numero record trovati! 
            {
               
              if(errors == null)
              {
                errors = new ValidationErrors();
              }
              errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_TROPPEAZIENDE_TROVATE));
            }
          }
        }
        
        AziendaCollegataVO azzCollVO = null;
        
        if(inserimentoManuale)
        {
          azzCollVO = new AziendaCollegataVO();
          azzCollVO.setDataIngresso(new Date());
          SoggettoAssociatoVO soggAssVO = new SoggettoAssociatoVO();
          soggAssVO.setCuaa(request.getParameter("cuaa"));
          soggAssVO.setDenominazione(request.getParameter("denominazione"));
          soggAssVO.setPartitaIva(request.getParameter("partitaIva"));
          soggAssVO.setIndirizzo(request.getParameter("indirizzoIns"));
          String comuneDesc =  request.getParameter("comuneIns");
          String provinciaDesc =  request.getParameter("provinciaIns");
          String istatComuneDesc = request.getParameter("istatComuneIns");
          String capDesc = request.getParameter("capIns");
          if(Validator.isNotEmpty(comuneDesc))
          {
            comuneDesc = comuneDesc.trim();
          }
          soggAssVO.setDenominazioneComune(comuneDesc);
          if(Validator.isNotEmpty(provinciaDesc))
          {
            provinciaDesc = provinciaDesc.trim();
          }
          soggAssVO.setSglProv(provinciaDesc);
          soggAssVO.setComune(istatComuneDesc);
          if(Validator.isNotEmpty(capDesc))
          {
            capDesc = capDesc.trim();
          }
          soggAssVO.setCap(capDesc);
          errors = soggAssVO.validateInserimento(errors);
          
          
          if(errors == null)
          {
          
            //Non inserito inserito il comune da pop Up            
            if(Validator.isNotEmpty(comuneDesc) && Validator.isNotEmpty(provinciaDesc))
            { 
              //caso in cui inseriti provincia e comune a mano
              //String istatComune = null;
              try 
              {
                istatComuneDesc = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneDesc, provinciaDesc);
                soggAssVO.setComune(istatComuneDesc);
                if(Validator.isNotEmpty(istatComuneDesc))
                {
                  ComuneVO comVO = anagFacadeClient.getComuneByISTAT(istatComuneDesc);                  
                  if(!capDesc.equalsIgnoreCase(comVO.getCap()))
                  {
                    errors = ErrorUtils.setValidErrNoNull(errors, "capIns", AnagErrors.ERRORE_CAP_NON_COERENTE);
                  }
                } 
              }
              catch(SolmrException se) 
              {
                errors = ErrorUtils.setValidErrNoNull(errors, "comuneIns", se.getMessage());
              }        
            }
            else
            {
              errors = ErrorUtils.setValidErrNoNull(errors, "comuneIns", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
              errors = ErrorUtils.setValidErrNoNull(errors, "provinciaIns", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
            }
            
          }
          
          if(errors == null)
          {            
            azzCollVO.setSoggettoAssociato(soggAssVO);
            
            
            if(vAziendaIns == null)
            {
              vAziendaIns = new Vector();
            }
                        
            vAziendaIns.add(azzCollVO);
          }
          
        }
        else
        {
          if(idAziendaFigliaL !=null)
          {
            Long idAziendaPadreL = anagAziendaVO.getIdAzienda();
            
            
            //Controllo se è già presente tra quelle figlie
            //che potrebbero non essere state selezionate in azindaCollegata.htm
            Vector elencoCollegateSorelle = null;
            try {
              elencoCollegateSorelle = anagFacadeClient.getAziendeCollegateByIdAzienda(idAziendaPadreL, false);     
            }
            catch (SolmrException se) {
              SolmrLogger.info(this, " - aziendeColleagateInserisciCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+""+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
            
            if(elencoCollegateSorelle !=null)
            {
              for(int i=0;i<elencoCollegateSorelle.size();i++)
              {
                AziendaCollegataVO azVO = (AziendaCollegataVO)elencoCollegateSorelle.get(i);
                if((azVO.getIdAziendaAssociata() != null) 
                  && (azVO.getIdAziendaAssociata().compareTo(idAziendaFigliaL) == 0))
                {
                  if(errors == null)
                  {
                    errors = new ValidationErrors();
                  }
                  errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_AZIENDA_GIAPRESENTE));
                  break; 
                }
              }        
            }
            //*****************************************
            
            
            //Controllo tra quelle inserite nella pagina aziendeCollegateModifica in sessione
            if((vAziendaIns !=null) && (errors == null))
            {
              for(int i=0;i<vAziendaIns.size();i++)
              {
                AziendaCollegataVO anagVOTmp = (AziendaCollegataVO)vAziendaIns.get(i);
                if((anagVOTmp.getIdAziendaAssociata() != null) 
                  && (anagVOTmp.getIdAziendaAssociata().compareTo(idAziendaFigliaL) == 0))
                {
                  if(errors == null)
                  {
                    errors = new ValidationErrors();
                  }
                  errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_AZIENDA_GIAPRESENTE));
                  break; 
                }
              }           
            }
            
            // Controllo che vengano selezionati un numero di elementi inferiore al parametro RAZA
            // Recupero il parametro che mi indica il numero massimo di record selezionabili
            if((vAziendaIns !=null) && (errors == null))
            {
              String parametroRaza = null;
              try {
                parametroRaza = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RAZA);;
              }
              catch(SolmrException se) {
                SolmrLogger.info(this, " - aziendeColleagateInserisciCtrl.jsp - FINE PAGINA");
                String messaggioErrore = (String)AnagErrors.ERRORE_KO_PARAMETRO_RAZA;
                String messaggio = messaggioErrore +": "+se.toString();
                request.setAttribute("messaggioErrore",messaggio);
                request.setAttribute("pageBack", actionUrl);
                %>
                  <jsp:forward page="<%= erroreViewUrl %>" />
                <%
                return;
              }
              
              Integer paramRazInt = new Integer(parametroRaza);
              if(vAziendaIns.size() >= paramRazInt.intValue())
              {
                if(errors == null)
                {
                  errors = new ValidationErrors();
                }
                errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_TROPPE_VOCI_INSERITE));     
              }
            }
            
            //Controllo che tra i "padri/nonni/ecc.." dell'azienda su cui si stà operando
            //è già presente l'azienda figlia che si vuole inserire
            if(idAziendaFigliaL.compareTo(idAziendaPadreL) == 0)
            { //Sto cercando di aggiungere un'azienda come figlia di se stessa!!!
            
              if(errors == null)
              {
                errors = new ValidationErrors();
              }
              errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_ESISTONOANTENATI));
            }
         
            
            if(errors == null) //Passato i controlli aggiungo l'azienda
            {
              
              AnagAziendaVO anagVOTmp = anagFacadeClient.findAziendaAttiva(idAziendaFigliaL);
              azzCollVO = new AziendaCollegataVO();
              azzCollVO.setDenominazione(anagVOTmp.getDenominazione());
              azzCollVO.setIdAziendaAssociata(anagVOTmp.getIdAzienda());
              azzCollVO.setCuaa(anagVOTmp.getCUAA());
              azzCollVO.setPartitaIva(anagVOTmp.getPartitaIVA());
              azzCollVO.setDataIngresso(new Date());
              
              if(Validator.isNotEmpty(anagVOTmp.getSedelegCittaEstero()))
              {
                azzCollVO.setDenominazioneComune(anagVOTmp.getSedelegCittaEstero());
                azzCollVO.setIndirizzo(anagVOTmp.getSedelegIndirizzo());
              }
              else
              {
                azzCollVO.setDenominazioneComune(anagVOTmp.getDescComune());
                azzCollVO.setSglProv(anagVOTmp.getSedelegProv());
                azzCollVO.setIndirizzo(anagVOTmp.getSedelegIndirizzo());
                azzCollVO.setCap(anagVOTmp.getSedelegCAP());
                azzCollVO.setIstatComune(anagVOTmp.getSedelegIstatComune());
              }
              
              if(vAziendaIns == null)
              {
                vAziendaIns = new Vector();
              }
              
              vAziendaIns.add(azzCollVO);
              
            }
          }
        }
        
        if(errors !=null)
        {
          request.setAttribute("errors", errors);
        }
        else
        {
          session.setAttribute("vAziendaIns", vAziendaIns);
          
          //Blocca l'inserimento a mano
          session.removeAttribute("sbloccaSociInsAMano");
        }
      }
      else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("conferma"))
      {
        //tutte le aziende per associare correttamente le date
        Vector vAziendaInsConf = (Vector)session.getAttribute("vAziendaIns");
        String[] elencoDataIngressoAll = (String[])session.getAttribute("elencoDataIngressoAzCollAll");
        String[] elencoDataUscitaAll = (String[])session.getAttribute("elencoDataUscitaAzCollAll");
        
        if(vAziendaInsConf !=null)
        {
          HashMap hElencoErrori = null;
          ValidationErrors errors = null;
          
          Long idAziendaPadreL = anagAziendaVO.getIdAzienda();
          
          
          for(int i=0;i<vAziendaInsConf.size();i++)
          {
            AziendaCollegataVO azCollVO = (AziendaCollegataVO)vAziendaInsConf.get(i);
            azCollVO.setIdAzienda(idAziendaPadreL);
            azCollVO.setDataIngressoStr(elencoDataIngressoAll[i]);
            azCollVO.setDataUscitaStr(elencoDataUscitaAll[i]);
          }
          
          for(int i=0;i<vAziendaInsConf.size();i++)
          {
            Long countL = new Long(i);
            AziendaCollegataVO azCollVO = (AziendaCollegataVO)vAziendaInsConf.get(i);
            errors = azCollVO.validateInsertAziende();
            if(errors != null)
            {
              if(hElencoErrori == null)
              {
                hElencoErrori = new HashMap();
              }
              hElencoErrori.put(countL,errors);
            }
          }
          
          
          if(hElencoErrori !=null)
          {
            request.setAttribute("hElencoErrori",hElencoErrori);
          }
          else // non sono stati rilevati errori storicizzo
          {
            try {
        
              anagFacadeClient.storicizzaAziendeCollegateBlocco(ruoloUtenza, vAziendaInsConf);
              
              %>
                <jsp:forward page="<%= aziendeCollegateCtrlUrl %>" />
              <%
              return;
            } 
            catch (SolmrException se) {
              String messaggio = errMsg+""+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
          }
        }
        else
        {
          request.setAttribute("messaggioErrore",AnagErrors.ERRORE_NO_AZIENDE_INSERITE);
        }
        
      }
      else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("elimina"))
      {        
        String[] chkIdCount = request.getParameterValues("chkIdCount");
        
        if(Validator.isNotEmpty(chkIdCount))
        {
          for(int i=(chkIdCount.length - 1);i>=0;i--)
          {
            vAziendaIns.remove(new Integer(chkIdCount[i]).intValue());
          }
        }
        else
        {
          flagPagErrore = true;
          request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_AZIENDE_SELEZIONATE);
        }
        
        if(vAziendaIns.size() == 0)
        {
          vAziendaIns = null;
        }
        
        session.setAttribute("vAziendaIns",vAziendaIns);
        
      }
      
    } //fine else attendere prego
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - aziendeColleagateInserisciCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  if(flagPagErrore && (request.getAttribute("messaggioErrore") !=null))
  {
    request.setAttribute("history","true");
    //request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;    
  } 
  
  
  
  

  

%>

<jsp:forward page= "<%= aziendeCollegateInserisciUrl %>" />


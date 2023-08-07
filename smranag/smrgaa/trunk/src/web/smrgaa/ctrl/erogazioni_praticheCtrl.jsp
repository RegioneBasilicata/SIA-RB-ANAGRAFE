<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.solmr.exception.SolmrException"%>
<%@ page import="it.csi.sigop.dto.services.PagamentiErogatiVO" %>
<%@ page import="it.csi.sigop.dto.services.PagamentoErogatoVO" %>
<%@ page import="it.csi.sigop.dto.services.RecuperiPregressiVO" %>
<%@ page import="it.csi.sigop.dto.services.RecuperoPregressoVO" %>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.WebUtils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.comune.AmmCompetenzaVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.Validator" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "erogazioni_praticheCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - erogazioni_praticheCtrl.jsp - INIZIO PAGINA");
  String erogazioniPraticheUrl = "/view/erogazioni_praticheView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  if(Validator.isNotEmpty(request.getParameter("regimeErogazioni")))
    WebUtils.removeUselessFilter(session, "arrPagamentoErogatoVO,arrRecuperoPregressoVO");
  else
    WebUtils.removeUselessFilter(session, null);
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  String settore = request.getParameter("settore");
  String idAnnoPagamento = request.getParameter("idAnnoPagamento");
  String idAnnoCampagna = request.getParameter("idAnnoCampagna");
  String regimeErogazioni = request.getParameter("regimeErogazioni");
  
  
  
  
  
  
  final String errMsg = "Il servizio di consultazione erogazioni è momentaneamente non disponibile. ";
  
  PagamentoErogatoVO[] arrPagamentoErogatoVO = (PagamentoErogatoVO[])session.getAttribute("arrPagamentoErogatoVO");
  RecuperoPregressoVO[] arrRecuperoPregressoVO = (RecuperoPregressoVO[])session.getAttribute("arrRecuperoPregressoVO");
  
  
  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  try 
  {
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) {
    request.setAttribute("statoAzienda", se);
  }
  
  
  String parametroSigp = null;
  try 
  {
    parametroSigp = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_SIGP);;
  }
  catch(SolmrException se) {
  
    SolmrLogger.info(this, " - aziendeColleagateCtrl.jsp - FINE PAGINA");
    String messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_SIGP;
    String messaggio = messaggioErrore +": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  if(Validator.isNotEmpty(parametroSigp) && parametroSigp.equalsIgnoreCase(SolmrConstants.FLAG_S))
  {
    //carico una sola volta in sessione per evitare coi filtri ogni volta di chiamare il servizio!!
    if(arrPagamentoErogatoVO == null)
    {  
      try
      {
        if(anagAziendaVO.getCUAA() != null)
        {
          PagamentiErogatiVO pagamentiErogati = anagFacadeClient.sigopservEstraiPagamentiErogati(anagAziendaVO.getCUAA(), null, null);      
          //ok
          if(pagamentiErogati.getEsitoServizio().getEsito() == 1)
          {
            arrPagamentoErogatoVO = pagamentiErogati.getListaPagamenti();
            session.setAttribute("arrPagamentoErogatoVO", arrPagamentoErogatoVO);
          }
          else
          {
            String messaggio = errMsg+""+pagamentiErogati.getEsitoServizio().getCodErrore()
            +":"+pagamentiErogati.getEsitoServizio().getMsgErrore();
		        request.setAttribute("messaggioErrore",messaggio);
		        request.setAttribute("pageBack", actionUrl);
		        %>
		          <jsp:forward page="<%= erroreViewUrl %>" />
		        <%
		        return;
          }
          
          RecuperiPregressiVO recuperiPregressi = anagFacadeClient.sigopservEstraiRecuperiPregressi(anagAziendaVO.getCUAA(), null, null);
          //ok
          if(recuperiPregressi.getEsitoServizio().getEsito() == 1)
          {
            arrRecuperoPregressoVO = recuperiPregressi.getListaRecuperi();
            session.setAttribute("arrRecuperoPregressoVO", arrRecuperoPregressoVO);
          }
          else
          {
            String messaggio = errMsg+""+recuperiPregressi.getEsitoServizio().getCodErrore()
            +":"+recuperiPregressi.getEsitoServizio().getMsgErrore();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
        }
      
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - erogazioni_praticheCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
    
    
    
    String messaggioErrore = "";
    String messaggioErroreRecupero = "";
    if((arrPagamentoErogatoVO == null) || 
      (arrPagamentoErogatoVO != null && arrPagamentoErogatoVO.length == 0))
    {
      messaggioErrore =  AnagErrors.ERR_NO_PAGAMENTI_BENEFICIARI;     
    }
    else
    {
      if((arrRecuperoPregressoVO == null) ||
        (arrRecuperoPregressoVO != null && arrRecuperoPregressoVO.length == 0))
      {
        messaggioErroreRecupero = AnagErrors.ERR_NO_RECUPERI_PREGRESSI;
      }
    
      TreeMap<Integer,String> tAnnoPagamento = null;
      for(int i=0;i<arrPagamentoErogatoVO.length;i++)
      {
	      Integer anno = arrPagamentoErogatoVO[i].getAnnoDisposizionePagamento();
	      if(anno != null)
	      {
	        if(tAnnoPagamento == null)
	        {
	          tAnnoPagamento = new TreeMap<Integer,String>(java.util.Collections.reverseOrder());
	        }
	        
	        if(tAnnoPagamento.get(anno) == null)
	        {        
	          tAnnoPagamento.put(anno, anno.toString());
	        }
	      }
	    }
	    request.setAttribute("tAnnoPagamento", tAnnoPagamento);
	    
	    //prima volta che entro
	    if(Validator.isEmpty(idAnnoPagamento) && Validator.isEmpty(regimeErogazioni))
	    {
	      idAnnoPagamento = tAnnoPagamento.get(tAnnoPagamento.firstKey());
	    }
	    
	    TreeMap<Integer,String> tAnnoCampagna = null;
      for(int i=0;i<arrPagamentoErogatoVO.length;i++)
      {
        Integer anno = arrPagamentoErogatoVO[i].getAnnoCampagna();
        if(anno != null)
        {
          if(tAnnoCampagna == null)
          {
            tAnnoCampagna = new TreeMap<Integer,String>(java.util.Collections.reverseOrder());
          }
          
          if(tAnnoCampagna.get(anno) == null)
          {        
            tAnnoCampagna.put(anno, anno.toString());
          }
        }
      }
      request.setAttribute("tAnnoCampagna", tAnnoCampagna);
      
      //prima volta che entro
      if(Validator.isEmpty(idAnnoCampagna) && Validator.isEmpty(regimeErogazioni))
      {
        idAnnoCampagna = tAnnoCampagna.get(tAnnoCampagna.firstKey());
      }
      
    
    
      //Applico filtri se valorizzati
      if(Validator.isNotEmpty(idAnnoPagamento) || Validator.isNotEmpty(settore)
        || Validator.isNotEmpty(idAnnoCampagna))
      {
        Vector<PagamentoErogatoVO> vPagamenti = null;
        for(int i=0;i<arrPagamentoErogatoVO.length;i++)
        {        
          boolean flagFiltroAnnoPagOK = true;
          boolean flagFiltroAnnoCampOK = true;
          boolean flagFiltroSettoreOK = true;
          if(Validator.isNotEmpty(idAnnoPagamento))
          {
            if((arrPagamentoErogatoVO[i].getAnnoDisposizionePagamento() != null)
              && (arrPagamentoErogatoVO[i].getAnnoDisposizionePagamento().compareTo(new Integer(idAnnoPagamento)) == 0))
            {
              flagFiltroAnnoPagOK = true;
            }
            else
            {
              flagFiltroAnnoPagOK = false;
            }
          }
          
          if(Validator.isNotEmpty(idAnnoCampagna))
          {
            if((arrPagamentoErogatoVO[i].getAnnoCampagna() != null)
              && (arrPagamentoErogatoVO[i].getAnnoCampagna().compareTo(new Integer(idAnnoCampagna)) == 0))
            {
              flagFiltroAnnoCampOK = true;
            }
            else
            {
              flagFiltroAnnoCampOK = false;
            }
          }
          
          if(Validator.isNotEmpty(settore))
          {
            if(Validator.isNotEmpty(arrPagamentoErogatoVO[i].getDescSettore())
              && arrPagamentoErogatoVO[i].getDescSettore().equalsIgnoreCase(settore))
            {
              flagFiltroSettoreOK = true;
            }
            else
            {
              flagFiltroSettoreOK = false;
            }
          
          }
          
          if(flagFiltroAnnoPagOK && flagFiltroAnnoCampOK && flagFiltroSettoreOK)
          {
            if(vPagamenti == null)
            {
              vPagamenti = new Vector<PagamentoErogatoVO>();
            }
            
            
            vPagamenti.add(arrPagamentoErogatoVO[i]);
            
          }
        
        }        
        
        if(vPagamenti != null)
        {
          PagamentoErogatoVO[] pagamentoErogatoFiltro =
            new PagamentoErogatoVO[vPagamenti.size()];
            
          for(int i=0;i<vPagamenti.size();i++)
          {
            pagamentoErogatoFiltro[i] = vPagamenti.get(i);
          }
          
          
          request.setAttribute("pagamentoErogatoElenco", pagamentoErogatoFiltro);
            
        }
        else
        {
          messaggioErrore = AnagErrors.ERR_NO_PAGAMENTI_BENEFICIARI_FILTRI;
        }
        
        
        Vector<RecuperoPregressoVO> vRecuperi = null;
        for(int i=0;i<arrRecuperoPregressoVO.length;i++)
        {        
          boolean flagFiltroAnnoPagOK = true;
          boolean flagFiltroAnnoCampOK = true;
          boolean flagFiltroSettoreOK = true;
          if(Validator.isNotEmpty(idAnnoPagamento))
          {
            if((arrRecuperoPregressoVO[i].getAnnoDisposizionePagamento() != null)
              && (arrRecuperoPregressoVO[i].getAnnoDisposizionePagamento().compareTo(new Integer(idAnnoPagamento)) == 0))
            {
              flagFiltroAnnoPagOK = true;
            }
            else
            {
              flagFiltroAnnoPagOK = false;
            }
          }
          
          if(Validator.isNotEmpty(idAnnoCampagna))
          {
            if((arrRecuperoPregressoVO[i].getAnnoCampagna() != null)
              && (arrRecuperoPregressoVO[i].getAnnoCampagna().compareTo(new Integer(idAnnoCampagna)) == 0))
            {
              flagFiltroAnnoCampOK = true;
            }
            else
            {
              flagFiltroAnnoCampOK = false;
            }
          }
          
          if(Validator.isNotEmpty(settore))
          {
            if(Validator.isNotEmpty(arrRecuperoPregressoVO[i].getDescSettore())
              && arrRecuperoPregressoVO[i].getDescSettore().equalsIgnoreCase(settore))
            {
              flagFiltroSettoreOK = true;
            }
            else
            {
              flagFiltroSettoreOK = false;
            }
          
          }
          
          if(flagFiltroAnnoPagOK && flagFiltroAnnoCampOK && flagFiltroSettoreOK)
          {
            if(vRecuperi == null)
            {
              vRecuperi = new Vector<RecuperoPregressoVO>();
            }          
            
            vRecuperi.add(arrRecuperoPregressoVO[i]);            
          }
        
        }        
        
        if(vRecuperi != null)
        {
          RecuperoPregressoVO[] recuperoPregressoFiltro =
            new RecuperoPregressoVO[vRecuperi.size()];
            
          for(int i=0;i<vRecuperi.size();i++)
          {
            recuperoPregressoFiltro[i] = vRecuperi.get(i);
          }
          
          
          request.setAttribute("recuperoPregressoElenco", recuperoPregressoFiltro);
            
        }
        else
        {
          messaggioErroreRecupero = AnagErrors.ERR_NO_RECUPERI_PREGRESSI_FILTRI;
        } 
        
      }
      else //Metto tutti perchè nessun filtro valorizzato
      {
        request.setAttribute("pagamentoErogatoElenco", arrPagamentoErogatoVO);
        request.setAttribute("recuperoPregressoElenco", arrRecuperoPregressoVO);
      }
    
    }
    
    if(Validator.isNotEmpty(messaggioErrore))
      request.setAttribute("messaggioErrore", messaggioErrore);
      
    if(Validator.isNotEmpty(messaggioErroreRecupero))
      request.setAttribute("messaggioErroreRecupero", messaggioErroreRecupero);
  }
  else
  {
    request.setAttribute("messaggioErrore", errMsg);    
  }
  
  
  


  SolmrLogger.debug(this, " - erogazioni_praticheCtrl.jsp - FINE PAGINA");

 %>
 
 
 
 <jsp:forward page= "<%= erogazioniPraticheUrl %>" />

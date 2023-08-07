<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popElencoAziendeAjaxCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
  
  final String errMsg = "Impossibile procedere nella ricerca aziende. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	String popElencoAziendeUrl = "/view/popElencoAziendeAjaxView.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  String cuaa = request.getParameter("cuaa");
  String partitaIva = request.getParameter("partitaIva");
  String denominazione = request.getParameter("denominazione");
  String operazione = request.getParameter("operazione");
  String provenienza = request.getParameter("provenienza");
  
  int sizeResult = 0;
  int numBlock = 1;
  
  int paginaCorrente = 0;
  Integer paginaCorrenteInteger;
  
  
  if(Validator.isEmpty(operazione))
  {
    session.removeAttribute("currPage");
    session.removeAttribute("listRange");
    session.removeAttribute("listIdAzienda");
    session.removeAttribute("idAziendaSelezionata");
    
    
    session.setAttribute("currPage",new Integer("1"));
  
    try 
    {
      //cerco solo le aziende con data_cessazione == null (terzo parametro = true)) 
      //e data_fine_validità == null (secondo parametro == null)
      AnagAziendaVO  aziendaVO = new AnagAziendaVO();
      if(Validator.isNotEmpty(cuaa))
      {
        cuaa = cuaa.trim();
        cuaa = cuaa.toUpperCase();
      }
      aziendaVO.setCUAA(cuaa);
      if(Validator.isNotEmpty(partitaIva))
      {
        partitaIva = partitaIva.trim();
        partitaIva = partitaIva.toUpperCase();
      }
      aziendaVO.setPartitaIVA(partitaIva);
      if(Validator.isNotEmpty(denominazione))
      {
        denominazione = denominazione.trim();
        denominazione = denominazione.toUpperCase();
      }
      aziendaVO.setDenominazione(denominazione);
      //false prendo le aziende non provvisorie
      boolean attive = false;     
      Vector<Long> vectIdAnagAziendaInitial = anagFacadeClient
        .getListIdAziendeFlagProvvisorio(aziendaVO, null,attive,false);
        
        
      sizeResult=vectIdAnagAziendaInitial.size();      
      session.setAttribute("listIdAzienda",vectIdAnagAziendaInitial);
      
      if(sizeResult == 0)
      {
        //Non ho trovato aziende coi parametri di ricerca impostati        
        %>
          <jsp:forward page ="<%=popElencoAziendeUrl %>" />
        <%
        return;
      }
      else
      {
      
        //ho trovato aziende coi parametri di ricerca impostati        
        Vector<Long> rangeIdAA=new Vector<Long>();
        
        int limiteA;
        
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG;
        for(int i=(numBlock-1)*SolmrConstants.NUM_MAX_ROWS_PAG;i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdAnagAziendaInitial.elementAt(i));
        }
        Vector<AnagAziendaVO> rangAnagAzienda = anagFacadeClient.getListAziendeByIdRangeFromIdAzienda(rangeIdAA);
        session.setAttribute("listRange",rangAnagAzienda);
      }
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - popElencoAziendeAjaxCtrl.jsp - FINE PAGINA");
      String messaggio = se.getMessage();
      request.setAttribute("messaggioErrore",messaggio);
      session.setAttribute("chiudi", "chiudi");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  Vector<Long> vectIdAnagAzienda = (Vector<Long>)session.getAttribute("listIdAzienda");
  Vector<AnagAziendaVO> rangeAnagAzienda = (Vector<AnagAziendaVO>)session.getAttribute("listRange");
  
  String aziendaSelezionata = request.getParameter("idAzienda");
  if(Validator.isNotEmpty(aziendaSelezionata)) 
  {
    session.setAttribute("idAziendaSelezionata", Long.decode(aziendaSelezionata));
  }

  
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("avanti")) 
  {
    try 
    {
      if(vectIdAnagAzienda!=null)
      {
        if(rangeAnagAzienda!=null && rangeAnagAzienda.size()>0)        
          sizeResult = vectIdAnagAzienda.size();
        paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
        if(paginaCorrenteInteger.toString().equals(request.getParameter("totalePagine")))
          paginaCorrente = paginaCorrenteInteger.intValue();
        else
          paginaCorrente = paginaCorrenteInteger.intValue()+1;
        Vector<Long> rangeIdAA=new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdAnagAzienda.elementAt(i));
        }
        session.removeAttribute("listRange");
        rangeAnagAzienda = anagFacadeClient.getListAziendeByIdRangeFromIdAzienda(rangeIdAA);
        session.setAttribute("listRange",rangeAnagAzienda);


        session.removeAttribute("currPage");
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);

      }
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - popElencoAziendeAjaxCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      session.setAttribute("chiudi", "chiudi");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  else 
  {
    try 
    {
      if(vectIdAnagAzienda!=null)
      {
        sizeResult = vectIdAnagAzienda.size();
        paginaCorrenteInteger = ((Integer)session.getAttribute("currPage"));
        if(paginaCorrenteInteger.toString().equals("1"))
          paginaCorrente = paginaCorrenteInteger.intValue();
        else
          paginaCorrente = paginaCorrenteInteger.intValue()-1;
        Vector<Long> rangeIdAA=new Vector<Long>();
        int limiteA;
        if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG*paginaCorrente;
        for(int i=(paginaCorrente-1)*SolmrConstants.NUM_MAX_ROWS_PAG;i<limiteA;i++)
        {
          rangeIdAA.addElement(vectIdAnagAzienda.elementAt(i));
        }
        session.removeAttribute("listRange");
        rangeAnagAzienda = anagFacadeClient.getListAziendeByIdRangeFromIdAzienda(rangeIdAA);
        session.setAttribute("listRange",rangeAnagAzienda);
        session.removeAttribute("currPage");
        paginaCorrenteInteger = new Integer(paginaCorrente);
        session.setAttribute("currPage",paginaCorrenteInteger);
      }
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - popElencoAziendeAjaxCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      session.setAttribute("chiudi", "chiudi");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }  
  
  
  %>
    	<jsp:forward page ="<%=popElencoAziendeUrl %>" />
    


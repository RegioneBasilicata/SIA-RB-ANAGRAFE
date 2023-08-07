<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>


<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO"%>
<%@ page  import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoCCVO"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%!
  private static final String NOME_ORDINE_PER_ANNO = "anno";
%>

<%

  String iridePageName = "popContiCorrentiPraticheCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%
	
  String popContiCorrentiPraticheUrl = "/view/popContiCorrentiPraticheView.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  String idContoCorrente = request.getParameter("idContoCorrente");
  String regimePopContiCorrenti = request.getParameter("regimePopContiCorrenti");
  RitornoPraticheCCAgriservVO ritornoPraticheCCAgriservVO
    = (RitornoPraticheCCAgriservVO)session.getAttribute("ritornoPraticheCCAgriservVO");
  
  if(!Validator.isNotEmpty(regimePopContiCorrenti))
  {
	  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    
    long[] idContiCorrenti = new long[1];
    idContiCorrenti[0] = new Long(idContoCorrente).longValue();
    
    try 
    {
      int tipologia = PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_BOZZA
        + PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_RESPINTO
        + PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_ANNULLATO
        + PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_ANNULLATO_PER_SOSTITUZIONE;
        
      ritornoPraticheCCAgriservVO = gaaFacadeClient
        .searchPraticheContoCorrente(idContiCorrenti, tipologia, null, null, null, 
        PraticaProcedimentoCCVO.ORDINAMENTO_PER_PRATICHE);
    }
    catch (SolmrException se) {
      SolmrLogger.info(this, " - popContiCorrentiPraticheCtrl.jsp - FINE PAGINA");
      String messaggio = se.getMessage();
      request.setAttribute("messaggioErrore",messaggio);
      session.setAttribute("chiudi", "chiudi");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  
  }
  
  
  HashMap<Long,PraticaProcedimentoCCVO[]> hPratiche = ritornoPraticheCCAgriservVO.getHPraticheCC();
  if(hPratiche != null)
  { 
    Iterator<PraticaProcedimentoCCVO[]> it = hPratiche.values().iterator();
    PraticaProcedimentoCCVO[] arrPraticaProcedimentoCCVO 
      = (PraticaProcedimentoCCVO[])it.next();
    
    ordinaPratiche(arrPraticaProcedimentoCCVO, request);
  }
  
  session.setAttribute("ritornoPraticheCCAgriservVO", ritornoPraticheCCAgriservVO);
  
  

	
	
	
  
  
  
  
  
  
  %>
    	<jsp:forward page ="<%=popContiCorrentiPraticheUrl %>" />
      
<%!

private void ordinaPratiche(PraticaProcedimentoCCVO[] arrPraticaProcedimentoCCVO, HttpServletRequest request)
{
  if (arrPraticaProcedimentoCCVO == null || arrPraticaProcedimentoCCVO.length<=1)
  {
    return; // Nessun ordinamento se vettore null o di lunghezza 0 o 1
  }
  String ordine = request.getParameter("ordine");
  if (ordine == null)
  {
    ordine = "";
  }
  
  if (NOME_ORDINE_PER_ANNO.equalsIgnoreCase(ordine))
  {
    if("true".equalsIgnoreCase(request.getParameter("ascendente")))
    {
      Arrays.sort(arrPraticaProcedimentoCCVO, new AnnoComparatorAsc());
    }
    else
    {
      Arrays.sort(arrPraticaProcedimentoCCVO, new AnnoComparatorDesc());
    }
  }
  else
  {
    Arrays.sort(arrPraticaProcedimentoCCVO, new AnnoComparatorDesc());
  }
}

class AnnoComparatorAsc implements Comparator<PraticaProcedimentoCCVO>
{
  public int compare(PraticaProcedimentoCCVO o1, PraticaProcedimentoCCVO o2) 
  {
  
    int anno1 = ((PraticaProcedimentoCCVO)o1).getAnnoCampagna();
    int anno2 = ((PraticaProcedimentoCCVO)o2).getAnnoCampagna();
  
    if( anno1 > anno2 )
    {
      return 1;
    }
    else if( anno1 < anno2)
    {
      return -1;
    }
    else
    {
      return 0;
    }
  }
}

class AnnoComparatorDesc implements Comparator<PraticaProcedimentoCCVO> 
{
  public int compare(PraticaProcedimentoCCVO o1, PraticaProcedimentoCCVO o2) 
  {
  
    int anno1 = ((PraticaProcedimentoCCVO)o1).getAnnoCampagna();
    int anno2 = ((PraticaProcedimentoCCVO)o2).getAnnoCampagna();
  
    if( anno1 > anno2 )
    {
      return -1;
    }
    else if( anno1 < anno2)
    {
      return 1;
    }
    else
    {
      return 0;
    }
  }
}



%>
    


<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page  import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoCCVO"%>


<%

  // Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);


  String iridePageName = "contiCorrentiCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  try 
  {
    AnagFacadeClient anagClient = new AnagFacadeClient();
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
    // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
    // in modo che venga sempre effettuato
    try 
    {
      anagClient.checkStatoAzienda(anagVO.getIdAzienda());
    }
    catch(SolmrException se) 
    {
      request.setAttribute("statoAzienda", se);
    }

    try 
    {
      if(request.getParameter("elimina") != null || request.getParameter("eliminaDett") != null) 
      {
        String messaggioErrore = "";
        String idContoCorrente = request.getParameter("idContoCorrente");
        ContoCorrenteVO ccVO = anagClient.getContoCorrente(idContoCorrente);
        if(ccVO != null)
        {
          ccVO.setIdContoCorrente(new Long(idContoCorrente));
          if(ccVO.getDataEstinzione() != null || ccVO.getDataFineValiditaContoCorrente() != null) 
          {
            messaggioErrore = "Conto corrente estinto";
            request.setAttribute("messaggioErrore", messaggioErrore);           
          }
          else if("S".equalsIgnoreCase(ccVO.getFlagContoVincolato()))
          {
            messaggioErrore = "Non è possibile modificare un conto corrente esclusivo";
            request.setAttribute("messaggioErrore", messaggioErrore);
          }
        }
      
        if(Validator.isEmpty(messaggioErrore))
        {
	        %>
	  	      <jsp:forward page="../ctrl/confermaEliminaContoCorrenteCtrl.jsp" />
	        <%
	       	return;
	    }
      }
      if(request.getParameter("modifica") != null || request.getParameter("modificaDett") != null) 
      {
    	String messaggioErrore = "";
    	
        String idContoCorrente = request.getParameter("idContoCorrente");
        ContoCorrenteVO ccVO = anagClient.getContoCorrente(idContoCorrente);
        if(ccVO != null)
        {
          ccVO.setIdContoCorrente(new Long(idContoCorrente));
          if(ccVO.getDataEstinzione() != null || ccVO.getDataFineValiditaContoCorrente() != null) 
          {
            messaggioErrore = "Conto corrente estinto";
            request.setAttribute("messaggioErrore", messaggioErrore);            
          }
          else if("S".equalsIgnoreCase(ccVO.getFlagContoVincolato()))
          {
            messaggioErrore = "Non è possibile modificare un conto corrente esclusivo";
            request.setAttribute("messaggioErrore", messaggioErrore); 
          }
          
          
          if(Validator.isEmpty(messaggioErrore))
          {
            request.setAttribute("ccVO",ccVO);
            %>
              <jsp:forward page="../ctrl/contiCorrentiModCtrl.jsp" />
            <%
            return;
          }
        }
      }
      if(request.getParameter("inserisci") != null) 
      {
      	%>
          <jsp:forward page="../ctrl/contiCorrentiNewCtrl.jsp" />
        <%
        return;
      }
    }
    catch(SolmrException s)
    {
      // La ValidationErrors è una collezione di ValidationError
      ValidationErrors ve = new ValidationErrors();
      // Il ValidationError rappresenta un'errore
      ve.add("error",new ValidationError(s.getMessage()));
      request.setAttribute("errors",ve);
      s.printStackTrace();
      if(request.getParameter("modificaDett") != null || request.getParameter("eliminaDett") != null)
      {
        //Devo rimanere nella pagina di dettaglio
        %>
          <jsp:forward page="../ctrl/contiCorrentiDetCtrl.jsp" />
        <%
        return;
      }
    }
    Vector<ContoCorrenteVO> conti = null;
    if("si".equals(request.getParameter("storico")))
    {
      //Il sistema visualizza l\u2019elenco di tutti i conti correnti legati all\u2019azienda ordinati dal più recente
      //al più vecchio.In questa visualizzazione compare anche una data al.
      //Per i conti estinti, questa data sarà costituita dalla data di estinzione.
      request.setAttribute("storico","si");
      conti = anagClient.getContiCorrenti(anagVO.getIdAzienda(),true);
    }
    else
    {
      //Il sistema visualizza in forma tabellare l\u2019elenco dei conti correnti attivi associati all\u2019azienda
      conti = anagClient.getContiCorrenti(anagVO.getIdAzienda(),false);
    }

    RitornoPraticheCCAgriservVO ritornoAgriservCCVO = null;
    if((conti !=null) && (conti.size() > 0))
    {
      Vector<Long> vIdContiCorrenti = new Vector<Long>();
      for(int i=0;i<conti.size();i++)
      {
        ContoCorrenteVO ccVO = (ContoCorrenteVO)conti.get(i);
        vIdContiCorrenti.add(ccVO.getIdContoCorrente());
      }
      long[] idContiCorrenti = new long[vIdContiCorrenti.size()];
      for(int i=0;i<vIdContiCorrenti.size();i++)
      {
        Long idContoCorrenteLg = (Long)vIdContiCorrenti.get(i);
        idContiCorrenti[i] = idContoCorrenteLg.longValue();
      }
  
      int tipologia = PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_BOZZA
        + PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_RESPINTO
        + PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_ANNULLATO
        + PraticaProcedimentoCCVO.FLAG_STATO_ESCLUDI_ANNULLATO_PER_SOSTITUZIONE;
        
      ritornoAgriservCCVO = gaaFacadeClient
        .searchPraticheContoCorrente(idContiCorrenti, tipologia, null, null, null, 
        PraticaProcedimentoCCVO.ORDINAMENTO_PER_PRATICHE); 
    }

    request.setAttribute("ritornoAgriservCCVO", ritornoAgriservCCVO);

    request.setAttribute("conti",conti);
  }
  catch(SolmrException s) 
  {
    // La ValidationErrors è una collezione di ValidationError
    ValidationErrors ve = new ValidationErrors();
    // Il ValidationError rappresenta un'errore
    ve.add("error",new ValidationError(s.getMessage()));
    request.setAttribute("errors",ve);
    s.printStackTrace();
    if(request.getParameter("modificaDett") != null || request.getParameter("eliminaDett") != null) 
    {
      //Devo rimanere nella pagina di dettaglio
      %>
      	<jsp:forward page="../ctrl/contiCorrentiDetCtrl.jsp" />
      <%
      return;
    }
  }
  catch(Exception e) 
  {
   	// La ValidationErrors è una collezione di ValidationError
   	it.csi.solmr.util.ValidationErrors ve = new ValidationErrors();
   	// Il ValidationError rappresenta un'errore
   	ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
   	request.setAttribute("errors",ve);
   	e.printStackTrace();
   	if(request.getParameter("modificaDett") != null || request.getParameter("eliminaDett") != null) 
    {
      //Devo rimanere nella pagina di dettaglio
      %>
        <jsp:forward page="../ctrl/contiCorrentiDetCtrl.jsp" />
      <%
      return;
    }
  }
  %>
  	<jsp:forward page="../view/contiCorrentiView.jsp" />

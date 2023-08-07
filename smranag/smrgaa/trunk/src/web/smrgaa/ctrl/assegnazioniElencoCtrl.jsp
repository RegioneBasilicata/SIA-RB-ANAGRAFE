<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.exception.SolmrException"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smruma.umaserv.dto.DittaUmaVO" %>
<%@ page import="it.csi.smruma.umaserv.dto.AnnoRiferimentoVO" %>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.WebUtils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.Validator" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "assegnazioniElencoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - assegnazioniElencoCtrl.jsp - INIZIO PAGINA");
  String assegnazioniElencoUrl = "/view/assegnazioniElencoView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  WebUtils.removeUselessFilter(session, null);
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  String idAnno = request.getParameter("idAnno");
  String regimeAssegnazioni = request.getParameter("regimeAssegnazioni");
  
  
  
  
  
  
  final String errMsg = "Il servizio dei buoni carburante è momentaneamente non disponibile. ";
  
  
  //assegnazioniElenco = (DittaUmaVO[])session.getAttribute("assegnazioniElenco");
  
  
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
  
  
  
  DittaUmaVO[] assegnazioniElenco = null;
  try
  {
    String[] arrCodiceStatoAnag = new String[1];
    arrCodiceStatoAnag[0] = DittaUmaVO.FLAG_STATO_CONCLUSA;
    assegnazioniElenco = gaaFacadeClient.umaservGetAssegnazioniByIdAzienda(
    anagAziendaVO.getIdAzienda().longValue(), arrCodiceStatoAnag);      
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - assegnazioniElencoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("assegnazioniElenco",assegnazioniElenco);
  
  
  if(assegnazioniElenco == null)
  {
    request.setAttribute("messaggioErrore", AnagErrors.ERR_NO_BUONI_CARBURANTE);
  }
  else
  {
  
    TreeMap<Integer,String> tAnno = null;
    for(int i=0;i<assegnazioniElenco.length;i++)
    {
      if(assegnazioniElenco[i].getArrAnnoRiferimentoVO() != null)
      {
	      for(int j=0;j<assegnazioniElenco[i].getArrAnnoRiferimentoVO().length;j++)
	      { 
		      Integer anno =  new Integer(
		        assegnazioniElenco[i].getArrAnnoRiferimentoVO()[j].getAnnoRiferimento());
		      if(anno != null)
		      {
		        if(tAnno == null)
		        {
		          tAnno = new TreeMap<Integer,String>(java.util.Collections.reverseOrder());
		        }
		       
		        if(tAnno.get(anno) == null)
		        {        
		          tAnno.put(anno, anno.toString());
		        }
		      }
		    }
		  }
    }
    request.setAttribute("tAnno", tAnno);
   
   //prima volta che entro
   /*if(Validator.isEmpty(idAnno) && Validator.isEmpty(regimeAssegnazioni))
   {
     idAnno = tAnno.get(tAnno.firstKey());
   }*/
    
  
  
    //Applico filtri se valorizzati
    if(Validator.isNotEmpty(idAnno))
    {
      
      //per assegnazione l'anno è presente una volta sola!!!!
      TreeMap<Integer,DittaUmaVO> hDittaUma = new TreeMap<Integer,DittaUmaVO>(); 
      for(int i=0;i<assegnazioniElenco.length;i++)
      { 
        boolean first = true;
        Vector<AnnoRiferimentoVO> vAnnoRiferimento = null;
        if(assegnazioniElenco[i].getArrAnnoRiferimentoVO() != null)
        {       
	        for(int j=0;j<assegnazioniElenco[i].getArrAnnoRiferimentoVO().length;j++)
	        {
	          boolean flagFiltroAnnoOK = true;
		        if(Validator.isNotEmpty(idAnno))
		        {
		          if(assegnazioniElenco[i].getArrAnnoRiferimentoVO()[j].getAnnoRiferimento()
		            ==  new Integer(idAnno).intValue())
		          {
		            flagFiltroAnnoOK = true;
		          }
		          else
		          {
		            flagFiltroAnnoOK = false;
		          }
		        }
		        
		        
		        if(flagFiltroAnnoOK)
		        {
		          if(first)
		          {
		            first = false;
		            hDittaUma.put(new Integer(i), assegnazioniElenco[i]);
		          }
		        
		          if(vAnnoRiferimento == null)
		          {
		            vAnnoRiferimento = new Vector<AnnoRiferimentoVO>();
		          }          
		          vAnnoRiferimento.add(assegnazioniElenco[i].getArrAnnoRiferimentoVO()[j]);	          
		        }	        
		      }
		    }
        
        if(vAnnoRiferimento != null)
        {
          DittaUmaVO dittaUmaVOtmp = hDittaUma.get(new Integer(i));
          dittaUmaVOtmp.setArrAnnoRiferimentoVO((AnnoRiferimentoVO[])vAnnoRiferimento.toArray(new AnnoRiferimentoVO[vAnnoRiferimento.size()]));
        }
        
      
      }        
      
      if(hDittaUma.size() > 0)
      {
        DittaUmaVO[] assegnazioniElencoFiltro =
          new DittaUmaVO[hDittaUma.size()];
        
        Iterator<DittaUmaVO> it = hDittaUma.values().iterator();
        int i=0;  
        while(it.hasNext())
        {
          assegnazioniElencoFiltro[i] = it.next();
          i++;
        }
        
        
        request.setAttribute("assegnazioniElencoFiltro", assegnazioniElencoFiltro);
          
      }
      else
      {
        request.setAttribute("messaggioErrore", AnagErrors.ERR_NO_BUONI_CARBURANTE_FILTRI);
      } 
      
    }
    else //Metto tutti perchè nessun filtro valorizzato
    {
      request.setAttribute("assegnazioniElencoFiltro", assegnazioniElenco);
    }
  
  }
  
  
  
  


  SolmrLogger.debug(this, " - assegnazioniElencoCtrl.jsp - FINE PAGINA");

 %>
 
 
 
 <jsp:forward page= "<%= assegnazioniElencoUrl %>" />

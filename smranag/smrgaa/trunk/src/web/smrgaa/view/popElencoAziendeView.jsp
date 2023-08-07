<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popElencoAziende.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
  
  int totalePagine;
  int pagCorrente;
  Integer currPage;

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Vector vectIdAnagAzienda = (Vector)session.getAttribute("listIdAzienda");
  Vector vectRange = (Vector)session.getAttribute("listRange");
  
  String provenienza = (String)request.getParameter("provenienza");
  
  htmpl.set("provenienza", provenienza);
  
  if(session.getAttribute("currPage")==null)
    pagCorrente=1;
  else
    pagCorrente = ((Integer)session.getAttribute("currPage")).intValue();
    
  if((vectIdAnagAzienda!=null) && (vectIdAnagAzienda.size() > 0))
  {
  
    if(!"NuovaIscrizione".equalsIgnoreCase(provenienza)
      && !"Comunicazione10RCessAcqu".equalsIgnoreCase(provenienza)
      && !"Comunicazione10RCessioni".equalsIgnoreCase(provenienza)
      && !"Comunicazione10RStocc".equalsIgnoreCase(provenienza) )
    {
      htmpl.newBlock("blkAbilitaScript");
    
    
	    htmpl.set("blkAbilitaScript.cuaaInserito","''");
	    htmpl.set("blkAbilitaScript.partitaIvaInserito","''");
	    htmpl.set("blkAbilitaScript.denominazioneInserito","''");
	  }
  
      
    htmpl.newBlock("blkNoErrore");
    
    htmpl.set("blkNoErrore.provenienza",provenienza);
    
    totalePagine=vectIdAnagAzienda.size()/SolmrConstants.NUM_MAX_ROWS_PAG;
    int resto = vectIdAnagAzienda.size()%SolmrConstants.NUM_MAX_ROWS_PAG;
    if(resto!=0)
      totalePagine+=1;
    htmpl.set("blkNoErrore.currPage",""+pagCorrente);
    htmpl.set("blkNoErrore.totPage",""+totalePagine);
    htmpl.set("blkNoErrore.numeroRecord",""+vectIdAnagAzienda.size());
    currPage = new Integer(pagCorrente);
    session.setAttribute("currPage",currPage);

    if(pagCorrente>1)
      htmpl.newBlock("blkNoErrore.bottoneIndietro");
    if(pagCorrente<totalePagine)
      htmpl.newBlock("blkNoErrore.bottoneAvanti");
  }
  else
  {
    htmpl.newBlock("blkAbilitaScript");
    String cuaa = request.getParameter("cuaa");
    if(Validator.isEmpty(cuaa))
    {
      cuaa = "''";
    }
    else
    {
      cuaa = "'"+cuaa+"'";
    }
    String partitaIva = request.getParameter("partitaIva");
    if(Validator.isEmpty(partitaIva))
    {
      partitaIva = "''";
    }
    else
    {
      partitaIva = "'"+partitaIva+"'";
    }
    String denominazione = request.getParameter("denominazione");
    if(Validator.isEmpty(denominazione))
    {
      denominazione = "''";
    }
    else
    {
      denominazione = "'"+denominazione+"'";
    }
    htmpl.set("blkAbilitaScript.cuaaInserito",cuaa);
    htmpl.set("blkAbilitaScript.partitaIvaInserito",partitaIva);
    htmpl.set("blkAbilitaScript.denominazioneInserito",denominazione);    
  }

  if(vectRange!=null && vectRange.size()>0)
  {
    Long idAziendaSelezionata = (Long)session.getAttribute("idAziendaSelezionata");
    for(int i=0; i<vectRange.size();i++)
    {
      AnagAziendaVO aaVO = (AnagAziendaVO)vectRange.elementAt(i);
      htmpl.newBlock("blkNoErrore.rigaAnagrafica");
      htmpl.set("blkNoErrore.rigaAnagrafica.idAzienda",""+aaVO.getIdAzienda());
      
      if(Validator.isNotEmpty(idAziendaSelezionata)) {
        if(idAziendaSelezionata.compareTo(aaVO.getIdAzienda()) == 0) {
          htmpl.set("blkNoErrore.rigaAnagrafica.checked","checked=\"checked\"");
        }
      }      
      
      htmpl.set("blkNoErrore.rigaAnagrafica.denominazione",aaVO.getDenominazione());
      htmpl.set("blkNoErrore.rigaAnagrafica.cuaa",aaVO.getCUAA());
      htmpl.set("blkNoErrore.rigaAnagrafica.partitaIVA",aaVO.getPartitaIVA());
      if(aaVO.getDescComune()!= null && !aaVO.getDescComune().equals(""))
        htmpl.set("blkNoErrore.rigaAnagrafica.comune",aaVO.getDescComune());
      else if(aaVO.getSedelegCittaEstero()!=null&&!aaVO.getSedelegCittaEstero().equals(""))
        htmpl.set("blkNoErrore.rigaAnagrafica.comune",aaVO.getSedelegCittaEstero());
      if(aaVO.getSedelegProv()!=null&&!aaVO.getSedelegProv().equals(""))
        htmpl.set("blkNoErrore.rigaAnagrafica.prov",aaVO.getSedelegProv());
      else if(aaVO.getSedelegEstero()!=null && !aaVO.getSedelegEstero().equals(""))
        htmpl.set("blkNoErrore.rigaAnagrafica.prov",aaVO.getSedelegEstero());
      htmpl.set("blkNoErrore.rigaAnagrafica.indirizzo",aaVO.getSedelegIndirizzo());
      
      htmpl.set("blkNoErrore.rigaAnagrafica.cap",aaVO.getSedelegCAP());
    }
  }
  else
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", AnagErrors.NESSUNA_AZIENDA_TROVATA);
  }
 	
  
  
  out.print(htmpl.text());
  
%>

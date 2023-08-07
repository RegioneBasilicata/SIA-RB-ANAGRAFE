<%@ page language="java" contentType="text/html" isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoCCVO" %>

<%
  //AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/conti_correnti.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  HtmplUtil.setErrors(htmpl, (ValidationErrors) request.getAttribute("errors"), request, application);
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  if ("si".equals(request.getAttribute("storico")))
  {
    htmpl.set("checkedStorico", "checked");
  }

  Vector<ContoCorrenteVO> conti=(Vector<ContoCorrenteVO>)request.getAttribute("conti");
  RitornoPraticheCCAgriservVO ritornoAgriservCCVO = 
    (RitornoPraticheCCAgriservVO)request.getAttribute("ritornoAgriservCCVO");
    
    
  String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
    
    
  int size=0;
  if (conti!=null) size=conti.size();
  if(size > 0) 
  {
    htmpl.newBlock("blkSiContiCorrenti");
    for(int i=0;i<size;i++) 
    {
      ContoCorrenteVO conto=(ContoCorrenteVO) conti.get(i);
      htmpl.newBlock("blkSiContiCorrenti.blkElenco");

      if (i==0)
      {
        htmpl.set("blkSiContiCorrenti.blkElenco.checked","checked");
      }
      htmpl.set("blkSiContiCorrenti.blkElenco.idContoCorrente",checkNull(conto.getIdContoCorrente()));
      htmpl.set("blkSiContiCorrenti.blkElenco.banca",conto.getDenominazioneBanca());
      htmpl.set("blkSiContiCorrenti.blkElenco.filiale",conto.getDenominazioneSportello());
      htmpl.set("blkSiContiCorrenti.blkElenco.codpaese",conto.getCodPaese());
      htmpl.set("blkSiContiCorrenti.blkElenco.cctrl",conto.getCifraCtrl());
      htmpl.set("blkSiContiCorrenti.blkElenco.cin",conto.getCin());
      htmpl.set("blkSiContiCorrenti.blkElenco.abi",conto.getAbi());
      htmpl.set("blkSiContiCorrenti.blkElenco.cab",conto.getCab());
      String flagContoGf = "No";
      if(Validator.isNotEmpty(conto.getFlagContoGf()) 
        && "S".equalsIgnoreCase(conto.getFlagContoGf()))
      {
        flagContoGf = "Si";
      }
      htmpl.set("blkSiContiCorrenti.blkElenco.flagContoGf", flagContoGf);
      
      String flagContoVincolato = "No";
      if(Validator.isNotEmpty(conto.getFlagContoVincolato()) 
        && "S".equalsIgnoreCase(conto.getFlagContoVincolato()))
      {
        flagContoVincolato = "Si";
      }
      htmpl.set("blkSiContiCorrenti.blkElenco.flagContoVincolato", flagContoVincolato);
      
      htmpl.set("blkSiContiCorrenti.blkElenco.numeroconto",conto.getNumeroContoCorrente());
      htmpl.set("blkSiContiCorrenti.blkElenco.intestatario",conto.getIntestazione());
      String dataInizioValidita="";
      try 
      {
        dataInizioValidita=DateUtils.formatDate(conto.getDataInizioValiditaContoCorrente());
      }
      catch(Exception e) {
        // Data inizio validità vuota
      }
      htmpl.set("blkSiContiCorrenti.blkElenco.dataInizioValidita",dataInizioValidita);
      
      Date dataEstinzione=conto.getDataEstinzione();
      if (dataEstinzione!=null)
      {
        htmpl.set("blkSiContiCorrenti.blkElenco.dataEstinzione",DateUtils.formatDate(dataEstinzione));
      }
      else if(conto.getDataFineValiditaContoCorrente() != null)
      {
        htmpl.set("blkSiContiCorrenti.blkElenco.dataEstinzione",DateUtils.formatDate(conto.getDataFineValiditaContoCorrente()));
      }
            
      // Icona relativa alla validazione del conto corrente
      if(Validator.isNotEmpty(conto.getflagValidato()) 
        && conto.getflagValidato().equalsIgnoreCase("S"))
      {
        htmpl.set("blkSiContiCorrenti.blkElenco.validazioneCC", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.MSG_VALIDAZIONE_CC_OK, ""}), null);
      }
      else if(Validator.isNotEmpty(conto.getflagValidato()) 
        && conto.getflagValidato().equalsIgnoreCase("N"))
      {
        htmpl.set("blkSiContiCorrenti.blkElenco.validazioneCC", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.MSG_VALIDAZIONE_CC_KO, ""}), null);              
      }
      else
      {
        htmpl.set("blkSiContiCorrenti.blkElenco.validazioneCC", "");
      }
      
      
      if((ritornoAgriservCCVO != null) && (ritornoAgriservCCVO.getHPraticheCC() != null))
      {  
        HashMap<Long,PraticaProcedimentoCCVO[]> hPraticheCC = ritornoAgriservCCVO.getHPraticheCC();     
        if(hPraticheCC.get(conto.getIdContoCorrente()) !=null) 
        {
          htmpl.newBlock("blkSiContiCorrenti.blkElenco.blkImmaginePratica");
          htmpl.set("blkSiContiCorrenti.blkElenco.blkImmaginePratica.idContoCorrente", 
            checkNull(conto.getIdContoCorrente()));
        }
        else 
        {
          htmpl.newBlock("blkSiContiCorrenti.blkElenco.blkNoImmaginePratica");
        }
      }
      else 
      {
        htmpl.newBlock("blkSiContiCorrenti.blkElenco.blkNoImmaginePratica");
      }
      
      
    }
    
    if(ritornoAgriservCCVO != null)
    {
      writeBlkErroriAgriserv(htmpl, ritornoAgriservCCVO.getErrori());
    }
    
  }
  else 
  {
    htmpl.newBlock("blkNoContiCorrenti");
  }
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }

  out.print(htmpl.text());

%>
<%!
  private String checkNull(Object obj) {
    return obj==null?"":obj.toString();
  }
  
  public void writeBlkErroriAgriserv(Htmpl htmpl, String errori[])
  {
    int len=errori==null?0:errori.length;
    if (len==0)
    {
      return;
    }
    StringBuffer sb=new StringBuffer();
    for(int i=0;i<len;++i)
    {
      if (i>0)
      {
        sb.append("<br/>");
      }
      sb.append(errori[i]);
    }
    htmpl.newBlock("blkEAgriServ");
    htmpl.set("blkEAgriServ.erroriAgriserv",sb.toString(),null);
  }
  
%>

<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.TipoReportVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/scelta_stampa.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");  
  Vector<TipoReportVO> vTipoReport = (Vector<TipoReportVO>)request.getAttribute("vTipoReport");
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  String codiceReport = request.getParameter("tipoStampa");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();    
  String[] orderBy = {SolmrConstants.ORDER_BY_ANNO_CONSISTENZA_DESC, SolmrConstants.ORDER_BY_DATA_CONSISTENZA_DESC};
  ConsistenzaVO[] elencoDichCons = anagFacadeClient.getListDichiarazioniConsistenzaByIdAziendaVarCat(anagAziendaVO.getIdAzienda(), orderBy);
  
  boolean trovatoVarCat = false;
  if(elencoDichCons.length > 0)
  {
    trovatoVarCat = true;
  }
  
  //non ho trovato notifiche quindi nn faccio vedere il report
  int z = 0;
  if(!trovatoVarCat)
  {
    for(int i=0;i<vTipoReport.size();i++)
    {
      TipoReportVO tipoReportVO = vTipoReport.get(i);
      if(SolmrConstants.COD_STAMPA_VARIAZIONI_CATASTALI.equalsIgnoreCase(tipoReportVO.getCodiceReport()))
      {
        z=i;
        break;
      }
    }    
    vTipoReport.remove(z);
  }
  
  if(vTipoReport != null)
  {
    //nel caso di check su stampa nuovo fascicolo e passaggio a dich consistenza...
    boolean tovatoAlmenoUno = false;
    for(int i=0;i<vTipoReport.size();i++)
    {
      TipoReportVO tipoReportVO = vTipoReport.get(i);
      if(Validator.isNotEmpty(codiceReport))
      {
        if(codiceReport.equalsIgnoreCase(tipoReportVO.getCodiceReport()))
        {
          tovatoAlmenoUno = true;
          break;
        }
      }
    }
        
    for(int i=0;i<vTipoReport.size();i++)
    {
      TipoReportVO tipoReportVO = vTipoReport.get(i);
      htmpl.newBlock("blkTipoReport");
      if(i==0)
      {
        htmpl.newBlock("blkTipoReport.blkFirstTipoReport");
        htmpl.set("blkTipoReport.blkFirstTipoReport.numRighe", ""+vTipoReport.size());
      } 
      
      htmpl.set("blkTipoReport.codiceReport", ""+tipoReportVO.getCodiceReport());
      htmpl.set("blkTipoReport.descrizioneReport", ""+tipoReportVO.getDescrizione());
      
      if(Validator.isNotEmpty(codiceReport))
      {
        if(codiceReport.equalsIgnoreCase(tipoReportVO.getCodiceReport()))
        {
          tovatoAlmenoUno = true;
          htmpl.set("blkTipoReport.checked","checked=\"checked\"", null);
          if(SolmrConstants.COD_STAMPA_VARIAZIONI_CATASTALI.equalsIgnoreCase(tipoReportVO.getCodiceReport()))
          {
            pianoRiferimentoUtils.popolaComboPianoRiferimentoVarCat(htmpl, anagFacadeClient,
				    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
				    ruoloUtenza);
          }
          else
          {
            pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
            anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
            ruoloUtenza);
          }
        }
        else if(!tovatoAlmenoUno)
		    {
		      if(i==0)
		      {
		        htmpl.set("blkTipoReport.checked","checked=\"checked\"", null);
		        pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
		          anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
		          ruoloUtenza);
		      }
		    }
      }
      else
      {
        if(i==0)
        {
          htmpl.set("blkTipoReport.checked","checked=\"checked\"", null);
          pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
            anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
            ruoloUtenza);
        }
      }
    
    }
    
    
    
    
    
  }
  
  
    

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>

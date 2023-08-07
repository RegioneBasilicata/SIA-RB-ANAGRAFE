<%@ page language="java"
         contentType="text/html"
%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoBioVO" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "allevamentiDettCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String url = "/view/allevamentiDettView.jsp";
  String actionUrl = "../layout/allevamenti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione dettaglio allevamenti."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  ConsistenzaVO consistenzaVO = null;

  Long idAllevamento = null;

  if(request.getParameter("elenco.x") != null) {
    url = "/layout/allevamenti.htm";
  }
  else if(request.getParameter("indietro") != null) {
    url = "/view/allevamentiView.jsp";
  }

  if(request.getParameter("radiobutton") != null && !"".equals(request.getParameter("radiobutton")))
  {
    idAllevamento = new Long((String)request.getParameter("radiobutton"));
  }
  if(idAllevamento != null)
  {
    AllevamentoAnagVO all = anagFacadeClient.getAllevamento(idAllevamento);
    //Imposto i seguenti campi per poterli visualizzare correttamente
    //come cifre decimali
    try
    {
      BigDecimal b1=new BigDecimal(all.getSuperficieLettieraPermanente());
      BigDecimal b2=new BigDecimal(all.getAltezzaLettieraPermanente());
      b1=b1.multiply(b2);
      String volume=StringUtils.parseDoubleFieldTwoDecimal(b1.toString());
      request.setAttribute("volume",volume);
    }
    catch(Exception e) {}


    if (Validator.isNotEmpty(all.getSuperficieLettieraPermanente()))
      all.setSuperficieLettieraPermanente(StringUtils.parseSuperficieField(all.getSuperficieLettieraPermanente()));

    if (Validator.isNotEmpty(all.getAltezzaLettieraPermanente()))
      all.setAltezzaLettieraPermanente(StringUtils.parseDoubleFieldTwoDecimal(all.getAltezzaLettieraPermanente()));

    
    Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = 
      gaaFacadeClient.getTipiSottoCategoriaAllevamento(all.getIdAllevamentoLong().longValue());
    
    Vector<StabulazioneTrattamento> stabulazioniTrattamenti = 
      gaaFacadeClient.getStabulazioni(all.getIdAllevamentoLong().longValue(),false);
      
    Vector<AllevamentoAcquaLavaggio> vAllevamentoAcquaLavaggio = 
      gaaFacadeClient.getElencoAllevamentoAcquaLavaggio(all.getIdAllevamentoLong().longValue());
    
    String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
    Date dataInserimentoDichiarazione = null;
    if(Validator.isNotEmpty(idDichiarazioneConsistenza) 
      && !idDichiarazioneConsistenza.equalsIgnoreCase("-1"))
    {  
      consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(new Long(idDichiarazioneConsistenza));
      dataInserimentoDichiarazione = consistenzaVO.getDataInserimentoDichiarazione();
    }
    request.setAttribute("dataInserimentoDichiarazione", dataInserimentoDichiarazione);
    
    
    Vector<AllevamentoBioVO> vAllevamentiBioVO = gaaFacadeClient
        .getAllevamentiBio(dataInserimentoDichiarazione, all.getIdAllevamentoLong().longValue());
    request.setAttribute("vAllevamentiBioVO", vAllevamentiBioVO);
    
    
    
    Date dataDettaglioAllevamenti = null;
    try 
    {
      Date parametroAllSuGnps = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ALL_SU_GNPS);
      request.setAttribute("parametroAllSuGnps", parametroAllSuGnps);
    
      dataDettaglioAllevamenti = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DETT_ALLEVAMENTI);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - allevamentiDettCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DETT_ALLEVAMENTI+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("dataDettaglioAllevamenti", dataDettaglioAllevamenti);
    
    
    
    
    
    
    request.setAttribute("allevamento", all);
    request.setAttribute("sottoCategorieAllevamenti", sottoCategorieAllevamenti);
    request.setAttribute("stabulazioniTrattamenti",stabulazioniTrattamenti);
    request.setAttribute("vAllevamentoAcquaLavaggio", vAllevamentoAcquaLavaggio);
    request.setAttribute("idAnno", request.getParameter("idAnno"));
  }
%>
 <jsp:forward page ="<%=url%>" />

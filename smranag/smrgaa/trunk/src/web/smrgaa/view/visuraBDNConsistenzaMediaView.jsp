<%@page language="java" contentType="text/html" isErrorPage="true"%>

<%@page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.teramo.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.smranags.wsbridge.dto.bdn.WBConsistenzaStatAllevamentoVO" %>
<%@ page import="it.csi.smranags.wsbridge.dto.bdn.WBUbaCensimentoOvino2012VO" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/visuraBDNConsistenzaMedia.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
  String messaggioErroreBovini = (String) request.getAttribute("messaggioErroreBovini");
  Vector<WBConsistenzaStatAllevamentoVO>  vConsistenzaBovini 
    = (Vector<WBConsistenzaStatAllevamentoVO>)request.getAttribute("vConsistenzaBovini");
  HashMap<String,String> hCodDescSpecie = (HashMap<String,String>)request.getAttribute("hCodDescSpecie");       
  String messaggioErroreCaprini = (String) request.getAttribute("messaggioErroreCaprini");
  Vector<WBUbaCensimentoOvino2012VO>  vConsistenzaCaprini 
    = (Vector<WBUbaCensimentoOvino2012VO>)request.getAttribute("vConsistenzaCaprini"); 

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

 
  if (Validator.isNotEmpty(messaggioErroreBovini)) 
  {
    htmpl.newBlock("blkBoviniErrore");
    htmpl.set("blkBoviniErrore.messaggio", messaggioErroreBovini, null);
  }
  
  if (vConsistenzaBovini !=null)
  {
    htmpl.newBlock("blkBoviniConsistenza");
    //Tutto ok, procedo quindi con la visualizzazione degli allevamenti        
    for (int i=0;i<vConsistenzaBovini.size();i++)
    {
      htmpl.newBlock("blkBoviniConsistenza.blkAllevamenti");
      WBConsistenzaStatAllevamentoVO consAll = vConsistenzaBovini.get(i);
      
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.anno", ""+DateUtils.extractYearFromDate(consAll.getDataMaxCalcoloCapi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.codiceAzienda", consAll.getCodiceAzienda());
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.descrizioneSpecie", hCodDescSpecie.get(consAll.getCodiceSpecie()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.maschiBov0_12", Formatter.formatDouble(consAll.getMediaMaschiBov0_12Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.femmineBov0_12", Formatter.formatDouble(consAll.getMediaFemmineBov0_12Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.maschiBov12_24", Formatter.formatDouble(consAll.getMediaMaschiBov12_24Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.femmineBov12_24", Formatter.formatDouble(consAll.getMediaFemmineBov12_24Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.maschiBuf0_12", Formatter.formatDouble(consAll.getMediaMaschiBuf0_12Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.femmineBuf0_12", Formatter.formatDouble(consAll.getMediaFemmineBuf0_12Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.maschiBuf12_24", Formatter.formatDouble(consAll.getMediaMaschiBuf12_24Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.femmineBuf12_24", Formatter.formatDouble(consAll.getMediaFemmineBuf12_24Mesi()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.mediaTotali", Formatter.formatDouble(consAll.getMediaCapiTotali()));
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.proprietario", consAll.getCodFiscProprietario());
      htmpl.set("blkBoviniConsistenza.blkAllevamenti.detentore", consAll.getCodFiscDetentore());
    }
  }
  
  
  if (Validator.isNotEmpty(messaggioErroreCaprini)) 
  {
    htmpl.newBlock("blkErroreCaprini");
    htmpl.set("blkErroreCaprini.messaggio", messaggioErroreCaprini, null);
  }
  
  if (vConsistenzaCaprini !=null)
  {
    htmpl.newBlock("blkCapriniConsistenza");
    //Tutto ok, procedo quindi con la visualizzazione degli allevamenti        
    for (int i=0;i<vConsistenzaCaprini.size();i++)
    {
      htmpl.newBlock("blkCapriniConsistenza.blkAllevamenti");
      
      WBUbaCensimentoOvino2012VO consAll = vConsistenzaCaprini.get(i);
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.anno", ""+DateUtils.extractYearFromDate(consAll.getDataCensimento()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.codiceAzienda", consAll.getCodiceAzienda());
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.descrizioneSpecie", consAll.getDescrizioneSpecie());
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiOviAdulti", Formatter.formatDouble(consAll.getOviniMaschiAdulti()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiOviAdultiLib", Formatter.formatDouble(consAll.getOviniMaschiAdultiLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiOviRimonta", Formatter.formatDouble(consAll.getOviniMaschiRimonta()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiOviRimontaLib", Formatter.formatDouble(consAll.getOviniMaschiRimontaLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineOviAdulti", Formatter.formatDouble(consAll.getOviniFemmineAdulte()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineOviAdultiLib", Formatter.formatDouble(consAll.getOviniFemmineAdulteLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineOviRimonta", Formatter.formatDouble(consAll.getOviniFemmineRimonta()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineOviRimontaLib", Formatter.formatDouble(consAll.getOviniFemmineRimontaLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.mediaTotaleAgnelliMacello", Formatter.formatDouble(consAll.getAgnelliMacellatiTotali()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.mediaTotaleOvini", Formatter.formatDouble(consAll.getOviniCapiTotali()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiCapAdulti", Formatter.formatDouble(consAll.getCapriniMaschiAdulti()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiCapAdultiLib", Formatter.formatDouble(consAll.getCapriniMaschiAdultiLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiCapRimonta", Formatter.formatDouble(consAll.getCapriniMaschiRimonta()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.maschiCapRimontaLib", Formatter.formatDouble(consAll.getCapriniMaschiRimontaLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineCapAdulti", Formatter.formatDouble(consAll.getCapriniFemmineAdulte()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineCapAdultiLib", Formatter.formatDouble(consAll.getCapriniFemmineAdulteLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineCapRimonta", Formatter.formatDouble(consAll.getCapriniFemmineRimonta()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.femmineCapRimontaLib", Formatter.formatDouble(consAll.getCapriniFemmineRimontaLib()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.mediaTotaleCapriniMacello", Formatter.formatDouble(consAll.getCaprettiMacellatiTotali()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.mediaTotaleCaprini", Formatter.formatDouble(consAll.getCapriniCapiTotali()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.dataCensimento", DateUtils.formatDateNotNull(consAll.getDataCensimento()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.dataComunicazioneAutorita", DateUtils.formatDateNotNull(consAll.getDataComunicazioneAutorita()));
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.proprietario", consAll.getCodFiscProprietario());
      htmpl.set("blkCapriniConsistenza.blkAllevamenti.detentore", consAll.getCodFiscDetentore());
    }
  }
  


  

%>
<%= htmpl.text()%>



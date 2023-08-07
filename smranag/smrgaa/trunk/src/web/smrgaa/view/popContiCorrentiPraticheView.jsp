<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.*" %>

<%@ page import="it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoCCVO"%>

<%!

  public static final String ORDINAMENTO_ASCENDENTE  = "ordine crescente";
  public static final String ORDINAMENTO_DISCENDENTE = "ordine decrescente";
  public static final String CLASSE_ORDINAMENTO_ASC  = "su";
  public static final String CLASSE_ORDINAMENTO_DESC = "giu";

 %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/popContiCorrentiPratiche.htm");
  
  Htmpl htmpl = new Htmpl(layout);
  
  %>
    	<%@include file = "/view/remoteInclude.inc" %>
  <%
  
  
  
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  RitornoPraticheCCAgriservVO ritornoPraticheCCAgriservVO
    = (RitornoPraticheCCAgriservVO)session.getAttribute("ritornoPraticheCCAgriservVO");
  

  HashMap hPratiche = ritornoPraticheCCAgriservVO.getHPraticheCC();
  if(hPratiche != null)
  {
    //se sono qui la hash ha un solo elemento!!!
    Iterator it = hPratiche.values().iterator();
    PraticaProcedimentoCCVO[] arrPraticaProcedimentoCCVO 
      = (PraticaProcedimentoCCVO[])it.next();
      
    htmpl.set("numeroRecord",""+arrPraticaProcedimentoCCVO.length);
     
    for(int i=0;i<arrPraticaProcedimentoCCVO.length;i++)
    {
      htmpl.newBlock("rigaPratiche");
      htmpl.set("rigaPratiche.tipologia", arrPraticaProcedimentoCCVO[i].getDescrizione());
      htmpl.set("rigaPratiche.numeroPratica", arrPraticaProcedimentoCCVO[i].getNumeroPratica());
      htmpl.set("rigaPratiche.annoCampagna", ""+arrPraticaProcedimentoCCVO[i].getAnnoCampagna());
      htmpl.set("rigaPratiche.stato", arrPraticaProcedimentoCCVO[i].getDescrizioneStato());
      htmpl.set("rigaPratiche.dataValiditaStato", 
        DateUtils.formatDateNotNull(arrPraticaProcedimentoCCVO[i].getDataValiditaStato()));    
    }
  
  }


 	
  writeBlkErroriAgriserv(htmpl, ritornoPraticheCCAgriservVO.getErrori());
  writeOrdinamentoCorrente(htmpl, request);  
  out.print(htmpl.text());
  
%>

<%!
  
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
  
  
  public void writeOrdinamentoCorrente(Htmpl htmpl, HttpServletRequest request)
  {
    String ordine = request.getParameter("ordine");
    
    if ("anno".equalsIgnoreCase(ordine))
    {
      // Ordinamento per anno
      writeOrdinamento(htmpl, "descOrdineAnno", "ordineAnnoAsc", "classOrdineAnno", !"false"
          .equals(request.getParameter("ascendente")));
    }
    else
    {
      // Ordinamento per anno
      writeOrdinamento(htmpl, "descOrdineAnno", "ordineAnnoAsc", "classOrdineAnno", false);
    }
  }

  public void writeOrdinamento(Htmpl htmpl, String segnapostoDesc, String segnapostoName, String segnapostoClass, boolean asc)
  {
    htmpl.set(segnapostoDesc, asc ? ORDINAMENTO_ASCENDENTE : ORDINAMENTO_DISCENDENTE);
    htmpl.set(segnapostoName, asc ? "false" : "true");
    htmpl.set(segnapostoClass, asc ? CLASSE_ORDINAMENTO_ASC : CLASSE_ORDINAMENTO_DESC);
  }
  
%>

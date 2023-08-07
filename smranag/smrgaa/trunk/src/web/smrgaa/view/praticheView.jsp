<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.comune.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/pratiche.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
      <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Vector<ProcedimentoAziendaVO> elencoPratiche = (Vector<ProcedimentoAziendaVO>)request.getAttribute("elencoPratiche");
  Hashtable elencoAmmCompetenza = (Hashtable)request.getAttribute("elencoAmmCompetenza");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  String procedimento = request.getParameter("procedimento");
  TreeMap descProcedimento = (TreeMap)session.getAttribute("filtriPraticheProcedimenti");
  TreeMap elencoCUAA = (TreeMap)session.getAttribute("elencoCUAA");
  String cuaa = request.getParameter("cuaa");
  String idAnno = request.getParameter("idAnno");
  ArrayList anni = (ArrayList)session.getAttribute("filtriPraticheAnniCombo");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 
  if(elencoCUAA != null && elencoCUAA.size() > 0) 
    {
      htmpl.newBlock("blkCUAA");
      Iterator iteraCUAA = elencoCUAA.entrySet().iterator();
          
      while(iteraCUAA.hasNext()) 
      {
        Map.Entry e = (Map.Entry)iteraCUAA.next();
        if (e != null) 
        {
          String key = ""+e.getKey();
          String value = (String)e.getValue();
          
          htmpl.set("blkCUAA.cuaaValue", key);
          htmpl.set("blkCUAA.cuaaDesc", value);
        
          if(Validator.isNotEmpty(cuaa) && cuaa.compareTo(key.toString()) == 0) 
          {
            htmpl.set("blkCUAA.selectedCUAA", "selected=\"selected\"");
          }
          //Se idAnno = null significa che è la prima volta che entro,
          //quindi devo far vedere il CUAA dell'azienda corrente
          else if(idAnno==null)
          {
             if(anagAziendaVO.getCUAA().equals(value))
             {
               htmpl.set("blkCUAA.selectedCUAA", "selected=\"selected\"");
             }
          }
        }
      }
    }
  
  if(descProcedimento != null && descProcedimento.size() > 0) 
    {
      htmpl.newBlock("blkProcedimento");
      Iterator iteraProcedimento = descProcedimento.entrySet().iterator();
      while(iteraProcedimento.hasNext()) 
      {
        Map.Entry e = (Map.Entry)iteraProcedimento.next();
        if (e != null) 
        {
          String key = (String)e.getKey();
          htmpl.set("blkProcedimento.procedimentoDesc", key);
          Long value = null;
          if (key != null && key.length() > 0) 
          {
            value = (Long)e.getValue();
            htmpl.set("blkProcedimento.procedimentoValue", value.toString());
          }
        
          if(Validator.isNotEmpty(procedimento) && procedimento.compareTo(value.toString()) == 0) {
            htmpl.set("blkProcedimento.selectedProc", "selected=\"selected\"");
          }
        }
      }
    }
    
    if(anni != null) 
    {
      htmpl.newBlock("blkAnni");
      String inizializzaAnno = (String)request.getAttribute("inizializzaAnno");
      
      for(int i=0;i<anni.size();i++)
      {
        Integer anno = (Integer)anni.get(i);
        htmpl.set("blkAnni.anno", anno.toString());
        
        if(Validator.isNotEmpty(idAnno) && idAnno.compareTo(anno.toString()) == 0) {
          htmpl.set("blkAnni.selectedAnno", "selected=\"selected\"");
        }
        else if(!Validator.isNotEmpty(idAnno) && Validator.isNotEmpty(inizializzaAnno))
        { //Se inizializzaAnno è diverso da null significa che è la prima volta che entro,
          //quindi devo far vedere l'anno corrente
          if(i==0) // prima occorrenza = anno corrente
          {
            htmpl.set("blkAnni.selectedAnno", "selected=\"selected\"");
          }
        }
        
      }
    }
 
 

  if(elencoPratiche != null && elencoPratiche.size() > 0) {
    htmpl.newBlock("blkPresenzaPratiche");
    
    
    Iterator<ProcedimentoAziendaVO> iteraPratiche = elencoPratiche.iterator();
    while(iteraPratiche.hasNext()) {
      ProcedimentoAziendaVO procedimentoAziendaVO = (ProcedimentoAziendaVO)iteraPratiche.next();
      TipoProcedimentoVO tipoProcedimentoVO = procedimentoAziendaVO.getTipoProcedimentoVO();
      AmmCompetenzaVO ammCompetenzaVO = null;
      htmpl.newBlock("blkPresenzaPratiche.blkElencoPratiche");
      htmpl.set("blkPresenzaPratiche.blkElencoPratiche.descrizioneEstesa", tipoProcedimentoVO.getDescrizioneEstesa());
      htmpl.set("blkPresenzaPratiche.blkElencoPratiche.numeroPratica", procedimentoAziendaVO.getNumeroPratica());
      htmpl.set("blkPresenzaPratiche.blkElencoPratiche.descrizione", procedimentoAziendaVO.getDescrizione());
      htmpl.set("blkPresenzaPratiche.blkElencoPratiche.note", procedimentoAziendaVO.getNote());
      htmpl.set("blkPresenzaPratiche.blkElencoPratiche.descrizioneStato", procedimentoAziendaVO.getDescrizioneStato());
      if(Validator.isNotEmpty(procedimentoAziendaVO.getDataValiditaStato())) {
        htmpl.set("blkPresenzaPratiche.blkElencoPratiche.dataValiditaStato", DateUtils.formatDate(procedimentoAziendaVO.getDataValiditaStato()));
      }
      if(procedimentoAziendaVO.getExIdAmmCompetenza() != null) 
      {
        ammCompetenzaVO = (AmmCompetenzaVO)elencoAmmCompetenza.get(procedimentoAziendaVO.getExIdAmmCompetenza().toString());
        if(ammCompetenzaVO !=null)
        {
          htmpl.set("blkPresenzaPratiche.blkElencoPratiche.amministrazioneCompetenza", ammCompetenzaVO.getDescrizione());
        }
      }
    }
  }
  else {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", messaggioErrore);
  }


%>
<%= htmpl.text()%>

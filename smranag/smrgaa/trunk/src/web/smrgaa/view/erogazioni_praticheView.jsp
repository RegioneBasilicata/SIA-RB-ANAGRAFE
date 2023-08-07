<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.sigop.dto.services.PagamentoErogatoVO" %>
<%@ page import="it.csi.sigop.dto.services.RecuperoPregressoVO" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/erogazioni_pratiche.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  String messaggioErroreRecupero = (String)request.getAttribute("messaggioErroreRecupero");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  PagamentoErogatoVO[] arrPagamentoErogatoVO = 
    (PagamentoErogatoVO[])session.getAttribute("arrPagamentoErogatoVO");
  
    
  String settore = request.getParameter("settore");
  String idAnnoPagamento = request.getParameter("idAnnoPagamento");
  String idAnnoCampagna = request.getParameter("idAnnoCampagna");
  String regimeErogazioni = request.getParameter("regimeErogazioni");
  TreeMap<Integer,String> tAnnoPagamento = (TreeMap<Integer,String>)request.getAttribute("tAnnoPagamento");
  TreeMap<Integer,String> tAnnoCampagna = (TreeMap<Integer,String>)request.getAttribute("tAnnoCampagna");
 
 
  if(arrPagamentoErogatoVO != null && arrPagamentoErogatoVO.length > 0)
  {
    htmpl.newBlock("blkSiErogazioni");
    TreeMap<String, String> tSettore = null;
    
    for(int i=0;i<arrPagamentoErogatoVO.length;i++)
    {      
      
      if(arrPagamentoErogatoVO[i].getDescSettore() != null)
      {
        if(tSettore == null)
        {
          tSettore = new TreeMap<String, String>();
        }
        
        if(tSettore.get(arrPagamentoErogatoVO[i].getDescSettore()) == null)
        {      
          tSettore.put(arrPagamentoErogatoVO[i].getDescSettore(),
            arrPagamentoErogatoVO[i].getDescSettore());
        }        
      }      
    }
    
    if(tSettore != null) 
    {
      Iterator iteraSettore = tSettore.keySet().iterator();
      while(iteraSettore.hasNext()) 
      {
        String desc = (String)iteraSettore.next();
        htmpl.newBlock("blkSiErogazioni.blkElencoSettore");
        htmpl.set("blkSiErogazioni.blkElencoSettore.codiceSettore", desc);
        htmpl.set("blkSiErogazioni.blkElencoSettore.descrizioneSettore", desc);
        
        if(Validator.isNotEmpty(settore) && settore.equalsIgnoreCase(desc)) 
        {
          htmpl.set("blkSiErogazioni.blkElencoSettore.selected", "selected=\"selected\"", null);
        }
        
      }
    }
    
    
    if(tAnnoPagamento != null) 
    {
      Iterator<String> iteraAnno = tAnnoPagamento.values().iterator();
      int i=0;
      while(iteraAnno.hasNext()) 
      {
        String anno = (String)iteraAnno.next();
        htmpl.newBlock("blkSiErogazioni.blkElencoAnnoPagamento");
        htmpl.set("blkSiErogazioni.blkElencoAnnoPagamento.anno", anno);
        
        if(Validator.isNotEmpty(idAnnoPagamento) && idAnnoPagamento.equalsIgnoreCase(anno)) 
        {
          htmpl.set("blkSiErogazioni.blkElencoAnnoPagamento.selected", "selected=\"selected\"", null);
        }
        else if(Validator.isEmpty(idAnnoPagamento) && Validator.isEmpty(regimeErogazioni))
        {
          if(i==0)
          {
            htmpl.set("blkSiErogazioni.blkElencoAnnoPagamento.selected", "selected=\"selected\"", null);
          }
        }
        i++;
      }
    }
    
    if(tAnnoCampagna != null) 
    {
      Iterator<String> iteraAnno = tAnnoCampagna.values().iterator();
      int i=0;
      while(iteraAnno.hasNext()) 
      {
        String anno = iteraAnno.next();
        htmpl.newBlock("blkSiErogazioni.blkElencoAnnoCampagna");
        htmpl.set("blkSiErogazioni.blkElencoAnnoCampagna.anno", anno);
        
        if(Validator.isNotEmpty(idAnnoCampagna) && idAnnoCampagna.equalsIgnoreCase(anno)) 
        {
          htmpl.set("blkSiErogazioni.blkElencoAnnoCampagna.selected", "selected=\"selected\"", null);
        }
        else if(Validator.isEmpty(idAnnoCampagna) && Validator.isEmpty(regimeErogazioni))
        {
          if(i==0)
          {
            htmpl.set("blkSiErogazioni.blkElencoAnnoCampagna.selected", "selected=\"selected\"", null);
          }
        }
        i++;
      }
    }      
 
  }
  
  
  
 
  PagamentoErogatoVO[] pagamentoErogatoElenco = 
    (PagamentoErogatoVO[])request.getAttribute("pagamentoErogatoElenco");
  
  TreeMap<String,PagamentoErogatoVO> tTotali = null;
  HashMap<String,Vector<PagamentoErogatoVO>> tSingoli = null;
  BigDecimal totImportoLordo = new BigDecimal(0);
  BigDecimal totImportoRecupero = new BigDecimal(0);  
  BigDecimal totImportoNetto = new BigDecimal(0);    
  if(pagamentoErogatoElenco != null)
  {
    htmpl.newBlock("blkElencoPagamenti");
    htmpl.set("blkElencoPagamenti.numeroRecord",""+pagamentoErogatoElenco.length);
    
    for(int i=0;i<pagamentoErogatoElenco.length;i++)
    {
      String key = "";
      
      if(pagamentoErogatoElenco[i].getAnnoCampagna() !=null)
      {
        key += ""+pagamentoErogatoElenco[i].getAnnoCampagna();
      }
      
      if(Validator.isNotEmpty(key))
      {
        key += "_";
      }
      key += pagamentoErogatoElenco[i].getNumeroDomanda();
      
      
      if(Validator.isNotEmpty(key))
      {
        key += "_";
      }
      if(pagamentoErogatoElenco[i].getAnnoDisposizionePagamento() !=null)
      {
        key += ""+pagamentoErogatoElenco[i].getAnnoDisposizionePagamento();
      }
      
      if(Validator.isNotEmpty(key))
      {
        key += "_";
      }      
      
      
      if(tSingoli == null)
      {
        tSingoli = new HashMap<String,Vector<PagamentoErogatoVO>>();
      }
      
      if(tSingoli.get(key) !=null)
      {
        Vector<PagamentoErogatoVO> vPagamenti = (Vector<PagamentoErogatoVO>)tSingoli.get(key);
        tSingoli.remove(key);
        vPagamenti.add(pagamentoErogatoElenco[i]);
        tSingoli.put(key, vPagamenti);
      }
      else
      {
        Vector<PagamentoErogatoVO> vPagamenti = new Vector<PagamentoErogatoVO>();     
        vPagamenti.add(pagamentoErogatoElenco[i]);
        tSingoli.put(key, vPagamenti);      
      }
      
      if(tTotali == null)
      {
        tTotali = new TreeMap<String,PagamentoErogatoVO>();      
      }
      
      if(tTotali.get(key) !=null)
      {
        PagamentoErogatoVO pagVO = tTotali.get(key);
        tTotali.remove(key);
        BigDecimal importoLordo = pagVO.getImportoLordo();
        importoLordo = importoLordo.add(pagamentoErogatoElenco[i].getImportoLordo()); 
        pagVO.setImportoLordo(importoLordo);
        BigDecimal importoRecupero = pagVO.getImportoRecupero();
        importoRecupero = importoRecupero.add(pagamentoErogatoElenco[i].getImportoRecupero());
        pagVO.setImportoRecupero(importoRecupero);
        BigDecimal importoNetto =  pagVO.getImportoNetto();
        importoNetto = importoNetto.add(pagamentoErogatoElenco[i].getImportoNetto());
        pagVO.setImportoNetto(importoNetto);
        tTotali.put(key, pagVO);
      }
      else
      {
        PagamentoErogatoVO clonePagamentoErogatoVO = clonePagamento(pagamentoErogatoElenco[i]);
        tTotali.put(key, clonePagamentoErogatoVO);
      }
      
      totImportoLordo = totImportoLordo.add(pagamentoErogatoElenco[i].getImportoLordo());
      totImportoRecupero = totImportoRecupero.add(pagamentoErogatoElenco[i].getImportoRecupero());
      totImportoNetto = totImportoNetto.add(pagamentoErogatoElenco[i].getImportoNetto()); 
      
      
    }
    
    int cont = 1;
    if(tTotali != null)
    {
      Iterator<String> iteraTotali = tTotali.keySet().iterator();
      while(iteraTotali.hasNext()) 
      {
        htmpl.newBlock("blkTotale");
        htmpl.set("blkElencoPagamenti.blkTotale.aggancio","tr"+cont);
        String key = (String)iteraTotali.next();
        PagamentoErogatoVO pagVO = tTotali.get(key);
        htmpl.set("blkElencoPagamenti.blkTotale.numeroDomanda", pagVO.getNumeroDomanda());
        if(pagVO.getAnnoCampagna() != null)
        {
          htmpl.set("blkElencoPagamenti.blkTotale.annoCampagna", ""+pagVO.getAnnoCampagna());
        }
        
        if(pagVO.getAnnoDisposizionePagamento() != null)
        {
          htmpl.set("blkElencoPagamenti.blkTotale.annoDisposizionePagamento", ""+pagVO.getAnnoDisposizionePagamento());
        }
                
        htmpl.set("blkElencoPagamenti.blkTotale.ammCompetenza", pagVO.getDescAmmCompetenza());
        htmpl.set("blkElencoPagamenti.blkTotale.descrizioneSettore", pagVO.getDescSettore());
        
        htmpl.set("blkElencoPagamenti.blkTotale.importoLordoParziale", 
          Formatter.formatDouble2Separator(pagVO.getImportoLordo()));        
        htmpl.set("blkElencoPagamenti.blkTotale.importoRecuperoParziale", 
          Formatter.formatDouble2Separator(pagVO.getImportoRecupero()));
        htmpl.set("blkElencoPagamenti.blkTotale.importoNettoParziale",   
          Formatter.formatDouble2Separator(pagVO.getImportoNetto()));
          
          
        Vector<PagamentoErogatoVO> vPagamentiSingoli = (Vector<PagamentoErogatoVO>)tSingoli.get(key);
        for(int j=0;j<vPagamentiSingoli.size();j++)
        {
          PagamentoErogatoVO pagSingVO = vPagamentiSingoli.get(j);
          htmpl.newBlock("blkSingolo");
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.aggancio","tr"+cont);
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.numeroDomanda", pagSingVO.getNumeroDomanda());
          if(pagVO.getAnnoCampagna() != null)
          {
            htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.annoCampagna", ""+pagSingVO.getAnnoCampagna());
          }        
          
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.ammCompetenza", pagVO.getDescAmmCompetenza());
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.descrizioneSettore", 
            pagSingVO.getDescSettore());      
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.importoLordoParziale", 
            Formatter.formatDouble2Separator(pagSingVO.getImportoLordo()));        
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.importoRecuperoParziale", 
            Formatter.formatDouble2Separator(pagSingVO.getImportoRecupero()));
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.importoNettoParziale",   
            Formatter.formatDouble2Separator(pagSingVO.getImportoNetto()));
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.intervento", pagSingVO.getDescIntervento());
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.tipoPagamento",
            pagSingVO.getTipoPagamento());
          String decreto = pagSingVO.getNumeroDecreto()+" - "+DateUtils.formatDate(pagSingVO.getDataDecreto());
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.decreto", decreto);
          String mandato = pagSingVO.getNumeroMandato()+" - "+DateUtils.formatDate(pagSingVO.getDataMandato()); 
          htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.mandato", mandato);
          
          if(pagSingVO.getDataDisposizionePagamento() != null)
          {
            htmpl.set("blkElencoPagamenti.blkTotale.blkSingolo.dataPagamento",
              DateUtils.formatDate(pagSingVO.getDataDisposizionePagamento()));
          }   
        }  
          
        cont++;
      }
      
      
      htmpl.set("blkElencoPagamenti.totImportoLordo",
        Formatter.formatDouble2Separator(totImportoLordo));
      htmpl.set("blkElencoPagamenti.totImportoRecupero",
        Formatter.formatDouble2Separator(totImportoRecupero));
      htmpl.set("blkElencoPagamenti.totImportoNetto",
        Formatter.formatDouble2Separator(totImportoNetto));
    
    }
    
    
    
  
  
  }
  
  RecuperoPregressoVO[] recuperoPregressoElenco = (RecuperoPregressoVO[])request.getAttribute("recuperoPregressoElenco"); 
  if((recuperoPregressoElenco != null)
    && (recuperoPregressoElenco.length > 0))
  {
    htmpl.newBlock("blkElencoRecupero");
    for(int i=0;i<recuperoPregressoElenco.length;i++)
    {
      htmpl.newBlock("blkElencoRecupero.blkRecupero");
      String numeroDomanda = "";
      if(Validator.isNotEmpty(recuperoPregressoElenco[i].getNumeriDomanda())
        && (recuperoPregressoElenco[i].getNumeriDomanda().length > 0))
      {
        for(int j=0;j<recuperoPregressoElenco[i].getNumeriDomanda().length;j++)
        {
          numeroDomanda += recuperoPregressoElenco[i].getNumeriDomanda()[j]+"<br>";
        }
      }
      htmpl.set("blkElencoRecupero.blkRecupero.numeroDomanda", numeroDomanda, null);
      String decreto = recuperoPregressoElenco[i].getNumeroDecreto()+" - "+DateUtils.formatDate(recuperoPregressoElenco[i].getDataDecreto());
      htmpl.set("blkElencoRecupero.blkRecupero.decreto", decreto);
      htmpl.set("blkElencoRecupero.blkRecupero.enteCreditore", recuperoPregressoElenco[i].getEnteCreditore());
      String normeTrasgredite = "";
      if(Validator.isNotEmpty(recuperoPregressoElenco[i].getNormeTrasgredite())
        && (recuperoPregressoElenco[i].getNormeTrasgredite().length > 0))
      {
        for(int j=0;j<recuperoPregressoElenco[i].getNormeTrasgredite().length;j++)
        {
          normeTrasgredite += recuperoPregressoElenco[i].getNormeTrasgredite()[j]+"<br>";
        }
      }
      htmpl.set("blkElencoRecupero.blkRecupero.normeTrasgredite", normeTrasgredite, null);      
      htmpl.set("blkElencoRecupero.blkRecupero.numeroScheda", recuperoPregressoElenco[i].getNumeroScheda());
      htmpl.set("blkElencoRecupero.blkRecupero.importoRecupero",
        Formatter.formatDouble2Separator(recuperoPregressoElenco[i].getImportoRecupero()));
    }
  }

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  if(Validator.isNotEmpty(messaggioErroreRecupero)) {
    htmpl.newBlock("blkErroreRecupero");
    htmpl.set("blkErroreRecupero.messaggioErroreRecupero", messaggioErroreRecupero);
  }
  
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>

<%!


  PagamentoErogatoVO clonePagamento(PagamentoErogatoVO pagVO)
  {
    PagamentoErogatoVO pagInt = new PagamentoErogatoVO();
    pagInt.setImportoLordo(pagVO.getImportoLordo());
    pagInt.setImportoNetto(pagVO.getImportoNetto());
    pagInt.setImportoRecupero(pagVO.getImportoRecupero());
    pagInt.setNumeroDomanda(pagVO.getNumeroDomanda());
    pagInt.setDescAmmCompetenza(pagVO.getDescAmmCompetenza());
    pagInt.setAnnoDisposizionePagamento(pagVO.getAnnoDisposizionePagamento());
    pagInt.setAnnoCampagna(pagVO.getAnnoCampagna());
    pagInt.setDescSettore(pagVO.getDescSettore());
    
    return pagInt;
  }


 %>


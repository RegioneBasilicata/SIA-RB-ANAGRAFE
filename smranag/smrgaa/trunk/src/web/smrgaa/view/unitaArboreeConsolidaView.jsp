<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeConsolida.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	
	String percorsoErrori = null;
  if(pathToFollow.equalsIgnoreCase("rupar")) {
    percorsoErrori = "/css_rupar/agricoltura/im/";
  }
  else if(pathToFollow.equalsIgnoreCase("sispie")){
    percorsoErrori = "/css/agricoltura/im/";
  }
  else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
	  percorsoErrori = "/css/agricoltura/im/";
  }
 	
	CompensazioneAziendaVO compensazioneAziendaVO = (CompensazioneAziendaVO)request.getAttribute("compensazioneAziendaVO");
	Vector<RiepilogoCompensazioneVO> vPostAllinea = (Vector<RiepilogoCompensazioneVO>)request.getAttribute("vPostAllinea");
	Vector<RiepilogoCompensazioneVO> vDirittiVitati = (Vector<RiepilogoCompensazioneVO>)request.getAttribute("vDirittiVitati");
	Vector<Vector<DirittoCompensazioneVO>> vDirittiCalcolati = (Vector<Vector<DirittoCompensazioneVO>>)request.getAttribute("vDirittiCalcolati");
	
  if(Validator.isNotEmpty(compensazioneAziendaVO))
  {
    htmpl.newBlock("blkDati");
    htmpl.set("blkDati.dataCalcoloCompensazione", 
      DateUtils.formatDateTimeNotNull(compensazioneAziendaVO.getDataCalcoloCompensazione()));
    htmpl.set("blkDati.dataAllineamentoCompensazione", 
      DateUtils.formatDateTimeNotNull(compensazioneAziendaVO.getDataAllineamentoCompensazione()));
    htmpl.set("blkDati.dataConsolidamentoGis", 
      DateUtils.formatDateTimeNotNull(compensazioneAziendaVO.getDataConsolidamentoGis()));
    htmpl.set("blkDati.dataDichiarazione", 
      DateUtils.formatDateTimeNotNull(compensazioneAziendaVO.getDataDichiarazione()));
    htmpl.set("blkDati.dataUltimaElaborazioneIsole", 
      DateUtils.formatDateTimeNotNull(compensazioneAziendaVO.getDataUltimaElaborazioneIsole()));
      
    if(vPostAllinea != null)
    {
      htmpl.newBlock("blkDati.blkSupPostAllinea");
      for(int i=0;i<vPostAllinea.size();i++)
      {
        htmpl.newBlock("blkDati.blkSupPostAllinea.blkElencoSupPostAllinea");
        RiepilogoCompensazioneVO riepilogoCompensazioneVO = vPostAllinea.get(i);
        String destProd = "["+riepilogoCompensazioneVO.getCodDestProd()+"] "+riepilogoCompensazioneVO.getDescDestProd();
        htmpl.set("blkDati.blkSupPostAllinea.blkElencoSupPostAllinea.destinazioneProduttiva", destProd);
        String vitigno = "["+riepilogoCompensazioneVO.getCodVitigno()+"] "+riepilogoCompensazioneVO.getDescVitigno();
        htmpl.set("blkDati.blkSupPostAllinea.blkElencoSupPostAllinea.vitigno", vitigno);
        htmpl.set("blkDati.blkSupPostAllinea.blkElencoSupPostAllinea.idoneita", riepilogoCompensazioneVO.getIdoneita());
        htmpl.set("blkDati.blkSupPostAllinea.blkElencoSupPostAllinea.supVitLavorazione", 
          Formatter.formatDouble4(riepilogoCompensazioneVO.getSupVitLavorazione()));
        htmpl.set("blkDati.blkSupPostAllinea.blkElencoSupPostAllinea.supPostAllinea",
          Formatter.formatDouble4(riepilogoCompensazioneVO.getSupPastAllinea()));
      }
    }
    
    if(vDirittiVitati != null)
    {
      htmpl.newBlock("blkDati.blkDirittiGenerati");
      for(int i=0;i<vDirittiVitati.size();i++)
      {
        htmpl.newBlock("blkDati.blkDirittiGenerati.blkElencoDirittiGenerati");
        RiepilogoCompensazioneVO riepilogoCompensazioneVO = vDirittiVitati.get(i);
        String destProd = "["+riepilogoCompensazioneVO.getCodDestProd()+"] "+riepilogoCompensazioneVO.getDescDestProd();
        htmpl.set("blkDati.blkDirittiGenerati.blkElencoDirittiGenerati.destinazioneProduttiva", destProd);
        String vitigno = "["+riepilogoCompensazioneVO.getCodVitigno()+"] "+riepilogoCompensazioneVO.getDescVitigno();
        htmpl.set("blkDati.blkDirittiGenerati.blkElencoDirittiGenerati.vitigno", vitigno);
        htmpl.set("blkDati.blkDirittiGenerati.blkElencoDirittiGenerati.supVitata", 
          Formatter.formatDouble4(riepilogoCompensazioneVO.getSupVitata()));
          
        String vitignoParticolare = "NO";
        if(riepilogoCompensazioneVO.getVitignoParticolare() == 1)
        {
          vitignoParticolare = "SI";
        } 
        htmpl.set("blkDati.blkDirittiGenerati.blkElencoDirittiGenerati.vitignoPart", vitignoParticolare);
      }
    }
    else
    {
      htmpl.newBlock("blkDati.blkNoDirittiGenerati");
      String msg = "L'azienda non ha mai consolidato la propria situazione vitivinicola";
      if(Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
      {
        msg = "Non sono stati generati diritti viticoli";
      }
      htmpl.set("blkDati.blkNoDirittiGenerati.messaggio", msg);
    }
    
    
    if(vDirittiCalcolati != null)
    {
      htmpl.newBlock("blkLegenda");
      htmpl.newBlock("blkDati.blkDirittiCalcolati");
      int numRigheTot = 0;
      for(int i=0;i<vDirittiCalcolati.size();i++)
      {
        numRigheTot += vDirittiCalcolati.get(i).size();
      }
      
      BigDecimal totSupDirittiComp = new BigDecimal(0);
      BigDecimal totSupDirittiIpo = new BigDecimal(0);
      BigDecimal totSupDirittiParticEstirpo = new BigDecimal(0);
      BigDecimal totSupFinaleAssegnata = new BigDecimal(0);
      
      for(int i=0;i<vDirittiCalcolati.size();i++)
      {
        Vector<DirittoCompensazioneVO> vDirittiCalcolatiVarieta = vDirittiCalcolati.get(i);
        for(int j=0;j<vDirittiCalcolatiVarieta.size();j++)
        {
	        htmpl.newBlock("blkDati.blkDirittiCalcolati.blkElencoDirittiCalcolati");
	        DirittoCompensazioneVO dirCalcVO = vDirittiCalcolatiVarieta.get(j);
	        if((i==0) && (j==0))
	        {
	          String blk = "blkDati.blkDirittiCalcolati.blkElencoDirittiCalcolati.blkPrimoBlocco";
	          htmpl.newBlock(blk);
	          htmpl.set(blk+".numRigheTot", ""+numRigheTot);
	          htmpl.set(blk+".numRigheVar", ""+vDirittiCalcolatiVarieta.size());
	          htmpl.set(blk+".descVarieta", dirCalcVO.getDescVarieta());
	          htmpl.set(blk+".supDirittiComp", Formatter.formatDouble4(dirCalcVO.getSupDirittiComp()));
	          if(Validator.isNotEmpty(dirCalcVO.getSupDirittiComp()))
	            totSupDirittiComp = totSupDirittiComp.add(dirCalcVO.getSupDirittiComp());
	          htmpl.set(blk+".descTipologiaVino", dirCalcVO.getDescTipologiaVino());
	          htmpl.set(blk+".supDirittiIpo", Formatter.formatDouble4(dirCalcVO.getSupDirittiIpo()));
	          if(Validator.isNotEmpty(dirCalcVO.getSupDirittiIpo()))
              totSupDirittiIpo = totSupDirittiIpo.add(dirCalcVO.getSupDirittiIpo());
	          
	          BigDecimal supDirittiParticEstirpo = new BigDecimal(0);
	          if("S".equalsIgnoreCase(dirCalcVO.getFlagVitignoParticolare()))
	          {
	            if(Validator.isNotEmpty(dirCalcVO.getSupVitiEstirpoVar()))
	              supDirittiParticEstirpo = supDirittiParticEstirpo.add(dirCalcVO.getSupVitiEstirpoVar());
              
              totSupDirittiParticEstirpo = totSupDirittiParticEstirpo.add(supDirittiParticEstirpo);
	          }
	          htmpl.set(blk+".supDirittiParticEstirpo", Formatter.formatDouble4(supDirittiParticEstirpo));
	          
	          BigDecimal supAltriDirittiEstirpo = new BigDecimal(0);
	          if(Validator.isNotEmpty(dirCalcVO.getSupVitiEstirpoAz()))
	            supAltriDirittiEstirpo = supAltriDirittiEstirpo.add(dirCalcVO.getSupVitiEstirpoAz());
	          if(Validator.isNotEmpty(dirCalcVO.getSupVitiEstirpoVarblocAz()))
              supAltriDirittiEstirpo = supAltriDirittiEstirpo.subtract(dirCalcVO.getSupVitiEstirpoVarblocAz());
            htmpl.set(blk+".supAltriDirittiEstirpo", Formatter.formatDouble4(supAltriDirittiEstirpo));
            
            htmpl.set(blk+".supUvDuplicate", Formatter.formatDouble4(dirCalcVO.getSupUvDuplicate()));
	          
	          BigDecimal supDirittiCondivisi = new BigDecimal(0);
	          if("S".equalsIgnoreCase(dirCalcVO.getDirittiCondivTempo()))
	          {
	            supDirittiCondivisi = dirCalcVO.getSupDirittiCondivisi();
	          }
	          htmpl.set(blk+".supDirittiCondivisi", Formatter.formatDouble4(supDirittiCondivisi));
	          
	          htmpl.set(blk+".supPostCons", Formatter.formatDouble4(dirCalcVO.getSupPostCons()));
	          
	          htmpl.set(blk+".supFinaleAssegnata", Formatter.formatDouble4(dirCalcVO.getSupFinaleAssegnata()));
	          if(Validator.isNotEmpty(dirCalcVO.getSupFinaleAssegnata()))
	            totSupFinaleAssegnata = totSupFinaleAssegnata.add(dirCalcVO.getSupFinaleAssegnata());
	        }
	        else if(j==0)
	        {
	          String blk = "blkDati.blkDirittiCalcolati.blkElencoDirittiCalcolati.blkSecondoBlocco";
            htmpl.newBlock(blk);
            htmpl.set(blk+".numRigheVar", ""+vDirittiCalcolatiVarieta.size());
            htmpl.set(blk+".descVarieta", dirCalcVO.getDescVarieta());
            htmpl.set(blk+".supDirittiComp", Formatter.formatDouble4(dirCalcVO.getSupDirittiComp()));
            if(Validator.isNotEmpty(dirCalcVO.getSupDirittiComp()))
              totSupDirittiComp = totSupDirittiComp.add(dirCalcVO.getSupDirittiComp());
            htmpl.set(blk+".descTipologiaVino", dirCalcVO.getDescTipologiaVino());
            htmpl.set(blk+".supDirittiIpo", Formatter.formatDouble4(dirCalcVO.getSupDirittiIpo()));
            if(Validator.isNotEmpty(dirCalcVO.getSupDirittiIpo()))
              totSupDirittiIpo = totSupDirittiIpo.add(dirCalcVO.getSupDirittiIpo());
            
            BigDecimal supDirittiParticEstirpo = new BigDecimal(0);
            if("S".equalsIgnoreCase(dirCalcVO.getFlagVitignoParticolare()))
            {
              
              if(Validator.isNotEmpty(dirCalcVO.getSupVitiEstirpoVar()))
                supDirittiParticEstirpo = supDirittiParticEstirpo.add(dirCalcVO.getSupVitiEstirpoVar());
              
              totSupDirittiParticEstirpo = totSupDirittiParticEstirpo.add(supDirittiParticEstirpo);
            }
            htmpl.set(blk+".supDirittiParticEstirpo", Formatter.formatDouble4(supDirittiParticEstirpo));
              
            
            htmpl.set(blk+".supFinaleAssegnata", Formatter.formatDouble4(dirCalcVO.getSupFinaleAssegnata()));
            if(Validator.isNotEmpty(dirCalcVO.getSupFinaleAssegnata()))
              totSupFinaleAssegnata = totSupFinaleAssegnata.add(dirCalcVO.getSupFinaleAssegnata());
	        }
	        else
	        {
	          String blk = "blkDati.blkDirittiCalcolati.blkElencoDirittiCalcolati.blkTerzoBlocco";
            htmpl.newBlock(blk);
            htmpl.set(blk+".descVarieta", dirCalcVO.getDescVarieta());
            htmpl.set(blk+".supDirittiComp", Formatter.formatDouble4(dirCalcVO.getSupDirittiComp()));
            htmpl.set(blk+".descTipologiaVino", dirCalcVO.getDescTipologiaVino());
            htmpl.set(blk+".supDirittiIpo", Formatter.formatDouble4(dirCalcVO.getSupDirittiIpo()));
            if(Validator.isNotEmpty(dirCalcVO.getSupDirittiIpo()))
              totSupDirittiIpo = totSupDirittiIpo.add(dirCalcVO.getSupDirittiIpo());
            htmpl.set(blk+".supFinaleAssegnata", Formatter.formatDouble4(dirCalcVO.getSupFinaleAssegnata()));
            if(Validator.isNotEmpty(dirCalcVO.getSupFinaleAssegnata()))
              totSupFinaleAssegnata = totSupFinaleAssegnata.add(dirCalcVO.getSupFinaleAssegnata());
	        }
	      }
        
      }
      
      //Totali
      htmpl.newBlock("blkDati.blkDirittiCalcolati.blkElencoDirittiCalcolati");
      String blk = "blkDati.blkDirittiCalcolati.blkElencoDirittiCalcolati.blkTotale";
      htmpl.newBlock(blk);
      htmpl.set(blk+".totSupDirittiComp", Formatter.formatDouble4(totSupDirittiComp));
      htmpl.set(blk+".totSupDirittiIpo", Formatter.formatDouble4(totSupDirittiIpo));
      htmpl.set(blk+".totSupDirittiParticEstirpo", Formatter.formatDouble4(totSupDirittiParticEstirpo));
      htmpl.set(blk+".totSupFinaleAssegnata", Formatter.formatDouble4(totSupFinaleAssegnata));
      
      
      
    }
    else
    {
      htmpl.newBlock("blkDati.blkNoDirittiCalcolati");
      String msg = "L'azienda non ha mai consolidato la propria situazione vitivinicola";
      if(Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
      {
        msg = "Non sono stati generati diritti viticoli";
      }
      htmpl.set("blkDati.blkNoDirittiCalcolati.messaggio", msg);
    }    
		
  }
  
  if(request.getAttribute("messaggio") != null)
  { 
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", (String)request.getAttribute("messaggio"));
  }
  
 
  
 
	

%>
<%= htmpl.text()%>


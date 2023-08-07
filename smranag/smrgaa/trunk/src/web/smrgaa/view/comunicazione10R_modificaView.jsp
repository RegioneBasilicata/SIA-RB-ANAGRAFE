<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.TipoCausaleEffluenteVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.TipoAcquaAgronomicaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.GenericQuantitaVO" %>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.TipoTrattamento" %>
<%@ page import="java.math.BigDecimal" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/comunicazione10R_modifica.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  //RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //Vettore di Comunicazione10RVO
  Vector<Comunicazione10RVO>  vCom10r = (Vector<Comunicazione10RVO>)session.getAttribute("vCom10r");
  //Vettore di EffluenteCesAcqVO
  Vector<EffluenteCesAcqVO> vCessioniAcquisizioni = (Vector<EffluenteCesAcqVO>)session.getAttribute("vCessioniAcquisizioni");
  //Vettore di EffluenteStocExtVO
  Vector<EffluenteStocExtVO> vStoccaggio = (Vector<EffluenteStocExtVO>)session.getAttribute("vStoccaggio");
  //Vettore di AcquaExtraVO
  Vector<AcquaExtraVO> vAcqueReflue = (Vector<AcquaExtraVO>)session.getAttribute("vAcqueReflue");
  //Vettore di GenericQuantitaVO
  Vector<GenericQuantitaVO> vVolumeSottogrigliato = (Vector<GenericQuantitaVO>)session.getAttribute("vVolumeSottogrigliato");
  //Vettore di GenericQuantitaVO
  Vector<GenericQuantitaVO> vVolumeRefluoAzienda = (Vector<GenericQuantitaVO>)session.getAttribute("vVolumeRefluoAzienda");
  //Vettore di EffluenteVO //trattamenti
  Vector<EffluenteVO> vEffluentiTratt = (Vector<EffluenteVO>)session.getAttribute("vEffluentiTratt");
  //Vettore di EffluenteCesAcqVO
  Vector<EffluenteCesAcqVO> vCessioni= (Vector<EffluenteCesAcqVO>)session.getAttribute("vCessioni");
  
  String flagAdesioneDerogaDb = (String)request.getAttribute("flagAdesioneDerogaDb");
  String flagModAdesioneDerogaDb = (String)request.getAttribute("flagModAdesioneDerogaDb");
  
  String operazione = request.getParameter("operazione");
  
  final Long CESSAZIONE = new Long(1);
  
  Vector<TipoCausaleEffluenteVO> vTipoCausaleEffluenti = (Vector<TipoCausaleEffluenteVO>)request.getAttribute("vTipoCausaleEffluenti");
  Vector<TipoEffluenteVO> vTipoEffluenti = (Vector<TipoEffluenteVO>)request.getAttribute("vTipoEffluenti");
  Vector<TipoTipologiaFabbricatoVO> vTipoFabbricato = (Vector<TipoTipologiaFabbricatoVO>)request.getAttribute("vTipoFabbricato");
  Vector<TipoAcquaAgronomicaVO> vTipoAcqueReflue = (Vector<TipoAcquaAgronomicaVO>)request.getAttribute("vTipoAcqueReflue");
  Vector<TipoTrattamento> vTrattamenti = (Vector<TipoTrattamento>)request.getAttribute("vTrattamenti");
  HashMap<Long,Vector<TipoEffluenteVO>> hTipoEffluentiTratt = (HashMap<Long,Vector<TipoEffluenteVO>>)request.getAttribute("hTipoEffluentiTratt");
  HashMap<Long,Vector<TipoEffluenteVO>> hTipoEffluentiCess = (HashMap<Long,Vector<TipoEffluenteVO>>)request.getAttribute("hTipoEffluentiCess");
  Vector<UteVO> elencoUte = (Vector<UteVO>)request.getAttribute("elencoUte");
 
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  HashMap<Integer,ValidationErrors> hElencoErroriVolSot = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriVolSot");
  HashMap<Integer,ValidationErrors> hElencoErroriVolRefAz = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriVolRefAz");
  HashMap<Integer,ValidationErrors> hElencoErroriCessAcqu = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriCessAcqu");
  HashMap<Integer,ValidationErrors> hElencoErroriTratt = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriTratt");
  HashMap<Integer,ValidationErrors> hElencoErroriCessioni = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriCessioni");
  HashMap<Integer,ValidationErrors> hElencoErroriStoc =
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriStoc");
  HashMap<Long,HashMap<Integer,ValidationErrors>> hElencoErroriEffGlobal = 
    (HashMap<Long,HashMap<Integer,ValidationErrors>>)request.getAttribute("hElencoErroriEffGlobal");
  HashMap<Integer,ValidationErrors> hElencoErroriAcqueReflue = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriAcqueReflue");
  HashMap<Integer,ValidationErrors> hElencoErroriNote = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriNote");
  HashMap<Integer,ValidationErrors> hElencoErroriAdesDer = 
    (HashMap<Integer,ValidationErrors>)request.getAttribute("hElencoErroriAdesDer");
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  //E' a nulla solo la prima volta che entro nella pagina
  //a regime è a true
  String regime = request.getParameter("regime");
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  if(vCom10r != null)
  {
    htmpl.newBlock("blkNoErr");
    //per alcuni campi che sono comuni a tutte le ute prendo
    //quelli della prima!!!
    Comunicazione10RVO com10Tmp = (Comunicazione10RVO)vCom10r.get(0);
    
    htmpl.set("blkNoErr.dataUltimoRicalcolo", DateUtils.formatDateTimeNotNull(com10Tmp.getDataRicalcolo()));
     
    if(errors != null)
    {
      String cuaaCessAcqu = request.getParameter("cuaaCessAcqu");
      String denominazioneCessAcqu = request.getParameter("denominazioneCessAcqu");
      String idAziendaCessAcqu = request.getParameter("idAziendaCessAcqu");
      String provinciaCessAcqu = request.getParameter("provinciaCessAcqu");
      String comuneCessAcqu = request.getParameter("comuneCessAcqu");
      String istatComuneCessAcqu = request.getParameter("istatComuneCessAcqu");
      
      if(Validator.isNotEmpty(cuaaCessAcqu)) {
        htmpl.set("blkNoErr.cuaaCessAcqu", cuaaCessAcqu.toUpperCase());
      }
      if(Validator.isNotEmpty(denominazioneCessAcqu)) {
        htmpl.set("blkNoErr.denominazioneCessAcqu", denominazioneCessAcqu.toUpperCase());
      }
      if(Validator.isNotEmpty(idAziendaCessAcqu)) {
        htmpl.set("blkNoErr.idAziendaCessAcqu", idAziendaCessAcqu);
      }
      if(Validator.isNotEmpty(provinciaCessAcqu)) {
        htmpl.set("blkNoErr.provinciaCessAcqu", provinciaCessAcqu.toUpperCase());
      }
      if(Validator.isNotEmpty(comuneCessAcqu)) {
        htmpl.set("blkNoErr.comuneCessAcqu", comuneCessAcqu.toUpperCase());
      }
      if(Validator.isNotEmpty(istatComuneCessAcqu)) {
        htmpl.set("blkNoErr.istatComuneCessAcqu", istatComuneCessAcqu);
      }
      
      
      String cuaaStocc = request.getParameter("cuaaStocc");
      String denominazioneStocc = request.getParameter("denominazioneStocc");
      String idAziendaStocc = request.getParameter("idAziendaStocc");
      String provinciaStocc = request.getParameter("provinciaStocc");
      String comuneStocc = request.getParameter("comuneStocc");
      String istatComuneStocc = request.getParameter("istatComuneStocc");
      
      if(Validator.isNotEmpty(cuaaStocc)) {
        htmpl.set("blkNoErr.cuaaStocc", cuaaStocc.toUpperCase());
      }
      if(Validator.isNotEmpty(denominazioneStocc)) {
        htmpl.set("blkNoErr.denominazioneStocc", denominazioneStocc.toUpperCase());
      }
      if(Validator.isNotEmpty(idAziendaStocc)) {
        htmpl.set("blkNoErr.idAziendaStocc", idAziendaStocc);
      }
      if(Validator.isNotEmpty(provinciaStocc)) {
        htmpl.set("blkNoErr.provinciaStocc", provinciaStocc.toUpperCase());
      }
      if(Validator.isNotEmpty(comuneStocc)) {
        htmpl.set("blkNoErr.comuneStocc", comuneStocc.toUpperCase());
      }
      if(Validator.isNotEmpty(istatComuneStocc)) {
        htmpl.set("blkNoErr.istatComuneStocc", istatComuneStocc);
      }
    }
    
    
    if(("S".equalsIgnoreCase(flagAdesioneDerogaDb) && "S".equalsIgnoreCase(flagModAdesioneDerogaDb))
      || ("S".equalsIgnoreCase(flagAdesioneDerogaDb) && "N".equalsIgnoreCase(flagModAdesioneDerogaDb)))
    {
      htmpl.newBlock("blkNoErr.blkAdesioneDeroga");
      
      
      if(elencoUte !=null)
      { 
        for(int j=0;j<elencoUte.size();j++)
        {
        
          htmpl.newBlock("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga");
          UteVO uteVO  = (UteVO)elencoUte.get(j);
          htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.descUteAdesioneDeroga", 
            uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+uteVO.getIndirizzo());
            
          htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.countAdesione",""+j);
          
          if("N".equalsIgnoreCase(flagModAdesioneDerogaDb))
          {
            htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.disabledSiAdesioneDeroga","disabled=\"true\"", null);
          }
          
          
          if(Validator.isNotEmpty(regime))
          { 
            String flagAdesioneDeroga = request.getParameter("flagAdesioneDeroga"+""+j); 
            if("N".equalsIgnoreCase(flagAdesioneDeroga))
            {  
              htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.flagAdesioneDerogaCheckedNo", "checked=\"checked\"", null);
            }
            else
            {
              htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.flagAdesioneDerogaCheckedSi", "checked=\"checked\"", null);
            }
          }
          else
          {
            for(int z=0;z<vCom10r.size();z++)
            {
              Comunicazione10RVO com10Adesione = (Comunicazione10RVO)vCom10r.get(z);
	            if(com10Adesione.getIdUte() ==  uteVO.getIdUte().longValue()) 
	            {
	              if("S".equalsIgnoreCase(com10Adesione.getAdesioneDeroga()))
					      {
					        htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.flagAdesioneDerogaCheckedSi", "checked=\"checked\"", null);
					      }
					      else
					      {
					        htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.flagAdesioneDerogaCheckedNo", "checked=\"checked\"", null);
					      }
					      break;
	            }
	          }
          }
          
          
          
          // GESTIONE ERRORI RELATIVI ALLA CONFERMA per adesione deroga
          if(hElencoErroriAdesDer != null) 
	        {
	          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriAdesDer.get(new Integer(j));
	          if(errorsRecord != null && errorsRecord.size() > 0) 
	          {
	            Iterator iter = htmpl.getVariableIterator();
	            while(iter.hasNext()) 
	            {
	              String key = (String)iter.next();
	              if(key.startsWith("err_")) 
	              {
	                String property = key.substring(4);
	                Iterator errorIterator = errorsRecord.get(property);
	                if(errorIterator != null) 
	                {
	                  ValidationError error = (ValidationError)errorIterator.next();
	                  htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.err_"+property,
	                    MessageFormat.format(htmlStringKO,
	                    new Object[] {
	                    pathErrori + "/"+ imko,
	                    "'"+jssp.process(error.getMessage())+"'",
	                    error.getMessage()}),
	                    null);
	                }
	              }
	            }
	          }
	        }
          
          
          
	        
        }       
        
      }     
    }
    
    
    if(vVolumeSottogrigliato != null)
    {
      String[] volumeSottoGrigliatoIn = request.getParameterValues("volumeSottoGrigliato");
      String[] idUteVolSotIn = request.getParameterValues("idUteVolSot");
      String[] volumeSottoGrigliato = null;
      String[] idUteVolSot = null;
      htmpl.newBlock("blkVolumeSottogrigliato");
      
      //hChkVolSot è valorizzato se sono nel cosa dell'elimina
      HashMap<Integer,Integer> hChkVolSot = (HashMap<Integer,Integer>)request.getAttribute("hChkVolSot");
      if(hChkVolSot !=null)
      {
        int size = vVolumeSottogrigliato.size();
        volumeSottoGrigliato = new String[size];
        idUteVolSot = new String[size];
        int j = 0;
        for(int i=0;i<volumeSottoGrigliatoIn.length;i++)
        {
          if(hChkVolSot.get(new Integer(i)) == null)
          {
            volumeSottoGrigliato[j] = volumeSottoGrigliatoIn[i];
            idUteVolSot[j] = idUteVolSotIn[i];
            j++;
          }
        }
      }
      else
      {
        volumeSottoGrigliato = volumeSottoGrigliatoIn;
        idUteVolSot = idUteVolSotIn;
      }
      
      
      for(int i=0;i<vVolumeSottogrigliato.size();i++)
      {   
        GenericQuantitaVO cqVO = (GenericQuantitaVO)vVolumeSottogrigliato.get(i);
        htmpl.newBlock("blkElencoVolumeSottogrigliato");
        htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.chkVolSot", new Integer(i).toString());
        
        
          
        if(elencoUte !=null)
        { 
          htmpl.newBlock("blkElencoUteVolSot");
          for(int j=0;j<elencoUte.size();j++)
          {
            UteVO uteVO  = (UteVO)elencoUte.get(j);
            htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.blkElencoUteVolSot.codiceUteVolSot", 
              uteVO.getIdUte().toString());
            htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.blkElencoUteVolSot.descrizioneUteVolSot", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") "+uteVO.getIndirizzo());
            
            if(elencoUte.size() == 1)
            {
              htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.blkElencoUteVolSot.selectedIdUteVolSot", "selected=\"selected\"");
            }
            else if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idUteVolSot) && (i < idUteVolSot.length))
              {  
                if(Validator.isNotEmpty(idUteVolSot[i]) && idUteVolSot[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
                {
                  htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.blkElencoUteVolSot.selectedIdUteVolSot", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(cqVO.getIdUte() ==  uteVO.getIdUte().longValue()) 
              {
                htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.blkElencoUteVolSot.selectedIdUteVolSot", "selected=\"selected\"");
              }
            }
          }
        }
        
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.volumeSottoGrigliato", 
            Formatter.formatDouble1(cqVO.getQuantita()));
        }
        else
        {
          if(Validator.isNotEmpty(volumeSottoGrigliato) && (i < volumeSottoGrigliato.length))
          {
            htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.volumeSottoGrigliato", 
              Formatter.formatDouble1(volumeSottoGrigliato[i]));
          }
        }
        
        
        
        // GESTIONE ERRORI RELATIVI ALLA CONFERMA
        if(hElencoErroriVolSot != null) 
        {
          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriVolSot.get(new Integer(i));
          if(errorsRecord != null && errorsRecord.size() > 0) 
          {
            Iterator iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator errorIterator = errorsRecord.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.err_"+property,
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(error.getMessage())+"'",
                    error.getMessage()}),
                    null);
                }
              }
            }
          }
        } 
      }
    }
    
    
    if(vVolumeRefluoAzienda != null)
    {
      String[] volumeRefluoAziendaIn = request.getParameterValues("volumeRefluoAzienda");
      String[] idUteVolRefAzIn = request.getParameterValues("idUteVolRefAz");
      String[] volumeRefluoAzienda = null;
      String[] idUteVolRefAz = null;
      htmpl.newBlock("blkVolumeRefluoAzienda");
      
      //hChkVolSot è valorizzato se sono nel cosa dell'elimina
      HashMap hChkVolRefAz = (HashMap)request.getAttribute("hChkVolRefAz");
      if(hChkVolRefAz !=null)
      {
        int size = vVolumeRefluoAzienda.size();
        volumeRefluoAzienda = new String[size];
        idUteVolRefAz = new String[size];
        int j = 0;
        for(int i=0;i<volumeRefluoAziendaIn.length;i++)
        {
          if(hChkVolRefAz.get(new Integer(i)) == null)
          {
            volumeRefluoAzienda[j] = volumeRefluoAziendaIn[i];
            idUteVolRefAz[j] = idUteVolRefAzIn[i];
            j++;
          }
        }
      }
      else
      {
        volumeRefluoAzienda = volumeRefluoAziendaIn;
        idUteVolRefAz = idUteVolRefAzIn;
      }
      
      
      for(int i=0;i<vVolumeRefluoAzienda.size();i++)
      {   
        GenericQuantitaVO cqVO = (GenericQuantitaVO)vVolumeRefluoAzienda.get(i);
        htmpl.newBlock("blkElencoVolumeRefluoAzienda");
        htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.chkVolRefAz", new Integer(i).toString());
        
        
          
        if(elencoUte !=null)
        { 
          htmpl.newBlock("blkElencoUteVolRefAz");
          for(int j=0;j<elencoUte.size();j++)
          {
            UteVO uteVO  = (UteVO)elencoUte.get(j);
            htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.blkElencoUteVolRefAz.codiceUteVolRefAz", 
              uteVO.getIdUte().toString());
            htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.blkElencoUteVolRefAz.descrizioneUteVolRefAz", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") "+uteVO.getIndirizzo());
            
            if(elencoUte.size() == 1)
            {
              htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.blkElencoUteVolRefAz.selectedIdUteVolRefAz", "selected=\"selected\"");
            }
            else if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idUteVolRefAz) && (i < idUteVolRefAz.length))
              {  
                if(Validator.isNotEmpty(idUteVolRefAz[i]) && idUteVolRefAz[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
                {
                  htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.blkElencoUteVolRefAz.selectedIdUteVolRefAz", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(cqVO.getIdUte() ==  uteVO.getIdUte().longValue()) 
              {
                htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.blkElencoUteVolRefAz.selectedIdUteVolRefAz", "selected=\"selected\"");
              }
            }
          }
        }
        
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.volumeRefluoAzienda", 
            Formatter.formatDouble1(cqVO.getQuantita()));
        }
        else
        {
          if(Validator.isNotEmpty(volumeRefluoAzienda) && (i < volumeRefluoAzienda.length))
          {
            htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.volumeRefluoAzienda", 
              Formatter.formatDouble1(volumeRefluoAzienda[i]));
          }
        }
        
        
        
        // GESTIONE ERRORI RELATIVI ALLA CONFERMA
        if(hElencoErroriVolRefAz != null) 
        {
          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriVolRefAz.get(new Integer(i));
          if(errorsRecord != null && errorsRecord.size() > 0) 
          {
            Iterator iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator errorIterator = errorsRecord.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.err_"+property,
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(error.getMessage())+"'",
                    error.getMessage()}),
                    null);
                }
              }
            }
          }
        } 
      }
    }
    
    
    
    int seqNote =0;
    String[] noteComunicazione = request.getParameterValues("noteComunicazione");
    for(int j=0;j<vCom10r.size();j++)
    {
      Comunicazione10RVO com10r = (Comunicazione10RVO)vCom10r.get(j);
      for(int k=0;k<elencoUte.size();k++)
      {
        UteVO uteVO = (UteVO)elencoUte.get(k);        
        if(uteVO.getIdUte().longValue() == com10r.getIdUte())
        {
          htmpl.newBlock("blkNote");  
          htmpl.set("blkNoErr.blkNote.descComuneUteNote", uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")");
          htmpl.set("blkNoErr.blkNote.indirizzoUteNote", uteVO.getIndirizzo());
          break;
        }
      }
          
      if(!Validator.isNotEmpty(regime)) 
      {
        htmpl.set("blkNoErr.blkNote.noteComunicazione", com10r.getNote());
      }
      else
      {            
        if(Validator.isNotEmpty(noteComunicazione)) {
          htmpl.set("blkNoErr.blkNote.noteComunicazione", noteComunicazione[seqNote]);
        }
      }         
          
      // GESTIONE ERRORI RELATIVI ALLA CONFERMA per le note
      if(hElencoErroriNote != null) 
      {
        ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriNote.get(new Integer(seqNote));
        if(errorsRecord != null && errorsRecord.size() > 0) 
        {
          Iterator iter = htmpl.getVariableIterator();
          while(iter.hasNext()) 
          {
            String key = (String)iter.next();
            if(key.startsWith("err_")) 
            {
              String property = key.substring(4);
              Iterator errorIterator = errorsRecord.get(property);
              if(errorIterator != null) 
              {
                ValidationError error = (ValidationError)errorIterator.next();
                htmpl.set("blkNoErr.blkNote.err_"+property,
                  MessageFormat.format(htmlStringKO,
                  new Object[] {
                  pathErrori + "/"+ imko,
                  "'"+jssp.process(error.getMessage())+"'",
                  error.getMessage()}),
                  null);
              }
            }
          }
        }
      }
      
      seqNote++;     
          
    }
      
    
    if(vAcqueReflue != null)
    {
      String[] volumeRefluoIn = request.getParameterValues("volumeRefluo");
      String[] idUteAcqueReflueIn = request.getParameterValues("idUteAcqueReflue");
      String[] idTipologiaAcquaAgronomicaIn = request.getParameterValues("idTipologiaAcquaAgronomica");
      String[] volumeRefluo = null;
      String[] idUteAcqueReflue = null;
      String[] idTipologiaAcquaAgronomica = null;
      htmpl.newBlock("blkAcqueReflue");
      
      //hChkAcqueReflue è valorizzato se sono nel cosa dell'elimina
      HashMap<Integer,Integer> hChkAcqueReflue = (HashMap<Integer,Integer>)request.getAttribute("hChkAcqueReflue");
      if(hChkAcqueReflue !=null)
      {
        int size = vAcqueReflue.size();
        volumeRefluo = new String[size];
        idTipologiaAcquaAgronomica = new String[size];
        int j = 0;
        for(int i=0;i<volumeRefluoIn.length;i++)
        {
          if(hChkAcqueReflue.get(new Integer(i)) == null)
          {
            volumeRefluo[j] = volumeRefluoIn[i];
            idUteAcqueReflue[j] = idUteAcqueReflueIn[i];
            idTipologiaAcquaAgronomica[j] = idTipologiaAcquaAgronomicaIn[i];
            j++;
          }
        }
      }
      else
      {
        volumeRefluo = volumeRefluoIn;
        idUteAcqueReflue = idUteAcqueReflueIn;
        idTipologiaAcquaAgronomica = idTipologiaAcquaAgronomicaIn;
      }
      
      
      for(int i=0;i<vAcqueReflue.size();i++)
      {   
        AcquaExtraVO acquVO = (AcquaExtraVO)vAcqueReflue.get(i);
        htmpl.newBlock("blkElencoAcqueReflue");
        htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.chkAcqueReflue", new Integer(i).toString());
        
        
        if(elencoUte !=null)
        { 
          htmpl.newBlock("blkElencoUteAcqueReflue");
          for(int j=0;j<elencoUte.size();j++)
          {
            UteVO uteVO  = (UteVO)elencoUte.get(j);
            htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoUteAcqueReflue.codiceUteAcqueReflue", 
              uteVO.getIdUte().toString());
            htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoUteAcqueReflue.descrizioneUteAcqueReflue", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") "+uteVO.getIndirizzo());
            
            if(elencoUte.size() == 1)
            {
              htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoUteAcqueReflue.selectedIdUteAcqueReflue", "selected=\"selected\"");
            }
            else if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idUteAcqueReflue) && (i < idUteAcqueReflue.length))
              {  
                if(Validator.isNotEmpty(idUteAcqueReflue[i]) && idUteAcqueReflue[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
                {
                  htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoUteAcqueReflue.selectedIdUteAcqueReflue", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(acquVO.getIdUte() ==  uteVO.getIdUte().longValue()) 
              {
                htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoUteAcqueReflue.selectedIdUteAcqueReflue", "selected=\"selected\"");
              }
            }
          }
        }
          
        if(vTipoAcqueReflue !=null)
        { 
          htmpl.newBlock("blkElencoTipoAcquaAgronomica");
          for(int j=0;j<vTipoAcqueReflue.size();j++)
          {
            TipoAcquaAgronomicaVO tipoVO  = (TipoAcquaAgronomicaVO)vTipoAcqueReflue.get(j);
            htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoTipoAcquaAgronomica.idAcquaAgronomica", 
              new Long(tipoVO.getIdAcquaAgronomica()).toString());
            htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoTipoAcquaAgronomica.descrizioneAcquaAgronomica", 
              tipoVO.getDescrizione());
            
            if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idTipologiaAcquaAgronomica) && (i < idTipologiaAcquaAgronomica.length))
              {  
                if(Validator.isNotEmpty(idTipologiaAcquaAgronomica[i]) && idTipologiaAcquaAgronomica[i].equalsIgnoreCase(new Long(tipoVO.getIdAcquaAgronomica()).toString())) 
                {
                  htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoTipoAcquaAgronomica.selectedAcquaAgronomica", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(acquVO.getIdAcquaAgronomica() ==  tipoVO.getIdAcquaAgronomica()) 
              {
                htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.blkElencoTipoAcquaAgronomica.selectedAcquaAgronomica", "selected=\"selected\"");
              }
            }
          }
        }
        
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.volumeRefluo", 
            Formatter.formatDouble1(acquVO.getVolumeRefluo()));
        }
        else
        {
          if(Validator.isNotEmpty(volumeRefluo) && (i < volumeRefluo.length))
          {
            htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.volumeRefluo", 
              Formatter.formatDouble1(volumeRefluo[i]));
          }
        }
        
        
        
        // GESTIONE ERRORI RELATIVI ALLA CONFERMA
        if(hElencoErroriAcqueReflue != null) 
        {
          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriAcqueReflue.get(new Integer(i));
          if(errorsRecord != null && errorsRecord.size() > 0) 
          {
            Iterator iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator errorIterator = errorsRecord.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.err_"+property,
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(error.getMessage())+"'",
                    error.getMessage()}),
                    null);
                }
              }
            }
          }
        } 
      }
    }
    
    if(vCessioniAcquisizioni != null)
    {
      htmpl.newBlock("blkCessAcqu");
      String[] quantitaCessAcquIn = request.getParameterValues("quantitaCessAcqu");
      String[] azotoDichiaratoCessAcquIn = request.getParameterValues("azotoDichiaratoCessAcqu");
      String[] idEffluenteIn = request.getParameterValues("idEffluente");
      String[] idUteCessAcquIn = request.getParameterValues("idUteCessAcqu");
      String[] chkStoccaggioCessAcquIn = request.getParameterValues("chkStoccaggioCessAcqu");
      
      String[] quantitaCessAcqu = null;
      String[] azotoDichiaratoCessAcqu = null;
      String[] idEffluente = null;
      String[] idUteCessAcqu = null;
      
      //hChkCessAcqu è valorizzato se sono nel cosa dell'elimina
      HashMap hChkCessAcqu = (HashMap)request.getAttribute("hChkCessAcqu");
      if(hChkCessAcqu !=null)
      {
        int size = vCessioniAcquisizioni.size();
        quantitaCessAcqu = new String[size];
        azotoDichiaratoCessAcqu = new String[size];
        //idCausaleEffluente = new String[size];
        idEffluente = new String[size];
        idUteCessAcqu = new String[size];
        
        int j = 0;
        for(int i=0;i<quantitaCessAcquIn.length;i++)
        {
          if(hChkCessAcqu.get(new Integer(i)) == null)
          {
            quantitaCessAcqu[j] = quantitaCessAcquIn[i];
            azotoDichiaratoCessAcqu[j] = azotoDichiaratoCessAcquIn[i];
            idEffluente[j] = idEffluenteIn[i];
            idUteCessAcqu[j] = idUteCessAcquIn[i];
            j++;
          }
        }
      }
      else
      {
        quantitaCessAcqu = quantitaCessAcquIn;
        azotoDichiaratoCessAcqu = azotoDichiaratoCessAcquIn;
        idEffluente = idEffluenteIn;
        idUteCessAcqu = idUteCessAcquIn;
      } 
      
      for(int i=0;i<vCessioniAcquisizioni.size();i++)
      {
        EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
        htmpl.newBlock("blkElencoCessAcqu");
        htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.chkCessAcqu",new Integer(i).toString());
        
        if(Validator.isNotEmpty(effVO.getIdAzienda()))
        {
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.idAziendaCessAcquElenco", 
            effVO.getIdAzienda().toString());
        }        
        htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.cuaaCessAcqu", 
          effVO.getCuaa());
        htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.denominazioneCessAcqu", 
          effVO.getDenominazione());
        htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.comuneCessAcqu", 
          effVO.getDescComune());
        htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.siglaProvCessAcqu", 
          effVO.getSglProv());
          
          
        if(elencoUte !=null)
        { 
          htmpl.newBlock("blkElencoUteCessAcqu");
          for(int j=0;j<elencoUte.size();j++)
          {
            UteVO uteVO  = (UteVO)elencoUte.get(j);
            htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoUteCessAcqu.codiceUteCessAcqu", 
              uteVO.getIdUte().toString());
            htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoUteCessAcqu.descrizioneUteCessAcqu", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") "+uteVO.getIndirizzo());
            
            if(elencoUte.size() == 1)
            {
              htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoUteCessAcqu.selectedIdUteCessAcqu", "selected=\"selected\"");
            }
            else if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idUteCessAcqu) && (i < idUteCessAcqu.length))
              {  
                if(Validator.isNotEmpty(idUteCessAcqu[i]) && idUteCessAcqu[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
                {
                  htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoUteCessAcqu.selectedIdUteCessAcqu", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(effVO.getIdUte() ==  uteVO.getIdUte().longValue()) 
              {
                htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoUteCessAcqu.selectedIdUteCessAcqu", "selected=\"selected\"");
              }
            }
          }
        }
        
        htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.chkStoccaggioCessAcqu",new Integer(i).toString());
        
        if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaCessAcqu"))
        {
          if(effVO.isFlagStoccaggioBl())
          {
            htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.checkedStoccaggioCessAcqu", "checked");
          }     
        }
        else
        {  
          if(Validator.isNotEmpty(regime))
          {          
            if(Validator.isNotEmpty(chkStoccaggioCessAcquIn))
            {
              for(int j=0;j<chkStoccaggioCessAcquIn.length;j++)
              {
                if(i == new Integer(chkStoccaggioCessAcquIn[j]).intValue())
                {
                  htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.checkedStoccaggioCessAcqu", "checked");
                  break;
                }
              }          
            }          
          }
          else
          {
            if(effVO.isFlagStoccaggioBl())
            {
              htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.checkedStoccaggioCessAcqu", "checked");
            }        
          }
        }
          
        
        
        if(vTipoEffluenti !=null)
        {
          htmpl.newBlock("blkElencoEffluente");
          for(int j=0;j<vTipoEffluenti.size();j++)
          {
            TipoEffluenteVO tipoVO  = (TipoEffluenteVO)vTipoEffluenti.get(j);
            htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoEffluente.codiceEffluente", 
              new Long(tipoVO.getIdEffluente()).toString());
            htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoEffluente.descrizioneEffluente", 
              tipoVO.getDescrizione());
            
            if(Validator.isNotEmpty(regime))
            {
              if(Validator.isNotEmpty(idEffluente) && (i < idEffluente.length))
              {  
                if(Validator.isNotEmpty(idEffluente[i]) && idEffluente[i].equalsIgnoreCase(new Long(tipoVO.getIdEffluente()).toString())) 
                {
                  htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoEffluente.selectedEffluente", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(Validator.isNotEmpty(effVO.getIdEffluente()) 
                && (effVO.getIdEffluente().compareTo(new Long(tipoVO.getIdEffluente())) == 0)) 
              {
                htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.blkElencoEffluente.selectedEffluente", "selected=\"selected\"");
              }
            }
          }
        }
        
        
                  
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.quantitaCessAcqu", 
            Formatter.formatDouble1(effVO.getQuantita()));
        }
        else
        {
          // Se valorizzato ho chiamato il javscript cambiaEffluenteAcquisito
          String nRigaCessAcqu = request.getParameter("nRigaCessAcqu");
          if(Validator.isNotEmpty(nRigaCessAcqu))
          {
            int nRigeCeAcquInt = new Integer(nRigaCessAcqu).intValue(); 
            if(nRigeCeAcquInt == i)
            {
              htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.quantitaCessAcqu", 
              Formatter.formatDouble1(effVO.getQuantita()));  
            }
            else
            {
              if(Validator.isNotEmpty(quantitaCessAcqu) && (i < quantitaCessAcqu.length))
              {
                htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.quantitaCessAcqu", 
                  Formatter.formatDouble1(quantitaCessAcqu[i]));
              }
            }
          }
          else
          {
            if(Validator.isNotEmpty(quantitaCessAcqu) && (i < quantitaCessAcqu.length))
            {
              htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.quantitaCessAcqu", 
                Formatter.formatDouble1(quantitaCessAcqu[i]));
            }
          }
        }
        
        
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.azotoDichiaratoCessAcqu", 
            Formatter.formatAndRoundBigDecimal1(effVO.getQuantitaAzotoDichiarato()));
        }
        else
        {
          if(Validator.isNotEmpty(azotoDichiaratoCessAcqu) && (i < azotoDichiaratoCessAcqu.length))
          {
            if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("calcolaAzotoCessAcqu"))
            {
              //se invece sono dell'acquisizione il valore del dichiarato corrisponde a quello
              // del calcolato solo se il dichiarato è vuoto. 
              //In tutti gli altri casi è preso quello
              //inserito nell'interfaccia utente.
              if(Validator.isEmpty(azotoDichiaratoCessAcqu[i]))
              {
                htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.azotoDichiaratoCessAcqu", 
                  Formatter.formatAndRoundBigDecimal1(effVO.getQuantitaAzotoDichiarato()));
              }
              else
              {
                htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.azotoDichiaratoCessAcqu", 
                  Formatter.formatDouble1(azotoDichiaratoCessAcqu[i]));
              }
            }
            else
            {
              htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.azotoDichiaratoCessAcqu", 
                Formatter.formatDouble1(azotoDichiaratoCessAcqu[i]));
            }
          }
        }
          
        htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.azotoCessAcqu", 
          Formatter.formatAndRoundBigDecimal1(effVO.getQuantitaAzoto()));
        
        // GESTIONE ERRORI RELATIVI ALLA CONFERMA
        if(hElencoErroriCessAcqu != null) 
        {
          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriCessAcqu.get(new Integer(i));
          if(errorsRecord != null && errorsRecord.size() > 0) 
          {
            Iterator iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator errorIterator = errorsRecord.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.err_"+property,
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(error.getMessage())+"'",
                    error.getMessage()}),
                    null);
                }
              }
            }
          }
        }
        
      }
    } 
    
    
    
    if(vEffluentiTratt != null)
    {
      String[] idUteTrattIn = request.getParameterValues("idUteTratt");
      String[] idEffluenteTrattIn = request.getParameterValues("idEffluenteTratt");
      String[] idTrattamentoIn = request.getParameterValues("idTrattamento");
      String[] quantitaTrattIn = request.getParameterValues("quantitaTratt");
      String[] quantitaPalDichTrattIn = request.getParameterValues("quantitaPalDichTratt");
      String[] azotoPalDichTrattIn = request.getParameterValues("azotoPalDichTratt");
      String[] quantitaNonPalDichTrattIn = request.getParameterValues("quantitaNonPalDichTratt");
      String[] azotoNonPalDichTrattIn = request.getParameterValues("azotoNonPalDichTratt");
      
      
      String[] idUteTratt = null;
      String[] idEffluenteTratt = null;
      String[] idTrattamento = null;
      String[] quantitaTratt = null;
      String[] quantitaPalDichTratt = null;
      String[] azotoPalDichTratt = null;
      String[] quantitaNonPalDichTratt = null;
      String[] azotoNonPalDichTratt = null;
      
      //hChkTrattamento è valorizzato se sono nel cosa dell'elimina
      HashMap hChkTrattamento = (HashMap)request.getAttribute("hChkTrattamento");
      if(hChkTrattamento !=null)
      {
        int size = vEffluentiTratt.size();
        idUteTratt = new String[size];
        idEffluenteTratt = new String[size];
        idTrattamento = new String[size];
        quantitaTratt = new String[size];
        quantitaPalDichTratt = new String[size];
        azotoPalDichTratt = new String[size];
        quantitaNonPalDichTratt = new String[size];
        azotoNonPalDichTratt = new String[size];
        
        int j = 0;
        for(int i=0;i<idUteTrattIn.length;i++)
        {
          if(hChkTrattamento.get(new Integer(i)) == null)
          {            
            idUteTratt[j] = idUteTrattIn[i];
            idEffluenteTratt[j] = idEffluenteTrattIn[i];
            idTrattamento[j] = idTrattamentoIn[i];
            quantitaTratt[j] = quantitaTrattIn[i];
            quantitaPalDichTratt[j] = quantitaPalDichTrattIn[i];
            azotoPalDichTratt[j] = azotoPalDichTrattIn[i];
            quantitaNonPalDichTratt[j] = quantitaNonPalDichTrattIn[i];
            azotoNonPalDichTratt[j] = azotoNonPalDichTrattIn[i];     
            
            j++;
          }
        }
      }
      else
      {
        idUteTratt = idUteTrattIn;
        idEffluenteTratt = idEffluenteTrattIn;
        idTrattamento = idTrattamentoIn;
        quantitaTratt= quantitaTrattIn;
        quantitaPalDichTratt = quantitaPalDichTrattIn;
        azotoPalDichTratt = azotoPalDichTrattIn;
        quantitaNonPalDichTratt = quantitaNonPalDichTrattIn;
        azotoNonPalDichTratt = azotoNonPalDichTrattIn;  
      } 
      
       
      htmpl.newBlock("blkTrattamenti");
      for(int i=0;i<vEffluentiTratt.size();i++)
      {
        String nRigaTratt = request.getParameter("nRigaTratt");
        EffluenteVO effVO = vEffluentiTratt.get(i);
        htmpl.newBlock("blkElencoTrattamenti");
        htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.chkTrattamento", new Integer(i).toString());
        
        
        long idUte = 0;
        if(elencoUte !=null)
        { 
          htmpl.newBlock("blkElencoUteTratt");
          for(int j=0;j<elencoUte.size();j++)
          {
            UteVO uteVO  = (UteVO)elencoUte.get(j);
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoUteTratt.codiceUteTratt", 
              uteVO.getIdUte().toString());
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoUteTratt.descrizioneUteTratt", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") "+uteVO.getIndirizzo());
            
            if(elencoUte.size() == 1)
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoUteTratt.selectedIdUteTratt", "selected=\"selected\"", null);
              idUte = uteVO.getIdUte();
            }
            else if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idUteTratt) && (i < idUteTratt.length))
              {  
                if(Validator.isNotEmpty(idUteTratt[i]) && idUteTratt[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
                {
                  htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoUteTratt.selectedIdUteTratt", "selected=\"selected\"", null);
                  idUte = uteVO.getIdUte();
                }
              }
            }
            else
            {
              if(effVO.getIdUte() ==  uteVO.getIdUte().longValue()) 
              {
                htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoUteTratt.selectedIdUteTratt", "selected=\"selected\"", null);
                idUte = uteVO.getIdUte();
              }
            }
          }
        }
        
        
        TipoEffluenteVO tipoEffTrattVO = null;
        if(hTipoEffluentiTratt !=null
          && (hTipoEffluentiTratt.get(new Long(idUte)) != null))
        {
          Vector<TipoEffluenteVO> vTipoEffluentiTratt = hTipoEffluentiTratt.get(new Long(idUte));
          htmpl.newBlock("blkElencoEffluenteTratt");
          for(int j=0;j<vTipoEffluentiTratt.size();j++)
          {
            TipoEffluenteVO tipoVO  = vTipoEffluentiTratt.get(j);
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoEffluenteTratt.codiceEffluenteTratt", 
              new Long(tipoVO.getIdEffluente()).toString());
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoEffluenteTratt.descrizioneEffluenteTratt", 
              tipoVO.getDescrizione());
            
            if(Validator.isNotEmpty(regime))
            {
              if(Validator.isNotEmpty(idEffluenteTratt) && (i < idEffluenteTratt.length))
              {  
                if(Validator.isNotEmpty(idEffluenteTratt[i]) && idEffluenteTratt[i].equalsIgnoreCase(new Long(tipoVO.getIdEffluente()).toString())) 
                {
                  htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoEffluenteTratt.selectedEffluenteTratt", "selected=\"selected\"", null);
                  tipoEffTrattVO = tipoVO;
                }
              }
            }
            else
            {
              if(Validator.isNotEmpty(effVO.getIdEffluenteOrigine()) 
                && (effVO.getIdEffluenteOrigine().longValue() == tipoVO.getIdEffluente())) 
              {
                htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoEffluenteTratt.selectedEffluenteTratt", "selected=\"selected\"", null);
                tipoEffTrattVO = tipoVO;
              }
            }
          }
        }
        
        if(tipoEffTrattVO != null)
        {
          String tipologiaEffluenteTratt = "Palabile";
          if("N".equalsIgnoreCase(tipoEffTrattVO.getFlagPalabile()))
          {
            tipologiaEffluenteTratt = "Non palabile";
          }
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.tipologiaEffluenteTratt", tipologiaEffluenteTratt);
          
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPreTratt", Formatter.formatDouble1(tipoEffTrattVO.getVolumeProdotto()));
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPreTratt", Formatter.formatDouble1(tipoEffTrattVO.getAzotoProdotto()));
        
        }
        
        
        TipoTrattamento tipoVOSel = null;
        if(vTrattamenti !=null)
        {
          htmpl.newBlock("blkElencoTrattamento");
          for(int j=0;j<vTrattamenti.size();j++)
          {
            boolean ok = false;
            TipoTrattamento tipoVO  = vTrattamenti.get(j);
            if(Validator.isNotEmpty(tipoEffTrattVO)
              && "S".equalsIgnoreCase(tipoEffTrattVO.getFlagPalabile()))
            {
              if(!tipoVO.isFlagCalcolo())
              {
                ok = true;
              }          
            }
            else
            {
              ok = true;
            }
            
            if(ok)
            {            
	            
	            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoTrattamento.idTrattamento", 
	              ""+tipoVO.getIdTrattamento());
	            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoTrattamento.codiceTrattamento", 
	              tipoVO.getCodiceSiap());
	            
	            if(Validator.isNotEmpty(regime))
	            {
	              if(Validator.isNotEmpty(idTrattamento) && (i < idTrattamento.length))
	              {  
	                if(Validator.isNotEmpty(idTrattamento[i]) && idTrattamento[i].equalsIgnoreCase(new Long(tipoVO.getIdTrattamento()).toString())) 
	                {
	                  htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoTrattamento.selectedTrattamento", "selected=\"selected\"", null);
	                  tipoVOSel = tipoVO;
	                }
	              }
	            }
	            else
	            {
	              if(Validator.isNotEmpty(effVO.getIdTrattamento()) 
	                && (effVO.getIdTrattamento().longValue() == tipoVO.getIdTrattamento())) 
	              {
	                htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.blkElencoTrattamento.selectedTrattamento", "selected=\"selected\"", null);
	                tipoVOSel = tipoVO;
	              }
	            }
	          }
          }
        }
               
        
        
        String quantitaTmp = null;
        if(Validator.isEmpty(regime))
        {
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaTratt", 
            Formatter.formatDouble1(effVO.getVolumeIniziale()));
            
          quantitaTmp = effVO.getVolumeIniziale().toString();
        }
        else
        {
          if(Validator.isNotEmpty(quantitaTratt) && (i < quantitaTratt.length))
          {
            if((tipoEffTrattVO != null)
              && "refresh".equalsIgnoreCase(operazione)
              && Validator.isNotEmpty(nRigaTratt) && i == new Integer(nRigaTratt).intValue()) 
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaTratt", 
                Formatter.formatDouble1(tipoEffTrattVO.getVolumeProdotto()));
              
              quantitaTmp = tipoEffTrattVO.getVolumeProdotto().toString();
            }
            else
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaTratt", 
               Formatter.formatDouble1(quantitaTratt[i]));
               
              if(Validator.isNotEmpty(quantitaTratt) 
		            && (i < quantitaTratt.length)        
		            && Validator.isNotEmpty(quantitaTratt[i]))
		          {
		            quantitaTmp = quantitaTratt[i];
		          }
            }
          }
        }
        
        
        BigDecimal risPalBg = null;
        BigDecimal risAzotoPalBg = null;
        BigDecimal risNonPalBg = null;
        BigDecimal risAzotoNonPalBg = null;
       
        
        BigDecimal quantitaAzotoTmp = null;
        if(Validator.isNotEmpty(quantitaTmp)
          && Validator.isNotEmpty(tipoEffTrattVO))
        {
          try
          {
            BigDecimal quantitaTmpBg = new BigDecimal(quantitaTmp.replace(',', '.')); 
            quantitaAzotoTmp = tipoEffTrattVO.getAzotoProdotto().multiply(quantitaTmpBg);
            quantitaAzotoTmp = quantitaAzotoTmp.divide(tipoEffTrattVO.getVolumeProdotto(),1,BigDecimal.ROUND_HALF_UP); 
            //Campo nascosto
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaAzoto", Formatter.formatAndRoundBigDecimal1(quantitaAzotoTmp));
          }
          catch(Exception e)
          {
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaAzoto", "0");
          }
        }
        
        if(Validator.isNotEmpty(tipoVOSel)
          && Validator.isNotEmpty(quantitaTmp) 
          && Validator.isNotEmpty(tipoEffTrattVO))
        {
          if(tipoVOSel.getIdTrattamento() == 8)
          {
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPalPostTratt", "0");
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPalPostTratt", "0");
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaNonPalPostTratt", "0");
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoNonPalPostTratt", "0");
          }
          else
          {
	          try
		        {
		          double formulaVol = 1 -(tipoVOSel.getPercVolumeSolido()/100);
		          double quantita = Double.parseDouble(quantitaTmp.replace(',', '.'));
		          double formulaAzoto = (1 -(tipoVOSel.getPercAzotoVolatile()/100)) * (1 -(tipoVOSel.getPercAzotoSolido()/100));
		          double quantitaAzoto = tipoEffTrattVO.getAzotoProdotto().doubleValue() * quantita / tipoEffTrattVO.getVolumeProdotto().doubleValue(); 
		          double risNonPal = quantita * formulaVol;
              double risAzotoNonPal = quantitaAzoto * formulaAzoto;
		          double risPal = quantita - risNonPal;
		          double risAzotoPal = quantitaAzoto - risAzotoNonPal;
		          risPalBg = new BigDecimal(risPal);
		          risAzotoPalBg = new BigDecimal(risAzotoPal);
		          risNonPalBg = new BigDecimal(risNonPal);
              risAzotoNonPalBg = new BigDecimal(risAzotoNonPal);
              //htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaAzoto", Formatter.formatAndRoundBigDecimal1(new BigDecimal(quantitaAzoto)));
		          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPalPostTratt", Formatter.formatAndRoundBigDecimal1(risPalBg));
		          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPalPostTratt", Formatter.formatAndRoundBigDecimal1(risAzotoPalBg));
		          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaNonPalPostTratt", Formatter.formatAndRoundBigDecimal1(risNonPalBg));
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoNonPalPostTratt", Formatter.formatAndRoundBigDecimal1(risAzotoNonPalBg));
		        }
		        catch (Exception e)
		        {
		          //htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaAzoto", "0");
		          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPalPostTratt", "0");
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPalPostTratt", "0");
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaNonPalPostTratt", "0");
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoNonPalPostTratt", "0");
            }
            
            
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.percVolumesolido", ""+tipoVOSel.getPercVolumeSolido());
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.percAzotoVolatile", ""+tipoVOSel.getPercAzotoVolatile());
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoProdotto", ""+tipoEffTrattVO.getAzotoProdotto());
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.volumeProdotto", ""+tipoEffTrattVO.getVolumeProdotto());
            htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.percAzotoSolido", ""+tipoVOSel.getPercAzotoSolido());
            
		      }
		      
		           
        
        }
        
        
        //palabile
        if(Validator.isEmpty(regime))
        {
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPalDichTratt", 
            Formatter.formatDouble1(effVO.getVolumePostDichiarato()));
        }
        else
        {
          if(Validator.isNotEmpty(quantitaPalDichTratt) && (i < quantitaPalDichTratt.length))
          {
            if(Validator.isNotEmpty(risPalBg) 
              && ("refreshTratt".equalsIgnoreCase(operazione) || "refresh".equalsIgnoreCase(operazione)) 
              && Validator.isNotEmpty(nRigaTratt) && i == new Integer(nRigaTratt).intValue()) 
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPalDichTratt", 
                Formatter.formatAndRoundBigDecimal1(risPalBg));
            }
            else
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPalDichTratt", 
                Formatter.formatDouble1(quantitaPalDichTratt[i]));
            }
          }
        }
        
        //palabile
        if(Validator.isEmpty(regime))
        {
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPalDichTratt", 
            Formatter.formatAndRoundBigDecimal1(effVO.getAzotoPostDichiarato()));
        }
        else
        {
          if(Validator.isNotEmpty(azotoPalDichTratt) && (i < azotoPalDichTratt.length))
          {
            if(Validator.isNotEmpty(risAzotoPalBg) 
              && ("refreshTratt".equalsIgnoreCase(operazione) || "refresh".equalsIgnoreCase(operazione))
              && Validator.isNotEmpty(nRigaTratt) && i == new Integer(nRigaTratt).intValue()) 
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPalDichTratt", 
                Formatter.formatAndRoundBigDecimal1(risAzotoPalBg));
            }
            else
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPalDichTratt", 
                Formatter.formatDouble1(azotoPalDichTratt[i]));
            }
          }
        }
        
        
        //non palabile
        if(Validator.isEmpty(regime))
        {
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaNonPalDichTratt", 
            Formatter.formatDouble1(effVO.getVolumePostDichiaratoNonPal()));
        }
        else
        {
          if(Validator.isNotEmpty(quantitaNonPalDichTratt) && (i < quantitaNonPalDichTratt.length))
          {
            if(Validator.isNotEmpty(risNonPalBg) 
              && ("refreshTratt".equalsIgnoreCase(operazione) || "refresh".equalsIgnoreCase(operazione))
              && Validator.isNotEmpty(nRigaTratt) && i == new Integer(nRigaTratt).intValue()) 
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaNonPalDichTratt", 
                Formatter.formatAndRoundBigDecimal1(risNonPalBg));
            }
            else
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaNonPalDichTratt", 
                Formatter.formatDouble1(quantitaNonPalDichTratt[i]));
            }
          }
        }
        
        //non palabile
        if(Validator.isEmpty(regime))
        {
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoNonPalDichTratt", 
            Formatter.formatAndRoundBigDecimal1(effVO.getAzotoPostDichiaratoNonPal()));
        }
        else
        {
          if(Validator.isNotEmpty(azotoNonPalDichTratt) && (i < azotoNonPalDichTratt.length))
          {
            if(Validator.isNotEmpty(risAzotoNonPalBg) 
              && ("refreshTratt".equalsIgnoreCase(operazione) || "refresh".equalsIgnoreCase(operazione))
              && Validator.isNotEmpty(nRigaTratt) && i == new Integer(nRigaTratt).intValue()) 
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoNonPalDichTratt", 
                Formatter.formatAndRoundBigDecimal1(risAzotoNonPalBg));
            }
            else
            {
              htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoNonPalDichTratt", 
                Formatter.formatDouble1(azotoNonPalDichTratt[i]));
            }
          }
        }
        
        
        
        // GESTIONE ERRORI RELATIVI ALLA CONFERMA
        if(hElencoErroriTratt != null) 
        {
          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriTratt.get(new Integer(i));
          if(errorsRecord != null && errorsRecord.size() > 0) 
          {
            Iterator iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator errorIterator = errorsRecord.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.err_"+property,
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(error.getMessage())+"'",
                    error.getMessage()}),
                    null);
                }
              }
            }
          }
        }
        
        
      }
    }
    
    
    
    
    
    if(vCessioni != null)
    {
      htmpl.newBlock("blkCessioni");
      String[] quantitaCessioniIn = request.getParameterValues("quantitaCessioni");
      String[] azotoCessioniIn = request.getParameterValues("azotoCessioni");
      String[] idEffluenteCessioniIn = request.getParameterValues("idEffluenteCessioni");
      String[] idUteCessioniIn = request.getParameterValues("idUteCessioni");
      String[] chkCessioniIn = request.getParameterValues("chkCessioni");
      String[] chkStoccaggioCessioniIn = request.getParameterValues("chkStoccaggioCessioni");
      
      String[] quantitaCessioni = null;
      String[] azotoCessioni = null;
      //String[] idCausaleEffluente = null; 
      String[] idEffluenteCessioni = null;
      String[] idUteCessioni = null;
      
      //hChkCessAcqu è valorizzato se sono nel cosa dell'elimina
      HashMap<Integer,Integer> hChkCessioni = (HashMap<Integer,Integer>)request.getAttribute("hChkCessioni");
      if(hChkCessioni !=null)
      {
        int size = vCessioni.size();
        quantitaCessioni = new String[size];
        azotoCessioni = new String[size];        
        idEffluenteCessioni = new String[size];
        idUteCessioni = new String[size];
        
        int j = 0;
        for(int i=0;i<idUteCessioniIn.length;i++)
        {
          if(hChkCessioni.get(new Integer(i)) == null)
          {
            quantitaCessioni[j] = quantitaCessioniIn[i];
            azotoCessioni[j] = azotoCessioniIn[i];
            idEffluenteCessioni[j] = idEffluenteCessioniIn[i];
            idUteCessioni[j] = idUteCessioniIn[i];
            j++;
          }
        }
      }
      else
      {
        quantitaCessioni = quantitaCessioniIn;
        azotoCessioni = azotoCessioniIn;
        idEffluenteCessioni = idEffluenteCessioniIn;
        idUteCessioni = idUteCessioniIn;
      } 
      
      for(int i=0;i<vCessioni.size();i++)
      {
        String nRigaCess = request.getParameter("nRigaCess");
        EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioni.get(i);
        htmpl.newBlock("blkElencoCessioni");
        htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.chkCessioni",new Integer(i).toString());
        
        if(Validator.isNotEmpty(effVO.getIdAzienda()))
        {
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.idAziendaCessioniElenco", 
            effVO.getIdAzienda().toString());
        }        
        htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.cuaaCessioni", 
          effVO.getCuaa());
        htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.denominazioneCessioni", 
          effVO.getDenominazione());
        htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.comuneCessioni", 
          effVO.getDescComune());
        htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.siglaProvCessioni", 
          effVO.getSglProv());
          
        
        Long idUte = null;  
        if(elencoUte !=null)
        { 
          htmpl.newBlock("blkElencoUteCessioni");
          for(int j=0;j<elencoUte.size();j++)
          {
            UteVO uteVO  = (UteVO)elencoUte.get(j);
            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoUteCessioni.codiceUteCessioni", 
              uteVO.getIdUte().toString());
            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoUteCessioni.descrizioneUteCessioni", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") "+uteVO.getIndirizzo());
            
            if(elencoUte.size() == 1)
            {
              htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoUteCessioni.selectedIdUteCessioni", "selected=\"selected\"", null);
              idUte = uteVO.getIdUte();
            }
            else if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idUteCessioni) && (i < idUteCessioni.length))
              {  
                if(Validator.isNotEmpty(idUteCessioni[i]) && idUteCessioni[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
                {
                  htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoUteCessioni.selectedIdUteCessioni", "selected=\"selected\"", null);
                  idUte = uteVO.getIdUte();
                }
              }
            }
            else
            {
              if(effVO.getIdUte() ==  uteVO.getIdUte().longValue()) 
              {
                htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoUteCessioni.selectedIdUteCessioni", "selected=\"selected\"", null);
                idUte = uteVO.getIdUte();
              }
            }
          }
        }
        
        
        
        htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.chkStoccaggioCessioni",new Integer(i).toString());
        
        if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaCessioni"))
        {
          if(effVO.isFlagStoccaggioBl())
          {
            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.checkedStoccaggioCessioni", "checked");
          }     
        }
        else
        {  
          if(Validator.isNotEmpty(regime))
          {          
            if(Validator.isNotEmpty(chkStoccaggioCessioniIn))
            {
              for(int j=0;j<chkStoccaggioCessioniIn.length;j++)
              {
                if(i == new Integer(chkStoccaggioCessioniIn[j]).intValue())
                {
                  htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.checkedStoccaggioCessioni", "checked");
                  break;
                }
              }          
            }          
          }
          else
          {
            if(effVO.isFlagStoccaggioBl())
            {
              htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.checkedStoccaggioCessioni", "checked");
            }        
          }
        }
        
        
        
       
        TipoEffluenteVO tipoEffluenteVOCessVO = null;
        if(hTipoEffluentiCess !=null
          && idUte != null && hTipoEffluentiCess.get(idUte) != null)
        {
          htmpl.newBlock("blkElencoEffluenteCessioni");
          Vector<TipoEffluenteVO> vTipoEffluentiCess = hTipoEffluentiCess.get(idUte);
          if(vTipoEffluentiCess != null)
          {
	          for(int j=0;j<vTipoEffluentiCess.size();j++)
	          {
	            TipoEffluenteVO tipoVO  = (TipoEffluenteVO)vTipoEffluentiCess.get(j);
	            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoEffluenteCessioni.codiceEffluenteCessioni", 
	              new Long(tipoVO.getIdEffluente()).toString());
	            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoEffluenteCessioni.descrizioneEffluenteCessioni", 
	              tipoVO.getDescrizione());
	            
	            if(Validator.isNotEmpty(regime))
	            {
	              if(Validator.isNotEmpty(idEffluenteCessioni) && (i < idEffluenteCessioni.length))
	              {  
	                if(Validator.isNotEmpty(idEffluenteCessioni[i]) && idEffluenteCessioni[i].equalsIgnoreCase(new Long(tipoVO.getIdEffluente()).toString())) 
	                {
	                  htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoEffluenteCessioni.selectedEffluenteCessioni", "selected=\"selected\"", null);
	                  tipoEffluenteVOCessVO = tipoVO;
	                }
	              }
	            }
	            else
	            {
	              if(Validator.isNotEmpty(effVO.getIdEffluente()) 
	                && (effVO.getIdEffluente().compareTo(new Long(tipoVO.getIdEffluente())) == 0)) 
	              {
	                htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.blkElencoEffluenteCessioni.selectedEffluenteCessioni", "selected=\"selected\"", null);
	                tipoEffluenteVOCessVO = tipoVO;
	              }
	            }
	          }
	        }
        }
        
        
        
        if(tipoEffluenteVOCessVO  != null)
        {
          String tipologiaEffluenteTratt = "Palabile";
          if("N".equalsIgnoreCase(tipoEffluenteVOCessVO.getFlagPalabile()))
          {
            tipologiaEffluenteTratt = "Non palabile";
          }
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.tipologiaEffluenteCess", tipologiaEffluenteTratt);
          
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.quantitaPreCess", Formatter.formatDouble1(tipoEffluenteVOCessVO.getVolumeProdotto()));
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.azotoPreCess", Formatter.formatDouble1(tipoEffluenteVOCessVO.getAzotoProdotto()));
        
        }
        
        
                  
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.quantitaCessioni", 
            Formatter.formatDouble1(effVO.getQuantita()));
        }
        else
        {
          if(Validator.isNotEmpty(tipoEffluenteVOCessVO)
            && "refreshCess".equalsIgnoreCase(operazione)
            && Validator.isNotEmpty(nRigaCess) && i == new Integer(nRigaCess).intValue()) 
          {
            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.quantitaCessioni", 
              Formatter.formatDouble1(tipoEffluenteVOCessVO.getVolumeProdotto()));
          }
          else
          {
            if(Validator.isNotEmpty(quantitaCessioni) && (i < quantitaCessioni.length))
            { 
              htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.quantitaCessioni", 
                Formatter.formatDouble1(quantitaCessioni[i]));
            }
          }
        
        }
        
        
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.azotoCessioni", 
            Formatter.formatDouble1(effVO.getQuantitaAzoto()));
        }
        else
        {
          if(Validator.isNotEmpty(azotoCessioni) && (i < azotoCessioni.length))
          {
            if(Validator.isNotEmpty(tipoEffluenteVOCessVO)
              && "refreshCess".equalsIgnoreCase(operazione)
	            && Validator.isNotEmpty(nRigaCess) && i == new Integer(nRigaCess).intValue()) 
	          {
	            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.azotoCessioni", 
	              Formatter.formatDouble1(tipoEffluenteVOCessVO.getAzotoProdotto()));
	          }
	          else
	          {
	            if(Validator.isNotEmpty(azotoCessioni) && (i < azotoCessioni.length))
              { 
                htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.azotoCessioni", 
                  Formatter.formatDouble1(azotoCessioni[i]));
              }
            }
          }
        }
          
        
        // GESTIONE ERRORI RELATIVI ALLA CONFERMA
        if(hElencoErroriCessioni != null) 
        {
          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriCessioni.get(new Integer(i));
          if(errorsRecord != null && errorsRecord.size() > 0) 
          {
            Iterator iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator errorIterator = errorsRecord.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.err_"+property,
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(error.getMessage())+"'",
                    error.getMessage()}),
                    null);
                }
              }
            }
          }
        }
        
      }
    }
    
    if(vStoccaggio != null)
    {
      String[] quantitaStoccIn = request.getParameterValues("quantitaStocc");
      String[] idTipologiaFabbricatoIn = request.getParameterValues("idTipologiaFabbricato");
      String[] idUteStoccIn = request.getParameterValues("idUteStocc");
      
      String[] quantitaStocc = null;
      String[] idTipologiaFabbricato = null;
      String[] idUteStocc = null;
      
      //hChkAcqueReflue è valorizzato se sono nel cosa dell'elimina
      HashMap hChkStocc = (HashMap)request.getAttribute("hChkStocc");
      if(hChkStocc !=null)
      {
        int size = vStoccaggio.size();
        quantitaStocc = new String[size];
        idTipologiaFabbricato = new String[size];
        idUteStocc = new String[size];
        
        int j = 0;
        for(int i=0;i<quantitaStoccIn.length;i++)
        {
          if(hChkStocc.get(new Integer(i)) == null)
          {
            quantitaStocc[j] = quantitaStoccIn[i];
            idTipologiaFabbricato[j] = idTipologiaFabbricatoIn[i];
            idUteStocc[j] = idUteStoccIn[i];
            j++;
          }
        }
      }
      else
      {
        quantitaStocc = quantitaStoccIn;
        idTipologiaFabbricato = idTipologiaFabbricatoIn;
        idUteStocc = idUteStoccIn;
      } 
      
       
      htmpl.newBlock("blkStocc");
      for(int i=0;i<vStoccaggio.size();i++)
      {
        EffluenteStocExtVO effVO = (EffluenteStocExtVO)vStoccaggio.get(i);
        htmpl.newBlock("blkElencoStocc");
        htmpl.set("blkNoErr.blkStocc.blkElencoStocc.chkStocc", new Integer(i).toString());
        
        if(Validator.isNotEmpty(effVO.getIdAzienda()))
        {
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.idAziendaStoccElenco", 
            effVO.getIdAzienda().toString());
        }  
         
        htmpl.set("blkNoErr.blkStocc.blkElencoStocc.cuaaStocc", 
          effVO.getCuaa());
        htmpl.set("blkNoErr.blkStocc.blkElencoStocc.denominazioneStocc", 
          effVO.getDenominazione());
        htmpl.set("blkNoErr.blkStocc.blkElencoStocc.comuneStocc", 
          effVO.getDescComune());
        htmpl.set("blkNoErr.blkStocc.blkElencoStocc.siglaProvStocc", 
          effVO.getSglProv());
          
        if(elencoUte !=null)
        { 
          htmpl.newBlock("blkElencoUteStocc");
          for(int j=0;j<elencoUte.size();j++)
          {
            UteVO uteVO  = (UteVO)elencoUte.get(j);
            htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoUteStocc.codiceUteStocc", 
              uteVO.getIdUte().toString());
            htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoUteStocc.descrizioneUteStocc", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") "+uteVO.getIndirizzo());
            
            if(elencoUte.size() == 1)
            {
              htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoUteStocc.selectedIdUteStocc", "selected=\"selected\"");
            }
            else if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idUteStocc) && (i < idUteStocc.length))
              {  
                if(Validator.isNotEmpty(idUteStocc[i]) && idUteStocc[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
                {
                  htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoUteStocc.selectedIdUteStocc", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(effVO.getIdUte() ==  uteVO.getIdUte().longValue()) 
              {
                htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoUteStocc.selectedIdUteStocc", "selected=\"selected\"");
              }
            }
          }
        }
          
        if(vTipoFabbricato !=null)
        {
          htmpl.newBlock("blkElencoTipoFabbricato");
          for(int j=0;j<vTipoFabbricato.size();j++)
          {
            TipoTipologiaFabbricatoVO tipoVO  = (TipoTipologiaFabbricatoVO)vTipoFabbricato.get(j);
            htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoTipoFabbricato.codiceFabbricato", 
              tipoVO.getIdTipologiaFabbricato().toString());
            htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoTipoFabbricato.descrizioneFabbricato", 
              tipoVO.getDescrizione());
            
            if(Validator.isNotEmpty(regime))
            {  
              if(Validator.isNotEmpty(idTipologiaFabbricato) && (i < idTipologiaFabbricato.length))
              {  
                if(Validator.isNotEmpty(idTipologiaFabbricato[i]) && idTipologiaFabbricato[i].equalsIgnoreCase(tipoVO.getIdTipologiaFabbricato().toString())) 
                {
                  htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoTipoFabbricato.selectedFabbricato", "selected=\"selected\"");
                }
              }
            }
            else
            {
              if(effVO.getIdTipologiaFabbricato() ==  tipoVO.getIdTipologiaFabbricato().longValue()) 
              {
                htmpl.set("blkNoErr.blkStocc.blkElencoStocc.blkElencoTipoFabbricato.selectedFabbricato", "selected=\"selected\"");
              }
            }
          }
        }
        
        if(!Validator.isNotEmpty(regime))
        {
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.quantitaStocc", 
            Formatter.formatDouble4(effVO.getQuantita()));
        }
        else
        {
          if(Validator.isNotEmpty(quantitaStocc) && (i < quantitaStocc.length))
          {
            htmpl.set("blkNoErr.blkStocc.blkElencoStocc.quantitaStocc", 
             Formatter.formatDouble4(quantitaStocc[i]));
          }
        }
        
        
        
        // GESTIONE ERRORI RELATIVI ALLA CONFERMA
        if(hElencoErroriStoc != null) 
        {
          ValidationErrors errorsRecord = (ValidationErrors)hElencoErroriStoc.get(new Integer(i));
          if(errorsRecord != null && errorsRecord.size() > 0) 
          {
            Iterator iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator errorIterator = errorsRecord.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkNoErr.blkStocc.blkElencoStocc.err_"+property,
                    MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(error.getMessage())+"'",
                    error.getMessage()}),
                    null);
                }
              }
            }
          }
        }
        
        
      }
    }
    
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>


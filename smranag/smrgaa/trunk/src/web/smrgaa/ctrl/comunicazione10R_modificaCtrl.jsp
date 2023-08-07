<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.GenericQuantitaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.TipoCausaleEffluenteVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.TipoAcquaAgronomicaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.TipoTrattamento" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "comunicazione10R_modificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - comunicazione10R_modificaCtrl.jsp - INIZIO PAGINA");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String comunicazione10R_modificaUrl = "/view/comunicazione10R_modificaView.jsp";
  
  //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
  ResourceBundle res = ResourceBundle.getBundle("config");
  String ambienteDeploy = res.getString("ambienteDeploy");
  SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
  String comunicazione10R_dettaglioUrl ="";
  if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
	  comunicazione10R_dettaglioUrl = "../layout/";
  else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
	  comunicazione10R_dettaglioUrl = "/layout/";
  comunicazione10R_dettaglioUrl += "comunicazione10R_dettaglio.htm";
  
  
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  ValidationErrors errors = null;
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  final int EFFLUENTE_ALTREACQUE = 19;
  final int LUNG_MAX_CUAA = 16;
  final int LUNG_MAX_PI = 11;
  
  //Vettore di Comunicazione10RVO
  Vector<Comunicazione10RVO> vCom10r = (Vector<Comunicazione10RVO>)session.getAttribute("vCom10r");
  //Vettore di EffluenteCesAcqVO //solo per Acqusizioni
  Vector<EffluenteCesAcqVO> vCessioniAcquisizioni = (Vector<EffluenteCesAcqVO>)session.getAttribute("vCessioniAcquisizioni");
  //Vettore di EffluenteCesAcqVO // solo cessioni
  Vector<EffluenteCesAcqVO> vCessioni = (Vector<EffluenteCesAcqVO>)session.getAttribute("vCessioni");
  //Vettore di EffluenteStocExtVO
  Vector<EffluenteStocExtVO> vStoccaggio = (Vector<EffluenteStocExtVO>)session.getAttribute("vStoccaggio");
  //Vettore di AcquaExtraVO
  Vector<AcquaExtraVO> vAcqueReflue = (Vector<AcquaExtraVO>)session.getAttribute("vAcqueReflue");
  //Vettore di GenericQuantitaVO
  Vector<GenericQuantitaVO> vVolumeSottogrigliato = (Vector<GenericQuantitaVO>)session.getAttribute("vVolumeSottogrigliato");
  //Vettore di GenericQuantitaVO
  Vector<GenericQuantitaVO> vVolumeRefluoAzienda = (Vector<GenericQuantitaVO>)session.getAttribute("vVolumeRefluoAzienda");
  
  //Vettore di EffluenteVO // Trattamenti
  Vector<EffluenteVO> vEffluentiTratt = (Vector<EffluenteVO>)session.getAttribute("vEffluentiTratt");
  
  
  final String errMsg = "Impossibile procedere nella sezione comunicazione 10R. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
    
  String flagAdesioneDerogaDb = null;
  String flagModAdesioneDerogaDb = null;
  String flagEffluentiControlloAdesDer = null;
  BigDecimal valoreControlloPalDeroga = null;
  BigDecimal valoreControlloNonPalDeroga = null;  
  HashMap<Long,Long> hIdEffluentiCtrlDeroga = new  HashMap<Long,Long>();
  try 
  {
    flagAdesioneDerogaDb = (String)anagFacadeClient.getValoreParametroAltriDati("DEROGA");
    flagModAdesioneDerogaDb = (String)anagFacadeClient.getValoreParametroAltriDati("MOD_DEROGA");
    flagEffluentiControlloAdesDer = (String)anagFacadeClient.getValoreParametroAltriDati("DEREFF");
    valoreControlloPalDeroga = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati("DTOLP");
    valoreControlloNonPalDeroga = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati("DTOLNP");
    StringTokenizer st = new StringTokenizer(flagEffluentiControlloAdesDer,",");        
    while (st.hasMoreTokens()) 
    {
      Long idEffluenteDeroga = new Long(st.nextToken());
      hIdEffluentiCtrlDeroga.put(idEffluenteDeroga, idEffluenteDeroga);
    }
    request.setAttribute("flagAdesioneDerogaDb", flagAdesioneDerogaDb);
    request.setAttribute("flagModAdesioneDerogaDb", flagModAdesioneDerogaDb);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - comunicazione10R_modifcaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+"::"+AnagErrors.ERR_PAR_ADESIONE_DEROGA+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
    
    
  
  // Recupero i valori relativi alle unità produttive
  Vector<UteVO> elencoUte = null;
  try 
  {
    elencoUte = anagFacadeClient.getListUteByIdAziendaAndIdPianoRiferimento(anagAziendaVO.getIdAzienda(), -1);
    request.setAttribute("elencoUte", elencoUte);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+"::"+AnagErrors.ERR_RICERCA_UTE+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  //inizializzazione
  //regime non è valorizzato solo se entro per la prima volta nella modifica
  String regime = request.getParameter("regime");
  //HashMap hEffluenti = null;
  long[] idComunicazione10R = null;
  if(Validator.isEmpty(regime))
  {
    vCom10r = null;
    vCessioniAcquisizioni = null;
    vCessioni = null;
    vStoccaggio = null;
    vAcqueReflue = null;
    vVolumeSottogrigliato = null;
    vVolumeRefluoAzienda = null;
    vEffluentiTratt = null;
    
    if(Validator.isNotEmpty(elencoUte) && (elencoUte.size() > 0 ))
    { 
      
      for(int i=0;i<elencoUte.size();i++)
      {
        UteVO uteVO = (UteVO)elencoUte.get(i);
        Comunicazione10RVO  com10 = null;
        try 
        {
          com10 = gaaFacadeClient.getComunicazione10RByIdUteAndPianoRifererimento(uteVO.getIdUte().longValue(),-1);      
        }
        catch (SolmrException se) 
        {
          SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+""+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        if(com10 !=null)
        {
          if(vCom10r == null)
          {
            vCom10r = new Vector<Comunicazione10RVO>();
          }        
          vCom10r.add(com10);         
        }
      }
    }
    
    if(vCom10r !=null)
    {
      idComunicazione10R = new long[vCom10r.size()];    
      for(int i=0;i<vCom10r.size();i++)
      {
        Comunicazione10RVO  com10 = (Comunicazione10RVO)vCom10r.get(i);
        idComunicazione10R[i] = com10.getIdComunicazione10R();
        
        //Popolamento volume sottogrigliato
        if(Validator.isNotEmpty(com10.getVolumeSottogrigliato()))
        {
          if(vVolumeSottogrigliato == null)
          {
            vVolumeSottogrigliato = new Vector<GenericQuantitaVO>();
          }
          GenericQuantitaVO cqSottoGrigliatoVO = new GenericQuantitaVO();
          cqSottoGrigliatoVO.setIdUte(com10.getIdUte());
          cqSottoGrigliatoVO.setQuantita(com10.getVolumeSottogrigliato());
          vVolumeSottogrigliato.add(cqSottoGrigliatoVO);
        }
        
        if(Validator.isNotEmpty(com10.getVolumeRefluoAzienda()))
        {
          if(vVolumeRefluoAzienda == null)
          {
            vVolumeRefluoAzienda = new Vector<GenericQuantitaVO>();
          }
          //Popolamento volume Refluo Azienda
          GenericQuantitaVO cqRefluoAZiendaVO = new GenericQuantitaVO();
          cqRefluoAZiendaVO.setIdUte(com10.getIdUte());
          cqRefluoAZiendaVO.setQuantita(com10.getVolumeRefluoAzienda());
          vVolumeRefluoAzienda.add(cqRefluoAZiendaVO);
        }
      }
      
      try 
      {
        //L'elenco include tutte le ute
        vCessioniAcquisizioni = gaaFacadeClient.getListEffluentiCessAcquByidComunicazione(idComunicazione10R, SolmrConstants.ID_TIPO_CAUS_EFF_ACQUISIZIONE);
        vStoccaggio = gaaFacadeClient.getListStoccaggiExtrAziendali(idComunicazione10R);
        vAcqueReflue = gaaFacadeClient.getListAcquaExtra(idComunicazione10R);
        vEffluentiTratt = gaaFacadeClient.getListTrattamenti(idComunicazione10R);
        vCessioni = gaaFacadeClient.getListEffluentiCessAcquByidComunicazione(idComunicazione10R, SolmrConstants.ID_TIPO_CAUS_EFF_CESSIONE);        
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
  }
  
  if(vCom10r !=null)
  {  
    Vector<TipoCausaleEffluenteVO> vTipoCausaleEffluenti = null;
    Vector<TipoEffluenteVO> vTipoEffluenti = null;
    Vector<TipoTipologiaFabbricatoVO> vTipoFabbricato = null;
    Vector<TipoAcquaAgronomicaVO> vTipoAcqueReflue = null;
    Vector<TipoTrattamento> vTrattamenti = null;
    
    try 
    {
      vTipoCausaleEffluenti = gaaFacadeClient.getListTipoCausaleEffluente();
      vTipoEffluenti =  gaaFacadeClient.getListTipoEffluente();
      vTipoFabbricato = gaaFacadeClient.getListTipoFabbricatoStoccaggio();
      vTipoAcqueReflue = gaaFacadeClient.getListTipoAcquaAgronomica();
      vTrattamenti = gaaFacadeClient.getTipiTrattamento();
      
      
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    request.setAttribute("vTipoCausaleEffluenti", vTipoCausaleEffluenti);
    request.setAttribute("vTipoEffluenti", vTipoEffluenti);
    request.setAttribute("vTipoFabbricato", vTipoFabbricato);
    request.setAttribute("vTipoAcqueReflue", vTipoAcqueReflue);
    request.setAttribute("vTrattamenti", vTrattamenti);
    
    
    
    
    
    //carico i dati che devono essere la somma di tutte le operazioni
    //con acquisizioni
    //HashMap di vettori di idUte, TipoEffluenteVO
    HashMap<Long,Vector<TipoEffluenteVO>> hTipoEffluentiTratt = null;
    try 
    {
      //String[] idCausaleEffluente = request.getParameterValues("idCausaleEffluente");
      String[] idEffluente = request.getParameterValues("idEffluente");
      String[] quantitaCessAcqu = request.getParameterValues("quantitaCessAcqu");
      String[] idUteCessAcqu = request.getParameterValues("idUteCessAcqu");
      String[] azotoDichiaratoCessAcqu = request.getParameterValues("azotoDichiaratoCessAcqu");     
      
      
      
      for(int i=0;i<vCom10r.size();i++)
      {
        if(hTipoEffluentiTratt == null)
        {
          hTipoEffluentiTratt = new HashMap<Long,Vector<TipoEffluenteVO>>();
        }
        long idUte = vCom10r.get(i).getIdUte();
        Vector<TipoEffluenteVO> vTipoEff = gaaFacadeClient.getListTipoEffluenteTrattamenti(idUte); 
        if(vTipoEff == null)
        {
          vTipoEff = new Vector<TipoEffluenteVO>();
        }
        
        //esiste almeno una acquisizione
        //prima volta che entro
        if(Validator.isEmpty(regime))
        {
          if(vCessioniAcquisizioni != null)
          {
	          for(int k=0;k<vCessioniAcquisizioni.size();k++)
		        {
		          EffluenteCesAcqVO effCessAcqVO = new EffluenteCesAcqVO();
		          effCessAcqVO.setIdEffluenteStr(vCessioniAcquisizioni.get(k).getIdEffluente().toString());
		          effCessAcqVO.setIdUte(vCessioniAcquisizioni.get(k).getIdUte());
		          
		          effCessAcqVO.setQuantitaStr(vCessioniAcquisizioni.get(k).getQuantita().toString());
		          effCessAcqVO.setQuantitaAzotoDichiaratoStr(vCessioniAcquisizioni.get(k).getQuantitaAzotoDichiarato().toString());
		          ValidationErrors errorsTmp = effCessAcqVO.validateConfermaTrattamenti();
		          if(errorsTmp.size() == 0)
		          {
		            boolean trovaEffUte = false;
		            TipoEffluenteVO tipoEffUte = null;
		            for(int h=0;h<vTipoEff.size();h++)
		            {
		              tipoEffUte = vTipoEff.get(h);
		              if((idUte == effCessAcqVO.getIdUte())
		                && (tipoEffUte.getIdEffluente() == effCessAcqVO.getIdEffluente().longValue()))
		              {
		                trovaEffUte = true;
		                break;                
		              }                  
		            }
		            
		            if(trovaEffUte)
		            {
		              BigDecimal valoreVolume = tipoEffUte.getVolumeProdotto();
                  valoreVolume = valoreVolume.add(effCessAcqVO.getQuantita());
                  tipoEffUte.setVolumeProdotto(valoreVolume);
                  BigDecimal valoreAzoto = tipoEffUte.getAzotoProdotto();
                  valoreAzoto = valoreAzoto.add(effCessAcqVO.getQuantitaAzotoDichiarato());
                  tipoEffUte.setAzotoProdotto(valoreAzoto);       
		            }
		            else
		            {
		              tipoEffUte = gaaFacadeClient.getTipoEffluenteById(
		                effCessAcqVO.getIdEffluente().longValue());
		              if((idUte == effCessAcqVO.getIdUte())
		                && (tipoEffUte.getIdEffluente() == effCessAcqVO.getIdEffluente().longValue()))
		              {
		                tipoEffUte.setAzotoProdotto(effCessAcqVO.getQuantitaAzotoDichiarato());
		                tipoEffUte.setVolumeProdotto(effCessAcqVO.getQuantita());
		                vTipoEff.add(tipoEffUte);
		              }
		            }
		          
		          }
		        }
		      }
        }
        else
        {        
	        if(Validator.isNotEmpty(idUteCessAcqu))
	        {
	          for(int k=0;k<idUteCessAcqu.length;k++)
	          {
	            EffluenteCesAcqVO effCessAcqVO = new EffluenteCesAcqVO();
	            effCessAcqVO.setIdEffluenteStr(idEffluente[k]);
	            if(Validator.isNotEmpty(idUteCessAcqu[k]))
	            {            
	              effCessAcqVO.setIdUte(new Long(idUteCessAcqu[k]).longValue());
	            }
	            effCessAcqVO.setQuantitaStr(quantitaCessAcqu[k]);
	            effCessAcqVO.setQuantitaAzotoDichiaratoStr(azotoDichiaratoCessAcqu[k]);
	            ValidationErrors errorsTmp = effCessAcqVO.validateConfermaTrattamenti();
	            if(errorsTmp.size() == 0)
	            {
	              boolean trovaEffUte = false;
	              TipoEffluenteVO tipoEffUte = null;
	              for(int h=0;h<vTipoEff.size();h++)
	              {
	                tipoEffUte = vTipoEff.get(h);
	                if((idUte == effCessAcqVO.getIdUte())
	                  && (tipoEffUte.getIdEffluente() == effCessAcqVO.getIdEffluente().longValue()))
	                {
	                  trovaEffUte = true;
	                  break;                
	                }                  
	              }
	              
	              if(trovaEffUte)
	              {
	                BigDecimal valoreVolume = tipoEffUte.getVolumeProdotto();
	                valoreVolume = valoreVolume.add(effCessAcqVO.getQuantita());
	                tipoEffUte.setVolumeProdotto(valoreVolume);
	                BigDecimal valoreAzoto = tipoEffUte.getAzotoProdotto();
                  valoreAzoto = valoreAzoto.add(effCessAcqVO.getQuantitaAzotoDichiarato());
                  tipoEffUte.setAzotoProdotto(valoreAzoto);	                
	              }
	              else
	              {
	                tipoEffUte = gaaFacadeClient.getTipoEffluenteById(
	                  effCessAcqVO.getIdEffluente().longValue());
	                if((idUte == effCessAcqVO.getIdUte())
	                  && (tipoEffUte.getIdEffluente() == effCessAcqVO.getIdEffluente().longValue()))
	                {
		                tipoEffUte.setAzotoProdotto(effCessAcqVO.getQuantitaAzotoDichiarato());
		                tipoEffUte.setVolumeProdotto(effCessAcqVO.getQuantita());
		                vTipoEff.add(tipoEffUte);
		              }
	              }
	            
	            }
	          
	          }        
	        }
	      }
        
        Collections.sort(vTipoEff, new TipoEffluentiComparator());       
                
        hTipoEffluentiTratt.put(new Long(idUte), vTipoEff);      
      }
      
      request.setAttribute("hTipoEffluentiTratt", hTipoEffluentiTratt);
      
      
      //Carico le formule per i trattamenti
      
      
           
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    
    
    //carico effluenti cessioni
    //HashMap di vettori di idUte, TipoEffluenteVO
    HashMap<Long,Vector<TipoEffluenteVO>> hTipoEffluentiCess = null;
    try 
    {
      String[] quantitaTratt = request.getParameterValues("quantitaTratt");
      String[] idEffluenteTratt = request.getParameterValues("idEffluenteTratt");
      String[] quantitaAzoto =  request.getParameterValues("quantitaAzoto");
      String[] quantitaPalDichTratt = request.getParameterValues("quantitaPalDichTratt");
      String[] azotoPalDichTratt = request.getParameterValues("azotoPalDichTratt");
      String[] quantitaNonPalDichTratt = request.getParameterValues("quantitaNonPalDichTratt");
      String[] azotoNonPalDichTratt = request.getParameterValues("azotoNonPalDichTratt");
      String[] idUteTratt = request.getParameterValues("idUteTratt");
      String[] idTrattamento = request.getParameterValues("idTrattamento");
      
      
      
      for(int i=0;i<vCom10r.size();i++)
      {
        if(hTipoEffluentiCess == null)
        {
          hTipoEffluentiCess = new HashMap<Long,Vector<TipoEffluenteVO>>();
        }
        long idUte = vCom10r.get(i).getIdUte();
        Vector<TipoEffluenteVO> vTipoEff = hTipoEffluentiTratt.get(new Long(idUte));
        Vector<TipoEffluenteVO> vTipoEffNew = null;
        Vector<TipoEffluenteVO> vTipoEffCess = new Vector<TipoEffluenteVO>();
        //Vector<Long> vIdEffluentiCess = new Vector<Long>();
        HashMap<Long, TipoEffluenteVO> hIdEffluentiCess = new HashMap<Long, TipoEffluenteVO>();  
        if(vTipoEff != null)
        {
          vTipoEffNew = new Vector<TipoEffluenteVO>();
          for(int g=0;g<vTipoEff.size();g++)
          {
            vTipoEffNew.add(TipoEffluenteVO.copyNewTipoEffluente(vTipoEff.get(g)));
          }
          
          if(Validator.isEmpty(regime))
          {
            if(vEffluentiTratt != null)
            {
              for(int h=0;h<vTipoEffNew.size();h++)
              {
                boolean trovatoEffluente = false;
                TipoEffluenteVO tipoEffTratt = vTipoEffNew.get(h);
                for(int k=0;k<vEffluentiTratt.size();k++)
                {
                  
                  if((tipoEffTratt.getIdEffluente() == vEffluentiTratt.get(k).getIdEffluenteOrigine().longValue())
                    && (vEffluentiTratt.get(k).getIdUte() == idUte))
                  {
                    trovatoEffluente = true;
                    BigDecimal quantitaBg = vEffluentiTratt.get(k).getVolumeIniziale();
                    BigDecimal azotoBg = vEffluentiTratt.get(k).getAzotoIniziale();
                    if(tipoEffTratt.getVolumeProdotto().compareTo(quantitaBg) > 0)
                    {
                      //if(!vIdEffluentiCess.contains(tipoEffTratt.getIdEffluente()))
                      if(hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente())) == null)
                      {
                        tipoEffTratt.setVolumeProdotto(tipoEffTratt.getVolumeProdotto().subtract(quantitaBg));
                        tipoEffTratt.setAzotoProdotto(tipoEffTratt.getAzotoProdotto().subtract(azotoBg));
                        
                        hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffTratt);
                        //vTipoEffCess.add(tipoEffTratt);
                        //vIdEffluentiCess.add(new Long(tipoEffTratt.getIdEffluente()));
                      }                                         
                    }
                    
                    Vector<TipoEffluenteVO> vEffTratt = gaaFacadeClient.getListTipoEffluenteAndValueByLegameId(
                      vCom10r.get(i).getIdComunicazione10R(), tipoEffTratt.getIdEffluente());
                    if(vEffTratt != null)
                    {
                      for(int j=0;j<vEffTratt.size();j++)
                      {
                        //if(!vIdEffluentiCess.contains(vEffTratt.get(j).getIdEffluente()))
                        if(hIdEffluentiCess.get(new Long(vEffTratt.get(j).getIdEffluente())) == null)
                        {
                          hIdEffluentiCess.put(new Long(vEffTratt.get(j).getIdEffluente()), vEffTratt.get(j));
                          
                          //vTipoEffCess.add(vEffTratt.get(j));
                          //vIdEffluentiCess.add(new Long(vEffTratt.get(j).getIdEffluente()));
                        }
                        else
                        {
                          TipoEffluenteVO tipoEffoVOCessTmp = hIdEffluentiCess.get(new Long(vEffTratt.get(j).getIdEffluente()));
                          tipoEffoVOCessTmp.setVolumeProdotto(tipoEffoVOCessTmp.getVolumeProdotto().add(vEffTratt.get(j).getVolumeProdotto()));
                          tipoEffoVOCessTmp.setAzotoProdotto(tipoEffoVOCessTmp.getAzotoProdotto().add(vEffTratt.get(j).getAzotoProdotto()));
                                     
                          hIdEffluentiCess.put(new Long(vEffTratt.get(j).getIdEffluente()), tipoEffoVOCessTmp);
                        }                         
                       
                      }                     
                    }               
                    
                    
                    
                  }
                }
                
                if(!trovatoEffluente)
                {
                  //vTipoEffCess.add(tipoEffTratt);
                  if(hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente())) == null)
                  {
                    hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffTratt);
                  }
                  else
                  {
                    TipoEffluenteVO tipoEffoVOCessTmp = hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente()));
                    tipoEffoVOCessTmp.setVolumeProdotto(tipoEffoVOCessTmp.getVolumeProdotto().add(tipoEffTratt.getVolumeProdotto()));
                    tipoEffoVOCessTmp.setAzotoProdotto(tipoEffoVOCessTmp.getAzotoProdotto().add(tipoEffTratt.getAzotoProdotto()));
                               
                    hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffoVOCessTmp);
                  }
                }
              
              }
            }
            else
            {
              vTipoEffCess = vTipoEffNew;
            }
          }
          else
          {          
	          if(Validator.isNotEmpty(idEffluenteTratt))
	          {
	            //effluenti che arrivano da acquisizioni e trattamenti!!!!!
	            for(int h=0;h<vTipoEffNew.size();h++)
	            {
	              TipoEffluenteVO tipoEffTratt = vTipoEffNew.get(h);
	              boolean trovatoEffluente = false;
	              for(int k=0;k<idEffluenteTratt.length;k++)
	              {
	                if(Validator.isNotEmpty(idEffluenteTratt[k])
	                  && Validator.isNotEmpty(quantitaTratt[k])  
	                  && (Validator.validateDouble(quantitaTratt[k], 999999.9999) != null)
	                  && Validator.isNotEmpty(quantitaAzoto[k])
	                  && new Long(idUteTratt[k]).longValue() == idUte)
	                { 
	                  if(tipoEffTratt.getIdEffluente() == new Long(idEffluenteTratt[k]).longValue())
	                  {
	                    trovatoEffluente = true;
	                    String quantitaStr = quantitaTratt[k].replace(',','.');
	                    BigDecimal quantitaBg = new BigDecimal(quantitaStr);
	                    String quantitaAzotoStr = quantitaAzoto[k].replace(',','.');
                      BigDecimal quantitaAzotoBg = new BigDecimal(quantitaAzotoStr);
	                    if(tipoEffTratt.getVolumeProdotto().compareTo(quantitaBg) > 0)
	                    {
	                      //if(!vIdEffluentiCess.contains(tipoEffTratt.getIdEffluente()))
	                      if(hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente())) == null)
	                      {
	                        tipoEffTratt.setVolumeProdotto(tipoEffTratt.getVolumeProdotto().subtract(quantitaBg));
	                        tipoEffTratt.setAzotoProdotto(tipoEffTratt.getAzotoProdotto().subtract(quantitaAzotoBg));	                        
	                        hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffTratt);
	                        //vTipoEffCess.add(tipoEffTratt);
	                        //vIdEffluentiCess.add(new Long(tipoEffTratt.getIdEffluente()));
	                      }                                         
	                    }
	                    EffluenteVO effVOTmp = new EffluenteVO();
	                    if(Validator.isNotEmpty(idUteTratt[k]))
	                      effVOTmp.setIdUte(new Long(idUteTratt[k]).longValue());
	                    effVOTmp.setIdEffluenteOrigineStr(idEffluenteTratt[k]);
	                    effVOTmp.setIdTrattamentoStr(idTrattamento[k]);
	                    effVOTmp.setVolumeInizialeStr(quantitaTratt[k]);
	                    effVOTmp.setVolumePostDichiaratoStr(quantitaPalDichTratt[k]);
	                    effVOTmp.setAzotoPostDichiaratoStr(azotoPalDichTratt[k]);
	                    effVOTmp.setVolumePostDichiaratoNonPalStr(quantitaNonPalDichTratt[k]);
                      effVOTmp.setAzotoPostDichiaratoNonPalStr(azotoNonPalDichTratt[k]);
	                    
				              Vector<TipoEffluenteVO> vEffTratt = gaaFacadeClient.getListTipoEffluenteByLegameId(tipoEffTratt.getIdEffluente());
				              if(vEffTratt != null)
                      {
                        for(int j=0;j<vEffTratt.size();j++)
                        {    
                          boolean palabile = true;                    
                          if("N".equalsIgnoreCase(vEffTratt.get(j).getFlagPalabile()))
                            palabile = false;
                          ValidationErrors errorsTmp = effVOTmp.validateConfermaCessazioni(palabile);
		                      if(errorsTmp.size() == 0)
                          {
		                        //if(!vIdEffluentiCess.contains(vEffTratt.get(j).getIdEffluente()))
		                        if(hIdEffluentiCess.get(new Long(vEffTratt.get(j).getIdEffluente())) == null)
			                      {			                        
			                        if(palabile)
			                        {
			                          vEffTratt.get(j).setVolumeProdotto(effVOTmp.getVolumePostDichiarato());
			                          vEffTratt.get(j).setAzotoProdotto(effVOTmp.getAzotoPostDichiarato());
			                        }
			                        else
			                        {
			                          vEffTratt.get(j).setVolumeProdotto(effVOTmp.getVolumePostDichiaratoNonPal());
                                vEffTratt.get(j).setAzotoProdotto(effVOTmp.getAzotoPostDichiaratoNonPal());
			                        }
			                        
			                        hIdEffluentiCess.put(new Long(vEffTratt.get(j).getIdEffluente()), vEffTratt.get(j));
			                        //vTipoEffCess.add(vEffTratt.get(j));
			                        //vIdEffluentiCess.add(new Long(vEffTratt.get(j).getIdEffluente()));
			                       
			                      }
			                      else
			                      {
			                        TipoEffluenteVO tipoEffoVOCessTmp = hIdEffluentiCess.get(new Long(vEffTratt.get(j).getIdEffluente()));
			                        if(palabile)
                              {
                                tipoEffoVOCessTmp.setVolumeProdotto(tipoEffoVOCessTmp.getVolumeProdotto().add(effVOTmp.getVolumePostDichiarato()));
                                tipoEffoVOCessTmp.setAzotoProdotto(tipoEffoVOCessTmp.getAzotoProdotto().add(effVOTmp.getAzotoPostDichiarato()));
                                //vEffTratt.get(j).setVolumeProdotto(effVOTmp.getVolumePostDichiarato());
                                //vEffTratt.get(j).setAzotoProdotto(effVOTmp.getAzotoPostDichiarato());
                              }
                              else
                              {
                                tipoEffoVOCessTmp.setVolumeProdotto(tipoEffoVOCessTmp.getVolumeProdotto().add(effVOTmp.getVolumePostDichiaratoNonPal()));
                                tipoEffoVOCessTmp.setAzotoProdotto(tipoEffoVOCessTmp.getAzotoProdotto().add(effVOTmp.getAzotoPostDichiaratoNonPal()));
                                //vEffTratt.get(j).setVolumeProdotto(effVOTmp.getVolumePostDichiaratoNonPal());
                                //vEffTratt.get(j).setAzotoProdotto(effVOTmp.getAzotoPostDichiaratoNonPal());
                              }
                              
                              //vTipoEffCess.add(vEffTratt.get(j));
                              //vIdEffluentiCess.add(new Long(vEffTratt.get(j).getIdEffluente()));
                              hIdEffluentiCess.put(new Long(vEffTratt.get(j).getIdEffluente()), tipoEffoVOCessTmp);			                      
			                      
			                      }                     
			                    }
		                    }         
	                    }
	                    
	                    break;
	                  }
	                  
	                }
	                /*else
	                {
	                  if(hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente())) == null)
	                  {
	                    hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffTratt);
	                  }
	                  else
	                  {
	                    TipoEffluenteVO tipoEffoVOCessTmp = hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente()));
	                    tipoEffoVOCessTmp.setVolumeProdotto(tipoEffoVOCessTmp.getVolumeProdotto().add(tipoEffTratt.getVolumeProdotto()));
	                    tipoEffoVOCessTmp.setAzotoProdotto(tipoEffoVOCessTmp.getAzotoProdotto().add(tipoEffTratt.getAzotoProdotto()));
	                               
	                    hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffoVOCessTmp);
	                  }
	                }*/
	              }
	              
	              //l'effluente non è stato trattato o ci sono degli errori nel trattamento
	              //quindi posso cederlo cosi' come è...
	              if(!trovatoEffluente)
	              {
	                //vTipoEffCess.add(tipoEffTratt);
	                if(hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente())) == null)
                  {
                    hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffTratt);
                  }
                  else
                  {
                    TipoEffluenteVO tipoEffoVOCessTmp = hIdEffluentiCess.get(new Long(tipoEffTratt.getIdEffluente()));
                    tipoEffoVOCessTmp.setVolumeProdotto(tipoEffoVOCessTmp.getVolumeProdotto().add(tipoEffTratt.getVolumeProdotto()));
                    tipoEffoVOCessTmp.setAzotoProdotto(tipoEffoVOCessTmp.getAzotoProdotto().add(tipoEffTratt.getAzotoProdotto()));
                               
	                  hIdEffluentiCess.put(new Long(tipoEffTratt.getIdEffluente()), tipoEffoVOCessTmp);
	                }
	              }
	            
	            }
	            
	          }
	          else
            {
              vTipoEffCess = vTipoEffNew;
            }
	          
	        } //else regime              
        }
        
        //almeno un trattamento!!!!
        if(hIdEffluentiCess.size() > 0)
        {
          vTipoEffCess = new Vector<TipoEffluenteVO>();
          Set<Long> list  = hIdEffluentiCess.keySet();
					Iterator<Long> iter = list.iterator();
					      
					while(iter.hasNext()) 
					{
					  Long key = iter.next();
					  vTipoEffCess.add(hIdEffluentiCess.get(key));
					}
        }
        
        Collections.sort(vTipoEffCess, new TipoEffluentiComparator());       
                
        hTipoEffluentiCess.put(new Long(idUte), vTipoEffCess);      
      }
      
      request.setAttribute("hTipoEffluentiCess", hTipoEffluentiCess);
      
      
      //Carico le formule per i trattamenti
      
      
           
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    
    
    
    
    
    
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciCessAcqu"))
    {
      if(vCessioniAcquisizioni == null)
      {
        vCessioniAcquisizioni = new Vector<EffluenteCesAcqVO>();
      }
      String cuaa = request.getParameter("cuaaCessAcqu");
      String denominazione = request.getParameter("denominazioneCessAcqu");
      
      //Controlli
      if(!Validator.isNotEmpty(cuaa) || !Validator.isNotEmpty(denominazione)) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessAcqu", AnagErrors.ERR_CUAA_DEN_OBBLIGATORI);
        errors = ErrorUtils.setValidErrNoNull(errors, "denominazioneCessAcqu", AnagErrors.ERR_CUAA_DEN_OBBLIGATORI);
      }
      // Se non lo è controllo la correttezza formale
      else 
      {
        // Se si tratta di un codice fiscale
        if(cuaa.length() == LUNG_MAX_CUAA) 
        {
          if(!Validator.controlloCf(cuaa)) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessAcqu", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
          }
        }
        else if (cuaa.length() == LUNG_MAX_PI) 
        {
          if(!Validator.controlloPIVA(cuaa)) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessAcqu", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
          }
        }
        else //diverso da 11 e da 16
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessAcqu", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      
      String istatComune = "";
      String comune = request.getParameter("comuneCessAcqu");
      String provincia = request.getParameter("provinciaCessAcqu");
      
      
      if(Validator.isNotEmpty(comune) && Validator.isNotEmpty(provincia))
      { 
        //caso in cui inseriti provincia e comune a mano
        //String istatComune = null;
        try 
        {
          istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(comune, provincia);
        }
        catch(SolmrException se) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "comuneCessAcqu", se.getMessage());
        }        
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "comuneCessAcqu", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
        errors = ErrorUtils.setValidErrNoNull(errors, "provinciaCessAcqu", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
      }
        
      
      
      if(errors !=null)
      {
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />
        <%
        return;
      }
      //Nel caso sia presente l'idAzienda uso questi per ricare il cuaa e la denominazione
      String idAziendaCessAcqu = request.getParameter("idAziendaCessAcqu");
      
      EffluenteCesAcqVO effCesAcq = new EffluenteCesAcqVO();
      
      if(!Validator.isNotEmpty(idAziendaCessAcqu))
      {
        AnagAziendaVO aziendaVOCessAcqu = new AnagAziendaVO(); 
        aziendaVOCessAcqu.setCUAA(cuaa);
        aziendaVOCessAcqu.setDenominazione(denominazione);
        Vector<Long> vectIdAnagAziendaInitial = null;
        try
        {
          //false prendo le aziende non provvisorie 
          vectIdAnagAziendaInitial = anagFacadeClient
            .getListIdAziendeFlagProvvisorio(aziendaVOCessAcqu, null,true,false);
        }
        catch (SolmrException se) 
        {}
        
        if((vectIdAnagAziendaInitial != null) && (vectIdAnagAziendaInitial.size() == 1))
        {
          effCesAcq.setIdAzienda((Long)vectIdAnagAziendaInitial.get(0));
        }
      }
      else
      {
        effCesAcq.setIdAzienda(new Long(idAziendaCessAcqu));
      }
      
      effCesAcq.setCuaa(cuaa.toUpperCase());
      effCesAcq.setDenominazione(denominazione.toUpperCase()); 
     
      effCesAcq.setIstatComune(istatComune);
      effCesAcq.setDescComune(comune.toUpperCase());
      effCesAcq.setSglProv(provincia.toUpperCase());
      
      vCessioniAcquisizioni.add(effCesAcq);
      
      session.setAttribute("vCessioniAcquisizioni", vCessioniAcquisizioni);
      
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciTrattamenti"))
    {
      //inserisco record vuoto
      if(vEffluentiTratt == null)
      {
        vEffluentiTratt = new Vector<EffluenteVO>();
      }
        
      EffluenteVO effVO = new EffluenteVO();
           
      vEffluentiTratt.add(effVO);
            
      session.setAttribute("vEffluentiTratt", vEffluentiTratt);
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciCessioni"))
    {
      if(vCessioni == null)
      {
        vCessioni = new Vector<EffluenteCesAcqVO>();
      }
      String cuaa = request.getParameter("cuaaCessioni");
      String denominazione = request.getParameter("denominazioneCessioni");
      
      //Controlli
      if(!Validator.isNotEmpty(cuaa) || !Validator.isNotEmpty(denominazione)) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessioni", AnagErrors.ERR_CUAA_DEN_OBBLIGATORI);
        errors = ErrorUtils.setValidErrNoNull(errors, "denominazioneCessioni", AnagErrors.ERR_CUAA_DEN_OBBLIGATORI);
      }
      // Se non lo è controllo la correttezza formale
      else 
      {
        // Se si tratta di un codice fiscale
        if(cuaa.length() == LUNG_MAX_CUAA) 
        {
          if(!Validator.controlloCf(cuaa)) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessioni", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
          }
        }
        else if (cuaa.length() == LUNG_MAX_PI) 
        {
          if(!Validator.controlloPIVA(cuaa)) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessioni", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
          }
        }
        else //diverso da 11 e da 16
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "cuaaCessioni", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      
      String istatComune = "";
      String comune = request.getParameter("comuneCessioni");
      String provincia = request.getParameter("provinciaCessioni");
      
      
      if(Validator.isNotEmpty(comune) && Validator.isNotEmpty(provincia))
      { 
        //caso in cui inseriti provincia e comune a mano
        //String istatComune = null;
        try 
        {
          istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(comune, provincia);
        }
        catch(SolmrException se) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "comuneCessioni", se.getMessage());
        }        
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "comuneCessioni", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
        errors = ErrorUtils.setValidErrNoNull(errors, "provinciaCessioni", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
      }
        
      
      
      if(errors !=null)
      {
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />
        <%
        return;
      }
      //Nel caso sia presente l'idAzienda uso questi per ricare il cuaa e la denominazione
      String idAziendaCessAcqu = request.getParameter("idAziendaCessioni");
      
      EffluenteCesAcqVO effCesAcq = new EffluenteCesAcqVO();
      
      if(!Validator.isNotEmpty(idAziendaCessAcqu))
      {
        AnagAziendaVO aziendaVOCessAcqu = new AnagAziendaVO(); 
        aziendaVOCessAcqu.setCUAA(cuaa);
        aziendaVOCessAcqu.setDenominazione(denominazione);
        Vector<Long> vectIdAnagAziendaInitial = null;
        try
        {
          //false prendo le aziende non provvisorie 
          vectIdAnagAziendaInitial = anagFacadeClient
            .getListIdAziendeFlagProvvisorio(aziendaVOCessAcqu, null,true,false);
        }
        catch (SolmrException se) 
        {}
        
        if((vectIdAnagAziendaInitial != null) && (vectIdAnagAziendaInitial.size() == 1))
        {
          effCesAcq.setIdAzienda((Long)vectIdAnagAziendaInitial.get(0));
        }
      }
      else
      {
        effCesAcq.setIdAzienda(new Long(idAziendaCessAcqu));
      }
      
      effCesAcq.setCuaa(cuaa.toUpperCase());
      effCesAcq.setDenominazione(denominazione.toUpperCase()); 
     
      effCesAcq.setIstatComune(istatComune);
      effCesAcq.setDescComune(comune.toUpperCase());
      effCesAcq.setSglProv(provincia.toUpperCase());
      
      vCessioni.add(effCesAcq);
      
      session.setAttribute("vCessioni", vCessioni);
      
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciStocc"))
    {
      if(vStoccaggio == null)
      {
        vStoccaggio = new Vector<EffluenteStocExtVO>();
      }
      
      String cuaa = request.getParameter("cuaaStocc");
      String denominazione = request.getParameter("denominazioneStocc");
      
      //Controlli
      if(!Validator.isNotEmpty(cuaa) || !Validator.isNotEmpty(denominazione)) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "cuaaStocc", AnagErrors.ERR_CUAA_DEN_OBBLIGATORI);
        errors = ErrorUtils.setValidErrNoNull(errors, "denominazioneStocc", AnagErrors.ERR_CUAA_DEN_OBBLIGATORI);
      }
      // Se non lo è controllo la correttezza formale
      else 
      {
        // Se si tratta di un codice fiscale
        if(cuaa.length() == LUNG_MAX_CUAA) 
        {
          if(!Validator.controlloCf(cuaa)) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "cuaaStocc", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
          }
        }
        else if (cuaa.length() == LUNG_MAX_PI) 
        {
          if(!Validator.controlloPIVA(cuaa)) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "cuaaStocc", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
          }
        }
        else //diverso da 11 e da 16
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "cuaaStocc", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      
      String istatComune = ""; //request.getParameter("istatComuneStocc");
      String comune = request.getParameter("comuneStocc");
      String provincia = request.getParameter("provinciaStocc");
      
      
      if(Validator.isNotEmpty(comune) && Validator.isNotEmpty(provincia))
      { 
        //caso in cui inseriti provincia e comune a mano
        //String istatComune = null;
        try 
        {
          istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(comune, provincia);
        }
        catch(SolmrException se) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "comuneStocc", se.getMessage());
        }        
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "comuneStocc", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
        errors = ErrorUtils.setValidErrNoNull(errors, "provinciaStocc", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
      }
      
      
      if(errors !=null)
      {
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />
        <%
        return;
      }
      
      String idAziendaStocc = request.getParameter("idAziendaStocc");
      
      EffluenteStocExtVO effStocExt = new EffluenteStocExtVO();
      
      if(!Validator.isNotEmpty(idAziendaStocc))
      {
        AnagAziendaVO aziendaVOStocExt = new AnagAziendaVO(); 
        aziendaVOStocExt.setCUAA(cuaa);
        aziendaVOStocExt.setDenominazione(denominazione);
        Vector<Long> vectIdAnagAziendaInitial = null;
        try
        {
          //false prendo le aziende non provvisorie 
          vectIdAnagAziendaInitial = anagFacadeClient
            .getListIdAziendeFlagProvvisorio(aziendaVOStocExt, null,true,false);
        }
        catch (SolmrException se) 
        {}
        
        if((vectIdAnagAziendaInitial != null) && (vectIdAnagAziendaInitial.size() == 1))
        {
          effStocExt.setIdAzienda((Long)vectIdAnagAziendaInitial.get(0));
        }
      }
      else
      {
        effStocExt.setIdAzienda(new Long(idAziendaStocc));
      } 
     
      effStocExt.setCuaa(cuaa.toUpperCase());
      effStocExt.setDenominazione(denominazione.toUpperCase());
      effStocExt.setIstatComune(istatComune);
      effStocExt.setDescComune(comune.toUpperCase());
      effStocExt.setSglProv(provincia.toUpperCase());
      
      vStoccaggio.add(effStocExt);
      
      
      
      session.setAttribute("vStoccaggio", vStoccaggio);
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciAcqueReflue"))
    {
      //inserisco record vuoto
      if(vAcqueReflue == null)
      {
        vAcqueReflue = new Vector<AcquaExtraVO>();
      }
        
      AcquaExtraVO acqueReflueExt = new AcquaExtraVO();
           
      vAcqueReflue.add(acqueReflueExt);
            
      session.setAttribute("vAcqueReflue", vAcqueReflue);
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciVolSot"))
    {
      //inserisco record vuoto
      if(vVolumeSottogrigliato == null)
      {
        vVolumeSottogrigliato = new Vector<GenericQuantitaVO>();
      }
        
      GenericQuantitaVO gqVO = new GenericQuantitaVO();
           
      vVolumeSottogrigliato.add(gqVO);
            
      session.setAttribute("vVolumeSottogrigliato", vVolumeSottogrigliato);
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisciVolRefAz"))
    {
      //inserisco record vuoto
      if(vVolumeRefluoAzienda == null)
      {
        vVolumeRefluoAzienda = new Vector<GenericQuantitaVO>();
      }
        
      GenericQuantitaVO gqVO = new GenericQuantitaVO();
           
      vVolumeRefluoAzienda.add(gqVO);
            
      session.setAttribute("vVolumeRefluoAzienda", vVolumeRefluoAzienda);
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaCessAcqu"))
    {
      String[] chkCessAcqu = request.getParameterValues("chkCessAcqu");
      String[] chkStoccaggioCessAcqu = request.getParameterValues("chkStoccaggioCessAcqu");
      
      
      
      //Acquisizioni
      //String[] idCausaleEffluente = request.getParameterValues("idCausaleEffluente");
      String[] idEffluente = request.getParameterValues("idEffluente");
      String[] quantitaCessAcqu = request.getParameterValues("quantitaCessAcqu");
      String[] idUteCessAcqu = request.getParameterValues("idUteCessAcqu");
      String[] azotoDichiaratoCessAcqu = request.getParameterValues("azotoDichiaratoCessAcqu");
      
      HashMap<Integer,ValidationErrors> hElencoErroriCessAcqu = new HashMap<Integer,ValidationErrors>();;
      ValidationErrors errorsCessAcqu = null;
      //HashMap<String,BigDecimal> hElencoQuantitaCessione = new HashMap<String,BigDecimal>(); // utilizzata per avere il totale delle cessioni
      if(vCessioniAcquisizioni != null)
      {
        for(int i=0;i<vCessioniAcquisizioni.size();i++)
        {
          errorsCessAcqu = null;
          EffluenteCesAcqVO effCessAcqu = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
          //effCessAcqu.setIdCausaleEffluenteStr(idCausaleEffluente[i]);
          effCessAcqu.setIdEffluenteStr(idEffluente[i]);
          effCessAcqu.setQuantitaStr(quantitaCessAcqu[i]);
          effCessAcqu.setQuantitaAzotoDichiaratoStr(azotoDichiaratoCessAcqu[i]);
          
          if(Validator.isNotEmpty(idUteCessAcqu[i]))
          {
            effCessAcqu.setIdUte(new Long(idUteCessAcqu[i]).longValue());
          }
          else
          {
            effCessAcqu.setIdUte(0);
          }
          
          //Assegno i flag stoccaggio
          effCessAcqu.setFlagStoccaggio("N");
          if(Validator.isNotEmpty(chkStoccaggioCessAcqu))
          {
            for(int j=0;j<chkStoccaggioCessAcqu.length;j++)
            {
              //Se non è checkato non esiste la riga dell'array
              if(i == new Integer(chkStoccaggioCessAcqu[j]).intValue())
              {
                effCessAcqu.setFlagStoccaggio("S");
                break;
              }
            }          
          }
          
          //Associo la comunicazione 10R relativa all'ute
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
              
            if(com10.getIdUte() == effCessAcqu.getIdUte())
            {
              effCessAcqu.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
          
          
          errorsCessAcqu = effCessAcqu.validateConferma(errorsCessAcqu);
          
          if(errorsCessAcqu != null)
          {
            hElencoErroriCessAcqu.put(new Integer(i),errorsCessAcqu);
          }
        }
      }
      
      
      
      
      //Trattamenti
      String[] idUteTratt = request.getParameterValues("idUteTratt");
      String[] idEffluenteTratt = request.getParameterValues("idEffluenteTratt");
      String[] idTrattamento = request.getParameterValues("idTrattamento");
      String[] quantitaTratt = request.getParameterValues("quantitaTratt");
      String[] quantitaAzoto = request.getParameterValues("quantitaAzoto");
      String[] quantitaPalDichTratt = request.getParameterValues("quantitaPalDichTratt");
      String[] azotoPalDichTratt = request.getParameterValues("azotoPalDichTratt");
      String[] quantitaNonPalDichTratt = request.getParameterValues("quantitaNonPalDichTratt");
      String[] azotoNonPalDichTratt = request.getParameterValues("azotoNonPalDichTratt");
      String[] quantitaPreTratt = request.getParameterValues("quantitaPreTratt");
      String[] quantitaPalPostTratt = request.getParameterValues("quantitaPalPostTratt");
      String[] azotoPalPostTratt = request.getParameterValues("azotoPalPostTratt");
      String[] quantitaNonPalPostTratt = request.getParameterValues("quantitaNonPalPostTratt");
      String[] azotoNonPalPostTratt = request.getParameterValues("azotoNonPalPostTratt");
      
      HashMap<Integer,ValidationErrors> hElencoErroriTratt = null;
      //HashMap<String, String> hEffTratt = new HashMap<String, String>();
      ValidationErrors errorsTratt = null;
      if(vEffluentiTratt !=null)
      {
        for(int i=0;i<vEffluentiTratt.size();i++)
        {
          errorsTratt = null;
          
          EffluenteVO eff = vEffluentiTratt.get(i);
          
          if(Validator.isNotEmpty(idUteTratt[i]))
          {
            eff.setIdUte(new Long(idUteTratt[i]).longValue());
          }
          else
          {
            eff.setIdUte(0);
          }
          
          eff.setIdEffluenteOrigineStr(idEffluenteTratt[i]);
          eff.setIdTrattamentoStr(idTrattamento[i]);
          eff.setVolumeInizialeStr(quantitaTratt[i]);
          eff.setAzotoInizialeStr(quantitaAzoto[i]);
          eff.setVolumePostDichiaratoStr(quantitaPalDichTratt[i]);
          eff.setAzotoPostDichiaratoStr(azotoPalDichTratt[i]);
          eff.setVolumePostDichiaratoNonPalStr(quantitaNonPalDichTratt[i]);
          eff.setAzotoPostDichiaratoNonPalStr(azotoNonPalDichTratt[i]);
          eff.setVolumePreTrattStr(quantitaPreTratt[i]);
          eff.setMaxVolumePostDichiaratoStr(quantitaPalPostTratt[i]);
          eff.setMaxAzotoPostDichiaratoStr(azotoPalPostTratt[i]);
          eff.setMaxVolumePostDichiaratoNonPalStr(quantitaNonPalPostTratt[i]);
          eff.setMaxAzotoPostDichiaratoNonPalStr(azotoNonPalPostTratt[i]);
          //Associo l'idComunicazione 10R all'ute.
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute                 
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == eff.getIdUte())
            {
              eff.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
         
          
          
          errorsTratt = eff.validateConferma();
          if(errorsTratt != null)
          {
            if(hElencoErroriTratt == null)
            {
              hElencoErroriTratt = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriTratt.put(new Integer(i), errorsTratt);
          }
          else
          {
            Vector<TipoEffluenteVO> vEffTratt = gaaFacadeClient.getListTipoEffluenteByLegameId(eff.getIdEffluenteOrigine());
            if(vEffTratt != null)
            {
              for(int j=0;j<vEffTratt.size();j++)
              {
                if("N".equalsIgnoreCase(vEffTratt.get(j).getFlagPalabile()))
                {
                  eff.setIdEffluenteNonPalabile(new Long(vEffTratt.get(j).getIdEffluente()));
                }
                else
                {
                  eff.setIdEffluentePalabile(new Long(vEffTratt.get(j).getIdEffluente()));
                }
              }
            }
          }
        }
      }
      
      
      
      
      
      for(int i=0;i<vCessioniAcquisizioni.size();i++)
      {
        if(Validator.isNotEmpty(chkStoccaggioCessAcqu))
        {
          EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
          
          boolean trovato = false;
          for(int j=0;j<chkStoccaggioCessAcqu.length;j++)
          {
            if(i == new Integer(chkStoccaggioCessAcqu[j]).intValue())
            {
              trovato = true;
              break;
            }
          }
          
          if(trovato)
          {
            effVO.setFlagStoccaggio("S");
            effVO.setFlagStoccaggioBl(true);         
            
          }
          else
          {
            effVO.setFlagStoccaggio("N");
            effVO.setFlagStoccaggioBl(false);
          }
        }
        
        
        if(Validator.isNotEmpty(chkCessAcqu))
        {
          EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
          
          boolean trovato = false;
          for(int j=0;j<chkCessAcqu.length;j++)
          {
            if(i == new Integer(chkCessAcqu[j]).intValue())
            {
              trovato = true;
              break;
            }
          }
          
          //Solo quelli che non hanno errori...
          if(trovato && (hElencoErroriCessAcqu.get(new Integer(i)) == null))
          {            
            if(hTipoEffluentiTratt != null)
            {
              Vector<TipoEffluenteVO> vTipoEff = hTipoEffluentiTratt.get(new Long(effVO.getIdUte()));
              if(vTipoEff != null)
              {
                for(int h=0;h<vTipoEff.size();h++)
                {
                  TipoEffluenteVO tipoEffluenteVOTmp = vTipoEff.get(h);
                  if(effVO.getIdEffluente().longValue() == tipoEffluenteVOTmp.getIdEffluente())
                  {
                    tipoEffluenteVOTmp.setVolumeProdotto(tipoEffluenteVOTmp.getVolumeProdotto().subtract(effVO.getQuantita()));
                    tipoEffluenteVOTmp.setAzotoProdotto(tipoEffluenteVOTmp.getAzotoProdotto().subtract(effVO.getQuantitaAzoto()));
                    if(tipoEffluenteVOTmp.getVolumeProdotto().compareTo(new BigDecimal(0)) <= 0)
                    {
                      vTipoEff.remove(h);
                      h--;
                    }
                  }
                  
                  
                }
                hTipoEffluentiTratt.put(new Long(effVO.getIdUte()), vTipoEff);
                
              }
              
              request.setAttribute("hTipoEffluentiTratt",hTipoEffluentiTratt); 
            }
            
            
            //HashMap di vettori di idUte, TipoEffluenteVO
            if(hTipoEffluentiCess != null)
            {
              //tolgo nelle cessoni da aquisizioni
              Vector<TipoEffluenteVO> vTipoEff = hTipoEffluentiCess.get(new Long(effVO.getIdUte()));
              if(vTipoEff != null)
              {
                if(vEffluentiTratt !=null)
                {
                  //Padri
                  for(int h=0;h<vTipoEff.size();h++)
                  {
                    boolean trovatoPadre = false;
                    TipoEffluenteVO tipoEffluenteVOTmp = vTipoEff.get(h);
                    for(int d=0;d<vEffluentiTratt.size();d++)
                    {                    
                      EffluenteVO effTratt = vEffluentiTratt.get(d);
	                    if(effTratt.getIdEffluenteOrigine().longValue() == tipoEffluenteVOTmp.getIdEffluente())
	                    {
                        trovatoPadre = true;
                        BigDecimal quantitaTrattamento = effTratt.getVolumeIniziale();
                        BigDecimal azotoTrattamento = effTratt.getAzotoIniziale();
                        //Sommo il trattamento lavorato che non ho più eliminando l'acquisizione
                        quantitaTrattamento = quantitaTrattamento.add(tipoEffluenteVOTmp.getVolumeProdotto());
                        azotoTrattamento = azotoTrattamento.add(tipoEffluenteVOTmp.getAzotoProdotto());
                        //Sottraggo l'acquisizone eliminata
                        quantitaTrattamento = quantitaTrattamento.subtract(effVO.getQuantita());
                        azotoTrattamento = azotoTrattamento.subtract(effVO.getQuantitaAzoto());
                        if(quantitaTrattamento.compareTo(new BigDecimal(0)) <= 0)
                        {
                          vTipoEff.remove(h);
                          h--;
                        }
                        else
                        {
                          tipoEffluenteVOTmp.setVolumeProdotto(quantitaTrattamento);
                          tipoEffluenteVOTmp.setAzotoProdotto(azotoTrattamento);
                        }
                        
                        break;
                      }                     
                    }
                    
                    if(!trovatoPadre)
                    {
                      tipoEffluenteVOTmp.getVolumeProdotto().subtract(effVO.getQuantita());
                      tipoEffluenteVOTmp.getAzotoProdotto().subtract(effVO.getQuantitaAzoto());
                      if(tipoEffluenteVOTmp.getVolumeProdotto().compareTo(new BigDecimal(0)) <= 0)
                      {
                        vTipoEff.remove(h);
                        h--;
                      }
                    }
                  }
                  
                  
                  //Per i trattati
                  for(int d=0;d<vEffluentiTratt.size();d++)
                  {
                    EffluenteVO effTratt = vEffluentiTratt.get(d);
                    for(int h=0;h<vTipoEff.size();h++)
                    {
	                    TipoEffluenteVO tipoEffluenteVOTmp = vTipoEff.get(h);                    
	                    if(Validator.isNotEmpty(effTratt.getIdEffluentePalabile())
	                      && (effTratt.getIdEffluentePalabile().longValue() == tipoEffluenteVOTmp.getIdEffluente()))
	                    {
	                      BigDecimal totale = effTratt.getVolumeIniziale().subtract(effVO.getQuantita());
	                      BigDecimal totaleAzoto = effTratt.getAzotoIniziale().subtract(effVO.getQuantitaAzoto());
	                      //non esiste più trattabile elimino anche i trattati
	                      if(totale.compareTo(new BigDecimal(0)) <=0)
	                      {
	                        vTipoEff.remove(h);
                          h--;
	                      }
	                      else
	                      {
	                        //cerco la rimanenza e la carico
	                        BigDecimal totaleQuantita = totale.multiply(effTratt.getVolumeIniziale());
	                        totaleQuantita.divide(effTratt.getVolumePostDichiarato(),1,BigDecimal.ROUND_HALF_UP);
	                        tipoEffluenteVOTmp.setVolumeProdotto(totaleQuantita);
	                        
	                        BigDecimal totaleQuantitaAzoto = totaleAzoto.multiply(effTratt.getAzotoIniziale());
                          totaleQuantitaAzoto.divide(effTratt.getAzotoPostDichiarato(),1,BigDecimal.ROUND_HALF_UP);
                          tipoEffluenteVOTmp.setAzotoProdotto(totaleQuantitaAzoto);
	                      }
	                    }
	                    
	                    if(Validator.isNotEmpty(effTratt.getIdEffluenteNonPalabile())
                        && (effTratt.getIdEffluenteNonPalabile().longValue() == tipoEffluenteVOTmp.getIdEffluente()))
                      {
                        BigDecimal totale = effTratt.getVolumeIniziale().subtract(effVO.getQuantita());
                        BigDecimal totaleAzoto = effTratt.getAzotoIniziale().subtract(effVO.getQuantitaAzoto());
                        //non esiste più trattabile elimino anche i trattati
                        if(totale.compareTo(new BigDecimal(0)) <=0)
                        {
                          vTipoEff.remove(h);
                          h--;
                        }
                        else
                        {
                          //cerco la rimanenza e la carico
                          BigDecimal totaleQuantita = totale.multiply(effTratt.getVolumeIniziale());
                          totaleQuantita.divide(effTratt.getVolumePostDichiaratoNonPal(),1,BigDecimal.ROUND_HALF_UP);
                          tipoEffluenteVOTmp.setVolumeProdotto(totaleQuantita);
                          
                          BigDecimal totaleQuantitaAzoto = totaleAzoto.multiply(effTratt.getAzotoIniziale());
                          totaleQuantitaAzoto.divide(effTratt.getAzotoPostDichiaratoNonPal(),1,BigDecimal.ROUND_HALF_UP);
                          tipoEffluenteVOTmp.setAzotoProdotto(totaleQuantitaAzoto);
                        }
                      }
	                    
	                  }
                    
                  }
                }
                else
                {
                  for(int h=0;h<vTipoEff.size();h++)
                  {
                    TipoEffluenteVO tipoEffluenteVOTmp = vTipoEff.get(h);
                    if(effVO.getIdEffluente().longValue() == tipoEffluenteVOTmp.getIdEffluente())
                    {                    
                      tipoEffluenteVOTmp.getVolumeProdotto().subtract(effVO.getQuantita());
                      tipoEffluenteVOTmp.getAzotoProdotto().subtract(effVO.getQuantitaAzoto());
                      if(tipoEffluenteVOTmp.getVolumeProdotto().compareTo(new BigDecimal(0)) <= 0)
		                  {
		                    vTipoEff.remove(h);
		                    h--;
		                  }
		                }
		              }
                }
              }
              
              
              hTipoEffluentiCess.put(new Long(effVO.getIdUte()), vTipoEff); 
            }
                         
            
          }
           
        }
        
        
              
      }
      
      HashMap<Integer,Integer> hChkCessAcqu = null;
      if(Validator.isNotEmpty(chkCessAcqu))
      {
        hChkCessAcqu = new HashMap<Integer,Integer>();
        for(int i=0;i<chkCessAcqu.length;i++)
        {
          hChkCessAcqu.put(new Integer(chkCessAcqu[i]),new Integer(chkCessAcqu[i]));
        }
        
        request.setAttribute("hChkCessAcqu",hChkCessAcqu); 
      } 
      
      if(Validator.isNotEmpty(chkCessAcqu))
      {
        for(int i=(chkCessAcqu.length - 1);i>=0;i--)
        {
          vCessioniAcquisizioni.remove(new Integer(chkCessAcqu[i]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaCessAcqu",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if(vCessioniAcquisizioni.size() == 0)
      {
        vCessioniAcquisizioni = null;
      }
      
     
      
      
      session.setAttribute("vCessioniAcquisizioni",vCessioniAcquisizioni);
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaTrattamento"))
    {
      String[] chkTrattamento = request.getParameterValues("chkTrattamento");
      
      
      
      
      //Trattamenti
      String[] idUteTratt = request.getParameterValues("idUteTratt");
      String[] idEffluenteTratt = request.getParameterValues("idEffluenteTratt");
      String[] idTrattamento = request.getParameterValues("idTrattamento");
      String[] quantitaTratt = request.getParameterValues("quantitaTratt");
      String[] quantitaAzoto = request.getParameterValues("quantitaAzoto");
      String[] quantitaPalDichTratt = request.getParameterValues("quantitaPalDichTratt");
      String[] azotoPalDichTratt = request.getParameterValues("azotoPalDichTratt");
      String[] quantitaNonPalDichTratt = request.getParameterValues("quantitaNonPalDichTratt");
      String[] azotoNonPalDichTratt = request.getParameterValues("azotoNonPalDichTratt");
      String[] quantitaPreTratt = request.getParameterValues("quantitaPreTratt");
      String[] quantitaPalPostTratt = request.getParameterValues("quantitaPalPostTratt");
      String[] azotoPalPostTratt = request.getParameterValues("azotoPalPostTratt");
      String[] quantitaNonPalPostTratt = request.getParameterValues("quantitaNonPalPostTratt");
      String[] azotoNonPalPostTratt = request.getParameterValues("azotoNonPalPostTratt");
      
      HashMap<Integer,ValidationErrors> hElencoErroriTratt = null;
      ValidationErrors errorsTratt = null;
      HashMap<String, String> hEffTratt = new HashMap<String, String>();
      HashMap<String, BigDecimal> hUteTipoEffluTrattMax = new HashMap<String, BigDecimal>();
      if(vEffluentiTratt !=null)
      {
        for(int i=0;i<vEffluentiTratt.size();i++)
        {
          errorsTratt = null;
          
          EffluenteVO eff = vEffluentiTratt.get(i);
          
          if(Validator.isNotEmpty(idUteTratt[i]))
          {
            eff.setIdUte(new Long(idUteTratt[i]).longValue());
          }
          else
          {
            eff.setIdUte(0);
          }
          
          eff.setIdEffluenteOrigineStr(idEffluenteTratt[i]);
          eff.setIdTrattamentoStr(idTrattamento[i]);
          eff.setVolumeInizialeStr(quantitaTratt[i]);
          eff.setAzotoInizialeStr(quantitaAzoto[i]);
          eff.setVolumePostDichiaratoStr(quantitaPalDichTratt[i]);
          eff.setAzotoPostDichiaratoStr(azotoPalDichTratt[i]);
          eff.setVolumePostDichiaratoNonPalStr(quantitaNonPalDichTratt[i]);
          eff.setAzotoPostDichiaratoNonPalStr(azotoNonPalDichTratt[i]);
          eff.setVolumePreTrattStr(quantitaPreTratt[i]);
          eff.setMaxVolumePostDichiaratoStr(quantitaPalPostTratt[i]);
          eff.setMaxAzotoPostDichiaratoStr(azotoPalPostTratt[i]);
          eff.setMaxVolumePostDichiaratoNonPalStr(quantitaNonPalPostTratt[i]);
          eff.setMaxAzotoPostDichiaratoNonPalStr(azotoNonPalPostTratt[i]);
          //Associo l'idComunicazione 10R all'ute.
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute                 
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == eff.getIdUte())
            {
              eff.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
         
          
          
          errorsTratt = eff.validateConferma();          
          
          if(errorsTratt == null)
          {
            String key = eff.getIdUte()+"_"+eff.getIdEffluenteOrigineStr()+"_"+eff.getIdTrattamentoStr();
            if(hEffTratt.get(key) != null)
            {
             
              errorsTratt = ErrorUtils.setValidErrNoNull(errorsTratt, "idUteTratt", AnagErrors.ERR_TRATT_EFF);
              errorsTratt = ErrorUtils.setValidErrNoNull(errorsTratt, "idEffluenteTratt", AnagErrors.ERR_TRATT_EFF);
              errorsTratt = ErrorUtils.setValidErrNoNull(errorsTratt, "idTrattamento", AnagErrors.ERR_TRATT_EFF);
            }
            else
            {
              hEffTratt.put(key, "true");
              String keyMax = eff.getIdUte()+"_"+eff.getIdEffluenteOrigineStr();
              if(hUteTipoEffluTrattMax.get(keyMax) != null)
              {
                BigDecimal valoreBg = hUteTipoEffluTrattMax.get(keyMax);
                valoreBg = valoreBg.add(eff.getVolumeIniziale());
                if(valoreBg.compareTo(eff.getVolumePreTratt()) > 0)
                {
                  errorsTratt = ErrorUtils.setValidErrNoNull(errors, "quantitaTratt", AnagErrors.ERR_SUP_TRATT_CALC);
                }
              }
              else
              {
                hUteTipoEffluTrattMax.put(keyMax, eff.getVolumeIniziale());
              }
              
            }
          }
          
          
          
          
          
          
          
          
          
          if(errorsTratt != null)
          {
            if(hElencoErroriTratt == null)
            {
              hElencoErroriTratt = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriTratt.put(new Integer(i), errorsTratt);
          }
          else
          {
            Vector<TipoEffluenteVO> vEffTratt = gaaFacadeClient.getListTipoEffluenteByLegameId(eff.getIdEffluenteOrigine());
            if(vEffTratt != null)
            {
              for(int j=0;j<vEffTratt.size();j++)
              {
                if("N".equalsIgnoreCase(vEffTratt.get(j).getFlagPalabile()))
                {
                  eff.setIdEffluenteNonPalabile(new Long(vEffTratt.get(j).getIdEffluente()));
                }
                else
                {
                  eff.setIdEffluentePalabile(new Long(vEffTratt.get(j).getIdEffluente()));
                }
              }
            }
          }
        }
      }
      
      if(vEffluentiTratt !=null)
      {
	      for(int i=0;i<vEffluentiTratt.size();i++)
	      {
	        if(Validator.isNotEmpty(chkTrattamento))
	        {
	          EffluenteVO effTrattVO = vEffluentiTratt.get(i);
	        
		        boolean trovato = false;
		        for(int j=0;j<chkTrattamento.length;j++)
		        {
		          if(i == new Integer(chkTrattamento[j]).intValue())
		          {
		            trovato = true;
		            break;
		          }
		        }
	        
	          //Solo quelli che non hanno errori...
	          if(trovato && (Validator.isEmpty(hElencoErroriTratt)
	             || Validator.isNotEmpty(hElencoErroriTratt) && (hElencoErroriTratt.get(new Integer(i)) == null)))
	          {
	            if(hTipoEffluentiCess != null)
	            {
	              //tolgo nelle cessoni da aquisizioni
	              Vector<TipoEffluenteVO> vTipoEff = hTipoEffluentiCess.get(new Long(effTrattVO.getIdUte()));
	              if(vTipoEff != null)
	              {	                	              
	                //Padri
	                boolean trovatoPadre = false;
	                TipoEffluenteVO tipoEffluenteVOTmp = null;
	                for(int h=0;h<vTipoEff.size();h++)
	                {
	                  tipoEffluenteVOTmp = vTipoEff.get(h);
	                  
	                  if(effTrattVO.getIdEffluenteOrigine().longValue() == tipoEffluenteVOTmp.getIdEffluente())
	                  {
	                    trovatoPadre = true;
	                    BigDecimal quantitaTrattamento = effTrattVO.getVolumeIniziale();
	                    BigDecimal azotoTrattamento = effTrattVO.getAzotoIniziale();
	                    //Sommo il trattamento lavorato che rimane dall'acquisizione
	                    quantitaTrattamento = quantitaTrattamento.add(tipoEffluenteVOTmp.getVolumeProdotto());
	                    azotoTrattamento = azotoTrattamento.add(tipoEffluenteVOTmp.getAzotoProdotto());
	                    tipoEffluenteVOTmp.setVolumeProdotto(quantitaTrattamento);
	                    tipoEffluenteVOTmp.setAzotoProdotto(azotoTrattamento);
	   
	                  }
	                  
	                  if(Validator.isNotEmpty(effTrattVO.getIdEffluentePalabile())
                      && (effTrattVO.getIdEffluentePalabile().longValue() == tipoEffluenteVOTmp.getIdEffluente()))
                    {
                      BigDecimal totale = tipoEffluenteVOTmp.getVolumeProdotto().subtract(effTrattVO.getVolumePostDichiarato());
                      BigDecimal totaleAzoto = tipoEffluenteVOTmp.getAzotoProdotto().subtract(effTrattVO.getAzotoPostDichiarato());
                      //non esiste più trattabile elimino anche i trattati
                      if(totale.compareTo(new BigDecimal(0)) <=0)
                      {
                        vTipoEff.remove(h);
                        h--;
                      }
                      else
                      {
                        //cerco la rimanenza e la carico
                        BigDecimal totaleQuantita = totale.multiply(effTrattVO.getVolumeIniziale());
                        totaleQuantita.divide(effTrattVO.getVolumePostDichiarato(),1,BigDecimal.ROUND_HALF_UP);
                        tipoEffluenteVOTmp.setVolumeProdotto(totaleQuantita);
                         
                        BigDecimal totaleQuantitaAzoto = totaleAzoto.multiply(effTrattVO.getAzotoIniziale());
                        totaleQuantitaAzoto.divide(effTrattVO.getAzotoPostDichiarato(),1,BigDecimal.ROUND_HALF_UP);
                        tipoEffluenteVOTmp.setAzotoProdotto(totaleQuantitaAzoto);
                      }
                    }
                        
                    if(Validator.isNotEmpty(effTrattVO.getIdEffluenteNonPalabile())
                      && (effTrattVO.getIdEffluenteNonPalabile().longValue() == tipoEffluenteVOTmp.getIdEffluente()))
                    {
                      BigDecimal totale = tipoEffluenteVOTmp.getVolumeProdotto().subtract(effTrattVO.getVolumePostDichiaratoNonPal());
                      BigDecimal totaleAzoto = tipoEffluenteVOTmp.getAzotoProdotto().subtract(effTrattVO.getAzotoPostDichiaratoNonPal());
                      //non esiste più trattabile elimino anche i trattati
                      if(totale.compareTo(new BigDecimal(0)) <=0)
                      {
                        vTipoEff.remove(h);
                        h--;
                      }
                      else
                      {
                        //cerco la rimanenza e la carico
                        BigDecimal totaleQuantita = totale.multiply(effTrattVO.getVolumeIniziale());
                        totaleQuantita.divide(effTrattVO.getVolumePostDichiaratoNonPal(),1,BigDecimal.ROUND_HALF_UP);
                        tipoEffluenteVOTmp.setVolumeProdotto(totaleQuantita);
                         
                        BigDecimal totaleQuantitaAzoto = totaleAzoto.multiply(effTrattVO.getAzotoIniziale());
                        totaleQuantitaAzoto.divide(effTrattVO.getAzotoPostDichiaratoNonPal(),1,BigDecimal.ROUND_HALF_UP);
                        tipoEffluenteVOTmp.setAzotoProdotto(totaleQuantitaAzoto);
                      }
                    }
                  }
	                
	                    
	                if(trovatoPadre)
	                {
	                  TipoEffluenteVO tipoEffluenteNewVO = new TipoEffluenteVO();
	                  BigDecimal quantitaTrattamento = effTrattVO.getVolumeIniziale();
                    BigDecimal azotoTrattamento = effTrattVO.getAzotoIniziale();
                    tipoEffluenteNewVO.setVolumeProdotto(quantitaTrattamento);
                    tipoEffluenteNewVO.setAzotoProdotto(azotoTrattamento);
                    tipoEffluenteNewVO.setIdEffluente(effTrattVO.getIdEffluenteOrigine());                    
                    vTipoEff.add(tipoEffluenteNewVO);
	                }
	              }	                  
	                
	              
	              Collections.sort(vTipoEff, new TipoEffluentiComparator());
	              hTipoEffluentiCess.put(new Long(effTrattVO.getIdUte()), vTipoEff); 
	            }
	          
	          
	          }
	        }
	      }
	    }
      
      
      
      
      
      
      
      
      HashMap<Integer,Integer> hChkTrattamento = null;
      if(Validator.isNotEmpty(chkTrattamento))
      {
        hChkTrattamento = new HashMap<Integer,Integer>();
        for(int i=0;i<chkTrattamento.length;i++)
        {
          hChkTrattamento.put(new Integer(chkTrattamento[i]),new Integer(chkTrattamento[i]));
        }
        
        request.setAttribute("hChkTrattamento",hChkTrattamento); 
      } 
      
      if(Validator.isNotEmpty(chkTrattamento))
      {
        for(int i=(chkTrattamento.length - 1);i>=0;i--)
        {
          vEffluentiTratt.remove(new Integer(chkTrattamento[i]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaTrattamento",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if(vEffluentiTratt.size() == 0)
      {
        vEffluentiTratt = null;
      }
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaCessioni"))
    {
      String[] chkCessioni = request.getParameterValues("chkCessioni");
      String[] quantitaCessioni = request.getParameterValues("quantitaCessioni");
      String[] azotoCessioni = request.getParameterValues("azotoCessioni");
      String[] idEffluenteCessioni = request.getParameterValues("idEffluenteCessioni");
      String[] idUteCessioni = request.getParameterValues("idUteCessioni");
      String[] chkStoccaggioCessioni = request.getParameterValues("chkStoccaggioCessioni");
      
  
      if(vCessioni != null)
      {
        for(int i=0;i<vCessioni.size();i++)
        {
          EffluenteCesAcqVO effCess = (EffluenteCesAcqVO)vCessioni.get(i);
          //effCessAcqu.setIdCausaleEffluenteStr(idCausaleEffluente[i]);
          effCess.setIdEffluenteStr(idEffluenteCessioni[i]);
          effCess.setQuantitaStr(quantitaCessioni[i]);
          effCess.setQuantitaAzotoDichiaratoStr(azotoCessioni[i]);
          
          if(Validator.isNotEmpty(idUteCessioni[i]))
          {
            effCess.setIdUte(new Long(idUteCessioni[i]).longValue());
          }
          else
          {
            effCess.setIdUte(0);
          }
          
          //Assegno i flag stoccaggio
          effCess.setFlagStoccaggio("N");
          if(Validator.isNotEmpty(chkStoccaggioCessioni))
          {
            for(int j=0;j<chkStoccaggioCessioni.length;j++)
            {
              //Se non è checkato non esiste la riga dell'array
              if(i == new Integer(chkStoccaggioCessioni[j]).intValue())
              {
                effCess.setFlagStoccaggio("S");
                break;
              }
            }          
          }
          
          //Associo la comunicazione 10R relativa all'ute
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
              
            if(com10.getIdUte() == effCess.getIdUte())
            {
              effCess.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
           effCess.validateConferma(null);  
        }     
       
      }
      
      
      
      
      
      
      
      
      
      HashMap<Integer,Integer> hChkCessioni = new HashMap<Integer,Integer>();
      if(Validator.isNotEmpty(chkCessioni))
      {
        hChkCessioni = new HashMap<Integer,Integer>();
        for(int i=0;i<chkCessioni.length;i++)
        {
          hChkCessioni.put(new Integer(chkCessioni[i]),new Integer(chkCessioni[i]));
        }
        
        request.setAttribute("hChkCessioni",hChkCessioni); 
      }
      
      
      if(Validator.isNotEmpty(chkCessioni))
      {
        for(int i=(chkCessioni.length - 1);i>=0;i--)
        {
          vCessioni.remove(new Integer(chkCessioni[i]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaCessioni",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if(vCessioni.size() == 0)
      {
        vCessioni = null;
      }
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaStocc"))
    {
      String[] chkStocc = request.getParameterValues("chkStocc");
      
      HashMap<Integer,Integer> hChkStocc = null;
      if(Validator.isNotEmpty(chkStocc))
      {
        hChkStocc = new HashMap<Integer,Integer>();
        for(int i=0;i<chkStocc.length;i++)
        {
          hChkStocc.put(new Integer(chkStocc[i]),new Integer(chkStocc[i]));
        }
        
        request.setAttribute("hChkStocc",hChkStocc); 
      } 
      
      if(Validator.isNotEmpty(chkStocc))
      {
        for(int i=(chkStocc.length - 1);i>=0;i--)
        {
          vStoccaggio.remove(new Integer(chkStocc[i]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaStocc",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if(vStoccaggio.size() == 0)
      {
        vStoccaggio = null;
      }
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaAcqueReflue"))
    {
      String[] chkAcqueReflue = request.getParameterValues("chkAcqueReflue");
      
      HashMap<Integer,Integer> hChkAcqueReflue = null;
      if(Validator.isNotEmpty(chkAcqueReflue))
      {
        hChkAcqueReflue = new HashMap<Integer,Integer>();
        for(int i=0;i<chkAcqueReflue.length;i++)
        {
          hChkAcqueReflue.put(new Integer(chkAcqueReflue[i]),new Integer(chkAcqueReflue[i]));
        }
        
        request.setAttribute("hChkAcqueReflue",hChkAcqueReflue); 
      } 
      
      if(Validator.isNotEmpty(chkAcqueReflue))
      {
        for(int i=(chkAcqueReflue.length - 1);i>=0;i--)
        {
          vAcqueReflue.remove(new Integer(chkAcqueReflue[i]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaAcqueReflue",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if(vAcqueReflue.size() == 0)
      {
        vAcqueReflue = null;
      }
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaVolSot"))
    {
      String[] chkVolSot = request.getParameterValues("chkVolSot");
      
      HashMap<Integer,Integer> hChkVolSot = null;
      if(Validator.isNotEmpty(chkVolSot))
      {
        hChkVolSot = new HashMap<Integer,Integer>();
        for(int i=0;i<chkVolSot.length;i++)
        {
          hChkVolSot.put(new Integer(chkVolSot[i]),new Integer(chkVolSot[i]));
        }
        
        request.setAttribute("hChkVolSot",hChkVolSot); 
      } 
      
      if(Validator.isNotEmpty(chkVolSot))
      {
        for(int i=(chkVolSot.length - 1);i>=0;i--)
        {
          vVolumeSottogrigliato.remove(new Integer(chkVolSot[i]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaVolSot",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if(vVolumeSottogrigliato.size() == 0)
      {
        vVolumeSottogrigliato = null;
      }
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eliminaVolRefAz"))
    {
      String[] chkVolRefAz = request.getParameterValues("chkVolRefAz");
      
      HashMap<Integer,Integer> hChkVolRefAz = null;
      if(Validator.isNotEmpty(chkVolRefAz))
      {
        hChkVolRefAz = new HashMap<Integer,Integer>();
        for(int i=0;i<chkVolRefAz.length;i++)
        {
          hChkVolRefAz.put(new Integer(chkVolRefAz[i]),new Integer(chkVolRefAz[i]));
        }
        
        request.setAttribute("hChkVolRefAz",hChkVolRefAz); 
      } 
      
      if(Validator.isNotEmpty(chkVolRefAz))
      {
        for(int i=(chkVolRefAz.length - 1);i>=0;i--)
        {
          vVolumeRefluoAzienda.remove(new Integer(chkVolRefAz[i]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaVolRefAz",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if(vVolumeRefluoAzienda.size() == 0)
      {
        vVolumeRefluoAzienda = null;
      }
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("calcolaAzotoCessAcqu"))
    {
      if(vCessioniAcquisizioni !=null)
      {
        String[] quantitaCessAcqu = request.getParameterValues("quantitaCessAcqu");
        String[] idUteCessAcqu = request.getParameterValues("idUteCessAcqu");
        //String[] idCausaleEffluente = request.getParameterValues("idCausaleEffluente");
        String[] idEffluente = request.getParameterValues("idEffluente");
        String[] azotoDichiaratoCessAcqu = request.getParameterValues("azotoDichiaratoCessAcqu");
        HashMap<Integer,ValidationErrors> hElencoErroriCessAcqu = null;
        for(int i=0;i<vCessioniAcquisizioni.size();i++)
        {
          ValidationErrors errorsCesAcq = null;
          PlSqlCodeDescription plCode = null;
          BigDecimal azotoProdotto = null;
          EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
          //effVO.setIdCausaleEffluenteStr(idCausaleEffluente[i]);
          effVO.setIdCausaleEffluente(SolmrConstants.ID_TIPO_CAUS_EFF_ACQUISIZIONE);
          effVO.setIdEffluenteStr(idEffluente[i]);
          effVO.setQuantitaStr(quantitaCessAcqu[i]);
          effVO.setQuantitaAzotoDichiaratoStr(azotoDichiaratoCessAcqu[i]);
          
          if(Validator.isNotEmpty(idUteCessAcqu[i]))
          {
            effVO.setIdUte(new Long(idUteCessAcqu[i]).longValue());
          }
          else
          {
            effVO.setIdUte(0);
          }
          
          //Associo l'idComunicazione 10R all'ute.
          //Serve per i nuovi record inseriti oppure a quelli che è stata cambiata
          // l'ute
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == effVO.getIdUte())
            {
              effVO.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
          
          
          
          errorsCesAcq = effVO.validateCalcolaAcquisizioni(errorsCesAcq);
          if(errorsCesAcq == null)
          {
            try
            {
              plCode = gaaFacadeClient.calcolaQuantitaAzotoPlSql(effVO.getIdUte(),
                  effVO.getIdComunicazione10R(), effVO.getIdCausaleEffluente().longValue(),
                  effVO.getIdEffluente().longValue(),effVO.getQuantita());
            }
            catch (SolmrException se) 
            {
              SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+""+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
            
            if((plCode !=null) && (plCode.getDescription() == null)) //Non ci sono errori nel PLSQL
            {
              azotoProdotto = (BigDecimal)plCode.getItem();
              if(Validator.isNotEmpty(azotoProdotto))
              {
                effVO.setQuantitaAzoto(azotoProdotto);
                
                
                if(!Validator.isNotEmpty(effVO.getQuantitaAzotoDichiarato()))
                {
                  effVO.setQuantitaAzotoDichiarato(azotoProdotto);
                }
              }
              else
              {
                errorsCesAcq = ErrorUtils.setValidErrNoNull(errorsCesAcq, "azotoCessAcqu", AnagErrors.ERR_NO_AZOTO_PRODOTTO);
              }
            }
            else
            {
              if(plCode !=null)
              {
                errorsCesAcq = ErrorUtils.setValidErrNoNull(errorsCesAcq, "azotoCessAcqu", plCode.getOtherdescription() +": "+plCode.getDescription());
              }
              else
              {
                errorsCesAcq = ErrorUtils.setValidErrNoNull(errorsCesAcq, "azotoCessAcqu", "pl null");
              }
            }           
          }
          
          if(errorsCesAcq != null)
          {
            if(hElencoErroriCessAcqu == null)
            {
              hElencoErroriCessAcqu = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriCessAcqu.put(new Integer(i),errorsCesAcq);
          }
                   
        }
        
        if(hElencoErroriCessAcqu !=null)
        {
          request.setAttribute("hElencoErroriCessAcqu",hElencoErroriCessAcqu);
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "calcolaAzotoCessAcqu",AnagErrors.ERR_NO_CESS_ACQU);
      }
      
      if(errors != null)
      {
          request.setAttribute("errors", errors); 
      }
    }
    //Qui entro solo se idCausale_modifca
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("calcoloEffluenteAcquisito"))
    {
      String nRigaCessAcqu = request.getParameter("nRigaCessAcqu");
      int nRigeCeAcquInt = new Integer(nRigaCessAcqu).intValue(); 
      String[] idEffluente = request.getParameterValues("idEffluente");
      String[] idAziendaCessAcquElenco = request.getParameterValues("idAziendaCessAcquElenco");
      BigDecimal quantita = null;
      HashMap<Integer,ValidationErrors> hElencoErroriCessAcqu = null;
      
      
      ValidationErrors errorsCesAcq = null;
      PlSqlCodeDescription plCode = null;
      EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(nRigeCeAcquInt);
        
      try
      {
        plCode = gaaFacadeClient.calcolaM3EffluenteAcquisitoPlSql(
            anagAziendaVO.getIdAzienda().longValue(), new Long(idAziendaCessAcquElenco[nRigeCeAcquInt]).longValue(),
            SolmrConstants.ID_TIPO_CAUS_EFF_CESSIONE, new Long(idEffluente[nRigeCeAcquInt]).longValue());
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
          
      if((plCode !=null) && (plCode.getDescription() == null)) //Non ci sono errori nel PLSQL
      {
        quantita = (BigDecimal)plCode.getItem();
        if(quantita.compareTo(new BigDecimal(0)) == 0)
        {
          effVO.setQuantita(null);  
        }
        else
        {
          effVO.setQuantita(quantita);
        }
        
      }
      else
      {
        if(plCode !=null)
        {
          errorsCesAcq = ErrorUtils.setValidErrNoNull(errorsCesAcq, "quantitaCessAcqu", plCode.getOtherdescription() +": "+plCode.getDescription());
        }
        else
        {
          errorsCesAcq = ErrorUtils.setValidErrNoNull(errorsCesAcq, "quantitaCessAcqu", "pl null");
        }
      }           
        
        
      if(errorsCesAcq != null)
      {
        if(hElencoErroriCessAcqu == null)
        {
          hElencoErroriCessAcqu = new HashMap<Integer,ValidationErrors>();
        }
        hElencoErroriCessAcqu.put(new Integer(nRigeCeAcquInt),errorsCesAcq);
      }
                 
      
      
      if(hElencoErroriCessAcqu !=null)
      {
        request.setAttribute("hElencoErroriCessAcqu",hElencoErroriCessAcqu);
      }
      
      
      if(errors != null)
      {
        request.setAttribute("errors", errors); 
      }
    }
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("refresh"))
    {}
    else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("conferma"))
    {
      //Volume Sottogrigliato
      String[] volumeSottoGrigliato = request.getParameterValues("volumeSottoGrigliato");
      String[] idUteVolSot = request.getParameterValues("idUteVolSot");
      
      HashMap<Integer,ValidationErrors> hElencoErroriVolSot = null;
      ValidationErrors errorsVolSot = null;
      ArrayList<Long> aIdUteVolSot = null;
      if(vVolumeSottogrigliato !=null)
      {
        for(int i=0;i<vVolumeSottogrigliato.size();i++)
        {
          errorsVolSot = null;
          GenericQuantitaVO gqVO = (GenericQuantitaVO)vVolumeSottogrigliato.get(i);
          if(Validator.isNotEmpty(idUteVolSot[i]))
          {
            gqVO.setIdUte(new Long(idUteVolSot[i]).longValue());
          }
          else
          {
            gqVO.setIdUte(0);
          }
          
          
          if(Validator.isNotEmpty(volumeSottoGrigliato[i]))
          {
            gqVO.setQuantitaStr(volumeSottoGrigliato[i]);
          }
          else
          {
            gqVO.setQuantitaStr(null);
          }
          
          //Controllo che non esistano due idUte identici
          if (aIdUteVolSot == null)
          {
            aIdUteVolSot = new ArrayList<Long>();
          }
          Long idUteLg = new Long(gqVO.getIdUte());
          if(idUteLg.compareTo(new Long("0")) != 0)
          {
            if(aIdUteVolSot.contains(idUteLg))
            {
              errorsVolSot = ErrorUtils.setValidErrNoNull(errorsVolSot,"idUteVolSot",AnagErrors.ERR_ID_UTE_DOPPIO);
            }
            else
            {
              aIdUteVolSot.add(idUteLg);
            }
          }
          
          errorsVolSot = gqVO.validateConferma(errorsVolSot, "idUteVolSot", "volumeSottoGrigliato");
          if(errorsVolSot != null)
          {
            if(hElencoErroriVolSot == null)
            {
              hElencoErroriVolSot = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriVolSot.put(new Integer(i),errorsVolSot);
          }
        }
        
        if(hElencoErroriVolSot !=null)
        {
          request.setAttribute("hElencoErroriVolSot",hElencoErroriVolSot);
        }
        
      }
      
      
      //Volume Refluo Azienda
      String[] volumeRefluoAzienda = request.getParameterValues("volumeRefluoAzienda");
      String[] idUteVolRefAz = request.getParameterValues("idUteVolRefAz");
      
      HashMap<Integer,ValidationErrors> hElencoErroriVolRefAz = null;
      ValidationErrors errorsVolRefAz = null;
      ArrayList<Long> aIdUteVolRefAz = null;
      if(vVolumeRefluoAzienda !=null)
      {
        for(int i=0;i<vVolumeRefluoAzienda.size();i++)
        {
          errorsVolRefAz = null;
          GenericQuantitaVO gqVO = (GenericQuantitaVO)vVolumeRefluoAzienda.get(i);
          if(Validator.isNotEmpty(idUteVolRefAz[i]))
          {
            gqVO.setIdUte(new Long(idUteVolRefAz[i]).longValue());
          }
          else
          {
            gqVO.setIdUte(0);
          }
          
          
          if(Validator.isNotEmpty(volumeRefluoAzienda[i]))
          {
            gqVO.setQuantitaStr(volumeRefluoAzienda[i]);
          }
          else
          {
            gqVO.setQuantitaStr(null);
          }
          
          //Controllo che non esistano due idUte identici
          if (aIdUteVolRefAz == null)
          {
            aIdUteVolRefAz = new ArrayList<Long>();
          }
          Long idUteLg = new Long(gqVO.getIdUte());
          if(idUteLg.compareTo(new Long("0")) != 0)
          {
            if(aIdUteVolRefAz.contains(idUteLg))
            {
              errorsVolRefAz = ErrorUtils.setValidErrNoNull(errorsVolRefAz,"idUteVolRefAz",AnagErrors.ERR_ID_UTE_DOPPIO);
            }
            else
            {
              aIdUteVolRefAz.add(idUteLg);
            }
          }
          
          errorsVolRefAz = gqVO.validateConferma(errorsVolRefAz, "idUteVolRefAz", "volumeRefluoAzienda");
          if(errorsVolRefAz != null)
          {
            if(hElencoErroriVolRefAz == null)
            {
              hElencoErroriVolRefAz = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriVolRefAz.put(new Integer(i),errorsVolRefAz);
          }
        }
        
        if(hElencoErroriVolRefAz !=null)
        {
          request.setAttribute("hElencoErroriVolRefAz",hElencoErroriVolRefAz);
        }
        
      }
      
      
      //Acque Reflue
      String[] idTipologiaAcquaAgronomica = request.getParameterValues("idTipologiaAcquaAgronomica");
      String[] volumeRefluo = request.getParameterValues("volumeRefluo");
      String[] idUteAcqueReflue = request.getParameterValues("idUteAcqueReflue");
      
      HashMap<Integer,ValidationErrors> hElencoErroriAcqueReflue = null;
      ValidationErrors errorsAcqueReflue = null;
      ArrayList<String> aAcqueReflue = null;
      if(vAcqueReflue !=null)
      {
        for(int i=0;i<vAcqueReflue.size();i++)
        {
          errorsAcqueReflue = null;
          AcquaExtraVO acquExtVO = (AcquaExtraVO)vAcqueReflue.get(i);
          
          if(Validator.isNotEmpty(idUteAcqueReflue[i]))
          {
            acquExtVO.setIdUte(new Long(idUteAcqueReflue[i]).longValue());
          }
          else
          {
            acquExtVO.setIdUte(0);
          }
          
          if(Validator.isNotEmpty(idTipologiaAcquaAgronomica[i]))
          {
            acquExtVO.setIdAcquaAgronomica(new Long(idTipologiaAcquaAgronomica[i]).longValue());
          }
          else
          {
            acquExtVO.setIdAcquaAgronomica(0);
          }
          
          
          if(Validator.isNotEmpty(volumeRefluo[i]))
          {
            acquExtVO.setVolumeRefluoStr(volumeRefluo[i]);
          }
          else
          {
            acquExtVO.setVolumeRefluoStr(null);
          }          
          
          //Controllo che non esistano due VO con idTipologiaAcquaAgronomica e idUte identici
          if(aAcqueReflue == null)
          {
            aAcqueReflue = new ArrayList<String>();
          }
          
          Long idUteLg = new Long(acquExtVO.getIdUte());
          Long idAcquaagronomicaLg = new Long(acquExtVO.getIdAcquaAgronomica());
          if((idAcquaagronomicaLg.compareTo(new Long("0")) != 0) 
            && (idUteLg.compareTo(new Long("0")) != 0))
          {
            String idUte_IdAcqua = idUteLg.toString()+"_"+idAcquaagronomicaLg.toString(); 
            if(aAcqueReflue.contains(idUte_IdAcqua))
            {
              errorsAcqueReflue = ErrorUtils.setValidErrNoNull(errorsAcqueReflue,"idUteAcqueReflue",AnagErrors.ERR_ID_TIPOLOGIA_ACQUE_DOPPIO);
              errorsAcqueReflue = ErrorUtils.setValidErrNoNull(errorsAcqueReflue,"idTipologiaAcquaAgronomica",AnagErrors.ERR_ID_TIPOLOGIA_ACQUE_DOPPIO);
            }
            else
            {
              aAcqueReflue.add(idUte_IdAcqua);
            }
          }
          
          //Associo l'idComunicazione 10R all'ute.
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == acquExtVO.getIdUte())
            {
              acquExtVO.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
          
          
          errorsAcqueReflue = acquExtVO.validateConferma(errorsAcqueReflue);
          if(errorsAcqueReflue != null)
          {
            if(hElencoErroriAcqueReflue == null)
            {
              hElencoErroriAcqueReflue = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriAcqueReflue.put(new Integer(i),errorsAcqueReflue);
          }
        }
        
        if(hElencoErroriAcqueReflue !=null)
        {
          request.setAttribute("hElencoErroriAcqueReflue",hElencoErroriAcqueReflue);
        }
        
      }
      
      
      //Acquisizioni
      //String[] idCausaleEffluente = request.getParameterValues("idCausaleEffluente");
      String[] idEffluente = request.getParameterValues("idEffluente");
      String[] quantitaCessAcqu = request.getParameterValues("quantitaCessAcqu");
      String[] idUteCessAcqu = request.getParameterValues("idUteCessAcqu");
      String[] chkStoccaggioCessAcqu = request.getParameterValues("chkStoccaggioCessAcqu");
      String[] azotoDichiaratoCessAcqu = request.getParameterValues("azotoDichiaratoCessAcqu");
      
      HashMap<Integer,ValidationErrors> hElencoErroriCessAcqu = null;
      ValidationErrors errorsCessAcqu = null;
      HashMap<String,BigDecimal> hElencoQuantitaCessione = new HashMap<String,BigDecimal>(); // utilizzata per avere il totale delle cessioni
      if(vCessioniAcquisizioni !=null)
      {
        for(int i=0;i<vCessioniAcquisizioni.size();i++)
        {
          errorsCessAcqu = null;
          EffluenteCesAcqVO effCessAcqu = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
          //effCessAcqu.setIdCausaleEffluenteStr(idCausaleEffluente[i]);
          effCessAcqu.setIdCausaleEffluente(SolmrConstants.ID_TIPO_CAUS_EFF_ACQUISIZIONE);
          effCessAcqu.setIdEffluenteStr(idEffluente[i]);
          effCessAcqu.setQuantitaStr(quantitaCessAcqu[i]);
          effCessAcqu.setQuantitaAzotoDichiaratoStr(azotoDichiaratoCessAcqu[i]);
          
          if(Validator.isNotEmpty(idUteCessAcqu[i]))
          {
            effCessAcqu.setIdUte(new Long(idUteCessAcqu[i]).longValue());
          }
          else
          {
            effCessAcqu.setIdUte(0);
          }
          
          //Assegno i flag stoccaggio
          effCessAcqu.setFlagStoccaggio("N");
          if(Validator.isNotEmpty(chkStoccaggioCessAcqu))
          {
            for(int j=0;j<chkStoccaggioCessAcqu.length;j++)
            {
              //Se non è checkato non esiste la riga dell'array
              if(i == new Integer(chkStoccaggioCessAcqu[j]).intValue())
              {
                effCessAcqu.setFlagStoccaggio("S");
                break;
              }
            }          
          }
          
          //Associo la comunicazione 10R relativa all'ute
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
              
            if(com10.getIdUte() == effCessAcqu.getIdUte())
            {
              effCessAcqu.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
          
          
          errorsCessAcqu = effCessAcqu.validateConferma(errorsCessAcqu);
          PlSqlCodeDescription plCode = null;
          BigDecimal azotoProdotto = null;
          errorsCessAcqu = effCessAcqu.validateCalcolaAcquisizioni(errorsCessAcqu);
          
          if(errorsCessAcqu == null)
          {
            try
            {
              plCode = gaaFacadeClient.calcolaQuantitaAzotoPlSql(effCessAcqu.getIdUte(),
                  effCessAcqu.getIdComunicazione10R(),effCessAcqu.getIdCausaleEffluente().longValue(),
                  effCessAcqu.getIdEffluente().longValue(),effCessAcqu.getQuantita());
            }
            catch (SolmrException se) 
            {
              SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+""+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
            
            if((plCode !=null) && (plCode.getDescription() == null)) //Non ci sono errori nel PLSQL
            {
              azotoProdotto = (BigDecimal)plCode.getItem();
              if(Validator.isNotEmpty(azotoProdotto))
              {
                effCessAcqu.setQuantitaAzoto(azotoProdotto);
                
                if(Validator.isEmpty(effCessAcqu.getQuantitaAzotoDichiarato()))
                {
                  effCessAcqu.setQuantitaAzotoDichiarato(azotoProdotto);
                  
                }
                
              }
              else
              {
                errorsCessAcqu = ErrorUtils.setValidErrNoNull(errorsCessAcqu, "azotoCessAcqu", AnagErrors.ERR_NO_AZOTO_PRODOTTO);
              }
            }
            else
            {
              if(plCode !=null)
              {
                errorsCessAcqu = ErrorUtils.setValidErrNoNull(errorsCessAcqu, "azotoCessAcqu", plCode.getOtherdescription() +": "+plCode.getDescription());
              }
              else
              {
                errorsCessAcqu = ErrorUtils.setValidErrNoNull(errorsCessAcqu, "azotoCessAcqu", "pl null");
              }
            }
          }
            
          
          if(errorsCessAcqu != null)
          {
            if(hElencoErroriCessAcqu == null)
            {
              hElencoErroriCessAcqu = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriCessAcqu.put(new Integer(i),errorsCessAcqu);
          }
        }
        
        if(hElencoErroriCessAcqu !=null)
        {
          request.setAttribute("hElencoErroriCessAcqu",hElencoErroriCessAcqu);
        }
      }
      
      //Trattamenti
      String[] idUteTratt = request.getParameterValues("idUteTratt");
      String[] idEffluenteTratt = request.getParameterValues("idEffluenteTratt");
      String[] idTrattamento = request.getParameterValues("idTrattamento");
      String[] quantitaTratt = request.getParameterValues("quantitaTratt");
      String[] quantitaAzoto = request.getParameterValues("quantitaAzoto");
      String[] quantitaPalDichTratt = request.getParameterValues("quantitaPalDichTratt");
      String[] azotoPalDichTratt = request.getParameterValues("azotoPalDichTratt");
      String[] quantitaNonPalDichTratt = request.getParameterValues("quantitaNonPalDichTratt");
      String[] azotoNonPalDichTratt = request.getParameterValues("azotoNonPalDichTratt");
      String[] quantitaPreTratt = request.getParameterValues("quantitaPreTratt");
      String[] quantitaPalPostTratt = request.getParameterValues("quantitaPalPostTratt");
      String[] azotoPalPostTratt = request.getParameterValues("azotoPalPostTratt");
      String[] quantitaNonPalPostTratt = request.getParameterValues("quantitaNonPalPostTratt");
      String[] azotoNonPalPostTratt = request.getParameterValues("azotoNonPalPostTratt");
      
      HashMap<Integer,ValidationErrors> hElencoErroriTratt = null;
      HashMap<String, String> hEffTratt = new HashMap<String, String>();
      ValidationErrors errorsTratt = null;
      HashMap<Long, String> hUteTrattAltriDati = new HashMap<Long, String>();
      HashMap<String, BigDecimal> hUteTipoEffluTrattMax = new HashMap<String, BigDecimal>();
      if(vEffluentiTratt !=null)
      {
        for(int i=0;i<vEffluentiTratt.size();i++)
        {
          errorsTratt = null;
          
          EffluenteVO eff = vEffluentiTratt.get(i);
          
          if(Validator.isNotEmpty(idUteTratt[i]))
          {
            eff.setIdUte(new Long(idUteTratt[i]).longValue());
          }
          else
          {
            eff.setIdUte(0);
          }
          
          eff.setIdEffluenteOrigineStr(idEffluenteTratt[i]);
          eff.setIdTrattamentoStr(idTrattamento[i]);
          eff.setVolumeInizialeStr(quantitaTratt[i]);
          eff.setAzotoInizialeStr(quantitaAzoto[i]);
          eff.setVolumePostDichiaratoStr(quantitaPalDichTratt[i]);
          eff.setAzotoPostDichiaratoStr(azotoPalDichTratt[i]);
          eff.setVolumePostDichiaratoNonPalStr(quantitaNonPalDichTratt[i]);
          eff.setAzotoPostDichiaratoNonPalStr(azotoNonPalDichTratt[i]);
          eff.setVolumePreTrattStr(quantitaPreTratt[i]);
          eff.setMaxVolumePostDichiaratoStr(quantitaPalPostTratt[i]);
          eff.setMaxAzotoPostDichiaratoStr(azotoPalPostTratt[i]);
          eff.setMaxVolumePostDichiaratoNonPalStr(quantitaNonPalPostTratt[i]);
          eff.setMaxAzotoPostDichiaratoNonPalStr(azotoNonPalPostTratt[i]);
          //Associo l'idComunicazione 10R all'ute.
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute                 
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == eff.getIdUte())
            {
              eff.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
          
          
          
          errorsTratt = eff.validateConferma();
          
          if(errorsTratt != null)
          {
            if(hElencoErroriTratt == null)
            {
              hElencoErroriTratt = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriTratt.put(new Integer(i), errorsTratt);
          }
          else
          {
            if(eff.getIdTrattamento().compareTo(SolmrConstants.TRATTAMENTO_ALTRI) == 0)
            {
              if(hUteTrattAltriDati.get(new Long(eff.getIdUte())) == null)
              {
                hUteTrattAltriDati.put(new Long(eff.getIdUte()), "true");
              }
            }
            String key = eff.getIdUte()+"_"+eff.getIdEffluenteOrigineStr()+"_"+eff.getIdTrattamentoStr();
            if(hEffTratt.get(key) != null)
            {
             
              errorsTratt = ErrorUtils.setValidErrNoNull(errorsTratt, "idUteTratt", AnagErrors.ERR_TRATT_EFF);
              errorsTratt = ErrorUtils.setValidErrNoNull(errorsTratt, "idEffluenteTratt", AnagErrors.ERR_TRATT_EFF);
              errorsTratt = ErrorUtils.setValidErrNoNull(errorsTratt, "idTrattamento", AnagErrors.ERR_TRATT_EFF);
              if(hElencoErroriTratt == null)
	            {
	              hElencoErroriTratt = new HashMap<Integer,ValidationErrors>();
	            }
	            hElencoErroriTratt.put(new Integer(i), errorsTratt);
            }
            else
            {
              hEffTratt.put(key, "true");
              String keyMax = eff.getIdUte()+"_"+eff.getIdEffluenteOrigineStr();
              if(hUteTipoEffluTrattMax.get(keyMax) != null)
              {
                BigDecimal valoreBg = hUteTipoEffluTrattMax.get(keyMax);
                valoreBg = valoreBg.add(eff.getVolumeIniziale());
                if(valoreBg.compareTo(eff.getVolumePreTratt()) > 0)
                {
                  errorsTratt = ErrorUtils.setValidErrNoNull(errors, "quantitaTratt", AnagErrors.ERR_SUP_TRATT_CALC);
                  if(hElencoErroriTratt == null)
		              {
		                hElencoErroriTratt = new HashMap<Integer,ValidationErrors>();
		              }
		              hElencoErroriTratt.put(new Integer(i), errorsTratt);
                }
              }
              else
              {
                hUteTipoEffluTrattMax.put(keyMax, eff.getVolumeIniziale());
              }
              
            }
          }
        }
        
        if(hElencoErroriTratt !=null)
        {
          request.setAttribute("hElencoErroriTratt",hElencoErroriTratt);
        }
      }
      
      
      
      //Cessioni
      String[] idUteCessioni = request.getParameterValues("idUteCessioni");
      String[] idEffluenteCessioni = request.getParameterValues("idEffluenteCessioni");
      String[] quantitaCessioni = request.getParameterValues("quantitaCessioni");
      String[] azotoCessioni = request.getParameterValues("azotoCessioni");
      String[] quantitaPreCess = request.getParameterValues("quantitaPreCess");
      String[] azotoPreCess = request.getParameterValues("azotoPreCess");
      String[] chkStoccaggioCessioni = request.getParameterValues("chkStoccaggioCessioni");
      
      HashMap<Integer,ValidationErrors> hElencoErroriCessioni = null;
      HashMap<String, String> hEffCess = new HashMap<String, String>();
      ValidationErrors errorsCessioni = null;
      HashMap<Long,HashMap<Long,BigDecimal>> hVolumeCeduto = new HashMap<Long,HashMap<Long,BigDecimal>>();
      HashMap<Long,HashMap<Long,BigDecimal>> hAzotoCeduto = new HashMap<Long,HashMap<Long,BigDecimal>>();
      if(vCessioni !=null)
      {
      
        
			       
        HashMap<Long,BigDecimal> hMaxVolUte = new HashMap<Long,BigDecimal>();
        HashMap<Long,BigDecimal> hMaxAzotoUte = new HashMap<Long,BigDecimal>();
        HashMap<Long,BigDecimal> hUteAcquaLav = new HashMap<Long,BigDecimal>();         
        for(int i=0;i<vCom10r.size();i++)
        {
        
          
        
          BigDecimal maxVolNonPalabile = new BigDecimal(0);
          BigDecimal maxAzotoNonPalabile = new BigDecimal(0);
          Comunicazione10RVO  com10 = (Comunicazione10RVO)vCom10r.get(i);
          
          //calcolo max acque
          //Sommo volume refluo aziedale inputato!!!
          if(Validator.isNotEmpty(vVolumeRefluoAzienda))
          { 
            for(int j=0;j<vVolumeRefluoAzienda.size();j++)
            {        
              GenericQuantitaVO gqVO = vVolumeRefluoAzienda.get(j);
              if(gqVO.getIdUte() == vCom10r.get(i).getIdUte())
              {
                hUteAcquaLav.put(new Long(gqVO.getIdUte()), gqVO.getQuantita());
              }
            }            
          }          
          //Sommo le acque reflue
          if(Validator.isNotEmpty(vAcqueReflue))
          {
            for(int j=0;j<vAcqueReflue.size();j++)
            {
              AcquaExtraVO acquExtVO = vAcqueReflue.get(j);
              if(acquExtVO.getIdUte() == vCom10r.get(i).getIdUte())
              {
                if(hUteAcquaLav.get(new Long(acquExtVO.getIdUte())) != null)
                {
                  BigDecimal sumTmp =  hUteAcquaLav.get(new Long(acquExtVO.getIdUte()));
                  sumTmp = sumTmp.add(acquExtVO.getVolumeRefluo());
                  hUteAcquaLav.put(new Long(acquExtVO.getIdUte()), sumTmp);
                }
                else
                {
                  hUteAcquaLav.put(new Long(acquExtVO.getIdUte()), acquExtVO.getVolumeRefluo());
                }
              
              }
            }
          }
          
          
          PlSqlCodeDescription plCode = null;                  
	        try 
	        {
	          plCode = gaaFacadeClient.calcolaVolumePioggeM3PlSql(vCom10r.get(i).getIdUte());
	        }
	        catch (SolmrException se) 
	        {
	          SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
	          String messaggio = errMsg+""+se.toString();
	          request.setAttribute("messaggioErrore",messaggio);
	          request.setAttribute("pageBack", actionUrl);
	          %>
	            <jsp:forward page="<%= erroreViewUrl %>" />
	          <%
	          return;
	        }
	        
	        //Errori nel PlSql
	        if((plCode !=null) && (plCode.getOtherdescription() != null))
	        {
	          String messaggio = "Si è verificato un errore durante la chiamata a calcolaVolumePioggeM3PlSql. Chiamare l'assistenza tecnica "+
	            "comunicando il seguente errore: "+plCode.getDescription()+" -  "+plCode.getOtherdescription(); 
	          request.setAttribute("messaggioErrore",messaggio);
	          %>
	            <jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />
	          <%
	        }
	        if(Validator.isNotEmpty(plCode) 
	          && Validator.isNotEmpty(plCode.getItem()))
	        {
	          if(hUteAcquaLav.get(new Long(vCom10r.get(i).getIdUte())) != null)
            {
              BigDecimal sumTmp =  hUteAcquaLav.get(new Long(vCom10r.get(i).getIdUte()));
              sumTmp = sumTmp.add((BigDecimal)plCode.getItem());
              hUteAcquaLav.put(new Long(vCom10r.get(i).getIdUte()), sumTmp);
            }
            else
            {
              hUteAcquaLav.put(new Long(new Long(vCom10r.get(i).getIdUte())), (BigDecimal)plCode.getItem());
            }        
	        }
	        
	        try 
          {
            plCode = gaaFacadeClient.calcolaAcqueMungituraPlSql(vCom10r.get(i).getIdUte());
          }
          catch (SolmrException se) 
          {
            SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+""+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          //Errori nel PlSql
          if((plCode !=null) && (plCode.getOtherdescription() != null))
          {
            String messaggio = "Si è verificato un errore durante la chiamata a calcolaAcqueMungituraPlSql. Chiamare l'assistenza tecnica "+
              "comunicando il seguente errore: "+plCode.getDescription()+" -  "+plCode.getOtherdescription(); 
            request.setAttribute("messaggioErrore",messaggio);
            %>
              <jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />
            <%
          }
          if(Validator.isNotEmpty(plCode) 
            && Validator.isNotEmpty(plCode.getItem()))
          {
            if(hUteAcquaLav.get(new Long(vCom10r.get(i).getIdUte())) != null)
            {
              BigDecimal sumTmp =  hUteAcquaLav.get(new Long(vCom10r.get(i).getIdUte()));
              sumTmp = sumTmp.add((BigDecimal)plCode.getItem());
              hUteAcquaLav.put(new Long(vCom10r.get(i).getIdUte()), sumTmp);
            }
            else
            {
              hUteAcquaLav.put(new Long(new Long(vCom10r.get(i).getIdUte())), (BigDecimal)plCode.getItem());
            }        
          }
          
          try 
          {
            plCode = gaaFacadeClient.calcolaCesAcquisizionePlSql(vCom10r.get(i).getIdComunicazione10R());
          }
          catch (SolmrException se) 
          {
            SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+""+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          //Errori nel PlSql
          if((plCode !=null) && (plCode.getOtherdescription() != null))
          {
            String messaggio = "Si è verificato un errore durante la chiamata a calcolaCesAcquisizionePlSql. Chiamare l'assistenza tecnica "+
              "comunicando il seguente errore: "+plCode.getDescription()+" -  "+plCode.getOtherdescription(); 
            request.setAttribute("messaggioErrore",messaggio);
            %>
              <jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />
            <%
          }
          if(Validator.isNotEmpty(plCode) 
            && Validator.isNotEmpty(plCode.getItem()))
          {
            if(hUteAcquaLav.get(new Long(vCom10r.get(i).getIdUte())) != null)
            {
              BigDecimal sumTmp =  hUteAcquaLav.get(new Long(vCom10r.get(i).getIdUte()));
              sumTmp = sumTmp.subtract((BigDecimal)plCode.getItem());
              hUteAcquaLav.put(new Long(vCom10r.get(i).getIdUte()), sumTmp);
            }
            else
            {
              BigDecimal sumTmp = new BigDecimal(0);
              sumTmp = sumTmp.subtract((BigDecimal)plCode.getItem());
              hUteAcquaLav.put(new Long(new Long(vCom10r.get(i).getIdUte())), (BigDecimal)plCode.getItem());
            }        
          }
	        
	                  
          
          
          
          Vector<TipoEffluenteVO> vTipoEffluentiCess = hTipoEffluentiCess.get(new Long(com10.getIdUte()));
          if(vTipoEffluentiCess != null)
          {
            for(int j=0;j<vTipoEffluentiCess.size();j++)
            {
              TipoEffluenteVO tipoVO  = (TipoEffluenteVO)vTipoEffluentiCess.get(j);
              if("N".equalsIgnoreCase(tipoVO.getFlagPalabile()))
              {
                maxVolNonPalabile = maxVolNonPalabile.add(tipoVO.getVolumeProdotto());
                maxAzotoNonPalabile = maxAzotoNonPalabile.add(tipoVO.getAzotoProdotto());
              }            
            }
          }
          
          if(hUteAcquaLav !=null)
          {
            if(Validator.isNotEmpty(hUteAcquaLav.get(new Long(com10.getIdUte()))))
              maxVolNonPalabile = maxVolNonPalabile.add(hUteAcquaLav.get(new Long(com10.getIdUte())));
          }
          
          hMaxVolUte.put(new Long(com10.getIdUte()), maxVolNonPalabile);
          hMaxAzotoUte.put(new Long(com10.getIdUte()), maxAzotoNonPalabile);
        }
        
      
        for(int i=0;i<vCessioni.size();i++)
        {
          errorsCessioni = null;
          
          EffluenteCesAcqVO effCesAcq = (EffluenteCesAcqVO)vCessioni.get(i);
          
          if(Validator.isNotEmpty(idUteCessioni[i]))
          {
            effCesAcq.setIdUte(new Long(idUteCessioni[i]).longValue());
          }
          else
          {
            effCesAcq.setIdUte(0);
          }
          
          
          //Assegno i flag stoccaggio
          effCesAcq.setFlagStoccaggio("N");
          if(Validator.isNotEmpty(chkStoccaggioCessioni))
          {
            for(int j=0;j<chkStoccaggioCessioni.length;j++)
            {
              //Se non è checkato non esiste la riga dell'array
              if(i == new Integer(chkStoccaggioCessioni[j]).intValue())
              {
                effCesAcq.setFlagStoccaggio("S");
                break;
              }
            }          
          }
          
          
          effCesAcq.setIdEffluenteStr(idEffluenteCessioni[i]);
          effCesAcq.setQuantitaStr(quantitaCessioni[i]);
          effCesAcq.setQuantitaAzotoStr(azotoCessioni[i]);
          effCesAcq.setQuantitaPreCessioniStr(quantitaPreCess[i]);
          effCesAcq.setQuantitaAzotoPreCessioniStr(azotoPreCess[i]);
          effCesAcq.setIdCausaleEffluente(SolmrConstants.ID_TIPO_CAUS_EFF_CESSIONE);
          
          
          //Associo liidComunicazione 10R all'ute.
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute       
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == effCesAcq.getIdUte())
            {
              effCesAcq.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
          
          
          
          //verifico il tipo effluente (pal , non pal)
          String flagPalabile = "";
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == effCesAcq.getIdUte())
            {               
              Vector<TipoEffluenteVO> vTipoEffluentiCess = hTipoEffluentiCess.get(new Long(com10.getIdUte()));
              if(vTipoEffluentiCess != null)
              {
                for(int j=0;j<vTipoEffluentiCess.size();j++)
                {
                  TipoEffluenteVO tipoVO  = (TipoEffluenteVO)vTipoEffluentiCess.get(j);
                  if(Validator.isNotEmpty(effCesAcq.getIdEffluenteStr()))
                  {
	                  if(tipoVO.getIdEffluente() == new Long(effCesAcq.getIdEffluenteStr()).longValue())
	                  {
	                    flagPalabile = tipoVO.getFlagPalabile();
	                    break;
	                  }
	                  
	                }
                }
              }
            }
          }
          
          
          
          
          
          errorsCessioni = effCesAcq.validateConfermaCessioni(
            errorsCessioni, flagPalabile, hMaxVolUte.get(new Long(effCesAcq.getIdUte())), hMaxAzotoUte.get(new Long(effCesAcq.getIdUte())));
          if(errorsCessioni == null)
          {          
            
            //Se l'effluente è non paplabile metto un idFittizio -1 in quanto
            //tutti i non palabili concorrono alla stessa somma         
	          if(hVolumeCeduto.get(new Long(effCesAcq.getIdUte())) != null)
	          {
	            if("N".equalsIgnoreCase(flagPalabile))
	            {
	              HashMap<Long,BigDecimal> hVolumeCedutoUte = hVolumeCeduto.get(new Long(effCesAcq.getIdUte()));
	              BigDecimal quantita = hVolumeCedutoUte.get(new Long(-1));
	              if(quantita != null)
	              {
		              quantita = quantita.add(effCesAcq.getQuantita());
		              BigDecimal quantitaPreCessioni = hMaxVolUte.get(new Long(effCesAcq.getIdUte()));
		              if(quantita.compareTo(quantitaPreCessioni) > 0)
		              {
		                errorsCessioni = ErrorUtils.setValidErrNoNull(errorsCessioni, "quantitaCessioni", AnagErrors.ERR_SUP_TRATT_CALC_SUM +": "+Formatter.formatDouble1(quantitaPreCessioni));
		              }
		              else
		              {
		                hVolumeCedutoUte.put(new Long(-1), quantita);
		              }
		            }
		            else
		            {
		              hVolumeCedutoUte.put(new Long(-1), effCesAcq.getQuantita());
		            }
	              hVolumeCeduto.put(new Long(effCesAcq.getIdUte()), hVolumeCedutoUte);
	            }
	            else
	            {	
	              HashMap<Long,BigDecimal> hVolumeCedutoUte = hVolumeCeduto.get(new Long(effCesAcq.getIdUte()));
	              BigDecimal quantita = hVolumeCedutoUte.get(effCesAcq.getIdEffluente());
	              if(quantita != null)
	              {
			            quantita = quantita.add(effCesAcq.getQuantita());
			            String quantitaPreCessioniStr = effCesAcq.getQuantitaPreCessioniStr().replace(',','.');
			            BigDecimal quantitaPreCessioni = new BigDecimal(quantitaPreCessioniStr);
			            if(quantita.compareTo(quantitaPreCessioni) > 0)
			            {
			              errorsCessioni = ErrorUtils.setValidErrNoNull(errorsCessioni, "quantitaCessioni", AnagErrors.ERR_SUP_TRATT_CALC_SUM+": "+Formatter.formatDouble1(quantitaPreCessioni));
			            }
			            else
			            {
			              hVolumeCedutoUte.put(effCesAcq.getIdEffluente(), quantita);
			            }
			          }
			          else
			          {
			            hVolumeCedutoUte.put(effCesAcq.getIdEffluente(), effCesAcq.getQuantita());
			          }
			          hVolumeCeduto.put(new Long(effCesAcq.getIdUte()), hVolumeCedutoUte);
		          }
	          }
	          else
	          {
	            HashMap<Long,BigDecimal> hVolumeCedutoUte = new HashMap<Long,BigDecimal>();
	            if("N".equalsIgnoreCase(flagPalabile))
              {                
                hVolumeCedutoUte.put(new Long(-1), effCesAcq.getQuantita());
              }
              else
              {
	              hVolumeCedutoUte.put(effCesAcq.getIdEffluente(), effCesAcq.getQuantita());
	            }
	            hVolumeCeduto.put(new Long(effCesAcq.getIdUte()), hVolumeCedutoUte);
	          }
	          
	          //Azoto
	          if(hAzotoCeduto.get(new Long(effCesAcq.getIdUte())) != null)
            {
              if("N".equalsIgnoreCase(flagPalabile))
              {
                HashMap<Long,BigDecimal> hAzotoCedutoUte = hAzotoCeduto.get(new Long(effCesAcq.getIdUte()));
                BigDecimal quantita = hAzotoCedutoUte.get(new Long(-1));
                if(quantita != null)
                {
	                quantita = quantita.add(effCesAcq.getQuantitaAzoto());
	                BigDecimal quantitaAzotoPreCessioni = hMaxAzotoUte.get(new Long(effCesAcq.getIdUte()));
	                if(quantita.compareTo(quantitaAzotoPreCessioni) > 0)
	                {
	                  errorsCessioni = ErrorUtils.setValidErrNoNull(errorsCessioni, "azotoCessioni", AnagErrors.ERR_SUP_TRATT_CALC_SUM+": "+Formatter.formatDouble1(quantitaAzotoPreCessioni));
	                }
	                else
	                {
	                  hAzotoCedutoUte.put(new Long(-1), quantita);
	                }
	              }
	              else
	              {
	                hAzotoCedutoUte.put(new Long(-1), effCesAcq.getQuantitaAzoto());
	              }
	              
	              hAzotoCeduto.put(new Long(effCesAcq.getIdUte()), hAzotoCedutoUte);
              }
              else
              {
                HashMap<Long,BigDecimal> hAzotoCedutoUte = hAzotoCeduto.get(new Long(effCesAcq.getIdUte()));
	              BigDecimal quantita = hAzotoCedutoUte.get(effCesAcq.getIdEffluente());
	              if(quantita != null)
	              {
		              quantita = quantita.add(effCesAcq.getQuantitaAzoto());
		              String quantitaAzotoPreCessioniStr = effCesAcq.getQuantitaAzotoPreCessioniStr().replace(',','.');
		              BigDecimal quantitaAzotoPreCessioni = new BigDecimal(quantitaAzotoPreCessioniStr);
		              if(quantita.compareTo(quantitaAzotoPreCessioni) > 0)
		              {
		                errorsCessioni = ErrorUtils.setValidErrNoNull(errorsCessioni, "azotoCessioni", AnagErrors.ERR_SUP_TRATT_CALC_SUM+": "+Formatter.formatDouble1(quantitaAzotoPreCessioni));
		              }
		              else
		              {
		                hAzotoCedutoUte.put(effCesAcq.getIdEffluente(), quantita);
		              }
		            }
		            else
		            {
		              hAzotoCedutoUte.put(effCesAcq.getIdEffluente(), effCesAcq.getQuantitaAzoto());
		            }
		            
		            hAzotoCeduto.put(new Long(effCesAcq.getIdUte()), hAzotoCedutoUte);
	            }
            }
            else
            {
              HashMap<Long,BigDecimal> hAzotoCedutoUte = new HashMap<Long,BigDecimal>();
              if("N".equalsIgnoreCase(flagPalabile))
              {
                hAzotoCedutoUte.put(new Long(-1), effCesAcq.getQuantitaAzoto());
              }
              else
              {
                hAzotoCedutoUte.put(effCesAcq.getIdEffluente(), effCesAcq.getQuantitaAzoto());
              }
              hAzotoCeduto.put(new Long(effCesAcq.getIdUte()), hAzotoCedutoUte);
            }
	        }
          
                   
          if(errorsCessioni != null)
          {
            if(hElencoErroriCessioni == null)
            {
              hElencoErroriCessioni = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriCessioni.put(new Integer(i),errorsCessioni);
          }
          
        }
        
        if(hElencoErroriCessioni !=null)
        {
          request.setAttribute("hElencoErroriCessioni",hElencoErroriCessioni);
        }
      }  //fine for cessioni
      
      
      
      
      
      
      
      
      String[] idTipologiaFabbricato = request.getParameterValues("idTipologiaFabbricato");
      String[] quantitaStocc = request.getParameterValues("quantitaStocc");
      String[] idUteStocc = request.getParameterValues("idUteStocc");
      
      HashMap<Integer,ValidationErrors> hElencoErroriStoc = null;
      ValidationErrors errorsStoc = null;
      if(vStoccaggio !=null)
      {
        for(int i=0;i<vStoccaggio.size();i++)
        {
          errorsStoc = null;
          
          EffluenteStocExtVO effStoc = (EffluenteStocExtVO)vStoccaggio.get(i);
          
          if(Validator.isNotEmpty(idUteStocc[i]))
          {
            effStoc.setIdUte(new Long(idUteStocc[i]).longValue());
          }
          else
          {
            effStoc.setIdUte(0);
          }
          
          
          if(Validator.isNotEmpty(idTipologiaFabbricato[i]))
          {
            effStoc.setIdTipologiaFabbricato(new Long(idTipologiaFabbricato[i]).longValue());
          }
          else
          {
            effStoc.setIdTipologiaFabbricato(0);
          }
          
          
          if(Validator.isNotEmpty(quantitaStocc[i]))
          {
            effStoc.setQuantitaStr(quantitaStocc[i]);
          }
          else
          {
            effStoc.setQuantitaStr(null);
          }
          
          //Associo liidComunicazione 10R all'ute.
          //Serve per i nuovi record inseriti oppure che è stata cambiata
          // l'ute                 
          for(int k=0;k<vCom10r.size();k++)
          {
            Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(k);
            
            if(com10.getIdUte() == effStoc.getIdUte())
            {
              effStoc.setIdComunicazione10R(com10.getIdComunicazione10R());
              break;
            }
          }
          
          
          
          errorsStoc = effStoc.validateConferma(errorsStoc);
          if(errorsStoc != null)
          {
            if(hElencoErroriStoc == null)
            {
              hElencoErroriStoc = new HashMap<Integer,ValidationErrors>();
            }
            hElencoErroriStoc.put(new Integer(i),errorsStoc);
          }
        }
        
        if(hElencoErroriStoc !=null)
        {
          request.setAttribute("hElencoErroriStoc",hElencoErroriStoc);
        }
      }
      
      String[] volumePostDich = request.getParameterValues("volumePostDich");
      String[] azotoPostDich = request.getParameterValues("azotoPostDich");
      String[] noteComunicazione = request.getParameterValues("noteComunicazione");
      
      //Se esite l'effluente esiste anche la nota
      int numeroElencoNote = 0;
      //serve per acquisire i campi di input a video nell'ordine in cui sono inseriti
      //indipendentemente dall'ute
      int sizePost = 0; 
      HashMap<Integer,ValidationErrors> hElencoErroriNote = null;
      ValidationErrors errorsNote = null;
      //HashMap<Long,HashMap<Integer,ValidationErrors>> hElencoErroriEffGlobal = null;
      
      HashMap<Integer,ValidationErrors> hElencoErroriAdesDer = null;
      
      for(int i=0;i<vCom10r.size();i++)
      {
        errorsNote = null;
        Comunicazione10RVO com10 = (Comunicazione10RVO)vCom10r.get(i);
        
        
        //Setto i volumi se e solo se è stato inserito
        //associandoli per ute alla dichiarazione 10r
        boolean flagTrovatoVolSot = false;
        if(Validator.isNotEmpty(vVolumeSottogrigliato))
        {
          for(int k=0;k<vVolumeSottogrigliato.size();k++)
          {
            GenericQuantitaVO gqVolSot = (GenericQuantitaVO)vVolumeSottogrigliato.get(k);
            if(com10.getIdUte() == gqVolSot.getIdUte())
            {
              com10.setVolumeSottogrigliato(gqVolSot.getQuantita());
              flagTrovatoVolSot = true;
              break;
            }
          }
        }
        
        if(!flagTrovatoVolSot)
        {
          com10.setVolumeSottogrigliato(null);
        }
        
        boolean flagTrovatoRefAz = false;
        if(Validator.isNotEmpty(vVolumeRefluoAzienda))
        {
          for(int k=0;k<vVolumeRefluoAzienda.size();k++)
          {
            GenericQuantitaVO gqVolRefAz = (GenericQuantitaVO)vVolumeRefluoAzienda.get(k);
            if(com10.getIdUte() == gqVolRefAz.getIdUte())
            {
              com10.setVolumeRefluoAzienda(gqVolRefAz.getQuantita());
              flagTrovatoRefAz = true;
              break;
            }
          }
        }
        
        if(!flagTrovatoRefAz)
        {
          com10.setVolumeRefluoAzienda(null);
        }
        
        
        com10.setNote(noteComunicazione[numeroElencoNote]);
           
        //Le note sono legate agli effluenti!
        //Si vedono solo se esitono effluenti per la ute corrente
        boolean flagNote = false;
        if(hUteTrattAltriDati.get(new Long(com10.getIdUte())) != null)
        {
          flagNote = true;
        } 
        errorsNote = com10.validateConferma(flagNote);
        if(errorsNote != null)
        {
          if(hElencoErroriNote == null)
          {
            hElencoErroriNote = new HashMap<Integer,ValidationErrors>();
          }
          hElencoErroriNote.put(new Integer(numeroElencoNote),errorsNote);
        }
        numeroElencoNote++;
        
        if(hElencoErroriNote != null)
	      {
	        request.setAttribute("hElencoErroriNote", hElencoErroriNote); 
	      }
        
         
	      if(("S".equalsIgnoreCase(flagAdesioneDerogaDb) && "S".equalsIgnoreCase(flagModAdesioneDerogaDb))
          || ("S".equalsIgnoreCase(flagAdesioneDerogaDb) && "N".equalsIgnoreCase(flagModAdesioneDerogaDb)))
	      {        
	        String flagAdesioneDeroga = request.getParameter("flagAdesioneDeroga"+""+i);
	        //disabilitata la funzione SI quindi se null vuol dire che ho selezionato SI
	        // e devo manatenere il si
	        //Richiesta Gabriella/Bassanino 16/02/2012
	        if(flagAdesioneDeroga == null)
	        {
	          flagAdesioneDeroga = "S";
	        }
	        
	        ValidationErrors errorsAdesDer = null;
	        boolean flagNonEseguireControllo = false;

	        
	        if("S".equalsIgnoreCase(flagAdesioneDeroga))
	        {
	          com10.setAdesioneDeroga("S");
	          
	          
            
            
            //controllo che siano disponibili le tipologie di effluente zootecnico ammesse in deroga, 
            //cioè nel riepilogo effluenti deve essere presente almeno un refluo  
            //fra quelli indicati nel campo codice di db_altri_dati per id_altri_dati='DEREFF' 
            //e deve esistere almeno un refluo per cui  la quantità (m3) post acquisizionil è >0
            //Gli effluenti presi sono quelli generati dal plsql e non presenti tra i trattamenti!!!!
            Vector<EffluenteVO> vEffluentiNoTratt = gaaFacadeClient.getListEffluentiNoTratt(com10.getIdComunicazione10R());                
            if(vEffluentiNoTratt != null)
            {                 
              for(int j=0;j<vEffluentiNoTratt.size();j++)
              {
                EffluenteVO effVODer = vEffluentiNoTratt.get(j);
                BigDecimal postAcquisizioneVol = effVODer.getVolumePostDichiarato();                
                postAcquisizioneVol = postAcquisizioneVol.subtract(effVODer.getVolumeCessione());
                postAcquisizioneVol = postAcquisizioneVol.add(effVODer.getVolumeAcquisizione());
                if(hIdEffluentiCtrlDeroga.get(new Long(effVODer.getIdEffluente())) != null)
                {
                  if(postAcquisizioneVol.compareTo(new BigDecimal(0)) > 0)
                  {
                    flagNonEseguireControllo = true;
                    break;
                  }
                }
              }
            }             
            
            
            if(!flagNonEseguireControllo)
            {
              //Il controllo non viene eseguito se non esistono reflui in azienda 
              //(cioè è stato dichiarato solo pascolo-->
              //la somma di giorni_pascolo_estate e giorni_pascolo_inverno è 365) 
              //e negli allevamenti è presente solo la specie  bovini (id_specie_animale=1,2)
              if(!gaaFacadeClient.controlloRefluoPascolo(com10.getIdUte()))
              {
                flagNonEseguireControllo = true;
              }
            } 
            
            
            
            if(!flagNonEseguireControllo)
            {             
              errorsAdesDer = ErrorUtils.setValidErrNoNull(errorsAdesDer, "flagAdesioneDeroga", AnagErrors.ERR_REFLUO_ADESIONE_DEROGA);
            }
            
            if(errorsAdesDer == null)
            {
              BigDecimal sumDerogaZVN = com10.getSuperficieAsservimentoZVN();
              sumDerogaZVN = sumDerogaZVN.add(com10.getSuperficieConduzioneZVN());
              
              if(sumDerogaZVN.compareTo(new BigDecimal(0)) <= 0)
              {
                errorsAdesDer = ErrorUtils.setValidErrNoNull(errorsAdesDer, "flagAdesioneDeroga", AnagErrors.ERR_SOMMA_ADESIONE_DEROGA);
              }
            }
            
            
            if(errorsAdesDer == null)
            {
              BigDecimal totStoccaggioDiponibilePalVol = new BigDecimal(0);
              BigDecimal totStocNecPalVol = new BigDecimal(0);
              BigDecimal totStoccaggioDiponibileNonPalVol = new BigDecimal(0);
              BigDecimal totStocNecNonPalVol = new BigDecimal(0);
              if(vEffluentiNoTratt != null)
              {
                totStoccaggioDiponibilePalVol = totStoccaggioDiponibilePalVol.add(com10.getStocDispPalabileVol());
                totStoccaggioDiponibileNonPalVol = totStoccaggioDiponibileNonPalVol.add(com10.getStocDispNonPalabileVol());
                
                totStocNecNonPalVol = totStocNecNonPalVol.add(com10.getStocAcqueNecVol());   
                for(int j=0;j<vEffluentiNoTratt.size();j++)
                {
                  EffluenteVO effVO = (EffluenteVO)vEffluentiNoTratt.get(j);
                  if(effVO.getTipoEffluente().equals("Palabile"))
				          {					            
				            totStocNecPalVol = totStocNecPalVol.add(effVO.getStocNecVol()); 
				          }
				          else
				          {				                
				            totStocNecNonPalVol = totStocNecNonPalVol.add(effVO.getStocNecVol()); 
				          }                
                }
                
                if(totStocNecPalVol.compareTo(new BigDecimal(0)) > 0)
                {
	                if((totStoccaggioDiponibilePalVol.compareTo(new BigDecimal(0)) > 0)
	                  && (totStocNecPalVol.compareTo(new BigDecimal(0)) > 0))
	                {
	                  BigDecimal totale = totStocNecPalVol.subtract(totStoccaggioDiponibilePalVol);
	                  totale = totale.divide(totStocNecPalVol,4,BigDecimal.ROUND_HALF_UP);
	                  if(totale.compareTo(valoreControlloPalDeroga) > 0)
	                  {
	                    errorsAdesDer = ErrorUtils.setValidErrNoNull(errorsAdesDer, "flagAdesioneDeroga", AnagErrors.ERR_DIFFERENZA_ADESIONE_DEROGA);
	                  }	                
	                }
	                else
                  {
                    errorsAdesDer = ErrorUtils.setValidErrNoNull(errorsAdesDer, "flagAdesioneDeroga", AnagErrors.ERR_DIFFERENZA_ADESIONE_DEROGA);
                  }
                }
                 
                 if(errorsAdesDer == null)
                 {
                   BigDecimal totale = totStocNecNonPalVol.subtract(totStoccaggioDiponibileNonPalVol);
                   if(totale.compareTo(valoreControlloNonPalDeroga) > 0 )
                   {
                     errorsAdesDer = ErrorUtils.setValidErrNoNull(errorsAdesDer, "flagAdesioneDeroga", AnagErrors.ERR_DIFFERENZA_ADESIONE_DEROGA);
                   }
                 }  
                          
                
              }
	            
              
                  
            }
            
            
                         
	            
	            
            if(errorsAdesDer != null)
            {
              if(hElencoErroriAdesDer == null)
              {
                hElencoErroriAdesDer = new HashMap<Integer,ValidationErrors>();
              }
              hElencoErroriAdesDer.put(new Integer(i), errorsAdesDer);
            }
            
            
            if(hElencoErroriAdesDer !=null)
            {
              request.setAttribute("hElencoErroriAdesDer", hElencoErroriAdesDer);
            }          
	          
	        }
	        else
	        {
	          com10.setAdesioneDeroga("N");
	        }
	      }
	      
	      
	      
	      
	      
	      
	       
      }    
          
      
      
      
      
      if((hElencoErroriVolSot == null) && (hElencoErroriVolRefAz == null) 
        && (hElencoErroriNote == null) && (hElencoErroriStoc == null) 
        && (hElencoErroriTratt == null)
        && (hElencoErroriCessAcqu == null) && (hElencoErroriCessioni == null)
        && (hElencoErroriAcqueReflue == null) && (hElencoErroriAdesDer == null))
      {
        PlSqlCodeDescription plCode = null;
        
        try 
        {
          plCode = gaaFacadeClient.storicizzaComunicazione10R(ruoloUtenza.getIdUtente(), anagAziendaVO.getIdAzienda().longValue(),
            vCom10r, vEffluentiTratt, vCessioniAcquisizioni, vCessioni, vStoccaggio, vAcqueReflue);
        }
        catch (SolmrException se) 
        {
          SolmrLogger.info(this, " - comunicazione10R_modificaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+""+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        //Errori nel PlSql
        if((plCode !=null) && (plCode.getDescription() != null))
        {
          String messaggio = "Si è verificato un errore durante il ricalcolo. Chiamare l'assistenza tecnica "+
            "comunicando il seguente errore: "+plCode.getDescription()+" -  "+plCode.getOtherdescription(); 
          request.setAttribute("messaggioErrore",messaggio);
          %>
            <jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />
          <%
        }
        
        %>
          <jsp:forward page="<%= comunicazione10R_dettaglioUrl %>" />
        <%
        return;
      }
      
    } // if conferma
    
    
    
    
    
    
    
    
    if(errors != null)
    {
      request.setAttribute("errors", errors); 
    }
    
    //Vettore di Comunicazione10RVOVO
    session.setAttribute("vCom10r",vCom10r);
    //Vettore di EffluenteCesAcqVO
    session.setAttribute("vCessioniAcquisizioni",vCessioniAcquisizioni);
    //Vettore di EffluenteCesAcqVO
    session.setAttribute("vCessioni",vCessioni);
    //Vettore di EffluenteStocExtVO
    session.setAttribute("vStoccaggio",vStoccaggio);
    //Vettore di AcquaExtraVO
    session.setAttribute("vAcqueReflue",vAcqueReflue);
    //Vettore di GenericQuantitaVO
    session.setAttribute("vVolumeSottogrigliato",vVolumeSottogrigliato);
    //Vettore di GenericQuantitaVO
    session.setAttribute("vVolumeRefluoAzienda",vVolumeRefluoAzienda);
    //Vettore di vettori di EffluenteVO
    session.setAttribute("vEffluentiTratt", vEffluentiTratt);
    
    
  }  //if v10r != null
  else
  {
    request.setAttribute("messaggioErrore",AnagErrors.ERR_NO_COMUNICAZIONE_10R);  
  }
  
  
  
  
  
  
  
  

  

%>

<jsp:forward page= "<%= comunicazione10R_modificaUrl %>" />



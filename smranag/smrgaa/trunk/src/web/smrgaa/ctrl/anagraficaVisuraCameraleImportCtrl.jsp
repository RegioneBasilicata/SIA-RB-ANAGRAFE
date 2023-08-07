<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@page import="javax.xml.bind.JAXBElement"%>
 <%@ page import="javax.xml.namespace.QName"%>

<%!
  private static final String URL_ANAGRAFICA_VISURA_CAMERE_VIEW = "/view/anagraficaVisuraCameraleView.jsp";
  private static final String URL_ERRORE = "/view/erroreView.jsp";
  private static final String URL_PAGE_BACK = "../layout/anagrafica.htm";
  private static final String URL_PAGE_BACK_ERRORE = "../layout/fonti.htm";
  private static final String ACTION = "../layout/anagraficaVisuraCameraleImport.htm";
  private static final String URL_ATTENDERE_PREGO = "/view/attenderePregoView.jsp";
%>

<%

  String iridePageName = "anagraficaVisuraCameraleImportCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  try
  {
    ValidationErrors errors = new ValidationErrors();
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
     
    //Vado a richiamare il sevizio di AAEP
    Azienda aziendaAAEP=null;
    AnagAAEPAziendaVO anagAAEPAziendaVO=new AnagAAEPAziendaVO();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");  

    String operazione = request.getParameter("operazione");
    
    // L'utente ha selezionato la voce di menù AAEP e io lo
    // mando alla pagina di attesa per il caricamento dati
    if("attenderePrego".equalsIgnoreCase(operazione)) 
    {	
	    request.setAttribute("denominazioneAttAAEP", request.getParameter("denominazione"));
	    request.setAttribute("partitaIVAAttAAEP", request.getParameter("partitaIVA"));
	    request.setAttribute("provinciaREAAttAAEP", request.getParameter("provinciaREA"));
	    request.setAttribute("numeroREAAttAAEP", request.getParameter("numeroREA"));
	    request.setAttribute("annoIscrizioneAttAAEP", request.getParameter("annoIscrizione"));
      request.setAttribute("numeroRegistroImpreseAttAAEP", request.getParameter("numeroRegistroImprese"));
	    request.setAttribute("pecAttAAEP", request.getParameter("pec"));
	    request.setAttribute("sedeLegaleAttAAEP", request.getParameter("sedeLegale"));
	    request.setAttribute("titolareRappresentanteAttAAEP", request.getParameter("titolareRappresentante"));
	    request.setAttribute("formaGiuridicaAttAAEP", request.getParameter("formaGiuridica"));
	    request.setAttribute("descrizioneAtecoAttAAEP", request.getParameter("descrizioneAteco"));
	    request.setAttribute("sezioneAttAAEP", request.getParameter("sezione"));
	    request.setAttribute("descrizioneAtecoSecAttAAEP", request.getParameter("descrizioneAtecoSec"));
	    request.setAttribute("dataIscrizioneREAAttAAEP", request.getParameter("dataIscrizioneREA"));
	    request.setAttribute("dataCancellazioneREAAttAAEP", request.getParameter("dataCancellazioneREA"));
	    request.setAttribute("dataIscrizioneRIAttAAEP", request.getParameter("dataIscrizioneRI"));
	    
    
      request.setAttribute("action", ACTION);
      operazione = "importaDati";
      request.setAttribute("operazione", operazione);
      %>
        <jsp:forward page= "<%= URL_ATTENDERE_PREGO %>" />
      <%
      return;
    }
    
    
    if(request.getParameter("annulla") != null) 
    {
      request.getRequestDispatcher(URL_ANAGRAFICA_VISURA_CAMERE_VIEW).forward(request, response);
      return;
    }
            
    aziendaAAEP = anagFacadeClient.cercaPerCodiceFiscale(anagAziendaVO.getCUAA());
    boolean rappLegaleAAEPValido=true;
    boolean sedeLegaleAAEPValido=true;

    
    //Vado a vedere che il dato restituito abbia come fonte infocamere
    /*if (!SolmrConstants.FONTE_DATO_INFOCAMERE_STR.equalsIgnoreCase(aziendaAAEP.getIdFonteDato()))
    {
      throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));
    }*/

    
    //Il servizio AAEP mi restituisce l'istat della provincia invece della
    //sigla, quindi vado a recuperarla
    ProvinciaVO prov=anagFacadeClient.getProvinciaByCriterio(aziendaAAEP.getProvinciaCCIAA().getValue());

    RappresentanteLegale rappresentanteLegaleAAEP=aziendaAAEP.getRappresentanteLegale().getValue();

    if (rappresentanteLegaleAAEP==null)
    {
      rappLegaleAAEPValido=false;
      rappresentanteLegaleAAEP=new RappresentanteLegale();
    }
    else{
      String codComuneRes = rappresentanteLegaleAAEP.getCodComuneResidenza().getValue();
      SolmrLogger.debug(this, " -- rappresentanteLegaleAAEP.codComuneRes ="+codComuneRes);
      
      // Risetto il codComuneRes secondo la decodifica presente sul nostro db (comune.istat_comune)
      if(codComuneRes != null && !codComuneRes.trim().equals("")){
	      String descrComuneRes = rappresentanteLegaleAAEP.getDescrComuneResidenza().getValue();
	      Vector<ComuneVO> comuni = anagFacadeClient.getComuniByDescCom(descrComuneRes);
	      if(comuni != null){
	    	  ComuneVO comune = comuni.get(0);
	    	  SolmrLogger.debug(this, " -- istat_comune residenza ="+comune.getIstatComune());
	    	  
	    	  QName codComuneResQName = new QName("http://servizio.frontend.ls.com", "codComuneResidenza");
	    	  JAXBElement<String> codComuneResidenzaValue = new JAXBElement<String>(codComuneResQName, String.class, comune.getIstatComune());
	    	  
	    	  rappresentanteLegaleAAEP.setCodComuneResidenza(codComuneResidenzaValue);
	      }
      }
      
      
      String codComuneNascita = rappresentanteLegaleAAEP.getCodComuneNascita().getValue();
      SolmrLogger.debug(this, " -- rappresentanteLegaleAAEP.codComuneNascita ="+codComuneNascita);
      // Risetto il codComuneNascita secondo la decodifica presente sul nostro db (comune.istat_comune)
      if(codComuneNascita != null && !codComuneNascita.trim().equals("")){
    	  String descrComuneNasc = rappresentanteLegaleAAEP.getDescrComuneNascita().getValue();
	      Vector<ComuneVO> comuni = anagFacadeClient.getComuniByDescCom(descrComuneNasc);
	      if(comuni != null){
	    	  ComuneVO comune = comuni.get(0);
	    	  SolmrLogger.debug(this, " -- istat_comune nascita ="+comune.getIstatComune());
	    	  
	    	  QName codComuneNascitaName = new QName("http://servizio.frontend.ls.com", "codComuneNascita");
	    	  JAXBElement<String> codComuneNascitaValue = new JAXBElement<String>(codComuneNascitaName, String.class, comune.getIstatComune());
	    	  
	    	  rappresentanteLegaleAAEP.setCodComuneNascita(codComuneNascitaValue);
	      }
      }
      
    }
    anagAAEPAziendaVO.setRappresentanteLegaleAAEP(rappresentanteLegaleAAEP);

    if(anagAziendaVO.getDataSituazioneAlStr()==null || anagAziendaVO.getDataSituazioneAlStr().equals(""))
    {
      anagAziendaVO.setDataSituazioneAlStr(DateUtils.formatDate(new Date(System.currentTimeMillis())));
    }

    PersonaFisicaVO personaVO = anagFacadeClient.getTitolareORappresentanteLegaleAzienda(anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));

    if (prov!=null)
    {
      anagAAEPAziendaVO.setProvinciaREAAAEP(prov.getSiglaProvincia());
    }
    
    //Mi faccio restituire la sede legale
    ListaSedi sedeRidottaAAEP=AnagAAEPAziendaVO.estraiSedeLegaleAAEP(aziendaAAEP.getListaSedi());

    
    //Con la sede legale restituita prima, che contiene solo un numero ridotto
    //di parametri vado a farmi dare una sede completa
    Sede sedeAAEP=null;
    if (sedeRidottaAAEP!=null)
    {
      sedeAAEP=anagFacadeClient.cercaPuntualeSede(aziendaAAEP.getCodiceFiscale().getValue(), sedeRidottaAAEP.getProgrSede().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);    		  
    }

    if (sedeAAEP==null)
    {
      sedeLegaleAAEPValido = false;
    }
    // Setto l'istat del comune secondo la nostra decodifica che abbiamo su comune.istat_comune
    if(sedeAAEP.getSiglaProvUL().getValue() != null){
      String istatProv = anagFacadeClient.getIstatProvinciaBySiglaProvincia(sedeAAEP.getSiglaProvUL().getValue());
      String istatComune = istatProv+sedeAAEP.getCodComune().getValue();
      SolmrLogger.debug(this, "--- istaComune sede new ="+istatComune);
      
      QName codComuneQName = new QName("http://servizio.frontend.ls.com", "codComune");
      JAXBElement<String> codComuneValue = new JAXBElement<String>(codComuneQName, String.class, istatComune);
      sedeAAEP.setCodComune(codComuneValue);
      
    }
    anagAAEPAziendaVO.setSedeAAEP(sedeAAEP);
    if(sedeAAEP != null)
    {
      String sedeCapAAEP = "";
      if("MULTI".equalsIgnoreCase(sedeAAEP.getCap().getValue()))
      {
        ComuneVO comuneSedeAAEP = anagFacadeClient.getComuneByISTAT(sedeAAEP.getCodComune().getValue());
        sedeCapAAEP = comuneSedeAAEP.getCap();
      }
      else
      {
        sedeCapAAEP = sedeAAEP.getCap().getValue();
      }        
      
      QName capQName = new QName("http://servizio.frontend.ls.com", "cap");
      JAXBElement<String> capValue = new JAXBElement<String>(capQName, String.class, sedeCapAAEP);
      
      anagAAEPAziendaVO.getSedeAAEP().setCap(capValue);
    }
    
    
    if (personaVO==null) 
    {
      personaVO=new PersonaFisicaVO();
    }

    
    //Dato che devo andare a leggere le descrizione dei comuni (nascita e
    //residenza) perchè AAEP mi passa solo l'istat li leggo qui e li imposto
    ComuneVO comune=anagFacadeClient.getComuneByISTAT(rappresentanteLegaleAAEP.getCodComuneNascita().getValue());
    anagAAEPAziendaVO.setRapLegNascitaComuneDesc(comune.getDescom());

    comune=anagFacadeClient.getComuneByISTAT(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue());
    if(comune != null)
    {
	    anagAAEPAziendaVO.setRapLegResidenzaComuneDesc(comune.getDescom());
	    if(rappresentanteLegaleAAEP.getCap() != null)
	    {
	      String resCapRap = "";
	      if("MULTI".equalsIgnoreCase(rappresentanteLegaleAAEP.getCap().getValue()))
	      {
	        resCapRap = comune.getCap();
	      }
	      else
	      {
	        resCapRap = rappresentanteLegaleAAEP.getCap().getValue();
	      }
	      anagAAEPAziendaVO.setRapLegCapResidenzaAAEP(resCapRap);
	    }
	  }

    comune=anagFacadeClient.getComuneByISTAT(personaVO.getNascitaComune());
    personaVO.setCittaNascita(comune.getDescom());

    anagAAEPAziendaVO.setFormaGiuridicaAAEP(anagFacadeClient.getDescTipoFormaGiuridica(aziendaAAEP.getIdNaturaGiuridica().getValue()));

    if(aziendaAAEP.getCodATECO2007().getValue() != null)
    {
      String codAtecoTmp = aziendaAAEP.getCodATECO2007().getValue(); 
      codAtecoTmp = codAtecoTmp.replaceAll("\\.", ""); 
      CodeDescription cd = anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codAtecoTmp);
      anagAAEPAziendaVO.setAtecoAAEP(cd);
    }
    
    String pec = aziendaAAEP.getPostaElettronicaCertificata().getValue();    		
    if(Validator.isNotEmpty(pec))
    {
      anagAAEPAziendaVO.setPecAAEP(pec);
    }
    
    /*ImpresaInfoc impresaInfoc = anagFacadeClient.cercaPerCodiceFiscaleINFOC(
      anagAziendaVO.getCUAA(),SolmrConstants.FONTE_DATO_INFOCAMERE_STR,null,null,null);*/

    /* Se viene restituito null segnalo che non è stata trovata nessuna anagrafica
       Non dovrebbe accadere perchè AAEP se non trova niente dovrebbe rilanciare un
       eccezione.
    */
    if (aziendaAAEP==null) 
      throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));
      
    List<ListaSedi> listaSede = aziendaAAEP.getListaSedi();
     
    Vector<CodeDescription> vAtecoSecAAEP = null;
    if (listaSede!=null)
    {
      Vector<String> vCodtecoSec = new Vector<String>();
      for(int i=0;i<listaSede.size();i++)
      {
        Sede sedeInfocamere = anagFacadeClient.cercaPuntualeSede(aziendaAAEP.getCodiceFiscale().getValue(), listaSede.get(i).getProgrSede().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
        		
        if (sedeInfocamere!=null)
        {           
          List<AtecoRI2007Infoc> atecoRI2007Infoc=sedeInfocamere.getListaAtecoRI2007Infoc();
          if (atecoRI2007Infoc!=null)
          {
            //Ateco Principale
            int k=0;
            boolean cicla=true;
            String codiceAteco = null;
            while(k<atecoRI2007Infoc.size() && cicla)
            {
              if ("P".equals(atecoRI2007Infoc.get(k).getCodImportanzaRI().getValue()))
                cicla=false;
              else k++;
            }           
            if (!cicla)
            {
              codiceAteco=atecoRI2007Infoc.get(k).getCodAteco2007().getValue();
              if(Validator.isNotEmpty(codiceAteco))
              {
                codiceAteco = codiceAteco.trim();
                codiceAteco = codiceAteco.replaceAll("\\.","");
                CodeDescription ateco=anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAteco);
                if (ateco!=null)
                {
                  anagAAEPAziendaVO.setIdAtecoAAEP(codiceAteco);
                  anagAAEPAziendaVO.setDescrizioneAtecoAAEP("("+StringUtils.checkNull(codiceAteco)+") "+
                    StringUtils.checkNull(ateco.getDescription()));
                }
              }
            }
            
            //Non trovato P cerco PI
            boolean consideroPI = false;
            if(Validator.isEmpty(codiceAteco))
            {
              consideroPI = true;
              k=0;
              cicla=true;
              while(k<atecoRI2007Infoc.size() && cicla)
              {
                if ("PI".equals(atecoRI2007Infoc.get(k).getCodImportanzaRI().getValue()))
                  cicla=false;
                else k++;
              }           
              if (!cicla)
              {
                codiceAteco=atecoRI2007Infoc.get(k).getCodAteco2007().getValue();
                if(Validator.isNotEmpty(codiceAteco))
                {
                  codiceAteco = codiceAteco.trim();
                  codiceAteco = codiceAteco.replaceAll("\\.","");
                  CodeDescription ateco=anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAteco);
                  if (ateco!=null)
                  {
                    anagAAEPAziendaVO.setIdAtecoAAEP(codiceAteco);
                    anagAAEPAziendaVO.setDescrizioneAtecoAAEP("("+StringUtils.checkNull(codiceAteco)+") "+
                      StringUtils.checkNull(ateco.getDescription()));
                  }
                }
              }
            }
            
            
            
            
            
            //Ateco Secondari
            for(int g=0;g<atecoRI2007Infoc.size();g++)
            {
              if(consideroPI)
              {
                if (!"PI".equalsIgnoreCase(atecoRI2007Infoc.get(g).getCodImportanzaRI().getValue()))
                {
                  if(!vCodtecoSec.contains(atecoRI2007Infoc.get(g).getCodAteco2007()))
                    vCodtecoSec.add(atecoRI2007Infoc.get(g).getCodAteco2007().getValue());   
                }
              }
              else
              {
                if (!"P".equalsIgnoreCase(atecoRI2007Infoc.get(g).getCodImportanzaRI().getValue()))
                {
                  if(!vCodtecoSec.contains(atecoRI2007Infoc.get(g).getCodAteco2007()))
                    vCodtecoSec.add(atecoRI2007Infoc.get(g).getCodAteco2007().getValue());   
                }
              }
            
            }
            
            /*String atecoSecondari = "";
            for(int g=0;g<vCodtecoSec.size();g++)
            {
              String codiceAtecoSec = vCodtecoSec.get(g).trim();
              codiceAtecoSec = codiceAtecoSec.replaceAll("\\.","");
              CodeDescription ateco=anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAtecoSec);
              if (ateco!=null)
              {
                //per il confronto****
                if(vAtecoSecAAEP == null)
                {
                  vAtecoSecAAEP = new Vector<CodeDescription>();
                }
                vAtecoSecAAEP.add(ateco);
                //****************
                
                if(g!=0)
                {
                  atecoSecondari += " - ";
                }
                atecoSecondari += "("+StringUtils.checkNull(codiceAtecoSec)+") "+
                  StringUtils.checkNull(ateco.getDescription());
              }            
            }             
            anagAAEPAziendaVO.setDescrizioneAtecoSecAAEP(atecoSecondari);
            anagAAEPAziendaVO.setvAtecoSecAAEP(vAtecoSecAAEP);*/
          }
        }       
      }
      
      String atecoSecondari = "";
      for(int g=0;g<vCodtecoSec.size();g++)
      {
        String codiceAtecoSec = vCodtecoSec.get(g).trim();
        codiceAtecoSec = codiceAtecoSec.replaceAll("\\.","");
        CodeDescription ateco=anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAtecoSec);
        if (ateco!=null)
        {
          //per il confronto****
          if(vAtecoSecAAEP == null)
          {
            vAtecoSecAAEP = new Vector<CodeDescription>();
          }
          vAtecoSecAAEP.add(ateco);
          //****************
          
          if(g!=0)
          {
            atecoSecondari += " - ";
          }
          atecoSecondari += "("+StringUtils.checkNull(codiceAtecoSec)+") "+
            StringUtils.checkNull(ateco.getDescription());
        }
      }
      anagAAEPAziendaVO.setDescrizioneAtecoSecAAEP(atecoSecondari);
      anagAAEPAziendaVO.setvAtecoSecAAEP(vAtecoSecAAEP);            
    }
    
    
    Vector<AziendaAtecoSecVO> vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
        anagAziendaVO.getIdAzienda().longValue());
    anagAziendaVO.setVAziendaATECOSec(vAziendaAtecoSec);
      
      
      
    //Stato impresa
    if(aziendaAAEP.getCodiceCausaleCessazione().getValue() == null || aziendaAAEP.getCodiceCausaleCessazione().getValue().trim().equals(""))
    {
      anagAAEPAziendaVO.setStatoImpresaAAEP("Attiva");
    }
    else
    {
      anagAAEPAziendaVO.setStatoImpresaAAEP("Cessata");
    }
    
    //Vigenza
    anagAAEPAziendaVO.setVigenzaAAEP("Si");
    if(aziendaAAEP.getListaProcConcors() != null && aziendaAAEP.getListaProcConcors().size() > 0)
    {
      for(int k=0;k<aziendaAAEP.getListaProcConcors().size();k++)
      {
        ProcConcors procConcorsInfoc = aziendaAAEP.getListaProcConcors().get(k);
        if(procConcorsInfoc != null && procConcorsInfoc.getDataAperturaProc().getValue() != null){
	        Date dataAperturaProc = DateUtils.convert(procConcorsInfoc.getDataAperturaProc().getValue());
	        GregorianCalendar gc = new GregorianCalendar();
	        gc.setTime(dataAperturaProc);
	        gc.add(Calendar.YEAR, 5);
	        if(gc.getTime().after(new Date()))
	        {
	          anagAAEPAziendaVO.setVigenzaAAEP("No");
	          break;
	        }
        }
      }      
    }
    
    //sezione
    List<SezSpec> listaSezInfoc = aziendaAAEP.getListaSezSpec();
    Vector<TipoSezioniAaepVO>  vTiposezioniAeep = null;
    String descrizioneTipoSezioneAAEP = "";
    if(listaSezInfoc != null && listaSezInfoc.size() > 0)
    {
      for(int k=0;k<listaSezInfoc.size();k++)
      {
    	SezSpec sezSpecInfoc = listaSezInfoc.get(k);
    	if(sezSpecInfoc != null){
    		SolmrLogger.debug(this, "-- sezSpecInfoc.getDataFine() ="+sezSpecInfoc.getDataFine().getValue());    		
	        if(sezSpecInfoc.getDataFine().getValue() == null
	            || (sezSpecInfoc.getDataFine().getValue() != null && DateUtils.convert(sezSpecInfoc.getDataFine().getValue()).after(new Date())))
	        {
		        String codiceSezione = sezSpecInfoc.getCodSezione().getValue();
		        TipoSezioniAaepVO tipoSezioniAaepVO = anagFacadeClient.getTipoSezioneAaepByCodiceSez(codiceSezione);
		        if(tipoSezioniAaepVO != null)
		        {
		          if(vTiposezioniAeep == null)
		            vTiposezioniAeep = new Vector<TipoSezioniAaepVO>();
		            
		          if( k!=0)
		            descrizioneTipoSezioneAAEP += " - ";
		            
		          descrizioneTipoSezioneAAEP += tipoSezioniAaepVO.getDescrizione();
		          vTiposezioniAeep.add(tipoSezioniAaepVO);
		        }
	        }
    	}
      }
    }
    anagAAEPAziendaVO.setDescrizioneTipoSezioneAAEP(descrizioneTipoSezioneAAEP);
    anagAAEPAziendaVO.setvTipoSezAAEP(vTiposezioniAeep);
    
    Vector<AziendaSezioniVO> vAziendaSezioni = gaaFacadeClient.getListActiveAziendaSezioniByIdAzienda(
          anagAziendaVO.getIdAzienda().longValue());
    anagAziendaVO.setvAziendaSezioni(vAziendaSezioni);
      

    String coltivatoreDiretto = "No";
    if(listaSezInfoc != null && listaSezInfoc.size() > 0)
    {
      for(int k=0;k<listaSezInfoc.size();k++)
      {
        SezSpec sezSpecInfoc = listaSezInfoc.get(k);
        if(sezSpecInfoc != null){
	        String codiceSezSpec = sezSpecInfoc.getCodiceSezSpec().getValue();
	        if("C".equalsIgnoreCase(codiceSezSpec))
	          coltivatoreDiretto = "Si";
        }
      }
    }    
    anagAAEPAziendaVO.setColtivatoreDirettoAAEP(coltivatoreDiretto);
    
    anagAAEPAziendaVO.setDataIscrizioneREAAAEP(aziendaAAEP.getDataIscrizioneRea().getValue());
    anagAAEPAziendaVO.setDataCancellazioneREAAAEP(aziendaAAEP.getDataCancellazRea().getValue());
    if(aziendaAAEP.getDataIscrRegistroImpr().getValue() != null && !aziendaAAEP.getDataIscrRegistroImpr().getValue().trim().equals(""))
    {
      anagAAEPAziendaVO.setDataIscrizioneRIAAEP(aziendaAAEP.getDataIscrRegistroImpr().getValue());
      Date dataIscrizioneRiDt = DateUtils.parseDate(aziendaAAEP.getDataIscrRegistroImpr().getValue());
      anagAAEPAziendaVO.setAnnoIscrizioneAAEP(new Integer(DateUtils.extractYearFromDate(dataIscrizioneRiDt)).toString());
    }
    
    

    anagAAEPAziendaVO.loadAnagAAEPAziendaVO(aziendaAAEP,anagAziendaVO,personaVO);
    
    
    request.setAttribute("anagAAEPAziendaVO", anagAAEPAziendaVO);


    if (sedeLegaleAAEPValido)
      request.setAttribute("sedeLegaleAAEPValido","true");
    else request.setAttribute("sedeLegaleAAEPValido","false");
    if (rappLegaleAAEPValido)
      request.setAttribute("rappLegaleAAEPValido","true");
    else request.setAttribute("rappLegaleAAEPValido","false");
  
  
  
    boolean denominazione=false;
    boolean partitaIVA=false;
    boolean descrizioneAteco=false;
    boolean provinciaREA=false;
    boolean numeroREA=false;
    boolean annoIscrizione=false;
    boolean numeroRegistroImprese=false;
    boolean pecControllo=false;
    boolean sedeLegale=false;
    boolean titolareRappresentante=false;
    boolean formaGiuridica=false;
    boolean sezione=false;
    boolean descrizioneAtecoSec=false;
    boolean dataIscrizioneREA=false;
    boolean dataCancellazioneREA=false;
    boolean dataIscrizioneRI=false;
    

    if ("true".equalsIgnoreCase(request.getParameter("denominazioneAttAAEP")))
      denominazione=true;
    if ("true".equalsIgnoreCase(request.getParameter("partitaIVAAttAAEP")))
      partitaIVA=true;
    if ("true".equalsIgnoreCase(request.getParameter("provinciaREAAttAAEP")))
      provinciaREA=true;
    if ("true".equalsIgnoreCase(request.getParameter("numeroREAAttAAEP")))
      numeroREA=true;
    if ("true".equalsIgnoreCase(request.getParameter("annoIscrizioneAttAAEP")))
      annoIscrizione=true;
    if ("true".equalsIgnoreCase(request.getParameter("numeroRegistroImpreseAttAAEP")))
      numeroRegistroImprese=true;
    if ("true".equalsIgnoreCase(request.getParameter("pecAttAAEP")))
      pecControllo=true;
    if ("true".equalsIgnoreCase(request.getParameter("sedeLegaleAttAAEP")))
      sedeLegale=true;
    if ("true".equalsIgnoreCase(request.getParameter("titolareRappresentanteAttAAEP")))
      titolareRappresentante=true;
    if ("true".equalsIgnoreCase(request.getParameter("formaGiuridicaAttAAEP")))
      formaGiuridica=true;
    if ("true".equalsIgnoreCase(request.getParameter("descrizioneAtecoAttAAEP")))
      descrizioneAteco = true;
    if ("true".equalsIgnoreCase(request.getParameter("sezioneAttAAEP")))
      sezione = true;
    if ("true".equalsIgnoreCase(request.getParameter("descrizioneAtecoSecAttAAEP")))
      descrizioneAtecoSec = true;
    if ("true".equalsIgnoreCase(request.getParameter("dataIscrizioneREAAttAAEP")))
      dataIscrizioneREA = true;
    if ("true".equalsIgnoreCase(request.getParameter("dataCancellazioneREAAttAAEP")))
      dataCancellazioneREA = true;
    if ("true".equalsIgnoreCase(request.getParameter("dataIscrizioneRIAttAAEP")))
      dataIscrizioneRI = true;
      
      
      
    
      
    
    boolean flagAtecoNonTrovato = false; 
    if(descrizioneAteco)
    {
      //non è stata trovata nesuna corrispondenza tra il codice ateco 2007
      //di aaep e le nostre tabelle
      if(anagAAEPAziendaVO.getAtecoAAEP() == null)
      {
        flagAtecoNonTrovato = true;
      }
    }
      
      

    if(!flagAtecoNonTrovato)
    {
      //Boh forse non serve più....
      //anagAAEPAziendaVO =(AnagAAEPAziendaVO)session.getAttribute("common"); 

      Long idAnagAzienda = anagFacadeClient.importaDatiAAEP(anagAAEPAziendaVO,anagAziendaVO,ruoloUtenza.getIdUtente(),
                                                        denominazione,partitaIVA,descrizioneAteco,
                                                        provinciaREA,numeroREA,annoIscrizione,
                                                        numeroRegistroImprese,pecControllo,sedeLegale,
                                                        titolareRappresentante,formaGiuridica, sezione,
                                                        descrizioneAtecoSec, dataIscrizioneREA, dataCancellazioneREA, dataIscrizioneRI);
      //session.removeAttribute("common");


      String dataPuntuale=anagAziendaVO.getDataSituazioneAlStr();


      Vector<AnagAziendaVO> result = anagFacadeClient.getAziendaCUAA(anagAziendaVO.getCUAA());

      int size=result.size();

      for(int i=0;i<size;i++)
      {
        AnagAziendaVO temp = (AnagAziendaVO) result.get(i);
        if (temp.getIdAnagAzienda().longValue()==idAnagAzienda.longValue())
        {
          anagAziendaVO= (AnagAziendaVO) result.get(i);
          break;
        }
      }



      anagAziendaVO.setDataSituazioneAlStr(dataPuntuale);

      
      //Rimuovo dalla sessione anagAziendaVO perchè devo aggiornarlo con i nuovi
      //dati presi da AAEP
      session.removeAttribute("anagAziendaVO");
      session.setAttribute("anagAziendaVO",anagAziendaVO);

      response.sendRedirect(URL_PAGE_BACK);

      return;
    }
    else
    {
      String messaggio = AnagErrors.ERR_AEEP_NO_COD_ATECO_2007_TROVATO;
      request.setAttribute("messaggioErrore",messaggio);
    }
    
  }
  catch(SolmrException se)
  {
	SolmrLogger.error(this, "-- SolmrException in anagraficaVisuraCameraleImportCtrl ="+se.getMessage());  
    request.setAttribute("errore",se.getMessage());
    request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
    %>
       <jsp:forward page="<%= URL_ERRORE %>"/>
    <%
    return;
  }
  catch(Exception e)
  {
	SolmrLogger.error(this, "-- Exeption in anagraficaVisuraCameraleImportCtrl ="+e.getMessage());  
    request.setAttribute("errore",e.getMessage());
    request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
    %>
       <jsp:forward page="<%= URL_ERRORE %>"/>
    <%
    return;
  }
%>
<jsp:forward page="<%= URL_ANAGRAFICA_VISURA_CAMERE_VIEW %>"/>


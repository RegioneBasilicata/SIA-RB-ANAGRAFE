<%@page import="javax.xml.bind.JAXBElement"%>
<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.dto.anag.infocamere.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.ws.infoc.*"%>
<%@ page
	import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@ page import="javax.xml.namespace.QName"%>

<%!
  private static final String URL_ANAGRAFICA_VISURA_CAMERE_VIEW = "/view/anagraficaVisuraCameraleView.jsp";
  private static final String URL_ERRORE = "/view/erroreView.jsp";
  private static final String URL_PAGE_BACK = "../layout/anagrafica.htm";
  private static final String URL_PAGE_BACK_ERRORE = "../layout/fonti.htm";
  private static final String ACTION = "../layout/anagraficaVisuraCamerale.htm";
  private static final String URL_ATTENDERE_PREGO = "/view/attenderePregoView.jsp";
%>

<%

  String iridePageName = "anagraficaVisuraCameraleCtrl.jsp";
  %><%@include file="/include/autorizzazione.inc"%>
<%
  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    
    //Vado a richiamare il sevizio di Infocamere
    Azienda azienda=null;
    AnagAziendaInfocVO anagAziendaInfocVO=new AnagAziendaInfocVO();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

    String operazione = request.getParameter("operazione");

    // L'utente ha selezionato la voce di menù AAEP e io lo
    // mando alla pagina di attesa per il caricamento dati
    if("attenderePrego".equalsIgnoreCase(operazione)) 
    {
      request.setAttribute("action", ACTION);
      operazione = null;
      request.setAttribute("operazione", operazione);
      %>
<jsp:forward page="<%= URL_ATTENDERE_PREGO %>" />
<%
      return;
    }


    try
    {
      azienda = anagFacadeClient.cercaPerCodiceFiscale(anagAziendaVO.getCUAA());
        
        
      boolean rappLegaleAAEPValido=true;
      boolean sedeLegaleAAEPValido=true;

      
      //Vado a vedere che il dato restituito abbia come fonte infocamere
     /* if(!SolmrConstants.FONTE_DATO_INFOCAMERE_STR.equalsIgnoreCase(azienda.getIdFonteDato()))
        throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));*/

      
      //Il servizio AAEP mi restituisce l'istat della provincia invece della
      //sigla, quindi vado a recuperarla
      ProvinciaVO prov=anagFacadeClient.getProvinciaByCriterio(azienda.getProvinciaCCIAA().getValue());

      RappresentanteLegale rappresentanteLegale=azienda.getRappresentanteLegale().getValue();

      if (rappresentanteLegale==null)
      {
        rappLegaleAAEPValido=false;
        rappresentanteLegale=new RappresentanteLegale();
      }
      anagAziendaInfocVO.setRappresentanteLegale(rappresentanteLegale);

      if(anagAziendaVO.getDataSituazioneAlStr()==null || anagAziendaVO.getDataSituazioneAlStr().equals(""))
      {
        anagAziendaVO.setDataSituazioneAlStr(DateUtils.formatDate(new Date(System.currentTimeMillis())));
      }

      PersonaFisicaVO personaVO = anagFacadeClient.getTitolareORappresentanteLegaleAzienda(anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));

      if (prov != null)
      {
        anagAziendaInfocVO.setProvinciaREAAAEP(prov.getSiglaProvincia());
      }
      
      //Mi faccio restituire la sede legale
      ListaSedi sedeLegale = null;
      if(azienda.getListaSedi() != null)
      {
        sedeLegale=AnagAziendaInfocVO.estraiSedeLegaleAAEP(azienda.getListaSedi());
      }

      //Con la sede legale restituita prima, che contiene solo un numero ridotto
      //di parametri vado a farmi dare una sede completa
      Sede sede=null;
      if (sedeLegale!=null)
      {
        sede = anagFacadeClient.cercaPuntualeSede(azienda.getCodiceFiscale().getValue(), sedeLegale.getProgrSede().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);        		
      }

      if (sede==null) 
      {
        sedeLegaleAAEPValido=false;
      }
      anagAziendaInfocVO.setSedeAAEP(sede);
      if(sede != null)
      {
        String sedeCapAAEP = "";
        if("MULTI".equalsIgnoreCase(sede.getCap().getValue()))
        {
          ComuneVO comuneSedeAAEP = anagFacadeClient.getComuneByISTAT(sede.getCodComune().getValue());
          sedeCapAAEP = comuneSedeAAEP.getCap();
        }
        else
        {
          sedeCapAAEP = sede.getCap().getValue();
        }  
        QName capQName = new QName("http://servizio.frontend.ls.com", "cap");
        JAXBElement<String> capValue = new JAXBElement<String>(capQName, String.class, sedeCapAAEP);       
        
        anagAziendaInfocVO.getSedeAAEP().setCap(capValue);
      }
      
      
      if (personaVO==null)
      {
        personaVO = new PersonaFisicaVO();
      }

      //Dato che devo andare a leggere le descrizione dei comuni (nascita e
      //residenza) perchè AAEP mi passa solo l'istat li leggo qui e li imposto
      ComuneVO comuneRapNas = null;
      if(rappresentanteLegale.getCodComuneNascita() != null)
      {
        comuneRapNas = anagFacadeClient.getComuneByISTAT(rappresentanteLegale.getCodComuneNascita().getValue());
      }
      if(comuneRapNas != null)
      {
        anagAziendaInfocVO.setRapLegNascitaComuneDesc(comuneRapNas.getDescom());
      }

      ComuneVO comuneRapRes = null;
      if(rappresentanteLegale.getCodComuneResidenza() != null)
      {
        comuneRapRes = anagFacadeClient.getComuneByISTAT(rappresentanteLegale.getCodComuneResidenza().getValue());
      }
      if(comuneRapRes != null)
      {
        anagAziendaInfocVO.setRapLegResidenzaComuneDesc(comuneRapRes.getDescom());
        if(rappresentanteLegale.getCap() != null)
        {
          String resCapRap = "";
          if("MULTI".equalsIgnoreCase(rappresentanteLegale.getCap().getValue()))
          {
            resCapRap = comuneRapRes.getCap();
          }
          else
          {
            resCapRap = rappresentanteLegale.getCap().getValue();
          }
          anagAziendaInfocVO.setRapLegCapResidenzaAAEP(resCapRap);
        }
        
      }

      ComuneVO comunePersNas = null;
      if(personaVO.getNascitaComune() !=null)
      {
        comunePersNas = anagFacadeClient.getComuneByISTAT(personaVO.getNascitaComune());
      }
      if(comunePersNas !=null)
      {
        personaVO.setCittaNascita(comunePersNas.getDescom());
      }
      
      if(azienda.getIdNaturaGiuridica() !=null)
      {
        String descTipoFormaGiuridica = anagFacadeClient.getDescTipoFormaGiuridica(azienda.getIdNaturaGiuridica().getValue());
        anagAziendaInfocVO.setFormaGiuridicaAAEP(descTipoFormaGiuridica);
      }
     
      anagAziendaInfocVO.setPecAAEP(azienda.getPostaElettronicaCertificata().getValue());
           
      
    /*  ImpresaInfoc impresaInfoc = anagFacadeClient.cercaPerCodiceFiscaleINFOC(
        anagAziendaVO.getCUAA(),SolmrConstants.FONTE_DATO_INFOCAMERE_STR,null,null,null);*/

      /* Se viene restituito null segnalo che non è stata trovata nessuna anagrafica
         Non dovrebbe accadere perchè AAEP se non trova niente dovrebbe rilanciare un
         eccezione.
      */
     /* if (impresaInfoc==null) 
        throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));*/
        
      List<ListaSedi> listaSede = azienda.getListaSedi();
      Vector<CodeDescription> vAtecoSecAAEP = null;
      if (listaSede!=null){
        Vector<String> vCodtecoSec = new Vector<String>();
        for(int i=0;i<listaSede.size();i++){
          ListaSedi listaSedi = listaSede.get(i); 
          if(listaSedi != null && listaSedi.getProgrSede().getValue() != null){
            Sede sedeInfocamere = anagFacadeClient.cercaPuntualeSede(azienda.getCodiceFiscale().getValue(), listaSede.get(i).getProgrSede().getValue() , SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
            
            if (sedeInfocamere!=null){			      
	          List<AtecoRI2007Infoc> atecoRI2007Infoc=sedeInfocamere.getListaAtecoRI2007Infoc();
			  if (atecoRI2007Infoc!=null){
			  //Ateco Principale
			  int k=0;
			  boolean cicla=true;
			  String codiceAteco = null;
			  while(k<atecoRI2007Infoc.size() && cicla){
			    if ("P".equals(atecoRI2007Infoc.get(k).getCodImportanzaRI()))
			      cicla=false;
			    else
			      k++;
			  }		        
			  if (!cicla){
                codiceAteco=atecoRI2007Infoc.get(k).getCodAteco2007().getValue();
			    if(Validator.isNotEmpty(codiceAteco)){
			      codiceAteco = codiceAteco.trim();
			      codiceAteco = codiceAteco.replaceAll("\\.","");
			      CodeDescription ateco=anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAteco);
			      if (ateco!=null){
			        anagAziendaInfocVO.setIdAtecoAAEP(codiceAteco);
                    anagAziendaInfocVO.setDescrizioneAtecoAAEP("("+StringUtils.checkNull(codiceAteco)+") "+
                    StringUtils.checkNull(ateco.getDescription()));			        
			      }
			    }
			  }
			        
			  //Non trovato P cerco PI
			  boolean consideroPI = false;
			  if(Validator.isEmpty(codiceAteco)){
			      consideroPI = true;
				  k=0;
	              cicla=true;
	              while(k<atecoRI2007Infoc.size() && cicla)
	              {
	                if ("PI".equals(atecoRI2007Infoc.get(k).getCodImportanzaRI()))
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
	                    anagAziendaInfocVO.setIdAtecoAAEP(codiceAteco);
	                    anagAziendaInfocVO.setDescrizioneAtecoAAEP("("+StringUtils.checkNull(codiceAteco)+") "+
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
			              if(!vCodtecoSec.contains(atecoRI2007Infoc.get(g).getCodAteco2007().getValue()))
			                vCodtecoSec.add(atecoRI2007Infoc.get(g).getCodAteco2007().getValue());   
			            }
			          }
			        
			        }			        			      
			      }
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
        anagAziendaInfocVO.setDescrizioneAtecoSecAAEP(atecoSecondari);
        anagAziendaInfocVO.setvAtecoSecAAEP(vAtecoSecAAEP);
      }
      
      
      Vector<AziendaAtecoSecVO> vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
          anagAziendaVO.getIdAzienda().longValue());
      anagAziendaVO.setVAziendaATECOSec(vAziendaAtecoSec);
             
        
        
      //Stato impresa
      if(Validator.isNotEmpty(azienda.getCodiceCausaleCessazione()))
      {
        anagAziendaInfocVO.setStatoImpresaAAEP("Attiva");
      }
      else
      {
        anagAziendaInfocVO.setStatoImpresaAAEP("Cessata");
      }
      
      //Vigenza
      anagAziendaInfocVO.setVigenzaAAEP("Si");
      if(azienda.getListaProcConcors() != null && azienda.getListaProcConcors().size() > 0)
      {
        for(int k=0;k<azienda.getListaProcConcors().size();k++)
        {
          ProcConcors procConcorsInfoc = azienda.getListaProcConcors().get(k);
          if(procConcorsInfoc != null){
	          Date dataAperturaProc = DateUtils.convert(procConcorsInfoc.getDataAperturaProc().getValue());
	          GregorianCalendar gc = new GregorianCalendar();
	          gc.setTime(dataAperturaProc);
	          gc.add(Calendar.YEAR, 5);
	          if(gc.getTime().after(new Date()))
	          {
	            anagAziendaInfocVO.setVigenzaAAEP("No");
	            break;
	          }
          }
        }      
      }

      
      List<SezSpec> listaSezInfoc = azienda.getListaSezSpec();
      
      //sezione
      Vector<TipoSezioniAaepVO>  vTiposezioniAeep = null;
      String descrizioneTipoSezioneAAEP = "";
      if(listaSezInfoc != null && listaSezInfoc.size() > 0)
      {
        for(int k=0;k<listaSezInfoc.size();k++)
        {
          SezSpec sezSpecInfoc = listaSezInfoc.get(k);
          if(Validator.isEmpty(sezSpecInfoc.getDataFine())
            || (Validator.isNotEmpty(DateUtils.convert(sezSpecInfoc.getDataFine().getValue())) && DateUtils.convert(sezSpecInfoc.getDataFine().getValue()).after(new Date())))
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
      anagAziendaInfocVO.setDescrizioneTipoSezioneAAEP(descrizioneTipoSezioneAAEP);
      anagAziendaInfocVO.setvTipoSezAAEP(vTiposezioniAeep);
      
      Vector<AziendaSezioniVO> vAziendaSezioni = gaaFacadeClient.getListActiveAziendaSezioniByIdAzienda(
          anagAziendaVO.getIdAzienda().longValue());
      anagAziendaVO.setvAziendaSezioni(vAziendaSezioni);
              
      
      String coltivatoreDiretto = "No";
      if(listaSezInfoc != null && listaSezInfoc.size() > 0)
      {
        for(int k=0;k<listaSezInfoc.size();k++)
        {
          SezSpec sezSpecInfoc = listaSezInfoc.get(k);
          String codiceSezSpec = sezSpecInfoc.getCodiceSezSpec().getValue();
          if("C".equalsIgnoreCase(codiceSezSpec))
            coltivatoreDiretto = "Si";
        }
      }      
      anagAziendaInfocVO.setColtivatoreDirettoAAEP(coltivatoreDiretto);
      
      anagAziendaInfocVO.setDataIscrizioneREAAAEP(azienda.getDataIscrizioneRea().getValue());
      anagAziendaInfocVO.setDataCancellazioneREAAAEP(azienda.getDataCancellazRea().getValue());
      if(Validator.isNotEmpty(azienda.getDataIscrRegistroImpr().getValue()))
      {
        anagAziendaInfocVO.setDataIscrizioneRIAAEP(azienda.getDataIscrRegistroImpr().getValue());
        Date dataIscrizioneRiDt = DateUtils.parseDate(azienda.getDataIscrRegistroImpr().getValue());
        anagAziendaInfocVO.setAnnoIscrizioneAAEP(new Integer(DateUtils.extractYearFromDate(dataIscrizioneRiDt)).toString());
      }
      //anagAziendaInfocVO.setDataIscrizioneRIAAEP(impresaInfoc.getDataIscrRegistroImpr());
      
      
      
      anagAziendaInfocVO.loadAnagAAEPAziendaVO(azienda,anagAziendaVO,personaVO);
      
      
      request.setAttribute("anagAziendaInfocVO", anagAziendaInfocVO);


      if (sedeLegaleAAEPValido)
        request.setAttribute("sedeLegaleAAEPValido","true");
      else request.setAttribute("sedeLegaleAAEPValido","false");
      if (rappLegaleAAEPValido)
        request.setAttribute("rappLegaleAAEPValido","true");
      else request.setAttribute("rappLegaleAAEPValido","false");

      //Boh forse non serve più....
      //session.setAttribute("common", anagAziendaInfocVO);
    }
    catch(SolmrException sx) 
    {
      request.setAttribute("messaggioErrore",sx.getMessage());
      request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
      %>
<jsp:forward page="<%= URL_ERRORE %>" />
<%
      return;
    }
  }
  catch(SolmrException se)
  {
    if (se!=null)
      if (se.getMessage().equals((String)AnagErrors.get("ERR_AAEP_DATI_TITOLARE_INCOMPLETI")))
      {
        request.setAttribute("messaggioErrore",se.getMessage());
        request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
        %>
<jsp:forward page="<%= URL_ERRORE %>" />
<%
        return;
      }
    it.csi.solmr.util.ValidationErrors ve=new ValidationErrors();
    ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors",ve);
    request.getRequestDispatcher(URL_ANAGRAFICA_VISURA_CAMERE_VIEW).forward(request, response);
    return;
  }
  catch(Exception e)
  {
    it.csi.solmr.util.ValidationErrors ve=new ValidationErrors();
    e.printStackTrace();
    ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors",ve);
    request.getRequestDispatcher(URL_ANAGRAFICA_VISURA_CAMERE_VIEW).forward(request, response);
    return;
  }
%>
<jsp:forward page="<%= URL_ANAGRAFICA_VISURA_CAMERE_VIEW %>" />


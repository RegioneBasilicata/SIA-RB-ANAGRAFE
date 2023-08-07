<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="aziendaVO" scope="request" class="it.csi.solmr.dto.anag.AnagAziendaVO">
<jsp:setProperty name="aziendaVO" property="*" />
<%
  aziendaVO.setCUAA(request.getParameter("CUAA"));
  aziendaVO.setCUAASubentro(request.getParameter("CUAASubentro"));
%>
</jsp:useBean>
<%

  String iridePageName = "nuovaAziendaCCIAACtrl.jsp";


  %>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%


  Azienda aziendaAAEP=null;

  Vector vectRange = (Vector)session.getAttribute("listRange");

  //Dato che la WebUtils.removeUselessAttributes(session) non possiamo utilizzarla
  //perchè cre dei problemi alla login elimino tutti i parametri messi in sessione
  //all'interno del caso d'uso della nuova azienda
  //WebUtils.removeUselessAttributes(session);

  session.removeAttribute("listRange");
  session.removeAttribute("sedeAAEP");
  //session.removeAttribute("pecAAEP");
  session.removeAttribute("anagTribForInsert");
  session.removeAttribute("rappresentanteLegaleAAEP");
  session.removeAttribute("insAnagVO");
  session.removeAttribute("nascitaStatoEstero");
  session.removeAttribute("insPerFisVO");
  session.removeAttribute("anagAziendaVO");
  session.removeAttribute("indietro");
  session.removeAttribute("erroreCF");
  session.removeAttribute("codiceFiscaleRapp");
  session.removeAttribute("modPerFisVO");
  session.removeAttribute("codiceFiscale");
  session.removeAttribute("insUteVO");
  session.removeAttribute("insVAziendaAtecoSec");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  String errorPage = "/view/nuovaAziendaCCIAAView.jsp";
  String ricAAEPURL = "/view/nuovaAziendaAnagraficaView.jsp";
  String confermaNuovaAzienda = "/view/confermaNuovaAziendaView.jsp";
  String pageBack= "../layout/nuovaAziendaAnagrafica.htm";
  String pageNext= "../layout/nuovaAziendaAnagraficaForward.htm";
  String URL_ERRORE = "/view/erroreNuovaAziendaAAEPView.jsp";
  String pageElenco= "/view/nuovoElencoView.jsp";

  ValidationErrors errors = new ValidationErrors();
  boolean multiploCUAA = false;
	// Recupero il cuaa subentro impostato
	String cuaaProvenienza = request.getParameter("cuaaProvenienza");
	request.setAttribute("cuaaProvenienza", cuaaProvenienza);
	AnagAziendaVO[] elencoAziende = null;
	// L'utente ha premuto il tasto avanti e non arriva dalla pagina di elenco
	// aziende subentro
	if(Validator.isNotEmpty(request.getParameter("avanti")) && !Validator.isNotEmpty(request.getParameter("multiploCUAA"))) 
  {
		request.setAttribute("radiobuttonAzienda",request.getParameter("radiobuttonAzienda"));
	  request.setAttribute("CUAA",aziendaVO.getCUAA());
		// Se l'utente ha selezionato l'opzione di subentro ad azienda cessata
		if(Validator.isNotEmpty(request.getParameter("radiobuttonAzienda")) 
      && request.getParameter("radiobuttonAzienda").equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
			errors = aziendaVO.validateRicAAEP(false, false);
			// Controllo che sia stato valorizzato
			if(!Validator.isNotEmpty(cuaaProvenienza)) 
      {
				errors.add("cuaaProvenienza", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
				request.setAttribute("errors", errors);
		    request.getRequestDispatcher(errorPage).forward(request, response);
		    return;
			}
			// Se lo è ...
			else 
      {
        
				if(cuaaProvenienza.length() != 16 && cuaaProvenienza.length() != 11) 
        {
					errors.add("cuaaProvenienza", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				}
				if(errors != null && errors.size() > 0) 
        {
			   	request.setAttribute("errors", errors);
			    request.getRequestDispatcher(errorPage).forward(request, response);
			    return;
			  }
				// ... cerco su DB_ANAGRAFICA_AZIENDA se esiste almeno un'azienda
				// agricola cessata
				try 
        {
					String[] orderBy = {SolmrConstants.ORDER_BY_DATA_CESSAZIONE_DESC};
					elencoAziende = anagFacadeClient.getListAnagAziendaVOByCuaa(cuaaProvenienza, false, true, orderBy);
				  if(elencoAziende == null || elencoAziende.length == 0) 
          {
						errors.add("cuaaProvenienza", new ValidationError(AnagErrors.ERRORE_NO_AZIENDE_CESSATE_FOR_CUAA));
						request.setAttribute("errors", errors);
				    request.getRequestDispatcher(errorPage).forward(request, response);
				    return;
					}
					else if(elencoAziende.length == 1) 
          {
						aziendaVO.setIdAziendaProvenienza(((AnagAziendaVO)elencoAziende[0]).getIdAzienda());
					}
					else 
          {
            Vector temp=new Vector();
            for (int i=0;i<elencoAziende.length;i++)
              temp.add(elencoAziende[i]);
            session.setAttribute("listRange", temp);
            request.setAttribute("CUAA",aziendaVO.getCUAA());
            request.setAttribute("cuaaProvenienza", cuaaProvenienza);
            request.setAttribute("radiobuttonAzienda", request.getParameter("radiobuttonAzienda"));
            %>
              <jsp:forward page ="<%=pageElenco%>" />
            <%
					}
				}
				catch(SolmrException se) 
        {
					errors.add("cuaaProvenienza", new ValidationError(AnagErrors.ERRORE_KO_ANAGRAFICA_AZIENDA));
					request.setAttribute("errors", errors);
			    request.getRequestDispatcher(errorPage).forward(request, response);
			    return;
				}
			}
			
		}
	}

  try
  {

    if ("avanti".equals(request.getParameter("multiploCUAA"))) multiploCUAA=true;

    if (multiploCUAA)
    {
      try
      {
        aziendaVO.setIdAziendaSubentro(new Long(Long.parseLong(request.getParameter("idAziendaSubentro"))));
        if(request.getParameter("idAziendaSubentro") != null) 
        {
        	aziendaVO.setIdAziendaProvenienza(new Long(Long.parseLong(request.getParameter("idAziendaSubentro"))));
        }
      }
      catch(Exception e)
      {
        request.setAttribute("CUAA",(String)request.getParameter("CUAA"));
        request.setAttribute("CUAASubentro",(String)request.getParameter("CUAASubentro"));
        request.setAttribute("radiobuttonAzienda",(String)request.getParameter("radiobuttonAzienda"));
        session.setAttribute("listRange",vectRange);
        ValidationError error = new ValidationError("Selezionare un''azienda");
        errors = new ValidationErrors();
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(pageElenco).forward(request, response);
        return;
      }
    }


    if(request.getParameter("ricercaAAEP")!=null || multiploCUAA)
    {
      boolean aziendaInCostituzione=false;
      Vector result=null;
      String radiobuttonAzienda=request.getParameter("radiobuttonAzienda");
      String idAziendaSubentro=request.getParameter("idAziendaSubentro");
      String idAziendaProvenienza = request.getParameter("idAziendaProvenienza");
      request.setAttribute("radiobuttonAzienda",radiobuttonAzienda);
      request.setAttribute("idAziendaSubentro",idAziendaSubentro);
      request.setAttribute("idAziendaProvenienza",idAziendaProvenienza);

      
      //se hanno selezionato il 2° o 3° radiobutton significa che l'azienda
      //è in costituzione
      if ("2".equals(radiobuttonAzienda)
          ||
          "3".equals(radiobuttonAzienda)
          )
      {
        aziendaInCostituzione=true;
      }

      errors = null;

      if (!multiploCUAA)
      {
        boolean CUAASubentroNonPresente=false,CUAAPresente=false;
        if ("3".equals(radiobuttonAzienda))
        {
          //Vado a fare i controlli solo se ho inserito il CUAA di subentro
          //dato che non è un dato obbligatorio
          if (Validator.isNotEmpty(aziendaVO.getCUAASubentro()))
          {
            aziendaVO.setCUAASubentro(aziendaVO.getCUAASubentro().trim());
            //1.	ricerca di un\u2019azienda attiva con il cuaa di provenienza inserito
            //(db_anagrafica_azienda.data_cessazione a null,
            //db_anagrafica_azienda.data_fine_validita a null)
            Vector temp=anagFacadeClient.getAziendaCUAA(aziendaVO.getCUAASubentro());

            //Dato che il metodo getAziendaCUAA mi restituisce anche quelle cessate
            //ciclo per andare ad escludere
            if (temp!=null)
            {
              result=new Vector();
              for (int i=0;i<temp.size();i++)
              {
                if (((AnagAziendaVO)temp.get(i)).getDataCessazione()==null)
                  result.add(temp.get(i));
              }
            }


            if (result==null || result.size()==0)
            {
              //2.	nel caso in cui non venga trovata alcuna azienda nel
              //passo precendente: ricerca di un\u2019azienda cessata con il cuaa di
              //provenienza inserito (db_anagrafica_azienda.data_cessazione not null,
              //db_anagrafica_azienda.data_fine_validita a null)
              String[] orderBy = {SolmrConstants.ORDER_BY_DATA_CESSAZIONE_DESC};
              elencoAziende = anagFacadeClient.getListAnagAziendaVOByCuaa(aziendaVO.getCUAASubentro(), true, true, orderBy);
              result=new Vector();
              for (int i=0;i<elencoAziende.length;i++)
                result.add(elencoAziende[i]);
            }

            if (result==null || result.size()==0)
              CUAASubentroNonPresente = true;
          }
        }
        // Se l'utente ha selezionato l'opzione di subentro a nuovo CUAA
        if(Validator.isNotEmpty(radiobuttonAzienda) && radiobuttonAzienda.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
			    // ... cerco su DB_ANAGRAFICA_AZIENDA se esiste almeno un'azienda
			    // agricola cessata
			    try 
          {
    				String[] orderBy = {SolmrConstants.ORDER_BY_DATA_CESSAZIONE_DESC};
    				elencoAziende = anagFacadeClient.getListAnagAziendaVOByCuaa(cuaaProvenienza, false, true, orderBy);
    			  if(elencoAziende == null || elencoAziende.length == 0) 
            {
    					errors.add("cuaaProvenienza", new ValidationError(AnagErrors.ERRORE_NO_AZIENDE_CESSATE_FOR_CUAA));
    					request.setAttribute("errors", errors);
    			    request.getRequestDispatcher(errorPage).forward(request, response);
    			    return;
				    }
				    else if(elencoAziende.length == 1) 
            {
					    aziendaVO.setIdAziendaProvenienza(((AnagAziendaVO)elencoAziende[0]).getIdAzienda());
				    }
				    else 
            {
					    session.setAttribute("listRange", elencoAziende);
			        request.setAttribute("CUAA",aziendaVO.getCUAA());
			        request.setAttribute("cuaaProvenienza", cuaaProvenienza);
			        request.setAttribute("radiobuttonAzienda", request.getParameter("radiobuttonAzienda"));
			        %>
			      	  <jsp:forward page ="<%=pageElenco%>" />
			        <%
				    }
			    }
			    catch(SolmrException se) 
          {
				    errors.add("cuaaProvenienza", new ValidationError(AnagErrors.ERRORE_KO_ANAGRAFICA_AZIENDA));
				    request.setAttribute("errors", errors);
		        request.getRequestDispatcher(errorPage).forward(request, response);
		        return;
			    }
        }
        errors = aziendaVO.validateRicAAEP(CUAASubentroNonPresente,CUAAPresente);

        if (! (errors == null || errors.size() == 0)) 
        {
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(errorPage).forward(request, response);
          return;
        }
      }
      // Se arrivo dalla pagina di elenco aziende e avevo impostato l'opzione di
      // subentro
      else if(Validator.isNotEmpty(radiobuttonAzienda) && radiobuttonAzienda.equalsIgnoreCase(SolmrConstants.FLAG_S) && multiploCUAA) 
      {
    	  // Setto l'id_azienda provenienza con il valore selezionato dall'utente
    	  // nella pagina
    	  aziendaVO.setIdAziendaProvenienza(new Long(Long.parseLong(request.getParameter("idAziendaSubentro"))));
      }
      // Ho scelto opzioni che non prevedono subentri
      else 
      {
    	  result=new Vector();
      }
      if (aziendaInCostituzione || multiploCUAA)
      {
        if ("3".equals(radiobuttonAzienda) && result!=null && result.size()>1)
        {
          //Ho trovato più CUAA corrispondenti al CUAA inserito come subentro
          //quindi devo presentare una lista in cui possano selezionare
          //quello corretto
          session.setAttribute("listRange",result);
          request.setAttribute("CUAA",aziendaVO.getCUAA());
          request.setAttribute("CUAASubentro",aziendaVO.getCUAASubentro());
          request.setAttribute("radiobuttonAzienda",radiobuttonAzienda);
          %>
              <jsp:forward page ="<%=pageElenco%>" />
          <%
          return;
        }
        else
        {
          if ("3".equals(radiobuttonAzienda) && result!=null && result.size()==1)
          {
            AnagAziendaVO aziendaTemp=(AnagAziendaVO)result.get(0);
            request.setAttribute("idAziendaSubentro",aziendaTemp.getIdAzienda().toString());
            aziendaVO.setIdAziendaSubentro(aziendaTemp.getIdAzienda());
          }
        }
        if (multiploCUAA)
        {
          aziendaVO.setCUAA(request.getParameter("CUAA"));
          aziendaVO.setCUAASubentro(request.getParameter("CUAASubentro"));
        }

        boolean aziendaAAEPTrovata=false;
        boolean aziendaAAEPCessata=false;
        boolean aziendaAAEPAltreFonti=false;

        String parametroAAEP = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_AAEP"));
        SolmrLogger.debug(this, "-- parametroAAEP ="+parametroAAEP);
        String parametroTRIB = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_TRIB"));
        SolmrLogger.debug(this, "-- parametroTRIB ="+parametroTRIB);
        
        // Se sono in piemonte e il parametro di AAEP è valorizzato a "S" significa
        // che devo richiamare il servizio di AAEP
        if ((parametroAAEP.equalsIgnoreCase(SolmrConstants.FLAG_S)))
        {
          %>
            <jsp:forward page ="<%=ricAAEPURL%>" />
          <%
          return;
        }


        try
        {
          aziendaAAEP = anagFacadeClient.cercaPerCodiceFiscale(aziendaVO.getCUAA());
          aziendaAAEPTrovata=true;
        }
        catch (SolmrException sex)
        {
          if (((String)AnagErrors.get("ERR_AAEP_GENERICO")).equals(sex.getMessage()))
          {
            request.setAttribute("pageBack",pageBack);
            request.setAttribute("msgErrore", (String)AnagErrors.get("ERR_AAEP_GENERICO"));
            request.setAttribute("CUAA", aziendaVO.getCUAA());
            request.getRequestDispatcher(URL_ERRORE).forward(request, response);
            return;
          }
          if (((String)AnagErrors.get("ERR_AAEP_TO_CONNECT")).equals(sex.getMessage()))
          {
            request.setAttribute("pageBack",pageBack);
            request.setAttribute("msgErrore", (String)AnagErrors.get("ERR_AAEP_TO_CONNECT"));
            request.setAttribute("CUAA", aziendaVO.getCUAA());
            request.getRequestDispatcher(URL_ERRORE).forward(request, response);
            return;
          }
          if (!((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA")).equals(sex.getMessage()))
          {
            ValidationError error = new ValidationError(sex.getMessage());
            errors.add("error", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(errorPage).forward(request, response);
            return;
          }
        }

        if (aziendaAAEPTrovata)
        {
          
          //Vado a vedere che il dato restituito abbia come fonte infocamere
        /*  if (!SolmrConstants.FONTE_DATO_INFOCAMERE_STR.equalsIgnoreCase(aziendaAAEP.getIdFonteDato()))
            aziendaAAEPAltreFonti=true;*/

          
          //Vado a controllare se l'azienda risulta cessata
          if (!aziendaAAEPAltreFonti)
            if (aziendaAAEP.getDataCessazione()!=null && !"".equals(aziendaAAEP.getDataCessazione()))
              aziendaAAEPCessata=true;
        }


        if (aziendaAAEPTrovata && !aziendaAAEPCessata && !aziendaAAEPAltreFonti)
        {
          aziendaVO.setCUAA(aziendaAAEP.getCodiceFiscale().getValue());
          aziendaVO.setPartitaIVA(aziendaAAEP.getPartitaIva().getValue());

         

          String temp=AnagAAEPAziendaVO.eliminaSpazi(aziendaAAEP.getRagioneSociale().getValue()).toUpperCase();
          if (temp.length()>120) temp=temp.substring(0,120);
          aziendaVO.setDenominazione(temp);

          try
          {
            aziendaVO.setStrCCIAAnumeroREA(aziendaAAEP.getNumeroCCIAA().getValue());
            aziendaVO.setCCIAAnumeroREA(new Long(aziendaAAEP.getNumeroCCIAA().getValue()));
          }
          catch(Exception e)
          {
            aziendaVO.setCCIAAnumeroREA(null);
          }

          if(Validator.isNotEmpty(aziendaAAEP.getProvinciaCCIAA().getValue())) {
          	aziendaVO.setCCIAAprovREA(aziendaAAEP.getProvinciaCCIAA().getValue().trim());
          }
          aziendaVO.setCCIAAannoIscrizione(aziendaAAEP.getAnnoCCIAA().getValue());
          aziendaVO.setCCIAAnumRegImprese(aziendaAAEP.getNRegistroImpreseCCIAA().getValue());

          try
          {
            if(aziendaAAEP.getCodATECO2007().getValue() != null)
            {
              String codAtecoTmp = aziendaAAEP.getCodATECO2007().getValue();
              codAtecoTmp = codAtecoTmp.replaceAll("\\.","");
              /*Long idAttivitaATECO=anagFacadeClient.ricercaIdAttivitaATECO(codAtecoTmp,null);
  
              if (idAttivitaATECO!=null)
              {
                CodeDescription codeATECO = new CodeDescription(new Integer(idAttivitaATECO+""),
                                                aziendaAAEP.getDescrATECO2007());
                codeATECO.setSecondaryCode(codAtecoTmp);
                aziendaVO.setTipoAttivitaATECO(codeATECO);
              }*/
              
              CodeDescription codeATECO = anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codAtecoTmp);
              aziendaVO.setTipoAttivitaATECO(codeATECO);
            }
          }
          catch(Exception e) {}

          try
          {
            Long idFormaGiuridica=anagFacadeClient.getIdTipoFormaGiuridica(aziendaAAEP.getIdNaturaGiuridica().getValue());

            aziendaVO.setIdFormaGiuridica(idFormaGiuridica);

            if (idFormaGiuridica!=null)
            {
              Long idTipologiaAzienda=anagFacadeClient.getIdTipologiaAziendaByFormaGiuridica(idFormaGiuridica,new Boolean(true));
              aziendaVO.setTipiFormaGiuridica(String.valueOf(idFormaGiuridica.longValue()));
              aziendaVO.setTipoFormaGiuridica(new CodeDescription(new Integer(""+idFormaGiuridica.longValue()),""));
              if (idTipologiaAzienda!=null)
              {
                aziendaVO.setTipiAzienda(String.valueOf(idTipologiaAzienda.longValue()));
                aziendaVO.setTipoTipologiaAzienda(new CodeDescription(new Integer(""+idTipologiaAzienda.longValue()),""));
              }
            }
          }
          catch(Exception e) {}
          
          
          String pecAAEP = aziendaAAEP.getPostaElettronicaCertificata().getValue();

          //session.setAttribute("pecAAEP", pecAAEP);
          //PostaCertificata pecAAEP = (PostaCertificata)session.getAttribute("pecAAEP");
			    if(Validator.isNotEmpty(pecAAEP))
			    {
			      aziendaVO.setPec(pecAAEP);
			      //session.setAttribute("insAnagVO", aziendaVO);
			    }
          
          
          //Vado a leggere i dati del titolare/rappresentante legale
          RappresentanteLegale rappresentanteLegaleAAEP=aziendaAAEP.getRappresentanteLegale().getValue();

          
          //Mi faccio restituire la sede legale
          ListaSedi sedeRidottaAAEP=AnagAAEPAziendaVO.estraiSedeLegaleAAEP(aziendaAAEP.getListaSedi());

          /**
           * Con la sede legale restituita prima, che contiene solo un numero ridotto
           * di parametri vado a farmi dare una sede completa
           * */
          if (sedeRidottaAAEP!=null)
          {
            Sede sedeAAEP=anagFacadeClient.cercaPuntualeSede(aziendaAAEP.getCodiceFiscale().getValue(), sedeRidottaAAEP.getProgrSede().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
            		

            if (sedeAAEP!=null)
            {
              session.setAttribute("sedeAAEP",sedeAAEP);
					    ComuneVO comune = null;
					    if(sedeAAEP != null) 
					    {
					      // Recupero solo i primi 100 caratteri dell'indirizzo sede di AAEP
					      // e faccio la trim per eliminare gli spazi
					      if(Validator.isNotEmpty(sedeAAEP.getIndirizzoSede().getValue())) 
					      {
					        if(sedeAAEP.getIndirizzoSede().getValue().length() > 100) 
					        {
					          aziendaVO.setSedelegIndirizzo(sedeAAEP.getIndirizzoSede().getValue().substring(0, 100).trim());
					        }
					        else 
					        {
					          aziendaVO.setSedelegIndirizzo(sedeAAEP.getIndirizzoSede().getValue().trim());
					        }
					      }
					      comune = anagFacadeClient.getComuneByISTAT(sedeAAEP.getCodComune().getValue());
					      aziendaVO.setSedelegComune(comune.getDescom());
					      aziendaVO.setSedelegProv(comune.getSiglaProv());
					      aziendaVO.setSedelegCAP(sedeAAEP.getCap().getValue());
					      aziendaVO.setTelefono(sedeAAEP.getTelefono().getValue());
					      aziendaVO.setFax(sedeAAEP.getFax().getValue());
					      aziendaVO.setMail(sedeAAEP.getEmail().getValue());
					      session.setAttribute("insAnagVO", aziendaVO);
					    } 
            }
          }
          if (rappresentanteLegaleAAEP!=null)
            session.setAttribute("rappresentanteLegaleAAEP",rappresentanteLegaleAAEP);
            
          session.setAttribute("insAnagVO",aziendaVO);

          /**
           * Ho trovato un'azienda su AAEP, non è cessata e non proviene da
           * altre fonti: quindi deve chiedere all'utente se vuole proseguire
           */
          request.setAttribute("pageBack",pageBack);
          request.setAttribute("pageNext",pageNext);
          request.setAttribute("domanda", (String)AnagErrors.get("ERR_AAEP_AZIENDA_SUBENTRO_1")+
                               " "+aziendaVO.getCUAA()+" "+
                               (String)AnagErrors.get("ERR_AAEP_AZIENDA_SUBENTRO_2")
                               );
          request.setAttribute("CUAA", aziendaVO.getCUAA());
          request.setAttribute("CUAASubentro", aziendaVO.getCUAASubentro());
          request.setAttribute("radiobuttonAzienda", radiobuttonAzienda);

          request.getRequestDispatcher(confermaNuovaAzienda).forward(request, response);
          return;
        }
        else
        {
          //Se non ho trovato l'azienda su AAEP la ricerco su anagrafe tributaria
          if (parametroTRIB.equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            SianAnagTributariaVO anagTrib = null;
            try 
            {
              anagTrib = anagFacadeClient.ricercaAnagrafica(
                aziendaVO.getCUAA(), ProfileUtils.getSianUtente(ruoloUtenza));
              
              if (anagTrib !=null)
              {
                
                aziendaVO.setCUAA(anagTrib.getCodiceFiscale());
                aziendaVO.setPartitaIVA(anagTrib.getPartitaIva());
      
      
                String temp=AnagAAEPAziendaVO.eliminaSpazi(anagTrib.getDenominazione()).toUpperCase();
                if (temp.length()>120) temp=temp.substring(0,120);
                aziendaVO.setDenominazione(temp);
      
      
      
                session.setAttribute("anagTribForInsert", anagTrib);
                session.setAttribute("insAnagVO",aziendaVO);  
      
                /**
                 * Ho trovato un'azienda su AT quindi deve chiedere all'utente se vuole proseguire
                 */
                request.setAttribute("pageBack",pageBack);
                request.setAttribute("pageNext",pageNext);
                request.setAttribute("domanda", (String)AnagErrors.get("ERR_AAEP_AZIENDA_SUBENTRO_1")+
                                     " "+aziendaVO.getCUAA()+" "+AnagErrors.ERR_SIAN_AZIENDA_SUBENTRO_3);
                request.setAttribute("CUAA", aziendaVO.getCUAA());
                request.setAttribute("CUAASubentro", aziendaVO.getCUAASubentro());
                request.setAttribute("radiobuttonAzienda", radiobuttonAzienda); 
      
                request.getRequestDispatcher(confermaNuovaAzienda).forward(request, response);
                return;
              }
              
            }
            catch(SolmrException se) 
            {
              //Dato che non sono in grado di distinguere il caso in cui il record non è presente dall'errore
              //vero e proprio non segnalo niente
            }
          }
          
        }
        

        %>
          <jsp:forward page ="<%=ricAAEPURL%>" />
        <%
      }
      else
      {
        String parametroTRIB = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_TRIB"));
        try
        {
          //boolean piemonte=SolmrConstants.ID_REG_PIEMONTE.equals(ruoloUtenza.getIstatRegioneAttiva());
          String parametroAAEP = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_AAEP"));
          // Se sono in piemonte e il parametro di AAEP è valorizzato a "S" significa
          // che devo richiamare il servizio di AAEP
          if (!(parametroAAEP.equalsIgnoreCase(SolmrConstants.FLAG_S)))
          {
            
            //Ricerco su anagrafe tributaria
            if (parametroTRIB.equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              SianAnagTributariaVO anagTrib = null;
              try 
              {
                anagTrib = anagFacadeClient.ricercaAnagrafica(
                  aziendaVO.getCUAA(), ProfileUtils.getSianUtente(ruoloUtenza));
                
                if (anagTrib !=null)
                {
                  
                  aziendaVO.setCUAA(anagTrib.getCodiceFiscale());
                  aziendaVO.setPartitaIVA(anagTrib.getPartitaIva());
        
        
                  String temp=AnagAAEPAziendaVO.eliminaSpazi(anagTrib.getDenominazione()).toUpperCase();
                  if (temp.length()>120) temp=temp.substring(0,120);
                  aziendaVO.setDenominazione(temp);
        
        
        
                  session.setAttribute("anagTribForInsert",anagTrib);
                  session.setAttribute("insAnagVO",aziendaVO);  
        
        
                  %>
                    <jsp:forward page ="<%=ricAAEPURL%>" />
                  <%
                  return;
                }
                
              }
              catch(SolmrException se) 
              {
                //Dato che non sono in grado di distinguere il caso in cui il record non è presente dall'errore
                //vero e proprio non segnalo niente
              }
              request.setAttribute("pageBack",pageBack);
              request.setAttribute("pageNext",pageNext);
              request.setAttribute("domanda", (String)AnagErrors.get("ERR_AT_NO_AZIENDA"));
              request.setAttribute("CUAA", aziendaVO.getCUAA());
              // Inserisco l'oggetto in sessione in modo che mantenga l'eventuale
              // id_azienda_provenienza
              session.setAttribute("insAnagVO",aziendaVO);
              request.getRequestDispatcher(confermaNuovaAzienda).forward(request, response);
              return;
            }
            
            %>
              <jsp:forward page ="<%=ricAAEPURL%>" />
            <%
            return;
          }

          aziendaAAEP = anagFacadeClient.cercaPerCodiceFiscale(aziendaVO.getCUAA());
          /**
           * Vado a vedere che il dato restituito abbia come fonte infocamere
           * */
         /* if(!SolmrConstants.FONTE_DATO_INFOCAMERE_STR.equalsIgnoreCase(aziendaAAEP.getIdFonteDato()))
            throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));*/

          aziendaVO.setCUAA(aziendaAAEP.getCodiceFiscale().getValue());
          aziendaVO.setPartitaIVA(aziendaAAEP.getPartitaIva().getValue());
          //aziendaVO.setDenominazione(AnagAAEPAziendaVO.eliminaSpazi(aziendaAAEP.getDenominazione()));

          String temp=AnagAAEPAziendaVO.eliminaSpazi(aziendaAAEP.getRagioneSociale().getValue()).toUpperCase();
          //if (temp.length()>120) temp=temp.substring(0,120);
          // Prelevo i primi 1000 caratteri e non più 120 come prima
          if(temp.length() > 1000) {
            temp=temp.substring(0,1000);
          }
          aziendaVO.setDenominazione(temp);

          try
          {
            aziendaVO.setStrCCIAAnumeroREA(aziendaAAEP.getNumeroCCIAA().getValue());
            aziendaVO.setCCIAAnumeroREA(new Long(aziendaAAEP.getNumeroCCIAA().getValue()));
          }
          catch(Exception e)
          {
            aziendaVO.setCCIAAnumeroREA(null);
          }

          if(Validator.isNotEmpty(aziendaAAEP.getProvinciaCCIAA())) {
          	aziendaVO.setCCIAAprovREA(aziendaAAEP.getProvinciaCCIAA().getValue().trim());
          }
          aziendaVO.setCCIAAannoIscrizione(aziendaAAEP.getAnnoCCIAA().getValue());
          aziendaVO.setCCIAAnumRegImprese(aziendaAAEP.getNRegistroImpreseCCIAA().getValue());
          
          
          String pecAAEP = aziendaAAEP.getPostaElettronicaCertificata().getValue();
          //session.setAttribute("pecAAEP", pecAAEP);
          if(Validator.isNotEmpty(pecAAEP))
          {
            aziendaVO.setPec(pecAAEP);
            //session.setAttribute("insAnagVO", aziendaVO);
          }

          try
          {
            if(aziendaAAEP.getCodATECO2007() != null)
            {
              String codAtecoTmp = aziendaAAEP.getCodATECO2007().getValue();
              codAtecoTmp = codAtecoTmp.replaceAll("\\.","");
              /*Long idAttivitaATECO=anagFacadeClient.ricercaIdAttivitaATECO(codAtecoTmp,null);
              if (idAttivitaATECO!=null)
              {
                CodeDescription codeATECO = new CodeDescription(new Integer(idAttivitaATECO+""),
                                                aziendaAAEP.getDescrATECO2007());
                codeATECO.setSecondaryCode(codAtecoTmp);
                aziendaVO.setTipoAttivitaATECO(codeATECO);
              }*/
              CodeDescription codeATECO = anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codAtecoTmp);
              aziendaVO.setTipoAttivitaATECO(codeATECO);
  
              
            }
          }
          catch(Exception e) {}

          try
          {
            Long idFormaGiuridica=anagFacadeClient.getIdTipoFormaGiuridica(aziendaAAEP.getIdNaturaGiuridica().getValue());

            aziendaVO.setIdFormaGiuridica(idFormaGiuridica);
            if (idFormaGiuridica!=null)
            {
              //Long idTipologiaAzienda=anagFacadeClient.getIdTipologiaAziendaByFormaGiuridica(idFormaGiuridica);
              Long idTipologiaAzienda = new Long(1);
              aziendaVO.setTipiFormaGiuridica(String.valueOf(idFormaGiuridica.longValue()));
              aziendaVO.setTipoFormaGiuridica(new CodeDescription(new Integer(""+idFormaGiuridica.longValue()),""));
              if (idTipologiaAzienda!=null)
              {
                aziendaVO.setTipiAzienda(String.valueOf(idTipologiaAzienda.longValue()));
                aziendaVO.setTipoTipologiaAzienda(new CodeDescription(new Integer(""+idTipologiaAzienda.longValue()),""));
              }
            }
          }
          catch(Exception e) {}


          
          //Vado a leggere i dati del titolare/rappresentante legale
          RappresentanteLegale rappresentanteLegaleAAEP=aziendaAAEP.getRappresentanteLegale().getValue();

          
          //Mi faccio restituire la sede legale
          ListaSedi sedeRidottaAAEP=AnagAAEPAziendaVO.estraiSedeLegaleAAEP(aziendaAAEP.getListaSedi());

          /**
           * Con la sede legale restituita prima, che contiene solo un numero ridotto
           * di parametri vado a farmi dare una sede completa
           * */
          if (sedeRidottaAAEP!=null)
          {
            Sede sedeAAEP=anagFacadeClient.cercaPuntualeSede(aziendaAAEP.getCodiceFiscale().getValue(), sedeRidottaAAEP.getProgrSede().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);            		

            if (sedeAAEP!=null)
            {
              session.setAttribute("sedeAAEP",sedeAAEP);
              ComuneVO comune = null;
              if(sedeAAEP != null) 
              {
                // Recupero solo i primi 100 caratteri dell'indirizzo sede di AAEP
                // e faccio la trim per eliminare gli spazi
                if(Validator.isNotEmpty(sedeAAEP.getIndirizzoSede())) 
                {
                  if(sedeAAEP.getIndirizzoSede().getValue().length() > 100) 
                  {
                    aziendaVO.setSedelegIndirizzo(sedeAAEP.getIndirizzoSede().getValue().substring(0, 100).trim());
                  }
                  else 
                  {
                    aziendaVO.setSedelegIndirizzo(sedeAAEP.getIndirizzoSede().getValue().trim());
                  }
                }
                comune = anagFacadeClient.getComuneByISTAT(sedeAAEP.getCodComune().getValue());
                aziendaVO.setSedelegComune(comune.getDescom());
                aziendaVO.setSedelegProv(comune.getSiglaProv());
                aziendaVO.setSedelegCAP(sedeAAEP.getCap().getValue());
                aziendaVO.setTelefono(sedeAAEP.getTelefono().getValue());
                aziendaVO.setFax(sedeAAEP.getFax().getValue());
                aziendaVO.setMail(sedeAAEP.getEmail().getValue());
                session.setAttribute("insAnagVO", aziendaVO);
              }        
              
            }
          }
          if (rappresentanteLegaleAAEP!=null)
            session.setAttribute("rappresentanteLegaleAAEP",rappresentanteLegaleAAEP);
            
          session.setAttribute("insAnagVO",aziendaVO);

          /**
           *  Vado a controllare se l'azienda risulta cessata
           */
          if (aziendaAAEP.getDataCessazione().getValue()!=null && !"".equals(aziendaAAEP.getDataCessazione().getValue().trim()))
          {
            //L'azienda risulta cessata, quindi devo avvertire l'utente
            request.setAttribute("pageBack",pageBack);
            request.setAttribute("pageNext",pageNext);
            request.setAttribute("domanda", (String)AnagErrors.get("ERR_AAEP_AZIENDA_CESSATA_1")+
                                 " "+aziendaVO.getCUAA()+" "+
                                 (String)AnagErrors.get("ERR_AAEP_AZIENDA_CESSATA_2")
                                 );
            request.setAttribute("CUAA", aziendaVO.getCUAA());
            request.getRequestDispatcher(confermaNuovaAzienda).forward(request, response);
            return;
          }
        }
        catch (SolmrException sex)
        {

          if (((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA")).equals(sex.getMessage()))
          {
            //Se non ho trovato l'azienda su AAEP la ricerco su anagrafe tributaria
            if (parametroTRIB.equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              SianAnagTributariaVO anagTrib = null;
              try 
              {
                anagTrib = anagFacadeClient.ricercaAnagrafica(
                  aziendaVO.getCUAA(), ProfileUtils.getSianUtente(ruoloUtenza));
                
                if (anagTrib !=null)
                {
                  
                  aziendaVO.setCUAA(anagTrib.getCodiceFiscale());
                  aziendaVO.setPartitaIVA(anagTrib.getPartitaIva());
        
        
                  String temp=AnagAAEPAziendaVO.eliminaSpazi(anagTrib.getDenominazione()).toUpperCase();
                  if (temp.length()>120) temp=temp.substring(0,120);
                  aziendaVO.setDenominazione(temp);
        
        
        
                  session.setAttribute("anagTribForInsert", anagTrib);
                  session.setAttribute("insAnagVO",aziendaVO);  
        
        
                  %>
                    <jsp:forward page ="<%=ricAAEPURL%>" />
                  <%
                  return;
                }
                
              }
              catch(SolmrException se) 
              {
                //Dato che non sono in grado di distinguere il caso in cui il record non è presente dall'errore
                //vero e proprio non segnalo niente
              }
            }
            request.setAttribute("pageBack",pageBack);
            request.setAttribute("pageNext",pageNext);
            if (parametroTRIB.equalsIgnoreCase(SolmrConstants.FLAG_S))
              request.setAttribute("domanda", (String)AnagErrors.get("ERR_AAEP_AT_NO_AZIENDA"));              
            else 
              request.setAttribute("domanda", (String)AnagErrors.get("ERR_AAEP_NO_AZIENDA"));
            request.setAttribute("CUAA", aziendaVO.getCUAA());
            // Inserisco l'oggetto in sessione in modo che mantenga l'eventuale
            // id_azienda_provenienza
            session.setAttribute("insAnagVO",aziendaVO);
            request.getRequestDispatcher(confermaNuovaAzienda).forward(request, response);
            return;
          }
          if (((String)AnagErrors.get("ERR_AAEP_GENERICO")).equals(sex.getMessage()))
          {
            request.setAttribute("pageBack",pageBack);
            request.setAttribute("msgErrore", (String)AnagErrors.get("ERR_AAEP_GENERICO"));
            request.setAttribute("CUAA", aziendaVO.getCUAA());
            request.getRequestDispatcher(URL_ERRORE).forward(request, response);
            return;
          }
          if (((String)AnagErrors.get("ERR_AAEP_TO_CONNECT")).equals(sex.getMessage()))
          {
            request.setAttribute("pageBack",pageBack);
            request.setAttribute("msgErrore", (String)AnagErrors.get("ERR_AAEP_TO_CONNECT"));
            request.setAttribute("CUAA", aziendaVO.getCUAA());
            request.getRequestDispatcher(URL_ERRORE).forward(request, response);
            return;
          }

          ValidationError error = new ValidationError(sex.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(errorPage).forward(request, response);
          return;
        }
        %>
          <jsp:forward page ="<%=ricAAEPURL%>" />
        <%
      }
    }
    else
    {
      %>
         <jsp:forward page = "/view/nuovaAziendaCCIAAView.jsp" />
      <%
    }
  }
  catch (SolmrException sex) 
  {
    ValidationError error = new ValidationError(sex.getMessage());
    errors = new ValidationErrors();
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(errorPage).forward(request, response);
    return;
  }
%>

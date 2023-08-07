<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@page import="javax.xml.bind.JAXBElement"%>
<%@ page import="javax.xml.namespace.QName"%>

<%!
  private static final String URL_ANAGRAFICA_VISURA_CAMERE_SEDI_VIEW = "/view/sediAAEPView.jsp";
  private static final String URL_ERRORE = "/view/erroreView.jsp";
  private static final String URL_PAGE_BACK = "../layout/sedi.htm";
  private static final String URL_PAGE_BACK_ERRORE = "../layout/sedi.htm";
  private static final String ACTION = "../layout/sediAAEP.htm";
  private static final String URL_ATTENDERE_PREGO = "/view/attenderePregoView.jsp";
%>


<%

  String iridePageName = "sediAAEPImportCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

    Azienda impresaInfoc = null;
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

    String operazione = request.getParameter("operazione");

    // L'utente ha selezionato la voce di menù AAEP e io lo
    // mando alla pagina di attesa per il caricamento dati
    if("attenderePrego".equalsIgnoreCase(operazione)) 
    {
      request.setAttribute("action", ACTION);
      operazione = null;
      request.setAttribute("operazione", operazione);
      %>
        <jsp:forward page= "<%= URL_ATTENDERE_PREGO %>" />
      <%
    }

    if("importaDati".equals(request.getParameter("importaDati")))
    {
      Sede[] sedeInfocamere = (Sede[])session.getAttribute("sedeInfocamere");
      
      Long[] idAteco = null;
      String sediSelezionate[] = request.getParameterValues("sedi");
      Vector<Integer> vSediSel = new Vector<Integer>();
      if(Validator.isNotEmpty(sediSelezionate))
      {
        for(int i=0;i<sediSelezionate.length;i++)
        {
          vSediSel.add(new Integer(sediSelezionate[i]));        
        }
      }
      
      
      
      boolean flagAtecoNonTrovato = false;
      for(int i=0;i<sedeInfocamere.length;i++)
      {
        
        //Controllo che la ute sia stata selzionata
        if(vSediSel.contains(new Integer(i)) && !flagAtecoNonTrovato)
        {        
	        List<AtecoRI2007Infoc> atecoRI2007Infoc=sedeInfocamere[i].getListaAtecoRI2007Infoc();
	        if (atecoRI2007Infoc !=null)
	        {
	          int k=0;
	          boolean cicla=true;
	          String codiceAteco = null;
	          while(k<atecoRI2007Infoc.size() && cicla)
	          {
	            if ("P".equalsIgnoreCase(atecoRI2007Infoc.get(k).getCodImportanzaRI().getValue()))
	              cicla=false;
	            else k++;
	          }
	          if (!cicla)
	          {            
	            codiceAteco = atecoRI2007Infoc.get(k).getCodAteco2007().getValue();
	            if (codiceAteco!=null) 
	            {
	              codiceAteco = codiceAteco.trim();
	              codiceAteco = codiceAteco.replaceAll("\\.","");
	              //Verifico tra quelli selezionati che i codici ateco 2007
                //trovino la corrispondenza su db_tipo_attivita_ateco
	              CodeDescription ateco = anagFacadeClient
	                .getAttivitaATECObyCodeParametroCATE(codiceAteco);
	              if(!(Validator.isNotEmpty(ateco) &&  Validator.isNotEmpty(ateco.getCode())))
	              {
	                 flagAtecoNonTrovato = true;           
	              }
	            }           
	            
	          }
	          
	          
	          //Non trovato P cerco PI
            if(Validator.isEmpty(codiceAteco))
            {
              k=0;
              cicla=true;
              while(k<atecoRI2007Infoc.size() && cicla)
              {
                if ("PI".equalsIgnoreCase(atecoRI2007Infoc.get(k).getCodImportanzaRI().getValue()))
                  cicla=false;
                else k++;
              }           
              if (!cicla)
              {
                codiceAteco = atecoRI2007Infoc.get(k).getCodAteco2007().getValue();
	              if (codiceAteco!=null) 
	              {
	                codiceAteco = codiceAteco.trim();
	                codiceAteco = codiceAteco.replaceAll("\\.","");
	                //Verifico tra quelli selezionati che i codici ateco 2007
	                //trovino la corrispondenza su db_tipo_attivita_ateco
	                CodeDescription ateco = anagFacadeClient
	                  .getAttivitaATECObyCodeParametroCATE(codiceAteco);
	                if(!(Validator.isNotEmpty(ateco) &&  Validator.isNotEmpty(ateco.getCode())))
	                {
	                   flagAtecoNonTrovato = true;           
	                }
	              }     
              }
            }
	          
	          
	          
	        }
	      }
      }    
      
      session.setAttribute("sedi", sediSelezionate);
      
      if(!flagAtecoNonTrovato)
      {
	      anagFacadeClient.importaUteAAEP(anagAziendaVO,
	                                        sedeInfocamere,
	                                        (String[])session.getAttribute("sedi"),
	                                        ruoloUtenza.getIdUtente());
	      session.removeAttribute("sedeInfocamere");
	      session.removeAttribute("sedi");
	      response.sendRedirect(URL_PAGE_BACK);
	      return;
	    }
	    else
	    {
	      String messaggio = AnagErrors.ERR_AEEP_NO_COD_ATECO_2007_TROVATO;
	      request.setAttribute("messaggioErrore",messaggio);
	    }
      
    }

    try
    {
      impresaInfoc = anagFacadeClient.cercaPerCodiceFiscale(anagAziendaVO.getCUAA());        

      /* Se viene restituito null segnalo che non è stata trovata nessuna anagrafica
         Non dovrebbe accadere perchè AAEP se non trova niente dovrebbe rilanciare un
         eccezione.
      */
      if (impresaInfoc==null) throw new SolmrException((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA"));

      List<ListaSedi> listaSede = impresaInfoc.getListaSedi();

      TreeMap<String,Sede> listaSedi = new TreeMap<String,Sede>();
      if (listaSede!=null)
      {
        for(int i=0;i<listaSede.size();i++)
        {
          Sede sedeInfocamere = anagFacadeClient.cercaPuntualeSede(impresaInfoc.getCodiceFiscale().getValue(), listaSede.get(i).getProgrSede().getValue(),SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
          String descComune=sedeInfocamere.getDescrStatoEstero().getValue();
          String capComune = sedeInfocamere.getCap().getValue();
          ComuneVO comune=null;
          if (descComune==null || "".equals(descComune))
          {
            try
            {
              comune=anagFacadeClient.getComuneByISTAT(sedeInfocamere.getCodComune().getValue());
            }
            catch(Exception e) {}
            if (comune!=null)
            {
              descComune=comune.getDescom();
            }
          }
          
          QName nomeComuneQName = new QName("http://servizio.frontend.ls.com", "nomeComune");
          JAXBElement<String> nomeComuneValue = new JAXBElement<String>(nomeComuneQName, String.class, descComune);
          sedeInfocamere.setNomeComune(nomeComuneValue);
          
          if("MULTI".equalsIgnoreCase(capComune))
          {
            if (comune!=null)
              capComune = comune.getCap();
          }
          
          QName capQName = new QName("http://servizio.frontend.ls.com", "cap");
          JAXBElement<String> capValue = new JAXBElement<String>(capQName, String.class, capComune);
          sedeInfocamere.setCap(capValue);
          

          if (sedeInfocamere!=null)
          {
            listaSedi.put(descComune+sedeInfocamere.getCodTipoLocalizzazione()+i,sedeInfocamere);
          }
        }
        Sede sedeInfocamere[]=(Sede[])listaSedi.values().toArray(new Sede[0]);
        session.setAttribute("sedeInfocamere",sedeInfocamere);
      }
    }
    catch(SolmrException sx) 
    {
      request.setAttribute("errore",sx.getMessage());
      request.setAttribute("pageBack",URL_PAGE_BACK_ERRORE);
      %>
         <jsp:forward page="<%= URL_ERRORE %>"/>
      <%
      return;
    }
  }
  catch(Exception e)
  {
    it.csi.solmr.util.ValidationErrors ve=new ValidationErrors();
    e.printStackTrace();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors",ve);
    request.getRequestDispatcher(URL_ANAGRAFICA_VISURA_CAMERE_SEDI_VIEW).forward(request, response);
    return;
  }
%>
<jsp:forward page="<%= URL_ANAGRAFICA_VISURA_CAMERE_SEDI_VIEW %>"/>



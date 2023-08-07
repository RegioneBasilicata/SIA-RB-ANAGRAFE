<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="contoCorrenteVO" scope="request" class="it.csi.solmr.dto.anag.ContoCorrenteVO"/>
<jsp:setProperty name="contoCorrenteVO" property="*" />

<%

	String iridePageName = "contiCorrentiNewCtrl.jsp";

  %>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

  String contiCorrentiNewUrl = "/view/contiCorrentiNewView.jsp";
  String elencoContiCorrentiUrl = "/view/contiCorrentiView.jsp";
  String actionUrl = "../layout/conti_correnti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione inserisci dei conti correnti. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  ValidationErrors errors = new ValidationErrors();

 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
 	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
 	// in modo che venga sempre effettuato
 	try 
  {
  	anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
   	request.setAttribute("statoAzienda", se);
  }
  
  
  String obbligoGF = null;
  try 
  {
    if((anagAziendaVO.getTipoFormaGiuridica() != null)
      &&  (anagAziendaVO.getTipoFormaGiuridica().getCode() != null))
    {
      obbligoGF = anagFacadeClient.getObbligoGfFromFormaGiuridica(new Long(anagAziendaVO.getTipoFormaGiuridica().getCode().intValue()));
      request.setAttribute("obbligoGF", obbligoGF);
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - contiCorrentiNewCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+" "+AnagErrors.ERRORE_KO_OBBLIGO_CF+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

  if(request.getParameter("salva") != null) 
  {
   	try 
    {
      
      String iban = contoCorrenteVO.getIban();      
      
      //Inserito l'IBAN a mano
      boolean flagIbanOk = true;
      if(Validator.isNotEmpty(iban))
      {
        if(iban.length() != 27)
        {
          errors.add("iban", new ValidationError(AnagErrors.ERRORE_KO_PARAMETRO_IBAN));
          flagIbanOk = false;
        }
        else
        {
          String abi = iban.substring(5,10);
          contoCorrenteVO.setAbi(abi); 
          
          BancaSportelloVO banche[] = anagFacadeClient.searchBanca(contoCorrenteVO.getAbi(), null);
          if(banche != null && banche.length == 1) 
          {
            contoCorrenteVO.setDenominazioneBanca(banche[0].getDenominazioneBanca());
            contoCorrenteVO.setAbi(banche[0].getAbi());
            contoCorrenteVO.setBic(banche[0].getBic());
          }
          else 
          {
            errors.add("abi",new ValidationError("Abi non valido"));
          }
          
          String cab = iban.substring(10,15);
          contoCorrenteVO.setCab(cab);
          BancaSportelloVO sportelli[] = anagFacadeClient.searchSportello(contoCorrenteVO.getAbi(), 
            contoCorrenteVO.getCab(), null);
          if(sportelli != null && sportelli.length == 1) 
          {
            BancaSportelloVO datiSportello = sportelli[0];
            contoCorrenteVO.setCapSportello(datiSportello.getCapSportello());
            contoCorrenteVO.setDenominazioneSportello(datiSportello.getDenominazioneSportello());
            contoCorrenteVO.setIndirizzoSportello(datiSportello.getIndirizzoSportello());
            contoCorrenteVO.setProvinciaSportello(datiSportello.getProvinciaSportello());
            contoCorrenteVO.setDescrizioneComuneSportello(datiSportello.getDescrizioneComuneSportello());
            
            String codPaese = iban.substring(0,2);
            if(codPaese.equalsIgnoreCase(datiSportello.getCodPaeseSportello()))
            {
              contoCorrenteVO.setCodPaese(datiSportello.getCodPaeseSportello());
            }
            else
            {
              errors.add("error", new ValidationError(AnagErrors.ERRORE_CODICE_PAESE_IBAN));
            }            
            
          }
          
          String cin = iban.substring(4,5);
          contoCorrenteVO.setCin(cin);
          
          String cifraCtrl = iban.substring(2,4);
          contoCorrenteVO.setCifraCtrl(cifraCtrl);
          String cCorr = iban.substring(15,27);
          contoCorrenteVO.setNumeroContoCorrente(cCorr);         
         
        
        }
      }
      else
      {
      
        contoCorrenteVO.setCifraCtrl(request.getParameter("cifraCtrl"));
        contoCorrenteVO.setCin(request.getParameter("cin")); 
  
  
        if(Validator.isNotEmpty(contoCorrenteVO.getAbi()))
        {
          BancaSportelloVO banche[] = anagFacadeClient.searchBanca(contoCorrenteVO.getAbi(),null);
          if(banche != null && banche.length == 1) 
          {
            contoCorrenteVO.setDenominazioneBanca(banche[0].getDenominazioneBanca());
            contoCorrenteVO.setAbi(banche[0].getAbi());
          }
          else 
          {
            errors.add("abi",new ValidationError("Abi non valido"));
          }
          if(Validator.isNotEmpty(contoCorrenteVO.getCab())) 
          {
            BancaSportelloVO sportelli[] = anagFacadeClient.searchSportello(contoCorrenteVO.getAbi(),contoCorrenteVO.getCab(),null);
            if(sportelli != null && sportelli.length == 1) 
            {
              BancaSportelloVO datiSportello = sportelli[0];
              contoCorrenteVO.setCapSportello(datiSportello.getCapSportello());
              contoCorrenteVO.setDenominazioneSportello(datiSportello.getDenominazioneSportello());
              contoCorrenteVO.setIndirizzoSportello(datiSportello.getIndirizzoSportello());
              contoCorrenteVO.setProvinciaSportello(datiSportello.getProvinciaSportello());
              contoCorrenteVO.setBic(request.getParameter("bic"));
              contoCorrenteVO.setDescrizioneComuneSportello(datiSportello.getDescrizioneComuneSportello());
              contoCorrenteVO.setCodPaese(datiSportello.getCodPaeseSportello());             
              
            }
          }
        }
                
        contoCorrenteVO.setIban(popolaIban(contoCorrenteVO.getCifraCtrl(),
                                           contoCorrenteVO.getCin(),
                                           contoCorrenteVO.getAbi(),
                                           contoCorrenteVO.getCab(),
                                           contoCorrenteVO.getNumeroContoCorrente(),
                                           contoCorrenteVO.getCodPaese()));
        
      }




  		// Effettuo il controllo formale sulla validità dei dati inseriti
  		if(flagIbanOk)
    	  errors = contoCorrenteVO.validateInsert(errors);


      if(errors != null && errors.size() != 0) 
      {
      	request.setAttribute("errors",errors);
      }
      else 
      {
      	String idSportello = null;
      	BancaSportelloVO sportelli[] = anagFacadeClient.searchSportello(contoCorrenteVO.getAbi(),contoCorrenteVO.getCab(),null);
      	if(sportelli != null && sportelli.length == 1) 
        {
          idSportello=sportelli[0].getIdSportello().toString();
      	}
      	if(idSportello == null) 
        {
        	errors = new ValidationErrors();
        	errors.add("abi",new ValidationError("Impossibile trovare lo sportello indicato da questo ABI e CAB"));
        	errors.add("cab",new ValidationError("Impossibile trovare lo sportello indicato da questo ABI e CAB"));
        	request.setAttribute("errors",errors);
      	}
      	else 
        {
          
          if("S".equalsIgnoreCase(obbligoGF))
          {
            if("S".equalsIgnoreCase(contoCorrenteVO.getFlagContoGf())
              && !"S".equalsIgnoreCase(sportelli[0].getFlagSportelloGf()))
            {
              errors = new ValidationErrors();
              errors.add("flagContoGf", new ValidationError(AnagErrors.ERRORE_SPORT_NO_GF));
            }
          }
          
          if(errors != null && errors.size() != 0) 
		      {
		        request.setAttribute("errors",errors);
		        %>
				      <jsp:forward page="../view/contiCorrentiNewView.jsp" />
				    <%
				    return;
		      }
          
        
        	contoCorrenteVO.setIdSportello(convertToLong(idSportello));
        	contoCorrenteVO.setIdAzienda(anagAziendaVO.getIdAzienda());
          java.util.Date dataCorrente = new Date();
          contoCorrenteVO.setDataInizioValiditaContoCorrente(dataCorrente);
          contoCorrenteVO.setDataAggiornamento(dataCorrente);
          contoCorrenteVO.setflagValidato("S");
        	anagFacadeClient.insertContoCorrente(contoCorrenteVO,ruoloUtenza.getIdUtente());
        	response.sendRedirect("../layout/conti_correnti.htm");
        	return;
      	}
    	}
    	contoCorrenteVO.setIntestazione(request.getParameter("intestazione"));
    }
    catch(SolmrException s) 
    {
  		ValidationErrors ve = new ValidationErrors();
  		ve.add("error",new ValidationError(s.getMessage()));
  		request.setAttribute("errors",ve);
  		s.printStackTrace();
    }
    catch(Exception e) 
    {
  		it.csi.solmr.util.ValidationErrors ve = new ValidationErrors();
  		ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  		request.setAttribute("errors",ve);
  		e.printStackTrace();
    }
  	%>
    		<jsp:forward page="../view/contiCorrentiNewView.jsp" />
  	<%
  }
  else if(request.getParameter("annulla") != null) 
  {
   	Vector conti = null;
   	try 
    {
    	conti = anagFacadeClient.getContiCorrenti(anagAziendaVO.getIdAzienda(),false);
    }
    catch(SolmrException se) 
    {
    	ValidationError error = new ValidationError(se.getMessage());
    	errors.add("error",error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(contiCorrentiNewUrl).forward(request, response);
    	return;
    }
    request.setAttribute("conti",conti);
    %>
    	<jsp:forward page= "<%= elencoContiCorrentiUrl %>" />
    <%
  }
  else 
  {
   	request.setAttribute("intestatario", anagAziendaVO.getDenominazione());
   	%>
   		<jsp:forward page="../view/contiCorrentiNewView.jsp" />
   	<%
  }
%>

<%!
	public Long convertToLong(String val) {
    	return val==null?null:new Long(val);
  	}
%>

<%!

 public String popolaIban(String cCtrl,String cin,String abi,String cab,String contoCorrente,String codPaese)
 {
   String iban=null;
   if (cCtrl!=null && cin!=null && abi!=null && cab!=null && contoCorrente!=null && codPaese!=null)
   {
      int len4ZeroCC = 12 - contoCorrente.trim().length();
      for(int i=0; i<len4ZeroCC; i++){
              contoCorrente = '0' + contoCorrente;
      }
      iban = codPaese.trim() + cCtrl.trim() + cin.trim() + abi.trim() + cab.trim() + contoCorrente.trim();
      iban = iban.toUpperCase();
   }
   return iban;
 }

%>



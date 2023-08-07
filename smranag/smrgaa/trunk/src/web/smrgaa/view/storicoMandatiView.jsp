<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.text.*" %>

<%

	SolmrLogger.debug(this, " - storicoMandatiView.jsp - INIZIO PAGINA");
 	java.io.InputStream layout = application.getResourceAsStream("/layout/storicoMandati.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");
 	
 	String parametroGDEL = (String)request.getAttribute("parametroGDEL");

 	try 
 	{
   	AnagFacadeClient client = new AnagFacadeClient();

   	// Nuova gestione fogli di stile
   	htmpl.set("head", head, null);
   	htmpl.set("header", header, null);
   	htmpl.set("footer", footer, null);

   	AnagAziendaVO anagAziendaVO=(AnagAziendaVO)session.getAttribute("anagAziendaVO");
   	PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");
	  String messaggioErrore = (String)request.getAttribute("messaggioErrore");

   	if(Validator.isNotEmpty(messaggioErrore)) 
   	{
   		htmpl.newBlock("blkErroreDelega");
   		htmpl.set("blkErroreDelega.messaggioErrore", messaggioErrore);
   	}
   	else 
   	{
    	DelegaVO[] elencoDeleghe = (DelegaVO[])request.getAttribute("elencoDeleghe");
    	int size = elencoDeleghe.length;
    	if (elencoDeleghe == null || size == 0) 
    	{
    		//Non sono presenti deleghe
      	htmpl.newBlock("blkNessunaDelega");
    	}
    	else 
    	{
    		htmpl.newBlock("blkElencoDeleghe");
     		for(int i = 0; i < size; i++) 
     		{
       		DelegaVO delegaVO = (DelegaVO)elencoDeleghe[i];
       		htmpl.newBlock("blkElencoDeleghe.blkDelega");
       		htmpl.set("blkElencoDeleghe.blkDelega.dataInizioMandato", DateUtils.formatDate(delegaVO.getDataInizioMandato()));
       		//Caso richiesta revoca
       		String dataFineMandato = "";
       		if(delegaVO.getDataRicevutaRitornoDelega() != null)
       		{
       		  if(delegaVO.getDataFine() != null)
       		  {
       		    dataFineMandato = DateUtils.formatDate(delegaVO.getDataFine());
       		  }
       		  else
       		  {
			        int incremento = new Integer(parametroGDEL).intValue();
			        Calendar cDateTmp = Calendar.getInstance();
			        cDateTmp.setTime(delegaVO.getDataRicevutaRitornoDelega());
			        cDateTmp.roll(Calendar.DAY_OF_YEAR, incremento);
			        
			        //se è minore della data odierna dopo l'incremento di 15 GG
			        if(cDateTmp.getTime().before(new Date()))
			        {
			          cDateTmp = Calendar.getInstance();
			          cDateTmp.roll(Calendar.DAY_OF_YEAR, 1);
			        }
			        
			        dataFineMandato = DateUtils.formatDate(cDateTmp.getTime());
			      }
       		}
       		else
       		{
       		  dataFineMandato = DateUtils.formatDate(delegaVO.getDataFineMandato());
       		}
       		htmpl.set("blkElencoDeleghe.blkDelega.dataFineMandato", dataFineMandato);
      	 	htmpl.set("blkElencoDeleghe.blkDelega.denomIntermediario", delegaVO.getDenomIntermediario());
      	 	//Caso richiesta revoca
          String dataFine = "";
          String nomeUtenIrideFineDelega = "";
          if(delegaVO.getDataRicevutaRitornoDelega() != null)
          {
            dataFine = DateUtils.formatDate(delegaVO.getDataFineMandato());
            
            if(Validator.isNotEmpty(delegaVO.getNomeUtenIrideRicRevoca()))
            {
              nomeUtenIrideFineDelega = delegaVO.getNomeUtenIrideRicRevoca();
            }
            if(Validator.isNotEmpty(delegaVO.getEnteUtenIrideRicRevoca()))
            {
              if(Validator.isNotEmpty(nomeUtenIrideFineDelega))
              {
                nomeUtenIrideFineDelega += " - ";
              }
              nomeUtenIrideFineDelega += delegaVO.getEnteUtenIrideRicRevoca();
            }
          }
          else
          {
            dataFine = DateUtils.formatDateNotNull(delegaVO.getDataFine());
            nomeUtenIrideFineDelega = delegaVO.getNomeUtenIrideFineDelega();
          }
      	 	htmpl.set("blkElencoDeleghe.blkDelega.dataFine", dataFine);
       		htmpl.set("blkElencoDeleghe.blkDelega.nomeUtenIrideFineDelega", nomeUtenIrideFineDelega);
       		
       	 	htmpl.set("blkElencoDeleghe.blkDelega.dataRicRevoca", DateUtils.formatDateNotNull(delegaVO.getDataRicevutaRitornoDelega()));
         
     		}
    	}
   	}
 	}
 	catch(Exception e) 
 	{
   	SolmrLogger.fatal(this,"\n\n Errore nella pagina storicoMandatiView.jsp: "+e.toString()+"\n\n");
   	if (errors ==null) errors=new ValidationErrors();
   	errors.add("error", new ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
 	}
 	HtmplUtil.setErrors(htmpl,errors,request, application);
 	out.print(htmpl.text());

  	SolmrLogger.debug(this, " - storicoMandatiView.jsp - FINE PAGINA");
%>


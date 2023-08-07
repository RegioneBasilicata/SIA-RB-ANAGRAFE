  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>



<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.util.*" %>


<%


  java.io.InputStream layout = application.getResourceAsStream("/layout/sediAAEP.htm");
  SolmrLogger.debug(this, "Found layout: "+layout);
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  Sede[] sedeInfocamere=(Sede[])session.getAttribute("sedeInfocamere");
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  

  HtmplUtil.setValues(htmpl, anagAziendaVO);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  try
  {
    if (sedeInfocamere!=null)
    {
      for(int i=0;i<sedeInfocamere.length;i++)
      {
        Vector<String> vCodtecoSec = new Vector<String>();
        if (sedeInfocamere[i]!=null)
        {
          htmpl.newBlock("blkSede");

          String indirizzo="";

          if (sedeInfocamere[i].getToponimo().getValue()!=null)
            indirizzo+=sedeInfocamere[i].getToponimo().getValue()+" ";
          if (sedeInfocamere[i].getIndirizzo().getValue()!=null)
            indirizzo+=sedeInfocamere[i].getIndirizzo().getValue()+" ";
          if (sedeInfocamere[i].getNumeroCivico().getValue()!=null)
            indirizzo+=sedeInfocamere[i].getNumeroCivico().getValue()+" ";
          if (sedeInfocamere[i].getFrazioneUL().getValue()!=null)
            indirizzo+=sedeInfocamere[i].getFrazioneUL().getValue();

          // La sede legale (codTipoLocazione = SE) non è importabile. Non visualizzare
          // il check di selezione nell\u2019elenco.
          // Le unità locali cessate (sedeInfocamere[i].dataCessaz not null) non sono
          // importabili. Non visualizzare il check di selezione nell\u2019elenco.
          // Le unità locali che presentano almeno una di queste casistiche,
          // non sono importabili (non visualizzare il check di selezione nell\u2019elenco):
          // o	Comune non valorizzato
          // o	Indirizzo non valorizzato
          if (!"SE".equals(sedeInfocamere[i].getCodTipoLocalizzazione().getValue())
               && sedeInfocamere[i].getDataCessaz().getValue() == null
               && sedeInfocamere[i].getNomeComune().getValue() != null
               && !"".equals(sedeInfocamere[i].getNomeComune().getValue())
               && indirizzo != null
               && !"".equals(indirizzo)
             )
          {
            htmpl.newBlock("blkSede.blkradioOK");
            htmpl.set("blkSede.blkradioOK.sedi",""+i);
          }
          else
          {
            htmpl.newBlock("blkSede.blkNoRadio");
          }


          //Comune	Decodifica di sedeInfocamere[i].codComuneUL
          //           Se sedeInfocamere[i].descrStatoEstero è valorizzato visualizzare
          //           solo sedeInfocamere[i].descrStatoEstero
          if(sedeInfocamere[i].getNomeComune().getValue() != null && !sedeInfocamere[i].getNomeComune().getValue().trim().equals(""))
            htmpl.set("blkSede.comune",StringUtils.checkNull(sedeInfocamere[i].getNomeComune().getValue()));
          else{
        	  //decodifica del comune
        	  String istatProv = anagFacadeClient.getIstatProvinciaBySiglaProvincia(sedeInfocamere[i].getSiglaProvUL().getValue());
			  SolmrLogger.error(this, " --- istatProv ="+istatProv);
			  String istatComuneNew = istatProv+sedeInfocamere[i].getCodComune().getValue();
			  SolmrLogger.error(this, " --- istatComuneNew ="+istatComuneNew);
			  
			  ComuneVO comune = anagFacadeClient.getComuneByISTAT(istatComuneNew);
			  if(comune != null){
				  String descrComune = comune.getDescom();
				  SolmrLogger.error(this, " --- descrComune ="+descrComune);
				  htmpl.set("blkSede.comune",StringUtils.checkNull(descrComune));
			  }
          }
        	  

          //Indirizzo	sedeInfocamere[i].descrToponimoUL + sedeInfocamere[i].viaUL + sedeInfocamere[i].numCivicoUL +
          //           sedeInfocamere[i].frazioneUL (se valorizzata).Tutti separati tra loro dallo spazio.
          htmpl.set("blkSede.indirizzo",StringUtils.checkNull(indirizzo));

          //Cap	sedeInfocamere[i].capUL
          htmpl.set("blkSede.cap",StringUtils.checkNull(sedeInfocamere[i].getCap().getValue()));


          if ("SE".equals(sedeInfocamere[i].getCodTipoLocalizzazione().getValue()))
          {
            htmpl.set("blkSede.tipo","1");
            htmpl.set("blkSede.descrizioneTipo","sede legale");
          }
          if ("SS".equals(sedeInfocamere[i].getCodTipoLocalizzazione().getValue()))
          {
            htmpl.set("blkSede.tipo","2");
            htmpl.set("blkSede.descrizioneTipo","sede secondaria");
          }
          if ("UL".equals(sedeInfocamere[i].getCodTipoLocalizzazione().getValue()))
          {
            htmpl.set("blkSede.tipo","3");
            htmpl.set("blkSede.descrizioneTipo","Unità locale");
          }

          //Denominazione	sedeInfocamere[i].DenominazioneUL (troncata a 100 chr)
          if(StringUtils.checkNull(sedeInfocamere[i].getDenominazioneSede().getValue()).length() > 100)
            htmpl.set("blkSede.denominazione",StringUtils.checkNull(sedeInfocamere[i].getDenominazioneSede().getValue()).substring(0, 100));
          else
            htmpl.set("blkSede.denominazione",StringUtils.checkNull(sedeInfocamere[i].getDenominazioneSede().getValue()));


          List<AtecoRI2007Infoc> atecoRI2007Infoc=sedeInfocamere[i].getListaAtecoRI2007Infoc();
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
              if(codiceAteco != null)
              {
                codiceAteco = codiceAteco.trim();
                codiceAteco = codiceAteco.replaceAll("\\.","");
                CodeDescription ateco = anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAteco);
                if (ateco!=null)
                  htmpl.set("blkSede.ateco","("+StringUtils.checkNull(codiceAteco)+") - "+
                  StringUtils.checkNull(ateco.getDescription()));
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
	              if(codiceAteco != null)
	              {
	                codiceAteco = codiceAteco.trim();
	                codiceAteco = codiceAteco.replaceAll("\\.","");
	                CodeDescription ateco=anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAteco);
	                if (ateco!=null)
	                  htmpl.set("blkSede.ateco","("+StringUtils.checkNull(codiceAteco)+") - "+
	                  StringUtils.checkNull(ateco.getDescription()));
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
                  if(!vCodtecoSec.contains(atecoRI2007Infoc.get(g).getCodAteco2007().getValue()))
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
            
            String atecoSecondari = "";
            for(int g=0;g<vCodtecoSec.size();g++)
            {
              String codiceAtecoSec = vCodtecoSec.get(g).trim();
              codiceAtecoSec = codiceAtecoSec.replaceAll("\\.","");
              CodeDescription atecoSec=anagFacadeClient.getAttivitaATECObyCodeParametroCATE(codiceAtecoSec);
              if (atecoSec!=null)
              {
                atecoSecondari += "("+StringUtils.checkNull(codiceAtecoSec)+") - "+
                StringUtils.checkNull(atecoSec.getDescription())+"<br>";
              }            
            }
            
            htmpl.set("blkSede.atecoSecondari",atecoSecondari, null);
          }

          //Data inizio attivita	sedeInfocamere[i].dataAperturaUL
          if (sedeInfocamere[i].getDataInizioAttivita().getValue() !=null)
            htmpl.set("blkSede.dataInizioAttivita",DateUtils.formatDate(DateUtils.convert((sedeInfocamere[i].getDataInizioAttivita().getValue()))));
          else
            htmpl.set("blkSede.dataInizioAttivita","");

          //Data cessazione	sedeInfocamere[i].dataCessaz
          if (sedeInfocamere[i].getDataCessaz().getValue()!=null)
            htmpl.set("blkSede.dataCessazione",DateUtils.formatDate(DateUtils.convert((sedeInfocamere[i].getDataCessaz().getValue()))));
          else
            htmpl.set("blkSede.dataCessazione","");

          //Causale cessazione	sedeInfocamere[i].descrCausaleCessaz
          htmpl.set("blkSede.causale",StringUtils.checkNull(sedeInfocamere[i].getDescrizioneCausaleCessazione().getValue()));
          //Telefono	sedeInfocamere[i].Telef
          htmpl.set("blkSede.telefono",StringUtils.checkNull(sedeInfocamere[i].getTelefono().getValue()));
          //Fax	sedeInfocamere[i].Telefax
          htmpl.set("blkSede.telefax",StringUtils.checkNull(sedeInfocamere[i].getFax().getValue()));

        }
      }
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
    if (errors==null) errors=ve;
  }
  HtmplUtil.setErrors(htmpl, errors, request, application);
%>
<%= htmpl.text()%>



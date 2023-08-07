<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.ws.cciaa.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>




<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/popUnitaVitateDettaglioCCIAA.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
  // le variabili sono presenti dentro il file include)
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // Se si è verificato un errore
  String messaggioErrore = (String) request.getAttribute("messaggioErrore");

  UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) session.getAttribute("uvResponseCCIAA");
  StoricoParticellaVO storicoParticellaVO= (StoricoParticellaVO) session.getAttribute("storicoParticellaVOCCIAA");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String parametroLockUvConsolidate = (String)request.getAttribute("parametroLockUvConsolidate");
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String[] idDaModificare = request.getParameterValues("chkIdUnitaArborea");
  ValidationError errori = (ValidationError)request.getAttribute("error");
  ValidationErrors errors = new ValidationErrors();
  HashMap<Long,DatiUvCCIAA> uvImportaDaCCIAA = new HashMap<Long,DatiUvCCIAA>();
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  
  
  if(errori != null) {
    errors.add("error", errori);
  }
  
  //Verfica se può importare dall'albo vigneti
  boolean flagImportazioneRuolo = false;
  if(Validator.isNotEmpty(request.getAttribute("visualizzaCheck")))
  {
    flagImportazioneRuolo = true;
  }
  
  

  if (storicoParticellaVO!=null)
  {
    /*
    Visualizzare in testa alla pagina le informazioni catastali dell\u2019u.v. selezionata precedentemente.
    Nota che nel caso in cui vengano restituite infomazioni dal w.s. della CCIAA invocando il servizio
    passando nel parametro della \u201Cparticella\u201D anche  la lettera \u201CP\u201D, evenziare nel relativo campo a video
    \u201CParticella\u201D il valore del relativo parametro.
    */
    ComuneVO comuneSiap=storicoParticellaVO.getComuneParticellaVO();

    if (comuneSiap!=null)
      htmpl.set("comuneSiap", comuneSiap.getDescom()+" ("+comuneSiap.getSiglaProv()+")");
      
    htmpl.set("sezioneSiap", storicoParticellaVO.getSezione());
    htmpl.set("foglioSiap", storicoParticellaVO.getFoglio());
    htmpl.set("particellaSiap", storicoParticellaVO.getParticella());
    htmpl.set("subalternoSiap", storicoParticellaVO.getSubalterno());
    htmpl.set("superficieGraficaSiap", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
  }

  if (Validator.isNotEmpty(messaggioErrore))
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", messaggioErrore);
  }
  else
  {
    if (uvResponseCCIAA!=null)
    {
      if(!SolmrConstants.CCIAA_CODICE_SERVIZIO_OK.equals(uvResponseCCIAA.getCodRet()))
      {
        //CCIAA ci ha risposto con un'errore che deve essere comunicato all'utente
        htmpl.newBlock("blkErrore");
        if(SolmrConstants.CCIAA_CODICE_SERVIZIO_NO_RECORD.equals(uvResponseCCIAA.getCodRet()))
          htmpl.set("blkErrore.messaggio", AnagErrors.ERR_NO_ALBO_VIGNETI_CCIAA);
        else
          htmpl.set("blkErrore.messaggio", AnagErrors.ERR_SERVIZIO_CCIAA+uvResponseCCIAA.getSegnalazione()+" ("+uvResponseCCIAA.getCodRet()+")");
      }
      else
      {
      
        boolean almenoUnoselezionabile = false;
        
      
        //Tutto ok, procedo quindi con la visualizzazione degli allevamenti
        if (uvResponseCCIAA.getDatiUv()!=null && uvResponseCCIAA.getDatiUv().length>0)
        {
        
          boolean flagImportazioneDati = false;
          boolean flagProvinciaCompetenza = true;
          boolean flagStessoCUAA = false;
          htmpl.newBlock("blkHeaderVigneto");
          
          
          /*if(flagImportazioneRuolo)
          {
            htmpl.newBlock("blkHeaderVigneto.blkImportazione");
          }*/
          
          
          htmpl.set("blkHeaderVigneto.numVigneti",uvResponseCCIAA.getDatiUv().length+"");

          for (int i=0;i<uvResponseCCIAA.getDatiUv().length;i++)
          {

            String blk="blkHeaderVigneto.blkVigneti";
            htmpl.newBlock(blk);
            
            
            
            
            Vector<UnitaArboreaCCIAAVO> vUnitaArboreaCCIAA = uvResponseCCIAA.getDatiUv()[i].getVUnitaArboreaCCIAA();
            
            Long idStoricoUnitaArborea = null;
            Long idStoricoUnitaArboreaAnnoSuperficie = null;
            Long idStoricoUnitaArboreaAnno = null;
            Long idStoricoUnitaArboreaSenzaAnno = null;
            
            boolean flagModificaVITI = false;
            boolean flagModificaVITIAnnoSuperficie = false;
            boolean flagModificaVITIAnno = false;
            boolean flagModificaVITISenzaAnno = false;
            
            boolean flagConsolidamento = false;
            boolean flagConsolidamentoAnnoSuperficie = false;
            boolean flagConsolidamentoAnno = false;
            boolean flagConsolidamentoSenzaAnno = false;
            
            //non ho trovato nessuna corrispondenza coi dati dell'albo
            if(vUnitaArboreaCCIAA == null)
            {
              flagImportazioneDati = false;
            }
            else
            {
              //se trovo più unita vitate che hanno corrispondenza
              
              //verico se l'UV appartiene alla provincia di competenza
              if(ruoloUtenza.isUtenteProvinciale())
              {
                if(!uvResponseCCIAA.getDatiUv()[i].isStessaProvinciaCompetenza())
                {
                  flagProvinciaCompetenza = false;
                }
              }
              
              int trovatiAnno = 0;
              int trovatiAnnoSuperficie = 0;
              int trovatiSenzaAnno = 0;  
              if(vUnitaArboreaCCIAA.size() > 1)
              {
                for(int j=0;j<vUnitaArboreaCCIAA.size();j++)
                {
                  UnitaArboreaCCIAAVO uCCIAA = vUnitaArboreaCCIAA.get(j);                  
                  
                  if(uCCIAA.isPresenzaVitignoCCIAA() && uCCIAA.isTrovatoAlbo()
                    && ((uCCIAA.getAnnoImpianto() != null) 
                          && uCCIAA.getAnnoImpianto().intValue() == uvResponseCCIAA.getDatiUv()[i].getAnnoVegetativo())
                    && ((uCCIAA.getSupIscritta() != null)
                          && (uCCIAA.getSupIscritta().doubleValue() == uvResponseCCIAA.getDatiUv()[i].getSuperficieH()))
                    )
                  {
                    
                    idStoricoUnitaArboreaAnnoSuperficie = uCCIAA.getIdStoricoUnitaArborea();
                    flagModificaVITIAnnoSuperficie = uCCIAA.isModificaVITI();
                    if("S".equalsIgnoreCase(parametroLockUvConsolidate))
                    {
                      flagConsolidamentoAnnoSuperficie = uCCIAA.isConsolidamentoGis();
                    }
                    trovatiAnnoSuperficie++;
                  }
                  else if(uCCIAA.isPresenzaVitignoCCIAA() && uCCIAA.isTrovatoAlbo()
                    && ((uCCIAA.getAnnoImpianto() != null) 
                          && uCCIAA.getAnnoImpianto().intValue() == uvResponseCCIAA.getDatiUv()[i].getAnnoVegetativo()))
                  {
                    idStoricoUnitaArboreaAnno = uCCIAA.getIdStoricoUnitaArborea();
                    flagModificaVITIAnno = uCCIAA.isModificaVITI();
                    if("S".equalsIgnoreCase(parametroLockUvConsolidate))
                    {
                      flagConsolidamentoAnno = uCCIAA.isConsolidamentoGis();
                    }
                    trovatiAnno++;
                  }
                  else if(uCCIAA.isPresenzaVitignoCCIAA() && uCCIAA.isTrovatoAlbo())
                  {
                    idStoricoUnitaArboreaSenzaAnno = uCCIAA.getIdStoricoUnitaArborea();
                    flagModificaVITISenzaAnno = uCCIAA.isModificaVITI();
                    if("S".equalsIgnoreCase(parametroLockUvConsolidate))
                    {
                      flagConsolidamentoSenzaAnno = uCCIAA.isConsolidamentoGis();
                    }
                    trovatiSenzaAnno++;
                  }                
                }
                //Devo trovare uno e uno solo che abbia la corrispondenza annoImpianto/annoVegetativo
                if(trovatiAnnoSuperficie == 1)
                {
                  idStoricoUnitaArborea = idStoricoUnitaArboreaAnnoSuperficie;
                  flagModificaVITI = flagModificaVITIAnnoSuperficie;
                  flagConsolidamento = flagConsolidamentoAnnoSuperficie; 
                  flagImportazioneDati = true;
                }
                else if(trovatiAnno == 1)
                {
                  idStoricoUnitaArborea = idStoricoUnitaArboreaAnno;
                  flagModificaVITI = flagModificaVITIAnno; 
                  flagConsolidamento = flagConsolidamentoAnno;
                  flagImportazioneDati = true;
                }
                //Se non esiste la condizione di sopra posso provare a vedere se ne esiste
                // uno e uno solo che abbia la corrispondenza senza annoImpianto/annoVegetativo
                else if(trovatiSenzaAnno == 1)
                {
                  idStoricoUnitaArborea = idStoricoUnitaArboreaSenzaAnno;
                  flagModificaVITI = flagModificaVITISenzaAnno;
                  flagConsolidamento = flagConsolidamentoSenzaAnno; 
                  flagImportazioneDati = true;
                }
                else
                {
                  flagImportazioneDati = false;
                }
              }
              else //se ne trovo solo uno inutile controllare la corrispondenza annoImpianto/annoVegetativo
              {
                UnitaArboreaCCIAAVO uCCIAA = vUnitaArboreaCCIAA.get(0);
                if(uCCIAA.isPresenzaVitignoCCIAA() && uCCIAA.isTrovatoAlbo())
                {
                  idStoricoUnitaArborea = uCCIAA.getIdStoricoUnitaArborea();
                  flagModificaVITI = uCCIAA.isModificaVITI();
                  if("S".equalsIgnoreCase(parametroLockUvConsolidate))
                  {
                    flagConsolidamento = uCCIAA.isConsolidamentoGis();
                  }
                  flagImportazioneDati = true;
                }
                else
                {
                  flagImportazioneDati = false;
                }
              }
              
            }
            
            
            
            if (anagAziendaVO.getCUAA() !=null)
            {
              if(anagAziendaVO.getCUAA().equalsIgnoreCase(uvResponseCCIAA.getDatiUv()[i].getCuaa()))
              {
                //Evidenziare in rosso quelle u.v. che non hanno 
                //il codice fiscale dell'azienda in questione
                flagStessoCUAA = true;
              }
            }
            
            
            
            
            if(flagImportazioneRuolo && flagImportazioneDati && flagProvinciaCompetenza 
              && !flagConsolidamento && (!flagModificaVITI || ruoloUtenza.isUtentePA()))
            {
              htmpl.newBlock("blkHeaderVigneto.blkVigneti.blkPieno");
              
              if(!almenoUnoselezionabile)
              {
                almenoUnoselezionabile = true;
              }
              
              if(idStoricoUnitaArborea != null)
              {
                htmpl.set(blk+".blkPieno.chkIdUnitaArborea", idStoricoUnitaArborea.toString());
                
                uvImportaDaCCIAA.put(idStoricoUnitaArborea, uvResponseCCIAA.getDatiUv()[i]);
                
                if(idDaModificare !=null)
                {
                  for(int j=0;j<idDaModificare.length;j++)
                  {
                    if(idStoricoUnitaArborea.toString().equals(idDaModificare[j]))
                    {
                      htmpl.set(blk+".blkPieno.checked","checked=\"checked\"");
                    }
                  }              
                }
              }
              
              
            }
            else
            {
              htmpl.newBlock("blkHeaderVigneto.blkVigneti.blkVuoto");
              if(flagConsolidamento)
              {                
                htmpl.set("blkHeaderVigneto.blkVigneti.blkVuoto.problema", MessageFormat.format(htmlStringKO,
                new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_CONSOLIDAMENTO_UV_IMPORTA_CCIAA_JAVASCRIPT+"'",
                AnagErrors.ERRORE_KO_CONSOLIDAMENTO_UV_IMPORTA_CCIAA}), null);
              }
              else if(flagModificaVITI && !ruoloUtenza.isUtentePA())
              {                
                htmpl.set("blkHeaderVigneto.blkVigneti.blkVuoto.problema", MessageFormat.format(htmlStringKO,
                new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_IMPORTA_CCIAA_JAVASCRIPT+"'",
                AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_IMPORTA_CCIAA}), null);
              }              
              
            }
            
            if (!flagStessoCUAA)
            {
              //Evidenziare in rosso quelle u.v. che non hanno 
              //il codice fiscale dell'azienda in questione
              htmpl.newBlock(blk+".blkRed");
            }
            else
            { 
              htmpl.newBlock(blk+".blkBlack");
            }
            
            
            

            htmpl.set(blk+".descAlbo",uvResponseCCIAA.getDatiUv()[i].getDescAlbo());
            htmpl.set(blk+".siglaProvincia",uvResponseCCIAA.getDatiUv()[i].getSiglaProvincia());
            htmpl.set(blk+".nrMatricola",uvResponseCCIAA.getDatiUv()[i].getNrMatricola()+"");
            htmpl.set(blk+".superficieH",it.csi.smranag.smrgaa.util.Formatter.formatDouble4(uvResponseCCIAA.getDatiUv()[i].getSuperficieH()));

            htmpl.set(blk+".varieta",uvResponseCCIAA.getDatiUv()[i].getVarieta());
            htmpl.set(blk+".annoVegetativo",uvResponseCCIAA.getDatiUv()[i].getAnnoVegetativo()+"");
            htmpl.set(blk+".numCeppi",uvResponseCCIAA.getDatiUv()[i].getNumCeppi()+"");

            htmpl.set(blk+".codiceMipaf",uvResponseCCIAA.getDatiUv()[i].getCodiceMipaf());
            htmpl.set(blk+".cuaaCCIAA",uvResponseCCIAA.getDatiUv()[i].getCuaa());
            htmpl.set(blk+".denominazioneCCIAA",uvResponseCCIAA.getDatiUv()[i].getDenominazioneAzienda());
            htmpl.set(blk+".comuneRes",uvResponseCCIAA.getDatiUv()[i].getDescComuneRes()+" ("+uvResponseCCIAA.getDatiUv()[i].getSiglaProvinciaRes()+")");
            htmpl.set(blk+".indirizzoResidenza",uvResponseCCIAA.getDatiUv()[i].getIndirizzoResidenza());
          }
          
          
          
          //se trovato almeno uno selezionabile
          //faccio vedere i tasti!!!!          
          if(almenoUnoselezionabile)
          {
            htmpl.newBlock("blkImportazione");
          }
          
          
          session.setAttribute("uvImportaDaCCIAA", uvImportaDaCCIAA);
          
          
          
          htmpl.set("blkHeaderVigneto.totSuperficieH",it.csi.smranag.smrgaa.util.Formatter.formatDouble4(uvResponseCCIAA.getTotSuperficieH()));

          //Gestisco le freccie relative all'ordinamento
          if("alboDesc".equalsIgnoreCase(uvResponseCCIAA.getOrdAlbo()))
          {
            htmpl.set("blkHeaderVigneto.ordinaAlbo", "su");
            htmpl.set("blkHeaderVigneto.descOrdinaAlbo", "ordine crescente");
            htmpl.set("blkHeaderVigneto.tipoOrdinaAlbo", "alboAsc");
          }
          else
          {
            htmpl.set("blkHeaderVigneto.ordinaAlbo", "giu");
            htmpl.set("blkHeaderVigneto.descOrdinaAlbo", "ordine decrescente");
            htmpl.set("blkHeaderVigneto.tipoOrdinaAlbo", "alboDesc");
          }

        }
        else
        {
          htmpl.newBlock("blkErrore");
          htmpl.set("blkErrore.messaggio", AnagErrors.ERR_NO_ALBO_VIGNETI_CCIAA);
        }
      }
    }
  }
  
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>


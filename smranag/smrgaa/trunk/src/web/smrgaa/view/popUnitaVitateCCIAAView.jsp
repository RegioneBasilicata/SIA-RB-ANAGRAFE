<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.ws.cciaa.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.UnitaArboreaCCIAAVO" %>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/popUnitaVitateCCIAA.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
  // le variabili sono presenti dentro il file include)
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";

  // Se si è verificato un errore
  String messaggioErrore = (String) request.getAttribute("messaggioErrore");

  UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) session.getAttribute("uvResponseCCIAA");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String[] idDaModificare = request.getParameterValues("chkIdUnitaArborea");
  ValidationError errori = (ValidationError)request.getAttribute("error");
  ValidationErrors errors = new ValidationErrors();
  HashMap<Long,DatiUvCCIAA> uvImportaDaCCIAA = new HashMap<Long,DatiUvCCIAA>();
  String parametroLockUvConsolidate = (String)request.getAttribute("parametroLockUvConsolidate");
  
  
  if(errori != null) {
    errors.add("error", errori);
  }
  
  //Verfica se può importare dall'albo vigneti
  boolean flagImportazioneRuolo = false;
  if(Validator.isNotEmpty(request.getAttribute("visualizzaCheck")))
  {
    flagImportazioneRuolo = true;
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
          htmpl.newBlock("blkHeaderVigneto");
          //Popolo il filtro di ricerca dell'albo
          if(uvResponseCCIAA.getAlbo() != null && uvResponseCCIAA.getAlbo().length > 0)
          {
            for (int i=0;i<uvResponseCCIAA.getAlbo().length;i++)
            {
              htmpl.newBlock("blkHeaderVigneto.blkCmbAlbo");
              htmpl.set("blkHeaderVigneto.blkCmbAlbo.albo",uvResponseCCIAA.getAlbo()[i]);
              if(uvResponseCCIAA.getAlbo()[i].equals(uvResponseCCIAA.getFiltroAlbo()))
                htmpl.set("blkHeaderVigneto.blkCmbAlbo.selected","selected");
            }
          }
          //Popolo il filtro di ricerca del vigneto
          if(uvResponseCCIAA.getAlbo() != null && uvResponseCCIAA.getAlbo().length > 0)
          {
            for (int i=0;i<uvResponseCCIAA.getVitigno().length;i++)
            {
              htmpl.newBlock("blkHeaderVigneto.blkCmbVitigno");
              htmpl.set("blkHeaderVigneto.blkCmbVitigno.vitigno",uvResponseCCIAA.getVitigno()[i]);
              if(uvResponseCCIAA.getVitigno()[i].equals(uvResponseCCIAA.getFiltroVitigno()))
                htmpl.set("blkHeaderVigneto.blkCmbVitigno.selected","selected");
            }
          }
          htmpl.set("blkHeaderVigneto.numVigneti",uvResponseCCIAA.getDatiUv().length+"");

          boolean filtroAlbo=Validator.isNotEmpty(uvResponseCCIAA.getFiltroAlbo());
          boolean filtroVitigno=Validator.isNotEmpty(uvResponseCCIAA.getFiltroVitigno());

          for (int i=0;i<uvResponseCCIAA.getDatiUv().length;i++)
          {
            boolean flagImportazioneDati = false;
            boolean flagProvinciaCompetenza = true;
            
            if (filtroAlbo)
              if (!uvResponseCCIAA.getFiltroAlbo().equals(uvResponseCCIAA.getDatiUv()[i].getDescAlbo())) continue;
            if (filtroVitigno)
              if (!uvResponseCCIAA.getFiltroVitigno().equals(uvResponseCCIAA.getDatiUv()[i].getVarieta())) continue;

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
                          && (uCCIAA.getSupIscritta().doubleValue() == uvResponseCCIAA.getDatiUv()[i].getSuperficieH())))
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
                if(trovatiAnnoSuperficie == 1)
                {
                  idStoricoUnitaArborea = idStoricoUnitaArboreaAnnoSuperficie;
                  flagModificaVITI = flagModificaVITIAnnoSuperficie;
                  flagConsolidamento = flagConsolidamentoAnnoSuperficie;
                  flagImportazioneDati = true;
                }
                //Devo trovare uno e uno solo che abbia la corrispondenza annoImpianto/annoVegetativo
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
              
              //guardo se nei record trovati relativi alle varie unita arboree esiste
              //almeno una con la conduzione!!!
              for(int j=0;j<vUnitaArboreaCCIAA.size();j++)
              {
                UnitaArboreaCCIAAVO uCCIAA = vUnitaArboreaCCIAA.get(j);
                if(uCCIAA.isTrovataConduzione())
                {
                  uvResponseCCIAA.getDatiUv()[i].setPresenteFascicolo(true);
                  break;
                }
              }
            }
            
            if(flagImportazioneRuolo && flagImportazioneDati && flagProvinciaCompetenza
              && !flagConsolidamento  && (!flagModificaVITI || ruoloUtenza.isUtentePA()))
            {
              htmpl.newBlock(blk+".blkPieno");
              
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
              htmpl.newBlock(blk+".blkVuoto");
              if(flagConsolidamento)
              {                
                htmpl.set(blk+".blkVuoto.problema", MessageFormat.format(htmlStringKO,
                new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_CONSOLIDAMENTO_UV_IMPORTA_CCIAA_JAVASCRIPT+"'",
                AnagErrors.ERRORE_KO_CONSOLIDAMENTO_UV_IMPORTA_CCIAA}), null);
              }
              else if(flagModificaVITI && !ruoloUtenza.isUtentePA())
              {
                //htmpl.set(blk+".blkVuoto.problema", idStoricoUnitaArborea.toString());
                
                htmpl.set(blk+".blkVuoto.problema", MessageFormat.format(htmlStringKO,
                new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_IMPORTA_CCIAA_JAVASCRIPT+"'",
                AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_IMPORTA_CCIAA}), null);
              }
              
            }
            
            if (!uvResponseCCIAA.getDatiUv()[i].isPresenteFascicolo())
            {
              //Evidenziare in rosso quelle u.v. che non trovano una corrispondenza
              //come chiave catastale nell\u2019archivio dell\u2019anagrafe
              htmpl.newBlock(blk+".blkRed");
            }
            else htmpl.newBlock(blk+".blkBlack");
            
            
            htmpl.set(blk+".descAlbo",uvResponseCCIAA.getDatiUv()[i].getDescAlbo());
            htmpl.set(blk+".siglaProvincia",uvResponseCCIAA.getDatiUv()[i].getSiglaProvincia());
            htmpl.set(blk+".nrMatricola",uvResponseCCIAA.getDatiUv()[i].getNrMatricola()+"");
            htmpl.set(blk+".comune",uvResponseCCIAA.getDatiUv()[i].getDescComune()+" ("+uvResponseCCIAA.getDatiUv()[i].getSiglaProvincia()+")");

            htmpl.set(blk+".sezione",uvResponseCCIAA.getDatiUv()[i].getSezione());
            htmpl.set(blk+".foglio",uvResponseCCIAA.getDatiUv()[i].getFoglio());
            htmpl.set(blk+".particella",uvResponseCCIAA.getDatiUv()[i].getParticella());
            htmpl.set(blk+".superficieH",it.csi.smranag.smrgaa.util.Formatter.formatDouble4(uvResponseCCIAA.getDatiUv()[i].getSuperficieH()));

            htmpl.set(blk+".varieta",uvResponseCCIAA.getDatiUv()[i].getVarieta());
            htmpl.set(blk+".annoVegetativo",uvResponseCCIAA.getDatiUv()[i].getAnnoVegetativo()+"");
            htmpl.set(blk+".numCeppi",uvResponseCCIAA.getDatiUv()[i].getNumCeppi()+"");
            htmpl.set(blk+".siglaCciaa",uvResponseCCIAA.getDatiUv()[i].getSiglaCciaa());
          }
          
          //se trovato almeno uno selezionabile
          //faccio vedere i tasti!!!!          
          if(almenoUnoselezionabile)
          {
            htmpl.newBlock("blkHeaderVigneto.blkTastiImportazione");
          }
          
          
          session.setAttribute("uvImportaDaCCIAA", uvImportaDaCCIAA);
          
          htmpl.set("blkHeaderVigneto.totSuperficieH",it.csi.smranag.smrgaa.util.Formatter.formatDouble4(uvResponseCCIAA.getTotSuperficieH()));

          //Gestisco le freccie relative all'ordinamento
          if("comuneAsc".equalsIgnoreCase(uvResponseCCIAA.getOrdComune()))
          {
            htmpl.set("blkHeaderVigneto.ordinaComune", "giu");
            htmpl.set("blkHeaderVigneto.descOrdinaComune", "ordine decrescente");
            htmpl.set("blkHeaderVigneto.tipoOrdinaComune", "comuneDesc");
          }
          else
          {
            htmpl.set("blkHeaderVigneto.ordinaComune", "su");
            htmpl.set("blkHeaderVigneto.descOrdinaComune", "ordine crescente");
            htmpl.set("blkHeaderVigneto.tipoOrdinaComune", "comuneAsc");
          }

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


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
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="javax.xml.namespace.QName"%>
<%@ page import="javax.xml.bind.JAXBElement"%>


<%


  java.io.InputStream layout = application.getResourceAsStream("/layout/contitolariAAEP.htm");
  SolmrLogger.debug(this, "Found layout: "+layout);
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  Azienda impresaInfoc=(Azienda)session.getAttribute("impresaInfoc");
  HashMap listaPersone=(HashMap)session.getAttribute("listaPersone");


  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  try
  {
    if(impresaInfoc!=null)
    {
      List<ListaPersoneRI> listaPersonaRI=impresaInfoc.getListaPersoneRI();

      if (listaPersonaRI!=null)
      {
        for(int i=0;i<listaPersonaRI.size();i++)
        {
          Object pers[]=new Object[2];
          pers=(Object[])listaPersone.get(listaPersonaRI.get(i).getProgrPersona().getValue());

          if (pers!=null)
          {
            PersonaRIInfoc personaRIInfoc=(PersonaRIInfoc)pers[0];
            Boolean legatoAzienda=(Boolean)pers[1];
            if (personaRIInfoc!=null)
            {
              List<CaricaPersonaInfoc> caricaPersonaInfoc=personaRIInfoc.getListaCaricaPersInfoc();
              if (caricaPersonaInfoc!=null)
              {

                TreeMap listaCaricaPers = new TreeMap();

                /***
                 * Ordino per descrizione carica. Aggiungere il valore di j mi permette
                 * di gestire anche codici fiscali duplicati
                 */
                for (int j=0;j<caricaPersonaInfoc.size();j++)
                  listaCaricaPers.put(caricaPersonaInfoc.get(j).getDescrCarica().getValue()+j,caricaPersonaInfoc.get(j));

                CaricaPersonaInfoc[] caricaPersonaInfocArr =(CaricaPersonaInfoc[])listaCaricaPers.values().toArray(new CaricaPersonaInfoc[0]);
                
                caricaPersonaInfoc = Arrays.asList(caricaPersonaInfocArr);

                String inizio="In anagrafe: ";

                //Visualizzo i dati dei soggetti collegati
                for (int j=0;j<caricaPersonaInfoc.size();j++)
                {
                  PersonaFisicaVO personaFisicaVO=null;
                  if (legatoAzienda!=null && legatoAzienda.booleanValue())
                    personaFisicaVO=anagFacadeClient.getRuolosuAnagrafe(personaRIInfoc.getCodiceFiscale().getValue(),
                                                                        anagAziendaVO.getIdAzienda().longValue(),
                                                                        caricaPersonaInfoc.get(j).getCodiceCarica().getValue());

                  htmpl.newBlock("blkpersona");

                  boolean statoEstero=false,statoEsteroRes=false;

                  //I soggetti con carica cessata (CaricaPersonaInfoc.dataFineCarica not null)
                  //non sono importabili, non visualizzare il ceck di selezione nell\u2019elenco.
                  // Anche I soggetti che presentano almeno una di queste casistiche,
                  // non sono importabili (non visualizzare il check di selezione nell\u2019elenco):
                  // - data inizio carica non valorizzata
                  // - codice fiscale non valorizzato
                  // - descrizione carica non valorizzato
                  // - cognome non valorizzato
                  // - nome non valorizzato
                  // - data di nascita non valorizzata
                  // - sesso
                  
                  boolean codiceFiscaleOK=Validator.controlloCf(personaRIInfoc.getCodiceFiscale().getValue());
                  
                  //Residenza Indirizzo PersonaRIInfoc.descrToponimoResidenza +
                  //PersonaRIInfoc.viaResidenza + PersonaRIInfoc.numCivicoResid +
                  //PersonaRIInfoc.descrFrazioneResid (separati tra loro dallo spazio)
                  String descrToponimoResid= personaRIInfoc.getDescrToponimoResid().getValue();
                  if (descrToponimoResid==null) descrToponimoResid="";
                  String viaResidenza= personaRIInfoc.getViaResidenza().getValue();
                  if (viaResidenza==null) viaResidenza="";
                  String numCivicoResid= personaRIInfoc.getNumCivicoResid().getValue();
                  if (numCivicoResid==null) numCivicoResid="";
                  String descrFrazioneRes= personaRIInfoc.getDescrFrazioneRes().getValue();
                  if (descrFrazioneRes==null) descrFrazioneRes="";
                  String resIndirizzo=descrToponimoResid+" "+viaResidenza+" "+numCivicoResid+" "+descrFrazioneRes;
                  resIndirizzo=resIndirizzo.trim();
                  
                  if (personaRIInfoc.getDescrStatoRes().getValue() !=null)
                  {
                    statoEsteroRes=statoEstero=true;
                    
                    String codComuneRes = anagFacadeClient.getIstatByDescComune(personaRIInfoc.getDescrStatoNascita().getValue());
                    
                    QName capQName = new QName("http://servizio.frontend.ls.com", "codComuneRes");
                    JAXBElement<String> codComuneResValue = new JAXBElement<String>(capQName, String.class, codComuneRes);
                    
                    personaRIInfoc.setCodComuneRes(codComuneResValue);
                  }
                  
                  if (personaRIInfoc.getDescrStatoNascita().getValue() !=null)
                    statoEstero=true;
                    
                  String siglaProvNasc= "";
                  String descComuneNasc="";
                  
                  try 
                  {
                    //Se il codice fiscale è OK estraggo l'istat del comune di nascita
                    ComuneVO comuneVO;
                    if (codiceFiscaleOK)
                    {
                      comuneVO=anagFacadeClient.getComuneByCUAA(personaRIInfoc.getCodiceFiscale().getValue().substring(11,15));
                      if (comuneVO!=null)
                      {
                        QName capQName = new QName("http://servizio.frontend.ls.com", "codComuneNascita");
                        JAXBElement<String> codComuneNascValue = new JAXBElement<String>(capQName, String.class, comuneVO.getIstatComune());
                        
                        personaRIInfoc.setCodComuneNascita(codComuneNascValue);
                        descComuneNasc=comuneVO.getDescom();
                        siglaProvNasc=comuneVO.getSiglaProv();
                      }
                    }
                  }
                  catch(Exception e) {}
                  
                  if (caricaPersonaInfoc.get(j).getDataFineCarica().getValue()==null
                  &&
                  caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()!=null
                  &&
                  codiceFiscaleOK
                  &&
                  caricaPersonaInfoc.get(j).getDescrCarica().getValue()!=null
                  &&
                  personaRIInfoc.getCognome().getValue()!=null
                  &&
                  personaRIInfoc.getNome().getValue()!=null
                  &&
                  personaRIInfoc.getCodComuneNascita().getValue()!=null
                  &&
                  personaRIInfoc.getDataNascita().getValue()!=null
                  &&
                  Validator.isNotEmpty(personaRIInfoc.getSesso().getValue())
                  &&
                  Validator.isNotEmpty(personaRIInfoc.getCodComuneRes().getValue())
                  &&
                  (Validator.isNotEmpty(personaRIInfoc.getCapResidenza().getValue()) || statoEsteroRes)
                  &&
                  Validator.isNotEmpty(resIndirizzo))
                  {
                    htmpl.newBlock("blkpersona.blkradioOK");
                    htmpl.set("blkpersona.blkradioOK.idSoggetto",personaRIInfoc.getProgrOrdineVisura()+"-"+caricaPersonaInfoc.get(j).getCodiceCarica().getValue());
                    htmpl.set("blkpersona.idSoggetto",personaRIInfoc.getProgrOrdineVisura()+"-"+caricaPersonaInfoc.get(j).getCodiceCarica().getValue());
                    if (legatoAzienda!=null && legatoAzienda.booleanValue())
                    {

                      if (personaFisicaVO!=null)
                      {
                        if(confrontaStr(personaFisicaVO.getCodiceFiscale(),personaRIInfoc.getCodiceFiscale().getValue()))
                          htmpl.set("blkpersona.codiceFiscaleColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.codiceFiscaleColor","rosso");
                          htmpl.set("blkpersona.codiceFiscaleAlt",inizio+StringUtils.checkNull(personaFisicaVO.getCodiceFiscale()));
                        }
                        if(confrontaStr(personaFisicaVO.getCodiceRuoloAAEP(),caricaPersonaInfoc.get(j).getCodiceCarica().getValue()))
                          htmpl.set("blkpersona.descrCaricaColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.descrCaricaColor","rosso");
                          htmpl.set("blkpersona.descrCaricaAlt",inizio+StringUtils.checkNull(personaFisicaVO.getRuolo()));
                        }
                        if(confrontaStr(personaFisicaVO.getCognome(),personaRIInfoc.getCognome().getValue()))
                          htmpl.set("blkpersona.cognomeColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.cognomeColor","rosso");
                          htmpl.set("blkpersona.cognomeAlt",inizio+StringUtils.checkNull(personaFisicaVO.getCognome()));
                        }
                        if(confrontaStr(personaFisicaVO.getNome(),personaRIInfoc.getNome().getValue()))
                          htmpl.set("blkpersona.nomeColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.nomeColor","rosso");
                          htmpl.set("blkpersona.nomeAlt",inizio+StringUtils.checkNull(personaFisicaVO.getNome()));
                        }
                        if(confrontaStr(personaFisicaVO.getSesso(),personaRIInfoc.getSesso().getValue()))
                          htmpl.set("blkpersona.sessoColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.sessoColor","rosso");
                          htmpl.set("blkpersona.sessoAlt",inizio+StringUtils.checkNull(personaFisicaVO.getSesso()));
                        }

                        String strDataNascitaAAEP="";
                        if (personaRIInfoc.getDataNascita().getValue()!=null)
                          strDataNascitaAAEP=DateUtils.formatDate(DateUtils.convert(personaRIInfoc.getDataNascita().getValue()));
                        if(confrontaStr(StringUtils.checkNull(personaFisicaVO.getStrNascitaData()),strDataNascitaAAEP))
                          htmpl.set("blkpersona.dataNascitaColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.dataNascitaColor","rosso");
                          htmpl.set("blkpersona.dataNascitaAlt",inizio+StringUtils.checkNull(personaFisicaVO.getStrNascitaData()));
                        }

                        if(confrontaStr(personaFisicaVO.getNascitaComune(),personaRIInfoc.getCodComuneNascita().getValue()))
                          htmpl.set("blkpersona.luogoNascitaColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.luogoNascitaColor","rosso");
                          htmpl.set("blkpersona.luogoNascitaAlt",inizio+StringUtils.checkNull(personaFisicaVO.getDescNascitaComune()));
                        }

                        String strInizioCaricaAAEP="";
                        if (caricaPersonaInfoc.get(j).getDataInizioCarica()!=null)
                          strInizioCaricaAAEP=DateUtils.formatDate(DateUtils.convert(caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()));
                        if(confrontaStr(StringUtils.checkNull(personaFisicaVO.getStrDataInizioRuolo()),strInizioCaricaAAEP))
                          htmpl.set("blkpersona.dataInizioCaricaColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.dataInizioCaricaColor","rosso");
                          htmpl.set("blkpersona.dataInizioCaricaAlt",inizio+StringUtils.checkNull(personaFisicaVO.getStrDataInizioRuolo()));
                        }

                        String strFineCaricaAAEP="";
                        if (caricaPersonaInfoc.get(j).getDataFineCarica().getValue() !=null)
                          strFineCaricaAAEP=DateUtils.formatDate(DateUtils.convert(caricaPersonaInfoc.get(j).getDataFineCarica().getValue()));
                        if(confrontaStr(StringUtils.checkNull(personaFisicaVO.getStrDataFineRuolo()),strFineCaricaAAEP))
                          htmpl.set("blkpersona.dataFineCaricaColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.dataFineCaricaColor","rosso");
                          htmpl.set("blkpersona.dataFineCaricaAlt",inizio+StringUtils.checkNull(personaFisicaVO.getStrDataFineRuolo()));
                        }

                        if(confrontaStr(personaFisicaVO.getResComune(),personaRIInfoc.getCodComuneRes().getValue()))
                          htmpl.set("blkpersona.luogoResidenzaColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.luogoResidenzaColor","rosso");
                          htmpl.set("blkpersona.luogoResidenzaAlt",inizio+StringUtils.checkNull(personaFisicaVO.getDescResComune()));
                        }
                        if(confrontaStr(personaFisicaVO.getResIndirizzo(),resIndirizzo))
                          htmpl.set("blkpersona.resIndirizzoColor","nero");
                        else
                        {
                          htmpl.set("blkpersona.resIndirizzoColor","rosso");
                          htmpl.set("blkpersona.resIndirizzoAlt",inizio+StringUtils.checkNull(personaFisicaVO.getResIndirizzo()));
                        }
                        if (statoEstero)
                        {
                          htmpl.set("blkpersona.asteriscoAlt","Attenzione! Il soggetto è nato/risiede in uno stato estero. Verificare che l'importazione dei dati sia terminata con successo");
                        }
                      }
                      else
                      {
                        htmpl.set("blkpersona.codiceFiscaleAlt",inizio);
                        htmpl.set("blkpersona.descrCaricaAlt",inizio);
                        htmpl.set("blkpersona.cognomeAlt",inizio);
                        htmpl.set("blkpersona.nomeAlt",inizio);
                        htmpl.set("blkpersona.sessoAlt",inizio);
                        htmpl.set("blkpersona.dataNascitaAlt",inizio);
                        htmpl.set("blkpersona.luogoNascitaAlt",inizio);
                        htmpl.set("blkpersona.dataInizioCaricaAlt",inizio);
                        htmpl.set("blkpersona.dataFineCaricaAlt",inizio);
                        htmpl.set("blkpersona.luogoResidenzaAlt",inizio);
                        htmpl.set("blkpersona.resIndirizzoAlt",inizio);
                        impostaRossoPerTutti(htmpl);
                      }
                    }
                    else impostaNeroPerTutti(htmpl);
                  }
                  else
                  {
                    impostaNeroPerTutti(htmpl);
                    htmpl.newBlock("blkpersona.blkNoRadio");
                  }

                  //Codice fiscale	PersonaRIInfoc.CodiceFiscale
                  htmpl.set("blkpersona.codiceFiscale",personaRIInfoc.getCodiceFiscale().getValue());

                  //Per verificare se il soggetto estratto da AAEP è già legato all\u2019azienda,
                  //accedere alle tabelle db_contitolare e db_persona_fisica per id_azienda
                  //dell'azienda in esame e codice fiscale del soggetto analizzato.
                  //Se esiste almeno un record il soggetto è già legato o comunque
                  //è stato legato all\u2019azienda. In questo caso concatenare al codice fiscale,
                  //visualizzato a video, un asterisco tra parentesi come apice.
                  //Se il soggetto non è ancora legato all\u2019azienda visualizzare
                  //unicamente il codice fiscale del soggetto.
                  if (legatoAzienda!=null && legatoAzienda.booleanValue())
                  {
                    htmpl.set("blkpersona.asterisco","(*)");
                    htmpl.set("blkpersona.presente","presente");
                  }

                  //Ruolo		PersonaRIInfoc.progrOridneVisura
                  htmpl.set("blkpersona.progrOrdineVisura",""+personaRIInfoc.getProgrOrdineVisura());

                  //Ruolo  descrizione	CaricaPersonaInfoc.descrCarica
                  htmpl.set("blkpersona.descrCarica",caricaPersonaInfoc.get(j).getDescrCarica().getValue());

                  //Cognome	PersonaRIInfoc.Cognome
                  htmpl.set("blkpersona.cognome",personaRIInfoc.getCognome().getValue());

                  //Nome	PersonaRIInfoc.Nome
                  htmpl.set("blkpersona.nome",personaRIInfoc.getNome().getValue());

                  //Sesso	PersonaRIInfoc.Sesso
                  htmpl.set("blkpersona.sesso",personaRIInfoc.getSesso().getValue());

                  //Nascita	Data	PersonaRIInfoc.DataNascita
                  if (personaRIInfoc.getDataNascita().getValue()!=null)
                    htmpl.set("blkpersona.dataNascita",DateUtils.formatDate(DateUtils.convert((personaRIInfoc.getDataNascita().getValue()))));
                  else
                    htmpl.set("blkpersona.dataNascita","");

                  //Nascita Comune/stato	Decodifica di PersonaRIInfoc.CodComuneNascita + tra parentesi
                  //la sigla della provincia 
                  //Se PersonaRIInfoc.DescrStatoNascita è valorizzato visualizzare
                  //solo PersonaRIInfoc.DescrStatoNascita
                  if (personaRIInfoc.getDescrStatoNascita().getValue()!=null && !"".equals(personaRIInfoc.getDescrStatoNascita().getValue()))
                  {
                    htmpl.set("blkpersona.luogoNascita",personaRIInfoc.getDescrStatoNascita().getValue());
                  }
                  else
                  {
                    if ("".equals(descComuneNasc+siglaProvNasc))
                      htmpl.set("blkpersona.luogoNascita","");
                    else
                      htmpl.set("blkpersona.luogoNascita",descComuneNasc+"("+siglaProvNasc+")");
                  }

                  //Inizio ruolo	CaricaPersonaInfoc.dataInizioCarica
                  if (caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()!=null)
                    htmpl.set("blkpersona.dataInizioCarica",DateUtils.formatDate(DateUtils.convert((caricaPersonaInfoc.get(j).getDataInizioCarica().getValue()))));

                  //Fine carica	CaricaPersonaInfoc.dataFineCarica
                  if (caricaPersonaInfoc.get(j).getDataFineCarica().getValue()!=null)
                    htmpl.set("blkpersona.dataFineCarica",DateUtils.formatDate(DateUtils.convert((caricaPersonaInfoc.get(j).getDataFineCarica().getValue()))));
                  
                  htmpl.set("blkpersona.resIndirizzo",resIndirizzo);

                  //Residenza Comune/stato	Decodifica di PersonaRIInfoc.CodComuneRes +
                  //tra parentesi la sigla della provincia PersonaRIInfoc.SiglaProvResidenza
                  //Se PersonaRIInfoc.DescrStatoRes è valorizzato visualizzare solo
                  //PersonaRIInfoc.DescrStatoRes
                  String siglaProvRes= personaRIInfoc.getSiglaProvResidenza().getValue();
                  if (siglaProvRes==null) siglaProvRes="";
                  String descComuneRes="";
                  ComuneVO comuneResidenza=null;
                  try
                  {
                	String istatProv = anagFacadeClient.getIstatProvinciaBySiglaProvincia(personaRIInfoc.getSiglaProvResidenza().getValue());
                	SolmrLogger.debug(this, "-- istatProv ="+istatProv);
                	SolmrLogger.debug(this, "-- codComuneRes ="+personaRIInfoc.getCodComuneRes().getValue());
                    comuneResidenza=anagFacadeClient.getComuneByISTAT(istatProv+personaRIInfoc.getCodComuneRes().getValue());                    
                  }
                  catch(Exception e) {}
                  if (comuneResidenza!=null && comuneResidenza.getDescom()!=null){
                    descComuneRes=comuneResidenza.getDescom();
                    SolmrLogger.debug(this, "-- descComuneRes ="+descComuneRes);
                  }  
                  if (personaRIInfoc.getDescrStatoRes().getValue()!=null && !"".equals(personaRIInfoc.getDescrStatoRes().getValue()))
                  {
                    htmpl.set("blkpersona.luogoResidenza",personaRIInfoc.getDescrStatoRes().getValue());
                  }
                  else
                  {
                    if ("".equals(descComuneRes+siglaProvRes))
                      htmpl.set("blkpersona.luogoResidenza","");
                    else
                      htmpl.set("blkpersona.luogoResidenza",descComuneRes+"("+siglaProvRes+")");
                  }
                }
              }
            }
          }
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



<%!

  private void impostaRossoPerTutti(Htmpl htmpl)
  {
    htmpl.set("blkpersona.codiceFiscaleColor","rosso");
    htmpl.set("blkpersona.descrCaricaColor","rosso");
    htmpl.set("blkpersona.cognomeColor","rosso");
    htmpl.set("blkpersona.nomeColor","rosso");
    htmpl.set("blkpersona.sessoColor","rosso");
    htmpl.set("blkpersona.dataNascitaColor","rosso");
    htmpl.set("blkpersona.luogoNascitaColor","rosso");
    htmpl.set("blkpersona.dataInizioCaricaColor","rosso");
    htmpl.set("blkpersona.dataFineCaricaColor","rosso");
    htmpl.set("blkpersona.luogoResidenzaColor","rosso");
    htmpl.set("blkpersona.resIndirizzoColor","rosso");
  }

  private void impostaNeroPerTutti(Htmpl htmpl)
  {
    htmpl.set("blkpersona.codiceFiscaleColor","nero");
    htmpl.set("blkpersona.descrCaricaColor","nero");
    htmpl.set("blkpersona.cognomeColor","nero");
    htmpl.set("blkpersona.nomeColor","nero");
    htmpl.set("blkpersona.sessoColor","nero");
    htmpl.set("blkpersona.dataNascitaColor","nero");
    htmpl.set("blkpersona.luogoNascitaColor","nero");
    htmpl.set("blkpersona.dataInizioCaricaColor","nero");
    htmpl.set("blkpersona.dataFineCaricaColor","nero");
    htmpl.set("blkpersona.luogoResidenzaColor","nero");
    htmpl.set("blkpersona.resIndirizzoColor","nero");
  }

  private boolean confrontaStr(String str1,String str2)
  {
    if (str1==null)
    {
      if (str2==null || "".equals(str2.trim())) return true;
      else return false;
    }
    else
    {
      if ("".equals(str1.trim()))
      {
        if (str2==null || "".equals(str2.trim())) return true;
        else return false;
      }
      else
      {
        if (str2==null) return false;
        if (eliminaSpazi(str1.trim()).equalsIgnoreCase(eliminaSpazi(str2.trim()))) return true;
      }
      return false;
    }
  }

  private String eliminaSpazi(String str)
  {
    StringBuffer result=new StringBuffer();
    if (str==null || str.equals("")) return "";
    int size=str.length();
    char old=str.charAt(0);
    boolean spazio=false;
    if (old==' ') spazio=true;
    if (!spazio) result.append(old);
    for (int i=1;i<size;i++)
    {
       if (!(spazio && str.charAt(i)==' ')) result.append(str.charAt(i));
       old=str.charAt(i);
       if (old==' ') spazio=true;
       else spazio=false;
    }
    return result.toString();
  }
%>

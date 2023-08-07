<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>

<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/allevamenti_mod.htm");

  AnagFacadeClient client = new AnagFacadeClient(); 
  GaaFacadeClient clientGaa = GaaFacadeClient.getInstance();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Date parametroAllSuGnps = (Date)request.getAttribute("parametroAllSuGnps");
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" " + "title=\"{2}\" border=\"0\"></a>";

  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  //Per evitare null pointer
  HtmplUtil.setErrors(htmpl, errors, request, application);
	
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String regime = request.getParameter("regimeModificaAll");

  htmpl.set("denominazione", anagVO.getDenominazione());
  if(anagVO.getCUAA()!=null&&!anagVO.getCUAA().equals(""))
  htmpl.set("CUAA", anagVO.getCUAA()+" - ");
  htmpl.set("dataSituazioneAlStr",anagVO.getDataSituazioneAlStr());

  AllevamentoAnagVO all = (AllevamentoAnagVO)session.getAttribute("allevamentoMod");
  
  //popolo vettori per confronto capi
  Vector<String> vCuaaPossDet = (Vector<String>)session.getAttribute("vCuaaPossDet");
  Vector<String> vCuaaPossProp = (Vector<String>)session.getAttribute("vCuaaPossProp");
  for(int i=0;i<vCuaaPossDet.size();i++)
  {
    htmpl.newBlock("blkCodFiscDet");
    htmpl.set("blkCodFiscDet.indexDet", ""+i);
    htmpl.set("blkCodFiscDet.codFiscDetVal", vCuaaPossDet.get(i));
  }
  
  for(int i=0;i<vCuaaPossProp.size();i++)
  {
    htmpl.newBlock("blkCodFiscProp");
    htmpl.set("blkCodFiscProp.indexProp", ""+i);
    htmpl.set("blkCodFiscProp.codFiscPropVal", vCuaaPossProp.get(i));
  }
 
  
  

  //Non posso modificare alcuni campi della pagina
  boolean flagDisabledBdn = false;
  if (!all.getTipoSpecieAnimaleAnagVO().isFlagMofCodAzZoot())
  {
    //htmpl.set("disabledCodZoo", "disabled");
    flagDisabledBdn = true;
  }

  //valorizzo la combo dell'UTE - START
  Vector<UteVO> UTE = client.getElencoIdUTEByIdAzienda(anagVO.getIdAzienda());
  for(int i = 0; i<UTE.size(); i++) 
  {
    UteVO uteVO = (UteVO)UTE.get(i);
    htmpl.newBlock("blkComboUTE");
    htmpl.set("blkComboUTE.idUTE", uteVO.getIdUte().toString());
    if(Validator.isNotEmpty(uteVO.getIndirizzo())) 
    {
      htmpl.set("blkComboUTE.UTEDesc", uteVO.getComune()+" - "+uteVO.getIndirizzo());
    }
    else 
    {
      htmpl.set("blkComboUTE.UTEDesc", uteVO.getComune());
    }
    if((all != null && all.getIdUTE() != null && all.getIdUTE().equals(uteVO.getIdUte().toString()))
       || (request.getParameter("idUTE")!=null && uteVO.getIdUte().toString().equals((String)request.getParameter("idUTE"))))
    {
      htmpl.set("blkComboUTE.selected", "selected");
    }
  }
  //valorizzo la combo dell'UTE - END
  
  if(request.getParameter("indirizzo") != null)
  {
    htmpl.set("indirizzo", request.getParameter("indirizzo"));
  }
  else
  {
    htmpl.set("indirizzo", all.getIndirizzo());
  }
  
  if(request.getParameter("cap") != null)
  {
    htmpl.set("cap", request.getParameter("cap"));
  }
  else
  {
    htmpl.set("cap", all.getCap());
  }
  
  if(request.getParameter("telefono") != null)
  {
    htmpl.set("telefono", request.getParameter("telefono"));
  }
  else
  {
    htmpl.set("telefono", all.getTelefono());
  }
  
  
  if(Validator.isNotEmpty(regime))
  {
    String codiceAziendaZootecnicaDb = request.getParameter("codiceAziendaZootecnicaDb");
    htmpl.set("codiceAziendaZootecnicaDb", codiceAziendaZootecnicaDb);
	  if(Validator.isNotEmpty(request.getParameter("codiceAziendaZootecnica")))
	  {
	    if(flagDisabledBdn
	      && Validator.isNotEmpty(codiceAziendaZootecnicaDb))
	    {
	      htmpl.set("blkHiddenCodZooAzZoo.codiceAziendaZootecnica", request.getParameter("codiceAziendaZootecnica").toUpperCase());
	      htmpl.set("disabledCodZooAzZoo", "disabled");
	    }
	    htmpl.set("codiceAziendaZootecnica", request.getParameter("codiceAziendaZootecnica").toUpperCase());
	  }
	}
  else
  { 
    if(Validator.isNotEmpty(all.getCodiceAziendaZootecnica()))
    {
      if (flagDisabledBdn)
      {
        htmpl.set("blkHiddenCodZooAzZoo.codiceAziendaZootecnica", all.getCodiceAziendaZootecnica());
        htmpl.set("disabledCodZooAzZoo", "disabled");
      }
      htmpl.set("codiceAziendaZootecnica", all.getCodiceAziendaZootecnica());
      htmpl.set("codiceAziendaZootecnicaDb", all.getCodiceAziendaZootecnica());
    }
  }
  
  if(Validator.isNotEmpty(regime))
  {
    String denominazioneAllevamentoDb = request.getParameter("denominazioneAllevamentoDb");
    htmpl.set("denominazioneAllevamentoDb", denominazioneAllevamentoDb);
    if(Validator.isNotEmpty(request.getParameter("denominazioneAllevamento")))
    {
      if (flagDisabledBdn
        && Validator.isNotEmpty(denominazioneAllevamentoDb))
      {
        htmpl.set("blkHiddenCodZooDenom.denominazioneAllevamento", request.getParameter("denominazioneAllevamento").toUpperCase());
        htmpl.set("disabledCodZooDenom", "disabled");
      }
      htmpl.set("denominazioneAllevamento", request.getParameter("denominazioneAllevamento").toUpperCase());
    }
  }
  else
  {
    if(Validator.isNotEmpty(all.getDenominazione()))
    {
      if (flagDisabledBdn)
      {
        htmpl.set("blkHiddenCodZooDenom.denominazioneAllevamento", all.getDenominazione());
        htmpl.set("disabledCodZooDenom", "disabled");
      }
      htmpl.set("denominazioneAllevamento", all.getDenominazione());
      htmpl.set("denominazioneAllevamentoDb", all.getDenominazione());
    }
  }
  
  if(Validator.isNotEmpty(regime))
  {
    String dataInizioAttivitaDb = request.getParameter("dataInizioAttivitaDb");
    htmpl.set("dataInizioAttivitaDb", dataInizioAttivitaDb);
	  if(Validator.isNotEmpty(request.getParameter("dataInizioAttivita")))
	  {
	    if (flagDisabledBdn
	      && Validator.isNotEmpty(dataInizioAttivitaDb))
	    {
	      htmpl.set("blkHiddenCodZooDataInizio.dataInizioAttivita", request.getParameter("dataInizioAttivita"));
	      htmpl.set("disabledCodZooDataInizio", "disabled");
	    }
	    htmpl.set("dataInizioAttivita", request.getParameter("dataInizioAttivita"));
	  }
	}
  else
  { 
    if(Validator.isNotEmpty(all.getDataInizioAttivita()))
    {
      if (flagDisabledBdn)
      {
        htmpl.set("blkHiddenCodZooDataInizio.dataInizioAttivita", all.getDataInizioAttivita());
        htmpl.set("disabledCodZooDataInizio", "disabled");
      }
      htmpl.set("dataInizioAttivita", all.getDataInizioAttivita());
      htmpl.set("dataInizioAttivitaDb", all.getDataInizioAttivita());
    }
  }
  
  if(Validator.isNotEmpty(regime))
  {
    String longitudineDb = request.getParameter("longitudineDb");
    htmpl.set("longitudineDb", longitudineDb);
	  if(Validator.isNotEmpty(request.getParameter("longitudine")))
	  {
	    if (flagDisabledBdn
	      && Validator.isNotEmpty(longitudineDb))
	    {
	      htmpl.set("blkHiddenCodZooLong.longitudine", request.getParameter("longitudine"));
	      htmpl.set("disabledCodZooLong", "disabled");
	    }
	    htmpl.set("longitudine", request.getParameter("longitudine"));
	  }
	}
  else
  { 
	  if(Validator.isNotEmpty(all.getLongitudine()))
	  {
	    if (flagDisabledBdn)
	    {
	      htmpl.set("blkHiddenCodZooLong.longitudine", Formatter.formatDouble6(all.getLongitudine()));
	      htmpl.set("disabledCodZooLong", "disabled");
	    }
	    htmpl.set("longitudine", Formatter.formatDouble6(all.getLongitudine()));
	    htmpl.set("longitudineDb", Formatter.formatDouble6(all.getLongitudine()));
	  }
	}
  
  if(Validator.isNotEmpty(regime))
  {
    String latitudineDb = request.getParameter("latitudineDb");
    htmpl.set("latitudineDb", latitudineDb);
	  if(Validator.isNotEmpty(request.getParameter("latitudine")))
	  {
	    if (flagDisabledBdn
	      && Validator.isNotEmpty(latitudineDb))
	    {
	      htmpl.set("blkHiddenCodZooLat.latitudine", request.getParameter("latitudine"));
	      htmpl.set("disabledCodZooLat", "disabled");
	    }
	    htmpl.set("latitudine", request.getParameter("latitudine"));
	  }
	}
  else
  { 
	  if(Validator.isNotEmpty(all.getLatitudine()))
	  {
	    if (flagDisabledBdn)
	    {
	      htmpl.set("blkHiddenCodZooLat.latitudine", Formatter.formatDouble6(all.getLatitudine()));
	      htmpl.set("disabledCodZooLat", "disabled");
	    }
	    htmpl.set("latitudine", Formatter.formatDouble6(all.getLatitudine()));
	    htmpl.set("latitudineDb", Formatter.formatDouble6(all.getLatitudine()));
	  }
	}
  
  

  //valorizzo la combo dell'ASL - START
  Vector<TipoASLAnagVO> ASL = client.getTipiASL();
  for(int i = 0; i<ASL.size(); i++)
  {
    TipoASLAnagVO aslVO = (TipoASLAnagVO)ASL.get(i);

    htmpl.newBlock("blkIdASL");
    htmpl.set("blkIdASL.idASL", aslVO.getIdASL());
    htmpl.set("blkIdASL.ASLDesc", aslVO.getDescrizione());
    if((all != null && all.getIdASL() != null && all.getIdASL().equals(aslVO.getIdASL()))
       || (request.getParameter("idASL")!=null && aslVO.getIdASL().equals((String)request.getParameter("idASL"))))
    {
      htmpl.set("blkIdASL.selected", "selected");
    }
  }
  //valorizzo la combo dell'ASL - END


  //valorizzo la combo dei Tipo di Produzione - START
  
  String idTipoProduzioneStr = null;
  if(Validator.isNotEmpty(regime))
  {
    String idTipoProduzioneDb = request.getParameter("idTipoProduzioneDb");
    htmpl.set("idTipoProduzioneDb", idTipoProduzioneDb);
	  if(Validator.isNotEmpty(request.getParameter("idTipoProduzione")))
	  {
	    if (flagDisabledBdn
	      && Validator.isNotEmpty(idTipoProduzioneDb))
	    {
	      htmpl.set("blkHiddenCodZooTipoProd.idTipoProduzione", request.getParameter("idTipoProduzione"));
	      htmpl.set("disabledCodZooTipoProd", "disabled");
	    } 
	    idTipoProduzioneStr = request.getParameter("idTipoProduzione");
	  }
	}
  else
  { 
	  if(Validator.isNotEmpty(all.getIdTipoProduzione()))
	  {
	    if (flagDisabledBdn)
	    {
	      htmpl.set("blkHiddenCodZooTipoProd.idTipoProduzione", all.getIdTipoProduzione());
	      htmpl.set("disabledCodZooTipoProd", "disabled");
	    }	    
	    idTipoProduzioneStr = all.getIdTipoProduzione();
	    htmpl.set("idTipoProduzioneDb", all.getIdTipoProduzione());
	  }
	}
  
  if(all.getIdSpecieAnimale()!=null)
  {
	  Vector<CodeDescription> tipoProduzione = client.getTipoTipoProduzione(new Long(all.getIdSpecieAnimale()).longValue());
	
	  for(int i = 0; i<tipoProduzione.size(); i++) 
	  {
	    CodeDescription cd = (CodeDescription)tipoProduzione.get(i);
	    htmpl.newBlock("blkComboTipoProduzione");
	    htmpl.set("blkComboTipoProduzione.idTipoProduzione", cd.getCode().toString());
	    htmpl.set("blkComboTipoProduzione.descTipoProduzione", cd.getDescription());
	    if(Validator.isNotEmpty(idTipoProduzioneStr) && cd.getCode().toString().equals(idTipoProduzioneStr))
	    {
	      htmpl.set("blkComboTipoProduzione.selected", "selected");
	    }
	
	  }
	}
  //valorizzo la combo dei Tipo di Produzione - END


  //valorizzo la combo degli Orientamenti - START  
  String idOrientamentoProduttivoStr = null;
  if(Validator.isNotEmpty(regime))
  {
    String idOrientamentoProduttivoDb = request.getParameter("idOrientamentoProduttivoDb");
    htmpl.set("idOrientamentoProduttivoDb", idOrientamentoProduttivoDb);
	  if(request.getParameter("idOrientamentoProduttivo") !=null)
	  {
	    if (flagDisabledBdn
	      && Validator.isNotEmpty(idOrientamentoProduttivoDb))
	    {
	      htmpl.set("blkHiddenCodZooOrProd.idOrientamentoProduttivo", request.getParameter("idOrientamentoProduttivo"));
	      htmpl.set("disabledCodZooOrProd", "disabled");
	    }
	    idOrientamentoProduttivoStr = request.getParameter("idOrientamentoProduttivo");
	  }
	}
  else
  { 
	  if(Validator.isNotEmpty(all.getIdOrientamentoProduttivo()))
	  {
	    if (flagDisabledBdn)
	    {
	      htmpl.set("blkHiddenCodZooOrProd.idOrientamentoProduttivo", all.getIdOrientamentoProduttivo());
	      htmpl.set("disabledCodZooOrProd", "disabled");
	    }	    
	    idOrientamentoProduttivoStr = all.getIdOrientamentoProduttivo();
	    htmpl.set("idOrientamentoProduttivoDb", all.getIdOrientamentoProduttivo());
	  }
	}
  
  
  
  if ((all.getIdTipoProduzione()!=null) && all.getIdSpecieAnimale()!=null)
  {
    try
    {
      long idTipoProduzione=0,idSpecie=0;
      idSpecie=Long.parseLong(all.getIdSpecieAnimale());
      idTipoProduzione=Long.parseLong(all.getIdTipoProduzione());
      Vector<CodeDescription> orientamenti = client.getOrientamentoProduttivo(idSpecie,idTipoProduzione);
      for(int i = 0; i<orientamenti.size(); i++) 
      {
        CodeDescription cd = (CodeDescription)orientamenti.get(i);
        htmpl.newBlock("blkComboOrientamentoProduttivo");
        htmpl.set("blkComboOrientamentoProduttivo.idOrientamentoProduttivo", cd.getCode().toString());
        htmpl.set("blkComboOrientamentoProduttivo.descOrientamentoProduttivo", cd.getDescription());
        if(Validator.isNotEmpty(idOrientamentoProduttivoStr) && cd.getCode().toString().equals(idOrientamentoProduttivoStr))
        {
          htmpl.set("blkComboOrientamentoProduttivo.selected", "selected");
          
        }
      }
    }
    catch(Exception e){}
  }
  //valorizzo la combo degli Orientamenti - END
  
  
  
  //valorizzo la combo dei tipi produzione COSMAN - START 
  String idTipoProduzioneCosmanStr = null;
  if(Validator.isNotEmpty(regime))
  {
    idTipoProduzioneCosmanStr = request.getParameter("idTipoProduzioneCosman");
  }
  else
  { 
    if(Validator.isNotEmpty(all.getIdTipoProduzioneCosman()))
    {
      idTipoProduzioneCosmanStr = all.getIdTipoProduzioneCosman();
    }
  }
  
  
  if(Validator.isNotEmpty(all.getIdTipoProduzione()) 
    && Validator.isNotEmpty(all.getIdSpecieAnimale())
    && Validator.isNotEmpty(all.getIdOrientamentoProduttivo()))
  {
    try
    {
      long idTipoProduzione=0,idSpecie=0,idOrientamentoProduttivo=0;
      idSpecie=Long.parseLong(all.getIdSpecieAnimale());
      idTipoProduzione=Long.parseLong(all.getIdTipoProduzione());
      idOrientamentoProduttivo=Long.parseLong(all.getIdOrientamentoProduttivo());
      Vector<CodeDescription> tipoCosman = client.getTipoProduzioneCosman(idSpecie, idTipoProduzione, idOrientamentoProduttivo);
      for(int i = 0; i<tipoCosman.size(); i++) 
      {
        CodeDescription cd = (CodeDescription)tipoCosman.get(i);
        htmpl.newBlock("blkComboTipoProduzioneCosman");
        htmpl.set("blkComboTipoProduzioneCosman.idTipoProduzioneCosman", cd.getCode().toString());
        htmpl.set("blkComboTipoProduzioneCosman.descTipoProduzioneCosman", cd.getDescription());
        if(Validator.isNotEmpty(idTipoProduzioneCosmanStr) && cd.getCode().toString().equals(idTipoProduzioneCosmanStr))
        {
          htmpl.set("blkComboTipoProduzioneCosman.selected", "selected");
          
        }
      }
    }
    catch(Exception e){}
  }
  //valorizzo la combo tipi produzione - END


  htmpl.set("idAllevamento", all.getIdAllevamento()); 
  
  
  if(Validator.isNotEmpty(regime))
  {
    String codiceFiscaleProprietario = request.getParameter("codiceFiscaleProprietario");
    if(Validator.isNotEmpty(codiceFiscaleProprietario))
      htmpl.set("codiceFiscaleProprietario", codiceFiscaleProprietario.toUpperCase());
	}
  else
  { 
	  if(Validator.isNotEmpty(all.getCodiceFiscaleProprietario()))
	  {
	    htmpl.set("codiceFiscaleProprietario", all.getCodiceFiscaleProprietario());
	  }
	}
  
  if(Validator.isNotEmpty(regime))
  {
    String denominazioneProprietario = request.getParameter("denominazioneProprietario");
	  if(Validator.isNotEmpty(denominazioneProprietario))
	  {
	    htmpl.set("denominazioneProprietario", denominazioneProprietario.toUpperCase());
	  }
	}
  else
  { 
    if(Validator.isNotEmpty(all.getDenominazioneProprietario()))
    {
      htmpl.set("denominazioneProprietario", all.getDenominazioneProprietario());
    }
  }
  
  if(Validator.isNotEmpty(regime))
  {
    String codiceFiscaleDetentore = request.getParameter("codiceFiscaleDetentore");
    if(Validator.isNotEmpty(codiceFiscaleDetentore))
    {
      htmpl.set("codiceFiscaleDetentore", codiceFiscaleDetentore.toUpperCase());
    }
  }
  else
  { 
    if(Validator.isNotEmpty(all.getCodiceFiscaleDetentore()))
    {
      htmpl.set("codiceFiscaleDetentore", all.getCodiceFiscaleDetentore());
    }
  }
  
  if(Validator.isNotEmpty(regime))
  {
    String denominazioneDetentore = request.getParameter("denominazioneDetentore");
    if(Validator.isNotEmpty(denominazioneDetentore))
    {
      htmpl.set("denominazioneDetentore", denominazioneDetentore.toUpperCase());
    }
  }
  else
  { 
    if(Validator.isNotEmpty(all.getDenominazioneDetentore()))
    {
      htmpl.set("denominazioneDetentore", all.getDenominazioneDetentore());
    }
  }
  
  if(Validator.isNotEmpty(regime))
  {
    String dataInizioDetenzioneDb = request.getParameter("dataInizioDetenzioneDb");
    htmpl.set("dataInizioDetenzioneDb", dataInizioDetenzioneDb);
    if(Validator.isNotEmpty(request.getParameter("dataInizioDetenzione")))
    {
      if (flagDisabledBdn
        && Validator.isNotEmpty(dataInizioDetenzioneDb))
      {
        htmpl.set("blkHiddenCodZooDataInizioDet.dataInizioDetenzione", request.getParameter("dataInizioDetenzione"));
        htmpl.set("disabledCodZooDataInizioDet", "disabled");
      }
      htmpl.set("dataInizioDetenzione", request.getParameter("dataInizioDetenzione"));
    }
  }
  else
  { 
    if(Validator.isNotEmpty(all.getDataInizioDetenzione()))
    {
      if (flagDisabledBdn)
      {
        htmpl.set("blkHiddenCodZooDataInizioDet.dataInizioDetenzione", all.getDataInizioDetenzione());
        htmpl.set("disabledCodZooDataInizioDet", "disabled");
      }
      htmpl.set("dataInizioDetenzione", all.getDataInizioDetenzione());
      htmpl.set("dataInizioDetenzioneDb", all.getDataInizioDetenzione());
    }
  }
  
  if(Validator.isNotEmpty(regime))
  {
    String dataFineDetenzioneDb = request.getParameter("dataFineDetenzioneDb");
    htmpl.set("dataFineDetenzioneDb", dataFineDetenzioneDb);
    if(Validator.isNotEmpty(request.getParameter("dataFineDetenzione")))
    {
      if (flagDisabledBdn
        && Validator.isNotEmpty(dataFineDetenzioneDb))
      {
        htmpl.set("blkHiddenCodZooDataFineDet.dataFineDetenzione", request.getParameter("dataFineDetenzione").toUpperCase());
        htmpl.set("disabledCodZooDataFineDet", "disabled");
      }
      htmpl.set("dataFineDetenzione", request.getParameter("dataFineDetenzione").toUpperCase());
    }
  }
  else
  { 
    if(Validator.isNotEmpty(all.getDataFineDetenzione()))
    {
      if (flagDisabledBdn)
      {
        htmpl.set("blkHiddenCodZooDataFineDet.dataFineDetenzione", all.getDataFineDetenzione());
        htmpl.set("disabledCodZooDataFineDet", "disabled");
      }
      htmpl.set("dataFineDetenzione", all.getDataFineDetenzione());
      htmpl.set("dataFineDetenzioneDb", all.getDataFineDetenzione());
    }
  }
  


  boolean trovatoComune = false;
  if(Validator.isNotEmpty(regime))
  {
    String descResComuneDb = request.getParameter("descResComuneDb");
    htmpl.set("descResComuneDb", descResComuneDb);
	  if(Validator.isNotEmpty(request.getParameter("descResComune")))
	  {
	    if (flagDisabledBdn
	      && Validator.isNotEmpty(descResComuneDb))
	    {
	      htmpl.set("blkHiddenCodZooComune.descResComune", request.getParameter("descResComune").toUpperCase());
	      htmpl.set("disabledCodZooComune", "disabled");
	      trovatoComune = true;
	    }
	    htmpl.set("descResComune", request.getParameter("descResComune").toUpperCase());
	  }
	}
  else
  { 
    if(Validator.isNotEmpty(all.getComuneVO().getDescom()))
	  {
	    if (flagDisabledBdn)
	    {
	      htmpl.set("blkHiddenCodZooComune.descResComune", all.getComuneVO().getDescom());
	      htmpl.set("disabledCodZooComune", "disabled");
	      trovatoComune = true;
	    }
	    htmpl.set("descResComune", all.getComuneVO().getDescom());
	    htmpl.set("descResComuneDb", all.getComuneVO().getDescom());
	  }
	}
  
  if(Validator.isNotEmpty(regime))
  {
    String resProvinciaDb = request.getParameter("resProvinciaDb");
    htmpl.set("resProvinciaDb", resProvinciaDb);
	  if(Validator.isNotEmpty(request.getParameter("resProvincia")))
	  {
	    if (flagDisabledBdn
	      && Validator.isNotEmpty(resProvinciaDb))
	    {
	      htmpl.set("blkHiddenCodZooProv.resProvincia", request.getParameter("resProvincia").toUpperCase());
	      htmpl.set("disabledCodZooProv", "disabled");
	    }
	    htmpl.set("resProvincia", request.getParameter("resProvincia").toUpperCase());
	  }
	}
  else
  { 
	  if(Validator.isNotEmpty(all.getComuneVO().getSiglaProv()))
	  {
	    if (flagDisabledBdn)
	    {
	      htmpl.set("blkHiddenCodZooProv.resProvincia", all.getComuneVO().getSiglaProv());
	      htmpl.set("disabledCodZooProv", "disabled");
	    }
	    htmpl.set("resProvincia", all.getComuneVO().getSiglaProv());
	    htmpl.set("resProvinciaDb", all.getComuneVO().getSiglaProv());
	  }
	}
  
  if (!flagDisabledBdn && !trovatoComune)
  {
    htmpl.newBlock("blkModificaComune");
  }  
  
  

  if(all.getTipoSpecieAnimaleAnagVO() != null 
    && Validator.isNotEmpty(all.getTipoSpecieAnimaleAnagVO().getDescrizione())) 
  {
    htmpl.set("descSpecie", all.getTipoSpecieAnimaleAnagVO().getDescrizione());
  }
  else 
  {
    htmpl.set("descSpecie", request.getParameter("descSpecie"));
  }
  htmpl.set("idSpecie", all.getIdSpecieAnimale());
  
  
  if(parametroAllSuGnps.after(new Date()))
  {
    htmpl.newBlock("blkLettiera");
	  String superficieLettieraPermanenteStr = "";
	  if(request.getParameter("superficieLettieraPermanente")!=null)
	  {
	    htmpl.set("blkLettiera.superficieLettieraPermanente", request.getParameter("superficieLettieraPermanente"));
	    superficieLettieraPermanenteStr = request.getParameter("superficieLettieraPermanente");
	  }
	  else if(all.getSuperficieLettieraPermanente() != null)
	  {
	    htmpl.set("blkLettiera.superficieLettieraPermanente", all.getSuperficieLettieraPermanente());
	    superficieLettieraPermanenteStr = all.getSuperficieLettieraPermanente();
	  }
	  
	  
	  htmpl.bset("blkLettiera.altezzaDefault", ""+all.getAltLettieraPermMin());
	  
	  String altezzaStr = "";
	  if (!Validator.isNotEmpty(all.getSuperficieLettieraPermanente()))
	  {
	    htmpl.set("blkLettiera.altReadOnly", "READONLY", null);
	    htmpl.set("blkLettiera.altezza", "");
	  }
	  else
	  {
	    if(request.getParameter("altezza")!=null)
		  {
		    htmpl.set("blkLettiera.altezza", request.getParameter("altezza"));
		    altezzaStr = request.getParameter("altezza");
		  }
		  else if(all.getAltezzaLettieraPermanente() != null)
		  {
		    htmpl.set("blkLettiera.altezza", all.getAltezzaLettieraPermanente());
		    altezzaStr = all.getAltezzaLettieraPermanente();
		  }
	  }
	  
	  //Calcolo il volume
	  
	  try
	  {
	    BigDecimal b1=new BigDecimal(superficieLettieraPermanenteStr.replace(',','.'));
	    BigDecimal b2=new BigDecimal(altezzaStr.replace(',','.'));
	    b1=b1.multiply(b2);
	    String volume=StringUtils.parseDoubleFieldTwoDecimal(b1.toString());
	    htmpl.set("blkLettiera.volume",volume);
	  }
	  catch(Exception e) {}
	}
  
  
  htmpl.set("note", all.getNote());
  htmpl.set("dal", all.getDataInizio());
  htmpl.set("al", all.getDataFine());
  
  if (all.getIdSpecieAnimale()!=null)
  {
    /******************************************************************************/
    /******************************************************************************/
    /****        INIZIO PARTE RELATIVA ALLA CONSISTENZA ZOOTECNICA             ****/
    /******************************************************************************/
    /******************************************************************************/
  
  
    if (all.getTipoSpecieAnimaleAnagVO().isFlagStallaPascolo())
      htmpl.bset("flagStallaPascolo", "S");
  
    String idSpecie = all.getIdSpecieAnimale();
    String idCategoria = request.getParameter("idCategoria");
    String idSottoCategoria = request.getParameter("idSottoCategoria");
      
    String unitaMisuraSpecie = all.getTipoSpecieAnimaleAnagVO().getUnitaDiMisura();
  
  
    //Se c'è una specie selezionata popolo la combo delle categorie
    Vector<TipoCategoriaAnimaleAnagVO> categorie = null;

    try
    {
      categorie = client.getCategorieByIdSpecie(new Long(idSpecie));
    }
    catch (SolmrException e)
    {
    }

    if (categorie != null)
    {
      int sizeCategorie = categorie.size();
      for (int i = 0; i < sizeCategorie; i++)
      {
        TipoCategoriaAnimaleAnagVO categoria = (TipoCategoriaAnimaleAnagVO) categorie.get(i);
        htmpl.newBlock("blkComboCategoria");
        htmpl.set("blkComboCategoria.idCategoria", "" + categoria.getIdCategoriaAnimale());
        if (categoria.getIdCategoriaAnimale().toString().equals(idCategoria))
        {
          htmpl.bset("descCategoria", categoria.getDescrizioneCategoriaAnimale());
          htmpl.set("blkComboCategoria.selected", "selected");
        }
        htmpl.set("blkComboCategoria.categoriaDesc", categoria.getDescrizioneCategoriaAnimale());
      }
    }

    if (idCategoria != null && !"".equals(idCategoria))
    {
      //Se c'è una categoria selezionata popolo la combo delle sottoCategorie
      TipoSottoCategoriaAnimale[] sottoCategorie = null;

      try
      {
        sottoCategorie = clientGaa.getTipiSottoCategoriaAnimale(Long.parseLong(idCategoria));
      }
      catch (SolmrException e)
      {
      }

      if (sottoCategorie != null)
      {
        if (sottoCategorie.length>1)
          htmpl.newBlock("blkDefault");
        for (int i = 0; i < sottoCategorie.length; i++)
        {
          htmpl.newBlock("blkComboSottoCategoria");
          htmpl.set("blkComboSottoCategoria.idSottoCategoria", "" + sottoCategorie[i].getIdSottocategoriaAnimale());
          if (("" + sottoCategorie[i].getIdSottocategoriaAnimale()).equals(idSottoCategoria))
            htmpl.set("blkComboSottoCategoria.selected", "selected");
          htmpl.set("blkComboSottoCategoria.sottoCategoriaDesc", sottoCategorie[i].getDescrizione());
        }
      }
    }
    
    //Vado a vedere se ci sono sottocategorie inserite dall'utente
    Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = (Vector<SottoCategoriaAllevamento>) session.getAttribute("sottoCategorieAllevamenti");
    if (sottoCategorieAllevamenti != null && sottoCategorieAllevamenti.size() > 0)
    {
      htmpl.newBlock("blksottoCategorieAllevamenti");
      htmpl.newBlock("blkLegendaAllevamenti");

      htmpl.set("blksottoCategorieAllevamenti.unitaMisuraSpecie", unitaMisuraSpecie);

      int size = sottoCategorieAllevamenti.size();
      //Devo visualizzare tante categorie quante sono presenti nel vettore
      for (int i = 0; i < size; i++)
      {
        SottoCategoriaAllevamento sottoCategoria = sottoCategorieAllevamenti.get(i);
        htmpl.newBlock("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti");

        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.idCategorieTab", "" + i);
        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.idSottoCategorieJS", ""+sottoCategoria.getIdSottoCategoriaAnimale() );
        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.descCategoriaTab", sottoCategoria.getDescCategoriaAnimale());
        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.descSottoCategoriaTab", sottoCategoria.getDescSottoCategoriaAnimale());
        
        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.capiTab", sottoCategoria.getQuantita());
        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.capiProprietaTab", sottoCategoria.getQuantitaProprieta());
        
       
        
        
        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.pesoVivoTab", sottoCategoria.getPesoVivo());

       
	      //Calcolo il peso vivo totale e azoto totale
	      try
	      {
	        long quantita = Long.parseLong(sottoCategoria.getQuantita());
	        if(quantita == 0)
	        {
	          quantita = Long.parseLong(sottoCategoria.getQuantitaProprieta());
	        }
	        double pesoVivo = Double.parseDouble(sottoCategoria.getPesoVivo().replace(',', '.'));
	        double pesoVivoAzoto = sottoCategoria.getPesoVivoAzoto();
	        double ris = pesoVivo * quantita;
	        double risAzoto = ris * pesoVivoAzoto / 1000;
	        BigDecimal risAzotoBg = new BigDecimal(risAzoto);
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.pesoVivoTotTab", StringUtils.parsePesoCapi("" + ris));
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.azotoZootecnicoTab", Formatter.formatAndRoundBigDecimal1(risAzotoBg));
	      }
	      catch (Exception e)
	      {
	        if (sottoCategoria.getQuantita() == null || "".equals(sottoCategoria.getQuantita()) || sottoCategoria.getPesoVivo() == null || "".equals(sottoCategoria.getPesoVivo()))
	        {
	          htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.pesoVivoTotTab", "");
	          htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.azotoZootecnicoTab", "");
	        }
	        else
	        {
	          htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.pesoVivoTotTab", "NaN");
	          htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.azotoZootecnicoTab", "NaN");
	        }
	      }
          
          
	      if (sottoCategoria.isFlagStallaPascolo())
	      {
	
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.numeroCicliAnnualiTab", sottoCategoria.getNumeroCicliAnnuali());
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.cicliTab", sottoCategoria.getCicli());
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.pesoVivoAzotoTab", StringUtils.parsePesoCapi("" + sottoCategoria.getPesoVivoAzoto()));
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.giorniPascoloEstateTab", sottoCategoria.getGiorniPascoloEstate());
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.orePascoloEstateTab", sottoCategoria.getOrePascoloEstate());
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.giorniPascoloInvernoTab", sottoCategoria.getGiorniPascoloInverno());
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.orePascoloInvernoTab", sottoCategoria.getOrePascoloInverno());
	      }
	      else
	      {
	        htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.tabConReadOnly", "READONLY='READONLY'", null);
	      }
	      //Visualizzazione degli errori dovuti alla consistenza
	      //vado a scorrere un array di doppie stringhe:
	      //una contiene il nome del segnaposto
	      //l'altra contiene il valore dell'errore
	      if (sottoCategoria.getErrori() != null)
	      {
	        for (int j = 0; j < sottoCategoria.getErrori().length; j++)
	        {
	          String segnaPosto = sottoCategoria.getErrori()[j][0];
	          String errore = sottoCategoria.getErrori()[j][1];
	          htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti." + segnaPosto, MessageFormat.format(htmlStringKO, new Object[]
	          { pathErrori + "/" + imko, "'" + jssp.process(errore) + "'", errore }), null);
	        }
	        //Ripulisco gli errori per evitare che vengano riproposti
	        //ai reload sucessivi
	        sottoCategoria.setErrori(null);
	      }
	    }
	    //memorizzo in sessione la cancellazione degli errori
	    session.setAttribute("sottoCategorieAllevamenti", sottoCategorieAllevamenti);
	  }
  
  
	  /******************************************************************************/
	  /******************************************************************************/
	  /****          FINE PARTE RELATIVA ALLA CONSISTENZA ZOOTECNICA             ****/
	  /******************************************************************************/
	  /******************************************************************************/
	    
	    
	  /******************************************************************************/
	  /******************************************************************************/
	  /****               INIZIO PARTE RELATIVA ALLE STABULAZIONI                ****/
	  /******************************************************************************/
	  /******************************************************************************/
	  //Le stabulazioni hanno senso solo se flagStallaPascolo è true
	  if (all.getTipoSpecieAnimaleAnagVO().isFlagStallaPascolo() && parametroAllSuGnps.after(new Date()))
	  {
	    htmpl.newBlock("blkStabulazioniInsert");
	    
	    //Vado a vedere se ci sono stabulazioni inserite dall'utente
	    Vector<StabulazioneTrattamento> stabulazioniTrattamenti = (Vector<StabulazioneTrattamento>) session.getAttribute("stabulazioniTrattamenti");
	    if (stabulazioniTrattamenti != null && stabulazioniTrattamenti.size() > 0)
	    {
	      htmpl.newBlock("blkStabulazioniInsert.blkStabulazioniTrattamenti");
	      htmpl.set("blkStabulazioniInsert.blkStabulazioniTrattamenti.unitaMisuraSpecie", unitaMisuraSpecie);
	
	      int size = stabulazioniTrattamenti.size();
	      //Devo visualizzare tante categorie quante sono presenti nel vettore
	      for (int i = 0; i < size; i++)
	      {
	        StabulazioneTrattamento stabulazione = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
	        String blk = "blkStabulazioniInsert.blkStabulazioniTrattamenti.blkStabulazioneTrattamento";
	        htmpl.newBlock(blk);
	        htmpl.set(blk + ".idStabulazioneTab", "" + i);
	
	        //Popolo la combo delle sottocategorie
	        if (sottoCategorieAllevamenti != null && sottoCategorieAllevamenti.size() > 0)
	        {
	          int sottoCategorieSize = sottoCategorieAllevamenti.size();
	          for (int j = 0; j < sottoCategorieSize; j++)
	          {
	            SottoCategoriaAllevamento sottoCategoria = (SottoCategoriaAllevamento) sottoCategorieAllevamenti.get(j);
	            if(Validator.isNotEmpty(sottoCategoria.getQuantita())
	              && Validator.isNotEmpty(sottoCategoria.getQuantitaProprieta())
	              && (!sottoCategoria.getQuantitaProprieta().equalsIgnoreCase("0")
	              || !sottoCategoria.getQuantita().equalsIgnoreCase("0")))
	            {
		            htmpl.newBlock(blk + ".blkComboCatSottocatStab");
		            htmpl.set(blk + ".blkComboCatSottocatStab.idCatSottocatStab", "" + sottoCategoria.getIdSottoCategoriaAnimale());
		            if (stabulazione.getIdSottoCategoriaAnimale() != null && stabulazione.getIdSottoCategoriaAnimale().toString().equals("" + sottoCategoria.getIdSottoCategoriaAnimale()))
		              htmpl.set(blk + ".blkComboCatSottocatStab.selected", "selected");
		            String desc = sottoCategoria.getDescCategoriaAnimale();
		            //Se la sottocategoria non è fittizia devo visualizzare anche la sua descrizione
		            if (!sottoCategoria.isFlagSottocatFittizia())
		              desc += " - " + sottoCategoria.getDescSottoCategoriaAnimale();
		            htmpl.set(blk + ".blkComboCatSottocatStab.catSottocatStabDesc", desc);
		          }
	
	          }
	        }
	
	        SottoCategoriaAllevamento sottoCategoriaCalcoli = null;
	        if (Validator.isNotEmpty(stabulazione.getIdSottoCategoriaAnimale()))
	        {
	          //Se ho selezionato la combo delle sottocategorie popolo la combo
	          //delle stabulazioni
	          BaseCodeDescription[] stabulazioni = clientGaa.getTipiStabulazione(Long.parseLong(stabulazione.getIdSottoCategoriaAnimale()));
	
	          for (int j = 0; j < sottoCategorieAllevamenti.size(); j++)
	          {
	            if(sottoCategorieAllevamenti.get(j).getIdSottoCategoriaAnimale() 
	              == new Long(stabulazione.getIdSottoCategoriaAnimale()).longValue())
	            {
	              sottoCategoriaCalcoli = sottoCategorieAllevamenti.get(j);
	            }           
	          }
	
	          if (stabulazioni != null)
	          {
	            for (int j = 0; j < stabulazioni.length; j++)
	            {
	              htmpl.newBlock(blk + ".blkComboStabulazione");
	              htmpl.set(blk + ".blkComboStabulazione.idStabulazione", "" + stabulazioni[j].getCode());
	              if (stabulazione.getIdStabulazione() != null && stabulazione.getIdStabulazione().toString().equals("" + stabulazioni[j].getCode()))
	                htmpl.set(blk + ".blkComboStabulazione.selected", "selected");
	              htmpl.set(blk + ".blkComboStabulazione.stabulazioneDesc", stabulazioni[j].getDescription());
	            }
	          }
	        }
	
	        
	
	        htmpl.set(blk + ".quantitaStab", stabulazione.getQuantita());
	        htmpl.set(blk + ".palabile", stabulazione.getPalabile());
	        htmpl.set(blk + ".palabileTAnno", stabulazione.getPalabileTAnno());
	        htmpl.set(blk + ".azotoPalabile", stabulazione.getAzotoPalabile());
          htmpl.set(blk + ".nonPalabile", stabulazione.getNonPalabile());
	        htmpl.set(blk + ".azotoNonPalabile", stabulazione.getAzotoNonPalabile());
	        
	        //Calcolo il peso vivo totale e azoto totale
	        try
	        {
	          long quantita = Long.parseLong(stabulazione.getQuantita());
	          double pesoVivo = Double.parseDouble(sottoCategoriaCalcoli.getPesoVivo().replace(',', '.'));
	          double pesoVivoAzoto = sottoCategoriaCalcoli.getPesoVivoAzoto();
	          double risPermanenza = sottoCategoriaCalcoli.ggPermanenzaStalla();
	          double risEscreto = pesoVivo / 1000 * quantita * pesoVivoAzoto * (1-(risPermanenza / 365));
	          
	          htmpl.set(blk + ".permanenzaInStalla", StringUtils.parsePesoCapi("" + risPermanenza));
	          htmpl.set(blk + ".escretoAlPascolo", StringUtils.parsePesoCapi("" + risEscreto));
	        }
	        catch (Exception e)
	        {
	          htmpl.set(blk + ".escretoAlPascolo", "");
	          htmpl.set(blk + ".permanenzaInStalla", "");
	        }    
	        
	       
	        //htmpl.set(blk + ".totaleAzoto", stabulazione.getTotaleAzoto());
	        
	        //Visualizzazione degli errori dovuti alla stabulazione
	        //vado a scorrere un array di doppie stringhe:
	        //una contiene il nome del segnaposto
	        //l'altra contiene il valore dell'errore
	        if (stabulazione.getErrori() != null)
	        {
	          for (int j = 0; j < stabulazione.getErrori().length; j++)
	          {
	            String segnaPosto = stabulazione.getErrori()[j][0];
	            String errore = stabulazione.getErrori()[j][1];
	            htmpl.set(blk + "." + segnaPosto, MessageFormat.format(htmlStringKO, new Object[]
	            { pathErrori + "/" + imko, "'" + jssp.process(errore) + "'", errore }), null);
	          }
	          //Ripulisco gli errori per evitare che vengano riproposti
	          //ai reload sucessivi
	          stabulazione.setErrori(null);
	        }
	      }
	      //memorizzo in sessione la cancellazione degli errori
	      session.setAttribute("stabulazioniTrattamenti", stabulazioniTrattamenti);
	      
	    }
	  }
	  /******************************************************************************/
	  /******************************************************************************/
	  /****               FINE PARTE RELATIVA ALLE STABULAZIONI                  ****/
	  /******************************************************************************/
	  /******************************************************************************/
    
  
    //Parte riservata agli avicoli
    if (AllevamentoAnagVO.ID_AVICOLI.equals(idSpecie) && parametroAllSuGnps.after(new Date()))
    {
      htmpl.newBlock("blkSoloAvicoli");
      if (SolmrConstants.FLAG_S.equals(all.getFlagDeiezioneAvicoli()))
        htmpl.set("blkSoloAvicoli.flagDeiezioneAvicoliCheckedSi", "checked");
      else
        htmpl.set("blkSoloAvicoli.flagDeiezioneAvicoliCheckedNo", "checked");
    }
    /******************************************************************************/
    /******************************************************************************/
    /****        INIZIO PARTE RELATIVA ALLA STRUTTURE DI MUNGITURA             ****/
    /******************************************************************************/
    /******************************************************************************/
  
    //Questa sezione è da prevedere unicamente se nella consistenza zootecnica è 
    //stata definita una specie animali Bovini o Ovini o Caprini.
    if ((AllevamentoAnagVO.ID_SPECIE_BOVINI_CARNE.equals(idSpecie) || AllevamentoAnagVO.ID_SPECIE_BOVINI_ALLEVAMENTO.equals(idSpecie) || AllevamentoAnagVO.ID_SPECIE_OVINI.equals(idSpecie)
        || AllevamentoAnagVO.ID_SPECIE_CAPRINI.equals(idSpecie)) && parametroAllSuGnps.after(new Date()))
    {
      htmpl.newBlock("blkStruttureMungitura");
      String mediaCapiLattazione = "";
      if(request.getParameter("mediaCapiLattazione")!=null)
		  {
		    mediaCapiLattazione = request.getParameter("mediaCapiLattazione");
		  }
		  else if(all.getIstatComuneAllevamento() != null)
		  {
		    mediaCapiLattazione = all.getMediaCapiLattazione();
		  }
      htmpl.set("blkStruttureMungitura.mediaCapiLattazione", mediaCapiLattazione);
      String quantitaAcquaLavaggio = "";
      if(request.getParameter("quantitaAcquaLavaggio")!=null)
      {
        quantitaAcquaLavaggio = request.getParameter("quantitaAcquaLavaggio");
      }
      else if(all.getIstatComuneAllevamento() != null)
      {
        quantitaAcquaLavaggio = all.getQuantitaAcquaLavaggio();
      }
      htmpl.set("blkStruttureMungitura.quantitaAcquaLavaggio", quantitaAcquaLavaggio);
      
      
      if(Validator.isNotEmpty(quantitaAcquaLavaggio) && Validator.isNotEmpty(mediaCapiLattazione))
      {
        try
        {
	        double quantitaAcquaLavaggioDb = Double.parseDouble(quantitaAcquaLavaggio.replace(',', '.'));
	        double mediaCapiLattazioneDb = Double.parseDouble(mediaCapiLattazione);
	        
	        
	        double litriAcquaCapoGiornoDb =  quantitaAcquaLavaggioDb * 1000 / 365  / mediaCapiLattazioneDb;
	        
	        htmpl.set("blkStruttureMungitura.litriAcquaCapoGiorno", StringUtils.parseIntegerField(String.valueOf(NumberUtils.arrotonda(litriAcquaCapoGiornoDb,0))));
	      }
	      catch(Exception e)
	      {
	        htmpl.set("blkStruttureMungitura.litriAcquaCapoGiorno", "");
	      }
      }
      
      
        
         
      
      Vector<AllevamentoAcquaLavaggio> vAllevamentoAcquaLavaggio = (Vector<AllevamentoAcquaLavaggio>)session.getAttribute("vAllevamentoAcquaLavaggio");
      try
	    {
	      Vector<TipoDestinoAcquaLavaggio> vTipoDestinoLavaggio = clientGaa.getElencoDestAcquaLavaggio();
	      if(Validator.isNotEmpty(vTipoDestinoLavaggio))
	      {
	        int size = vTipoDestinoLavaggio.size();
	        boolean first = true;
		      for(int i = 0; i<size; i++) 
		      {
		        htmpl.newBlock("blkStruttureMungitura.blkDestinoAcquaLavaggio");
		        TipoDestinoAcquaLavaggio tipoDestinoAcquaLavaggio = vTipoDestinoLavaggio.get(i);
		        AllevamentoAcquaLavaggio allevamentoAcquaLavaggioSel = null;
		        //Mi cerco se è presente il valore..
		        boolean flagDefault = false;
		        if(vAllevamentoAcquaLavaggio != null)
		        {
		          for(int k=0;k<vAllevamentoAcquaLavaggio.size();k++)
		          {
		            if(vAllevamentoAcquaLavaggio.get(k).getIdDestinoAcquaLavaggio()
		              .compareTo(tipoDestinoAcquaLavaggio.getIdDestinoAcquaLavaggio()) == 0)
		            {
		              allevamentoAcquaLavaggioSel = vAllevamentoAcquaLavaggio.get(k);
		              break;
		            }
		          }
		        }
		        else
		        {
		          flagDefault = true;
		        }
		        
		        String blk = "";
		        if(first)
		        {
		          blk = "blkStruttureMungitura.blkDestinoAcquaLavaggio.blkPrimoAcquaLavaggio";
		          htmpl.newBlock(blk);
		          htmpl.set(blk+".indice", ""+i);
		          htmpl.set(blk+".numDestinoAcqualLavaggio", ""+size);
		          htmpl.set(blk+".idDestinoAcquaLavaggio", ""+tipoDestinoAcquaLavaggio.getIdDestinoAcquaLavaggio());
		          htmpl.set(blk+".descDestinoAcquaLavaggio", tipoDestinoAcquaLavaggio.getDescrizione());
		          if(flagDefault && Validator.isNotEmpty(quantitaAcquaLavaggio))
		          {
		            htmpl.set(blk+".checkedAcquaLavaggio", "checked");
                htmpl.set(blk+".destQuantitaAcquaLavaggio", 
                  quantitaAcquaLavaggio);
		          }
		          else if(Validator.isNotEmpty(allevamentoAcquaLavaggioSel))
		          {
		            htmpl.set(blk+".checkedAcquaLavaggio", "checked");
		            htmpl.set(blk+".destQuantitaAcquaLavaggio", 
		              allevamentoAcquaLavaggioSel.getQuantitaAcquaLavaggioStr());
		          }
		          else
		          {
		            htmpl.set(blk+".readOnly", "readOnly");
		          }
		          
		          first = false;
		        }
		        else
		        {
		          blk =  "blkStruttureMungitura.blkDestinoAcquaLavaggio.blkSecondoAcquaLavaggio";
		          htmpl.newBlock(blk);
		          htmpl.set(blk+".indice", ""+i);
              htmpl.set(blk+".idDestinoAcquaLavaggio", ""+tipoDestinoAcquaLavaggio.getIdDestinoAcquaLavaggio());
              htmpl.set(blk+".descDestinoAcquaLavaggio", tipoDestinoAcquaLavaggio.getDescrizione());
              
              if(Validator.isNotEmpty(allevamentoAcquaLavaggioSel))
              {
                htmpl.set(blk+".checkedAcquaLavaggio", "checked");
                htmpl.set(blk+".destQuantitaAcquaLavaggio", 
                  allevamentoAcquaLavaggioSel.getQuantitaAcquaLavaggioStr());
              }
              else
              {
                htmpl.set(blk+".readOnly", "readOnly");
              }
		        }
		        
		        
		        if(Validator.isNotEmpty(allevamentoAcquaLavaggioSel)
              && allevamentoAcquaLavaggioSel.getErrori() != null)
	          {
	            for (int j = 0; j < allevamentoAcquaLavaggioSel.getErrori().length; j++)
	            {
	              String segnaPosto = allevamentoAcquaLavaggioSel.getErrori()[j][0];
	              String errore = allevamentoAcquaLavaggioSel.getErrori()[j][1];
	              htmpl.set(blk + "." + segnaPosto, MessageFormat.format(htmlStringKO, new Object[]
	              { pathErrori + "/" + imko, "'" + jssp.process(errore) + "'", errore }), null);
	            }
	            //Ripulisco gli errori per evitare che vengano riproposti
	            //ai reload sucessivi
	            allevamentoAcquaLavaggioSel.setErrori(null);
	          }
		        
		      }
		    }
	    }
	    catch(Exception e){}
		  

      if (AllevamentoAnagVO.ID_SPECIE_BOVINI_CARNE.equals(idSpecie) || AllevamentoAnagVO.ID_SPECIE_BOVINI_ALLEVAMENTO.equals(idSpecie))
      {
        //Visibile ed obbligatorio se la specie dichiarata in consistenza zootecnica 
        //è bovini altrimenti questo campo non è visibile e null
        htmpl.newBlock("blkStruttureMungitura.blkTipologiaStruttureMungitura");
        String idMungitura = "";
	      if(request.getParameter("idMungitura")!=null)
	      {
	        idMungitura = request.getParameter("idMungitura");
	      }
	      else if(all.getIdMungitura() != null)
	      {
	        idMungitura = all.getIdMungitura();
	      }

        TipoMungitura[] tipoMungitura = null;

        //devo leggere i dati relativi alla mungitura
        try
        {
          tipoMungitura = clientGaa.getTipiMungitura();
        }
        catch (SolmrException e)
        {
        }

        if (tipoMungitura != null)
        {
          htmpl.newBlock("blkStrutturaMungituraJavaScript");
          for (int i = 0; i < tipoMungitura.length; i++)
          {
            htmpl.newBlock("blkStruttureMungitura.blkTipologiaStruttureMungitura.blkTipoMungitura");
            htmpl.set("blkStruttureMungitura.blkTipologiaStruttureMungitura.blkTipoMungitura.idMungitura", "" + tipoMungitura[i].getIdMungitura());
            if (("" + tipoMungitura[i].getIdMungitura()).equals(idMungitura))
            {
              htmpl.set("blkStruttureMungitura.blkTipologiaStruttureMungitura.blkTipoMungitura.selected", "selected");
            }
            htmpl.set("blkStruttureMungitura.blkTipologiaStruttureMungitura.blkTipoMungitura.mungituraDesc", tipoMungitura[i].getDescrizione());

            //popolo il javascript
            htmpl.newBlock("blkStrutturaMungituraJavaScript.blkStruttureMungituraJavaScript");
            htmpl.set("blkStrutturaMungituraJavaScript.blkStruttureMungituraJavaScript.index", "" + i);
            htmpl.set("blkStrutturaMungituraJavaScript.blkStruttureMungituraJavaScript.value", "" + tipoMungitura[i].getCoeffVolumeLavaggio());

          }
        }

      }
    }

	  /******************************************************************************/
	  /******************************************************************************/
	  /****         FINE PARTE RELATIVA ALLA STRUTTURE DI MUNGITURA             ****/
	  /******************************************************************************/
	  /******************************************************************************/
    
  
  
  }
  
 

  //Valorizza la soccida - START
  String soccida = request.getParameter("soccida");
  if(request.getParameter("soccida") != null)
  {
    soccida = request.getParameter("soccida");
  }
  else
  {
    if(all.isFlagSoccida())
      soccida = "S";
  }
  if(Validator.isNotEmpty(soccida) && "S".equalsIgnoreCase(soccida))
    htmpl.set("checkedSi","checked");
  else 
    htmpl.set("checkedNo","checked");
    
    
  if(request.getParameter("motivoSoccida") != null)
  {
    htmpl.set("motivoSoccida", request.getParameter("motivoSoccida"));
  }
  else
  {
    htmpl.set("motivoSoccida", all.getMotivoSoccida());
  }
  
  htmpl.set("labelProprietario", SolmrConstants.DESC_PROPRIETARIO);
	htmpl.set("proprietario", SolmrConstants.PROPRIETARIO);
	htmpl.set("labelDetentore", SolmrConstants.DESC_DETENTORE);
	htmpl.set("detentore", SolmrConstants.DETENTORE);
                
	String assicuratoSoccida = request.getParameter("assicuratoSoccida");
	
	if (assicuratoSoccida==null) {
		assicuratoSoccida = all.getFlagAssicuratoCosman();
	}
	
  if (SolmrConstants.PROPRIETARIO.equals(assicuratoSoccida)) {
    htmpl.set("checkedProprietario", "checked");
  }else {
    htmpl.set("checkedDetentore", "checked");
  }
  //Valorizza la soccida - END


  //HtmplUtil.setValues(htmpl, all);

  out.print(htmpl.text());
%>

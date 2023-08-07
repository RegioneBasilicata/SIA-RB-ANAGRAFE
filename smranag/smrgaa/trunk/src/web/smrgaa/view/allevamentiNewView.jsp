<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*"%>
<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="java.math.BigDecimal"%>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/allevamenti_new.htm");
  AnagAziendaVO anagVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
%>
  <%@include file="/view/remoteInclude.inc"%>
<%
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String operazione = (String) request.getAttribute("operazione");

  AnagFacadeClient client = new AnagFacadeClient();
  GaaFacadeClient clientGaa = GaaFacadeClient.getInstance();
  ValidationErrors errors = (ValidationErrors) request.getAttribute("errors");
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" " + "title=\"{2}\" border=\"0\"></a>";

  Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = (Vector<SottoCategoriaAllevamento>) session.getAttribute("sottoCategorieAllevamenti");

  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  
  //popolo vettori per confronto capi
  Vector<String> vCuaaPossDet = (Vector<String>)request.getAttribute("vCuaaPossDet");
  Vector<String> vCuaaPossProp = (Vector<String>)request.getAttribute("vCuaaPossProp");
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
  
  
  
  

  htmpl.set("denominazione", anagVO.getDenominazione());
  if (anagVO.getCUAA() != null && !anagVO.getCUAA().equals(""))
    htmpl.set("CUAA", anagVO.getCUAA() + " - ");

  htmpl.set("dataSituazioneAlStr", anagVO.getDataSituazioneAlStr());
  
  
  String regimeInserisciAllevamento = request.getParameter("regimeInserisciAllevamento");
  
  
  
  

  //valorizzo la combo dell'UTE - START
  Vector UTE = client.getElencoIdUTEByIdAzienda(anagVO.getIdAzienda());
  if (UTE.size() == 1)
  {
    UteVO uteVO = (UteVO) UTE.elementAt(0);
    htmpl.newBlock("blkComboUTE");
    htmpl.set("blkComboUTE.idUTE", uteVO.getIdUte().toString());
    if (Validator.isNotEmpty(uteVO.getIndirizzo()))
    {
      htmpl.set("blkComboUTE.UTEDesc", uteVO.getComune() + " (" + uteVO.getProvincia() + ") - " + uteVO.getIndirizzo());
    }
    else
    {
      htmpl.set("blkComboUTE.UTEDesc", uteVO.getComune() + " (" + uteVO.getProvincia() + ")");
    }
    if(Validator.isEmpty(regimeInserisciAllevamento))
    {
      if (errors == null || errors.size() == 0)
      {
        htmpl.set("resProvincia", uteVO.getProvincia());
        htmpl.set("descResComune", uteVO.getComune());
        htmpl.set("cap", uteVO.getCap());
      }
    }
  }
  else
  {
    htmpl.newBlock("blkComboUTE");
    htmpl.set("blkComboUTE.idUTE", "");
    htmpl.set("blkComboUTE.UTEDesc", "");
    for (int i = 0; i < UTE.size(); i++)
    {
      UteVO uteVO = (UteVO) UTE.get(i);
      htmpl.newBlock("blkComboUTE");
      htmpl.set("blkComboUTE.idUTE", uteVO.getIdUte().toString());
      if (Validator.isNotEmpty(uteVO.getIndirizzo()))
      {
        htmpl.set("blkComboUTE.UTEDesc", uteVO.getComune() + " (" + uteVO.getProvincia() + ") - " + uteVO.getIndirizzo());
      }
      else
      {
        htmpl.set("blkComboUTE.UTEDesc", uteVO.getComune() + " (" + uteVO.getProvincia() + ")");
      }
      if (Validator.isNotEmpty(request.getParameter("idUTE")) && uteVO.getIdUte().toString().equals((String) request.getParameter("idUTE")))
      {
        htmpl.set("blkComboUTE.selected", "selected");
        if(Validator.isEmpty(regimeInserisciAllevamento))
        {
	        if (errors == null || errors.size() == 0)
	        {
	          htmpl.set("resProvincia", uteVO.getProvincia());
	          htmpl.set("descResComune", uteVO.getComune());
	          htmpl.set("cap", uteVO.getCap());
	        }
	      }
      }
    }
  }
  //valorizzo la combo dell'UTE - END

  if (request.getParameter("codiceAziendaZootecnica") != null)
    htmpl.set("codiceAziendaZootecnica", request.getParameter("codiceAziendaZootecnica").toUpperCase());
    
  if (request.getParameter("denominazioneAllevamento") != null)
    htmpl.set("denominazioneAllevamento", request.getParameter("denominazioneAllevamento"));
    
  if (request.getParameter("dataInizioAttivita") != null)
    htmpl.set("dataInizioAttivita", request.getParameter("dataInizioAttivita"));
    
  if (request.getParameter("longitudine") != null)
    htmpl.set("longitudine", request.getParameter("longitudine"));
    
  if (request.getParameter("latitudine") != null)
    htmpl.set("latitudine", request.getParameter("latitudine"));

  //valorizzo la combo dell'ASL - START
  Vector<TipoASLAnagVO> ASL = client.getTipiASL();

  for (int i = 0; i < ASL.size(); i++)
  {
    TipoASLAnagVO aslVO = ASL.get(i);
    htmpl.newBlock("blkIdASL");
    htmpl.set("blkIdASL.idASL", aslVO.getIdASL());
    htmpl.set("blkIdASL.ASLDesc", aslVO.getDescrizione());
    if (request.getParameter("idASL") != null && aslVO.getIdASL().toString().equals((String) request.getParameter("idASL")))
      htmpl.set("blkIdASL.selected", "selected");
  }
  //valorizzo la combo dell'ASL - END

  //valorizzo la combo dei Tipo di Produzione - START
  if(Validator.isNotEmpty(request.getParameter("idSpecie")))
  {  
	  Vector<CodeDescription> tipoProduzione = client.getTipoTipoProduzione(new Long(request.getParameter("idSpecie")).longValue());
	
	  for (int i = 0; i < tipoProduzione.size(); i++)
	  {
	    CodeDescription cd = tipoProduzione.get(i);
	    htmpl.newBlock("blkComboTipoProduzione");
	    htmpl.set("blkComboTipoProduzione.idTipoProduzione", cd.getCode().toString());
	    htmpl.set("blkComboTipoProduzione.descTipoProduzione", cd.getDescription());
	    if (request.getParameter("idTipoProduzione") != null && cd.getCode().toString().equals((String) request.getParameter("idTipoProduzione")))
	      htmpl.set("blkComboTipoProduzione.selected", "selected");
	  }
	}
  //valorizzo la combo dei Tipo di Produzione - END

  //valorizzo la combo degli Orientamenti - START

  if(Validator.isNotEmpty(request.getParameter("idTipoProduzione")) && Validator.isNotEmpty(request.getParameter("idSpecie")))
  {
    try
    {
      long idTipoProduzione = 0, idSpecie = 0;
      idSpecie = Long.parseLong(request.getParameter("idSpecie"));
      idTipoProduzione = Long.parseLong(request.getParameter("idTipoProduzione"));
      Vector<CodeDescription> orientamenti = client.getOrientamentoProduttivo(idSpecie, idTipoProduzione);
      for (int i = 0; i < orientamenti.size(); i++)
      {
        CodeDescription cd = orientamenti.get(i);
        htmpl.newBlock("blkComboOrientamentoProduttivo");
        htmpl.set("blkComboOrientamentoProduttivo.idOrientamentoProduttivo", cd.getCode().toString());
        htmpl.set("blkComboOrientamentoProduttivo.descOrientamentoProduttivo", cd.getDescription());
        if (request.getParameter("idOrientamentoProduttivo") != null && cd.getCode().toString().equals((String) request.getParameter("idOrientamentoProduttivo")))
          htmpl.set("blkComboOrientamentoProduttivo.selected", "selected");
      }
    }
    catch (Exception e)
    {
    }
  }
  //valorizzo la combo degli Orientamenti - END
  
  
  
  //valorizzo la combo dei tipi produzione COSMAN - START 
  
  if(Validator.isNotEmpty(request.getParameter("idTipoProduzione")) 
    && Validator.isNotEmpty(request.getParameter("idSpecie"))
    && Validator.isNotEmpty(request.getParameter("idOrientamentoProduttivo")) )
  {
    try
    {
      long idTipoProduzione=0,idSpecie=0,idOrientamentoProduttivo=0;
      idSpecie=Long.parseLong(request.getParameter("idSpecie"));
      idTipoProduzione=Long.parseLong(request.getParameter("idTipoProduzione"));
      idOrientamentoProduttivo=Long.parseLong(request.getParameter("idOrientamentoProduttivo"));
      Vector<CodeDescription> tipoCosman = client.getTipoProduzioneCosman(idSpecie, idTipoProduzione, idOrientamentoProduttivo);
      for(int i = 0; i<tipoCosman.size(); i++) 
      {
        CodeDescription cd = (CodeDescription)tipoCosman.get(i);
        htmpl.newBlock("blkComboTipoProduzioneCosman");
        htmpl.set("blkComboTipoProduzioneCosman.idTipoProduzioneCosman", cd.getCode().toString());
        htmpl.set("blkComboTipoProduzioneCosman.descTipoProduzioneCosman", cd.getDescription());
        if(request.getParameter("idTipoProduzioneCosman") != null && cd.getCode().toString().equals(request.getParameter("idTipoProduzioneCosman")))
        {
          htmpl.set("blkComboTipoProduzioneCosman.selected", "selected");
          
        }
      }
    }
    catch(Exception e){}
  }
  //valorizzo la combo tipi produzione - END
  
  
  
  

  //valorizzo i campi comune, provincia e istat comune con i valori inseriti precedentemente - START
  if (!Validator.isNotEmpty(request.getAttribute("ricarica")))
  {
    if (request.getParameter("resProvincia") != null)
    {
      htmpl.set("resProvincia", request.getParameter("resProvincia").toUpperCase());
    }
    if (request.getParameter("resComune") != null)
    {
      htmpl.set("resComune", request.getParameter("resComune"));
    }
    if (request.getParameter("descResComune") != null)
    {
      htmpl.set("descResComune", request.getParameter("descResComune").toUpperCase());
    }
  }
  if (request.getParameter("dal") == null)
  {
    htmpl.set("dal", DateUtils.formatDate(new Date()));
  }

  /******************************************************************************/
  /******************************************************************************/
  /****        INIZIO PARTE RELATIVA ALLA CONSISTENZA ZOOTECNICA             ****/
  /******************************************************************************/
  /******************************************************************************/

  //valorizzo i campi comune, provincia e istat comune con i valori inseriti precedentemente - END
  //Valorizzo gli array per le specie e categorie - START
  Vector<TipoSpecieAnimaleAnagVO> specie = new Vector<TipoSpecieAnimaleAnagVO>();
  try
  {
    if (anagVO.isFlagAziendaProvvisoria()) specie = client.getTipiSpecieAnimaleAzProv();
    else specie = client.getTipiSpecieAnimale();
  }
  catch (SolmrException e)
  {
  }
  int specieSize = specie.size();

  String idSpecie = request.getParameter("idSpecie");
  String idCategoria = request.getParameter("idCategoria");
  String idSottoCategoria = request.getParameter("idSottoCategoria");
  boolean cambioSpecie = false;
  boolean flagStallaPascolo=false;
 
  //Devo controlalre se è cambiata la specie, perchè se  cambiata
  //devo ripulire idCategoria, idSottoCategoria, vettore contenente
  //le sottoCategorie degli allevamenti
  if ("comboSpecie".equals(operazione))
    cambioSpecie = true;

  if (cambioSpecie)
  {
    idCategoria = null;
    idSottoCategoria = null;
  }

  String unitaMisuraSpecie = "";

  for (int i = 0; i < specieSize; i++)
  {
    TipoSpecieAnimaleAnagVO specieVO = (TipoSpecieAnimaleAnagVO) specie.get(i);
    htmpl.newBlock("blkComboSpecie");
    htmpl.set("blkComboSpecie.idSpecie", "" + specieVO.getIdSpecieAnimale());
    if (specieVO.getIdSpecieAnimale().toString().equals(idSpecie))
    {
      htmpl.set("blkComboSpecie.selected", "selected");
      flagStallaPascolo=specieVO.isFlagStallaPascolo();
      
      htmpl.bset("altezzaDefault", ""+specieVO.getAltLettieraPermMin());
      
      if (specieVO.isFlagStallaPascolo())
        htmpl.bset("flagStallaPascolo", "S");
      unitaMisuraSpecie = specieVO.getUnitaDiMisura();
    }
    htmpl.set("blkComboSpecie.specieDesc", specieVO.getDescrizione());
  }
  

  if (idSpecie != null && !"".equals(idSpecie))
  {
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
        TipoCategoriaAnimaleAnagVO categoria = categorie.get(i);
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
    if (sottoCategorieAllevamenti != null && sottoCategorieAllevamenti.size() > 0)
    {
      htmpl.newBlock("blksottoCategorieAllevamenti");
			htmpl.newBlock("blkLegendaAllevamenti");
			
      htmpl.set("blksottoCategorieAllevamenti.unitaMisuraSpecie", unitaMisuraSpecie);

      int size = sottoCategorieAllevamenti.size();
      //Devo visualizzare tante categorie quante sono presenti nel vettore
      for (int i = 0; i < size; i++)
      {
        SottoCategoriaAllevamento sottoCategoria = (SottoCategoriaAllevamento) sottoCategorieAllevamenti.get(i);
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
          htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.azotoZootecnicoTab", Formatter.formatAndRoundBigDecimal0(risAzotoBg));
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

          //htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.giorniVuotoSanitarioTab", sottoCategoria.getGiorniVuotoSanitario());
          htmpl.set("blksottoCategorieAllevamenti.blksottoCategoriaAllevamenti.numeroClicliAnnualiTab", sottoCategoria.getNumeroCicliAnnuali());
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

  Date parametroAllSuGnps = (Date)request.getAttribute("parametroAllSuGnps");
  //Le stabulazioni hanno senso solo se flagStallaPascolo è true
  if (flagStallaPascolo && parametroAllSuGnps.after(new Date()))
  {
    htmpl.newBlock("blkStabulazioniInsert");
    //htmpl.set("blkStabulazioniInsert.descrizioneAltriTrattam", request.getParameter("descrizioneAltriTrattam"));
    //Vado a vedere se ci sono stabulazioni inserite dall'utente
    Vector stabulazioniTrattamenti = (Vector) session.getAttribute("stabulazioniTrattamenti");
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
        
        //htmpl.set(blk + ".escretoAlPascolo", stabulazione.getEscretoAlPascolo());
        //htmpl.set(blk + ".permanenzaInStalla", stabulazione.getPermanenzaInStalla());
       
        
        
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
    if (SolmrConstants.FLAG_S.equals(request.getParameter("flagDeiezioneAvicoli")))
      htmpl.set("blkSoloAvicoli.flagDeiezioneAvicoliCheckedSi", "checked");
    else
      htmpl.set("blkSoloAvicoli.flagDeiezioneAvicoliCheckedNo", "checked");
  }

  if(parametroAllSuGnps.after(new Date()))
  {
    htmpl.newBlock("blkLettiera");
	  String superficieLettieraPermanente=request.getParameter("superficieLettieraPermanente");
	
	  htmpl.set("blkLettiera.superficieLettieraPermanente", superficieLettieraPermanente);
	  
	  
	  
	  String altezza = request.getParameter("altezza");
	  if (!Validator.isNotEmpty(superficieLettieraPermanente))
	  {
	    htmpl.set("blkLettiera.altReadOnly", "READONLY");
	    htmpl.set("blkLettiera.altezza", "");
	  }
	  else
	  {
	    htmpl.set("blkLettiera.altezza", altezza);
	  }
	  
	  try
    {
      BigDecimal b1=new BigDecimal(superficieLettieraPermanente.replace(',','.'));
      BigDecimal b2=new BigDecimal(altezza.replace(',','.'));
      b1=b1.multiply(b2);
      String volume=StringUtils.parseDoubleFieldTwoDecimal(b1.toString());
      htmpl.set("blkLettiera.volume",volume);
    }
    catch(Exception e) {}
	}
  

  //Valorizza le note - START
  if (request.getParameter("note") != null)
    htmpl.set("note", request.getParameter("note"));
  //Valorizza le note - END

  //Valorizza la dataDal - START
  if (request.getParameter("dal") != null)
    htmpl.set("dal", request.getParameter("dal"));
  //Valorizza la dataDal - END

  //Valorizza la soccida - START
  if (SolmrConstants.FLAG_S.equals(request.getParameter("soccida")))
    htmpl.set("checkedSi", "checked");
  else
    htmpl.set("checkedNo", "checked");
  
	htmpl.set("labelProprietario", SolmrConstants.DESC_PROPRIETARIO);
	htmpl.set("proprietario", SolmrConstants.PROPRIETARIO);
	htmpl.set("labelDetentore", SolmrConstants.DESC_DETENTORE);
	htmpl.set("detentore", SolmrConstants.DETENTORE);
                		
  if (SolmrConstants.PROPRIETARIO.equals(request.getParameter("assicuratoSoccida"))) {
    htmpl.set("checkedProprietario", "checked");
  }else {
    htmpl.set("checkedDetentore", "checked");
  }
  //Valorizza la soccida - END

  HtmplUtil.setValues(htmpl, request);
  HtmplUtil.setErrors(htmpl, errors, request, application);

  out.print(htmpl.text());
%>

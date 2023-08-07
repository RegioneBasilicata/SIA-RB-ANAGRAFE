<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoRichVarIrr.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String regimeInserimentoRichVarIrr = request.getParameter("regimeInserimentoRichVarIrr"); 
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  //AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagAziendaVO");
  Vector<UteVO> vUte = (Vector<UteVO>)request.getAttribute("vUte");  
  Vector<TipoGenereMacchinaGaaVO> vGenereMacchina = (Vector<TipoGenereMacchinaGaaVO>)request.getAttribute("vGenereMacchina");
  HashMap<Long, Vector<TipoCategoriaGaaVO>> hCategorie = (HashMap<Long, Vector<TipoCategoriaGaaVO>>)request.getAttribute("hCategorie");
  //Vector<CodeDescription> vTipoMarca = (Vector<CodeDescription>)request.getAttribute("vTipoMarca");
  HashMap<Long, Vector<CodeDescription>> hMarche = (HashMap<Long, Vector<CodeDescription>>)request.getAttribute("hMarche");  
  Vector<TipoFormaPossessoGaaVO> vTipoFormaPossesso = (Vector<TipoFormaPossessoGaaVO>)request.getAttribute("vTipoFormaPossesso");
  Vector<MacchinaAziendaNuovaVO> vMacchine = (Vector<MacchinaAziendaNuovaVO>)request.getAttribute("vMacchine");
  
  
  
  htmpl.set("idAzienda", request.getParameter("idAzienda"));
  
  //htmpl.set("cuaa", anagAziendaVO.getCUAA());
  //htmpl.set("denominazione", anagAziendaVO.getDenominazione());
  
  
  if(request.getAttribute("msgErrore") != null)
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", (String)request.getAttribute("msgErrore"));
  }
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  Vector<ValidationErrors> elencoErrori = (Vector<ValidationErrors>)request.getAttribute("elencoErrori");
  
  String[] idMacchina = request.getParameterValues("idMacchina"); 
  
    
  int numMacchine = 0;
  if(Validator.isEmpty(regimeInserimentoRichVarIrr) 
    && Validator.isNotEmpty(vMacchine))
  {
    numMacchine = vMacchine.size();
  }
  else
  {
    if(Validator.isNotEmpty(idMacchina))
      numMacchine = idMacchina.length;
  }
  
  
  if(numMacchine > 0)
  {
  
    for(int i=0;i<numMacchine;i++)
    {
      htmpl.newBlock("blkElencoMacchine");
      MacchinaAziendaNuovaVO macchinaAziendaNuovaVO = null;
      if(Validator.isEmpty(regimeInserimentoRichVarIrr) 
        && Validator.isNotEmpty(vMacchine))
      {      
        macchinaAziendaNuovaVO = vMacchine.get(i);
      }
      
      String idMacchinaStr = null;
      if(Validator.isNotEmpty(macchinaAziendaNuovaVO))
      {
        if(Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdMacchina()))
        {
          idMacchinaStr = macchinaAziendaNuovaVO.getIdMacchina().toString();
        }
      }
      else
      {
        idMacchinaStr = idMacchina[i];
      }
      
      if(Validator.isNotEmpty(idMacchinaStr))
      {
        htmpl.newBlock("blkElencoMacchine.blkPresoDaDb");
        htmpl.set("blkElencoMacchine.blkPresoDaDb.idMacchina",idMacchinaStr);
        //htmpl.set("blkElencoMacchine.blkPresoDaDb.idRiga", ""+i);
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
	      {
	        if(request.getParameterValues("idUte") != null 
	          && i <  request.getParameterValues("idUte").length 
	          && Validator.isNotEmpty(request.getParameterValues("idUte")[i])) 
	        {
	          htmpl.set("blkElencoMacchine.blkPresoDaDb.idUte", request.getParameterValues("idUte")[i]);
	          for(int j=0;j<vUte.size();j++)
	          {
	            if(request.getParameterValues("idUte")[i].equalsIgnoreCase(vUte.get(j).getIdUte().toString()))
	            {
	              String descUte = vUte.get(j).getComuneUte().getDescom()+ "("
	                +vUte.get(j).getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+vUte.get(j).getIndirizzo();
	              htmpl.set("blkElencoMacchine.blkPresoDaDb.descUte", descUte);
	              break;
	            }
	          }
	        }
	      }
	      else
	      {
	        if((macchinaAziendaNuovaVO != null)
	          && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdUte())) 
	        {
	          htmpl.set("blkElencoMacchine.blkPresoDaDb.idUte", ""+macchinaAziendaNuovaVO.getIdUte());
	          htmpl.set("blkElencoMacchine.blkPresoDaDb.descUte", macchinaAziendaNuovaVO.getDescUte());
	        } 
	      }
	      
	      if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("idGenereMacchina") != null 
            && i <  request.getParameterValues("idGenereMacchina").length 
            && Validator.isNotEmpty(request.getParameterValues("idGenereMacchina")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idGenereMacchina", request.getParameterValues("idGenereMacchina")[i]);
            for(int j=0;j<vGenereMacchina.size();j++)
            {
              if(request.getParameterValues("idGenereMacchina")[i].equalsIgnoreCase(new Long(vGenereMacchina.get(j).getIdGenereMacchina()).toString()))
              {
                htmpl.set("blkElencoMacchine.blkPresoDaDb.descGenere", vGenereMacchina.get(j).getDescrizione());
                break;
              }
            }
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdGenereMacchina())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idGenereMacchina", ""+macchinaAziendaNuovaVO.getIdGenereMacchina());
            htmpl.set("blkElencoMacchine.blkPresoDaDb.descGenere", macchinaAziendaNuovaVO.getDescGenereMacchina());
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("idCategoria") != null 
            && i <  request.getParameterValues("idCategoria").length 
            && Validator.isNotEmpty(request.getParameterValues("idCategoria")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idCategoria", request.getParameterValues("idCategoria")[i]);
            Vector<TipoCategoriaGaaVO> vTipoCategoria = hCategorie.get(new Long(request.getParameterValues("idGenereMacchina")[i]));            
            for(int j=0;j<vTipoCategoria.size();j++)
            {
              if(request.getParameterValues("idCategoria")[i].equalsIgnoreCase(new Long(vTipoCategoria.get(j).getIdCategoria()).toString()))
              {
                htmpl.set("blkElencoMacchine.blkPresoDaDb.descCategoria", vTipoCategoria.get(j).getDescrizione());
                break;
              }
            }
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdCategoria())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idCategoria", ""+macchinaAziendaNuovaVO.getIdCategoria());
            htmpl.set("blkElencoMacchine.blkPresoDaDb.descCategoria", macchinaAziendaNuovaVO.getDescGenereMacchina());
          } 
        }       
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("annoCostruzione") != null 
            && i <  request.getParameterValues("annoCostruzione").length 
            && Validator.isNotEmpty(request.getParameterValues("annoCostruzione")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.annoCostruzione", request.getParameterValues("annoCostruzione")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getAnnoCostruzione())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.annoCostruzione", ""+macchinaAziendaNuovaVO.getAnnoCostruzione());
          } 
        }
        
        /*if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("idMarca") != null 
            && i <  request.getParameterValues("idMarca").length 
            && Validator.isNotEmpty(request.getParameterValues("idMarca")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idMarca", request.getParameterValues("idMarca")[i]);
            for(int j=0;j<vTipoMarca.size();j++)
            {
              if(request.getParameterValues("idMarca")[i].equalsIgnoreCase(vTipoMarca.get(j).getCode().toString()))
              {
                htmpl.set("blkElencoMacchine.blkPresoDaDb.descMarca", vTipoMarca.get(j).getDescription());
                break;
              }
            }
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdMarca())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idMarca", ""+macchinaAziendaNuovaVO.getIdMarca());
            htmpl.set("blkElencoMacchine.blkPresoDaDb.descMarca", macchinaAziendaNuovaVO.getDescMarca());
          } 
        }*/
        
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("idMarca") != null 
            && i <  request.getParameterValues("idMarca").length 
            && Validator.isNotEmpty(request.getParameterValues("idMarca")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idMarca", request.getParameterValues("idMarca")[i]);
            
            Vector<CodeDescription> vTipoMarca = null;
            if(Validator.isNotEmpty(new Long(request.getParameterValues("idGenereMacchina")[i])))
            {
              vTipoMarca = hMarche.get(new Long(request.getParameterValues("idGenereMacchina")[i]));            
            }
            
            if(vTipoMarca == null)
              vTipoMarca = hMarche.get(new Long(-1));
            
            for(int j=0;j<vTipoMarca.size();j++)
            {
              if(request.getParameterValues("idMarca")[i].equalsIgnoreCase(vTipoMarca.get(j).getCode().toString()))
              {
                htmpl.set("blkElencoMacchine.blkPresoDaDb.descMarca", vTipoMarca.get(j).getDescription());
                break;
              }
            }
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdMarca())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idMarca", ""+macchinaAziendaNuovaVO.getIdMarca());
            htmpl.set("blkElencoMacchine.blkPresoDaDb.descMarca", macchinaAziendaNuovaVO.getDescMarca());
          } 
        }       
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("modello") != null 
            && i <  request.getParameterValues("modello").length 
            && Validator.isNotEmpty(request.getParameterValues("modello")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.modello", request.getParameterValues("modello")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getTipoMacchina())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.modello", ""+macchinaAziendaNuovaVO.getTipoMacchina());
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("matricolaTelaio") != null 
            && i <  request.getParameterValues("matricolaTelaio").length 
            && Validator.isNotEmpty(request.getParameterValues("matricolaTelaio")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.matricolaTelaio", request.getParameterValues("matricolaTelaio")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getMatricolaTelaio())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.matricolaTelaio", ""+macchinaAziendaNuovaVO.getMatricolaTelaio());
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("idTipoFormaPossesso") != null 
            && i <  request.getParameterValues("idTipoFormaPossesso").length 
            && Validator.isNotEmpty(request.getParameterValues("idTipoFormaPossesso")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idTipoFormaPossesso", request.getParameterValues("idTipoFormaPossesso")[i]);
            for(int j=0;j<vTipoFormaPossesso.size();j++)
            {
              if(request.getParameterValues("idTipoFormaPossesso")[i].equalsIgnoreCase(new Long(vTipoFormaPossesso.get(j).getIdTipoFormaPossesso()).toString()))
              {
                htmpl.set("blkElencoMacchine.blkPresoDaDb.descTipoFormPossesso", vTipoFormaPossesso.get(j).getDescrizione());
                break;
              }
            }
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdTipoFormaPossesso())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.idTipoFormaPossesso", ""+macchinaAziendaNuovaVO.getIdTipoFormaPossesso());
            htmpl.set("blkElencoMacchine.blkPresoDaDb.descTipoFormPossesso", macchinaAziendaNuovaVO.getDescTipoFormaPossesso());
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("percentualePossesso") != null 
            && i <  request.getParameterValues("percentualePossesso").length 
            && Validator.isNotEmpty(request.getParameterValues("percentualePossesso")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.percentualePossesso", request.getParameterValues("percentualePossesso")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getPercentualePossesso())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.percentualePossesso", Formatter.formatDouble(macchinaAziendaNuovaVO.getPercentualePossesso()));
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("dataCarico") != null 
            && i <  request.getParameterValues("dataCarico").length 
            && Validator.isNotEmpty(request.getParameterValues("dataCarico")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.dataCarico", request.getParameterValues("dataCarico")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getDataCarico())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.dataCarico", DateUtils.formatDate(macchinaAziendaNuovaVO.getDataCarico()));
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("dataScarico") != null 
            && i <  request.getParameterValues("dataScarico").length 
            && Validator.isNotEmpty(request.getParameterValues("dataScarico")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.dataScarico", request.getParameterValues("dataScarico")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getDataScarico())) 
          {
            htmpl.set("blkElencoMacchine.blkPresoDaDb.dataScarico", DateUtils.formatDate(macchinaAziendaNuovaVO.getDataScarico()));
          } 
        }
        
        
        
        
         
      
      } //Machine prese da db
      else
      {
        htmpl.newBlock("blkElencoMacchine.blkInseritoMano");
        htmpl.set("blkElencoMacchine.blkInseritoMano.idRiga", ""+i);
        
        if(vUte !=null)
	      {
	        for(int j=0;j<vUte.size();j++)
	        {
	          UteVO uteVO = vUte.get(j);
	          htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkElencoUte");
	          htmpl.set("blkElencoMacchine.blkInseritoMano.blkElencoUte.idUte", ""+uteVO.getIdUte());
	          String descUte = uteVO.getComuneUte().getDescom()+ "("
                  +uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+uteVO.getIndirizzo();
	          htmpl.set("blkElencoMacchine.blkInseritoMano.blkElencoUte.descUte", descUte);
	          
	          
	          if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
	          {
	            if(request.getParameterValues("idUte") != null && i < request.getParameterValues("idUte").length) 
	            {
	              if(Validator.isNotEmpty(request.getParameterValues("idUte")[i]) 
	                && request.getParameterValues("idUte")[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
	              {
	                htmpl.set("blkElencoMacchine.blkInseritoMano.blkElencoUte.selected", "selected=\"selected\"", null);
	              }
	            }
	          }
	          //prima volta che entro...
	          else 
	          {
	            if(macchinaAziendaNuovaVO != null) 
	            {
	              if(Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdUte()) 
	                && macchinaAziendaNuovaVO.getIdUte().compareTo(uteVO.getIdUte()) == 0)
	              {
	                htmpl.set("blkElencoMacchine.blkInseritoMano.blkElencoUte.selected", "selected=\"selected\"", null);
	              }
	            }
	          }         
	          
	        }
	      }
	      
	      
	      if(vGenereMacchina !=null)
        {
          for(int j=0;j<vGenereMacchina.size();j++)
          {
            TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = vGenereMacchina.get(j);
            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoGenereMacchina");
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoGenereMacchina.idGenereMacchina", ""+tipoGenereMacchinaGaaVO.getIdGenereMacchina());
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoGenereMacchina.descGenere", tipoGenereMacchinaGaaVO.getDescrizione());
            
            
            if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
            {
              if(request.getParameterValues("idGenereMacchina") != null && i < request.getParameterValues("idGenereMacchina").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idGenereMacchina")[i]) 
                  && request.getParameterValues("idGenereMacchina")[i].equalsIgnoreCase(new Long(tipoGenereMacchinaGaaVO.getIdGenereMacchina()).toString())) 
                {
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoGenereMacchina.selected", "selected=\"selected\"", null);
                }
              }
            }
            //prima volta che entro...
            else 
            {
              if(macchinaAziendaNuovaVO != null) 
              {
                if(Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdGenereMacchina()) 
                  && macchinaAziendaNuovaVO.getIdGenereMacchina().compareTo(new Long(tipoGenereMacchinaGaaVO.getIdGenereMacchina())) == 0)
                {
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoGenereMacchina.selected", "selected=\"selected\"", null);
                }
              }
            }         
            
          }
        }
        
        
        if(hCategorie != null && hCategorie.size() > 0) 
	      {
	        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr))
	        {
	          if((request.getParameterValues("idGenereMacchina") != null)
	            && (i < request.getParameterValues("idGenereMacchina").length)
	            && Validator.isNotEmpty(request.getParameterValues("idGenereMacchina")[i]))
	          {
	            Vector<TipoCategoriaGaaVO> vTipoCategoria = hCategorie.get(new Long(request.getParameterValues("idGenereMacchina")[i]));
	            if(vTipoCategoria != null)
	            {
	              for(int l = 0; l < vTipoCategoria.size(); l++) 
	              {
	                TipoCategoriaGaaVO tipoCategoriaGaaVO = vTipoCategoria.get(l);
	                htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoCategoria");
	                htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoCategoria.idCategoria", ""+tipoCategoriaGaaVO.getIdCategoria());
	                htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoCategoria.descCategoria", tipoCategoriaGaaVO.getDescrizione());
	              
	                if(request.getParameterValues("idCategoria") != null && i < request.getParameterValues("idCategoria").length) 
	                {
	                  if(Validator.isNotEmpty(request.getParameterValues("idCategoria")[i]) 
	                    && request.getParameterValues("idCategoria")[i].equalsIgnoreCase(new Long(tipoCategoriaGaaVO.getIdCategoria()).toString())) 
	                  {
	                    htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoCategoria.selected", "selected=\"selected\"", null);
	                  }
	                }               
	              }
	            }
	            else 
	            {
	              htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkNoTipoCategoria");
	            } 
	          }
	          else 
	          {
	            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkNoTipoCategoria");
	          }   
	        }
	        //prima volta che entro nella popup.
	        else
	        {
	          
	          if(Validator.isNotEmpty(macchinaAziendaNuovaVO)
	            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdGenereMacchina()))
	          {
	            Vector<TipoCategoriaGaaVO> vTipoCategoria = hCategorie.get(macchinaAziendaNuovaVO.getIdGenereMacchina());
	            if(Validator.isNotEmpty(vTipoCategoria))
	            {
	              for(int l = 0; l < vTipoCategoria.size(); l++) 
	              {
	                TipoCategoriaGaaVO tipoCategoriaGaaVO = vTipoCategoria.get(l);
	                htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoCategoria");
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoCategoria.idCategoria", ""+tipoCategoriaGaaVO.getIdCategoria());
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoCategoria.descCategoria", tipoCategoriaGaaVO.getDescrizione());
	                
	                if(Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdCategoria())
	                  && (macchinaAziendaNuovaVO.getIdCategoria().compareTo(new Long(tipoCategoriaGaaVO.getIdCategoria())) == 0)) 
	                {
	                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoCategoria.selected", "selected=\"selected\"", null);
	                }             
	              }
	            }
	            else 
	            {
	              htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkNoTipoCategoria");
	            }   
	          }
	          else 
	          {
	            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkNoTipoCategoria");
	          }             
	        }       
	      }
	      else 
	      {
	        htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkNoTipoCategoria");
	      }
	      
	      
	      if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("annoCostruzione") != null 
            && i <  request.getParameterValues("annoCostruzione").length 
            && Validator.isNotEmpty(request.getParameterValues("annoCostruzione")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.annoCostruzione", request.getParameterValues("annoCostruzione")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getAnnoCostruzione())) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.annoCostruzione", ""+macchinaAziendaNuovaVO.getAnnoCostruzione());
          } 
        }
        
        /*if(vTipoMarca !=null)
        {
          for(int j=0;j<vTipoMarca.size();j++)
          {
            CodeDescription marcaVO = vTipoMarca.get(j);
            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoMarca");
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.idMarca", ""+marcaVO.getCode());
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.descMarca", marcaVO.getDescription());
            
            
            if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
            {
              if(request.getParameterValues("idMarca") != null && i < request.getParameterValues("idMarca").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idMarca")[i]) 
                  && request.getParameterValues("idMarca")[i].equalsIgnoreCase(marcaVO.getCode().toString())) 
                {
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.selected", "selected=\"selected\"", null);
                }
              }
            }
            //prima volta che entro...
            else 
            {
              if(macchinaAziendaNuovaVO != null) 
              {
                if(Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdMarca()) 
                  && macchinaAziendaNuovaVO.getIdMarca().compareTo(new Long(marcaVO.getCode().intValue())) == 0)
                {
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.selected", "selected=\"selected\"", null);
                }
              }
            }         
            
          }
        }*/
        
        
        
        if(Validator.isNotEmpty(hMarche))
        {
          Vector<CodeDescription> vTipoMarca = null;
          
          if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
          {
	          if((request.getParameterValues("idGenereMacchina") != null)
	            && (i < request.getParameterValues("idGenereMacchina").length)
	            && Validator.isNotEmpty(request.getParameterValues("idGenereMacchina")[i]))
	          {
	            vTipoMarca = hMarche.get(new Long(request.getParameterValues("idGenereMacchina")[i]));
	          }
	        }
	        //prima volta che entro
	        else
	        {
	          if(Validator.isNotEmpty(macchinaAziendaNuovaVO)
              && Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdGenereMacchina()))
            {
              vTipoMarca = hMarche.get(macchinaAziendaNuovaVO.getIdGenereMacchina());
            }	        
	        }
          
          if(vTipoMarca == null)
          {
            vTipoMarca = hMarche.get(new Long(-1));
          }
          
          for(int j=0;j<vTipoMarca.size();j++)
          {
            CodeDescription marcaVO = vTipoMarca.get(j);
            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoMarca");
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.idMarca", ""+marcaVO.getCode());
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.descMarca", marcaVO.getDescription());
            
            
            if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
            {
	            if(request.getParameterValues("idMarca") != null && i < request.getParameterValues("idMarca").length) 
	            {
	              if(Validator.isNotEmpty(request.getParameterValues("idMarca")[i]) 
	                && request.getParameterValues("idMarca")[i].equalsIgnoreCase(marcaVO.getCode().toString())) 
	              {
	                htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.selected", "selected=\"selected\"", null);
	              }             
	            }
	          }
	          //prima volta che entro
	          else
	          {            
	            if(macchinaAziendaNuovaVO != null) 
	            {
	              if(Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdMarca()) 
	                && macchinaAziendaNuovaVO.getIdMarca().compareTo(new Long(marcaVO.getCode().intValue())) == 0)
	              {
	                htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.selected", "selected=\"selected\"", null);
	              }
	            }
	          }
          }             
        }
        
        
        
        
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("modello") != null 
            && i <  request.getParameterValues("modello").length 
            && Validator.isNotEmpty(request.getParameterValues("modello")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.modello", request.getParameterValues("modello")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getTipoMacchina())) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.modello", ""+macchinaAziendaNuovaVO.getTipoMacchina());
          } 
        }
        
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("matricolaTelaio") != null 
            && i <  request.getParameterValues("matricolaTelaio").length 
            && Validator.isNotEmpty(request.getParameterValues("matricolaTelaio")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.matricolaTelaio", request.getParameterValues("matricolaTelaio")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getMatricolaTelaio())) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.matricolaTelaio", ""+macchinaAziendaNuovaVO.getMatricolaTelaio());
          } 
        }
        
        if(vTipoFormaPossesso !=null)
        {
          for(int j=0;j<vTipoFormaPossesso.size();j++)
          {
            TipoFormaPossessoGaaVO tipoPossessoVO = vTipoFormaPossesso.get(j);
            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso");
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso.idTipoFormaPossesso", ""+tipoPossessoVO.getIdTipoFormaPossesso());
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso.descTipoFormPossesso", tipoPossessoVO.getDescrizione());
            
            
            if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
            {
              if(request.getParameterValues("idTipoFormaPossesso") != null && i < request.getParameterValues("idTipoFormaPossesso").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoFormaPossesso")[i]) 
                  && request.getParameterValues("idTipoFormaPossesso")[i].equalsIgnoreCase(new Long(tipoPossessoVO.getIdTipoFormaPossesso()).toString())) 
                {
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso.selected", "selected=\"selected\"", null);
                }
              }
            }
            //prima volta che entro...
            else 
            {
              if(macchinaAziendaNuovaVO != null) 
              {
                if(Validator.isNotEmpty(macchinaAziendaNuovaVO.getIdTipoFormaPossesso()) 
                  && macchinaAziendaNuovaVO.getIdTipoFormaPossesso().compareTo(new Long(tipoPossessoVO.getIdTipoFormaPossesso())) == 0)
                {
                  htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso.selected", "selected=\"selected\"", null);
                }
              }
            }         
            
          }
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("percentualePossesso") != null 
            && i <  request.getParameterValues("percentualePossesso").length 
            && Validator.isNotEmpty(request.getParameterValues("percentualePossesso")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.percentualePossesso", request.getParameterValues("percentualePossesso")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getPercentualePossesso())) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.percentualePossesso", Formatter.formatDouble(macchinaAziendaNuovaVO.getPercentualePossesso()));
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("dataCarico") != null 
            && i <  request.getParameterValues("dataCarico").length 
            && Validator.isNotEmpty(request.getParameterValues("dataCarico")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.dataCarico", request.getParameterValues("dataCarico")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getDataCarico())) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.dataCarico", DateUtils.formatDate(macchinaAziendaNuovaVO.getDataCarico()));
          } 
        }
        
        if(Validator.isNotEmpty(regimeInserimentoRichVarIrr)) 
        {
          if(request.getParameterValues("dataScarico") != null 
            && i <  request.getParameterValues("dataScarico").length 
            && Validator.isNotEmpty(request.getParameterValues("dataScarico")[i])) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.dataScarico", request.getParameterValues("dataScarico")[i]);
          }
        }
        else
        {
          if((macchinaAziendaNuovaVO != null)
            && Validator.isNotEmpty(macchinaAziendaNuovaVO.getDataScarico())) 
          {
            htmpl.set("blkElencoMacchine.blkInseritoMano.dataScarico", DateUtils.formatDate(macchinaAziendaNuovaVO.getDataScarico()));
          } 
        }
        
        
        
        
        
      } //else inserite nuove...
              
      
        
      //Da verificare....  
      if(elencoErrori != null && elencoErrori.size() > 0) 
		  {
		    ValidationErrors errors = (ValidationErrors)elencoErrori.elementAt(i);
		    if(errors != null && errors.size  () > 0) 
		    {
		      Iterator iter = htmpl.getVariableIterator();
		      while(iter.hasNext()) 
		      {
		        String key = (String)iter.next();
		        if(key.startsWith("err_")) 
		        {
		          String property = key.substring(4);
		          Iterator errorIterator = errors.get(property);
		          if(errorIterator != null) 
		          {
		            ValidationError error = (ValidationError)errorIterator.next();
		            String blkErr = "blkElencoMacchine.blkInseritoMano.err_";
		            if(Validator.isNotEmpty(idMacchina))
		            {
		              if(Validator.isNotEmpty(idMacchina[i]))
		                blkErr = "blkElencoMacchine.blkPresoDaDb.err_";		            
		            }		            
		            
		            htmpl.set(blkErr+property,
		                  MessageFormat.format(htmlStringKO,
		                  new Object[] {
		                  pathErrori + "/"+ imko,
		                  "'"+jssp.process(error.getMessage())+"'",
		                  error.getMessage()}),
		                  null);
		          }
		        }
		      }
		    }
		  }
		  
		  
    }
  
  }
  
  
  
  
  
  
  
  
  
  
	
	


      
  

%>
<%= htmpl.text() %>

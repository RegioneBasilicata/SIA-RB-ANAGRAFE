<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/tabellaDatiNewRichVarIrr.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  String tipoModifica = request.getParameter("tipoModifica");
  String numRiga = request.getParameter("numRiga");
  
  String idAzienda = request.getParameter("idAzienda");
  String[] idMacchina = request.getParameterValues("idMacchina");
  String[] idUte = request.getParameterValues("idUte");
  String[] idGenereMacchina = request.getParameterValues("idGenereMacchina");
  String[] idCategoria = request.getParameterValues("idCategoria");
  String[] annoCostruzione = request.getParameterValues("annoCostruzione");
  String[] idMarca = request.getParameterValues("idMarca");
  String[] modello = request.getParameterValues("modello");
  String[] matricolaTelaio = request.getParameterValues("matricolaTelaio");
  String[] idTipoFormaPossesso = request.getParameterValues("idTipoFormaPossesso");
  String[] percentualePossesso = request.getParameterValues("percentualePossesso");
  String[] dataCarico = request.getParameterValues("dataCarico");
  String[] dataScarico = request.getParameterValues("dataScarico");
 
  
  try
  {
  
    AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
    request.setAttribute("anagAziendaVO", anagAziendaVO);
  
    String[] orderBy = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE, (String)SolmrConstants.ORDER_BY_UTE_INDIRIZZO};
    Vector<UteVO> vUte = anagFacadeClient.getListUteByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy);
    request.setAttribute("vUte", vUte);
  
    Vector<TipoGenereMacchinaGaaVO> vGenereMacchina = gaaFacadeClient.getElencoTipoGenereMacchinaFromRuolo(
        new Long(1), ruoloUtenza.getCodiceRuolo());
    request.setAttribute("vGenereMacchina", vGenereMacchina);
    
    Vector<CodeDescription> vTipoMarca = gaaFacadeClient.getElencoTipoMarca();
    //request.setAttribute("vTipoMarca", vTipoMarca);    
    
    Vector<TipoFormaPossessoGaaVO> vTipoFormaPossesso = gaaFacadeClient.getElencoTipoFormaPossesso();
    request.setAttribute("vTipoFormaPossesso", vTipoFormaPossesso);
    
    
    // Ricerco la categoria in relazione al genere
    HashMap<Long, Vector<TipoCategoriaGaaVO>> hCategorie = new HashMap<Long, Vector<TipoCategoriaGaaVO>>(); 
    
    if(idGenereMacchina != null) 
    {
      for(int i = 0; i < idGenereMacchina.length; i++) 
      {         
        if(Validator.isNotEmpty(idGenereMacchina[i])) 
        {
          Vector<TipoCategoriaGaaVO> vTipoCategoria = gaaFacadeClient.getElencoTipoCategoria(new Long(idGenereMacchina[i]));
          hCategorie.put(new Long(idGenereMacchina[i]), vTipoCategoria);   
        }
      }
    }
    request.setAttribute("hCategorie", hCategorie);
    
    
    
    HashMap<Long, Vector<CodeDescription>> hMarche = new HashMap<Long, Vector<CodeDescription>>();  
    hMarche.put(new Long(-1), vTipoMarca);
    if(idGenereMacchina != null) 
    {
      for(int i = 0; i < idGenereMacchina.length; i++) 
      {         
        if(Validator.isNotEmpty(idGenereMacchina[i])) 
        {
          Vector<CodeDescription> vTipoMarcaTmp = gaaFacadeClient.getElencoTipoMarcaByIdGenere(new Long(idGenereMacchina[i]));
          if(vTipoMarcaTmp != null)
          {
            hMarche.put(new Long(idGenereMacchina[i]), vTipoMarcaTmp);
          }   
        }
      }
    }
    request.setAttribute("hMarche", hMarche);
    
    
    
    
    
    
    //Calcolo numero macchine
    int numMacchine = 0;
    if(Validator.isNotEmpty(idMacchina))
    {
      numMacchine = idMacchina.length;
    }
    
    if(Validator.isNotEmpty(tipoModifica)
      && "inserisci".equalsIgnoreCase(tipoModifica))
    {
      numMacchine++;
    }
    
    
    int countMacchine = 0;
    
    for(int i=0;i<numMacchine;i++)
    {
      if(Validator.isNotEmpty(tipoModifica)
        && "elimina".equalsIgnoreCase(tipoModifica))
      {
        if(i == new Integer(numRiga).intValue())
        {           
          continue;
        }
      }
    
    
      htmpl.newBlock("blkElencoMacchine");
      
      String idMacchinaStr = null;      
      
      if(request.getParameterValues("idMacchina") != null 
          && i <  request.getParameterValues("idMacchina").length 
          && Validator.isNotEmpty(request.getParameterValues("idMacchina")[i]))
      {
        idMacchinaStr = request.getParameterValues("idMacchina")[i];
      } 
      
      
      
      if(Validator.isNotEmpty(idMacchinaStr))
      {
        htmpl.newBlock("blkElencoMacchine.blkPresoDaDb");
        htmpl.set("blkElencoMacchine.blkPresoDaDb.idMacchina",idMacchinaStr);
        //htmpl.set("blkElencoMacchine.blkPresoDaDb.idRiga", ""+i);
        
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
           
        
        if(request.getParameterValues("annoCostruzione") != null 
          && i <  request.getParameterValues("annoCostruzione").length 
          && Validator.isNotEmpty(request.getParameterValues("annoCostruzione")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkPresoDaDb.annoCostruzione", request.getParameterValues("annoCostruzione")[i]);
        }
        
        
        if(request.getParameterValues("idMarca") != null 
          && i <  request.getParameterValues("idMarca").length 
          && Validator.isNotEmpty(request.getParameterValues("idMarca")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkPresoDaDb.idMarca", request.getParameterValues("idMarca")[i]);
          
          Vector<CodeDescription> vTipoMarcaTmp = hMarche.get(new Long(request.getParameterValues("idGenereMacchina")[i]));            
          if(vTipoMarcaTmp == null)
            vTipoMarcaTmp = hMarche.get(new Long(-1));
          
          for(int j=0;j<vTipoMarcaTmp.size();j++)
          {
            if(request.getParameterValues("idMarca")[i].equalsIgnoreCase(vTipoMarcaTmp.get(j).getCode().toString()))
            {
              htmpl.set("blkElencoMacchine.blkPresoDaDb.descMarca", vTipoMarcaTmp.get(j).getDescription());
              break;
            }
          }
        }
        
        
        if(request.getParameterValues("modello") != null 
          && i <  request.getParameterValues("modello").length 
          && Validator.isNotEmpty(request.getParameterValues("modello")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkPresoDaDb.modello", request.getParameterValues("modello")[i]);
        }
        
        if(request.getParameterValues("matricolaTelaio") != null 
          && i <  request.getParameterValues("matricolaTelaio").length 
          && Validator.isNotEmpty(request.getParameterValues("matricolaTelaio")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkPresoDaDb.matricolaTelaio", request.getParameterValues("matricolaTelaio")[i]);
        }
        
        
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
        
        if(request.getParameterValues("percentualePossesso") != null 
          && i <  request.getParameterValues("percentualePossesso").length 
          && Validator.isNotEmpty(request.getParameterValues("percentualePossesso")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkPresoDaDb.percentualePossesso", request.getParameterValues("percentualePossesso")[i]);
        }
        
        
        if(request.getParameterValues("dataCarico") != null 
          && i <  request.getParameterValues("dataCarico").length 
          && Validator.isNotEmpty(request.getParameterValues("dataCarico")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkPresoDaDb.dataCarico", request.getParameterValues("dataCarico")[i]);
        }
        
        
        if(request.getParameterValues("dataScarico") != null 
          && i <  request.getParameterValues("dataScarico").length 
          && Validator.isNotEmpty(request.getParameterValues("dataScarico")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkPresoDaDb.dataScarico", request.getParameterValues("dataScarico")[i]);
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
            
            
            if(request.getParameterValues("idUte") != null && i < request.getParameterValues("idUte").length) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("idUte")[i]) 
                && request.getParameterValues("idUte")[i].equalsIgnoreCase(uteVO.getIdUte().toString())) 
              {
                htmpl.set("blkElencoMacchine.blkInseritoMano.blkElencoUte.selected", "selected=\"selected\"", null);
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
            
            
            if(request.getParameterValues("idGenereMacchina") != null && i < request.getParameterValues("idGenereMacchina").length) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("idGenereMacchina")[i]) 
                && request.getParameterValues("idGenereMacchina")[i].equalsIgnoreCase(new Long(tipoGenereMacchinaGaaVO.getIdGenereMacchina()).toString())) 
              {
                htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoGenereMacchina.selected", "selected=\"selected\"", null);
              }
            }           
          }
        }
        
        
        if(hCategorie != null && hCategorie.size() > 0) 
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
        else 
        {
          htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkNoTipoCategoria");
        }
        
        
        if(request.getParameterValues("annoCostruzione") != null 
          && i <  request.getParameterValues("annoCostruzione").length 
          && Validator.isNotEmpty(request.getParameterValues("annoCostruzione")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkInseritoMano.annoCostruzione", request.getParameterValues("annoCostruzione")[i]);
        }
        
        
        if(hMarche !=null)
        {
        
          Vector<CodeDescription> vTipoMarcaTmp = null;
          if((request.getParameterValues("idGenereMacchina") != null)
              && (i < request.getParameterValues("idGenereMacchina").length)
              && Validator.isNotEmpty(request.getParameterValues("idGenereMacchina")[i]))
          {
            vTipoMarcaTmp = hMarche.get(new Long(request.getParameterValues("idGenereMacchina")[i]));
          }
          
          if(vTipoMarcaTmp == null)
          {
            vTipoMarcaTmp = hMarche.get(new Long(-1));
          }
        
          for(int j=0;j<vTipoMarcaTmp.size();j++)
          {
            CodeDescription marcaVO = vTipoMarcaTmp.get(j);
            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoMarca");
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.idMarca", ""+marcaVO.getCode());
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.descMarca", marcaVO.getDescription());
            
            
            if(request.getParameterValues("idMarca") != null && i < request.getParameterValues("idMarca").length) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("idMarca")[i]) 
                && request.getParameterValues("idMarca")[i].equalsIgnoreCase(marcaVO.getCode().toString())) 
              {
                htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoMarca.selected", "selected=\"selected\"", null);
              }
            }            
            
          }
        }
        
        if(request.getParameterValues("modello") != null 
	        && i <  request.getParameterValues("modello").length 
	        && Validator.isNotEmpty(request.getParameterValues("modello")[i])) 
	      {
	        htmpl.set("blkElencoMacchine.blkInseritoMano.modello", request.getParameterValues("modello")[i]);
	      }        
        
        
        if(request.getParameterValues("matricolaTelaio") != null 
          && i <  request.getParameterValues("matricolaTelaio").length 
          && Validator.isNotEmpty(request.getParameterValues("matricolaTelaio")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkInseritoMano.matricolaTelaio", request.getParameterValues("matricolaTelaio")[i]);
        }
        
        
        if(vTipoFormaPossesso !=null)
        {
          for(int j=0;j<vTipoFormaPossesso.size();j++)
          {
            TipoFormaPossessoGaaVO tipoPossessoVO = vTipoFormaPossesso.get(j);
            htmpl.newBlock("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso");
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso.idTipoFormaPossesso", ""+tipoPossessoVO.getIdTipoFormaPossesso());
            htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso.descTipoFormPossesso", tipoPossessoVO.getDescrizione());
            
            
            if(request.getParameterValues("idTipoFormaPossesso") != null && i < request.getParameterValues("idTipoFormaPossesso").length) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("idTipoFormaPossesso")[i]) 
                && request.getParameterValues("idTipoFormaPossesso")[i].equalsIgnoreCase(new Long(tipoPossessoVO.getIdTipoFormaPossesso()).toString())) 
              {
                htmpl.set("blkElencoMacchine.blkInseritoMano.blkTipoFormaPossesso.selected", "selected=\"selected\"", null);
              }
            }           
          }
        }
        
        if(request.getParameterValues("percentualePossesso") != null 
          && i <  request.getParameterValues("percentualePossesso").length 
          && Validator.isNotEmpty(request.getParameterValues("percentualePossesso")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkInseritoMano.percentualePossesso", request.getParameterValues("percentualePossesso")[i]);
        }
        
        
        if(request.getParameterValues("dataCarico") != null 
          && i <  request.getParameterValues("dataCarico").length 
          && Validator.isNotEmpty(request.getParameterValues("dataCarico")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkInseritoMano.dataCarico", request.getParameterValues("dataCarico")[i]);
        }
        
        
        if(request.getParameterValues("dataScarico") != null 
          && i <  request.getParameterValues("dataScarico").length 
          && Validator.isNotEmpty(request.getParameterValues("dataScarico")[i])) 
        {
          htmpl.set("blkElencoMacchine.blkInseritoMano.dataScarico", request.getParameterValues("dataScarico")[i]);
        }
        
        
        
        
        
        
      } //else inserite nuove...
              
      
        
      
      
    
      
      countMacchine++;  
        
      
    }  
	  
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>

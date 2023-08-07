<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popVariazioniEleggibilita.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
  
 	Vector<Vector<ParticellaCertElegVO>> elencoStoricoEleggibilita = (Vector<Vector<ParticellaCertElegVO>>)request.getAttribute("elencoStoricoEleggibilita");
 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  
  
  
  
  if(Validator.isNotEmpty(storicoParticellaVO))
  {
    htmpl.newBlock("blkStoricoParticella");
    
    ComuneVO comune = storicoParticellaVO.getComuneParticellaVO();

    if (comune !=null)
    {
      ProvinciaVO provincia = comune.getProvinciaVO();
      htmpl.set("blkStoricoParticella.comune", comune.getDescom()+" ("+provincia.getSiglaProvincia()+")");
    }
      
    htmpl.set("blkStoricoParticella.sezione", storicoParticellaVO.getSezione());
    htmpl.set("blkStoricoParticella.foglio", storicoParticellaVO.getFoglio());
    htmpl.set("blkStoricoParticella.particella", storicoParticellaVO.getParticella());
    htmpl.set("blkStoricoParticella.subalterno", storicoParticellaVO.getSubalterno());
    htmpl.set("blkStoricoParticella.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
  
  
  }
  
  
  

 	// Sì è verificato un errore durante il recupero delle variazioni storiche della particella
 	// selezionata
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
  	htmpl.newBlock("blkErrore");
  	htmpl.set("blkErrore.messaggio", messaggioErrore);
 	}
 	else 
  {
    
   
  	htmpl.newBlock("blkStoricoEleggibilita");
  	for(int i=0;i<elencoStoricoEleggibilita.size();i++) 
    {      
    	Vector<ParticellaCertElegVO> vParticellaCertElegVO = elencoStoricoEleggibilita.get(i);
      htmpl.newBlock("blkStoricoEleggibilita.blkElencoStoricoEleggibilita");
      htmpl.newBlock("blkStoricoEleggibilita.blkElencoStoricoEleggibilita.blkRigaPrimo");
      htmpl.set("blkStoricoEleggibilita.blkElencoStoricoEleggibilita.blkRigaPrimo.dimRighe",""+vParticellaCertElegVO.size());
      htmpl.set("blkStoricoEleggibilita.blkElencoStoricoEleggibilita.blkRigaPrimo.annoCampagna",""+vParticellaCertElegVO.get(0).getAnnoCampagna());
      /*if(i==0)
      {
        htmpl.set("blkStoricoEleggibilita.blkElencoStoricoEleggibilita.blkRigaPrimo.color", (String)SolmrConstants.COLOR_PARTICELLE_MODIFICATE);
      }*/      
        
      for(int j=0;j<vParticellaCertElegVO.size();j++)
      {
         
        ParticellaCertElegVO particellaCertElegVO = vParticellaCertElegVO.get(j);
        String descEleggiblita = particellaCertElegVO.getDescrizioneFit();
        /*if(Validator.isNotEmpty(particellaCertElegVO.getDescrizioneFit()))
        {
          descEleggiblita += " - "+particellaCertElegVO.getDescrizione();
        }*/
        
        if(j!=0)
        {
          htmpl.newBlock("blkStoricoEleggibilita.blkElencoStoricoEleggibilita");
        }
          
        /*if(i==0)
        {
          htmpl.set("blkStoricoEleggibilita.blkElencoStoricoEleggibilita.color", (String)SolmrConstants.COLOR_PARTICELLE_MODIFICATE);
        }*/   
           
        htmpl.set("blkStoricoEleggibilita.blkElencoStoricoEleggibilita.descEleggibilita", descEleggiblita);
        htmpl.set("blkStoricoEleggibilita.blkElencoStoricoEleggibilita.superficie", 
          Formatter.formatDouble4(particellaCertElegVO.getSuperficie()));
          
      }
      
      
      
    	
 		}
    
    
    
    
 		
 	}

%>
<%= htmpl.text()%>

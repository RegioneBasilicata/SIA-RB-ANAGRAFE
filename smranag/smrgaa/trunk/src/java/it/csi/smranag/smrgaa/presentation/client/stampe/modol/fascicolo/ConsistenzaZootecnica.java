package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Allevamenti;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Allevamento;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ElencoCategorie;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroAllevamenti;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaCapi;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaTotaliAllev;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ConsistenzaZootecnica extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "CONSISTENZA_ZOOTECNICA";

  public ConsistenzaZootecnica() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroAllevamenti quadroAllevamenti = new QuadroAllevamenti();
    fascicoDigitale.setQuadroAllevamenti(quadroAllevamenti);
    quadroAllevamenti.setVisibility(true);
    
    quadroAllevamenti.setTitoloAllevamenti(richiestaTipoReportVO.getQuadro());
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");    
    
    
    Vector<Long> idall = anagFacadeClient.getAllevamentiQuadroG(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione);
    int size = 0;
    if (idall != null)
      size = idall.size();

    if (size > 0)
    {
      
      AllevamentoAnagVO all = null;
      quadroAllevamenti.setAllevamenti(new Allevamenti());
      
      for (int i=0;i<size;i++)
      {
        //mi ricerco l'allevamento
        all = anagFacadeClient.getAllevamento(idall.get(i));
        Allevamento allevamento = new Allevamento();
        
        //Ricavo l'ute
        String descUte = "";
        UteVO ute = anagFacadeClient.getUteById(all.getIdUTELong());
        //Mi ricavo i dati del comune dell'ute
        ComuneVO comune = anagFacadeClient.getComuneByISTAT(ute.getIstat());
        if (comune != null)
          descUte = ute.getIndirizzo()+" - "+comune.getCap()+" "+comune.getDescom()
              +" ("+comune.getSiglaProv()+")";
        allevamento.setUte(descUte);
        
        allevamento.setCodiceAsl(all.getCodiceAziendaZootecnica());
        allevamento.setSpecie(all.getTipoSpecieAnimaleAnagVO().getDescrizione());     
        allevamento.setTipoProduzione(all.getDescTipoProduzione());
        allevamento.setOrientamentoProduttivo(all.getDescOrientamentoProduttivo());
        allevamento.setTipologiaAssicurativa(all.getDescTipoProduzioneCosman());    
              
        /**
         * aggiungo le tabelle con le varie categorie ed il numero di capi
         * posseduto
         */
        
        
        Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = 
          gaaFacadeClient.getTipiSottoCategoriaAllevamento(all.getIdAllevamentoLong().longValue());       
        int sizeCat=0;
        if (sottoCategorieAllevamenti!=null)
          sizeCat=sottoCategorieAllevamenti.size();        
          
        if(sizeCat > 0)
        {
          allevamento.setElencoCategorie(new ElencoCategorie());
        }
        
        
        int numeroCapiProprieta = 0;
        int numeroCapiDetenzione = 0;
        BigDecimal totAzoto = new BigDecimal(0);
        
        for (int j=0;j<sizeCat;j++)
        {
          
        
          RigaCapi rigaCapi = new RigaCapi();
          SottoCategoriaAllevamento sottoCategoria = (SottoCategoriaAllevamento) sottoCategorieAllevamenti.get(j);
          
          rigaCapi.setCategoria(sottoCategoria.getDescCategoriaAnimale());
          rigaCapi.setSottoCategoria(sottoCategoria.getDescSottoCategoriaAnimale());
          rigaCapi.setCapiDetenuti(sottoCategoria.getQuantita());
          numeroCapiDetenzione = numeroCapiDetenzione 
              + new Integer(sottoCategoria.getQuantita()).intValue();
          rigaCapi.setCapiPropr(sottoCategoria.getQuantitaProprieta());
          numeroCapiProprieta = numeroCapiProprieta 
              + new Integer(sottoCategoria.getQuantitaProprieta()).intValue();
          rigaCapi.setCicli(sottoCategoria.getNumeroCicliAnnuali());    
          rigaCapi.setDurata(sottoCategoria.getCicli());         
          
          String azoto = "";
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
            azoto = Formatter.formatAndRoundBigDecimal1(risAzotoBg);
            totAzoto = totAzoto.add(new BigDecimal(azoto.replace(",", ".")));
          }
          catch (Exception e)
          {
            azoto = "";
          }
          rigaCapi.setAzoto(azoto);
          
          allevamento.getElencoCategorie().getRigaCapi().add(rigaCapi);
        }
        
        //metto totali categorie
        RigaTotaliAllev rigaTotaliAllev = new RigaTotaliAllev();
        rigaTotaliAllev.setTotCapiPropr(""+numeroCapiProprieta);
        rigaTotaliAllev.setTotCapiDetenuti(""+numeroCapiDetenzione);
        rigaTotaliAllev.setTotAzoto(Formatter.formatAndRoundBigDecimal1(totAzoto));
        
        allevamento.setRigaTotaliAllev(rigaTotaliAllev);
        
        quadroAllevamenti.getAllevamenti().getAllevamento().add(allevamento);
      }  
    }
    else
    {
      quadroAllevamenti.setSezioneVuota(true);
    }
    
    
    
  }
  
  
}
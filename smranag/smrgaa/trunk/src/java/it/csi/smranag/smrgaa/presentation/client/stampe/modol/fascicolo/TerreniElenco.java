package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ElencoParticelle;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Particella;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Particellare;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroTerreni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaTotali;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFonteVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO;
import it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniElenco extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "TERRENI_ELENCO";

  public TerreniElenco() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    if(fascicoDigitale.getQuadroTerreni() == null)
      fascicoDigitale.setQuadroTerreni(new QuadroTerreni());
    QuadroTerreni quadroTerreni = fascicoDigitale.getQuadroTerreni();
    quadroTerreni.setParticellare(new Particellare());
    quadroTerreni.getParticellare().setVisibility(true);    
    quadroTerreni.setTitoloTerreni(richiestaTipoReportVO.getQuadro());
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    int size=0;
    Vector<ParticellaVO> particelle = anagFacadeClient.getElencoParticelleQuadroI1(
        anagAziendaVO.getIdAzienda(), codiceFotografia);
    if (particelle!=null) 
      size=particelle.size();
    
    if (size>0)
    {     
      
      Vector<Long> vIdTipoConduzione = new Vector<Long>();
      Vector<Long> vIdFonte = new Vector<Long>();
      BigDecimal[] totSupCatGraf = anagFacadeClient
        .getTotSupQuadroI1CatastaleAndGrafica(anagAziendaVO.getIdAzienda(),codiceFotografia);
      BigDecimal[] superfici = anagFacadeClient.getTotSupQuadroI1CondottaAndAgronomica(
          anagAziendaVO.getIdAzienda(),codiceFotografia);
      
      BigDecimal totSupUtilizzata = new BigDecimal(0);      
      quadroTerreni.getParticellare().setElencoParticelle(new ElencoParticelle());
      
      for (int i = 0; i < size; i++)
      {
        
        Particella particellaMd = new Particella();        
        ParticellaVO particella= particelle.get(i);
        particellaMd.setComune(particella.getDescComuneParticella());
        particellaMd.setSezione(particella.getSezione());
        if(Validator.isNotEmpty(particella.getFoglio()))
          particellaMd.setFoglio(new Long(particella.getFoglio()).toString());
        if(Validator.isNotEmpty(particella.getParticella()))
          particellaMd.setPart(new Long(particella.getParticella()).toString());
        particellaMd.setSub(particella.getSubalterno()); 
        
        BigDecimal supCatastale =new BigDecimal(0);
        try
        {
          supCatastale = new BigDecimal(particella.getSupCatastale());
        }
        catch(Exception e)
        {
          supCatastale = new BigDecimal(0);
        }
        particellaMd.setSupCatastale(Formatter.formatDouble4(supCatastale));
        
        if(Validator.isNotEmpty(particella.getIdTitoloPossesso()))
          particellaMd.setCond(particella.getIdTitoloPossesso().toString()); 
        
        if(!vIdTipoConduzione.contains(particella.getIdTitoloPossesso()))
          vIdTipoConduzione.add(particella.getIdTitoloPossesso());
        
        
        
        String uso = "";
        if(Validator.isNotEmpty(particella.getDescUtilizzoParticella()))
          uso += particella.getDescUtilizzoParticella();
        if(Validator.isNotEmpty(particella.getDescDestinazione()))
          uso += " - "+particella.getDescDestinazione();
        if(Validator.isNotEmpty(particella.getDescDettaglioUso()))
          uso += " - "+particella.getDescDettaglioUso();
        if(Validator.isNotEmpty(particella.getDescQualitaUso()))
          uso += " - "+particella.getDescQualitaUso();
        if(Validator.isNotEmpty(particella.getDescVarieta()))
          uso += " - "+particella.getDescVarieta();
        
        particellaMd.setUso(uso);
        
        
        String usoSec = "";
        if(Validator.isNotEmpty(particella.getDescUtilizzoParticellaSecondaria()))
          usoSec += particella.getDescUtilizzoParticellaSecondaria();
        if(Validator.isNotEmpty(particella.getDescDestinazioneSecondaria()))
          usoSec += " - "+particella.getDescDestinazioneSecondaria();
        if(Validator.isNotEmpty(particella.getDescDettaglioUsoSecondario()))
          usoSec += " - "+particella.getDescDettaglioUsoSecondario();
        if(Validator.isNotEmpty(particella.getDescQualitaUsoSecondaria()))
          usoSec += " - "+particella.getDescQualitaUsoSecondaria();
        if(Validator.isNotEmpty(particella.getDescVarietaSecondaria()))
          usoSec += " - "+particella.getDescVarietaSecondaria();
        
        particellaMd.setUsoSec(usoSec);
        
        BigDecimal supUtilizzata = new BigDecimal(0); 
        try
        {
          if(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.compareTo(particella.getIdTitoloPossesso()) == 0)
          {
            supUtilizzata = new BigDecimal(0);
            totSupUtilizzata=totSupUtilizzata.add(supUtilizzata);
          }
          else
          {
            supUtilizzata = new BigDecimal(particella.getSuperficieUtilizzata());
            totSupUtilizzata=totSupUtilizzata.add(supUtilizzata);
          }
        }
        catch(Exception e)
        {
          supUtilizzata = new BigDecimal(0);
        }
        particellaMd.setSupUtil(Formatter.formatDouble4(supUtilizzata));  
        
        BigDecimal supUtilizzataSec = new BigDecimal(0); 
        if(Validator.isNotEmpty(particella.getSuperficieUtilizzataSecondaria()))
          supUtilizzataSec = particella.getSuperficieUtilizzataSecondaria();
        
        particellaMd.setSupUtilSec(Formatter.formatDouble4(supUtilizzataSec));  
        
        
        BigDecimal supAgronomica = new BigDecimal(0);
        try
        {
          if(particella.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            supAgronomica = new BigDecimal(particella.getSuperficieCondotta());
          }
          else
          {
            supAgronomica = new BigDecimal(particella.getSupAgronomico());  
          }
          
        }
        catch(Exception e)
        {
          supAgronomica = new BigDecimal(0);
        }
        particellaMd.setSupAgron(Formatter.formatDouble4(supAgronomica));  
        
        particellaMd.setSupGis(Formatter.formatDouble4(particella.getSuperficieGrafica()));
        
        if(Validator.isNotEmpty(particella.getIdRotazioneColturale()))
          particellaMd.setCicloOrtivo(""+particella.getIdRotazioneColturale());
        
        if(Validator.isNotEmpty(particella.getIdPotenzaIrrigua()))
          particellaMd.setIrrigua(""+particella.getIdPotenzaIrrigua());
        
        String vincoli = "";
        if(Validator.isNotEmpty(particella.getvIdFonte()))
        {
          for(int j=0;j<particella.getvIdFonte().size();j++)
          {
            if(j!=0)
              vincoli +=",";
            
            vincoli +=""+particella.getvIdFonte().get(j);
            if(!vIdFonte.contains(particella.getvIdFonte().get(j)))
              vIdFonte.add(particella.getvIdFonte().get(j));
          }
        }
        
        particellaMd.setVincoli(vincoli);
        
        quadroTerreni.getParticellare().getElencoParticelle().getParticella().add(particellaMd);
        
       
      }
      quadroTerreni.getParticellare().setRigaTotali(new RigaTotali());
      quadroTerreni.getParticellare().getRigaTotali().setTotSupCatastale(
          Formatter.formatDouble4(totSupCatGraf[0]));
      quadroTerreni.getParticellare().getRigaTotali().setTotSupUtilizzata(
        Formatter.formatDouble4(totSupUtilizzata));
      quadroTerreni.getParticellare().getRigaTotali().setTotSupSau(
        Formatter.formatDouble4(superfici[1]));
      
      //Legenda
      String legendaConduzione = "";
      CodeDescription[] elencoTitoliPossesso = 
          anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
      boolean primoCond = true;
      for(int i=0;i<elencoTitoliPossesso.length;i++)
      {
        if(vIdTipoConduzione.contains(new Long(elencoTitoliPossesso[i].getCode().intValue())))
        {
          
          if(Validator.isEmpty(legendaConduzione))
            legendaConduzione += "Cond: ";
          if(Validator.isNotEmpty(legendaConduzione)
            && !primoCond)
          {
            legendaConduzione += ", ";
          }
          legendaConduzione += elencoTitoliPossesso[i].getCode() +" - ";
          legendaConduzione += elencoTitoliPossesso[i].getDescription();
          
          primoCond = false;
        }
      }
      
      quadroTerreni.getParticellare().setLegendaPart(legendaConduzione);
      
      
      
      String legendaRot = "";
      TipoRotazioneColturaleVO[] elencoRotazioneColturale  = anagFacadeClient
          .getListTipoRotazioneColturale("ID_ROTAZIONE_COLTURALE", dataInserimentoDichiarazione);
      for(int i=0;i<elencoRotazioneColturale.length;i++)
      {
        if(Validator.isEmpty(legendaRot))
          legendaRot += "Rot. colturale: ";
        if(Validator.isNotEmpty(legendaRot)
          && i!=0)
          legendaRot += ", ";
        legendaRot += elencoRotazioneColturale[i].getIdRotazioneColturale() +" - ";
        legendaRot += elencoRotazioneColturale[i].getDescrizione();
      }
      
      quadroTerreni.getParticellare().setLegendaRot(legendaRot);
      
      String legendaPotIrr = "";
      TipoPotenzialitaIrriguaVO[] elencoPotenzialitaIrrigua  = anagFacadeClient
          .getListTipoPotenzialitaIrrigua("ID_POTENZIALITA_IRRIGUA", dataInserimentoDichiarazione); 
      for(int i=0;i<elencoPotenzialitaIrrigua.length;i++)
      {
        if(Validator.isEmpty(legendaPotIrr))
          legendaPotIrr += "Pot. irrigua: ";
        if(Validator.isNotEmpty(legendaPotIrr)
            && i!=0)
          legendaPotIrr += ", ";
        legendaPotIrr += elencoPotenzialitaIrrigua[i].getIdPotenzialitaIrrigua() +" - ";
        legendaPotIrr += elencoPotenzialitaIrrigua[i].getDescrizione();
      }
      
      quadroTerreni.getParticellare().setLegendaIrrig(legendaPotIrr);
      
      
      String legendaVincoli = "";
      Vector<TipoFonteVO> vTipoFonte  = gaaFacadeClient.getElencoAllTipoFonte(); 
      for(int i=0;i<vTipoFonte.size();i++)
      {
        TipoFonteVO tipoFonteVO = vTipoFonte.get(i);
        if(vIdFonte.contains(tipoFonteVO.getIdFonte()))
        {
          if(Validator.isEmpty(legendaVincoli))
            legendaVincoli += "Vincoli: ";
          if(Validator.isNotEmpty(legendaVincoli) && i!=0)
            legendaVincoli += ", ";
          legendaVincoli += ""+tipoFonteVO.getIdFonte()+" - ";
          legendaVincoli += tipoFonteVO.getDescrizione();
        }
      }
      
      quadroTerreni.getParticellare().setLegendaVincoli(legendaVincoli);
      
      
      
      
    }
    else
    {
      quadroTerreni.getParticellare().setSezioneVuota(true);
    }
    
    
    
  }
  
}
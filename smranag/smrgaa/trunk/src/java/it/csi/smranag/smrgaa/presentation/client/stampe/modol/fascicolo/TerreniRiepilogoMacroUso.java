package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroTerreni;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Riepiloghi;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Riepilogo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaRiepilogo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigheRiepilogo;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniRiepilogoMacroUso extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "TERRENI_RIEPILOGO_MACROUSO";

  public TerreniRiepilogoMacroUso() throws IOException, SolmrException
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
    
    if(quadroTerreni.getRiepiloghi() == null)
    {
      quadroTerreni.setRiepiloghi(new Riepiloghi());    
      quadroTerreni.getRiepiloghi().setVisibility(true);
    }
    
    Riepilogo riepilogo = new Riepilogo();
    quadroTerreni.getRiepiloghi().getRiepilogo().add(riepilogo); 
    riepilogo.setNomeRiepilogo(richiestaTipoReportVO.getQuadro());
    riepilogo.setIntestazione1(richiestaTipoReportVO.getSezione());
    riepilogo.setIntestazione2("n. particelle");
    riepilogo.setIntestazione3("Sup. totale");
    
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    
    int size = 0;
    
    
    Vector<BaseCodeDescription> terreni = anagFacadeClient
      .getTerreniQuadroI5(anagAziendaVO.getIdAzienda(), 
          dataInserimentoDichiarazione, codiceFotografia);
    
    size = terreni.size();
    
    if (size > 0)
    {
      riepilogo.setRigheRiepilogo(new RigheRiepilogo());
      for (int i = 0; i < size; i++)
      {        
        BaseCodeDescription temp = terreni.get(i);
        RigaRiepilogo rigaRiepilogo = new RigaRiepilogo();
        
        rigaRiepilogo.setValore1(temp.getDescription());
        rigaRiepilogo.setValore2(new Long(temp.getCode()).toString());
        rigaRiepilogo.setValore3(Formatter.formatDouble4((BigDecimal)temp.getItem()));
        
        riepilogo.getRigheRiepilogo().getRigaRiepilogo().add(rigaRiepilogo);
      }
    }
    else
    {
      riepilogo.setSezioneVuota(true);
    }    
  }
}
package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroUnitaVitate;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaTotaliUv;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.RigaUv;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.UnitaVitate;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaDichiarataVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.services.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class UVElenco extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "UV_ELENCO";

  public UVElenco() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {   
    if(fascicoDigitale.getQuadroUnitaVitate() == null)
      fascicoDigitale.setQuadroUnitaVitate(new QuadroUnitaVitate());    
    QuadroUnitaVitate quadroUnitaViatate = fascicoDigitale.getQuadroUnitaVitate();
    
    quadroUnitaViatate.setUnitaVitate(new UnitaVitate());
    quadroUnitaViatate.getUnitaVitate().setVisibility(true);    
    quadroUnitaViatate.setTitoloUnitaVitate(richiestaTipoReportVO.getQuadro());
      
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");   
    
    String orderBy[] = null;
    if (idDichiarazioneConsistenza == null)
    {
      orderBy = new String[] {
          SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY,
          SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC,
          SolmrConstants.ORDER_BY_PROGR_UNAR_ASC,
          SolmrConstants.ORDER_BY_UV_DATA_FINE_VALIDITA_DESC };
    }
    else
    {
      orderBy = new String[] {
          SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY,
          SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC,
          SolmrConstants.ORDER_BY_PROGR_UNAR_ASC,
          SolmrConstants.ORDER_BY_UV_DICHIARATA_DATA_FINE_VALIDITA_DESC };
    }
    FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = null;
    filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
    filtriUnitaArboreaRicercaVO.setOrderBy(null);
    if (idDichiarazioneConsistenza == null)
    {
      filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(new Long(-1));
    }
    else
    {
      filtriUnitaArboreaRicercaVO
          .setIdPianoRiferimento(idDichiarazioneConsistenza);
    }
    StoricoParticellaVO partVO[] = anagFacadeClient
        .searchStoricoUnitaArboreaByParametersForStampa(
            anagAziendaVO.getIdAzienda(),
            filtriUnitaArboreaRicercaVO, orderBy);

    if (partVO == null || partVO.length == 0)
    {
      quadroUnitaViatate.getUnitaVitate().setSezioneVuota(true);
    }
    else
    {
      BigDecimal totArea = new BigDecimal(0);
      for (int i = 0; i < partVO.length; i++)
      {        
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) partVO[i];
        RigaUv rigaUv = new RigaUv();
        if (storicoParticellaVO != null)
        {
          StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
          UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = null;
          if (idDichiarazioneConsistenza == null)
            // alla data odierna
            storicoUnitaArboreaVO = storicoParticellaVO
                .getStoricoUnitaArboreaVO();
          else
            // alla dichiarazione
            unitaArboreaDichiarataVO = storicoParticellaVO
                .getUnitaArboreaDichiarataVO();

          if (storicoUnitaArboreaVO != null)
          {
            
            String comune = null;
            if (storicoParticellaVO.getComuneParticellaVO() != null)
            {
              comune = StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                  .getDescom()+ " (");
              comune += StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                      .getSiglaProv()) + ")";
            }
            rigaUv.setComune(comune);
            rigaUv.setSezione(storicoParticellaVO.getSezione());
            rigaUv.setFoglio(storicoParticellaVO.getFoglio());
            rigaUv.setPart(storicoParticellaVO.getParticella());
            rigaUv.setSub(storicoParticellaVO.getSubalterno());
            rigaUv.setProgr(storicoUnitaArboreaVO.getProgrUnar());
            rigaUv.setSupVitata(StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
            totArea = totArea.add(new BigDecimal(storicoUnitaArboreaVO.getArea().replace(",", ".")));
            
            String vitigno = "";
            if (storicoUnitaArboreaVO.getIdVarieta() != null)
            {              
              vitigno += "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+ "] ";
              vitigno += storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione();
            }
            rigaUv.setVitigno(vitigno);
            
            if(storicoUnitaArboreaVO.getTipoTipologiaVinoVO() !=null)
            { 
              rigaUv.setIdoneita(storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getDescrizione());
            }
            
            if(storicoUnitaArboreaVO.getDataImpianto() != null)
            {
              rigaUv.setDataImpianto(DateUtils.formatDate(storicoUnitaArboreaVO.getDataImpianto()));
            }
          }
          else
          {
            String comune = null;
            if (storicoParticellaVO.getComuneParticellaVO() != null)
            {
              comune = StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                  .getDescom()+ " (");
              comune += StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                      .getSiglaProv()) + ")";
            }
            rigaUv.setComune(comune);
            
            rigaUv.setSezione(storicoParticellaVO.getSezione());
            rigaUv.setFoglio(storicoParticellaVO.getFoglio());
            rigaUv.setPart(storicoParticellaVO.getParticella());
            rigaUv.setSub(storicoParticellaVO.getSubalterno());
            
            rigaUv.setProgr(unitaArboreaDichiarataVO.getProgrUnar());
            rigaUv.setSupVitata(StringUtils.parseSuperficieField(unitaArboreaDichiarataVO.getArea()));
            totArea = totArea.add(new BigDecimal(unitaArboreaDichiarataVO.getArea().replace(",", ".")));
            
            String vitigno = "";
            if (unitaArboreaDichiarataVO.getIdVarieta() != null)
            {              
              vitigno += "["+unitaArboreaDichiarataVO.getTipoVarietaVO().getCodiceVarieta()+ "] ";
              vitigno += unitaArboreaDichiarataVO.getTipoVarietaVO().getDescrizione();
            }
            rigaUv.setVitigno(vitigno);
            
            if(unitaArboreaDichiarataVO.getTipoTipologiaVinoVO() !=null)
            { 
              rigaUv.setIdoneita(unitaArboreaDichiarataVO.getTipoTipologiaVinoVO().getDescrizione());
            }
            
            if(unitaArboreaDichiarataVO.getDataImpianto() != null)
            {
              rigaUv.setDataImpianto(DateUtils.formatDate(unitaArboreaDichiarataVO.getDataImpianto()));
            }
          }

        }
        quadroUnitaViatate.getUnitaVitate().getRigaUv().add(rigaUv);
        quadroUnitaViatate.getUnitaVitate().setRigaTotaliUv(new RigaTotaliUv());
        quadroUnitaViatate.getUnitaVitate().getRigaTotaliUv()
          .setTotSupVite(Formatter.formatDouble4(totArea));
      }
      
    }
    
    
   
    
  }
  
  
  
  
}
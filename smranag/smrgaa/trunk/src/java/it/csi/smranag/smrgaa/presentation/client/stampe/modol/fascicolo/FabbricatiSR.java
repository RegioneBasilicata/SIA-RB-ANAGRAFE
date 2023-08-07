package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fabbricati;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fabbricato;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.ParticellaFab;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Particelle;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroFabbricati;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class FabbricatiSR extends SubReportModol
{
  private final static String CODICE_SUB_REPORT = "FABBRICATI";

  public FabbricatiSR() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    QuadroFabbricati quadroFabbricati = new QuadroFabbricati();
    fascicoDigitale.setQuadroFabbricati(quadroFabbricati);
    quadroFabbricati.setVisibility(true);
    
    quadroFabbricati.setTitoloFabbricati(richiestaTipoReportVO.getQuadro());
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    Vector<FabbricatoVO> vFabbricati = gaaFacadeClient
      .getStampaFabbricati(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione); 
    
    if (vFabbricati != null)
    {
      
      quadroFabbricati.setFabbricati(new Fabbricati());

      for (int i=0;i<vFabbricati.size();i++)
      {
        FabbricatoVO fabbricato = vFabbricati.get(i);
        Fabbricato fabbricatoMd = new Fabbricato();

        fabbricatoMd.setTipo(fabbricato.getDescrizioneTipoFabbricato());
        fabbricatoMd.setAnnoRiatt(fabbricato.getAnnoCostruzioneFabbricato());
        fabbricatoMd.setUnitaMisuraDimensione("Dimensione ("+fabbricato.getUnitaMisura()+")");
        fabbricatoMd.setDimensione(StringUtils.parseDoubleField(fabbricato
            .getDimensioneFabbricato()));
        fabbricatoMd.setSup(StringUtils.parseDoubleField(fabbricato
            .getSuperficieFabbricato()));

        int numParticelle = 0;
        Vector<ParticellaVO> particelle = anagFacadeClient.getFabbricatiParticelle(fabbricato
            .getIdFabbricato(), dataInserimentoDichiarazione);
        if (particelle != null)
          numParticelle = particelle.size();
        
        fabbricatoMd.setParticelle(new Particelle());
        if (numParticelle > 0)
        {          
          for (int j = 0; j < numParticelle; j++)
          {
            ParticellaFab particella = new ParticellaFab();
            ParticellaVO pV = (ParticellaVO) particelle.get(j);
            String comune = pV.getDescComuneParticella()+" ("+pV.getSiglaProvinciaParticella()+")";
            particella.setComune(comune);
            particella.setSezione(pV.getSezione());
            particella.setFoglio(pV.getStrFoglio());
            particella.setPart(pV.getStrParticella());
            particella.setSub(pV.getSubalterno());
            
            fabbricatoMd.getParticelle().getParticellaFab().add(particella);
          }
        }
        else
        {
          fabbricatoMd.getParticelle().setNoParticelle(true);
        }

        
        quadroFabbricati.getFabbricati().getFabbricato().add(fabbricatoMd);
      }

    }
    else
    {
      quadroFabbricati.setSezioneVuota(true);
    }
    
    
    
    
    
  }
}
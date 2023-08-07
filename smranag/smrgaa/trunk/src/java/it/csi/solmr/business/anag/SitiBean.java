package it.csi.solmr.business.anag;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.smranags.sitiserv.dto.SitiPlavVO;
import it.csi.smranags.sitiserv.exception.InvalidParameterException;
import it.csi.smranags.sitiserv.interfacecsi.SitiServServiceCSIInterface;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.integration.anag.ParticellaCertificataDAO;
import it.csi.solmr.util.SolmrLogger;


import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/Siti",mappedName="comp/env/solmr/anag/Siti")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
public class SitiBean implements SitiLocal
{

  private static final long serialVersionUID = -2650292931541050563L;

  SessionContext sessionContext;

  String sitiServPortaDelegataXML = (String) SolmrConstants
      .get("FILE_PD_SITISERV");
  private transient ParticellaCertificataDAO particellaCertificataDAO = null;


  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  private void initializeDAO() throws EJBException
  {
    try
    {
      particellaCertificataDAO = new ParticellaCertificataDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  /**
   * 
   * @throws Exception
   * @return SitiServServiceCSIInterface
   */
  private SitiServServiceCSIInterface getSitiClient() throws Exception
  {
    // caricamento porta delegata
    InfoPortaDelegata informazioniPortaDelegata = PDConfigReader
        .read(getClass().getResourceAsStream(sitiServPortaDelegataXML));
    return (SitiServServiceCSIInterface) PDProxy
        .newInstance(informazioniPortaDelegata);
  }

  public Boolean serviceGetDataLavParticelle(ParticellaVO[] particelle)
      throws SolmrException, Exception
  {
    try
    {
      if (particelle != null && particelle.length != 0)
      {
        SitiPlavVO[] part = new SitiPlavVO[particelle.length];
        for (int i = 0; i < part.length; i++)
        {
          // Creo un oggetto di tipo SitiPlavVO e lo valorizzo con i dati
          // catastali restituiti da anagrafe
          part[i] = new SitiPlavVO();
          part[i]
              .setIdStoricoParticella(particelle[i].getIdStoricoParticella());
          if (particelle[i].getIstatComuneParticella() != null)
          {
            part[i].setIstatProv(particelle[i].getIstatComuneParticella()
                .substring(0, 3));
            part[i].setIstatCom(particelle[i].getIstatComuneParticella()
                .substring(3, 6));
          }
          part[i].setSezione(particelle[i].getSezione());
          part[i].setFoglio(particelle[i].getFoglio());
          if (particelle[i].getParticella() != null)
            part[i].setParticella(particelle[i].getParticella().toString());
          part[i].setSubalterno(particelle[i].getSubalterno());
        }
        // Interrogo siti per farmi restituiore i dati
        SitiPlavVO[] sitiPlav = getSitiClient().serviceGetDataLavParticelle(
            part);

        // Aggiorno i dati sul db di anagrafe
        particellaCertificataDAO.updateParticellaCertificata(sitiPlav,
            particelle);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (InvalidParameterException i)
    {
      SolmrLogger.fatal(this,
          "serviceGetDataLavParticelle - InvalidParameterException: "
              + i.getMessage() + " " + i.getParameterName());
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
    }
    catch (java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "serviceGetDataLavParticelle - IOException: "
          + ioex.getMessage());
      throw new Exception((String) AnagErrors
          .get("ERR_SITISERV_TO_CONNECT"));
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "serviceGetDataLavParticelle - Exception: "
          + e.getMessage());
      throw new Exception((String) AnagErrors
          .get("ERR_SITISERV_TO_CONNECT"));
    }
    return new Boolean(true);
  }
  
  
  public String serviceGetParticellaUrl3D(String istatComune,
      String sezione, String foglio, String particella, String subalterno)
    throws SolmrException, Exception
  {
    String url3D = null;
    try
    {
      // Interrogo siti per farmi restituiore i dati
      url3D = getSitiClient().serviceGetParticellaUrl3D(
          istatComune, sezione, foglio, particella, subalterno);
    
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (InvalidParameterException i)
    {
      SolmrLogger.fatal(this,
          "serviceGetParticellaUrl3D - InvalidParameterException: "
              + i.getMessage() + " " + i.getParameterName());
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
    }
    catch (java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "serviceGetParticellaUrl3D - IOException: "
          + ioex.getMessage());
      throw new Exception((String) AnagErrors
          .get("ERR_SITISERV_TO_CONNECT"));
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "serviceGetParticellaUrl3D - Exception: "
          + e.getMessage());
      throw new Exception((String) AnagErrors
          .get("ERR_SITISERV_TO_CONNECT"));
    }
    
    return url3D;
  }

}

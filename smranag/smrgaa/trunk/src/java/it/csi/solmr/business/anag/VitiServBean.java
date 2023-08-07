package it.csi.solmr.business.anag;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.smrvit.vitiserv.dto.diritto.DirittoVO;
import it.csi.smrvit.vitiserv.interfacecsi.IVitiservCSIInterface;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.SolmrLogger;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;


@Stateless(name="comp/env/solmr/anag/VitiServ",mappedName="comp/env/solmr/anag/VitiServ")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
public class VitiServBean implements VitiServLocal 
{
	/**
   * serialVersionUID
   */
  private static final long serialVersionUID = -8462119322809596274L;

  SessionContext sessionContext;

	String smrCommPortaDelegataXML = (String)SolmrConstants.get("FILE_PD_VITI_SERV");

	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	}

	
	private IVitiservCSIInterface getInstance() 
	  throws Exception
  {
	  InfoPortaDelegata informazioniPortaDelegata = PDConfigReader.read(getClass().getResourceAsStream(smrCommPortaDelegataXML));
    return (IVitiservCSIInterface) PDProxy.newInstance(informazioniPortaDelegata);
  }

	
  public long[] vitiservSearchListIdDiritto(long idAzienda, boolean flagAttivi, int tipoOrdinamento)
    throws Exception
  {
    try
    {
      return getInstance().vitiservSearchListIdDiritto(idAzienda, flagAttivi, tipoOrdinamento);
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, e.getMessage());
      throw e;
    }
  }

  public DirittoVO[] vitiservGetListDirittoByIdRange(long[] ids, int tipoRisultato)
    throws Exception
  {
    try
    {
      return getInstance().vitiservGetListDirittoByIdRange(ids, tipoRisultato);  
    }
    catch(Exception e)
    {
      SolmrLogger.fatal(this, e.getMessage());
      throw e;
    }   
  }
	
}

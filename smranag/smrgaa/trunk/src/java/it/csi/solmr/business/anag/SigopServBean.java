package it.csi.solmr.business.anag;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.sigop.dto.services.PagamentiErogatiVO;
import it.csi.sigop.dto.services.RecuperiPregressiVO;
import it.csi.sigop.dto.services.SchedaCreditoVO;
import it.csi.sigop.interfacecsi.services.SigopServiceCSIInterface;
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

@Stateless(name="comp/env/solmr/anag/SigopServ",mappedName="comp/env/solmr/anag/SigopServ")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
public class SigopServBean implements SigopServLocal 
{
	

  /**
   * 
   */
  private static final long serialVersionUID = 8852195877410138171L;
  
  

  SessionContext sessionContext;

	String sigopServPortaDelegataXML = (String)SolmrConstants.get("FILE_PD_SIGOP_SERV");
	Integer extIdProcedimento = (Integer)SolmrConstants.EXT_ID_PROCEDIMENTO;
	String sottoProcedimento = SolmrConstants.SOTTO_PROCEDIMENTO;

	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	}

	
	private SigopServiceCSIInterface getInstance() 
	  throws Exception
  {
	  InfoPortaDelegata informazioniPortaDelegata = PDConfigReader.read(getClass().getResourceAsStream(sigopServPortaDelegataXML));
    return (SigopServiceCSIInterface) PDProxy.newInstance(informazioniPortaDelegata);
  }

	public SchedaCreditoVO[] sigopservVisualizzaDebiti(String cuaa)
    throws Exception
  {
    try
    {
      return getInstance().serviceVisualizzaDebiti(cuaa, 1, 2, 3, 4);
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, e.getMessage());
      throw e;
    }
  }
  
  public PagamentiErogatiVO sigopservEstraiPagamentiErogati(String cuaa, String settore,
      Integer anno)
    throws Exception
  {
    try
    {
      return getInstance().serviceEstraiPagamentiErogati(cuaa, settore, anno);
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, e.getMessage());
      throw e;
    }
  }
  
  public RecuperiPregressiVO sigopservEstraiRecuperiPregressi(String cuaa, String settore,
      Integer anno)
    throws Exception
  {
    try
    {
      return getInstance().serviceEstraiRecuperiPregressi(cuaa, settore, anno);
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, e.getMessage());
      throw e;
    }
  }
	
}

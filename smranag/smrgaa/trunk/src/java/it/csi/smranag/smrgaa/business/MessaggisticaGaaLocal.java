package it.csi.smranag.smrgaa.business;

import it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio;
import it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi;
import it.csi.papua.papuaserv.exception.messaggistica.LogoutException;
import it.csi.solmr.exception.SolmrException;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;

@Local
public interface MessaggisticaGaaLocal 
{
  
  
  public void confermaLetturaMessaggio(long idElencoMessaggi, String codiceFiscale) 
      throws SolmrException;
  public byte[] getAllegato(long idAllegato) 
      throws SolmrException;
  public ListaMessaggi getListaMessaggi(int idProcedimento, String codiceRuolo, 
      String codiceFiscale, int tipoMessaggio, Boolean letto, Boolean obbligatorio, Boolean visibile ) 
   throws SolmrException, LogoutException;
  public DettagliMessaggio getDettagliMessaggio(long idElencoMessaggi, String codiceFiscale) 
      throws SolmrException;
  
}

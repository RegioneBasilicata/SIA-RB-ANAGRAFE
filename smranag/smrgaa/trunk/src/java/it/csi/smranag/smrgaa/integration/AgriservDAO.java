package it.csi.smranag.smrgaa.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import it.csi.smranag.smrgaa.dto.AgriservChiamataVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.SolmrLogger;
/**
 * DAO di accesso alle tabelle db_agriserv per l'elenco chiamate/espositori del
 * servizio Agriserv
 * Oct 15, 2008
 * @author TOBECONFIG
 *
 */
public class AgriservDAO extends BaseDAO
{
  public AgriservDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public AgriservDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }
/**
 * Restituisce l'elenco delle chiamate con relativo espositore del servizio
 * per le chiamate ai metodi di agriserv identificati tramite il codice chiamata
 * @param codice
 * @return
 * @throws DataAccessException
 */
  public AgriservChiamataVO[] getElencoChiamateAgriservByCodiceChiamata(String codice)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    String query = null;
    AgriservChiamataVO agriservChiamataVO = null;
    AgriservChiamataVO chiamate[] = null;
    Vector<AgriservChiamataVO> vChiamate = null;
    try
    {
      SolmrLogger.debug(this,
          "[AgriservDAO::getElencoChiamateAgriservByCodiceChiamata] BEGIN.");

      conn = getDatasource().getConnection();
      query = "   SELECT   "
          + "     AE.CODICE_ESPOSITORE,  "
          + "     AE.NOME_JNDI_PA,   "
          + "     AE.URL_PD,   "
          + "     ATC.DESCRIZIONE AS DESCRIZIONE_CHIAMATA,   "
          + "     ATC.CODICE AS CODICE_CHIAMATA,  "
          + "     AE.DESCRIZIONE_ESPOSITORE "
          + "   FROM   "
          + "     DB_AGRISERV_CHIAMATA AC,   "
          + "     DB_AGRISERV_ESPOSITORE AE,   "
          + "     DB_AGRISERV_TIPO_CHIAMATA ATC  "
          + "   WHERE    "
          + "     AC.ID_AGRISERV_ESPOSITORE = AE.ID_AGRISERV_ESPOSITORE    AND   "
          + "     AC.ID_AGRISERV_TIPO_CHIAMATA = ATC.ID_AGRISERV_TIPO_CHIAMATA    AND  "
          + "     ATC.CODICE = ? ";
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[AgriservDAO::getElencoChiamateAgriservByCodiceChiamata] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this,"-- codice ="+codice);
      stmt.setString(1, codice);
      ResultSet rs = stmt.executeQuery();
      vChiamate = new Vector<AgriservChiamataVO>();
      while (rs.next())
      {
        agriservChiamataVO=new AgriservChiamataVO();
        agriservChiamataVO.setCodiceEspositore(rs
            .getString("CODICE_ESPOSITORE"));
        agriservChiamataVO.setNomeJndiPA(rs.getString("NOME_JNDI_PA"));
        agriservChiamataVO.setUrlPD(rs.getString("URL_PD"));
        agriservChiamataVO.setDescrizioneChiamata(rs
            .getString("DESCRIZIONE_CHIAMATA"));
        agriservChiamataVO.setCodiceChiamata(rs.getString("CODICE_CHIAMATA"));
        agriservChiamataVO.setDescrizioneChiamata(rs.getString("DESCRIZIONE_ESPOSITORE"));
        vChiamate.add(agriservChiamataVO);
      }
      int size = vChiamate.size();
      if (size != 0)
      {
        chiamate = (AgriservChiamataVO[]) vChiamate
            .toArray(new AgriservChiamataVO[size]);
      }
      return chiamate;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vChiamate", vChiamate),
          new Variabile("agriservChiamataVO", agriservChiamataVO),
          new Variabile("chiamate", chiamate) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codice", codice) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AgriservDAO::getElencoChiamateAgriservByCodiceChiamata] ", t, query,
          variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[AgriservDAO::getElencoChiamateAgriservByCodiceChiamata] END.");
    }
  }

}

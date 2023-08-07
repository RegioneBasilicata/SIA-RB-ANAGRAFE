package it.csi.smranag.smrgaa.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

public class AnagraficaDAO extends it.csi.solmr.integration.BaseDAO
{

  public AnagraficaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public AnagraficaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * 
   * @param pratiche
   * @return HashMap
   * @throws DataAccessException
   */
  public HashMap<String, String[]> getDatiAziendaDettaglioPraticheParticella(
      PraticaProcedimentoVO pratiche[]) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf1 = new StringBuffer();
    StringBuffer queryBuf2 = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<String, String[]> mapDatiAzienda = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagraficaDAO::getDatiAziendaDettaglioPraticheParticella] BEGIN.");

      queryBuf1
          .append(" SELECT "
              + " AA.ID_AZIENDA, DC.ID_DICHIARAZIONE_CONSISTENZA, AA.CUAA, AA.DENOMINAZIONE "
              + " FROM "
              + " DB_ANAGRAFICA_AZIENDA AA, "
              + " DB_DICHIARAZIONE_CONSISTENZA DC "
              + " WHERE "
              + " AA.DATA_INIZIO_VALIDITA<=DC.DATA_INSERIMENTO_DICHIARAZIONE "
              + " AND NVL(AA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY'))> DC.DATA_INSERIMENTO_DICHIARAZIONE "
              + "             AND DC.ID_AZIENDA = AA.ID_AZIENDA "
              + " AND DC.ID_DICHIARAZIONE_CONSISTENZA IN (");
      queryBuf2
          .append("SELECT "
              + " AA.ID_AZIENDA, NULL AS ID_DICHIARAZIONE_CONSISTENZA, AA.CUAA, AA.DENOMINAZIONE "
              + " FROM " + " DB_ANAGRAFICA_AZIENDA AA " + " WHERE "
              + "   AA.DATA_FINE_VALIDITA IS NULL "
              + "   AND AA.ID_AZIENDA IN ( ");

      int len = pratiche == null ? 0 : pratiche.length;
      boolean isFirstAzienda = true;
      boolean isFirstDC = true;
      for (int i = 0; i < len; ++i)
      {
        Long idDichiarazioneConsistenza = pratiche[i]
            .getIdDichiarazioneConsistenza();
        if (idDichiarazioneConsistenza != null && idDichiarazioneConsistenza.longValue()>0)
        {
          if (!isFirstDC)
          {
            queryBuf1.append(",");
          }
          queryBuf1.append(idDichiarazioneConsistenza.longValue());
          isFirstDC = false;
        }
        else
        {
          if (!isFirstAzienda)
          {
            queryBuf2.append(",");
          }
          queryBuf2.append(pratiche[i].getIdAzienda());
          isFirstAzienda = false;
        }
      }
      if (isFirstDC) // Nessuna DC
      {
        queryBuf1.append("null"); // query = ... IN (null) così evito syntax
        // error
      }
      if (isFirstAzienda) // Nessuna Azienda
      {
        queryBuf2.append("null"); // query = ... IN (null) così evito syntax
        // error
      }
      queryBuf1.append(") UNION ");
      queryBuf2.append(")");
      queryBuf2.append(" ORDER BY 1,2");
      query = queryBuf1.append(queryBuf2).toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[AnagraficaDAO::getDatiAziendaDettaglioPraticheParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      mapDatiAzienda = new HashMap<String, String[]>();
      while (rs.next())
      {
        String key = rs.getString("ID_AZIENDA") + "-"
            + rs.getString("ID_DICHIARAZIONE_CONSISTENZA"); // XXXX_YYYY con
        // YYYY che può
        // corrispondere anche a
        // "null" es 12_null
        String dati[] = new String[]
        { rs.getString("CUAA"), rs.getString("DENOMINAZIONE") };
        mapDatiAzienda.put(key, dati);
      }
      return mapDatiAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("mapDatiAzienda", mapDatiAzienda) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("pratiche", pratiche), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagraficaDAO::getDatiAziendaDettaglioPraticheParticella] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
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
          "[AnagraficaDAO::getDatiAziendaDettaglioPraticheParticella] END.");
    }
  }
}

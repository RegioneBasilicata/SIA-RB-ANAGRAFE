package it.csi.smranag.smrgaa.util;

import java.io.Serializable;
import java.util.Vector;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.search.paginazione.IPaginazione;
import it.csi.smranag.smrgaa.dto.search.paginazione.PaginazioneGruppo;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;

/**
 * Classe di utilità per la gestione della paginazione. Esegue tutti i calcoli
 * necessari per ricavare gli elementi da visualizzare e i gruppi delle pagine
 * 
 * @author TOBECONFIG
 * 
 */
public class PaginazioneUtils implements Serializable
{
  public static final String BLK_NOME_ELENCO            = "blkElenco";
  public static final String BLK_NOME_RIGA              = "blkRiga";
  public static final String BLK_NOME_GRUPPO_PRECEDENTE = "blkGruppoPrecedente";
  public static final String BLK_NOME_GRUPPO_SUCCESSIVO = "blkGruppoSuccessivo";
  public static final String BLK_NOME_GRUPPO_CORRENTE   = "blkGruppoCorrente";
  public static final String BLK_NOME_PAGINA_SUCCESSIVA = "blkPaginaSuccessiva";
  public static final String BLK_NOME_PAGINA_PRECEDENTE = "blkPaginaPrecedente";
  private String             blkNomeElenco              = BLK_NOME_ELENCO;
  private String             blkNomeRiga                = BLK_NOME_RIGA;
  private String             blkGruppoPrecedente        = BLK_NOME_GRUPPO_PRECEDENTE;
  private String             blkGruppoSuccessivo        = BLK_NOME_GRUPPO_SUCCESSIVO;
  private String             blkGruppoCorrente          = BLK_NOME_GRUPPO_CORRENTE;
  private String             blkPaginaSuccessiva        = BLK_NOME_PAGINA_SUCCESSIVA;
  private String             blkPaginaPrecedente        = BLK_NOME_PAGINA_PRECEDENTE;

  /** serialVersionUID */
  private static final long  serialVersionUID           = -387244610199141322L;
  private long               ids[]                      = null;
  private int                numeroRighePerPagina;
  private int                ultimaPaginaValida;
  private String             paginaCorrenteRichiesta    = null;
  private String             nomeErrorPaginaCorrente    = null;
  private int                totaleRighe;
  private int                totalePagine;
  private int                paginaCorrente;
  private ValidationErrors   errors                     = null;
  private int                primoElementoPaginaCorrente;
  private int                ultimoElementoPaginaCorrente;
  private int                numPaginePerGruppo;
  private IPaginazione       righe[];
  private PaginazioneGruppo  gruppi[];

  public IPaginazione[] getRighe()
  {
    return righe;
  }

  public void setRighe(IPaginazione[] righe)
  {
    this.righe = righe;
  }

  /**
   * Restituisce una istanza della classe per i dati passati
   * 
   * @param ids
   * @param numeroRighePerPagina
   * @param ultimaPaginaValida
   * @param paginaCorrenteRichiesta
   * @param nomeErrorPaginaCorrente
   * @return
   */
  public static final PaginazioneUtils newInstance(long ids[],
      int numeroRighePerPagina, int ultimaPaginaValida,
      String paginaCorrenteRichiesta, String nomeErrorPaginaCorrente)
  {
    return new PaginazioneUtils(ids, numeroRighePerPagina, ultimaPaginaValida,
        paginaCorrenteRichiesta, nomeErrorPaginaCorrente);
  }
  
  /**
   * Restituisce una istanza della classe per i dati passati
   * 
   * @param ids
   * @param numeroRighePerPagina
   * @param ultimaPaginaValida
   * @param paginaCorrenteRichiesta
   * @param nomeErrorPaginaCorrente
   * @return
   */
  public static final PaginazioneUtils newInstance(int numRecordTot,
      int numeroRighePerPagina, int ultimaPaginaValida,
      String paginaCorrenteRichiesta, String nomeErrorPaginaCorrente)
  {
    return new PaginazioneUtils(numRecordTot, numeroRighePerPagina, ultimaPaginaValida,
        paginaCorrenteRichiesta, nomeErrorPaginaCorrente);
  }
  
  
  /**
   * Restituisce una istanza della classe per i dati passati
   * 
   * @param numRecord
   * @param numeroRighePerPagina
   * @param paginaCorrente
   * @return
   */
  public static final PaginazioneUtils newInstance(int numRecord, int numeroRighePerPagina,int paginaCorrente)
  {
    return new PaginazioneUtils(numRecord,numeroRighePerPagina,paginaCorrente);
  }
  
  /**
   * Costruttore usato dalla ricerca terreni
   * 
   * @param numRecord
   * @param numeroRighePerPagina
   * @param paginaCorrente
   */
  protected PaginazioneUtils(int numRecord, int numeroRighePerPagina,int paginaCorrente)
  {
    this.numeroRighePerPagina = numeroRighePerPagina;
    this.totaleRighe=numRecord;
    this.totalePagine = this.totaleRighe / numeroRighePerPagina;
    this.paginaCorrente=paginaCorrente;
    if (totaleRighe % numeroRighePerPagina > 0) ++totalePagine;
    calcolaPrimoUltimo();
  }


  /**
   * Costruttore
   * 
   * @param ids
   * @param numeroRighePerPagina
   * @param ultimaPaginaValida
   * @param paginaCorrenteRichiesta
   * @param nomeErrorPaginaCorrente
   */
  protected PaginazioneUtils(long ids[], int numeroRighePerPagina,
      int ultimaPaginaValida, String paginaCorrenteRichiesta,
      String nomeErrorPaginaCorrente)
  {
    this.ids = ids;
    this.numeroRighePerPagina = numeroRighePerPagina;
    this.paginaCorrenteRichiesta = paginaCorrenteRichiesta;
    this.nomeErrorPaginaCorrente = nomeErrorPaginaCorrente;
    this.totaleRighe = ids == null ? 0 : ids.length;
    this.totalePagine = this.totaleRighe / numeroRighePerPagina;
    this.numPaginePerGruppo = new Long(SolmrConstants.NUM_PAGINE_PER_GRUPPO)
        .intValue();
    if (totaleRighe % numeroRighePerPagina > 0)
    {
      ++totalePagine;
    }
    if (paginaCorrenteRichiesta != null)
    {
      try
      {
        paginaCorrente = new Long(this.paginaCorrenteRichiesta).intValue();
        if (paginaCorrente < 1 || paginaCorrente > totalePagine)
        {
          errors = new ValidationErrors();
          errors.add(nomeErrorPaginaCorrente, new ValidationError(
              "Il numero della pagina deve essere compreso tra 1 e "
                  + totalePagine));
        }
      }
      catch (Throwable e)
      {
        errors = new ValidationErrors();
        errors.add(nomeErrorPaginaCorrente, new ValidationError(
        "Numero non valido"));
      }
    }
    else
    {
      paginaCorrente = 1;
    }
    this.ultimaPaginaValida = safePage(ultimaPaginaValida);
    paginaCorrente = getPaginaCorrenteEffettiva();
    calcolaPrimoUltimo();
    setPagesForFastChoice();
  }
  
  /**
   * Costruttore
   * 
   * @param ids
   * @param numeroRighePerPagina
   * @param ultimaPaginaValida
   * @param paginaCorrenteRichiesta
   * @param nomeErrorPaginaCorrente
   */
  protected PaginazioneUtils(int numRecord, int numeroRighePerPagina,
      int ultimaPaginaValida, String paginaCorrenteRichiesta,
      String nomeErrorPaginaCorrente)
  {
    this.ids = null;
    this.numeroRighePerPagina = numeroRighePerPagina;
    this.paginaCorrenteRichiesta = paginaCorrenteRichiesta;
    this.nomeErrorPaginaCorrente = nomeErrorPaginaCorrente;
    this.totaleRighe = numRecord;
    this.totalePagine = this.totaleRighe / numeroRighePerPagina;
    this.numPaginePerGruppo = new Long(SolmrConstants.NUM_PAGINE_PER_GRUPPO)
        .intValue();
    if (totaleRighe % numeroRighePerPagina > 0)
    {
      ++totalePagine;
    }
    if (paginaCorrenteRichiesta != null)
    {
      try
      {
        paginaCorrente = new Long(this.paginaCorrenteRichiesta).intValue();
        if (paginaCorrente < 1 || paginaCorrente > totalePagine)
        {
          errors = new ValidationErrors();
          errors.add(nomeErrorPaginaCorrente, new ValidationError(
              "Numero non valido"));
        }
      }
      catch (Throwable e)
      {
        errors = new ValidationErrors();
        errors.add(nomeErrorPaginaCorrente, new ValidationError(
            "Il numero della pagina deve essere compreso tra 1 e "
                + totalePagine));
      }
    }
    else
    {
      paginaCorrente = 1;
    }
    this.ultimaPaginaValida = safePage(ultimaPaginaValida);
    paginaCorrente = getPaginaCorrenteEffettiva();
    calcolaPrimoUltimo();
    setPagesForFastChoice();
  }

  /**
   * Restituisce l'elenco degli id i cui dati devono essere visualizzati nella
   * pagina corrente
   * 
   * @param idsForTooltips
   *          se true aggiunge all'elenco degli id, anche il primo id di ogni
   *          pagina che fa parte di un gruppo (le scelte rapide in alto a
   *          destra) per permettere la creazione dei tooltip con la loro
   *          descrizione
   * @return
   */
  public long[] getIdForCurrentPage(boolean idsForTooltips)
  {
    int numElements = ultimoElementoPaginaCorrente
        - primoElementoPaginaCorrente;
    int range = numElements;
    if (range <= 0)
    {
      return null;
    }
    long idRange[] = new long[range];
    System.arraycopy(ids, primoElementoPaginaCorrente, idRange, 0, numElements);
    if (idsForTooltips)
    {
      idRange = AnagUtils.merge(idRange, getPageIdsForFastChoice());
    }
    return idRange;
  }

  private long[] getPageIdsForFastChoice()
  {
    int len = gruppi.length;
    long pageIds[] = new long[len];
    for (int i = 0; i < len; ++i)
    {
      pageIds[i] = gruppi[i].getValue();
    }
    return pageIds;
  }

  public int safePage(int page)
  {
    if (page > totalePagine)
    {
      return totalePagine;
    }
    if (page <= 0)
    {
      return 1;
    }
    return page;
  }

  /**
   * Restuisce la pagina corrente effettiva, ossia quella passata come
   * paginaCorrenteRichiesta se è valida, altrimenti ultimaPaginaValida
   * normalizzate sul totalePagine
   * 
   * @return
   */
  private int getPaginaCorrenteEffettiva()
  {
    int paginaEffettiva = ultimaPaginaValida;
    if (errors == null)
    {
      return paginaCorrente;
    }
    if (paginaEffettiva > totalePagine)
    {
      paginaEffettiva = totalePagine;
    }
    return paginaEffettiva;
  }

  /**
   * Calcola il primo e l'ultimo elemento della pagina corrente
   */
  private void calcolaPrimoUltimo()
  {
    primoElementoPaginaCorrente = (paginaCorrente - 1) * numeroRighePerPagina;
    if (primoElementoPaginaCorrente > totaleRighe)
    {
      primoElementoPaginaCorrente = totaleRighe;
      ultimoElementoPaginaCorrente = totaleRighe;
      return;
    }
    ultimoElementoPaginaCorrente = primoElementoPaginaCorrente
        + numeroRighePerPagina;
    if (ultimoElementoPaginaCorrente > totaleRighe)
    {
      ultimoElementoPaginaCorrente = totaleRighe;
    }
  }

  public long[] getIds()
  {
    return ids;
  }

  public int getNumeroRighePerPagina()
  {
    return numeroRighePerPagina;
  }

  public int getUltimaPaginaValida()
  {
    return ultimaPaginaValida;
  }

  public String getPaginaCorrenteRichiesta()
  {
    return paginaCorrenteRichiesta;
  }

  public String getNomeErrorPaginaCorrente()
  {
    return nomeErrorPaginaCorrente;
  }

  public int getTotaleRighe()
  {
    return totaleRighe;
  }

  public int getTotalePagine()
  {
    return totalePagine;
  }

  public int getPaginaCorrente()
  {
    return paginaCorrente;
  }

  public ValidationErrors getErrors()
  {
    return errors;
  }

  public int getPrimoElementoPaginaCorrente()
  {
    return primoElementoPaginaCorrente;
  }

  public int getUltimoElementoPaginaCorrente()
  {
    return ultimoElementoPaginaCorrente;
  }

  public void setPagesForFastChoice()
  {
    int numGruppiSX = new Long(SolmrConstants.NUM_GRUPPI_SX).intValue();
    int numGruppiDX = new Long(SolmrConstants.NUM_GRUPPI_DX).intValue();
    Vector<PaginazioneGruppo> vNumbers = new Vector<PaginazioneGruppo>();
    int gruppoPrecedente = safePage((int) (paginaCorrente / numPaginePerGruppo)
        * numPaginePerGruppo);
    int gruppoSuccessivo = (int) ((paginaCorrente / numPaginePerGruppo) + 1)
        * numPaginePerGruppo;
    int gruppoCorrente = gruppoPrecedente;
    if (gruppoCorrente >= numPaginePerGruppo)
    {
      gruppoCorrente = safePage(gruppoCorrente - numPaginePerGruppo);
      for (int i = 0; i < numGruppiSX
          && (gruppoCorrente > 1 || gruppoCorrente == 1 && paginaCorrente != 1); ++i)
      {
        vNumbers.add(0, new PaginazioneGruppo(gruppoCorrente,ids==null?0:
            ids[(gruppoCorrente - 1) * numeroRighePerPagina], true, true));
        if (gruppoCorrente == 1)
        {
          break;
        }
        gruppoCorrente = safePage(gruppoCorrente - numPaginePerGruppo);
      }
    }
    gruppoCorrente = gruppoPrecedente;
    int len = gruppoSuccessivo - gruppoPrecedente;
    if (gruppoSuccessivo == totalePagine
        && gruppoSuccessivo % numPaginePerGruppo != 0)
    {
      ++len;
    }
    for (int i = 0; i < len && gruppoCorrente <= totalePagine; ++i, ++gruppoCorrente)
    {
      if (gruppoCorrente != paginaCorrente)
      {
        vNumbers.add(new PaginazioneGruppo(gruppoCorrente, ids==null?0:ids[(gruppoCorrente - 1) * numeroRighePerPagina], gruppoCorrente < paginaCorrente, false));
      }
    }

    if (gruppoSuccessivo != paginaCorrente)
    {
      gruppoCorrente = gruppoSuccessivo;
      for (int i = 0; i < numGruppiDX && gruppoCorrente <= totalePagine; ++i)
      {
        vNumbers.add(new PaginazioneGruppo(gruppoCorrente, ids==null?0:ids[(gruppoCorrente - 1) * numeroRighePerPagina], false, true));
        // gruppoCorrente = safePage(gruppoCorrente + numPaginePerGruppo);
        gruppoCorrente += numPaginePerGruppo;
      }
    }
    int size = vNumbers.size();
    gruppi = new PaginazioneGruppo[size];
    for (int i = 0; i < size; ++i)
    {
      gruppi[i] = ((PaginazioneGruppo) vNumbers.get(i));
    }
  }

  public void writeGruppiAndPrecedenteSuccessivo(Htmpl htmpl)
  {
    // Nomi dei blocchi per i gruppi
    String blkLeft = AnagUtils.concatBlk(blkNomeElenco, blkGruppoPrecedente);
    String blkRight = AnagUtils.concatBlk(blkNomeElenco, blkGruppoSuccessivo);
    String blkMiddle = AnagUtils.concatBlk(blkNomeElenco, blkGruppoCorrente);
    int len = 0;
    if (gruppi != null)
    {
      len = gruppi.length;
    }
    // Gruppi Dx e Sx e pagine del gruppo corrente
    for (int i = 0; i < len; ++i)
    {
      PaginazioneGruppo pg = gruppi[i];
      String blk = null;
      if (pg.isSinistra())
      {
        blk = blkLeft;
      }
      else
      {
        blk = blkRight;
      }
      htmpl.newBlock(blk);
      String idx = String.valueOf(pg.getIdx());
      htmpl.set(blk + ".value", idx);
      htmpl.set(blk + ".text", idx);
      if (pg.isGruppo())
      {
        htmpl.set(blk + ".class", "gruppo");
      }
      String tooltip = getTooltip(righe, i);
      if (tooltip != null)
      {
        htmpl.set(blk + ".tooltip", tooltip);
      }
    }
    // Pagina corrente
    htmpl.newBlock(blkMiddle);
    htmpl.set(blkMiddle + ".paginaCorrente", String.valueOf(paginaCorrente));

    // Pagina Successiva
    int paginaSuccessiva = safePage(paginaCorrente + 1);
    if (paginaCorrente != paginaSuccessiva)
    {
      String blkNext = AnagUtils.concatBlk(blkNomeElenco, blkPaginaSuccessiva);
      htmpl.newBlock(blkNext);
      htmpl.set(blkNext + ".pagina", String.valueOf(paginaSuccessiva));
    }

    // Pagina Precedente
    int paginaPrecedente = safePage(paginaCorrente - 1);
    if (paginaCorrente != paginaPrecedente)
    {
      String blkPrev = AnagUtils.concatBlk(blkNomeElenco, blkPaginaPrecedente);
      htmpl.newBlock(blkPrev);
      htmpl.set(blkPrev + ".pagina", String.valueOf(paginaPrecedente));
    }
  }
  
  public void writeGruppiAndPrecedenteSuccessivoFolder(Htmpl htmpl, String idFolder)
  {
    // Nomi dei blocchi per i gruppi
    String blkLeft = AnagUtils.concatBlk(blkNomeElenco, blkGruppoPrecedente);
    String blkRight = AnagUtils.concatBlk(blkNomeElenco, blkGruppoSuccessivo);
    String blkMiddle = AnagUtils.concatBlk(blkNomeElenco, blkGruppoCorrente);
    int len = 0;
    if (gruppi != null)
    {
      len = gruppi.length;
    }
    // Gruppi Dx e Sx e pagine del gruppo corrente
    for (int i = 0; i < len; ++i)
    {
      PaginazioneGruppo pg = gruppi[i];
      String blk = null;
      if (pg.isSinistra())
      {
        blk = blkLeft;
      }
      else
      {
        blk = blkRight;
      }
      htmpl.newBlock(blk);
      String idx = String.valueOf(pg.getIdx());
      htmpl.set(blk + ".idFolder", idFolder);
      htmpl.set(blk + ".value", idx);
      htmpl.set(blk + ".text", idx);
      if (pg.isGruppo())
      {
        htmpl.set(blk + ".class", "gruppo");
      }
      String tooltip = getTooltip(righe, i);
      if (tooltip != null)
      {
        htmpl.set(blk + ".tooltip", tooltip);
      }
    }
    // Pagina corrente
    htmpl.newBlock(blkMiddle);
    htmpl.set(blkMiddle + ".paginaCorrente", String.valueOf(paginaCorrente));

    // Pagina Successiva
    int paginaSuccessiva = safePage(paginaCorrente + 1);
    if (paginaCorrente != paginaSuccessiva)
    {
      String blkNext = AnagUtils.concatBlk(blkNomeElenco, blkPaginaSuccessiva);
      htmpl.newBlock(blkNext);
      htmpl.set(blkNext + ".pagina", String.valueOf(paginaSuccessiva));
      htmpl.set(blkNext + ".idFolder", idFolder);
    }

    // Pagina Precedente
    int paginaPrecedente = safePage(paginaCorrente - 1);
    if (paginaCorrente != paginaPrecedente)
    {
      String blkPrev = AnagUtils.concatBlk(blkNomeElenco, blkPaginaPrecedente);
      htmpl.newBlock(blkPrev);
      htmpl.set(blkPrev + ".pagina", String.valueOf(paginaPrecedente));
      htmpl.set(blkPrev + ".idFolder", idFolder);
    }
  }

  private String getTooltip(Object[] righe, int idx)
  {
    idx += ultimoElementoPaginaCorrente - primoElementoPaginaCorrente;
    try
    {
      return righe[idx].toString(); // L'oggetto deve restituire nel toString il
      // valore del tooltip
    }
    catch (Exception e)
    {
      return null;
    }
  }
  
  public void paginazione(Htmpl htmpl)
  {
    int numElementiInPagina = ultimoElementoPaginaCorrente
        - primoElementoPaginaCorrente;
    writeGruppiAndPrecedenteSuccessivo(htmpl);
    String blkRiga = AnagUtils.concatBlk(blkNomeElenco, blkNomeRiga);
    for (int i = 0; i < numElementiInPagina; ++i)
    {
      htmpl.newBlock(blkRiga);
      righe[i].scriviRiga(htmpl, blkRiga);
    }
    htmpl.set(AnagUtils.concatBlk(blkNomeElenco, "totaleRighe"), String
        .valueOf(totaleRighe));
    htmpl.set(AnagUtils.concatBlk(blkNomeElenco, "totalePagine"), String
        .valueOf(totalePagine));
  }

  public void paginazione(Htmpl htmpl, String id[])
  {
    int numElementiInPagina = ultimoElementoPaginaCorrente
        - primoElementoPaginaCorrente;
    writeGruppiAndPrecedenteSuccessivo(htmpl);
    String blkRiga = AnagUtils.concatBlk(blkNomeElenco, blkNomeRiga);
    for (int i = 0; i < numElementiInPagina; ++i)
    {
      htmpl.newBlock(blkRiga);
      righe[i].scriviRiga(htmpl, blkRiga, id);
    }
    htmpl.set(AnagUtils.concatBlk(blkNomeElenco, "totaleRighe"), String
        .valueOf(totaleRighe));
    htmpl.set(AnagUtils.concatBlk(blkNomeElenco, "totalePagine"), String
        .valueOf(totalePagine));
  }
  
  
  public void paginazioneFolder(Htmpl htmpl, String idFolder)
  {
    int numElementiInPagina = ultimoElementoPaginaCorrente
        - primoElementoPaginaCorrente;
    writeGruppiAndPrecedenteSuccessivoFolder(htmpl, idFolder);
    String blkRiga = AnagUtils.concatBlk(blkNomeElenco, blkNomeRiga);
    for (int i = 0; i < numElementiInPagina; ++i)
    {
      htmpl.newBlock(blkRiga);
      righe[i].scriviRiga(htmpl, blkRiga);
    }
    htmpl.set(AnagUtils.concatBlk(blkNomeElenco, "totaleRighe"), String
        .valueOf(totaleRighe));
    htmpl.set(AnagUtils.concatBlk(blkNomeElenco, "totalePagine"), String
        .valueOf(totalePagine));
  }

  public String getBlkNomeElenco()
  {
    return blkNomeElenco;
  }

  public void setBlkNomeElenco(String blkNomeElenco)
  {
    this.blkNomeElenco = blkNomeElenco;
  }

  public String getBlkNomeRiga()
  {
    return blkNomeRiga;
  }

  public void setBlkNomeRiga(String blkNomeRiga)
  {
    this.blkNomeRiga = blkNomeRiga;
  }

  public String getBlkGruppoPrecedente()
  {
    return blkGruppoPrecedente;
  }

  public void setBlkGruppoPrecedente(String blkGruppoPrecedente)
  {
    this.blkGruppoPrecedente = blkGruppoPrecedente;
  }

  public String getBlkGruppoSuccessivo()
  {
    return blkGruppoSuccessivo;
  }

  public void setBlkGruppoSuccessivo(String blkGruppoSuccessivo)
  {
    this.blkGruppoSuccessivo = blkGruppoSuccessivo;
  }

  public String getBlkGruppoCorrente()
  {
    return blkGruppoCorrente;
  }

  public void setBlkGruppoCorrente(String blkGruppoCorrente)
  {
    this.blkGruppoCorrente = blkGruppoCorrente;
  }

  public int getNumPaginePerGruppo()
  {
    return numPaginePerGruppo;
  }

  public void setNumPaginePerGruppo(int numPaginePerGruppo)
  {
    this.numPaginePerGruppo = numPaginePerGruppo;
  }
}

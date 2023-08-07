package it.csi.smranag.smrgaa.util;

import it.csi.smranag.smrgaa.dto.RigaPraticaParticellaVO;
import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO;

public class RigaPraticheParticellaComparator extends BaseComparator
{
  public static final int COMPARE_DEFAULT = 0;
  public static final int COMPARE_CUAA    = 1;
  public static final int COMPARE_ANNO    = 2;
  private int             comparationType = COMPARE_DEFAULT;
  private boolean         isReverse       = false;

  /**
   * 
   * @param comparationType
   *          tipo di comparazione: base, per cuaa o anno. Vedi costanti
   *          COMPARE_XXXX
   * @param isReverse
   *          indica di invertire l'ordine di comparazione, ossia 1 ==> -1, -1
   *          ==> 1 e ovviamente 0 ==> 0.
   */
  public RigaPraticheParticellaComparator(int comparationType, boolean isReverse)
  {
    this.comparationType = comparationType;
    this.isReverse = isReverse;
  }

  public int compareCuaa(Object o1, Object o2)
  {
    RigaPraticaParticellaVO r1 = (RigaPraticaParticellaVO) o1;
    RigaPraticaParticellaVO r2 = (RigaPraticaParticellaVO) o2;
    int result=compareComparableObjects(r1.getCuaa(), r2.getCuaa(), true, true);
    if (result==0)
    {
      result=-1 * compareOnlyAnno(o1, o2); // Di default anno è desc non asc ==> moltiplico * -1      
    }
    return result;
  }

  public int compareOnlyCuaa(Object o1, Object o2)
  {
    RigaPraticaParticellaVO r1 = (RigaPraticaParticellaVO) o1;
    RigaPraticaParticellaVO r2 = (RigaPraticaParticellaVO) o2;
    int result=compareComparableObjects(r1.getCuaa(), r2.getCuaa(), true, true);
    return result;
  }
  
  public int compareOnlyAnno(Object o1, Object o2)
  {
    RigaPraticaParticellaVO r1 = (RigaPraticaParticellaVO) o1;
    RigaPraticaParticellaVO r2 = (RigaPraticaParticellaVO) o2;
    long a1 = r1.getPraticaProcedimentoVO().getAnnoCampagna();
    long a2 = r2.getPraticaProcedimentoVO().getAnnoCampagna();
    if ( a1==a2)
    {
      return 0;
    }
    return a1>a2?-1:1;
  }
  
  public int compareAnno(Object o1, Object o2)
  {
    RigaPraticaParticellaVO r1 = (RigaPraticaParticellaVO) o1;
    RigaPraticaParticellaVO r2 = (RigaPraticaParticellaVO) o2;
    long a1 = r1.getPraticaProcedimentoVO().getAnnoCampagna();
    long a2 = r2.getPraticaProcedimentoVO().getAnnoCampagna();
    if (a1 == a2)
    {
      return compareOnlyCuaa(o1, o2);      
    }
    if (a1 > a2)
    {
      return -1;
    }
    else
    {
      return 1;
    }
  }

  public int compare(Object o1, Object o2)
  {
    int returnType = 0;
    switch (comparationType)
    {
      case COMPARE_CUAA:
        returnType = compareCuaa(o1, o2);
      case COMPARE_ANNO:
        returnType = compareAnno(o1, o2);
      default:
        returnType = compareBase(o1, o2);
    }
    return isReverse ? returnType * -1 : returnType;
  }

  public int compareBase(Object o1, Object o2)
  {
    if (o1 == o2)
    {
      return 0; // Null o stesso oggetto
    }
    if (o1 == null)
    {
      return -1;
    }
    if (o2 == null)
    {
      return 1;
    }
    RigaPraticaParticellaVO r1 = (RigaPraticaParticellaVO) o1;
    RigaPraticaParticellaVO r2 = (RigaPraticaParticellaVO) o2;
    // Ogni RigaPraticaParticellaVO ha un proprio PraticaProcedimentoVO!=null
    PraticaProcedimentoVO p1 = r1.getPraticaProcedimentoVO();
    PraticaProcedimentoVO p2 = r2.getPraticaProcedimentoVO();

    // ANNO CAMPAGNA DESC
    int a1 = p1.getAnnoCampagna();
    int a2 = p2.getAnnoCampagna();
    if (a1 < a2)
    {
      return 1;
    }
    if (a1 > a2)
    {
      return -1;
    }
    // a1==a2 ==> continuo con i cuaa
    // CUAA ASC
    String cuaa1 = r1.getCuaa();
    String cuaa2 = r2.getCuaa();
    int comparation = compareComparableObjects(cuaa1, cuaa2, true, true);
    if (comparation != 0)
    {
      return comparation;
    }
    // cuaa1==cuaa2 ==> continuo con gli idProcedimento
    int idProcedimento1 = p1.getIdProcedimento();
    int idProcedimento2 = p2.getIdProcedimento();
    // idProcedimento ASC
    if (idProcedimento1 < idProcedimento2)
    {
      return -1;
    }
    if (idProcedimento1 > idProcedimento2)
    {
      return 1;
    }
    return 0;
  }
}

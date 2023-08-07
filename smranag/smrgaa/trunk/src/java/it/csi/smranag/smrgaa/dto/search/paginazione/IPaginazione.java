package it.csi.smranag.smrgaa.dto.search.paginazione;

import java.io.Serializable;

import it.csi.jsf.htmpl.Htmpl;

public interface IPaginazione  extends Serializable
{
  public void scriviRiga(Htmpl htmpl, String blk);
  public void scriviRiga(Htmpl htmpl, String blk, String id[]);
}

/************************************************************\
  Questa funzione restituisce il valore 
  di un radiobutton selezionato da un gruppo.
  
  Parametro di input
    radio = è il radiobutton da leggere
    
  Valori di output
    null, se il radiobutton non esiste)
    stringa vuota, se nessun radiobutton risulta selezionato
    altrimenti restituisce il "value" del radiobutton
\************************************************************/
function getRadioValue(radio)
{
  var valore=null;
  if (typeof radio != "undefined")
  {
    valore = "";
    if (typeof radio.length=="undefined")
      valore = radio.value;
    else
    {
      for (var i=0; i < radio.length; i++)
        if (radio[i].checked )
        {
          valore = radio[i].value;
          break;
        }
    }
  }
  return valore;
}

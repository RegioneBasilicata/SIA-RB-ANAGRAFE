//Verifica in checkbox se almeno uno è selezionato
function isOneChecked(obj)
{
  var risp=false;
  var lung=obj.length;
  if(lung>1)
  {
    for(i=0;i<lung;i++)
    {
      if(obj[i].checked==true)
      {
        risp=true;
        break;
      }
    }//end-for
  }//end-if
  else
  {
    risp=obj.checked;
  }
  return risp;
}

//Verifica se è una cifra numerica
function isDigit(p)
{
  if (p=="0" || p=="1" || p=="2" || p=="3" || p=="4"
      || p=="5" || p=="6" || p=="7" || p=="8" || p=="9")
    return true;
  else
    return false;
}


//Verifica se è una cifra numerica o virgola
function isDigitDouble(p)
{
  if (isDigit(p) || p=='.')
    return true;
  else
    return false;
}


//Verifica se un numero intero
function isInteger(p)
{
  var appo=new String(p);
  var risp=true;
  for(i=0;i<appo.length;i++)
  {
    if(!isDigit(appo.charAt(i)))
    {
      risp=false;
      break;
    }//if
  }//for
  if(p=="")
  {
    risp=false;
  }
  return risp;
}


//Verifica se un numero con la virgola
function isNumber(p)
{
  var appo=new String(p);
  var risp=true;
  for(i=0;i<appo.length;i++)
  {
    if(!isDigitDouble(appo.charAt(i)))
    {
      risp=false;
      break;
    }//if
  }//for
  if(p=="")
  {
    risp=false;
  }
  return risp;
}


//Elimina gli spazi in una stringa
function jTrim(p)
{
  var appo=new String(p);
  var appo2='';
  for(i=0;i<appo.length;i++)
  {
    if(appo.charAt[i]!=' ')
    {
      appo2=appo2+appo.charAt[i];
    }//if
  }//for
  return appo2;
}


//Verifica se la stringa e una data del tipo GG/MM/AAAA
function isDate(p)
{
  var appo =new String(p);
  var risp=false;
  if(appo.length==10)
  {
    //alert('Check lunghezza OK');
    var gg=appo.substring(0,2);
    var mm=appo.substring(3,5);
    var aaaa=appo.substring(6,10);
    //alert('gg : '+gg);
    //alert('mm : '+mm);
    //alert('aaaa : '+aaaa);
    if(isInteger(gg) && isInteger(mm) && isInteger(aaaa))
    {
      var numGG = new Number(gg);
      var numMM = new Number(mm);
      var numAAAA = new Number(aaaa);
      if( (numGG>0 && numGG<32) && (numMM>0 && numMM<13) && (numAAAA>1900) )
      {
        risp=true;
      }
    }
  }
  return risp;
}


//Funzione che mi permette di controllare se un campo è numerico
function IsNumeric(sText) {
  if(!sText)
    return false;
  var ValidChars = "0123456789.,";
  var IsNumber=true;
  var Char;
  for(i = 0; i < sText.length && IsNumber == true; i++) { 
      Char = sText.charAt(i); 
      if(ValidChars.indexOf(Char) == -1) {
        IsNumber = false;
      }
    }
  return IsNumber;
}

function arrotonda(numero, numeroDecimali)
{ 
  var modificatore = '1'; 
  for(var i=0;i<numeroDecimali;i++)
    modificatore += "0"; 
  modificatore = parseInt(modificatore,10);
  
  return Math.round(numero*(modificatore))/(modificatore);   
}

function aggiungiZeri(totale)
{
  if(totale.lastIndexOf(',') != -1 )
  {
    var str = totale.split(',');
    var numZeri = 4 - str[1].length;
    for(a=0;a<numZeri;a++)
    {
      totale = totale+'0';
    } 
  }
  else
  {
    totale = totale+',0000';
  }  
  
  return totale;  
}





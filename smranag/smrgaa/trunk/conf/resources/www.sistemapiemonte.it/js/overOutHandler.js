/*
JavaScript per la gestione di overOutHandler solo su Internet Explorer.
A causa di un bug grafico su Opera è stata gestita la funzione in modo che non venga processata da Opera e Browser Gecko.
*/

 function overOutHandler(obj, styleClassName)
 {
   if (navigator.appName=='Microsoft Internet Explorer')
    { obj.className=styleClassName; }
 }
 
function messageLoop()
{
  __container=document.getElementById("id_div_system_message");
  __scrollText=document.getElementById("id_inner_div_system_message");
  if(__container!=null)
  {
	var w=__container.clientWidth;
	var left=parseInt(__scrollText.style.left);
	left=left-2;
	if (left<-__scrollText.clientWidth)
	{
	  left=w-5;
	}

	__scrollText.style.left=left+"px";
	setTimeout(messageLoop,35);
  }
}

function systemMessagesOld(__messageText, numMessage)
{
  var __datiUtente=document.getElementById("datiUtente");
  var __corpo=document.getElementById('datiUtente');
	
  if (__datiUtente)
  {
	if(numMessage!='')
	{
	  __imgContainer = document.createElement("a");
	  __imgContainer.href="messaggi_utente.shtml";
	  __imgContainer.alt=numMessage;
	  __imgContainer.title=numMessage;

	  __img=document.createElement("img");
	  __img.src="../css/im/msg.png";
	  __img.style.cssFloat="right";
      __img.style.paddingRight='20px';
	  __img.style.height="16px";
	  __img.style.width ="16px";
	  __img.alt=numMessage;
			
	  __imgContainer.appendChild(__img);
	  __corpo.appendChild(__imgContainer);				
	}			
	if(__messageText || numMessage)
	{
	  var __container=document.createElement("div");
	  __container.id="id_div_system_message";
	  __container.style.overflow="hidden";
	  __container.style.width="500px";
	  __container.style.cssFloat="right";
	  __container.style.backgroundColor ='#00000000';
	  __container.style.position='relative';
	  __container.style.top='0px';
			
	  __scrollBox=document.createElement("div");
	  __scrollBox.style.backgroundColor ='#00000000';
	  __scrollBox.id="id_div_container_system_message";
	  __scrollBox.style.width="10000px";
	  var __scrollText=document.createElement("div");
	  __scrollText.style.backgroundColor ='#00000000';
	  __scrollText.style.cssFloat="left";
	  __scrollText.id="id_inner_div_system_message";
	  __scrollText.style.position="relative";
	  __scrollText.style.left="20px";
	  __scrollText.innerHTML=__messageText;
	  __scrollText.style.color='#900';
	  __scrollBox.appendChild(__scrollText);
	  __container.appendChild(__scrollBox);

	  __corpo.appendChild(__container);
		
	}

	setTimeout(messageLoop,100);
  }
}

function systemMessages(__messageText, numMessage)
{
  var divPrecedente=document.getElementById('Utente');
  if (divPrecedente)
  {
	if(__messageText || numMessage )
	{
	  var __container=document.createElement("div");
	  var marquee=document.createElement("marquee");
	  marquee.id="marquee_messaggi_testata";
	  marquee.style.color='#900';
	  marquee.style.width='95%';
	  marquee.style.fontWeight="bold";
	  marquee.scrollAmount='3';
      marquee.scrollDelay='1';
	  marquee.innerHTML=__messageText;
      marquee.onmouseover=ferma;
	  marquee.onmouseout=avvia;				

	  __container.appendChild(marquee);	
	  insertAfter(divPrecedente, __container);			
		
	  if(numMessage!='')
	  {
		__imgContainer = document.createElement("a");
		__imgContainer.href="messaggi_utente.htm";
		__imgContainer.alt=numMessage;
		__imgContainer.title=numMessage;
		__imgContainer.style.cssFloat="right";

		__img=document.createElement("img");
		__img.src="../css/im/msg.png";
		__img.style.height="16px";
		__img.style.width ="16px";
		__img.alt=numMessage;
				
		__imgContainer.appendChild(__img);	
		__imgContainer.style.position="relative";
		insertAfter(divPrecedente, __imgContainer);
	  }	
	}
				
  }	
}

function systemMessages2(__messageText)
{	
  var __datiUtente=document.getElementById("datiUtente");
  if (__datiUtente && __messageText)
  {
	var __container=document.createElement("div");
	__container.id="id_div_system_message";
	__container.style.overflow="hidden";

	__container.style.backgroundColor ='#00000000';
	__container.style.height="18px";
		
	__scrollBox=document.createElement("div");
	__scrollBox.style.backgroundColor ='#00000000';
	__scrollBox.id="id_div_container_system_message";
	__scrollBox.style.width="10000px";
	__scrollBox.style.fontWeight="bold";
	var __scrollText=document.createElement("div");
	__scrollText.style.backgroundColor ='#00000000';
	__scrollText.style.cssFloat="left";
	__scrollText.id="id_inner_div_system_message";
	__scrollText.style.position="relative";
	__scrollText.style.left="20px";
	__scrollText.innerHTML=__messageText;
	__scrollText.style.color='#900';
	__scrollBox.appendChild(__scrollText);
	__container.appendChild(__scrollBox);

	__img=document.createElement("img");
	__img.src="../css/im/email.png";
	__img.style.cssFloat="right";
	__img.style.paddingRight='20px';
	__img.style.height="40px";
	var __corpo=document.getElementById('corpo');
	var __utente=document.getElementById('Utente');
	__corpo.insertBefore(__container, __utente);
	__corpo.insertBefore(__img, __container);
	__span=document.createElement("div");
	__span.style.clear='left';
	__br = document.createElement("br");
	__br.style.clear='left';
	__br.style.fontSize='1px';
	__corpo.insertBefore(__span, __utente);
	__corpo.insertBefore(__br, __utente);
	setTimeout(messageLoop,100);
  }
}

// This function inserts newNode after referenceNode
function insertAfter( referenceNode, newNode )
{
  referenceNode.parentNode.insertBefore( newNode, referenceNode.nextSibling );
}
function ferma()
{
  if(document.getElementById("marquee_messaggi_testata")!=null)
  {
	document.getElementById("marquee_messaggi_testata").stop();
  }
}
function avvia()
{
  if(document.getElementById("marquee_messaggi_testata")!=null)
  {
	document.getElementById("marquee_messaggi_testata").start();
  }
}	

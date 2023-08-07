function popCentro(page,w,h,target) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars=yes,resizable=yes';
  win = window.open(page, target, winprops);
  if (parseInt(navigator.appVersion) >= 4) {
    win.window.focus();
  }
  return;
}

function stampaMod(form) {
  var d = new Date();
  var unique = (d.getUTCHours()*60+d.getUTCMinutes())*60+d.getUTCSeconds();
  var target = "stampa"+unique;
  popCentro("",645,500,target);
  form.target = target;
  form.submit();
  return;
}

var checkPass = function() {
  if (/^.{1,39}$/.test(document.getElementById('password').value) &&
	(document.getElementById('password').value ==
    document.getElementById('confirm').value)) {
    document.getElementById('password').style.borderColor = '#ccc';
    document.getElementById('password').style.color = 'black';
    document.getElementById('confirm').style.borderColor = '#ccc';
    document.getElementById('confirm').style.color = 'black';
    document.getElementById('passbtn').disabled = false;
  } else {
    document.getElementById('password').style.borderColor = 'red';
    document.getElementById('password').style.color = 'red';
    document.getElementById('confirm').style.borderColor = 'red';
    document.getElementById('confirm').style.color = 'red';
    document.getElementById('passbtn').disabled = true;
  }
}

var checkData = function() {
  if (/^\d{12}$/.test(document.getElementById('phone').value)) {
    document.getElementById('phone').style.borderColor = '#ccc';
    document.getElementById('phone').style.color = 'black';
  } else {
    document.getElementById('phone').style.borderColor = 'red';
    document.getElementById('phone').style.color = 'red';
  }
  if (/^[\wА-яІіЇїЄє'Ґґ -]+$/.test(document.getElementById('firstname').value)) {
    document.getElementById('firstname').style.borderColor = '#ccc';
    document.getElementById('firstname').style.color = 'black';
  } else {
    document.getElementById('firstname').style.borderColor = 'red';
    document.getElementById('firstname').style.color = 'red';
  }
  if (/^[\wА-яІіЇїЄє'Ґґ-]+$/.test(document.getElementById('lastname').value)) {
    document.getElementById('lastname').style.borderColor = '#ccc';
    document.getElementById('lastname').style.color = 'black';
  } else {
    document.getElementById('lastname').style.borderColor = 'red';
    document.getElementById('lastname').style.color = 'red';
  }
  if (/^[\w-]+@[\w-]+\.[A-Za-z]+$/.test(document.getElementById('email').value)) {
    document.getElementById('email').style.borderColor = '#ccc';
    document.getElementById('email').style.color = 'black';
  } else {
    document.getElementById('email').style.borderColor = 'red';
    document.getElementById('email').style.color = 'red';
  }
  if (/^\w+$/.test(document.getElementById('passport').value)) {
    document.getElementById('passport').style.borderColor = '#ccc';
    document.getElementById('passport').style.color = 'black';
  } else {
    document.getElementById('passport').style.borderColor = 'red';
    document.getElementById('passport').style.color = 'red';
  }
  if ((/^\d{12}$/.test(document.getElementById('phone').value))
  	&& (/^[\wА-яІіЇїЄє'Ґґ -]+$/.test(document.getElementById('firstname').value))
  	&& (/^[\wА-яІіЇїЄє'Ґґ-]+$/.test(document.getElementById('lastname').value))
  	&& (/^[\w-]+@[\w-]+\.[A-Za-z]+$/.test(document.getElementById('email').value))
  	&& (/^\w+$/.test(document.getElementById('passport').value))) {
    document.getElementById('databtn').disabled = false;
  } else {
    document.getElementById('databtn').disabled = true;
 }   
}

var checkRegData = function() {
  if (/^.{1,39}$/.test(document.getElementById('password').value) &&
	(document.getElementById('password').value ==
    document.getElementById('confirm').value)) {
    document.getElementById('password').style.borderColor = '#ccc';
    document.getElementById('password').style.color = 'black';
    document.getElementById('confirm').style.borderColor = '#ccc';
    document.getElementById('confirm').style.color = 'black';
  } else {
    document.getElementById('password').style.borderColor = 'red';
    document.getElementById('password').style.color = 'red';
    document.getElementById('confirm').style.borderColor = 'red';
    document.getElementById('confirm').style.color = 'red';
  }
  if (/^\d{12}$/.test(document.getElementById('phone').value)) {
    document.getElementById('phone').style.borderColor = '#ccc';
    document.getElementById('phone').style.color = 'black';
  } else {
    document.getElementById('phone').style.borderColor = 'red';
    document.getElementById('phone').style.color = 'red';
  }
  if (/^[\wА-яІіЇїЄє'Ґґ -]+$/.test(document.getElementById('firstname').value)) {
    document.getElementById('firstname').style.borderColor = '#ccc';
    document.getElementById('firstname').style.color = 'black';
  } else {
    document.getElementById('firstname').style.borderColor = 'red';
    document.getElementById('firstname').style.color = 'red';
  }
  if (/^[\wА-яІіЇїЄє'Ґґ-]+$/.test(document.getElementById('lastname').value)) {
    document.getElementById('lastname').style.borderColor = '#ccc';
    document.getElementById('lastname').style.color = 'black';
  } else {
    document.getElementById('lastname').style.borderColor = 'red';
    document.getElementById('lastname').style.color = 'red';
  }
  if (/^[\w-]+@[\w-]+\.[A-Za-z]+$/.test(document.getElementById('email').value)) {
    document.getElementById('email').style.borderColor = '#ccc';
    document.getElementById('email').style.color = 'black';
  } else {
    document.getElementById('email').style.borderColor = 'red';
    document.getElementById('email').style.color = 'red';
  }
  if (/^\w+$/.test(document.getElementById('passport').value)) {
    document.getElementById('passport').style.borderColor = '#ccc';
    document.getElementById('passport').style.color = 'black';
  } else {
    document.getElementById('passport').style.borderColor = 'red';
    document.getElementById('passport').style.color = 'red';
  }
  if ((/^\d{12}$/.test(document.getElementById('phone').value))
  	&& (/^[\wА-яІіЇїЄє'Ґґ -]+$/.test(document.getElementById('firstname').value))
  	&& (/^[\wА-яІіЇїЄє'Ґґ-]+$/.test(document.getElementById('lastname').value)) 
  	&& (/^.+$/.test(document.getElementById('password').value))
  	&& (/^[\w-]+@[\w-]+\.[A-Za-z]+$/.test(document.getElementById('email').value))
  	&& (/^\w+$/.test(document.getElementById('passport').value)) 
  	&& (document.getElementById('password').value ==
        document.getElementById('confirm').value)){
    document.getElementById('regbtn').disabled = false;
  } else {
    document.getElementById('regbtn').disabled = true;
 }   
}
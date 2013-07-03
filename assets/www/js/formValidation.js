function validaForm(codigo){
	erro = new String;		
	var check = false;
	if(codigo.length != 13) erro += "O Código dos Correios deve ter tamanho 13!!!";
	else{
		var tst = /\W/;//Find a non-word character
		var tst2 = /\d/;//Find a digit
		if (tst2.test(codigo.substring(0,1)) || tst.test(codigo.substring(2,10)) || tst2.test(codigo.substring(11,12)) ) erro += "O Código está inválido!!!";
	}				
					
	//caso tenha algum erro, retorna false e exibe error;
	if (erro.length > 0){
		navigator.notification.alert(erro);				
		return false;
	}
	
	return true;
}
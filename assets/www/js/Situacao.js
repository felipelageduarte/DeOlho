// Define a class like this
function Situacao(codigo, data, local, situacao){
	this.codigo = codigo;
	this.data = data;
	this.local = local;
	this.situacao = situacao;
}

Situacao.prototype.sqlInsertSituacao = function(){
	tx.executeSql('CREATE TABLE SITUACAO(CODIGO VARCHAR(13) PRIMARY KEY NOT NULL, DATA DATETIME PRIMARY KEY NOT NULL, LOCAL TEXT NOT NULL, SITUACAO TEXT NOT NULL)');
	tx.executeSql('INSERT INTO SITUACAO (CODIGO, DATA, LOCAL, SITUACAO) VALUES ("' + this.codigo + '", "' + this.data + '", "' + this.local + '", "' + this.situacao + '")');
}

Situacao.prototype.InsertError = function(tx, err){
	alert("Error processing SQL: "+err);
}

Situacao.prototype.InsertSuccess = function(){
	alert("success!");
}

Situacao.prototype.insert = function(){
    db.transaction(this.sqlInsertSituacao(), this.InsertError, this.InsertSuccess);
}
/*
// Instantiate new objects with 'new'
var person = new Person("Bob", "M");

// Invoke methods like this
person.speak(); // alerts "Howdy, my name is Bob"
*/
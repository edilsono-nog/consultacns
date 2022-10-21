const divMessage = document.querySelector(".alert");
var radios = document.getElementsByName("fav_language");

document.getElementById('cns').onclick = function () {
	$("#nomepesquisa").val('');
	limpa();
	$("#nomepesquisa").focus();
}

document.getElementById('cpf').onclick = function () {
	$("#nomepesquisa").val('');
	limpa();
	$("#nomepesquisa").focus();
}

document.getElementById('nome').onclick = function () {
	$("#nomepesquisa").val('');
	limpa();
	$("#nomepesquisa").focus();
}

document.getElementById("localiza").onclick = function() {
	for (var i = 0; i < radios.length; i++) {
	        if (radios[i].checked) {
	         var tipo = radios[i].value;
	         var filtro = $("#nomepesquisa").val();
	         loccns(filtro, tipo);
	        }
	    }
}

function loccns(filtros, tipos){
	
	$.ajax({
		method: "GET",
		url : "cnsconsulta/listacns",
		data : {
			filtro: filtros,
			tipo: tipos
		},
		success: function (response) {
			if(response.length > 1){
				modal.classList.toggle('hide')
				fade.classList.toggle('hide')
				$("#tableCns tr").remove();
				for (var i = 0; i < response.length; i++) {
					$('#tableCns > tbody').append(					
						'<tr>'+
						'<td>'+  response[i].cns + '</td>'+
						'<td id="td_nome">' + response[i].nome + 
			            '<p style="font-size: 11px; margin-top: 5px;" >' + response[i].mae+'</p></td>' +
						'<td><button class="btn" onclick=selectcns('+response[i].id+') title="Dados do Usuário"><i class="fa-regular fa-hand-point-left"></i></button> </td>' +
						'</tr>');
				}
			}else if(response.length === 1){
				//console.log(response[0].cns)
				document.querySelector('.cns').innerHTML = response[0].cns;
				document.querySelector('.nome').innerHTML = response[0].nome;
				document.querySelector('.cpf').innerHTML = response[0].cpf;
				document.querySelector('.mae').innerHTML = response[0].mae;
				document.querySelector('.pai').innerHTML = response[0].pai;
				document.querySelector('.end').innerHTML = response[0].endereco;
				document.querySelector('.comp').innerHTML = response[0].complemento;
				document.querySelector('.numero').innerHTML = response[0].numero;
				document.querySelector('.bairro').innerHTML = response[0].bairro;
				document.querySelector('.cidade').innerHTML = response[0].cidade;
				document.querySelector('.estado').innerHTML = response[0].estado;
				document.querySelector('.sexo').innerHTML = response[0].sexo;
				document.querySelector('.raca').innerHTML = response[0].raca;
				document.querySelector('.nasc').innerHTML = response[0].dtNasc;
			}else if(response.length === 0){
				const msg = "Não foi encontrado, tente novamente...";
				msgError(msg);
				setTimeout(() => {
					//sair();
				},3000)
			}
			
		},
		error : function(e) {
	          if (e.status == 400){
				const msg = "Não foi encontrado e/ou \n Falha ao Acessar o Servidor, \n tente novamente...";
				msgError(msg);
				setTimeout(() => {
					//sair();
				},3000)
			}	
	    }
	})
}

function selectcns(id){
	$.ajax({
		method: "GET",
		url : "cnsconsulta/selectcns",
		data : "cns="+id,
		success: function (response) {
			modal.classList.toggle('hide')
			fade.classList.toggle('hide')
			document.querySelector('.cns').innerHTML = response.cns;
			document.querySelector('.nome').innerHTML = response.nome;
			document.querySelector('.cpf').innerHTML = response.cpf;
			document.querySelector('.mae').innerHTML = response.mae;
			document.querySelector('.pai').innerHTML = response.pai;
			document.querySelector('.end').innerHTML = response.endereco;
			document.querySelector('.comp').innerHTML = response.complemento;
			document.querySelector('.numero').innerHTML = response.numero;
			document.querySelector('.bairro').innerHTML = response.bairro;
			document.querySelector('.cidade').innerHTML = response.cidade;
			document.querySelector('.estado').innerHTML = response.estado;
			document.querySelector('.sexo').innerHTML = response.sexo;
			document.querySelector('.raca').innerHTML = response.raca;
			document.querySelector('.nasc').innerHTML = response.dtNasc;
		},
	})
}

function limpa(){
	document.querySelector('.cns').innerHTML = '';
	document.querySelector('.nome').innerHTML = '';
	document.querySelector('.cpf').innerHTML = '';
	document.querySelector('.mae').innerHTML = '';
	document.querySelector('.pai').innerHTML = '';
	document.querySelector('.end').innerHTML = '';
	document.querySelector('.comp').innerHTML = '';
	document.querySelector('.numero').innerHTML = '';
	document.querySelector('.bairro').innerHTML = '';
	document.querySelector('.cidade').innerHTML = '';
	document.querySelector('.estado').innerHTML = '';
	document.querySelector('.sexo').innerHTML = '';
	document.querySelector('.raca').innerHTML = '';
	document.querySelector('.nasc').innerHTML = '';
}

function msgError(msg) {
    const message = document.createElement("div");
    message.classList.add("messageError");
    message.innerText = msg;
    divMessage.appendChild(message);

    setTimeout(() => {
        message.style.display = "none";
    }, 3000);
}

/*Modal Matricula*/
let modal = document.querySelector('#modal');
let fade = document.querySelector('#fade');

document.getElementById("close-modal").addEventListener("click", function(event){
	modal.classList.toggle('hide')
	fade.classList.toggle('hide')
	window.location.reload(true);
});

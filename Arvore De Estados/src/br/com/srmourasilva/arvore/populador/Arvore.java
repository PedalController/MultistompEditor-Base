package br.com.srmourasilva.arvore.populador;

import br.com.srmourasilva.arvore.dominio.Noh;

public class Arvore {

	private Noh raiz;

	public Arvore(String mensagemRaiz) {
		this.raiz = new Noh(mensagemRaiz);
	}
	
	public void criarNoh(String mensagem, int effect, int param) {
		Noh parametro = get(effect, param);

		Noh filho = new Noh(parametro, mensagem);

		parametro.add(filho);
	}

	public Noh raiz() {
		return raiz;
	}

	public Noh get(int effect) {
		return raiz.filho(effect); 
	}
	
	public Noh get(int effect, int param) {
		return raiz.filho(effect).filho(param);
	}
}
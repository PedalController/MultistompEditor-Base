package br.com.srmourasilva.main;

import java.util.Set;

import br.com.srmourasilva.arvore.populador.Arvore;

public class Main {
	public static void main(String[] args) {
		teste1();
	}

	private static void teste1() {
		String mensagemRaiz = "00 00 00 00 00 00 00 00";
		Arvore arvore = new Arvore(mensagemRaiz);

		String mensagem;

		mensagem = "00 00 00 00 00 00 00 01";
		arvore.criarNoh(mensagem, 0, 0);
		
		//System.out.println(arvore.get(0, 0).mensagem());
		
		mensagem = "00 00 00 00 00 00 00 1A";
		arvore.criarNoh(mensagem, 0, 0);

		mensagem = "00 00 00 00 00 00 01 00";
		arvore.criarNoh(mensagem, 0, 1);

		definaPara(arvore, 0, 0);
	}
	

	private static void definaPara(Arvore arvore, int efeito, int param) {
		Set<Integer> definicao = arvore.get(efeito, param).defina();
		
		String mensagemSinalizada = arvore.raiz().mensagem().sinalizar(definicao);
		System.out.println(mensagemSinalizada);
	}

	private static void definaPara(Arvore arvore, int efeito) {
		Set<Integer> definicao = arvore.get(efeito).defina();
		
		String mensagemSinalizada = arvore.raiz().mensagem().sinalizar(definicao);
		System.out.println(mensagemSinalizada);
	}
}
package br.com.srmourasilva.arvore.dominio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.com.srmourasilva.arvore.util.Mensagem;

public class Noh implements Iterable<Noh> {

	private Mensagem mensagem;
	private Noh pai;
	private List<Noh> filhos;

	public Noh(Noh pai, String mensagem) {
		this(pai, new Mensagem(mensagem));
	}

	public Noh(Noh pai, Mensagem mensagem) {
		this(mensagem);
		this.pai = pai;
	}

	public Noh(String mensagem) {
		this(new Mensagem(mensagem));
	}

	private Noh(Mensagem mensagem) {
		this.mensagem = mensagem;
		this.filhos = new ArrayList<>();
	}

	public Noh pai() {
		return pai;
	}

	public Noh filho(int index) {
		for (int i=filhos.size()-1; i<index; i++)
			add(new Noh(this, this.mensagem()));

		return filhos.get(index);
	}

	public void add(Noh filho) {
		filhos.add(filho);
	}

	public Iterator<Noh> iterator() {
		return filhos.iterator();
	}
	
	/** 
	 * Define todas as mudanças possíveis com base nos filhos
	 */
	public Set<Integer> defina() {
		if (isFolha())
			return mensagem.compararCom(pai.mensagem);

		Set<Integer> retorno = new HashSet<>();
		for (Noh filho : filhos)
			retorno.addAll(filho.defina());

		return retorno;
	}

	public boolean isFolha() {
		return filhos.size() == 0;
	}
	
	public Mensagem mensagem() {
		return mensagem;
	}
}
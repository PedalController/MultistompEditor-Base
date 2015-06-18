package br.com.srmourasilva.decoder.zoom.g3.v2;

import java.util.Set;

import br.com.srmourasilva.arvore.populador.Arvore;
import br.com.srmourasilva.decoder.arquitetura.DecodeFor;
import br.com.srmourasilva.decoder.arquitetura.DecoderSeparator;

public class ZoomG3Decoder {
	private Arvore arvore;
	
	public static void main(String[] args) {
		new ZoomG3Decoder();
	}

	public ZoomG3Decoder() {
		// TUDO ZERADO
		//String msg = "f0 52 00 5a 28 50 56 0c 00 01 20 03 00 02 64 0c 64 64 00 56 00 00 00 00 00 00 00 00 00 08 00 00 00 56 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 0c 04 0c 64 64 00 56 00 00 00 00 00 00 00 00 00 00 00 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7";
		// FD COMBO - Zerado
		String msg = "f0 52 00 5a 28 40 39 00 00 00 00 00 00 02 00 00 00 00 00 56 00 00 00 00 00 00 00 00 00 08 00 00 00 56 00 00 00 00 00 00 00 00 00 00 00 20 00 56 00 00 00 00 00 01 00 00 00 00 00 00 56 00 00 00 00 00 00 00 0c 04 0c 64 64 00 56 00 00 00 00 00 00 00 00 00 00 00 00 00 64 00 1c 00 00 20 00 40 58 62 61 61 61 00 61 61 61 61 61 61 00 f7";
		this.arvore = new Arvore(msg);

		add(arvore, new Effect0Param0());
		add(arvore, new Effect0Param1());
		add(arvore, new Effect0Param2());
		add(arvore, new Effect0Param3());
		add(arvore, new Effect0Param4());
		add(arvore, new Effect0Param5());
		add(arvore, new Effect0Param6());
		add(arvore, new Effect0Param7());

		add(arvore, new Effect0Param8TapTempo());
		
		add(arvore, new Effect0Effect1());

		/*
		add(arvore, new Effect4Param0());

		add(arvore, new Effect5Param0());
		add(arvore, new Effect5Param1());
		add(arvore, new Effect5Param2());
		*/

		System.out.println();
		definaPara(arvore, 0);
		System.out.println();

		/*
		defina(Effect0Param0.class);
		defina(Effect0Param1.class);
		defina(Effect0Param2.class);
		defina(Effect0Param3.class);
		defina(Effect0Param4.class);
		defina(Effect0Param5.class);
		defina(Effect0Param6.class);
		defina(Effect0Param7.class);

		defina(Effect0Param8TapTempo.class);
		*/
		
		defina(Effect0Effect1.class);
		
		/*
		System.out.println();
		definaPara(arvore, 4);
		System.out.println();

		defina(Effect4Param0.class);
		
		System.out.println();
		definaPara(arvore, 5);
		System.out.println();

		defina(Effect5Param0.class);
		defina(Effect5Param1.class);
		defina(Effect5Param2.class);
		*/
	}

	private void add(Arvore arvore, DecoderSeparator separator) {
		DecodeFor decodeFor = anotacaoDe(separator);
		
		for (String mensagem : separator.mensagens())
			arvore.criarNoh(mensagem, decodeFor.effect(), decodeFor.param());
	}

	private DecodeFor anotacaoDe(DecoderSeparator separator) {
		return separator.getClass().getAnnotation(DecodeFor.class);
	}

	private void defina(Class<? extends DecoderSeparator> classe) {
		DecodeFor decodeFor = classe.getAnnotation(DecodeFor.class);

		if (decodeFor.param() == -1)
			definaPara(arvore, decodeFor.effect());
		else
			definaPara(arvore, decodeFor.effect(), decodeFor.param());
	}

	private void definaPara(Arvore arvore, int efeito) {
		Set<Integer> definicao = arvore.get(efeito).defina();
		
		String mensagemSinalizada = arvore.raiz().mensagem().sinalizar(definicao);

		System.out.println("Efeito: " + efeito);
		System.out.println(mensagemSinalizada);
	}

	private static void definaPara(Arvore arvore, int efeito, int param) {
		Set<Integer> definicao = arvore.get(efeito, param).defina();
		
		String mensagemSinalizada = arvore.raiz().mensagem().sinalizar(definicao);
		
		System.out.println("Efeito: " + efeito + " Parametro " + param);
		System.out.println(mensagemSinalizada);
	}
}

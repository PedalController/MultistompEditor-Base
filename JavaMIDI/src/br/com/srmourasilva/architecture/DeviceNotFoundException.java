package br.com.srmourasilva.architecture;

@SuppressWarnings("serial")
public class DeviceNotFoundException extends RuntimeException {
	public DeviceNotFoundException(String erro) {
		super(erro);
	}
}

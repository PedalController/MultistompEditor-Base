package br.com.srmourasilva.multistomp.connection;

import java.util.List;
import java.util.Optional;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.arvore.util.BinarioUtil;
import br.com.srmourasilva.domain.PedalType;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.multistomp.Multistomp;
import br.com.srmourasilva.multistomp.connection.codification.MessageDecoder;
import br.com.srmourasilva.multistomp.connection.codification.MessageDecoderFactory;
import br.com.srmourasilva.multistomp.connection.codification.MessageEncoder;
import br.com.srmourasilva.multistomp.connection.codification.MessageEncoderFactory;
import br.com.srmourasilva.multistomp.connection.transport.MidiReader;
import br.com.srmourasilva.multistomp.connection.transport.MidiReader.MidiReaderListenner;
import br.com.srmourasilva.multistomp.connection.transport.MidiSender;

public class MidiConnection implements MidiReaderListenner {

	public interface OnUpdateListenner {
		void update(Messages messages);
	}
	
	private Multistomp multistomp;

	private MidiSender sender;
	private MidiReader reader;

	private MessageEncoder encoder;
	private MessageDecoder decoder;	

	private Optional<OnUpdateListenner> listenner = Optional.empty();

	public MidiConnection(Multistomp multistomp, PedalType pedalType) throws DeviceNotFoundException {
		this.multistomp = multistomp;

		this.sender = new MidiSender(pedalType);
		this.reader = new MidiReader(pedalType);
		reader.setListenner(this);

		this.encoder = MessageEncoderFactory.For(pedalType);
		this.decoder = MessageDecoderFactory.For(pedalType);
	}

	/*************************************************/
	
	public void start() throws MidiUnavailableException {
		sender.start();
		reader.start();
	}

	public void stop() {
		sender.stop();
		reader.stop();
	}

	/*************************************************/

	public void send(Messages messages) {
		for (MidiMessage midiMessage : generateMidiMessages(messages))
			this.send(midiMessage);
	}

	private List<MidiMessage> generateMidiMessages(Messages messages) {
		return encoder.encode(messages);
	}

	public void send(MidiMessage message) {
		System.out.println("MIDI sended: ");
		System.out.println(" " + BinarioUtil.byteArrayToHex(message.getMessage()));

		this.sender.send(message);
	}

	/*************************************************/

	public void setListenner(OnUpdateListenner listenner) {
		this.listenner = Optional.of(listenner);
	}

	@Override
	public void onDataReceived(MidiMessage message) {
		System.out.println("MIDI received: ");
    	System.out.println(" " + BinarioUtil.byteArrayToHex(message.getMessage()));

		if (!decoder.isForThis(message)) {
			System.out.println(" unknown ");
			return;
		}

		Messages messagesDecoded = decoder.decode(message, multistomp);

    	if (listenner.isPresent())
			this.listenner.get().update(messagesDecoded);
	}
}

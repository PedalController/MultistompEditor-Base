package br.com.srmourasilva.multistomp.connection.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiUnavailableException;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.domain.PedalType;

public abstract class MidiTransmition {
	
	private MidiDevice device;

	public MidiTransmition(PedalType pedalType) throws DeviceNotFoundException {
		List<Info> devices = MidiTransmition.findDevices(pedalType);

		Optional<MidiDevice> device = locateDeviceIn(devices);

		if (!device.isPresent())
			throw new DeviceNotFoundException("Midi "+deviceType()+" device not found for: " + pedalType + " ("+pedalType.getUSBName()+")");
		else
			this.device = device.get();
	}

	private Optional<MidiDevice> locateDeviceIn(List<Info> devices) {
		MidiDevice device;

		for (Info deviceInfo : devices) {
			try {
				device = MidiSystem.getMidiDevice(deviceInfo);
			} catch (MidiUnavailableException e) {
				continue;
			}

			if (isThis(device))
				return Optional.of(device);
		}

		return Optional.empty();
	}

	protected abstract boolean isThis(MidiDevice device);

	protected abstract String deviceType();

	public void start() throws MidiUnavailableException {
		device.open();
	}

	public void stop() {
    	device.close();
    }
	
	protected MidiDevice device() {
		return device;
	}

	///////////////////////////////////////////////////

	/** @return all devices that corresponding
	 *  the PedalType
	 */
	public static List<Info> findDevices(PedalType type) {
		List<Info> devices = new ArrayList<Info>();

		Info[] infos = MidiSystem.getMidiDeviceInfo();
		Info device;

		for (int i=0; i<infos.length; i++) {
			device = infos[i];

			if (device.getName().contains(type.getUSBName()))
				devices.add(device);
		}

		return devices;
	}
}

JavaMIDI
=============

This ambitious project aims to provide a single API for MIDI control pedal ANY multi-effect

Test:
--------

### Gui

Execute EasyEditShare.jar (DoubleClick or java -jar EasyEditShare.jar). Open when the real multistomp has gone be connected on PC.

![Live example](https://github.com/SrMouraSilva/JavaPedalMIDI/blob/master/example/Use_example.png?raw=true)

### Terminal

Execute br.com.srmourasilva.main.KeyboardController.java
 
(Old) example: https://www.youtube.com/watch?v=yWB6dpbQ1xc

To control/detect what?
-----------------------

### To detect:

* **CommonCause.TO_PATCH** : Patch changed to specific patch
* **CommonCause.GENERAL_VOLUME** : General volume changed
* **CommonCause.PATCH_NUMBER** : Details of a patch number
* **CommonCause.PATCH_VOLUME** : Details of a patch volume
* **CommonCause.PATCH_NAME** : Details of a patch name
* **CommonCause.EFFECT_ACTIVE** : Specific effect actived
* **CommonCause.EFFECT_DISABLE** : Specific effect disabled
* **CommonCause.EFFECT_TYPE** : Specific effect type
* **CommonCause.PARAM_VALUE** : Specific param of effect actived

### To control:

* Patch
 * PedalController.nextPatch()
 * PedalController.beforePatch()
 * PedalController.toPatch(int index)
* Current patch
 * PedalController.toogleEffect(int idEffect)
 * PedalController.hasActived(int idEffect)
 * PedalController.activeEffect(int idEffect)
 * PedalController.disableEffect(int idEffect)
 * PedalController.setEffectParam(int idEffect, int idParam, int value) 
 * PedalController.getAmountEffects()
* All the multistomp options
 * PedalController.multistomp()


Support:
--------
* PedalCompany.ZoomCorp
 * ~~PedalType.G2Nu~~ (basic, not tested)
 * PedalType.G3

| PedalType    | CommonCause    | Detects pedal change? | Request changes to pedal? |
|--------------|----------------|-----------------------|---------------------------|
| PedalType.G3 | TO_PATCH       | (x)                   | (x)                       |
| PedalType.G3 | GENERAL_VOLUME | ( )                   | ( )                       |
| PedalType.G3 | PATCH_NUMBER   | ( ) *                 | TO_PATCH equivalent       |
| PedalType.G3 | PATCH_VOLUME   | ( )                   | ( )                       |
| PedalType.G3 | PATCH_NAME     | (x)                   | ( )                       |
| PedalType.G3 | EFFECT_ACTIVE  | (x)                   | (x)                       |
| PedalType.G3 | EFFECT_DISABLE | (x)                   | (x)                       |
| PedalType.G3 | EFFECT_TYPE    | ( ) *                 | ( )                       |
| PedalType.G3 | PARAM_VALUE    | (x)                   | (x)                       |

* Only by ZoomGSeriesMessages.REQUEST_SPECIFIC_PATCH_DETAILS(idPatch). It inform
 * PATCH_NUMBER
 * PATCH_NAME
 * EFFECT_ACTIVE
 * EFFECT_DISABLE
 * EFFECT_TYPE


How to use:
-----------

### Changing

```java
// Throws DeviceNotFoundException if not found
PedalController multistomp = PedalControllerFactory.searchPedal();

// Init the system (not your pedal)
// Throws MidiUnavailableException
multistomp.on();

// TO ZOOM G2Nu
// 0 to 7: Comp to Reverb
// 8 Mute
// 9 Bypass

multistomp.activeEffect(0);
multistomp.disableEffect(1);

// TO ZOOM G3
// 0 to 5 (6 Pedals)
// ...

// Set Patch
multistomp.beforePatch();
multistomp.nextPatch();
multistomp.setPatch(99);

System.out.println(pedaleira);

// Turn down the system, not your pedal :P
multistomp.off();
```

### Detect changes

You can receive notifications directly from your pedal

```java
PedalController multistomp = PedalControllerFactory.searchPedal();

pedal.addListener(messages -> {
	messages.getBy(CommonCause.ACTIVE_EFFECT)
			.forEach(message -> message.details().effect);
	messages.getBy(CommonCause.DISABLE_EFFECT)
			.forEach(message -> message.details().effect);

	messages.getBy(CommonCause.TO_PATCH)
			.forEach(message -> message.details().patch);

	messages.getBy(CommonCause.PATCH_NAME)
			.forEach(message -> (String) message.details().other);

	//messages.getBy(CommonCause.SET_PARAM)
	//		.forEach(message -> System.out.println(pedal));
});
```

How to Collabore
----------------

Understand the structure:

* Archicheture: The utils and exceptions of the system;
* Controller: It offers a simple API to use;
** MultistompChanger: 
* Domain: The multistomp structure and message structure;
* Connection: The connection with Real Multistomp and this lib;
* Multistomp: Specific implementation for pedals;
* Test: Unit tests;
* Main: User demonstrations.

### Define

1. Add PedalCompany in controller.PedalCompany.java;
2. Add "if" for your pedal company in controller.PedalControllerFactory.java;
3. Add your multistomp definition in domain.PedalType.

### Implement

1. In multistomp, create your pedal package;
2. Extends domain.Multistomp.java if necessary;
3. Implements MultistompFactory.java if necessary
4. Implements MessageEncoder.java for encript the commands in MIDI messages.
4. Implements MessageDecoder.java for decript the MIDI messages commands.

Thanks for:
-----------
* https://github.com/vegos/ArduinoMIDI
* http://www.onicos.com/staff/iz/formats/midi-event.html
* http://www.loopers-delight.com/LDarchive/201104/msg00195.html
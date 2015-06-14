JavaMIDI
=============

This ambitious project aims to provide a single API for MIDI control pedal ANY multi-effect



Example: https://www.youtube.com/watch?v=yWB6dpbQ1xc

Support:
--------
* PedalCompany.ZoomCorp
 * PedalType.G2Nu
 * PedalType.G3

How to use:
-----------

```java
PedalController multistomp = PedalControllerFactory.searchPedal();

// Init the system, not your pedal hihi
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

How to Collabore
----------------

Understand the structure:

* src: Classes not refactored yet;
* Archicheture: The utils of the system;
* Controller: It offers a simple API to use;
* Domain: The multistomp structure and message structure;
* Multistomp: Specific implementation for pedals;
* Test: Junit unit tests;
* Main: Use demonstrations.

### Define

1. Add PedalCompany in controller.PedalCompany.java;
2. Add "if" for your pedal company in controller.PedalControllerFactory.java;
3. Add your multistomp definition in src.PedalType.

### Implement

1. In multistomp, create your pedal package;
2. Extends domain.Multistomp.java if necessary;
3. Implements MultistompFactory.java if necessary
4. Implements MessageEncoder.java for encript the commands in MIDI messages.

Thanks for:
-----------
* https://github.com/vegos/ArduinoMIDI
* http://www.onicos.com/staff/iz/formats/midi-event.html
* http://www.loopers-delight.com/LDarchive/201104/msg00195.html
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
PedalController pedaleira = PedalControllerFactory.searchPedal();

// Init the system, not your pedal hihi
pedaleira.on();

// TO ZOOM G2Nu
// 0 to 7: Comp to Reverb
// 8 Mute
// 9 Bypass

pedaleira.activeEffect(0);
pedaleira.disableEffect(1);

// TO ZOOM G3
// 0 to 5 (6 Pedals)
// ...

// Set Patch
pedaleira.beforePatch();
pedaleira.nextPatch();
pedaleira.setPatch(99);

System.out.println(pedaleira);

// Turn down the system, not your pedal :P
pedaleira.off();
```

For all commands, please view:
br.com.srmourasilva.multieffects.controller.PedalController.java

Thanks for:
-----------
* https://github.com/vegos/ArduinoMIDI
* http://www.onicos.com/staff/iz/formats/midi-event.html
* http://www.loopers-delight.com/LDarchive/201104/msg00195.html
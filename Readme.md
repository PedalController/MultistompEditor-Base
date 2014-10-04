JavaMIDI
=============
Java Version of https://github.com/vegos/ArduinoMIDI

Support:
--------
* PedalType.G2Nu

How to use:
-----------

```java
Pedal pedaleira = ZoomFactory.getPedal(PedalType.G2Nu);

MidiSender sender = new MidiSender(pedaleira);
sender.start();

// TO ZOOM G2Nu

// 0 to 7: Comp to Reverb
pedaleira.activeEffect(0);
pedaleira.disableEffect(1);

// Mute
pedaleira.activeEffect(8);
// Bypass
pedaleira.activeEffect(9);

// Set Patch
pedaleira.beforePatch(); // -1
pedaleira.nextPatch();   // -1
pedaleira.setPatch(99);

System.out.println(pedaleira);

sender.stop();
```
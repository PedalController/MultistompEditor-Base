package com.pi4j.component.util;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class DataTransmitionUtil {
    public enum BitOrderFirst {
        LSB,
        MSB
    }
    
    /**
     * @deprecated FIXME Use native shiftOut!
     */
    @Deprecated
    public static void shiftOut(byte data, GpioPinDigitalOutput dataPin, GpioPinDigitalOutput clockPin, BitOrderFirst order) {
        if (order == BitOrderFirst.MSB) {
            writeDataMSBFirst(data, dataPin, clockPin);
        } else {
            writeDataLSBFirst(data, dataPin, clockPin);
        }
    }
    
    private static void writeDataLSBFirst(byte data, GpioPinDigitalOutput dataPin, GpioPinDigitalOutput clockPin) {
        for (byte i = 0; i < 8; ++i) {
            PinState bitState = (data & (1 << i)) >> i == 1 ? PinState.HIGH : PinState.LOW;
            dataPin.setState(bitState);

            toggleClock(clockPin);
        }
    }

    private static void writeDataMSBFirst(byte data, GpioPinDigitalOutput dataPin, GpioPinDigitalOutput clockPin) {
        for (byte i = 7; i >= 0; --i) {
            PinState bitState = (data & (1 << i)) >> i == 1 ? PinState.HIGH : PinState.LOW;
            dataPin.setState(bitState);

            toggleClock(clockPin);
        }
    }

    private static void toggleClock(GpioPinDigitalOutput clock) {
        clock.high();
        // The pin changes usign wiring pi are 20ns?
        // The pi4j in Snapshot 1.1.0 are 1MHz ~ 1 microssecond in Raspberry 2      http://www.savagehomeautomation.com/projects/raspberry-pi-with-java-programming-the-internet-of-things-io.html#follow_up_pi4j
        // Its necessary only 10ns    Pag 22 - https://www.sparkfun.com/datasheets/LCD/Monochrome/Nokia5110.pdf
        // Not discoment :D
        //Gpio.delayMicroseconds(CLOCK_TIME_DELAY);
        clock.low();
    }
}

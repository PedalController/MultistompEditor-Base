package com.pi4j.component.util;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DataTransmitionUtil.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 * 
 * @author SrMouraSilva
 * Based in 2013 Giacomo Trudu - wicker25[at]gmail[dot]com
 * Based in 2010 Limor Fried, Adafruit Industries
 * Based in CORTEX-M3 version by Le Dang Dung, 2011 LeeDangDung@gmail.com (tested on LPC1769)
 * Based in Raspberry Pi version by Andre Wussow, 2012, desk@binerry.de
 * Based in Raspberry Pi Java version by Cleverson dos Santos Assis, 2013, tecinfcsa@yahoo.com.br
 */

/**
 * Utils for change data transmittion
 */
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

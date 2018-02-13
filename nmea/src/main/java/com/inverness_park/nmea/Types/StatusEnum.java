/*
 * Copyright (c) 2018.  Inverness Park Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or
 *  substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.inverness_park.nmea.Types;

/**
 * ﻿GPS Status (NMEA-0183)
 */
public enum StatusEnum {
    Active,
    Void;

    /**
     * From enum to the NMEA representation
     * @return NMEA string format for the given mode value
     */
    public String ToNmeaString() {
        switch (this) {
            case Active:
                return "A";
            case Void:
                return "V";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * From NMEA representation to enum
     * @param s String as read from a NMEA message
     * @return Corresponding enumvalue
     */
    public static StatusEnum FromNmeaString(String s) {
        switch (s.trim().toUpperCase()) {
            case "A":
                return StatusEnum.Active;
            case "V":
                return StatusEnum.Void;
            default:
                throw new IllegalArgumentException(s);
        }
    }
}

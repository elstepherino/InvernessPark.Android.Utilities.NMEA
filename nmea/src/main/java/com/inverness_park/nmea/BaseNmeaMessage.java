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

package com.inverness_park.nmea;

import android.content.Context;

import java.nio.charset.StandardCharsets;

/**
 * Abstract base class wrapper for NMEA messages
 */
public abstract class BaseNmeaMessage implements INmeaMessage {
    /**
     * Field delimiter
     */
    protected static final String DELIM_FIELDS = ",";

    /**
     * Field delimiter set
     */
    static final char[] DELIMS = new char[] { ',' };

    /**
     * Computes the checksum
     * @param bytes
     * @param index
     * @param count
     * @return
     */
    public static byte ComputeChecksum(byte[] bytes, int index, int count) {
        byte rc = 0;
        int maxLen = Math.min(bytes.length-index, count);
        for (int i = 0; i < maxLen; ++i) {
            rc ^= bytes[index + i];
        }
        return rc;
    }

    /**
     * Stringification to full NMEA format, including SOM and EOM delimiters.
     * @return
     */
    @Override
    public String toString() {
        return "$" + get_payload() + "*" + String.format("%02X", get_checksum()) + "\r\n";
    }

    /**
     * As per NMEA 0183 documentation
     * @return
     */
    @Override
    public abstract String get_description( Context context ) ;

    /**
     * NMEA sentence without the leading "$" and the trailing "*" & checksum
     * @return
     */
    @Override
    public abstract String get_payload() ;

    /**
     * Get the NMEA ckecksum
     * @return
     */
    @Override
    public byte get_checksum() {
        byte[] payloadBytes = get_payload().getBytes( StandardCharsets.US_ASCII) ;
        return ComputeChecksum(payloadBytes, 0, payloadBytes.length);
    }

    /**
     * Loads NMEA fields that have been parsed into stirng tokens
     * @param tokens
     * @return
     */
    @Override
    public abstract INmeaMessage parseFields( String[] tokens ) ;
}

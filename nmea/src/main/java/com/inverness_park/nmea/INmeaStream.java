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

package com.inverness_park.nmea;/**
 * ﻿NMEA stream interface contract
 */

public interface INmeaStream {

    /**
     * Handler for received NMEA messages
     */
    interface OnNMEAMessageReceivedHandler {
        void NMEAMessageReceived( byte[] bytes, int index, int count ) ;
    }

    /**
     * Sets the handler for received NMEA messages
     * @param handler
     */
    void set_OnNMEAMessageReceivedHandler( OnNMEAMessageReceivedHandler handler ) ;

    /**
     * Number of bytes currently contained in the stream
     * @return
     */
    int get_length();

    /**
     * Stream's internal capacity
     * @return
     */
    int get_capacity();

    /**
     * Space left in the stream before it gets full
     * @return
     */
    int get_available();

    /**
     * Appends a byte to the stream
     * @param b
     */
    void Append(byte b);

    /**
     * Appends many bytes to the stream
     * @param bytes
     */
    void Append(byte [] bytes);

    /**
     * Resets the stream
     */
    void Reset();
}

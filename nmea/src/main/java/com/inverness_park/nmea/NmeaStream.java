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

/**
 * Default implementation of the NMEA stream contract
 */

public class NmeaStream implements INmeaStream {

    /**
     * NMEA-0183 mandates an 80 byte message limit, but nobody follows this rule.
     * So let's be safe and use a big enough buffer.
     */
    private static final int MAX_RECEIVE_SIZE = 1024;

    /**
     * Start of NMEA sentence
     */
    private static final char DELIM_SOM = '$';

    /**
     * End of NMEA sentence
     */
    private static final char DELIM_EOM = '\n';

    /**
     * Placeholder for received bytes
     */
    private byte[] _buffer = new byte[MAX_RECEIVE_SIZE];

    /**
     * Offset in the buffer, indicating the number of bytes buffered thus far.
     */
    private int _offset = 0;

    /**
     * StateEnum of NMEA parsing
     */
    public enum StateEnum {
        Idle,   // Currently not parsing a NMEA sentence (buffering bytes)
        Payload // Actively parsing a NMEA sentence
    }

    /**
     * Default starting state
     */
    private StateEnum _state = StateEnum.Idle;

    private OnNMEAMessageReceivedHandler _onNMEAMessageReceived ;

    private void invokeOnNMEAMessageReceivedHandler(byte[] bytes, int index, int count) {
        if ( _onNMEAMessageReceived != null ) {
            _onNMEAMessageReceived.NMEAMessageReceived( bytes, index, count );
        }
    }

    public StateEnum get_state() {
        return _state;
    }

    @Override
    public void set_OnNMEAMessageReceivedHandler( OnNMEAMessageReceivedHandler handler ) {
        _onNMEAMessageReceived = handler ;
    }

    /**
     * Number of bytes buffered thus far.
     * @return
     */
    @Override
    public int get_length() {
        return _offset;
    }

    /**
     * Total number of bytes that can be buffered
     * @return
     */
    @Override
    public int get_capacity() {
        return _buffer.length;
    }

    /**
     * Available space left in the buffer
     * @return
     */
    @Override
    public int get_available() {
        return get_capacity() - get_length();
    }

    /**
     * Appends a byte to the buffer, and invokes the NMEA message callback if a complete NMEA sentence has been received.
     * @param b
     */
    @Override
    public void Append( byte b ) {

        // ... If we've run out of space
        if (get_available() == 0) {
            // ... reset everything
            Reset();
        }

        switch (_state) {
            // ... If we're waiting for the start of NMEA message
            case Idle:

                // ... If the byte is indeed the SOM
                if ((char)b == DELIM_SOM) {

                    // ... Append the byte to the buffer
                    _buffer[_offset++] = b;

                    // ... We are now officially parsing a NMEA message
                    _state = StateEnum.Payload;
                }

                // *** Note: we only buffe rbytes if we're parsing a NMEA sentence
                break;

            // ... If we're actively in the process of parsing a NMEA message
            case Payload:

                boolean isMessageReceived = false;

                // ... If we get yet another SOM, assume corruption and use this as the new SOM position
                if ((char)b == DELIM_SOM) {
                    _offset = 0;
                }
                // ... If the byte is the End of NMEA message, we have received a complete NMEA sentence
                else if ((char)b == DELIM_EOM) {
                    isMessageReceived = true;
                }

                // ... AAppend the byte to the buffer
                _buffer[_offset++] = b;

                // ... If we have a complete NMEA sentence
                if (isMessageReceived) {
                    // ... Invoke the callback
                    invokeOnNMEAMessageReceivedHandler(_buffer, 0, get_length());
                    // ... Then reset the state and the buffer
                    Reset();
                }
                break;
        }
    }

    @Override
    public void Append( byte[] bytes ) {
        for ( byte b : bytes ) {
            Append(b);
        }
    }

    /**
     * Resets the parsing state and offset in the receive buffer
     */
    @Override
    public void Reset() {
        _offset = 0 ;
        _state = StateEnum.Idle;
    }
}

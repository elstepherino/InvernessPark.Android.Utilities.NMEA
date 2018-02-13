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

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by stephanm on 2018-02-12.
 */

public class NmeaStreamUnitTest extends INmeaStreamUnitTest {

    /**
     * Default constructor uses a default NmeaStream
     */
    public NmeaStreamUnitTest() {
        super( new NmeaStream() );
    }

    /**
     * placeholder for a boolean variable that gets set from within a listener callback
     */
    class BoolPlaceholder {
        private boolean _state = false ;

        public boolean is_state() {
            return _state;
        }

        public void set_state( boolean _state ) {
            this._state = _state;
        }
    }

    @Test
    public void StateIsIdle() {
        get_strm().Reset() ;
        assertEquals( ((NmeaStream)get_strm()).get_state(), NmeaStream.StateEnum.Idle ) ;
    }

    @Test
    public void StateIsPayload() {
        get_strm().Reset() ;
        get_strm().Append( SOM );
        assertEquals( ((NmeaStream)get_strm()).get_state(), NmeaStream.StateEnum.Payload ) ;
        get_strm().Append( SOM );
        assertEquals( ((NmeaStream)get_strm()).get_state(), NmeaStream.StateEnum.Payload ) ;
        get_strm().Append( SOM );
        assertEquals( ((NmeaStream)get_strm()).get_state(), NmeaStream.StateEnum.Payload ) ;
        get_strm().Append( SOM );
        assertEquals( ((NmeaStream)get_strm()).get_state(), NmeaStream.StateEnum.Payload ) ;
    }

    @Test
    public void  ReceiveCompleteMessages() {
        get_strm().Reset() ;
        for ( String nmeaSentence: get_sampleData() ) {
            ReceiveCompleteMessage(nmeaSentence) ;
        }
    }

    private void ReceiveCompleteMessage( String randomNmeaString ) {
        final BoolPlaceholder msgIsReceived = new BoolPlaceholder();
        get_strm().set_OnNMEAMessageReceivedHandler( new INmeaStream.OnNMEAMessageReceivedHandler() {
            @Override
            public void NMEAMessageReceived( byte[] bytes, int index, int count ) {
                msgIsReceived.set_state( true );
            }
        } );
        byte[] nmeaBytes = randomNmeaString.getBytes(StandardCharsets.US_ASCII);
        get_strm().Append( nmeaBytes );
        assertTrue( "msgIsReceived", msgIsReceived.is_state() ) ;
    }
}

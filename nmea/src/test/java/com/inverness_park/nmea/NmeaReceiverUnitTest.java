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

import android.util.Log;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * These unit tests simply verify that we're parsing the correct data type
 */

public class NmeaReceiverUnitTest implements INmeaHandler {

    private NmeaReceiver _receiver;
    private BooleanPlaceholder _ggaReceived = new BooleanPlaceholder() ;
    private BooleanPlaceholder _gsaReceived = new BooleanPlaceholder() ;
    private BooleanPlaceholder _gstReceived = new BooleanPlaceholder() ;
    private BooleanPlaceholder _gsvReceived = new BooleanPlaceholder() ;
    private BooleanPlaceholder _hdtReceived = new BooleanPlaceholder() ;
    private BooleanPlaceholder _rmcReceived = new BooleanPlaceholder() ;
    private BooleanPlaceholder _vtgReceived = new BooleanPlaceholder() ;

    /**
     * Default constructor
     */
    public NmeaReceiverUnitTest() {
        _receiver = new NmeaReceiver( this ) ;
        _receiver.setMessageHandlers( new NmeaReceiver.MessageHandlers() {
            @Override
            public void OnNmeaMessageFailedChecksum( byte[] bytes, int index, int count, byte expected, byte actual ) {
                String nmea = new String( bytes, index, count, StandardCharsets.US_ASCII ) ;
                assertTrue( "OnNmeaMessageFailedChecksum: " + nmea, false );
            }

            @Override
            public void OnNmeaMessageDropped( byte[] bytes, int index, int count, String reason ) {
                String nmea = new String( bytes, index, count, StandardCharsets.US_ASCII ) ;
                assertTrue( "OnNmeaMessageDropped: " + nmea + "; " + reason, false ) ;
            }

            @Override
            public void OnNmeaMessageIgnored( byte[] bytes, int index, int count ) {
                String nmea = new String( bytes, index, count, StandardCharsets.US_ASCII ) ;
                assertTrue( "OnNmeaMessageIgnored: " + nmea, false ) ;
            }
        } );
    }

    /**
     * Class used to set state from within callbacks
     */
    class BooleanPlaceholder {
        private boolean _state = false ;

        public boolean is_state() {
            return _state;
        }

        public void set_state( boolean _state ) {
            this._state = _state;
        }
    }

    private void TestValidNmeaSentence( String sentence, BooleanPlaceholder placeholder ) {
        try {
            placeholder.set_state( false );
            byte[] nmeaBytes = sentence.getBytes( StandardCharsets.US_ASCII );
            _receiver.Receive( nmeaBytes );
            assertEquals( sentence, placeholder.is_state(), true );
        }
        catch (Exception e ) {
            assertEquals( sentence, false, true );
        }
    }

    @Test
    public void TestValidGGA() {
        String [] sampleData = new String [] {
            "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n",
            "$GPGGA,092751.000,5321.6802,N,00630.3371,W,1,8,1.03,61.7,M,55.3,M,,*75\r\n",
        } ;

        for ( String sample : sampleData ) {
            TestValidNmeaSentence( sample, _ggaReceived ) ;
        }
    }
    @Test
    public void TestValidGSA() {
        String [] sampleData = new String [] {
                "$GPGSA,A,3,10,07,05,02,29,04,08,13,,,,,1.72,1.03,1.38*0A\r\n",
                "$GPGSA,A,3,10,07,05,02,29,04,08,13,,,,,1.72,1.03,1.38*0A\r\n",
        } ;

        for ( String sample : sampleData ) {
            TestValidNmeaSentence( sample, _gsaReceived ) ;
        }
    }
    @Test
    public void TestValidGST() {
        String [] sampleData = new String [] {
                "$GPGST,141451.00,1.18,0.00,0.00,0.0000,0.00,0.00,0.00*6B\r\n",
                "$GNGST,143333.00,7.38,1.49,1.30,68.1409,1.47,1.33,2.07*4A\r\n"
        } ;

        for ( String sample : sampleData ) {
            TestValidNmeaSentence( sample, _gstReceived ) ;
        }
    }
    @Test
    public void TestValidGSV() {
        String [] sampleData = new String [] {
                "$GPGSV,3,1,11,10,63,137,17,07,61,098,15,05,59,290,20,08,54,157,30*70\r\n",
                "$GPGSV,3,2,11,02,39,223,19,13,28,070,17,26,23,252,,04,14,186,14*79\r\n",
                "$GPGSV,3,3,11,29,09,301,24,16,09,020,,36,,,*76\r\n",
                "$GPGSV,3,1,11,10,63,137,17,07,61,098,15,05,59,290,20,08,54,157,30*70\r\n",
                "$GPGSV,3,2,11,02,39,223,16,13,28,070,17,26,23,252,,04,14,186,15*77\r\n",
                "$GPGSV,3,3,11,29,09,301,24,16,09,020,,36,,,*76\r\n",
        } ;

        for ( String sample : sampleData ) {
            TestValidNmeaSentence( sample, _gsvReceived ) ;
        }
    }
    @Test
    public void TestValidHDT() {
        String [] sampleData = new String [] {
                "$GPHDT,75.5664,T*36\r\n",
                "$GNHDT,75.5554,T*28\r\n"
        } ;

        for ( String sample : sampleData ) {
            TestValidNmeaSentence( sample, _hdtReceived ) ;
        }
    }
    @Test
    public void TestValidRMC() {
        String [] sampleData = new String [] {
                "$GPRMC,092750.000,A,5321.6802,N,00630.3372,W,0.02,31.66,280511,,,A*43\r\n",
                "$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n",
        } ;

        for ( String sample : sampleData ) {
            TestValidNmeaSentence( sample, _rmcReceived ) ;
        }
    }
    @Test
    public void TestValidVTG() {
        String [] sampleData = new String [] {
                "$GPVTG,172.516,T,155.295,M,0.049,N,0.090,K,D*2B\r\n",
                "$GNVTG,134.395,T,134.395,M,0.019,N,0.035,K,A*33\r\n"
        } ;

        for ( String sample : sampleData ) {
            TestValidNmeaSentence( sample, _vtgReceived ) ;
        }
    }

    @Override
    public void HandleGGA( INmeaMessage msg ) {
        _ggaReceived.set_state( true );
    }

    @Override
    public void HandleGSA( INmeaMessage msg ) {
        _gsaReceived.set_state( true );
    }

    @Override
    public void HandleGST( INmeaMessage msg ) {
        _gstReceived.set_state( true );
    }

    @Override
    public void HandleGSV( INmeaMessage msg ) {
        _gsvReceived.set_state( true );
    }

    @Override
    public void HandleHDT( INmeaMessage msg ) {
        _hdtReceived.set_state( true );
    }

    @Override
    public void HandleRMC( INmeaMessage msg ) {
        _rmcReceived.set_state( true );
    }

    @Override
    public void HandleVTG( INmeaMessage msg ) {
        _vtgReceived.set_state( true );
    }
}

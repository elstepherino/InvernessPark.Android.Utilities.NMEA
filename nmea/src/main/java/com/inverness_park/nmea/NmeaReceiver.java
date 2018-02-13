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

import android.util.Xml;

import com.inverness_park.nmea.Sentences.GGA;
import com.inverness_park.nmea.Sentences.GSA;
import com.inverness_park.nmea.Sentences.GST;
import com.inverness_park.nmea.Sentences.GSV;
import com.inverness_park.nmea.Sentences.HDT;
import com.inverness_park.nmea.Sentences.RMC;
import com.inverness_park.nmea.Sentences.VTG;
import com.inverness_park.nmea.Types.NotImplementedException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * his class implements a NMEA receiver.  Use an instance ofthis objects to
 * feed bytes via the 'Receive()' method.
 */

public class NmeaReceiver {

    /**
     * Message handlers used to communicate back to the application when a message is received,
     * dropped, ignored, etc.
     */
    public interface MessageHandlers {
        /**
         * When a NMEA sentence fails its checksum check
         * @param bytes
         * @param index
         * @param count
         * @param expected
         * @param actual
         */
        void OnNmeaMessageFailedChecksum(byte[] bytes, int index, int count, byte expected, byte actual );

        /**
         * When a NMEA sentence is corrupted and dropped
         * @param bytes
         * @param index
         * @param count
         * @param reason
         */
        void OnNmeaMessageDropped(byte[] bytes, int index, int count, String reason);

        /**
         * When a NMEA sentence passes the checksum check, but is not supported
         * @param bytes
         * @param index
         * @param count
         */
        void OnNmeaMessageIgnored(byte[] bytes, int index, int count);
    }

    /**
     * Application-defined callbacks
     */
    private MessageHandlers _messageHandlers ;

    /**
     * Field delimiter
     */
    private static final String DELIMS = ",";

    /**
     * Start of NMEA message delimiter
     */
    private static final char DELIM_SOM = '$';

    /**
     * Checksum delimiter
     */
    private static final char DELIM_CKSUM = '*';

    /**
     * First End of message delimiter
     */
    private static final char DELIM_CR = '\r';

    /**
     * Second end of message delimiter
     */
    private static final char DELIM_LF = '\n';

    /**
     * NMEA stream instance used to parse received bytes
     */
    private INmeaStream _stream;

    /**
     * NMEA message handler provided by the application
     */
    private INmeaHandler _handler;

    /**
     * Constructor
     * @param factory Will create a NmeaStream object for use when parsing NMEA messages
     * @param handler A handler for NMEA sentences successfully received
     */
    public NmeaReceiver( INmeaStreamFactory factory, INmeaHandler handler ) {
        _stream = factory.Create();
        _handler = handler;
        _stream.set_OnNMEAMessageReceivedHandler( _onNMEAMessageReceived );
    }

    /**
     * Constructor
     * @param handler A handler for NMEA sentences successfully received
     */
    public NmeaReceiver(INmeaHandler handler) {
        _stream = new NmeaStream();
        _handler = handler;
        _stream.set_OnNMEAMessageReceivedHandler( _onNMEAMessageReceived );
    }

    /**
     * AAssigns the callback/message handlers
     * @param messageHandlers
     */
    public void setMessageHandlers( MessageHandlers messageHandlers ) {
        _messageHandlers = messageHandlers;
    }

    /**
     * Receives an arbitray number of bytes, to be parsed and handled
     * @param bytes
     */
    public void Receive(byte[] bytes) {
        for (byte b : bytes) {
            _stream.Append(b);
        }
    }

    /**
     * Invokes the proper handler for a NMEA message
     *
     * *** NOTE: If additional NMEA data types are added to the INmeaHandler
     * contract, this method will need to be updated to support it.
     *
     */
    private INmeaStream.OnNMEAMessageReceivedHandler _onNMEAMessageReceived = new INmeaStream.OnNMEAMessageReceivedHandler() {
        @Override
        public void NMEAMessageReceived( byte[] bytes, int index, int count ) {
            // ... Parse the bytes
            int somOffset = index;
            int crOffset = index + count - 2;
            int lnOffset = index + count - 1;
            int cksumOffset = index + count - 5;
            int payloadLen = cksumOffset - 1;

            // .... Sanity check
            if (bytes.length <= 4) {
                invokeOnNmeaMessageDropped(bytes, index, count, "Insufficient number of bytes");
            }
            if ((char)bytes[somOffset] != DELIM_SOM) {
                invokeOnNmeaMessageDropped(bytes, index, count, "Invalid start of message");
            }
            if ((char)bytes[crOffset] != DELIM_CR) {
                invokeOnNmeaMessageDropped(bytes, index, count, "Invalid end of message delimiter (no CR)");
            }
            if ((char)bytes[lnOffset] != DELIM_LF) {
                invokeOnNmeaMessageDropped(bytes, index, count, "Invalid end of message delimiter (no LF)");
            }
            if ((char)bytes[cksumOffset] != DELIM_CKSUM) {
                invokeOnNmeaMessageDropped(bytes, index, count, "Invalid checksum delimiter");
            }

            // ... Verify checksum
            byte msgCksum = FromHexString( bytes, cksumOffset+1 );
            byte compCksum = BaseNmeaMessage.ComputeChecksum(bytes, 1, payloadLen);
            if (msgCksum != compCksum) {
//                invokeOnNmeaMessageFailedChecksum(bytes, index, count, compCksum, msgCksum );
                invokeOnNmeaMessageFailedChecksum(bytes, 1, payloadLen, compCksum, msgCksum );
                return;
            }

            // ... Split up the NMEA sentence into parameters/tokens
            String nmeaSentence = new String(bytes, index + 1, payloadLen, StandardCharsets.US_ASCII );
            String[] tokens = nmeaSentence.split(DELIMS, -1);

            // ... Parse by NMEA data type
            switch (tokens[0].substring(Math.max(0, tokens[0].length() - 3)).toUpperCase()) {
                case "GGA":
                    _handler.HandleGGA(new GGA().parseFields(tokens));
                    break;
                case "GSA":
                    _handler.HandleGSA(new GSA().parseFields(tokens));
                    break;
                case "GST":
                    _handler.HandleGST(new GST().parseFields(tokens));
                    break;
                case "HDT":
                    _handler.HandleHDT(new HDT().parseFields(tokens));
                    break;
                case "GSV":
                    _handler.HandleGSV(new GSV().parseFields(tokens));
                    break;
                case "RMC":
                    _handler.HandleRMC(new RMC().parseFields(tokens));
                    break;
                case "VTG":
                    _handler.HandleVTG(new VTG().parseFields(tokens));
                    break;
                default:
                    invokeOnNmeaMessageIgnored(bytes, index, count);
                    break;
            }
        }

        /**
         * used to extract the HEX byte representing the NMEA checksum
         * @param bytes
         * @param index
         * @return
         */
        private byte FromHexString( byte [] bytes, int index ) {
            byte rc = 0 ;
            rc = (byte) ((Character.digit((char)bytes[index], 16) << 4)
                    + Character.digit((char)bytes[index+1], 16));
            return rc ;
        }
    } ;

    /**
     * Called when ckecksum fails
     * @param bytes byte buffer in which the NMEA senetnce is stored
     * @param index Offset when the NMEA sentence starts
     * @param count Number of bytes in the NMEA sentence
     * @param expected Expected ckscksum
     * @param actual Actual checksum in NMEA sentence
     */
    private void invokeOnNmeaMessageFailedChecksum( byte[] bytes, int index, int count, byte expected, byte actual ) {
        if ( _messageHandlers != null ) {
            _messageHandlers.OnNmeaMessageFailedChecksum( bytes, index, count, expected, actual );
        }
    }

    /**
     * Called when a message payload is invalid and the message must be dropped
     * @param bytes byte buffer in which the NMEA senetnce is stored
     * @param index Offset when the NMEA sentence starts
     * @param count Number of bytes in the NMEA sentence
     * @param reason Descriptive String explaining why the NMEA sentence was dropped
     */
    private void invokeOnNmeaMessageDropped(byte[] bytes, int index, int count, String reason) {
        if ( _messageHandlers != null ) {
            _messageHandlers.OnNmeaMessageDropped( bytes, index, count, reason );
        }
    }

    /**
     * Called when a valid NMEA sentence is not supported
     * @param bytes byte buffer in which the NMEA senetnce is stored
     * @param index Offset when the NMEA sentence starts
     * @param count Number of bytes in the NMEA sentence
     */
    private void invokeOnNmeaMessageIgnored(byte[] bytes, int index, int count) {
        if ( _messageHandlers != null ) {
            _messageHandlers.OnNmeaMessageIgnored( bytes, index, count );
        }
    }
}

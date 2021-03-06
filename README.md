# NMEA-0183 Parser Library in Java for Android

This is a basic NMEA-0183 parser that processes bytes fed into its API. When a complete NMEA sentence is successfully read, application-level handlers are invoked.

# Quick Example

This is the simplest scenario: using the `DefaultNmeaHandler` class.  `DefaultNmeaHandler` implements the contract `INmeaHandler`, which defines support for the following NMEA-0183 data types:
* GGA : _Global Positioning System Fix Data_
* GSA : _Satellite status_
* GSV : _Satellites in view_
* GST : _GPS Pseudorange Noise Statistics_
* HDT : _NMEA heading log_
* RMC : _Recommended Minimum data for gps_
* VTG : _Track made good and ground speed_

`DefaultNmeaHandler` implements the contract by invoking the event handler `LogNmeaMessage` each time a supported NMEA sentence is successfully parsed.
    
        // ... Create an object to handle parsed NMEA messages
        DefaultNmeaHandler nmeaHandler = new DefaultNmeaHandler();
        nmeaHandler.set_OnLogNmeaMessageHandler( new DefaultNmeaHandler.OnLogNmeaMessageHandler() {
            @Override
            public void OnLogNmeaMessage( String msg ) {
                Log.i("NmeaTester","New NMEA Message: " + msg );
            }
        } );

        // ... Create the NMEA receiver
        NmeaReceiver nmeaReceiver = new NmeaReceiver( nmeaHandler ) ;

        // ... Attach handler for NMEA messages that fail NMEA checksum verification
        nmeaReceiver.setMessageHandlers( new NmeaReceiver.MessageHandlers() {
            @Override
            public void OnNmeaMessageFailedChecksum( byte[] bytes, int index, int count, byte expected, byte actual ) {
                String sentence = new String(bytes, index, count, StandardCharsets.US_ASCII ) ;
                Log.e("NmeaTester", "Failed Checksum: " + sentence + "; expected "+expected+" but got " + actual );
            }

            @Override
            public void OnNmeaMessageDropped( byte[] bytes, int index, int count, String reason ) {
                String sentence = new String(bytes, index, count, StandardCharsets.US_ASCII ) ;
                Log.i("NmeaTester","Bad Syntax: "+sentence+"; reason: "+reason );
            }

            @Override
            public void OnNmeaMessageIgnored( byte[] bytes, int index, int count ) {
                String sentence = new String(bytes, index, count, StandardCharsets.US_ASCII ) ;
                Log.i("NmeaTester","Ignored: " + sentence ) ;
            }
        } ) ;

        // ... Your byte receiving logic...
        boolean keepReceiving = true ;
        while ( keepReceiving ) {
            byte [] bytesReceived = /* receive some bytes from socket, file, whatever... */

            // ... Feed the bytes into the NMEA receiver
            nmeaReceiver.Receive( bytesReceived ) ;
        }



The above code will invoke the appropriate callbacks each time a NMEA sentence is received.

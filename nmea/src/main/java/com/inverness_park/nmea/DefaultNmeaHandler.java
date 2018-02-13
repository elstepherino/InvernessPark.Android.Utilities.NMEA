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
 * Convenience NMEA handler class, where all received messages are routed through
 * a single handler callback.
 *
 * *** NOTE:
 * If more NMEA data types are added to the INmeaHandler contract, this class will
 * need to be updated.
 */
public class DefaultNmeaHandler implements INmeaHandler {

    public interface OnLogNmeaMessageHandler {
        void OnLogNmeaMessage(String msg);
    }

    public OnLogNmeaMessageHandler _onLogNmeaMessageHandler ;

    public void set_OnLogNmeaMessageHandler(OnLogNmeaMessageHandler handler) {
        _onLogNmeaMessageHandler = handler ;
    }

    private void invokeOnLogNmeaMessageHandler( INmeaMessage msg ) {
        if ( _onLogNmeaMessageHandler != null ) {
            _onLogNmeaMessageHandler.OnLogNmeaMessage( msg.toString() );
        }
    }

    @Override
    public void HandleGGA( INmeaMessage msg ) {
        invokeOnLogNmeaMessageHandler(msg);
    }

    @Override
    public void HandleGSA( INmeaMessage msg ) {
        invokeOnLogNmeaMessageHandler(msg);
    }

    @Override
    public void HandleGST( INmeaMessage msg ) {
        invokeOnLogNmeaMessageHandler(msg);
    }

    @Override
    public void HandleGSV( INmeaMessage msg ) {
        invokeOnLogNmeaMessageHandler(msg);
    }

    @Override
    public void HandleHDT( INmeaMessage msg ) {
        invokeOnLogNmeaMessageHandler(msg);
    }

    @Override
    public void HandleRMC( INmeaMessage msg ) {
        invokeOnLogNmeaMessageHandler(msg);
    }

    @Override
    public void HandleVTG( INmeaMessage msg ) {
        invokeOnLogNmeaMessageHandler(msg);
    }
}

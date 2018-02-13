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

package com.inverness_park.nmea.Sentences;

import android.content.Context;
import android.text.TextUtils;

import com.inverness_park.nmea.BaseNmeaMessage;
import com.inverness_park.nmea.INmeaMessage;
import com.inverness_park.nmea.R;

import java.util.ArrayList;
import java.util.List;

/**
 * NMEA-0183 VTG
 */

public class VTG extends BaseNmeaMessage {
    
    private String _dataTypeName ;
    private float _trueTrackMadeGoodDegrees ;
    private float _magneticTrackMadeGoodDegrees ;
    private float _groundSpeedKnots ;
    private float _groundSpeedKph ;

    public VTG() {
        _dataTypeName = "GPVTG";
        Reset();
    }

    public void Reset() {
        _trueTrackMadeGoodDegrees = 0;
        _magneticTrackMadeGoodDegrees = 0;
        _groundSpeedKnots = 0;
        _groundSpeedKph = 0;
    }

    @Override
    public String get_description( Context context ) {
        return context.getString( R.string.vtg);
    }

    @Override
    public String get_payload() {
        List<String> tokens = new ArrayList<String>() ;

        tokens.add( get_dataTypeName() ) ;
        tokens.add( String.valueOf(get_trueTrackMadeGoodDegrees()) ) ;
        tokens.add( "T" ) ;
        tokens.add( String.valueOf(get_magneticTrackMadeGoodDegrees()) ) ;
        tokens.add( "M" ) ;
        tokens.add( String.valueOf(get_groundSpeedKnots()) ) ;
        tokens.add( "N" ) ;
        tokens.add( String.valueOf(get_groundSpeedKph()) ) ;
        tokens.add( "K" ) ;

        return TextUtils.join(DELIM_FIELDS, tokens);
    }

    @Override
    public INmeaMessage parseFields( String[] tokens ) {

        set_dataTypeName( tokens[0] );
        set_trueTrackMadeGoodDegrees( Float.parseFloat(tokens[1]) );
        set_magneticTrackMadeGoodDegrees( Float.parseFloat(tokens[3]) );
        set_groundSpeedKnots( Float.parseFloat(tokens[5]) );
        set_groundSpeedKph( Float.parseFloat(tokens[7]) );

        return this;
    }

    public String get_dataTypeName() {
        return _dataTypeName;
    }

    public void set_dataTypeName( String _dataTypeName ) {
        this._dataTypeName = _dataTypeName;
    }

    public float get_trueTrackMadeGoodDegrees() {
        return _trueTrackMadeGoodDegrees;
    }

    public void set_trueTrackMadeGoodDegrees( float _trueTrackMadeGoodDegrees ) {
        this._trueTrackMadeGoodDegrees = _trueTrackMadeGoodDegrees;
    }

    public float get_magneticTrackMadeGoodDegrees() {
        return _magneticTrackMadeGoodDegrees;
    }

    public void set_magneticTrackMadeGoodDegrees( float _magneticTrackMadeGoodDegrees ) {
        this._magneticTrackMadeGoodDegrees = _magneticTrackMadeGoodDegrees;
    }

    public float get_groundSpeedKnots() {
        return _groundSpeedKnots;
    }

    public void set_groundSpeedKnots( float _groundSpeedKnots ) {
        this._groundSpeedKnots = _groundSpeedKnots;
    }

    public float get_groundSpeedKph() {
        return _groundSpeedKph;
    }

    public void set_groundSpeedKph( float _groundSpeedKph ) {
        this._groundSpeedKph = _groundSpeedKph;
    }
}

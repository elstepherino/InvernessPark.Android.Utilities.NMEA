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

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * NMEA-0183 GST
 */

public class GST extends BaseNmeaMessage {

    private String _dataTypeName;
    private LocalTime _utc ;
    private float _rms ;
    private float _smjrStdev ;
    private float _smnrStdev ;
    private float _orientation ; // degrees true north
    private float _latitudeErrorStdev ;
    private float _longitudeErrorStdev ;
    private float _altitudeErrorStdev ;

    private DateTimeFormatter _utcFormatter = DateTimeFormat.forPattern("HHmmss.SSS");

    public GST() {
        _dataTypeName = "GPGST";
        Reset();
    }

    private void Reset() {
        _utc = new LocalTime();
        _rms = 0;
        _smjrStdev = 0;
        _smnrStdev = 0;
        _orientation = 0;
        _latitudeErrorStdev = 0;
        _longitudeErrorStdev = 0;
        _altitudeErrorStdev = 0;
    }

    @Override
    public String get_description( Context context ) {
        return context.getString( R.string.gst );
    }

    @Override
    public String get_payload() {
        List<String> tokens = new ArrayList<String>() ;

        tokens.add( get_dataTypeName() ) ;
        tokens.add( get_utc().toString( _utcFormatter ) ) ;
        tokens.add( String.format( Locale.getDefault(),"%.2f", get_rms() ) ) ;
        tokens.add( String.format( Locale.getDefault(),"%.2f", get_smjrStdev() ) ) ;
        tokens.add( String.format( Locale.getDefault(),"%.2f", get_smnrStdev() ) ) ;
        tokens.add( String.format( Locale.getDefault(),"%.4f", get_orientation() ) ) ;
        tokens.add( String.format( Locale.getDefault(),"%.2f", get_latitudeErrorStdev() ) ) ;
        tokens.add( String.format( Locale.getDefault(),"%.2f", get_longitudeErrorStdev() ) ) ;
        tokens.add( String.format( Locale.getDefault(),"%.2f", get_altitudeErrorStdev() ) ) ;

        return TextUtils.join(DELIM_FIELDS, tokens);
    }

    @Override
    public INmeaMessage parseFields( String[] tokens ) {
        set_dataTypeName( tokens[0].trim().substring(1) );
        set_utc( LocalTime.parse( tokens[1], _utcFormatter ) ) ;
        set_rms( Float.parseFloat( tokens[2] )) ;
        set_smjrStdev( Float.parseFloat( tokens[3] )) ;
        set_smnrStdev( Float.parseFloat( tokens[4] )) ;
        set_orientation( Float.parseFloat( tokens[5] )) ;
        set_latitudeErrorStdev( Float.parseFloat( tokens[6] )) ;
        set_longitudeErrorStdev( Float.parseFloat( tokens[7] )) ;
        set_altitudeErrorStdev( Float.parseFloat( tokens[8] )) ;
        return this;
    }

    public String get_dataTypeName() {
        return _dataTypeName;
    }

    public void set_dataTypeName( String _dataTypeName ) {
        this._dataTypeName = _dataTypeName;
    }

    public LocalTime get_utc() {
        return _utc;
    }

    public void set_utc( LocalTime _utc ) {
        this._utc = _utc;
    }

    public float get_rms() {
        return _rms;
    }

    public void set_rms( float _rms ) {
        this._rms = _rms;
    }

    public float get_smjrStdev() {
        return _smjrStdev;
    }

    public void set_smjrStdev( float _smjrStdev ) {
        this._smjrStdev = _smjrStdev;
    }

    public float get_smnrStdev() {
        return _smnrStdev;
    }

    public void set_smnrStdev( float _smnrStdev ) {
        this._smnrStdev = _smnrStdev;
    }

    public float get_orientation() {
        return _orientation;
    }

    public void set_orientation( float _orientation ) {
        this._orientation = _orientation;
    }

    public float get_latitudeErrorStdev() {
        return _latitudeErrorStdev;
    }

    public void set_latitudeErrorStdev( float _latitudeErrorStdev ) {
        this._latitudeErrorStdev = _latitudeErrorStdev;
    }

    public float get_longitudeErrorStdev() {
        return _longitudeErrorStdev;
    }

    public void set_longitudeErrorStdev( float _longitudeErrorStdev ) {
        this._longitudeErrorStdev = _longitudeErrorStdev;
    }

    public float get_altitudeErrorStdev() {
        return _altitudeErrorStdev;
    }

    public void set_altitudeErrorStdev( float _altitudeErrorStdev ) {
        this._altitudeErrorStdev = _altitudeErrorStdev;
    }
}

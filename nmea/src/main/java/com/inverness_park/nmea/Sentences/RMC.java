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
import com.inverness_park.nmea.Types.GeoAngleFormat;
import com.inverness_park.nmea.Types.GeoAngleFormatOptions;
import com.inverness_park.nmea.Types.Latitude;
import com.inverness_park.nmea.Types.Longitude;
import com.inverness_park.nmea.Types.StatusEnum;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * NMEA-0183 RMC
 */

public class RMC extends BaseNmeaMessage {

    private String _dataTypeName ;
    private DateTime _utc;
    private StatusEnum _status ;
    private Latitude _latitude ;
    private Longitude _longitude ;
    private float _speedAboveGroundKnots ;
    private float _trackAngleTrueNorthDegrees ;
    private Float _magneticVariation ;
    private String _extraField ;

    private DateTimeFormatter _utcTimeFormatter = DateTimeFormat.forPattern("HHmmss.SSS");
    private DateTimeFormatter _utcDateFormatter = DateTimeFormat.forPattern("ddMMyy");
    private DateTimeFormatter _utcFormatter = DateTimeFormat.forPattern("ddMMyy HHmmss.SSS");

    public RMC() {
        _dataTypeName = "GPSRMC";
        Reset();
    }

    public void Reset() {
        _utc = new DateTime();
        _status = StatusEnum.Void;
        _latitude = new Latitude();
        _longitude = new Longitude();
        _speedAboveGroundKnots = 0;
        _trackAngleTrueNorthDegrees = 0;
        _magneticVariation = null;
        _extraField = null;
    }

    @Override
    public String get_description( Context context ) {
        return context.getString( R.string.rmc );
    }

    @Override
    public String get_payload() {
        List<String> tokens = new ArrayList<String>();

        tokens.add( get_dataTypeName() ) ;
        tokens.add( get_utc().toString( _utcTimeFormatter ) ) ;
        tokens.add( get_status().ToNmeaString() );
        tokens.add( get_latitude().ToString( GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact));
        tokens.add( get_longitude().ToString(GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact));
        tokens.add( String.format( Locale.getDefault(), "%.2f", get_speedAboveGroundKnots() ) );
        tokens.add( String.format( Locale.getDefault(), "%.2f", get_trackAngleTrueNorthDegrees() ) );
        tokens.add( get_utc().toString( _utcDateFormatter ) ) ;
        if ( get_magneticVariation() != null ) {
            tokens.add(String.format(Locale.getDefault(), "%.1f",  Math.abs( get_magneticVariation() ) ));
            tokens.add(get_magneticVariation() < 0 ? "W" : "E");
        } else {
            tokens.add("");
            tokens.add("");
        }
        if (get_extraField() != null ) {
            tokens.add(get_extraField());
        }
        return TextUtils.join(DELIM_FIELDS, tokens);
    }

    @Override
    public INmeaMessage parseFields( String[] tokens ) {
        set_dataTypeName( tokens[0].trim().substring(1) );
        set_utc( DateTime.parse( tokens[9] + " " + tokens[1], _utcFormatter )) ;
        set_status( StatusEnum.FromNmeaString(tokens[2]) );
        set_latitude( Latitude.Parse(tokens[3]+ DELIM_FIELDS + tokens[4], GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact) );
        set_longitude( Longitude.Parse(tokens[5]+ DELIM_FIELDS + tokens[6], GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact) );
        set_speedAboveGroundKnots( Float.parseFloat(tokens[7]) );
        set_trackAngleTrueNorthDegrees( Float.parseFloat(tokens[8]) );

        if (!tokens[10].isEmpty()) {
            set_magneticVariation( Float.parseFloat(tokens[10]) );
            if (!tokens[11].isEmpty()) {
                int sign = tokens[ 11 ].trim().toUpperCase().equals( "W" ) ? -1 : 1;
                set_magneticVariation( get_magneticVariation() * sign );
            }
        } else {
            set_magneticVariation( null );
        }
        if (tokens.length > 12) {
            set_extraField( tokens[12] );
        }
        return this;
    }

    public String get_dataTypeName() {
        return _dataTypeName;
    }

    public void set_dataTypeName( String _dataTypeName ) {
        this._dataTypeName = _dataTypeName;
    }

    public DateTime get_utc() {
        return _utc;
    }

    public void set_utc( DateTime _utc ) {
        this._utc = _utc;
    }

    public StatusEnum get_status() {
        return _status;
    }

    public void set_status( StatusEnum _status ) {
        this._status = _status;
    }

    public Latitude get_latitude() {
        return _latitude;
    }

    public void set_latitude( Latitude _latitude ) {
        this._latitude = _latitude;
    }

    public Longitude get_longitude() {
        return _longitude;
    }

    public void set_longitude( Longitude _longitude ) {
        this._longitude = _longitude;
    }

    public float get_speedAboveGroundKnots() {
        return _speedAboveGroundKnots;
    }

    public void set_speedAboveGroundKnots( float _speedAboveGroundKnots ) {
        this._speedAboveGroundKnots = _speedAboveGroundKnots;
    }

    public float get_trackAngleTrueNorthDegrees() {
        return _trackAngleTrueNorthDegrees;
    }

    public void set_trackAngleTrueNorthDegrees( float _trackAngleTrueNorthDegrees ) {
        this._trackAngleTrueNorthDegrees = _trackAngleTrueNorthDegrees;
    }

    public Float get_magneticVariation() {
        return _magneticVariation;
    }

    public void set_magneticVariation( Float _magneticVariation ) {
        this._magneticVariation = _magneticVariation;
    }

    public String get_extraField() {
        return _extraField;
    }

    public void set_extraField( String _extraField ) {
        this._extraField = _extraField;
    }
}

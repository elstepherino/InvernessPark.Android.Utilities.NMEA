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
import com.inverness_park.nmea.Types.FixQualityEnum;
import com.inverness_park.nmea.Types.GeoAngle;
import com.inverness_park.nmea.Types.GeoAngleFormat;
import com.inverness_park.nmea.Types.GeoAngleFormatOptions;
import com.inverness_park.nmea.Types.Latitude;
import com.inverness_park.nmea.Types.Longitude;

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.joda.time.TimeOfDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * NMEA-0183 GGA
 */

public class GGA extends BaseNmeaMessage {

    private String _dataTypeName;
    private LocalTime _utc ;
    private Latitude _latitude ;
    private Longitude _longitude ;
    private FixQualityEnum _fixQuality ;
    private int _satelliteCount ;
    private float _hdop ;
    private float _altitude ;
    private float _geoidHeight ;

    private DateTimeFormatter _utcFormatter = DateTimeFormat.forPattern("HHmmss.SSS");

    public GGA() {
        _dataTypeName = "GPGGA";
        Reset();
    }

    public void Reset() {
        _utc = new LocalTime();
        _latitude = new Latitude();
        _longitude = new Longitude();
        _fixQuality = FixQualityEnum.Invalid;
        _satelliteCount = 0;
        _hdop = 99;
        _altitude = 0;
        _geoidHeight = 0;
    }

    @Override
    public String get_description( Context context ) {
        return context.getString( R.string.gga ) ;
    }

    @Override
    public String get_payload() {
        List<String> tokens = new ArrayList<String>() ;

        tokens.add( get_dataTypeName() ) ;
        tokens.add( get_utc().toString( _utcFormatter ) ) ;
        tokens.add( get_latitude().ToString( GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact) ) ;
        tokens.add( get_longitude().ToString(GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact) ) ;
        tokens.add( get_fixQuality().toString() ) ;
        tokens.add( String.valueOf( get_satelliteCount() ) ) ;
        tokens.add( String.format( Locale.getDefault(), "%.2f", get_hdop() ) ) ;
        tokens.add( String.format( Locale.getDefault(), "%.1f%sM", get_altitude(), DELIM_FIELDS ) ) ;
        tokens.add( String.format( Locale.getDefault(), "%.1f%sM", get_geoidHeight(), DELIM_FIELDS ) ) ;
        tokens.add( "" ) ;
        tokens.add( "" ) ;

        return TextUtils.join(DELIM_FIELDS, tokens);
    }

    @Override
    public INmeaMessage parseFields( String[] tokens ) {
        set_dataTypeName( tokens[0].trim().substring(1) );
        set_utc( LocalTime.parse( tokens[1], _utcFormatter ) ) ;
        set_latitude( Latitude.Parse(tokens[2]+ DELIM_FIELDS + tokens[3], GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact) );
        set_longitude( Longitude.Parse(tokens[4]+ DELIM_FIELDS + tokens[5], GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact) );
        set_fixQuality( FixQualityEnum.values()[Integer.valueOf(tokens[6])] );
        set_satelliteCount( Integer.valueOf(tokens[7]));
        set_hdop( Float.valueOf(tokens[8]));
        set_altitude( Float.valueOf(tokens[9]));
        set_geoidHeight( Float.valueOf( tokens[11] ));
        return this ;
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

    public FixQualityEnum get_fixQuality() {
        return _fixQuality;
    }

    public void set_fixQuality( FixQualityEnum _fixQuality ) {
        this._fixQuality = _fixQuality;
    }

    public int get_satelliteCount() {
        return _satelliteCount;
    }

    public void set_satelliteCount( int _satelliteCount ) {
        this._satelliteCount = _satelliteCount;
    }

    public float get_hdop() {
        return _hdop;
    }

    public void set_hdop( float _hdop ) {
        this._hdop = _hdop;
    }

    public float get_altitude() {
        return _altitude;
    }

    public void set_altitude( float _altitude ) {
        this._altitude = _altitude;
    }

    public float get_geoidHeight() {
        return _geoidHeight;
    }

    public void set_geoidHeight( float _geoidHeight ) {
        this._geoidHeight = _geoidHeight;
    }
}

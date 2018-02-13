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
import com.inverness_park.nmea.Types.Fix3DEnum;
import com.inverness_park.nmea.Types.FixSelectionMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.inverness_park.nmea.Types.Fix3DEnum.Fix3D;

/**
 * NMEA-0183 GSA
 */

public class GSA extends BaseNmeaMessage {

    private String _dataTypeName ;
    private FixSelectionMode _fixSelectionMode ;
    private int _fix3D ;
    private String [] _prn;
    private float _pdop ;
    private float _hdop ;
    private float _vdop ;

    public GSA() {
        _dataTypeName = "GPGSA";
        Reset();
    }

    public void Reset() {
        _fixSelectionMode = FixSelectionMode.Auto;
        _fix3D = Fix3DEnum.NoFix;
        _prn = new String[12];
        ClearPRN();
        _pdop = 99;
        _hdop = 99;
        _vdop = 99;
    }

    private void ClearPRN() {
        for (int i = 0; i < _prn.length; ++i) {
            _prn[i] = "";
        }
    }

    @Override
    public String get_description( Context context ) {
        return context.getString( R.string.gsa);
    }

    @Override
    public String get_payload() {
        List<String> tokens = new ArrayList<String> ();

        tokens.add( get_dataTypeName() ) ;
        tokens.add( _fixSelectionMode.ToNmeaString() ) ;
        tokens.add( String.valueOf(_fix3D) ) ;
        tokens.addAll( Arrays.asList(_prn) ) ;
        tokens.add( String.format( Locale.getDefault(), "%.2f", _pdop ) ) ;
        tokens.add( String.format( Locale.getDefault(), "%.2f", _hdop ) ) ;
        tokens.add( String.format( Locale.getDefault(), "%.2f", _vdop ) ) ;

        return TextUtils.join(DELIM_FIELDS, tokens);
    }

    @Override
    public INmeaMessage parseFields( String[] tokens ) {
        set_dataTypeName( tokens[0].trim().substring(1) );
        set_fixSelectionMode( FixSelectionMode.FromNmeaString(tokens[1]) );
        set_fix3D( Integer.valueOf(tokens[2]) );
        for (int i = 0; i < 12; ++i) {
            _prn[i] = tokens[3 + i];
        }
        set_pdop( Float.valueOf(tokens[15]) );
        set_hdop( Float.valueOf(tokens[16]) );
        set_vdop( Float.valueOf(tokens[17]) );
        return this;
    }

    public String get_dataTypeName() {
        return _dataTypeName;
    }

    public void set_dataTypeName( String _dataTypeName ) {
        this._dataTypeName = _dataTypeName;
    }

    public FixSelectionMode get_fixSelectionMode() {
        return _fixSelectionMode;
    }

    public void set_fixSelectionMode( FixSelectionMode _fixSelectionMode ) {
        this._fixSelectionMode = _fixSelectionMode;
    }

    public int get_fix3D() {
        return _fix3D;
    }

    public void set_fix3D( int _fix3D ) {
        this._fix3D = _fix3D;
    }

    public String[] get_prn() {
        return _prn;
    }

    public void set_prn( String[] _prn ) {
        this._prn = _prn;
    }

    public float get_pdop() {
        return _pdop;
    }

    public void set_pdop( float _pdop ) {
        this._pdop = _pdop;
    }

    public float get_hdop() {
        return _hdop;
    }

    public void set_hdop( float _hdop ) {
        this._hdop = _hdop;
    }

    public float get_vdop() {
        return _vdop;
    }

    public void set_vdop( float _vdop ) {
        this._vdop = _vdop;
    }
}

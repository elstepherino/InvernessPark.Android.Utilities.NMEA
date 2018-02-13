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
import java.util.Locale;

/**
 * NMEA-0183 HDT
 */

public class HDT extends BaseNmeaMessage {

    private String _dataTypeName ;
    private float _headingTrue ;

    public HDT() {
        _dataTypeName = "GPHDT";
        Reset();
    }

    public void Reset() {
        _headingTrue = 0;
    }

    @Override
    public String get_description( Context context ) {
        return context.getString( R.string.hdt ) ;
    }

    @Override
    public String get_payload() {
        List<String> tokens = new ArrayList<String>();
        tokens.add(get_dataTypeName());
        tokens.add(String.format( Locale.getDefault(),"%.1f", get_headingTrue()));
        tokens.add("T");
        return TextUtils.join(DELIM_FIELDS, tokens);
    }

    @Override
    public INmeaMessage parseFields( String[] tokens ) {
        set_dataTypeName( tokens[0] );
        set_headingTrue( Float.parseFloat(tokens[1]));
        return this;
    }

    public String get_dataTypeName() {
        return _dataTypeName;
    }

    public void set_dataTypeName( String _dataTypeName ) {
        this._dataTypeName = _dataTypeName;
    }

    public float get_headingTrue() {
        return _headingTrue;
    }

    public void set_headingTrue( float _headingTrue ) {
        this._headingTrue = _headingTrue;
    }
}

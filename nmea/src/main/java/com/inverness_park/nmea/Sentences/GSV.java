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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * NMEA-0183 GSV
 */

public class GSV extends BaseNmeaMessage {

    private String _dataTypeName ;
    private int _numSentences ;
    private int _sentenceIndex ;
    private int _numSatellitesInView ;
    private Satellite[] _satelliteInfo ;

    public GSV() {
        _dataTypeName = "GSPGSV";
        _numSentences = 0;
        _sentenceIndex = 0;
        _numSatellitesInView = 0;
        _satelliteInfo = new Satellite[] {
                new Satellite(),
                new Satellite(),
                new Satellite(),
                new Satellite(),
        };
    }

    private void ClearSatelliteInfo() {
        for (int i = 0; i < _satelliteInfo.length; ++i) {
            _satelliteInfo[i].Clear();
        }
    }

    /// <summary>
    /// Placeholder for specific satellite info
    /// </summary>
    public class Satellite {
        private String _prn ;
        private Integer _elevationDegrees ; //may be empty
        private Integer _azimuthDegrees ; //may be empty
        private Integer _snr ; //may be empty

        public Satellite() {
            Clear();
        }

        public void Clear() {
            _prn = null;
            _elevationDegrees = null;
            _azimuthDegrees = null;
            _snr = null;
        }

        public String [] ToArray() {
            String[] tokens = new String[] {
                    _prn != null ? _prn : "",
                    _elevationDegrees != null ? String.format( Locale.getDefault(),"%02d", _elevationDegrees) : "",
                    _azimuthDegrees != null ? String.format(Locale.getDefault(),"%03d", _elevationDegrees) : "",
                    _snr != null ? String.valueOf(_snr):""
                };
            return tokens;
        }

        public void FromArray(String[] tokens, int index) {
            if (tokens.length - index < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }

            Clear();

            _prn = tokens[index];
            if (!tokens[index + 1].trim().isEmpty()) {
                _elevationDegrees = Integer.parseInt(tokens[index + 1]);
            }
            if (!tokens[index + 2].trim().isEmpty()) {
                _azimuthDegrees = Integer.parseInt(tokens[index + 2]);
            }
            if (!tokens[index + 3].trim().isEmpty()) {
                _snr = Integer.parseInt(tokens[index + 3]);
            }
        }
    }

    @Override
    public String get_description( Context context ) {
        return context.getString( R.string.gsv ) ;
    }

    @Override
    public String get_payload() {
        List<String> tokens = new ArrayList<String>();

        tokens.add(get_dataTypeName()) ;
        tokens.add(String.valueOf(get_numSentences()) );
        tokens.add(String.valueOf(get_sentenceIndex()) );
        tokens.add(String.valueOf(get_numSatellitesInView()) );

        for (int i = 0; i < get_satelliteInfo().length; ++i ) {
            tokens.addAll( Arrays.asList(get_satelliteInfo()[i].ToArray()));
        }

        return TextUtils.join(DELIM_FIELDS, tokens);
    }

    @Override
    public INmeaMessage parseFields( String[] tokens ) {
        set_dataTypeName( tokens[0] );
        set_numSentences( Integer.parseInt(tokens[1]));
        set_sentenceIndex( Integer.parseInt(tokens[2]));
        set_numSatellitesInView( Integer.parseInt(tokens[3]));
        for (int i = 0; i < get_satelliteInfo().length; ++i) {
            int offset = 4 + i * 4;
            if (offset < tokens.length) {
                get_satelliteInfo()[i].FromArray(tokens, offset);
            }
        }
        return this;
    }

    public String get_dataTypeName() {
        return _dataTypeName;
    }

    public void set_dataTypeName( String _dataTypeName ) {
        this._dataTypeName = _dataTypeName;
    }

    public int get_numSentences() {
        return _numSentences;
    }

    public void set_numSentences( int _numSentences ) {
        this._numSentences = _numSentences;
    }

    public int get_sentenceIndex() {
        return _sentenceIndex;
    }

    public void set_sentenceIndex( int _sentenceIndex ) {
        this._sentenceIndex = _sentenceIndex;
    }

    public int get_numSatellitesInView() {
        return _numSatellitesInView;
    }

    public void set_numSatellitesInView( int _numSatellitesInView ) {
        this._numSatellitesInView = _numSatellitesInView;
    }

    public Satellite[] get_satelliteInfo() {
        return _satelliteInfo;
    }

    public void set_satelliteInfo( Satellite[] _satelliteInfo ) {
        this._satelliteInfo = _satelliteInfo;
    }
}

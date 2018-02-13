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

package com.inverness_park.nmea.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Breaks a decimal degree value into it's DMS components
 */

public class DMSComponents {

    static final String DELIMS = "[, \'\"\u00b0]";

    private int _sign;
    private int _wholeDegrees ;
    private int _wholeMinutes ;
    private double _decimalSeconds ;

    public DMSComponents() {
        _sign = 0 ;
        _wholeDegrees = 0 ;
        _wholeMinutes = 0 ;
        _decimalSeconds = 0 ;
    }

    public DMSComponents( double degrees ) {
        // ... Compute DMM
        DMMComponents dmm = new DMMComponents( degrees ) ;
        _sign = dmm.get_sign();
        _wholeDegrees = dmm.get_wholeDegrees();
        _wholeMinutes = (int)Math.floor(dmm.get_decimalMinutes());
        double fraction = dmm.get_decimalMinutes() - _wholeMinutes;
        _decimalSeconds = fraction * 60;
    }

    public double ToDDD() {
        double fraction = _decimalSeconds / 60;
        fraction = (1.0 * _wholeMinutes + fraction) / 60;
        return _sign * (1.0 * _wholeDegrees + fraction);
    }

    public int get_sign() {
        return _sign;
    }

    public void set_sign( int _sign ) {
        this._sign = _sign;
    }

    public int get_wholeDegrees() {
        return _wholeDegrees;
    }

    public void set_wholeDegrees( int _wholeDegrees ) {
        this._wholeDegrees = _wholeDegrees;
    }

    public int get_wholeMinutes() {
        return _wholeMinutes;
    }

    public void set_wholeMinutes( int _wholeMinutes ) {
        this._wholeMinutes = _wholeMinutes;
    }

    public double get_decimalSeconds() {
        return _decimalSeconds;
    }

    public void set_decimalSeconds( double _decimalSeconds ) {
        this._decimalSeconds = _decimalSeconds;
    }

    /**
     * Returns the numeric sign for a given NMEA hemisphere character
     * @param hemisphereChar
     * @return
     */
    private static int getHemisphereSign( char hemisphereChar ) {
        int sign=0;
        switch( hemisphereChar ) {
            case 'N':case 'n':
            case 'E':case 'e':
                sign = 1 ;
                break ;
            case 'S':case 's':
            case 'W':case 'w':
                sign = -1 ;
                break ;
            default:
                throw new DMSFormatException( "hemisphereChar: " + String.valueOf( hemisphereChar ) );
        }
        return sign ;
    }

    /**
     * Parses a DMS string representation
     * @param s
     * @return
     */
    public static DMSComponents parse( String s, GeoAngleFormatOptions options ) {
        int sign=1;
        int wholeDegrees=0;
        int wholeMinutes=0;
        double decimalSeconds=0;

        if (options == GeoAngleFormatOptions.Compact) {
            // ... Split value and hemisphere letter
            String[] tokens = s.split(DELIMS);
            if (tokens.length != 2) {
                throw new DMSFormatException(s);
            }
            sign=getHemisphereSign(tokens[1].trim().charAt(0));

            double number = Double.parseDouble(tokens[0].trim());
            wholeDegrees = (int)Math.floor(number / 10000);
            double decimalMinutes = number - wholeDegrees*10000;
            wholeMinutes = (int)Math.floor(decimalMinutes / 100);
            decimalSeconds = decimalMinutes - wholeMinutes*100;
        }
        else {
            // ... Split value and hemisphere letter
            List<String> tokens = new ArrayList<>( Arrays.asList( s.split( DELIMS ) ) );
            tokens.removeAll( Arrays.asList( "", null ) );

            wholeDegrees = Integer.parseInt( tokens.get( 0 ) );
            wholeMinutes = Integer.parseInt( tokens.get( 1 ) );
            decimalSeconds = Double.parseDouble( tokens.get( 2 ) );

            switch ( tokens.size() ) {
                case 3:
                    sign = wholeDegrees < 0 ? -1 : 1;
                    wholeDegrees *= sign; // Abs
                    break;
                case 4:
                    sign = getHemisphereSign( tokens.get( 3 ).trim().charAt( 0 ) );
                    break;
                default:
                    throw new DMSFormatException( s );
            }
        }

        DMSComponents rc = new DMSComponents();
        rc.set_sign( sign );
        rc.set_wholeDegrees( wholeDegrees );
        rc.set_wholeMinutes( wholeMinutes );
        rc.set_decimalSeconds( decimalSeconds );
        return rc ;
    }
}

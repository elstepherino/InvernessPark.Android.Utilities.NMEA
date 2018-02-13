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

import java.util.EnumSet;
import java.util.Locale;

/**
 * Specialization of GeoAngle when used for Latitudes
 */

public class Latitude extends GeoAngle {

    private static final double HALF_PI = Math.PI / 2;

    /**
     * Default constructor
     */
    public Latitude() {
        super();
    }

    /**
     * Constructor
     * @param degrees
     */
    public Latitude( double degrees ) {
        super(BoundCheckedDegrees(degrees));
    }

    /**
     * Performs boudnary check
     * @param degrees
     * @return
     */
    private static double BoundCheckedDegrees( double degrees ) {
        if ( degrees < -90 || degrees > 90 ){
            throw new IllegalArgumentException();
        }
        return degrees;
    }

    /**
     * Setter adds a value range sanity check
     * @param degrees angle in degrees
     */
    @Override
    public void set_degrees( double degrees ) {
        if ( degrees < -90 || degrees > 90 ){
            throw new IllegalArgumentException();
        }
        super.set_degrees( degrees );
    }

    /**
     * Setter adds a value range sanity check
     * @param radians angle in radians
     */
    @Override
    public void set_radians( double radians ) {
        if ( radians < -HALF_PI || radians > HALF_PI ) {
            throw new IllegalArgumentException();
        }
        super.set_radians( radians );
    }

    /**
     * setter adds a value range sanity check
     * @param s
     * @param fmt
     * @param options
     * @return
     */
    public static Latitude Parse( String s, GeoAngleFormat fmt, GeoAngleFormatOptions options ) {
        Latitude rc = new Latitude();
        rc.set_degrees( ParseDegrees( s, fmt, options ) );
        return rc ;
    }

    /**
     * Implements string representation of DMM for latitudes
     * @param dmm
     * @param options
     * @return
     */
    @Override
    protected String ToStringDMM( DMMComponents dmm, GeoAngleFormatOptions options ) {
        String rc = null;
        char letter = dmm.get_sign() < 0 ? 'S' : 'N';

        if (options == GeoAngleFormatOptions.Compact) {
            rc = String.format( Locale.getDefault(), "%02d%07.4f,%c",
                    dmm.get_wholeDegrees(),
                    dmm.get_decimalMinutes(),
                    letter);
        }
        else {
            rc = String.format(Locale.getDefault(), "%d%s %.4f'",
                    dmm.get_sign() * dmm.get_wholeDegrees(),
                    DEGREE_SYMBOL,
                    dmm.get_decimalMinutes());
        }
        return rc;
    }

    /**
     * Implements string representation of DMS for latitudes
     * @param dms
     * @param options
     * @return
     */
    @Override
    protected String ToStringDMS( DMSComponents dms, GeoAngleFormatOptions options ) {
        String rc = null;
        char letter = dms.get_sign() < 0 ? 'S' : 'N';

        if (options == GeoAngleFormatOptions.Compact) {
            rc = String.format( Locale.getDefault(), "%02d%02d%07.4f,%c",
                    dms.get_wholeDegrees(),
                    dms.get_wholeMinutes(),
                    dms.get_decimalSeconds(),
                    letter);
        }
        else {
            rc = String.format( Locale.getDefault(), "%d%s %d' %.4f\" %c",
                    dms.get_wholeDegrees(),
                    DEGREE_SYMBOL,
                    dms.get_wholeMinutes(),
                    dms.get_decimalSeconds(),
                    letter);
        }
        return rc;
    }
}

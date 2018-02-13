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

import android.support.annotation.NonNull;

import java.util.EnumSet;
import java.util.Locale;

import static java.util.Locale.getDefault;

/**
 * Created by stephanm on 2018-02-11.
 */

public abstract class GeoAngle implements Comparable<GeoAngle>  {
        /// <summary>
    /// Delimiters to be used when parsing a String representation
    /// </summary>
    static final char[] DELIMS = new char[] { ',', ' ', '\'', '"', '\u00b0' };
    protected static final String DEGREE_SYMBOL = "\u00b0";
    private static final double DEG2RAD = Math.PI / 180.0 ;
    private static final double RAD2DEG = 180.0 / Math.PI ;

    /// <summary>
    /// Paceholder for the angle value in degrees
    /// </summary>
    private double _degrees;

    /// <summary>
    /// Default constructor
    /// </summary>
    public GeoAngle() {
        _degrees = 0;
    }

    /// <summary>
    /// Constructor
    /// </summary>
    /// <param name="degrees"></param>
    public GeoAngle( double degrees ) {
        _degrees = degrees;
    }

    /**
     * Get angle in degrees
     * @return angle in degrees
     */
    public double get_degrees() {
        return _degrees;
    }

    /**
     * Set angle using degrees
     * @param degrees angle in degrees
     */
    public void set_degrees( double degrees ) {
        this._degrees = degrees;
    }

    /**
     * Get angle in radians
     * @return
     */
    public double get_radians() {
        return get_degrees() * DEG2RAD;
    }

    /**
     * Set angle using radians
     * @param radians angle in radians
     */
    public void set_radians( double radians ) {
        set_degrees( radians * RAD2DEG );
    }

    /// <summary>
    /// Stringify, with format
    /// </summary>
    /// <param name="fmt"></param>
    /// <returns></returns>
    public String ToString( GeoAngleFormat fmt, GeoAngleFormatOptions options /*= GeoAngleFormatOptions.ShowUnits*/) {
        String rc = null;
        switch (fmt) {
            default:
                throw new IllegalArgumentException("Unsupported format: " + fmt);

            case DDD:
                if ( options == GeoAngleFormatOptions.Compact ) {
                    rc = String.format( getDefault(), "%.7f", get_degrees() );
                } else {
                    rc = String.format( getDefault(), "%.7f%s", get_degrees(), DEGREE_SYMBOL );
                }
                break;

            case DMM: {
                DMMComponents dmmComponents = new DMMComponents( get_degrees() ) ;
                rc = ToStringDMM(dmmComponents, options);
            }
            break;

            case DMS: {
                DMSComponents dmsComponents = new DMSComponents(get_degrees());
                rc = ToStringDMS(dmsComponents, options);
            }
            break;
        }
        return rc;
    }

    /// <summary>
    /// Converts a String representation back to numeric degrees
    /// </summary>
    /// <param name="s"></param>
    /// <param name="fmt"></param>
    /// <param name="options"></param>
    /// <returns></returns>
    protected static double ParseDegrees(String s, GeoAngleFormat fmt, GeoAngleFormatOptions options) {
        double rc = 0;

        switch (fmt) {
            case DDD:
                rc = Double.parseDouble(s);
                break;
            case DMM:
                rc = ParseDMM(s, options);
                break;
            case DMS:
                rc = ParseDMS(s, options);
                break;
        }

        return rc;
    }

    /// <summary>
    /// Parses a DMM representation and converts to degrees
    /// </summary>
    /// <param name="s"></param>
    /// <param name="options"></param>
    /// <returns></returns>
    protected static double ParseDMM(String s, GeoAngleFormatOptions options) {
        DMMComponents dmm = DMMComponents.parse( s, options ) ;
        return dmm.ToDDD() ;
    }

    /// <summary>
    /// Parses a DMS representation and converts to degrees
    /// </summary>
    /// <param name="s"></param>
    /// <param name="options"></param>
    /// <returns></returns>
    protected static double ParseDMS(String s, GeoAngleFormatOptions options) {
        DMSComponents dms = DMSComponents.parse( s, options );
        return dms.ToDDD();
    }

    /// <summary>
    /// Converts from DMM components to its String representation
    /// </summary>
    /// <param name="wholeDegrees"></param>
    /// <param name="decimalMinutes"></param>
    /// <param name="sign"></param>
    /// <param name="options"></param>
    /// <returns></returns>
    protected abstract String ToStringDMM(DMMComponents dmm, GeoAngleFormatOptions options );

    /// <summary>
    /// Converts from DMS components to its String representation
    /// </summary>
    /// <param name="wholeDegrees"></param>
    /// <param name="wholeMinutes"></param>
    /// <param name="decimalSeconds"></param>
    /// <param name="sign"></param>
    /// <param name="options"></param>
    /// <returns></returns>
    protected abstract String ToStringDMS(DMSComponents dms, GeoAngleFormatOptions options );

    /**
     * Comparable<GeoAngle>
     * @param geoAngle
     * @return
     */
    @Override
    public int compareTo( @NonNull GeoAngle geoAngle ) {
        return Double.compare( _degrees, geoAngle._degrees );
    }
}

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

package com.inverness_park.nmea;

import com.inverness_park.nmea.Types.DMMComponents;
import com.inverness_park.nmea.Types.DMMFormatException;
import com.inverness_park.nmea.Types.GeoAngleFormatOptions;

import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by stephanm on 2018-02-12.
 */

public class DMMComponentsUnitsTest {
    private Random _rnd = new Random(new Date().getTime());

    @Test
    public void DefaultConstructor() {
        DMMComponents dmm = new DMMComponents();
        assertEquals( dmm.get_sign(), 0 ) ;
        assertEquals( dmm.get_wholeDegrees(), 0 ) ;
        assertTrue( "Decimal minutes default init", Math.abs( dmm.get_decimalMinutes() ) < 0.000001 ) ;
    }

    @Test
    public void ConstructorWithPositiveParameter() {
        double degrees = _rnd.nextDouble()*180;

        DMMComponents dmm = new DMMComponents(degrees);

        DMMComponents dmm2 = new DMMComponents() ;
        dmm2.set_sign( dmm.get_sign() );
        dmm2.set_wholeDegrees( dmm.get_wholeDegrees() );
        dmm2.set_decimalMinutes( dmm.get_decimalMinutes() );

        assertTrue( "Converting to and from should be consistent", Math.abs( dmm2.ToDDD() - degrees) < 0.000001 ) ;
    }

    @Test
    public void ConstructorWithNegaitiveParameter() {
        double degrees = -_rnd.nextDouble()*180;

        DMMComponents dmm = new DMMComponents(degrees);

        DMMComponents dmm2 = new DMMComponents() ;
        dmm2.set_sign( dmm.get_sign() );
        dmm2.set_wholeDegrees( dmm.get_wholeDegrees() );
        dmm2.set_decimalMinutes( dmm.get_decimalMinutes() );

        assertTrue( "Converting to and from should be consistent", Math.abs( dmm2.ToDDD() - degrees) < 0.000001 ) ;
    }

    @Test
    public void ParseFromValidCompactString() {
        Map<String,DMMComponents> testCases = new TreeMap<>();
        testCases.put( "6113.083336,N", new DMMComponents( 61.21805560 ) );
        testCases.put( "3134.310112,E", new DMMComponents( 31.5718352 ) );
        testCases.put( "5708.849490,n", new DMMComponents( 57.1474915 ) );
        testCases.put( "5224.918180,e", new DMMComponents( 52.41530300 ) );
        testCases.put( "14954.016668,W", new DMMComponents( -149.90027780 ) );
        testCases.put( "8515.029358,S", new DMMComponents(  -85.2504893 ) );
        testCases.put( "205.723820,w", new DMMComponents( -2.095397 ) );
        testCases.put( "404.975200,s", new DMMComponents( -4.08292000 ) );

        for ( Map.Entry<String,DMMComponents> kv : testCases.entrySet() ) {
            DMMComponents dmm = DMMComponents.parse( kv.getKey(), GeoAngleFormatOptions.Compact ) ;
            assertEquals( dmm.get_sign(), kv.getValue().get_sign() ) ;
            assertEquals( dmm.get_wholeDegrees(), kv.getValue().get_wholeDegrees() ) ;
            assertTrue( "Decimal minutes equality", Math.abs( dmm.get_decimalMinutes() - kv.getValue().get_decimalMinutes() ) < 0.000001 ) ;
        }
    }

    @Test
    public void ParseFromValidFormattedString() {
        Map<String,DMMComponents> testCases = new TreeMap<>();
        testCases.put( "61° 13.083336'", new DMMComponents( 61.21805560 ) );
        testCases.put( "31° 34.310112'", new DMMComponents( 31.5718352 ) );
        testCases.put( "57° 8.849490'", new DMMComponents( 57.1474915 ) );
        testCases.put( "52° 24.918180'", new DMMComponents( 52.41530300 ) );
        testCases.put( "-149° 54.016668'", new DMMComponents( -149.90027780 ) );
        testCases.put( "-85° 15.029358'", new DMMComponents(  -85.2504893 ) );
        testCases.put( "-2° 5.723820'", new DMMComponents( -2.095397 ) );
        testCases.put( "-4° 4.975200'", new DMMComponents( -4.08292000 ) );

        for ( Map.Entry<String,DMMComponents> kv : testCases.entrySet() ) {
            DMMComponents dmm = DMMComponents.parse( kv.getKey(), GeoAngleFormatOptions.ShowUnits ) ;
            assertEquals( dmm.get_sign(), kv.getValue().get_sign() ) ;
            assertEquals( dmm.get_wholeDegrees(), kv.getValue().get_wholeDegrees() ) ;
            assertTrue( "Decimal minutes equality", Math.abs( dmm.get_decimalMinutes() - kv.getValue().get_decimalMinutes() ) < 0.000001 ) ;
        }
    }

    @Test (expected = DMMFormatException.class)
    public void ParseFromInvalidCompactString() {
        DMMComponents.parse( "61x13.083336;N", GeoAngleFormatOptions.Compact ) ;
    }

    @Test (expected = DMMFormatException.class)
    public void ParseFromInvalidFormattedString() {
        DMMComponents.parse( "61° 61° 13;083336'", GeoAngleFormatOptions.ShowUnits ) ;
    }
}

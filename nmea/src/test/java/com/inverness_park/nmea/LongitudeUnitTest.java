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

import com.inverness_park.nmea.Types.GeoAngle;
import com.inverness_park.nmea.Types.GeoAngleFormat;
import com.inverness_park.nmea.Types.GeoAngleFormatOptions;
import com.inverness_park.nmea.Types.Longitude;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertTrue;

/**
 * Created by stephanm on 2018-02-12.
 */

public class LongitudeUnitTest {

    @Test (expected=IllegalArgumentException.class)
    public void ConstructorOutOfBoundsDegreesPositive() {
        Longitude lat = new Longitude(360) ;
    }

    @Test (expected=IllegalArgumentException.class)
    public void ConstructorOutOfBoundsDegreesNegative() {
        Longitude lat = new Longitude(-360) ;
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsDegreesPositive() {
        Longitude lat = new Longitude() ;
        lat.set_degrees( 360 );
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsDegreesNegative() {
        Longitude lat = new Longitude() ;
        lat.set_degrees( -360 );
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsRadiansPositive() {
        Longitude lat = new Longitude() ;
        lat.set_radians( 2 * Math.PI );
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsRadiansNegative() {
        Longitude lat = new Longitude() ;
        lat.set_radians( -2 * Math.PI );
    }

    @Test
    public void SerializationToCompactDDD() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Longitude(-149.90027780), "-149.9002778" ) ;
        testCases.put( new Longitude(85.2504893), "85.2504893" ) ;
        testCases.put( new Longitude(2.095397), "2.0953970" ) ;
        testCases.put( new Longitude(-4.08292000), "-4.0829200" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DDD, GeoAngleFormatOptions.Compact );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToFormattedDDD() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Longitude(-149.90027780), "-149.9002778°" ) ;
        testCases.put( new Longitude(85.2504893), "85.2504893°" ) ;
        testCases.put( new Longitude(2.095397), "2.0953970°" ) ;
        testCases.put( new Longitude(-4.08292000), "-4.0829200°" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DDD, GeoAngleFormatOptions.ShowUnits );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToCompactDMM() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Longitude(-149.90027780), "14954.0167,W" ) ;
        testCases.put( new Longitude(85.2504893), "08515.0294,E" ) ;
        testCases.put( new Longitude(2.095397), "00205.7238,E" ) ;
        testCases.put( new Longitude(-4.08292000), "00404.9752,W" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToFormattedDMM() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Longitude(-149.90027780), "-149° 54.0167'" ) ;
        testCases.put( new Longitude(85.2504893), "85° 15.0294'" ) ;
        testCases.put( new Longitude(2.095397), "2° 5.7238'" ) ;
        testCases.put( new Longitude(-4.08292000), "-4° 4.9752'" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMM, GeoAngleFormatOptions.ShowUnits );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToCompactDMS() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Longitude(-149.90027780), "1495401.0001,W" ) ;
        testCases.put( new Longitude(85.2504893), "0851501.7615,E" ) ;
        testCases.put( new Longitude(2.095397), "0020543.4292,E" ) ;
        testCases.put( new Longitude(-4.08292000), "0040458.5120,W" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMS, GeoAngleFormatOptions.Compact );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToFormattedDMS() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Longitude(-149.90027780), "149° 54' 1.0001\" W" ) ;
        testCases.put( new Longitude(85.2504893), "85° 15' 1.7615\" E" ) ;
        testCases.put( new Longitude(2.095397), "2° 5' 43.4292\" E" ) ;
        testCases.put( new Longitude(-4.08292000), "4° 4' 58.5120\" W" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMS, GeoAngleFormatOptions.ShowUnits );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }
}

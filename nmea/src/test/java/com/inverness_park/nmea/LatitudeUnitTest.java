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
import com.inverness_park.nmea.Types.Latitude;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertTrue;

/**
 * Created by stephanm on 2018-02-12.
 */

public class LatitudeUnitTest {

    @Test (expected=IllegalArgumentException.class)
    public void ConstructorOutOfBoundsDegreesPositive() {
        Latitude lat = new Latitude(360) ;
    }

    @Test (expected=IllegalArgumentException.class)
    public void ConstructorOutOfBoundsDegreesNegative() {
        Latitude lat = new Latitude(-360) ;
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsDegreesPositive() {
        Latitude lat = new Latitude() ;
        lat.set_degrees( 360 );
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsDegreesNegative() {
        Latitude lat = new Latitude() ;
        lat.set_degrees( -360 );
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsRadiansPositive() {
        Latitude lat = new Latitude() ;
        lat.set_radians( 2 * Math.PI );
    }

    @Test (expected=IllegalArgumentException.class)
    public void SetterOutOfBoundsRadiansNegative() {
        Latitude lat = new Latitude() ;
        lat.set_radians( -2 * Math.PI );
    }

    @Test
    public void SerializationToCompactDDD() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Latitude( 61.2180556), "61.2180556" ) ;
        testCases.put( new Latitude(-31.5718352), "-31.5718352" ) ;
        testCases.put( new Latitude(-57.1474915), "-57.1474915" ) ;
        testCases.put( new Latitude( 52.4153030), "52.4153030" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DDD, GeoAngleFormatOptions.Compact );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToFormattedDDD() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Latitude(61.2180556), "61.2180556°" ) ;
        testCases.put( new Latitude(-31.5718352), "-31.5718352°" ) ;
        testCases.put( new Latitude(-57.1474915), "-57.1474915°" ) ;
        testCases.put( new Latitude(52.4153030), "52.4153030°" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DDD, GeoAngleFormatOptions.ShowUnits );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToCompactDMM() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Latitude(61.21805560), "6113.0833,N" ) ;
        testCases.put( new Latitude(-31.5718352), "3134.3101,S" ) ;
        testCases.put( new Latitude(-57.1474915), "5708.8495,S" ) ;
        testCases.put( new Latitude(52.41530300), "5224.9182,N" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMM, GeoAngleFormatOptions.Compact );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToFormattedDMM() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Latitude(61.21805560), "61° 13.0833'" ) ;
        testCases.put( new Latitude(-31.5718352), "-31° 34.3101'" ) ;
        testCases.put( new Latitude(-57.1474915), "-57° 8.8495'" ) ;
        testCases.put( new Latitude(52.41530300), "52° 24.9182'" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMM, GeoAngleFormatOptions.ShowUnits );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToCompactDMS() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Latitude(61.21805560), "611305.0002,N" ) ;
        testCases.put( new Latitude(-31.5718352), "313418.6067,S" ) ;
        testCases.put( new Latitude(-57.1474915), "570850.9694,S" ) ;
        testCases.put( new Latitude(52.41530300), "522455.0908,N" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMS, GeoAngleFormatOptions.Compact );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }

    @Test
    public void SerializationToFormattedDMS() {
        Map<GeoAngle,String> testCases = new TreeMap<>() ;

        testCases.put( new Latitude(61.21805560), "61° 13' 5.0002\" N" ) ;
        testCases.put( new Latitude(-31.5718352), "31° 34' 18.6067\" S" ) ;
        testCases.put( new Latitude(-57.1474915), "57° 8' 50.9694\" S" ) ;
        testCases.put( new Latitude(52.41530300), "52° 24' 55.0908\" N" ) ;

        for ( Map.Entry<GeoAngle,String> kv : testCases.entrySet() ) {
            String serialized = kv.getKey().ToString( GeoAngleFormat.DMS, GeoAngleFormatOptions.ShowUnits );
            String msg = String.format("%s != %s", kv.getValue(), serialized);
            assertTrue( msg, kv.getValue().equalsIgnoreCase( serialized ) );
        }
    }
}

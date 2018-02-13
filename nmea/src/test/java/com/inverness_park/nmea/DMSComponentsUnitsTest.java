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

import com.inverness_park.nmea.Types.DMSComponents;
import com.inverness_park.nmea.Types.DMSFormatException;
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

public class DMSComponentsUnitsTest {
    private Random _rnd = new Random(new Date().getTime());

    @Test
    public void DefaultConstructor() {
        DMSComponents dms = new DMSComponents();
        assertEquals( dms.get_sign(), 0 ) ;
        assertEquals( dms.get_wholeDegrees(), 0 ) ;
        assertEquals( dms.get_wholeMinutes(), 0 ) ;
        assertTrue( "Decimal seconds default init", Math.abs( dms.get_decimalSeconds() ) < 0.000001 ) ;
    }

    @Test
    public void ConstructorWithPositiveParameter() {
        double degrees = _rnd.nextDouble()*180;

        DMSComponents dms = new DMSComponents(degrees);

        DMSComponents dms2 = new DMSComponents() ;
        dms2.set_sign( dms.get_sign() );
        dms2.set_wholeDegrees( dms.get_wholeDegrees() );
        dms2.set_wholeMinutes( dms.get_wholeMinutes() );
        dms2.set_decimalSeconds( dms.get_decimalSeconds() );

        assertTrue( String.format("ToDDD(%f) != %f", dms2.ToDDD(), degrees),
                Math.abs( dms2.ToDDD() - degrees) < 0.000001 ) ;
    }

    @Test
    public void ConstructorWithNegaitiveParameter() {
        double degrees = -_rnd.nextDouble()*180;

        DMSComponents dms = new DMSComponents(degrees);

        DMSComponents dms2 = new DMSComponents() ;
        dms2.set_sign( dms.get_sign() );
        dms2.set_wholeDegrees( dms.get_wholeDegrees() );
        dms2.set_wholeMinutes( dms.get_wholeMinutes() );
        dms2.set_decimalSeconds( dms.get_decimalSeconds() );

        assertTrue( String.format("ToDDD(%f) != %f", dms2.ToDDD(), degrees),
                Math.abs( dms2.ToDDD() - degrees) < 0.000001 ) ;
    }

    @Test
    public void ParseFromValidCompactString() {
        Map<String,DMSComponents> testCases = new TreeMap<>();
        testCases.put( "611305.0002,N", new DMSComponents( 61.21805560 ) );
        testCases.put( "313418.6067,n", new DMSComponents( 31.5718352 ) );
        testCases.put( "570850.9694,N", new DMSComponents( 57.1474915 ) );
        testCases.put( "522455.0908,n", new DMSComponents( 52.41530300 ) );
        testCases.put( "1495401.0001,W", new DMSComponents( -149.90027780 ) );
        testCases.put( "0851501.7615,w", new DMSComponents(  -85.2504893 ) );
        testCases.put( "0020543.4292,W", new DMSComponents( -2.095397 ) );
        testCases.put( "0040458.5120,w", new DMSComponents( -4.08292000 ) );

        for ( Map.Entry<String,DMSComponents> kv : testCases.entrySet() ) {
            DMSComponents dms = DMSComponents.parse( kv.getKey(), GeoAngleFormatOptions.Compact ) ;
            assertEquals( dms.get_sign(), kv.getValue().get_sign() ) ;
            assertEquals( dms.get_wholeDegrees(), kv.getValue().get_wholeDegrees() ) ;
            assertEquals( dms.get_wholeMinutes(), kv.getValue().get_wholeMinutes() ) ;
            String msg = String.format("Parsed: %s: %f != %f (Expected)",
                    kv.getKey(),
                    dms.get_decimalSeconds(),
                    kv.getValue().get_decimalSeconds()) ;
            assertTrue( msg, Math.abs( dms.get_decimalSeconds() - kv.getValue().get_decimalSeconds() ) < 0.0001 ) ;
        }
    }


    @Test
    public void ParseFromValidFormattedString() {
        Map<String,DMSComponents> testCases = new TreeMap<>();
        testCases.put( "61° 13' 5.0002\" N", new DMSComponents( 61.21805560 ) );
        testCases.put( "31° 34' 18.6067\" N", new DMSComponents( 31.5718352 ) );
        testCases.put( "57° 8' 50.9694\" N", new DMSComponents( 57.1474915 ) );
        testCases.put( "52° 24' 55.0908\" N", new DMSComponents( 52.41530300 ) );
        testCases.put( "149° 54' 1.0001\" W", new DMSComponents( -149.90027780 ) );
        testCases.put( "85° 15' 1.7615\" W", new DMSComponents(  -85.2504893 ) );
        testCases.put( "2° 5' 43.4292\" W", new DMSComponents( -2.095397 ) );
        testCases.put( "4° 4' 58.5120\" W", new DMSComponents( -4.08292000 ) );

        for ( Map.Entry<String,DMSComponents> kv : testCases.entrySet() ) {
            DMSComponents dms = DMSComponents.parse( kv.getKey(), GeoAngleFormatOptions.ShowUnits ) ;
            assertEquals( dms.get_sign(), kv.getValue().get_sign() ) ;
            assertEquals( dms.get_wholeDegrees(), kv.getValue().get_wholeDegrees() ) ;
            assertEquals( dms.get_wholeMinutes(), kv.getValue().get_wholeMinutes() ) ;
            String msg = String.format("Parsed: %s: %f != %f (Expected)",
                    kv.getKey(),
                    dms.get_decimalSeconds(),
                    kv.getValue().get_decimalSeconds()) ;
            assertTrue( msg, Math.abs( dms.get_decimalSeconds() - kv.getValue().get_decimalSeconds() ) < 0.0001 ) ;
        }
    }

    @Test (expected = DMSFormatException.class)
    public void ParseFromInvalidCompactString() {
        DMSComponents.parse( "611c305.0002;N", GeoAngleFormatOptions.Compact ) ;
    }

    @Test (expected = DMSFormatException.class)
    public void ParseFromInvalidFormattedString() {
        DMSComponents.parse( "61° 13' 13' 5.0002\" N", GeoAngleFormatOptions.ShowUnits ) ;
    }
}

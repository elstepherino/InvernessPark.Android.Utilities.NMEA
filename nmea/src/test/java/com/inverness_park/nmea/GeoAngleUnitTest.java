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
import com.inverness_park.nmea.Types.DMSComponents;
import com.inverness_park.nmea.Types.GeoAngle;
import com.inverness_park.nmea.Types.GeoAngleFormatOptions;

import org.junit.Test;

import java.util.Date;
import java.util.Random;

import static junit.framework.Assert.assertTrue;

/**
 * Created by stephanm on 2018-02-12.
 */

public class GeoAngleUnitTest {

    /**
     * To test the abstract base class, we need a concreate class
     */
    class GeoAngleStub extends GeoAngle {
        public GeoAngleStub() {
        }

        public GeoAngleStub( double degrees ) {
            super( degrees );
        }

        @Override
        protected String ToStringDMM( DMMComponents dmm, GeoAngleFormatOptions options ) {
            return null;
        }

        @Override
        protected String ToStringDMS( DMSComponents dms, GeoAngleFormatOptions options ) {
            return null;
        }
    }

    Random _rnd = new Random(new Date().getTime() );

    @Test
    public void DefaultConstructor() {
        GeoAngle stub = new GeoAngleStub() ;
        assertTrue( Math.abs( stub.get_degrees() ) < 0.000001 );
    }

    @Test
    public void Constructor() {
        double degrees = _rnd.nextDouble() * 360 ;
        GeoAngle stub = new GeoAngleStub(degrees ) ;
        assertTrue( Math.abs( stub.get_degrees() - degrees ) < 0.000001 );
    }

    @Test
    public void DegreesToRadians() {
        double degrees = _rnd.nextDouble() * 360 ;
        GeoAngle stub = new GeoAngleStub() ;
        stub.set_degrees( degrees );

        double radians = stub.get_radians() ;
        double expected = degrees * Math.PI / 180.0 ;

        assertTrue( Math.abs( radians - expected ) < 0.000001 );
    }

    @Test
    public void RadiansToDegrees() {
        double radians = _rnd.nextDouble() * Math.PI * 2 ;
        GeoAngle stub = new GeoAngleStub() ;
        stub.set_radians( radians );

        double degrees = stub.get_degrees() ;
        double expected = radians * 180.0 / Math.PI;

        assertTrue( Math.abs( degrees - expected ) < 0.000001 );
    }
}

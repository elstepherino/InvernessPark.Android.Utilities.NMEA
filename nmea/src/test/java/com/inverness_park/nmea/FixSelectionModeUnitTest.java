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

import com.inverness_park.nmea.Types.FixSelectionMode;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by stephanm on 2018-02-12.
 */

public class FixSelectionModeUnitTest {
    @Test
    public void ToNmeaString_Auto_IsCorrect() {
        assertEquals( FixSelectionMode.Auto.ToNmeaString(), "A" ) ;
    }
    @Test
    public void ToNmeaString_Manual_IsCorrect() {
        assertEquals( FixSelectionMode.Manual.ToNmeaString(), "M" ) ;
    }
    @Test
    public void FromNmeaString_Auto_IsCorrect() {
        assertEquals( FixSelectionMode.FromNmeaString("a"), FixSelectionMode.Auto ) ;
        assertEquals( FixSelectionMode.FromNmeaString("A"), FixSelectionMode.Auto ) ;
    }
    @Test
    public void FromNmeaString_Manual_IsCorrect() {
        assertEquals( FixSelectionMode.FromNmeaString("m"), FixSelectionMode.Manual ) ;
        assertEquals( FixSelectionMode.FromNmeaString("M"), FixSelectionMode.Manual ) ;
    }
    @Test (expected=IllegalArgumentException.class)
    public void FromNmeaString_Auto_IsIncorrect() {
        FixSelectionMode.FromNmeaString("x");
    }
    @Test (expected=IllegalArgumentException.class)
    public void FromNmeaString_Manual_IsIncorrect() {
        FixSelectionMode.FromNmeaString("x");
    }
}

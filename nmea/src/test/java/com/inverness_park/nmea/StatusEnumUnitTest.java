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

import com.inverness_park.nmea.Types.StatusEnum;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by stephanm on 2018-02-12.
 */

public class StatusEnumUnitTest {
    @Test
    public void ToNmeaString_Void_IsCorrect() {
        assertEquals( StatusEnum.Void.ToNmeaString(), "V" ) ;
    }
    @Test
    public void ToNmeaString_Active_IsCorrect() {
        assertEquals( StatusEnum.Active.ToNmeaString(), "A" ) ;
    }
    @Test
    public void FromNmeaString_Void_IsCorrect() {
        assertEquals( StatusEnum.FromNmeaString("V"), StatusEnum.Void ) ;
        assertEquals( StatusEnum.FromNmeaString("v"), StatusEnum.Void ) ;
    }
    @Test
    public void FromNmeaString_Active_IsCorrect() {
        assertEquals( StatusEnum.FromNmeaString("A"), StatusEnum.Active ) ;
        assertEquals( StatusEnum.FromNmeaString("a"), StatusEnum.Active ) ;
    }
    @Test (expected=IllegalArgumentException.class)
    public void FromNmeaString_Void_IsIncorrect() {
        StatusEnum.FromNmeaString("x");
    }
    @Test (expected=IllegalArgumentException.class)
    public void FromNmeaString_Active_IsIncorrect() {
        StatusEnum.FromNmeaString("x");
    }
}

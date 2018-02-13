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

import android.content.Context;

/**
 * ﻿NMEA message contract
 */

public interface INmeaMessage {
    /**
     * ﻿As per NMEA 0183's documentation
     * @return Human readable description
     */
    String get_description( Context context ) ;

    /**
     * ﻿NMEA sentence without the leading "$" and the trailing "*" & checksum
     * @return NMEA sentence without start/end/checksum delimiters
     */
    String get_payload();

    /**
     * NMEA-0183 NMEA checksum
     * @return NMEA checkksum value
     */
    byte get_checksum() ;

    /**
     * ﻿Loads NMEA fields that have been parsed into string tokens
     * @param tokens
     * @return this
     */
    INmeaMessage parseFields(String[] tokens);
}

/* Copyright 2002-2019 CS Systèmes d'Information
 * Licensed to CS Systèmes d'Information (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.orekit.Utils;
import org.orekit.errors.OrekitException;
import org.orekit.errors.OrekitMessages;
import org.orekit.gnss.SatelliteSystem;

public class GNSSDateTest {

    @Test
    public void testFromWeekAndMilliGPS() {
        final DateComponents date = new DateComponents(2006, 8, 9);
        final TimeComponents time = new TimeComponents(16, 31, 03);
        doTestFromWeekAndMilli(SatelliteSystem.GPS, date, time, 1387, 318677000.0);
    }

    @Test
    public void testFromWeekAndMilliGalileo() {
        final DateComponents date = new DateComponents(1999, 12, 25);
        final TimeComponents time = new TimeComponents(12, 00, 00);
        doTestFromWeekAndMilli(SatelliteSystem.GALILEO, date, time, 17, 561600000.0);
    }

    @Test
    public void testFromWeekAndMilliQZSS() {
        final DateComponents date = new DateComponents(2006, 8, 9);
        final TimeComponents time = new TimeComponents(16, 31, 03);
        doTestFromWeekAndMilli(SatelliteSystem.QZSS, date, time, 1387, 318677000.0);
    }

    @Test
    public void testFromWeekAndMilliBeidou() {
        final DateComponents date = new DateComponents(2010, 2, 26);
        final TimeComponents time = new TimeComponents(23, 15, 12);
        doTestFromWeekAndMilli(SatelliteSystem.BEIDOU, date, time, 216, 515713000.0);
    }

    private void doTestFromWeekAndMilli(final SatelliteSystem system,
                                        final DateComponents date, final TimeComponents time,
                                        final int refWeek, final double refMilliSeconds) {
        GNSSDate GNSSDate  = new GNSSDate(refWeek, refMilliSeconds, system);
        AbsoluteDate ref  = new AbsoluteDate(date, time, utc);
        Assert.assertEquals(refWeek, GNSSDate.getWeekNumber());
        Assert.assertEquals(refMilliSeconds, GNSSDate.getMilliInWeek(), 1.0e-15);
        Assert.assertEquals(0, GNSSDate.getDate().durationFrom(ref), 1.0e-15);
    }

    @Test
    public void testFromAbsoluteDateGPS() {
        final DateComponents date = new DateComponents(2006, 8, 9);
        final TimeComponents time = new TimeComponents(16, 31, 03);
        doTestFromAbsoluteDate(SatelliteSystem.GPS, date, time, 1387, 318677000.0);
    }

    @Test
    public void testFromAbsoluteDateGalileo() {
        final DateComponents date = new DateComponents(1999, 12, 25);
        final TimeComponents time = new TimeComponents(12, 00, 00);
        doTestFromAbsoluteDate(SatelliteSystem.GALILEO, date, time, 17, 561600000.0);
    }

    @Test
    public void testFromAbsoluteDateQZSS() {
        final DateComponents date = new DateComponents(2006, 8, 9);
        final TimeComponents time = new TimeComponents(16, 31, 03);
        doTestFromAbsoluteDate(SatelliteSystem.QZSS, date, time, 1387, 318677000.0);
    }

    @Test
    public void testFromAbsoluteDateBeidou() {
        final DateComponents date = new DateComponents(2010, 2, 26);
        final TimeComponents time = new TimeComponents(23, 15, 12);
        doTestFromAbsoluteDate(SatelliteSystem.BEIDOU, date, time, 216, 515713000.0);
    }

    private void doTestFromAbsoluteDate(final SatelliteSystem system,
                                        final DateComponents date, final TimeComponents time,
                                        final int refWeek, final double refMilliSeconds) {
        GNSSDate GNSSDate = new GNSSDate(new AbsoluteDate(date, time, utc), system);
        Assert.assertEquals(refWeek, GNSSDate.getWeekNumber());
        Assert.assertEquals(refMilliSeconds, GNSSDate.getMilliInWeek(), 1.0e-15);
    }

    @Test
    public void testZeroGPS() {
        doTestZero(SatelliteSystem.GPS);
    }

    @Test
    public void testZeroGalileo() {
        doTestZero(SatelliteSystem.GALILEO);
    }

    @Test
    public void testZeroQZSS() {
        doTestZero(SatelliteSystem.QZSS);
    }

    @Test
    public void testZeroBeidou() {
        doTestZero(SatelliteSystem.BEIDOU);
    }

    private void doTestZero(final SatelliteSystem system) {
        AbsoluteDate epoch = null;
        switch (system) {
            case GPS:
                epoch = AbsoluteDate.GPS_EPOCH;
                break;
            case GALILEO:
                epoch = AbsoluteDate.GALILEO_EPOCH;
                break;
            case QZSS:
                epoch = AbsoluteDate.QZSS_EPOCH;
                break;
            case BEIDOU:
                epoch = AbsoluteDate.BEIDOU_EPOCH;
                break;
            default:
                break;
        }
        GNSSDate date = new GNSSDate(epoch, system);
        Assert.assertEquals(0, date.getWeekNumber());
        Assert.assertEquals(0.0, date.getMilliInWeek(), 1.0e-15);
    }

    @Test
    public void testZeroZeroGPS() {
        GNSSDate.setRolloverReference(new DateComponents(DateComponents.GPS_EPOCH, 7 * 512));
        GNSSDate date1 = new GNSSDate(0, 0.0, SatelliteSystem.GPS);
        Assert.assertEquals(0.0, date1.getDate().durationFrom(AbsoluteDate.GPS_EPOCH), 1.0e-15);
        GNSSDate.setRolloverReference(new DateComponents(GNSSDate.getRolloverReference(), 1));
        GNSSDate date2 = new GNSSDate(0, 0.0, SatelliteSystem.GPS);
        Assert.assertEquals(1024, date2.getWeekNumber());
    }

    @Test
    public void testZeroZeroGalileo() {
        GNSSDate.setRolloverReference(new DateComponents(DateComponents.GALILEO_EPOCH, 7 * 2048));
        GNSSDate date1 = new GNSSDate(0, 0.0, SatelliteSystem.GALILEO);
        Assert.assertEquals(0.0, date1.getDate().durationFrom(AbsoluteDate.GALILEO_EPOCH), 1.0e-15);
        GNSSDate.setRolloverReference(new DateComponents(GNSSDate.getRolloverReference(), 1));
        GNSSDate date2 = new GNSSDate(0, 0.0, SatelliteSystem.GALILEO);
        Assert.assertEquals(4096, date2.getWeekNumber());
    }

    @Test
    public void testZeroZeroQZSS() {
        GNSSDate.setRolloverReference(new DateComponents(DateComponents.QZSS_EPOCH, 7 * 512));
        GNSSDate date1 = new GNSSDate(0, 0.0, SatelliteSystem.QZSS);
        Assert.assertEquals(0.0, date1.getDate().durationFrom(AbsoluteDate.QZSS_EPOCH), 1.0e-15);
        GNSSDate.setRolloverReference(new DateComponents(GNSSDate.getRolloverReference(), 1));
        GNSSDate date2 = new GNSSDate(0, 0.0, SatelliteSystem.QZSS);
        Assert.assertEquals(1024, date2.getWeekNumber());
    }

    @Test
    public void testZeroZeroBeidou() {
        GNSSDate.setRolloverReference(new DateComponents(DateComponents.BEIDOU_EPOCH, 7 * 4096));
        GNSSDate date1 = new GNSSDate(0, 0.0, SatelliteSystem.BEIDOU);
        Assert.assertEquals(0.0, date1.getDate().durationFrom(AbsoluteDate.BEIDOU_EPOCH), 1.0e-15);
        GNSSDate.setRolloverReference(new DateComponents(GNSSDate.getRolloverReference(), 1));
        GNSSDate date2 = new GNSSDate(0, 0.0, SatelliteSystem.BEIDOU);
        Assert.assertEquals(8192, date2.getWeekNumber());
    }

    @Test
    public void testSerializationGPS() throws ClassNotFoundException, IOException {
        final DateComponents date = new DateComponents(2006, 8, 9);
        final TimeComponents time = new TimeComponents(16, 31, 03);
        doTestSerialization(SatelliteSystem.GPS, date, time, 1387, 318677000.0);
    }

    @Test
    public void testSerializationGalileo() throws ClassNotFoundException, IOException {
        final DateComponents date = new DateComponents(1999, 12, 25);
        final TimeComponents time = new TimeComponents(12, 00, 00);
        doTestSerialization(SatelliteSystem.GALILEO, date, time, 17, 561600000.0);
    }

    @Test
    public void testSerializationQZSS() throws ClassNotFoundException, IOException {
        final DateComponents date = new DateComponents(2006, 8, 9);
        final TimeComponents time = new TimeComponents(16, 31, 03);
        doTestSerialization(SatelliteSystem.QZSS, date, time, 1387, 318677000.0);
    }

    @Test
    public void testSerializationBeidou() throws ClassNotFoundException, IOException {
        final DateComponents date = new DateComponents(2010, 2, 26);
        final TimeComponents time = new TimeComponents(23, 15, 12);
        doTestSerialization(SatelliteSystem.BEIDOU, date, time, 216, 515713000.0);
    }

    @Test
    public void testDefaultRolloverReference() {
        Assert.assertNull(GNSSDate.getRolloverReference());
        GNSSDate date = new GNSSDate(305, 1.5, SatelliteSystem.GPS);
        // the default reference is extracted from last EOP entry
        // which in this test comes from bulletin B 218, in the final values section
        Assert.assertEquals("2006-03-05", GNSSDate.getRolloverReference().toString());
        Assert.assertEquals(305 + 1024, date.getWeekNumber());
    }

    @Test
    public void testUserRolloverReference() {
        GNSSDate.setRolloverReference(new DateComponents(DateComponents.GPS_EPOCH, 7 * (3 * 1024 + 512)));
        GNSSDate date = new GNSSDate(305, 1.5, SatelliteSystem.GPS);
        Assert.assertEquals("2048-09-13", GNSSDate.getRolloverReference().toString());
        Assert.assertEquals(305 + 3 * 1024, date.getWeekNumber());
    }

    private void doTestSerialization(final SatelliteSystem system,
                                     final DateComponents date, final TimeComponents time,
                                     final int refWeek, final double refMilliSeconds)
        throws IOException, ClassNotFoundException {
        GNSSDate GNSSDate = new GNSSDate(refWeek, refMilliSeconds, system);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream    oos = new ObjectOutputStream(bos);
        oos.writeObject(GNSSDate);

        Assert.assertTrue(bos.size() > 95);
        Assert.assertTrue(bos.size() < 236);

        ByteArrayInputStream  bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream     ois = new ObjectInputStream(bis);
        GNSSDate deserialized  = (GNSSDate) ois.readObject();
        AbsoluteDate ref  = new AbsoluteDate(date, time, utc);
        Assert.assertEquals(refWeek, deserialized.getWeekNumber());
        Assert.assertEquals(refMilliSeconds, deserialized.getMilliInWeek(), 1.0e-15);
        Assert.assertEquals(0, deserialized.getDate().durationFrom(ref), 1.0e-15);

    }

    @Test
    public void testBadSatelliteSystem() {
        try {
            @SuppressWarnings("unused")
            GNSSDate date = new GNSSDate(new AbsoluteDate(), SatelliteSystem.GLONASS);
            Assert.fail("an exception should have been thrown");
        } catch (OrekitException oe) {
            Assert.assertEquals(OrekitMessages.INVALID_SATELLITE_SYSTEM, oe.getSpecifier());
        }
    }

    @Before
    public void setUp() {
        Utils.setDataRoot("regular-data");
        utc = TimeScalesFactory.getUTC();
    }

    private TimeScale utc;

}

/* Copyright 2002-2020 CS Group
 * Licensed to CS Group (CS) under one or more
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
package org.orekit.errors;


import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class OrekitInternalErrorTest {

    @Test
    public void testMessage() {
        OrekitInternalError e = new OrekitInternalError(null);
        Assert.assertEquals(OrekitMessages.INTERNAL_ERROR, e.getSpecifier());
        Assert.assertEquals(1, e.getParts().length);
        Assert.assertEquals("https://gitlab.orekit.org/orekit/orekit/issues", e.getParts()[0]);
        Assert.assertTrue(e.getMessage().contains("https://gitlab.orekit.org/orekit/orekit/issues"));
        Assert.assertEquals(e.getMessage(Locale.getDefault()), e.getLocalizedMessage());
        Assert.assertEquals("erreur interne, merci de signaler le problème en ouvrant une fiche d'anomalie sur https://gitlab.orekit.org/orekit/orekit/issues",
                            e.getMessage(Locale.FRENCH));
    }

}

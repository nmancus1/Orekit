/* Copyright 2011-2012 Space Applications Services
 * Licensed to CS Communication & Systèmes (CS) under one or more
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
package org.orekit.models.earth.troposphere;

import java.util.Collections;
import java.util.List;

import org.hipparchus.Field;
import org.hipparchus.RealFieldElement;
import org.hipparchus.util.FastMath;
import org.hipparchus.util.MathArrays;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.utils.ParameterDriver;

/** The Marini-Murray tropospheric delay model for laser ranging.
 *
 * @see "Marini, J.W., and C.W. Murray, correction of Laser Range Tracking Data for
 *      Atmospheric Refraction at Elevations Above 10 degrees, X-591-73-351, NASA GSFC, 1973"
 *
 * @author Joris Olympio
 */
public class MariniMurrayModel implements DiscreteTroposphericModel {

    /** The temperature at the station, K. */
    private double T0;

    /** The atmospheric pressure, mbar. */
    private double P0;

    /** water vapor pressure at the laser site, mbar. */
    private double e0;

    /** Geodetic site latitude, radians. */
    private double latitude;

    /** Laser wavelength, micrometers. */
    private double lambda;

    /** Create a new Marini-Murray model for the troposphere using the given
     * environmental conditions.
     * @param t0 the temperature at the station, K
     * @param p0 the atmospheric pressure at the station, mbar
     * @param rh the humidity at the station, percent (50% -&gt; 0.5)
     * @param latitude site latitude
     * @param lambda laser wavelength (c/f), nm
     */
    public MariniMurrayModel(final double t0, final double p0, final double rh, final double latitude, final double lambda) {
        this.T0 = t0;
        this.P0 = p0;

        this.e0 = getWaterVapor(rh);

        this.latitude = latitude;

        this.lambda = lambda * 1e-3;
    }

    /** Create a new Marini-Murray model using a standard atmosphere model.
     *
     * <ul>
     * <li>temperature: 20 degree Celsius</li>
     * <li>pressure: 1013.25 mbar</li>
     * <li>humidity: 50%</li>
     * </ul>
     *
     * @param latitude site latitude
     * @param lambda laser wavelength (c/f), nm
     *
     * @return a Marini-Murray model with standard environmental values
     */
    public static MariniMurrayModel getStandardModel(final double latitude, final double lambda) {
        return new MariniMurrayModel(273.15 + 20, 1013.25, 0.5, latitude, lambda);
    }

    @Override
    public double pathDelay(final double elevation, final double height,
                            final double[] parameters, final AbsoluteDate date) {
        final double A = 0.002357 * P0 + 0.000141 * e0;
        final double K = 1.163 - 0.00968 * FastMath.cos(2 * latitude) - 0.00104 * T0 + 0.00001435 * P0;
        final double B = (1.084 * 1e-8) * P0 * T0 * K + (4.734 * 1e-8) * P0 * (P0 / T0) * (2 * K) / (3 * K - 1);
        final double flambda = getLaserFrequencyParameter();

        final double fsite = getSiteFunctionValue(height / 1000.);

        final double sinE = FastMath.sin(elevation);
        final double dR = (flambda / fsite) * (A + B) / (sinE + B / ((A + B) * (sinE + 0.01)) );
        return dR;
    }

    @Override
    public <T extends RealFieldElement<T>> T pathDelay(final T elevation, final T height,
                                                       final T[] parameters, final FieldAbsoluteDate<T> date) {
        final double A = 0.002357 * P0 + 0.000141 * e0;
        final double K = 1.163 - 0.00968 * FastMath.cos(2 * latitude) - 0.00104 * T0 + 0.00001435 * P0;
        final double B = (1.084 * 1e-8) * P0 * T0 * K + (4.734 * 1e-8) * P0 * (P0 / T0) * (2 * K) / (3 * K - 1);
        final double flambda = getLaserFrequencyParameter();

        final T fsite = getSiteFunctionValue(height.divide(1000.));

        final T sinE = FastMath.sin(elevation);
        final T dR = fsite.divide(flambda).reciprocal().multiply(A + B).divide(sinE.add(sinE.add(0.01).multiply(A + B).divide(B).reciprocal()));
        return dR;
    }

    @Override
    public double[] computeZenithDelay(final double height, final double[] parameters,
                                       final AbsoluteDate date) {
        return new double[] {
            pathDelay(0.5 * FastMath.PI, height, parameters, date),
            0.
        };
    }

    @Override
    public <T extends RealFieldElement<T>> T[] computeZenithDelay(final T height, final T[] parameters,
                                                                  final FieldAbsoluteDate<T> date) {
        final Field<T> field = height.getField();
        final T zero = field.getZero();
        final T[] delay = MathArrays.buildArray(field, 2);
        delay[0] = pathDelay(zero.add(0.5 * FastMath.PI), height, parameters, date);
        delay[1] = zero;
        return delay;
    }

    @Override
    public double[] mappingFactors(final double elevation, final double height,
                                   final double[] parameters, final AbsoluteDate date) {
        return new double[] {
            1.0,
            1.0
        };
    }

    @Override
    public <T extends RealFieldElement<T>> T[] mappingFactors(final T elevation, final T height,
                                                              final T[] parameters, final FieldAbsoluteDate<T> date) {
        final Field<T> field = date.getField();
        final T one = field.getOne();
        final T[] factors = MathArrays.buildArray(field, 2);
        factors[0] = one;
        factors[1] = one;
        return factors;
    }

    @Override
    public List<ParameterDriver> getParametersDrivers() {
        return Collections.emptyList();
    }

    /** Get the laser frequency parameter f(lambda).
     * It is one for Ruby laser (lambda = 0.6943 micron)
     * For infrared lasers, f(lambda) = 0.97966.
     *
     * @return the laser frequency parameter f(lambda).
     */
    private double getLaserFrequencyParameter() {
        return 0.9650 + 0.0164 * FastMath.pow(lambda, -2) + 0.000228 * FastMath.pow(lambda, -4);
    }

    /** Get the laser frequency parameter f(lambda).
     *
     * @param height height above the geoid, km
     * @return the laser frequency parameter f(lambda).
     */
    private double getSiteFunctionValue(final double height) {
        return 1. - 0.0026 * FastMath.cos(2 * latitude) - 0.00031 * height;
    }

    /** Get the laser frequency parameter f(lambda).
    *
    * @param <T> type of the elements
    * @param height height above the geoid, km
    * @return the laser frequency parameter f(lambda).
    */
    private <T extends RealFieldElement<T>> T getSiteFunctionValue(final T height) {
        return height.multiply(0.00031).negate().subtract(0.0026 * FastMath.cos(2 * latitude)).add(1.);
    }

    /** Get the water vapor.
     * The water vapor model is the one of Giacomo and Davis as indicated in IERS TN 32, chap. 9.
     *
     * See: Giacomo, P., Equation for the dertermination of the density of moist air, Metrologia, V. 18, 1982
     *
     * @param rh relative humidity, in percent (50% -&gt; 0.5).
     * @return the water vapor, in mbar (1 mbar = 100 Pa).
     */
    private double getWaterVapor(final double rh) {

        // saturation water vapor, equation (3) of reference paper, in mbar
        // with amended 1991 values (see reference paper)
        final double es = 0.01 * FastMath.exp((1.2378847 * 1e-5) * T0 * T0 -
                                              (1.9121316 * 1e-2) * T0 +
                                              33.93711047 -
                                              (6.3431645 * 1e3) * 1. / T0);

        // enhancement factor, equation (4) of reference paper
        final double fw = 1.00062 + (3.14 * 1e-6) * P0 + (5.6 * 1e-7) * FastMath.pow(T0 - 273.15, 2);

        final double e = rh * fw * es;
        return e;
    }

}

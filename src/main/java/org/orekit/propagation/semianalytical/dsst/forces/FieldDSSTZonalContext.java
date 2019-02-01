/* Copyright 2002-2018 CS Systèmes d'Information
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
package org.orekit.propagation.semianalytical.dsst.forces;

import java.util.TreeMap;

import org.hipparchus.RealFieldElement;
import org.hipparchus.util.FastMath;
import org.orekit.forces.gravity.potential.UnnormalizedSphericalHarmonicsProvider;
import org.orekit.propagation.semianalytical.dsst.utilities.AuxiliaryElements;
import org.orekit.propagation.semianalytical.dsst.utilities.CoefficientsFactory;
import org.orekit.propagation.semianalytical.dsst.utilities.CoefficientsFactory.NSKey;
import org.orekit.propagation.semianalytical.dsst.utilities.FieldAuxiliaryElements;

/** This class is a container for the field attributes of
 * {@link org.orekit.propagation.semianalytical.dsst.forces.DSSTZonal DSSTZonal}.
 * <p>
 * It replaces the last version of the method
 * {@link  org.orekit.propagation.semianalytical.dsst.forces.DSSTForceModel#initializeStep(AuxiliaryElements)
 * initializeStep(AuxiliaryElements)}.
 * </p>
 */
public class FieldDSSTZonalContext<T extends RealFieldElement<T>> extends FieldForceModelContext<T> {

    /** Maximal degree to consider for harmonics potential. */
    private final int maxDegree;

    /** Coefficient used to define the mean disturbing function V<sub>ns</sub> coefficient. */
    private final TreeMap<NSKey, Double> Vns;

    /** A = sqrt(μ * a). */
    private final T A;

    // Common factors for potential computation
    /** &Chi; = 1 / sqrt(1 - e²) = 1 / B. */
    private T X;
    /** &Chi;². */
    private T XX;
    /** &Chi;³. */
    private T XXX;
    /** 1 / (A * B) .*/
    private T ooAB;
    /** B / A .*/
    private T BoA;
    /** B / A(1 + B) .*/
    private T BoABpo;
    /** -C / (2 * A * B) .*/
    private T mCo2AB;
    /** -2 * a / A .*/
    private T m2aoA;
    /** μ / a .*/
    private T muoa;
    /** R / a .*/
    private T roa;

    /** Keplerian mean motion. */
    private final T n;

    /** Keplerian period. */
    private final T period;

    // Short period terms
    /** h * k. */
    private T hk;
    /** k² - h². */
    private T k2mh2;
    /** (k² - h²) / 2. */
    private T k2mh2o2;
    /** 1 / (n² * a²). */
    private T oon2a2;
    /** 1 / (n² * a) . */
    private T oon2a;
    /** χ³ / (n² * a). */
    private T x3on2a;
    /** χ / (n² * a²). */
    private T xon2a2;
    /** (C * χ) / ( 2 * n² * a² ). */
    private T cxo2n2a2;
    /** (χ²) / (n² * a² * (χ + 1 ) ). */
    private T x2on2a2xp1;
    /** B * B.*/
    private T BB;

    /** Simple constructor.
     * Performs initialization at each integration step for the current force model.
     * This method aims at being called before mean elements rates computation
     * @param auxiliaryElements auxiliary elements related to the current orbit
     * @param provider provider for spherical harmonics
     * @param parameters values of the force model parameters
     */
    public FieldDSSTZonalContext(final FieldAuxiliaryElements<T> auxiliaryElements,
                                 final UnnormalizedSphericalHarmonicsProvider provider,
                                 final T[] parameters) {

        super(auxiliaryElements);

        this.maxDegree = provider.getMaxDegree();

        // Vns coefficients
        this.Vns = CoefficientsFactory.computeVns(provider.getMaxDegree() + 1);

        final T mu = parameters[0];

        // Keplerian mean motion
        final T absA = FastMath.abs(auxiliaryElements.getSma());
        n = FastMath.sqrt(mu.divide(absA)).divide(absA);

        // Keplerian period
        final T a = auxiliaryElements.getSma();
        period = (a.getReal() < 0) ? auxiliaryElements.getSma().getField().getZero().add(Double.POSITIVE_INFINITY) : a.multiply(2 * FastMath.PI).multiply(a.divide(mu).sqrt());

        A = FastMath.sqrt(mu.multiply(auxiliaryElements.getSma()));

        // &Chi; = 1 / B
        X   = auxiliaryElements.getB().reciprocal();
        XX  = X.multiply(X);
        XXX = X.multiply(XX);

        // 1 / AB
        ooAB   = (A.multiply(auxiliaryElements.getB())).reciprocal();
        // B / A
        BoA    = auxiliaryElements.getB().divide(A);
        // -C / 2AB
        mCo2AB = auxiliaryElements.getC().multiply(ooAB).divide(2.).negate();
        // B / A(1 + B)
        BoABpo = BoA.divide(auxiliaryElements.getB().add(1.));
        // -2 * a / A
        m2aoA  = auxiliaryElements.getSma().divide(A).multiply(2.).negate();
        // μ / a
        muoa   = mu.divide(auxiliaryElements.getSma());
        // R / a
        roa    = auxiliaryElements.getSma().divide(provider.getAe()).reciprocal();

        // Short period terms

        // h * k.
        hk = auxiliaryElements.getH().multiply(auxiliaryElements.getK());
        // k² - h².
        k2mh2 = auxiliaryElements.getK().multiply(auxiliaryElements.getK()).subtract(auxiliaryElements.getH().multiply(auxiliaryElements.getH()));
        // (k² - h²) / 2.
        k2mh2o2 = k2mh2.divide(2.);
        // 1 / (n² * a²) = 1 / (n * A)
        oon2a2 = (A.multiply(n)).reciprocal();
        // 1 / (n² * a) = a / (n * A)
        oon2a = auxiliaryElements.getSma().multiply(oon2a2);
        // χ³ / (n² * a)
        x3on2a = XXX.multiply(oon2a);
        // χ / (n² * a²)
        xon2a2 = X.multiply(oon2a2);
        // (C * χ) / ( 2 * n² * a² )
        cxo2n2a2 = xon2a2.multiply(auxiliaryElements.getC()).divide(2.);
        // (χ²) / (n² * a² * (χ + 1 ) )
        x2on2a2xp1 = xon2a2.multiply(X).divide(X.add(1.));
        // B * B
        BB = auxiliaryElements.getB().multiply(auxiliaryElements.getB());

    }

    /** Get A = sqrt(μ * a).
     * @return A
     */
    public T getA() {
        return A;
    }

    /** Get &Chi; = 1 / sqrt(1 - e²) = 1 / B.
     * @return &Chi;
     */
    public T getX() {
        return X;
    }

    /** Get &Chi;².
     * @return &Chi;².
     */
    public T getXX() {
        return XX;
    }

    /** Get &Chi;³.
     * @return &Chi;³
     */
    public T getXXX() {
        return XXX;
    }

    /** Get m2aoA = -2 * a / A.
     * @return m2aoA
     */
    public T getM2aoA() {
        return m2aoA;
    }

    /** Get B / A.
     * @return BoA
     */
    public T getBoA() {
        return BoA;
    }

    /** Get ooAB = 1 / (A * B).
     * @return ooAB
     */
    public T getOoAB() {
        return ooAB;
    }

    /** Get mCo2AB = -C / 2AB.
     * @return mCo2AB
     */
    public T getMCo2AB() {
        return mCo2AB;
    }

    /** Get BoABpo = B / A(1 + B).
     * @return BoABpo
     */
    public T getBoABpo() {
        return BoABpo;
    }

    /** Get μ / a .
     * @return muoa
     */
    public T getMuoa() {
        return muoa;
    }

    /** Get roa = R / a.
     * @return roa
     */
    public T getRoa() {
        return roa;
    }

    /** Get the maximal degree to consider for harmonics potential.
     * @return maxDegree
     */
    public int getMaxDegree() {
        return maxDegree;
    }

    /** Get the V<sub>ns</sub> coefficients.
     * @return Vns
     */
    public TreeMap<NSKey, Double> getVns() {
        return Vns;
    }

    /** Get the Keplerian period.
     * <p>The Keplerian period is computed directly from semi major axis
     * and central acceleration constant.</p>
     * @return Keplerian period in seconds, or positive infinity for hyperbolic orbits
     */
    public T getKeplerianPeriod() {
        return period;
    }

    /** Get the Keplerian mean motion.
     * <p>The Keplerian mean motion is computed directly from semi major axis
     * and central acceleration constant.</p>
     * @return Keplerian mean motion in radians per second
     */
    public T getMeanMotion() {
        return n;
    }

    /** Get h * k.
     * @return hk
     */
    public T getHK() {
        return hk;
    }

    /** Get k² - h².
     * @return k2mh2
     */
    public T getK2MH2() {
        return k2mh2;
    }

    /** Get (k² - h²) / 2.
     * @return k2mh2o2
     */
    public T getK2MH2O2() {
        return k2mh2o2;
    }

    /** Get 1 / (n² * a²).
     * @return oon2a2
     */
    public T getOON2A2() {
        return oon2a2;
    }

    /** Get 1 / (n² * a).
     * @return oon2a
     */
    public T getOON2A() {
        return oon2a;
    }

    /** Get χ³ / (n² * a).
     * @return x3on2a
     */
    public T getX3ON2A() {
        return x3on2a;
    }

    /** Get χ / (n² * a²).
     * @return xon2a2
     */
    public T getXON2A2() {
        return xon2a2;
    }

    /** Get (C * χ) / ( 2 * n² * a² ).
     * @return cxo2n2a2
     */
    public T getCXO2N2A2() {
        return cxo2n2a2;
    }

    /** Get (χ²) / (n² * a² * (χ + 1 ) ).
     * @return x2on2a2xp1
     */
    public T getX2ON2A2XP1() {
        return x2on2a2xp1;
    }

    /** Get B * B.
     * @return BB
     */
    public T getBB() {
        return BB;
    }

}
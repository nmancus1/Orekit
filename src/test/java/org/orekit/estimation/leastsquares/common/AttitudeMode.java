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

package org.orekit.estimation.leastsquares.common;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.attitudes.AttitudeProvider;
import org.orekit.attitudes.BodyCenterPointing;
import org.orekit.attitudes.LofOffset;
import org.orekit.attitudes.NadirPointing;
import org.orekit.attitudes.YawCompensation;
import org.orekit.attitudes.YawSteering;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.LOFType;

/** Attitude modes.
 * @author Luc Maisonobe
 */
enum AttitudeMode {

    /** Nadir pointing with yaw compensation. */
    NADIR_POINTING_WITH_YAW_COMPENSATION() {
        /** {@inheritDoc} */
        @Override
        public AttitudeProvider getProvider(final Frame inertialFrame, final OneAxisEllipsoid body) {
            return new YawCompensation(inertialFrame, new NadirPointing(inertialFrame, body));
        }
    },

    /** Body center pointing with yaw compensation. */
    CENTER_POINTING_WITH_YAW_STEERING {
        /** {@inheritDoc} */
        @Override
        public AttitudeProvider getProvider(final Frame inertialFrame, final OneAxisEllipsoid body) {
            return new YawSteering(inertialFrame,
                                   new BodyCenterPointing(inertialFrame, body),
                                   CelestialBodyFactory.getSun(),
                                   Vector3D.PLUS_I);
        }
    },

    /** Aligned with Local Vertical, Local Horizontal frame. */
    LOF_ALIGNED_LVLH {
        /** {@inheritDoc} */
        @Override
        public AttitudeProvider getProvider(final Frame inertialFrame, final OneAxisEllipsoid body) {
            return new LofOffset(inertialFrame, LOFType.LVLH);
        }
    },

    /** Aligned with QSW frame. */
    LOF_ALIGNED_QSW {
        /** {@inheritDoc} */
        @Override
        public AttitudeProvider getProvider(final Frame inertialFrame, final OneAxisEllipsoid body) {
            return new LofOffset(inertialFrame, LOFType.QSW);
        }
    },

    /** Aligned with TNW frame. */
    LOF_ALIGNED_TNW {
        /** {@inheritDoc} */
        @Override
        public AttitudeProvider getProvider(final Frame inertialFrame, final OneAxisEllipsoid body) {
            return new LofOffset(inertialFrame, LOFType.TNW);
        }
    },

    /** aligned with Velocity - Normal - Co-normal frame. */
    LOF_ALIGNED_VNC {
        /** {@inheritDoc} */
        @Override
        public AttitudeProvider getProvider(final Frame inertialFrame, final OneAxisEllipsoid body) {
            return new LofOffset(inertialFrame, LOFType.VNC);
        }
    },

    /** Aligned with Vehicle Velocity, Local Horizontal frame. */
    LOF_ALIGNED_VVLH {
        /** {@inheritDoc} */
        @Override
        public AttitudeProvider getProvider(final Frame inertialFrame, final OneAxisEllipsoid body) {
            return new LofOffset(inertialFrame, LOFType.VVLH);
        }
    };

    /** Get attitude provider.
     * @param inertialFrame inertial frame
     * @param body central body
     * @return attitude provider
     */
    public abstract AttitudeProvider getProvider(Frame inertialFrame, OneAxisEllipsoid body);

}

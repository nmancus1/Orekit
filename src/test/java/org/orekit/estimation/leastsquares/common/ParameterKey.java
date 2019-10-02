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

/** Input parameter keys.
 * @author Luc Maisonobe
 */
public enum ParameterKey {
    // CHECKSTYLE: stop JavadocVariable check
    ORBIT_DATE,
    ORBIT_CIRCULAR_A,
    ORBIT_CIRCULAR_EX,
    ORBIT_CIRCULAR_EY,
    ORBIT_CIRCULAR_I,
    ORBIT_CIRCULAR_RAAN,
    ORBIT_CIRCULAR_ALPHA,
    ORBIT_EQUINOCTIAL_A,
    ORBIT_EQUINOCTIAL_EX,
    ORBIT_EQUINOCTIAL_EY,
    ORBIT_EQUINOCTIAL_HX,
    ORBIT_EQUINOCTIAL_HY,
    ORBIT_EQUINOCTIAL_LAMBDA,
    ORBIT_KEPLERIAN_A,
    ORBIT_KEPLERIAN_E,
    ORBIT_KEPLERIAN_I,
    ORBIT_KEPLERIAN_PA,
    ORBIT_KEPLERIAN_RAAN,
    ORBIT_KEPLERIAN_ANOMALY,
    ORBIT_ANGLE_TYPE,
    ORBIT_TLE_LINE_1,
    ORBIT_TLE_LINE_2,
    ORBIT_CARTESIAN_PX,
    ORBIT_CARTESIAN_PY,
    ORBIT_CARTESIAN_PZ,
    ORBIT_CARTESIAN_VX,
    ORBIT_CARTESIAN_VY,
    ORBIT_CARTESIAN_VZ,
    MASS,
    IERS_CONVENTIONS,
    INERTIAL_FRAME,
    PROPAGATOR_MIN_STEP,
    PROPAGATOR_MAX_STEP,
    PROPAGATOR_POSITION_ERROR,
    BODY_FRAME,
    BODY_EQUATORIAL_RADIUS,
    BODY_INVERSE_FLATTENING,
    CENTRAL_BODY_DEGREE,
    CENTRAL_BODY_ORDER,
    OCEAN_TIDES_DEGREE,
    OCEAN_TIDES_ORDER,
    SOLID_TIDES_SUN,
    SOLID_TIDES_MOON,
    THIRD_BODY_SUN,
    THIRD_BODY_MOON,
    DRAG,
    DRAG_CD,
    DRAG_CD_ESTIMATED,
    DRAG_AREA,
    SOLAR_RADIATION_PRESSURE,
    SOLAR_RADIATION_PRESSURE_CR,
    SOLAR_RADIATION_PRESSURE_CR_ESTIMATED,
    SOLAR_RADIATION_PRESSURE_AREA,
    GENERAL_RELATIVITY,
    ATTITUDE_MODE,
    POLYNOMIAL_ACCELERATION_NAME,
    POLYNOMIAL_ACCELERATION_DIRECTION_X,
    POLYNOMIAL_ACCELERATION_DIRECTION_Y,
    POLYNOMIAL_ACCELERATION_DIRECTION_Z,
    POLYNOMIAL_ACCELERATION_COEFFICIENTS,
    POLYNOMIAL_ACCELERATION_ESTIMATED,
    ONBOARD_RANGE_BIAS,
    ONBOARD_RANGE_BIAS_MIN,
    ONBOARD_RANGE_BIAS_MAX,
    ONBOARD_RANGE_BIAS_ESTIMATED,
    ON_BOARD_ANTENNA_PHASE_CENTER_X,
    ON_BOARD_ANTENNA_PHASE_CENTER_Y,
    ON_BOARD_ANTENNA_PHASE_CENTER_Z,
    ON_BOARD_CLOCK_OFFSET,
    ON_BOARD_CLOCK_OFFSET_MIN,
    ON_BOARD_CLOCK_OFFSET_MAX,
    ON_BOARD_CLOCK_OFFSET_ESTIMATED,
    GROUND_STATION_NAME,
    GROUND_STATION_LATITUDE,
    GROUND_STATION_LONGITUDE,
    GROUND_STATION_ALTITUDE,
    GROUND_STATION_POSITION_ESTIMATED,
    GROUND_STATION_CLOCK_OFFSET,
    GROUND_STATION_CLOCK_OFFSET_MIN,
    GROUND_STATION_CLOCK_OFFSET_MAX,
    GROUND_STATION_CLOCK_OFFSET_ESTIMATED,
    GROUND_STATION_TROPOSPHERIC_MODEL_ESTIMATED,
    GROUND_STATION_TROPOSPHERIC_ZENITH_DELAY,
    GROUND_STATION_TROPOSPHERIC_DELAY_ESTIMATED,
    GROUND_STATION_GLOBAL_MAPPING_FUNCTION,
    GROUND_STATION_NIELL_MAPPING_FUNCTION,
    GROUND_STATION_WEATHER_ESTIMATED,
    GROUND_STATION_RANGE_SIGMA,
    GROUND_STATION_RANGE_BIAS,
    GROUND_STATION_RANGE_BIAS_MIN,
    GROUND_STATION_RANGE_BIAS_MAX,
    GROUND_STATION_RANGE_BIAS_ESTIMATED,
    GROUND_STATION_RANGE_RATE_SIGMA,
    GROUND_STATION_RANGE_RATE_BIAS,
    GROUND_STATION_RANGE_RATE_BIAS_MIN,
    GROUND_STATION_RANGE_RATE_BIAS_MAX,
    GROUND_STATION_RANGE_RATE_BIAS_ESTIMATED,
    GROUND_STATION_AZIMUTH_SIGMA,
    GROUND_STATION_AZIMUTH_BIAS,
    GROUND_STATION_AZIMUTH_BIAS_MIN,
    GROUND_STATION_AZIMUTH_BIAS_MAX,
    GROUND_STATION_ELEVATION_SIGMA,
    GROUND_STATION_ELEVATION_BIAS,
    GROUND_STATION_ELEVATION_BIAS_MIN,
    GROUND_STATION_ELEVATION_BIAS_MAX,
    GROUND_STATION_AZ_EL_BIASES_ESTIMATED,
    GROUND_STATION_ELEVATION_REFRACTION_CORRECTION,
    GROUND_STATION_RANGE_TROPOSPHERIC_CORRECTION,
    GROUND_STATION_IONOSPHERIC_CORRECTION,
    SOLID_TIDES_DISPLACEMENT_CORRECTION,
    SOLID_TIDES_DISPLACEMENT_REMOVE_PERMANENT_DEFORMATION,
    OCEAN_LOADING_CORRECTION,
    RANGE_MEASUREMENTS_BASE_WEIGHT,
    RANGE_RATE_MEASUREMENTS_BASE_WEIGHT,
    AZIMUTH_MEASUREMENTS_BASE_WEIGHT,
    ELEVATION_MEASUREMENTS_BASE_WEIGHT,
    PV_MEASUREMENTS_BASE_WEIGHT,
    PV_MEASUREMENTS_POSITION_SIGMA,
    PV_MEASUREMENTS_VELOCITY_SIGMA,
    RANGE_OUTLIER_REJECTION_MULTIPLIER,
    RANGE_OUTLIER_REJECTION_STARTING_ITERATION,
    RANGE_RATE_OUTLIER_REJECTION_MULTIPLIER,
    RANGE_RATE_OUTLIER_REJECTION_STARTING_ITERATION,
    AZ_EL_OUTLIER_REJECTION_MULTIPLIER,
    AZ_EL_OUTLIER_REJECTION_STARTING_ITERATION,
    PV_OUTLIER_REJECTION_MULTIPLIER,
    PV_OUTLIER_REJECTION_STARTING_ITERATION,
    SATELLITE_ID_IN_RINEX_FILES,
    MEASUREMENTS_FILES,
    OUTPUT_BASE_NAME,
    ESTIMATOR_OPTIMIZATION_ENGINE,
    ESTIMATOR_LEVENBERG_MARQUARDT_INITIAL_STEP_BOUND_FACTOR,
    ESTIMATOR_ORBITAL_PARAMETERS_POSITION_SCALE,
    ESTIMATOR_NORMALIZED_PARAMETERS_CONVERGENCE_THRESHOLD,
    ESTIMATOR_MAX_ITERATIONS,
    ESTIMATOR_MAX_EVALUATIONS;
    // CHECKSTYLE: resume JavadocVariable check
}

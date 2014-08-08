/*
 * Copyright (c) 2011-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.abst.fiducial;

import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.LensDistortionOps;
import boofcv.alg.fiducial.DetectFiducialSquareBinary;
import boofcv.struct.calib.IntrinsicParameters;
import boofcv.struct.image.ImageSingleBand;
import boofcv.struct.image.ImageType;
import georegression.struct.se.Se3_F64;

/**
 * Wrapper around {@link boofcv.alg.fiducial.DetectFiducialSquareBinary} for {@link boofcv.abst.fiducial.FiducialDetector}
 *
 * @author Peter Abeles
 */
public class SquareBinary_to_FiducialDetector<T extends ImageSingleBand>
	implements FiducialDetector<T>
{
	DetectFiducialSquareBinary<T> alg;

	IntrinsicParameters intrinsicAdj = new IntrinsicParameters();

	ImageDistort<T,T> undistorter;

	T undistorted;

	ImageType<T> type;

	public SquareBinary_to_FiducialDetector(DetectFiducialSquareBinary<T> alg) {
		this.alg = alg;
		this.type = ImageType.single(alg.getInputType());
		this.undistorted = type.createImage(1,1);
	}

	@Override
	public void detect(T input) {
		if( undistorter != null ) {
			undistorted.reshape(input.width,input.height);
			undistorter.apply(input,undistorted);
			input = undistorted;
		}
		alg.process(input);
	}

	@Override
	public void setIntrinsic(IntrinsicParameters intrinsic) {
		if( intrinsic.radial != null) {
			undistorter = LensDistortionOps.removeDistortion(true, null, intrinsic, intrinsicAdj, type);
			alg.setIntrinsic(intrinsicAdj);
		} else {
			undistorter = null;
			alg.setIntrinsic(intrinsic);
		}
	}

	@Override
	public int totalFound() {
		return alg.getFound().size;
	}

	@Override
	public void getFiducialToWorld(int which, Se3_F64 fiducialToSensor ) {
		fiducialToSensor.set(alg.getFound().get(which).targetToSensor);
	}

	@Override
	public int getId( int which ) {
		return alg.getFound().get(which).index;
	}

	@Override
	public ImageType<T> getInputType() {
		return type;
	}
}
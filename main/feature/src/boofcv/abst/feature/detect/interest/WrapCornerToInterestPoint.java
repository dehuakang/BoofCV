/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
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

package boofcv.abst.feature.detect.interest;

import boofcv.abst.filter.derivative.ImageGradient;
import boofcv.abst.filter.derivative.ImageHessian;
import boofcv.struct.QueueCorner;
import boofcv.struct.image.ImageSingleBand;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I16;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around {@link GeneralFeatureDetector} to make it compatible with {@link InterestPointDetector}.
 *
 * @param <T> Input image type.
 * @param <D> Image derivative type.
 *
 * @author Peter Abeles
 */
// TODO rename
public class WrapCornerToInterestPoint<T extends ImageSingleBand, D extends ImageSingleBand>
		extends EasyGeneralFeatureDetector<T,D>
		implements InterestPointDetector<T>
{

	double scale = 1;

	// list of points it found
	protected List<Point2D_F64> foundPoints;

	public WrapCornerToInterestPoint(GeneralFeatureDetector<T, D> detector,
									 double scale ,
									 Class<T> imageType, Class<D> derivType ) {
		super(detector,imageType,derivType);
		this.scale = scale;
	}

	public WrapCornerToInterestPoint(GeneralFeatureDetector<T, D> detector,
									 ImageGradient<T, D> gradient,
									 ImageHessian<D> hessian,
									 double scale,
									 Class<D> derivType) {
		super(detector, gradient, hessian, derivType);
		this.scale = scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getScale() {
		return scale;
	}

	@Override
	public void detect(T input) {
		super.detect(input,null);

		QueueCorner corners = detector.getFeatures();

		foundPoints = new ArrayList<Point2D_F64>();
		for (int i = 0; i < corners.size; i++) {
			Point2D_I16 p = corners.get(i);
			foundPoints.add(new Point2D_F64(p.x, p.y));
		}
	}

	@Override
	public int getNumberOfFeatures() {
		return foundPoints.size();
	}

	@Override
	public Point2D_F64 getLocation(int featureIndex) {
		return foundPoints.get(featureIndex);
	}

	@Override
	public double getScale(int featureIndex) {
		return scale;
	}

	@Override
	public double getOrientation(int featureIndex) {
		return 0;
	}

	@Override
	public boolean hasScale() {
		return false;
	}

	@Override
	public boolean hasOrientation() {
		return false;
	}
}

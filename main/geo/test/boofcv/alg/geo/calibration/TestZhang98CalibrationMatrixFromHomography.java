package boofcv.alg.geo.calibration;

import boofcv.alg.calibration.Zhang98CalibrationMatrixFromHomographies;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestZhang98CalibrationMatrixFromHomography {

	Random rand = new Random(123);
	List<DenseMatrix64F> homographies;

	@Test
	public void withSkew() {

		DenseMatrix64F K = GenericCalibrationGrid.createStandardCalibration();

		// try different numbers of observations
		for( int N = 3; N <= 6; N++ ) {
			homographies = GenericCalibrationGrid.createHomographies(K, N, rand);

			Zhang98CalibrationMatrixFromHomographies alg =
					new Zhang98CalibrationMatrixFromHomographies(false);

			alg.process(homographies);

			DenseMatrix64F K_found = alg.getCalibrationMatrix();

			checkK(K,K_found);
		}
	}

	@Test
	public void withNoSkew() {

		// try different sizes
		for( int N = 2; N <= 5; N++ ) {
			DenseMatrix64F K = GenericCalibrationGrid.createStandardCalibration();
			// force skew to zero
			K.set(0,1,0);

			homographies = GenericCalibrationGrid.createHomographies(K, N, rand);

			Zhang98CalibrationMatrixFromHomographies alg =
					new Zhang98CalibrationMatrixFromHomographies(true);

			alg.process(homographies);

			DenseMatrix64F K_found = alg.getCalibrationMatrix();

			checkK(K, K_found);
		}
	}

	/**
	 * compare two calibration matrices against each other taking in account the differences in tolerance
	 * for different elements
	 */
	private void checkK( DenseMatrix64F a , DenseMatrix64F b ) {
		assertEquals(a.get(0,0),b.get(0,0),0.05);
		assertEquals(a.get(1,1),b.get(1,1),0.05);
		assertEquals(a.get(0,1),b.get(0,1),0.01);
		assertEquals(a.get(0,2),b.get(0,2),2);
		assertEquals(a.get(1,2),b.get(1,2),2);
		assertEquals(a.get(2,2),b.get(2,2),1e-8);
	}


}
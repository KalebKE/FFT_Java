/*
Copyright (C) 2011  Kircher Engineering, LLC (http://www.kircherEngineering.com)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package calculations;

/**
 * Class that produces a third degree interpolation polynomial.
 * @author Kaleb Kircher and unknown authors
 *
 */
public class InterpolatePoly
{
	private double[] interpolateSignal;

	/**
	 * @return the interpolateSignal
	 */
	public double[] getInterpolateSignal()
	{
		return interpolateSignal;
	}

	/**
	 * Initializes x,y, value to be interpolated, and degree.
	 * @param radiansSignal radian signal
	 * @param signal signal
	 * @param interpolateRads radians for signal
	 * @param degree degree to interpolate
	 */
	public InterpolatePoly(double[] radiansSignal, double[] signal,
			double[] interpolateRads, int degree)
	{
		interpolateSignal = new double[interpolateRads.length];
		
		for (int i = 0; i < interpolateRads.length; i++)
		{
			interpolateSignal[i] = poly_interpolate(radiansSignal, signal,
					interpolateRads[i], degree);
		}
	}

	/**
	 * A class that performs Gaussian elimination to find the polynomial. 
	 * Author: Unknown
	 * @param matrix to be solved
	 * @return interpolated value
	 */
	private static double[] lin_solve(double[][] matrix)
	{
		double[] results = new double[matrix.length];
		int[] order = new int[matrix.length];
		for (int i = 0; i < order.length; ++i)
		{
			order[i] = i;
		}
		for (int i = 0; i < matrix.length; ++i)
		{
			// partial pivot
			int maxIndex = i;
			for (int j = i + 1; j < matrix.length; ++j)
			{
				if (Math.abs(matrix[maxIndex][i]) < Math.abs(matrix[j][i]))
				{
					maxIndex = j;
				}
			}
			if (maxIndex != i)
			{
				// swap order
				{
					int temp = order[i];
					order[i] = order[maxIndex];
					order[maxIndex] = temp;
				}
				// swap matrix
				for (int j = 0; j < matrix[0].length; ++j)
				{
					double temp = matrix[i][j];
					matrix[i][j] = matrix[maxIndex][j];
					matrix[maxIndex][j] = temp;
				}
			}
			if (Math.abs(matrix[i][i]) < 1e-15)
			{
				throw new RuntimeException("Singularity detected");
			}
			for (int j = i + 1; j < matrix.length; ++j)
			{
				double factor = matrix[j][i] / matrix[i][i];
				for (int k = i; k < matrix[0].length; ++k)
				{
					matrix[j][k] -= matrix[i][k] * factor;
				}
			}
		}
		for (int i = matrix.length - 1; i >= 0; --i)
		{
			// back substitute
			results[i] = matrix[i][matrix.length];
			for (int j = i + 1; j < matrix.length; ++j)
			{
				results[i] -= results[j] * matrix[i][j];
			}
			results[i] /= matrix[i][i];
		}
		double[] correctResults = new double[results.length];
		for (int i = 0; i < order.length; ++i)
		{
			// switch the order around back to the original order
			correctResults[order[i]] = results[i];
		}
		return results;
	}

	public static double poly_interpolate(double[] dataX, double[] dataY,
			double x, int power)
	{
		int xIndex = 0;
		while (xIndex < dataX.length - (1 + power + (dataX.length - 1) % power)
				&& dataX[xIndex + power] < x)
		{
			xIndex += power;
		}

		double matrix[][] = new double[power + 1][power + 2];
		for (int i = 0; i < power + 1; ++i)
		{
			for (int j = 0; j < power; ++j)
			{
				matrix[i][j] = Math.pow(dataX[xIndex + i], (power - j));
			}
			matrix[i][power] = 1;
			matrix[i][power + 1] = dataY[xIndex + i];
		}
		double[] coefficients = lin_solve(matrix);
		double answer = 0;
		for (int i = 0; i < coefficients.length; ++i)
		{
			answer += coefficients[i] * Math.pow(x, (power - i));
		}
		return answer;
	}
}

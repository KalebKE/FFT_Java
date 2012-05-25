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
 * A class that sums the Fourier Coefficients.
 * @author Kaleb Kircher
 *
 */
public class SumCoefficiants
{
	/**
	 * A method that sums the Fourier Coefficients based on sample size.
	 * @param C A double[] containing the coefficients
	 * @param NN A int containing the sample size
	 * @return A double containing the sum
	 */
	public double sum(double[] C, double NN)
	{
		double CSum = 0;
		// sum the coefficients
		for (int i = 0; i < C.length; i++)
		{
			CSum += C[i];
		}

		// the sum of all the real parts divided by NS, number of samples
		CSum = CSum / NN;
		
		return CSum;
	}

}

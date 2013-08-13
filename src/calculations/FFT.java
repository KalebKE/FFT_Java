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

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;


/**
 * A class that performs the Fast Fourier Transform.
 * @author Kaleb Kircher
 *
 */
public class FFT
{
	/**
	 * Overloaded method to generate signal values.
	 * 
	 * @return a double[] of signal values.
	 */
	public double[] generateSignal()
	{
		double[] signal =
		{ 2.28025, 1.32888, 0.39326, -0.49619, -1.31121, -2.02672, -2.62174,
				-3.08015, -3.39124, -3.55077, -3.55763, -3.42069, -3.15151,
				-2.76733, -2.28963, -1.74326, -1.15541, -0.55456, 0.03068,
				0.57271, 1.04606, 1.42835, 1.7122, 1.85105, 1.86948, 1.75376,
				1.50688, 1.13742, 0.65924, 0.09094, -0.54489, -1.22254,
				-1.91419, -2.59102, -3.22433, -3.78672, -4.25312, -4.60188,
				-4.81556, -4.8817, -4.79336, -4.54939, -4.15456, -3.61943,
				-2.95996, -2.19702, -1.35554, -0.46368, 0.44824, 1.34888,
				2.20699, 2.99264, 3.6783, 4.23987, 4.65761, 4.91685, 5.00855,
				4.92967, 4.68323, 4.27822, 3.72929, 3.05615, 2.28286, 1.43692,
				0.54824, -0.35197, -1.23239, -2.06272, -2.81485, -3.46385,
				-3.9889, -4.37404, -4.60875, -4.68828, -4.61378, -4.39223,
				-4.03611, -3.56286, -2.99417, -2.3551, -1.67308, -0.97682,
				-0.29515, 0.3441, 0.91522, 1.3956, 1.76664, 2.01448, 2.13052,
				2.1118, 1.96104, 1.68661, 1.30214, 0.826, 0.28055, -0.3087,
				-0.91416, -1.50721, -2.05935, -2.54337, -2.93442, -3.21099,
				-3.35583, -3.35666, -3.20667, -2.90487, -2.45619, -1.87128,
				-1.16626, -0.36208, 0.51622, 1.44039, 2.38004, 3.30375,
				4.18023, 4.97949, 5.67398, 6.23957, 6.65652, 6.91015, 6.99145,
				6.89738, 6.631, 6.20138, 5.62319, 4.91623, 4.10463, 3.216};
		return signal;
	}
	/**
	 * Overload method to generate a signal based on a function.
	 * @param Number of signal points to be generated.
	 * @return a double[] of signal values.
	 */
	public double[] generateSignal(int N)
	{
			double[] signal = new double[N];
			for (int i = 0; i < N; i++)
			{
				signal[i] = Math.cos((2 * i * 1 * Math.PI) / N) + 0.5
						* Math.cos(((2 * i * 2 * Math.PI) / N) + Math.PI / 4);
			}
		return signal;
	}

	/**
	 * Main method calculates the coefficients and phase. 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// boot strap 
		FFT calc = new FFT();
		double[] signal = calc.generateSignal();
		
		// instantiate classes to perform calculations
		CalcReal calcReal = new CalcReal();
		CalcImag calcImag = new CalcImag();
		SumCoefficiants sumC = new SumCoefficiants();
		
		// size of table
		int N = signal.length;
		// samples per cycle
		int NN = signal.length;
		// harmonics
		int K = 4;

		// initialize arrays
		double[] amplitude = new double[K];
		double[] phase = new double[K];
		double[] cosSignal = new double[N];
		double[] degreesSignal = new double[N];
		double[] IMCSum = new double[K];
		double[] radiansSignal = new double[N];
		double[] RECSum = new double[K];
		double[][] IMC = new double[K][NN];
		double[][] REC = new double[K][NN];

		// generate the radians for the signal
		for (int i = 0; i < N; i++)
		{
			degreesSignal[i] = (360 / (double) N) * i;
			//System.out.println(degreesSignal[i]);
			radiansSignal[i] = degreesSignal[i] * (Math.PI / 180);
		}

		// get the real coefficients
		for (int i = 0; i < K; i++)
		{
			REC[i] = calcReal.calc(radiansSignal, signal, i + 1);
		}

		// sum the real coefficients
		for (int i = 0; i < K; i++)
		{
			RECSum[i] = (sumC.sum(REC[i], (double) NN));
			// System.out.println(RECSum[i]);
		}

		// get the imag coefficients
		for (int i = 0; i < K; i++)
		{
			IMC[i] = calcImag.calc(radiansSignal, signal, i + 1);
		}

		// sum the imag coefficients
		for (int i = 0; i < K; i++)
		{
			IMCSum[i] = (sumC.sum(IMC[i], (double) NN));
			// System.out.println(IMCSum[i]);
		}

		// calculate amplitude (Fourier coefficients)
		// a[i] = 2(abs(RECSum[i]*abs(IMCSum[i])
		for (int i = 0; i < K; i++)
		{
			amplitude[i] = 2 * (Math.sqrt(Math.pow(RECSum[i], 2)
					+ Math.pow(IMCSum[i], 2)));
			// System.out.println(amplitude[i]);
		}

		// calculate phase
		// atan(abs(b/a))
		for (int i = 0; i < K; i++)
		{
			double temp = (Math.sqrt(Math.pow(IMCSum[i], 2)
					/ Math.pow(RECSum[i], 2)));
			phase[i] = Math.atan(temp);
			// System.out.println(phase[i]);
		}
		
		Plot2DPanel plot = new Plot2DPanel();
		
		plot.addLinePlot("Signal", new Color(255,68,68), signal);
		plot.addLinePlot("Harmonic #1", new Color(51,181,229), REC[0]);
		plot.addLinePlot("Harmonic #2", new Color(153,204,0), REC[1]);
		plot.addLinePlot("Harmonic #3", new Color(255,187,51), REC[2]);
		plot.addLinePlot("Harmonic #4", new Color(170,102,204), REC[3]);
		
		  // define the legend position
        plot.addLegend("SOUTH");
		
		// put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("FFT");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);

		// produce output
		for (int i = 0; i < K; i++)
		{
			System.out.println("");
			System.out.println("REC" + (i + 1) + ": " + RECSum[i]);
			System.out.println("IMC" + (i + 1) + ": " + IMCSum[i]);
			System.out.println("Amplitude(K=" + (i + 1) + "): " + amplitude[i]);
			System.out.println("Phase(K=" + (i + 1) + "): " + phase[i]);
			System.out.println("Degrees(K=" + (i + 1) + "): " + phase[i]*(180/Math.PI));
			System.out.println("");
		}
	}
}

package numerics;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

public class RandomnessValidation {

  public static double normsinv(double probability) {
    // create an instance of NormalDistribution with mean 0 and standard deviation 1
    // (standard normal)
    NormalDistribution normalDist = new NormalDistribution(0, 1);
    // return the inverse cumulative probability (Z-score for the given probability)
    return normalDist.inverseCumulativeProbability(probability);
  }

  public static double chiinv(double probability, int degreesOfFreedom) {

    ChiSquaredDistribution chiSquareDist = new ChiSquaredDistribution(degreesOfFreedom);
    // return chiSquareDist.inverseCumulativeProbability(probability);

    // Excel method
    return chiSquareDist.inverseCumulativeProbability(1.0 - probability);
  }

  public static boolean meanTestValidation(ArrayList<Double> numbersList) {

    int n = numbersList.size();

    // Calculate sample mean
    double sampleMean = numbersList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

    // Population mean (assumed to be 0.5)
    double populationMean = 0.5;

    // Significance level
    double beta = 0.95;
    double alpha = 1 - beta;
    double alphaDiv2 = alpha / 2;

    // Z_alpha/2 for a 95% confidence level (standard value: 1.96)
    double Z_alpha_div_2 = normsinv(alphaDiv2);

    // Lower Limit (LI)
    double lowerLimit = populationMean - Math.abs(Z_alpha_div_2) * (1.0 / Math.sqrt(12.0 * n));

    // Upper Limit (LS)
    double upperLimit = populationMean + Math.abs(Z_alpha_div_2) * (1.0 / Math.sqrt(12.0 * n));

    // Check if the sample mean is within the acceptance range
    return (lowerLimit < sampleMean) && (sampleMean < upperLimit);
  }

  public static String getInfoMeanTestValidation(ArrayList<Double> numbersList) {

    int n = numbersList.size();

    double sampleMean = numbersList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    double populationMean = 0.5;

    double beta = 0.95;
    double alpha = 1 - beta;
    double alphaDiv2 = alpha / 2;

    double Z_alpha_div_2 = normsinv(alphaDiv2);

    double lowerLimit = populationMean - Math.abs(Z_alpha_div_2) * (1 / Math.sqrt(12 * n));
    double upperLimit = populationMean + Math.abs(Z_alpha_div_2) * (1 / Math.sqrt(12 * n));

    double sum = numbersList.stream().mapToDouble(Double::doubleValue).sum();
    double mean = sum / n;
    double varianceSum = 0.0;

    for (double num : numbersList) {
      varianceSum += Math.pow(num - mean, 2);
    }

    // https://support.microsoft.com/en-us/office/stdev-s-function-7d69cf97-0c1f-4acf-be27-f3e83904cc23
    double sampleVariance = varianceSum / (n - 1);
    double sampleStdDev = Math.sqrt(sampleVariance);

    // Information about the test
    String info = "Means Test [Uniformity]\n";
    info += String.format("Sample Mean (r): %.9f\n", sampleMean);
    info += String.format("Population Mean (mu): %.9f\n", populationMean);
    info += String.format("Significance Level (alpha): %.9f\n", alpha);
    info += String.format("Z_alpha/2: %.5f\n", Z_alpha_div_2);
    info += String.format("Lower Limit (LI): %.9f\n", lowerLimit);
    info += String.format("Upper Limit (LS): %.9f\n", upperLimit);
    info += String.format("Sample Standard Deviation (sigma): %.9f\n", sampleStdDev);
    info += String.format("Test Result (LI < r < LS): %s\n",
        (lowerLimit < sampleMean && sampleMean < upperLimit) ? "Accepted" : "Rejected");

    return info;
  }

  public static boolean varianceTestValidation(ArrayList<Double> numbersList) {

    int n = numbersList.size();

    double sampleMean = numbersList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

    // sample variance (S^2)
    double varianceSum = 0.0;
    for (double num : numbersList) {
      varianceSum += Math.pow(num - sampleMean, 2);
    }
    double sampleVariance = varianceSum / (n - 1);

    // Significance level
    double beta = 0.95;
    double alpha = 1 - beta;
    double alphaDiv2 = alpha / 2;

    // Chi-square critical values
    double chi2_alpha_div_2_n_minus_1 = chiinv(alphaDiv2, n - 1);
    double chi2_1_minus_alpha_div_2_n_minus_1 = chiinv(beta, n - 1);

    // Calculate the lower and upper limits
    double lowerLimit = chi2_1_minus_alpha_div_2_n_minus_1 / (12 * (n - 1));
    double upperLimit = chi2_alpha_div_2_n_minus_1 / (12 * (n - 1));

    // Validate if the sample variance is within the limits
    return (lowerLimit < sampleVariance) && (sampleVariance < upperLimit);
  }

  public static String getInfoVarianceTestValidation(ArrayList<Double> numbersList) {

    int n = numbersList.size();

    double sampleMean = numbersList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

    double varianceSum = 0.0;
    for (double num : numbersList) {
      varianceSum += Math.pow(num - sampleMean, 2);
    }
    double sampleVariance = varianceSum / (n - 1);

    double beta = 0.95;
    double alpha = 1 - beta;
    double alphaDiv2 = alpha / 2;

    double chi2_alpha_div_2_n_minus_1 = chiinv(alphaDiv2, n - 1);
    double chi2_1_minus_alpha_div_2_n_minus_1 = chiinv(beta, n - 1);

    double lowerLimit = chi2_1_minus_alpha_div_2_n_minus_1 / (12 * (n - 1));
    double upperLimit = chi2_alpha_div_2_n_minus_1 / (12 * (n - 1));

    // Build information string
    String info = "Variance Test [Uniformity]\n";
    info += String.format("Sample Mean (r):  %.9f\n", sampleMean);
    info += String.format("Sample Variance (S^2):  %.9f\n", sampleVariance);
    info += String.format("Significance Level (α): %.9f\n", alpha);
    info += String.format("Chi-square critical value for alpha/2   : %.9f\n", chi2_alpha_div_2_n_minus_1);
    info += String.format("Chi-square critical value for 1-alpha/2 : %.9f\n", chi2_1_minus_alpha_div_2_n_minus_1);
    info += String.format("Lower Limit (LI): %.9f\n", lowerLimit);
    info += String.format("Upper Limit (LS): %.9f\n", upperLimit);
    info += String.format("Test Result (LI<S2<LS): %s\n",
        (lowerLimit < sampleVariance && sampleVariance < upperLimit) ? "Accepted" : "Rejected");

    return info;
  }

  public static boolean corridaTestValidation(ArrayList<Double> numbersList) {
    
    int n = numbersList.size();

    //Construct ArrayList1 of 0s and 1s
    ArrayList<Integer> arrayList1 = new ArrayList<>();
    for (int i = 1; i < n; i++) {
      if (numbersList.get(i) > numbersList.get(i - 1)) {
        arrayList1.add(1); // r[i] > r[i-1], add 1
      } else {
        arrayList1.add(0); // r[i] <= r[i-1], add 0
      }
    }

    //Construct ArrayList2 of 0s and 1s based on arrayList1
    ArrayList<Integer> arrayList2 = new ArrayList<>();
    arrayList2.add(1); // Start with 1 because there's no previous element to compare
    for (int i = 1; i < arrayList1.size(); i++) {
      if (arrayList1.get(i).equals(arrayList1.get(i - 1))) {
        arrayList2.add(1); // Sequence is the same, add 1
      } else {
        arrayList2.add(0); // Sequence is different, add 0
      }
    }

    // Calculate the number of 0s
    int C_0 = 0;
    for (int value : arrayList2) {
      if (value == 0) {
        C_0++;
      }
    }

    double mu_c = (2 * n - 1) / 3.0;

    // Significance level
    double beta = 0.95;
    double alpha = 1 - beta;
    double alphaDiv2 = alpha / 2;

    double Z_alpha_div_2 = Math.abs(normsinv(alphaDiv2));

    double sigma_c_squared = (16 * n - 29) / 90.0;

    double Z_hat = Math.abs((C_0 - mu_c) / Math.sqrt(sigma_c_squared));

    return Z_hat <= Z_alpha_div_2;
  }

  public static String getInfoCorridaTestValidation(ArrayList<Double> numbersList) {

    int n = numbersList.size();

    // Step 1: Construct ArrayList1 of 0s and 1s
    ArrayList<Integer> arrayList1 = new ArrayList<>();
    for (int i = 1; i < n; i++) {
      if (numbersList.get(i) > numbersList.get(i - 1)) {
        arrayList1.add(1); // r[i] > r[i-1], add 1
      } else {
        arrayList1.add(0); // r[i] <= r[i-1], add 0
      }
    }

    // Step 2: Construct ArrayList2 of 0s and 1s based on arrayList1
    ArrayList<Integer> arrayList2 = new ArrayList<>();
    arrayList2.add(1); // Start with 1 because there's no previous element to compare
    for (int i = 1; i < arrayList1.size(); i++) {
      if (arrayList1.get(i).equals(arrayList1.get(i - 1))) {
        arrayList2.add(1); // Sequence is the same, add 1
      } else {
        arrayList2.add(0); // Sequence is different, add 0
      }
    }

    // Step 3: Calculate the number of 0s in arrayList2 (C_0)
    int C_0 = 0;
    for (int value : arrayList2) {
      if (value == 0) {
        C_0++;
      }
    }

    // Step 4: Mean of the number of runs (mu_c)
    double mu_c = (2 * n - 1) / 3.0;

    // Step 5: Significance level
    double beta = 0.95;
    double alpha = 1 - beta;
    double alphaDiv2 = alpha / 2;

    // Step 6: Z_alpha/2 for the 95% confidence level
    double Z_alpha_div_2 = Math.abs(normsinv(alphaDiv2));

    // Step 7: Variance of the number of runs (sigma_c^2)
    double sigma_c_squared = (16 * n - 29) / 90.0;

    // Step 8: Calculate Z_hat
    double Z_hat = Math.abs((C_0 - mu_c) / Math.sqrt(sigma_c_squared));

    // Step 9: Build detailed information
    String info = "Corrida Test [Independence]\n";
    info += String.format("Number of runs (C_0): %d\n", C_0);
    info += String.format("Sample Mean of runs (mu_c): %.9f\n", mu_c);
    info += String.format("Variance of the number of runs (sigma_c^2): %.9f\n", sigma_c_squared);
    info += String.format("Z_alpha/2: %.9f\n", Z_alpha_div_2);
    info += String.format("Z_hat: %.9f\n", Z_hat);
    info += String.format("Test Result (Z_hat <= Z_alpha/2): %s\n", (Z_hat <= Z_alpha_div_2) ? "Accepted" : "Rejected");

    return info;
  }

}





/*
 * 
 * Means Test [Uniformity]
 * Variance Test [Uniformity]
 * Corrida Test [Independence]
 * 
 * 
 * --Mean Test
 * The expected value (population mean) is assumed to be 0.5.
 * A 95% confidence level is considered.
 * H_0: 𝜇 = 0.5
 * H_1: 𝜇 ≠ 0.5
 * 
 * --Sample Mean
 * n = numbers_list.length()
 * sample_mean = r = sum(numbers_list)/n = average(numbers_list)
 * sample_mean = r = 0.519138
 * 
 * --Population Mean (Assumed)
 * population_mean = mu = 0.5
 * 
 * --Significance Level
 * beta = 0.95
 * alpha = 1 - beta
 * alpha_div_2 = alpha/2
 * Z_alpha_div_2 = NORMSINV(alpha_div_2) //returns the inverse of the standard
 * normal cumulative distribution for a given probability.
 * 
 * --Lower Limit (LI)
 * lower_limit = 0.5 - abs(Z_alpha_div_2) *(1 / sqrt(12*n))
 * 
 * --Upper Limit (LS)
 * upper_limit = 0.5 + abs(Z_alpha_div_2) * (1 /sqrt(12 * n))
 * 
 * -- Standard Deviation
 * sd = STDEV.S(numbers_list)
 * 
 * -- Conditions for accepting random numbers
 * LI < r && r < LS
 * 
 * 
 * 
 * -- Variance Test
 * The expected variance value is 1/12.
 * A 95% confidence level is considered.
 * H_0:𝜎^2=1/12
 * H_1:𝜎^2 ≠ 1/12
 * 
 * -- Sample Mean
 * n = numbers_list.length()
 * sample_mean = r = sum(numbers_list) / n
 * 
 * -- Sample Variance (S^2)
 * sample_variance = S2 = sum((r_i - sample_mean)**2 for r_i in numbers_list) /
 * (n - 1)
 * 
 * -- Significance Level
 * beta = 0.95
 * alpha = 1 - beta
 * alpha_div_2 = alpha/2
 * Z_alpha_div_2 = NORMSINV(alpha_div_2) //returns the inverse of the standard
 * normal cumulative distribution for a given probability.
 * 
 * -- Chi-Square critical values
 * chi2_alpha_div_2_n_minus_1 = CHIINV(alpha_div_2 , n-1) # Chi-square value for
 * alpha/2 and n-1 degrees of freedom
 * chi2_1_minus_alpha_div_2_n_minus_1 = CHIINV(beta,50-1) # Chi-square value for
 * 1-alpha/2 and n-1 degrees of freedom
 * 
 * -- Lower Limit (LI)
 * lower_limit = (chi2_1_minus_alpha_div_2_n_minus_1) / (12 * (n - 1))
 * 
 * -- Upper Limit (LS)
 * upper_limit = (chi2_alpha_div_2_n_minus_1) / (12 * (n - 1))
 * 
 * -- Conditions for accepting random numbers
 * LI < S2 && S2 < LS
 * 
 * 
 * 
 * 
 */
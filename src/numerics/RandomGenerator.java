package numerics;

import java.util.ArrayList;
import java.util.Random;

public class RandomGenerator {

  private int totalNumbers;
  private long seed;
  private double lowerBound;
  private double upperBound;
  private ArrayList<Double> randomNumbers;
  private Random random;

  //custom modulus function
  public double mod(double x, double y) {
    return x - y*Math.floor(x/y);
  }

  public RandomGenerator(long seed, int totalNumbers, double lowerBound, double upperBound) {

    this.seed = seed;
    this.totalNumbers = totalNumbers;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.random = new Random(seed);

    return;
  }

  public RandomGenerator(long seed, int totalNumbers) {
    this(seed, totalNumbers, 0.0, 1.0);
  }

  public ArrayList<Double> generateWithJavaRandom() {

    randomNumbers = new ArrayList<>();

    for (int i = 0; i < totalNumbers; i++) {
      double randomValue = lowerBound + (upperBound - lowerBound) * random.nextDouble();
      randomNumbers.add(randomValue);
    }

    return randomNumbers;
  }

  public ArrayList<Double> generateWithJavaRandom(double min, double max) {

    randomNumbers = new ArrayList<>();
    this.lowerBound = min;
    this.upperBound = max;

    for (int i = 0; i < totalNumbers; i++) {
      double randomValue = min + (max - min) * random.nextDouble();
      randomNumbers.add(randomValue);
    }

    return randomNumbers;
  }

  public ArrayList<Double> generateWithJavaRandom(long seed, int totalNumbers, double min, double max) {

    this.seed = seed;
    this.totalNumbers = totalNumbers;
    this.lowerBound = min;
    this.upperBound = max;

    randomNumbers = new ArrayList<>();
    this.random = new Random(seed);

    for (int i = 0; i < totalNumbers; i++) {
      double randomValue = min + (max - min) * random.nextDouble();
      randomNumbers.add(randomValue);
    }

    return randomNumbers;
  }

  public ArrayList<Double> generateWithMiddleSquare() {

    randomNumbers = new ArrayList<>();
    long currentSeed = seed;

    for (int i = 0; i < totalNumbers; i++) {
      // Square the current seed
      long square = currentSeed * currentSeed;
      String squareStr = Long.toString(square);

      while (squareStr.length() < 8) {
        squareStr = "0" + squareStr;
      }

      // If the square has more than 8 digits, handle the situation as per your
      if (squareStr.length() > 8) {

        // Make the number of digits even by padding with leading zeros
        while (squareStr.length() % 2 != 0) {
          squareStr = "0" + squareStr;
        }

        // Calculate the starting index based on (seed / 2 - 2)
        int startIdx = (squareStr.length() / 2) - 2;
        squareStr = squareStr.substring(startIdx, startIdx + 4);

      } else {
        // Otherwise, just take the middle 4 digits
        int startIdx = (squareStr.length() / 2) - 2;
        squareStr = squareStr.substring(startIdx, startIdx + 4);
      }

      // Update the seed for the next iteration
      currentSeed = Long.parseLong(squareStr);

      // Convert the middle digits to a double in the range [0.0, 1.0] and add to the
      // list
      double randomValue = currentSeed / 10000.0;
      randomNumbers.add(randomValue);
    }

    return randomNumbers;
  }

  public ArrayList<Double> generateWithMiddleSquare(long seed, int totalNumbers) {

    this.seed = seed;
    this.totalNumbers = totalNumbers;

    randomNumbers = new ArrayList<>();
    long currentSeed = seed;

    for (int i = 0; i < totalNumbers; i++) {
      // Square the current seed
      long square = currentSeed * currentSeed;
      String squareStr = Long.toString(square);

      while (squareStr.length() < 8) {
        squareStr = "0" + squareStr;
      }

      // If the square has more than 8 digits, handle the situation as per your
      if (squareStr.length() > 8) {

        // Make the number of digits even by padding with leading zeros
        while (squareStr.length() % 2 != 0) {
          squareStr = "0" + squareStr;
        }

        // Calculate the starting index based on (seed / 2 - 2)
        int startIdx = (squareStr.length() / 2) - 2;
        squareStr = squareStr.substring(startIdx, startIdx + 4);

      } else {
        // Otherwise, just take the middle 4 digits
        int startIdx = (squareStr.length() / 2) - 2;
        squareStr = squareStr.substring(startIdx, startIdx + 4);
      }

      // Update the seed for the next iteration
      currentSeed = Long.parseLong(squareStr);

      // Convert the middle digits to a double in the range [0.0, 1.0] and add to the
      // list
      double randomValue = currentSeed / 10000.0;
      randomNumbers.add(randomValue);
    }

    return randomNumbers;
  }

  public ArrayList<Double> generateWithLehmerLCG() {
    
    randomNumbers = new ArrayList<>();
    final double MOD = Math.pow(2, 31) - 1; // 2^31 - 1
    double s_k = seed;

    for (int i = 0; i < totalNumbers; i++) {

      //calculate s_k+1
      double s_k_plus_1 = mod(48271*s_k, MOD);
      double a_k = s_k_plus_1/MOD;

      //add the normalized number to the list
      randomNumbers.add(a_k);

      //update the seed for the next iteration
      s_k = s_k_plus_1;
    }

    return randomNumbers;
  }

  public ArrayList<Double> generateWithLehmerLCG(long seed, int totalNumbers) {
    
    this.seed = seed;
    this.totalNumbers = totalNumbers;

    randomNumbers = new ArrayList<>();
    final double MOD = Math.pow(2, 31) - 1; // 2^31 - 1
    double s_k = seed;

    for (int i = 0; i < totalNumbers; i++) {

      //calculate s_k+1
      double s_k_plus_1 = mod(48271*s_k, MOD);
      double a_k = s_k_plus_1 / MOD;

      //add the normalized number to the list
      randomNumbers.add(a_k);

      //update the seed for the next iteration
      s_k = s_k_plus_1;
    }

    return randomNumbers;
  }

  public int gettotalNumbers() {
    return totalNumbers;
  }

  public void settotalNumbers(int totalNumbers) {
    this.totalNumbers = totalNumbers;
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
    this.random = new Random(seed);
  }

  public double getLowerBound() {
    return lowerBound;
  }

  public void setLowerBound(double lowerBound) {
    this.lowerBound = lowerBound;
  }

  public double getUpperBound() {
    return upperBound;
  }

  public void setUpperBound(double upperBound) {
    this.upperBound = upperBound;
  }

  // You can later implement other random number generation methods here like:
  // 1. Middle-Square Method
  // 2. Lehmer RNG Method
}

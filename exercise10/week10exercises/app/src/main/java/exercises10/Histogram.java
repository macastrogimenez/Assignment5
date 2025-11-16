package exercises10;

// first version by Kasper modified by jst@itu.dk 24-09-2021
// raup@itu.dk * 05/10/2022
// jst@itu.dk * 08/20/2025

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public int getSpan();
}

package example;

import java.util.Arrays;

public class QuickSort {

  public static int[] sort(int[] unsortedArray) {
    return sort(unsortedArray, 0, unsortedArray.length);
  }

  private static int[] sort(int[] unsortedArray, int start, int end) {
    if (unsortedArray.length <= 1 || end - start <= 1) return unsortedArray;

    if (start > end || start > unsortedArray.length || end > unsortedArray.length)
      throw new IllegalArgumentException("start: " + start + ", end: " + end + ", len: " + unsortedArray.length);

    //partition
    int pivotPos = start;
    int leftEdge = pivotPos + 1;
    for (int i = leftEdge; i < end; i++) {
      if (unsortedArray[i] <= unsortedArray[pivotPos]) {
        int tmpVal = unsortedArray[i];
        unsortedArray[i] = unsortedArray[leftEdge];
        unsortedArray[leftEdge] = tmpVal;
        leftEdge++;
      }
    }
    --leftEdge;
    int tmpVal = unsortedArray[leftEdge];
    unsortedArray[leftEdge] = unsortedArray[pivotPos];
    unsortedArray[pivotPos] = tmpVal;

    //recursively call on each half of the partitioned array
    sort(unsortedArray, start, leftEdge);
    sort(unsortedArray, leftEdge + 1, end);

    return unsortedArray;
  }
}

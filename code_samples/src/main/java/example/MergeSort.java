package example;

import java.util.Arrays;

public class MergeSort {

  public static int[] sort(int[] unsortedArray) {
    if (unsortedArray.length <= 1) return unsortedArray;

    //divide
    int len = unsortedArray.length;
    int half = len / 2;
    int[] left = Arrays.copyOfRange(unsortedArray, 0, half);
    int[] right = Arrays.copyOfRange(unsortedArray, half, len);

    //conquer/merge
    return
        merge(
            sort(left),
            sort(right));
  }

  private static int[] merge(int[] left, int[] right) {
    int[] result = new int[left.length + right.length];
    int leftPos = 0;
    int rightPos = 0;

    for (int resultPos = 0; resultPos < result.length; resultPos++) {
      if (leftPos < left.length && rightPos < right.length) {
        if (left[leftPos] <= right[rightPos]) {
          result[resultPos] = left[leftPos++];
        } else {
          result[resultPos] = right[rightPos++];
        }
      } else if (leftPos < left.length) {
        result[resultPos] = left[leftPos++];
      } else if (rightPos < right.length) {
        result[resultPos] = right[rightPos++];
      }
    }
    return result;
  }
}

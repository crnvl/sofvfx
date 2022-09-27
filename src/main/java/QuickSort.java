public class QuickSort {
    public static void sort(int[] unsorted) {
        recursive(unsorted, 0, unsorted.length - 1);
    }

    private static void recursive(int[] unsorted, int low, int high) {
        if (low >= high)
            return;

        int pivot = partition(unsorted, low, high);
        recursive(unsorted, low, pivot - 1);
        recursive(unsorted, pivot + 1, high);
    }

    private static int partition(int[] unsorted, int low, int high) {
        int pivot = unsorted[high];

        int i = low;
        int j = high - 1;
        while (i < j) {
            while (unsorted[i] < pivot)
                i++;

            while (j > low && unsorted[j] >= pivot)
                j--;

            if (i < j) {
                int temp = unsorted[i];
                unsorted[i] = unsorted[j];
                unsorted[j] = temp;

                j++;
                i++;
            }
        }

        if (i == j && unsorted[i] < pivot)
            i++;

        if (unsorted[i] != pivot) {
            int temp = unsorted[i];
            unsorted[i] = unsorted[high];
            unsorted[high] = temp;
        }
        return i;
    }
}

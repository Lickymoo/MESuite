package com.buoobuoo.mesuite.meutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class JavaUtils {

    public static String[] stringToArr(String str){
        return str.split(",");
    }

    public static String arrToString(String[] arr){
        StringBuilder sb = new StringBuilder();
        for(String str : arr){
            sb.append(str);
            sb.append(",");
        }
        return sb.toString();
    }

    public static <T> T[] concatenateArrays(T[] a, T[] b){
        List<T> list = new ArrayList<>();
        list.addAll(List.of(a));
        list.addAll(List.of(b));

        return list.toArray(a);
    }

    public static <T> T[] setArrSize(T[] arr, int size){
        return Arrays.copyOf(arr, size);
    }

    public static String[] padEmpty(String[] arr, int size){
        String[] ret = new String[size];
        for(int i = 0; i < arr.length; i++){
            ret[i] = arr[i];
        }
        for(int i = arr.length; i < size; i++){
            ret[i] = "";
        }
        return ret;
    }

    public static String[] insertEmptyStr(String[] arr, String val) {
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null ||  isEmpty(arr[i]) || arr[i].equals("null")) {
                arr[i] = val;
                return arr;
            }
        }
        return arr;
    }

    public static <T> T[] addToArr(T[] arr, T... str){
        List<T> list = new ArrayList<>();
        list.addAll(Arrays.asList(arr));
        list.addAll(Arrays.asList(str));
        return list.toArray(arr);
    }

    public static <T> T[] addToBeginningOfArray(T[] elements, T element)
    {
        T[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, elements.length);

        return newArray;
    }

    public static String fromList(List<String> strList){
        StringBuilder builder = new StringBuilder();
        for(String str : strList){
            builder.append(str);
            builder.append(",");
        }
        return builder.toString();
    }

    public static String formatDouble(double value) {
        String[] suffixes = {"K", "M", "B", "T", "Qad", "Qin", "Sext", "Sept", "Oct", "Non", "Dec", "Und", "Duod", "Tred", "Quat", "Quind", "Sexd", "Septe", "Octo", "Nov", "Vigin"};
        if(value > 1000) {
            for (int pwr = 3; pwr <= 63; pwr += 3) {
                int index = (pwr / 3) - 1;

                double powVal = Math.pow(10, pwr);
                if (value < powVal) {
                    String str = String.format("%.2f" + suffixes[index - 1], value / Math.pow(10, pwr - 3));
                    str = str.replace(".00", "");
                    return str;
                }
            }
        }

        String str = String.format("%.0f", value);
        str = str.replace(".00", "");
        return str;
    }

    public static float clamp(float val, float min, float max){
        if(Float.isNaN(val))
            return 0;

        if(val < min)
            return min;
        if(val > max)
            return max;

        return val;
    }

    public static String[] removeOccurrences(String[] arr, String str){
        List<Integer> marks = new ArrayList<>();
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null)
                continue;

            if(arr[i].equals(str))
                marks.add(i);
        }

        for(int i : marks){
            arr[i] = null;
        }
        return arr;
    }

    public static <E extends Enum<E>> List<String> getAllList(Class<E> clazz){
        List<String> ret = new ArrayList<>();
        for(Enum<E> t : clazz.getEnumConstants())
        {
            ret.add(t.toString());
        }
        return ret;
    }

    public static int randomInt(int min, int max){
        if(min == max) return min;
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static double randomDouble(double min, double max){
        if(min == max) return min;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static boolean isEmpty(String str){
        if(str == null)
            return true;
        if(str.isEmpty())
            return true;
        if(str.isBlank())
            return true;

        return false;
    }
}

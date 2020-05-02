package assigement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public class GCD {

    public static int GCD(int m, int n) {
        while (n != 0) {
            n = m % n;
        }
        return m;

    }


    public static int PrimeGCD(int m, int n) {
        int result = 1;
        Set<Integer> set1 = getFactor(m);
        Set<Integer> set2 = getFactor(n);
        set1.retainAll(set2);
        result = Collections.max(set1);
        return result;
    }


    public static int equalGCD(int m, int n) {
        while (m != n) {
            if (m > n)
                m = m - n;
            else
                n = n - m;
        }
        return m;
    }


    private static Set<Integer> getFactor(int m) {
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 2; i <= m; i++) {
            if (m % i == 0) {
                set.add(i);
            }
        }
        return set;
    }

    public static void main(String[] args) {
        int result = equalGCD(32, 48);
        System.out.println(result);
    }
}

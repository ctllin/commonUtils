package com.ctl.utils;
/**
 * @author ctl
 *
 */
public class CheckCardNumUtil {
	public static boolean checkcard(String banknumber) {
		String last = banknumber.substring(banknumber.length() - 1);
		String front_last = banknumber.substring(0, banknumber.length() - 1);
		StringBuilder front_arr = new StringBuilder(front_last);
		front_arr.reverse();
		int sum1, sum2, sum3;
		sum1 = sum2 = sum3 = 0;
		for (int j = 0; j < front_arr.length(); j++) {
			if ((j + 1) % 2 == 1) {//
				if (Integer.parseInt(new String().valueOf(front_arr.charAt(j))) * 2 < 9) {
					sum1 += Integer.parseInt(new String().valueOf(front_arr
							.charAt(j))) * 2;
				} else {
					int str = Integer.parseInt(new String().valueOf(front_arr
							.charAt(j))) * 2;
					int str1 = 1;
					int str2 = str % 10;
					sum2 += str1;
					sum2 += str2;
				}
			} else {
				sum3 += Integer.parseInt(new String().valueOf(front_arr
						.charAt(j)));
			}
		}
		int sum = sum1 + sum2 + sum3;
		int luhn = sum % 10 == 0 ? 0 : 10 - sum % 10;
		if (luhn == Integer.parseInt(last)) {
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args) {//6217855000008856858
		System.out.println(checkcard("6217855000008856858"));
		System.out.println(checkcard("6210984980008021117"));
		System.out.println(checkcard("6205137600000007098"));

	}
}


class IdCard {
	public static String getCardLastNum(String cardNo) {
		if (cardNo.length() < 17)
			return "Length error";
		int[] checkFactor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4,
				2 };
		String[] nums = cardNo.split("");
		int count = 0;
		for (int i = 0; i < 17; i++) {
			count += Integer.valueOf(nums[i]) * checkFactor[i];
		}
		int a = 12 - count % 11;
		if (a == 10) {
			return "X";
		} else if (a > 10) {
			return String.valueOf(a - 11);
		} else {
			return String.valueOf(a);
		}
	}
}

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class StringUtils {

	private static List<String> strList = new ArrayList<String>();

	public static List<String> getStrList(String str, String startStr,
			String endStr) {
		int pos = str.indexOf(startStr);
		if (pos != -1) {
			str = str.substring(pos + startStr.length());
			pos = str.indexOf(endStr);
			if (pos != -1) {
				strList.add(str.substring(0, pos));
				getStrList(str.substring(pos + endStr.length()), startStr,
						endStr);
			}
		}
		return strList;
	}

	public static List<String> getStrList(String str, String startStr,
			String endStr, boolean caseSensitive) {
		if (!caseSensitive) {
			str = str.replace("\n", "").toLowerCase();
			startStr = startStr.toLowerCase();
			endStr = endStr.toLowerCase();
		}
		return getStrList(str, startStr, endStr);
	}
	
	public static String listToStr(List list) {
		StringBuffer sb = new StringBuffer();
		for(Object o : list){
			sb.append(o).append("\n");
		}
		return sb.toString();
	}
	//"C:\\Users\\whaty\\Desktop\\2.txt"
	public static String concatOracleIn(String filePath){
		String oStr = FileUtils.readFile(new File(filePath));
		String[] strs = oStr.split("\r");
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< strs.length; i++){
			sb.append(" update sso_user set flag_isvalid = '3' WHERE login_id = '");
			sb.append(strs[i].replace("\n", "")).append("';\n");
		}
		return sb.toString();
	}
}

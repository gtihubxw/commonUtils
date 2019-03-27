import java.text.ParseException;
import java.text.SimpleDateFormat;


class Time {
	/**
	 * 
	 * @param updateDate
	 * @param timeInterval 毫秒间隔
	 * @return
	 */
	public static boolean isOverInterval(String lastDate,long timeInterval){
//		long timeInterval = 30*60*1000; //计次时间间隔(毫秒)30min
		if(lastDate == null){
			return true;
		}
		long nowMillis = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long lastMillis = sdf.parse(lastDate).getTime();
			if(nowMillis - lastMillis > timeInterval){
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
}

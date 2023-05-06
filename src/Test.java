import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Test().test();
	}
	
	public static String getStringR(String siteCode){
		return siteCode.replace("_", "")+getCurDate()+randomString(10);
	}
	public static final String dtShort = "yyyyMMdd";
	/**
     * 获得一个格式为yyyyMMdd的当前日期
     * */
    public static String getCurDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShort);
		return df.format(date);    	
    }
	/**
	 * 获得一个100位的字符串
	 * */
    public static String randomString(long length) {
        int len = (int) length;
        //System.out.println("come in le~$$#####54555555555555555");
        Random randGen = null;
        char[] numbersAndLetters = null;
        Object initLock = new Object();
        if (len < 1) {
            return null;
        }
        if (randGen == null) {
            synchronized (initLock) {
                if (randGen == null) {
                    randGen = new Random();
                    numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
                            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
                    //numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
                }
            }
        }
        char[] randBuffer = new char[len];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
            //randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
        }
        return new String(randBuffer);
    }
	
	
	
	
	public static List<String> getImageSrc(String htmlCode) {
	    List<String> imageSrcList = new ArrayList<String>();
	    Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
	    Matcher m = p.matcher(htmlCode);
	    String quote = null;
	    String src = null;
	    while (m.find()) {
	        quote = m.group(1);
	        // src=https://sms.reyo.cn:443/temp/screenshot/zY9Ur-KcyY6-2fVB1-1FSH4.png
	        src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
	        imageSrcList.add(src);
	 
	    }
	    return imageSrcList;
	}
	
	public void makeImage(String urlRoot, String resPath, String filePath) {
		// 网络请求所需变量
		try {
			// 获取输入流
			BufferedInputStream in = new BufferedInputStream(new URL(urlRoot
					+ resPath).openStream());
			// 创建文件流
			File file = new File(filePath + resPath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				file = new File(filePath + resPath);
			}
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(file));
			// 缓冲字节数组
			byte[] data = new byte[1024];
			/*int length = in.read(data);
			while (length != -1) {
				out.write(data, 0, data.length);
				length = in.read(data);
			}*/
			int len = 0;
            while((len = in.read(data)) >0){
            	out.write(data, 0, len);
            	out.flush();
            }
			//System.out.println("正在执行下载任务：当前正在下载图片" + resPath);
			in.close();
			out.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String listToSqlIn(List<String> list) {
		StringBuffer condition = new StringBuffer();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			condition.append("(");
			for (String id : list) {
				condition.append("'").append(id).append("',");
			}
			condition.delete(condition.length() - 1,condition.length()).append(")");
		}else{
			condition.append("('')");
		}
		return condition.toString();
	}
	
	public static String concatComboSQL(String str){
		String[] strs = str.split(",");
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<strs.length; i++){
			sb.append("{").append(strs[i]).append(",").append(strs[i])
					.append("}-");
		}
		sb.delete(sb.length()-1, sb.length());
		return sb.toString();		
	}
	
	public static String concatExamAddUrl(String str) {
		String[] strs = str.split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append("'bean.peGelExamPlan.name=")
			.append("机考2018B")
			.append("&bean.peTchCourse.name=")
			.append(strs[i])
			.append("&bean.computerExamName=")
			.append("机考2018B")
			.append(strs[i])
			.append("',");
		}
		return sb.toString();
	}
	
	public static void copyFileByBufferSteam() {
		File iFile = new File("D:\\kjChange\\presentation.mp4");
		File oFile = new File("D:\\kjChange\\haha.mp4");
		int len = 0;
		try {
			BufferedInputStream bufInputStream = new BufferedInputStream(
					new FileInputStream(iFile)); // 缓冲输入流
			BufferedOutputStream bufOutputStream = new BufferedOutputStream(
					new FileOutputStream(oFile)); // 缓冲输出流
			int i = -1;
			byte[] data = new byte[10240];
			while ((i = bufInputStream.read(data, 0, data.length)) != -1) {
				len += i;
				bufOutputStream.write(data, 0, i);
			}
			System.out.println(len);
			bufOutputStream.flush();
			bufInputStream.close();
			bufOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test(){
		String[] S = new String[5];
		S[0] = "1";
		S[1] = "2";
		System.out.println(S[1]);
		
	}

}

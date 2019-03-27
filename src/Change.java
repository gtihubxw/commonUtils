import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Change {

	/**
	 * @param args
	 */
	// 源文件标签未设置取此处默认值
	private final int frameRate = 25;// 帧率 <frameRate>[帧率]</tTime>标签
	private final String tTime = "16:01:10";// 总时长 <tTime>[总时长]</tTime>标签
	private final static String section = "01";
	private final String screen = "/videoForSeg/yunPan/201903/whatycloud/33/a7/33a77150-4f6a-11e9-ab55-39873601a95d.asf.VIDEOSEGMENTS/meta.json";
	private final String presentation = "/videoForSeg/yunPan/201809/whatycloud/61/86/6186e010-c45a-11e8-bf94-0fefd14e2d4a.asf.VIDEOSEGMENTS/meta.json";
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// int frameRate = 25;//帧率
		Change change = new Change();
//		String str = change.readFile(new File("F:/vedio/11002-技术经济学/"+section+"/frmleftdown.xml"));
		String str = change.readFile(new File("F:/财务管理/财务管理-吴秋霜（合成）/frmleftdown.xml"));
		// System.out.println(str);
		Map map = change.xmlToMap(str);
		change.outFiles(map);
		System.out.println("complete");
		// change.outWhatyPresentation(map);
	}

	private String readFile(File file) {
		StringBuilder result = new StringBuilder();
		BufferedReader br = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			br = new BufferedReader(read);
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result.toString().trim();
	}

	private void writeFile(String str, String fileName) {
		File file = new File("D:" + File.separator + "kjChange"
				+ File.separator + "out" + File.separator + fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			output.write(str.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Map xmlToMap(String str) {
		Map<String, Object> map = new HashMap<String, Object>();
		String totalSec = this.tTime;
		if (str.indexOf("<tTime>") != -1) {
			totalSec = str.substring(str.indexOf("<tTime>") + 7,
					str.indexOf("</tTime>"));
		}
		String title = str.substring(str.indexOf("<title>") + 17,
				str.indexOf("</title>") - 10);
		String author = str.substring(str.indexOf("<author>") + 18,
				str.indexOf("</author>") - 10);
		String indextree = str.substring(str.indexOf("<indextree>") + 13,
				str.indexOf("</indextree>") - 2);
		//分章节单章处理
		//indextree = "<chapter>" + indextree + "</chapter>,";
		String[] chapters = indextree.split("</chapter>");
		List list = new ArrayList();
		for (int i = 0; i < chapters.length - 1; i++) {
			List<Map<String, Object>> chapterList = new ArrayList<Map<String, Object>>();
			String chapter = chapters[i];
			chapter = chapters[i].substring(chapter.indexOf("<index"));
			String[] indexs = chapter.split("</index>");
			for (int j = 0; j < indexs.length - 1; j++) {
				Map<String, Object> indexMap = new HashMap<String, Object>();
				String index = indexs[j];
				String name = index.substring(index.indexOf(">") + 1,
						index.lastIndexOf("("));
				String startTime = index.substring(index.lastIndexOf("(") + 1,
						index.lastIndexOf(")"));
				int time = Integer.valueOf(index.substring(
						index.indexOf("time=\"") + 6, index.indexOf("\">")));
				if (i == chapters.length - 2 && j == indexs.length - 2) {
					time = strToSec(totalSec) - time;
				} else if (j == indexs.length - 2) {
					String nextIdx = chapters[i + 1].substring(
							chapters[i + 1].indexOf("<index"))
							.split("</index>")[0];
					time = Integer.valueOf(nextIdx.substring(
							nextIdx.indexOf("time=\"") + 6,
							nextIdx.indexOf("\">")))
							- time;
				} else {
					String nextIdx = indexs[j + 1];
					time = Integer.valueOf(nextIdx.substring(
							nextIdx.indexOf("time=\"") + 6,
							nextIdx.indexOf("\">")))
							- time;
				}
//				Integer.valueOf(index.substring(index.indexOf("time=\"") + 6,
//						index.indexOf("\">")));
				int level = 0;
				if (name.indexOf(".") != -1) {
					// level = point count
					level = name.split("\\.").length - 1; // 按点截取
				} else if (name.indexOf("总结") != -1 || name.indexOf("小结") != -1
						|| name.indexOf("思考题") != -1
						||  name.indexOf("课间练习") != -1
						||  name.indexOf("课后练习") != -1
						||  name.indexOf("课堂练习") != -1
						) { // 特殊章节处理
					level = 1;
				}
				indexMap.put("title", name);
				indexMap.put("time", startTime);
				int frameRate = this.frameRate;
				if (str.indexOf("<frameRate>") != -1) {
					frameRate = Integer.valueOf(
							str.substring(str.indexOf("<frameRate>") + 11,
									str.indexOf("</frameRate>"))).intValue();
				}
				indexMap.put("totalframes", time * frameRate);
				indexMap.put("level", level);
				chapterList.add(indexMap);
			}
			list.add(chapterList);
		}
		map.put("title", title);
		map.put("author", author);
		map.put("totalSec", totalSec);
		map.put("list", list);
		return map;
	}

	private String secToStr(int sec) {
		StringBuffer time = new StringBuffer();
		// hour
		if (sec / (60 * 60) < 10) {
			time.append("0").append(sec / (60 * 60));
		} else {
			time.append(sec / (60 * 60));
		}
		time.append(":");
		// minute
		if ((sec % (60 * 60)) / 60 < 10) {
			time.append("0").append((sec % (60 * 60)) / 60);
		} else {
			time.append((sec % (60 * 60)) / 60);
		}
		time.append(":");
		// second
		if (sec % 60 < 10) {
			time.append("0").append(sec % 60);
		} else {
			time.append(sec % 60);
		}
		return time.toString();
	}

	/**
	 * 
	 * @param str
	 *            00:00:00
	 * @return
	 */
	private int strToSec(String str) {
		String[] secs = str.split(":");
		if (secs.length != 3) {
			return 0;
		}
		return Integer.valueOf(secs[0]) * 3600 + Integer.valueOf(secs[1]) * 60
				+ Integer.valueOf(secs[2]);
	}

	private void outWhatyPresentation(Map map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String creationdate = sdf.format(new Date());
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>                             \n");
		sb.append("<presentation>                                                        \n");
		sb.append("  <properties>                                                        \n");
		sb.append("    <publisher>北京网梯科技发展有限公司</publisher>                  \n");
		sb.append("    <publisherURL>http://www.whaty.com</publisherURL>                 \n");
		sb.append("    <title><![CDATA[" + map.get("title")
				+ "]]></title>                     \n");
		sb.append("    <creationdate>" + creationdate
				+ "</creationdate>                  \n");
		sb.append("    <totaltime>" + map.get("totalSec")
				+ "</totaltime>                                   \n");
		sb.append("    <audio>off</audio>                                              \n");
		sb.append("    <navigationonclick>false</navigationonclick>                    \n");
		sb.append("    <animations>false</animations>                                  \n");
		sb.append("    <transitions>false</transitions>                                \n");
		sb.append("    <video><![CDATA[" + presentation + "]]></video>                \n");
		sb.append("    <screen><![CDATA[" + screen + "]]></screen>                    \n");
		sb.append("    <caption>false</caption>                                        \n");
		sb.append("    <captionfile><![CDATA[Data/whatyCaption.xml]]></captionfile>    \n");
		sb.append("    <captionlanguage>ch</captionlanguage>                           \n");
		sb.append("    <templatecolor>Silver</templatecolor>                           \n");
		sb.append("    <layout>E-Learning Standard</layout>                            \n");
		sb.append("    <presenterphoto><![CDATA[Data/photo.jpg]]></presenterphoto>     \n");
		sb.append("    <presentertitle><![CDATA[]]></presentertitle>                   \n");
		sb.append("    <presentername><![CDATA[" + map.get("author")
				+ "]]></presentername>                 \n");
		sb.append("    <presentercompany><![CDATA[]]></presentercompany>               \n");
		sb.append("    <presenteremail><![CDATA[]]></presenteremail>                   \n");
		sb.append("    <presentationtitle>" + map.get("title")
				+ "</presentationtitle>      \n");
		sb.append("    <powerpointfile>                                                \n");
		sb.append("    </powerpointfile>                                               \n");
		sb.append("    <presentationdescription><![CDATA[]]></presentationdescription> \n");
		sb.append("    <presentationkeywords>                                          \n");
		sb.append("    </presentationkeywords>                                         \n");
		sb.append("    <audioquality>Medium</audioquality>                             \n");
		sb.append("    <imagequality>9</imagequality>                                  \n");
		sb.append("    <showslidenotesinoutput>true</showslidenotesinoutput>           \n");
		sb.append("    <usenarrations>true</usenarrations>                             \n");
		sb.append("    <drawfromfirstslide>true</drawfromfirstslide>                   \n");
		sb.append("  </properties>                                                     \n");
		sb.append("  <slides>                                                          \n");
		int count = 1;
		List<List<Map<String, Object>>> list = (List) map.get("list");
		for (int i = 0; i < list.size(); i++) {
			List<Map<String, Object>> slides = list.get(i);
			for (int j = 0; j < slides.size(); j++, count++) {
				Map<String, Object> slideMap = slides.get(j);
				sb.append("    <slide id=\""
						+ count
						+ "\">                                                  \n");
				sb.append("      <transition />                                                \n");
				sb.append("      <type>flash</type>                                            \n");
				sb.append("      <screenWidth>1440</screenWidth>                               \n");
				sb.append("      <screenHeight>900</screenHeight>                              \n");
				sb.append("      <width>720</width>                                            \n");
				sb.append("      <height>540</height>                                          \n");
				sb.append("      <fileurl>Slides/flash" + count
						+ ".swf</fileurl>                          \n");
				sb.append("      <slideimage>                                                  \n");
				sb.append("      </slideimage>                                                 \n");
				sb.append("      <title><![CDATA[" + slideMap.get("title")
						+ "]]></title>                 \n");
				sb.append("      <level><![CDATA[" + slideMap.get("level")
						+ "]]></level>                                  \n");
				sb.append("      <notes><![CDATA[]]></notes>                                   \n");
				sb.append("      <slidetext><![CDATA[]]></slidetext>                           \n");
				sb.append("      <scormid><![CDATA[" + (i + 1) + "_0_" + j
						+ "]]></scormid>                         \n");
				sb.append("      <waitforuser>false</waitforuser>                              \n");
				sb.append("      <totalframes>" + slideMap.get("totalframes")
						+ "</totalframes>                                \n");
				sb.append("      <time>" + slideMap.get("time")
						+ "</time>                                         \n");
				sb.append("    </slide>                                                        \n");
			}
		}
		sb.append("  </slides>                                                         \n");
		sb.append("</presentation>                                                     \n");
		// System.out.println(sb.toString());
		this.writeFile(sb.toString(), section + File.separator + "WhatyFlash"
				+ File.separator + "Data" + File.separator
				+ "whatyPresentation.xml");
	}

	private void outConfig(Map<Object, Object> map) {
		String str = this.readFile(new File("D:/kjChange/template/config.xml"));
		str = str.replace("[title]", map.get("title").toString());
		this.writeFile(str, "config.xml");
	}

	private void outIndexHtm(Map<Object, Object> map) {
		String str = this.readFile(new File("D:/kjChange/template/index.htm"));
		str = str.replace("[title]", map.get("title").toString());
		this.writeFile(str, "index.htm");
	}

	private void outImsmanifest(Map<Object, Object> map) {
		String str = this.readFile(new File(
				"D:/kjChange/template/imsmanifest.xml"));
		str = str.replace("[title]", map.get("title").toString());
		this.writeFile(str, "imsmanifest.xml");
	}

	private void outNavigate(Map<Object, Object> map) {
		String str = this
				.readFile(new File("D:/kjChange/template/navigate.xml"));
		str = str.replace("[title]", map.get("title").toString());
		this.writeFile(str, "navigate" + File.separator + "navigate.xml");
	}

	public void outFiles(Map<Object, Object> map) {
		// print files
		this.outImsmanifest(map);
		this.outConfig(map);
		this.outNavigate(map);
		this.outIndexHtm(map);
		this.outWhatyPresentation(map);
	}
}

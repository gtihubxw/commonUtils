import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DelFiles {

	/**
	 * ����ɾ���ļ�
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String srcPath = "C:\\Users\\whaty\\Documents\\Tencent Files\\1175912263\\FileRecv\\20171224���������׸ֳɼ���_������";
		
		System.out.println(FileUtils.countFiles(srcPath));
//		delFiles(srcPath);
	}
	
	

}

class FileUtils {
	
//	private List<File> fileList= new ArrayList<File>();
//	private List<String> fileNames= new ArrayList<String>();
	
	public static int countFiles(String srcPath) {
		int count = 0;
		File path = new File(srcPath);
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			String[] fileNames = f.list();
			if(fileNames.length == 0){
				System.out.println(f.getName() + "======== 0");
			}
			count += fileNames.length;
		}
		return count;
	}

	public static void delFiles(String srcPath) {
		File path = new File(srcPath);
		// String[] fileNames = path.list();
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			File[] xlsFiles = f.listFiles();
			for (int j = 0; j < xlsFiles.length; j++) {
				File xlsFile = xlsFiles[j];
				if (!xlsFile.getName().startsWith("�ɼ���[��ͬ")) {
					boolean flag = xlsFile.delete();
					if (!flag) {
						System.out.println("Del error,file:" + xlsFile);
					}
				}
			}
		}
	}

	/**
	 * ���Ҹ�Ŀ¼�������ļ���ƥ������������ļ�
	 * 
	 * @param path
	 * @param regex
	 * @return
	 */
	public static List<File> getFileListByRex(String path, String regex) {
		List<File> fileList = new ArrayList<File>();
		new FileUtils().getFinalFileList(new File(path), regex, fileList);
		return fileList;
	}

	/**
	 * �ݹ�����ļ�
	 * 
	 * @param dirFile
	 * @param regex
	 * @param fileList
	 */
	private void getFinalFileList(File dirFile, String regex,
			List<File> fileList) {
		File[] files = dirFile.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				getFinalFileList(f, regex, fileList);
			} else if (f.getName().matches(regex)) {
				// this.fileNames.add(f.getName());
				fileList.add(f);
			}
		}
	}
	/**
	 * Use OS command to copy file
	 * @param source
	 * @param dest
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void copyFileUsingCommand(File source, File dest)
			throws IOException, InterruptedException {
		Properties props = System.getProperties();
		String osName = props.getProperty("os.name");
		// System.out.println("osName========" + osName + "========");
		if (osName == null) {
			return;
		}
		String cmd = null;
		String src = source.getAbsolutePath();
		String des = dest.getAbsolutePath();
		if (osName.toLowerCase().startsWith("win")) {
			// cmd = "xcopy " + src + " " + des;
			cmd = "xcopy " + src + " " + des + "/d/y";
		} else {
			cmd = "cp -u " + src + " " + des;
		}
		// ��ȡ����ʱ
		Runtime rt = Runtime.getRuntime();
		// ��ȡ����
		Process p = rt.exec(cmd);
		OutputStream stdout = p.getOutputStream();
		System.out.println("Process outputStream:");
		// Ŀ�� source ���ļ�������Ŀ¼��, (F = �ļ���D = Ŀ¼)?
		if (src.substring(src.lastIndexOf('\\')).contains(".")) {
			char f = 'f';
			stdout.write(f);
		}
		stdout.close();
		// ��ȡ���̵ı�׼������
		final InputStream is1 = p.getInputStream();
		// ��ȡ���̵Ĵ�����
		final InputStream is2 = p.getErrorStream();
		// ���������̣߳�һ���̸߳������׼���������һ���������׼������
		new Thread() {
			public void run() {
				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						is1));
				try {
					String line1 = null;
		            StringBuilder sb = new StringBuilder();  
					while ((line1 = br1.readLine()) != null) {
						sb.append(line1); 
					}
					System.out.println(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is1.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		new Thread() {
			public void run() {
				BufferedReader br2 = new BufferedReader(new InputStreamReader(
						is2));
				try {
					String line2 = null;
		            StringBuilder sb = new StringBuilder();
					while ((line2 = br2.readLine()) != null) {
						sb.append(line2); 
					}
					System.out.println(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is2.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		p.waitFor();
		p.destroy();
		/*
		 * String line = null; StringBuilder sb = new StringBuilder();
		 * BufferedReader bufferedReader = new BufferedReader( new
		 * InputStreamReader(p.getInputStream())); while ((line =
		 * bufferedReader.readLine()) != null) { sb.append(line + "\n");
		 * System.out.println(line); }
		 */
		// ���p��Ϊ�գ���ôҪ���
		if (p != null) {
			p.destroy();
			p = null;
		}
	}
	
	public static String readFile(File file) {
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

	public static void writeFile(String str, String fileName, String outPath) {
		File file = new File(outPath + fileName);
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
}

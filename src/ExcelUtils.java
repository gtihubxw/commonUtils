import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtils {

	/*
	 * private List<File> fileList= new ArrayList<File>(); private List<String>
	 * fileNames= new ArrayList<String>();
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * 合并成绩单Excel
	 * 
	 * @param parentPath
	 * @param outPath
	 */
	public void mergeExcels(String parentPath, String outPath, int startRow,
			String[] fields, String[] oFields, boolean allowBlank) {
		List<Map<String, String>> scores = new ArrayList<Map<String, String>>();
		// .xlsx会报错，需另存为.xls
		List<File> fileList = FileUtils.getFileListByRex(parentPath,
				".*\\.(xls|xlsx)$");
		// System.out.println(fileList.size() + "===" + fileList.get(0));
		for (File xls : fileList) {
			Workbook work = null;
			try {
				work = Workbook.getWorkbook(xls);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(xls);
				continue;
			}
			// Sheet[] sheets = work.getSheets()
			for (Sheet sheet : work.getSheets()) {
				// Sheet sheet = work.getSheet(0);
				int rows = sheet.getRows();
				for (int i = startRow; i < rows; i++) {
					Map<String, String> map = new HashMap<String, String>();
					boolean blank = true;
					for (int j = 0; j < fields.length; j++) {
						String con = sheet.getCell(j, i).getContents().trim();
						map.put(fields[j], con);
						if (blank && !"".equals(con)) {
							blank = false;
						}
					}
					if (allowBlank || !blank) {
						scores.add(map);
					}
				}
			}
		}
		// 输出到一个Excel中
		WritableWorkbook outBook = null;
		try {
			if (oFields == null) {
				oFields = fields;
			}
			outBook = Workbook.createWorkbook(new File(outPath));
			WritableSheet outSheet = outBook.createSheet("Sheet_1", 0);
			for (int i = 0; i < scores.size(); i++) {
				Map<String, String> map = scores.get(i);
				for (int j = 0; j < oFields.length; j++) {
					Label labe0 = new Label(j, i, map.get(oFields[j]));
					outSheet.addCell(labe0);
				}
			}
			if (outBook != null) {
				outBook.write(); // 写入Excel
			}
			System.out.println("Complete" + scores.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outBook != null) {
				try {
					outBook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 分割Excel
	 * 
	 * @param srcPath
	 * @param outDir
	 */
	public void divideExcels(String srcPath, String outDir, int titleRow,
			int divideNum) {
		Workbook work = null;
		File file = new File(srcPath);
		String fileName = file.getName().substring(0,
				file.getName().lastIndexOf("."));
		if (!".xls".equals(file.getName().substring(
				file.getName().lastIndexOf(".")))) {
			System.out.println("Only support .xls files!");
			return;
		}
		try {
			work = Workbook.getWorkbook(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = work.getSheet(0);
		int rows = sheet.getRows();
		WritableWorkbook outBook = null;
		WritableSheet outSheet = null;
		String fName = fileName + "(0).xls";
		int curRow = titleRow;
		try {
			outBook = Workbook.createWorkbook(new File(outDir + fName));
			outSheet = outBook.createSheet("Sheet_1", 0);
			for (int i = titleRow; i < rows; i++) {
				for (Cell c : sheet.getRow(i)) {
					String con = c.getContents().trim();
					if (con == null || "".equals(con)) { // 成绩导入特殊处理
						con = "0";
					}
					Label label = new Label(c.getColumn(), curRow, con);
					outSheet.addCell(label);
				}
				if ((i != titleRow && (i - titleRow) % divideNum == 0)
						|| i + 1 == rows) {
					// append title
					for (int h = 0; h < titleRow; h++) {
						for (Cell c : sheet.getRow(h)) {
							Label label = new Label(c.getColumn(), c.getRow(),
									c.getContents());
							outSheet.addCell(label);
						}
					}
					// print
					if (outBook != null) {
						outBook.write(); // 写入Excel
						outBook.close();
						outBook = null;
					}
					System.out.println(fName);
					// init
					if (i + 1 != rows) {
						fName = fileName + "(" + ((i - titleRow) / divideNum)
								+ ").xls";
						outBook = Workbook.createWorkbook(new File(outDir
								+ fName));
						outSheet = outBook.createSheet("Sheet_1", 0);
						curRow = titleRow - 1;
					}
				}
				curRow++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outBook != null) {
				try {
					outBook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 未完成
	public void export(String tempPath, String oPath) {
		String[] options = { "field" };
		try {
			WritableWorkbook outBook = null;
			WritableSheet outSheet = null;
			outBook = Workbook.createWorkbook(new File(oPath));
			outSheet = outBook.createSheet("Sheet_1", 0);
			Workbook book = Workbook.getWorkbook(new File(tempPath));
			Sheet sheet = book.getSheet(0);
			int rows = sheet.getRows();
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < sheet.getColumns(); c++) {
					Cell cell = sheet.getCell(r, c);
					CellFormat cf = cell.getCellFormat();
					String content = cell.getContents().trim();
					CellFeatures features = cell.getCellFeatures();
					if (features != null) {
						String comment = features.getComment().trim();
						Map<String, String> map = new HashMap<String, String>();
						String[] items = comment.split(";");
						for (String item : items) {
							String[] its = item.split(":");
							if (its.length != 2) {
								System.out.println("Features error!");
								return;
							}
							map.put(its[0], its[1]);
						}
						/*
						 * for(String option : options){ map.get(option); }
						 */
						content = map.get("field");
					}
					Label label = new Label(c, r, content, cf);
					outSheet.addCell(label);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	*//**
	 * 递归查找根目录下所有文件名匹配所传正则的文件
	 * 
	 * @param fileList
	 */
	/*
	 * public void getFinalFileList(File dirFile,String regex){ File[] files =
	 * dirFile.listFiles(); for(File f : files){ if(f.isDirectory()){
	 * getFinalFileList(f,regex); }else if (f.getName().matches(regex)) {
	 * this.fileNames.add(f.getName()); this.fileList.add(f); } } }
	 */
}

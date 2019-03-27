public class Course {

	public static void main(String[] args) {
		Course c = new Course();
		String sql = null;
		/*sql = new Course().copyCourseSql(
				"ff808081532bfd4b01532fb7424303df",
				"40288aad2d02c234012d02c59a280486");*/
		sql = c.copyCourseSql("ff8080816426a5a50164917d60352dc7","ff8080814a9e8824014a9f51265102c7");
		System.out.println(sql);
		/*sql = c.copyCourseSql("ff80808163ece10f0163f1ed60b6303e","40288aad2d02c234012d02c4f4e20210");
		System.out.println(sql);
		sql = c.copyCourseSql("ff8080816426a5a501648292336a729a","40288aad2d02c234012d02c4f4100206");
		System.out.println(sql);
		sql = c.copyCourseSql("ff80808163ba84800163cdca307f3965","40288aad2d02c234012d02c4f5c8021b");
		System.out.println(sql);
		sql = c.copyCourseSql("ff80808163ba84800163cdcc971d397d","40288aad2d02c234012d02c4f4380208");
		System.out.println(sql);
		sql = c.copyCourseSql("ff80808163ba84800163cdc593a43936","40288aad2d02c234012d02c5004a023b");
		System.out.println(sql);
		sql = c.copyCourseSql("ff80808163ece10f0163f1ebf1c63015","40288aad2d02c234012d02c4f465020a");
		System.out.println(sql);
		sql = c.copyCourseSql("ff8080816426a5a5016482913702728b","40288aad2d02c234012d02c4f53c0214");
		System.out.println(sql);
		sql = c.copyCourseSql("ff8080816426a5a501648291a06a7292","40288aad2d02c234012d02c5d81c0594");
		System.out.println(sql);
		sql = c.copyCourseSql("ff8080816426a5a50164829064b27282","40288aad2d02c234012d02c5d790058e");
		System.out.println(sql);
		sql = c.copyCourseSql("ff80808163ba84800163cdc828f53952","40288aad2d02c234012d02c4f59b0219");
		System.out.println(sql);*/
	}

	public String copyCourseSql(String srcId, String toId) {
		StringBuffer sql = new StringBuffer();
		copyScorm(sql, srcId, toId);
		copyCourseTeacher(sql, srcId, toId);
		copyLearnTeachingRecord(sql, srcId, toId);
		copyWorkroomColumn(sql, srcId, toId);
		copyLearnSettings(sql, srcId, toId);
		copyCourseLearn(sql, srcId, toId);
		copyCourseTeacherLearn(sql, srcId, toId);
		copyCsResource(sql, srcId, toId);
		copyScormStuSco(sql, srcId, toId);
		copyHomeworkpaperInfo(sql, srcId, toId);
		copyTestLoreInfo(sql, srcId, toId);
		copyThemeMainPost(sql, srcId, toId);
		copyHomeworkInfo(sql, srcId, toId);
		return sql.toString();
	}

	/**
	 * 1 scorm_course_info
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyScorm(StringBuffer sql, String srcId, String toId) {
		sql.append("update scorm_course_info sci set sci.FK_COURSE_ID='")
				.append(toId).append("' where sci.FK_COURSE_ID='")
				.append(srcId).append("';\n");
	}

	/**
	 * 2 pr_tch_course_teacher
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyCourseTeacher(StringBuffer sql, String srcId, String toId) {
		sql.append("update pr_tch_course_teacher sci set sci.FK_COURSE_ID='")
				.append(toId).append("' where sci.FK_COURSE_ID='")
				.append(srcId).append("';\n");
	}

	/**
	 * 3 learn_teaching_record
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyLearnTeachingRecord(StringBuffer sql, String srcId,
			String toId) {
		sql.append("UPDATE learn_teaching_record r set r.FK_COURSE_ID='")
				.append(toId).append("' where r.FK_COURSE_ID='").append(srcId)
				.append("';\n");
	}

	/**
	 * 4 pe_workroom_column
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyWorkroomColumn(StringBuffer sql, String srcId, String toId) {
		sql.append("UPDATE pe_workroom_column w set w.course='").append(toId)
				.append("' where w.course='").append(srcId).append("';\n");
	}

	/**
	 * 5 learn_settings
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyLearnSettings(StringBuffer sql, String srcId, String toId) {
		sql.append("update learn_settings s set s.FK_COURSE_ID='").append(toId)
				.append("' where s.FK_COURSE_ID='").append(srcId)
				.append("';\n");
	}

	/**
	 * 6 course_learn
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyCourseLearn(StringBuffer sql, String srcId, String toId) {
		sql.append("update course_learn l set l.fk_course_id='").append(toId)
				.append("' where l.fk_course_id='").append(srcId)
				.append("';\n");
	}

	/**
	 * 7 course_teacher_learn
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyCourseTeacherLearn(StringBuffer sql, String srcId,
			String toId) {
		sql.append("UPDATE course_teacher_learn ctl set ctl.fk_course_id='")
				.append(toId).append("' where ctl.fk_course_id='")
				.append(srcId).append("';\n");
	}

	/**
	 * 8 cs_resource
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyCsResource(StringBuffer sql, String srcId, String toId) {
		sql.append("update cs_resource r set r.FK_COURSE_ID='").append(toId)
				.append("' where r.FK_COURSE_ID='").append(srcId)
				.append("';\n");
	}

	/**
	 * 9 scorm_stu_sco
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyScormStuSco(StringBuffer sql, String srcId, String toId) {
		sql.append("update scorm_stu_sco sss set sss.FK_COURSE_ID='")
				.append(toId).append("' where sss.FK_COURSE_ID='")
				.append(srcId).append("';\n");
	}

	/**
	 * 10 test_homeworkpaper_info
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyHomeworkpaperInfo(StringBuffer sql, String srcId,
			String toId) {
		sql.append("update test_homeworkpaper_info f set f.course='")
				.append(toId).append("' where f.course='").append(srcId)
				.append("';\n");
	}

	/**
	 * 11 test_lore_info
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyTestLoreInfo(StringBuffer sql, String srcId, String toId) {
		sql.append("update test_lore_info tli set tli.fk_course_id='")
				.append(toId).append("' where tli.fk_course_id='")
				.append(srcId).append("';\n");
	}

	/**
	 * 12 theme_main_post
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyThemeMainPost(StringBuffer sql, String srcId, String toId) {
		sql.append("update theme_main_post tmp set tmp.course_id='")
				.append(toId).append("' where tmp.course_id='").append(srcId)
				.append("';\n");
	}

	/**
	 * 13 homework_info
	 * 
	 * @param sql
	 * @param srcId
	 * @param toId
	 */
	private void copyHomeworkInfo(StringBuffer sql, String srcId, String toId) {
		sql.append("UPDATE homework_info  hif set hif.COURSE_ID='")
				.append(toId).append("' where hif.COURSE_ID='").append(srcId)
				.append("';\n");
	}

}

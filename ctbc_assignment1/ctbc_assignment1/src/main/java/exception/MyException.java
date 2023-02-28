package exception;

import model.Course;
import model.CourseCategory;
import service.CourseCategoryServiceJDKImplement;
import service.CourseServiceJDKImplement;

public class MyException {

	private CourseCategoryServiceJDKImplement ccs;
	private CourseServiceJDKImplement cs;

	public MyException(CourseCategoryServiceJDKImplement ccs, CourseServiceJDKImplement cs) {
		super();
		this.ccs = ccs;
		this.cs = cs;
	}

	public MyException(CourseCategoryServiceJDKImplement ccs) {
		super();
		this.ccs = ccs;
	}

	public MyException(CourseServiceJDKImplement cs) {
		super();
		this.cs = cs;
	}

	// Check if the courseCategory is Exist(Allow to search from String
	// courseCategoryName or courseCategory Object
	public <T> boolean isCourseCategoryExist(T courseCategory) {
		for (CourseCategory cc : ccs.getAllCourseCategories()) {
			if (courseCategory instanceof String && cc.getCourseCategoryName().equals(courseCategory)) {
				System.out.printf("Category '%s' is exist!!!\n", (String) courseCategory);
				return true;
			}

			else if (courseCategory instanceof CourseCategory && cc.equals(courseCategory)) {
				System.out.printf("Category '%s' is exist!!!\n",
						((CourseCategory) courseCategory).getCourseCategoryName());
				return true;
			}

			else { // Other T type
				System.out.printf("Wrong type of parameter, please use 'String' or 'CourseCategory' Object.\n");
				return false;
			}

		}

		return false;
	}

	public <T> boolean isCourseExist(T course) {
		for (Course c : cs.getAllCourses()) {
			if (course instanceof String && c.getCourseName().equals(course)) {
				System.out.printf("Course '%s' is exist!!!\n", (String) course);
				return true;
			} else if (course instanceof Course && c.equals(course)) {
				System.out.printf("Course '%s' is exist!!!\n", ((Course) course).getCourseName());
				return true;
			} else {
				System.out.printf("Wrong type of parameter, please use 'String' or 'Course' Object.\n");
				return false;
			}
		}
		return false;

	}

}

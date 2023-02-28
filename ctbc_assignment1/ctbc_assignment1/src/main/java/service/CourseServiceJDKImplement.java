package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import exception.MyException;
import lombok.Getter;
import lombok.Setter;
import model.Course;
import model.CourseCategory;

public class CourseServiceJDKImplement implements CourseService {
	@Getter
	private List<Course> allCourses = new ArrayList<>();
	private Map<UUID, Course> courseHashmap = new HashMap<UUID, Course>();
	// TODO:Optimize setter method
	@Setter
	private CourseCategoryServiceJDKImplement courseCategoryService = null;

	private MyException exception = new MyException(courseCategoryService, this);

	@Override
	public void displayAllCourses() {
		System.out.printf("%-50s%-20s%-20s%-20s\n", "UUID", "CourseName", "CoursePrice", "CourseCategory");
		System.out.println("-".repeat(120));
		if (allCourses.isEmpty()) {
			System.out.printf("(null)\n");
			return;
		}
		for (Course c : allCourses) {
			System.out.printf("%-50s%-20s%-20d%-20s\n", c.getCourseUUID(), c.getCourseName(), c.getCoursePrice(),
					c.getCourseCategory().getCourseCategoryName());
		}
		System.out.printf("\n");
	}

	// Add new Course (Can add course by use CourseCategory object or String
	// CourseCategoryName)
	@Override
	public <T> Course addCourse(String courseName, Integer coursePrice, T courseCategory) {
		CourseCategory cc = null;
		// CourseCategoryName
		if (courseCategory instanceof String) {
			cc = courseCategoryService.getCourseCategoryFromName((String) courseCategory);
		}
		// CourseCategoryObject
		else if (courseCategory instanceof CourseCategory) {
			cc = (CourseCategory) courseCategory;
		} else { // Other T type
			System.out.printf("Wrong type of parameter, please use 'String' or 'Course' Object.\n");
		}
		System.out.println("ex: "+exception.isCourseExist(courseName));
		if (exception.isCourseExist(courseName) || cc == null)
			return null;
		UUID uuid = UUID.randomUUID();
		Course c = new Course(uuid, courseName, coursePrice, cc);
		allCourses.add(c);
		courseHashmap.put(uuid, c);
		cc.getCoursesOfCategory().add(c);
		return c;
	}

	@Override
	public <T> void delCourse(T course) {
		Course c = null;
		// Delete course by UUID
		if (course instanceof String) {
			c = getCourseFromUUID((String) course);
		}
		// Delete course by Course Object
		else if (course instanceof Course) {
			c = (Course) course;
		} else {
			System.out.printf("Wrong type of parameter, please use 'String' or 'Course' Object.\n");
		}
		if (c == null)
			return;
		CourseCategory cc = c.getCourseCategory();
		allCourses.remove(c);
		courseHashmap.remove(c);
		cc.getCoursesOfCategory().remove(c);
	}

	@Override
	public <T> Course modifyCourse(T course) {
		Course c = null;
		// Modify course by UUID
		if (course instanceof String) {
			c = getCourseFromUUID((String) course);
		}
		// Modify course by Course Object
		else if (course instanceof Course) {
			c = (Course) course;
		} else {
			System.out.printf("Wrong type of parameter, please use 'String' or 'Course' Object.\n");
		}
		if (c == null)
			return null;
		// User input
		Scanner sc = new Scanner(System.in);
		String input = "";
		System.out.printf("%-50s%-20s%-20s%-20s\n", "UUID", "CourseName", "CoursePrice", "CourseCategory");
		System.out.println("-".repeat(120));
		System.out.printf("%-50s%-20s%-20d%-20s\n", c.getCourseUUID(), c.getCourseName(), c.getCoursePrice(),
				c.getCourseCategory().getCourseCategoryName());
		do {
			System.out.printf("Input the Course Name, or Enter to skip: \n");
			input = sc.nextLine();
		} while (input.equals(c.getCourseName()) || input.equals("") || exception.isCourseExist(input));
		if (!input.equals(""))
			c.setCourseName(input);
		do {
			System.out.printf("Input the Course Price, or Enter to skip: \n");
			input = sc.nextLine();
		} while (!input.matches("\\d+") || input.equals(""));
		if (!input.equals(""))
			c.setCoursePrice(Integer.parseInt(input));

		courseCategoryService.displayAllCategories();
		do {
			System.out.printf("Input the Course Category, or Enter to skip: \n");
			input = sc.nextLine();
		} while (!exception.isCourseCategoryExist(input) || input.equals(""));
		if (!input.equals("")) {
			CourseCategory oldCategory = c.getCourseCategory();
			CourseCategory newCourseCategory = courseCategoryService.getCourseCategoryFromName(input);
			oldCategory.getCoursesOfCategory().remove(c);
			newCourseCategory.getCoursesOfCategory().add(c);
		}
		return c;
	}

	// To get Course Object from UUID String
	private Course getCourseFromUUID(String courseUUID) {
		UUID uuid;
		try {
			uuid = UUID.fromString(courseUUID);
			return courseHashmap.get(uuid);
		} catch (IllegalArgumentException e) {
			System.out.printf("Course UUID '%s' is not exist!!!\n", courseUUID);
			return null;
		}
	}

	@org.junit.jupiter.api.Test
	public void Test() {
		Scanner sc = new Scanner(System.in);
		CourseCategoryServiceJDKImplement ccs = new CourseCategoryServiceJDKImplement();
		setCourseCategoryService(ccs);
		CourseCategory cc1 = ccs.addCourseCategory("category1", "des1");
		CourseCategory cc2 = ccs.addCourseCategory("category2", "des2");
		ccs.addCourseCategory("category3", "des3");
		ccs.displayAllCategories();
		addCourse("course1", 500, cc1);
		addCourse("course2", 500, cc1);
		addCourse("course2", 1000, "category2");
		addCourse("course3", 500, "category3");
		addCourse("course4", 500, "category2");
		addCourse("course5", 500, "category1");
		displayAllCourses();

//		System.out.printf("Input uuid to del the course: \n");
//		delCourse(sc.nextLine());
//		displayAllCourses();

		System.out.printf("Input uuid to modify the course: \n");
		modifyCourse(sc.nextLine());
		displayAllCourses();
		ccs.displayAllCoursesOfTheCategory(cc1);
		ccs.displayAllCoursesOfTheCategory(cc2);

	}

}

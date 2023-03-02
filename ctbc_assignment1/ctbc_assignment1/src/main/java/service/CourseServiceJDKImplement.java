package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.jupiter.api.Test;

import exception.MyException;
import lombok.Getter;
import model.Course;
import model.CourseCategory;

public class CourseServiceJDKImplement implements CourseService {

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();

	@Getter
	private List<Course> allCourses = new ArrayList<>();
	private Map<UUID, Course> courseHashmap = new HashMap<UUID, Course>();
	private static MyException exception = MyException.getInstance();
	private static CourseCategoryServiceJDKImplement courseCategoryService = CourseCategoryServiceJDKImplement
			.getInstance();

	private CourseServiceJDKImplement() {
		super();
	}

	// BillPugh Singleton Implementation
	private static class SingletonHolder {
		private static final CourseServiceJDKImplement courseServiceInstance = new CourseServiceJDKImplement();
	}

	public static CourseServiceJDKImplement getInstance() {
		return SingletonHolder.courseServiceInstance;
	}

	@Override
	public void displayAllCourses() {
		readLock.lock();
		try {
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
		} finally {
			readLock.unlock();
		}

	}

	// Add new Course (Can add course by use CourseCategory object or String
	// CourseCategoryName)
	@Override
	public <T> Course addCourse(String courseName, Integer coursePrice, T courseCategory) {
		writeLock.lock();
		try {
			CourseCategory cc = null;
			// CourseCategoryName
			if (courseCategory instanceof String) {
				cc = courseCategoryService.getCourseCategoryFromName((String) courseCategory);
			}
			// CourseCategoryObject
			else if (courseCategory instanceof CourseCategory) {
				cc = (CourseCategory) courseCategory;
			} else { // Other T type
				exception.errorMessage(MyException.WrongParameterType);
			}
			if (isCourseExist(courseName) || cc == null)
				return null;

			UUID uuid = UUID.randomUUID();
			Course c = new Course(uuid, courseName, coursePrice, cc);
			allCourses.add(c);
			courseHashmap.put(uuid, c);
			cc.getCoursesOfCategory().add(c);
			return c;
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public <T> void delCourse(T course) {
		writeLock.lock();
		try {
			Course c = null;
			// Delete course by UUID
			if (course instanceof String) {
				c = getCourseFromUUID((String) course);
			}
			// Delete course by Course Object
			else if (course instanceof Course) {
				c = (Course) course;
			} else {
				exception.errorMessage(MyException.WrongParameterType);
			}
			if (c == null)
				return;
			CourseCategory cc = c.getCourseCategory();
			allCourses.remove(c);
			courseHashmap.remove(c);
			cc.getCoursesOfCategory().remove(c);
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public <T> Course modifyCourse(T course) {
		writeLock.lock();
		try {
			Course c = null;
			// Modify course by UUID
			if (course instanceof String) {
				c = getCourseFromUUID((String) course);
			}
			// Modify course by Course Object
			else if (course instanceof Course) {
				c = (Course) course;
			} else {
				exception.errorMessage(MyException.WrongParameterType);
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
			} while (input.equals(c.getCourseName()) || input.equals("") || isCourseExist(input));
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
			} while (!courseCategoryService.isCourseCategoryExist(input) || input.equals(""));
			if (!input.equals("")) {
				CourseCategory oldCategory = c.getCourseCategory();
				CourseCategory newCourseCategory = courseCategoryService.getCourseCategoryFromName(input);
				oldCategory.getCoursesOfCategory().remove(c);
				newCourseCategory.getCoursesOfCategory().add(c);
			}
			return c;
		} finally {
			writeLock.unlock();
		}

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

	// TODO:Exception message
	public <T> boolean isCourseExist(T course) {
		for (Course c : allCourses) {
			if (course instanceof String) {
				if (c.getCourseName().equals((String) course)) {
					exception.errorMessage(MyException.CourseExist);
					return true;
				}
			} else if (course instanceof Course) {
				if (c.getCourseName().equals(((Course) course).getCourseName())) {
					exception.errorMessage(MyException.CourseExist);
					return true;
				}
			} else {
				exception.errorMessage(MyException.WrongParameterType);
				return false;
			}
		}
		return false;

	}

	@Test
	public void Test() {
		Scanner sc = new Scanner(System.in);
		CourseCategoryServiceJDKImplement ccs = CourseCategoryServiceJDKImplement.getInstance();
		CourseCategory cc1 = ccs.addCourseCategory("category1", "des1");
		CourseCategory cc2 = ccs.addCourseCategory("category2", "des2");
		ccs.addCourseCategory("category3", "des3");
		ccs.displayAllCategories();
		addCourse("course1", 500, cc1);
		addCourse("course2", 500, cc1);
		addCourse("course2", 500, 123);
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

//	@Test
	public void TestForCompareInstance() {
		List<CourseServiceJDKImplement> courseServices = new ArrayList<>();
		CourseServiceJDKImplement csStaticInstance1 = CourseServiceJDKImplement.getInstance();
		CourseServiceJDKImplement csStaticInstance2 = CourseServiceJDKImplement.getInstance();

		for (int i = 0; i < 3; i++) {
			courseServices.add(new CourseServiceJDKImplement());
		}
		courseServices.add(csStaticInstance1);
		courseServices.add(csStaticInstance2);

		System.out.printf("cs0 equal cs1: %b\n", courseServices.get(0).equals(courseServices.get(1)));
		System.out.printf("ccs1 equal ccs2: %b\n", courseServices.get(1).equals(courseServices.get(2)));
		System.out.printf("ccsStaticInstance1 equal ccs0: %b\n", csStaticInstance1.equals(courseServices.get(0)));
		System.out.printf("ccsStaticInstance1 equal ccsStaticInstance2: %b\n\n",
				csStaticInstance1.equals(csStaticInstance2));

		System.out.printf("Display HashCode: \n");
		for (CourseServiceJDKImplement ccs : courseServices) {
			System.out.printf("%s\n", Integer.toHexString(ccs.hashCode()));
		}
		System.out.println();
	}

}

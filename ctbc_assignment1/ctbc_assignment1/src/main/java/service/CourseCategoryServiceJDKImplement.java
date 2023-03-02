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

public class CourseCategoryServiceJDKImplement implements CourseCategoryService {

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();

	@Getter
	private List<CourseCategory> allCourseCategories = new ArrayList<>();

	private Map<UUID, CourseCategory> courseCategoriesHashmap = new HashMap<>();
	private static MyException exception = MyException.getInstance();

	private CourseCategoryServiceJDKImplement() {
		super();
	}

	// BillPugh Singleton Implementation
	private static class SingletonHolder {
		private static final CourseCategoryServiceJDKImplement courseCategoryServiceInstance = new CourseCategoryServiceJDKImplement();
	}

	public static CourseCategoryServiceJDKImplement getInstance() {
		return SingletonHolder.courseCategoryServiceInstance;
	}

	@Override
	public void displayAllCategories() {
		readLock.lock();
		try {
			System.out.printf("%-50s%-20s%-20s\n", "UUID", "CategoryName", "Description");
			System.out.println("-".repeat(100));
			if (allCourseCategories.isEmpty()) {
				System.out.printf("(null)\n");
				return;
			}
			for (CourseCategory cc : allCourseCategories) {
				System.out.printf("%-50s%-20s%-20s\n", cc.getCourseCategoryUUID().toString(),
						cc.getCourseCategoryName(), cc.getCourseCategoryDescription());
			}
			System.out.printf("\n");
		} finally {
			readLock.unlock();
		}
	}

	// Display all Course of a specific category by courseCategory(polynomial)
	public <T> void displayAllCoursesOfTheCategory(T courseCategory) {
		readLock.lock();
		try {
			List<Course> allCourses = null;
			if (courseCategory instanceof String) {
				allCourses = getCourseCategoryFromName((String) courseCategory).getCoursesOfCategory();
				System.out.printf("All Courses of Category '%s': \n", courseCategory);
			} else if (courseCategory instanceof CourseCategory) {
				allCourses = ((CourseCategory) courseCategory).getCoursesOfCategory();
				System.out.printf("All Courses of Category '%s': \n",
						((CourseCategory) courseCategory).getCourseCategoryName());
			} else { // Other T type
				System.out.printf("Wrong type of parameter, please use 'String' or 'CourseCategory' Object.\n");
			}

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

	@Override
	public CourseCategory addCourseCategory(String courseCategoryName, String courseCategoryDesription) {
		writeLock.lock();
		try {
			UUID uuid = UUID.randomUUID();
			if (isCourseCategoryExist(courseCategoryName))
				return null;

			CourseCategory newCourseCategory = new CourseCategory(uuid, courseCategoryName, courseCategoryDesription);
			allCourseCategories.add(newCourseCategory);
			courseCategoriesHashmap.put(uuid, newCourseCategory);
			return newCourseCategory;
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public void delCourseCategory(String courseCategoryUUID) {
		writeLock.lock();
		try {
			CourseCategory cc = getCourseCategoryFromUUID(courseCategoryUUID);
			if (cc == null)
				return;
			allCourseCategories.remove(cc);
			courseCategoriesHashmap.remove(cc);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public CourseCategory modifyCourseCategory(String courseCategoryUUID) {
		writeLock.lock();
		try {
			CourseCategory cc = getCourseCategoryFromUUID(courseCategoryUUID);
			if (cc == null)
				return null;
			// User input
			Scanner sc = new Scanner(System.in);
			String input = "";
			System.out.printf("%-50s%-20s%-20s\n", "UUID", "CategoryName", "Description");
			System.out.println("-".repeat(100));
			System.out.printf("%-50s%-20s%-20s\n", cc.getCourseCategoryUUID().toString(), cc.getCourseCategoryName(),
					cc.getCourseCategoryDescription());
			do {
				System.out.printf("Input the Course Category Name, or Enter to skip: \n");
				input = sc.nextLine();
			} while (isCourseCategoryExist(input));
			if (!input.equals(""))
				cc.setCourseCategoryName(input);

			System.out.printf("Input the Course Category Description, or Enter to skip: \n");
			input = sc.nextLine();
			if (!input.equals(""))
				cc.setCourseCategoryDescription(input);
			return cc;
		} finally {
			writeLock.unlock();
		}

	}

	// public for CourseService modify method
	public CourseCategory getCourseCategoryFromName(String courseCategoryName) {
		for (CourseCategory cc : allCourseCategories) {
			if (cc.getCourseCategoryName().equals(courseCategoryName)) {
				return cc;
			}
		}
//		System.out.printf("Category '%s' is exist!!!\n", courseCategoryName);
		return null;

	}

	// To get Course Category object from UUID String
	private CourseCategory getCourseCategoryFromUUID(String courseCategoryUUID) {
		UUID uuid;
		try {
			uuid = UUID.fromString(courseCategoryUUID);
			return courseCategoriesHashmap.get(uuid);
		} catch (IllegalArgumentException e) {
			System.out.printf("Category UUID '%s' is not exist!!!\n", courseCategoryUUID);
			return null;
		}
	}

	// TODO:Exception message
	public <T> boolean isCourseCategoryExist(T courseCategory) {
		for (CourseCategory cc : allCourseCategories) {
			if (courseCategory instanceof String) {
				if (cc.getCourseCategoryName().equals((String) courseCategory)) {
					exception.errorMessage(MyException.CategoryExist);
					return true;
				}
			} else if (courseCategory instanceof CourseCategory) {
				if (cc.getCourseCategoryName().equals(((CourseCategory) courseCategory).getCourseCategoryName())) {
					exception.errorMessage(MyException.CategoryExist);
					return true;
				}
			} else { // Other T type
				exception.errorMessage(MyException.WrongParameterType);
				return false;
			}
		}

		return false;
	}

	@Test
	public void Test() {
		Scanner sc = new Scanner(System.in);
		CourseCategory cc1 = addCourseCategory("cc1", "des1");
		addCourseCategory("cc2", "des1");
		addCourseCategory("cc2", "des1");
		addCourseCategory("cc3", "des1");
		displayAllCategories();
//		System.out.printf("Input uuid to del the course category: \n");
//		delCourseCategory(sc.nextLine());
//		displayAllCategories();
//		System.out.printf("Input uuid to modify the course category: \n");
//		modifyCourseCategory(sc.nextLine());
//		displayAllCategories();
		displayAllCoursesOfTheCategory(cc1);
	}

//	@Test
	public void TestForCompareInstance() {

		List<CourseCategoryServiceJDKImplement> courseCategoryServices = new ArrayList<>();
		CourseCategoryServiceJDKImplement ccsStaticInstance1 = this.getInstance();
		CourseCategoryServiceJDKImplement ccsStaticInstance2 = this.getInstance();

		for (int i = 0; i < 3; i++) {
			courseCategoryServices.add(new CourseCategoryServiceJDKImplement());
		}
		courseCategoryServices.add(ccsStaticInstance1);
		courseCategoryServices.add(ccsStaticInstance2);

		System.out.printf("ccs0 equal ccs1: %b\n", courseCategoryServices.get(0).equals(courseCategoryServices.get(1)));
		System.out.printf("ccs1 equal ccs2: %b\n", courseCategoryServices.get(1).equals(courseCategoryServices.get(2)));
		System.out.printf("ccsStaticInstance1 equal ccs0: %b\n",
				ccsStaticInstance1.equals(courseCategoryServices.get(0)));
		System.out.printf("ccsStaticInstance1 equal ccsStaticInstance2: %b\n\n",
				ccsStaticInstance1.equals(ccsStaticInstance2));

		System.out.printf("Display HashCode: \n");
		for (CourseCategoryServiceJDKImplement ccs : courseCategoryServices) {
			System.out.printf("%s\n", Integer.toHexString(ccs.hashCode()));
		}
		System.out.println();

	}

}

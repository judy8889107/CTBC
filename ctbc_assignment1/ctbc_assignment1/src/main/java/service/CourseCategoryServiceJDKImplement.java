package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import lombok.Getter;
import model.Course;
import model.CourseCategory;
import exception.MyException;

public class CourseCategoryServiceJDKImplement implements CourseCategoryService {

	@Getter
	private List<CourseCategory> allCourseCategories = new ArrayList<>();

	private Map<UUID, CourseCategory> courseCategoriesHashmap = new HashMap<>();
	@Getter
	private MyException exception = new MyException(this);

	@Override
	public void displayAllCategories() {
		System.out.printf("%-50s%-20s%-20s\n", "UUID", "CategoryName", "Description");
		System.out.println("-".repeat(100));
		if (allCourseCategories.isEmpty()) {
			System.out.printf("(null)\n");
			return;
		}
		for (CourseCategory cc : allCourseCategories) {
			System.out.printf("%-50s%-20s%-20s\n", cc.getCourseCategoryUUID().toString(), cc.getCourseCategoryName(),
					cc.getCourseCategoryDescription());
		}
		System.out.printf("\n");
	}

	// Display all Course of a specific category by courseCategory(polynomial)
	public <T> void displayAllCoursesOfTheCategory(T courseCategory) {
		List<Course> allCourses = null;
		if (courseCategory instanceof String) {
			allCourses = getCourseCategoryFromName((String) courseCategory).getCoursesOfCategory();
			System.out.printf("All Courses of Category '%s': \n", courseCategory);
		} else if (courseCategory instanceof CourseCategory) {
			allCourses = ((CourseCategory) courseCategory).getCoursesOfCategory();
			System.out.printf("All Courses of Category '%s': \n",
					((CourseCategory) courseCategory).getCourseCategoryName());
		} else { //Other T type
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
	}

	@Override
	public CourseCategory addCourseCategory(String courseCategoryName, String courseCategoryDesription) {
		UUID uuid = UUID.randomUUID();
		if (exception.isCourseCategoryExist(courseCategoryName))
			return null;
		CourseCategory newCourseCategory = new CourseCategory(uuid, courseCategoryName, courseCategoryDesription);
		allCourseCategories.add(newCourseCategory);
		courseCategoriesHashmap.put(uuid, newCourseCategory);
		return newCourseCategory;
	}

	@Override
	public void delCourseCategory(String courseCategoryUUID) {
		CourseCategory cc = getCourseCategoryFromUUID(courseCategoryUUID);
		if (cc == null)
			return;
		allCourseCategories.remove(cc);
		courseCategoriesHashmap.remove(cc);
	}

	@Override
	public CourseCategory modifyCourseCategory(String courseCategoryUUID) {
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
		} while (exception.isCourseCategoryExist(input));
		if (!input.equals(""))
			cc.setCourseCategoryName(input);

		System.out.printf("Input the Course Category Description, or Enter to skip: \n");
		input = sc.nextLine();
		if (!input.equals(""))
			cc.setCourseCategoryDescription(input);
		return cc;
	}

	// public for CourseService modify method
	public CourseCategory getCourseCategoryFromName(String courseCategoryName) {
		for (CourseCategory cc : allCourseCategories) {
			if (cc.getCourseCategoryName().equals(courseCategoryName)) {
				return cc;
			}
		}
		System.out.printf("Category '%s' is exist!!!\n", courseCategoryName);
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

}

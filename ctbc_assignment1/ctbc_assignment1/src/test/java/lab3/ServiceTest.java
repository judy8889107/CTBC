package lab3;

import java.util.Scanner;

import service.CourseCategoryServiceJDKImplement;
import service.CourseServiceJDKImplement;

public class ServiceTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		CourseCategoryServiceJDKImplement categoryService = new CourseCategoryServiceJDKImplement();
		CourseServiceJDKImplement courseService = new CourseServiceJDKImplement();
		// TODO:Optimize setter method
		courseService.setCourseCategoryService(categoryService);
		String opt = "";
		do {

			System.out.printf("Welcome to Course Service System, please choose an option:\n");
			System.out.printf("(1)Add (2)Modify (3)Del (4)Display (5)Quit\n");
			opt = sc.nextLine();
			switch (opt) {
			// Add new Course
			case "1":

				System.out.printf("(1)Add new Course (2)Add new Course Category \n");
				opt = sc.nextLine();
				if (opt.equals("1")) {
					if (categoryService.getAllCourseCategories().isEmpty()) {
						System.out.printf("Please add some Course Category first. \n");
						break;
					}
					String coursName = "", coursePrice = "", courseCategory = "";
					do {
						System.out.printf("New Course Name: \n");
						coursName = sc.nextLine();
					} while (coursName.equals(""));
					do {
						System.out.printf("New Course Price: \n");
						coursePrice = sc.nextLine();
					} while (!coursePrice.matches("\\d+"));

					categoryService.displayAllCategories();
					do {
						System.out.printf("New Course Category: \n");
						courseCategory = sc.nextLine();
					} while (courseCategory.equals(""));
					courseService.addCourse(coursName, Integer.parseInt(coursePrice), courseCategory);
					courseService.displayAllCourses();
				}
				// Add new Course Category
				if (opt.equals("2")) {
					String categoryName = "", categoryDes = "";
					do {
						System.out.printf("New Course Category Name: \n");
						categoryName = sc.nextLine();
					} while (categoryName.equals(""));
					do {
						System.out.printf("New Course Category Description: \n");
						categoryDes = sc.nextLine();
					} while (categoryDes.equals(""));
					categoryService.addCourseCategory(categoryName, categoryDes);
					categoryService.displayAllCategories();
				}
				break;
			case "2":
				System.out.printf("(1)Modify Course (2)Modify Course Category \n");
				opt = sc.nextLine();
				if (opt.equals("1")) {
					System.out.printf("Input Course UUID: \n");
					courseService.modifyCourse(sc.nextLine());
				}
				if (opt.equals("2")) {
					System.out.printf("Input Course Category UUID: \n");
					categoryService.modifyCourseCategory(sc.nextLine());
				}
				break;
			case "3":
				System.out.printf("(1)Del Course (2)Del Course Category \n");
				if (opt.equals("1")) {
					System.out.printf("Input Course UUID: \n");
					courseService.delCourse(opt);
				}
				if (opt.equals("2")) {
					System.out.printf("Input Course Category UUID: \n");
					categoryService.delCourseCategory(opt);
				}
				break;
			case "4":
				System.out.printf("(1)Show all Courses (2)Show all Course Categories \n");
				opt = sc.nextLine();
				if (opt.equals("1"))
					courseService.displayAllCourses();
				if (opt.equals("2"))
					categoryService.displayAllCategories();
				break;
			}
		} while (!opt.equals("5"));
		System.out.printf("Press any key to exit...");
		sc.close();
	}
}

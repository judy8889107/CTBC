package lab1;

import java.util.UUID;

import model.Course;
import model.CourseCategory;

public class ModelTest {
	public static void main(String[] args) {
		CourseCategory category1 = new CourseCategory(UUID.randomUUID(), "category1", "description1");
		CourseCategory category2 = new CourseCategory(UUID.randomUUID(), "category2", "description2");

		Course course1 = new Course(UUID.randomUUID(), "course1", 500, category1);
		Course course2 = new Course(UUID.randomUUID(), "course2", 1500, category2);

		System.out.printf("Course Category:\n");
		System.out.printf("%-50s%-20s%-20s\n", category1.getCourseCategoryUUID().toString(),
				category1.getCourseCategoryName(), category1.getCourseCategoryDescription());
		System.out.printf("%-50s%-20s%-20s\n", category2.getCourseCategoryUUID().toString(),
				category2.getCourseCategoryName(), category2.getCourseCategoryDescription());
		System.out.println("-".repeat(100));
		System.out.printf("Course:\n");
		System.out.printf("%-50s%-20s%-20s%-20s\n", "UUID", "CourseName", "CoursePrice", "CourseCategory");
		System.out.printf("%-50s%-20s%-20s%-20s\n", course1.getCourseUUID().toString(), course1.getCourseName(),
				course1.getCoursePrice(), course1.getCourseCategory().getCourseCategoryName());
		System.out.printf("%-50s%-20s%-20s%-20s\n", course2.getCourseUUID().toString(), course2.getCourseName(),
				course2.getCoursePrice(), course2.getCourseCategory().getCourseCategoryName());
		System.out.printf("After reset Course Price:\n");

	}
}

package lab4;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import service.CourseCategoryServiceJDKImplement;
import service.CourseServiceJDKImplement;

public class ServiceObjectTestBeforeHandleThem {
	public static void main(String[] args) {
		Set<CourseCategoryServiceJDKImplement> allCategoryServices = new HashSet<>();
		Set<CourseServiceJDKImplement> allCourseServices = new HashSet<>();
		
		System.out.printf("Enter the number of object you want to generate: \n");
		int n = new Scanner(System.in).nextInt();
		System.out.printf("Add Service Object to Set... \n");
		for (int i = 0; i < n; i++) {
			allCategoryServices.add(new CourseCategoryServiceJDKImplement());
			allCourseServices.add(new CourseServiceJDKImplement());
		}
		System.out.printf("Complete. \n\n");

		System.out.printf("All Category Services size: %d\n", allCategoryServices.size());
		System.out.printf("All Course Services size: %d\n\n", allCourseServices.size());

		System.out.printf("Some test if Category objects equal: \n");
		for (int i = 1; i < n; i++) {
			System.out.printf("(%s, %s)\t", Integer.toHexString(allCategoryServices.toArray()[i - 1].hashCode()),
					Integer.toHexString(allCategoryServices.toArray()[i].hashCode()));
			System.out.printf("%b\n", allCategoryServices.toArray()[i - 1].equals(allCategoryServices.toArray()[i]));
		}
		System.out.printf("\nSome test if Course objects equal: \n");
		for (int i = 1; i < n; i++) {
			System.out.printf("(%s, %s)\t", Integer.toHexString(allCourseServices.toArray()[i - 1].hashCode()),
					Integer.toHexString(allCourseServices.toArray()[i].hashCode()));
			System.out.printf("%b\n", allCourseServices.toArray()[i - 1].equals(allCourseServices.toArray()[i]));
		}

	}
}

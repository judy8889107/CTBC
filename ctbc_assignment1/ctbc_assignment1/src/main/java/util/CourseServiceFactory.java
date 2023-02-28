package util;

import lombok.Getter;
import service.CourseCategoryServiceJDKImplement;
import service.CourseServiceJDKImplement;

public class CourseServiceFactory {

	@Getter
	private static CourseCategoryServiceJDKImplement categoryService = new CourseCategoryServiceJDKImplement();
	@Getter
	private static CourseServiceJDKImplement courseService = new CourseServiceJDKImplement();
	private static CourseServiceFactory facotry = new CourseServiceFactory();

	public static CourseServiceFactory getInstance() {
		return facotry;
	}

	// Assign static to member and private for Constructor can prevent user generate
	// multiple instance
	private CourseServiceFactory() {
		// TODO:Optimize setter method
		courseService.setCourseCategoryService(categoryService);
	}

}

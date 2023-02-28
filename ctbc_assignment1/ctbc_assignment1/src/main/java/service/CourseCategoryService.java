package service;

import model.CourseCategory;

public interface CourseCategoryService {
	
	public abstract void displayAllCategories();

	public abstract CourseCategory addCourseCategory(String courseCategoryName, String courseCategoryDesription);

	public void delCourseCategory(String courseCategoryUUID);

	public CourseCategory modifyCourseCategory(String courseCategoryUUID);

}

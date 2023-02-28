package service;

import model.Course;
import model.CourseCategory;

public interface CourseService {
	public abstract void displayAllCourses();

	public abstract <T> Course addCourse(String courseName, Integer coursePrice, T courseCategory);

	public <T> void delCourse(T courseCategoryUUID);

	public <T> Course modifyCourse(T courseCategoryUUID);
}

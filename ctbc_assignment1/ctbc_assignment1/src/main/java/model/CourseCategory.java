package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class CourseCategory {

	@Getter
	private List<Course> coursesOfCategory = new ArrayList<>();
	@Getter
	@Setter
	private UUID courseCategoryUUID;
	@Getter
	@Setter
	private String courseCategoryName;
	@Getter
	@Setter
	private String courseCategoryDescription;

	public CourseCategory() {
		super();
	}

	public CourseCategory(UUID courseCategoryUUID, String courseCategoryName, String courseCategoryDescription) {
		super();
		this.courseCategoryUUID = courseCategoryUUID;
		this.courseCategoryName = courseCategoryName;
		this.courseCategoryDescription = courseCategoryDescription;
	}

	
}

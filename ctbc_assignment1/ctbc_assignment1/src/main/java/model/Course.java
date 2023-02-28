package model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class Course {
	@Getter
	@Setter
	private UUID courseUUID;
	@Getter
	@Setter
	private String courseName;
	@Getter
	@Setter
	private Integer coursePrice;
	@Getter
	@Setter
	private CourseCategory courseCategory;

	public Course() {
		super();
	}

	public Course(UUID courseUUID, String courseName, Integer coursePrice, CourseCategory courseCategory) {
		super();
		this.courseUUID = courseUUID;
		this.courseName = courseName;
		this.coursePrice = coursePrice;
		this.courseCategory = courseCategory;
	}

}

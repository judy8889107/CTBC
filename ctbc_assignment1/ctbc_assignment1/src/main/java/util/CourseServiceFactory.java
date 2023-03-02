package util;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import lombok.Getter;
import service.CourseCategoryServiceJDKImplement;
import service.CourseServiceJDKImplement;

public class CourseServiceFactory {

	@Getter
	private static CourseCategoryServiceJDKImplement categoryService = CourseCategoryServiceJDKImplement.getInstance();
	@Getter
	private static CourseServiceJDKImplement courseService = CourseServiceJDKImplement.getInstance();

	private CourseServiceFactory() {
		super();
	}

	// BillPugh Singleton Implementation
	private static class SingletonHolder {
		private static final CourseServiceFactory facotory = new CourseServiceFactory();
	}

	public static CourseServiceFactory getInstance() {
		return SingletonHolder.facotory;
	}

	/********
	 * Test
	 ************************************************************************************************************************************/
//	@Test
	public void NormalTest() {
		CourseServiceFactory.getCategoryService().addCourseCategory("category1", "category1Des");
		CourseServiceFactory.getCategoryService().addCourseCategory("category1", "category1Des");
		CourseServiceFactory.getCategoryService().displayAllCategories();
	}

//	@Test
	public void ThreadTest() throws InterruptedException {
		Integer numberOfThread = 30;
		Thread[] allThreads = new Thread[numberOfThread];
		for (int i = 0; i < numberOfThread; i++) {
			allThreads[i] = new Thread(() -> {
				threadProcess();
			});
		}
		for (int i = 0; i < numberOfThread; i++) {
			allThreads[i].start();
		}
		CourseServiceFactory.getCategoryService().displayAllCategories();
		CourseServiceFactory.getCourseService().displayAllCourses();
	}

	@Test
	public void CopyOnWriteArrayListThreadTest() throws InterruptedException {
		final CopyOnWriteArrayList<Thread> allThreads = new CopyOnWriteArrayList<>();
		Integer numberOfThread = 50;
		for (int i = 0; i < numberOfThread; i++) {
			Thread t = new Thread(() -> threadProcess());
			allThreads.add(t);

		}
		for (Thread t : allThreads) {
			t.join();
			t.start();
		}
		
		
		CourseServiceFactory.getCategoryService().displayAllCategories();
		CourseServiceFactory.getCourseService().displayAllCourses();
	}

//	@Test
	public void threadPoolTest() throws InterruptedException {
		Integer numberOfThread = 50;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < numberOfThread; i++) {
			threadPool.execute(new Runnable() {

				@Override
				public void run() {
					threadProcess();
					System.out.printf("Thread %s done. \n", Thread.currentThread().toString());
				}
			});
		}

		threadPool.shutdown();
		threadPool.awaitTermination(10, TimeUnit.SECONDS);
		CourseServiceFactory.getCategoryService().displayAllCategories();
		CourseServiceFactory.getCourseService().displayAllCourses();
	}

	private Integer count = 0;

	private void threadProcess() {

		CourseServiceFactory factory = CourseServiceFactory.getInstance();
		System.out.println(factory.hashCode());
		count++;
		Integer coursePrice = new Random().nextInt(0, 5000);
		String categoryName = "category" + String.valueOf(count);
		String courseName = "course" + String.valueOf(count);
		CourseServiceFactory.getCategoryService().addCourseCategory(categoryName, categoryName + "Des");
		CourseServiceFactory.getCourseService().addCourse(courseName, coursePrice, categoryName);
		
	}

}

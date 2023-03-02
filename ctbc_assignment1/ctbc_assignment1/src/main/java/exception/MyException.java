package exception;

public class MyException {

	private volatile static MyException exception = new MyException();

	public final static int CourseExist = 0;
	public final static int CategoryExist = 1;
	public final static int WrongParameterType = 2;

	private MyException() {
		super();
	}

	public static MyException getInstance() {
		return exception;
	}

	public void errorMessage(int status) {
		switch (status) {
		case CourseExist:
			System.out.printf("Error: Course is exist \n");
			break;
		case CategoryExist:
			System.out.printf("Error: Category is exist \n");
			break;
		case WrongParameterType:
			System.out.printf("Error: Wrong parameter type \n");
			break;
		}

	}

}

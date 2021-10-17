package ssafy;

import org.apache.hadoop.util.ProgramDriver;

public class Driver {
	public static void main(String[] args) {
		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		System.out.println("Driver Accessed");
		try {
			pgd.addClass("reviewcount", ReviewWordcount.class, "A map/reduce program that performs review counting");
			pgd.driver(args);
			exitCode = 0;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}

		System.exit(exitCode);
	}

}

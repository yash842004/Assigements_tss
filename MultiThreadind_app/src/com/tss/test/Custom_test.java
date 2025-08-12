package com.tss.test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.tss.tasks.MyTask;
import com.tss.tasks.MyTask2;
import com.tss.tasks.MyTask3;

public class Custom_test {

	public static void main(String[] args) {
		List<MyTask3> tasks = Arrays.asList(new MyTask3(), new MyTask3(), new MyTask3(), new MyTask3());

		ExecutorService service = Executors.newSingleThreadExecutor();
		// ExecutorService service = Executors.newCachedThreadPool();
		// ExecutorService service = Executors.newFixedThreadPool(2);
//		service.submit(new MyTask());
//		service.submit(new MyTask2());
//		service.submit(new MyTask());
//		service.submit(new MyTask());

		try {

			List<Future<Integer>> futures = service.invokeAll(tasks);

			for (Future<Integer> future : futures) {
				System.out.println(future.get());
			}

			Future<Integer> future = service.submit(new MyTask3());
			System.out.println(future.get());

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			service.shutdown();
		}
	}

}

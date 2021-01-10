from time import perf_counter_ns
import threading
import sys


class benchmark(object):

    def __init__(self, warmup=0, iter=1, verbose=False, csv_file=None):
      self.warmup = warmup
      self.iter = iter;
      self.verbose = verbose;
      self.csv_file = csv_file;

    def __call__(self, func, *args, **kwargs):
      self.func = func

      def wrapper_benchmark(*args, **kwargs):
        warmup_times = []
        if self.warmup: #check if exec warm-up iterations
          if self.verbose:
            print("****** WARM-UP ******")
          for i in range(0,self.warmup):
            if self.verbose:
              print(f"- Warm-up : {i}") 
            start_time = perf_counter_ns()
            self.func(*args, **kwargs)
            end_time = perf_counter_ns()
            run_time = end_time - start_time
            warmup_times.append(run_time)

        if self.verbose:
          print("\n****** ITERATIONS ******")
        exec_times = []
        nanoseconds = 1000000000
        for i in range(0,self.iter): #exec iterations
          start_time = perf_counter_ns()
          self.func(*args, **kwargs)
          end_time = perf_counter_ns()
          run_time = end_time - start_time
          exec_times.append(run_time)
          if self.verbose:
            print(f"- Iteration: {i}\t Time: {run_time/nanoseconds:.4f} secs") #convert from nanoseconds to seconds

        accum = 0
        for elem in exec_times:
          accum = accum + elem

        accum = accum/len(exec_times) #take the avg of time execution
        accum = accum / nanoseconds #convert from nanoseconds to seconds

        print("\n****** SUMMARY ******")
        print(f"- Warm-up iterations: {self.warmup}")
        print(f"- Number of invokations: {self.iter}")
        print(f"- Avg time: {accum:.4f} secs")
        print(f"- Verbose: {self.verbose}")
        print(f"- Csv file: {self.csv_file}")

        if self.csv_file:
          f = open(self.csv_file, "w") #write into csv file
          f.write("run num, is warmup, timing (secs)\n")
          for (i,elem) in enumerate(warmup_times):
            f.write(f"{i}, true, {elem/nanoseconds:.4f}\n") #convert from nanoseconds to seconds
          for (i,elem) in enumerate(exec_times):
            f.write(f"{i}, false, {elem/nanoseconds:.4f}\n") #convert from nanoseconds to seconds
      return wrapper_benchmark(*args, **kwargs)


def test(func,threads,times):
  @benchmark(0,1,True,f"f_{threads}_{times}.csv")
  def run_test():
    def thread_task(): #task for the thread
      for i in range(0,times):
        func()

    threads_arr = []
    for i in range(0,threads): #create the threads
      threads_arr.append(threading.Thread(target=thread_task, args=()))

    for th in threads_arr:
      th.start()

    for th in threads_arr:
      th.join()

  return run_test


def fibonacci(n=31): 
    if n<0: 
        print("Incorrect input") 
    elif n==0: 
        return 0
    elif n==1: 
        return 1
    else: 
        return fibonacci(n-1)+fibonacci(n-2)


if __name__ == "__main__":
  print("\n****** TEST (1,16) ******")  
  test(fibonacci, 1, 16)
  print("\n\n****** TEST (2,8) ******")
  test(fibonacci, 2, 8)
  print("\n\n****** TEST (4,4) ******")
  test(fibonacci, 4, 4)
  print("\n\n****** TEST (8,2) ******")
  test(fibonacci, 8, 2)


""" QUESTION: Discuss briefly the results in a comment in the Python file."""
""" 
  ANSWER: As expected the execution of the method test with multithread is more 
  slower than execute it with a single thread. The cause is the GIL, with multithread 
  program each thread need to way that the thread in execution release the lock of the 
  interpreter while with a single thread there isn't this problem
"""


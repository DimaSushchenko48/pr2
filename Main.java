import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

class MultiplyArray {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Вхідний масив
        int[] array = new int[100];

        // Заповнення масиву випадковими числами в діапазоні [-100, 100]
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(201) - 100; // Генеруємо числа в діапазоні [-100, 100]
        }

        // Множник
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть множник: ");
        int multiplier = scanner.nextInt();

        // Розділення масиву на частини та створення пулу потоків
        int numThreads = 4; // Кількість потоків
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Список для зберігання результатів
        CopyOnWriteArrayList<Integer> resultList = new CopyOnWriteArrayList<>();

        // Розбиття масиву на частини
        int chunkSize = array.length / numThreads;
        List<Callable<Void>> tasks = new ArrayList<>();

        // Створення завдань
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? array.length : (i + 1) * chunkSize;

            final int chunkStart = start;
            final int chunkEnd = end;

            tasks.add(() -> {
                for (int j = chunkStart; j < chunkEnd; j++) {
                    resultList.add(array[j] * multiplier);  // Множимо елементи і додаємо в результат
                }
                return null;
            });
        }

        // Виконання завдань за допомогою Future
        List<Future<Void>> futures = executor.invokeAll(tasks);

        // Чекаємо завершення всіх завдань
        for (Future<Void> future : futures) {
            future.get();
        }

        // Закриття Executor
        executor.shutdown();

        // Виведення результатів
        System.out.println("Результат після множення:");
        for (int i = 0; i < resultList.size(); i++) {
            System.out.print(resultList.get(i) + " ");
        }
    }
}
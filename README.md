# Divide-and-Conquer Algorithms: Implementation and Analysis

Учебный проект по классическим алгоритмам Divide-and-Conquer с безопасными шаблонами рекурсии, системой метрик и отчётом.

## Архитектура и метрики
- Метрики: время (System.nanoTime), глубина рекурсии, сравнения, аллокации.
- Класс `algo.Metrics`: thread-local счётчик глубины (enterRecursion/exitRecursion), атомарные счётчики сравнений/аллокаций, snapshot для CSV.
- Минимизация аллокаций: `MergeSort` использует один буфер; `ClosestPair` — копия массива точек + вспомогательный массив полосы.
- Контроль глубины стека:
  - `QuickSort`: рекурсия только в меньшую часть; большая часть — итеративно (tail recursion elimination). Типичная глубина O(log n).
  - `Select`: MoM5 и хвостовая рекурсия в нужную (меньшую) часть.

## Реализованные алгоритмы
1) MergeSort (Master Case 2)
   - Рекуррентность: T(n) = 2T(n/2) + Θ(n). По Master Theorem, Case 2: Θ(n log n).
   - Практика: глубина ≈ ⌈log2 n⌉; линейное слияние, буфер переиспользуется; cut-off на insertion sort для малых n.

2) QuickSort (рандомизированный, bounded stack)
   - Идея: случайный pivot, рекурсия только в меньшую часть; большая — в цикле. Ожидаемая глубина O(log n).
   - Оценка: ожидаемое время Θ(n log n); худший случай Θ(n^2) маловероятен при случайном pivot.

3) Deterministic Select (Median-of-Medians, MoM5)
   - Группы по 5, берём медианы, затем медиану медиан как pivot; in-place partition.
   - Рекуррентность (интуитивно Akra–Bazzi): T(n) ≤ T(⌈n/5⌉) + T(7n/10) + O(n) ⇒ Θ(n).

4) Closest Pair of Points (2D)
   - Сортировка по x, деление пополам, полоса по y (проверки ≤ 7–8 соседей).
   - Рекуррентность: T(n) = 2T(n/2) + O(n) ⇒ Θ(n log n).

## Планы измерений и графиков
- CLI `algo.BenchCLI` выводит CSV: algo,n,trial,time_ns,comparisons,allocations,max_depth.
- Размеры n: 1e2, 5e2, 1e3, 5e3, 1e4, 5e4, 1e5 (по времени/возможностям машины).
- Построить графики (внешним инструментом): time vs n, depth vs n, comparisons vs n.
- Обсуждение факторов: прогрев JIT, кэш, распределение данных, GC.

## Быстрый старт
- Сборка: `mvn -q -DskipTests package`
- Тесты: `mvn -q test`
- CLI примеры:
  - `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo mergesort --n 100000 --trials 5`
  - `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo quicksort --n 100000 --trials 5`
  - `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo select --n 500000 --k 12345 --trials 3`
  - `java -cp target/divide-and-conquer-1.0.0.jar algo.BenchCLI --algo closestpair --n 20000 --trials 3`

## Тестирование
- Сортировка: корректность на случайных и «плохих» массивах (reverse, dups), глубина QS: ≤ ~2·⌊log2 n⌋ + O(1) типично.
- Select: сравнение с Arrays.sort(a)[k] на 100 тестах.
- Closest Pair: сверка с O(n^2) на малых n (≤ 2000).

## Гайд по Git workflow (для вашей репы)
- Ветки: `main`, `feature/metrics`, `feature/mergesort`, `feature/quicksort`, `feature/select`, `feature/closest`, `feature/cli`.
- История коммитов (пример): init → feat(metrics) → feat(mergesort) → feat(quicksort) → refactor(util) → feat(select) → feat(closest) → feat(cli) → docs(report) → release: v1.0.

## Краткие выводы (заполнить после прогонов)
- Теория vs замеры: где совпадает, где расходится; причины (кэш, GC, распределения).

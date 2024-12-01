## Домашнее задание

Определение нужного размера хипа

### Цель:

На примере простого приложения понять какое влияние оказывают сборщики мусора.

### Описание:

1. Есть готовое приложение (модуль homework).
2. Запустите его с размером хипа 256 Мб и посмотрите в логе время выполнения.
   Пример вывода:
   spend msec:18284, sec:18
3. Увеличьте размер хипа до 2Гб, замерьте время выполнения.
4. Историю запусков записывайте в таблицу.
5. Определите оптимальный размер хипа, т.е. размер, превышение которого,
   не приводит к сокращению времени выполнения приложения.
6. Оптимизируйте работу приложения.
   Т.е. не меняя логики работы (но изменяя код), сделайте так, чтобы приложение работало быстро
   с минимальным хипом.
7. Повторите измерения времени выполнения программы для тех же значений размера хипа.
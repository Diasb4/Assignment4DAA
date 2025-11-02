# Smart Campus — Assignment 4

## Что реализовано
- SCC (Kosaraju)
- Condensation graph build
- Topological ordering (Kahn)
- Shortest and Longest path on DAG (edge-weighted)
- Dataset generator (data/)
- Metrics counters and timing hooks
- JUnit tests under src/test/java

## Запуск
1. Сборка:
   mvn clean package
2. Генерация датасетов:
   mvn -q exec:java -Dexec.mainClass="com.example.Util.DataGenerator"
   Датасеты появятся в /data
3. Прогон всех датасетов:
   mvn -q exec:java -Dexec.mainClass="com.example.Main"

## Формат данных
JSON:
{
  "nodes": [0,1,2,...]  или "nodes": n
  "edges": [{"from":u,"to":v,"weight":w}, ...]
}

## Выбор модели
Используется модель **edge weights** (веса на рёбрах). Кратчайшие и длиннейшие пути считаются по сумме весов рёбер.

## Тесты
mvn test

## Отчёт
README.md содержит инструкции, PDF-отчёт находится в /report/report.pdf или /report/README.md с таблицами времени и метрик.

# Smart Campus — Assignment 4

## What is implemented
- SCC (Kosaraju)
- Condensation graph build
- Topological ordering (Kahn)
- Shortest and Longest path on DAG (edge-weighted)
- Dataset generator (data/)
- Metrics counters and timing hooks
- JUnit tests under src/test/java

## Launch
1. Assembly:
   mvn clean package
2. Dataset generation:
mvn -q exec:java -Dexec.mainClass="com.example.Util.DataGenerator"
   Datasets will appear in /data
3. Run through all datasets:
mvn -q exec:java -Dexec.mainClass="com.example.Main"

## Data format
JSON:
{
  "nodes": [0,1,2,...] or "nodes": n
"edges": [{"from":u,"to":v,"weight":w}, ...]
}

## Model selection
The **edge weights** model is used. The shortest and longest paths are calculated based on the sum of the edge weights.

##
mvn test tests

## Report
README.md It contains instructions, and the PDF report is located in /report/report.pdf or /report/README.md with tables of time and metrics.
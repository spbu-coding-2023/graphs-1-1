# Graphses - Graph Analysis & Visualization App

![Coverage](badges/jacoco.svg)
![Branches](badges/branches.svg)library/build/reports/jacoco/test/html/index.html

## Description 

Graphses is a desktop application designed for visualizing and analyzing graphs. It provides a user-friendly interface for visualizing various types of graphs and analyzing them using different algorithms.

## Roadmap

<details>
<summary>Tasks/Ideas for releases in the future</summary>

### Algorithm Implementation
- [ ] **Implement Main Algorithms**
    - [X] Graph layout on the plane
    - [ ] Key vertex identification
    - [ ] Community search
- [ ] **Implement Classical Algorithms**
    - [ ] Strongly connected components extraction (directed graph)
    - [ ] Bridge search (undirected graph)
    - [X] Cycle search for a given vertex (directed and/or undirected graph)
    - [ ] Minimum spanning tree construction (undirected graph)
    - [ ] Shortest path between a pair of vertices by Dijkstra's algorithm (directed and/or undirected graph)
    - [X] Shortest path between a pair of vertices by the Ford-Bellman algorithm (directed and/or undirected graph)

### Graph Saving and Loading
- [ ] **Implement Graph Saving and Loading**
    - [X] Read and save graphs from/to files (CSV, JSON, etc.)
    - [ ] Save and read from SQLite
    - [ ] Save and read from Neo4j

### Testing
- [ ] **Write Unit Tests**
    - [X] Unit tests for each implemented algorithm !implemented(PathFind, BellmanFord, CycleSearch)
    - [X] Unit tests for graph saving and loading functionality !implemented(JSON)
- [ ] **Write Integration Tests**
    - [ ] Integration tests covering main user scenarios
- [ ] **Test Documentation**
    - [ ] Document test scenarios and justifications
- [ ] **UI Testing**
    - [ ] Implement UI tests for meaningful user interactions

</details>

## Contributing



## Authors

- [kinokotakenoko9](https://www.github.com/kinokotakenoko9)
- [Mukovenkov-Roman-Sergeyevich](https://www.github.com/Mukovenkov-Roman-Sergeyevich)
- [mshipilov5](https://www.github.com/mshipilov5)

## License

[MIT](LICENSE.txt)
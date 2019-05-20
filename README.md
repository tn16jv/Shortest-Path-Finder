# Shortest Path Finder
Java application that will use artificial intelligence to approximate solutions to the Travelling Salesman problem in
any given set of locations. It also provides a visualization of the path taken.

Travelling Salesman Problem: visit each of the designated locations once, in the most efficient way.

### Running
* Have a file full of X Y coordinates separated by either spaces or commas. You can use the ones found in 'examples'
* Specify the number of threads (how many threads will be running searches), the number of searches, and number of
iterations. Good baseline is 10 threads, 50 searches, 10000 iterations.
* The distance of the shortest path found will be shown in the box.

Note: drag your cursor across the map to see the corresponding X Y coordinates.

&nbsp;
<p align = "center">
    <img src="https://github.com/tn16jv/Shortest-Path-Finder/blob/VertexMarkers/images/example1.png" alt="Good Example">
    Standard run
</p>

&nbsp;
<p align = "center">
    <img src="https://github.com/tn16jv/Shortest-Path-Finder/blob/VertexMarkers/images/example2.png" alt="Bad Example">
    Essentially a random path. See how random and disjointed it is from a learned path.
</p>
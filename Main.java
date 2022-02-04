package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] tmp1 = scanner.nextLine().split(" ");


        int numberOfVertex = Integer.parseInt(tmp1[0]);
        int numberOfEdges = Integer.parseInt(tmp1[1]);

        String[] numbers = scanner.nextLine().split(" ");


        Graph myGraph = new Graph(numberOfEdges, numberOfVertex, numbers);

        for (int i = 0;i < numberOfEdges;i++){
            String[] temp = scanner.nextLine().split(" ");
            myGraph.addEdges(Integer.parseInt(temp[2]), myGraph.getVertex(temp[1]), myGraph.getVertex(temp[0]));
        }

        String[] input;
//        myGraph.dijkstraAlgorithm(myGraph.vertexes.get(0));

        HashMap<Vertex, HashMap<Vertex, Integer>> finalDijkstra = new HashMap<>();
        ArrayList<Vertex> newVertexes = new ArrayList<>();
        ArrayList<Vertex> notJoined = new ArrayList<>();

        for (int j = 0;j < myGraph.vertexes.size();j++){
            notJoined.add(myGraph.vertexes.get(j));
        }

        while (true){
            input = scanner.nextLine().split(" ");

            if (input[0].equals("test")){
                myGraph.depthFirstSearch(myGraph.vertexes.get(0));

                for (Vertex vertex : myGraph.vertexes)
                    vertex.visit = false;
            }

            if (input[0].equals("exit"))
                break;

            if (input[0].equals("join")){
                finalDijkstra.put(myGraph.findVertex(input[1]), myGraph.dijkstraAlgorithm(myGraph.findVertex(input[1])));
                newVertexes.add(myGraph.findVertex(input[1]));
                notJoined.remove(myGraph.findVertex(input[1]));
            }

            if (input[0].equals("left")){
                finalDijkstra.remove(myGraph.findVertex(input[1]));
                newVertexes.remove(myGraph.findVertex(input[1]));
                notJoined.add(myGraph.findVertex(input[1]));
            }

            if (input[0].equals("calculate")){
                float minimum = Integer.MAX_VALUE;
                ArrayList<Vertex> finalResult = new ArrayList<>();
                float currentScore = 0;

                for (Vertex v1 : notJoined){
                    for (Vertex v2 : newVertexes){
                        for (Vertex v3 : newVertexes){
                            currentScore += Math.abs(finalDijkstra.get(v3).get(v1) - finalDijkstra.get(v2).get(v1));
                        }
                    }
                    currentScore = (float) (currentScore / newVertexes.size());
                    if (minimum > currentScore){
                        finalResult.clear();
                        finalResult.add(v1);
                        minimum = currentScore;
                    }
                    else if (minimum == currentScore){
                        finalResult.add(v1);
                    }
                    currentScore = 0;
                }

                for (Vertex vertex : finalResult){
                    System.out.print(vertex.number + " ");
                }
                System.out.println();
            }
        }
    }
}

class Graph {
    int numberOfVertexes;
    int numberOfEdges;
    HashMap<Vertex, ArrayList<Edge>> myGraph;
    ArrayList<Vertex> vertexes;

    public Graph (int numberOfEdges, int numberOfVertexes, String[] nameOfVertexes) {
        this.numberOfEdges = numberOfEdges;
        this.numberOfVertexes = numberOfVertexes;

        myGraph = new HashMap<Vertex, ArrayList<Edge>>();
        vertexes = new ArrayList<>();

        for (int i = 0;i < numberOfVertexes;i++){
            Vertex vertex = new Vertex(nameOfVertexes[i]);
            myGraph.put(vertex, new ArrayList<Edge>());
            vertexes.add(vertex);
        }
    }

    public Vertex getVertex (String a){
        for(Vertex v : vertexes)
            if(v.number.equals(a))
                return v;
            return null;
    }

    public Vertex findVertex(String num){
        for (Vertex v : vertexes){
            if (v.number.equals(num))
                return v;
        }
        return null;
    }

    public void addEdges(int weight, Vertex source, Vertex destination){
        myGraph.get(source).add(new Edge(destination, weight));
        myGraph.get(destination).add(new Edge(source, weight));
    }

    public void depthFirstSearch(Vertex vertex){
        vertex.setVisit();
        System.out.print(vertex.number + " ");
        ArrayList<Vertex> neighbors = new ArrayList<>();

        for (int i = 0;i < myGraph.get(vertex).size();i++){
            for (int j = 0;j < vertexes.size();j++){
                if (myGraph.get(vertex).get(i).destination.number.equals(vertexes.get(j).number)){
                    neighbors.add(vertexes.get(j));
                }
            }
        }

        if (neighbors == null)
            return;

        for (Vertex vertex1 : neighbors)
            if (!(vertex1.visit))
                depthFirstSearch(vertex1);
    }

    public Vertex calculateMinimumDistance(HashMap<Vertex, Integer> distances, ArrayList<Vertex> notVisited){
        Vertex finalResult = null;
        int finalDistance = Integer.MAX_VALUE;
        for (Vertex vertex : notVisited){
            if (distances.get(vertex) < finalDistance){
                finalResult = vertex;
                finalDistance = distances.get(vertex);
            }
        }
        return finalResult;
    }

    public HashMap<Vertex, Integer> dijkstraAlgorithm(Vertex vertex) {
        HashMap<Vertex, Integer> distances = new HashMap<Vertex, Integer>();

        for (int j = 0;j < numberOfVertexes;j++){
            distances.put(vertexes.get(j), Integer.MAX_VALUE);
        }

        distances.put(vertex, 0);
        ArrayList<Vertex> notVisited = new ArrayList<>(vertexes);

        while (!notVisited.isEmpty()){
            Vertex current = calculateMinimumDistance(distances, notVisited);
            notVisited.remove(current);

            for (Edge edge : myGraph.get(current)){
                int currentDistance = distances.get(current) + edge.weight;
                if (currentDistance < distances.get(edge.destination)){
                    distances.put(edge.destination, currentDistance);
                }
            }
        }
        return distances;
    }
}

class Vertex {
    String number;
    boolean visit;

    public Vertex(String number) {
        this.number = number;
        this.visit = false;
    }

    public void setVisit(){
        this.visit = true;
    }
}

class Edge {
    Vertex destination;
    int weight;

    public Edge(Vertex destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}


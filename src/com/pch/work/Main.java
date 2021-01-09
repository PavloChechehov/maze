package com.pch.work;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Main {

    private static final List<Point> DIRECTIONS = new LinkedList<>();

    static {
        DIRECTIONS.add(new Point(0, 1));
        DIRECTIONS.add(new Point(1, 0));
        DIRECTIONS.add(new Point(0, -1));
        DIRECTIONS.add(new Point(-1, 0));
    }

    public static void main(String[] args) {
        int[] arr =
                //x |^|
                //y ->
                        {0, 0, 0, 0, 0,
                        0, 0, 4, 0, 0,
                        0, 4, 4, 4, 4,
                        0, 4, 0, 4, 0,
                        0, 0, 0, 4, 0};
        List<List<Point>> map = createMap(arr, 5);

        Main main = new Main();
        List<List<Point>> possibleDirections = main.getAllPossibleWays(map, new Point(3, 3), new Point(4, 3), 5);

        for (List<Point> possibleDirection : possibleDirections) {
            System.out.println(possibleDirection);
        }
    }

    private static List<List<Point>> createMap(int[] arr, int length) {
        List<List<Point>> map = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                Point point = new Point(i, j, arr[i * length + j]);
                points.add(point);
            }
            map.add(points);
        }
        return map;
    }

    private List<List<Point>> getAllPossibleWays(List<List<Point>> map,
                                                 Point startPoint,
                                                 Point previous,
                                                 int dices) {
        List<List<Point>> possibleDirections = new CopyOnWriteArrayList<>();
        List<Point> startDirection = new ArrayList<>();
        startDirection.add(previous);
        startDirection.add(startPoint);
        possibleDirections.add(startDirection);
        for (int i = 0; i < dices; i++) {
            getNewDirections(map, possibleDirections);
        }
        return possibleDirections;
    }

    private void getNewDirections(List<List<Point>> map, List<List<Point>> directions) {
        for (List<Point> way : directions) {
            Point current = way.get(way.size() - 1);
            Point previous = way.get(way.size() - 2);
            List<Point> nextDirections = getNextDirection(map, current, previous);
            for (Point direction : nextDirections) {
                Point point = goToNextCell(map, way.get(way.size() - 1), direction);
                List<Point> newWay = new ArrayList<>(way);
                newWay.add(point);
                directions.remove(way);
                directions.add(newWay);
            }
        }
    }

    private List<Point> getNextDirection(List<List<Point>> map, Point current, Point previous) {
        int x = previous.getX() - current.getX();
        int y = previous.getY() - current.getY();
        Point direction = new Point(x, y);

        return DIRECTIONS.stream()
                .filter(d -> !d.equals(direction)) // todo: delete reverse direction
                .filter(d -> isPossibleWay(map, current, d))
                .collect(Collectors.toList());
    }

    private boolean isPossibleWay(List<List<Point>> map, Point current, Point direction) {
        Point nextCell = goToNextCell(map, current, direction);
        return nextCell != null && nextCell.getValue() != 0;
    }

    private Point goToNextCell(List<List<Point>> map, Point current, Point direction) {
        int xDirection = current.getX() + direction.getX();
        int yDirection = current.getY() + direction.getY();
        if (xDirection < 0 || yDirection < 0 || xDirection >= map.size() || yDirection >= map.size()) return null;
        return map.get(xDirection).get(yDirection);
    }
}

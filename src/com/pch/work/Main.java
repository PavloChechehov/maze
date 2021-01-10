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

    private static List<List<Point>> map;

    public static void main(String[] args) {
        int[] arr =
                //x |^|
                //y ->
                {0, 0, 0, 0, 0,
                        0, 0, 4, 0, 0,
                        0, 4, 4, 4, 4,
                        0, 4, 0, 4, 0,
                        0, 0, 0, 4, 0};
        map = createMap(arr, 5);

        Main main = new Main();
        List<List<Point>> possibleDirections = main.getAllPossiblePaths(new Point(3, 3), new Point(4, 3), 5);

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

    private List<List<Point>> getAllPossiblePaths(Point startPoint,
                                                  Point previous,
                                                  int dices) {
        List<List<Point>> possibleDirections = new CopyOnWriteArrayList<>();
        List<Point> startDirection = new ArrayList<>();
        startDirection.add(previous);
        startDirection.add(startPoint);
        possibleDirections.add(startDirection);
        for (int i = 0; i < dices; i++) {
            getNewDirections(possibleDirections);
        }
        return possibleDirections;
    }


    private void getNewDirections(List<List<Point>> paths) {
        for (List<Point> path : paths) {
            Point current = path.get(path.size() - 1);
            Point previous = path.get(path.size() - 2);
            for (Point direction : DIRECTIONS) {

                if (isReversePath(current, previous, direction)) {
                    continue;
                }

                if (!isPossiblePath(current, direction)) {
                    continue;
                }

                Point point = getNextPoint(current, direction);
                List<Point> newPath = new ArrayList<>(path);
                newPath.add(point);
                paths.remove(path);
                paths.add(newPath);
            }
        }
    }

    private Point getNextPoint(Point current, Point direction) {
        int yDirection = current.getY() + direction.getY();
        int xDirection = current.getX() + direction.getX();
        return map.get(xDirection).get(yDirection);
    }

    private boolean isReversePath(Point current, Point previous, Point direction) {
        int x = previous.getX() - current.getX();
        int y = previous.getY() - current.getY();
        return direction.getX() == x && direction.getY() == y;
    }

    private boolean isPossiblePath(Point current, Point direction) {
        int xDirection = current.getX() + direction.getX();
        int yDirection = current.getY() + direction.getY();
        if (xDirection < 0
                || yDirection < 0
                || xDirection >= map.size()
                || yDirection >= map.size())
            return false;

        return map.get(xDirection).get(yDirection).getValue() != 0;
    }
}

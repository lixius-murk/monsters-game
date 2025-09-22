package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class App {
    public static void main(String[] args) throws FileNotFoundException {
        File examp = new File("input.txt");
        Scanner s = new Scanner(examp);

        int width = s.nextInt();
        int height = s.nextInt();
        int n = s.nextInt();
        if (n == 0 || n > width * height) {
            return;
        }


        char[][] field = new char[height][width];
        for (int i = 0; i < height; i++) {
            Arrays.fill(field[i], '-');
        }

        for (int i = 0; i < n; i++) {
            int row = s.nextInt();
            int col = s.nextInt();
            field[row][col] = 'M';
        }
        printField(field, n);
        int result = solve(field, n);

        System.out.println(result);

        s.close();
    }


    public static void printField(char[][] field, int n) {
        System.out.println("Число монстров: " + n);
        System.out.println("Игровое поле:");
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();


        }
    }

    public static boolean isWalkable(char[][] field, int r, int c) {
        return r >= 0 && r < field.length &&
                c >= 0 && c < field[0].length &&
                field[r][c] != 'M';
    }

    private static int handleSingleMonster(char[][] field) {
        int r = -1, c = -1;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 'M') {
                    r = i;
                    c = j;
                    break;
                }
            }
        }

        int minR = r, maxR = r;
        int minC = c, maxC = c;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newR = r + dir[0];
            int newC = c + dir[1];
            if (newR >= 0 && newR < field.length &&
                    newC >= 0 && newC < field[0].length) {
                minR = Math.min(minR, newR);
                maxR = Math.max(maxR, newR);
                minC = Math.min(minC, newC);
                maxC = Math.max(maxC, newC);
            }
        }

        return (maxR - minR + 1) * (maxC - minC + 1);
    }
    public static int[] getBorder(char[][] field) {
        int minR = Integer.MAX_VALUE, minC = Integer.MAX_VALUE;
        int maxR = -1, maxC = -1;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 'M') {
                    minR = Math.min(minR, i);
                    maxR = Math.max(maxR, i);
                    minC = Math.min(minC, j);
                    maxC = Math.max(maxC, j);
                }
            }
        }

        return new int[]{minR, maxR, minC, maxC};
    }

    private static int solve(char[][] field, int n) {
        if(n == 1){return handleSingleMonster(field);}
        if (n ==0){return 0;}
        int[] boards = getBorder(field);
        int minR = boards[0], maxR = boards[1];
        int minC = boards[2], maxC = boards[3];
        int side = Math.max(maxR - minR + 1, maxC - minC + 1);
        int worstCost = side*side;


        final int[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        List<int[]> allMonsters = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 'M') {
                    allMonsters.add(new int[]{i, j});
                }
            }
        }

        for (int[] monster : allMonsters) {
            int r = monster[0], c = monster[1];
            for (int[] d : dir) {
                int newR = r + d[0], newC = c + d[1];
                if (isWalkable(field, newR, newC)) {
                    field[r][c] = '-';
                    field[newR][newC] = 'M';

                    int newCost = calculateSquare(field);
                    worstCost = Math.max(worstCost, newCost);

                }
            }
        }
        return worstCost;
    }

    private static int calculateSquare(char[][] field) {
        int[] bounds = getBorder(field);

        int minR = bounds[0], maxR = bounds[1];
        int minC = bounds[2], maxC = bounds[3];

        int side = Math.max(maxR - minR + 1, maxC - minC + 1);
        return side * side;
    }
}

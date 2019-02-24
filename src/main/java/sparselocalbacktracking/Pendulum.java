/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparselocalbacktracking;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author thorsten
 */
public class Pendulum implements Domain {

    static class Point {

        double x, y, vx, vy, decayx, decayy;

        public Point(double x, double y, double decayx, double decayy) {
            this.x = x;
            this.y = y;
            this.decayx = decayx;
            this.decayy = decayy;
            vx = 0;
            vy = 0;
        }
    }

    static class Line {

        Point p1, p2;
        double length;

        public Line(Point p1, Point p2, double length) {
            this.p1 = p1;
            this.p2 = p2;
            this.length = length;
        }
    }

    final ArrayList<Point> points = new ArrayList<>();
    final ArrayList<Line> lines = new ArrayList<>();
    Point agent, pendulum;

    public Pendulum() {
        agent = new Point(0.5, 0.5, 0.99, 0);
        pendulum = new Point(0.6, 1.0, 0.99, 0.99);
        points.add(agent);
        points.add(pendulum);
        lines.add(new Line(agent, pendulum, 0.5));
    }

    @Override
    public int getNumActions() {
        return 1;
    }

    @Override
    public int getNumStates() {
        return 3;
    }

    @Override
    public void act(double[] action) {
        agent.vx += 0.1 * action[0];

        for (int k = 0; k < 5; ++k) {
            for (Line l : lines) {
                double dx = l.p2.x - l.p1.x;
                double dy = l.p2.y - l.p1.y;
                double len = Math.sqrt(dx * dx + dy * dy);
                double f = 0.3 * (len - l.length) / len;

                l.p1.vx += f * dx;
                l.p1.vy += f * dy;
                l.p2.vx -= f * dx;
                l.p2.vy -= f * dy;
            }

            for (Point p : points) {
                p.vy -= 0.01;

                p.vx *= p.decayx;
                p.vy *= p.decayy;

                p.x += 0.1 * p.vx;
                p.y += 0.1 * p.vy;
            }

            if (agent.x < 0.1) {
                agent.x = 0.1;
                agent.vx = 0;
            }

            if (agent.x > 0.9) {
                agent.x = 0.9;
                agent.vx = 0;
            }
        }
    }

    @Override
    public double[] getState() {
        return new double[]{agent.x, pendulum.x, pendulum.y};
    }

    @Override
    public double getReward() {
        return pendulum.y;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.black);
        Rectangle br = g.getClipBounds();

        g.fillRect(0, 0, br.width, br.height);

        for (Line l : lines) {
            g.setColor(Color.yellow);

            g.drawLine(
                    (int) (l.p1.x * br.width),
                    (int) ((1.0 - l.p1.y) * br.height),
                    (int) (l.p2.x * br.width),
                    (int) ((1.0 - l.p2.y) * br.height)
            );
        }

        for (Point p : points) {
            g.setColor(Color.blue);

            g.drawArc(
                    (int) (p.x * br.width) - 10,
                    (int) ((1.0 - p.y) * br.height) - 10,
                    20, 20, 0, 360
            );
        }

        g.setColor(Color.red);
        g.drawLine(0, br.height / 2, br.width, br.height / 2);
    }

}

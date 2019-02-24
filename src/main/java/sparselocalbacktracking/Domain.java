/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparselocalbacktracking;

import java.awt.Graphics;

/**
 *
 * @author thorsten
 */
public interface Domain {

    int getNumActions();

    void act(double[] action);

    int getNumStates();

    double[] getState();

    double getReward();

    void render(Graphics g);
}

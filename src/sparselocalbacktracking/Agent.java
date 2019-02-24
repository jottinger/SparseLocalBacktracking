/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparselocalbacktracking;

import java.util.ArrayList;

/**
 *
 * @author thorsten
 */
public class Agent {

    private static class State {

        public final RNN rnn;
        public double reward = 0.0;
        public int mutationCounter = 0;

        public State(RNN rnn, double r) {
            this.rnn = rnn;
            this.reward = r;
        }

    }

    private State best = null;
    private final ArrayList<State> stack = new ArrayList<>();
    private int counter = 0;
    private final int numStack;
    private final int numCounter;
    private final int numMutations;

    public Agent(int numInputs, int numNeurons, int numOutputs, int numStack, int numCounter, int numMutations) {
        RNN rnn = new RNN(numInputs, numNeurons, numOutputs);
        stack.add(new State(rnn, 0));
        best = new State(rnn, -Double.MAX_VALUE);
        this.numStack = numStack;
        this.numCounter = numCounter;
        this.numMutations = numMutations;
    }

    public double[] act(double[] state) {
        State s = stack.get(stack.size() - 1);
        return s.rnn.compute(state);
    }

    public RNN getBest() {
        return best.rnn;
    }
    
    public void learn(double reward) {
        State s = stack.get(stack.size() - 1);
        s.reward += reward * reward;
        counter += 1;

        if (counter >= numCounter) {
            counter = 0;

            if (s.reward > best.reward) {
                best = s;
            }

            if (stack.size() >= numStack) {
                //System.out.println(stack.size());

                stack.remove(stack.size() - 1);
                s = stack.get(stack.size() - 1);
            }

            while (s.mutationCounter >= numMutations) {
                //System.out.println(stack.size());

                stack.remove(stack.size() - 1);
                s = stack.get(stack.size() - 1);

                if (stack.size() == 1) {
                    System.out.println("yo");
                    stack.set(0, best);
                    s = best;
                    s.mutationCounter = 0;
                }
            }

            s.mutationCounter += 1;
            stack.add(new State(s.rnn.mutate(), 0));
        }
    }
}

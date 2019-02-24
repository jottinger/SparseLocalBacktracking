/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sparselocalbacktracking;

/**
 *
 * @author thorsten
 */
public class RNN {

    private final int numInputs;
    private final int numNeurons;
    private final int numOutputs;
    private final double[][] weights;
    private final double[][] inputWeights;
    private final double[] activations;
    private final double[] potentials;

    public RNN(int numInputs, int numNeurons, int numOutputs) {
        this.numInputs = numInputs;
        this.numNeurons = numNeurons;
        this.numOutputs = numOutputs;

        weights = new double[numNeurons][numNeurons];
        inputWeights = new double[numInputs][numNeurons];

        for (int i = 0; i < numNeurons; ++i) {
            for (int j = 0; j < numNeurons; ++j) {
                weights[i][j] = 0.1 * (Math.random() - 0.5);
            }
        }

        for (int i = 0; i < numInputs; ++i) {
            for (int j = 0; j < numNeurons; ++j) {
                inputWeights[i][j] = 0.1 * (Math.random() - 0.5);
            }
        }

        activations = new double[numNeurons];
        potentials = new double[numNeurons];
    }

    public RNN copy() {
        RNN rnn = new RNN(numInputs, numNeurons, numOutputs);

        for (int i = 0; i < numNeurons; ++i) {
            for (int j = 0; j < numNeurons; ++j) {
                rnn.weights[i][j] = weights[i][j];
            }
        }

        for (int i = 0; i < numInputs; ++i) {
            for (int j = 0; j < numNeurons; ++j) {
                rnn.inputWeights[i][j] = inputWeights[i][j];
            }
        }

        return rnn;
    }

    public RNN mutate() {
        RNN rnn = copy();

        for (int i = 0; i < numNeurons; ++i) {
            for (int j = 0; j < numNeurons; ++j) {
                rnn.weights[i][j] += 1 * (Math.random() - 0.5);
            }
        }

        for (int i = 0; i < numInputs; ++i) {
            for (int j = 0; j < numNeurons; ++j) {
                rnn.inputWeights[i][j] += 0.4 * (Math.random() - 0.5);
            }
        }

        return rnn;
    }

    public double[] compute(double[] state) {
        assert state.length == numInputs;

        for (int k = 0; k < numNeurons; ++k) {
            potentials[0] = 1.0;
            for (int i = 0; i < numNeurons; ++i) {
                activations[i] = 0;
                for (int j = 0; j < numNeurons; ++j) {
                    activations[i] += weights[j][i] * potentials[j];
                }
                for (int j = 0; j < numInputs; ++j) {
                    activations[i] += inputWeights[j][i] * state[j];
                }
            }
            potentials[0] = 1.0;
            for (int i = 0; i < numNeurons; ++i) {
                potentials[i] = Math.atan(activations[i]);
            }
            potentials[0] = 1.0;
        }

        double[] out = new double[numOutputs];
        for (int i = 0; i < numOutputs; ++i) {
            out[i] = potentials[numNeurons - 1 - i];
        }
        
        return out;
    }

    public void reset() {
        for (int i = 0; i < numNeurons; ++i) {
            activations[i] = 0;
            potentials[i] = 0;
        }
    }
}

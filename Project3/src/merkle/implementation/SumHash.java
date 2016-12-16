package merkle.implementation;

import merkle.hash.HashFunction;

/**
 * TASK 3
 *
 * @author Jun Soo Kim kim1893
 * @pso PSO-17
 * @date 10/24/16
 */
public class SumHash implements HashFunction {

    /**
     * This function returns an integer (cast to a string) which is
     * the sum of all the bytes in the <i>input</i> String
     * You can cast bytes to integer using <i>(int)</i>
     * You can cast integers to String using <i>String.valueOf</i>
     */
    @Override
    public String hashBlock(String input) throws Exception {
        int hash = 0;
        byte[] bytes = input.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            hash += (int)bytes[i];
        }
        return String.valueOf(hash);
    }

    @Override
    public String concatenateHash(MerkleTree.Node leftNode, MerkleTree.Node rightNode) throws Exception {
        return String.valueOf(Integer.parseInt(leftNode.getHash()) + Integer.parseInt(rightNode.getHash()));
    }
}

package merkle.implementation;

import merkle.IMerkleTree;
import merkle.IServer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * TASK 2
 * TODO: IMPLEMENT generateResponse
 *
 * @author Jun Soo Kim kim1893
 * @pso PSO-17
 * @date 10/21/16
 */
public class Server extends IServer {

    /**
     * Given a node to verify identified by <i>blockToTest</i>
     * which corresponds to the node received by calling <i>merkleTree.getNode(blockToTest)</i>
     * this function generates the path siblings which are required for verification
     * The returned list should contains Nodes in order from node to the root, i.e. bottom up
     */
    public List<IMerkleTree.Node> generateResponse(int blockToTest) {
        List<IMerkleTree.Node> verificationList = new LinkedList<>();
        int times = 0;
        while (blockToTest != 1) {
            if (blockToTest % 2 == 1) { //right child node
                if (times == 0) {
                    verificationList.add(merkleTree.getNode(blockToTest));
                }
                verificationList.add(merkleTree.getNode(blockToTest - 1));
            } else { //left child node
                if (times == 0) {
                    verificationList.add(merkleTree.getNode(blockToTest));
                }
                verificationList.add(merkleTree.getNode(blockToTest + 1));
            }
            times++;
            blockToTest = (int) Math.floor(blockToTest / 2);
        }
        return verificationList;
    }
    public void printList(List<IMerkleTree.Node> list) {
        int i;
        System.out.println("\nVERIFICATION LIST");
        for (i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            }
    }
}

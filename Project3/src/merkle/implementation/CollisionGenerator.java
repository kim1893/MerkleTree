package merkle.implementation;

import merkle.Configuration;
import merkle.ICollisionGenerator;
import merkle.IMerkleTree;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * TASK 4 BONUS
 * THIS IS FOR BONUS POINTS
 * DO NOT DO THIS BEFORE COMPLETING EVERYTHING ELSE FIRST
 * TODO: IMPLEMENT generateCollision
 *
 * @author Jun Soo Kim kim1893
 * @pso PSO-17
 * @date 10/29/16
 *
 * COLLABORATORs: Joel Uban Elvin Uthuppan
 */

public class CollisionGenerator extends ICollisionGenerator {

    /**
     * Given a <i>merkleTree</i> this function needs to
     * generate a file which will generate the merkleTree
     * The file then has to be dumped to <i>outputFile</i>
     * Basic code for writing blocks to a file is provided.
     */
    @Override
    public void generateCollision(File outputFile, IMerkleTree merkleTree) throws Exception {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            byte[] bytes;


            int i;
            int j;
            int numLeaves;
            int position = merkleTree.getTree().length - 1;

            int quotient;

            if (merkleTree.getTree().length % 2 == 0) {
                numLeaves = merkleTree.getTree().length / 2;
            } else {
                numLeaves = (merkleTree.getTree().length / 2) + 1;
            }
            MerkleTree.Node[] leaves_array = new MerkleTree.Node[numLeaves];

            i = 0;
            while (i < numLeaves) {
                leaves_array[i] = merkleTree.getNode(position--);
                i++;
            }

            j = 0;
            while (j < numLeaves) {

                quotient = Integer.valueOf(leaves_array[j].getHash()) / 127; //integer division

                int num = quotient * 127; //get int * 127
                int other = Integer.valueOf(leaves_array[j].getHash()) - num; //master hash - (int* 127)

                String spoof = "";
                i = 0;

                while (i < quotient) {
                    spoof += (char) 127;
                    i++;
                }
                spoof += (char) other;

                i = spoof.length();
                while (i < 1024) {
                    spoof += '\0';
                    i++;
                }

                bytes = spoof.getBytes();
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.flush();
                j++;
            }
            i = 0;
            j = 0;
        }
    }
    //DELETE LATER

    public static void main(String[] args) {
        MerkleTree m = new MerkleTree();
        CollisionGenerator c = new CollisionGenerator();

        try {
            File file = new File("project_3_data/md5.txt");
            File ofile = new File("project_3_data/output.txt");
            String hash = m.buildSumHash(file);

            c.generateCollision(ofile, m);

            String spoofedHash = m.buildSumHash(ofile);

            System.out.println("\nOriginal hash: " + hash);
            System.out.println("Spoofed hash: " + spoofedHash);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*Search Tree class
 * Your implementation goes in this file
 */

/*
kim1893
PSO-17
Completetion Date: 11/14/16
Collaborators: Elvin Uthuppan and Joel Uban
 */

import java.util.*;

public class SearchTree {

    private Node root; //The root of the RB Tree
    private JobTable jobs; //The jobTable
    //The following variables are used for measuring utilities, do not change them!
    private int[] machineLoads;
    private int numOfMachine;
    private int scheduled;
    private int requests;
    //You can add any other variables here if needed

    //Create a balanced search tree consisting of all the machines initially empty
    public SearchTree(int space, int numOfMachine) {
        jobs = new JobTable();
        scheduled = requests = 0;
        this.numOfMachine = numOfMachine;
        machineLoads = new int[numOfMachine];
        machineLoads[0] = 0;
        root = new Node(0, space, 0);
        for (int i = 1; i < numOfMachine; i++) {
            insertNewNode(i, space, 0);
            machineLoads[i] = 0;
        }

    }

    public SearchTree() {
    } //added

    public SearchTree(JobTable jt) {
        jobs = jt;
    }

    public void insertNewNode(int machine, int free, int numjobs) {
        root = RedBlackBST.insert(root, new Node(machine, free, numjobs));
    }

    //TODO: All parts needed for you implementation are in functions listed below:

    //Find the machine with just enough free space to schedule a job
    //Update the free size and number of jobs on the machine
    //Return machine id... -1 if no such machine exists
    public int scheduleJobMinSpace(int jobid, int size) {


        //System.out.println("Scheduleing " + jobid + " with size " + size + " ..."); //added

        Node m;
        Node temp;
        Node bestOption;
        requests++;
        /* Do not modify the code above */
        if (count(size) == 0) {
            return -1;
        }

        if (root == null) {
            return -1;
        }

        m = root;
        bestOption = null; //most optimal node
        int value = Integer.MAX_VALUE;
        while (true) {
            if (m.free == size) { //first instance, automatically break
                bestOption = m; //this is best option
                break;
            } else if (m.free > size) { //if there is too much space go to left
                if (m.free < value) { //if available space is less than prev free value
                    value = m.free;
                    bestOption = m; //update best option
                }
                m = m.left; //go left even to find an even lesser value
            } else {
                m = m.right; //go right when greater
            }
            if (m == null) { //when you reach the end, break
                break;
            }
        }

        if (bestOption == null) {
            return -1;
        }

        //System.out.println("Success! Scheduled to " + bestOption.id + " with " + (bestOption.free - size) + " more free space"); //added

        temp = new Node(bestOption.id, bestOption.free, bestOption.numjobs);

        root = RedBlackBST.delete(root, bestOption);
        bestOption = new Node(temp.id, temp.free, temp.numjobs);
        bestOption.addJob(size);
        root = RedBlackBST.insert(root, bestOption);

        Node bestOption1 = get(root, bestOption);
        jobs.addJob(jobid, size, bestOption1);

        m = bestOption1;

        //System.out.println("-------------------------------------------");
        //inOrder(root);
        //System.out.println("-------------------------------------------");

		/* Do not modify the following part */
        scheduled++;
        machineLoads[m.id]++;
        return m.id;
    }

    public static Node get(Node root, Node key) {
        Node x = root;
        while (x != null) {
            if (key.free < x.free) {
                x = x.left;
            } else if (key.free > x.free) {
                x = x.right;
            } else {
                if (key.id == x.id) {
                    return x;
                } else if (key.id > x.id) {
                    x = x.right;
                } else {
                    x = x.left;
                }
            }
        }
        return null;
    }


    //Find the machine with enough free space and minimum number of jobs to schedule a job
    //Update the free size and number of jobs on the machine
    //Return machine id... -1 if no such machine exists
    public int scheduleJobMinJob(int jobid, int size) {

        Node m;
        requests++;

        //System.out.println("Scheduleing " + jobid + " ..."); //added

        if (count(size) == 0) {
            return -1;
        }

        if (root == null) {
            return -1;
        }

        Node bestOption = null;
        bestOption = minJob(root, size, bestOption);

        if (bestOption == null) {
            return -1;
        }

        //System.out.println("Success! Scheduled to " + bestOption.id + " with " + (bestOption.free - size) + " more free space"); //added
        //System.out.println("Success! Scheduled to " + bestOption.id);

        Node temp = new Node(bestOption.id, bestOption.free, bestOption.numjobs);

        root = RedBlackBST.delete(root, bestOption);
        bestOption = new Node(temp.id, temp.free, temp.numjobs);
        bestOption.addJob(size);
        root = RedBlackBST.insert(root, bestOption);

        Node bestOption1 = get(root, bestOption);
        jobs.addJob(jobid, size, bestOption1);

        m = bestOption1;

        //System.out.println("-------------------------------------------");
        //inOrder(root);
        //System.out.println("-------------------------------------------");

		/* Do not modify the following part */
        machineLoads[m.id]++;
        scheduled++;
        return m.id;
    }

    public Node minJob(Node root, int size, Node bestOption) {
        Node w;
        int jobsOfM;
        int minJobs;

        Node u = root;
        if (u == null) {
            return bestOption;
        }
        //find where it's possible i schedueld to a node with not enough free space
        if (u.free < size) {
            return minJob(u.right, size, bestOption);
        } else if (u.free >= size){ //covers each right subtree
            if (u.right == null || u.numjobs <= u.right.minJobsNode.numjobs) { //if null or node has less space than all of right
                if (bestOption == null) {
                    bestOption = u;
                    return minJob(u.left, size, bestOption);
                }
                if (u.numjobs == bestOption.numjobs) {
                    if (u.free < bestOption.free) {
                        bestOption = u;
                    } else if (u.free == bestOption.free) {
                        if (u.id < bestOption.id) {
                            bestOption = u;
                        }
                    }
                } else if (u.numjobs < bestOption.numjobs) {
                    bestOption = u;
                }
                return minJob(u.left, size, bestOption);
            } else if (u.numjobs > u.right.minJobsNode.numjobs) {//if right least node is better than current node
                if (bestOption == null) {
                    bestOption = u.right.minJobsNode;
                    return minJob(u.left, size, bestOption);
                }
                if (bestOption.numjobs == u.right.minJobsNode.numjobs) {
                    if (bestOption.free > u.right.minJobsNode.free) {
                        bestOption = u.right.minJobsNode;
                    } else if (bestOption.free == u.right.minJobsNode.free) {
                        if (bestOption.id > u.right.minJobsNode.id) {
                            bestOption = u.right.minJobsNode;
                        }
                    }
                } else if (bestOption.numjobs > u.right.minJobsNode.numjobs) {
                    bestOption = u.right.minJobsNode;
                }
                return minJob(u.left, size, bestOption);
            }
        }
        return bestOption;
    }

    //Update the free space and number of jobs on machine releasing a job
    public void releaseJob(int jobid) {

        Node m = jobs.jobMachine(jobid); //node in tree

        if (m != null) {
            int space = jobs.jobSize(jobid); //amount of free space taken by the job
            Node temp = new Node(m.id, m.free, m.numjobs); //copy of m

            root = RedBlackBST.delete(root, m); //delete
            m = temp;
            m.removeJob(space); //update m with deleted job
            root = RedBlackBST.insert(root, m); //then insert new one

            Node temp1 = get(root, m);
            jobs.deleteJob(jobid, temp1);

            machineLoads[m.id]--;
        }
    }

    public int count(int free) {
        return count(free, root);
    }

    //Return the number of machines that have at least given free space
    public int count(int free, Node x) {
        if (x == null) {
            return 0;
        }
        int determ;
        if (free <= x.free) //enough space on all right subtree
            determ = 1;
        else  //if no space then go to right subtree
            determ = -1;

        if (determ < 0) //no free space then go to right subtree
            return count(free, x.right); //recursive call on right subtree
        else { //enough space on all of right
            return 1 + size(x.right) + count(free, x.left); //recursive call on left subtree + size + itself
        }
    }

    public int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    /*
    * DO NOT EDIT THE FOLLOWING FUNCTION
    * IT IS INVOLVED IN MEASURING THE UTILITIES FOR EXPERIMENTAL SECTION
    */
    public void measureUtility() {
        double ideal = 0.0;
        double medianload = 0.0;
        ArrayList<Integer> loads = new ArrayList<Integer>();
        int size = 0;
        for (int i = 0; i < numOfMachine; i++) {
            int load = machineLoads[i];
            loads.add(load);
            size += load;
        }

        int len = loads.size();

        Collections.sort(loads);
        if (size % 2 == 0) {
            medianload = loads.get(len / 2);
        } else {
            medianload = (loads.get(len / 2) + loads.get(len / 2 + 1)) / 2;
        }
        System.out.println(size);
        ideal = size / (double) numOfMachine;
        double fairness = medianload / ideal;
        double thoroughput = scheduled / (double) requests;
        System.out.format("Fairness: %f, Thoroughput: %f\n", fairness, thoroughput);
    }
    /*
    * DO NOT EDIT THE FUNCTION ABOVE
	*/

    public void inOrder(Node root) { //added
        if (root != null) {
            inOrder(root.left);
            System.out.println(root);
            inOrder(root.right);
        }
    }

    public static void main(String[] args) { //added
        //SearchTree(space, number of machines)
        SearchTree st = new SearchTree(50, 5);
        Random random = new Random();
    /*
		int i;
		int numMachines = 15;

		for (i = 0; i < numMachines; i++) {
			st.insertNewNode(i, random.nextInt(35) + 5, 0);
		}
	*/
        System.out.println(st.root);
        System.out.println(st.root.left);
        System.out.println(st.root.right);
        System.out.println();

        st.inOrder(st.root);
        System.out.println();
        System.out.println(st.scheduleJobMinJob(3, 10));
        System.out.println(st.scheduleJobMinJob(3, 10));
        System.out.println(st.scheduleJobMinJob(3, 40));
        System.out.println(st.scheduleJobMinJob(3, 40));
        System.out.println(st.scheduleJobMinJob(3, 20));

        System.out.println();
        st.inOrder(st.root);

		/*System.out.println("\nTotal number of machines: " + numMachines + "\n");

		for (i = 1; i <= 35; i++) {
			System.out.printf("Number of machines with at least %d free space: %d\n", i, st.count(i));
		}*/


        //System.out.println("Count: " + st.count(5));
    }
}

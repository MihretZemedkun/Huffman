/*Mihret Zemedkun
Huffman coding is a lossless compression algorithm that achieves
a very impressive rate of compression. It's a general method for finding the
optimal variable-length prefix-codes.
To run:
compile, run this Huff.java file, and you will get a "huffCodingResults.txt"
output with the result of the encoding algorithm.
 */

import java.io.*;
import java.util.Map;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Huff {
  private Map<Character, Integer> freqTable = new Hashtable<Character, Integer>();
  private Map<Character, String> codeTable = new Hashtable<Character, String>();
  private PriorityQueue<Node> aQ = new PriorityQueue<Node>();
  private Node root;
  private static String textFileName = "medTale.txt";
  private static String binaryOutFileName = "huffCodingResults.txt";

  private class Node implements Comparable{
    Character letter; // the letter for this node;
    Integer freq;  // the letter's frequence
    Node left, right;

    //Costructor1
    public Node(Character letter, Integer freq){
      this.letter=letter;
      this.freq=freq;
    }

    //Constructor2
    public Node(Character letter, Integer freq, Node left, Node right){
      this.letter=letter;
      this.freq=freq;
      this.left=left;
      this.right=right;
    }

    //IMPLEMENTING THE COMPARETO() METHOD, SO THAT NODES ARE COMPARABLE BY THEIR FREQUENCY.
    public int compareTo(Object o){
        if (this.freq < ((Node)o).freq)
            return -1;
        else if (this.freq > ((Node)o).freq)
            return 1;
        else
            return 0;
 // FOR COMPILARION, RETURN 0. PLEASE MODIFY IT.
    }
  }

  //Constructor for Huff class
  public Huff(){}

  public static void main(String[] args) throws Exception {
    Huff aHuff = new Huff();
    aHuff.buildFreqTable(textFileName); // build the frequency table based upon input text file.
    aHuff.buildHuffTree(); // construct the Huffman tree.
    aHuff.getCodes(); // encode letters based on the Huffman tree constructed.
    aHuff.outputBinaryFile(textFileName, binaryOutFileName); // output the encoding bitstream to an external file.
  }

  public void buildFreqTable(String fileName) throws Exception {
    /** READ THROUGH THE INPUT FILE, GET THE FREQUENCY OF EACH APPEARING LETTER, AND KEEP IT IN A HASHTABLE FREQTABLE.
     */

    FileReader fr;
    BufferedReader bf;

    try {
      fr = new FileReader(fileName);
      bf = new BufferedReader(fr);
      int value =0;
      while ((value = bf.read())!=-1){
        char c = (char)value; // read letter one by one from a text file.
        // YOUR CODES HERE TO MAINTAIN THE FREQUENCY TABLE.
        if (freqTable.containsKey(c)) {
            Integer frequency = freqTable.get(c);
            frequency++;
            freqTable.put(c, frequency);
        }
        else {
            freqTable.put(c, 1);
        }
      }
      bf.close();
    }
    catch (FileNotFoundException e1) {
      System.out.println("The file does not exist!");
    }
    catch (IOException e2) {
      System.out.println("Cannot open that file!");
    }
  }

  public void buildHuffTree(){
      //CONSTRUCT A HUFFMAN TREE.

      for (char x = 0; x < 128; x++){
          if (freqTable.containsKey(x)){
                aQ.add(new Node(x, freqTable.get(x), null, null));
          }
      }
      while (aQ.size() > 1) {
          char c = 'p';
          Node newLeft  = aQ.remove();
          Node newRight = aQ.remove();
          Node newParent = new Node(c, newLeft.freq + newRight.freq, newLeft, newRight);
          root = newParent;
          aQ.add(newParent);
      }
  }

  public Map<Character,String> getCodes(){
      List<String> jesuList = new ArrayList<String>();
      getCodesHelper(this.root, jesuList);
      return codeTable;
  }

  public void getCodesHelper(Node node, List<String> dList){
      if (node.left == null && node.right == null) {
          String output = "";
          for (int i = 0; i < dList.size(); i++){
              output += dList.get(i);

          }
          codeTable.put(node.letter, output);
      }
      if (node.left!=null){
          dList.add("0");
          getCodesHelper(node.left, dList);
      }
      if (node.right!=null){
          dList.add("1");
          getCodesHelper(node.right, dList);
      }
      if (dList.size() != 0){
          dList.remove(dList.size() - 1);
      }

  }


  public void outputBinaryFile(String inFileName, String outFileName) throws Exception {
    FileReader frIn = new FileReader(inFileName);
    BufferedReader bfIn = new BufferedReader(frIn);
    FileWriter frOut = new FileWriter(outFileName);
    BufferedWriter bfOut=new BufferedWriter(frOut);
    int value =0;
    while ((value = bfIn.read())!=-1){
        char c = (char)value;
        // RE-READ THE TEXT FILE, OUTPUT THE ENCODING RESULTS FOR EACH LETTER.
        bfOut.write(codeTable.get(c));
    }
    bfIn.close();
    bfOut.flush();
    bfOut.close();
  }
}

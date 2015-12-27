package spelling;

import java.util.*;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should ignore the word's case.
	 * That is, you should convert the string to all lower case as you insert it. */
	public boolean addWord(String word)
	{
	    //TODO: Implement this method.
		word = word.toLowerCase();
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if(node.getChild(c) == null){
                node.insert(c);
            }
            node = node.getChild(c);
        }
        if (!node.endsWord()) {
            node.setEndsWord(true);
            size += 1;
            return true;
        } else {
            return false;
        }
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    //TODO: Implement this method
	    return size;
	}
	
	
	/** Returns whether the string is a word in the trie */
	@Override
	public boolean isWord(String s) 
	{
	    // TODO: Implement this method
        s = s.toLowerCase();
        TrieNode node = root;
        for (char c : s.toCharArray()) {
            if(node.getChild(c) == null){
                return false;
            }
            node = node.getChild(c);
        }
		return node.endsWord();
	}

	/** 
	 *  * Returns up to the n "best" predictions, including the word itself,
     * in terms of length
     * If this string is not in the trie, it returns null.
     * @param text The text to use at the word stem
     * @param n The maximum number of predictions desired.
     * @return A list containing the up to n best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 prefix = prefix.toLowerCase();
         TrieNode node = root;
         List<String> res = new LinkedList<String>();
         for (char c : prefix.toCharArray()) {
             if (node.getChild(c) == null){
                 return res;
             }
             node = node.getChild(c);
         }
         if (node.endsWord()){
             res.add(node.getText());
             numCompletions -= 1;
         }
         Queue<TrieNode> q = new LinkedList<>();
         while (numCompletions > 0){
             Set<Character> candidates = node.getValidNextCharacters();
             for ( Character c : candidates) {
                 TrieNode nextNode = node.getChild(c);
                 if(nextNode.endsWord()){
                     res.add(nextNode.getText());
                     numCompletions -= 1;
                     if (numCompletions == 0) {
                         return res;
                     }
                 }
                 q.add(nextNode);
             }
             if (!q.isEmpty()) {
                node = q.remove();
             } else {
                 return res;
             }
         }
         return res;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}
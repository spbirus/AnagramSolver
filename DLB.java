import java.util.*;

public class DLB implements DictInterface {

    private char terminateChar = '$';

    private Nodelet root;

    public DLB() {
        root = new Nodelet();
    }

    public boolean add(String s) {
        //make the string lowercase
        s = s.toLowerCase();
        //add the terminator char to the end of the string needing added
        s = s + terminateChar;
        StringBuilder word = new StringBuilder(s);
        
        Nodelet current = root;
        
        boolean add = false;
        for(int i = 0; i < s.length(); i++){
            char character = s.charAt(i);
            NodeletResult result = addChildNodelet(current, character);
            current = result.nodelet;
            add = result.add;
        }
        
        return add;
    }

    public int searchPrefix(StringBuilder s) {
        Nodelet current = root;
        //iterates through the entire stringbuilder to check if the string exists
        //if it doesn't it returns 0
        for(int i = 0; i <s.length(); i++){
            char value = s.charAt(i);
            current = getChildNodelet(current, value);
            if(current == null){
                //neither a word or a prefix
                return 0;
            }
        }
        
        //this checks where the terminater character is located at to tell if the string is a prefix, word or both
        Nodelet terminateNodelet =  getChildNodelet(current, terminateChar);
        if(terminateNodelet == null){
            //is no terminate character
            //this means that it is a prefix since if would have returned 0 if it was neither
            return 1;
        } else if(terminateNodelet.siblingRef == null){
            //terminate nodelet has no other siblings 
            //is a word
            return 2;
        } else {
            //terminate nodelet has siblings
            //is both
            return 3;
        }
    }

    public int searchPrefix(StringBuilder s, int start, int end) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    private Nodelet getSiblingNodelet(Nodelet sibStart, char value){
        Nodelet nextSib = sibStart;
        while(nextSib != null){
            if(nextSib.letter == value){
                //you have found a match so break
                break;
            } 
            
            //otherwise keep iterating to the next node
            nextSib = nextSib.siblingRef;
        }
        return nextSib;
    }
    
    private Nodelet getChildNodelet(Nodelet parent, char value){
        //grabs a child of a parent nodelet and then goes through all the sibling references to find correct one
        return getSiblingNodelet(parent.childRef, value);
    }
    
    private NodeletResult addChildNodelet(Nodelet parentNodelet, char value){
        if(parentNodelet.childRef == null){
            //create a new child node
            parentNodelet.childRef = new Nodelet(value);
            return new NodeletResult(parentNodelet.childRef, true);
        } else {
            //parent nodelet already has a node so add a sibling nodelet
            return addSiblingNodelet(parentNodelet.childRef, value);
        }
    }
    
    private NodeletResult addSiblingNodelet(Nodelet sibStart, char value){
        if(sibStart == null){
            sibStart = new Nodelet(value);
            return new NodeletResult(sibStart, true);
        } else {
            Nodelet nextSib = sibStart;
            while (nextSib.siblingRef != null){
                if(nextSib.letter == value){
                    break;
                }
                nextSib = nextSib.siblingRef;
            }
            if(nextSib.letter == value){
                //found a nodelet with the data we are looking for
                return new NodeletResult(nextSib, false);
            } else {
                //create new nodelet at end of chain as the nodelet was not found
                nextSib.siblingRef = new Nodelet(value);
                return new NodeletResult(nextSib.siblingRef, true);
            }
        }
    }

    //nodelet class
    //has a letter character, a sibling reference and a child reference
    private static class Nodelet {

        private char letter;
        private Nodelet siblingRef;
        private Nodelet childRef;

        public Nodelet() {
            siblingRef = null;
            childRef = null;
        }
        
        public Nodelet(char value){
            this(value, null, null);
        }
        
        public Nodelet(char letter, Nodelet siblingRef, Nodelet childRef){
            this.letter = letter;
            this.siblingRef = siblingRef;
            this.childRef = childRef;
        }
    }

    //result of an add operation
    //easy way to store if a node has been added
    private static class NodeletResult{
        Nodelet nodelet;
        boolean add;
        
        public NodeletResult(Nodelet nodelet, boolean add){
            this.nodelet = nodelet;
            this.add = add;
        }
    }
}


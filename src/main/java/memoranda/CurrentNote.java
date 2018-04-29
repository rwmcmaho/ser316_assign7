package main.java.memoranda;

import java.util.Collection;
import java.util.Vector;

import main.java.memoranda.interfaces.INote;
import main.java.memoranda.interfaces.INoteListener;

public class CurrentNote {

	private static INote currentNote = null;
    private static Vector noteListeners = new Vector();

    public static INote get() {
        return currentNote;
    }

    //TASK 2-1 SMELL WITHIN A CLASS
    //method previously called private static method which is not used anywhere else;
    //  single non branching loop does not need it's only method. 
    public static void set(INote note, boolean toSaveCurrentNote) {
        for (int i = 0; i < noteListeners.size(); i++) {
            ((INoteListener)noteListeners.get(i)).noteChange(note,toSaveCurrentNote);
        }
        currentNote = note;
    }

    public static void reset() {
//    	 set toSave to true to mimic status quo behaviour only. the appropriate setting could be false
        set(null, true);
    }

    public static void addNoteListener(INoteListener nl) {
        noteListeners.add(nl);
    }

    public static Collection getChangeListeners() {
        return noteListeners;
    }

    //TASK 2-1 SMELL WITHIN A CLASS
    /*private static void noteChanged(INote note, boolean toSaveCurrentNote) {
        for (int i = 0; i < noteListeners.size(); i++) {
            ((INoteListener)noteListeners.get(i)).noteChange(note,toSaveCurrentNote);
		}
    }*/
}

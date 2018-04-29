package main.java.memoranda;

import java.util.Vector;

import nu.xom.Element;
import nu.xom.Elements;

//TASK 2-2 SMELL BETWEEN CLASSES - made it's own class, previously sub class in both EventsManager and NoteListImpl

public class Day {
    Element dEl = null;

    public Day(Element el) {
        dEl = el;
    }

    public int getValue() {
        return new Integer(dEl.getAttribute("day").getValue()).intValue();
    }

    /*
     * public Note getNote() { return new NoteImpl(dEl);
     */

    public Element getElement() {
        return dEl;
    }
    
    public NoteElement getNote(String d) {
        if (dEl == null) 
            return null;
        Elements ne = dEl.getChildElements("note");
        
        for (int i = 0; i < ne.size(); i++)
            if (ne.get(i).getAttribute("refid").getValue().equals(d))
                return new NoteElement(ne.get(i));
        //return createDay(d);
        return null;
    }

    public NoteElement createNote(String d) {
        Element el = new Element("note");
//      el.addAttribute(new Attribute("refid", d));
/*            el.addAttribute(new Attribute("day", new Integer(d).toString()));
                    el.addAttribute(
            new Attribute(
                "date",
                new CalendarDate(
                    10,
                    10,
                    2004).toString()));
*/                      
        dEl.appendChild(el);
        return new NoteElement(el);
    }

    public Vector getNotes() {
        if (dEl == null)
            return null;
        Vector v = new Vector();
        Elements ds = dEl.getChildElements("note");
        for (int i = 0; i < ds.size(); i++)
            v.add(new NoteElement(ds.get(i)));                                    
        return v;
    }
    
    public class NoteElement {
        Element nEl;
        
        public NoteElement(Element el) {
            nEl = el;
        }
        
        public Element getElement() {
            return nEl;
        }
    }
}

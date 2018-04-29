package main.java.memoranda;

import java.util.Vector;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

//TASK 2-2 SMELL BETWEEN CLASSES - made it's own class, previously sub class in both EventsManager and NoteListImpl

public class Year {
    Element yearElement = null;

    public Year(Element el) {
        yearElement = el;
    }

    public int getValue() {
        return new Integer(yearElement.getAttribute("year").getValue()).intValue();
    }

    public Month getMonth(int m) {
        Elements ms = yearElement.getChildElements("month");
        String mm = new Integer(m).toString();
        for (int i = 0; i < ms.size(); i++)
            if (ms.get(i).getAttribute("month").getValue().equals(mm))
                return new Month(ms.get(i));
        //return createMonth(m);
        return null;
    }

    //TASK 2-2 SMELL BETWEEN CLASSES - method changed to public
    public Month createMonth(int m) {
        Element el = new Element("month");
        el.addAttribute(new Attribute("month", new Integer(m).toString()));
        yearElement.appendChild(el);
        return new Month(el);
    }

    public Vector getMonths() {
        Vector v = new Vector();
        Elements ms = yearElement.getChildElements("month");
        for (int i = 0; i < ms.size(); i++)
            v.add(new Month(ms.get(i)));
        return v;
    }

    public Element getElement() {
        return yearElement;
    }
}

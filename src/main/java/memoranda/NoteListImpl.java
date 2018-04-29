/**
 * NoteListImpl.java
 * Created on 21.02.2003, 15:43:26 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda;
import java.util.Collection;
import java.util.Vector;

import main.java.memoranda.Day.NoteElement;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.interfaces.INote;
import main.java.memoranda.interfaces.INoteList;
import main.java.memoranda.interfaces.IProject;
import main.java.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
/**
 * 
 */
/*$Id: NoteListImpl.java,v 1.14 2004/10/28 11:30:15 alexeya Exp $*/
public class NoteListImpl implements INoteList {

    private IProject _project = null;
    private Document _doc = null;
    private Element _root = null;

//    public static final String NS_JNNL = "http://www.openmechanics.org/2003/jnotes-noteslist";

    /**
     * Constructor for NoteListImpl.
     */
    public NoteListImpl(Document doc, IProject prj) {
        _doc = doc;
        _root = _doc.getRootElement();
        _project = prj;
    }

    public NoteListImpl(IProject prj) {
    	
        //_root = new Element("noteslist", NS_JNNL);
        _root = new Element("noteslist");
        _doc = new Document(_root);
        _project = prj;    
    }

    public Collection getAllNotes() {
        Vector v = new Vector();
        Elements yrs = _root.getChildElements("year");
        for (int yi = 0; yi < yrs.size(); yi++) {
            Year y = new Year(yrs.get(yi));
            Vector ms = y.getMonths();
            for (int mi = 0; mi < ms.size(); mi++) {
                Month m = (Month) ms.get(mi);
                Vector ds = m.getDays();
                for (int di = 0; di < ds.size(); di++) {
                    Day d = (Day) ds.get(di);
					Vector ns = d.getNotes();
					for(int ni = 0; ni < ns.size(); ni++) {
						NoteElement n = (NoteElement) ns.get(ni);
						v.add(new NoteImpl(n.getElement(), _project));
					}
                }
            }
        }
        return v;
    }
    
    /**
     * @see main.java.memoranda.interfaces.INoteList#getMarkedNotes()
     */
    public Collection getMarkedNotes() {
        Vector v = new Vector();
        Elements yrs = _root.getChildElements("year");
        for (int yi = 0; yi < yrs.size(); yi++) {
            Year y = new Year(yrs.get(yi));
            Vector ms = y.getMonths();
            for (int mi = 0; mi < ms.size(); mi++) {
                Month m = (Month) ms.get(mi);
                Vector ds = m.getDays();
                for (int di = 0; di < ds.size(); di++) {
                    Day d = (Day) ds.get(di);
					Vector ns = d.getNotes();
					for(int ni = 0; ni < ns.size(); ni++) {
						NoteElement ne = (NoteElement) ns.get(ni);
						INote n = new NoteImpl(ne.getElement(), _project);
						if (n.isMarked()) v.add(n);
                }
            }
        }
    }
	        return v;
	}

    public Collection getNotesForPeriod(CalendarDate startDate, CalendarDate endDate) {
        Vector v = new Vector();
        Elements yrs = _root.getChildElements("year");
        for (int yi = 0; yi < yrs.size(); yi++) {
            Year y = new Year(yrs.get(yi));
            if ((y.getValue() >= startDate.getYear()) && (y.getValue() <= endDate.getYear())) {
                Vector months = y.getMonths();
                for (int mi = 0; mi < months.size(); mi++) {
                    Month m = (Month) months.get(mi);
                    if (!((y.getValue() == startDate.getYear()) && (m.getValue() < startDate.getMonth()))
                        || !((y.getValue() == endDate.getYear()) && (m.getValue() > endDate.getMonth()))) {
                        Vector days = m.getDays();
                        for (int di = 0; di < days.size(); di++) {
                            Day d = (Day) days.get(di);
                            if (!((m.getValue() == startDate.getMonth()) && (d.getValue() < startDate.getDay()))
							|| !((m.getValue() == endDate.getMonth()) && (d.getValue() > endDate.getDay()))) {
								Vector ns = d.getNotes();
								for(int ni = 0; ni < ns.size(); ni++) {
									NoteElement n = (NoteElement) ns.get(ni);
									v.add(new NoteImpl(n.getElement(), _project));
								}
							}
                        }
                    }
                }
            }
        }
        return v;
    }

	/**
	 * returns the first note for a date.
	 * @param CalendarDate
	 * @return Note
	 */
	 
    public INote getNoteForDate(CalendarDate date) {
        Day d = getDay(date);
        if (d == null)
            return null;
		Vector ns = d.getNotes();
		if(ns.size()>0) {
			NoteElement n = (NoteElement) ns.get(0);
			INote currentNote = new NoteImpl(n.getElement(), _project);
			return currentNote; 
		}
		return null;
        //return new NoteImpl(d.getElement(), _project);
    }

    public INote createNoteForDate(CalendarDate date) {
        Year y = getYear(date.getYear());
        if (y == null)
            y = createYear(date.getYear());
        Month m = y.getMonth(date.getMonth());
        if (m == null)
            m = y.createMonth(date.getMonth());
        Day d = m.getDay(date.getDay());
        if (d == null) 
            d = m.createDay(date.getDay());
		NoteElement ne = d.createNote(Util.generateId());
        return new NoteImpl(ne.getElement(), _project);
    }
    
     /*
     * @see net.sf.memoranda.NoteList#removeNoteForDate(net.sf.memoranda.date.CalendarDate)
     */
/*    public void removeNoteForDate(CalendarDate date) {
        Day d = getDay(date);
        if (d == null) return;
        d.getElement().getParent().removeChild(d.getElement());             
    }
*/
	 public void removeNote(CalendarDate date, String id) {
        Day d = getDay(date);
        if (d == null) return;
		Vector ns = d.getNotes();
		for(int i=0;i<ns.size();i++) {
			NoteElement n = (NoteElement) ns.get(i);
			Element ne = n.getElement();
			if(ne.getAttribute("refid").getValue().equals(id)) d.getElement().removeChild(n.getElement());
		}
//		CurrentNote.set(null);
    }
	
    public INote getActiveNote() {
        //return CurrentNote.get(); 
    	return getNoteForDate(CurrentDate.get());
    	// FIXED: Must return the first note for today [alexeya]
    }

    private Year getYear(int y) {
        Elements yrs = _root.getChildElements("year");
        String yy = new Integer(y).toString();
        for (int i = 0; i < yrs.size(); i++)
            if (yrs.get(i).getAttribute("year").getValue().equals(yy))
                return new Year(yrs.get(i));
        //return createYear(y);
        return null;
    }

    private Year createYear(int y) {
        Element el = new Element("year");
        el.addAttribute(new Attribute("year", new Integer(y).toString()));
        _root.appendChild(el);
        return new Year(el);
    }
/*
    private Vector getYears() {
        Vector v = new Vector();
        Elements yrs = _root.getChildElements("year");
        for (int i = 0; i < yrs.size(); i++)
            v.add(new Year(yrs.get(i)));
        return v;
    }
*/
    private Day getDay(CalendarDate date) {
        Year y = getYear(date.getYear());
        if (y == null)
            return null;
        Month m = y.getMonth(date.getMonth());
        if (m == null)
            return null;
        return m.getDay(date.getDay());
    }
    
    //TASK 2-2 SMELL BETWEEN CLASSES - removed sub class Year/Month/Day and made as their own classes
    //functionality of year/month/day used in other classes and thus duplicated. 
	
    /**
     * @see main.java.memoranda.interfaces.INoteList#getXMLContent()
     */
    public Document getXMLContent() {
        return _doc;
    }
}

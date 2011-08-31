/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author isabelle
 */
public class TagList {

    private HashMap<String, Tag> _list;

    public TagList() {
        this._list = new HashMap<String, Tag>();
    }

    public ArrayList<Tag> getList() {
        ArrayList<Tag> list = new ArrayList<Tag>();
        for (Entry<String, Tag> entry : _list.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public boolean addTag(Tag t) {
        if (t == null) {
            return false;
        }
        if (this.exists(t)) {
            return false;
        }
        this._list.put(t.getKey(), t);
        if (this.exists(t)) {
            return true;
        }
        return false;
    }

    public int getSize() {
        return this._list.size();
    }

    public boolean deleteTag(String key) {
        this._list.remove(key);
        return exists(key);
    }

    public boolean deleteTag(Tag t) {
        return this.deleteTag(t.getKey());
    }

    public boolean exists(Tag t) {
        return this.exists(t.getKey());
    }

    public boolean exists(String key) {
        return this._list.containsKey(key);
    }

    public Tag getTag(Tag t) {
        if(!exists(t)) return null;
        return this._list.get(t.getKey());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movie;

import movie.tagvalue.TagBoolean;
import movie.tagvalue.TagString;
import movie.tagvalue.TagValue;

/**
 *
 * @author isabelle
 */
public class Tag {
    private String _key;
    private TagValue _value;

    public Tag(String key, TagValue value) throws Exception {
        this._setKey(key);
        this._setValue(value);
    }

    public Tag(String key, boolean value) throws Exception {
        this._setKey(key);
        this._setValue(new TagBoolean(value));
    }

    public Tag(String key, String value) throws Exception {
        this._setKey(key);
        this._setValue(new TagString(value));
    }

    public boolean isDefined(Tag otherTag) {
        return this._key.equals(otherTag.getKey());
    }

    public String getKey() {
        return _key;
    }

    private void _setKey(String _key) throws Exception {
        if(_key == null ? "" == null : _key.equals("")) throw new Exception("Invalid key value.");
        this._key = _key;
    }

    private void _setValue(TagValue _value) {
        this._value = _value;
    }


    public TagValue getValue() {
        return _value;
    }

    public void setValue(TagValue _value) {
        this._setValue(_value);
    }

    public void setValue(boolean value) {
        this._setValue(new TagBoolean(value));
    }

    public void setValue(String value) {
        this._setValue(new TagString(value));
    }

    @Override
    public String toString() {
        return "Tag{" + "key: " + _key + " - value: " + _value + '}';
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movie.tagvalue;

/**
 *
 * @author gmanciet
 */
public class TagBoolean extends TagValue {
    private boolean _valueBool;

    public TagBoolean(boolean _valueBool) {
        this._valueBool = _valueBool;
    }

    public boolean getValue() {
        return _valueBool;
    }

    public void setValue(boolean _value) {
        this._valueBool = _value;
    }

    @Override
    public boolean isEqual(TagValue tag_value) {
        if(tag_value.getClass() != this.getClass()) return false;
        return (this.getValue() == ((TagBoolean)tag_value).getValue());
    }

    @Override
    public String toString() {
        return String.valueOf(_valueBool);
    }

    @Override
    public boolean isLowerThan(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isGreaterThan(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLike(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBetween(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNull(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isIn(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

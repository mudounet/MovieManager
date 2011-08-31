/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movie.tagvalue;

/**
 *
 * @author gmanciet
 */
public class TagString extends TagValue {
    private String _value;

    public TagString(String value) {
        this._value = value;
    }

    public void setValue(String _value) {
        this._value = _value;
    }

    @Override
    public boolean isEqual(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNotEqual(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLowerThan(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLowerEqualThan(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isGreaterThan(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isGreaterEqualThan(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLike(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNotLike(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBetween(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNotBetween(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNull(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNotNull(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isIn(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNotIn(TagValue tag_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "TagString{" + "_value=" + _value + '}';
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movie.tagvalue;

/**
 *
 * @author gmanciet
 */
public class TagInteger extends TagValue {
    private int _value;


    public void setValue(int _value) {
        this._value = _value;
    }

    public TagInteger(int _value) {
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

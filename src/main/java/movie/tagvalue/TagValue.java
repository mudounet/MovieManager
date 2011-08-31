/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movie.tagvalue;

/**
 *
 * @author isabelle
 */
public abstract class TagValue {

    @Override
    public abstract String toString();

    /**
     * Tests if it is considered as equal
     * @param tag_value Value to test
     * @return true when it is equal
     */
    public abstract boolean isEqual(TagValue tag_value);

    /**
     *
     * @param movie
     * @return
     */
    public boolean isNotEqual(TagValue tag_value) {
        return !isEqual(tag_value);
    }

    /**
     *
     * @param movie
     * @return
     */
    public abstract boolean isLowerThan(TagValue tag_value);

    /**
     *
     * @param movie
     * @return
     */
    public boolean isGreaterEqualThan(TagValue tag_value) {
        return !this.isLowerThan(tag_value);
    }

     /**
     * This function checks if this terminal filter is is greater than movie in field contained onto film
     * @param movie Movie to test
     * @return true when condition is met
     */
    public abstract boolean isGreaterThan(TagValue tag_value);

    /**
     *
     * @param movie
     * @return
     */
    public boolean isLowerEqualThan(TagValue tag_value) {
        return !this.isGreaterThan(tag_value);
    }

    /**
     *
     * @param movie
     * @return
     */
    public abstract boolean isLike(TagValue tag_value);

    /**
     *
     * @param movie
     * @return
     */
    public boolean isNotLike(TagValue tag_value) {
        return !isLike(tag_value);
    }

    /**
     *
     * @param movie
     * @return
     */
    public abstract boolean isBetween(TagValue tag_value);

    /**
     *
     * @param movie
     * @return
     */
    public boolean isNotBetween(TagValue tag_value) {
        return !isBetween(tag_value);
    }

    /**
     *
     * @param movie
     * @return
     */
    public abstract boolean isNull(TagValue tag_value);

    /**
     *
     * @param movie
     * @return
     */
    public boolean isNotNull(TagValue tag_value) {
        return !isNull(tag_value);
    }

    /**
     *
     * @param movie
     * @return
     */
    public abstract boolean isIn(TagValue tag_value);

    /**
     *
     * @param movie
     * @return
     */
    public boolean isNotIn(TagValue tag_value) {
        return !isIn(tag_value);
    }

}
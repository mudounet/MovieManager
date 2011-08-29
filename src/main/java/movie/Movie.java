/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movie;

/**
 *
 * @author isabelle
 */
public class Movie {
    private String _title;
    private Rating _rating;
    private TechData _techData;
    private Thumbnail _thumbnail;
    private Index _index;
    private TagList _tagList;

    public Movie(String _title) {
        this._title = _title;
        this._tagList = new TagList();
    }

    public Movie() {
        this("");
    }


    public Index getIndex() {
        return _index;
    }

    public void setIndex(Index _index) {
        this._index = _index;
    }

    public Rating getRating() {
        return _rating;
    }

    public void setRating(Rating _rating) {
        this._rating = _rating;
    }

    public TagList getTagList() {
        return _tagList;
    }

    public void setTagList(TagList _tagList) {
        this._tagList = _tagList;
    }

    public TechData getTechData() {
        return _techData;
    }

    public void setTechData(TechData _techData) {
        this._techData = _techData;
    }

    public Thumbnail getThumbnail() {
        return _thumbnail;
    }

    public void setThumbnail(Thumbnail _thumbnail) {
        this._thumbnail = _thumbnail;
    }

    public boolean addTag(Tag t) {
        return this._tagList.addTag(t);
    }

    @Override
    public String toString() {
        return _title;
    }
}

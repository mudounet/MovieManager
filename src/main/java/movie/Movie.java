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

    public Movie(String _title) {
        this._title = _title;
    }

    public Movie() {
        this("");
    }

    public Rating getRating() {
        return _rating;
    }

    public void setRating(Rating _rating) {
        this._rating = _rating;
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


    @Override
    public String toString() {
        return _title;
    }
}

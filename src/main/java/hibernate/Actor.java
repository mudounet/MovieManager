package hibernate;

import com.mudounet.hibernate.movie.Movie;
import java.util.HashSet;
import java.util.Set;

/**
 * A persistant Hibernate object.
 *
 * @author Guillaume MANCIET
 * @hibernate.class table="ACTOR"
 */
public class Actor  implements java.io.Serializable {


     private long id;
     /**
      * Name of actor
     */
     private String name;
     /**
      * Movies played by this actor
     */
     private Set<Movie> movies = new HashSet<Movie>(0);

    public Actor() {
    }


    public Actor(String name) {
        this.name = name;
    }
    public Actor(String name, Set<Movie> movies) {
       this.name = name;
       this.movies = movies;
    }

    /**
     * @hibernate.property column="ID"
     * @return
     */
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @hibernate.property column="NAME"
     * @return
     */
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @hibernate.collection-key column="MOVIE_ID"
     * @hibernate.collection-many-to-many class="hibernate.movie.Movie"
     * @return
     */
    public Set<Movie> getMovies() {
        return this.movies;
    }
    
    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    /**
     * toString
     * @return String
     */
     public String toString() {
	  StringBuffer buffer = new StringBuffer();

      buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
      buffer.append("name").append("='").append(getName()).append("' ");			
      buffer.append("]");
      
      return buffer.toString();
     }



}



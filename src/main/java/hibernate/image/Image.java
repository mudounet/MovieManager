package hibernate.image;
// Generated 3 janv. 2011 12:49:11 by Hibernate Tools 3.2.0.b9



/**
 * A persistant Hibernate object.
 *
 * @author Guillaume MANCIET
 * @hibernate.class table="IMAGE"
 */
public class Image  implements java.io.Serializable {


     private long id;
     private String path;
     private String md5sum;
     private int offset;

    public Image() {
    }

    public Image(String path, String md5sum, int offset) {
       this.path = path;
       this.md5sum = md5sum;
       this.offset = offset;
    }
   
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    public String getMd5sum() {
        return this.md5sum;
    }
    
    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }
    public int getOffset() {
        return this.offset;
    }
    
    public void setOffset(int offset) {
        this.offset = offset;
    }




}



package beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SessionMgt implements Serializable {
  // modèle

  private String password = "bonjour";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
  
      public String checkLogin()
	    {
	        if("test".equals(getPassword()))
	        {
	            return "pm:loginSuccess";
	        }
	        else
	        {
	            return "pm:login";
	        }
	    }
}
